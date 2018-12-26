package ds.dsid.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID04;
import ds.dsid.domain.DSID11_7;
import util.ComponentColumn;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID11MProgram extends Master {

	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster, btnImport, btnExport;	
	@Wire
	private Textbox txtMODEL_NA, txtType_1, txtType_2, txtType_3, txtType_4, txtType_5, txtType_6, txtType_7, txtType_8; 
	@Wire
	Tabpanel Detail1, Detail2, Detail3, Detail4, Detail5, Detail6;
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
//		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(txtMODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_1, "BLOCK1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_2, "BLOCK2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_3, "BLOCK3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_4, "BLOCK4", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_5, "BLOCK5", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_6, "BLOCK6", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_7, "BLOCK7", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType_8, "BLOCK8", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	

//		super.doFillListbox(0);
	}
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID11MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID11_7.class;
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
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 0);
		super.addDetailPrograms("/ds/dsid/DSID11_1MDetail.zul", Detail1, map);
		
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 1);
		super.addDetailPrograms("/ds/dsid/DSID11_2MDetail.zul", Detail2, map);
		
/*		map.put("masterProgram", this);
		map.put("indexDetailProgram", 2);
		super.addDetailPrograms("/ds/dsid/DSID11_3MDetail.zul", Detail3, map);
		
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 3);
		super.addDetailPrograms("/ds/dsid/DSID11_4MDetail.zul", Detail4, map);
		
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 4);
		super.addDetailPrograms("/ds/dsid/DSID11_5MDetail.zul", Detail5, map);
		
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 5);
		super.addDetailPrograms("/ds/dsid/DSID11_6MDetail.zul", Detail6, map);*/
		
		setDetail(6);
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("MODEL_NA");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID11_7 entity = (DSID11_7) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());

		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
	}

	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String sql = "";
		return sql;
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
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID11_7 entity = (DSID11_7) entityMaster;
		txtMODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		txtType_1.setValue(entity == null ? "" : entity.getBLOCK1());
		txtType_2.setValue(entity == null ? "" : entity.getBLOCK2());
		txtType_3.setValue(entity == null ? "" : entity.getBLOCK3());
		txtType_4.setValue(entity == null ? "" : entity.getBLOCK4());
		txtType_5.setValue(entity == null ? "" : entity.getBLOCK5());
		txtType_6.setValue(entity == null ? "" : entity.getBLOCK6());
		txtType_7.setValue(entity == null ? "" : entity.getBLOCK7());
		txtType_8.setValue(entity == null ? "" : entity.getBLOCK8());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID11_7";
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 5;
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
		
		txtMODEL_NA.setReadonly(true);
		txtType_1.setReadonly(true);
		txtType_2.setReadonly(true);
		txtType_3.setReadonly(true);
		txtType_4.setReadonly(true);
		txtType_5.setReadonly(true);
		txtType_6.setReadonly(true);
		txtType_7.setReadonly(true);
		txtType_8.setReadonly(true);
		
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
		
		txtMODEL_NA.setReadonly(false);
		txtType_1.setReadonly(false);
		txtType_2.setReadonly(false);
		txtType_3.setReadonly(false);
		txtType_4.setReadonly(false);
		txtType_5.setReadonly(false);
		txtType_6.setReadonly(false);
		txtType_7.setReadonly(false);
		txtType_8.setReadonly(false);
	}

}
