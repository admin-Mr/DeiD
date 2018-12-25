package util.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface CRUDDao {
	<T> EntityManager getEm();
	
	<T> List<T> getAll(Class<T> klass);

	<T> T save(T t);
	
	<T> T merge(T t);
	
	<T> void delete(T t);

	<T> List<T> findXXXXX(String sql);

	<T> List<T> findByOffsetLimit(String sql, int offset, int limit);

	<T> List<T> getAllByLimit(Class<T> klass, int offset, int limit);

	<T> List<T> getBetweenByLimit(String sql, Date beginDate, Date endDate, int offset, int limit);

	<T> T singleResult(String sql);

	<T> T singleBetweenResult(String sql, Date beginDate, Date endDate);

	<T> Query getBetweenByLimit2(String sql);

	<T> Query createSQL(String sql);
	
	<T> Query createSQL(String sql, Class<T> entityClass);

	<T> Query createSQLMap(String sql);	

	<T> Long findtotalSize(String sql);
	
	<T> Query insertOrUpdate(String... sqlExecute);

	<T> Query createSQLDeleteBatch(T t, String sqlMaster, String sqlDetail);
	
	<T> Query createSQLDeleteBatch(T t, String... sqlExecute);
	
	default void clearSession(){}
}