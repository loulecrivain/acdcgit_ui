package pacpresentations;

import javax.swing.*;
import java.awt.BorderLayout;
import pacpresentations.RepoDetailPanel;
import paccontrollers.*;

public class RepoPresentation extends JFrame {
	public RepoPresentation(RepoPACController control) {
		super("repo name");
		this.setLayout(new BorderLayout());
		this.add(new RepoDetailPanel(control), BorderLayout.CENTER);

		this.pack();
	}
}
