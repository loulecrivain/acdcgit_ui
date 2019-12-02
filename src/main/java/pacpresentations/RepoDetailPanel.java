package pacpresentations;

import javax.swing.JPanel;

import paccontrollers.RepoPACController;

import javax.swing.*;

public class RepoDetailPanel extends JPanel {
	private JLabel pathLabel;
	private JLabel statusLabel;
	private String[] lists;
	private JList<String> waFileList;
	private JLabel diffLabel;
	private JLabel commitLabel;
	private JButton addFilesButton;
	private JTextField textField;
	private JButton commitAndPushBtn;
	private JTextArea diffTextArea;

	public RepoDetailPanel(RepoPACController repoController) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pathLabel = new JLabel("/home/user/repo");
		statusLabel = new JLabel("status");
		lists = new String[] { "filea", "fileb", "filec" };
		waFileList = new JList<String>(lists);
		diffLabel = new JLabel("diff");
		commitLabel = new JLabel("commit message");
		diffTextArea = new JTextArea("+blablabla");
		addFilesButton = new JButton("add file(s)");
		textField = new JTextField();
		commitAndPushBtn = new JButton("commit and push");
		this.add(pathLabel);
		this.add(statusLabel);
		this.add(waFileList);
		this.add(addFilesButton);
		this.add(diffLabel);
		this.add(diffTextArea);
		this.add(commitLabel);
		this.add(textField);
		this.add(commitAndPushBtn);
	}
}
