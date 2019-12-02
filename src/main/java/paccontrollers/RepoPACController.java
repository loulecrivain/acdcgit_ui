package paccontrollers;

import implementations.RepositorySystemCommandImplementation;
import interfaces.RepositoryInterface;

public class RepoPACController {
	private RepositoryInterface boRepository;
	/*  for dialogue/change notification, following PAC principles */
	private ReposPACController reposController; 
	
	public RepoPACController(ReposPACController reposController) {
		this.boRepository = new RepositorySystemCommandImplementation();
	}
	/* implement all the getters for repo status */
	public String getState(String pathToRepo) throws Exception {
		return this.boRepository.getState(pathToRepo);
	}
}
