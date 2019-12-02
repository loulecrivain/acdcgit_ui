package pacpresentations;

import javax.swing.JMenuBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import paccontrollers.ReposPACController;

// DONE
public class AppMenuBar extends JMenuBar implements ActionListener {
	private JMenu fileMenu;
	private JMenuItem importFromFolderItem;
	private JMenuItem importFromFileItem;
	private JFileChooser importFromFolderChooser;
	private JFileChooser importFromFileChooser;
	private final String IMPFOLDER = "import from folder";
	private final String IMPFILE = "import from file";
	private ReposPACController reposPACController;
	// quit item is useless !

	public AppMenuBar(ReposPACController reposController) {
		this.reposPACController = reposController;
		fileMenu = new JMenu("file");
		/* menu items */
		importFromFolderItem = new JMenuItem(this.IMPFOLDER);
		importFromFolderItem.addActionListener(this);
		importFromFileItem = new JMenuItem(this.IMPFILE);
		importFromFileItem.addActionListener(this);
		/* init choosers */
		importFromFileChooser = new JFileChooser();
		importFromFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		importFromFolderChooser = new JFileChooser();
		importFromFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileMenu.add(importFromFolderItem);
		fileMenu.add(importFromFileItem);
		this.add(fileMenu);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int chooserReturn = JFileChooser.CANCEL_OPTION;

		switch(event.getActionCommand()) {
		case IMPFOLDER:
			chooserReturn = this.importFromFolderChooser.showOpenDialog(this);
			if(chooserReturnOK(chooserReturn)) {
				this.reposPACController.addRepositoriesFromDirectory(this.importFromFolderChooser.getSelectedFile().getAbsolutePath());
			}
			break;
		case IMPFILE:
			chooserReturn = this.importFromFileChooser.showOpenDialog(this);
			if(chooserReturnOK(chooserReturn)) {
				this.reposPACController.addRepositoriesFromFile(this.importFromFileChooser.getSelectedFile().getAbsolutePath());
			}
			break;
		}
	}

	protected boolean chooserReturnOK(int chooserReturn) {
		return chooserReturn == JFileChooser.APPROVE_OPTION;
	}
}