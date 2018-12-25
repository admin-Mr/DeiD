package ds.common.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import util.dao.CRUDDao;

@Service
public class CRUDServiceImpl implements CRUDService {

	@Autowired
	@Qualifier("CRUDDao")
	private CRUDDao crudDao;

	@Transactional
	public <T> EntityManagerFactory getEmf() {
		return crudDao.getEm().getEntityManagerFactory();
	}
	
	@Transactional
	public <T> Session getSession() {
		SessionFactory sessionFactory = ((HibernateEntityManagerFactory) getEmf()).getSessionFactory();
		return ((SessionFactoryImpl) sessionFactory).getCurrentSession();
	}
	
	@Transactional(readOnly = true)
	public <T> List<T> getAll(Class<T> klass) {
		return crudDao.getAll(klass);
	}

	@Transactional
	public <T> T save(T t) {
		T newRecord = null;
		newRecord = crudDao.save(t);
		return newRecord;
	}

	@Transactional
	public <T, K> T save(T t, ArrayList<K> arr) {
		T newRecord = null;
		newRecord = crudDao.save(t);
		for(K k : arr) //ex : ArrayList<OpenCourseDetailVO>
			crudDao.save(k);
		return null;
	}
	
	
	@Transactional
	public <T> T merge(T t) {
		crudDao.merge(t);
		return t;
	}
	
	@Transactional
	public <T> void delete(T t) {
		crudDao.delete(t);
	}

	@Transactional
	public <T> List<T> findXXXXX(String sql) {
		return crudDao.findXXXXX(sql);
	}

	@Transactional
	public <T> List<T> findByOffsetLimit(String sql, int offset, int limit) {
		return crudDao.findByOffsetLimit(sql, offset, limit);
	}

	@Transactional(readOnly = true)
	public <T> List<T> getAllByLimit(Class<T> klass, int offset, int limit) {
		return crudDao.getAllByLimit(klass, offset, limit);
	}

	@Transactional(readOnly = true)
	public <T> List<T> getBetweenByLimit(String sql, Date beginDate, Date endDate, int offset, int limit) {
		return crudDao.getBetweenByLimit(sql, beginDate, endDate, offset, limit);
	}

	@Transactional
	public <T> T singleResult(String sql) {
		return crudDao.singleResult(sql);
	}

	@Transactional
	public <T> T singleBetweenResult(String sql, Date beginDate, Date endDate) {
		return crudDao.singleBetweenResult(sql, beginDate, endDate);
	}

	@Transactional
	public <T> Query createSQL(String sql) {
		return crudDao.createSQL(sql);
	}

	@Transactional
	public <T> Query getBetweenByLimit2(String sql) {
		return crudDao.getBetweenByLimit2(sql);
	}
	
	@Transactional
	public <T> Query insertOrUpdate(String... sqlExecute) {
		return crudDao.insertOrUpdate(sqlExecute);
	}
	
	@Transactional
	public <T> Query createSQLDeleteBatch(T t, String sqlMaster, String sqlDetail) {
		return crudDao.createSQLDeleteBatch(t, sqlMaster, sqlDetail);
	}
	
	@Transactional
	public <T> Query createSQLDeleteBatch(T t, String... sqlExecute){
		return crudDao.createSQLDeleteBatch(t, sqlExecute);
	}
	
	@Transactional
	public Long findtotalSize(String sql) {
		return crudDao.findtotalSize(sql);
	}

	@Transactional
	public void clearSession(){
		crudDao.clearSession();
	}
	
	@Transactional
	public <T> Query createSQL(String sql, Class<T> entityClass) {
		return crudDao.createSQL(sql, entityClass);
	}
	
	@Transactional
	public <T> Query createSQLMap(String sql) {
		return crudDao.createSQLMap(sql);
	}
}
