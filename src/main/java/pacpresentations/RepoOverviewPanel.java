package pacpresentations;

import javax.swing.JPanel;

import paccontrollers.RepoPACController;

import javax.swing.*;
import java.awt.BorderLayout;

public class RepoOverviewPanel extends JPanel {

	private JButton detailsButton;
	private JLabel statusLabel;
	private JLabel titleLabel;

	public RepoOverviewPanel(String path, RepoPACController repoPACController) {
		String status = "unknown state";
		this.setLayout(new BorderLayout());
		detailsButton = new JButton("details");
		try {
			status = repoPACController.getState(path);
		} catch (Exception e) {
			e.printStackTrace(); // TODO add modal dialog, terminate
		}
		statusLabel = new JLabel(status);
		titleLabel = new JLabel(path);
		this.add(titleLabel, BorderLayout.NORTH);
		this.add(statusLabel, BorderLayout.WEST);
		this.add(detailsButton, BorderLayout.EAST);
	}
}
