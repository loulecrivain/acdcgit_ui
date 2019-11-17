package interfaces;

import java.util.List;
import java.util.Map;

/**
 * That interface describe all the actions that can be performed on a single
 * repository.
 */
public interface RepositoryInterface {

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
     * Get the status of the repository, this represent all the files that
     * are not currently added to the repository with their status.
     * Status can be:
     * - ADDED
     * - COPIED
     * - DELETED
     * - MODIFIED
     * - RENAMED
     * - UNTRACKED
     *
     * @param pathToRepository the path to the repository without ".git".
     * @return a list containing all the files that can be added to
     * the repository with their status. The first entry is the file path,
     * the second is the status
     */
    Map<String, String> status(String pathToRepository) throws Exception;

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
}
