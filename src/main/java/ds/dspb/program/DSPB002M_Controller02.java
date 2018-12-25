package ds.dspb.program;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import ds.dspb.domain.DSPB00_NEW;
import ds.dspb.domain.DSPB01_GRP;
import ds.dspb.domain.DSPB03;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.EntityCheck;
import util.OperationMode;

public class DSPB002M_Controller02 extends Detail {
	private static final long serialVersionUID = 1L;

	@Wire
	protected Div detail1;
	@Wire
	protected Listbox detailListbox;
	@Wire
	private Textbox txtPB_ID;
	@Wire
	private Button btnPB_ID;
	@Wire
	private Checkbox chkPB_RH01, chkPB_RH02, chkPB_RH03, chkPB_RH04, chkPB_RH08, chkPB_RH09, chkPB_RH10;
	@Wire
	private Button btnCreateDetail, btnSaveDetail, btnCancelDetail;
	private String strPB_SYSID, strPB_GROUPID, strPB_ID;
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");				
		List<EntityCheck> arrCheck = new ArrayList<EntityCheck>();
		List<String> arrErrMsg = new ArrayList<String>();
		arrCheck.add(EntityCheck.NULL_EMPTYSTRING);
		arrErrMsg.add(Labels.getLabel("DSPB.DSPB01_GRP.PB_ID") + " " + Labels.getLabel("PUBLIC.MSG0003"));		
		detailComponentColumns.add(new ComponentColumn<String>(null, "PB_GROUPID", null, null, null, true));		
		detailComponentColumns.add(new ComponentColumn<String>(null, "PB_ID", null, arrCheck, arrErrMsg, true));
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
	
	@Listen("onAfterRender = #detailListbox")
	public void onAfterRenderDetailListbox(Event event){
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)){
			chkPB_RH01 = (Checkbox)getParentWindow().getFellow("chkPB_RH01");		
			chkPB_RH02 = (Checkbox)getParentWindow().getFellow("chkPB_RH02");		
			chkPB_RH03 = (Checkbox)getParentWindow().getFellow("chkPB_RH03");		
			chkPB_RH04 = (Checkbox)getParentWindow().getFellow("chkPB_RH04");		
			chkPB_RH08 = (Checkbox)getParentWindow().getFellow("chkPB_RH08");		
			chkPB_RH09 = (Checkbox)getParentWindow().getFellow("chkPB_RH09");
			chkPB_RH09 = (Checkbox)getParentWindow().getFellow("chkPB_RH10");
			if (getData_Mode().equals(DataMode.CREATE_MODE)){
				btnPB_ID = (Button)getParentWindow().getFellow("btnPB_ID");
				txtPB_ID = (Textbox)getParentWindow().getFellow("txtPB_ID");				
			}
		}
	}
	
