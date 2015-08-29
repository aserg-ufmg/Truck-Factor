package aserg.gtf.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityTransaction;

public class PersistThread <T> extends Thread{
	Collection<T> list;
	GenericDAO<T> persistDAO;
	public PersistThread(Collection<T> list, GenericDAO<T> persistDAO) {
		this.list = list;
		this.persistDAO = persistDAO; 
	}
	@Override
	public void run() {
		System.out.println("Thread iniciada = "+this.getName() + " persisting objects: "+  list.size());
		EntityTransaction tx = persistDAO.em.getTransaction();
		try {
			tx.begin();
			for (T t : list) {
				persistDAO.em.persist(t);
			}
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			persistDAO.em.clear();
		}
			
		System.out.println("Thread Finalizada = "+this.getName());		
	}

	public void setList(Collection<T> list) {
		this.list = list;
	}
}
