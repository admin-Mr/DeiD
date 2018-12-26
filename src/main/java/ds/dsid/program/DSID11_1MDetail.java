package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID10;
import ds.dsid.domain.DSID10_1;
import ds.dsid.domain.DSID11_1;
import ds.dsid.domain.DSID11_7;
import util.Common;
import util.ComponentColumn;
import util.Detail;
import util.OperationMode;

public class DSID11_1MDetail extends Detail{

	
	@Wire
	protected Div detail1;
	@Wire
	private Button  btnCreateDetail1, btnEditDetail1,  btnSaveDetail1, btnCancelDetail1, btnDeleteDetail1;
	@Wire
	protected Listbox detailListbox1;
	@Wire
	private Textbox txtModel_na1, txtPoints1, txtGender1, txtSeq1;
	@Wire
	private Textbox txtPeriod1_1, txtPeriod1_2, txtPeriod1_3, txtPeriod1_4, txtPeriod1_5, txtPeriod1_6, txtPeriod1_7, txtPeriod1_8, txtPeriod1_9;

	private static final long serialVersionUID = -3935954267204155158L;
	private String StrModel_na;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>(txtModel_na1, "MODEL_NA", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtSeq1, "EL_SEQ", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPoints1, "POINTS_NB", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtGender1, "MODEL_GENDER", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_1, "PERIOD1", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_2, "PERIOD2", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_3, "PERIOD3", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_4, "PERIOD4", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_5, "PERIOD5", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_6, "PERIOD6", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_7, "PERIOD7", null, null, null));		
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_8, "PERIOD8", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(txtPeriod1_9, "PERIOD9", null, null, null));
		
		
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	
	}
	
	@Override
	protected Div getRootDiv() {
		// TODO Auto-generated method stub
		return detail1;
	}

	@Override
	public Class getDetailClass() {
		// TODO Auto-generated method stub
		return DSID11_1.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}

	@Override
	protected Listbox getDetailListbox() {
		// TODO Auto-generated method stub
		return detailListbox1;
	}

	@Override
	protected ArrayList<String> getDetailKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> detailKey = new ArrayList<String>();
		detailKey.add("MODEL_NA");
		detailKey.add("EL_SEQ");
		return detailKey;
	}

	@Override
	protected ArrayList<String> getDetailKeyValue(Object entityDetail) {
		// TODO Auto-generated method stub
		DSID11_1 detail = (DSID11_1) entityDetail;
		ArrayList<String> detailValue = new ArrayList<String>();
		detailValue.add(detail.getMODEL_NA());
		detailValue.add(detail.getEL_SEQ());
		return detailValue;
	}

	@Override
	protected String getPagingIdDetail() {
		// TODO Auto-generated method stub
		return "pagingDetailCourse1";
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
		String str = "";
		
		return str;
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
		DSID11_1 entity = (DSID11_1) entityDetail;
		txtModel_na1.setValue(entity == null ? "" : entity.getMODEL_NA());
		txtPoints1.setValue(entity == null ? "" : entity.getPOINTS_NB());
		txtSeq1.setValue(entity == null ? "" : entity.getEL_SEQ());
		txtGender1.setValue(entity == null ? "" : entity.getMODEL_GENDER());
		txtPeriod1_1.setValue(entity == null ? "" : entity.getPERIOD1());
		txtPeriod1_2.setValue(entity == null ? "" : entity.getPERIOD2());
		txtPeriod1_3.setValue(entity == null ? "" : entity.getPERIOD3());
		txtPeriod1_4.setValue(entity == null ? "" : entity.getPERIOD4());
		txtPeriod1_5.setValue(entity == null ? "" : entity.getPERIOD5());
		txtPeriod1_6.setValue(entity == null ? "" : entity.getPERIOD6());
		txtPeriod1_7.setValue(entity == null ? "" : entity.getPERIOD7());
		txtPeriod1_8.setValue(entity == null ? "" : entity.getPERIOD8());
		txtPeriod1_9.setValue(entity == null ? "" : entity.getPERIOD9());
		
		
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		DSID11_7 master = (DSID11_7) getMasterProgram().getMasterSel();
		StrModel_na = master.getMODEL_NA();
		txtModel_na1.setText(StrModel_na);
		// 序號
		txtSeq1.setText(AutoSeq().toString());
		
	}

	private Object AutoSeq() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int seq = 0;
		
		String sql = "select max(el_seq)+1 Elseq from dsid11_1";
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()){
				seq = rs.getInt("Elseq");
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		mapButton.put("btncreatedetail", btnCreateDetail1);
//		mapButton.put("btnquery", btnQuery);
		super.detailReadMode(mapButton);
		btnSaveDetail1.setDisabled(true);
		btnCancelDetail1.setDisabled(true);
		btnEditDetail1.setDisabled(false);
		btnDeleteDetail1.setDisabled(false);
		
		txtModel_na1.setReadonly(true);
		txtSeq1.setReadonly(true);
		txtPoints1.setReadonly(true);
		txtGender1.setReadonly(true);
		txtPeriod1_1.setReadonly(true);
		txtPeriod1_2.setReadonly(true);
		txtPeriod1_3.setReadonly(true);
		txtPeriod1_4.setReadonly(true);
		txtPeriod1_5.setReadonly(true);
		txtPeriod1_6.setReadonly(true);
		txtPeriod1_7.setReadonly(true);
		txtPeriod1_8.setReadonly(true);
		txtPeriod1_9.setReadonly(true);
				
	}

	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail1);
//		mapButton.put("btnquery", btnQuery);
		super.detailCreateMode(mapButton);
		btnSaveDetail1.setDisabled(false);
		btnCancelDetail1.setDisabled(false);
		btnEditDetail1.setDisabled(true);
		btnDeleteDetail1.setDisabled(true);
		
		txtModel_na1.setReadonly(false);
		txtSeq1.setReadonly(false);
		txtPoints1.setReadonly(false);
		txtGender1.setReadonly(false);
		txtPeriod1_1.setReadonly(false);
		txtPeriod1_2.setReadonly(false);
		txtPeriod1_3.setReadonly(false);
		txtPeriod1_4.setReadonly(false);
		txtPeriod1_5.setReadonly(false);
		txtPeriod1_6.setReadonly(false);
		txtPeriod1_7.setReadonly(false);
		txtPeriod1_8.setReadonly(false);
		txtPeriod1_9.setReadonly(false);
		
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return null;
	}

}
