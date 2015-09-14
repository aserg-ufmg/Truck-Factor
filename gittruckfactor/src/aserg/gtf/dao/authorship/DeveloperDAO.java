package aserg.gtf.dao.authorship;

import java.util.List;

import javax.persistence.Query;

import aserg.gtf.dao.GenericDAO;
import aserg.gtf.dao.PersistThread;
import aserg.gtf.model.authorship.Developer;


public class DeveloperDAO extends GenericDAO<Developer> {
	
	
	@Override
	public void persist(Developer o) {
		if (o.getId()!=null){
			Developer persistedDeveloper = this.em.find(Developer.class, o.getId());
			if (persistedDeveloper != null)
				return;
		}
		super.persist(o);
	}

	@Override
	public Developer find(Object id) {
		return this.em.find(Developer.class, id);
	}
	public List<Developer> getDevelopers(String repositoryName, String newusername){

		if (newusername.contains("\'"))
			newusername = newusername.replace("'", "''");
		String hql = "SELECT d FROM Repository r "
				+ "JOIN r.developers as d "
				
				+ "WHERE r.fullName = "+ "\'" + repositoryName +"\' AND d.removed = \'FALSE\' AND d.newUserName = "+ "\'" + newusername +"\'";
		Query q = em.createQuery(hql);
		return q.getResultList();
	}
	
	@Override
	public List<Developer> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(Developer.class);
	}
	
	
	@Override
	public void merge(Developer o) {
		super.merge(o);
	}
	
	public List<Developer> getAllDevelopers(String repositoryName){
		String hql = "SELECT d FROM Repository r "
				+ "JOIN r.developers as d "
				
				+ "WHERE r.fullName = "+ "\'" + repositoryName +"\' AND d.removed = \'FALSE\' ";
		Query q = em.createQuery(hql);
		return q.getResultList();
	}

	@Override
	public boolean exist(Developer entity) {
		return this.find(entity.getId())!=null;
	}
	PersistThread<Developer> thread = null;
	public void persistAll(List<Developer> developers){
		if (thread == null)
			thread = new PersistThread<Developer>(developers, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<Developer>(developers, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}
	
	public void update(Developer o){
		Developer persistedDeveloper = this.em.find(Developer.class, o.getId());
		if (persistedDeveloper != null){
			persistedDeveloper.setAuthorshipInfos(o.getAuthorshipInfos());
			persistedDeveloper.setEmail(o.getEmail());
			persistedDeveloper.setName(o.getName());
			persistedDeveloper.setNewUserName(o.getNewUserName());
			if (o.isRemoved())
				persistedDeveloper.setAsRemoved();
			persistedDeveloper.setStatus(o.getStatus());
			persistedDeveloper.setOrigemDevelopers(o.getOrigemDevelopers());
			super.merge(persistedDeveloper);
		}
	}

	public List<String> getDuplicatedUsernames(String repositoryName) {
		String hql = "SELECT newusername FROM repository r "
				+ "JOIN repository_developer rd ON rd.repository_id = r.id "
				+ "JOIN developer d ON rd.developers_id = d.id "
				+ "	WHERE r.fullname = "+ "\'" + repositoryName +"\' AND  removed = \'FALSE\' "
				+ "GROUP BY newusername "
				+ "HAVING COUNT(DISTINCT username)>1 "
				+ "ORDER BY newusername;";
		Query q = em.createNativeQuery(hql);
		return q.getResultList();
	}
}
