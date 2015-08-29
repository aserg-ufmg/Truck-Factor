package aserg.gtf.dao;

import java.util.List;

import javax.persistence.Query;

import aserg.gtf.model.CommitFileInfo;
import aserg.gtf.model.CommitInfo;
import aserg.gtf.model.ProjectInfo;


public class CommitFileDAO extends GenericDAO<CommitFileInfo> {
	
	
	@Override
	public void persist(CommitFileInfo project) {
		super.persist(project);
	}

	@Override
	public CommitFileInfo find(Object id) {
		return this.em.find(CommitFileInfo.class, id);
	}
	
	@Override
	public List<CommitFileInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(CommitFileInfo.class);
	}
	
	@Override
	public void merge(CommitFileInfo o) {
		super.merge(o);
	}
	
	private void update(ProjectInfo o){
		
	}

	@Override
	public boolean exist(CommitFileInfo entity) {
		return this.find(entity.getId())!=null;
	}

	public List<Long> getAddsCommitFileOrderByNumberOfCFs(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_COMMITFILEINFO ci_cfi	"
				+ "JOIN COMMITINFO ci ON ci_cfi.CommitInfo_ID = ci.ID    "
				+ "JOIN COMMITFILEINFO cfi ON ci_cfi.commitFiles_ID = cfi.ID    "
				+ "WHERE ci.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND cfi.STATUS = \"ADDED\"    "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY COUNT(*) DESC;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	
	public List<Long> newGetAddsCommitFileOrderByNumberOfCFs(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_COMMITFILEINFO ci_cfi	"
				+ "JOIN COMMITINFO ci ON ci_cfi.CommitInfo_ID = ci.ID    "
				+ "JOIN COMMITFILEINFO cfi ON ci_cfi.commitFiles_ID = cfi.ID    "
				
				+ "JOIN projectinfo_fileinfo pi_fi ON pi_fi.ProjectInfo_FULLNAME = ci.REPOSITORYNAME    "
				+ "JOIN fileinfo fi ON pi_fi.files_ID = fi.ID    "
				
				+ "WHERE ci.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND cfi.STATUS = \"ADDED\" AND cfi.NEWFILENAME = fi.PATH   "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY COUNT(*) DESC;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	public List<Long> getAddsCommitFileOrderByDate(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_COMMITFILEINFO ci_cfi	"
				+ "JOIN COMMITINFO ci ON ci_cfi.CommitInfo_ID = ci.ID    "
				+ "JOIN COMMITFILEINFO cfi ON ci_cfi.commitFiles_ID = cfi.ID    "
				+ "WHERE ci.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND cfi.STATUS = \"ADDED\"    "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY ci.DATE;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	
	public List<Long> newGetAddsCommitFileOrderByDate(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_COMMITFILEINFO ci_cfi	"
				+ "JOIN COMMITINFO ci ON ci_cfi.CommitInfo_ID = ci.ID    "
				+ "JOIN COMMITFILEINFO cfi ON ci_cfi.commitFiles_ID = cfi.ID    "
				
				+ "JOIN projectinfo_fileinfo pi_fi ON pi_fi.ProjectInfo_FULLNAME = ci.REPOSITORYNAME    "
				+ "JOIN fileinfo fi ON pi_fi.files_ID = fi.ID    "
				
				+ "WHERE ci.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND cfi.STATUS = \"ADDED\" AND cfi.NEWFILENAME = fi.PATH   "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY ci.DATE;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	
}
