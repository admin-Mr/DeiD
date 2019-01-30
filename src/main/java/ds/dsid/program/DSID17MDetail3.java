package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
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
import ds.dsid.domain.DSID17_OUT;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;

public class DSID17MDetail3 extends Detail{

	@Wire
	protected Div detail3;
	@Wire
	private Textbox TOUT_NO,TADH_ELNO3,TOUT_QTY,query_OUT_NO;
	@Wire
	private Button  btnCreateDetail3, btnEditDetail3,  btnSaveDetail3, btnCancelDetail3, btnDeleteDetail3;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>(TOUT_NO, "OUT_NO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TADH_ELNO3, "ADH_ELNO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TOUT_QTY, "OUT_QTY", null, null, null));
		
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	}
	
	@Listen("onAfterRender = #detailListbox")
	public void onAfterRenderDetailListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TADH_ELNO3 = (Textbox) getParentWindow().getFellow("TADH_ELNO3");
		}
	}
	
	@Override
	protected Div getRootDiv() {
		return detail3;
	}

	@Override
	public Class getDetailClass() {
		// TODO Auto-generated method stub
		return DSID17_OUT.class;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		// TODO Auto-generated method stub	
		DSID17_OUT entity = (DSID17_OUT) entityDetail;
		TOUT_NO.setValue(entity == null ? "" : entity.getOUT_NO());
		TADH_ELNO3.setValue(entity == null ? "" : entity.getADH_ELNO());
		TOUT_QTY.setValue(entity == null ? "" : entity.getOUT_QTY());
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		DSID17 master = (DSID17) getMasterProgram().getMasterSel();
		TADH_ELNO3.setText(master.getADH_ELNO());
		//流水號
		try {
			TOUT_NO.setText(AutoSeq());
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(!"".equals(query_OUT_NO.getValue())){
			Sql+=" AND OT_NO LIKE '%"+query_OUT_NO.getValue()+"%'";
		}	
		return Sql;
	}
	
	@Override
	public void detailReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail3);
//		mapButton.put("btnquery", btnQuery);
		super.detailReadMode(mapButton);
		btnSaveDetail3.setDisabled(true);
		btnCancelDetail3.setDisabled(true);
		btnEditDetail3.setDisabled(false);
		btnDeleteDetail3.setDisabled(false);
		
		TOUT_NO.setReadonly(true);
		TADH_ELNO3.setReadonly(true);
		TOUT_QTY.setReadonly(true);			
	}

	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail3);
//		mapButton.put("btnquery", btnQuery);
		super.detailCreateMode(mapButton);
		btnSaveDetail3.setDisabled(false);
		btnCancelDetail3.setDisabled(false);
		btnEditDetail3.setDisabled(true);
		btnDeleteDetail3.setDisabled(true);
				
		TOUT_NO.setReadonly(true);
		TADH_ELNO3.setReadonly(true);
		TOUT_QTY.setReadonly(false);	
	}

	private String AutoSeq() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SEC = "";
		
		String sql = "SELECT TO_CHAR(SYSDATE,'YYMMDD')||LPAD(NVL(MAX(SUBSTR(OUT_NO,7,8)),0)+1,2,'0') SEC FROM DSID17_OUT WHERE TO_CHAR(UP_DATE,'YYYY/MM/DD')=TO_CHAR(SYSDATE,'YYYY/MM/DD')";
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
	
	@Override
	public String getOrderByDetail(){
		String orderSql="";
		orderSql=" ORDER BY OUT_NO DESC ";
		return orderSql;
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
			
			OutRaw_elno();
		}
		return Enough;
	}
	
	private void OutRaw_elno() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		Connection Conn=Common.getOraDbConnection("10.8.1.32", "FTLDB1", "DSOD", "ORA@IT2013");
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM DSID07 WHERE ADH_ELNO='"+TADH_ELNO3.getValue()+"'";
		System.err.println("--sql--："+sql);
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				for(int i=1;i<4;i++){
					if(!"".equals(rs.getString("RAW_ELNO"+i))&&rs.getString("RAW_ELNO"+i)!=null){
						
						
						
					}	
				}
			}
			rs.close();
			ps.close();		
		} catch (Exception e) {
			e.printStackTrace();
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
			Common.closeConnection(Conn);	
		}
	}

	private boolean ChkEnough() {
		// TODO Auto-generated method stub

		boolean Enough=true;
		String OUT_QTY=TOUT_QTY.getValue();
		String mess="";
		
		if(!"".equals(OUT_QTY)&&OUT_QTY!=null){			
			
			Connection conn=Common.getDbConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			String sql = "SELECT * FROM DSID17 WHERE ADH_ELNO='"+TADH_ELNO3.getValue()+"'";
//			System.err.println("--SEC--："+sql);
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if(rs.next()){
					if(Double.valueOf(OUT_QTY)>Double.valueOf(rs.getString("ADH_QTY"))){
						mess="出庫數量大於庫存數量，不可出庫！！！";
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
			mess="出庫數量不能為空！！";
		}
		
		if(mess.length()>0){
			Messagebox.show(mess);
			Enough= false;
		}
		return Enough;
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
						"SELECT ADH_QTY FROM ( SELECT NVL(SUM(A.AC_QTY),0)-(SELECT SUM(OUT_QTY) OUT_QTY FROM DSID17_OUT B WHERE B.ADH_ELNO=A.ADH_ELNO ) ADH_QTY FROM DSID17_ACP A WHERE A.ADH_ELNO='"+TADH_ELNO3.getValue()+"' GROUP BY ADH_ELNO)\n" + 
							") WHERE ADH_ELNO='"+TADH_ELNO3.getValue()+"'";

//		System.err.println("--UpSql--："+UpSql);
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
