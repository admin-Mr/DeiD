package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sun.tracing.dtrace.ProviderAttributes;

import ds.dsid.domain.DSID04;
import ds.dsid.domain.DSID21;
import util.Common;
import util.ComponentColumn;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID21MProgram extends Master {

	@Wire private Window windowMaster;
	@Wire private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDelete, btnImport, btnExport, btnCustomExr,
					btnBatdelete;	
	@Wire private Textbox txtModel_na, txtItems, txtGr_no, txtGr_na, txtColor, txtEl_no, txtEl_na, txtSize_fd, txtNote,
					txtType, txtUp_user;
	@Wire private Datebox txtImport_date, txtUp_date;
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		
		super.doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(txtModel_na, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtItems, "ITEMS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType, "TYPE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtGr_no, "GR_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtGr_na, "GR_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtColor, "COLOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtEl_na, "EL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtEl_no, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtSize_fd, "SEZI_FD", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtNote, "NOTE", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<Date>(txtImport_date, "IMPORT_DATE", new Date(), null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtUp_user, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(txtUp_date, "UP_DATE", new Date(), null, null));	
		
	}
	
	// 匯入
	@Listen("onClick =#btnImport")
	public void onClickbtnImport(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MImport.zul", null, map);
	}
	
	// 匯出
	@Listen("onClick =#btnExport")
	public void onClickbtnExport(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MExport.zul", null, map);
	}
	
	// 批量刪除
	@Listen("onClick =#btnBatdelete")
	public void onClickbtnBatdelete(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MBatDelete.zul", null, map);
	}
	
	
	// 批量刪除
	@Listen("onClick =#btnCustomExr")
	public void onClickbtnCustomExr(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MCustomExr.zul", null, map);
	}
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID21MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID21.class;
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
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("MODEL_NA");
		masterKey.add("ITEMS");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID21 entity = (DSID21) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getITEMS());

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
		String string =  "";
		return string;
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
		DSID21 entity = (DSID21) entityMaster;
		txtModel_na.setValue(entity == null ? "" : entity.getMODEL_NA());
		txtItems.setValue(entity == null ? "" : entity.getITEMS());
		txtGr_no.setValue(entity == null ? "" : entity.getGR_NO());
		txtGr_na.setValue(entity == null ? "" : entity.getGR_NA());
		txtColor.setValue(entity == null ? "" : entity.getCOLOR());
		txtEl_no.setValue(entity == null ? "" : entity.getEL_NO());
		txtEl_na.setValue(entity == null ? "" : entity.getEL_NA());
		txtSize_fd.setValue(entity == null ? "" : entity.getSIZE_FD());
		txtNote.setValue(entity == null ? "" : entity.getNOTE());
		txtImport_date.setValue(entity == null ? null : entity.getIMPORT_DATE());
		txtUp_user.setValue(entity == null ? "" : entity.getUP_USER());
		txtUp_date.setValue(entity == null ? null : entity.getUP_DATE());
		
		if(entity == null || "".equals(entity)){
			txtType.setValue("");
		}else{

			//System.out.println(" ----- 類型判斷 ！" + entity.getTYPE());
			switch (entity.getTYPE()) {
			case "0":
				txtType.setValue("未設定"); 
				break;
			case "1":
				txtType.setValue("前面片"); 
				break;
			case "2":
				txtType.setValue("鞋帶");
				break;
			case "3":
				txtType.setValue("前氣墊");
				break;
			case "4":
				txtType.setValue("後氣墊");
				break;
			case "5":
				txtType.setValue("港寶"); 
				break;
			case "6":
				txtType.setValue("布標"); 
				break;
			case "7":
				txtType.setValue("鞋帶");
				break;
			default:
				txtType.setValue("/");
				break;
			}
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
		txtItems.setValue(AutoSeq());
	}
	
	private String AutoSeq() {
		// TODO Auto-generated method stub
		DSID21 entity = new DSID21();
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String items = "";
		
		String sql = "SELECT LPAD(NVL(MAX(items),0)+1,4,'0')items FROM DSID21 where model_no = '"+entity.getMODEL_NA()+"'";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				items = rs.getString("items");
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Common.closeConnection(conn);	
		}
		
		return items;
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID21";
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	// btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster,
	// btnDelete, btnImport, btnExport, btnCustomExr
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnQuery.setDisabled(false);
		btnEditMaster.setDisabled(false);
		btnCreateMaster.setDisabled(false);
		btnDelete.setDisabled(false);
		btnImport.setDisabled(false);
		btnExport.setDisabled(false);
		btnCustomExr.setDisabled(false);
		btnEditMaster.setDisabled(false);
		btnDelete.setDisabled(false);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnBatdelete.setDisabled(false);
		btnCustomExr.setDisabled(false);
		
		txtModel_na.setReadonly(true);
		txtItems.setReadonly(true);
		txtGr_no.setReadonly(true);
		txtGr_na.setReadonly(true);
		txtColor.setReadonly(true);
		txtEl_no.setReadonly(true);
		txtEl_na.setReadonly(true);
		txtSize_fd.setReadonly(true);
		txtNote.setReadonly(true);
		txtType.setReadonly(true);
		txtImport_date.setReadonly(true);
		txtUp_user.setReadonly(true);
		txtUp_date.setReadonly(true);

	}
	
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnQuery.setDisabled(true);
		btnEditMaster.setDisabled(true);
		btnCreateMaster.setDisabled(true);
		btnDelete.setDisabled(true);
		btnImport.setDisabled(true);
		btnExport.setDisabled(true);
		btnCustomExr.setDisabled(true);
		btnEditMaster.setDisabled(true);
		btnDelete.setDisabled(true);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnBatdelete.setDisabled(true);
		btnCustomExr.setDisabled(true);
		
		txtModel_na.setReadonly(false);
		txtItems.setReadonly(false);
		txtGr_no.setReadonly(false);
		txtGr_na.setReadonly(false);
		txtColor.setReadonly(false);
		txtEl_no.setReadonly(false);
		txtEl_na.setReadonly(false);
		txtSize_fd.setReadonly(false);
		txtNote.setReadonly(false);
		txtType.setReadonly(false);
		txtImport_date.setReadonly(false);
		txtUp_user.setReadonly(false);
		txtUp_date.setReadonly(false);
		
	}
}
