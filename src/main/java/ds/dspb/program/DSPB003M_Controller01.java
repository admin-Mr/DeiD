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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dspb.domain.DSPB00_NEW;
import ds.dspb.domain.DSPB01_GRP;
import ds.dspb.domain.DSPB02;
import util.BlackBox;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.EntityCheck;
import util.Master;
import util.OperationMode;
import util.admin.Convert;

public class DSPB003M_Controller01 extends Master {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window windowMaster;
	@Wire
	private Tabbox tabDetail;
	@Wire
	private Tabpanel panDetail1, panDetail2;
	@Wire
	private Textbox txtPB_EMUSERQ, txtPB_NAMEQ, txtPB_USERIDQ;
	@Wire
	private Textbox txtPB_EMUSER, txtPB_NAME, txtPB_EMAIL, txtUSERID, txtPASS, txtPB_FANO, txtPB_DASHBOARD;
	@Wire
	private Button btnQuery, btnCreateMaster, btnSaveMaster, btnCancelMaster, btnDashBoard;
	@Wire
	private Checkbox chkPB_LOCK;
	
	final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	private String strPB_PASSENCRY = "", strPB_SYSID = "", sDASHBOARD;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		ServletContext sc = Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		List<EntityCheck> arrCheck = new ArrayList<EntityCheck>();
		List<String> arrErrMsg = new ArrayList<String>();
		arrCheck.add(EntityCheck.NULL_EMPTYSTRING);
		arrErrMsg.add(Labels.getLabel("DSPB.DSPB02.PB_CODE") + " " + Labels.getLabel("PUBLIC.MSG0003"));
		masterComponentColumns.add(new ComponentColumn<String>("txtPB_EMUSER", "PB_EMUSER", null, arrCheck, arrErrMsg, false));
		arrErrMsg = new ArrayList<String>();
		arrErrMsg.add(Labels.getLabel("DSPB.DSPB02.PB_NAME") + " " + Labels.getLabel("PUBLIC.MSG0003"));
		masterComponentColumns.add(new ComponentColumn<String>("txtPB_NAME", "PB_NAME", null, arrCheck, arrErrMsg, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_CNAME", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_FANO", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<String>("txtPB_EMAIL", "PB_EMAIL", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_USERID", null, null, null, true));
		arrErrMsg = new ArrayList<String>();
		arrErrMsg.add(Labels.getLabel("DSPB.DSPB02.PB_PASS") + " " + Labels.getLabel("PUBLIC.MSG0003"));
		masterComponentColumns.add(new ComponentColumn<String>("txtPASS", "PB_PASS", null, arrCheck, arrErrMsg, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_PASSENCRY", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<String>("chkPB_LOCK", "PB_LOCK", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null, false));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null, false));
		super.setWhereConditionals(" and 1=0");
		super.doFillListbox(0);
	}

	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			txtPB_EMUSER = (Textbox) windowMaster.getFellow("txtPB_EMUSER");
			txtPB_NAME = (Textbox) windowMaster.getFellow("txtPB_NAME");
			txtPB_FANO = (Textbox) windowMaster.getFellow("txtPB_FANO");
			txtPB_EMAIL = (Textbox) windowMaster.getFellow("txtPB_EMAIL");
			txtUSERID = (Textbox) windowMaster.getFellow("txtUSERID");
			txtPASS = (Textbox) windowMaster.getFellow("txtPASS");
			chkPB_LOCK = (Checkbox) windowMaster.getFellow("chkPB_LOCK");
			txtPB_DASHBOARD = (Textbox) windowMaster.getFellow("txtPB_DASHBOARD");
		}
	}

	@Override
	protected Window getRootWindow() {
		return windowMaster;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMasterClass() {
		return DSPB003M_Controller01.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSPB02.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getDetailClass(int indexDetail) {
		return getDetailProgram(indexDetail).getDetailClass();
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		return getArrDetailPrograms().get(indexDetail);
	}

	@Override
	protected void addDetailPrograms() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 0);
		super.addDetailPrograms("/ds/dspb/DSPB003M02.zul", panDetail1, map);
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 1);
		super.addDetailPrograms("/ds/dspb/DSPB003M03.zul", panDetail2, map);
		setDetail(2);
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("PB_USERID");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSPB02 entity = (DSPB02) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getPB_USERID());
		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {
		return "pagingCourse";
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
	protected void resetEditAreaMaster(Object entityMaster) {
		DSPB02 e = (DSPB02) entityMaster;
		strPB_PASSENCRY = (e == null ? "" : (e.getPB_PASSENCRY() == null ? "" : e.getPB_PASSENCRY()));
		sDASHBOARD = (e == null ? "" : (e.getDSPB48() == null ? "" : e.getDSPB48().getPB_DASHBOARD()));
	}

	@Override
	public void masterReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterReadMode(mapButton);
	}

	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
	}

	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		if (getData_Mode().equals(DataMode.CREATE_MODE)) {
			if (txtUSERID.getValue().isEmpty()) {
				Messagebox.show(Labels.getLabel("DSPB.DSPB02.PB_USERID") + " " + Labels.getLabel("PUBLIC.MSG0003"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return false;
			} else {
				String strUSERID = txtUSERID.getValue().toUpperCase();
				Query qry = CRUDService.createSQL("Select * From DSPB02 Where PB_USERID = '" + strUSERID + "' or PB_EMUSER = '" + txtPB_EMUSER.getValue() + "'");
				if (qry.getResultList().size() > 0) {
					Messagebox.show(Labels.getLabel("DSPB.DSPB02.PB_CODE") + "/" + Labels.getLabel("DSPB.DSPB02.PB_USERID") + " " + Labels.getLabel("PUBLIC.MSG0024"), "Warning", Messagebox.OK,
							Messagebox.EXCLAMATION);
					txtUSERID.setValue("");
					txtPASS.setValue("");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
	}

	@Override
	protected String getEntityName() {
		return null;
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		switch (columnName) {
		case "PB_USERID":
			return txtUSERID.getValue().toUpperCase();
		case "PB_CNAME":
			return txtPB_NAME.getValue();
		case "PB_FANO":
			return txtPB_FANO.getValue();
		case "PB_PASSENCRY":
			strPB_PASSENCRY = (txtPASS.getValue() == "0000" ? strPB_PASSENCRY : (Convert.process(txtPASS.getValue())).substring(0, 8));
			return strPB_PASSENCRY;
		case "UP_USER":
			return _userInfo.getAccount();
		case "UP_DATE":
			return new Date();
		}
		return null;
	}

	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}

	@Listen("onOK=#txtPB_EMUSERQ, #txtPB_NAMEQ, #txtPB_USERIDQ")
	public void onOKTextbox() {
		super.executeQuery();
	}

	@Override
	protected String getWhereConditionals() {
		String sql = "";
		if (txtPB_EMUSERQ.getValue().trim().isEmpty() && txtPB_NAMEQ.getValue().trim().isEmpty() && txtPB_USERIDQ.getValue().trim().isEmpty()) {
			sql += "and 1=0";
		} else {
			if (!txtPB_EMUSERQ.getValue().trim().isEmpty()) {
				sql += " and t.PB_EMUSER = '" + txtPB_EMUSERQ.getValue() + "' ";
			}
			if (!txtPB_NAMEQ.getValue().trim().isEmpty()) {
				sql += " and Upper(t.PB_NAME) like '%" + txtPB_NAMEQ.getValue().toUpperCase() + "%' ";
			}
			if (!txtPB_USERIDQ.getValue().trim().isEmpty()) {
				sql += " and t.PB_USERID = '" + txtPB_USERIDQ.getValue().toUpperCase() + "' ";
			}
		}
		return sql;
	}

	@Override
	protected HashMap getMasterCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailConditionals() {
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
		if (sDASHBOARD != null && !sDASHBOARD.isEmpty()) {
			String strUSERID = txtUSERID.getValue().toUpperCase();
			Query qryPost = CRUDService.createSQL("SELECT PB_DASHBOARD FROM DSPB48 WHERE PB_USERID = :PB_USERID");

			if (qryPost.setParameter("PB_USERID", strUSERID).getResultList().isEmpty()) {
				try {
					String inSql = "INSERT INTO DSPB48 (PB_USERID, PB_DASHBOARD) "
							+ "VALUES (:PB_USERID, :PB_DASHBOARD)";
					List list = new ArrayList<Object>();
					list.add(strUSERID);
					list.add(sDASHBOARD);

					BlackBox.executeSQLStatement(conn, inSql, list);
				} catch (Exception e) {
					logger.error("", e);
					return false;
				}
			} else {
				try {
					String upSql = "UPDATE DSPB48 SET PB_DASHBOARD = :PB_DASHBOARD "
							+ "WHERE PB_USERID = :PB_USERID ";
					List list = new ArrayList<Object>();
					list.add(sDASHBOARD);
					list.add(strUSERID);

					BlackBox.executeSQLStatement(conn, upSql, list);
				} catch (Exception e) {
					logger.error("", e);
					return false;
				} 
			}
		}
		return true;
	}

	@Override
	protected List getCustList() {
		return null;
	}
	
	@Listen("onQryDashBoard=#masterListbox")
	public void onQryDashBoardmasterListbox(Event event) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("parentWindow", getRootWindow());
		map.put("USER_ID", _userInfo.getAccount());
		map.put("returnMethod", "onQryDashBoard");
		Executions.createComponents("/ds/dspb/DSPB003M05.zul", null, map);
	}
	
	@Listen("onQryDashBoard = #windowMaster")
	public void onQryDashBoardwindowMaster(Event event) {
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSPB00_NEW selectedRecord = (DSPB00_NEW) map.get("selectedRecord");
		
		txtPB_DASHBOARD = (Textbox) windowMaster.getFellow("txtPB_DASHBOARD");
		sDASHBOARD = selectedRecord.getPB_ID();
		txtPB_DASHBOARD.setValue(Labels.getLabel(selectedRecord.getPB_LANGTAG()));
	}
	
	@Listen("onQryDashBoardClear=#masterListbox")
	public void onQryDashBoardClearmasterListbox(Event event) {
		sDASHBOARD = "";
		txtPB_DASHBOARD.setValue(null);
	}
}
