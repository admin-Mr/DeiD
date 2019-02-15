package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

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
	private Textbox TOUT_NO,TADH_ELNO3,TOUT_QTY,query_OUT_NO,TMT_PONO,TMT_SEQ;
	@Wire
	private Button  btnCreateDetail3, btnEditDetail3,  btnSaveDetail3, btnCancelDetail3, btnDeleteDetail3;
	private String MT_PONO="";
	private int Mseq=0;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>(TOUT_NO, "OUT_NO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TADH_ELNO3, "ADH_ELNO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TOUT_QTY, "OUT_QTY", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TMT_PONO, "MT_PONO", null, null, null));
		detailComponentColumns.add(new ComponentColumn<String>(TMT_SEQ, "MT_SEQ", null, null, null));

		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	}
	
	@Listen("onAfterRender = #detail3Listbox")
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
		TMT_PONO.setValue(entity == null ? "" : entity.getMT_PONO());
		TMT_SEQ.setValue(entity == null ? "" : entity.getMT_SEQ());
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		DSID17 master = (DSID17) getMasterProgram().getMasterSel();
		TADH_ELNO3.setText(master.getADH_ELNO());
		//流水號
		try {
			TOUT_NO.setText(AutoSeq());
			ArrayList<String> Mt_list=GetMt_pono();
			TMT_PONO.setText(Mt_list.get(0));
			TMT_SEQ.setText(Mt_list.get(1));
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
//		btnEditDetail3.setDisabled(true);
		btnDeleteDetail3.setDisabled(false);
		
		TOUT_NO.setReadonly(true);
		TADH_ELNO3.setReadonly(true);
		TOUT_QTY.setReadonly(true);	
		TMT_PONO.setReadonly(true);
		TMT_SEQ.setReadonly(true);
	}

	@Override
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatedetail", btnCreateDetail3);
//		mapButton.put("btnquery", btnQuery);
		super.detailCreateMode(mapButton);
		btnSaveDetail3.setDisabled(false);
		btnCancelDetail3.setDisabled(false);
