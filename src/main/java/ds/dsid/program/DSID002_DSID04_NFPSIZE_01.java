package ds.dsid.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID04_NFPSIZE;
import util.BlackBox;
import util.ComponentColumn;
import util.DataMode;
import util.Master;
import util.OperationMode;
import util.openwin.ReturnBox;

public class DSID002_DSID04_NFPSIZE_01 extends Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox qry_MODEL_NA;
	@Wire
	private Textbox txt_MODEL_NA,txt_EL_NO,txt_SIZES;
	@Wire
	private Button btnQuery, btnSaveMaster, btnCancelMaster, btnCreateMaster;
	@Wire
	private Button onqryDSID04,ontxtDSID04,ontxtDSID04ELNO;
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
		masterComponentColumns.add(new ComponentColumn<String>(txt_MODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZES, "SIZES", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "SEQ", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
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
		return DSID002_DSID04_NFPSIZE_01.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID04_NFPSIZE.class;
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
		masterKey.add("SEQ");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID04_NFPSIZE entity = (DSID04_NFPSIZE) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getEL_NO());
		masterKeyValue.add(entity.getSEQ());
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
		String strSQL = "";
		
		if (!qry_MODEL_NA.getValue().trim().isEmpty()){
			strSQL += " AND  t.MODEL_NA ='"+qry_MODEL_NA.getValue()+"'";
		}
		
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		DSID04_NFPSIZE entity = (DSID04_NFPSIZE) entityMaster;
		if(entity == null){
			return;
		}
		txt_MODEL_NA.setValue((entity == null ? "" : entity.getMODEL_NA()));
		txt_EL_NO.setValue((entity == null ? "" : entity.getEL_NO()));
		txt_SIZES.setValue((entity == null ? "" : entity.getSIZES()));
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btnCreateMaster", btnCreateMaster);
		mapButton.put("btnQuery", btnQuery);
		mapButton.put("btnSaveMaster", btnSaveMaster);
		mapButton.put("btnCancelMaster", btnCancelMaster);
		super.masterReadMode(mapButton);
		
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		
		/*過濾區*/
		onqryDSID04.setDisabled(false);
		qry_MODEL_NA.setReadonly(false);
		/*編輯區*/
		txt_MODEL_NA.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_SIZES.setReadonly(true);
		ontxtDSID04.setDisabled(true);
		ontxtDSID04ELNO.setDisabled(true);
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
		/*過濾區*/
		onqryDSID04.setDisabled(true);
		qry_MODEL_NA.setReadonly(true);
		/*編輯區*/
		txt_MODEL_NA.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_SIZES.setReadonly(false);
		ontxtDSID04.setDisabled(false);
		ontxtDSID04ELNO.setDisabled(false);
	}
	
	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		
		return true;
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		txt_MODEL_NA.setText("");
		txt_EL_NO.setText("");
		txt_SIZES.setText("");
	}
	
	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		DSID04_NFPSIZE DSID04_NFPSIZE= (DSID04_NFPSIZE)getMasterSel();
		switch (columnName) {
		case "SEQ":
			if (getData_Mode().equals(DataMode.CREATE_MODE))
				return getSEQ();
			else
				return DSID04_NFPSIZE.getSEQ();
		default:
			break;
		}
		return null;
	}
	
	private String getSEQ(){
		final Query qry = CRUDService.createSQL("SELECT LPAD(NVL(MAX(SEQ)+1,'001'),3,0) SEQ FROM DSID04_NFPSIZE WHERE MODEL_NA='"+txt_MODEL_NA.getText()+"'");
		return BlackBox.getValue(qry.getSingleResult());
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Listen("onOpenQueryField = #windowMaster")
	public void onOpenQueryField(ForwardEvent event) {
		 final HashMap<String, Object> map = new HashMap<String, Object>();

         map.put("parentWindow", windowMaster);
         map.put("WindoWidth", "60%");	//開窗長度
         map.put("WindoHeight", "80%");	//開窗高度
         map.put("Sizable", true);		//是否可手動調整大小
         map.put("Closable", true);		//是否可關閉視窗
         map.put("Maximizable",true);	//是否可最大化視窗
         map.put("multiple", false);    // false:單選,true:多選目前不支援共用
         /**********查詢MODEL_NA開窗**********/
         if(event.getData().equals("onqryDSID04")){
	         map.put("xmlQryID","dsid/DSID04");
	         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
	         returnBoxList.add(new ReturnBox("MODEL_NA",qry_MODEL_NA));
	         map.put("returnTextBoxList", returnBoxList);
	     /**********編輯MODEL_NA開窗**********/
         }else if(event.getData().equals("ontxtDSID04")){
        	 map.put("xmlQryID","dsid/DSID04");
	         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
	         returnBoxList.add(new ReturnBox("MODEL_NA",txt_MODEL_NA));
        	 map.put("returnTextBoxList", returnBoxList);	
    	 /**********編輯EL_NO開窗**********/	 
         }else if(event.getData().equals("ontxtDSID04ELNO")){
        	 map.put("xmlQryID","dsid/DSID_ELNO");
	         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
	         returnBoxList.add(new ReturnBox("EL_NO",txt_EL_NO));
        	 map.put("returnTextBoxList", returnBoxList);
         }
         
         Executions.createComponents("/util/openwin/QueryField.zul", null, map);
	}
	
	@Override
	@Listen("onUpdate = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void editMasterListbox(ForwardEvent evt) throws Exception {
		super.editMasterListbox(evt);
	}
}
