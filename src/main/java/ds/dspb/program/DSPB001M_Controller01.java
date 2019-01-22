package ds.dspb.program;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dspb.domain.DSPB00_NEW;
import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSPB001M_Controller01 extends Master {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txtPB_LANGTAG, txtPB_MUNODE, txtPB_MUITEM, txtPB_FILEPATH, txtPB_ICONPATH, txtPB_PRGNAME, txtPRGNAME, txtPB_ICONSCLASS;
	@Wire
	private Checkbox chkPB_ADMIN;
	@Wire
	private Label lblOPName;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnClear, btnPB_MUNODE;
	private UserCredential _userInfo = (UserCredential)Sessions.getCurrent().getAttribute("userCredential");
	private String strPB_NO, strPB_ID;

	String strPB_SYSID;
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		ServletContext sc =
			    (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);

		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_NO", getNewNO(), null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_SYSID", strPB_SYSID, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_ID", getNewID(), null, null));		
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_LANGTAG, "PB_LANGTAG", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_MUITEM, "PB_MUITEM", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_MUNODE, "PB_MUNODE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_FILEPATH, "PB_FILEPATH", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_ICONPATH, "PB_ICONPATH", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_ICONSCLASS, "PB_ICONSCLASS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtPB_PRGNAME, "PB_PRGNAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(chkPB_ADMIN, "PB_ADMIN", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));	
	}
	
	@Override
	protected Window getRootWindow() {
		return windowMaster;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMasterClass() {
		return DSPB001M_Controller01.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSPB00_NEW.class;
	}

	@Override
	protected OperationMode getOperationMode() {
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
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("PB_NO");
		masterKey.add("PB_SYSID");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSPB00_NEW entity = (DSPB00_NEW) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getPB_NO());
		masterKeyValue.add(entity.getPB_SYSID());

		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {
		return "pagingCourse";
	}

	@Override
	protected String getWhereConditionals() {
		String strSQL = " and t.PB_SYSID = '" + strPB_SYSID + "'";
		if (!txtPRGNAME.getValue().isEmpty())
			strSQL += " and t.PB_PRGNAME like '%" + txtPRGNAME.getValue() + "%'";
		return strSQL;
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
		DSPB00_NEW entity = (DSPB00_NEW) entityMaster;
		strPB_NO = (entity == null ? getNewNO() : entity.getPB_NO());
		strPB_ID = (entity == null ? getNewID() : entity.getPB_ID());
		txtPB_LANGTAG.setValue(entity == null ? "" : entity.getPB_LANGTAG());
		lblOPName.setValue(entity == null ? "" : Labels.getLabel(entity.getPB_LANGTAG()));
		txtPB_MUITEM.setValue(entity == null ? (getData_Mode().equals(DataMode.READ_MODE) ? "" : getNewItem()) : entity.getPB_MUITEM());
		txtPB_MUNODE.setValue(entity == null ? "" : entity.getPB_MUNODE());
		txtPB_FILEPATH.setValue(entity == null ? "" : entity.getPB_FILEPATH());
		txtPB_ICONPATH.setValue(entity == null ? "" : entity.getPB_ICONPATH());
		txtPB_ICONSCLASS.setValue(entity == null ? "" : entity.getPB_ICONSCLASS());
		txtPB_PRGNAME.setValue(entity == null ? "" : entity.getPB_PRGNAME());
		if (entity == null)
			chkPB_ADMIN.setChecked(false);
		else
			chkPB_ADMIN.setChecked(entity.getPB_ADMIN().equals("Y") ? true : false);
	}

	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnPB_MUNODE.setDisabled(true);
		txtPB_LANGTAG.setReadonly(true);
		txtPB_MUNODE.setReadonly(true);
		txtPB_MUITEM.setReadonly(true);
		txtPB_FILEPATH.setReadonly(true);
		txtPB_ICONPATH.setReadonly(true);
		txtPB_ICONSCLASS.setReadonly(true);
		txtPB_PRGNAME.setReadonly(true);
		chkPB_ADMIN.setDisabled(true);
		btnClear.setVisible(false);
	}
	
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnPB_MUNODE.setDisabled(false);
		txtPB_LANGTAG.setReadonly(false);
		txtPB_MUNODE.setReadonly(true);
		txtPB_MUITEM.setReadonly(true);
		txtPB_FILEPATH.setReadonly(false);
		txtPB_ICONPATH.setReadonly(false);
		txtPB_ICONSCLASS.setReadonly(false);
		txtPB_PRGNAME.setReadonly(false);
		chkPB_ADMIN.setDisabled(false);	
		btnClear.setVisible(true);		
	}
		
	@Override
	protected boolean beforeMasterSave(Object entityMaster) {		
		return true;
	}

	@Override
	protected void doCreateDefault() {
		chkPB_ADMIN.setChecked(true);
		txtPB_MUITEM.setValue(getNewItem());
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		switch (columnName){
			case "PB_NO":
				if (strPB_NO.isEmpty())
					return getNewNO();
				else
					return strPB_NO;
			case "PB_SYSID":
				return strPB_SYSID;
			case "PB_ID":
				if (strPB_ID.isEmpty())
					return getNewID();
				else
					return strPB_ID;
			case "UP_USER":
				return _userInfo.getAccount();
			case "UP_DATE":
				return new Date();
		}
		return "";
		
	}

	@Override
	protected String getEntityName() {
		return "DSPB00_NEW";
	}

	@Listen("onClick = #btnPB_MUNODE")
	public void onClickbtnPB_MUNODE(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("sysid", strPB_SYSID);
		Executions.createComponents("/ds/dspb/DSPB001M02.zul", null, map);
	}
	
	@Listen("onClick = #chkPB_ADMIN")
	public void onClickchkPB_ADMIN(Event event){
		txtPB_MUNODE.setValue("");
		txtPB_MUITEM.setValue(getNewItem());
	}
	
	@Listen("onClick = #btnClear")
	public void onClickbtnClear(Event evnet){
		txtPB_MUNODE.setValue("");
		txtPB_MUITEM.setValue(getNewItem());
	}
	
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event){
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSPB00_NEW e = (DSPB00_NEW) map.get("selectedRecord");
		txtPB_MUNODE.setValue(e.getPB_MUITEM());
		txtPB_MUITEM.setValue(getNewItem());
	}
	
	private String getNewNO(){
		String strNO = "";
		if (CRUDService != null){
			Query qry = CRUDService.getBetweenByLimit2("Select Max(t.PB_NO) From " + ENTITY_CLASSNAME + " t Where PB_SYSID = '" + strPB_SYSID + "'");
			if (qry.getResultList() == null || qry.getSingleResult() == null){
				strNO = "0001";			
			}else{
				int iNo = Integer.parseInt((String) qry.getSingleResult()) + 1;
				strNO = String.format("%04d", iNo);
			}
		}
		return strNO;
	}
	
	private String getNewID(){
		return strPB_SYSID + "_" + getNewNO();
	}
	
	private String getNewItem(){
		String sNO = "";
		if (CRUDService != null){
			String sSQL = "Select Max(t.PB_MUITEM) From " + ENTITY_CLASSNAME + 
					" t Where t.PB_SYSID = '" + strPB_SYSID + "' and t.PB_ADMIN = '" + (chkPB_ADMIN.isChecked()? "Y":"N") + 
					"' and t.PB_MUNODE" + (txtPB_MUNODE.getValue().isEmpty() ? " is null" : " = '" + txtPB_MUNODE.getValue() + "'");
			Query qry = CRUDService.getBetweenByLimit2(sSQL);
			if (qry.getResultList() == null || qry.getSingleResult() == null)
				sNO = "01";
			else			
				sNO = String.format("%02d", Integer.parseInt((String) qry.getSingleResult()) + 1);
		}
		return sNO;
	}

	@Override
	protected String getDetailConditionals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean beforeMasterDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
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
}
