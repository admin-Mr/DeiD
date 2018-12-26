package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID10;
import ds.dsid.domain.DSID10_1;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.OperationMode;

public class DSID10MDetail1 extends Detail{

	@Wire
	protected Div detail1;
	@Wire
	private Textbox txtnike_sh_aritcle1,txtseq,txtori_info,txtrep_info,txtspl_info1,txtspl_info2,query_group,query_info;
	@Wire
	Checkbox rep_check,spl_check;
	@Wire
	Combobox Com_pid1,Com_Spid;
	@Wire
	private Button  btnCreateDetail, btnEditDetail,  btnSaveDetail, btnCancelDetail, btnDeleteDetail;
	@Wire
	protected Listbox detailListbox;
	private String strNIKE_SH_ARITCLE;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>(txtnike_sh_aritcle1, "NIKE_SH_ARITCLE", strNIKE_SH_ARITCLE, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtseq, "SEQ", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(Com_pid1, "GROUP_NO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtori_info, "ORI_INFO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(rep_check, "IS_REP", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtrep_info, "REP_INFO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(spl_check, "IS_SPL", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtspl_info1, "SPL_INFO1", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(Com_Spid, "SPL_GROUP", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtspl_info2, "SPL_INFO2", null, null, null));		
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	
	}
	
	private void getWhereConditionals(String whereConditionals) {
		// TODO Auto-generated method stub
		
	}

	@Listen("onAfterRender = #detailListbox")
	public void onAfterRenderDetailListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			txtnike_sh_aritcle1 = (Textbox) getParentWindow().getFellow("txtnike_sh_aritcle1");
		}
	}

	@Override
	protected Div getRootDiv() {
		return detail1;
	}


	@Override
	public Class getDetailClass() {
		return DSID10_1.class;
	}



	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}


	@Override
	protected Listbox getDetailListbox() {
		// TODO Auto-generated method stub
		return detailListbox;
	}


	@Override
	protected ArrayList<String> getDetailKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> detailKey = new ArrayList<String>();
		detailKey.add("SEQ");
		detailKey.add("NIKE_SH_ARITCLE");
		return detailKey;
	}


	@Override
	protected ArrayList<String> getDetailKeyValue(Object entityDetail) {
		// TODO Auto-generated method stub
		DSID10_1 detail = (DSID10_1) entityDetail;
		ArrayList<String> detailValue = new ArrayList<String>();
		detailValue.add(detail.getSEQ());
		detailValue.add(detail.getNIKE_SH_ARITCLE());
		return detailValue;
	}


	@Override
	protected String getPagingIdDetail() {
		// TODO Auto-generated method stub
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
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String sql="";
		if(!query_group.getValue().isEmpty()){
			sql+=" AND GROUP_NO='"+query_group.getValue()+"'";
		}
		if(!query_info.getValue().isEmpty()){
			sql+=" AND ORI_INFO='"+query_info.getValue()+"'";
		}
		System.err.println(">>>"+sql);
		return sql;
	}


	@Override
	protected boolean beforeDetailSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	protected boolean beforeDetailDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		// TODO Auto-generated method stub
		
		DSID10_1 entity = (DSID10_1) entityDetail;
		txtnike_sh_aritcle1.setValue(entity == null ? "" : entity.getNIKE_SH_ARITCLE());
		txtseq.setValue(entity == null ? "" : entity.getSEQ());
		Com_pid1.setValue(entity == null ? "" : entity.getGROUP_NO());
		txtori_info.setValue(entity == null ? "" : entity.getORI_INFO());
		
		if (entity == null || "N".equals(entity.getIS_REP())){
			rep_check.setChecked(false);
		}else{
			rep_check.setChecked(true);
		}
		txtrep_info.setValue(entity == null ? "" : entity.getREP_INFO());
		
		if (entity == null || "N".equals( entity.getIS_SPL())){
			spl_check.setChecked(false);
		}else{
			spl_check.setChecked(true);
		}
		
		txtspl_info1.setValue(entity == null ? "" : entity.getSPL_INFO1());
		Com_Spid.setValue(entity == null ? "" : entity.getSPL_GROUP());
		txtspl_info2.setValue(entity == null ? "" : entity.getSPL_INFO2());
		
		
	}


	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		DSID10 master = (DSID10) getMasterProgram().getMasterSel();
		strNIKE_SH_ARITCLE = master.getNIKE_SH_ARITCLE();
		txtnike_sh_aritcle1.setText(strNIKE_SH_ARITCLE);
		//流水號
		txtseq.setText(AutoSeq(strNIKE_SH_ARITCLE));
	}


	private String AutoSeq(String strNIKE_SH_ARITCLE) {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String seq = "";
		
		String sql = "SELECT LPAD(NVL(MAX(SEQ),0)+1,4,'0')SEQ  FROM  DSID10_1 WHERE NIKE_SH_ARITCLE='"+strNIKE_SH_ARITCLE+"'";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				seq = rs.getString("SEQ");
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Common.closeConnection(conn);	
		}
		
		return seq;
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void detailReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail);
//		mapButton.put("btnquery", btnQuery);
		super.detailReadMode(mapButton);
		btnSaveDetail.setDisabled(true);
		btnCancelDetail.setDisabled(true);
		btnEditDetail.setDisabled(false);
		btnDeleteDetail.setDisabled(false);
		
		txtnike_sh_aritcle1.setReadonly(true);
		txtseq.setReadonly(true);
		Com_pid1.setReadonly(true);
		txtori_info.setReadonly(true);
		rep_check.setDisabled(true);
		txtrep_info.setReadonly(true);
		spl_check.setDisabled(true);
		txtspl_info1.setReadonly(true);
		Com_Spid.setReadonly(true);
		txtspl_info2.setReadonly(true);
		
				
	}

	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail);
//		mapButton.put("btnquery", btnQuery);
		super.detailCreateMode(mapButton);
		btnSaveDetail.setDisabled(false);
		btnCancelDetail.setDisabled(false);
		btnEditDetail.setDisabled(true);
		btnDeleteDetail.setDisabled(true);
		
		
		txtnike_sh_aritcle1.setReadonly(false);
		txtseq.setReadonly(false);
		Com_pid1.setReadonly(false);
		txtori_info.setReadonly(false);
		rep_check.setDisabled(false);
		spl_check.setDisabled(false);
		txtrep_info.setReadonly(false);	
		txtspl_info1.setReadonly(false);	
		Com_Spid.setReadonly(false);	
		txtspl_info2.setReadonly(false);	
		
	}
	
	@Listen("onClick=#rep_check")
	public void ononclickrep_check(Event event) {
		spl_check.setChecked(false);
		txtrep_info.setReadonly(false);	
		txtspl_info1.setReadonly(true);
		Com_pid1.setReadonly(true);	
		txtspl_info2.setReadonly(true);
		txtspl_info1.setText("");
		Com_Spid.setText("");
		txtspl_info2.setText("");
	}
	
	@Listen("onClick=#spl_check")
	public void ononclickspl_check(Event event) {
		rep_check.setChecked(false);
		txtrep_info.setReadonly(true);			
		txtspl_info1.setReadonly(false);
		Com_Spid.setReadonly(false);	
		txtspl_info2.setReadonly(false);
		txtrep_info.setText("");
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
