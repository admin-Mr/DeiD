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
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dspb.domain.DSPB01_GRP;
import ds.dspb.domain.DSPB03;
import util.BlackBox;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.EntityCheck;
import util.Master;
import util.OperationMode;

public class DSPB002M_Controller01 extends Master {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window windowMaster;
	@Wire
	private Tabbox tabDetail;
	@Wire
	private Tabpanel panDetail1;
	@Wire
	private Textbox txtGROUPNAME, txtPB_GROUPNA, txtPB_DASHBOARD;
	@Wire
	private Button btnQuery, btnCreateMaster, btnSaveMaster, btnCancelMaster, btnDashBoard, btnDashBoardClear;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	private String strPB_GROUPID, sDASHBOARD, strPB_SYSID;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		List<EntityCheck> arrCheck = new ArrayList<EntityCheck>();
		List<String> arrErrMsg = new ArrayList<String>();
		arrCheck.add(EntityCheck.NULL_EMPTYSTRING);
		arrErrMsg.add(Labels.getLabel("DSPB.DSPB03.PB_GROUPNA") + " " + Labels.getLabel("PUBLIC.MSG0003"));
		masterComponentColumns.add(new ComponentColumn<String>("txtPB_GROUPNA", "PB_GROUPNA", null, arrCheck, arrErrMsg, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_GROUPID", null, null, null, true));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_SYSID", null, null, null, true));
		masterComponentColumns.add(new ComponentColumn<String>(null, "PB_USERID", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", null, null, null, false));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", null, null, null, false));

	}

	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			txtPB_GROUPNA = (Textbox) windowMaster.getFellow("txtPB_GROUPNA");
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
		return DSPB002M_Controller01.class;
	}

	@Override
	protected Class getEntityClass() {
		return DSPB03.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}

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
		super.addDetailPrograms("/ds/dspb/DSPB002M02.zul", panDetail1, map);
		setDetail(1);
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("PB_GROUPID");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSPB03 entity = (DSPB03) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getPB_GROUPID());
		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {
		return "pagingCourse";
	}

	@Override
	protected String getWhereConditionals() {
		String strSQL = " and t.PB_SYSID = '" + strPB_SYSID + "'";
		if (!txtGROUPNAME.getValue().isEmpty())
			strSQL += " and t.PB_GROUPNA = '" + txtGROUPNAME.getValue() + "'";
		
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
		DSPB03 entity = (DSPB03) entityMaster;
		strPB_GROUPID = (entity == null ? getNewID() : entity.getPB_GROUPID());
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
		return true;
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object doSaveDefault(String columnName) {
		switch (columnName) {
		case "PB_GROUPID":
			if (strPB_GROUPID.isEmpty())
				return getNewID();
			else
				return strPB_GROUPID;
		case "PB_USERID":
			return "ADMIN";
		case "PB_SYSID":
			return strPB_SYSID;
		case "UP_USER":
			return _userInfo.getAccount();
		case "UP_DATE":
			return new Date();
		}
		return null;
	}

	@Override
	protected String getEntityName() {
		return null;
	}

	private String getNewID() {
		String strNO = "";
		if (CRUDService != null) {
			Query qry = CRUDService.getBetweenByLimit2("Select Max(t.PB_GROUPID) From "
					+ ENTITY_CLASSNAME.replace("VO", "") + " t Where PB_SYSID='" + strPB_SYSID + "'");
			if (qry.getResultList() == null || qry.getSingleResult() == null) {
				strNO = "0001";
			} else {
				String sOldNO = (String) qry.getSingleResult();
				int iNO = Integer.parseInt(sOldNO.replace(strPB_SYSID, "")) + 1;
				strNO = String.format("%04d", iNO);
			}
		}
		return strPB_SYSID + strNO;
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
		if (sDASHBOARD != null && !sDASHBOARD.isEmpty()) {
			Query qryPost = CRUDService.createSQL("SELECT PB_DASHBOARD FROM DSPB47 WHERE PB_GROUPID = :PB_GROUPID");

			if (qryPost.setParameter("PB_GROUPID", strPB_GROUPID).getResultList().isEmpty()) {
				try {
					String inSql = "INSERT INTO DSPB47 (PB_GROUPID, PB_USERID, PB_DASHBOARD) "
							+ "VALUES (:PB_GROUPID, :PB_USERID, :PB_DASHBOARD) ";
					List list = new ArrayList<Object>();
					list.add(strPB_GROUPID);
					list.add("ADMIN");
					list.add(sDASHBOARD);
					
					BlackBox.executeSQLStatement(conn, inSql, list);
				} catch (Exception e) {
					logger.error(null, e);
					return false;
				}
			} else {
				try {
					String upSql = "UPDATE DSPB47 SET PB_DASHBOARD = :PB_DASHBOARD "
							+ "WHERE PB_GROUPID = :PB_GROUPID "
							+ " AND PB_USERID = :PB_USERID ";
					List list = new ArrayList<Object>();
					list.add(sDASHBOARD);
					list.add(((DSPB03) getMasterSel()).getPB_GROUPID());
					list.add(((DSPB03) getMasterSel()).getPB_USERID());
					
					BlackBox.executeSQLStatement(conn, upSql, list);
				} catch (Exception e) {
					logger.error(null, e);
					return false;
				} 
			}

		}
		return true;
	}


	@Override
	public boolean doCustomerDel(Connection conn) {
		// TODO Auto-generated method stub
		return super.doCustomerDel(conn);
	}
	
	@Override
	protected List getCustList() {
		return null;
	}
	
	@Listen("onQryDashBoard=#masterListbox")
	public void onQryDashBoardmasterListbox(Event event) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("parentWindow", getRootWindow());
		map.put("PB_GROUPID", strPB_GROUPID);
		map.put("returnMethod", "onQryDashBoard");
		Executions.createComponents("/ds/dspb/DSPB002M04.zul", null, map);
	}
	
	@Listen("onQryDashBoard = #windowMaster")
	public void onQryDashBoardwindowMaster(Event event) {
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSPB01_GRP selectedRecord = (DSPB01_GRP) map.get("selectedRecord");
		
		txtPB_DASHBOARD = (Textbox) windowMaster.getFellow("txtPB_DASHBOARD");
		sDASHBOARD = selectedRecord.getDSPB00_NEW().getPB_ID();
		txtPB_DASHBOARD.setValue(Labels.getLabel(selectedRecord.getDSPB00_NEW().getPB_LANGTAG()));
	}
	
	@Listen("onQryDashBoardClear=#masterListbox")
	public void onQryDashBoardClearmasterListbox(Event event) {
		sDASHBOARD = "";
		txtPB_DASHBOARD.setValue(null);
	}
}
