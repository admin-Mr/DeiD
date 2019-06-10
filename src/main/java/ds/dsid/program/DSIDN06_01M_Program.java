package ds.dsid.program;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.zkoss.zul.Checkbox;
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
import ds.dsid.domain.DSIDN06_01;
import ds.dsid.domain.DSIDN06_02;
import ds.dsid.domain.DSIDN08;
import util.Common;
import util.ComponentColumn;
import util.Master;
import util.OperationMode;

public class DSIDN06_01M_Program extends Master {
	
	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowMaster;
	@Wire
	private Listbox abcListbox,abcListboxa,abcListboxd,masterListbox;
	@Wire
	private Textbox txt_EL_NO_ADD_SQL,txt_MODEL_NA_ADD_SQL,txt_EL_CNAME_ADD_SQL,txt_PLACE_ADD_SQL,txt_CUPBOARD,txt_EL_NO_SQL,txt_MODEL_NA_SQL,txt_EL_CNAME_SQL,txt_PLACE_SQL,txt_MODEL_NA,txt_EL_NO,txt_EL_CNAME,txt_PLACE,txt_UNIQUEID,txt_DAY_NO,txt_OD_NO,txt_MT_CODE,
	txt_MT_PONO,txt_MT_SEQ,
	txt_Temp,txt_PO_NOSQL,txt_PO_SEQSQL,txt_PO_NOa,txt_OD_NOSQL,txt_QTY,txt_NOTE;
	@Wire private Doublebox txt_PO_QTY;
	@Wire private Div divadd,divedit,divshowdate,divcenter;
	@Wire private Center detailcenter;
	@Wire private Datebox querystartdate,queryenddate;
	@Wire private Paging  pagingCourse;
	@Wire private Checkbox checkbox_temp;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnClear,btnCleara, btnPO_NO,btnPO_NOa,btnImport,btnExport,btnQueryA;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	
	private boolean  Add=false;;
	
	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		masterComponentColumns.add(new ComponentColumn<String>(txt_MODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_CNAME, "EL_CNAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_PLACE, "PLACE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_NOTE, "NOTE",null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_CUPBOARD, "CUPBOARD",null, null, null));
		
	}
	
	private String GetQTY(String MODEL_NA, String EL_NO) {
		// TODO Auto-generated method stub
		Connection conn = Common.getDbConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String MT_QTY="";
		String sql1="SELECT MT_QTY FROM DSID77 WHERE MODEL_NA='"+MODEL_NA+"' AND EL_NO='"+EL_NO+"'";
//		System.err.println(">>>>>"+sql1);
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				if(!"".equals(rs1.getString("MT_QTY"))&&rs1.getString("MT_QTY")!=null){
					MT_QTY=rs1.getString("MT_QTY");
				}else{
					MT_QTY="";
				}
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			Common.closeConnection(conn);
		}
		
		return MT_QTY;
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
		return DSIDN06_01M_Program.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSIDN06_01.class;
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
//		masterKey.add("PLACE");
//		masterKey.add("EL_NO");
		masterKey.add("MODEL_NA");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSIDN06_01 entity = (DSIDN06_01) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
//		masterKeyValue.add(entity.getPLACE());
//		masterKeyValue.add(entity.getEL_NO());
		masterKeyValue.add(entity.getMODEL_NA());
		return masterKeyValue;
	}

	/**
	 * 必須複寫方法九
	 */
	@Override
	protected String getWhereConditionals() {
		String strSQL = "";// " and MT_PONO is not null ";
		
		if (!txt_EL_CNAME_SQL.getValue().trim().isEmpty()){
			strSQL += " and  EL_CNAME  like '%"+txt_EL_CNAME_SQL.getValue()+"%'";
		}
		if (!txt_PLACE_SQL.getValue().trim().isEmpty()){
			strSQL += " and  PLACE  like '%"+txt_PLACE_SQL.getValue()+"%'";
		}
		
		if (!txt_MODEL_NA_SQL.getValue().trim().isEmpty()){
			strSQL += " and  MODEL_NA  like '%"+txt_MODEL_NA_SQL.getValue()+"%'";
		}
		
		
		if (!txt_EL_NO_SQL.getValue().trim().isEmpty()){
			strSQL += " and  EL_NO  like '%"+txt_EL_NO_SQL.getValue()+"%'";
		}
	
		return strSQL;
	}
	
	
	
//	@Override
//	public String getQueryResultSizeBase() {
//
//		return " SELECT COUNT(t.EL_NO) FROM (select abc.*,d1.PLACE,d1.CUPBOARD from ( "
//				+" select dsid77.MODEL_NA,dsid77.el_no,dsel00.el_cname from dsid77 "
//				+ " join  dsel00 "
//				+ " on dsid77.el_no=dsel00.el_no   where is_hide is null  ) abc "
//				+" left join  DSIDN06_01  d1 "
//				+ "on abc.el_no = d1.el_no and abc.model_na = d1.model_na) t    ";
//	}

