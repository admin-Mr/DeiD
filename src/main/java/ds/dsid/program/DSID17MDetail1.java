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
import ds.dsid.domain.DSID17_OT;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;

public class DSID17MDetail1 extends Detail{

	@Wire
	protected Div detail1;
	@Wire
	private Textbox TOT_NO,TADH_ELNO1,TOT_QTY,query_OT_NO;
	@Wire
	private Button  btnCreateDetail, btnEditDetail,  btnSaveDetail, btnCancelDetail, btnDeleteDetail;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>(TOT_NO, "OT_NO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TADH_ELNO1, "ADH_ELNO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TOT_QTY, "OT_QTY", null, null, null));
		
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	}
	
	@Listen("onAfterRender = #detailListbox")
	public void onAfterRenderDetailListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TADH_ELNO1 = (Textbox) getParentWindow().getFellow("TADH_ELNO1");
		}
	}
	
	@Override
	protected Div getRootDiv() {
		return detail1;
	}

	@Override
	public Class getDetailClass() {
		// TODO Auto-generated method stub
		return DSID17_OT.class;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		// TODO Auto-generated method stub	
		DSID17_OT entity = (DSID17_OT) entityDetail;
		TOT_NO.setValue(entity == null ? "" : entity.getOT_NO());
		TADH_ELNO1.setValue(entity == null ? "" : entity.getADH_ELNO());
		TOT_QTY.setValue(entity == null ? "" : entity.getOT_QTY());
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		DSID17 master = (DSID17) getMasterProgram().getMasterSel();
		TADH_ELNO1.setText(master.getADH_ELNO());
		//流水號
		try {
			TOT_NO.setText(AutoSeq());
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
	
		Connection conn=Common.getDbConnection();
		Connection Conn=Common.getOraDbConnection("10.8.1.32", "FTLDB1", "DSOD", "ORA@IT2013");
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String mess="";
		boolean ChkEnough=true;
		
		Double OT_QTY=Double.valueOf(TOT_QTY.getValue());
		String sql = "SELECT * FROM DSID07 A,DSID17 B WHERE A.ADH_ELNO=B.ADH_ELNO AND A.ADH_ELNO='"+TADH_ELNO1.getValue()+"'";
		System.err.println("--sql--："+sql);
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				for(int i=1;i<4;i++){
					if(!"".equals(rs.getString("RAW_ELNO"+i))&&rs.getString("RAW_ELNO"+i)!=null){
						
						Double MT_QTY=GetMt_qty(rs.getString("MODEL_NA"),rs.getString("RAW_ELNO"+i),Conn);
						Double USE_QTY=GetUse_qty(rs.getString("RAW_ELNO"+i),conn);
						if((OT_QTY+USE_QTY)>MT_QTY){
							mess+="原材料："+rs.getString("RAW_ELNO"+i)+"數量不足,不能進行貼合發料！！\n";
						}
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
		if(mess.length()>0){
			Messagebox.show(mess);
			ChkEnough= false;
		}
		return ChkEnough;
	}

	private Double GetUse_qty(String RAW_ELNO, Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		Double USE_QTY=0.0;
		
		String sql = "SELECT * FROM DSID07 A,DSID17 B WHERE A.ADH_ELNO=B.ADH_ELNO AND (RAW_ELNO1='"+RAW_ELNO+"' OR RAW_ELNO2='"+RAW_ELNO+"' OR RAW_ELNO3='"+RAW_ELNO+"')";
		System.err.println("--------\n："+sql);
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				for(int i=1;i<4;i++){
					if(RAW_ELNO.equals(rs.getString("RAW_ELNO"+i))){
						USE_QTY+=Double.valueOf(rs.getString("ADH_QTY"))*Double.valueOf(rs.getString("RAW+PRO"+i));
						System.err.println("--USE_QTY--\n："+USE_QTY);
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

		}
		
		return USE_QTY;
	}

	private double GetMt_qty(String MODEL_NA,String EL_NO, Connection Conn) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		Double MT_QTY=0.0;
		String sql = "SELECT * FROM DSID77 WHERE EL_NO='"+EL_NO+"' AND ( MODEL_NA='"+MODEL_NA+"' OR MODEL_NA='0') ORDER BY MODEL_NA DESC";
		System.err.println("--Mt_qty--\n："+sql);
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				MT_QTY=Double.valueOf(rs.getString("MT_QTY"));
			}else{
				MT_QTY=0.0;
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

		}
		
		return MT_QTY;
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
		if(!"".equals(query_OT_NO.getValue())){
			Sql+=" AND OT_NO LIKE '%"+query_OT_NO.getValue()+"%'";
		}
		return Sql;
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
		
		TOT_NO.setReadonly(true);
		TADH_ELNO1.setReadonly(true);
		TOT_QTY.setReadonly(true);			
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
				
		TOT_NO.setReadonly(true);
		TADH_ELNO1.setReadonly(true);
		TOT_QTY.setReadonly(false);		
	}

	private String AutoSeq() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String SEC = "";
		
		String sql = "SELECT TO_CHAR(SYSDATE,'YYMMDD')||LPAD(NVL(MAX(SUBSTR(OT_NO,7,8)),0)+1,2,'0') SEC FROM DSID17_OT WHERE TO_CHAR(UP_DATE,'YYYY/MM/DD')=TO_CHAR(SYSDATE,'YYYY/MM/DD')";
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
		orderSql=" ORDER BY OT_NO DESC ";
		return orderSql;
	}

}
