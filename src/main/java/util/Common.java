package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;

import ds.common.services.CRUDService;
import util.info.SystemInfo;

public class Common {
	/** 佈署資料庫連線 */
	// 控制廠別
	static public String USED_DATABASE;
	final static public String DATABASE_FVEHR02 = "FVEHR02";
	final static public String DATABASE_DEVDB = "DEVDB";

	// case future....
	final static public String OS_CONTROL = System.getProperty("os.name");
	final static public String OS_MODE = "Windows";
	final static public String OS_WINDOWS_OUTPUT = "c:\\DS\\report\\output\\";
	final static public String OS_WINDOWS_TEMP = "c:\\DS\\report\\temp\\";
	final static public String OS_LINUX_OUTPUT = "/web/tomcat/temp/report/output/";
	final static public String OS_LINUX_TEMP = "/web/tomcat/temp/report/temp/";
	final static public String DATA_MODE_NEW = "DATA_MODE_NEW";
	final static public String DATA_MODE_QUERY = "DATA_MODE_QUERY";
	final static public String DATA_MODE_EDIT = "DATA_MODE_EDIT";
	final static public int LISTBOX_TOTAL_1000 = 1000;
	final static public int LISTBOX_TOTAL_500 = 500;
	final static public String LINE_SEPARATOR = System.getProperty(System.lineSeparator());
	final static public int PAGE_SIZE = 5;
	final static public String LOGININFO = "logininfo";
	final static public String CONNECTIONNAME = "dataSource_VM";
	final static public int QueryMaxRecord = 1000;
	final static public String CompanyID = "VM";
	final static public String UserID = "UserID";
	final static public String UserName = "FTLIT";
	final static public String UserEmail = "";
	static public String GETDBIP;
	public static String DBUser = "";
	public static String DBPass = "";

	final static public String[] langs = { "", "English", "Vietnam", "Traditional Chinese", "Simplied Chinese",
			"Indonesia" };
	final static public String[] lOCALE_ALL_LANGUAGES = { "", "en", "vi", "zh", "zh", "in" };
	final static public String[] lOCALE_ALL_COUNTRYS = { "", "", "VN", "TW", "CN", "ID" };
	final static public HashMap<String, String> maplangs = new HashMap<String, String>(){{
		  put("", "Traditional Chines");
		  put("en", "English");
		  put("vn", "Vietnam");
		  put("tw", "Traditional Chinese");
		  put("cn", "Simplied Chinese");
		  put("in", "Indonesia");
		 }};
		 
	public static String[] DBConnInfo = { "", "", "" };

	final static public String ZK_LAST_LOCALE = "ZK_LAST_LOCALE";

	public static ListModelList<String> createSelectboxModel(String[] datas) {
		List<String> list = Arrays.asList(datas);
		ListModelList<String> model = new ListModelList<String>(list);
		return model;
	}

	public static void setZkWebLocale(String language, String country) {
		Locale locale = new Locale(language, country);
		Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, locale);
	}

	public static String getCookieValue(String name) {
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getCookies();
		if (cookies != null) {
			for (int j = cookies.length; --j >= 0;) {
				if (cookies[j].getName().equals(name)) {
					String val = cookies[j].getValue();
					return val;
				}
			}
			return null;
		}
		return null;
	}

	public static void addCookie(String name, String value) {
		HttpServletResponse response = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
		Cookie cookie = new Cookie(name, value);
		response.addCookie(cookie);
	}

	// 建立驗證物件 (與要驗證哪一個資料物件無關)
	final static Validator _validator = Validation.buildDefaultValidatorFactory().getValidator();

	public static Validator getValidator() {
		return _validator;
	}

	public static <D> StringBuilder doValidate(D d) {
		StringBuilder sbViolations = new StringBuilder();
		// 取得某一個資料物件，所違反的全部限制條件
		Set<ConstraintViolation<D>> violations = _validator.validate(d);
		// 準備要顯示給使用者的訊息
		// v.getRootBeanClass() : 類別完整名稱
		// v.getPropertyPath() : 類別屬性(違反限制規則的欄位)
		// v.getInvalidValue() : 違反限制規則的值
		// message = "Name is required, maximum 255 characters."
		for (ConstraintViolation<D> v : violations) {
			sbViolations.append(Common.LINE_SEPARATOR + " *** " + v.getRootBeanClass() + "." + v.getPropertyPath() + "="
					+ v.getInvalidValue() + " <<" + v.getMessage() + ">>");
		}
		return sbViolations;
	}