//	@Override
//	public String getQueryPagingBase() {
////		return "SELECT t.PB_GROUPID, t.PB_GROUPNA, t.PB_CNAME, t.UP_USER, t.UP_DATE, t.PB_SYSID, t.PB_USERID "
////				+ "FROM DSPB03 t ";
//		
//		return " SELECT t.* FROM (select abc.*,d1.PLACE,d1.CUPBOARD,NOTE from ( "
//				+" select dsid77.MODEL_NA,dsid77.el_no,dsel00.el_cname from dsid77 "
//				+ " join  dsel00 "
//				+ " on dsid77.el_no=dsel00.el_no   where is_hide is null  ) abc "
//				+" left join  DSIDN06_01  d1 "
//				+ "on abc.el_no = d1.el_no and abc.model_na = d1.model_na) t    ";
//	}

//	@Override
//	public String getQueryOneColBase() {
////		return "SELECT distinct t.PB_GROUPID FROM DSPB03 t ";
//		
//		//return "SELECT distinct t.SEQ FROM FTBMI00 t ";
//		
//		return " SELECT distinct t.EL_NO FROM (select abc.*,d1.PLACE,d1.CUPBOARD,NOTE from ( "
//		+" select dsid77.MODEL_NA,dsid77.EL_NO,dsel00.EL_CNAME from dsid77 "
//		+ " join  dsel00 "
//		+ " on dsid77.el_no=dsel00.el_no   where is_hide is null  ) abc "
//		+" left join  DSIDN06_01  d1 "
//		+ "on abc.el_no = d1.el_no and abc.model_na = d1.model_na) t   ";
//	}
	
/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		DSIDN06_01  entity = (DSIDN06_01 ) entityMaster;
		txt_MODEL_NA.setValue((entity == null ? "" : entity.getMODEL_NA()));
		txt_EL_NO.setValue((entity == null ? "" : entity.getEL_NO()));
		txt_EL_CNAME.setValue((entity == null ? "" : entity.getEL_CNAME()));
		txt_PLACE.setValue(entity == null ? "" : entity.getPLACE());
		txt_CUPBOARD.setValue(entity == null ? "" : entity.getCUPBOARD());
		txt_NOTE.setValue(entity == null ? "" : entity.getNOTE());
