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
import aserg.gtf.model.CommitInfo;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.authorship.File;


public class FileDAO extends GenericDAO<File> {
	
	
	@Override
	public void persist(File o) {
		if (o.getId()!=null){
			File persistedFile = this.em.find(File.class, o.getId());
			if (persistedFile != null)
				return;
		}
		super.persist(o);
	}

	@Override
	public File find(Object id) {
		return this.em.find(File.class, id);
	}
	
	@Override
	public List<File> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(File.class);
	}
	
	
	@Override
	public void merge(File o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(File entity) {
		return this.find(entity.getId())!=null;
	}
	PersistThread<File> thread = null;
	public void persistAll(List<File> files){
		if (thread == null)
			thread = new PersistThread<File>(files, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<File>(files, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}
}
