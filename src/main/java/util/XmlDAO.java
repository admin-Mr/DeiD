package util;
import java.awt.Dimension;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import ds.common.services.CRUDService;
import javafx.application.Application;
import util.function.XmlUtil;
import util.openwin.QryField;


/**
 * 以 XML 定義查詢敘述，建立連線至資料庫進行查詢，取回查詢結果資料集.
 * 程式執行步驟:
 * <ol>
 * <li>執行{@link #setFilterCondition setFilterCondition}設定查詢過濾條件，條件字串請參考JDBC PreparedStatment說明。</li>
 * <li>執行{@link #doQueryData doQueryData}建立連線至資料庫進行查詢。</li>
 * <li>執行{@link #getResultSet getResultSet}取得查詢結果資料集，以<code>ArrayList</code>存放。</li>
 * </ol>
 * 
 * @author Vivian Liaw
 */
public class XmlDAO {
	@WireVariable
	private CRUDService CRUDService;
	/**
	 * 表示查詢結果無資料
	 */
	public static final int RET_NoData = 0;
	
	/**
	 * 表示查詢結果有資料
	 */
	public static final int RET_OK = 1;
	
	/**
	 * 表示查詢資料庫時發生錯誤
	 */
	public static final int RET_DBError = -1;
	
	/**
	 * 表示處理 XML 定義檔時發生錯誤
	 */
	public static final int RET_XmlFileError = -2;
	
	//localization resource bundle
	private ResourceBundle resourceBundle_TableField;
	private ResourceBundle resourceBundle_CommonMsg;

	private ArrayList resultSet = new ArrayList();
	private ArrayList<QryField> qryField = new ArrayList();
	private HashMap fieldIndex = new HashMap();
//	private SqlQueryStringUtil sqlQryStr = new SqlQueryStringUtil();

	private int statusNo = 0;
	private String errorMsg = "";
	
	private String sqlStatment = "";		//SQL基本 Select
	private String sqlBaseCondition = "";	//SQL基本條件
	private String sqlOrderBy = "";	    	//SQL排序
	
	private String filter = "";				//過濾條件
	private Object[] filterParams;			//過濾條件的參數

	private String windowLocation = "100,200";
	private String windowSize = "500x400";
	protected Logger logger = BlackBox.getLogger(this);
	public XmlDAO() {
		
	}
	
	/**
	 * Create a new <code>XmlDAO</code>.
	 * @param queryID 查詢來源代碼
	 * @throws Exception 
	 */
	public XmlDAO(String queryID) throws Exception {
		//Set localization resource bundle.
		
		//Get XML file of query definition.
		if(!getQryWinDefinition("/conf/xmlquery/" + queryID + ".xml")) {
			throw new Exception("find not found :" + "/conf/xmlquery/" + queryID + ".xml");
		}
		logger.info("Reading Xml Path:"+"/conf/xmlquery/" + queryID + ".xml");
	} 
	
	
	
	/**
	 * 取得基本過濾條件
	 * @return
	 */
	public String getSqlBaseCondition() {
		return sqlBaseCondition;
	}
	/**
	 * 設定基本過濾條件
	 * **/
	public void setSqlBaseCondition(String sqlBaseCondition) {
		this.sqlBaseCondition = sqlBaseCondition;
	}

	/**
	 * 設定查詢代號
	 *
	 * @param queryID
	 * @return
	 */
	public boolean setupQueryID(String queryID) {
		return getQryWinDefinition("/conf/xmlquery/" + queryID + ".xml");
	}//setupQueryID
	
	/**
	 * 設定查詢過濾條件.
	 * @param filterCondition 查詢過濾條件
	 */
	public void setFilterCondition(String filterCondition) {
//		sqlQryStr.setFilterCondition(filterCondition);
		filter = filterCondition;
	}
	
	/**
	 * 設定錯誤訊息.
	 * @param value 錯誤訊息
	 */
	public void setErrorMessage(String value) {
		errorMsg = value;
	}
	
	/**
	 * 取得錯誤訊息.
	 * @return 錯誤訊息
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
	
	/**
	 * 取得 SQL 查詢敘述.
	 * @return SQL 查詢敘述
	 */
	public String getSqlQueryString() {
		//Query query=CRUDService.createSQL(sqlStatment);
		ArrayList<String> fieldList=new ArrayList<>();
		for (QryField field : qryField) {
			if(field.getType().equals("S")){
				fieldList.add(field.getFieldName());
				//fieldList.add("NVL("+field.getFieldName()+",' ') "+field.getFieldName());
			}else if(field.getType().equals("D")){
				fieldList.add("NVL(TO_CHAR("+field.getFieldName()+",'YYYY/MM/DD'),' ')"+field.getFieldName());
			}else if(field.getType().equals("N")){
				fieldList.add("NVL("+field.getFieldName()+",0) "+field.getFieldName());
			}
		}
		String sql =sqlStatment;
		String where = "";
		String order = sqlOrderBy;
		
		if ((sqlBaseCondition!=null) && (sqlBaseCondition.trim().length() > 0)) {
			where = sqlBaseCondition;
		}
		if ((filter!=null) && (filter.trim().length() > 0)) {
			if(where.length() > 0) {
				where = where + " and ";
			}
			where = where + "(" + filter + ")";
		}
		if (where.length() > 0) where = " where " + where;
		if (order.length() > 0) order = " order by " + order;
		
		sql = "SELECT "+fieldList.toString().replace("[","").replace("]","")+" FROM ("+sql + where+ ") "+order;
		return sql ;
	}
	
