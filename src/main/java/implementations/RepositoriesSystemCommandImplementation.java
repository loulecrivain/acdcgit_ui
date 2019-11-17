package implementations;

import interfaces.RepositoriesInterface;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositoriesSystemCommandImplementation implements RepositoriesInterface {
    @Override
    public Map<String, Boolean> push(String... pathToRepositories) throws Exception {
        RepositorySytemCommandImplementation repository = new RepositorySytemCommandImplementation();

        return Arrays.asList(pathToRepositories).stream()
                .map(repositoryPath -> {
                    Boolean success = false;

                    try {
                        success = repository.push(repositoryPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return new AbstractMap.SimpleEntry<String, Boolean>(repositoryPath, success);
                })
                .collect(Collectors.toMap(element -> element.getKey(),
                        element -> element.getValue()));
    }

    @Override
    public Map<String, Boolean> pull(String... pathToRepositories) throws Exception {
        RepositorySytemCommandImplementation repository = new RepositorySytemCommandImplementation();

        return Arrays.asList(pathToRepositories).stream()
                .map(repositoryPath -> {
                    Boolean success = false;

                    try {
                        success = repository.pull(repositoryPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return new AbstractMap.SimpleEntry<String, Boolean>(repositoryPath, success);
                })
                .collect(Collectors.toMap(element -> element.getKey(),
                        element -> element.getValue()));
    }

    @Override
    public Map<String, String> getState(String... pathToRepositories) throws Exception {
        RepositorySytemCommandImplementation repository = new RepositorySytemCommandImplementation();

        return Arrays.asList(pathToRepositories).stream()
                .map(repositoryPath -> {
                    String state = "UP_TO_DATE";

                    try {
                        state = repository.getState(repositoryPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return new AbstractMap.SimpleEntry<String, String>(repositoryPath, state);
                })
                .collect(Collectors.toMap(element -> element.getKey(),
                        element -> element.getValue()));
    }
}
