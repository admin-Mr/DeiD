package ds.dsid.program;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID01_TEMP;
import org.zkoss.zk.ui.event.Event;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID01MProgram2 extends Master{

	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txtod_no, txttype, txtstatus, txtwork_order_id, txtship_order_id, txorder_id, txtsh_styleno;
	@Wire
	private Textbox txordrer_num, txtleft_size, txtright_size, txttooling_size, txtnike_sh_aritcle, txtmodel_na, txtround,txtregion;
	@Wire
	private Textbox TOD_NO, TL_PID, TR_PID, TGROUP1, TGROUP2, TGROUP3, TGROUP4, TGROUP5, TGROUP6, TGROUP7, TGROUP8, TGROUP9, TGROUP10, TGROUP11, TGROUP12;	
	@Wire
	private Datebox	txtship_date,txtpg_date,txtorder_date,query_order_date;
	@Wire
	private Textbox query_od_no,query_work_order_id;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery,btnImport,btnOrder,btnArrange,btnDalete_All,btnTransfer;

	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		
//		masterComponentColumns.add(new ComponentColumn<String>(txtod_no, "OD_NO", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txttype, "TYPE", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtstatus, "STATUS", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtwork_order_id, "SHIP_GROUP_ID", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtship_order_id, "WORK_ORDER_ID ", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txorder_id, "ORDER_ID", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtsh_styleno, "SH_STYLENO", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txordrer_num, "ORDER_NUM", null, null, null));
//		
//		masterComponentColumns.add(new ComponentColumn<String>(txtleft_size, "LEFT_SIZE_RUN", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtright_size, "RIGHT_SIZE_RUN", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txttooling_size, "TOOLING_SIZE", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtnike_sh_aritcle, "NIKE_SH_ARITCLE", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtmodel_na, "MODEL_NA", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtround, "ROUND", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<String>(txtregion, "REGION", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<Date>(txtship_date, "SHIPING_DATE", null, null, null));
//		masterComponentColumns.add(new ComponentColumn<Date>(txtpg_date, "PG_DATE", null, null, null));		
//		masterComponentColumns.add(new ComponentColumn<Date>(txtorder_date, "ORDER_DATE", null, null, null));
	
		masterComponentColumns.add(new ComponentColumn<String>("TOD_NO", "OD_NO", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TL_PID", "PID01", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TR_PID", "PID02", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP1", "GROUP1", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP2", "GROUP2", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP3", "GROUP3", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP4", "GROUP4", null, null, null,false));		
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP5", "GROUP5", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP6", "GROUP6", null, null, null,false));		
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP7", "GROUP7", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP8", "GROUP8", null, null, null,false));		
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP9", "GROUP9", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP10", "GROUP10", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP11", "GROUP11", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TGROUP12", "GROUP12", null, null, null,false));		
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	
				
	}
	
	// xml匯入 彈窗
	@Listen("onClick =#btnImport")
	public void onClickbtnSPEC(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID01MProgram2", this);
		map.put("TABLE", "DSID01_TEMP");
		Executions.createComponents("/ds/dsid/DSID01MImport.zul", null, map);
	}
	
	// 整理GROUP、PID信息
	@Listen("onClick =#btnOrder")
	public void onClickbtnOrder(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID01MProgram2", this);
		map.put("TABLE", "DSID01_TEMP");
		Executions.createComponents("/ds/dsid/DSID01MOrder.zul", null, map);
	}
	
	//批量刪除
	@Listen("onClick =#btnDalete_All")
	public void onClickbtnDalete_All(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID01MProgram2", this);
		map.put("TABLE", "DSID01_TEMP");
		Executions.createComponents("/ds/dsid/DSID01MDelete.zul", null, map);
	}
	
	// 整理GROUP、PID信息
	@Listen("onClick =#btnReceive")
	public void onClickbtnReceive(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID01MProgram2", this);
		Executions.createComponents("/ds/dsid/DSID01MReceive.zul", null, map);
	}
	
	// create listbox裡面的cell，將資料及元件建立
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			
			TOD_NO = (Textbox) windowMaster.getFellow("TOD_NO");
			TL_PID = (Textbox) windowMaster.getFellow("TL_PID");
			TR_PID = (Textbox) windowMaster.getFellow("TR_PID");				
			TGROUP1 = (Textbox) windowMaster.getFellow("TGROUP1");
			TGROUP2 = (Textbox) windowMaster.getFellow("TGROUP2");
			TGROUP3 = (Textbox) windowMaster.getFellow("TGROUP3");
			TGROUP4 = (Textbox) windowMaster.getFellow("TGROUP4");
			TGROUP5 = (Textbox) windowMaster.getFellow("TGROUP5");
			TGROUP6 = (Textbox) windowMaster.getFellow("TGROUP6");
			TGROUP7 = (Textbox) windowMaster.getFellow("TGROUP7");
			TGROUP8 = (Textbox) windowMaster.getFellow("TGROUP8");
			TGROUP9 = (Textbox) windowMaster.getFellow("TGROUP9");
			TGROUP10 = (Textbox) windowMaster.getFellow("TGROUP10");
			TGROUP11 = (Textbox) windowMaster.getFellow("TGROUP11");
			TGROUP12 = (Textbox) windowMaster.getFellow("TGROUP12");

			}
		
		if (getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TOD_NO.setReadonly(true);
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
		return DSID01MProgram2.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID01_TEMP.class ;
	}

	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}

	@Override
	protected Class getDetailClass(int indexDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("OD_NO");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID01_TEMP entity = (DSID01_TEMP) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getOD_NO());
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
		if(!query_od_no.getValue().isEmpty()){
			sql+=" AND t.OD_NO='"+query_od_no.getValue()+"'";
		}
		if(query_order_date.getValue() !=null ){
			DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
			sql+=" AND TO_CHAR(t.ORDER_DATE,'YYYY/MM/DD')='"+Format.format(query_order_date.getValue())+"'";
		}
		if(!query_work_order_id.getValue().isEmpty()){
			sql+=" AND t.WORK_ORDER_ID LIKE '%"+query_work_order_id.getValue()+"%'";
		}
		System.err.println(">>>Query sql "+sql);
		
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
		DSID01_TEMP entity = (DSID01_TEMP) entityMaster;
		txtod_no.setValue(entity == null ? "" : entity.getOD_NO());
		txtorder_date.setValue(entity == null ? null : entity.getORDER_DATE());
		txttype.setValue(entity == null ? "" : OutShow1(entity.getTYPE()));
		txtstatus.setValue(entity == null ? "" : OutShow2(entity.getSTATUS()));		
		txtwork_order_id.setValue(entity == null ? "" : entity.getWORK_ORDER_ID());
		txtship_order_id.setValue(entity == null ? "" : entity.getSHIP_GROUP_ID());
		txorder_id.setValue(entity == null ? "" : entity.getORDER_ID());
		txtsh_styleno.setValue(entity == null ? "" : entity.getSH_STYLENO());
		txordrer_num.setValue(entity == null ? "" : entity.getORDER_NUM());
		txtleft_size.setValue(entity == null ? "" : entity.getLEFT_SIZE_RUN());
		txtright_size.setValue(entity == null ? "" : entity.getRIGHT_SIZE_RUN());
		txttooling_size.setValue(entity == null ? "" : entity.getTOOLING_SIZE());
		txtnike_sh_aritcle.setValue(entity == null ? "" : entity.getNIKE_SH_ARITCLE());
		txtmodel_na.setValue(entity == null ? "" : entity.getMODEL_NA());
		txtpg_date.setValue(entity == null ? null : entity.getPG_DATE());
		txtround.setValue(entity == null ? "" : entity.getROUND());
		txtship_date.setValue(entity == null ? null : entity.getSHIPING_DATE());
		txtregion.setValue(entity == null ? "" : entity.getREGION());		
	}

	protected String OutShow1(String type) {
		// TODO Auto-generated method stub
		String TYPE="";
		if("0".equals(type)){
			TYPE="普通";
		}else if("1".equals(type)){
			TYPE="特殊";
		}
//		else if("2".equals(type)){
//			TYPE="虛擬";
//		}
		return TYPE;
	}
	
	protected String OutShow2(String status) {
		// TODO Auto-generated method stub
		String STATUS="";
		if("0".equals(status)){
			STATUS="預接單";
		}else if("7".equals(status)){
			STATUS="正式單";
		}else if("99".equals(status)){
			STATUS="取消單";
		}
		return STATUS;
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
		return "DSID01_TEMP";
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	protected boolean doCustomSave(Connection conn) {
		// TODO Auto-generated method stub
		return true;
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

	}
	
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
	
	}

}
