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

	protected void setRepositories(String[] newRepositoriesValue) {
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
		// then objects listening to this property should
		// update accordingly
		
		this.setRepositories(this.model.getRepos().toArray(new String[] {}));
	}

	// used by childs to communicate changes made to some repositories
	public void notifyRepositoriesChanged() {
		System.out.println("refreshing");
		this.setRepositories(repositories); // trigger interface refresh
	}
	
	public void addRepositoriesFromDirectory(String dirPath) {
		this.model.addReposFromDirectory(dirPath);
		// then update repoScrollPane
		this.setRepositories(this.model.getRepos().toArray(new String[] {}));
	}
}
