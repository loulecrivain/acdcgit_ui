package paccontrollers;

import controllers.*;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ReposPACController {
	private RepositoriesControllerInterface model;
	private String[] repositories;
	public static final String PROPERTY_REPOSITORIES = "repositories";
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void setRepositories(String[] newRepositoriesValue) {
		String[] oldRepositoriesValue = this.repositories;
		this.repositories = newRepositoriesValue;
		this.pcs.firePropertyChange(PROPERTY_REPOSITORIES, oldRepositoriesValue, newRepositoriesValue);
	}

	public ReposPACController(RepositoriesControllerInterface model) {
		this.model = model;
		this.repositories = new String[] {};
	}

	public void addRepositoriesFromFile(String filePath) {
		this.model.addReposFromFile(filePath);
		// change the internal repositories property
		// then objects linked to this property should
		// update accordingly, idk how to do in swing ?
		
		// according to my searches: you have to use java.beans
		
		this.setRepositories(this.model.getRepos().toArray(new String[] {}));
	}

	public void addRepositoriesFromDirectory(String dirPath) {
		this.model.addReposFromDirectory(dirPath);
		// then update repoScrollPane
		this.setRepositories(this.model.getRepos().toArray(new String[] {}));
	}
}
