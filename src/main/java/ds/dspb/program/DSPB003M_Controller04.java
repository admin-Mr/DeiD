package ds.dspb.program;

import java.awt.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import ds.dspb.domain.DSPB01_GRP;
import util.QueryBase;

public class DSPB003M_Controller04 extends QueryBase {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window windowQuery,windowParent;
	@Wire
	private Combobox cboRole;
	@Wire
	private Button btnCancel,btnConfirm;

	String strPB_SYSID;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Execution execution = Executions.getCurrent();
		strPB_SYSID = (String) execution.getArg().get("sysid");
		windowParent = (Window) execution.getArg().get("parentWindow");
		Query qry = CRUDService.getBetweenByLimit2("SELECT t FROM DSPB03 t WHERE t.PB_SYSID='" + strPB_SYSID + "'");
		List lst = qry.getResultList();
		ListModelList modelRole = new ListModelList(lst, true);
		cboRole.setModel(modelRole);
		cboRole.setReadonly(true);
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowQuery;
	}

	@Override
	protected Class getQueryClass() {
		// TODO Auto-generated method stub
		return DSPB003M_Controller04.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSPB01_GRP.class;
	}

	@Override
	protected ArrayList<String> getKeyName() {
		ArrayList<String> key = new ArrayList<String>();
		key.add("PB_ID");
		key.add("PB_GROUPID");
		return key;
	}

	@Override
	protected ArrayList<String> getKeyValue(Object entity) {
		DSPB01_GRP DSPB01_GRPentity= (DSPB01_GRP) entity;
		ArrayList<String> keyValue = new ArrayList<String>();
		keyValue.add(DSPB01_GRPentity.getPB_ID());
		keyValue.add(DSPB01_GRPentity.getPB_GROUPID());
		return keyValue;
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
	}

	@Override
	protected String getWhereConditionals() {
		String sWhere=" AND 1=2 ";
		if(!cboRole.getValue().equals("")){
			sWhere=" AND PB_GROUPID='"+cboRole.getSelectedItem().getValue()+"'";
		}
		return sWhere;
	}

	@Override
	protected void resetEditArea(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 10;
	}
	
	@Listen("onClick = #btnConfirm")
	public void onClickbtnConfirm(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (cboRole.getSelectedItem() == null) {
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Fail", Messagebox.OK, Messagebox.ERROR);
			return;
		} else {
			if (!cboRole.getSelectedItem().getValue().equals("")){
				map.put("groupname", cboRole.getValue());
				map.put("groupid", cboRole.getSelectedItem().getValue());
			} else {
				map.put("groupname", "");
				map.put("groupid", "");				
			}
			windowQuery.detach();
			Events.sendEvent(new Event("onRoleQueryWindowsSend", windowParent, map));
		}
	
	}
	
	@Listen("onChange = #cboRole")
	public void onChangecboRole(Event event){
		executeQuery();
	}
	
	@Listen("onClick = #btnCancel")
	public void onClickbtnCancel(Event event){
		getRootWindow().detach();
	}
}
