package aserg.gtf.dao;

import java.util.List;

import javax.persistence.Query;

import aserg.gtf.model.CommitFileInfo;
import aserg.gtf.model.CommitInfo;
import aserg.gtf.model.GitRepository;
import aserg.gtf.model.ProjectInfo;


public class CommitInfoDAO extends GenericDAO<CommitInfo> {
	PersistThread<CommitInfo> thread = null;
	@Override
	public void persist(CommitInfo project) {
		super.persist(project);
	}

	@Override
	public CommitInfo find(Object id) {
		return this.em.find(CommitInfo.class, id);
	}
	
	@Override
	public List<CommitInfo> findAll(Class clazz) {
		return super.findAll(CommitInfo.class);
	}
	
	public CommitInfo find(String repositoryName, String sha){
		String hql = "select a from " +  CommitInfo.class.getSimpleName() + " a " + 
					 " WHERE a.repositoryName = \""+repositoryName+"\"" +  " AND a.sha = \"" + sha +"\"";
		Query q = em.createQuery(hql);
		return (CommitInfo)q.getSingleResult();
	}
	
	public List<CommitInfo> findAllOrderByRepositoryName() {
		String hql = "select a from " +  CommitInfo.class.getSimpleName() + " a order by a.repositoryName";
		Query q = em.createQuery(hql);
		return q.getResultList();
	}
	
	public int getNumberAuthors(String repositoryName) {
//		String hql = "SELECT COUNT(C) FROM " +  CommitInfo.class.getSimpleName() +  " C WHERE C.repositoryName = \":name\"";
		String hql = "SELECT COUNT(DISTINCT ci.email) FROM COMMITINFO ci WHERE ci.repositoryName = \'" +  repositoryName+"\'";
		Query q = em.createNativeQuery(hql);
		
		return ((Long)q.getSingleResult()).intValue();
	}
	
	@Override
	public void merge(CommitInfo o) {
		CommitInfo commit = this.find(o.getSha());
		if (commit == null)
			super.merge(o);
	}
	
	public void persistAll(List<CommitInfo> commits){
		if (thread == null)
			thread = new PersistThread<CommitInfo>(commits, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread.setList(commits);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.run();
	}

	public void update(CommitInfo o){
		CommitInfo persistedCommit = this.em.find(CommitInfo.class, o.getId());
		if (persistedCommit != null){
			persistedCommit.setCommitFiles(o.getCommitFiles());			
			persistedCommit.setDate(o.getDate());
			persistedCommit.setEmail(o.getEmail());
			persistedCommit.setLogCommitFiles(o.getLogCommitFiles());
			persistedCommit.setMessage(o.getMessage());
			persistedCommit.setName(o.getName());
			persistedCommit.setRepositoryName(o.getRepositoryName());
			persistedCommit.setSha(o.getSha());
			persistedCommit.setNameCommiter(o.getNameCommiter());
			persistedCommit.setEmaiCommiter(o.getEmaiCommiter());
			super.merge(persistedCommit);
		}
	}
	
//	public void updateOnlyLogFiles(CommitInfo o){
//		CommitInfo persistedCommit = this.em.find(CommitInfo.class, o.getId());
//		if (persistedCommit.equals(o)){
//			super.merge(o);
//		}
//		if (persistedCommit != null){
//			persistedCommit.setLogCommitFiles(o.getLogCommitFiles());
//			super.merge(persistedCommit);
//		}
//	}

	@Override
	public boolean exist(CommitInfo entity) {
		return this.find(entity.getSha())!=null;
	}
}
