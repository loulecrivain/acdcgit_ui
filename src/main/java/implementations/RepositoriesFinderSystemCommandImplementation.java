package implementations;

import interfaces.RepositoriesFinderInterface;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositoriesFinderSystemCommandImplementation implements RepositoriesFinderInterface {
    @Override
    public List<String> findRepositoriesInDirectory(String directoryPath) throws Exception {

        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException("The directory path must be defined.");
        }

        Path folderPath = Paths.get(directoryPath);

        if (!Files.isDirectory(folderPath)) {
            throw new IllegalArgumentException("Folder path should be a directory.");
        }


        Stream.Builder<String> projectBuilder = Stream.builder();

        Files.newDirectoryStream(Path.of(directoryPath), Files::isDirectory)
                .forEach(directory -> {
                    try {
                        this.findRepositoriesInDirectory(directory.toString()).forEach(projectBuilder::accept);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (directory.getFileName().toString().equals(".git")) {

                        projectBuilder.accept(directory.getParent().toString());
                    }

                });

        return projectBuilder.build().collect(Collectors.toList());
    }

    @Override
    public List<String> findRepositoriesInFile(String filePath) throws Exception {

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("The path must be defined.");
        }

        Path file = Paths.get(filePath);

        if (Files.isDirectory(file)) {
            throw new IllegalArgumentException("Folder path should be a file.");
        }

        List<String> lines = Files.readAllLines(file);
        List<String> projectDirectories = lines.stream()
                .map(Paths::get)
                .filter(directory -> Files.exists(Path.of(directory.toAbsolutePath().toString() + "/.git")))
                .map(Path::getParent)
                .map(Path::toString)
                .collect(Collectors.toList());

        return projectDirectories;
    }
}
