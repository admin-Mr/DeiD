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
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.dspb.domain.DSPB02;
import ds.dspb.domain.DSPB04;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.OperationMode;

public class DSPB003M_Controller02 extends Detail {
	private static final long serialVersionUID = 1L;

	@Wire
	protected Div detail1;
	@Wire
	protected Listbox detailListbox;
	@Wire
	private Textbox txtPB_GROUPID;
	@Wire
	private Button btnCreateDetail, btnSaveDetail, btnCancelDetail;
	@Wire
	private Button btnPB_GROUPID;

	private String strPB_SYSID, strPB_USERID, strPB_GROUPID;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		detailComponentColumns.add(new ComponentColumn<String>(null, "PB_USERID", strPB_USERID, null, null, true));
		detailComponentColumns.add(new ComponentColumn<String>(null, "PB_GROUPID", strPB_GROUPID, null, null, true));
		detailComponentColumns
				.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null, false));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null, false));
	}

	@Listen("onAfterRender = #detailListbox")
	public void onAfterRenderDetailListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			txtPB_GROUPID = (Textbox) getParentWindow().getFellow("txtPB_GROUPID");
			btnPB_GROUPID = (Button) getParentWindow().getFellow("btnPB_GROUPID");
		}
	}

	@Override
	protected Div getRootDiv() {
		return detail1;
	}

	@Override
	public Class getDetailClass() {
		return DSPB04.class;
	}

	@Override
	protected ArrayList<String> getDetailKeyName() {
		ArrayList<String> detailKey = new ArrayList<String>();
		detailKey.add("PB_GROUPID");
		detailKey.add("PB_USERID");
		return detailKey;
	}

	@Override
	protected ArrayList<String> getDetailKeyValue(Object entityDetail) {
		DSPB04 e = (DSPB04) entityDetail;
		ArrayList<String> detailValue = new ArrayList<String>();
		detailValue.add(e.getPB_GROUPID());
		detailValue.add(e.getPB_USERID());
		return detailValue;
	}

	@Override
	protected String getPagingIdDetail() {
		return "pagingDetailCourse";
	}

	@Override
	protected String getDetailCreateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getDetailCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailUpdateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean beforeDetailSave(Object entityMaster) {
		if (getData_Mode().equals(DataMode.CREATE_MODE)) {
			Query qry = CRUDService.getBetweenByLimit2("Select t From DSPB04 t Where t.PB_USERID='" + strPB_USERID
					+ "' AND t.PB_GROUPID='" + strPB_GROUPID + "'");
			if (qry.getResultList().size() > 0) {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0056"), "Fail", Messagebox.OK, Messagebox.ERROR);
				return false;
			}
		}
		return true;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		DSPB04 entity = (DSPB04) entityDetail;
		strPB_USERID = (entity == null ? "" : entity.getPB_USERID());
		strPB_GROUPID = (entity == null ? "" : entity.getPB_GROUPID());
	}

	@Override
	public void detailReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail);
		super.detailReadMode(mapButton);
	}

	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail);
		super.detailCreateMode(mapButton);
	}

	@Override
	protected void doCreateDefault() {
		DSPB02 master = (DSPB02) getMasterProgram().getMasterSel();
		strPB_USERID = master.getPB_USERID();
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		switch (columnName) {
		case "PB_USERID":
			return strPB_USERID;
		case "PB_GROUPID":
			return strPB_GROUPID;
		case "UP_USER":
			return _userInfo.getAccount();
		case "UP_DATE":
			return new Date();
		}
		return null;
	}

	@Override
	protected Listbox getDetailListbox() {
		return detailListbox;
	}

	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}

	@Listen("onGroupID = #detailListbox")
	public void onClickbtnPB_GROUPID(Event event) {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", getParentWindow());
		map.put("sysid", strPB_SYSID);
		Executions.createComponents("/ds/dspb/DSPB003M04.zul", null, map);
	}

	@SuppressWarnings({ "unchecked" })
	@Listen("onRoleQueryWindowsSend = #windowMaster")
	public void onRoleQueryWindowsSend(Event event) {
		Map<String, Object> map = (Map<String, Object>) event.getData();
		txtPB_GROUPID.setValue((String) map.get("groupname"));
		strPB_GROUPID = (String) map.get("groupid");
	}

	@Override
	protected String getWhereConditionals() {
		DSPB02 master = (DSPB02) getMasterProgram().getMasterSel();
		if (master != null) {
			return " AND t.PB_USERID='" + master.getPB_USERID() + "' AND t.PB_GROUPID LIKE'" + strPB_SYSID + "%'";
		} else {
			return null;
		}
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean beforeDetailDel(Object entityMaster) {
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
		return null;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return null;
	}
}
