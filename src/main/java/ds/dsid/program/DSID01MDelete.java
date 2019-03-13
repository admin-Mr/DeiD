package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID01MDelete extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnConfirm_delete,btnConfirm_delete_old;
	@Wire
	private Datebox Dateorder_date;
	String TABLE="";
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	
		Execution execution = Executions.getCurrent();
		TABLE =  (String) execution.getArg().get("TABLE"); 
		System.err.println(">>>>>"+TABLE);
		if("DSID01_TEMP".equals(TABLE)){
			btnConfirm_delete_old.setVisible(false);	
		}

	}
	
	// 確認批量刪除
	@Listen("onClick =#btnConfirm_delete")
	public void onClickbtnConfirm_delete(Event event) throws Exception{
		
		doDelete_All();
	}

	// 確認批量刪除
	@Listen("onClick =#btnConfirm_delete_old")
	public void onClickbtnConfirm_delete_old(Event event) throws Exception{
		
		doDelete_All_old();
	}
	
	
	private void doDelete_All() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement pstm1=null,pstm2=null;
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		String  Sql1="",Sql2="";
		
		if(Dateorder_date.getValue()!=null){
			Sql1="DELETE DSID01_TEMP2 WHERE WORK_ORDER_ID IN (SELECT WORK_ORDER_ID FROM "+TABLE+" WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"')";
			Sql2="DELETE "+TABLE+" WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"'";
			
			System.err.println("01_TEMP2批量刪除>>>>>"+Sql1);
			System.err.println("01批量刪除>>>>>"+Sql2);
			try {
				pstm1 = conn.prepareStatement(Sql1);
				pstm1.executeUpdate();
				pstm1.close();
				pstm2 = conn.prepareStatement(Sql2);
				pstm2.executeUpdate();
				pstm2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(pstm1!=null){
					pstm1.close();
				}
				if(pstm2!=null){
					pstm2.close();
				}
				Common.closeConnection(conn);
			}
			
			Messagebox.show(Format.format(Dateorder_date.getValue())+" "+Labels.getLabel("DSID.MSG0017"));
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0018"));
		}

			
	}

	private void doDelete_All_old() throws SQLException {
		// TODO Auto-generated method stub
//		Connection conn=Common.getDbConnection();
		Connection conn=Common.getService1Conn();
		PreparedStatement pstm1=null,pstm2=null;
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		String  Sql1="",Sql2="";
		
		if(Dateorder_date.getValue()!=null){
//			Sql1="DELETE DSID31@FTLDB01.DEANSHOES.COM WHERE OD_NO IN (SELECT OD_NO FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"')";
//			Sql2="DELETE DSID30@FTLDB01.DEANSHOES.COM WHERE OD_NO IN (SELECT OD_NO FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"')";
			Sql1="DELETE DSID31 WHERE OD_NO IN (SELECT OD_NO FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"')";
			Sql2="DELETE DSID30 WHERE OD_NO IN (SELECT OD_NO FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"')";
			
			System.err.println("DSID31批量刪除>>>>>"+Sql1);
			System.err.println("DSID30批量刪除>>>>>"+Sql2);
			try {
				pstm1 = conn.prepareStatement(Sql1);
				pstm1.executeUpdate();
				pstm1.close();
				pstm2 = conn.prepareStatement(Sql2);
				pstm2.executeUpdate();
				pstm2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(pstm1!=null){
					pstm1.close();
				}
				if(pstm2!=null){
					pstm2.close();
				}
				Common.closeConnection(conn);
			}
			
			Messagebox.show(Format.format(Dateorder_date.getValue())+" "+Labels.getLabel("DSID.MSG0017"));
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0018"));
		}

		Common.closeConnection(conn);	
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
	protected boolean doCustomSave(Connection conn) {
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
