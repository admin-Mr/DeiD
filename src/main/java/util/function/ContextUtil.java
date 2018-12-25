/**
 * Project:     Web應用系統開發平台專案
 * Filename:    ContextUtil.java
 * Author:      
 * Create Date: 
 * Version:     
 * Description: 
 */
package util.function;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import util.dao.IGenericDAO;
import util.dao.MultiSessionFactory;
import util.info.CompanyInfo;
import util.info.SystemInfo;
import util.Common;


public class ContextUtil {
    private static final Logger logger          = Logger.getLogger(ContextUtil.class);
    
    private static ClassPathXmlApplicationContext  context;
//    static {
        String[] paths = new String[] { "/conf/applicationContext.xml", };
//        context = new ClassPathXmlApplicationContext(paths);
//    }
    
    public static CompanyInfo[] getCompanies() {
        SystemInfo systemInfo = (SystemInfo) context.getBean("systemInfo");
        return systemInfo.getCompanies().toArray(new CompanyInfo[systemInfo.getCompanies().size()]);
    }

    public static String getReportOutputFolder() {//tiger
//      SystemInfo systemInfo = (SystemInfo) context.getBean("systemInfo");
//      return systemInfo.getReportOutputFolder();
  	if (Common.OS_CONTROL.contains(Common.OS_MODE)){
  		return Common.OS_WINDOWS_OUTPUT; //tiger
  	}else {
  		return Common.OS_LINUX_OUTPUT;
  	}

  }

  public static String getReportTempFolder() {//tiger
//      SystemInfo systemInfo = (SystemInfo) context.getBean("systemInfo");
//      return systemInfo.getReportTempFolder();
  	if (Common.OS_CONTROL.contains(Common.OS_MODE)){
  		return Common.OS_WINDOWS_TEMP;
  	}else{
  		return Common.OS_LINUX_TEMP;
  	}
  }
//  tigereye..20100406
    public static String getEncryFolder() {
        SystemInfo systemInfo = (SystemInfo) context.getBean("systemInfo");
        return systemInfo.getEncryFolder();
    }
    
	public static Map<String, String> getOptionalParams() {
		SystemInfo systemInfo = (SystemInfo) context.getBean("systemInfo");
		return systemInfo.getOptionalParams();
	}
    /**
     * 存取各公司別資料庫來源的資料存取物件
     * @param companyID 公司ID
     * @param domainClass value object class
     * @return 資料存取物件
     */
    @SuppressWarnings("rawtypes")
	public static IGenericDAO getDao(String companyID) {
    	//20081106 modified by garywu for issue #0005901
        //switchDatabase(companyID);
		CompanyInfo companyInfo = findCompanyById(companyID);
		//20081112 added by garywu fo rissue #0005901
		return getDaoByDSName(companyInfo.getConnectionName());
    }
    /**
     * 存取各公司別資料庫來源的資料存取物件
     * @param companyID 公司ID
     * @param domainClass value object class
     * @return 資料存取物件
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static IGenericDAO getDao(String companyID, Class domainClass) {
    	//20080611 modified by garywu for issue #0005901
        //switchDatabase(companyID);
        IGenericDAO dao = getDao(companyID);
        dao.setSupportsClass(domainClass);
        return dao;
    }
    

    /**
     * 結束connection，提供通用的close connection的目的是為了除錯，而且，如果關閉connecton
     * 的方式目前是用close，但也有可能會有其他的方式來close，所以統一管理會比較容易修改.
     * @param conn 要結束的connection.
     * @see dsc.echo2app.Application
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            logger.error("close connection fail!", e);
        }
    }
    /**
     * @see dsc.echo2app.Application
     */
    public static CompanyInfo findCompanyById(String companyID) {
        SystemInfo sysInfo = (SystemInfo) context.getBean("systemInfo");
        List<CompanyInfo> companies = sysInfo.getCompanies();
        CompanyInfo result = null;
        for (CompanyInfo each : companies) {
            if (StringUtils.equals(companyID, each.getCompanyID())) {
                return each;
            }
        }
        return result;
    }

    /**
     * 從connection pool取得connection
     * @return connection
     * @see dsc.echo2app.Application
     */
    public static Connection getConnection(String companyID) {
        CompanyInfo companyInfo = findCompanyById(companyID);;
        DataSource ds = (DataSource) context.getBean(companyInfo.getConnectionName());
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            logger.error("get connection fail!", e);
            return null;
        }
    }
    
	public static Connection getConnectionByDSName(String dataSourceName) {
		DataSource ds = (DataSource) context.getBean(dataSourceName);
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			logger.error("get connection fail!", e);
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static synchronized IGenericDAO getDaoByDSName(String dataSourceName) {
		assert context != null : "spring context is null";
		assert StringUtils.isNotBlank(dataSourceName) : "data source name is null";
		
		//20081104 modified by garywu, issue #0005901
		//HotSwappableTargetSource hsts = (HotSwappableTargetSource) context.getBean("swappingDataSourceTargetSource");
		//Object dataSourceLocal = context.getBean(dataSourceName);
		//hsts.swap(dataSourceLocal);

		//20081112 added by garywu
		MultiSessionFactory multiSessionFactory = (MultiSessionFactory)context.getBean("&sessionFactory");
        multiSessionFactory.setDataSource((DataSource)context.getBean(dataSourceName));
		IGenericDAO result = (IGenericDAO) context.getBean("dao");
		return result;
	}
	//20080106 modified by garywu for issue #0005901
	@SuppressWarnings("rawtypes")
	public static synchronized PlatformTransactionManager getTransactionManager(IGenericDAO dao) {
		assert context != null : "spring context is null";
		PlatformTransactionManager ptm = (PlatformTransactionManager)context.getBean("transactionManager");
		if (ptm instanceof HibernateTransactionManager) {
			HibernateTransactionManager htm = (HibernateTransactionManager)ptm;
			htm.setSessionFactory(dao.getSessionFactory());
		}
		return ptm;
	}
	
	//tigereye...20100128
	public static PlatformTransactionManager getTransactionManager() {
		assert context != null : "spring context is null";
		
		return (PlatformTransactionManager)context.getBean("transactionManager");
	}
	
    /**
     * 切換資料庫來源.
     * @see dsc.echo2app.Application
     */
	//20081106 commented by garywu
//    public static void switchDatabase(String companyID) {
//        assert StringUtils.isNotBlank(companyID) : "company id of login info is null";
//        CompanyInfo companyInfo = findCompanyById(companyID);
//        //20081104 modified by garywu, fix issue #0005901
//        //HotSwappableTargetSource hsts = (HotSwappableTargetSource) context.getBean("swappingDataSourceTargetSource");
//        HotSwappableTargetSource hsts = (HotSwappableTargetSource) context.getBean("swappingSessionFactoryTargetSource");
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("using database [" + companyInfo.getConnectionName()
//                    + "]");
//        }
//        //20081104 modified by garywu, fix issue #0005901
//        //Object dataSourceLocal = context.getBean(companyInfo.getConnectionName());
//        //hsts.swap(dataSourceLocal);
//		Map map = (Map)context.getBean("sessionFactories");
//		Object sessionFactory = map.get(companyInfo.getConnectionName());
//		hsts.swap(sessionFactory);
//    }
}
