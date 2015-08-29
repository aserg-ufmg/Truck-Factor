package aserg.gtf.dao;

import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import aserg.gtf.model.FileType;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.model.authorship.DeveloperAuthorshipInfo;

public class NewFileInfoDAO extends GenericDAO<NewFileInfo>{

	@Override
	public void persist(NewFileInfo o) {
		if (o.getId()!=null){
			NewFileInfo newFile = this.em.find(NewFileInfo.class, o.getId());
			if (newFile != null)
				return;
		}
		super.persist(o);
		
	}

	@Override
	public void merge(NewFileInfo o) {
		super.merge(o);
	}
	
	@Override
	public NewFileInfo find(Object o) {
		return this.em.find(NewFileInfo.class, o);
	}	

	@Override
	public boolean exist(NewFileInfo entity) {
		return this.find(entity.getId())!=null;
	}

//	public void update(FileInfo o){
//		FileInfo persistedProject = this.em.find(FileInfo.class, o.getId());
//		if (persistedProject != null){
//			persistedProject.setFiltered(o.getFiltered());
//			persistedProject.setFilterInfo(o.getFilterInfo());
//			persistedProject.setMode(o.getMode());
//			persistedProject.setPath(o.getPath());
//			persistedProject.setSha(o.getSha());
//			persistedProject.setSize(o.getSize());
//			persistedProject.setType(o.getType());
//			persistedProject.setLanguage(o.getLanguage());
//			super.merge(persistedProject);
//		}
//	}
//	public List<String> getPathsOfNotFilteredProjectFiles(String projectName){
//		String custom = "";
//		     
//		String hql = "SELECT fi.path FROM projectinfo_fileinfo pi_fi "
//				+ "JOIN projectinfo pi ON pi_fi.projectinfo_fullname = pi.fullname "
//				+ "JOIN newfileinfo nfi on pi_fi.files_id = nfi.id "
//				+ "WHERE pi.fullname = \'"+projectName+"\' AND nfi.filtered = 'FALSE' "
//				+ ";";
//				
//		Query q = em.createNativeQuery(hql);
//		return q.getResultList();
//			
//	}
	
	public int filterAndUpdateFilesInfo(String whereClauses, String filterStamp){
		String custom = "";
		     
		String hql = "UPDATE  newfileinfo AS fi "
				+ "SET filtered = \'TRUE\', filterinfo = \'"+ filterStamp +"\'  "
				+ "WHERE "
				+ whereClauses
				+ ";";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
	}
	public int filterAndUpdateFilesInfoByLanguage(String whereClauses, String filterStamp){
		String custom = "";
		     
		String hql = "UPDATE  newfileinfo AS fi "
				+ "SET filtered = \'TRUE\', filterinfo = \'"+ filterStamp +"\'  "
				+ "FROM projectinfo AS pi    "
				+ "WHERE pi.fullname = fi.repositoryname AND "
				+ whereClauses
				+ ";";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}
	public int removeFilterAndUpdateFilesInfo(String whereClauses, String filterStamp){
		String custom = "";
		     
		String hql = "UPDATE  newfileinfo AS fi "
				+ "SET filtered = \'FALSE\', filterinfo = \'\'  "
				+ "WHERE "
				+ whereClauses
				+ ";";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}
	
	public int classifierAndUpdateFilesInfo(String whereClauses, FileType fileType){
		String custom = "";
		     
		String hql = "UPDATE  newfileinfo AS fi "
				+ "SET kind = \'"+ fileType +"\'  "
				+ "WHERE  "
				+ whereClauses
				+ ";";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}
	
	public int cleanFilter(){
			     
		String hql = "UPDATE  newfileinfo "
				+ "SET filtered = \'FALSE', filterinfo = \'\';";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}
	public int setAllAsNotLinguist(){
	     
		String hql = "UPDATE  newfileinfo "
				+ "SET filtered = \'TRUE', kind = \'NOTIDENTIFIED\', filterinfo = \'#NOTLINGUIST#\', language = \'\';";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}

	public int updateLanguageFileInfo(String projectName, String language, List<String> paths) {
		FileType fileType = FileType.getType(language);
		List<Query> queries = new ArrayList<Query>();
		for (String path : paths) {			
			if (path.contains("\'"))
				path = path.replace("'", "''");
			String hql = "UPDATE  newfileinfo "
					+ "SET kind = \'"
					+ fileType
					+ "\'  , language = \'"
					+ language
					+ "\', filtered = \'FALSE\' "
					+ ", filterinfo = \'\' "
					+ "WHERE repositoryname = \'" + projectName + "\' AND path =  \'"
					+ path + "\' " + ";";
			queries.add(em.createNativeQuery(hql));
		}
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			for (Query query : queries) {
				rows += query.executeUpdate();
			}			
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
		
	}
	PersistThread<NewFileInfo> thread = null;
	public void persistAll(Collection<NewFileInfo> newFiles){
		if (thread == null)
			thread = new PersistThread<NewFileInfo>(newFiles, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<NewFileInfo>(newFiles, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}

	public void clear() {
		this.em.clear();
	}

}
