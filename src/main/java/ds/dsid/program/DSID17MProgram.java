package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID07;
import ds.dsid.domain.DSID17;
import ds.dspb.domain.DSPB00_NEW;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;

public class DSID17MProgram extends Master{
	
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster,btnQuery_Elno;	
	@Wire
	private Textbox TADH_ELNO,TPRO_TYPE,TEL_UNIT,TCOLOR,TADH_ELNA,TADH_QTY,query_ADH_ELNO;
	@Wire
	Tabpanel Detail1,Detail2,Detail3;
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(TADH_ELNO, "ADH_ELNO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TPRO_TYPE, "PRO_TYPE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TEL_UNIT, "EL_UNIT", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TCOLOR, "COLOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TADH_ELNA, "ADH_ELNA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TADH_QTY, "ADH_QTY", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	
	}
	
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TADH_ELNO = (Textbox) windowMaster.getFellow("TADH_ELNO");
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
		return DSID17MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID17.class;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID17 entity = (DSID17) entityMaster;
		TADH_ELNO.setValue(entity == null ? "" : entity.getADH_ELNO());
		TPRO_TYPE.setValue(entity == null ? "" : entity.getPRO_TYPE());
		TEL_UNIT.setValue(entity == null ? "" : entity.getEL_UNIT());
		TCOLOR.setValue(entity == null ? "" : entity.getCOLOR());
		TADH_ELNA.setValue(entity == null ? "" : entity.getADH_ELNA());	
		TADH_QTY.setValue(entity == null ? "" : entity.getADH_QTY());

		
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
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String sql="";
		if(!query_ADH_ELNO.getValue().isEmpty()){
			sql+=" AND ADH_ELNO LIKE '%"+query_ADH_ELNO.getValue()+"%'";
		}
		
		return sql;
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
		btnQuery_Elno.setDisabled(true);
		
		TADH_ELNO.setReadonly(true);
		TPRO_TYPE.setReadonly(true);	
		TEL_UNIT.setReadonly(true);
		TCOLOR.setReadonly(true);
		TADH_ELNA.setReadonly(true);
		TADH_QTY.setReadonly(true);
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
		btnQuery_Elno.setDisabled(false);
		
		TADH_ELNO.setReadonly(false);
		TPRO_TYPE.setReadonly(false);	
		TEL_UNIT.setReadonly(false);
		TCOLOR.setReadonly(false);
		TADH_ELNA.setReadonly(false);
		TADH_QTY.setReadonly(false);
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
		super.addDetailPrograms("/ds/dsid/DSID17MDetail1.zul", Detail1, map);
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 1);
		super.addDetailPrograms("/ds/dsid/DSID17MDetail2.zul", Detail2, map);
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 2);
		super.addDetailPrograms("/ds/dsid/DSID17MDetail3.zul", Detail3, map);
		setDetail(3);
	}
	
	@Listen("onClick = #btnQuery_Elno")
	public void onClickbtnQuery_Elno(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("returnMethod", "onQryAdh_elno");
		Executions.createComponents("/ds/dsid/DSID17MQueryADH_ELNO.zul", null, map);
	}
	
	@SuppressWarnings("unchecked")
	@Listen("onQryAdh_elno = #windowMaster")
	public void onQryAdh_elno(Event event){
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSID07 e = (DSID07) map.get("selectedRecord");
		TADH_ELNO.setValue(e.getADH_ELNO());		
		TPRO_TYPE.setValue(e.getPRO_TYPE());
		TEL_UNIT.setValue(e.getEL_UNIT());
		TCOLOR.setValue(e.getCOLOR());
		TADH_ELNA.setValue(e.getADH_ELNA());
		TADH_QTY.setValue("0");
	}
	
}
