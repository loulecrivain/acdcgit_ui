package implementations;

import interfaces.RepositoriesFinderInterface;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoriesFinderSystemCommandImplementationTest {

    private RepositoriesFinderInterface repositoriesFinderInterface = new RepositoriesFinderSystemCommandImplementation();

    @TempDir
    public File folder;

    private void setupFlatDirectoryStructureWithoutProjects() {
        new File(folder, "subfolder").mkdir();
    }

    /**
     * Setup the directory structure. The use of JGit here is justified by
     * the file access problem and setup with system commands. In the future,
     * that need to be removed.
     *
     * @throws GitAPIException
     */
    private void setupFlatDirectoryStructureWithAProjectInTheRoot() throws GitAPIException {
        this.setupFlatDirectoryStructureWithoutProjects();
        Git.init().setDirectory(this.folder).call().getRepository();
    }

    /**
     * Setup the directory structure. The use of JGit here is justified by
     * the file access problem and setup with system commands. In the future,
     * that need to be removed.
     *
     * @throws IOException
     * @throws GitAPIException
     */
    private void setupNestedDirectoryStructureWithProjectsInADirectory() throws IOException, GitAPIException {
        File project = new File(this.folder, "project");
        project.mkdir();

        File readme = new File(project, "README.md");
        readme.createNewFile();
        File changelog = new File(project, "CHANGELOG.md");
        changelog.createNewFile();


        Repository projectRepository = Git.init().setDirectory(project).call().getRepository();
        Git projectGit = new Git(projectRepository);
        projectGit.add().addFilepattern(readme.getPath()).call();
        projectGit.commit().setMessage("init project").call();

        Git.init().setDirectory(project).call().getRepository();

        File myProjectDirectory = new File(this.folder, "my-project");
        myProjectDirectory.mkdir();

        Git.init().setDirectory(myProjectDirectory).call().getRepository();

        File projectBehind = new File(this.folder, "project-behind");
        projectBehind.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectBehind).call();

        projectGit.add().addFilepattern(changelog.getPath()).call();
        projectGit.commit().setMessage("add changelog").call();

        File projectAhead = new File(this.folder, "project-ahead");
        projectAhead.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectAhead).call();
        File file1 = new File(project, "file1");
        file1.createNewFile();
        Git projectAheadGit = Git.open(projectAhead);
        projectAheadGit.add().addFilepattern(file1.getPath()).call();
        projectAheadGit.commit().setMessage("file1 was added").call();

        File projectClean = new File(this.folder, "project-clean");
        projectClean.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectClean).call();
    }

    /**
     * Setup the directory structure. The use of JGit here is justified by
     * the file access problem and setup with system commands. In the future,
     * that need to be removed.
     *
     * @throws IOException
     * @throws GitAPIException
     */
    private void setupNestedDirectoryStructureWithProjectsInADirectoryAndSymlink() throws IOException, GitAPIException {
        File project = new File(this.folder, "project");
        project.mkdir();

        File readme = new File(project, "README.md");
        readme.createNewFile();
        File changelog = new File(project, "CHANGELOG.md");
        changelog.createNewFile();


        Repository projectRepository = Git.init().setDirectory(project).call().getRepository();
        Git projectGit = new Git(projectRepository);
        projectGit.add().addFilepattern(readme.getPath()).call();
        projectGit.commit().setMessage("init project").call();

        File myProjectDirectory = new File(this.folder, "my-project");
        myProjectDirectory.mkdir();

        File readmeMyProject = new File(project, "README.md");
        readmeMyProject.createNewFile();

        Repository myProjectRepository = Git.init().setDirectory(myProjectDirectory).call().getRepository();
        Git myProjectGit = new Git(myProjectRepository);
        myProjectGit.add().addFilepattern(readmeMyProject.getPath()).call();
        myProjectGit.commit().setMessage("init project").call();

        File link = new File(myProjectDirectory, "link");
        Files.createSymbolicLink(link.toPath(), project.toPath());

        File projectBehind = new File(this.folder, "project-behind");
        projectBehind.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectBehind).call();

        projectGit.add().addFilepattern(changelog.getPath()).call();
        projectGit.commit().setMessage("add changelog").call();

        File projectAhead = new File(this.folder, "project-ahead");
        projectAhead.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectAhead).call();
        File file1 = new File(project, "file1");
        file1.createNewFile();
        Git projectAheadGit = Git.open(projectAhead);
        projectAheadGit.add().addFilepattern(file1.getPath()).call();
        projectAheadGit.commit().setMessage("file1 was added").call();

        File projectClean = new File(this.folder, "project-clean");
        projectClean.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectClean).call();
    }


    @Test
    public void whenFolderPathIsNull_thenFindRepositoriesInDirectoryShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.repositoriesFinderInterface.findRepositoriesInDirectory(null);
        });
    }

    @Test
    public void whenFolderPathIsEmpty_thenFindRepositoriesInDirectoryShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.repositoriesFinderInterface.findRepositoriesInDirectory("");
        });
    }

    @Test
    public void whenFolderPathIsNotADirectory_thenFindRepositoriesInDirectoryShouldThrowIllegalArgumentException() {
        File file = new File(this.folder, "file");

        assertThrows(IllegalArgumentException.class, () -> {
            this.repositoriesFinderInterface.findRepositoriesInDirectory(file.getPath());
        });
    }

    @Test
    public void whenFolderPathDoesNotContainsProject_thenFindRepositoriesInDirectoryShouldReturnAnEmptyList() throws Exception {
        this.setupFlatDirectoryStructureWithoutProjects();

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInDirectory(this.folder.getPath());

        assertEquals(0, files.size());
    }

    @Test
    public void whenFolderPathContainsOneProject_thenFindRepositoriesInDirectoryShouldReturnAListWithOneElement() throws Exception {
        this.setupFlatDirectoryStructureWithAProjectInTheRoot();

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInDirectory(this.folder.getPath());
        assertEquals(1, files.size());
    }

    @Test
    public void whenFolderPathContainsMultipleProjects_thenFindRepositoriesInDirectoryShouldReturnAListWithMultipleEntries() throws Exception {
        this.setupNestedDirectoryStructureWithProjectsInADirectory();

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInDirectory(this.folder.getPath());
        assertEquals(5, files.size());
    }

    @Test
    public void whenFolderPathContainsMultipleProjectsWithSymlink_thenFindRepositoriesInDirectoryShouldReturnAListWithMultipleEntries() throws Exception {
        this.setupNestedDirectoryStructureWithProjectsInADirectoryAndSymlink();

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInDirectory(this.folder.getPath());
        assertEquals(6, files.size());
    }

    @Test
    public void whenPathIsNull_thenFindRepositoriesInFileShouldThrowsAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.repositoriesFinderInterface.findRepositoriesInFile(null));
    }

    @Test
    public void whenPathIsEmpty_thenFindRepositoriesInFileShouldThrowsAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.repositoriesFinderInterface.findRepositoriesInFile(""));
    }

    @Test
    public void whenPathIsNotAFile_thenFindRepositoriesInFileShouldThrowsAnException() {
        File project = new File(this.folder, "project");
        project.mkdir();

        assertThrows(IllegalArgumentException.class,
                () -> this.repositoriesFinderInterface.findRepositoriesInFile(project.getPath()));
    }

    @Test
    public void whenTheTargetedFileDoesNotContainAnyProject_thenFindRepositoriesInFileShouldReturnAnEmptyList() throws Exception {
        File myFile = new File(this.folder, "myFile");
        FileWriter fileWriter = new FileWriter(myFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("");

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInFile(myFile.getPath());

        assertEquals(0, files.size());
    }

    @Test
    public void whenTheTargetedFileContainOneProject_thenFindRepositoriessInFileShouldReturnAListWithOneElement() throws Exception {
        this.setupFlatDirectoryStructureWithAProjectInTheRoot();
        File myFile = new File(this.folder, "myFile");
        FileWriter fileWriter = new FileWriter(myFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(this.folder.getPath());
        printWriter.close();

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInFile(myFile.getPath());

        assertEquals(1, files.size());
    }

    @Test
    public void whenTheTargetedFileContainMultipleProjects_thenFindRepositoriesInFileShouldReturnAListWithMultipleElements() throws Exception {
        this.setupNestedDirectoryStructureWithProjectsInADirectory();
        File myFile = new File(this.folder, "myFile");
        FileWriter fileWriter = new FileWriter(myFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(this.folder.getPath() + "/project-clean");
        printWriter.println(this.folder.getPath() + "/my-project");
        printWriter.close();

        List<String> files =
                this.repositoriesFinderInterface.findRepositoriesInFile(myFile.getPath());

        assertEquals(2, files.size());
    }
}