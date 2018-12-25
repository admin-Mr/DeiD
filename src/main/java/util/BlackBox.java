package util;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.zkoss.json.JSONObject;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import bitronix.tm.BitronixTransactionManager;
import ds.common.services.CRUDService;
import ds.common.services.ProgramAuth;
import ds.common.services.UserCredential;
import ds.rethinkdb.PushNoticeThreed;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import util.component.dscQueryField;

/** 共同程式集
 * 寫入entity對應之欄位資料 > propertyWriteMethod(Object entity, String columnName, Object value)
 * 讀取entity對應之欄位資料 > propertyReadMethod(Object entity, String columnName)
 * 設定Combobox對應之項目 > setComboboxValue(Combobox cboObject, String strValue)
 * 取得操作權限 > getPRGAuth(UserCredential pUserInfo, String pPRGName)
 * 取得資料庫的資料所對應的Value(外顯) > getItemLabel(String sKeyColumn, String sLabelColumn, String sTableName, String sCondition)
 * 自動設定控件對應Listheader長度，使用InLinde Edit時使用 > setComponentWidth(Listbox listbox, Listitem item, String name, int iCust)
 * 回傳該欄位所對應Listbox欄位之componet > getComponent(Listbox listbox, Listitem item, String name)
 * 共用匯入combobox method > setcbobobox(Combobox Combobox, String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby)
 * 共用匯入combobox method 多語系 > setcbobobox(Combobox Combobox, String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby, boolean bLocal)		
 * 可取得List<temp> 可自訂控制何時才需匯入combobox > getListModelList(String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby)
 * 透過listheader排序 > getOrderBy(Event event, String sOrderBy, ArrayList<String> list)
 * 判斷新舊值是否不同 > getDiffValue(Object obj, Component cp)
 * 檢查之欄位不能為空之訊息 > getCheckMsg(String field)
 * String/Clob 轉string > getValue(Object o)
 * 將Clob轉成String類型 > ClobToString(Clob sc)
 * 使用poi匯出excel > ExcelExport(String sSql, String sFileName, String sTitle, String[] arrHead)
 * POI設定框線 > setStandStyle(HSSFCellStyle style)
 * Rollback Connection > connectionRollback(Connection conn)
 * Close Connection > connectionClose(Connection conn)
 * 關閉Connection及相關Statement或ResultSet > closeQuietly(Connection conn, Object objstmt, ResultSet rs)
 * 關閉Connection/Statement/PreparedStatement/NameParameterStatement/ResultSet > close(Object obj)
 * 取得登入語系 > getLocale()
 * iReport匯出Excel > ExporterXls(String siReportPath, String sfileName, String[] sSheetNames, HashMap<String, Object> parameters)
 * iReport匯出Excel > ExporterXls(String siReportPath, String sfileName, String[] sSheetNames, HashMap<String, Object> parameters, Connection conn)
 * iReport匯出Excel multiple sheet > ExporterXls(List<String> list, String sfileName, String[] sSheetNames, List<HashMap<String, Object>> parameters)
 * iReport匯出Excel multiple sheet > ExporterXls(List<String> list, String sfileName, String[] sSheetNames, List<HashMap<String, Object>> parameters, Connection conn)
 * iReport匯出PDF > ExporterPdf(String siReportPath, String sfileName, HashMap<String, Object> parameters)
 * iReport匯出PDF > ExporterPdf(String siReportPath, String sfileName, HashMap<String, Object> parameters, Connection conn)
 * Object轉Double > Double getDouble(Object o)
 * Object[]轉String[] > objectArray2StringArray(Object[] objects)
 * Object轉Integer > getInteger(Object o)
 * 取得HSSFCell Value > getCellValue(HSSFCell cell)
 * 字串轉Double > string2Value(String value)
 * 字串轉日期 > string2Date(String sDate)
 * 日期轉字串，格式yyyy/MM/dd > utilDate2String(java.util.Date date)
 * 取得現在日期 > now_sqlDate()
 * java.util.Date轉換java.sql.Date > utilDaye2sqlDate(java.util.Date util_date)
 * 將字串中deli1取代為deli2，並轉為java.sql.Date > stringDate_utilDaye2sqlDate(String stringDate, String deli1, String deli2)
 * 代換日期 > DBDate2UIComponent(String stringDate, String format, String deli1, String deli2)
 * 執行SQL語法 > executeSQLStatement(String sql, List params)
 * 批次執行SQL語法 > executeSQLStatement(Connection conn, String sql, List params)
 * 取得Client IP > getClientIp()
 * 取得國家 > getCountry()
 * 計算兩日期差 > differenceOfTwo(Calendar befor, Calendar after, String defferencyType)
 * 比較二個日期的天數差 > daysOfTwo(Calendar befor, Calendar after)
 * 設定主次檔按鈕，支援非系統設定之按鈕  > setButtonAuth(Object objProgram, boolean bDisable)
 * 設定其他Detail按鈕為Disable > setOtherDetailButton(Object objMasterProgram, Detail detailSelf)
 * 資料定位 > setPosition(Object objProgram, Object entity)
 * 檢查domain object是否有錯誤 > validate(Object objSel)
 * 檢查pk是否重覆 > CheckPK(Object objProgram, Object objSel)
 * 將Component Value寫入entity > writeMethod(Object objProgram, String className, Object objSel, List<ComponentColumn> comColumns)
 * 底層存檔功能 > executeSave(Object objProgram, Object objSel, Connection conn)
 * 底層檢查Component > checkComponent(Object objProgram)
 * 底層設定entity ENTITY_CLASSNAME and ENTITY_PATH > setEntityClassPath(Object objProgram)
 * 重新載入entity 延遲加載 > entityRefresh(Object objDomain, Object objJoin)
 * 顯示或隱藏Listbox之操作圖示(修改/刪除) > setListboxImageAuth(DataMode dataMode, Listbox listbox, ProgramAuth prgAuth)
 * Set In Line Edit > setInLineListbox(Object objProgram, Object objSel, boolean bEdit)
 * 選定資料 > programmacticallySelectListbox(Object objProgram, int index)
 * Listbox Listheader排序功能 > orderByListheader(Object objProgram, Event event)
 * 吐司訊息Show > showToast(Div toast, String message, long time)
 * 吐司訊息Stop > stopToast(Div toast, Div div, long microsecond)
 * 取得dspb02資訊 > getDSPB02Info(String column)
 * Listbox多選功能，點擊listitem觸發 > listboxMulitSel(Object objProgram, Event evt)
 * get Object Class Logger > getLogger(Object obj)
 * 比對物件 > List<ArrayList<Object>> getDiffField(Object objA, Object objB)
 * 存檔前檢查 > beforeSave(Object objProgram, Object entity)
 * 刪除功能 > doDelete(Object objProgram, Object objDelete, Connection conn)
 * InputStream 轉字串 > convertRequestBODY2String(InputStream requestBodyStream)
 * 呼叫Web API 回傳值 > getAPIResult(String pAPIName, JSONObject pJson, String sServletContext)
 * 取得entity Id > ArrayList<String> getKeyName(Class entityClass)
 * 取得key值資料 > ArrayList<String> getKeyValue(Object objProgram, Object entity)
 * 取得多語系table之語系欄位 > getLocalField(Object entity, String sColumnName)
 * 計算天數差 > countDiffDay(Calendar c1, Calendar c2)
 * 關閉物件 > closeNps(Object... object)
 * 取得Connenction from EntityManagerFactory > getDbConnection(EntityManagerFactory emf)
 * 取得目前Session裡面的CRUDService > getCRUDService()
 * 取得登入中資料庫的Connection > getDbConnection(String sCRUDService)
 */

public class BlackBox {
	protected static Logger logger = getLogger(BlackBox.class);
	
	final static UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	public static HashMap<String, Object> hmItemLabel = new HashMap<String, Object>();
	public static PushNoticeThreed ptToDoList;
	public static PushNoticeThreed ptNotice;
	
