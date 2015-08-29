package aserg.gtf.dao.authorship;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import aserg.gtf.dao.GenericDAO;
import aserg.gtf.dao.PersistThread;
import aserg.gtf.model.authorship.Developer;
import aserg.gtf.model.authorship.DeveloperAuthorshipInfo;
import aserg.gtf.model.authorship.File;


public class DeveloperAuthorshipInfoDAO extends GenericDAO<DeveloperAuthorshipInfo> {
	
	
	@Override
	public void persist(DeveloperAuthorshipInfo o) {
		if (o.getId()!=null){
			DeveloperAuthorshipInfo developerAuthorshipInfo = this.em.find(DeveloperAuthorshipInfo.class, o.getId());
			if (developerAuthorshipInfo != null)
				return;
		}
		super.persist(o);
		
	}

	@Override
	public DeveloperAuthorshipInfo find(Object id) {
		return this.em.find(DeveloperAuthorshipInfo.class, id);
	}
	
	@Override
	public List<DeveloperAuthorshipInfo> findAll(Class clazz) {
		return super.findAll(DeveloperAuthorshipInfo.class);
	}
	
	
	@Override
	public void merge(DeveloperAuthorshipInfo o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(DeveloperAuthorshipInfo entity) {
		return this.find(entity.getId())!=null;
	}

	PersistThread<DeveloperAuthorshipInfo> thread = null;
	public void persistAll(Collection<DeveloperAuthorshipInfo> developerAuthors){
		if (thread == null)
			thread = new PersistThread<DeveloperAuthorshipInfo>(developerAuthors, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<DeveloperAuthorshipInfo>(developerAuthors, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}

	public List<String> projectsAlreadyCalculated(){
		String hql = "SELECT dai.repositoryname FROM developerauthorshipinfo dai GROUP BY dai.repositoryname;";
		Query q = em.createNativeQuery(hql);
		return q.getResultList();
	}
	
	public int numDevelopers(String repositoryName){
		String hql = "SELECT COUNT(*) FROM developerauthorshipinfo dai WHERE dai.repositoryname = \'"+repositoryName+"\';";
		Query q = em.createNativeQuery(hql);
		
		return Integer.parseInt(q.getSingleResult().toString());
	}
}
