package pacpresentations;

import javax.swing.JFrame;

import implementations.RepositoriesSystemCommandImplementation;
import implementations.RepositorySystemCommandImplementation;

import javax.swing.*;
import java.awt.BorderLayout;
import paccontrollers.*;
import controllers.*;


public class ReposPresentation extends JFrame {
	private AppMenuBar appMenuBar;
	private ReposScrollPane reposScrollPane;
	private ReposPACController reposController;
	
	public ReposPresentation(ReposPACController reposController) {
		super("ACDCGit");
		this.reposController = reposController;
		this.setLayout(new BorderLayout());
		this.appMenuBar = new AppMenuBar(reposController);
		this.reposScrollPane = new ReposScrollPane(reposController);
		this.reposController.addPropertyChangeListener(reposScrollPane);
		this.add(appMenuBar, BorderLayout.NORTH);
		this.add(reposScrollPane, BorderLayout.CENTER);
		this.pack();
	}
	
	public static void main(String[] args) {
		ReposPACController reposController = new ReposPACController(new RepositoriesController());
		RepoPACController repoController = new RepoPACController(reposController);
		ReposPresentation mf = new ReposPresentation(reposController);
		mf.setVisible(true);
		/* RepoPresentation rf = new RepoPresentation(repoController);
		rf.setVisible(true); */
	}
}
