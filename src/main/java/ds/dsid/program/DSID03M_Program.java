package ds.dsid.program;

import java.util.ArrayList;
import java.util.HashMap;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.dsid.domain.DSID03;
import util.ComponentColumn;
import util.OperationMode;

public class DSID03M_Program extends COMM_Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txt_UNIQUEID ,txt_FU_ID    ,txt_MU         ,txt_PART_NAME,
					txt_VENDOR   ,txt_CP_NAME  ,txt_EL_NAME    ,txt_COLOR_CODE,
					txt_STATUS   ,txt_FU_IDSQL ,txt_MODEL_NAME ,txt_YIELD;
					
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnClear, btnPB_MUNODE;

	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		masterComponentColumns.add(new ComponentColumn<String>(txt_UNIQUEID, "UNIQUEID", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_FU_ID, "FU_ID", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MU, "MU", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_PART_NAME, "PART_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_VENDOR, "VENDOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_CP_NAME, "CP_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NAME, "EL_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_COLOR_CODE, "COLOR_CODE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_STATUS, "STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MODEL_NAME, "MODEL_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_YIELD, "YIELD", null, null, null));

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
		return DSID03M_Program.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID03.class;
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
		DSID03 entity = (DSID03) entityMaster;
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
		String strSQL = " and FU_ID is not null ";
		
		if (!txt_FU_IDSQL.getValue().trim().isEmpty()){
			strSQL += " and  FU_ID ='"+txt_FU_IDSQL.getValue()+"'";
		}
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		
		System.out.println("=========onclick=Editor===========");
		DSID03 entity = (DSID03) entityMaster;
		/*
		System.out.println(entity==null);
		txtPB_ZKID.setValue(entity == null ? "" : entity.getZK_ID());
		txtPB_ZKACCOUNT.setValue(entity == null ? "" : entity.getZK_ACCOUNT());
		txtPB_ZKNAME.setValue(entity == null ? "" : entity.getZK_NAME());
		txtPB_ZKPASSWORD.setValue(entity == null ? "" : entity.getZK_PASSWORD());
		txtPB_ZKREMAKR.setValue(entity == null ? "" : entity.getZK_REMARK());*/
		
		 txt_UNIQUEID.setValue(entity == null ? "" : entity.getUNIQUEID());
	      txt_FU_ID.setValue(entity == null ? "" : entity.getFU_ID());
	      txt_MU.setValue(entity == null ? "" : entity.getMU());
	      txt_PART_NAME.setValue(entity == null ? "" : entity.getMU());
		  txt_VENDOR.setValue(entity == null ? "" : entity.getVENDOR());
		  txt_CP_NAME.setValue(entity == null ? "" : entity.getCP_NAME());
		  txt_EL_NAME.setValue(entity == null ? "" : entity.getEL_NAME());
		  txt_COLOR_CODE.setValue(entity == null ? "" : entity.getCOLOR_CODE());
		  txt_STATUS.setValue(entity == null ? "" : entity.getSTATUS());
		  
		  
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
      txt_FU_ID.setReadonly(true);
      txt_MU.setReadonly(false);
      txt_PART_NAME.setReadonly(false);
	  txt_VENDOR.setReadonly(false);
	  txt_CP_NAME.setReadonly(false);
	  txt_EL_NAME.setReadonly(false);
	  txt_COLOR_CODE.setReadonly(false);
	  txt_STATUS.setReadonly(false);
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
		txt_UNIQUEID.setReadonly(true);
	    txt_FU_ID.setReadonly(true);
	    txt_MU.setReadonly(false);
	    txt_PART_NAME.setReadonly(false);
		txt_VENDOR.setReadonly(false);
		txt_CP_NAME.setReadonly(false);
		txt_EL_NAME.setReadonly(false);
		txt_COLOR_CODE.setReadonly(false);
		txt_STATUS.setReadonly(false);
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
		//Component con=  Executions.createComponents("/ds/test/DSPLM102M03.zul", null, map);
		Component con=  Executions.createComponents("/ds/dsid/DSID03M_Import01.zul", null, map);
		
	}

	

}
