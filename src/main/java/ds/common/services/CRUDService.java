package ds.common.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.Session;

public interface CRUDService {
	<T> EntityManagerFactory getEmf();
	
	<T> Session getSession();
	
	<T> List<T> getAll(Class<T> klass);

	<T> T save(T t);
		
	<T, K> T save(T t, ArrayList<K> arr); //tiger 20160412

	<T> T merge(T t);
	
	<T> void delete(T t);

	<T> List<T> findXXXXX(String sql);

	/**
	 * 
	 * @param sql
	 *            sql指令
	 * @param offset
	 *            搜尋起始row
	 * @param limit
	 *            限制筆數
	 * @return
	 */
	<T> List<T> findByOffsetLimit(String sql, int offset, int limit);

	/**
	 * 
	 * @param klass
	 *            className
	 * @param offset
	 *            搜尋起始row
	 * @param limit
	 *            限制筆數
	 * @return
	 */
	<T> List<T> getAllByLimit(Class<T> klass, int offset, int limit);

	<T> List<T> getBetweenByLimit(String sql, Date beginDate, Date endDate, int offset, int limit);

	<T> T singleResult(String sql);

	<T> T singleBetweenResult(String sql, Date beginDate, Date endDate);

	<T> Query getBetweenByLimit2(String sql);
	
	<T> Query insertOrUpdate(String... sqlExecute);
	
	<T> Query createSQLDeleteBatch(T t, String sqlMaster, String sqlDetail);
	
	<T> Query createSQLDeleteBatch(T t, String... sqlExecute);
	
	<T> Query createSQL(String sql);
	
	<T> Query createSQL(String sSQL, Class<T> entityClass);
	
	<T> Query createSQLMap(String sSQL);

	<T> Long findtotalSize(String sql);
	
	default void clearSession(){}


}