	/**
	 * 取得查詢結果資料集.
	 * 回傳的資料集以二層的<code>ArrayList</code>結構存放.
	 * 第一層<code>ArrayList</code>存放單筆資料.
	 * 第二層<code>ArrayList</code>存放單筆資料中的個別欄位.
	 * @return 查詢結果資料集
	 */
	public ArrayList getResultSet() {
		return resultSet;		
	}
	
	/**
	 * 取得查詢結果的資料筆數.
	 * @return 查詢結果的資料筆數
	 */
	public int getRowCount() {
		return resultSet.size();
	}	
	
	/**
	 * 取得查詢欄位.
	 * 回傳的<code>ArrayList</code>中存放的是<code>QryField</code>物件.
	 * @return 查詢欄位
	 */
	public ArrayList<QryField> getQryField() {
		return qryField;
	}
		
	/**
	 * 取得查詢設定的 XML 定義檔.
	 * @param definitionFileName XML 定義檔的檔案名稱
	 */
	private boolean getQryWinDefinition(String definitionFileName) {
		try {
			//讀入 XML 檔
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String file = classLoader.getResource(definitionFileName).getFile();
			Document doc = XmlUtil.openXmlFile(file, false);
			Element rootElement = doc.getDocumentElement();
			
			//讀取: 基本查詢條件、預設排序、基本查詢子句
			Element element = XmlUtil.getFirstChildren(rootElement, "SQL");
			if (element != null) {
				sqlBaseCondition = XmlUtil.getChildNodeText(element,"BaseCondition");
				sqlOrderBy = XmlUtil.getChildNodeText(element,"OrderBy");
				sqlStatment = XmlUtil.getChildNodeText(element,"SQLStatement");
				
				sqlStatment.replace("!", !BlackBox.getLocale().toUpperCase().equals("en")?"_"+BlackBox.getLocale().toUpperCase():""); //20180809 kobe新增多國語系
			}
			
			//讀取: 查詢欄位 (欄位名稱、顯示名稱、資料型態)
			element = XmlUtil.getFirstChildren(rootElement, "Fields");
			if (element != null) {
				Element elements[] = XmlUtil.getChildren(element, "Field");
				for(int i=0; i<elements.length; i++) {
					Element itemElement = elements[i];			
					String name = XmlUtil.getAttribute(itemElement, "Name");
					String width= XmlUtil.getAttribute(itemElement, "Width")!=null?XmlUtil.getAttribute(itemElement, "Width"):"";
					String caption;
					try {
						caption = resourceBundle_TableField.getString(XmlUtil.getAttribute(itemElement, "Caption"));
					}
					catch (Exception e) {
						caption = XmlUtil.getAttribute(itemElement, "Caption");
					}
					String type = XmlUtil.getAttribute(itemElement, "Type");
					
					QryField qf = new QryField(name, caption, type, i,width);
					qryField.add(qf);
					//依欄位名稱建立 HashMap，回傳資料時由 HashMap 取得 Index 作為由 ResultSet 取資料的 Column Index
					fieldIndex.put(name, qf);
				}
			}
			
			//讀取: UI: 視窗位置、視窗大小
			element = XmlUtil.getFirstChildren(rootElement, "UI");
			if (element != null) {
				Element winElement = XmlUtil.getFirstChildren(element, "Window");
				if (winElement != null) {
					String s = XmlUtil.getChildNodeText(winElement,"Location");
					if (Pattern.matches("[0-9]+,[0-9]+", s))
						windowLocation = s;
					s = XmlUtil.getChildNodeText(winElement,"Size");
					if (Pattern.matches("[0-9]+x[0-9]+", s))
						windowSize = s;
				}
			}
			return true;
 		}
		catch (Exception e) {
			statusNo = RET_XmlFileError;
			setErrorMessage(resourceBundle_CommonMsg.getString("Generic.MSG.XmlFileError"));
			System.out.println("err::"+e);
			return false;
		}
	}
	/**
	 * 取得XML上定義的視窗位置
	 * @return
	 */
	public Point getWindowLocation() {
		String[] s = windowLocation.split(",");
		return new Point(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
	}

	/**
	 * 取得XML上定義的視窗大小
	 * @return
	 */
	public Dimension getWindowSize() {
		String[] s = windowSize.split("x");
		return new Dimension(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
	}
/*
	public void addBaseCondition(String baseCondition) {
		if (!baseCondition.equals(""))
			sqlQryStr.setBaseCondition(baseCondition);
	}
*/	
	/**
	 * 執行資料庫查詢程序.
	 * @return 查詢結果狀態碼
	 */
	public int doQueryData(Connection conn) {
		if (statusNo >= 0) {
	        ResultSet rsData = null;
//	        Statement stmtData = null;
	        PreparedStatement pstmtData = null;
	        
	        String sql = getSqlQueryString();
	        System.out.println("doQueryXML:"+sql);
	        try {	        	
	            pstmtData = conn.prepareStatement(sql,
	            		ResultSet.TYPE_SCROLL_INSENSITIVE,//ResultSet.TYPE_SCROLL_SENSITIVE,
	            		ResultSet.CONCUR_READ_ONLY
	            		);
	            
	            //設定查詢最大資料筆數
	            //pstmtData.setMaxRows(Application.getApp().getSystemInfo().getQueryMaxRecord());

	            //查詢 ResultSet
//	            rsData = stmtData.executeQuery(sqlQryStr.getSqlQueryString());
	            //設定參數
	            for(int i=0; filterParams != null && i<filterParams.length; i++) {
		            pstmtData.setString(i+1, filterParams[i].toString());
	            }
	            rsData = pstmtData.executeQuery();
	
	            //依 XML 檔所定義的顯示欄位依序將查詢結果存放至 ArrayList[ArrayList] 結構中
	            resultSet.clear();
	            String value="";
	            while(rsData.next()) {
	            	ArrayList row = new ArrayList();
	            	for(int i=0;i<qryField.size();i++) {
	            		value=rsData.getString(((QryField)qryField.get(i)).getFieldName());
	            		try {
	            			if(value!=null)
	            				row.add(rsData.getString(((QryField)qryField.get(i)).getFieldName()));
	            			else
	            				row.add("");
	            		}
	            		catch (Exception e2) {
	            			//查無該欄位時填入空白
	            			row.add("");
	            		}	            		
	            	}
	            	resultSet.add(row);
	            }
	            
	            //依查詢結果資料筆數設定狀態碼
	            if (resultSet.size() > 0)
	            	statusNo = RET_OK;
	            else
	            	statusNo = RET_NoData;
	        }
	        catch (Exception e) {
	        	statusNo = RET_DBError;
	        	setErrorMessage(resourceBundle_CommonMsg.getString("Generic.MSG.DBError"));
	        	e.printStackTrace();
	        }
	        finally {
	            try {
	                if(rsData != null) { rsData.close(); rsData = null; }
	                if(pstmtData != null) {pstmtData.close(); pstmtData = null; }
	            }
	            catch (Exception e) {
	            }
	        }
		}
        
        return statusNo;
	}

	/*public int doQueryData() {
		if (statusNo >= 0) {
			Connection conn= null;
	        try {	        	
	        	conn = Application.getApp().getConnection();
	        	return doQueryData(conn);
	        }
	        catch (Exception e) {
	        	statusNo = RET_DBError;
	        	setErrorMessage(resourceBundle_CommonMsg.getString("Generic.MSG.DBError"));
	        }
	        finally {
	            try {
	                Application.getApp().closeConnection(conn);
	            }
	            catch (Exception e) {
	            }
	        }
		}
        
        return statusNo;
	}*/

	public Object getRowFieldData(int rowIndex, String fieldName) {
		QryField qf = (QryField) fieldIndex.get(fieldName);
		if (qf == null) return null;
		return getRowFieldData(rowIndex, qf.getIndex());
	}
	
	public Object getRowFieldData(int rowIndex, int fieldIndex) {
		ArrayList data = (ArrayList)resultSet.get(rowIndex);
		if(data != null) {
			Object o = data.get(fieldIndex);
			if (o == null) return "";
			else return o;
		}
		else return null;
	}
	
	public static XmlDAO doit(String queryID, String condition) {
		try {
			XmlDAO v = new XmlDAO(queryID);
			if (v.statusNo < 0) return null;
			v.setFilterCondition(condition);
			//if (v.doQueryData() > 0 ) return v;
			//else return null;
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static XmlDAO doit(Connection conn, String queryID, String condition) {
		try {
			XmlDAO v = new XmlDAO(queryID);
			if (v.statusNo < 0) return null;
			v.setFilterCondition(condition);
			if (v.doQueryData(conn) > 0 ) return v;
			else return null;
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