	static {
		try {
			ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
			if (sc.getInitParameter("RethinkDB") != null) {
				String[] rethinkDB = sc.getInitParameter("RethinkDB").split(";");

				ptToDoList = new PushNoticeThreed();
				ptNotice = new PushNoticeThreed();
				
				ptToDoList.setRethinkDbinfo(rethinkDB[0], rethinkDB[1], rethinkDB[2], rethinkDB[3], rethinkDB[4], rethinkDB[5]);
				ptNotice.setRethinkDbinfo(rethinkDB[0], rethinkDB[1], rethinkDB[2], rethinkDB[3], rethinkDB[4], rethinkDB[6]);

				Thread threadToDoList = new Thread(ptToDoList);
				Thread threadptNotice = new Thread(ptNotice);
				threadToDoList.start();
				threadptNotice.start();
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
	
	/**
	 * 寫入entity對應之欄位資料
	 * @param entity 資料物件
	 * @param columnName 欄位名稱
	 * @param value 資料
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void propertyWriteMethod(Object entity, String columnName, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entity.getClass(), columnName);
		Method mWrite = pd.getWriteMethod();
		mWrite.invoke(entity, value);
	}

	/**
	 * 讀取entity對應之欄位資料
	 * @param entity 資料物件
	 * @param columnName 欄位名稱
	 * @return 資料
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object propertyReadMethod(Object entity, String columnName)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entity.getClass(), columnName);
		Method mWrite = pd.getReadMethod();
		return mWrite.invoke(entity);
	}

	@SuppressWarnings("unused")
	public static int idxListbox(ArrayList<Object[]> arrSource, ArrayList<String> arrCheck) {
		int iFields = arrCheck.size();
		int iFindCount = 0;
		int iFindIndex = -1;
		String[] arrFields;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			int iLen = arrSource.get(0).length;
		} catch (Exception ex) {
			arrFields = arrSource.toArray(new String[arrSource.size()]);
			for (int j = 0; j < arrFields.length; j++) {
				// iFindIndex = Arrays.binarySearch(arrFields, arrCheck.get(j));
				// if (iFindIndex > -1 )
				try {
					Date date = sdf.parse(arrFields[j]);
					if (arrFields[j].substring(0, 10).replace("/", "-").equals(arrCheck.get(0).replace("/", "-")))
						iFindCount++;
				} catch (ParseException e) {
					if (arrFields[j].equals(arrCheck.get(0).toString()))
						iFindCount++;
				}
				if (iFindCount == iFields)
					return j;
			}
			return -1;
		}

		for (int i = 0; i < arrSource.size(); i++) {
			try {
				arrFields = Arrays.asList(arrSource.get(i)).toArray(new String[arrSource.get(i).length]);
			} catch (Exception e) {
				ArrayList<String[]> strSource = new ArrayList<String[]>();
				for (Object[] obj : arrSource) {
					String[] arrStr = new String[obj.length];
					for (int j = 0; j < obj.length; j++) {
						arrStr[j] = String.valueOf(obj[j]);
					}
					strSource.add(arrStr);
				}
				arrFields = Arrays.asList(strSource.get(i)).toArray(new String[strSource.get(i).length]);
			}
			iFindCount = 0;
			for (int j = 0; j < iFields; j++) {
				// iFindIndex = Arrays.binarySearch(arrFields,
				// arrCheck.get(j).toString());
				// if (iFindIndex > -1)
				try {
					Date date = sdf.parse(arrFields[j]);
					if (arrFields[j].substring(0, 10).replace("/", "-").equals(arrCheck.get(j).replace("/", "-")))
						iFindCount++;
				} catch (ParseException e) {
					if (arrFields[j].equals(arrCheck.get(j).toString()))
						iFindCount++;
				}
				if (iFindCount == iFields)
					return i;
			}
		}
		return -1;
	}

	/**
	 * 設定Combobox對應之項目
	 * @param cboObject
	 * @param strValue
	 * @return
	 */
	public static Comboitem setComboboxValue(Combobox cboObject, String strValue) {
		cboObject.setSelectedItem(null);
		for (Comboitem item : cboObject.getItems()) {
			if (item.getValue() == null || item.getValue().equals(strValue)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 取得操作權限
	 * @param pUserInfo 登入者資訊
	 * @param pPRGName 程式名稱
	 * @return
	 */
	public static ProgramAuth getPRGAuth(UserCredential pUserInfo, String pPRGName) {
		String strAuthQuery = "N", strAuthInsert = "N", strAuthEdit = "N", strAuthDelete = "N";
		String strAuthPrint = "N", strAuthExport = "N", strAuthEmail = "N";
		for (String auth : pUserInfo.getAuth()) {
			if (auth.indexOf(pPRGName) > -1) {
				String[] arr = auth.split(",");
				strAuthInsert = arr[1]; // PB_RH01
				strAuthQuery = arr[2]; // PB_RH02
				strAuthDelete = arr[3]; // PB_RH03
				strAuthEdit = arr[4]; // PB_RH04
				strAuthEmail = arr[5]; // PB_RH08
				strAuthPrint = arr[6]; // PB_RH09
				strAuthExport = arr[7]; // PB_RH10
				break;
			}
		}
		return new ProgramAuth(pUserInfo.getAccount(), pPRGName, strAuthQuery, strAuthInsert, strAuthEdit,
				strAuthDelete, strAuthPrint, strAuthExport, strAuthEmail);
	}

	/**********************************************************************************************************************************/
	/**
	 * 取得資料庫的資料所對應的Value(外顯)
	 * @param sKeyColumn 內存值
	 * @param sLabelColumn 外顯值
	 * @param sTableName table name
	 * @param sCondition SQL條件
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> getItemLabel(String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition) {
		String sSql = "select " + sKeyColumn + " as KEY, " + sLabelColumn + " as VALUE from " + sTableName
				+ " where 1=1 " + sCondition;

		Query qry = getCRUDService().createSQL(sSql);
		for (Iterator data = qry.getResultList().iterator(); data.hasNext();) {
			Object[] obj = (Object[]) data.next();
			hmItemLabel.put((String) obj[0], obj[1]);
		}
		return hmItemLabel;
	}

	/**
	 * 自動設定控件對應Listheader長度，使用InLinde Edit時使用
	 * @param listbox 目標Listbox
	 * @param item 選擇之item
	 * @param name 需手動調整之欄位名稱
	 * @param iCust 自訂需增減之長度
	 */
	public static void setComponentWidth(Listbox listbox, Listitem item, String name, int iCust) {
		Component cpReturn = null;
		for (Object child : listbox.getListhead().getChildren()) {
			Listheader hd = (Listheader) child;
			int i = hd.getId().indexOf("lh");
			String sWidth = "";

			if (i != -1) {
				if (name != null && !name.equals("")) {
					if (hd.getId().substring(i, hd.getId().length()).equals("lh" + name)) {
						cpReturn = item.getChildren().get(hd.getColumnIndex()).getFirstChild();
					}
				} else {
					cpReturn = item.getChildren().get(hd.getColumnIndex()).getFirstChild();
				}

				if (cpReturn != null) {
					if (hd.getWidth() != null) {
						int j = hd.getWidth().indexOf("px");
						if (j > 0) {
							int iWidth = Integer.valueOf(hd.getWidth().substring(0, j));
							sWidth = String.valueOf(iWidth - 10 + iCust) + "px";
						} else {
							sWidth = hd.getWidth();
						}

						if (cpReturn instanceof Textbox) {
							((Textbox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Combobox) {
							((Combobox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Doublebox) {
							((Doublebox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Intbox) {
							((Intbox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Datebox) {
							((Datebox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Decimalbox) {
							((Decimalbox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Longbox) {
							((Longbox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Bandbox) {
							((Bandbox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Timebox) {
							((Timebox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Selectbox) {
							((Selectbox) cpReturn).setWidth(sWidth);
						} else if (cpReturn instanceof Colorbox) {
							((Colorbox) cpReturn).setWidth(sWidth);
						}
						if (name != null) {
							break;
						}
					}
				}
			}

		}
		cpReturn = null;
	}

	/**
	 * 回傳該欄位所對應Listbox欄位之componet
	 * @param listbox 為設定抓取目標Listheader之Listbox
	 * @param item 取得目前該record之item
	 * @param name Column name
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Component getComponent(Listbox listbox, Listitem item, String name) {
		Component cpReturn = null;
		for (Object child : listbox.getListhead().getChildren()) {
			Listheader hd = (Listheader) child;
			int i = hd.getId().indexOf("lh");
			String sWidth = "";
			if (i != -1) {
				if (hd.getId().substring(i, hd.getId().length()).equals("lh" + name)) {
					cpReturn = item.getChildren().get(hd.getColumnIndex()).getFirstChild();
				}
			}
		}

		return cpReturn;
	}

	public static class temp implements Serializable {
		private static final long serialVersionUID = 1L;
		private String KEY;
		private String VALUE;

		public String getKEY() {
			return KEY;
		}

		public void setKEY(String kEY) {
			KEY = kEY;
		}

		public String getVALUE() {
			return VALUE;
		}

		public void setVALUE(String vALUSE) {
			VALUE = vALUSE;
		}
	}

	/**
	 * 共用匯入combobox method
	 * @param Combobox zk Combobox
	 * @param sKeyColumn 內存之key Value
	 * @param sLabelColumn 外顯之label
	 * @param sTableName from table Name
	 * @param sCondition SQL條件
	 * @param sOrderby SQL排序
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setcbobobox(Combobox Combobox, String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby) {
		setcbobobox(Combobox, sKeyColumn, sLabelColumn, sTableName, sCondition, sOrderby, false, getCRUDService());
	}
	
	/**
	 * 共用匯入combobox method
	 * @param Combobox zk Combobox
	 * @param sKeyColumn 內存之key Value
	 * @param sLabelColumn 外顯之label
	 * @param sTableName from table Name
	 * @param sCondition SQL條件
	 * @param sOrderby SQL排序
	 * @param CRUDService CRUDService
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setcbobobox(Combobox Combobox, String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby, CRUDService CRUDService) {
		setcbobobox(Combobox, sKeyColumn, sLabelColumn, sTableName, sCondition, sOrderby, false, CRUDService);
	}
	
	/**
	 * 共用匯入combobox method 多語系
	 * @param Combobox zk Combobox
	 * @param sKeyColumn 內存之key Value
	 * @param sLabelColumn 外顯之label
	 * @param sTableName from table Name
	 * @param sCondition SQL條件
	 * @param sOrderby SQL排序
	 * @param sOrderby SQL排序
	 * @param bLocal 是否為多語系, 依據登入語系統組合欄位名稱
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setcbobobox(Combobox Combobox, String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby, boolean bLocal, CRUDService CRUDService) {
		if (bLocal) {
			sLabelColumn = sLabelColumn + (getLocale().equals("en") ? "" : "_" + getLocale().toUpperCase());
		}
		
		ListModelList lItems = new ListModelList(
				getListModelList(sKeyColumn, sLabelColumn, sTableName, sCondition, sOrderby, CRUDService), true);
		Combobox.setModel(lItems);
	}	
	
	
	/**
	 * 可取得List<temp> 可自訂控制何時才需匯入combobox
	 * @param sKeyColumn  內存之key Value
	 * @param sLabelColumn 外顯之label
	 * @param sTableName from table Name
	 * @param sCondition SQL條件
	 * @param sOrderby SQL排序
	 * @param CRUDService 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<temp> getListModelList(String sKeyColumn, String sLabelColumn, String sTableName,
			String sCondition, String sOrderby, CRUDService CRUDService) {		
		String sSql = "select " + sKeyColumn + " as KEY, " + sLabelColumn + " as VALUE from " + sTableName
				+ " where 1=1 " + sCondition;
		
		if (sOrderby != null && !sOrderby.equals("")) {
			sSql += " order by " + sOrderby;
		}

		Query qry = CRUDService.createSQL(sSql);

		List<temp> lstResult = new ArrayList<temp>();

		for (Iterator data = qry.getResultList().iterator(); data.hasNext();) {
			Object[] obj = (Object[]) data.next();
			temp rsl = new temp();
			rsl.setKEY((String) obj[0]);
			rsl.setVALUE((String) obj[1]);
			lstResult.add(rsl);
		}
		lstResult.add(null);
		return lstResult;
	}

	/**
	 * 透過listheader排序
	 * @param event 觸發之event
	 * @param sOrderBy 原排序之條件
	 * @param list domain entity key值
	 * @return
	 */
	public static String getOrderBy(Event event, String sOrderBy, ArrayList<String> list) {
		String sID = event.getTarget().getId();
		Listbox listbox = (Listbox) event.getTarget().getParent().getParent();
		String sColumn = "", sReturn = "", sUpperColumn = "";

		if (!sID.isEmpty() && listbox.getItems().size() > 0) {
			int i = sID.indexOf("lh");
			if (i != -1) {
				// +2為"lh" length
				sColumn = sID.substring(i + 2);

				try {
					if (propertyReadMethod(listbox.getItems().get(0).getValue(), sColumn) instanceof String) {
						sUpperColumn = "UPPER(t." + sColumn + ")";
					} else if (propertyReadMethod(listbox.getItems().get(0).getValue(), sColumn) instanceof Clob ||
							propertyReadMethod(listbox.getItems().get(0).getValue(), sColumn) instanceof Blob){
						sUpperColumn = "";
					} else {
						sUpperColumn = "t." + sColumn;
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(null, e);
				}

				if (!sUpperColumn.isEmpty()) {
					if (sOrderBy.equals("") || sOrderBy.indexOf(sColumn) > -1) {
						if (sOrderBy.indexOf(sUpperColumn + " DESC") > -1) {						
							sOrderBy = " ORDER BY " + sUpperColumn;
						} else {
							sOrderBy = " ORDER BY " + sUpperColumn + " DESC";
						}
					} else {
						sOrderBy = " ORDER BY " + sUpperColumn;
					}

					String skey = "";
					for (int j = 0; j < list.size(); j++) {
						skey += ", t." + list.get(j);
					}

					if (!skey.isEmpty()) {
						sReturn = sOrderBy + skey;
					}
				}
			}
		}
		return sReturn;
	}

	/**
	 * 判斷新舊值是否不同
	 * @param obj
	 * @param cp zk Component
	 * @return true:2者不同, false:2者相同
	 */
	public static Boolean getDiffValue(Object obj, Component cp) {
		String sOld = "", sNew = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		boolean bflag = true;

		if (obj == null){
			sOld = "";
		} else {
			if (obj instanceof Date) {
				sOld = sdf.format(obj);
			} else {
				sOld = String.valueOf(obj);
			}
		}

		if (cp instanceof Decimalbox) {
			sNew = String.valueOf(((Decimalbox) cp).getValue() == null ? "" : ((Decimalbox) cp).getValue());
		} else if (cp instanceof Combobox) {
			sNew = ((Combobox) cp).getSelectedItem() == null || ((Combobox) cp).getSelectedItem().getValue() == null ? "" : ((Combobox) cp).getSelectedItem().getValue();
		} else if (cp instanceof Label) {
			sNew = ((Label) cp).getValue() == null ? "" : ((Label) cp).getValue();
		} else if (cp instanceof Textbox) {
			sNew = ((Textbox) cp).getValue() == null ? "" : ((Textbox) cp).getValue();
		} else if (cp instanceof Datebox) {
			sNew = ((Datebox) cp).getValue() == null ? "" :sdf.format(((Datebox) cp).getValue());
		} else if (cp instanceof Checkbox) {
			sNew = ((Checkbox) cp).isChecked() ? "true" : "false";
		} else if (cp instanceof Intbox) {
			sNew = String.valueOf(((Intbox) cp).getValue() == null ? "" : ((Intbox) cp).getValue());
		} else if (cp instanceof Spinner) {
			sNew = String.valueOf(((Spinner) cp).getValue() == null ? "" : ((Spinner) cp).getValue());
		} else if (cp instanceof Doublebox) {
			sNew = String.valueOf(((Doublebox) cp).getValue() == null ? "" : ((Doublebox) cp).getValue());
		} else if (cp instanceof Longbox) {
			sNew = String.valueOf(((Longbox) cp).getValue() == null ? "" : ((Longbox) cp).getValue());
		} else if (cp instanceof dscQueryField) {
			sNew = String.valueOf(((dscQueryField) cp).getValue() == null ? "" : ((dscQueryField) cp).getValue());
		}

		if (sOld.equals(getValue(sNew))){
			bflag = false;
		} else {
			bflag = true;
		}

		return bflag;		
	}

	/**
	 * 檢查之欄位不能為空之訊息
	 * @param field
	 * @return 欄位[xx]不能為空
	 */
	public static List<String> getCheckMsg(String field) {
		List<String> checkMsg = new ArrayList<String>();
		checkMsg.add(Labels.getLabel(field) + Labels.getLabel("PUBLIC.MSG0003"));
		return checkMsg;
	}

	/**
	 * String/Clob 轉 String
	 * @param o
	 * @return
	 */
	public static String getValue(Object o) {
		if (o == null)
			return "";
		if (o instanceof String) {
			return (String) o;
		} else if (o instanceof Clob) {
			try {
				return ClobToString((Clob) o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return o.toString();
	}

	/**
	 * 將Clob轉成String類型
	 * @param sc
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String ClobToString(Clob sc) throws SQLException, IOException {
		String reString = "";
		Reader is = sc.getCharacterStream();
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();

		// 執行迴圈將字串全部取出付值給StringBuffer由StringBuffer轉成STRING
		while (s != null) {
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}

	/**
	 * 使用POI匯出excel
	 * @param sSql SQL語法
	 * @param sFileName 匯出之檔名
	 * @param sTitle 標題
	 * @param sArrHead 欄位名稱
	 */
	@SuppressWarnings({ "resource", "rawtypes" })
	public static void ExcelExport(String sSql, String sFileName, String sTitle, String[] sArrHead) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();

			// 標題，設定格式:粗體置中 字體14
			HSSFCellStyle styleTitle = (HSSFCellStyle) wb.createCellStyle();
			Font fontTitle = wb.createFont();
			fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontTitle.setFontHeightInPoints((short) 14); // 字體
			styleTitle.setFont(fontTitle);
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleTitle);

			// 標題，設定格式:粗體置中 字體12
			HSSFCellStyle styleHead = (HSSFCellStyle) wb.createCellStyle();
			Font fontHead = wb.createFont();
			fontHead.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontHead.setFontHeightInPoints((short) 12); // 字體
			styleHead.setFont(fontHead);
			styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleHead); // 設定Border及自動換行

			// 內容格式-文字
			HSSFCellStyle styleString = (HSSFCellStyle) wb.createCellStyle();
			styleString.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleString.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleString);

			// 內容格式-日期
			HSSFCellStyle styleDate = (HSSFCellStyle) wb.createCellStyle();
			HSSFDataFormat dateFormat = (HSSFDataFormat) wb.createDataFormat();
			styleDate.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleDate.setDataFormat(dateFormat.getFormat("YYYY/MM/DD"));
			setStandStyle(styleDate);

			// 內容格式-數字
			HSSFCellStyle styleNumber = (HSSFCellStyle) wb.createCellStyle();
			styleNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleNumber);

			// 內容格式-數字-float
			HSSFCellStyle styleFloat = (HSSFCellStyle) wb.createCellStyle();
			styleFloat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFloat.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			setStandStyle(styleFloat);

			Query qry = getCRUDService().createSQL(sSql);

			// 設定 Sheet名稱
			Sheet sh = wb.createSheet(sTitle);

			int iRow = 0;
			Row rowHead = sh.createRow(iRow);

			// 報表名稱
			Cell celltitle;
			for (int i = 0; i < sArrHead.length; i++) {
				celltitle = rowHead.createCell(i);
				celltitle.setCellStyle(styleTitle);
			}
			celltitle = rowHead.createCell(0);
			celltitle.setCellValue(sTitle);
			celltitle.setCellStyle(styleTitle);

			// 合併儲存格
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, sArrHead.length - 1));
			// 設定欄高
			rowHead.setHeightInPoints((short) 40);

			iRow++;
			rowHead = sh.createRow(iRow);

			// 標題
			for (int i = 0; i < sArrHead.length; i++) {
				Cell cell = rowHead.createCell(i);
				cell.setCellStyle(styleHead);
				cell.setCellValue(sArrHead[i]);

				int iWidth = sArrHead[i].length();

				sh.setColumnWidth(i, (short) 256 * 3 * iWidth);
				// sh.autoSizeColumn(i);
			}

			iRow++;
			Row rowCell = sh.createRow(iRow);
			int k = 0;

			// 內容
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				rowCell = sh.createRow(iRow + k);
				Object[] obj = (Object[]) i.next();
				for (int j = 0; j < sArrHead.length; j++) {
					Cell cell0 = rowCell.createCell(j);
					if (obj[j] instanceof String) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue((String) obj[j]);
					} else if (obj[j] instanceof BigDecimal) {
						cell0.setCellStyle(styleNumber);
						cell0.setCellValue(((BigDecimal) obj[j]).doubleValue());
					} else if (obj[j] instanceof Date) {
						cell0.setCellStyle(styleDate);
						cell0.setCellValue((Date) obj[j]);
					} else if (obj[j] == null) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue("");
					}
				}
				k++;
			}		
			
			FileOutputStream stream = new FileOutputStream(sFileName);			
			wb.write(stream);
			stream.flush();
			stream.close();
			
			// 檔名
			File file = new File(sFileName);
			Filedownload.save(file, "application/file");
		} catch (IOException e) {
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "File Not Found", Messagebox.OK, Messagebox.ERROR);
		}

	}

	/**
	 * POI設定框線
	 * @param style
	 */
	private static void setStandStyle(HSSFCellStyle style) {
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setWrapText(true);
	}

	/**
	 * Rollback Connection
	 * @param conn
	 * @return
	 */
	public static boolean connectionRollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Close Connection
	 * @param conn
	 * @return
	 */
	public static boolean connectionClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 關閉Connection及相關Statement或ResultSet
	 * @param conn
	 * @param objstmt 可傳入Statement, PreparedStatement, NameParameterStatement
	 * @param rs
	 * @throws Exception
	 */
	public static void closeQuietly(Connection conn, Object objstmt, ResultSet rs) throws Exception {
		try {
			close(rs);
		} finally {
			try {
				close(objstmt);        	
			} finally {
				close(conn);
			}
		}
	}	
	
	/**
	 * 關閉Connection/Statement/PreparedStatement/NameParameterStatement/ResultSet
	 * @param obj
	 * @throws Exception
	 */
	public static void close(Object obj) throws Exception {
		if (obj != null) { 	
			if (obj instanceof ResultSet) {
				((ResultSet) obj).close();
			} else if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof NameParameterStatement) {
				((NameParameterStatement) obj).close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();				
			} else if (obj instanceof BitronixTransactionManager) {
				((BitronixTransactionManager) obj).shutdown();
			}
			
			obj = null;
		}
	}	
    
	/**
	 * 取得登入語系
	 * @return
	 */
	public static String getLocale() {
		String sLanguage = "";
		Locale preferredLocale = (Locale) Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);

		if (preferredLocale != null) {
			if (preferredLocale.getLanguage() == "en" && preferredLocale.getCountry().equals(""))
				sLanguage = "en";
			if (preferredLocale.getLanguage() == "vi" && preferredLocale.getCountry().equals("VN"))
				sLanguage = "vn";
			if (preferredLocale.getLanguage() == "zh" && preferredLocale.getCountry().equals("TW"))
				sLanguage = "tw";
			if (preferredLocale.getLanguage() == "zh" && preferredLocale.getCountry().equals("CN"))
				sLanguage = "cn";
			if (preferredLocale.getLanguage() == "in" && preferredLocale.getCountry().equals("ID"))
				sLanguage = "in";
		} else {
			sLanguage = "tw";
		}
		return sLanguage;
	}
	
	/**
	 * iReport匯出Excel
	 * @param siReportPath iReport路徑
	 * @param sfileName	匯出之檔名
	 * @param sSheetNames Sheet Name
	 * @param parameters 傳入iReport之參數
	 */
	public static void ExporterXls(String siReportPath, String sfileName, String[] sSheetNames, HashMap<String, Object> parameters) {
		ExporterXls(siReportPath, sfileName, sSheetNames, parameters, Common.getDbConnection());
	}
	
	/**
	 * iReport匯出Excel
	 * @param siReportPath iReport路徑
	 * @param sfileName	匯出之檔名
	 * @param sSheetNames Sheet Name
	 * @param parameters 傳入iReport之參數
	 * @param Connection 連線
	 */
	public static void ExporterXls(String siReportPath, String sfileName, String[] sSheetNames, HashMap<String, Object> parameters, Connection conn) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Execution exe = Executions.getCurrent();
			// jasper檔案路徑
			String path = exe.getDesktop().getWebApp().getRealPath(siReportPath);

			JasperPrint jasperPrint = JasperFillManager.fillReport(path, parameters, conn);
			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			// 指定路徑
			// Exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(OutFilePath));
			// 下載自訂路徑
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			
			SimpleXlsReportConfiguration xlsExporterConfiguration = new SimpleXlsReportConfiguration();
			xlsExporterConfiguration.setOnePagePerSheet(false);
			xlsExporterConfiguration.setRemoveEmptySpaceBetweenRows(true);
			xlsExporterConfiguration.setDetectCellType(true);
			xlsExporterConfiguration.setWhitePageBackground(false);
			xlsExporterConfiguration.setSheetNames(sSheetNames);
			
			exporter.setConfiguration(xlsExporterConfiguration);
			exporter.exportReport();

			AMedia amedia = new AMedia(sfileName + ".xls", "xls", "application/file", outputStream.toByteArray());
			Filedownload.save(amedia);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * iReport匯出Excel multiple sheet
	 * @param list iReport路徑
	 * @param sfileName 匯出之檔名
	 * @param sSheetNames Sheet Name
	 * @param parameters 傳入iReport之參數
	 */
	public static void ExporterXls(List<String> list, String sfileName, String[] sSheetNames, List<HashMap<String, Object>> parameters) {
		ExporterXls(list, sfileName, sSheetNames, parameters, Common.getDbConnection());
	}	
	
	/**
	 * iReport匯出Excel multiple sheet
	 * @param list iReport路徑
	 * @param sfileName 匯出之檔名
	 * @param sSheetNames Sheet Name
	 * @param parameters 傳入iReport之參數
	 * @param Connection 連線
	 */
	public static void ExporterXls(List<String> list, String sfileName, String[] sSheetNames, List<HashMap<String, Object>> parameters, Connection conn) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Execution exe = Executions.getCurrent();
			// jasper檔案路徑
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			for (int i = 0; i < list.size(); i++) {
			    JasperPrint report = JasperFillManager.fillReport(exe.getDesktop().getWebApp().getRealPath(list.get(i)), parameters.get(i), conn);
			    jasperPrintList.add(report);
			}
			
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
			
			// 指定路徑
			// Exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(OutFilePath));
			// 下載自訂路徑
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			
			SimpleXlsReportConfiguration xlsExporterConfiguration = new SimpleXlsReportConfiguration();
			xlsExporterConfiguration.setOnePagePerSheet(false);
			xlsExporterConfiguration.setRemoveEmptySpaceBetweenRows(true);
			xlsExporterConfiguration.setDetectCellType(true);
			xlsExporterConfiguration.setWhitePageBackground(false);
			xlsExporterConfiguration.setSheetNames(sSheetNames);
			
			exporter.setConfiguration(xlsExporterConfiguration);
			exporter.exportReport();

			AMedia amedia = new AMedia(sfileName + ".xls", "xls", "application/file", outputStream.toByteArray());
			Filedownload.save(amedia);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * iReport匯出PDF
	 * @param siReportPath iReport路徑
	 * @param sfileName	匯出之檔名
	 * @param parameters 傳入iReport之參數
	 */
	@SuppressWarnings("unused")
	public static void ExporterPdf(String siReportPath, String sfileName, HashMap<String, Object> parameters) {
		ExporterPdf(siReportPath, sfileName, parameters, Common.getDbConnection());
	}

	/**
	 * iReport匯出PDF
	 * @param siReportPath iReport路徑
	 * @param sfileName	匯出之檔名
	 * @param parameters 傳入iReport之參數
	 * @param Connection 連線
	 */
	@SuppressWarnings("unused")
	public static void ExporterPdf(String siReportPath, String sfileName, HashMap<String, Object> parameters, Connection conn) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Execution exe = Executions.getCurrent();
			// jasper檔案路徑
			String path = exe.getDesktop().getWebApp().getRealPath(siReportPath);

			JasperPrint jasperPrint = JasperFillManager.fillReport(path, parameters, conn);

			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);

			exporter.exportReport();

			AMedia amedia = new AMedia(sfileName + ".pdf", "pdf", "application/pdf", outputStream.toByteArray());

			if (amedia != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("amedia", amedia);
				Executions.createComponents("/util/admin/ReportView.zul", null, map);
			} else {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "error", Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Object轉Double
	 * @param o
	 * @return
	 */
	public static Double getDouble(Object o) {
		if (o == null)
			return 0.0;
		if (o.toString().equals(""))
			return 0.0;
		return Double.valueOf(o.toString());
	}
	
	/**
	 * Object[]轉String[]
	 * @param objects
	 * @return
	 */
	public static String[] objectArray2StringArray(Object[] objects) {
		String[] ss = new String[objects.length];
		for (int i = 0; i <= ss.length - 1; ++i)
			ss[i] = (String) objects[i];
		return ss;
	}

	/**
	 * Object轉Integer
	 * @param o
	 * @return
	 */
	public static Integer getInteger(Object o) {
		if (o == null)
			return 0;
		if (o.toString().equals(""))
			return 0;
		return Integer.valueOf(o.toString());
	}

	/**
	 * 取得HSSFCell Value
	 * @param cell poi cell
	 * @return Object cell value
	 */
	public static Object getCellValue(HSSFCell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return null;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return new Boolean(cell.getBooleanCellValue());
		case HSSFCell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue();
		case HSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula().trim();
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isValidExcelDate(cell.getNumericCellValue()) && HSSFDateUtil.isCellDateFormatted(cell))
				return cell.getDateCellValue();
			return new Double(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		}
		return null;
	}

	// COMMON METHOD ****************************************** 2015/07
	/**
	 * 字串轉Double
	 * @param value 字串
	 * @return Double
	 */
	public static Double string2Value(String value) {
		return ("".equalsIgnoreCase(value)) ? (null) : (Double.valueOf(value));
	}

	/**
	 * 字串轉日期
	 * @param sDate 日期
	 * @return Date
	 */
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

	/**
	 * 日期轉字串，格式yyyy/MM/dd
	 * @param date 日期
	 * @return String
	 */
	public static String utilDate2String(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String sDate = df.format(date);
		return sDate;
	}

	/**
	 * 取得現在日期
	 * @return Date
	 */
	public static java.sql.Date now_sqlDate() {
		java.util.Date now = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(now.getTime());
		return sqlDate;
	}

	/**
	 * java.util.Date轉換java.sql.Date
	 * @param util_date 
	 * @return java.sql.Date
	 */
	public static java.sql.Date utilDaye2sqlDate(java.util.Date util_date) {
		return new java.sql.Date(util_date.getTime());
	}

	/**
	 * 將字串中deli1取代為deli2，並轉為java.sql.Date
	 * @param stringDate
	 * @param deli1
	 * @param deli2
	 * @return java.sql.Date
	 */
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

	/**
	 * 代換日期
	 * @param stringDate
	 * @param format
	 * @param deli1
	 * @param deli2
	 * @return
	 */
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

	/**
	 * 執行SQL語法
	 * @param sql SQL語法
	 * @param params 傳入SQL參數
	 * @return true:執行成功/false:執行失敗
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static boolean executeSQLStatement(String sql, List params) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean ok = false;
		try {
			conn = Common.getDbConnection();
			ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.size(); i++) {
					if (params.get(i) instanceof Date)
						ps.setTimestamp(i + 1, new Timestamp(((Date)params.get(i)).getTime()));
					else
						ps.setObject(i + 1, params.get(i));
				}
			}
			int count = ps.executeUpdate();
			ok = count > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeQuietly(conn, ps, null);
		}
		return ok;
	}

	/**
	 * 批次執行SQL語法
	 * @param conn 傳入Connection
	 * @param sql SQL語法
	 * @param params 入傳SQL之參數
	 * @return true:執行成功/false:執行失敗
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static boolean executeSQLStatement(Connection conn, String sql, List params) throws Exception {
		PreparedStatement ps = null;
		boolean ok = false;
		try {
			ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.size(); i++) {
					if (params.get(i) instanceof Date)
						ps.setTimestamp(i + 1, new Timestamp(((Date)params.get(i)).getTime()));
					else
						ps.setObject(i + 1, params.get(i));
				}
			}
			int count = ps.executeUpdate();
			ok = count > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(ps);
		}

		return ok;
	}
	
	/**
	 * 取得Client IP
	 * @return IP
	 */
	public static String getClientIp() {
		if (Executions.getCurrent().getHeader("x-forwarded-for") == null) {
			return Executions.getCurrent().getRemoteAddr();
		} else {
			return Executions.getCurrent().getHeader("x-forwarded-for");
		}
	}
	
	
	/**
	 * 取得國家
	 * @return
	 */
	public static String getCountry() {
		return Locale.getDefault().getCountry();
	}	
	
	/**
	 * 計算兩日期差
	 * @param befor 開始日
	 * @param after 結束日
	 * @param defferencyType 時間格式SECOND, MINUTE, HOUR, DAY
	 * @return 回傳該日期差之對應格式
	 */
	public static long differenceOfTwo(Calendar befor, Calendar after, String defferencyType) {		
		long m = after.getTimeInMillis() - befor.getTimeInMillis();

		switch (defferencyType) {
		case "SECOND":
			m = m / (1000);
			break;
		case "MINUTE":
			m = m / (60 * 1000);
			break;
		case "HOUR":
			m = m / (60 * 60 * 1000);
			break;
		case "DAY":
			m = m / (24 * 60 * 60 * 1000);
			// 判斷是不是同一天
			if (m == 0 && after.get(Calendar.DAY_OF_YEAR) != befor.get(Calendar.DAY_OF_YEAR)) {
				m += 1;
			}
			break;
		default:
			break;
		}
		return m;
	}

	
	/**
     * 比較二個日期的天數差
     * @param befor 開始日
     * @param after 結束日
     * @return 回傳天數差
     */
    public int daysOfTwo(Calendar befor, Calendar after) {
        long m = after.getTimeInMillis() - befor.getTimeInMillis();
        m = m / (24 * 60 * 60 * 1000);
        //判斷是不是同一天
        if (m == 0 && after.get(Calendar.DAY_OF_YEAR) != befor.get(Calendar.DAY_OF_YEAR)) {
            m += 1;
        }
        return (int) m;
    }
    
	
	/**
	 *20171031 johnny 設定主次檔按鈕，支援非系統設定之按鈕 
	 *@param objProgram	底層
	 *@param bDisable 控制button disable
	 */	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public static void setButtonAuth(Object objProgram, boolean bDisable) {
		HashMap<String, Object> mapBtn = null;
		String sProgram = null;
		ProgramAuth prgAuth  = null;
		Object ObjectSel = null;
		DataMode dataMode = null;
		Paging paging = null;
		List<Detail> arrDetailPrograms = null;
		
		if (objProgram instanceof Master) {
			mapBtn = ((Master) objProgram).getButtons();
			sProgram = "master";
			prgAuth = ((Master) objProgram).prgAuth;
			ObjectSel = ((Master) objProgram).getMasterSel();
			dataMode = ((Master) objProgram).getData_Mode();
			paging = ((Master) objProgram).masterPaging;
			arrDetailPrograms = ((Master) objProgram).getArrDetailPrograms();
		} else if (objProgram instanceof OpenWinCRUD) {
			mapBtn = ((OpenWinCRUD) objProgram).getButtons();
			sProgram = "master";
			prgAuth = ((OpenWinCRUD) objProgram).prgAuth;
			ObjectSel = ((OpenWinCRUD) objProgram).getParentSel();
			dataMode = ((OpenWinCRUD) objProgram).getRecordMode();
			arrDetailPrograms = ((OpenWinCRUD) objProgram).getArrDetailPrograms();
		} else if (objProgram instanceof Detail) {
			mapBtn = ((Detail) objProgram).getButtons();
			sProgram = "detail";
			prgAuth = ((Detail) objProgram).prgAuth;
			ObjectSel = ((Detail) objProgram).getDetailSel();
			dataMode = ((Detail) objProgram).getData_Mode();
			paging = ((Detail) objProgram).detailPaging;			
		}
		
		if (mapBtn != null) {
			//將HashMap序列化						
			List keys = new ArrayList(mapBtn.keySet());
			for (int i = 0; i < keys.size(); i++) {
				String obj = (String) keys.get(i);

				boolean bAuth = false;
				if (mapBtn.get(obj) != null) {
					String sbtnName = obj.substring(0, obj.indexOf(sProgram) > 0 ? obj.indexOf(sProgram) : obj.length());
					switch (sbtnName.toUpperCase()) {
					case "BTNCREATE":
						if (mapBtn.get(obj) != null && prgAuth.getAuthInsert()) {							
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = true;
							} else {
								bAuth = false;
							}
						}	
						break;
					case "BTNEDIT":
						if (mapBtn.get(obj) != null && prgAuth.getAuthEdit()) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								//無資料時，無權限
								if (ObjectSel == null) {
									bAuth = false;
								} else {
									bAuth = true;	
								}
							} else {
								bAuth = false;
							}
						}
						break;
					case "BTNDELETE":
						if (mapBtn.get(obj) != null && prgAuth.getAuthDelete()) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								//無資料時，無權限
								if (ObjectSel == null) {
									bAuth = false;
								} else {
									bAuth = true;	
								}
							} else {
								bAuth = false;
							}
						}
						break;
					case "BTNQUERY":
						if (mapBtn.get(obj) != null && prgAuth.getAuthQuery()) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = true;
							} else {
								bAuth = false;
							}
						}
						break;
					case "BTNEXPORT":
						if (mapBtn.get(obj) != null && prgAuth.getAuthExport()) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = true;
							} else {
								bAuth = false;
							}
						}
						break;
					case "BTNPRINT":
						if (mapBtn.get(obj) != null && prgAuth.getAuthPrint()) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = true;
							} else {
								bAuth = false;
							}
						}
						break;
					case "BTNSAVE":
						if (mapBtn.get(obj) != null) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = false;
							} else {
								bAuth = true;
							}
						}
						break;
					case "BTNCANCEL":
						if (mapBtn.get(obj) != null) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = false;
							} else {
								bAuth = true;
							}
						}
						break;
					default:
						//非上述按鈕，套用此block
						if (mapBtn.get(obj) != null) {
							if (dataMode.equals(DataMode.READ_MODE)) {
								bAuth = true;
							} else {
								bAuth = false;
							}
						}
					}
					
					//若bDisable為true, 則鈕按設定為Disabled, 若bDisable為false，則套用權限設定, 有權限setDisabled為false
					((Button) mapBtn.get(obj)).setDisabled(bDisable ? bDisable : !bAuth);
				}
			}		
		}
		
		if (paging != null) {
			if (dataMode.equals(DataMode.READ_MODE)) {
				paging.setDisabled(bDisable ? bDisable : false);
			} else {
				paging.setDisabled(true);
			}
		}
	}
	
	
	/**
	 *20171031 johnny 設定其他Detail按鈕為Disable
	 *@param objMasterProgram 傳入之MasterPrograms
	 *@param detailSelf 自身Detail 
	 */
	@SuppressWarnings("rawtypes")
	public static void setOtherDetailButton(Object objMasterProgram, Detail detailSelf) {		
		boolean bDisable = false;
		List arrDetailPrograms = null;
		
		if (objMasterProgram instanceof Master) {
			arrDetailPrograms = ((Master) objMasterProgram).arrDetailPrograms;
			if (((Master) objMasterProgram).getData_Mode().equals(DataMode.READ_MODE)) {
				bDisable = false;
				
			} else {
				bDisable = true;				
			}
		} else if (objMasterProgram instanceof OpenWinCRUD) {
			arrDetailPrograms = ((OpenWinCRUD) objMasterProgram).arrDetailPrograms;
			if (((OpenWinCRUD) objMasterProgram).getRecordMode().equals(DataMode.READ_MODE)) {
				bDisable = false;
			} else {
				bDisable = true;
			}
		}
		
		if (detailSelf != null){
			if (detailSelf.getData_Mode().equals(DataMode.READ_MODE)){
				bDisable = false;
			} else {
				bDisable = true;
			}
		}
		
		for (int i=0; i<arrDetailPrograms.size(); i++){			
			//排除自身Detai的按鈕設定
			if (!arrDetailPrograms.get(i).equals(detailSelf)){
				setButtonAuth(arrDetailPrograms.get(i), bDisable);
			}
		}
	}
	
	
	/**
	 * 資料定位
	 * @param objProgram	底層
	 * @param entity
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public static void setPosition(Object objProgram, Object entity) throws Exception {
		ArrayList<String> keyname = null;
		String sSqlOneColBase = null;
		ArrayList<String> KeyName = null;
		CRUDService CRUDService = null;
		
		if (objProgram instanceof Master) {
			keyname = ((Master) objProgram).getMasterKeyName();
			sSqlOneColBase = ((Master) objProgram).sSqlOneColBase;
			KeyName = ((Master) objProgram).getMasterKeyName();
			CRUDService = ((Master) objProgram).CRUDService;
		} else if (objProgram instanceof Detail) {
			keyname = ((Detail) objProgram).getDetailKeyName();
			sSqlOneColBase = ((Detail) objProgram).sSqlOneColBase;
			KeyName = ((Detail) objProgram).getDetailKeyName();
			CRUDService = ((Detail) objProgram).CRUDService;
		}

		Query qry = null;
		int idx = 0;
		
		if (entity != null) {
			String sSql = " with getRowNum as ( SELECT ROWNUM as rownum_, row_.* FROM (" + sSqlOneColBase + ") row_ ) "
					+ " select rownum_ from getRowNum ";
			String sWhere = " WHere 1=1 ";
			for (int i = 0; i < keyname.size(); i++) {
				sWhere += " AND " + keyname.get(i) + " = :" + keyname.get(i);
			}
			sSql += sWhere;

			qry = CRUDService.createSQL(sSql);
			for (int i = 0; i < keyname.size(); i++) {
				Object column = propertyReadMethod(entity, keyname.get(i));
				qry.setParameter(keyname.get(i), column);
			}
			
			
			if (qry.getResultList().size() > 0) {
				idx = ((BigDecimal) qry.getResultList().get(0)).intValue() - 1;
			} else {
				idx = 0;
			}
		}
		
		if (objProgram instanceof Master) {
			((Master) objProgram).doFillListbox(idx);
		} else if (objProgram instanceof Detail) {
			((Detail) objProgram).doFillListboxDetail(idx);
		}
	}
	
	
	/**
	 * johnny 20170926 檢查domain object是否有錯誤
	 * @param objSel 傳入的entity
	 * @return true:無錯誤, false:有錯誤
	 */
	public static boolean validate(Object objSel) {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator validator = vf.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(objSel);
		String[] ss;
		String sMsg = "";
		for(ConstraintViolation<Object> v : violations){
			ss = v.getMessage().split(";");
			for (int i = 0; i < ss.length; i++) {
				sMsg += Labels.getLabel(ss[i]);
			}
			sMsg += "\n";
		}

		if (sMsg.isEmpty())
			return true;
		else {
			Messagebox.show(sMsg);		
			return false;
		}
	}
	
	/**
	 * 20171018 johnny add 檢查pk是否重覆
	 * @param objSel 傳入的entity
	 * @throws SQLException 
	 */
	public static boolean CheckPK(Object objProgram, Object objSel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		DataMode Data_Mode = null;
		String ENTITY_CLASSNAME = null;
		ArrayList<String> KeyName = null;
		HashMap<String, Object[]> mapPKColumns = null;
		String sWhere = null;
		boolean bReturn = true;
		CRUDService CRUDService = null;
		
		try {
			if (objProgram instanceof Master) {
				Data_Mode = ((Master) objProgram).getData_Mode();
				ENTITY_CLASSNAME = ((Master) objProgram).ENTITY_CLASSNAME;
				KeyName = ((Master) objProgram).getMasterKeyName();
				sWhere = ((Master) objProgram).sWhere;
				mapPKColumns = ((Master) objProgram).mapPKColumns;
				CRUDService = ((Master) objProgram).CRUDService;
			} else if (objProgram instanceof OpenWinCRUD) {
				Data_Mode = ((OpenWinCRUD) objProgram).getRecordMode();
				ENTITY_CLASSNAME = ((OpenWinCRUD) objProgram).ENTITY_CLASSNAME;
				KeyName = ((OpenWinCRUD) objProgram).getKeyName();
				sWhere = ((OpenWinCRUD) objProgram).sWhere;
				mapPKColumns = ((OpenWinCRUD) objProgram).mapPKColumns;
				CRUDService = ((OpenWinCRUD) objProgram).CRUDService;
			} else if (objProgram instanceof Detail) {
				Data_Mode = ((Detail) objProgram).getData_Mode();
				ENTITY_CLASSNAME = ((Detail) objProgram).ENTITY_CLASSNAME;
				KeyName = ((Detail) objProgram).getDetailKeyName();
				sWhere = ((Detail) objProgram).sWhere;
				mapPKColumns = ((Detail) objProgram).mapPKColumns;
				CRUDService = ((Detail) objProgram).CRUDService;
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
		
		if (Data_Mode.equals(DataMode.CREATE_MODE)){
			String sEntity = objSel.getClass().getName().split("\\.")[objSel.getClass().getName().split("\\.").length-1];
			String sSql = "Select * From " + ENTITY_CLASSNAME.replace("VO", "") + " t Where 1=1 ";
			
			if (ENTITY_CLASSNAME.equals(sEntity)) {
				String sPkWhere = "";
				for (int i = 0; i < KeyName.size(); i++) {
					sPkWhere += " AND " + KeyName.get(i) + " = :"+ KeyName.get(i);
				}

				sSql += sPkWhere;

				Query qry = CRUDService.createSQL(sSql);
				for (int i = 0; i < KeyName.size(); i++) {
					Object column = propertyReadMethod(objSel, KeyName.get(i));
						qry.setParameter(KeyName.get(i), column);
				}
				
				if (!qry.getResultList().isEmpty()) {				
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0056"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					bReturn = false;
				}
			} else {
				sSql += sWhere;
				Connection conn = Common.getDbConnection();
				NameParameterStatement npstmt = null;
				try {
					npstmt = new NameParameterStatement(conn, sSql);
					for (int i=0; i<mapPKColumns.size(); i++){
						if (mapPKColumns.get("Col" + String.valueOf(i))[1] instanceof Double)
							npstmt.setDouble((String)mapPKColumns.get("Col" + String.valueOf(i))[0], (Double)mapPKColumns.get("Col" + String.valueOf(i))[1]);
						else if (mapPKColumns.get("Col" + String.valueOf(i))[1] instanceof String)
							npstmt.setString((String)mapPKColumns.get("Col" + String.valueOf(i))[0], (String)mapPKColumns.get("Col" + String.valueOf(i))[1]);
						else if (mapPKColumns.get("Col" + String.valueOf(i))[1] instanceof Date)
							npstmt.setTimestamp((String)mapPKColumns.get("Col" + String.valueOf(i))[0], new Timestamp(((Date)mapPKColumns.get("Col" + String.valueOf(i))[1]).getTime()));
						else if (mapPKColumns.get("Col" + String.valueOf(i))[1] instanceof Integer)
							npstmt.setInt((String)mapPKColumns.get("Col" + String.valueOf(i))[0], (Integer)mapPKColumns.get("Col" + String.valueOf(i))[1]);
						else if (mapPKColumns.get("Col" + String.valueOf(i))[1] instanceof byte[])
							npstmt.setBytes((String)mapPKColumns.get("Col" + String.valueOf(i))[0], (byte[])mapPKColumns.get("Col" + String.valueOf(i))[1]);
						else{
							if (mapPKColumns.get("Col" + String.valueOf(i))[1] == null){
								switch((int)mapPKColumns.get("Col" + String.valueOf(i))[2]){
								case java.sql.Types.NUMERIC:
									npstmt.setNull((String)mapPKColumns.get("Col"+String.valueOf(i))[0], java.sql.Types.NUMERIC);
									break;
								case java.sql.Types.DATE:
									npstmt.setNull((String)mapPKColumns.get("Col"+String.valueOf(i))[0], java.sql.Types.DATE);
									break;
								case java.sql.Types.VARCHAR:
									npstmt.setString((String)mapPKColumns.get("Col"+String.valueOf(i))[0], "");
									break;
								case java.sql.Types.JAVA_OBJECT:
									npstmt.setObject((String)mapPKColumns.get("Col"+String.valueOf(i))[0], null);
									break;
								}
							}
						}
					}
					ResultSet rs = npstmt.executeQuery();
					if (rs.next()){
						Messagebox.show(Labels.getLabel("PUBLIC.MSG0056"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
						bReturn = false;
					}
				} catch(Exception e) {
					conn.rollback();
					throw e;
				} finally {
					try {
						closeQuietly(conn, npstmt, null);
					} catch (Exception e) {
						logger.error(null, e);
					}
				}					
			}
		}
		return bReturn;
	}

	
	/**
	 * 將Component Value寫入entity
	 * @param objProgram 底層
	 * @param className
	 * @param objSel 資料物件
	 * @param comColumns 寫入資料之欄位
	 * @return 成功回傳true, 失敗回傳false
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static boolean writeMethod(Object objProgram, String className, Object objSel, List<ComponentColumn> comColumns)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		try {	
			HashMap<String, Object[]> mapColumns = null;
			Window RootWindow = null;
			Div RootDiv = null;
			boolean bRegular = true;
			boolean bInLineEdit = false;
			ArrayList<String> KeyName  = null;
			String sWhere = "";

			if (objProgram instanceof Master) {
				mapColumns = ((Master) objProgram).mapColumns;
				RootWindow = ((Master) objProgram).getRootWindow();
				bRegular = ((Master) objProgram).bRegular;
				bInLineEdit = ((Master) objProgram).bInLineEdit;
				KeyName = ((Master) objProgram).getMasterKeyName();
				sWhere = ((Master) objProgram).sWhere;
			} else if (objProgram instanceof OpenWinCRUD) {
				mapColumns = ((OpenWinCRUD) objProgram).mapColumns;
				RootWindow = ((OpenWinCRUD) objProgram).getRootWindow();
				bRegular = ((OpenWinCRUD) objProgram).bRegular;
				KeyName = ((OpenWinCRUD) objProgram).getKeyName();
				sWhere = ((OpenWinCRUD) objProgram).sWhere;
			} else if (objProgram instanceof Detail) {
				mapColumns = ((Detail) objProgram).mapColumns;
				RootWindow = ((Detail) objProgram).getRootWindow();
				RootDiv = ((Detail) objProgram).getRootDiv();
				bRegular = ((Detail) objProgram).bRegular;
				bInLineEdit = ((Detail) objProgram).bInLineEdit;
				KeyName = ((Detail) objProgram).getDetailKeyName();
				sWhere = ((Detail) objProgram).sWhere;
			}

			if (comColumns.size() > 0){
				for (int i = 0; i <= comColumns.size() - 1; ++i) {
					ComponentColumn cc = comColumns.get(i);
					Component component = cc.getComponent();
					String componentName = cc.getComponentName();
					if (component == null && componentName != null)
						if (objProgram instanceof Master || objProgram instanceof OpenWinCRUD) {
							component = RootWindow.getFellow(componentName);
						} else if (objProgram instanceof Detail) {	
							if (RootWindow != null)
								component = RootWindow.getFellow(componentName);
							else
								component = RootDiv.getFellow(componentName);
						}

					String columnName = cc.getColumnName();
					String sRealColumnName = cc.getRealColumnName() != null ? cc.getRealColumnName() : columnName;

					boolean pk = cc.getPK();
					if (component instanceof Decimalbox) {
						Double value = null;
						if (((Decimalbox) component).getValue() == null)
							value = (cc.getDefaultValue() == null ? null : Common.getDouble(cc.getDefaultValue()));
						else
							value = ((Decimalbox) component).getValue().doubleValue();


						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.NUMERIC });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}

						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Combobox) {
						String value = null;
						if (((Combobox) component).getSelectedItem() == null)
							value = (cc.getDefaultValue() == null ? null : (String) cc.getDefaultValue());
						else
							value = ((Combobox) component).getSelectedItem().getValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.VARCHAR });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Label) {
						String value = "";
						if (((Label) component).getValue().isEmpty())
							value = (cc.getDefaultValue() == null ? "" : (String) cc.getDefaultValue());
						else
							value = ((Label) component).getValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.VARCHAR });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Textbox) {
						String value = "";
						if (((Textbox) component).getValue().isEmpty())
							value = (cc.getDefaultValue() == null ? "" : (String) cc.getDefaultValue());
						else
							value = ((Textbox) component).getValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.VARCHAR });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Datebox) {
						Date value = null;
						if (((Datebox) component).getValue() == null)
							value = (cc.getDefaultValue() == null ? null : (Date) cc.getDefaultValue());
						else
							value = ((Datebox) component).getValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i), 
									new Object[] { sRealColumnName, value, java.sql.Types.DATE });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Checkbox) {
						String value = ((Checkbox) component).isChecked() ? "Y" : "N";
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.VARCHAR });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Intbox) {
						Integer value = null;
						if (((Intbox) component).getValue() == null)
							value = (cc.getDefaultValue() == null ? null : Common.getInteger(cc.getDefaultValue()));
						else
							value = ((Intbox) component).getValue().intValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.NUMERIC });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Spinner) {
						Integer value = null;
						if (((Spinner) component).getValue() == null)
							value = (cc.getDefaultValue() == null ? null : Common.getInteger(cc.getDefaultValue()));
						else
							value = ((Spinner) component).getValue().intValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.NUMERIC });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof Doublebox) {
						Double value = null;
						if (((Doublebox) component).getValue() == null)
							value = (cc.getDefaultValue() == null ? null : (Double) cc.getDefaultValue());
						else
							value = ((Doublebox) component).getValue();
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, java.sql.Types.NUMERIC });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);

					} else if (component instanceof dscQueryField) {
						Object value = null;
						int iColType;
						if (((dscQueryField) component).getReturnComponent().equals("Double")){
							iColType =java.sql.Types.NUMERIC;
						}else{
							iColType =java.sql.Types.VARCHAR;
						}
//						if (((dscQueryField) component).getValue() == null)
//							value = (cc.getDefaultValue() == null ? null : cc.getDefaultValue());
//						else
//							value = ((dscQueryField) component).getValue();
						
						if (((dscQueryField) component).getValue() == null || ((dscQueryField) component).getValue().isEmpty())
							value = (cc.getDefaultValue() == null ? null : cc.getDefaultValue());
						else{
							if (iColType==java.sql.Types.NUMERIC)
								value = Double.valueOf(((dscQueryField) component).getValue());
							else
								value = ((dscQueryField) component).getValue();
						}
						
						if (bRegular == true || bInLineEdit == true) {
							mapColumns.put("Col" + String.valueOf(i),
									new Object[] { sRealColumnName, value, iColType });

							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + value);
						propertyWriteMethod(objSel, columnName, value);
						
					} else if (component == null) {
						Object v = null;
						if (objProgram instanceof Master) {
							v = ((Master) objProgram).doSaveDefault(columnName);
						} else if (objProgram instanceof OpenWinCRUD) {	
							v = ((OpenWinCRUD) objProgram).doSaveDefault(columnName);
						} else if (objProgram instanceof Detail) {	
							v = ((Detail) objProgram).doSaveDefault(columnName);
						}

						if (v == null || v.equals("")) {
							v = cc.getDefaultValue();
						}
						if (bRegular == true || bInLineEdit == true) {
							if (v instanceof byte[])
								mapColumns.put("Col" + String.valueOf(i), 
										new Object[] { sRealColumnName, v, java.sql.Types.BLOB });
							else
								mapColumns.put("Col" + String.valueOf(i),
										new Object[] { sRealColumnName, v, java.sql.Types.JAVA_OBJECT });
							if (KeyName.contains(sRealColumnName) || pk)
								sWhere += " AND " + sRealColumnName + " = :" + sRealColumnName;
						}
						System.out.println("columnName:" + sRealColumnName + ", value:" + v);
						propertyWriteMethod(objSel, columnName, v);

					}
				}

				if (objProgram instanceof Master) {
					((Master) objProgram).sWhere = sWhere;
				} else if (objProgram instanceof OpenWinCRUD) {
					((OpenWinCRUD) objProgram).sWhere = sWhere;
				} else if (objProgram instanceof Detail) {
					((Detail) objProgram).sWhere = sWhere;
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	
	/**
	 * 底層存檔功能
	 * @param objProgram	底層
	 * @param objSel
	 * @param conn
	 * @param ut 
	 * @throws SQLException
	 */
	@SuppressWarnings("incomplete-switch")
	public static void executeSave(Object objProgram, Object objSel, Connection conn, UserTransaction ut) throws Exception {
		boolean bRegular = true;
		boolean bInLineEdit = false;
		DataMode data_Mode = null;
		String ENTITY_CLASSNAME = "";
		HashMap<String, Object[]> mapColumns = null;
		String sWhere = "";
		boolean bDoSaveAfter = true;
		CRUDService CRUDService = null;
		
		if (objProgram instanceof Master) {
			bRegular = ((Master) objProgram).bRegular;
			bInLineEdit = ((Master) objProgram).bInLineEdit;
			data_Mode = ((Master) objProgram).getData_Mode();
			ENTITY_CLASSNAME = ((Master) objProgram).ENTITY_CLASSNAME;
			mapColumns = ((Master) objProgram).mapColumns;
			sWhere = ((Master) objProgram).sWhere;
			CRUDService = ((Master) objProgram).CRUDService;
		} else if (objProgram instanceof OpenWinCRUD) {
			bRegular = ((OpenWinCRUD) objProgram).bRegular;
			bInLineEdit = false;
			data_Mode = ((OpenWinCRUD) objProgram).getRecordMode();
			ENTITY_CLASSNAME = ((OpenWinCRUD) objProgram).ENTITY_CLASSNAME;
			mapColumns = ((OpenWinCRUD) objProgram).mapColumns;
			sWhere = ((OpenWinCRUD) objProgram).sWhere;
			CRUDService = ((OpenWinCRUD) objProgram).CRUDService;
		} else if (objProgram instanceof Detail) {
			bRegular = ((Detail) objProgram).bRegular;
			bInLineEdit = ((Detail) objProgram).bInLineEdit;
			data_Mode = ((Detail) objProgram).getData_Mode();
			ENTITY_CLASSNAME = ((Detail) objProgram).ENTITY_CLASSNAME;
			mapColumns = ((Detail) objProgram).mapColumns;
			sWhere = ((Detail) objProgram).sWhere;
			CRUDService = ((Detail) objProgram).CRUDService;
		}
		
		if (bRegular || bInLineEdit) {
			String sSQL = "";
			switch (data_Mode) {
			case CREATE_MODE:
				String sFields = "", sValue = "";
				sSQL = "Insert into " + ENTITY_CLASSNAME.replace("VO", "") + " (";
				for (int i = 0; i < mapColumns.size(); i++) {
					sValue += ", :" + mapColumns.get("Col" + String.valueOf(i))[0];
					sFields += ", " + mapColumns.get("Col" + String.valueOf(i))[0];
				}
				sSQL = sSQL + sFields.substring(1) + ") VALUES(" + sValue.substring(1) + ")";
				break;
			case UPDATE_MODE:
				sSQL = "Update " + ENTITY_CLASSNAME.replace("VO", "") + " Set ";
				String sUpdate = "";
				for (int i = 0; i < mapColumns.size(); i++) {
					sUpdate += ", " + mapColumns.get("Col" + String.valueOf(i))[0] + " = :"
							+ mapColumns.get("Col" + String.valueOf(i))[0];
				}
				sSQL = sSQL + sUpdate.substring(1) + " Where 1=1 " + sWhere;
				break;
			}
			
			long startTime = System.nanoTime();  //計算處理時間-開始
			NameParameterStatement npstmt = new NameParameterStatement(conn, sSQL);
			System.out.println("execute SQL:"+sSQL);
			try {
				for (int i = 0; i < mapColumns.size(); i++) {
					if (mapColumns.get("Col" + String.valueOf(i))[1] instanceof Double)
						npstmt.setDouble((String) mapColumns.get("Col" + String.valueOf(i))[0],
								(Double) mapColumns.get("Col" + String.valueOf(i))[1]);
					else if (mapColumns.get("Col" + String.valueOf(i))[1] instanceof String)
						npstmt.setString((String) mapColumns.get("Col" + String.valueOf(i))[0],
								(String) mapColumns.get("Col" + String.valueOf(i))[1]);
					else if (mapColumns.get("Col" + String.valueOf(i))[1] instanceof Date)
						npstmt.setTimestamp((String) mapColumns.get("Col" + String.valueOf(i))[0],
								new Timestamp(((Date) mapColumns.get("Col" + String.valueOf(i))[1]).getTime()));
					else if (mapColumns.get("Col" + String.valueOf(i))[1] instanceof Integer)
						npstmt.setInt((String) mapColumns.get("Col" + String.valueOf(i))[0],
								(Integer) mapColumns.get("Col" + String.valueOf(i))[1]);
					else if (mapColumns.get("Col" + String.valueOf(i))[1] instanceof byte[])
						npstmt.setBytes((String) mapColumns.get("Col" + String.valueOf(i))[0],
								(byte[]) mapColumns.get("Col" + String.valueOf(i))[1]);
					else {
						if (mapColumns.get("Col" + String.valueOf(i))[1] == null) {
							switch ((int) mapColumns.get("Col" + String.valueOf(i))[2]) {
							case java.sql.Types.NUMERIC:
								npstmt.setNull((String) mapColumns.get("Col" + String.valueOf(i))[0], java.sql.Types.NUMERIC);
								break;
							case java.sql.Types.DATE:
								npstmt.setNull((String) mapColumns.get("Col" + String.valueOf(i))[0], java.sql.Types.DATE);
								break;
							case java.sql.Types.VARCHAR:
								npstmt.setString((String) mapColumns.get("Col" + String.valueOf(i))[0], "");
								break;
							case java.sql.Types.JAVA_OBJECT:
								npstmt.setObject((String) mapColumns.get("Col" + String.valueOf(i))[0], null);
								break;
							}
						}
					}
				}
				
				npstmt.execute();
				long processTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime); //計算處理時間-結束
				System.out.println("Execute Save Spend:"+processTime);
				
				if (objProgram instanceof Master) {
					bDoSaveAfter = ((Master) objProgram).doSaveAfter(conn);
				} else if (objProgram instanceof OpenWinCRUD) {
					bDoSaveAfter = ((OpenWinCRUD) objProgram).doSaveAfter(conn);
				} else if (objProgram instanceof Detail) {
					bDoSaveAfter = ((Detail) objProgram).doSaveAfter(conn);
				}

				if (bDoSaveAfter) {
					ut.commit();
					conn.commit();
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				ut.rollback();
				conn.rollback();
				throw e;
			} finally {
				try {
					close(npstmt);
				} catch (Exception e) {
					logger.error(null, e);
				}
				
				if (objProgram instanceof Master) {
					((Master) objProgram).sWhere = "";
				} else if (objProgram instanceof OpenWinCRUD) {
					((OpenWinCRUD) objProgram).sWhere = sWhere;
				} else if (objProgram instanceof Detail) {
					((Detail) objProgram).sWhere = "";
				}
			}
		} else {
			CRUDService.save(objSel);
		}
	}
	
	/**
	 * 底層檢查Component
	 * @param objProgram	底層
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean checkComponent(Object objProgram) {
		List<ComponentColumn> ComponentColumns = null;
		Window RootWindow = null;
		Component RootDiv = null;
		boolean bReturn = true;
		
		if (objProgram instanceof Master) {
			ComponentColumns = ((Master) objProgram).masterComponentColumns;
			RootWindow = ((Master) objProgram).getRootWindow();
		} else if (objProgram instanceof OpenWinCRUD) {
			ComponentColumns = ((OpenWinCRUD) objProgram).componentColumns;
			RootWindow = ((OpenWinCRUD) objProgram).getRootWindow();
		} else if (objProgram instanceof Detail) {
			ComponentColumns = ((Detail) objProgram).detailComponentColumns;
			RootWindow = ((Detail) objProgram).getRootWindow();
			RootDiv = ((Detail) objProgram).getRootDiv();
		}


		for (int i = 0; i <= ComponentColumns.size() - 1; ++i) {
			ComponentColumn cc = ComponentColumns.get(i);
			List<EntityCheck> entityChecks = cc.getArrChecks();
			List<String> checkMessages = cc.getArrCheckMessages();
			Component component = cc.getComponent();
			String componentName = cc.getComponentName();
			if (component == null && componentName != null)
				if (objProgram instanceof Master) {
					component = RootWindow.getFellow(componentName);
				} else if (objProgram instanceof Detail) {
					if (RootWindow != null)
						component = RootWindow.getFellow(componentName);
					else	
						component = RootDiv.getFellow(componentName);
				}

			if (entityChecks != null) {
				if (component instanceof Decimalbox) {
					Double value = ((Decimalbox) component).getValue().doubleValue();
					for (int j = 0; j <= entityChecks.size() - 1; ++j) {
						String checkMessage = checkMessages.get(j);
						switch (entityChecks.get(j)) {
						case NULL_EMPTYSTRING:
						case EMPTYSTRING:
							if (value == null) {
								Messagebox.show(checkMessage, "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
								bReturn = false;
							}
							break;
						case NULL:
							break;
						}
					}
				} else if (component instanceof Combobox) {
					String selectedLabel = ((Combobox) component).getSelectedItem() == null ? ""
							: ((Combobox) component).getSelectedItem().getLabel();
					for (int j = 0; j < entityChecks.size(); j++) {
						String checkMessage = checkMessages.get(j);
						switch (entityChecks.get(j)) {
						case NULL_EMPTYSTRING:
						case EMPTYSTRING:
							if (selectedLabel.isEmpty()) {
								Messagebox.show(checkMessage, "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
								bReturn = false;
							}
							break;
						case NULL:
							break;
						}
					}
				} else if (component instanceof Textbox) {
					String value = ((Textbox) component).getValue();
					for (int j = 0; j <= entityChecks.size() - 1; ++j) {
						String checkMessage = checkMessages.get(j);
						switch (entityChecks.get(j)) {
						case NULL_EMPTYSTRING:
						case EMPTYSTRING:
							if (value == null || value.isEmpty()) {
								Messagebox.show(checkMessage, "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
								bReturn = false;
							}
							break;
						case NULL:
							break;
						}
					} 
				} else if (component instanceof Datebox) {
					Date value = ((Datebox) component).getValue();
					for (int j = 0; j <= entityChecks.size() - 1; ++j) {
						String checkMessage = checkMessages.get(j);
						switch (entityChecks.get(j)) {
						case NULL_EMPTYSTRING:
						case EMPTYSTRING:
							if (value == null || value.equals("")) {
								Messagebox.show(checkMessage, "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
								bReturn = false;
							}
							break;
						case NULL:
							break;
						}
					}
				}
			}
		}
		return bReturn;
	}
	

	/**
	 * 底層設定entity ENTITY_CLASSNAME and ENTITY_PATH
	 * @param objProgram	底層
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void setEntityClassPath(Object objProgram) {
		boolean bInLineEdit = false;
		Class cClass = null;
		String sEntityClassName = "";
		String sEntityPath= "";

		if (objProgram instanceof Master) {
			cClass = ((Master) objProgram).getEntityClass();
		} else if (objProgram instanceof OpenWinCRUD) {
			cClass = ((OpenWinCRUD) objProgram).getEntityClass();
		} else if (objProgram instanceof Detail) {
			cClass = ((Detail) objProgram).getDetailClass();
		}

		if (cClass == null) {
			sEntityClassName = "";
		} else {
			bInLineEdit = BeanUtils.isPropertyExist(cClass, "ISEDIT");
			String eClass[] = cClass.getName().split("\\.");
			sEntityClassName = eClass[eClass.length - 1];
			sEntityPath = "";
			for (int i = 0; i < eClass.length; i++)
				sEntityPath += "." + eClass[i];
			sEntityPath = sEntityPath.substring(1);
		}
		
		if (objProgram instanceof Master) {
			((Master) objProgram).bInLineEdit = bInLineEdit;
			((Master) objProgram).ENTITY_CLASSNAME = sEntityClassName;
			((Master) objProgram).ENTITY_PATH = sEntityPath;
		} else if (objProgram instanceof OpenWinCRUD) {
			((OpenWinCRUD) objProgram).ENTITY_CLASSNAME = sEntityClassName;
			((OpenWinCRUD) objProgram).ENTITY_PATH = sEntityPath;
		} else if (objProgram instanceof Detail) {
			((Detail) objProgram).bInLineEdit = bInLineEdit;
			((Detail) objProgram).ENTITY_CLASSNAME = sEntityClassName;
			((Detail) objProgram).ENTITY_PATH = sEntityPath;
		}
	}
	

	/**
	 * 重新載入entity 延遲加載
	 * @param objDomain domain entity
	 */
	public static void entityRefresh(Object objDomain) {
		if (objDomain != null) {
			SessionFactory sessionFactory = ((HibernateEntityManagerFactory) ((CRUDService) Sessions.getCurrent().getAttribute("CRUDService")).getEmf()).getSessionFactory();
			Session session = ((SessionFactoryImpl) sessionFactory).getCurrentSession();
			Transaction transaction = (Transaction) session.beginTransaction();
			session.refresh(objDomain);
//			session.update(ee);
//			session.merge(ee);
//			Hibernate.initialize(objDomain);
			transaction.commit();			
		}

	}

	/**
	 * 顯示或隱藏Listbox之操作圖示(修改/刪除)
	 * @param dataMode
	 * @param listbox
	 * @param prgAuth
	 */
	public static void setListboxImageAuth(DataMode dataMode, Listbox listbox, ProgramAuth prgAuth) {
		String imgEdit = "/resource/imgs/icons/btn_edit2_16x16.gif";
		String imgDelete = "/resource/imgs/icons/btn_delete2_16x16.gif";
		
		if (listbox != null) {
			List<Listitem> list = listbox.getItems();
			for (Listitem item : list) {
				if (item.getFirstChild().getChildren().size() > 0){
					Component objHbox = item.getChildren().get(0).getChildren().get(0);
					if (objHbox != null && objHbox instanceof Hbox) {
						for (int i = 0; i < objHbox.getChildren().size(); i++) {
							if (((Hbox) objHbox).getChildren().get(i) instanceof Image) {							
								Image image = (Image) ((Hbox) objHbox).getChildren().get(i);
								if (dataMode.equals(DataMode.READ_MODE)) {
									if (image.getSrc().equals(imgEdit)) {
										image.setVisible(prgAuth.getAuthEdit());
									} else if (image.getSrc().equals(imgDelete)) {
										image.setVisible(prgAuth.getAuthDelete());
									}
								} else {
									if (image.getSrc().equals(imgEdit)) {
										image.setVisible(false);
									} else if (image.getSrc().equals(imgDelete)) {
										image.setVisible(false);
									}
								}
							}
						}	
					}
				}

			}
		}
	}
	
	
	/**
	 * Set In Line Edit
	 * @param objProgram	底層
	 * @param objSel		entity
	 * @param bEdit			是否為編輯模式
	 * @param lsCustList 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static void setInLineListbox(Object objProgram, Object objSel, boolean bEdit)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ListModelList lmlModel = null;
		List lsCustList = null;
		boolean bIsCustList = false;
		List lsEntity = null;
		Object EntitySel = null;
		Listbox listbox = null;
		boolean bListboxMulit = false;
		
		if (objProgram instanceof Master) {
			lmlModel = ((Master) objProgram).getMasterModel();
			bIsCustList = ((Master) objProgram).getIsCustList();
			lsCustList = ((Master) objProgram).getCustList();			
			lsEntity = ((Master) objProgram).masterList;
			EntitySel = ((Master) objProgram).getMasterSel();
			listbox = ((Master) objProgram).getmasterListbox();
			bListboxMulit = ((Master) objProgram).getListboxMulit();
		} else if (objProgram instanceof Detail) {
			lmlModel = ((Detail) objProgram).getDetailModel();
			bIsCustList = ((Detail) objProgram).getIsCustList();
			lsCustList = ((Detail) objProgram).getCustList();
			lsEntity = ((Detail) objProgram).detailList;
			EntitySel = ((Detail) objProgram).getDetailSel();
			listbox = ((Detail) objProgram).getDetailListbox();
			bListboxMulit = ((Detail) objProgram).getListboxMulit();
		}
		
		int idx = -1;  
		idx = lmlModel.indexOf(objSel);

		if (bIsCustList){
			List lstEdit = lsCustList;
			propertyWriteMethod(lstEdit.get(idx), "ISEDIT", bEdit);
			lmlModel = new ListModelList(lstEdit, true);
		} else {
			propertyWriteMethod(objSel, "ISEDIT", bEdit);
			lmlModel = new ListModelList(lsEntity, true);		 
		}

		if (idx == -1)
			EntitySel = null;
		else
			EntitySel = lmlModel.get(idx);
		
		listbox.setModel(lmlModel);
		listbox.renderAll();
		listbox.setMultiple(bListboxMulit);
		programmacticallySelectListbox(objProgram, idx);
	}
	
	
	/**
	 * 選定資料
	 * @param objProgram	底層
	 * @param index		資料index
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void programmacticallySelectListbox(Object objProgram, int index)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ListModelList Model = null;
		
		if (objProgram instanceof Master) {
			Model = ((Master) objProgram).masterModel;
			
		} else if (objProgram instanceof Detail) {
			Model = ((Detail) objProgram).detailModel;
		}
		
		Object ocv = null;
		if (Model.getSize() >= 1 && index >= 0) {
			ocv = Model.get(index);
			Model.addToSelection(ocv);

		}
		
		if (objProgram instanceof Master) {
			((Master) objProgram).onClickMasterListbox(ocv);
		} else if (objProgram instanceof Detail) {
			((Detail) objProgram).onClickDetailListbox(ocv);
		}
	}
	
	
	/**
	 * Listbox Listheader排序功能
	 * @param objProgram	底層
	 * @param event
	 */
	public static void orderByListheader(Object objProgram, Event event) {
		try {
			DataMode dataMode = null;
			String sOrderBy = "";
			ArrayList<String> KeyName = null;
			String sOrderByListhead = "";

			if (objProgram instanceof Master) {
				dataMode = ((Master) objProgram).getData_Mode();
				sOrderBy = ((Master) objProgram).getOrderBy();
				KeyName = ((Master) objProgram).getMasterKeyName();
			} else if (objProgram instanceof Detail) {
				dataMode = ((Detail) objProgram).getData_Mode();
				sOrderBy = ((Detail) objProgram).getOrderByDetail();
				KeyName = ((Detail) objProgram).getDetailKeyName();
			}

			if (!dataMode.equals(DataMode.READ_MODE)) {
				return;
			}

			sOrderByListhead = getOrderBy(event, sOrderBy, KeyName);

			if (!sOrderByListhead.isEmpty()) {
				if (objProgram instanceof Master) {
					((Master) objProgram).sOrderByListhead = sOrderByListhead;
					((Master) objProgram).setWhereConditionals(((Master) objProgram).getWhereConditionals());
					((Master) objProgram).doFillListbox(0);
				} else if (objProgram instanceof Detail) {
					((Detail) objProgram).sOrderByListhead = sOrderByListhead;
					((Detail) objProgram).doFillListboxDetail(0);
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 吐司訊息Show
	 * @param toast Div
	 * @param message 顯示訊息
	 * @param time 秒數
	 */
	public static void showToast(Div toast, String message, long time) {
		Div tmp = new Div();
		toast.setSclass("toast-container");
		toast.appendChild(tmp);
		Label label = new Label(message);
		tmp.appendChild(label);
		stopToast(toast, tmp, time);
		tmp.setSclass("toast toast-animation");
	}

	
	/**
	 * 吐司訊息Stop
	 * @param toast Div
	 * @param div 顯示訊息
	 * @param microsecond 秒數
	 */
	public static void stopToast(Div toast, Div div, long microsecond) {
		Desktop desktop = Executions.getCurrent().getDesktop();
		desktop.enableServerPush(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (desktop != null && desktop.isAlive()) {
					try {
						Executions.activate(desktop);
						div.setSclass("toast");
						toast.removeChild(div);
						Executions.deactivate(desktop);
					} catch (Exception e) {

					}
				}
			}
		}, (long) microsecond);
	}
	
	
	/**
	 * 取得dspb02資訊
	 * @param column 取得dspb02欄位
	 * @return 回傳該欄位之資料
	 */
	public static String getDSPB02Info(String column) {
		UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
		Connection conn = Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String strSQL = "SELECT " + column + " FROM DSPB02 WHERE PB_USERID='" + _userInfo.getAccount() + "'";
			ps = conn.prepareStatement(strSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getString(column);
			}
		} catch (Exception e) {
			return "";
		} finally {
			try {
				closeQuietly(conn, ps, rs);
			} catch (Exception e) {
				logger.error(null, e);
			}
		}
		return "";
	}

	/**
	 * Listbox多選功能，點擊listitem觸發
	 * @param objProgram 底層
	 * @param evt clickListbox
	 */
	public static void listboxMulitSel(Object objProgram, Event evt) {
		Listbox listbox = (Listbox) evt.getTarget();
		boolean bListboxMulit = false;
		boolean bIsCustList = false;
		ArrayList<Object> arrMulitSelect = null;
		ListModelList Model = null;
		
		if (objProgram instanceof Master) {
			bListboxMulit = ((Master) objProgram).getListboxMulit();
			bIsCustList = ((Master) objProgram).getIsCustList();
			arrMulitSelect = ((Master) objProgram).getListboxMulitSelect();
			Model = ((Master) objProgram).getMasterModel();
		} else if (objProgram instanceof Detail) {
			bListboxMulit = ((Detail) objProgram).getListboxMulit();
			bIsCustList = ((Detail) objProgram).getIsCustList();
			arrMulitSelect = ((Detail) objProgram).getListboxMulitSelect();
			Model = ((Detail) objProgram).getDetailModel();
		} else if (objProgram instanceof QueryBase) {
			bListboxMulit = ((QueryBase) objProgram).getListboxMulit();
			arrMulitSelect = ((QueryBase) objProgram).getListboxMulitSelect();
			Model = ((QueryBase) objProgram).queryModel;
		} else if (objProgram instanceof QueryWindow) {
			bListboxMulit = ((QueryWindow) objProgram).getListboxMulit();
			arrMulitSelect = ((QueryWindow) objProgram).getListboxMulitSelect();
			Model = ((QueryWindow) objProgram).queryModel;
		} else if (objProgram instanceof QueryWindowField) {
			bListboxMulit = ((QueryWindowField) objProgram).getListboxMulit();
			arrMulitSelect = ((QueryWindowField) objProgram).getListboxMulitSelect();
			Model = ((QueryWindowField) objProgram).queryModel;
		}
		
		if (bListboxMulit) {
			if (listbox.getSelectedItems().size() > 0) {
				for (Listitem ltAll : listbox.getItems()) {
					if (ltAll.isSelected() == false ) {
						for (int i = 0; i < arrMulitSelect.size(); i++) {
							if (getDiffField(ltAll.getValue(), arrMulitSelect.get(i)) == null) {
								arrMulitSelect.remove(i);
							}
						}
					}
					if (ltAll.isSelected()) {							
						boolean bExist = false;
						for (int i = 0; i < arrMulitSelect.size(); i++) {
							if (getDiffField(ltAll.getValue(), arrMulitSelect.get(i)) == null) {
								bExist = true;
							}
						}

						if (!bExist)
							arrMulitSelect.add((Object) ltAll.getValue());
					}
				}
			} else {
				for (Listitem ltAll : listbox.getItems()) {
					for (int i = 0; i < arrMulitSelect.size(); i++) {
						if (getDiffField(ltAll.getValue(), arrMulitSelect.get(i)) == null) {
							arrMulitSelect.remove(i);
						}
					}
				}
			}
		} else {
			for (Listitem ltAll : listbox.getItems()) {
				if (ltAll.isSelected()) {
					arrMulitSelect.clear();
					arrMulitSelect.add((Object) ltAll.getValue());
				}
			}
		}
				
	}
	
	/**
	 * get Object Class Logger
	 * @param obj
	 * @return Logger
	 */
	public static Logger getLogger(Object obj) {		
		Logger logger = Logger.getLogger(obj.getClass());
		return logger;
	}
	
	/**
	 * 比對物件
	 * @param objA Ojbect A
	 * @param objB Ojbect B
	 * @return 物件內容不一致，回傳差異欄位
	 */
	public static List<ArrayList<Object>> getDiffField(Object objA, Object objB) {
		try {
			if (objA.getClass().getName().equals(objB.getClass().getName())) {
				Field[] fields_a = objA.getClass().getDeclaredFields();
				Field[] fields_b = objB.getClass().getDeclaredFields();

				List<ArrayList<Object>> list = new ArrayList<>();
				
				//屬於Object
				if (fields_a.length == 0) {
					int iLength = 0;
					
					//若object column數不一致，取最小的
					iLength = (Array.getLength(objA) <= Array.getLength(objB) ? Array.getLength(objA) : Array.getLength(objB));

					for (int i = 0; i < iLength; i++) {
						if (!Array.get(objA, i).equals(Array.get(objB, i))) {
							ArrayList<Object> sub = new ArrayList<>();
							sub.add(i);
							sub.add(Array.get(objA, i));
							sub.add(Array.get(objB, i));
							list.add(sub);
						}
					}
				} else {
					for (Field field_a : fields_a) {
						field_a.setAccessible(true);
						Object value_a = field_a.get(objA);
						
						//避開join之domain class
						if (field_a.getGenericType().getTypeName().contains("domain")) {
							continue;
						}
							
						for (Field field_b : fields_b) {
							field_b.setAccessible(true);
							Object value_b = field_b.get(objB);

							//避開join之domain class
							if (field_b.getGenericType().getTypeName().contains("domain")) {
								continue;
							}
							
							if (field_a.getName().equals(field_b.getName())) {
								System.out.println("field:" + field_a.getName() + ", value_a:" + value_a + ", value_b:" + value_b);
								if ((value_a == null && value_a != value_b) || (value_a != null && !value_a.equals(value_b))) {
									ArrayList<Object> sub = new ArrayList<>();
									sub.add(field_a.getName());
									sub.add(value_a);
									sub.add(value_b);
									list.add(sub);
								}
								break;
							}
						}
					}
				}
				
				//如果物件內容不一致，回傳差異欄位
				if (list.size() > 0) {
					return list;
				} else {
					return null;
				}

			}
		} catch (Exception e) {
			logger.error(null, e);
		}
		return null;
	}
	
	/**
	 * 存檔前檢查
	 * @param objProgram 底層
	 * @param entity 資料物件
	 * @return 檢查無問題，回傳TRUE
	 */
	public static boolean beforeSave(Object objProgram, Object entity) {		
		try {
			Listbox listbox = null;
			Class EntityClass = null;

			if (objProgram instanceof Master) {
				listbox = ((Master) objProgram).getmasterListbox();
				EntityClass = ((Master) objProgram).getEntityClass();
			} else if (objProgram instanceof OpenWinCRUD) {
				EntityClass = ((OpenWinCRUD) objProgram).getEntityClass();
			} else if (objProgram instanceof Detail) {
				listbox = ((Detail) objProgram).getDetailListbox();
				EntityClass = ((Detail) objProgram).getDetailClass();
			}

			if (listbox != null) {
				Listheader hd = null;
				String columnId = null;
				String columnLabel = null;
				Length columnLengthObj = null;
				Object columnValue = null;
				int i = -1;
				Method lengthMethod = null;
				Method entityMethod = null;
				listbox.getSelectedIndex();
				for (Object child : listbox.getListhead().getChildren()) {
					hd = (Listheader) child;
					columnLabel=hd.getLabel();
					columnId=hd.getId().replace("lh", "");
					i =hd.getId().indexOf("lh");
					if(i>-1){
						lengthMethod = EntityClass.getDeclaredMethod("get"+columnId);
						if(lengthMethod!=null){
							columnLengthObj = lengthMethod.getAnnotation(Length.class);
							if(columnLengthObj != null) {
								//判斷長度是否過短或過長
								entityMethod=entity.getClass().getMethod("get"+columnId,new Class[] {});
								columnValue = entityMethod.invoke(entity, new Object[] {});
								
								int ilength = 0;
								if (entityMethod.getReturnType().equals(String.class)) {
									if (columnValue != null) {
										ilength = ((String) columnValue).getBytes("utf-8").length;
									}									
									
									if(ilength < columnLengthObj.min()){
										Messagebox.show(columnLabel+(Labels.getLabel(columnLengthObj.message())==null ? Labels.getLabel("PUBLIC.MSG0112")+"\r\n"+Labels.getLabel("PUBLIC.MSG0108")+columnLengthObj.min()/3+"\r\n"+Labels.getLabel("PUBLIC.MSG0109")+columnLengthObj.min():Labels.getLabel(columnLengthObj.message())), "Error", Messagebox.OK, Messagebox.ERROR);
										return false;
									}
									
									if(ilength > columnLengthObj.max()){
										Messagebox.show(columnLabel+(Labels.getLabel(columnLengthObj.message())==null ? Labels.getLabel("PUBLIC.MSG0112")+"\r\n"+Labels.getLabel("PUBLIC.MSG0106")+columnLengthObj.max()/3+"\r\n"+Labels.getLabel("PUBLIC.MSG0107")+columnLengthObj.max():Labels.getLabel(columnLengthObj.message())), "Error", Messagebox.OK, Messagebox.ERROR);
										return false;
									}
								}						
							}
						}
					}
				}
			}

			return true;
		} catch (Exception e) {
			logger.error(null, e);
			return false;
		} 
	}

	/***
	 * 刪除功能
	 * @param objProgram 底層
	 * @param objDelete 刪除的物件
	 * @param conn Connection
	 * @param ut 
	 * @throws Exception
	 */
	public static void doDelete(Object objProgram, Object objDelete, Connection conn, UserTransaction ut) throws Exception {
		ListModelList model = null;
		int	DetailCount = 0;	
		List<Detail> lsDetail = null;
		ArrayList<String> keyName = new ArrayList<>();
		ArrayList<String> keyValue = new ArrayList<>();
		String ENTITY_CLASSNAME = null;
		boolean bdoDeleteAfter = true;
		Paging paging = null;
		int MY_PAGE_SIZE = 0;
		
		if (objProgram instanceof Master) {
			model = ((Master) objProgram).getMasterModel();
			DetailCount = ((Master) objProgram).DetailCount;
			lsDetail = ((Master) objProgram).getArrDetailPrograms();
			keyName = ((Master) objProgram).getMasterKeyName();
			keyValue = ((Master) objProgram).getMasterKeyValue(objDelete);
			ENTITY_CLASSNAME = ((Master) objProgram).ENTITY_CLASSNAME;
			paging = ((Master) objProgram).getMasterPaging();
			MY_PAGE_SIZE = ((Master) objProgram).getMY_PAGE_SIZE();
		} else if (objProgram instanceof OpenWinCRUD) {
			DetailCount = ((OpenWinCRUD) objProgram).DetailCount;
			lsDetail = ((OpenWinCRUD) objProgram).getArrDetailPrograms();
			keyName = ((OpenWinCRUD) objProgram).getKeyName();
			keyValue = ((OpenWinCRUD) objProgram).getKeyValue(objDelete);
			ENTITY_CLASSNAME = ((OpenWinCRUD) objProgram).ENTITY_CLASSNAME;
		} else if (objProgram instanceof Detail) {
			model = ((Detail) objProgram).getDetailModel();
			keyName = ((Detail) objProgram).getDetailKeyName();
			keyValue = ((Detail) objProgram).getDetailKeyValue(objDelete);
			ENTITY_CLASSNAME = ((Detail) objProgram).ENTITY_CLASSNAME;
			paging = ((Detail) objProgram).getDetailPaging();
			MY_PAGE_SIZE = ((Detail) objProgram).getMY_PAGE_SIZE();
		}
		
		try {	
			int idxDel = (model == null ? 0 : model.indexOf(objDelete));
			//次檔
			String strWhere = "";
			int countArray = 1;
			if (DetailCount > 0) {
				for (int i = 0; i < DetailCount; i++) {
					if (lsDetail.get(i).ENTITY_CLASSNAME.length() > 0)
						countArray++;
				}
			}
			
			String[] arrSQL = new String[countArray];
			for (int i = 0; i < keyName.size(); i++) {
				strWhere += " and t." + keyName.get(i) + "= :" + keyName.get(i);
			}	
			//刪除次檔資料
			if (DetailCount > 0) {
				int j = 0;
				for (int i = 0; i < DetailCount; i++) {
					if (lsDetail.get(i).ENTITY_CLASSNAME.length() > 0) {
						arrSQL[j] = "Delete From " + lsDetail.get(i).ENTITY_CLASSNAME.replace("VO", "") + " t Where 1=1 " + strWhere;
						j++;
					}
				}
			}

			//刪除主檔資料
			if (ENTITY_CLASSNAME.length() > 0) {
				// RS022150101很多筆 : 250
				arrSQL[countArray - 1] = "Delete From " + ENTITY_CLASSNAME.replace("VO", "") + " t Where 1=1 " + strWhere;
			}
			
			NameParameterStatement npst = null;
			for (int i = 0; i < arrSQL.length; i++) {
				npst = new NameParameterStatement(conn, arrSQL[i]);
				for (int j = 0; j < keyName.size(); j++) {
					npst.setObject(keyName.get(j), keyValue.get(j));
				}
				npst.execute();
			}
			
			if (objProgram instanceof Master) {
				bdoDeleteAfter = ((Master) objProgram).doDeleteAfter(conn);
			} else if (objProgram instanceof OpenWinCRUD) {
				bdoDeleteAfter = ((OpenWinCRUD) objProgram).doDeleteAfter(conn);
			} else if (objProgram instanceof Detail) {
				bdoDeleteAfter = ((Detail) objProgram).doDeleteAfter(conn);
			}

			if (bdoDeleteAfter) {
				ut.commit();
				conn.commit();
			} else {
				throw new Exception();
			}			

			if (objProgram instanceof Master || objProgram instanceof Detail) {
				model.remove(objDelete);

				if (model.getSize() >= 1) {
					final int idxDelNew = (idxDel >= 1) ? (idxDel - 1) : (0);
					
					if (objProgram instanceof Master) {
						((Master) objProgram).refreshListbox(model.get(idxDelNew));
					} else if (objProgram instanceof Detail) {
						((Detail) objProgram).refreshListbox(model.get(idxDelNew));
					}
				} else {// 回到上一頁
					int actPage = paging.getActivePage();
					int idx = (actPage == 0) ? (0) : ((actPage - 1) * MY_PAGE_SIZE);
					
					if (objProgram instanceof Master) {
						((Master) objProgram).doFillListbox(idx);
					} else if (objProgram instanceof Detail) {
						((Detail) objProgram).doFillListboxDetail(idx);
					}
				}				
			} else if (objProgram instanceof OpenWinCRUD) {
				((OpenWinCRUD) objProgram).setParentSel(null);
				((OpenWinCRUD) objProgram).resetComponent(null);
			}

		} catch (Exception e) {
			ut.rollback();
			conn.rollback();
			logger.error(null, e);
			throw e;
		}
		
	}
	
	
	/***
	 * InputStream 轉字串
	 * @param requestBodyStream
	 * @param charsetName
	 * @return 
	 * @throws Exception 
	 */
	public static String convertRequestBODY2String(InputStream requestBodyStream) throws Exception {
		int bufferSize = 1024;

		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			Reader in = new InputStreamReader(requestBodyStream, "UTF-8");
			for (;;) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				out.append(buffer, 0, rsz);
			}
			
			return out.toString();
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	
	/***
	 * 呼叫Web API 回傳值
	 * @param pAPIName API名稱
	 * @param pJson 傳入之Json格式
	 * @param sServletContext web.xml context-param
	 * @return 
	 * @throws Exception
	 */
	public static String getAPIResult(String pAPIName, JSONObject pJson, String sServletContext) throws Exception {
		try {
			ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
			String sAPIURL = sc.getInitParameter(sServletContext);
			String sResult = "";
			
			if (pAPIName.indexOf("/") == 0)
				pAPIName = pAPIName.substring(1);
			
			URL urlAPI = new URL(sAPIURL + pAPIName);
			URLConnection conn = urlAPI.openConnection();
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setDoOutput(true);
			
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			String message = pJson.toString();
			byte[] postData = message.getBytes(StandardCharsets.UTF_8);
			out.write(postData);
			out.close();
			
			InputStream response = conn.getInputStream();
			sResult = convertRequestBODY2String(response);
			return sResult;
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/***
	 * 取得entity Id
	 * @param entityClass entity 物件
	 * @return entity Id Field
	 */
	public static ArrayList<String> getKeyName(Class entityClass) {
		ArrayList<String> Key = new ArrayList<String>();

		try {
			Field[] declaredFields = entityClass.getDeclaredFields();

			for (Field field : declaredFields) {

				if (!field.getName().equals("serialVersionUID")) {
					if (field.isAnnotationPresent(Id.class)) {               
						//在field的annotion
						Key.add(field.getName());
					} else if (entityClass.getMethod("get"+field.getName(),new Class[] {}).isAnnotationPresent(Id.class)) {
						//在method裡面的annotion
						Key.add(field.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
		}

		return Key;
	}

	/***
	 * 取得key值資料
	 * @param objProgram 底層
	 * @param entity entity 物件
	 * @return key值資料
	 */
	public static ArrayList<String> getKeyValue(Object objProgram, Object entity) {
		ArrayList<String> KeyName = null;
		
		if (objProgram instanceof Master) {
			KeyName = ((Master) objProgram).getMasterKeyName();
		} else if (objProgram instanceof OpenWinCRUD) {
			KeyName = ((OpenWinCRUD) objProgram).getKeyName();
		} else if (objProgram instanceof Detail) {
			KeyName = ((Detail) objProgram).getDetailKeyName();
		} else if (objProgram instanceof QueryBase) {
			KeyName = ((QueryBase) objProgram).getKeyName();
		}
		
		ArrayList<String> KeyValue = new ArrayList<String>(); 
		
		try {
			for (int i = 0; i < KeyName.size(); i++) {
				KeyValue.add(String.valueOf(propertyReadMethod(entity, KeyName.get(i))));
			}

		} catch (Exception e) {
			logger.error(null, e);
		}

		return KeyValue;
	}
	
	/**
	 * 取得多語系table之語系欄位
	 * @param entityClass 
	 * @param sColumnName 需更換為local的欄位
	 * @return 登入語系之對應欄位
	 */
	public static String getLocalField(Object entity, String sColumnName) {
		try {
			return (String) propertyReadMethod(entity, sColumnName + (getLocale().equals("en") ? "" : "_" + getLocale().toUpperCase()));
		} catch (Exception e){
			logger.error(null, e);
		}
		return "";
	}
	
	/**
	 * 計算天數差
	 * @param c1 開始日
	 * @param c2 結束日
	 * @return 天數
	 */
	public static int countDiffDay(Date d1, Date d2) {
		int returnInt = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		DateTime start = new DateTime();
		DateTime end = new DateTime();
		start = start.parse(sdf.format(d1));
		end = end.parse(sdf.format(d2));
		returnInt = Days.daysBetween(new LocalDate(start), new LocalDate(end)).getDays();

		return returnInt + 1;
	}
	
	/**
	 * 關閉物件
	 * @param object ResultSet, Statement, PreparedStatement, NameParameterStatement, Connection, BitronixTransactionManager
	 */
	public static void closeNps(Object... object) {
		for (Object obj : object) {
			try {
				close(obj);
			} catch (Exception e) {
				logger.error(null, e);
				continue;
			}
		}
	}
	
	/**
	 * 取得Connenction from EntityManagerFactory
	 * @param emf EntityManagerFactory
	 * @return Connenction
	 */
	public static Connection getDbConnection(EntityManagerFactory emf) {
		SessionFactory sessionFactory = ((HibernateEntityManagerFactory) emf).getSessionFactory();
		
		Properties properties = ((SessionFactoryImpl) sessionFactory).getProperties();
		String driverClass = (String) properties.get("hibernate.connection.driver_class");
		String url = (String) properties.get("hibernate.connection.url");
		String userName = (String) properties.get("hibernate.connection.username");
		String passWord = (String) properties.get("hibernate.connection.password");
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(passWord);
		dataSource.setMaxIdle(1);	//最大空閒連接  
        dataSource.setMaxActive(1); //最大活動連接 
        dataSource.setTimeBetweenEvictionRunsMillis(5000);	//定時對線程池中的鏈接進行validateObject校驗，對無效的鏈接進行關閉後，會調用ensureMinIdle，適當建立鏈接保證最小的minIdle連接數。
        dataSource.setMinEvictableIdleTimeMillis(5000);	//連接池中連接，在時間段內一直空閒，被逐出連接池的時間
		try{		
			return dataSource.getConnection();
		} catch (SQLException e){
			logger.error(null, e);
		}
		return null;
	}

	/**
	 * 取得目前Session裡面的CRUDService
	 * @return CRUDService
	 */
	public static CRUDService getCRUDService() {
		return (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
	}
	
	/**
	 * 取得登入中資料庫的Connection
	 * @param sCRUDService CRUDService名稱
	 * @return Connection
	 */
	public static Connection getDbConnection(String sCRUDService) {
		CRUDService CRUDService = (CRUDService) SpringUtil.getBean(sCRUDService);
		if (CRUDService != null) {
			return getDbConnection(CRUDService.getEmf());
		} else {
			logger.error("Can not get Db Connection");
			return null;
		}
	}
}
