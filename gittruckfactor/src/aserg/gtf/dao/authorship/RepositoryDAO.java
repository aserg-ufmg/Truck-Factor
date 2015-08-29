package aserg.gtf.dao.authorship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import aserg.gtf.dao.GenericDAO;
import aserg.gtf.dao.PersistThread;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.model.authorship.File;
import aserg.gtf.model.authorship.FileAuthors;
import aserg.gtf.model.authorship.Repository;


public class RepositoryDAO extends GenericDAO<Repository> {
	
	
	@Override
	public void persist(Repository o) {
		if (o.getId()!=null){
			Repository persistedRepository = this.em.find(Repository.class, o.getId());
			if (persistedRepository != null)
				return;
		}
		prePersist(o);
		super.persist(o);
	}
	
	public void clear(){
		this.em.clear();
	}

	private void prePersist(Repository repository) {
//		new DeveloperDAO().persistAll(repository.getDevelopers());
//		new FileDAO().persistAll(repository.getFiles());
//		new AuthorshipInfoDAO().persistAll(repository.getAuthorships());
	}

	@Override
	public Repository find(Object id) {
		return this.em.find(Repository.class, id);
	}
	
	public List<Repository> findAll() {
		// TODO Auto-generated method stub
		return super.findAll(Repository.class);
	}
	
	
	@Override
	public void merge(Repository o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(Repository entity) {
		return this.find(entity.getId())!=null;
	}
	
	public Map<String, Set<String>> getFilesAuthor(String repositoryName){
		String hql = "SELECT d.newusername, fi.path FROM repository_file rf	"
				+ "JOIN repository r ON r.id = rf.repository_id "
				+ "JOIN file fi ON fi.id = rf.files_id "
				+ "JOIN authorshipinfo ai ON ai.id = fi.bestauthorshipinfo_id "
				+ "JOIN developer d ON ai.developer_id = d.id "
				+ "WHERE r.status <> \'REMOVED\' AND r.fullname = \'" +  repositoryName +"\' " 
				+ "ORDER BY d.newusername;";
		Query q = em.createNativeQuery(hql);
		List<Object[]> authorsFiles = q.getResultList();
		
		Map<String, Set<String>> maps = new HashMap<String, Set<String>>();
		for (Object[] objects : authorsFiles) {
			String developerName = (String)objects[0];
			String fileName = (String)objects[1];
			if (!maps.containsKey(developerName))
				maps.put(developerName, new HashSet<String>());
			maps.get(developerName).add(fileName);			
		}
		return maps;
	}
	
	public List<File> getFiles(String repositoryName){
		String hql = "SELECT fi FROM repository_file rf	"
				+ "JOIN repository r ON r.id = rf.repository_id "
				+ "JOIN file fi ON fi.id = rf.files_id "
				+ "WHERE r.status <> \'REMOVED\' AND r.fullname = \'" +  repositoryName +"\' ;" ;
		Query q = em.createNativeQuery(hql);
		return q.getResultList();
	}
	
	public Set<Long> getFilesSet(String repositoryName){
		String hql = "SELECT fi.id FROM repository_file rf	"
				+ "JOIN repository r ON r.id = rf.repository_id "
				+ "JOIN file fi ON fi.id = rf.files_id "
				+ "WHERE r.status <> \'REMOVED\' AND r.fullname = \'" +  repositoryName +"\'  AND fi.removed = \'FALSE\' ;" ;
		Query q = em.createNativeQuery(hql);
		return new HashSet<Long>(q.getResultList());
	}
	
	public Map<Long, Set<Long>> getFilesAuthorMap(String repositoryName){
		Map<Long, Set<Long>> map = new HashMap<Long, Set<Long>>();
		String hql = "SELECT d.id, fi.id FROM repository_file rf	"
				+ "JOIN repository r ON r.id = rf.repository_id "
				+ "JOIN file fi ON fi.id = rf.files_id "
				+ "JOIN authorshipinfo ai ON ai.id = fi.bestauthorshipinfo_id "
				+ "JOIN developer d ON ai.developer_id = d.id "
				+ "WHERE r.fullname = \'" +  repositoryName +"\' AND fi.removed = \'FALSE\' " 
				+ "ORDER BY fi.path;";
		Query q = em.createNativeQuery(hql);
		List<Object[]> authorsFiles = q.getResultList();
		
		List<FileAuthors> fileAuthors = new ArrayList<FileAuthors>();
		for (Object[] objects : authorsFiles) {
			Long authorId = (Long)objects[0];
			Long fileId = (Long)objects[1];
			if(!map.containsKey(authorId))
				map.put(authorId, new HashSet<Long>());
			map.get(authorId).add(fileId);
		}
		return map;
	}
	
	public List<FileAuthors> getFilesAuthorList(String repositoryName){
		String hql = "SELECT d.newusername, fi.path FROM repository_file rf	"
				+ "JOIN repository r ON r.id = rf.repository_id "
				+ "JOIN file fi ON fi.id = rf.files_id "
				+ "JOIN authorshipinfo ai ON ai.id = fi.bestauthorshipinfo_id "
				+ "JOIN developer d ON ai.developer_id = d.id "
				+ "WHERE r.fullname = \'" +  repositoryName +"\' " 
				+ "ORDER BY fi.path;";
		Query q = em.createNativeQuery(hql);
		List<Object[]> authorsFiles = q.getResultList();
		
		List<FileAuthors> fileAuthors = new ArrayList<FileAuthors>();
		for (Object[] objects : authorsFiles) {
			fileAuthors.add(new FileAuthors((String)objects[1], (String)objects[0]));			
		}
		return fileAuthors;
	}
	public List<String> getAllRepositoryNames(){
		String hql = "SELECT fullname FROM repository r WHERE r.status <> \'REMOVED\'  ;";
		Query q = em.createNativeQuery(hql);
		return q.getResultList();
	}
	
	public void update(Repository o){
		Repository persistedRepository = this.em.find(Repository.class, o.getId());
		if (persistedRepository != null){
			persistedRepository.setAuthorshipInfoMap(o.getAuthorshipInfoMap());
			persistedRepository.setDevelopers(o.getDevelopers());
			persistedRepository.setFiles(o.getFiles());
			persistedRepository.setFullName(o.getFullName());
			persistedRepository.setStatus(o.getStatus());
			super.merge(persistedRepository);
		}
	}

	public int getNumDevelopers() {
		// TODO Auto-generated method stub
		return 0;
	}
}
