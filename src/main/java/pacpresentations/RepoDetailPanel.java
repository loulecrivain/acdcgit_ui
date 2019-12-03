package pacpresentations;

import paccontrollers.RepoPACController;
import pojo.WaFileStatus;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class RepoDetailPanel extends JPanel implements PropertyChangeListener, ActionListener {
	private String path;
	private JLabel pathLabel;
	private JLabel stateLabel;
	private DefaultListModel<WaFileStatus> waFileListModel;
	private JList<WaFileStatus> waFileList;
	private JLabel diffLabel;
	private JLabel commitLabel;
	private JButton addFilesButton;
	private JTextField commitMessageTextField;
	private JButton commitAndPushButton;
	private JTextArea diffTextArea;
	private JButton updateButton;
	private RepoPACController repoController;
	private JScrollPane diffTextAreaScrollPane;
	
	private static final String BTN_UPDATE = "update";
	private static final String BTN_ADD = "add file(s)";
	private static final String BTN_COMMIT_AND_PUSH = "commit and push";
	
	public RepoDetailPanel(String path, RepoPACController repoController) {
		this.repoController = repoController;
		this.path = path;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pathLabel = new JLabel(path);
		stateLabel = new JLabel("unknown state");
		waFileListModel = new DefaultListModel<WaFileStatus>();
		waFileList = new JList<WaFileStatus>(waFileListModel);
		diffLabel = new JLabel("diff");
		commitLabel = new JLabel("commit message");
		diffTextArea = new JTextArea("no diff");
		diffTextArea.setEditable(false);
		diffTextAreaScrollPane = new JScrollPane(diffTextArea);
		diffTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		addFilesButton = new JButton(BTN_ADD);
		addFilesButton.addActionListener(this);
		updateButton = new JButton(BTN_UPDATE);
		updateButton.addActionListener(this);
		commitMessageTextField = new JTextField();
		commitAndPushButton = new JButton(BTN_COMMIT_AND_PUSH);
		commitAndPushButton.addActionListener(this);
		this.add(pathLabel);
		this.add(stateLabel);
		this.add(updateButton);
		this.add(new JSeparator(JSeparator.HORIZONTAL));
		this.add(waFileList);
		this.add(addFilesButton);
		this.add(diffLabel);
		this.add(diffTextAreaScrollPane);
		this.add(commitLabel);
		this.add(commitMessageTextField);
		this.add(commitAndPushButton);
	}
	
	protected void recomputePresentation() {
		try {
			this.repoController.getDiff();
			this.stateLabel.setText(this.repoController.getState());
			this.repoController.getWAFileList();
		} catch (Exception e) {
			GenError.show(e);
		}
	}
	public void setDiff(String diff) {
		this.diffTextArea.setText(diff);
		this.revalidate();
	}
	
	public void setState(String state) {
		this.stateLabel.setText(state);
		this.revalidate();
	}
	
	public void setWaFileList(List<WaFileStatus> waFileList) {
		this.waFileListModel.removeAllElements();
		this.waFileListModel.addAll(waFileList);
		this.revalidate();
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		switch(propertyChangeEvent.getPropertyName()) {
		case RepoPACController.PROPERTY_DIFF:
			this.setDiff((String)propertyChangeEvent.getNewValue());
			break;
		case RepoPACController.PROPERTY_STATE:
			this.setState((String)propertyChangeEvent.getNewValue());
			break;
		case RepoPACController.PROPERTY_WAFILELIST:
			this.setWaFileList((List<WaFileStatus>)propertyChangeEvent.getNewValue());
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()) {
		case BTN_ADD: // add files to WorkingArea
			List<WaFileStatus> selectedFiles = this.waFileList.getSelectedValuesList();
			List<String> selectedFilesStrList = new ArrayList<String>();
			for(WaFileStatus selectedFile: selectedFiles) {
				selectedFilesStrList.add(selectedFile.getPath());
			}
			try {
				if(!selectedFilesStrList.isEmpty()) {
					this.repoController.addFiles2WA(selectedFilesStrList.toArray(new String[] {}));
				}
			} catch (Exception e) {
				GenError.show(e);
			}
			break;
		case BTN_UPDATE:
			String state = "UNKNOWN";
			try {
				state = this.repoController.getState();
			} catch (Exception e) {
				GenError.show(e);
			}
			switch(state) {
			case "AHEAD":
				pushDialog();
				break;
			case "BEHIND":
				pullDialog();
				break;
			case "UP_TO_DATE":
				JOptionPane.showMessageDialog(this, "Nothing to do, already up to date", "info", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
			break;
		case BTN_COMMIT_AND_PUSH:
			String commitMsg = this.commitMessageTextField.getText();
			try {
				this.repoController.commit(commitMsg);
			} catch (Exception e) {
				GenError.show(e);
			}
			pushDialog();
			break;
		}
	}
	
	protected void pullDialog() {
		try {
			boolean ok = this.repoController.pull();
			if(!ok) {
				// can't pull without credentials, open dialogs
				String username = JOptionPane.showInputDialog("please provide username");
				String password = JOptionPane.showInputDialog("please provide password");
				boolean ok2 = this.repoController.pull(username, password);
				if(!ok2) {
					JOptionPane.showMessageDialog(this, "Can't pull ! Either you provided wrong credentials or a network error happened", "pull error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {
			GenError.show(e);
		}
	}

	protected void pushDialog() {
		try {
			boolean ok = this.repoController.push();
			if(!ok) {
				// can't push without credentials, open dialogs
				String username = JOptionPane.showInputDialog("please provide username");
				String password = JOptionPane.showInputDialog("please provide password");
				boolean ok2 = this.repoController.push(username, password);
				if(!ok2) {
					JOptionPane.showMessageDialog(this, "Can't push ! Either you provided wrong credentials or a network error happened", "push error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {
			GenError.show(e);
		}
	}
}
