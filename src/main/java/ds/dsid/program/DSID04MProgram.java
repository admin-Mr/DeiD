package ds.dsid.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID04;
import ds.dsid.domain.DSID10;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID04MProgram extends Master{

	
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDelete,btnImport,btnExport;	
	@Wire
	Textbox txtMODEL_NA,txtMODEL_UPD,txtVAMP_UPD,txtIS_DROP,query_sh_aritcle;
	@Wire
	Datebox txtLA_DATE,txtDR_DATE;
	@Wire
	Tabpanel Detail1;

	
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
//		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(txtMODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtLA_DATE, "LA_DATE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtDR_DATE, "DR_DATE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtMODEL_UPD, "MODEL_UPD", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtVAMP_UPD, "VAMP_UPD", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtIS_DROP, "IS_DROP", null, null, null));

		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	

//		super.doFillListbox(0);
	}
	
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			txtMODEL_NA = (Textbox) windowMaster.getFellow("txtMODEL_NA");
		}
	}
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Listen("onClick =#btnImport")
	public void onClickbtnImport(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID04MProgram", this);
		Executions.createComponents("/ds/dsid/DSID04MImport.zul", null, map);
	}
	
	@Listen("onClick =#btnExport")
	public void onClickbtnExport(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID04MProgram", this);
		Executions.createComponents("/ds/dsid/DSID04MExport.zul", null, map);
	}
	
	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID04MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID04.class;
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
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("masterProgram", this);
//		map.put("indexDetailProgram", 0);
//		super.addDetailPrograms("/ds/dsid/DSID10MDetail1.zul", Detail1, map);
//		setDetail(1);
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
		DSID04 entity = (DSID04) entityMaster;
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
		String sql="";
		if(!query_sh_aritcle.getValue().isEmpty()){
			sql+=" AND NIKE_SH_ARITCLE='"+query_sh_aritcle.getValue()+"'";
		}
		
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
		DSID04 entity = (DSID04) entityMaster;
		txtMODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		txtLA_DATE.setValue(entity == null ? null : entity.getLA_DATE());
		txtDR_DATE.setValue(entity == null ? null : entity.getDR_DATE());
		txtMODEL_UPD.setValue(entity == null ? "" : entity.getMODEL_UPD());
		txtVAMP_UPD.setValue(entity == null ? "" : entity.getVAMP_UPD());
		txtIS_DROP.setValue(entity == null ? "" : entity.getIS_DROP());

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
		return "DSID04";
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
		btnDelete.setDisabled(false);
		
		txtMODEL_NA.setReadonly(true);
		txtLA_DATE.setReadonly(true);
		txtDR_DATE.setReadonly(true);
		txtMODEL_UPD.setReadonly(true);
		txtVAMP_UPD.setReadonly(true);
		txtIS_DROP.setReadonly(true);
		
		

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
		btnDelete.setDisabled(true);
		
		txtMODEL_NA.setReadonly(false);
		txtLA_DATE.setReadonly(false);
		txtDR_DATE.setReadonly(false);
		txtMODEL_UPD.setReadonly(false);
		txtVAMP_UPD.setReadonly(false);
		txtIS_DROP.setReadonly(false);
		
	}
}