//		System.err.println(">>>>"+txt_MODEL_NA.getValue()+"  >>>>"+txt_EL_NO.getValue());
		txt_QTY.setValue(GetQTY(txt_MODEL_NA.getValue(),txt_EL_NO.getValue()));
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
		//btnExport.setDisabled(false);
		
		txt_MODEL_NA.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_EL_CNAME.setReadonly(true);
		txt_PLACE.setReadonly(true);
		txt_QTY.setReadonly(true);
		btnPO_NO.setDisabled(true);
		btnClear.setDisabled(true);
		
		btnPO_NOa.setDisabled(true);
		btnCleara.setDisabled(true);

	}
	
	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
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
	//	btnExport.setDisabled(true);
		
		txt_MODEL_NA.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_EL_CNAME.setReadonly(true);
		txt_PLACE.setReadonly(true);
		txt_QTY.setReadonly(true);
		btnPO_NO.setDisabled(false);
		btnClear.setDisabled(false);
		
		btnPO_NOa.setDisabled(false);
		btnCleara.setDisabled(false);
	}
	@Listen("onOK=#txt_PO_NOSQL,#txt_PO_SEQSQL,#txt_OD_NOSQL,#txt_EL_CNAME_SQL")
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
		Component con=  Executions.createComponents("/ds/dsid/DSID02M_Import01.zul", null, map);
		
	}
	
	
	@Listen("onClick = #btnPO_NO")
 	public void onClickbtnPO_NO(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("multiple", false);
		//map.put("PO_NO", txt_OD_NO.getValue());
		map.put("returnMethod", "onQueryOD_NOSendUp_date");  
		Executions.createComponents("/ds/dsid/DSIDN06_01M_Query2.zul", null, map);
	}
	
	@Listen("onQueryOD_NOSendUp_date = #windowMaster")
	public void onQueryOD_NOSendUp_date(Event event){
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) event.getData();
			DSIDN06_02 e = (DSIDN06_02) map.get("selectedRecord");
			//divadd.setVisible(true);
			txt_PLACE.setValue(e.getPLACE());
		}

	
	@Listen("onClick = #btnCleara")
	public void onClickbtnbtnQueryA(Event event){
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="Select t From DSID05 t  where 1=1  and t.MT_PONO='"+txt_PO_NOa.getValue()+"'  Order by  MT_SEQ";
		Query  tempquery = CRUDServicetemp.getBetweenByLimit2(sql);
		List	tempList = tempquery.getResultList();
		ListModelList  tempModel = new ListModelList(tempList, true);
		//System.out.println("===sql=="+sql);
	    abcListboxd.setModel(tempModel);
	}
	
	@Listen("onClick = #btnClear")
	public void onClickbtnClear(Event evnet){
		txt_PLACE.setValue("");
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

	
	@Listen("onClick = #btnCancelMaster")
	public void masterCancel(Event event) throws Exception {
		Add=false;
		super.executeQuery();
		super.masterCancel(event);
		//btnExport.setDisabled(true);
		
		divadd.setVisible(false);
		divedit.setVisible(true);
		divshowdate.setVisible(false);
		divcenter.setVisible(true);
		
		List<DSIDN08> empty = new ArrayList<DSIDN08>();
		
		ListModelList  emptyModel = new ListModelList(empty, true);
		
		abcListbox.setModel(emptyModel);
		
		List<DSID05> empty1 = new ArrayList<DSID05>();
	
		abcListboxd.setModel(emptyModel);
		txt_MODEL_NA_ADD_SQL.setText("");
		txt_EL_CNAME_ADD_SQL.setText("");
		txt_PLACE_ADD_SQL.setText("");
		txt_EL_NO_ADD_SQL.setText("");
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
	   

		@Listen("onClick = #aaaaaa")
		public void clickMasterListbox(ForwardEvent evt)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("parentWindow", windowMaster);
			map.put("multiple", false);

			map.put("returnMethod", "onQueryOD_NOSend");  
			txt_Temp = 	(Textbox) evt.getOrigin().getTarget();
			Listcell lc = (Listcell) evt.getOrigin().getTarget().getParent();
			Listitem lt = (Listitem) lc.getParent();
			Listcell lcc =(Listcell) lt.getFirstChild();
			checkbox_temp = (Checkbox) lcc.getFirstChild();
			Executions.createComponents("/ds/dsid/DSIDN06_01M_Query2.zul", null, map);
		}
		@Listen("onClick = #cccccc")
		public void clickMasterCheckox(ForwardEvent evt)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("parentWindow", windowMaster);
			map.put("multiple", false);
			if( evt.getOrigin().getTarget() instanceof Checkbox ){
			Checkbox checkbox = (Checkbox) evt.getOrigin().getTarget();
			System.out.println("============checkbox==========="+checkbox.isChecked());
			}
		}
		
		
		@SuppressWarnings("unchecked")
		@Listen("onClick = #bbbbbb")
		public void onClickShowWindow(ForwardEvent evt) {
			String place = txt_PLACE.getText();
			String model_na = txt_MODEL_NA.getText();
			if(!"".equals(place)){
				//System.out.println("===onClickShowWindow===");
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("parentWindow", windowMaster);
				map.put("multiple", false);
				map.put("PLACE", place);
				map.put("MODEL_NA", model_na);
				Executions.createComponents("/ds/dsid/DSIDN09M_Program_show.zul", null, map);
			}else{
				//Messagebox.show(Labels.getLabel("PUBLIC.MSG0088"), "Error", Messagebox.OK, Messagebox.ERROR);
				Messagebox.show("請確認改材料已設定位置！！", "Warning", Messagebox.OK, Messagebox.QUESTION);
			}
		}
			
		@Listen("onQueryOD_NOSend = #windowMaster")
		public void onQueryOD_NOSend(Event event){
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) event.getData();
				DSIDN06_02 e = (DSIDN06_02) map.get("selectedRecord");
				divadd.setVisible(true);
				txt_Temp.setText(e.getPLACE());
				if(txt_Temp != null && !"".equals(txt_Temp.getText())){
					checkbox_temp.setChecked(true);
				}
			}
		
		/**
		 * listbox 刪除鈕
		 * 
		 * @param evt
		 */
		@SuppressWarnings("unchecked")
		@Listen("onDelete = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
		public void onDeleteMasterListbox(ForwardEvent evt) {

			Event origin = Events.getRealOrigin(evt);
			Image btn = (Image) origin.getTarget();
			Listitem litem = (Listitem) btn.getParent().getParent().getParent();
			Object deleteMaster = litem.getValue();
			setMasterSel(deleteMaster);
			if (beforeMasterDel(deleteMaster) == false)
				return;
			Messagebox.show(Labels.getLabel("COMM.DELETE"), Labels.getLabel("PUBLIC.MSG0001"),
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event e)
								throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
							if (Messagebox.ON_OK.equals(e.getName())) {
								if (doCustomerDel(null)) {
									System.out.println("delete"+deleteMaster);
									//doDelete(deleteMaster);
								} else {
									return;
								}
							} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
								// Cancel is clicked
							}
						}
					});
		}



		@Override
		protected Object doSaveDefault(String columnName) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void doCreateDefault() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected List getCustList() {
			// TODO Auto-generated method stub
			return null;
		}
}