//	@Override
//	public String getQueryResultSizeDetailBase(){
//		return "SELECT COUNT(t.PB_ID) " +
//				"FROM DSPB01_GRP t LEFT JOIN DSPB00_NEW ON t.PB_ID=DSPB00_NEW.PB_ID ";
//	}
//	
//	@Override
//	public String getQueryPagingDetailBase(){
//		return "SELECT PB_GROUPID, t.PB_ID, PB_LANGTAG, PB_RH01, PB_RH02, PB_RH03, PB_RH04, PB_RH08, PB_RH09, PB_RH10, t.UP_USER, t.UP_DATE "+
//				"FROM DSPB01_GRP t LEFT JOIN DSPB00_NEW ON t.PB_ID=DSPB00_NEW.PB_ID ";
//	}
//	
//	@Override
//	public String getQueryOneColDetailBase(){
//		return "SELECT distinct PB_GROUPID, t.PB_ID " +
//				"FROM DSPB01_GRP t LEFT JOIN DSPB00_NEW ON t.PB_ID=DSPB00_NEW.PB_ID ";
//	}
	
	
	@Override
	public String getOrderByDetail(){
		return " Order by t.PB_ID ";
	}
	
	@Override
	protected Div getRootDiv() {
		return detail1;
	}
	
	@Override
	public Class getDetailClass() {
//		return DSPB01_GRP.class;
		return DSPB01_GRP.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}

	@Override
	protected Listbox getDetailListbox() {
		return detailListbox;
	}

	@Override
	protected ArrayList<String> getDetailKeyName() {
		ArrayList<String> detailKey = new ArrayList<String>();
		detailKey.add("PB_GROUPID");
		detailKey.add("PB_ID");
		return detailKey;
	}

	@Override
	protected ArrayList<String> getDetailKeyValue(Object entityDetail) {
		DSPB01_GRP entity = (DSPB01_GRP) entityDetail;
		ArrayList<String> detailValue = new ArrayList<String>();
		detailValue.add(entity.getPB_GROUPID());
		detailValue.add(entity.getPB_ID());
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
		if (getData_Mode().equals(DataMode.CREATE_MODE)){
			Query qry = CRUDService.getBetweenByLimit2("Select t From DSPB01_GRP t Where PB_GROUPID = '" + strPB_GROUPID +
					"' and PB_ID = '" + strPB_ID + "'");
			if (qry.getResultList().size() > 0){
				Messagebox.show(Labels.getLabel("PUBLIC.MSG02"), "Fail", Messagebox.OK, Messagebox.ERROR);				
				return false;
			}
		}
		return true;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		DSPB01_GRP entity = (DSPB01_GRP) entityDetail;
		strPB_ID = (entity == null ? "" : entity.getPB_ID());
		strPB_GROUPID = (entity == null ? "" : entity.getPB_GROUPID());		
	}

	@Override
	public void detailReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail);		
		super.detailReadMode(mapButton);		
	}
	
	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail);
		super.detailCreateMode(mapButton);	
	}
	
	@Override
	protected void doCreateDefault() {
		DSPB03 master = (DSPB03) getMasterProgram().getMasterSel();
		strPB_GROUPID = master.getPB_GROUPID();		
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		switch(columnName){
			case "PB_GROUPID":
				return strPB_GROUPID;
			case "PB_ID":
				return strPB_ID;
			case "UP_USER":
				return _userInfo.getAccount();
			case "UP_DATE":
				return new Date();
		}
		return null;
	}

	@Listen("onPBID=#detailListbox")
	public void onClickbtnPB_ID (ForwardEvent evt){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", getParentWindow());
		map.put("pbgroupid", strPB_GROUPID);
		Executions.createComponents("/ds/dspb/DSPB002M03.zul", null, map);
	}
	
	@SuppressWarnings("unchecked")
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event){
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSPB00_NEW e = (DSPB00_NEW) map.get("selectedRecord");
		strPB_ID = e.getPB_ID();
		txtPB_ID.setValue(Labels.getLabel(e.getPB_LANGTAG()));
	}
	
	public String getLabel(String sTag){
		return Labels.getLabel(sTag);
	}
	@Override
	protected String getWhereConditionals() {
//		return " and DSPB00_NEW.PB_SYSID='" + strPB_SYSID + "' ";
		return " and t.PB_ID in (select PB_ID from DSPB00_NEW n where n.PB_SYSID = '"+strPB_SYSID+"')";
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
//		List<DSPB01_GRP> lst = new ArrayList<DSPB01_GRP>();
//		Query qry = super.getQueryPagingbySize();
//		if (qry != null){
//			for(Iterator i = qry.getResultList().iterator(); i.hasNext();){
//				Object[] obj = (Object[])i.next();
//				DSPB01_GRP e = new DSPB01_GRP();
//				e.setPB_GROUPID((String)obj[0]);
//				e.setPB_ID((String)obj[1]);
//				e.setPB_LANGTAG((String)obj[2]);
//				e.setPB_RH01((String)obj[3]);
//				e.setPB_RH02((String)obj[4]);
//				e.setPB_RH03((String)obj[5]);
//				e.setPB_RH04((String)obj[6]);
//				e.setPB_RH08((String)obj[7]);
//				e.setPB_RH09((String)obj[8]);
//				e.setPB_RH10((String)obj[9]);
//				e.setUP_USER((String)obj[10]);
//				e.setUP_DATE((Date)obj[11]);
//				e.setISEDIT(false);
//				e.setISADD(false);
//				lst.add(e);
//			}
//		}
//		return lst;	
		return null;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