//		btnEditDetail3.setDisabled(true);
		btnDeleteDetail3.setDisabled(true);
				
		TOUT_NO.setReadonly(true);
		TADH_ELNO3.setReadonly(true);
		TOUT_QTY.setReadonly(false);
		TMT_PONO.setReadonly(true);
		TMT_SEQ.setReadonly(true);
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
		}	
		
		return Enough;
	}
	
	
	private String GetModel_na(String EL_NO,String MODEL_NA, Connection Conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		String model_na="";
		
		String sql = "SELECT * FROM DSID77 WHERE EL_NO='"+EL_NO+"' AND ( MODEL_NA='"+MODEL_NA+"' OR MODEL_NA='0') ORDER BY MODEL_NA DESC";
		System.err.println("--------\n："+sql);
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){		
				model_na=rs.getString("MODEL_NA");
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
		
		return model_na;
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
			OutRaw_elno();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	private ArrayList<String> GetMt_pono() {
		Connection Conn=Common.getOraDbConnection("10.8.1.32", "FTLDB1", "DSOD", "ORA@IT2013");
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> Mt_list=new ArrayList<>();
		
		String seq="",MT_PONO="";
		SimpleDateFormat format2 = new SimpleDateFormat("yyMMdd"); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 
		
		String sql = "SELECT A.MT_PONO,MAX(TO_NUMBER(B.MT_SEQ)) MT_SEQ FROM DSID75 A,DSID76 B WHERE A.MT_CODE=B.MT_CODE AND A.MT_PONO=B.MT_PONO AND  A.MT_CODE='1D' AND TO_CHAR(A.UP_DATE,'YYYY/MM/DD')='"+format.format(new Date())+"' AND  A.MT_PONO LIKE '%120000' GROUP BY A.MT_PONO";
		System.err.println("--sql--："+sql);
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				MT_PONO=rs.getString("MT_PONO");
				Mseq=Integer.valueOf(rs.getString("MT_SEQ"));
			}else{
				MT_PONO="1D"+format2.format(new Date())+"120000";
				Mseq=0;
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

			Common.closeConnection(Conn);	
		}	
		for(int i=1;i<4;i++){
				seq+=(Mseq+i)+",";
		}
		if(seq.length()>0){
			seq=seq.substring(0, seq.length()-1);
		}
		
		System.err.println("MT_PONO>>>"+MT_PONO+"seq>>>"+seq);
		Mt_list.add(MT_PONO);
		Mt_list.add(seq);
		
		return Mt_list;
	}

	private void OutRaw_elno() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		Connection Conn=Common.getOraDbConnection("10.8.1.32", "FTLDB1", "DSOD", "ORA@IT2013");
		PreparedStatement ps = null,pstm=null;
		ResultSet rs = null;
		String seq="";
		
		try {
			String InsertSql="",sql="";

			HashMap<String, Object> Model_namap = new HashMap<String, Object>();
			HashMap<String, Object> Qtymap = new HashMap<String, Object>();
			
			sql = "SELECT * FROM DSID07 WHERE ADH_ELNO='"+TADH_ELNO3.getValue()+"'";
			System.err.println("--sql--："+sql);
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if(rs.next()){
					for(int i=1;i<4;i++){
						if(!"".equals(rs.getString("RAW_ELNO"+i))&&rs.getString("RAW_ELNO"+i)!=null){
							String Model_na=GetModel_na(rs.getString("RAW_ELNO"+i),rs.getString("MODEL_NA"),Conn);
							InsertSql+=" INTO DSID76 (MT_CODE,MT_PONO,MT_SEQ,EL_NO,MT_AREANO,MT_QTY,CU_SALE,UP_USER,UP_DATE,MT_STOCKNO,MODEL_NA) VALUES ('1D','"+MT_PONO+"','"+(Mseq+i)+"','"+rs.getString("RAW_ELNO"+i)+"','A','"+(Double.valueOf(TOUT_QTY.getValue())*Double.valueOf(rs.getString("RAW_PRO"+i)))+"','"+_userInfo.getAccount()+"','"+_userInfo.getAccount()+"',TO_DATE(TO_CHAR(SYSDATE,'YYYY/MM/DD'),'FTL-0002','"+Model_na+"')";
							seq+=(Mseq+i)+",";
							Model_namap.put(rs.getString("RAW_ELNO"+i),Model_na);
							Qtymap.put(rs.getString("RAW_ELNO"+i),(Double.valueOf(TOUT_QTY.getValue())*Double.valueOf(rs.getString("RAW_PRO"+i))));
						}	
					}
				}
				
				rs.close();
				ps.close();		
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(seq.length()>0){
				seq=seq.substring(0, seq.length()-1);
			}

			System.err.println("MT_PONO>>>"+MT_PONO+"seq>>>"+seq);

			InsertSql="INSERT ALL "+InsertSql+" SELECT * FROM DUAL";
			System.err.println(">>>>>>"+InsertSql);
//			
//			try {
//				pstm = conn.prepareStatement(InsertSql);	
//				pstm.executeUpdate();
//				pstm.close();
//			} catch (Exception e) {
//				try {
//					conn.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				e.printStackTrace();						
//			}
			
			Set<Entry<String, Object>> Model_naentries = Model_namap.entrySet();
			Set<Entry<String, Object>> Qtyentries = Qtymap.entrySet();

			for (Entry<String, Object> Mentry : Model_naentries) { 
				for (Entry<String, Object> Qentry : Qtyentries) {
					if(Qentry.getKey().equals(Mentry.getKey())){
						String UpSql="UPDATE DSID77 SET MT_QTY=MT_QTY-"+Qentry.getValue()+" WHERE MODEL_NA='"+Mentry.getValue()+"' AND EL_NO='"+Mentry.getKey()+"'";
						System.err.println(">>>>>>"+UpSql);
//					try {
//						pstm = conn.prepareStatement(UpSql);	
//						pstm.executeUpdate();
//						pstm.close();
//					} catch (Exception e) {
//						try {
//							conn.rollback();
//						} catch (SQLException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						e.printStackTrace();						
//					}
					}
				}
			}
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

	@Override
	protected boolean doDeleteAfter(Connection conn) {
		try {
			UpAdh_elno(conn);
			Anti_Raw_elno();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	private void Anti_Raw_elno() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		Connection Conn=Common.getOraDbConnection("10.8.1.32", "FTLDB1", "DSOD", "ORA@IT2013");
		PreparedStatement ps = null,pstm=null;
		ResultSet rs = null;
		try {

			HashMap<String, Object> Model_namap = new HashMap<String, Object>();
			HashMap<String, Object> Qtymap = new HashMap<String, Object>();
			
			String sql = "SELECT * FROM DSID07 WHERE ADH_ELNO='"+TADH_ELNO3.getValue()+"'";
			System.err.println("--sql--："+sql);
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if(rs.next()){
					for(int i=1;i<4;i++){
						if(!"".equals(rs.getString("RAW_ELNO"+i))&&rs.getString("RAW_ELNO"+i)!=null){
							String Model_na=GetModel_na(rs.getString("RAW_ELNO"+i),rs.getString("MODEL_NA"),Conn);
							
							Model_namap.put(rs.getString("RAW_ELNO"+i),Model_na);
							Qtymap.put(rs.getString("RAW_ELNO"+i),(Double.valueOf(TOUT_QTY.getValue())*Double.valueOf(rs.getString("RAW_PRO"+i))));
						}	
					}
				}
				
				rs.close();
				ps.close();		
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			Set<Entry<String, Object>> Model_naentries = Model_namap.entrySet();
			Set<Entry<String, Object>> Qtyentries = Qtymap.entrySet();

			for (Entry<String, Object> Mentry : Model_naentries) { 
				for (Entry<String, Object> Qentry : Qtyentries) {
					if(Qentry.getKey().equals(Mentry.getKey())){
						String UpSql="UPDATE DSID77 SET MT_QTY=MT_QTY+"+Qentry.getValue()+" WHERE MODEL_NA='"+Mentry.getValue()+"' AND EL_NO='"+Mentry.getKey()+"'";
						System.err.println(">>>>>>"+UpSql);
//					try {
//						pstm = conn.prepareStatement(UpSql);	
//						pstm.executeUpdate();
//						pstm.close();
//					} catch (Exception e) {
//						try {
//							conn.rollback();
//						} catch (SQLException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						e.printStackTrace();						
//					}
						
					}
				}
			}
			
			sql = "SELECT * FROM DSID17_OUT WHERE OUT_NO='"+TOUT_NO.getValue()+"'";
			System.err.println("--sql--："+sql);
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if(rs.next()){

					//刪除其他出庫中的資料
					String DeSql="DELETE DSID76 WHERE MT_PONO='"+rs.getString("MT_PONO")+"' AND MT_SEQ IN ('"+rs.getString("MT_SEQ").replace(",", "','")+"') ";
					System.err.println(">>>>>>"+DeSql);
//					
//					try {
//						pstm = Conn.prepareStatement(InsertSql);	
//						pstm.executeUpdate();
//						pstm.close();
//					} catch (Exception e) {
//						try {
//							conn.rollback();
//						} catch (SQLException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						e.printStackTrace();						
//					}
				}
				
				rs.close();
				ps.close();		
			} catch (Exception e) {
				e.printStackTrace();
			}
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


	private void UpAdh_elno(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstm=null;
		 
		String UpSql="UPDATE DSID17 SET ADH_QTY=(\n" +
						"SELECT ADH_QTY FROM ( SELECT NVL(SUM(A.AC_QTY),0)-(SELECT NVL(SUM(OUT_QTY),0) OUT_QTY FROM DSID17_OUT B WHERE B.ADH_ELNO=A.ADH_ELNO ) ADH_QTY FROM DSID17_ACP A WHERE A.ADH_ELNO='"+TADH_ELNO3.getValue()+"' GROUP BY ADH_ELNO)\n" + 
							") WHERE ADH_ELNO='"+TADH_ELNO3.getValue()+"'";

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
