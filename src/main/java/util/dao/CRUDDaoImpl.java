package util.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class CRUDDaoImpl implements CRUDDao {

	@PersistenceContext(unitName = "DeID")
	private EntityManager em;
	
	public EntityManager getEm() {
		em.clear();
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> klass) {
		return getEm().createQuery("Select t from " + klass.getSimpleName() + " t").getResultList();
	}

	public <T> T save(T t) {
		T newRecord = null;
		em.persist(t);
		em.flush();
		em.refresh(t);
		return newRecord;
	}

	public <T> T merge(T t) {
		em.merge(t);
		em.flush();
		return t;
	}
	
	public <T> void delete(T t) {
		em.remove(em.merge(t));
		em.flush();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findXXXXX(String sql) {
		return getEm().createQuery(sql).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAllByLimit(Class<T> klass, int offset, int limit) {
		return getEm().createQuery("Select t from " + klass.getSimpleName() + " t").setFirstResult(offset)
				.setMaxResults(limit).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> T singleResult(String sql) {
		return (T) getEm().createQuery(sql).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public <T> T singleBetweenResult(String sql, Date beginDate, Date endDate) {
		return (T) getEm().createQuery(sql).setParameter("begin", beginDate, TemporalType.TIMESTAMP)
				.setParameter("end", endDate, TemporalType.TIMESTAMP).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByOffsetLimit(String sql, int offset, int limit) {
		return getEm().createQuery(sql).setFirstResult(offset).setMaxResults(limit).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getBetweenByLimit(String sql, Date beginDate, Date endDate, int offset, int limit) {
		return getEm().createQuery(sql).setParameter("begin", beginDate, TemporalType.TIMESTAMP)
				.setParameter("end", endDate, TemporalType.TIMESTAMP).setFirstResult(offset).setMaxResults(limit)
				.getResultList();
	}

	@Override
	public <T> Query getBetweenByLimit2(String sql) {
		Query q = getEm().createQuery(sql);
		return q;
	}

	@Override
	public <T> Query createSQLDeleteBatch(T t, String sqlDetail, String sqlMaster) {
		int count = em.createQuery(sqlDetail).executeUpdate();
		int count1 = em.createQuery(sqlMaster).executeUpdate();
		// http://stackoverflow.com/questions/12875595/entitymanger-refresh-after-jpql-bulk-delete
		System.out.println("sqlDetail delete count:" + count);
		System.out.println("sqlMaster delete count:" + count1);
		return null;
	}

	@Override
	public <T> Query createSQLDeleteBatch(T t, String... sqlExecute) {
		for (int i = 0; i < sqlExecute.length; i++) {
			// int count = em.createQuery(sqlExecute[i].toString()).executeUpdate();
			int count = em.createNativeQuery(sqlExecute[i].toString()).executeUpdate();
			System.out.println("Execute:" + sqlExecute[i] + " count:" + count);
		}
		return null;
	}

	@Override
	public <T> Query createSQL(String sql) {
		Query q = getEm().createNativeQuery(sql);
		return q;
	}

	@Override
	public <T> Query createSQL(String sql, Class<T> entityClass) {
		Query q = getEm().createNativeQuery(sql, entityClass);
		return q;
	}

	@Override
	public <T> Query createSQLMap(String sql) {
		Query q = getEm().createNativeQuery(sql);  
        q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q;
	}	
	
	@Override
	public Long findtotalSize(String sql) {
		Query q = getEm().createNativeQuery(sql);
		return ((BigDecimal) q.getSingleResult()).longValue();
	}

	@Override
	public void clearSession() {
		getEm().clear();
	}


	@Override
	public <T> Query insertOrUpdate(String... sqlExecute) {
		for (int i = 0; i < sqlExecute.length; i++) {
			int count = em.createNativeQuery(sqlExecute[i].toString()).executeUpdate();
			System.out.println("Execute:" + sqlExecute[i] + " count:" + count);
		}
		return null;
	}

}
