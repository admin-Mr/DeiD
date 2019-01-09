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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.dsid.domain.GENERIC;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID01MReceive extends OpenWinCRUD{

	@WireVariable
	private CRUDService CRUDService;
	@Wire
	Window Receivewindow;
	@Wire
	Button btnSearch,btnConfirm;
	@Wire
	Listbox ListBox;
	@Wire
	Textbox txt_UpLimit,txt_rec_odno,txt_Pro;
	@Wire
	Datebox txtorder_date;
//	List<String> WOI_List = new ArrayList<String>();
	List<String> Model_naList = new ArrayList<String>();
	String WOI_List="";
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		txtorder_date.setValue(null);
//		doSearch();
	}
	
	@Listen("onClick =#btnConfirm")
	public void onClickbtnConfirm(Event event) throws SQLException{	
		

		Date ORDER_DATE=txtorder_date.getValue();
		if(!"".equals(ORDER_DATE)&&ORDER_DATE!=null){
			Connection conn =Common.getDbConnection();
			PreparedStatement pstm1=null;
			try {
			String ODNO_list=txt_rec_odno.getValue();
			if(!"".equals(ODNO_list)){
				try {
//					String InsSql="INSERT INTO DSID01 SELECT * FROM DSID01_TEMP WHERE WORK_ORDER_ID IN ('"+ODNO_list.substring(0, ODNO_list.length()-1).replace(",", "','")+"')";
//					System.err.println("--InsSql--\n"+InsSql);
//					try {
//						pstm1 = conn.prepareStatement(InsSql);
//						pstm1.executeUpdate();
//						pstm1.close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//					String Sql1="DELETE DSID01_TEMP WHERE WORK_ORDER_ID IN ('"+ODNO_list.substring(0, ODNO_list.length()-1).replace(",", "','")+"')";
//					System.err.println("--刪除DSID01_TEMP--\n"+Sql1);
//					try {
//						pstm1 = conn.prepareStatement(Sql1);
//						pstm1.executeUpdate();
//						pstm1.close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//					String Sql2="UPDATE DSID01 SET ORDER_DATE='"+format.format(ORDER_DATE);+"' WHERE  WHERE WORK_ORDER_ID IN ('"+ODNO_list.substring(0, ODNO_list.length()-1).replace(",", "','")+"')";
//					System.err.println("--刪除DSID01_TEMP--\n"+Sql2);
//					try {
//						pstm1 = conn.prepareStatement(Sql2);
//						pstm1.executeUpdate();
//						pstm1.close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
					Messagebox.show("轉單成功！！！");
				} catch (Exception e) {
					// TODO: handle exception
					conn.rollback();
					Messagebox.show("轉單失敗！！！\n"+e);
				}
			}
			} catch (SQLException e) {				
				e.printStackTrace();
			}finally{
				if(pstm1!=null){
					pstm1.close();
				}
				Common.closeConnection(conn);
			}
			
		}else{
			Messagebox.show("接單日期為空，請核查！！！");
		}
	
	}
	
	@Listen("onClick =#btnSearch")
	public void onClickbtnSearch(Event event) throws SQLException{		
		doSearch();		
	}
	
	private void doSearch() throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn =Common.getDbConnection();
		PreparedStatement  ps1 = null,ps2 = null;
		ResultSet  rs1 = null,rs2= null;
		
		try {
		String UpLimit=txt_UpLimit.getValue();
		if("".equals(UpLimit)){
			UpLimit=GetLastOrder(conn);  //如果被設定接單上限，則獲取最新日期的接單數量
		}
//		Model_naList = new ArrayList<String>();
		List<GENERIC> list = new ArrayList<GENERIC>();	

		String InsSql="";
	
		String Sql="SELECT A.MODEL_NA,GROUP_NO,COLOR,A.EL_NO,A.YIELD,B.MT_QTY,FLOOR(B.MT_QTY/A.YIELD) NUM\n" +
				"FROM DSID04_1 A,DSID77@FTLDB01.DEANSHOES.COM B WHERE A.MODEL_NA=B.MODEL_NA AND A.EL_NO=B.EL_NO\n" + 
				"AND A.MODEL_NA IN (SELECT DISTINCT CASE WHEN MODEL_NA LIKE 'W%' THEN SUBSTR(MODEL_NA,3) ELSE MODEL_NA END AS MODEL_NA FROM DSID01_TEMP) \n" + 
				" AND A.GROUP_NO LIKE 'GROUP%' AND A.EL_NO IS NOT NULL AND A.COLOR NOT LIKE '%/%' ORDER BY A.MODEL_NA,NUM ";		
		System.err.println(">>>>"+Sql);
			try {
					ps1 = conn.prepareStatement(Sql);
					rs1 = ps1.executeQuery();			
					while(rs1.next()){	
						GENERIC GENERIC=new GENERIC();
						GENERIC.setAA(rs1.getString("MODEL_NA"));
						GENERIC.setBB(rs1.getString("GROUP_NO"));
						GENERIC.setCC(rs1.getString("COLOR"));
						GENERIC.setDD(rs1.getString("EL_NO"));
						GENERIC.setEE(rs1.getString("YIELD"));
						GENERIC.setFF(rs1.getString("MT_QTY"));
						GENERIC.setGG(rs1.getString("NUM"));
						String  Odno_num=GetOd_noNum(rs1.getString("MODEL_NA"),rs1.getString("GROUP_NO"),rs1.getString("COLOR"),conn);
						GENERIC.setHH(Odno_num);
										
						list.add(GENERIC);
						InsSql+="INTO DSID01_TEMP_LOG (MODEL_NA,GROUP_NO,COLOR,EL_NO,NUM) "
								+ "VALUES('"+rs1.getString("MODEL_NA")+"','"+rs1.getString("GROUP_NO")+"','"+rs1.getString("COLOR")+"','"+rs1.getString("EL_NO")+"','"+rs1.getString("NUM")+"')";
						
					}
					ps1.close();
					rs1.close();
					} catch (SQLException e) {				
						e.printStackTrace();
					}
//					}

		ListBox.setModel(new ListModelList<Object>(list));
		
		//存儲接單臨時資料
		Insert(InsSql,conn);

		boolean Enough=false;
		String Last_WOI_List="",ErrMess="";
		int order_num=0;
		
			String MODEL_NA="";
			String sql2="SELECT * FROM DSID01_TEMP ORDER BY FACTRECDATE";		
			System.err.println("OD_NO>>>>>"+sql2);
			try {
				ps1 = conn.prepareStatement(sql2);
				rs1 = ps1.executeQuery();			
				while(rs1.next()){	
					
					if(order_num<Double.valueOf(UpLimit)){	
						if(rs1.getString("MODEL_NA").startsWith("W ")){
							MODEL_NA=rs1.getString("MODEL_NA").substring(2);
						}else{
							MODEL_NA=rs1.getString("MODEL_NA");
						}
						
						List<String> GroupList = new ArrayList<String>();
						String GrSql="SELECT DISTINCT GROUP_NO FROM DSID01_TEMP_LOG WHERE MODEL_NA='"+MODEL_NA+"' ORDER BY GROUP_NO";		
//						System.err.println("GroupList>>>>>"+GrSql);
						try {
							ps2 = conn.prepareStatement(GrSql);
							rs2 = ps2.executeQuery();			
							while(rs2.next()){	
								GroupList.add(rs2.getString("GROUP_NO"));				
							}
							ps2.close();
							rs2.close();
						} catch (SQLException e) {				
							e.printStackTrace();
						}
//						System.err.println("GroupList>>>>>"+GroupList);
						
						
						
						WOI_List+=rs1.getString("WORK_ORDER_ID")+",";
						for(int j=0;j<GroupList.size();j++){
							Enough=CheckEnough(MODEL_NA,GroupList.get(j),rs1.getString(GroupList.get(j)),WOI_List,conn);
							if(Enough==false){
								System.err.println(MODEL_NA+"部位："+GroupList.get(j)+"顏色："+rs1.getString(GroupList.get(j))+" 材料不足!");							
								ErrMess+=MODEL_NA+"部位："+GroupList.get(j)+"顏色："+rs1.getString(GroupList.get(j))+" 對應材料："+GetEl_no(MODEL_NA,GroupList.get(j),rs1.getString(GroupList.get(j)),conn)+"材料不足,導致"+rs1.getString("WORK_ORDER_ID")+"不可接；\n";
								continue;
							}
						}
						if(Enough==true){
							Last_WOI_List=WOI_List;
							order_num++; 
						}else{
							WOI_List=Last_WOI_List;
						}
					}else{
						continue;
					}
					
//					System.err.println("Last_WOI_List>>>>"+Last_WOI_List);
				}
				ps1.close();
				rs1.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			
//		}
		txt_rec_odno.setValue(Last_WOI_List);
		String Detial=GetDetial(Last_WOI_List,conn);
		
//		Messagebox.show("不可接篩選結果：\n"+ErrMess);
//		Messagebox.show("可接篩選結果：\n"+Detial);
		txt_Pro.setValue(Detial+"\n"+ErrMess);
		
		btnConfirm.setVisible(true);
		} catch (Exception e) {				
			e.printStackTrace();
		}
		finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
			if(rs2!=null){
				rs2.close();
			}
			if(ps2!=null){
				ps2.close();
			}
			Common.closeConnection(conn);
		}
	
		
	}

	private String GetEl_no(String MODEL_NA, String GROUP_NO, String COLOR, Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;	
		String EL_NO="";
		String Sql="SELECT * FROM DSID01_TEMP_LOG WHERE MODEL_NA='"+MODEL_NA+"' AND GROUP_NO='"+GROUP_NO+"' AND COLOR='"+COLOR+"' ORDER BY NUM";		
//		System.err.println("--可做>>>>>\n"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){	
				EL_NO=rs1.getString("EL_NO");		
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}
		return EL_NO;
	}

	private String GetDetial(String last_WOI_List, Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;	
		int num=0;
		String Detial="";
		String Sql="SELECT NIKE_SH_ARITCLE,COUNT(*) COU FROM DSID01_TEMP WHERE WORK_ORDER_ID IN ('"+last_WOI_List.substring(0, last_WOI_List.length()-1).replace(",", "','")+"') GROUP BY NIKE_SH_ARITCLE ORDER BY NIKE_SH_ARITCLE";		
//		System.err.println("GetOd_noNum>>>>>"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			while(rs1.next()){	
				Detial+="型體："+rs1.getString("NIKE_SH_ARITCLE")+" 預接："+rs1.getString("COU")+" 雙\n";
				num+=Integer.valueOf(rs1.getString("COU"));
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}
		Detial="共可預接"+num+",其中：\n"+Detial+"詳細 WORK_ORDER_ID 已列出！！！\n";
		return Detial;
	}

	private String GetLastOrder(Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;	
		String Maxnum="";
		String Sql="SELECT COUNT(*) COU FROM DSID01 WHERE ORDER_DATE = (SELECT MAX(ORDER_DATE) FROM DSID01)";		
//		System.err.println("GetOd_noNum>>>>>"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){	
				Maxnum=rs1.getString("COU");				
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}
		return Maxnum;
	}

	private void Insert(String insSql, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstm1 =null;
		String Sql1="DELETE DSID01_TEMP_LOG";
//		System.err.println("DSID01_TEMP_LOG刪除>>>>>"+Sql1);
		try {
			pstm1 = conn.prepareStatement(Sql1);
			pstm1.executeUpdate();
			pstm1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String InsSql="INSERT ALL "+insSql +" SELECT * FROM DUAL";
//		System.err.println("InsSql刪除>>>>>"+InsSql);
		try {
			pstm1 = conn.prepareStatement(InsSql);
			pstm1.executeUpdate();
			pstm1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstm1!=null){
				pstm1.close();
			}

		}
		
		
	}

	private String GetOd_noNum(String MODEL_NA, String GROUP_NO, String COLOR, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;	
		String od_nonum="";
		String Sql="SELECT COUNT(*) COU FROM DSID01_TEMP WHERE "+GROUP_NO.toUpperCase().replace("GROUP ", "GROUP")+" LIKE '"+COLOR+"%' AND MODEL_NA LIKE '%"+MODEL_NA+"'";		
//		System.err.println("GetOd_noNum>>>>>"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){	
				od_nonum=rs1.getString("COU");				
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
		}

		return od_nonum;
	}

	private boolean CheckEnough(String MODEL_NA, String GROUP_NO, String COLOR, String WOI_List, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		Boolean Enough=true;
		WOI_List=WOI_List.substring(0,WOI_List.length()-1).replace(",", "','");
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;	
		String Max_num="0.0",Pre_num="0.0";
		String Sql="SELECT * FROM DSID01_TEMP_LOG WHERE MODEL_NA='"+MODEL_NA+"' AND GROUP_NO='"+GROUP_NO+"' AND COLOR='"+COLOR+"' ORDER BY NUM";		
//		System.err.println("--可做>>>>>\n"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){	
				Max_num=rs1.getString("NUM");		
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}
		 
		String Sql2="SELECT COUNT(*) COU FROM DSID01_TEMP WHERE WORK_ORDER_ID IN ('"+WOI_List+"') AND "+GROUP_NO+" LIKE '"+COLOR+"%' AND MODEL_NA LIKE '%"+MODEL_NA+"'";		
//		System.err.println("--目前訂單顏色>>>>>\n"+Sql2);
		try {
			ps1 = conn.prepareStatement(Sql2);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){	
				Pre_num=rs1.getString("COU");				
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
		}

		
		if(Double.valueOf(Max_num)<Double.valueOf(Pre_num)){
			Enough=false;
		}
		
		return Enough;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return Receivewindow;
	}

	@Override
	protected MSMode getMSMode() {
		// TODO Auto-generated method stub
		return MSMode.MASTER;
	}

	@Override
	protected ArrayList<String> getKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<String> getKeyValue(Object objectEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean beforeSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean doCustomSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap getReturnMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
