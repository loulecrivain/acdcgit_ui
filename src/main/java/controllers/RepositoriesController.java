package controllers;

import implementations.RepositoriesFinderSystemCommandImplementation;
import implementations.RepositoriesSystemCommandImplementation;
import implementations.RepositorySystemCommandImplementation;
import interfaces.RepositoriesFinderInterface;
import interfaces.RepositoriesInterface;
import interfaces.RepositoryInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepositoriesController implements RepositoriesControllerInterface {

    private final RepositoryInterface repositoryInterface = new RepositorySystemCommandImplementation();
    private final RepositoriesFinderInterface finder = new RepositoriesFinderSystemCommandImplementation();
    private final RepositoriesInterface repositoriesInterface = new RepositoriesSystemCommandImplementation();

    private List<String> repositories;

    public RepositoriesController() {
        this.repositories = new ArrayList<>();
    }

    @Override
    public void addReposFromFile(String filePath) {
        try {
            List<String> repositories = this.finder.findRepositoriesInFile(filePath);
            this.repositories.addAll(repositories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addReposFromDirectory(String directoryPath) {
        try {
            List<String> repositories = this.finder.findRepositoriesInDirectory(directoryPath);
            this.repositories.addAll(repositories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getRepos() {
        return this.repositories;
    }

    @Override
    public Map<String, Boolean> pushAll() throws Exception {
        return this.repositoriesInterface.push(this.repositories.toArray(String[]::new));
    }

    @Override
    public Map<String, Boolean> pullAll() throws Exception {
        return this.repositoriesInterface.pull(this.repositories.toArray(String[]::new));
    }

    @Override
    public Map<String, String> getAllStates() throws Exception {
        return this.repositoriesInterface.getStates(this.repositories.toArray(String[]::new));
    }

    @Override
    public Map<String, Boolean> pushMultiple(String... pathToRepositories) throws Exception {
        return this.repositoriesInterface.push(pathToRepositories);
    }

    @Override
    public Map<String, Boolean> pullMultiple(String... pathToRepositories) throws Exception {
        return this.repositoriesInterface.pull(pathToRepositories);
    }

    @Override
    public Map<String, String> getMultipleStates(String... pathToRepositories) throws Exception {
        return this.repositoriesInterface.getStates(pathToRepositories);
    }

    @Override
    public void add(String pathToRepository, String... pathsToFiles) throws Exception {
        this.repositoryInterface.add(pathToRepository, pathsToFiles);
    }

    @Override
    public void commit(String pathToRepository, String message) throws Exception {
        this.repositoryInterface.commit(pathToRepository, message);
    }

    @Override
    public boolean push(String pathToRepository) throws Exception {
        return this.repositoryInterface.push(pathToRepository);
    }

    @Override
    public boolean push(String pathToRepository, String username, String password) throws Exception {
        return this.repositoryInterface.push(pathToRepository, username, password);
    }

    @Override
    public boolean pull(String pathToRepository) throws Exception {
        return this.repositoryInterface.pull(pathToRepository);
    }

    @Override
    public boolean pull(String pathToRepository, String username, String password) throws Exception {
        return this.repositoryInterface.pull(pathToRepository, username, password);
    }

    @Override
    public List<String> getNotAddedFiles(String pathToRepository) throws Exception {
        return this.repositoryInterface.getNotAddedFiles(pathToRepository);
    }

    @Override
    public String getState(String pathToRepository) throws Exception {
        return this.repositoryInterface.getState(pathToRepository);
    }

    @Override
    public String diffs(String pathToRepository) throws Exception {
        return this.repositoryInterface.diffs(pathToRepository);
    }

    @Override
    public boolean saveReposToFile(String targetedPath) {
        try {
            File file = new File(targetedPath);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String repository : this.repositories) {
                bufferedWriter.write(repository + System.getProperty("line.separator"));
            }

            bufferedWriter.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }
}
