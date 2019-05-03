package ds.dsid.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.UserCredential;
import ds.dsid.domain.DSID02;
import ds.dsid.domain.DSID10;
import ds.dsid.domain.DSID12;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID12MProgram  extends Master{
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;	
	@Wire
	private Textbox TMODEL_NA,TSH_STYLENO,TSH_LAST,TSH_TYPE,TSH_COLOR,TSH_REMARK,TUPPER_CLASS,TSOLE_CLASS,TMODEL_NONA;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		masterComponentColumns.add(new ComponentColumn<String>("TMODEL_NA", "MODEL_NA", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TSH_STYLENO", "SH_STYLENO", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TSH_LAST", "SH_LAST", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TSH_TYPE", "SH_TYPE", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TSH_COLOR", "SH_COLOR", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TSH_REMARK", "SH_REMARK", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TUPPER_CLASS", "UPPER_CLASS", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TSOLE_CLASS", "SOLE_CLASS", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER",  _userInfo.getAccount(), null, null,false));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	
	}
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TMODEL_NA = (Textbox) windowMaster.getFellow("TMODEL_NA");
		}
	}
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID12MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID12.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}

	@Override
	protected Class getDetailClass(int indexDetail) {
		// TODO Auto-generated method stub
		return getDetailProgram(indexDetail).getDetailClass();
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		// TODO Auto-generated method stub
		return getArrDetailPrograms().get(indexDetail);
	}

	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("MODEL_NA");
		masterKey.add("SH_STYLENO");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID12 entity = (DSID12) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getSH_STYLENO());
		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {
		
		return "pagingCourseqq";
	}

	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String sql="";
		if(!TMODEL_NONA.getValue().isEmpty()){
				sql+=" AND MODEL_NA='"+TMODEL_NONA.getValue()+"'";
		}		
		return sql;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
//		System.out.println("=========onclick=Editor===========");
		DSID12 entity = (DSID12) entityMaster;
		System.out.println(entity==null);
		TMODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		TSH_STYLENO.setValue(entity == null ? "" : entity.getSH_STYLENO());
		TSH_LAST.setValue(entity == null ? "" : entity.getSH_LAST());
		TSH_TYPE.setValue(entity == null ? "" : entity.getSH_TYPE());
		TSH_COLOR.setValue(entity == null ? "" : entity.getSH_COLOR());
		TSH_REMARK.setValue(entity == null ? "" : entity.getSH_REMARK());
		TUPPER_CLASS.setValue(entity == null ? "" : entity.getUPPER_CLASS());
		TSOLE_CLASS.setValue(entity == null ? "" : entity.getSOLE_CLASS());
	}
	@Override
	protected String getDetailConditionals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterCreateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterUpdateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterQueryZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getMasterCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean beforeMasterDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void doCreateDefault() {
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID12";
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnEditMaster.setDisabled(false);
		btnDeleteMaster.setDisabled(false);
		
		TMODEL_NA.setReadonly(true);
		TSH_STYLENO.setReadonly(true);
		TSH_LAST.setReadonly(true); 
		TSH_TYPE.setReadonly(true);
		TSH_COLOR.setReadonly(true);
		TSH_REMARK.setReadonly(true);
		TUPPER_CLASS.setReadonly(true);
		TSOLE_CLASS.setReadonly(true);
	}
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnEditMaster.setDisabled(true);
		btnDeleteMaster.setDisabled(true);
		
		TMODEL_NA.setReadonly(false);
		TSH_STYLENO.setReadonly(false);
		TSH_LAST.setReadonly(false); 
		TSH_TYPE.setReadonly(false);
		TSH_COLOR.setReadonly(false);
		TSH_REMARK.setReadonly(false);
		TUPPER_CLASS.setReadonly(false);
		TSOLE_CLASS.setReadonly(false);
	}

}
