package aserg.gtf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.sessions.Session;


public abstract class GenericDAO<T> {
	protected final EntityManager em =  Persistence.createEntityManagerFactory("main").createEntityManager();

	public GenericDAO() {
		super();
	}
	
	public void persist(T o) {
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			this.em.persist(o);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}
	}
	
	public abstract T find(Object id);
	public abstract boolean exist(T entity);
		
	public void remove(T o) {
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			this.em.remove(o);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}
		finally{
			this.em.clear();
		}
	}
	
	public void merge(T o) {
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			this.em.merge(o);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}
		finally{
			this.em.clear();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.em.close();
		super.finalize();
	}
	
	public List<T> findAll(Class clazz) {
		String hql = "select a from " +  clazz.getSimpleName() + " a";
		Query q = em.createQuery(hql);
		return q.getResultList();
	}
    
	
	
}
