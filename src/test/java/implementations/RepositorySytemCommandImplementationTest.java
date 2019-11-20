package implementations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositorySytemCommandImplementationTest {

    @TempDir
    File folder;

    private RepositorySytemCommandImplementation repository;

    @BeforeEach
    public void setup() throws IOException, GitAPIException {
        this.setupNestedDirectoryStructureWithProjectsInADirectory();

        this.repository = new RepositorySytemCommandImplementation();
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
        RevCommit initialCommit = projectGit.commit().setMessage("init " +
                "project").call();
        projectGit.add().addFilepattern(changelog.getPath()).call();
        projectGit.commit().setMessage("add changelog").call();

        File projectBehind = new File(this.folder, "project-behind");
        projectBehind.mkdir();
        Git.cloneRepository().setURI(projectRepository.getDirectory().getAbsolutePath()).setDirectory(projectBehind).call();
        Git projectBehindGit = Git.open(projectBehind);
        projectBehindGit.reset().setMode(ResetCommand.ResetType.HARD).setRef(initialCommit.getName()).call();

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

    @Disabled
    @Test
    public void whenRepositoryIsBehind_thenGetStateShouldReturnBEHIND() throws Exception {

        String state =
                this.repository.getState(this.folder.getAbsolutePath() +
                        "/project-behind");

        assertEquals("BEHIND", state);
    }

    @Disabled
    @Test
    public void whenRepositoryIsAhead_thenGetStateShouldReturnAHEAD() throws Exception {

        String state =
                this.repository.getState(this.folder.getAbsolutePath() +
                        "/project-ahead");

        assertEquals("AHEAD", state);
    }

    @Disabled
    @Test
    public void whenRepositoryIsUpToDate_thenGetStateShouldReturnUPTODATE() throws Exception {

        String state =
                this.repository.getState(this.folder.getAbsolutePath() +
                        "/project-clean");

        assertEquals("UP_TO_DATE", state);
    }
}