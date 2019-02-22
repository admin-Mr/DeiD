package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID17;
import ds.dsid.domain.DSID17_ACP;
import ds.dsid.domain.DSID17_OT;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;

public class DSID17MDetail2 extends Detail{

	@Wire
	protected Div detail2;
	@Wire
	private Textbox TAC_NO,TOT_NO2,TADH_ELNO2,TAC_QTY,query_AC_NO;
	@Wire
	private Button  btnCreateDetail2, btnEditDetail2,  btnSaveDetail2, btnCancelDetail2, btnDeleteDetail2,btnQuery_Otno;
	private Double OT_QTY=0.0;
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>(TAC_NO, "AC_NO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TOT_NO2, "OT_NO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TADH_ELNO2, "ADH_ELNO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TAC_QTY, "AC_QTY", null, null, null));
		
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	}
	
	@Listen("onAfterRender = #detail2Listbox")
	public void onAfterRenderDetailListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TADH_ELNO2 = (Textbox) getParentWindow().getFellow("TADH_ELNO2");
		}
	}
	
	@Override
	protected Div getRootDiv() {
		return detail2;
	}

	@Override
	public Class getDetailClass() {
		// TODO Auto-generated method stub
		return DSID17_ACP.class;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		// TODO Auto-generated method stub	
		DSID17_ACP entity = (DSID17_ACP) entityDetail;
		TAC_NO.setValue(entity == null ? "" : entity.getAC_NO());
		TOT_NO2.setValue(entity == null ? "" : entity.getOT_NO());
		TADH_ELNO2.setValue(entity == null ? "" : entity.getADH_ELNO());
		TAC_QTY.setValue(entity == null ? "" : entity.getAC_QTY());
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		DSID17 master = (DSID17) getMasterProgram().getMasterSel();
		TADH_ELNO2.setText(master.getADH_ELNO());
		//流水號
		try {
			TAC_NO.setText(AutoSeq());
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub

		boolean Enough=ChkEnough();
		
		if(Enough==true){
			switch (columnName) {
			case "UP_USER":
				return _userInfo.getAccount();
			case "UP_DATE":
				return new Date();
			}
		}	
		
		return Enough;
	}
	
	private boolean ChkEnough() {
		// TODO Auto-generated method stub

		boolean Enough=true;
		String OT_NO=TOT_NO2.getValue();
		String mess="";
		
		if(!"".equals(OT_NO)&&OT_NO!=null){			
			String AC_QTY=TAC_QTY.getValue();
			if(!"".equals(AC_QTY)&&AC_QTY!=null){
				
				Connection conn=Common.getDbConnection();
				PreparedStatement ps = null;
				ResultSet rs = null;
				
				String sql = "SELECT SUM(OT_QTY)-NVL(SUM(B.AC_QTY),0) SOT_QTY FROM DSID17_OT A,DSID17_ACP B WHERE A.OT_NO=B.OT_NO(+) AND  A.OT_NO='"+OT_NO+"'";
//				System.err.println("--SEC--："+sql);
				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					if(rs.next()){
						if(Double.valueOf(AC_QTY)>Double.valueOf(rs.getString("SOT_QTY"))){
							mess=Labels.getLabel("DSID.MSG0062");
						}
						
					}
					rs.close();
					ps.close();		
				} catch (Exception e) {
					e.printStackTrace();
					Enough= false;
				}finally{
					if(rs!=null){
						try {
							rs.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ps!=null){
						try {
							ps.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Common.closeConnection(conn);	
				}
			}else{
				mess=Labels.getLabel("DSID.MSG0063");
			}
			
		}else{
			mess=Labels.getLabel("DSID.MSG0064");
		}
		
		if(mess.length()>0){
			Messagebox.show(mess);
			Enough= false;
		}
		return Enough;
	}
	
	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getWhereConditionals() {
		String Sql="";
		if(!"".equals(query_AC_NO.getValue())){
			Sql+=" AND AC_NO LIKE '%"+query_AC_NO.getValue()+"%'";
		}	
		return Sql;
	}
	
	@Override
	public void detailReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail2);
//		mapButton.put("btnquery", btnQuery);
		super.detailReadMode(mapButton);
		btnSaveDetail2.setDisabled(true);
		btnCancelDetail2.setDisabled(true);
		btnEditDetail2.setDisabled(false);
		btnDeleteDetail2.setDisabled(false);
		btnQuery_Otno.setDisabled(true);
		
		TAC_NO.setReadonly(true);
		TOT_NO2.setReadonly(true);
		TADH_ELNO2.setReadonly(true);
		TAC_QTY.setReadonly(true);			
	}

	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail2);
