package ds.dsid.program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID13RProgram extends OpenWinCRUD{
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnExport;
	@Wire
	private Datebox po_date1,po_date2;
	@Wire
//	private Textbox txtMODEL_NA,txtSENDWK_NAME;
	private Listbox List_MODEL_NA,List_SENDWK_NAME;
//	@Wire
//	private Textbox txtSENDWK_NAME;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
	}
	//查詢按鈕，根據所選日期查詢該天的所有形體
	@Listen("onClick =#btnQuery")
	public void onClickbtnQuery(Event event) throws SQLException{
		String START=po_date1.getValue().toString();
		String SARTY=po_date2.getValue().toString();
		if(!"".equals(START)&&START!=null && !"".equals(SARTY) && SARTY!=null){
			START=sdf.format(po_date1.getValue());
			SARTY=sdf.format(po_date2.getValue());
			Connection conn = Common.getDbConnection();
			PreparedStatement  ps1 = null;
			ResultSet  rs1 = null;
			List<String> NA_list = new ArrayList<String>();
			NA_list.add("");
			String Sql="SELECT DISTINCT NIKE_SH_ARITCLE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')BETWEEN'"+START+"'AND '"+SARTY+"' ORDER BY NIKE_SH_ARITCLE";
			System.err.println(">>>"+Sql);
			try {
				ps1 = conn.prepareStatement(Sql);
				rs1 = ps1.executeQuery();	
				while(rs1.next()){
					NA_list.add(rs1.getString("NIKE_SH_ARITCLE"));
				}
				ps1.close();
				rs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {	
				if(rs1!=null){
					rs1.close();
				}
				if(ps1!=null){
					ps1.close();
				}
				Common.closeConnection(conn);
			}
			List_MODEL_NA.setModel(new ListModelList<Object>(NA_list));
		}else{
			Messagebox.show("日期不能為空！！！");
		}
	  }

		//查詢按鈕，根據所選日期查詢該天的所有形體
		@Listen("onClick =#btnQuery1")
		public void onClickbtnQuery1(Event event) throws SQLException{
				Connection conn = Common.getDbConnection();
				PreparedStatement  ps1 = null;
				ResultSet  rs1 = null;
				List<String> NA_list = new ArrayList<String>();
				NA_list.add("");
				String Sql="SELECT PG_NAME FROM DSID13";
				System.err.println(">>>"+Sql);
				try {
					ps1 = conn.prepareStatement(Sql);
					rs1 = ps1.executeQuery();
					while(rs1.next()){
						NA_list.add(rs1.getString("PG_NAME"));
					}
					ps1.close();
					rs1.close();
				} catch (SQLException e){
					e.printStackTrace();
				} finally {
					if(rs1!=null){
						rs1.close();
					}
					if(ps1!=null){
						ps1.close();
					}
					Common.closeConnection(conn);
				}
				List_SENDWK_NAME.setModel(new ListModelList<Object>(NA_list));
		  }
	
	//導出按鈕，根據日期來判斷形體獲取值
	@Listen("onClick =#btnExport")
	public void onClickbtnexport(Event event) throws Exception{
		Date date=po_date1.getValue();
		Date date1=po_date2.getValue();
		
		if(!"".equals(date) && date!=null && !"".equals(date1)&& date1!=null){
			String START=sdf.format(po_date1.getValue());
			String SARTY=sdf.format(po_date2.getValue());
			
			String MODEL_NA="";
			if(List_MODEL_NA.getSelectedItems()!=null){
				for(Listitem ltAll : List_MODEL_NA.getItems()){
					if(ltAll.isSelected()){
						if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
							MODEL_NA=(String)ltAll.getValue();
						}
					}
				}
			}
			String PG_NAME="";
			if(List_SENDWK_NAME.getSelectedItems()!=null){
				for(Listitem ltAll : List_SENDWK_NAME.getItems()){
					if(ltAll.isSelected()){
						if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
							PG_NAME=(String)ltAll.getValue();
						}
					}
				}
			}
			DSID13_1RTask.ExcelExport(MODEL_NA,START,SARTY,PG_NAME);
		}else{
			Messagebox.show("日期不能為空!");
		}
		}
		
	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
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
	//连接数据库
	public static Connection getDB01Conn(){
		Connection  conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.8.1.32:1521:ftldb1";
		String user = "DSOD";
		String pwd = "ora@it2013";
		try{
	        Class.forName(driver);
	     }catch(Exception e){
	        e.printStackTrace();
	     }
	    try{
	    	conn=DriverManager.getConnection(url,user,pwd);
	    	System.err.println(">>>鏈接DB01數據庫");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return conn;
	}
}
