package ds.dsid.program;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID07;
import util.ComponentColumn;
import util.Master;


public class DSID07MProgram extends Master{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;	
	@Wire
	private Textbox TADH_ELNO,TPRO_TYPE,TEL_UNIT,TCOLOR,TMODEL_NA,TADH_ELNA,TRAW_ELNO1,TRAW_PRO1,TRAW_ELNO2,TRAW_PRO2,TRAW_ELNO3,TRAW_PRO3,query_ADH_ELNO;
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(TADH_ELNO, "ADH_ELNO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TPRO_TYPE, "PRO_TYPE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TEL_UNIT, "EL_UNIT", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TCOLOR, "COLOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TMODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TADH_ELNA, "ADH_ELNA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_ELNO1, "RAW_ELNO1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_PRO1, "RAW_PRO1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_ELNO2, "RAW_ELNO2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_PRO2, "RAW_PRO2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_ELNO3, "RAW_ELNO3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_PRO3, "RAW_PRO3", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	
	}
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID07MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID07.class;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID07 entity = (DSID07) entityMaster;
		TADH_ELNO.setValue(entity == null ? "" : entity.getADH_ELNO());
		TPRO_TYPE.setValue(entity == null ? "" : entity.getPRO_TYPE());
		TEL_UNIT.setValue(entity == null ? "" : entity.getEL_UNIT());
		TCOLOR.setValue(entity == null ? "" : entity.getCOLOR());
		TMODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		TADH_ELNA.setValue(entity == null ? "" : entity.getADH_ELNA());	
		TRAW_ELNO1.setValue(entity == null ? "" : entity.getRAW_ELNO1());
		TRAW_PRO1.setValue(entity == null ? "" : entity.getRAW_PRO1());
		TRAW_ELNO2.setValue(entity == null ? "" : entity.getRAW_ELNO2());
		TRAW_PRO2.setValue(entity == null ? "" : entity.getRAW_PRO2());
		TRAW_ELNO3.setValue(entity == null ? "" : entity.getRAW_ELNO3());
		TRAW_PRO3.setValue(entity == null ? "" : entity.getRAW_PRO3());
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
		
		TADH_ELNO.setReadonly(true);
		TPRO_TYPE.setReadonly(true);	
		TEL_UNIT.setReadonly(true);
		TCOLOR.setReadonly(true);
		TMODEL_NA.setReadonly(true);
		TADH_ELNA.setReadonly(true);
		TRAW_ELNO1.setReadonly(true);
		TRAW_PRO1.setReadonly(true);	
		TRAW_ELNO2.setReadonly(true);
		TRAW_PRO2.setReadonly(true);
		TRAW_ELNO3.setReadonly(true);
		TRAW_PRO3.setReadonly(true);
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
		
		TADH_ELNO.setReadonly(false);
		TPRO_TYPE.setReadonly(false);	
		TEL_UNIT.setReadonly(false);
		TCOLOR.setReadonly(false);
		TMODEL_NA.setReadonly(false);
		TADH_ELNA.setReadonly(false);
		TRAW_ELNO1.setReadonly(false);
		TRAW_PRO1.setReadonly(false);	
		TRAW_ELNO2.setReadonly(false);
		TRAW_PRO2.setReadonly(false);
		TRAW_ELNO3.setReadonly(false);
		TRAW_PRO3.setReadonly(false);
		
		TRAW_PRO1.setValue("1");
		TRAW_PRO2.setValue("1");
		TRAW_PRO3.setValue("1");
	}
		
}