//	private static EntityManagerFactory entityManagerFactory;
//
//	// 取得連線Persistence URL
//	static {
//		try {
//			SystemInfo SystemInfo = (SystemInfo) SpringUtil.getBean("systemInfo");
//			String[] Companies = new String[SystemInfo.getCompanies().size()];
//			HashMap<Integer, String> mapCRUDService = new HashMap<>();
//			for (int i = 0; i < SystemInfo.getCompanies().size(); i++) {
//				Companies[i] = SystemInfo.getCompanies().get(i).getCompanyID();
//				mapCRUDService.put(i, SystemInfo.getCompanies().get(i).getConnectionName());
//			}
//
//			//啟動時預設以applicationContext.xml, systemInfo company01第一個為連線DB
//			CRUDService CRUDService = (CRUDService) SpringUtil.getBean(mapCRUDService.get(0));
//			entityManagerFactory = CRUDService.getEmf();
//			String getURL = CRUDService.getEmf().getProperties().get("hibernate.connection.url").toString();
//			String[] arrURL = getURL.split(":");
//			USED_DATABASE = arrURL[5];	// DB name
//			GETDBIP = arrURL[3].substring(1); // ip位置
//			DBConnInfo[0] = GETDBIP; // ip位置
//			DBConnInfo[1] = arrURL[4]; // port
//			DBConnInfo[2] = USED_DATABASE; // DB name
//		} catch (Throwable ex) {
//			throw new ExceptionInInitializerError(ex);
//		}
//	}
//
//	public static EntityManagerFactory getEntityManagerFactory() {
//		return entityManagerFactory;
//	}

