package interfaces;

import java.util.Map;

/**
 * That interface describe all the actions that can be done in order to
 * perform actions on multiple repositories.
 */
public interface RepositoriesInterface {

    /**
     * Push all the committed changes to the remote.
     * <p>
     * That can fail when user must be authenticated. (Maybe throwing a
     * custom exception like *NotAuthenticatedException* is more explicit
     * than a boolean)
     *
     * @param pathToRepositories the paths to all the desired repositories
     *                           without ".git".
     * @return a map describing the success of the command on each repository.
     */
    Map<String, Boolean> push(String... pathToRepositories) throws Exception;

    /**
     * Pull changes from the remote.
     * <p>
     * That can fail when user must be authenticated. (Maybe throwing a
     * custom exception like *NotAuthenticatedException* is more explicit
     * than a boolean)
     *
     * @param pathToRepositories the paths to the all the desired repositories
     *                           without ".git".
     * @return a map describing the success of the command on each repository.
     */
    Map<String, Boolean> pull(String... pathToRepositories) throws Exception;

    /**
     * Get the states of the given repositories. It can be:
     * - AHEAD
     * - BEHIND
     * - UP_TO_DATE
     *
     * @param pathToRepositories the paths to all the desired repositories
     *                           without ".git".
     * @return a map describe the current state of all given repositories.
     */
    Map<String, String> getState(String... pathToRepositories) throws Exception;
}
