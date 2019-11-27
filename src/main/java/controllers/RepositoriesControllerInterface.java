package controllers;

import java.util.List;
import java.util.Map;

/**
 * Common controller to manage multiple repositories.
 * Repositories can be added from a file,
 * where the absolute paths for the repositories are
 * stored line-by-line, or from a search path.
 * <p>
 * Client class may then use the getRepos() method in
 * order to perform the desired operations on the repositories
 * which were found. Refer to RepoInterface for more information.
 * <p>
 * Exceptions are thrown in order to let the class using this
 * interface define its own error handling.
 */
public interface RepositoriesControllerInterface {

    /**
     * Read file content and add found repos
     *
     * @param filePath the path to the file containing
     *                 the list of repositories. Paths are absolute
     *                 and may contain .git dir or not.
     */
    void addReposFromFile(String filePath);

    /**
     * Recursively search from the given directoryPath
     * entry point into the filesystem and add found repos
     *
     * @param directoryPath the path to the directory.
     */
    void addReposFromDirectory(String directoryPath);

    /**
     * Get all the stored repos. Client may then iterate
     * the list and call the appropriate methods. Or it may
     * use batch methods
     *
     * @return the list of repos.
     */
    List<String> getRepos();

    /**
     * Try to push all repositories known to the controller
     * <p>
     * without authentication
     *
     * @return true for success, false else
     * @throws Exception
     */
    Map<String, Boolean> pushAll() throws Exception;

    /**
     * Try to pull all repositories known to the controller
     * <p>
     * without authentication
     *
     * @return true for success, false else
     * @throws Exception
     */
    Map<String, Boolean> pullAll() throws Exception;

    /**
     * Get the states for all the repositories known to the controller.
     * <p>
     * Keys are the path to the repository. Values are their states.
     *
     * @return List of states (please refer to getState documentation).
     * @throws Exception
     */
    Map<String, String> getAllStates() throws Exception;

    /**
     * Try to push all repositories known to the controller
     * <p>
     * without authentication
     *
     * @return true for success, false else
     * @throws Exception
     */
    Map<String, Boolean> pushMultiple(String... pathToRepositories) throws Exception;

    /**
     * Try to pull all repositories known to the controller
     * <p>
     * without authentication
     *
     * @return true for success, false else
     * @throws Exception
     */
    Map<String, Boolean> pullMultiple(String... pathToRepositories) throws Exception;

    /**
     * Get the states for all the repositories known to the controller.
     * <p>
     * Keys are the path to the repository. Values are their states.
     *
     * @return List of states (please refer to getState documentation).
     * @throws Exception
     */
    Map<String, String> getMultipleStates(String... pathToRepositories) throws Exception;

    /**
     * Add the desired files to the repository.
     * <p>
     * Ignored files should not be added!
     *
     * @param pathToRepository the path to the repository without ".git".
     * @param pathsToFiles     the list of all the files that should be added.
     */
    void add(String pathToRepository, String... pathsToFiles) throws Exception;

    /**
     * Commit all added files. User should specify a message in order to
     * allow that.
     *
     * @param pathToRepository the path to the repository without ".git".
     * @param message          the message used in the commit.
     */
    void commit(String pathToRepository, String message) throws Exception;

    /**
     * Push all the committed changes to the remote.
     * <p>
     * That can fail when user must be authenticated. (Maybe throwing a
     * custom exception like *NotAuthenticatedException* is more explicit
     * than a boolean)
     *
     * @param pathToRepository the path to the repository without ".git".
     * @return the success of the command.
     */
    boolean push(String pathToRepository) throws Exception;

    /**
     * Push all the committed changes to the remote using authentication with
     * user and password.
     *
     * @param pathToRepository the path to the repository without ".git".
     * @param username         the username that will be used in authentication.
     * @param password         the password that will be used in authentication;
     * @return the success of the command.
     */
    boolean push(String pathToRepository, String username, String password) throws Exception;

    /**
     * Pull changes from the remote.
     * <p>
     * That can fail when user must be authenticated. (Maybe throwing a
     * custom exception like *NotAuthenticatedException* is more explicit
     * than a boolean)
     *
     * @param pathToRepository the path to the repository without ".git".
     * @return the success of the command.
     */
    boolean pull(String pathToRepository) throws Exception;

    /**
     * Pull changes from the remote using authentication with user and password.
     *
     * @param pathToRepository the path to the repository without ".git".
     * @param username         the username that will be used in authentication.
     * @param password         the password that will be used in authentication;
     * @return the success of the command
     */
    boolean pull(String pathToRepository, String username, String password) throws Exception;

    /**
     * Get the files that can be added to the repository.
     *
     * @param pathToRepository the path to the repository without ".git".
     * @return a list containing all the files that can be added to
     * the repository.
     */
    List<String> getNotAddedFiles(String pathToRepository) throws Exception;

    /**
     * Get the state of the repository. It can be:
     * - AHEAD
     * - BEHIND
     * - UP_TO_DATE
     *
     * @param pathToRepository the path to the repository without ".git".
     * @return the current state of the repository.
     */
    String getState(String pathToRepository) throws Exception;

    /**
     * Get the diffs from the remote.
     *
     * @param pathToRepository the path to the repository without ".git".
     * @return the diffs from the current branch as string.
     */
    String diffs(String pathToRepository) throws Exception;

    /**
     * Save repos to target file. Note that the file
     * is *overwritten*.
     *
     * @return true for success, false for failure to create/write to file
     */
    boolean saveReposToFile(String targetedPath);


}
