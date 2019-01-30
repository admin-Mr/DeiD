package ds.dsid.program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSIDN06_01M_Delete extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btndelete;
	@Wire
	private Textbox txtMODEL_NA;
	
	//需要重写的方法
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	
	}
		// 刪除
	@Listen("onClick =#btndelete")
	public void onClickbtnConfirm_delete(Event event) throws Exception{
		doDelete();
		}
	//根据形体删除资料
	private void doDelete() {
		//开启数据库连接
		Connection Conn = getDB01Conn();	
		String MODEL_NA=txtMODEL_NA.getValue();//获取用户输入的形体
			if(!"".equals(MODEL_NA)){//判断空字符串于形体是否相同
			String sql ="DELETE TEST003 WHERE MODEL_NA='"+MODEL_NA+"'";
			System.out.println(">>>刪除資料");
			try {
				PreparedStatement psm = Conn.prepareStatement(sql);//执行sql
				psm.executeUpdate();
				psm.close();
				Messagebox.show("型體："+MODEL_NA+"資料全部刪除完成！！！");
			} catch (Exception e) {
				// TODO: handle exception
				Messagebox.show("型體："+MODEL_NA+"資料全部刪除失敗,"+e);
				e.printStackTrace();
			}finally{
				Common.closeConnection(Conn);	
			}
			}else{
				Messagebox.show("型體為空,刪除失敗!");
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
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap getReturnMap() {
		// TODO Auto-generated method stub
		return null;
	}
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
