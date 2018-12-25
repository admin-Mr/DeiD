package ds.dspb.program;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.dspb.domain.DSPB02;
import ds.dspb.domain.DSPB00_NEW;
import ds.dspb.domain.DSPB01;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.OperationMode;

public class DSPB003M_Controller03 extends Detail {
	private static final long serialVersionUID = 1L;

	@Wire
	protected Div detail2;
	@Wire
	protected Listbox detail2Listbox;
	@Wire
	private Textbox txtPB_ID;
	@Wire
	private Checkbox chkPB_RH01, chkPB_RH02, chkPB_RH03, chkPB_RH04, chkPB_RH08, chkPB_RH09, chkPB_RH10; 	
	@Wire
	private Button btnCreateDetail2, btnSaveDetail2, btnCancelDetail2;
	@Wire
	private Button btnPB_ID;
	
	private String strPB_SYSID, strPB_ID, strPB_USERID;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");			
		detailComponentColumns.add(new ComponentColumn<String>(null, "PB_USERID", strPB_USERID, null, null, true));
		detailComponentColumns.add(new ComponentColumn<String>(null, "PB_ID", strPB_ID, null, null, true));		
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH01", "PB_RH01", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH02", "PB_RH02", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH03", "PB_RH03", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH04", "PB_RH04", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH08", "PB_RH08", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH09", "PB_RH09", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>("chkPB_RH10", "PB_RH10", null, null, null, false));
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null, false));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null, false));
	}
	
	@Listen("onAfterRender = #detail2Listbox")
	public void onAfterRenderDetail2Listbox(Event event){
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)){
			chkPB_RH01 = (Checkbox)event.getTarget().getFellow("chkPB_RH01");
			chkPB_RH02 = (Checkbox)event.getTarget().getFellow("chkPB_RH02");		
			chkPB_RH03 = (Checkbox)event.getTarget().getFellow("chkPB_RH03");		
			chkPB_RH04 = (Checkbox)event.getTarget().getFellow("chkPB_RH04");		
			chkPB_RH08 = (Checkbox)event.getTarget().getFellow("chkPB_RH08");		
			chkPB_RH09 = (Checkbox)event.getTarget().getFellow("chkPB_RH09");
			chkPB_RH09 = (Checkbox)event.getTarget().getFellow("chkPB_RH10");
			if (getData_Mode().equals(DataMode.CREATE_MODE)){
				btnPB_ID = (Button)event.getTarget().getFellow("btnPB_ID");
				txtPB_ID = (Textbox)event.getTarget().getFellow("txtPB_ID");				
			}
		}		
	}	

	
	@Override
	public String getOrderByDetail(){
		return " Order by t.PB_ID ";
	}
	
	@Override
	protected Div getRootDiv() {
		return detail2;
	}

	@Override
	public Class getDetailClass() {
		return DSPB01.class;
	}

	@Override
	protected ArrayList<String> getDetailKeyName() {
		ArrayList<String> detailKey = new ArrayList<String>();
		detailKey.add("PB_USERID");
		detailKey.add("PB_ID");		
		return detailKey;
	}

	@Override
	protected ArrayList<String> getDetailKeyValue(Object entityDetail) {
		DSPB01 e = (DSPB01) entityDetail;
		ArrayList<String> detailValue = new ArrayList<String>();
		detailValue.add(e.getPB_USERID());
		detailValue.add(e.getPB_ID());		
		return detailValue;
	}

	@Override
	protected String getPagingIdDetail() {
		return "pagingDetail2Course";
	}

	@Override
	protected HashMap getDetailCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getDetailCreateZul() {
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
		if (getData_Mode().equals(DataMode.CREATE_MODE)){
			Query qry = CRUDService.getBetweenByLimit2("Select t From DSPB01 t Where t.PB_USERID='" + strPB_USERID +
						"' AND t.PB_ID='" + strPB_ID + "'");
			if (qry.getResultList().size() > 0){
				Messagebox.show(Labels.getLabel("PUBLIC.MSG02"), "Fail", Messagebox.OK, Messagebox.ERROR);				
				return false;
			}
		}
		return true;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		DSPB01 entity = (DSPB01) entityDetail;		
		strPB_ID = (entity == null ? "" : entity.getPB_ID());
		strPB_USERID = (entity == null ? "" : entity.getPB_USERID());
	}
	
	@Override
	public void detailReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail2);
		super.detailReadMode(mapButton);
	}
	
	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail2);
		super.detailCreateMode(mapButton);
	}

	@Override
	protected void doCreateDefault() {
		DSPB02 master = (DSPB02) getMasterProgram().getMasterSel();	
		strPB_USERID = master.getPB_USERID();			
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		switch (columnName){
			case "PB_USERID":
				return strPB_USERID;
			case "PB_ID":
				return strPB_ID;
			case "UP_USER":
				return _userInfo.getAccount();
			case "UP_DATE":
				return new Date();
		}
		return null;
	}

	@Override
	protected Listbox getDetailListbox() {
		return detail2Listbox;
	}

	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}

	@Listen("onPBID=#detail2Listbox")
	public void onClickbtnPB_ID(ForwardEvent evt){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", getParentWindow());
		map.put("detailindex", "1");
		map.put("account", strPB_USERID);
		Executions.createComponents("/ds/dspb/DSPB002M03.zul", null, map);
	}
	
	@SuppressWarnings({"unchecked"})
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event){
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSPB00_NEW e = (DSPB00_NEW) map.get("selectedRecord");
		txtPB_ID.setValue(Labels.getLabel(e.getPB_LANGTAG()));
		strPB_ID = e.getPB_ID();
	}

	@Override
	protected String getWhereConditionals() {
		return "and t.PB_ID in (select PB_ID from DSPB00_NEW n where n.PB_SYSID ='" + strPB_SYSID + "')";
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

	@SuppressWarnings("rawtypes")
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
