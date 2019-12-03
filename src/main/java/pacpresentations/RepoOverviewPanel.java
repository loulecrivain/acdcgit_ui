package pacpresentations;

import javax.swing.JPanel;

import paccontrollers.RepoPACController;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepoOverviewPanel extends JPanel implements ActionListener {
	private final String DETAILS = "details";
	private JButton detailsButton;
	private JLabel statusLabel;
	private JLabel titleLabel;
	private RepoPACController repoPACController;
	private String path;
	
	public RepoOverviewPanel(String path, RepoPACController repoPACController) {
		this.repoPACController = repoPACController;
		this.path = path;
		String status = "unknown state";
		this.setLayout(new BorderLayout());
		detailsButton = new JButton(DETAILS);
		detailsButton.addActionListener(this);
		try {
			status = repoPACController.getState();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception " + e.getClass() + " raised !", "error", JOptionPane.ERROR_MESSAGE);
		}
		statusLabel = new JLabel(status);
		titleLabel = new JLabel(path);
		this.add(titleLabel, BorderLayout.NORTH);
		this.add(statusLabel, BorderLayout.WEST);
		this.add(detailsButton, BorderLayout.EAST);
	}

	// on button press
	@Override
	public void actionPerformed(ActionEvent event) {
		// details button pressed
		if(event.getActionCommand().contentEquals(DETAILS)) {
			// new frame with details about repository
			RepoPresentation presentationFrame = RepoPresentation.getInstance(path, repoPACController);
			presentationFrame.setVisible(true);
		}
	}
}