//	public static void shutdown() {
//		getEntityManagerFactory().close();
//	}

	// 控制資料庫連線
	static public Connection getDbConnection() {
		//johnny 20171106 抓取Persistence屬性
		CRUDService CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		SessionFactory sessionFactory = ((HibernateEntityManagerFactory) CRUDService.getEmf()).getSessionFactory();
		Properties properties = ((SessionFactoryImpl) sessionFactory).getProperties();
		String driverClass = (String) properties.get("hibernate.connection.driver_class");
		String url = (String) properties.get("hibernate.connection.url");
		String userName = (String) properties.get("hibernate.connection.username");
		String passWord = (String) properties.get("hibernate.connection.password");

		String[] getURL = url.split(":"); // 使用:分隔
		DBConnInfo[0] = getURL[3].substring(1); // ip位置
		DBConnInfo[1] = getURL[4]; // port
		DBConnInfo[2] = getURL[5]; // DB name

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(passWord);		
		dataSource.setMaxIdle(1);	//最大空閒連接  
        dataSource.setMaxActive(1); //最大活動連接 
        dataSource.setTimeBetweenEvictionRunsMillis(5000);	//定時對線程池中的鏈接進行validateObject校驗，對無效的鏈接進行關閉後，會調用ensureMinIdle，適當建立鏈接保證最小的minIdle連接數。
        dataSource.setMinEvictableIdleTimeMillis(5000);	//連接池中連接，在時間段內一直空閒，被逐出連接池的時間
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// MySQL
	static public Connection getDbMySQLConnection(String name) {
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		if (sc.getInitParameter(name) != null) {
			String[] connInfo = sc.getInitParameter(name).split(";");
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://" + connInfo[0] + ":3306/" + connInfo[1] + "?user=" + connInfo[2]
					+ "&password=" + connInfo[3]);

			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// SQLServer
	static public Connection getDbMSSQLConnection(String name) {
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		if (sc.getInitParameter(name) != null) {
			String[] connInfo = sc.getInitParameter(name).split(";");
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			dataSource.setUrl("jdbc:sqlserver://" + connInfo[0] + ":1433;" + "databaseName=" + connInfo[1] + ";user="
					+ connInfo[2] + ";password=" + connInfo[3]);
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// VM 資料庫
	static public Connection getMySQLDbConnection_mysql() {
		return getMySQLDbConnection("10.8.1.121", "shdhv1", "dsit", "dsit");
	}

	// MySQL 資料庫
	public static Connection getMySQLDbConnection(String ip, String dbId, String username, String password) {
		// ip 10.8.1.121 dbid shdhv1 dsit
		Connection conn = null;
		try {
			// 連接MySQL
			Class.forName("com.mysql.jdbc.Driver");
			// 建立讀取資料庫 (test 為資料庫名稱; user 為MySQL使用者名稱; passwrod 為MySQL使用者密碼)
			String datasource = "jdbc:mysql://" + ip + ":3306/" + dbId + "?user=" + username + "&password=" + password;
			// 以下的資料庫操作請參考本blog中: "使用 Java 連結與存取 access 資料庫 (JDBC)"
			conn = DriverManager.getConnection(datasource);
			System.out.println("連接成功MySQL");
		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	static public Connection getOraDbConnection(String ip, String dbId, String username, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@" + ip + ":1521:" + dbId);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// SQL Server
	public static Connection getMssqlConnection(String ip, String dbId, String username, String password) {
		// 設定jdbc連結字串，請依你的SQL Server設定值修改
		String conUrl = "jdbc:sqlserver://" + ip + ":1433;" + "databaseName=" + dbId + ";user=" + username
				+ ";password=" + password + ";";
		Connection conn = null;
		try {
			// 註冊JODBC類
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(conUrl);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

	static public boolean closeConnection(Connection conn) {
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String getValue(Object o) {
		if (o == null)
			return "";
		if (o instanceof String) {
			return (String) o;
		} else if (o instanceof Clob) {
			try {
				Clob c = (Clob) o;
				return c.getSubString(1, (int) c.length());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return o.toString();
	}

	public static Double getDouble(Object o) {
		if (o == null)
			return 0.0;
		if (o.toString().equals(""))
			return 0.0;
		return Double.valueOf(o.toString());
	}

	public static Integer getInteger(Object o) {
		if (o == null)
			return 0;
		if (o.toString().equals(""))
			return 0;
		return Integer.valueOf(o.toString());
	}

	// COMMON METHOD ****************************************** 2015/07
	public static Double string2Value(String value) {
		return ("".equalsIgnoreCase(value)) ? (null) : (Double.valueOf(value));
	}

	public static Date string2Date(String sDate) {
		if ("".equalsIgnoreCase(sDate))
			return null;
		// 利用 DateFormat 來parse 日期的字串
		DateFormat df = DateFormat.getDateInstance();
		java.util.Date date = null;
		try {
			date = df.parse(sDate);// "2009/1/1"
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String utilDate2String(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String sDate = df.format(date);
		return sDate;
	}

	public static java.sql.Date now_sqlDate() {
		java.util.Date now = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(now.getTime());
		return sqlDate;
	}

	public static java.sql.Date utilDaye2sqlDate(java.util.Date util_date) {
		return new java.sql.Date(util_date.getTime());
	}

	public static java.sql.Date stringDate_utilDaye2sqlDate(String stringDate, String deli1, String deli2) {
		java.util.Date util_date = null;
		try {
			stringDate = stringDate.replace(deli1, deli2);
			util_date = DateFormat.getDateInstance().parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return new java.sql.Date(util_date.getTime());
	}

	public static String DBDate2UIComponent(String stringDate, String format, String deli1, String deli2) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df.parse(stringDate.replace(deli1, deli2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sdata = df.format(date);

		return sdata.replace(deli2, deli1);
	}

	public static int diff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int diffDay = 0;
		if (c1.before(c2)) {
			diffDay = countDiffDay(c1, c2);
		} else {
			diffDay = countDiffDay(c2, c1);
		}
		return diffDay;
	}

	public static int countDiffDay(Calendar c1, Calendar c2) {
		int returnInt = 0;
		while (!c1.after(c2)) {
			c1.add(Calendar.DAY_OF_MONTH, 1);
			returnInt++;
		}
		if (returnInt > 0) {
			returnInt = returnInt - 1;
		}
		return (returnInt);
	}

	// 下SQL直接存入資料庫
	@SuppressWarnings("rawtypes")
	public static boolean executeSQLStatement(String sql, List params) throws Exception {
		Connection conn = null;
		boolean ok = false;
		try {
			conn = Common.getDbConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i + 1, params.get(i));
				}
			}
			int count = ps.executeUpdate();
			ok = count > 0 ? true : false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		} finally {
			Common.closeConnection(conn);
		}
		return ok;
	}

	public static ByteArrayOutputStream byteToStream(byte[] pByte) {
		if (pByte == null)
			return null;
		ByteArrayOutputStream bReturn = new ByteArrayOutputStream(pByte.length);
		bReturn.write(pByte, 0, pByte.length);
		return bReturn;
	}

	/**
	 * 把Blob类型转换为byte数组类型
	 * 
	 * @param Object
	 * @return
	 */
	public static byte[] blobToBytes(Object pObject) {
		if (pObject == null)
			return null;
		BufferedInputStream is = null;
		Blob blob = (Blob) pObject;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;
			while (offset < len && (read = is.read(bytes, offset, len)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}
	/**
	 * 2018-8-23 添加隆發廠DB01 連接
	 */
	public static Connection getDB01Conn() {

		Connection conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.8.1.32:1521:ftldb1";
		String user = "DSOD";
		String pwd = "ora@it2013";
		try {
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			System.err.println(">>>鏈接DB01數據庫");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
