package ds.dsid.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID10;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID10MProgram extends Master{

	
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;	
	@Wire
	Textbox txtnike_sh_aritcle,query_sh_aritcle,txtpid_txt,txtorder_num,txtmodel_nas;		
	@Wire
	Checkbox ch_group1,ch_group2,ch_group3,ch_group4,ch_group5,ch_group6,ch_group7,ch_group8,ch_group9,ch_group10;
	@Wire
	Checkbox ch_group11,ch_group12,ch_group13,ch_group14,ch_group15,ch_group16,ch_group17,ch_group18,ch_group19,ch_group20;
	@Wire
	Textbox txtgroup1,txtgroup2,txtgroup3,txtgroup4,txtgroup5,txtgroup6,txtgroup7,txtgroup8,txtgroup9,txtgroup10;	
	@Wire
	Textbox txtgroup11,txtgroup12,txtgroup13,txtgroup14,txtgroup15,txtgroup16,txtgroup17,txtgroup18,txtgroup19,txtgroup20;		
//	@Wire
//	Bandbox bdpid_group;
	@Wire
	Tabpanel Detail1;
	@Wire
	Combobox Com_pid;
	
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
//		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(txtnike_sh_aritcle, "NIKE_SH_ARITCLE", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(bdpid_group, "PID_GROUP", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(Com_pid, "PID_GROUP", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtpid_txt, "PID_TXT", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtorder_num, "ORDER_NUM", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtmodel_nas, "MODEL_NAS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group1, "GROUP1_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup1, "GROUP1_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group2, "GROUP2_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup2, "GROUP2_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group3, "GROUP3_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup3, "GROUP3_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group4, "GROUP4_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup4, "GROUP4_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group5, "GROUP5_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup5, "GROUP5_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group6, "GROUP6_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup6, "GROUP6_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group7, "GROUP7_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup7, "GROUP7_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group8, "GROUP8_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup8, "GROUP8_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group9, "GROUP9_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup9, "GROUP9_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group10, "GROUP10_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup10, "GROUP10_NA", null, null, null));

		masterComponentColumns.add(new ComponentColumn<String>(ch_group11, "GROUP11_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup11, "GROUP11_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group12, "GROUP12_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup12, "GROUP12_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group13, "GROUP13_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup13, "GROUP13_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group14, "GROUP14_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup14, "GROUP14_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group15, "GROUP15_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup15, "GROUP15_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group16, "GROUP16_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup16, "GROUP16_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group17, "GROUP17_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup17, "GROUP17_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group18, "GROUP18_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup18, "GROUP18_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group19, "GROUP19_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup19, "GROUP19_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(ch_group20, "GROUP20_STATUS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtgroup20, "GROUP20_NA", null, null, null));		
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	

//		super.doFillListbox(0);
	}
	
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			txtnike_sh_aritcle = (Textbox) windowMaster.getFellow("txtnike_sh_aritcle");
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
		return DSID10MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID10.class;
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
		super.addDetailPrograms("/ds/dsid/DSID10MDetail1.zul", Detail1, map);
		setDetail(1);
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("NIKE_SH_ARITCLE");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID10 entity = (DSID10) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getNIKE_SH_ARITCLE());

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
		DSID10 entity = (DSID10) entityMaster;
		txtnike_sh_aritcle.setValue(entity == null ? "" : entity.getNIKE_SH_ARITCLE());
//		txtpid_group.setValue(entity == null ? "" : entity.getPID_GROUP());
		Com_pid.setValue(entity == null ? "" : entity.getPID_GROUP());
		txtpid_txt.setValue(entity == null ? "" : entity.getPID_TXT());
		txtorder_num.setValue(entity == null ? "" : entity.getORDER_NUM());
		txtmodel_nas.setValue(entity == null ? "" : entity.getMODEL_NAS());
		
		if (entity == null || entity.getGROUP1_STATUS().equals("N") ){
			ch_group1.setChecked(false);
		}else{
			ch_group1.setChecked(entity.getGROUP1_STATUS().equals("Y") ? true : false);	
			txtgroup1.setValue(entity == null ? "" : entity.getGROUP1_NA());
		}

		if (entity == null || entity.getGROUP2_STATUS().equals("N")){
			ch_group2.setChecked(false);
		}else{
			ch_group2.setChecked(entity.getGROUP2_STATUS().equals("Y") ? true : false);	
			txtgroup2.setValue(entity == null ? "" : entity.getGROUP2_NA());
		}

		if (entity == null || entity.getGROUP3_STATUS().equals("N")){
			ch_group3.setChecked(false);
		}else{
			ch_group3.setChecked(entity.getGROUP3_STATUS().equals("Y") ? true : false);	
			txtgroup3.setValue(entity == null ? "" : entity.getGROUP3_NA());
		}

		if (entity == null || entity.getGROUP4_STATUS().equals("N")){
			ch_group4.setChecked(false);
		}else{
			ch_group4.setChecked(entity.getGROUP4_STATUS().equals("Y") ? true : false);
			txtgroup4.setValue(entity == null ? "" : entity.getGROUP4_NA());
		}

		if (entity == null){
			ch_group5.setChecked(false);
		}else{
			ch_group5.setChecked(entity.getGROUP5_STATUS().equals("Y") ? true : false);
			txtgroup5.setValue(entity == null ? "" : entity.getGROUP5_NA());
		}

		if (entity == null){
			ch_group6.setChecked(false);
		}else{
			ch_group6.setChecked(entity.getGROUP6_STATUS().equals("Y") ? true : false);	
			txtgroup6.setValue(entity == null ? "" : entity.getGROUP6_NA());
		}

		if (entity == null){
			ch_group7.setChecked(false);
		}else{
			ch_group7.setChecked(entity.getGROUP7_STATUS().equals("Y") ? true : false);	
			txtgroup7.setValue(entity == null ? "" : entity.getGROUP7_NA());
		}

		if (entity == null){
			ch_group8.setChecked(false);
		}else{
			ch_group8.setChecked(entity.getGROUP8_STATUS().equals("Y") ? true : false);	
			txtgroup8.setValue(entity == null ? "" : entity.getGROUP8_NA());
		}

		if (entity == null){
			ch_group9.setChecked(false);
		}else{
			ch_group9.setChecked(entity.getGROUP9_STATUS().equals("Y") ? true : false);	
			txtgroup9.setValue(entity == null ? "" : entity.getGROUP9_NA());
		}

		if (entity == null){
			ch_group10.setChecked(false);
		}else{
			ch_group10.setChecked(entity.getGROUP10_STATUS().equals("Y") ? true : false);	
			txtgroup10.setValue(entity == null ? "" : entity.getGROUP10_NA());
		}

		
		if (entity == null){
			ch_group11.setChecked(false);
		}else{
			ch_group11.setChecked(entity.getGROUP11_STATUS().equals("Y") ? true : false);	
			txtgroup11.setValue(entity == null ? "" : entity.getGROUP11_NA());
		}

		if (entity == null){
			ch_group12.setChecked(false);
		}else{
			ch_group12.setChecked(entity.getGROUP12_STATUS().equals("Y") ? true : false);
			txtgroup12.setValue(entity == null ? "" : entity.getGROUP12_NA());
		}

		if (entity == null){
			ch_group13.setChecked(false);
		}else{
			ch_group13.setChecked(entity.getGROUP13_STATUS().equals("Y") ? true : false);
			txtgroup13.setValue(entity == null ? "" : entity.getGROUP13_NA());
		}

		if (entity == null){
			ch_group14.setChecked(false);
		}else{
			ch_group14.setChecked(entity.getGROUP14_STATUS().equals("Y") ? true : false);	
			txtgroup14.setValue(entity == null ? "" : entity.getGROUP14_NA());
		}

		if (entity == null){
			ch_group15.setChecked(false);
		}else{
			ch_group15.setChecked(entity.getGROUP15_STATUS().equals("Y") ? true : false);
			txtgroup15.setValue(entity == null ? "" : entity.getGROUP15_NA());
		}

		if (entity == null){
			ch_group16.setChecked(false);
		}else{
			ch_group16.setChecked(entity.getGROUP16_STATUS().equals("Y") ? true : false);
			txtgroup16.setValue(entity == null ? "" : entity.getGROUP16_NA());
		}

		if (entity == null){
			ch_group17.setChecked(false);
		}else{
			ch_group17.setChecked(entity.getGROUP17_STATUS().equals("Y") ? true : false);	
			txtgroup17.setValue(entity == null ? "" : entity.getGROUP17_NA());
		}

		if (entity == null){
			ch_group18.setChecked(false);
		}else{
			ch_group18.setChecked(entity.getGROUP18_STATUS().equals("Y") ? true : false);
			txtgroup18.setValue(entity == null ? "" : entity.getGROUP18_NA());
		}

		if (entity == null){
			ch_group19.setChecked(false);
		}else{
			ch_group19.setChecked(entity.getGROUP19_STATUS().equals("Y") ? true : false);	
			txtgroup19.setValue(entity == null ? "" : entity.getGROUP19_NA());
		}

		if (entity == null){
			ch_group20.setChecked(false);
		}else{
			ch_group20.setChecked(entity.getGROUP20_STATUS().equals("Y") ? true : false);
			txtgroup20.setValue(entity == null ? "" : entity.getGROUP20_NA());
		}

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
		return "DSID10";
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
		
		txtnike_sh_aritcle.setReadonly(true);
		txtpid_txt.setReadonly(true);
		txtorder_num.setReadonly(true);
		txtmodel_nas.setReadonly(true);
		ch_group1.setDisabled(true);
		ch_group2.setDisabled(true);
		ch_group3.setDisabled(true);
		ch_group4.setDisabled(true);
		ch_group5.setDisabled(true);
		ch_group6.setDisabled(true);
		ch_group7.setDisabled(true);
		ch_group8.setDisabled(true);
		ch_group9.setDisabled(true);
		ch_group10.setDisabled(true);
		ch_group11.setDisabled(true);
		ch_group12.setDisabled(true);
		ch_group13.setDisabled(true);
		ch_group14.setDisabled(true);
		ch_group15.setDisabled(true);
		ch_group16.setDisabled(true);
		ch_group17.setDisabled(true);
		ch_group18.setDisabled(true);
		ch_group19.setDisabled(true);
		ch_group20.setDisabled(true);
		txtgroup1.setReadonly(true);
		txtgroup2.setReadonly(true);
		txtgroup3.setReadonly(true);
		txtgroup4.setReadonly(true);
		txtgroup5.setReadonly(true);
		txtgroup6.setReadonly(true);
		txtgroup7.setReadonly(true);
		txtgroup8.setReadonly(true);
		txtgroup9.setReadonly(true);
		txtgroup10.setReadonly(true);	
		txtgroup11.setReadonly(true);
		txtgroup12.setReadonly(true);
		txtgroup13.setReadonly(true);
		txtgroup14.setReadonly(true);
		txtgroup15.setReadonly(true);
		txtgroup16.setReadonly(true);
		txtgroup17.setReadonly(true);
		txtgroup18.setReadonly(true);
		txtgroup19.setReadonly(true);
		txtgroup20.setReadonly(true);		
		Com_pid.setReadonly(true);

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
		
		txtnike_sh_aritcle.setReadonly(false);
		txtpid_txt.setReadonly(false);	
		txtorder_num.setReadonly(false);
		txtmodel_nas.setReadonly(false);
		ch_group1.setDisabled(false);
		ch_group2.setDisabled(false);
		ch_group3.setDisabled(false);
		ch_group4.setDisabled(false);
		ch_group5.setDisabled(false);
		ch_group6.setDisabled(false);
		ch_group7.setDisabled(false);
		ch_group8.setDisabled(false);
		ch_group9.setDisabled(false);
		ch_group10.setDisabled(false);
		ch_group11.setDisabled(false);
		ch_group12.setDisabled(false);
		ch_group13.setDisabled(false);
		ch_group14.setDisabled(false);
		ch_group15.setDisabled(false);
		ch_group16.setDisabled(false);
		ch_group17.setDisabled(false);
		ch_group18.setDisabled(false);
		ch_group19.setDisabled(false);
		ch_group20.setDisabled(false);
		txtgroup1.setReadonly(false);
		txtgroup2.setReadonly(false);
		txtgroup3.setReadonly(false);
		txtgroup4.setReadonly(false);
		txtgroup5.setReadonly(false);
		txtgroup6.setReadonly(false);
		txtgroup7.setReadonly(false);
		txtgroup8.setReadonly(false);
		txtgroup9.setReadonly(false);
		txtgroup10.setReadonly(false);	
		txtgroup11.setReadonly(false);
		txtgroup12.setReadonly(false);
		txtgroup13.setReadonly(false);
		txtgroup14.setReadonly(false);
		txtgroup15.setReadonly(false);
		txtgroup16.setReadonly(false);
		txtgroup17.setReadonly(false);
		txtgroup18.setReadonly(false);
		txtgroup19.setReadonly(false);
		txtgroup20.setReadonly(false);		
		Com_pid.setReadonly(false);
		
	}
}