//		mapButton.put("btnquery", btnQuery);
		super.detailCreateMode(mapButton);
		btnSaveDetail2.setDisabled(false);
		btnCancelDetail2.setDisabled(false);
		btnEditDetail2.setDisabled(true);
		btnDeleteDetail2.setDisabled(true);
		btnQuery_Otno.setDisabled(false);
		
		TAC_NO.setReadonly(true);		
		TOT_NO2.setReadonly(true);
		TADH_ELNO2.setReadonly(true);
		TAC_QTY.setReadonly(false);	
	}

	private String AutoSeq() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SEC = "";
		
		String sql = "SELECT TO_CHAR(SYSDATE,'YYMMDD')||LPAD(NVL(MAX(SUBSTR(AC_NO,7,8)),0)+1,2,'0') SEC FROM DSID17_ACP WHERE TO_CHAR(UP_DATE,'YYYY/MM/DD')=TO_CHAR(SYSDATE,'YYYY/MM/DD')";
//		System.err.println("--SEC--："+sql);
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				SEC = rs.getString("SEC");
			}
			rs.close();
			ps.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			Common.closeConnection(conn);	
		}
		
		return SEC;
	}
	
	@Listen("onClick = #btnQuery_Otno")
	public void onClickbtnQuery_Otno(Event event){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		System.err.println("getParentWindow()"+getParentWindow());
		map.put("parentWindow", getParentWindow());
		map.put("detailindex", "1");
		map.put("returnMethod", "onQryOt_no");
		map.put("ADH_ELNO", TADH_ELNO2.getValue());
		Executions.createComponents("/ds/dsid/DSID17MQueryOT_NO.zul", null, map);
	}
	
	@SuppressWarnings("unchecked")
	@Listen("onQryOt_no = #windowMaster")
	public void onQryOt_no(Event event){
		Map<String, Object> map = (Map<String, Object>) event.getData();
		DSID17_OT e = (DSID17_OT) map.get("selectedRecord");
		TOT_NO2.setValue(e.getOT_NO());
		OT_QTY=Double.valueOf(e.getOT_QTY());
	}
	
	@Override
	public String getOrderByDetail(){
		String orderSql="";
		orderSql=" ORDER BY AC_NO DESC ";
		return orderSql;
	}
	

	@Override
	protected boolean doSaveAfter(Connection conn) {
		try {
			UpAdh_elno(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected boolean doDeleteAfter(Connection conn) {
		try {
			UpAdh_elno(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void UpAdh_elno(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstm=null;
		 
		String UpSql="UPDATE DSID17 SET ADH_QTY=(\n" +
						"SELECT ADH_QTY FROM ( SELECT NVL(SUM(A.AC_QTY),0)-(SELECT NVL(SUM(OUT_QTY),0) OUT_QTY FROM DSID17_OUT B WHERE B.ADH_ELNO=A.ADH_ELNO ) ADH_QTY FROM DSID17_ACP A WHERE A.ADH_ELNO='"+TADH_ELNO2.getValue()+"' GROUP BY ADH_ELNO)\n" + 
							") WHERE ADH_ELNO='"+TADH_ELNO2.getValue()+"'";

		System.err.println("--UpSql--："+UpSql);
		try {	
			pstm = conn.prepareStatement(UpSql);	
			pstm.executeUpdate();
			pstm.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();						
		}finally{
			if(pstm!=null){
				pstm.close();
			}
			
		}
	}
}
