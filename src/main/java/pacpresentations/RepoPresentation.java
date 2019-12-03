package pacpresentations;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import pacpresentations.RepoDetailPanel;
import paccontrollers.*;

/* singleton */
public class RepoPresentation extends JFrame {
	private RepoDetailPanel repoDetailPanel;
	private static Map<String,RepoPresentation> selfs;
	
	public static RepoPresentation getInstance(String path, RepoPACController control) {
		if(selfs==null) { // init hash map if needed
			selfs = new HashMap<String,RepoPresentation>();
		}
		if(selfs.get(path) == null) { // if there is no instance for this path, create one
			selfs.put(path, new RepoPresentation(path,control));
		}
		return selfs.get(path); // return the existing instance
	}
	
	private RepoPresentation(String path, RepoPACController control) {
		super("Repository: " + path);
		this.setLayout(new BorderLayout());
		repoDetailPanel = new RepoDetailPanel(path, control);
		// the detail panel is interested in events from
		// the controller
		control.addPropertyChangeListener(repoDetailPanel);
		// trigger 1st refresh (avoid specific components init in RepoDetail constructor)
		repoDetailPanel.recomputePresentation();
		this.add(repoDetailPanel, BorderLayout.CENTER);

		this.pack();
	}
}
