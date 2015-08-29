package aserg.gtf.dao;

import java.util.List;

import aserg.gtf.model.GitRepository;
import aserg.gtf.model.ProjectInfo;


public class GitRepositoryDAO extends GenericDAO<GitRepository> {
	
	
	@Override
	public void persist(GitRepository o) {
		GitRepository repository = this.em.find(GitRepository.class, o.getRepositoryName());
		if (repository == null)
			super.persist(o);
	}

	@Override
	public GitRepository find(Object id) {
		return this.em.find(GitRepository.class, id);
	}
	
	@Override
	public List<GitRepository> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(GitRepository.class);
	}
	
	@Override
	public void merge(GitRepository o) {
		GitRepository gitRepo = this.find(o.getRepositoryName());
		if (gitRepo != null){
			super.remove(gitRepo);
		}
		super.merge(o);
	}
	
	private void removeDuplicadeCommitFiles(GitRepository gitRepo,
			GitRepository o) {
		
		
	}

	private void update(ProjectInfo o){
		
	}

	@Override
	public boolean exist(GitRepository entity) {
		return this.find(entity.getRepositoryName())!=null;
	}
}
