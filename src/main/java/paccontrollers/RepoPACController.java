package paccontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import implementations.RepositorySystemCommandImplementation;
import interfaces.RepositoryInterface;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import pojo.WaFileStatus;

public class RepoPACController {
	private RepositoryInterface boRepository;
	private String pathToRepo;
	/*  for dialogue/change notification, following PAC principles
	 * 	the reposController is the "parent" controller here
	 */
	private ReposPACController reposController;
	
	/* properties constants and associated properties */
	public static final String PROPERTY_DIFF="diff";
	private String diff;
	public static final String PROPERTY_WAFILELIST="wafilelist";
	private List<WaFileStatus> wafilelist;
	public static final String PROPERTY_STATE="state";
	private String state;
	
	/* property support (change property, add listener...) */
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	/* associated property change methods */
	protected void setDiff(String newDiffValue) {
		String oldDiffValue = this.diff;
		this.diff = newDiffValue;
		this.pcs.firePropertyChange(PROPERTY_DIFF, oldDiffValue, newDiffValue);
	}
	
	protected void setWaFileList(List<WaFileStatus> newWaFileListValue) {
		List<WaFileStatus> oldWaFileListValue = this.wafilelist;
		this.wafilelist = newWaFileListValue;
		this.pcs.firePropertyChange(PROPERTY_WAFILELIST, oldWaFileListValue, newWaFileListValue);
	}
	
	protected void setState(String newStateValue) {
		String oldStateValue = this.state;
		this.state = newStateValue;
		this.pcs.firePropertyChange(PROPERTY_STATE, oldStateValue, newStateValue);
	}
	
	
	/* constructor needs path because the PAC controller is stateful */
	public RepoPACController(String path, ReposPACController reposController) {
		this.pathToRepo = path;
		this.reposController = reposController;
		this.boRepository = new RepositorySystemCommandImplementation();
	}
	
	/* implement all the getters for repo */
	public String getState() throws Exception {
		String state = this.boRepository.getState(pathToRepo);
		this.setState(state); // notify state has changed
		return state;
	}
	
	public String getDiff() throws Exception {
		String diff = this.boRepository.diffs(pathToRepo);
		this.setDiff(diff); // notify diff has changed
		return diff;
	}
	
	public List<WaFileStatus> getWAFileList() throws Exception {
		List<WaFileStatus> list = new ArrayList<WaFileStatus>();
		Map<String,String> status = this.boRepository.status(pathToRepo);
		for(String waFilePath : status.keySet()) {
			list.add(new WaFileStatus(status.get(waFilePath),waFilePath));
		}
		this.setWaFileList(list); // notify change
		return list;
	}
	
	/* modifiers */
	public void addFiles2WA(String[] pathsToFiles) throws Exception {
		this.boRepository.add(pathToRepo, pathsToFiles);
		this.getWAFileList(); // trigger WAFileList change
		this.getDiff(); // trigger diff change, let subscribed components update
	}
	
	public void commit(String message) throws Exception {
		this.boRepository.commit(this.pathToRepo, message);
		this.getWAFileList();
		this.getDiff();
		this.getState();
		this.reposController.notifyRepositoriesChanged();
	}
	
	public boolean push() throws Exception {
		boolean ok = this.boRepository.push(this.pathToRepo);
		this.getState(); // trigger state change
		this.reposController.notifyRepositoriesChanged();
		return ok;
	}
	
	public boolean push(String username, String password) throws Exception {
		boolean ok = this.boRepository.push(this.pathToRepo, username, password);
		this.getState();
		this.reposController.notifyRepositoriesChanged();
		return ok;
	}
	
	public boolean pull() throws Exception {
		boolean ok = this.boRepository.pull(this.pathToRepo);
		this.getState();
		this.reposController.notifyRepositoriesChanged();
		return ok;
	}
	
	public boolean pull(String username, String password) throws Exception {
		boolean ok = this.boRepository.pull(this.pathToRepo, username, password);
		this.getState();
		this.reposController.notifyRepositoriesChanged();
		return ok;
	}
}
