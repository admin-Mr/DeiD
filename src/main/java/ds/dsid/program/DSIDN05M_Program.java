package ds.dsid.program;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.SessionScope;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID05;
import ds.dsid.domain.DSIDN08;
import ds.dsid.domain.DSIDN08_T;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.OperationMode;

public class DSIDN05M_Program extends COMM_Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	
	@Wire
	private Window windowMaster;
	@Wire
	private Listbox abcListbox,abcListboxa,abcListboxd,masterListbox;
	//@Wire
	///private Textbox txt_UNIQUEID,txt_PO_NO,txt_PO_SEQ,txt_EL_NO,txt_EL_CNAME,
		//			txt_PO_NOSQL,txt_PO_SEQSQL,txt_PO_NOa,txt_PC_QTY,txt_Temp;
	@Wire
	private Textbox txt_UNIQUEID,txt_DAY_NO,txt_OD_NO,txt_MT_CODE,txt_MT_PONO,txt_MT_SEQ,txt_EL_NO,txt_MODEL_NA,txt_Temp,txt_PO_NOSQL,txt_PO_SEQSQL,txt_PO_NOa,txt_OD_NOSQL;
	@Wire private Doublebox txt_PO_QTY;//,txt_PC_QTY;
	//@Wire private Intbox txt_PC_QTY;
	@Wire private Div divadd,divedit,divshowdate,divcenter;
	@Wire private Center detailcenter;
	@Wire private Datebox querystartdate,queryenddate;
	@Wire private Paging  pagingCourse;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnClear,btnCleara, btnPO_NO,btnPO_NOa,btnImport,btnExport,btnQueryA;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	
	private boolean  Add=false;;
	
