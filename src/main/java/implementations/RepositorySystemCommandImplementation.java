package implementations;

import interfaces.RepositoryInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositorySystemCommandImplementation implements RepositoryInterface {

    @Override
    public void add(String pathToRepository, String... pathsToFiles) throws IOException {
        String files = Arrays.stream(pathsToFiles)
                .reduce((pathFile1, pathFile2) -> String.join(" ", pathFile1, pathFile2))
                .get();

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " add " + files).split("\\s+"));

        Process process = builder.start();

        BufferedReader errorOutput =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String error = errorOutput.lines().collect(Collectors.joining("\n"));

        if (!error.isEmpty()) {
            throw new IOException(error);
        }
    }

    @Override
    public void commit(String pathToRepository, String message) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " commit -m \"" + message + "\"").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    @Override
    public boolean push(String pathToRepository) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " push").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return !standardError.lines().findFirst().isPresent();
    }

    @Override
    public boolean push(String pathToRepository, String username, String password) throws Exception {
        String url = this.getRemotePushUrl(pathToRepository);

        if (url.startsWith("https://")) {
            url = url.replace("https://", "https://" + username + ":" + password + "@");
        }

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " push " + url).split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return !standardError.lines().findFirst().isPresent();
    }

    @Override
    public boolean pull(String pathToRepository) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " pull").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return !standardError.lines().findFirst().isPresent();
    }

    @Override
    public boolean pull(String pathToRepository, String username, String password) throws Exception {
        String url = this.getRemotePullUrl(pathToRepository);

        if (url.startsWith("https://")) {
            url = url.replace("https://", "https://" + username + ":" + password + "@");
        }

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " pull " + url).split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return !standardError.lines().findFirst().isPresent();
    }

    @Override
    public Map<String, String> status(String pathToRepository) throws Exception {
        Map<String, String> filesWithStatus = new HashMap<>();

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " status --porcelain").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String error = standardError.lines().collect(Collectors.joining("\n"));

        if (!error.isEmpty()) {
            throw new IOException(error);
        }
        
        filesWithStatus = standardInput.lines()
                .filter(this::canLineBeAdded)
                .map(line -> line.split(" "))
                .collect(Collectors.toMap(parsedLine -> parsedLine[1],
                        parsedLine -> parsedLine[0]));

        return filesWithStatus;
    }

    @Override
    public List<String> getNotAddedFiles(String pathToRepository) throws Exception {
        Map<String, String> filesWithStatus = this.status(pathToRepository);

        return filesWithStatus.values().stream().collect(Collectors.toList());
    }

    @Override
    public String getState(String pathToRepository) throws Exception {
        String currentState = RepositoryState.UP_TO_DATE.name();


        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " status --porcelain -b").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardOutput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String firstLine = standardOutput.lines().findFirst().get();

        int openingBracket = firstLine.indexOf('[');
        int closingBracket = firstLine.indexOf(']');

        if (openingBracket != -1) {
            boolean isBehind = firstLine.substring(openingBracket, closingBracket).contains("behind");
            boolean isAhead = firstLine.substring(openingBracket, closingBracket).contains("ahead");

            if (isBehind) {
                currentState = RepositoryState.BEHIND.name();
            }

            if (isAhead) {
                currentState = RepositoryState.AHEAD.name();
            }
        }

        return currentState;
    }

    @Override
    public String diffs(String pathToRepository) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " diff").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return standardInput.lines().collect(Collectors.joining("\n"));
    }

    public String getRemotePullUrl(String pathToRepository) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " remote get-url origin").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return standardInput.lines().findFirst().get();
    }

    public String getRemotePushUrl(String pathToRepository) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(("git -C " + pathToRepository + " remote get-url origin --push").split("\\s+"));

        Process process = builder.start();

        BufferedReader standardInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader standardError =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return standardInput.lines().findFirst().get();
    }

    public boolean canLineBeAdded(String line) {
        String status = line.substring(0, 2);
        boolean isNotTracked = status.contains("?");
        boolean isModified = status.contains("M");
        boolean isAdded = status.contains("A");
        boolean isDeleted = status.contains("D");
        boolean isRenamed = status.contains("R");
        boolean isCopied = status.contains("C");

        return isNotTracked || isModified || isAdded || isDeleted || isRenamed || isCopied;
    }

    public enum RepositoryState {
        BEHIND,
        AHEAD,
        UP_TO_DATE
    }
}
