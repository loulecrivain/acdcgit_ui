package interfaces;

import java.util.List;

/**
 * That interface describe all the actions that can be performed to find
 * repositories.
 */
public interface RepositoriesFinderInterface {

    /**
     * An utility that parse a directory and all it's child in order to find
     * all the repositories in.
     *
     * @param directoryPath the path to the root directory.
     * @return a list of all repositories paths without ".git".
     */
    List<String> findRepositoriesInDirectory(String directoryPath) throws Exception;

    /**
     * An utility that parse a file and return all the directories that are
     * contained in.
     *
     * @param filePath the path to the file.
     * @return a list of all repositories paths without ".git".
     */
    List<String> findRepositoriesInFile(String filePath) throws Exception;
}
