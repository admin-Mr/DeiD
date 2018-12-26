package ds.dsid.program;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.dsid.domain.DSIDN10VO;
import util.OperationMode;

public class DSIDN10M_Program extends COMM_Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire private Datebox querystartdate,queryenddate;
	@Wire
	private Textbox txt_UNIQUEID ,txt_FU_ID    ,txt_MU         ,txt_PART_NAME,
					txt_VENDOR   ,txt_CP_NAME  ,txt_EL_NAME    ,txt_COLOR_CODE,
					txt_STATUS   ,txt_FU_IDSQL ,txt_MODEL_NAME ,txt_YIELD,txt_MODEL_NA;
					
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnExport,btnClear, btnPB_MUNODE,aaa;

	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		 CRUDService CRUDServiceabc  = (CRUDService) SpringUtil.getBean("CRUDService2");
		 setCRUDService(CRUDServiceabc);
	}
	
	/**
	 * 必須複寫方法二 getRootWindow
	 */
	@Override
	protected Window getRootWindow() {
		return windowMaster;
	}

	/**
	 * 必須複寫方法三 getMasterClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMasterClass() {
		return DSIDN10M_Program.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSIDN10VO.class;
	}
	
	

	@Override
	protected String getEntityName() {
		return "DSIDN10VO";
	}

	/**
	 * 必須複寫方法五 getOperationMode
	 */
	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}
	
	/**
	 * 必須複寫方法六 getMasterKeyName
	 */
	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("MODEL_NA");
		masterKey.add("EL_NO");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSIDN10VO entity = (DSIDN10VO) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getEL_NO());
		return masterKeyValue;
	}

	/**
	 * 必須複寫方法八
	 */
	@Override
	protected String getPagingId() {
		return "pagingCourse";
	}

	/**
	 * 必須複寫方法九
	 */
	@Override
	protected String getWhereConditionals() {
		String strSQL = " and MODEL_NA is not null ";
		
		if (!txt_MODEL_NA.getValue().trim().isEmpty()){
			strSQL += " and  MODEL_NA ='"+txt_MODEL_NA.getValue()+"'";
		}
		
		
		if(querystartdate.getValue() != null && queryenddate.getValue()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			strSQL += " and  TO_CHAR(up_date,'YYYY/MM/DD')  BETWEEN '"
			+sdf.format(querystartdate.getValue())
			+"' AND  '"+sdf.format(queryenddate.getValue())+"' ";
		}
		//strSQL +=" AND TO_CHAR(up_date,'YYYY/MM/DD') BETWEEN '2018/07/25' AND '2018/07/25'";
		
		
		if(txt_MODEL_NA.getValue().trim().isEmpty() && querystartdate.getValue() == null && queryenddate.getValue()==null)
		{
			strSQL += " and  t.MODEL_NA ='"+txt_MODEL_NA.getValue()+"'";
		}
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
	}
	
	/**
	 * 點擊新增按鍵時作業
	 */
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
	}
	@Listen("onOK=#txt_FU_IDSQL")
	public void onOKTextbox(){
		super.executeQuery();
	}
	
	/**
	 * 查詢按鈕, 也可以做開窗查詢
	 */
	@Listen("onClick = #btnImport")
	public void onClickbtnQuerya(Event event) {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("pbgroupid", "abcedd");
		Component con=  Executions.createComponents("/ds/dsid/DSID03M_Import01.zul", null, map);
		
	}
	
	/**
	 * 查詢按鈕, 也可以做開窗查詢
	 */
	@Listen("onClick = #btnExport")
	public void onClickbtnExport(Event event) {
		String sql ="";
		String strSQL = " where 1=1 ";
		sql += strSQL;
		sql += " order by  PC_REDATE DESC,to_number(PO_SEQ)";
		String [] title2 = {Labels.getLabel("DSIDN10.MODEL_NA"),Labels.getLabel("DSIDN10.EL_NO"),Labels.getLabel("DSIDN10.EL_CNAME"),"指令","總用量","總領料"};
		sql = "SELECT MODEL_NA,EL_NO,"
				+ "(SELECT EL_CNAME FROM DSEL00  WHERE DSEL00.EL_NO=DSID76.EL_NO) EL_CNAME,MT_QTY FROM DSID76 "
				+ "WHERE 1=1  "+getWhereConditionals()+"  order by model_na ";//and model_na='PEGASUS+35 MIN FA18 ID' 

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String starttime=sdf.format(querystartdate.getValue());
		String endtime =sdf.format(queryenddate.getValue());
		DSIDN10M_ExcelUtil.ExcelExportb(sql, "出庫統計", "出庫統計", title2,starttime,endtime);

	}
	
	
	
	/**
	 * 獲取序列號
	 * 
	 * 用於統計改時段端出庫的形體
	 */
	private static String  getNewMessage(String starttime,String endtime){
		List<String> tempList  = new ArrayList<String>();
		String incondition ="";
		Connection conn = DSIDN08M_DBManger.getConnectionDB1();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql ="SELECT EL_NO||MODEL_NA FROM DSID76 "+
				" WHERE TO_CHAR(up_date,'YYYY/MM/DD') "+
				" BETWEEN ? AND ? GROUP BY  EL_NO||MODEL_NA";
	    try{
			pst = conn.prepareStatement(sql);
			pst.setString(1, starttime);
			pst.setString(2, endtime);
			rs = pst.executeQuery();
			while(rs.next()){
				String file1 = rs.getString(1);
				incondition+="'"+file1+"',";
				tempList.add(file1);
			}
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }finally{
	    	DSIDN08M_DBManger.closeConnection(pst, conn, rs);
	    }
		
	    incondition = incondition.length()>1?incondition.substring(0, incondition.length()-1):"";
	    return incondition;
	}
	/**
	 * 統計用量
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	private  static Map<String,String> getNewMessage2(String starttime,String endtime){
		Map<String,String> tempmap = new  HashMap<String,String>();
		String  condition = getNewMessage(starttime,endtime);
		try {
			if (condition != null && condition.length() > 0) {
				CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
				String sql = "SELECT MODEL_NA ,SUM(YIELD) FROM ( "
						+ " SELECT EL_NO,MODEL_NA,SUM(YIELD) YIELD FROM DSID04_1 WHERE EL_NO||MODEL_NA " + " IN("
						+ condition + ") GROUP BY EL_NO,MODEL_NA" + " ) abc GROUP BY MODEL_NA";
				Query tempquery = CRUDServicetemp.createSQL(sql);
				List tempList = tempquery.getResultList();
				for (int i = 0; i < tempList.size(); i++) {
					Object[] obj = (Object[]) tempList.get(i);
					String key = "";
					String value = "";
					if (obj.length > 1) {
						key = String.valueOf(obj[0]);
						value = String.valueOf(obj[1]);
					}
					System.out.println(key + "===" + value);
					tempmap.put(key, value);
				}

				return tempmap;
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * 統計改時間段內的指令號
	 * 
	 * @return 返回序列
	 */
	private static Map<String,String>  getNewMessage3(String starttime,String endtime){
		Map<String,String> tempmap = new  HashMap<String,String>();
		Connection conn = DSIDN08M_DBManger.getConnectionDB1();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql ="SELECT count(OD_NO),REPLACE(MODEL_NA,'W ','') FROM DSID65 "
				+" WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD') BETWEEN ? AND ? "
				+" GROUP BY REPLACE(MODEL_NA,'W ','') ";
	    try{
			pst = conn.prepareStatement(sql);
			pst.setString(1, starttime);
			pst.setString(2, endtime);
			rs = pst.executeQuery();
			while(rs.next()){
				String file1 = rs.getString(1);
				String file2 = rs.getString(2);
				tempmap.put(file2, file1);
			}
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }finally{
	    	DSIDN08M_DBManger.closeConnection(pst, conn, rs);
	    }
	    return tempmap;
	}
	
	/**
	 *  統計總用量
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public static Map<String,String> getNewMessage4(String starttime,String endtime){
		Map<String,String> tempmap = new  HashMap<String,String>();
		Map<String,String> oneMap =getNewMessage3(starttime,endtime);
		Map<String,String> twoMap =getNewMessage2(starttime,endtime);
		if(twoMap != null && oneMap != null){
			for (String key : twoMap.keySet()) {
				String tempstr = oneMap.get(key);
				System.out.println(key);
				System.out.println(oneMap.get(key));
				System.out.println(twoMap.get(key));
				String tempvalue ="0";
				if(tempstr != null && tempstr.length()>0){
					tempvalue = String.valueOf(Double.parseDouble(twoMap.get(key))*Double.parseDouble(tempstr));
				}
				tempmap.put(key, tempvalue);
				System.out.println("key= "+ key + " and value= " + twoMap.get(key));
			 }
			return tempmap;
		}else{
			return null;
		}
	}
	/**
	 * 
	 * 統計指令號
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public static Map<String,String>  getNewMessage5(String starttime,String endtime){
		Map<String,String> tempmap = new  HashMap<String,String>();
		String incondition ="";
		Connection conn = DSIDN08M_DBManger.getConnectionDB1();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql ="SELECT OD_NO,REPLACE(MODEL_NA,'W ','') FROM DSID65  "
					+" WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD') "
					+" BETWEEN ? AND ? ORDER BY  REPLACE(MODEL_NA,'W ','')";
	    try{
			pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst.setString(1, starttime);
			pst.setString(2, endtime);
			rs = pst.executeQuery();
			int i =1;
			String tempstr ="";
			String substrtemp ="";
			
			  rs.last(); //结果集指针知道最后一行数据
			  int n = rs.getRow();
			//  System.out.println("---獲取最後一行--"+n);
			  rs.beforeFirst();
			while(rs.next()){
				String file1 = rs.getString(1);
				String file2 = rs.getString(2);
				if(i==0){
					tempstr = file2;
					substrtemp +=file1 +"、";
				}else{
					if(tempstr.equals(file2)){
						substrtemp +=file1 +"、";
					}else{
						tempmap.put(tempstr, substrtemp);
						tempstr = file2;
						substrtemp = "";
						substrtemp += file1 +"、";
					}
				}
				
				if(i==n){
					tempmap.put(tempstr, substrtemp);
				}
				
				i++;
			}
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }finally{
	    	DSIDN08M_DBManger.closeConnection(pst, conn, rs);
	    }
		
		
		for (String key : tempmap.keySet()) {
			System.out.println(key);
			System.out.println(tempmap.get(key));
			System.out.println();
			System.out.println();
		 }
	    return tempmap;
	}
	
	/**
	 * 查詢按鈕, 也可以做開窗查詢
	 */
	@Listen("onClick = #aaa")
	public void onClickbtnQueryaa(Event event) {
		String starttime="2018/07/25";
		String	endtime ="2018/07/25";
		getNewMessage5(starttime,endtime);
		getNewMessage4(starttime,endtime);
		
	}
	@Override
	public String getQueryResultSizeBase() {
		return "SELECT COUNT(t.MODEL_NA) FROM DSID76  t";
	}

	@Override
	public String getQueryPagingBase() {
		return  "SELECT t.MODEL_NA,t.EL_NO,"
				+ "(SELECT EL_CNAME FROM DSEL00  WHERE DSEL00.EL_NO=t.EL_NO) EL_CNAME,t.MT_QTY FROM DSID76 t ";
	}

	@Override
	public String getQueryOneColBase() {
		return "SELECT distinct  t.MODEL_NA FROM DSID76 t ";
	}
	
	@Override
	protected List getCustList() {
		List<DSIDN10VO> lst = new ArrayList<DSIDN10VO>();
		Query qry = super.getQueryPagingbySize();
		if (qry != null) {
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				Object[] obj = (Object[]) i.next();
				DSIDN10VO e = new DSIDN10VO();
				e.setMODEL_NA((String) obj[0]);
				e.setEL_NO((String) obj[1]);
				e.setEL_CNAME((String) obj[2]);
				e.setMT_QTY(String.valueOf((BigDecimal) obj[3]));
				lst.add(e);
			}
		}
		return lst;
	}
	@Listen("onChange = #querystartdate,#queryenddate")
	public void dateboxchange(Event event){
		String targetID = event.getTarget().getId();
		System.out.println("==================="+targetID);
		switch(targetID){
		case "querystartdate":
			if(querystartdate.getValue() == null)
				queryenddate.setValue(null);
			queryenddate.setValue(querystartdate.getValue());
			break;
		case "queryenddate":
			if(queryenddate.getValue() == null)
				querystartdate.setValue(null);
			if(querystartdate.getValue() == null&&queryenddate.getValue() != null)
				querystartdate.setValue(queryenddate.getValue());
			if(querystartdate.getValue() == null&&queryenddate.getValue() == null)
				break;
			int i = compare_date(queryenddate.getValue(), querystartdate.getValue());
			if(i == -1)
				queryenddate.setValue(querystartdate.getValue());
			break;
		}
	}
	 public static int compare_date( Date dt1, Date dt2) {
	        try {
	            if (dt1.getTime() > dt2.getTime()) {
	                System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	

}
