package ds.dsid.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.SessionScope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID02;
import ds.dspb.domain.DSPB00_NEW;
import util.ComponentColumn;
import util.OperationMode;

public class DSID02M_Program extends COMM_Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txt_EL_NOSQL,txt_UNIQUEID,txt_EL_NO,txt_EL_MODEL,
					txt_PART_NAME,txt_COLOR,txt_STATUS;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnClear, btnPB_MUNODE,btnImport;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
//	@Wire
	//private Intbox txtUNIQUEIDa;
	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_UNIQUEID, "UNIQUEID", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_MODEL, "EL_MODEL", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_PART_NAME, "PART_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_COLOR, "COLOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_STATUS, "STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "CREATE_USER", _userInfo.getAccount(), null, null));
	//	masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	
		System.out.println(Sessions.getCurrent().getAttribute("UAuth")+"=====uauth===");
		System.out.println(Sessions.getCurrent().getAttribute("DAuth")+"=====dauth===");;
		System.out.println("_userInfo"+_userInfo.getAuth());
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
		return DSID02M_Program.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID02.class;
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
		DSID02 entity = (DSID02) entityMaster;
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
		String strSQL = " and EL_NO is not null ";
		
		if (!txt_EL_NOSQL.getValue().trim().isEmpty()){
			strSQL += " and  EL_NO ='"+txt_EL_NOSQL.getValue()+"'";
		}
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		
		System.out.println("=========onclick=Editor===========");
		DSID02 entity = (DSID02) entityMaster;
		System.out.println(entity==null);
		txt_UNIQUEID.setValue((entity == null ? "" : entity.getUNIQUEID()));
		txt_EL_NO.setValue(entity == null ? "" : entity.getEL_NO());
		txt_EL_MODEL.setValue(entity == null ? "" : entity.getEL_MODEL());
		txt_PART_NAME.setValue(entity == null ? "" : entity.getPART_NAME());
		txt_COLOR.setValue(entity == null ? "" : entity.getCOLOR());
		txt_STATUS.setValue(entity == null ? "" : entity.getSTATUS());/**/
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		
		System.out.println("========readDate====================");
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		txt_UNIQUEID.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_EL_MODEL.setReadonly(true);
		txt_PART_NAME.setReadonly(true);
		txt_COLOR.setReadonly(true);
		txt_STATUS.setReadonly(true);/**/
	}
	
	/**
	 * 點擊新增按鍵時作業
	 */
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		
		System.out.println("=====masterwindow==show =========");
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		txt_EL_NO.setReadonly(false);
		txt_EL_MODEL.setReadonly(false);
		txt_PART_NAME.setReadonly(false);
		txt_COLOR.setReadonly(false);
		txt_STATUS.setReadonly(false);
	}
	@Listen("onOK=#txt_EL_NOSQL")
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
		//Component con=  Executions.createComponents("/ds/test/DSPLM102M03.zul", null, map);
		Component con=  Executions.createComponents("/ds/dsid/DSID02M_Import01.zul", null, map);
		
	}
	
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event){
		executeQuery();
		//Map<String, Object> map = (Map<String, Object>) event.getData();
		//DSPB00_NEW e = (DSPB00_NEW) map.get("selectedRecord");
		//txtPB_MUNODE.setValue(e.getPB_MUITEM());
		//txtPB_MUITEM.setValue(getNewItem());
	}

	

}
