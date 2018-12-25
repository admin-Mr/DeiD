package util.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;

/**
 * 泛用型的Data Access Gateway(DAO)，可以利用這個當Gateway，對Enity做新增、修改、刪除、
 * 查詢等動作.
 * 使用DAO前，必須先設定操作的目標物為那個Entity
 * 
 * 一般來說，如果為雙檔的情況下(master/detail)，為了performace，我們在從資料庫取物件時，
 * 並不會將所有的子單，如果取母單時，要一併取子單的話，要透過確定只會有一筆傳回值的資料，如
 * <code>GenericDAO#findById</code>、<code>GenericDAO#findUnique</code>、
 * <code>GenericDAO#findUniqueByExample</code>
 * 
 * @author Kent <KentChu@dsc.com.tw>
 *
 * @param <T> Entity物件，
 * @param <PK> Primary Key, 可以是單一鍵或複合鍵(composite key)
 */
public interface IGenericDAO<T, PK extends Serializable> {

	/**
	 *  將entity行資料庫中刪除. 
	 * @param entity 要刪除的Entity物件
	 */
	public void delete(T entity);

	/**
	 * 刪除所有的entities.
	 * @param c Entity集合.
	 */
	public void deleteAll(List<T> c);

	/**
	 * 利用Hibernate Query Language(HQL)進行查詢
	 * @param maxResults 查詢結果最大數量
	 * @param queryString HQL
	 * @param params 參數(可以0到多個參數，但須與HQL的裡條件相符)
	 */
	public List<T> find(final int maxResults, String queryString, Object... params);

	/**
	 * 傳回所有的資料.如果沒有任何資料，就傳回一個空的(長度為0)的array
	 * @param maxResults 查詢結果最大數量
	 * @return 所有的entity 
	 */
	public List<T> findAll(int maxResults);

	/**
	 * 簡易查詢專用的method.
	 * @param maxResults 查詢結果最大數量
	 * @param criteria 查詢條件
	 * @return 取得eitity，如果是子母單，所有子單所會被一併取回.
	 */
	public List<T> findByCriteria(int maxResults, DetachedCriteria criteria);

	/**
	 * Query By Example(QBE)的query method, 傳入一個查詢條件樣本(exampleInstance)，會傳回符合樣本條件的結果集.
	 * 查詢的結果必須確定只有一筆，如果有超過一筆的資料，會擲出例外.
	 * @param maxResults 查詢結果最大數量
	 * @param exampleInstance 查詢條件樣本
	 * @param excludeProperty 除外屬性，可將某些特定屬性(properties)除外.
	 * @return 符合樣本條件的結果集.
	 */
	public List<T> findByExample(int maxResults, T exampleInstance, String... excludeProperty);

	/**
	 * 由Primary Key取得entity.
	 * 此query method會將相關的屬性(含集合)進行lazy initil.
	 * @param pk
	 * @return 取得eitity，如果是子母單，所有子單所會被一併取回.
	 */
	public T findById(PK pk);

	/**
	 * 由單頭單身間對應key的關聯來找所單頭的某個(視{@link #getSupportsClass()}決定)單身的
	 * 所有資料.
	 * @param maxResults 最大筆數
	 * @param parentDO parent 物件，單頭部份是用來取得其單身
	 * @param parentPK parent primary key
	 * @param matchMasterKeys 單身中與單頭相對應該鍵值.
	 */
	public List<T> findByParent(int maxResults, final Object parentDO, final String[] parentPK, String[] matchMasterKeys);

	/**
	 * 利用Hibernate Query Language(HQL)進行查詢特定一筆的資料.
	 * 查詢的結果必須確定只有一筆，如果有超過一筆的資料，會擲出例外.
	 * 此query method會將相關的屬性(含集合)進行lazy initil.
	 * @param queryString HQL
	 * @param params 參數(可以0到多個參數，但須與HQL的裡條件相符)
	 * @return 取得eitity，如果是子母單，所有子單所會被一併取回.
	 */
	public T findUnique(String queryString, Object... params);

	/**
	 * Query By Example(QBE)的query method, 傳入一個查詢條件樣本(exampleInstance)，會傳回符合樣本條件的結果集.
	 * 此query method會將相關的屬性(含集合)進行lazy initil.
	 * @param exampleInstance 查詢條件樣本
	 * @param excludeProperty 除外屬性，可將某些特定屬性(properties)除外.
	 * @return 符合樣本條件的結果集.
	 */
	public T findUniqueByExample(T exampleInstance, String... excludeProperty);

	/**
	 * 取得欄位最大值(max)
	 * @param columnName 欄位名稱
	 * @return 欄位最大值
	 */
	public Object getMaxNumber(String columnName);

	@SuppressWarnings("rawtypes")
	public Class getSupportsClass();

	/**
	 * 重新從資料庫的讀取指定的資料.
	 * @param entity 要重新整理的物件
	 */
	public void reflash(T entity);

	/**
	 * 將entity存入資料庫中.如果entity己經存在資料庫中，會擲出<code>DataAccessException<code>
	 * @param entity
	 */
	public void save(T entity);

	/**
	 * 將entity存入資料庫中，如果entity己經是存在資料庫中了，就會執行SQL Update的動作，
	 * 如果entity 是新的，就會執行SQL Insert的動作.
	 * saveOrUpdate 的實作中，判斷是不是己存在資料庫的條件是透過查詢ID(Primary Key)
	 * @param entity 要新增或更新的Entity物件.
	 */
	public void saveOrUpdate(T entity);

	public void setSessionFactory(SessionFactory sessionFactory);
	
	//20081106 added by garywu
	public SessionFactory getSessionFactory();

	/**
	 * 在使用dao前，必須先設定entiry的class type.
	 * @param supportsClass entiry的class type
	 */
	public void setSupportsClass(Class<T> supportsClass);

	/**
	 * 將entity存入資料庫中.如果entity己經"不"存在資料庫中，會擲出<code>DataAccessException<code>
	 * @param entity
	 */
	public void update(T entity);
}
