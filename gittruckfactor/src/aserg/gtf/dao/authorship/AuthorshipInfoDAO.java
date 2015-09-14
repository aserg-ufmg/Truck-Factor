package aserg.gtf.dao.authorship;

import java.util.List;

import javax.persistence.Query;

import aserg.gtf.dao.GenericDAO;
import aserg.gtf.dao.PersistThread;
import aserg.gtf.model.authorship.AuthorshipInfo;


public class AuthorshipInfoDAO extends GenericDAO<AuthorshipInfo> {
	
	
	@Override
	public void persist(AuthorshipInfo o) {
		if (o.getId()!=null){
			AuthorshipInfo authorshipInfo = this.em.find(AuthorshipInfo.class, o.getId());
			if (authorshipInfo != null)
				return;
		}
		super.persist(o);
	}

	@Override
	public AuthorshipInfo find(Object id) {
		return this.em.find(AuthorshipInfo.class, id);
	}
	
	@Override
	public List<AuthorshipInfo> findAll(Class clazz) {
		return super.findAll(AuthorshipInfo.class);
	}
	
	
	@Override
	public void merge(AuthorshipInfo o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(AuthorshipInfo entity) {
		return this.find(entity.getId())!=null;
	}

	PersistThread<AuthorshipInfo> thread = null;
	public void persistAll(List<AuthorshipInfo> authorshipsInfo){
		if (thread == null)
			thread = new PersistThread<AuthorshipInfo>(authorshipsInfo, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<AuthorshipInfo>(authorshipsInfo, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}
	
	public List<AuthorshipInfo> getAuthorshipInfoList(String repositoryName){
		String hql = "SELECT a FROM Repository r "
				+ "JOIN r.files as f "
				+ "JOIN f.authorshipInfos a "
				
				+ "WHERE r.fullName = "+ "\'" + repositoryName +"\' ";
		Query q = em.createQuery(hql);
		return q.getResultList();
	}
	
	public void updateDOA(AuthorshipInfo a){
		AuthorshipInfo persistedAuthorshipInfo = this.em.find(AuthorshipInfo.class, a.getId());
		persistedAuthorshipInfo.updateDOA();
		super.merge(persistedAuthorshipInfo);
	}
}
