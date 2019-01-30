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
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID13;
import util.ComponentColumn;
import util.Master;

public class DSID13MProgram extends Master {

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster,btnQryNIKE_SH_ARITCLE,btnQryGROUP_NO;	
	@Wire
	private Textbox TNIKE_SH_ARITCLE,TPG_NAME,TPG_TYPE,TGROUP_NOS;
	
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(TPG_NAME, "PG_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TNIKE_SH_ARITCLE, "NIKE_SH_ARITCLE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TPG_TYPE, "PG_TYPE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TGROUP_NOS, "GROUP_NOS", null, null, null));

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
		return DSID13MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID13.class;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID13 entity = (DSID13) entityMaster;
		TPG_NAME.setValue(entity == null ? "" : entity.getPG_NAME());
		TNIKE_SH_ARITCLE.setValue(entity == null ? "" : entity.getNIKE_SH_ARITCLE());
		TPG_TYPE.setValue(entity == null ? "" : entity.getPG_TYPE());
		TGROUP_NOS.setValue(entity == null ? "" : entity.getGROUP_NOS());
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
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnEditMaster.setDisabled(false);
		btnDeleteMaster.setDisabled(false);
		
		TPG_NAME.setReadonly(true);
		TNIKE_SH_ARITCLE.setReadonly(true);	
		TPG_TYPE.setReadonly(true);
		TGROUP_NOS.setReadonly(true);
		
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
		
		TPG_NAME.setReadonly(false);
		TNIKE_SH_ARITCLE.setReadonly(false);
		TNIKE_SH_ARITCLE.setValue("NIKE AIR ZOOM PEGASUS 35 ID");
		TPG_TYPE.setReadonly(false);
		TGROUP_NOS.setReadonly(false);
		
	}

	@Listen("onClick = #btnQryGROUP_NO")
	public void onClickbtnQryGROUP_NO(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("returnMethod", "onQryGROUP_NO");
		map.put("multiple", true);
		map.put("NIKE_SH_ARITCLE", TNIKE_SH_ARITCLE.getValue());
		Executions.createComponents("/ds/dsid/DSID13MQryGROUP_NO.zul", null, map);
	}
	
	@SuppressWarnings("unchecked")
	@Listen("onQryGROUP_NO = #windowMaster")
	public void onQryGROUP_NO(Event event){
		ArrayList<Object> arrList = (ArrayList<Object>) event.getData();		
		System.err.println(">>>map "+arrList);
		TGROUP_NOS.setValue(arrList.get(0).toString());
	}
}