//	@Wire
	//private Intbox txtUNIQUEIDa;
	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//	UNIQUEID,DAY_NO,OD_NO,MT_CODE,MT_PONO,MT_SEQ,EL_NO,MODEL_NA,UP_USER,CREATEDATE,UP_DATE
		masterComponentColumns.add(new ComponentColumn<String>(txt_UNIQUEID, "UNIQUEID", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_DAY_NO, "DAY_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_OD_NO, "OD_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MT_CODE, "MT_CODE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MT_PONO, "MT_PONO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MT_SEQ, "MT_SEQ", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_DATE", sdf.format(new Date()), null, null, false));
		
		
		
		/*
		//---testListbox.setModel(testmodel);
		masterListbox.setItemRenderer(new testItemRenderer());

        //设置分页
		//--masterListbox.setMold("pagingCourse");
		pagingCourse.setAutohide(false);
		
		Listheader [] arraryListHeaders = {DAY_NO_Sort,PO_NO_Sort,PO_SEQ_Sort,EL_NO_Sort,EL_CNAME_Sort,PC_REDATE_Sort};
		String [] arraryFileds ={"DAY_NO","PO_NO","PO_SEQ","EL_NO","EL_CNAME","PC_REDATE"};
		
		for(int i=0;i<arraryListHeaders.length;i++){
			Listheader tempheader = arraryListHeaders[i];
			String filed=arraryFileds[i];
			tempheader.setSort("auto");
			tempheader.setSortAscending(new ComparatorHeader(filed));
			tempheader.setSortDescending(new ComparatorDescHeader(filed));
		}*/
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
		return DSIDN05M_Program.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID05.class;
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
		masterKey.add("UNIQUEID");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID05 entity = (DSID05) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getUNIQUEID());
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
		String strSQL = " and MT_PONO is not null ";
		if (!txt_PO_NOSQL.getValue().trim().isEmpty()){
			//String PO_NOSQL = getNHFinsh(txt_PO_NOSQL.getValue());
			strSQL += " and  MT_PONO ='"+txt_PO_NOSQL.getValue()+"'";
		}
		if (!txt_PO_SEQSQL.getValue().trim().isEmpty()){
			strSQL += " and  MT_SEQ ='"+txt_PO_SEQSQL.getValue()+"'";
		}
		if (!txt_OD_NOSQL.getValue().trim().isEmpty()){
			strSQL += " and  OD_NO ='"+txt_OD_NOSQL.getValue()+"'";
		}
		
		
		
		if(querystartdate.getValue() != null && queryenddate.getValue()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			strSQL += " and  CREATEDATE  between to_date('"+sdf.format(querystartdate.getValue())
			+" 00:00:00','yyyy/MM/dd hh24:mi:ss')  and  to_date('"+sdf.format(queryenddate.getValue())+" 23:59:00','yyyy/MM/dd hh24:mi:ss') ";
		}
		
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		/*
			<textbox id="txt_UNIQUEID" mold="rounded"
			<textbox id="txt_OD_NO" mold="rounded"
			<textbox id="txt_MT_PONO" mold="rounded"
			<textbox id="txt_MT_SEQ" mold="rounded"
			<textbox id="txt_EL_NO" mold="rounded"
			<textbox id="txt_MT_CODE"
			<textbox id="txt_MODEL_NA"
								
		 */
		DSID05 entity = (DSID05) entityMaster;
		txt_UNIQUEID.setValue((entity == null ? "" : entity.getUNIQUEID()));
		txt_OD_NO.setValue((entity == null ? "" : entity.getOD_NO()));
		txt_MT_PONO.setValue(entity == null ? "" : entity.getMT_PONO());
		txt_MT_SEQ.setValue(entity == null ? "" : entity.getMT_SEQ());
		txt_EL_NO.setValue(entity == null ? "" : entity.getEL_NO());
		txt_MT_CODE.setValue(entity == null ? "" : entity.getMT_CODE());
		txt_MODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		txt_DAY_NO.setValue(entity == null ? "" : entity.getDAY_NO());

		//txt_PO_QTY.setValue(entity == null ? null : entity.getPO_QTY());
		//txt_PC_QTY.setValue(entity == null ? null: entity.getPC_QTY());
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnExport.setDisabled(false);
		//txt_UNIQUEID,txt_DAY_NO,txt_OD_NO,txt_MT_CODE,txt_MT_PONO,txt_MT_SEQ,txt_EL_NO,txt_MODEL_NA
		txt_OD_NO.setReadonly(true);
		//txt_MT_CODE.setReadonly(true);
		txt_MT_PONO.setReadonly(true);
		txt_MT_SEQ.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_MODEL_NA.setReadonly(true);
		txt_UNIQUEID.setReadonly(true);
		txt_DAY_NO.setReadonly(true);
		txt_MODEL_NA.setReadonly(true);
		
		
		btnPO_NO.setDisabled(true);
		btnClear.setDisabled(true);
		
		btnPO_NOa.setDisabled(true);
		btnCleara.setDisabled(true);
	}
	
	/**
	 * 點擊新增按鍵時作業
	 */
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnExport.setDisabled(true);
		
		//txt_UNIQUEID,txt_DAY_NO,txt_OD_NO,txt_MT_CODE,txt_MT_PONO,txt_MT_SEQ,txt_EL_NO,txt_MODEL_NA

		//txt_UNIQUEID.setText(getSequenceid());
		txt_UNIQUEID.setReadonly(true);
		txt_OD_NO.setReadonly(true);
		txt_MT_CODE.setReadonly(true);
		txt_MT_PONO.setReadonly(true);
		txt_MT_SEQ.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_DAY_NO.setReadonly(true);
		txt_MODEL_NA.setReadonly(true);
		
		btnPO_NO.setDisabled(false);
		btnClear.setDisabled(false);
		
		btnPO_NOa.setDisabled(false);
		btnCleara.setDisabled(false);
	}
	@Listen("onOK=#txt_PO_NOSQL,#txt_PO_SEQSQL,#txt_OD_NOSQL")
	public void onOKPO_NOTextbox(){
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
		//Component con=  Executions.createComponents("/ds/test/DSPLM102M03.zul", null, map);
		Component con=  Executions.createComponents("/ds/dsid/DSID02M_Import01.zul", null, map);
		
	}
	
	@Listen("onClick = #btnPO_NOa")
 	public void onClickbtnPB_MUNODE(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("multiple", true);
		map.put("PO_NO", txt_PO_NOa.getValue());
		map.put("PO_NO_CONDITION", getHFinsh(txt_PO_NOa.getValue()));
		
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="Select t From DSID05 t  where 1=1  and t.MT_PONO='"+txt_PO_NOa.getValue()+"'  Order by  MT_SEQ";
		Query  tempquery = CRUDServicetemp.getBetweenByLimit2(sql);
		List	tempList = tempquery.getResultList();
		ListModelList  tempModel = new ListModelList(tempList, true);
		System.out.println("===sql=="+sql);
	    abcListboxd.setModel(tempModel);
	
	
	
		Executions.createComponents("/ds/dsid/DSIDN05M_Query.zul", null, map);
	}
	
	@Listen("onClick = #btnPO_NO")
 	public void onClickbtnPO_NO(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("multiple", false);
		//map.put("PO_NO", txt_OD_NO.getValue());
		map.put("returnMethod", "onQueryOD_NOSendUp_date");  
		Executions.createComponents("/ds/dsid/DSIDN05M_Query2.zul", null, map);
	}
	
	@Listen("onQueryOD_NOSendUp_date = #windowMaster")
	public void onQueryOD_NOSendUp_date(Event event){
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) event.getData();
			DSID05 e = (DSID05) map.get("selectedRecord");
			//divadd.setVisible(true);
			txt_OD_NO.setValue(e.getOD_NO());
		}
	
	private String getHFinsh(String PO_NO){
		PO_NO = getNHFinsh(PO_NO);
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="SELECT PO_NO,PO_SEQ,PO_QTY,SUM(PC_QTY) FROM DSIDN08  where PO_NO='"+PO_NO+"'  GROUP BY PO_NO,PO_SEQ,PO_QTY HAVING PO_QTY =SUM(PC_QTY)  ";
		Query  tempquery = CRUDServicetemp.createSQL(sql);
		List	tempList = tempquery.getResultList();
		String reHF ="";
		String tempHF="";
		
		for (Iterator i =tempList.iterator(); i.hasNext();) {
			Object[] obj = (Object[]) i.next();
			String tempPO_SEQ = (String) obj[1];
			System.out.println("====tempPO_SEQ===="+tempPO_SEQ);
			if(tempPO_SEQ != null && !"".equals(tempPO_SEQ))
				tempHF += "'"+tempPO_SEQ+"',";
		}
		if(tempHF.length()>1){
			System.out.println("------before-----"+tempHF);
			tempHF = tempHF.substring(0, tempHF.length()-1);
			System.out.println("------after-----"+tempHF);
			reHF = "("+tempHF+")";
		}
		
		return reHF;
	}
	
	
	
	private String getNHFinsh(String PO_PURNO){
		
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService2");
		String sql =" SELECT PO_NO FROM DSPO05 WHERE  PO_PURNO='"+PO_PURNO+"' OR PO_NO='"+PO_PURNO+"' ";
		Query  tempquery = CRUDServicetemp.createSQL(sql);
		List	tempList = tempquery.getResultList();
		String reHF ="";
		if(tempList !=null && tempList.size()>0)
			reHF = String.valueOf(tempList.get(0));
		
		return reHF;
	}
	
	
	
	@Listen("onClick = #btnCleara")
	public void onClickbtnbtnQueryA(Event event){
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="Select t From DSID05 t  where 1=1  and t.MT_PONO='"+txt_PO_NOa.getValue()+"'  Order by  MT_SEQ";
		Query  tempquery = CRUDServicetemp.getBetweenByLimit2(sql);
		List	tempList = tempquery.getResultList();
		ListModelList  tempModel = new ListModelList(tempList, true);
		System.out.println("===sql=="+sql);
	    abcListboxd.setModel(tempModel);
	}
	
	
	
	
	

	
	@Listen("onClick = #btnClear,#btnClear")
	public void onClickbtnClear(Event evnet){
		//txt_UNIQUEID,txt_DAY_NO,txt_OD_NO,txt_MT_CODE,txt_MT_PONO,
		//txt_MT_SEQ,txt_EL_NO
		//,txt_MODEL_NA,txt_Temp,txt_PO_NOSQL,txt_PO_SEQSQL,txt_PO_NOa;
		txt_OD_NO.setValue("");
		//txt_MT_CODE.setValue("");
		//txt_MT_PONO.setValue("");
		//txt_MT_SEQ.setValue("");
		//txt_EL_NO.setValue("");
		//txt_MODEL_NA.setValue("");
	}
	
	@Listen("onClick = #btnExport")
	public void onClickExport(Event evnet){
		String [] title={Labels.getLabel("DSID05.MT_PONO"),Labels.getLabel("DSID05.MT_SEQ"),
						 Labels.getLabel("DSID05.OD_NO"),Labels.getLabel("DSID05.EL_NO"),
						 Labels.getLabel("DSID05.MODEL_NA"),Labels.getLabel("DSID05.UP_USER"),
						 Labels.getLabel("DSID05.DAY_NO"),Labels.getLabel("DSID05.UP_DATE")
						 };
		String sql ="select MT_PONO,MT_SEQ,OD_NO,EL_NO,MODEL_NA,UP_USER,DAY_NO,UP_DATE from DSID05  ";
		String strSQL = " where 1=1 ";
		
		if (!txt_PO_NOSQL.getValue().trim().isEmpty()){
			//String PO_NOSQL = getNHFinsh(txt_PO_NOSQL.getValue());
			strSQL += " and  MT_PONO ='"+txt_PO_NOSQL.getValue()+"'";
		}
		if (!txt_PO_SEQSQL.getValue().trim().isEmpty()){
			strSQL += " and  MT_SEQ ='"+txt_PO_SEQSQL.getValue()+"'";
		}
		if (!txt_OD_NOSQL.getValue().trim().isEmpty()){
			strSQL += " and  OD_NO ='"+txt_OD_NOSQL.getValue()+"'";
		}
		
		
		
		if(querystartdate.getValue() != null && queryenddate.getValue()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			strSQL += " and  CREATEDATE  between to_date('"+sdf.format(querystartdate.getValue())
			+" 00:00:00','yyyy/MM/dd hh24:mi:ss')  and  to_date('"+sdf.format(queryenddate.getValue())+" 23:59:00','yyyy/MM/dd hh24:mi:ss') ";
		}
		sql += strSQL;
		sql += " order by  MT_PONO DESC,to_number(MT_SEQ)";
		DSIDN08M_ExcelUtil.ExcelExporta(sql,"已對應指令號", "已對應指令號材料", title);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event){
		Map<String, Object> map = (Map<String, Object>) event.getData();
		List<DSID05> e = (List<DSID05>) map.get("selectedRecord");
		divadd.setVisible(true);
		ListModelList  masterModel1 = new ListModelList(e, true);
		abcListbox.setModel(masterModel1);
	}

	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		
		/*String strPC_QTY = "";//txt_PC_QTY.getText();
		String strPO_QTY = "";//txt_PO_QTY.getText();
		String strPO_NO = txt_MT_PONO.getText();
		
		if(strPO_NO == null || strPO_NO ==""){
			Messagebox.show(Labels.getLabel("DSIDN08.MSG0008"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}
		
		if(strPC_QTY == null || strPC_QTY == ""){
			Messagebox.show(Labels.getLabel("DSIDN08.MSG0002"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}else{
		   Double douPC_QTY  = Double.valueOf(strPC_QTY);
		   Double douPO_QTY  = Double.valueOf(strPO_QTY);
		   Double douLPO_QTY = getLSum(txt_MT_PONO.getText(),txt_MT_PONO.getText());
		   Double douTLPO_QTY = douPO_QTY - douLPO_QTY;
		   
		   if(douPC_QTY.doubleValue() == 0){
			   Messagebox.show(Labels.getLabel("DSIDN08.MSG0007")+":"+douPC_QTY.doubleValue(), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			   return false;
		   }
		   if(douTLPO_QTY.doubleValue() == 0){
			  
		   }
		   if(douPC_QTY.doubleValue() > douPO_QTY.doubleValue())
			{
				Messagebox.show(Labels.getLabel("DSIDN08.MSG0001"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return false;
			}
		}*/
		return super.beforeMasterSave(entityMaster);
	}
	
	
	/**
	 * 取消鈕(新增/編輯),把狀態回復到瀏覽狀態(READ_MODE)
	 * 
	 * @param event
	 */
	@Listen("onClick = #btnCancelMaster")
	public void masterCancel(Event event) {
		Add=false;
		super.executeQuery();
		try {
			super.masterCancel(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnExport.setDisabled(true);
		
		divadd.setVisible(false);
		divedit.setVisible(true);
		divshowdate.setVisible(false);
		divcenter.setVisible(true);
		
		List<DSIDN08> empty = new ArrayList<DSIDN08>();
		
		ListModelList  emptyModel = new ListModelList(empty, true);
		
		abcListbox.setModel(emptyModel);
		
		List<DSID05> empty1 = new ArrayList<DSID05>();
		ListModelList  emptyModel2 = new ListModelList(empty1, true);
		abcListboxd.setModel(emptyModel);
		txt_PO_NOa.setText("");
	}

	/**
	 * 獲取序列號
	 * 
	 * @return 返回序列
	 */
	private String getSequenceid(){
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="select  seq_DSID05.Nextval from dual";
		Query  tempquery = CRUDServicetemp.createSQL(sql);
		Object	tempList = tempquery.getSingleResult();
		
		System.out.println("===sql=="+tempList);
	    
	    return String.valueOf(tempList);
	}

	private Double getLSum(String PO_NO,String PO_SEQ){
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="select SUM(PC_QTY) from DSIDN08 where   PO_NO='"+PO_NO+"' AND PO_SEQ='"+PO_SEQ+"'  ";
		Query  tempquery = CRUDServicetemp.createSQL(sql);
		List	tempList = tempquery.getResultList();
		
		Double LSum = 0.0 ;
		
		if(tempList != null && tempList.size()>0){
			try{
				LSum = Double.valueOf(String.valueOf(tempList.get(0)));
			}catch(Exception ex){
			}
		}
		return LSum;
	}
	
	
	private String getDAY_NO(){
		
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="select  decode(max(to_number(DAY_NO)),null,0,max(to_number(DAY_NO))) from DSID05 where to_char(CREATEDATE,'yyyyMMdd') = to_char(sysdate,'yyyyMMdd')";
		Query  tempquery = CRUDServicetemp.createSQL(sql);
		List	tempList = tempquery.getResultList();
		
		Integer LSum = 0 ;
		String reLSum = "";
		if(tempList!=null && tempList.size()>0)
			LSum = Integer.valueOf(String.valueOf(tempList.get(0)));
		LSum = LSum+1;
		Integer slen = String.valueOf(LSum).length();
		if(slen==1 )
			reLSum ="000"+LSum;
		else if(slen==2 )
			reLSum ="00"+LSum;
		else if(slen==3 )
			reLSum ="0"+LSum;
		else 
			reLSum =String.valueOf(LSum);
		return reLSum;
	}
	@Listen("onClick = #btnSaveMaster")
	@Override
	public boolean onClickbtnSaveMaster(Event event) {
		boolean isok = false;
		if(!Add){
			try {
				super.onClickbtnSaveMaster(event);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		System.out.println("======"+abcListbox.getItems());
		List<DSID05> templist=null;
		int i =0;
		F1:for(Listitem ltAll : abcListbox.getItems()){
			if(i==0)
				templist= new ArrayList<DSID05>();
			DSID05 ds05_t = ltAll.getValue();
			DSID05 ds05 = new DSID05();
			ds05 = ds05_t;
			System.out.println(ltAll.getValue().getClass());
			List<Component> list = ltAll.getChildren();
			for(Component commsons:list){
			   List<Component> listcomm = commsons.getChildren();
			   if(listcomm !=null && listcomm.size() >0){
				    for(Component  com:listcomm){
					   if(com  instanceof  org.zkoss.zul.Textbox){
						   Textbox comtextbox = (Textbox) com;
						   String strPC_QTY = comtextbox.getText();
							  System.out.println("====value===="+comtextbox.getText() 
									  +"=="+comtextbox.getValue()+"========textboxindex====");
							  System.out.println("10.0".equals(comtextbox.getText().toString() ));
							  
							  ds05.setOD_NO(strPC_QTY);
							  templist.add(ds05);
						  }
				   }
			   }
			}
			i++;
		}
		if(templist!= null){
			for(int x =0;x<templist.size();x++){
				DSID05 ds05 = templist.get(x);
				ds05.setUNIQUEID(getSequenceid());
				ds05.setUP_USER(_userInfo.getAccount());
				ds05.setDAY_NO(getDAY_NO());
				System.out.println(ds05.getOD_NO()+"============double=====");
				CRUDServicetemp.save(ds05);
			}
		}
		
		super.executeQuery();
		try {
			super.masterCancel(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txt_PO_NOa.setText("");
	//	ListModelList  masterModel1 = new ListModelList(null, true);
		//abcListbox.setModel(masterModel1);
		abcListbox.clearSelection();
		divadd.setVisible(false);
		divedit.setVisible(true);
		divshowdate.setVisible(false);
		divcenter.setVisible(true);
		List<DSID05> empty = new ArrayList<DSID05>();
		ListModelList  emptyModel = new ListModelList(empty, true);
		abcListbox.setModel(emptyModel);
		abcListboxd.setModel(emptyModel);
		Add = false;
		txt_PO_NOa.setText("");
		return isok;
	}
	
	@Listen("onClick = #btnCreateMaster")
    @Override
	public void masterCreate(Event event) {
		Add = true;
		divadd.setVisible(true);
		divedit.setVisible(false);
		divshowdate.setVisible(true);
		divcenter.setVisible(false);
		super.masterCreate(event);

	}
	@Listen("onChange = #querystartdate,#queryenddate")
	public void dateboxchange(Event event){
		String targetID = event.getTarget().getId();
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
	
	 public static int compare_date(String DATE1, String DATE2) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	                //System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	               // System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
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
	/*   給列表排序監聽
	 @Wire private  Listheader DAY_NO_Sort,PO_NO_Sort,PO_SEQ_Sort,EL_NO_Sort,EL_CNAME_Sort,PC_REDATE_Sort;
	 @Listen("onSort = #PO_NO_Sort")
	 public void masteronSort(Event event) {
		System.out.println( event.getData());
			System.out.println("sddsdsddsds");

		}
	 
	// @Listen("onClick = #PO_NO_Sort")
	 public void masteronClick(Event event) {
		System.out.println( event.getData());
			System.out.println("sddsdsddsds");

		}
	 
	
	 
	 
	    public class testItemRenderer implements ListitemRenderer<DSIDN08>{

	        @Override
	        public void render(Listitem item, DSIDN08 person, int index)
	                throws Exception {
	        	item.setValue(person);
	        	
	        	Hbox hb = new Hbox();
	        	hb.setSpacing("10px");
	        	Image imageupdate = new Image();
	        	imageupdate.setVisible(prgAuth.getAuthEdit());
	        	imageupdate.setSrc("/resource/imgs/icons/btn_edit2_16x16.gif");
	        	imageupdate.setTooltip(Labels.getLabel("COMM.EDIT"));
	        	imageupdate.addForward("onClick", masterListbox, "onUpdate");
	        	imageupdate.setSclass("fimage");
	        	Image imagedelete = new Image();
	        	imagedelete.setVisible(prgAuth.getAuthDelete());
	        	imagedelete.setSrc("/resource/imgs/icons/btn_delete2_16x16.gif");
	        	imagedelete.setTooltip(Labels.getLabel("COMM.DEL"));
	        	imagedelete.addForward("onClick", masterListbox, "onDelete");
	        	imagedelete.setSclass("fimage");
	        	hb.appendChild(imageupdate);
	        	hb.appendChild(imagedelete);
				
	        	Listcell  UNIQUEID_Cell =   new Listcell(person.getUNIQUEID());
	        	UNIQUEID_Cell.setParent(item);
	        	UNIQUEID_Cell.setValue(person.getUNIQUEID());
	        	
	        	Listcell  DAY_NO_Cell = new Listcell(person.getDAY_NO());
	        	DAY_NO_Cell.setValue(person.getDAY_NO());
	        	DAY_NO_Cell.setParent(item);
	        	
	        	Listcell  PO_NO_Cell = new Listcell(person.getPO_NO());
	        	PO_NO_Cell.setValue(person.getPO_NO());
	        	PO_NO_Cell.setParent(item);
	        	
	        	Listcell  PO_SEQ_Cell = new Listcell(person.getPO_SEQ());
	        	PO_SEQ_Cell.setValue(person.getPO_SEQ());
	        	PO_SEQ_Cell.setParent(item);
	        	
	        	Listcell  EL_NO_Cell = new Listcell(person.getEL_NO());
	        	EL_NO_Cell.setValue(person.getEL_NO());
	        	EL_NO_Cell.setParent(item);
	        	
	        	Listcell  EL_CNAME_Cell = new Listcell(person.getEL_CNAME());
	        	EL_CNAME_Cell.setValue(person.getEL_CNAME());
	        	EL_CNAME_Cell.setParent(item);
	        	
	        	Listcell  PO_QTY_Cell = new Listcell(String.valueOf(person.getPO_QTY()));
	        	PO_QTY_Cell.setValue(person.getPO_QTY());
	        	PO_QTY_Cell.setParent(item);
	        	
	        	Listcell  PC_QTY_Cell = new Listcell(String.valueOf(person.getPC_QTY()));
	        	PC_QTY_Cell.setValue(person.getPC_QTY());
	        	PC_QTY_Cell.setParent(item);
	        	Listcell  aa = new Listcell();
	        	aa.appendChild(hb);
	        	aa.setParent(item);
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	        	System.out.println("===person.getPC_REDATE()==="+person.getPC_REDATE());
	        	System.out.println("===sdf.format(person.getPC_REDATE())==="+sdf.format(person.getPC_REDATE()));
	        	Listcell  PC_REDATE_Cell = new Listcell((person.getPC_REDATE() != null?sdf.format(person.getPC_REDATE()):String.valueOf(person.getPC_REDATE())));
	        	PC_REDATE_Cell.setValue(person.getPC_REDATE());
	        	PC_REDATE_Cell.setParent(item);
	        	
	        	Listcell  UP_USER_Cell = new Listcell(person.getUP_USER());
	        	UP_USER_Cell.setValue(person.getUP_USER());
	        	UP_USER_Cell.setParent(item);
	        	System.out.println("===aa.getChildren()===="+aa.getChildren());
	            }
	    }
	 
	    public class ComparatorName implements Comparator<DSIDN08>{

	        @Override
	        public int compare(DSIDN08 o1, DSIDN08 o2) {
	            return o1.getPO_NO().compareTo(o2.getPO_NO());
	        }
	    }
	    
	    public class ComparatorDescName implements Comparator<DSIDN08>{
	        @Override
	        public int compare(DSIDN08 o1, DSIDN08 o2) {
	            return o2.getPO_NO().compareTo(o1.getPO_NO());
	        }
	    }
	    
	    
	    public class ComparatorHeader implements Comparator<DSIDN08>{

	    	String tempfiled="";
	    	public ComparatorHeader(String tempfiled){
	    		this.tempfiled = tempfiled;
	    	}
	        @Override
	        public int compare(DSIDN08 o1, DSIDN08 o2) {
	        	System.out.println("===tempfiled==="+tempfiled);
	        	if( invokeGet(o1,tempfiled) instanceof Timestamp){
	        		return ((Timestamp)invokeGet(o1,tempfiled)).compareTo(((Timestamp)invokeGet(o2,tempfiled)));
	        	}else{
	        		String compare01 = (String) invokeGet(o1,tempfiled);
		        	String compare02 = (String) invokeGet(o2,tempfiled);
		        	return (compare01==null?"":compare01).compareTo((compare02==null?"":compare02));
	        	}
	        }
	    }
	    
	    public class ComparatorDescHeader implements Comparator<DSIDN08>{
	    	String tempfiled="";
	    	public ComparatorDescHeader(String tempfiled){
	    		this.tempfiled = tempfiled;
	    	}
	        @Override
	        public int compare(DSIDN08 o1, DSIDN08 o2) {
	        	System.out.println("===tempfiled==="+tempfiled);
	        
	        	if( invokeGet(o1,tempfiled) instanceof Timestamp){
	        		return ((Timestamp)invokeGet(o2,tempfiled)).compareTo(((Timestamp)invokeGet(o1,tempfiled)));
	        	}else{
	        		String compare01 = (String) invokeGet(o1,tempfiled);
		        	String compare02 = (String) invokeGet(o2,tempfiled);
		        	return (compare02==null?"":compare02).compareTo((compare01==null?"":compare01));
	        	}
	        	
	        }
	    }
	    
	     
		   @SuppressWarnings("unchecked")  
		public static Method getGetMethod(Class objectClass, String fieldName) {  
		       StringBuffer sb = new StringBuffer();  
		       sb.append("get");  
		       sb.append(fieldName.substring(0, 1).toUpperCase());  
		       sb.append(fieldName.substring(1));  
		       try {  
		           return objectClass.getMethod(sb.toString());  
		       } catch (Exception e) {  
		       }  
		       return null;  
		   }
		   */
	   /** 
	     * 执行get方法 
	     *  
	     * @param o 执行对象 
	     * @param fieldName 属性 
	    
		public static Object invokeGet(Object o, String fieldName) { 
		        Method method = getGetMethod(o.getClass(), fieldName);  
		        try {  
		            return method.invoke(o, new Object[0]);  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        return null;  
		    }
		     */  
	    /**
		 * listbox 修改鈕, 有區分作業模式  有異常
		 * 
		 * @param evt
		 * @throws InvocationTargetException
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 */
		@Listen("onClick = #aaaaaa")
		public void clickMasterListbox(ForwardEvent evt)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("parentWindow", windowMaster);
			map.put("multiple", false);
			map.put("PO_NO", txt_PO_NOa.getValue());
		//	map.put("PO_NO_CONDITION", getHFinsh(txt_PO_NOa.getValue()));
			map.put("returnMethod", "onQueryOD_NOSend");  
			txt_Temp = 	(Textbox) evt.getOrigin().getTarget();
			Executions.createComponents("/ds/dsid/DSIDN05M_Query2.zul", null, map);
		}
			
		@Listen("onQueryOD_NOSend = #windowMaster")
		public void onQueryOD_NOSend(Event event){
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) event.getData();
				DSID05 e = (DSID05) map.get("selectedRecord");
				divadd.setVisible(true);
				txt_Temp.setText(e.getOD_NO());
			}
}
