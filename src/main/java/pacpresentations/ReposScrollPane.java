package pacpresentations;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import paccontrollers.RepoPACController;
import paccontrollers.ReposPACController;

public class ReposScrollPane extends JScrollPane implements PropertyChangeListener {
	private JPanel panel;
	private ReposPACController controller;
	
	protected void recomputeList(String[] repoPathList) {
		// remove everything first
		this.panel.removeAll();
		// then add each repository
		for(String path: repoPathList) {
			RepoOverviewPanel overview = new RepoOverviewPanel(path, new RepoPACController(this.controller));
			panel.add(overview);
		}
		// don't forget to redraw !
		this.revalidate();
	}
	
	public ReposScrollPane(ReposPACController controller) {
		this.controller = controller;
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		/* for(int i=0; i<=10; i++) {
			panel.add(new RepoOverviewPanel());
		} */
		this.setViewportView(panel);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChange) {
		// property change from controller
		// first make sure the name is right
		if(propertyChange.getPropertyName().contentEquals(ReposPACController.PROPERTY_REPOSITORIES)) {
			this.recomputeList((String[]) propertyChange.getNewValue());
		}
	}
}
