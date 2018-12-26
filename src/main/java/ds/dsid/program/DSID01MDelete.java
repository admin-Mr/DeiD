package ds.dsid.program;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import util.BlackBox;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID01MDelete extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnConfirm_delete;
	@Wire
	private Datebox Dateorder_date;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	

	}
	
	// 確認批量刪除
	@Listen("onClick =#btnConfirm_delete")
	public void onClickbtnConfirm_delete(Event event) throws Exception{
		
		doDelete_All();
	}

	
	
	private void doDelete_All() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		String  Sql1="",Sql2="";
		
		if(Dateorder_date.getValue()!=null){
			Sql1="DELETE DSID01_TEMP2 WHERE WORK_ORDER_ID IN (SELECT WORK_ORDER_ID FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"')";
			Sql2="DELETE DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(Dateorder_date.getValue())+"'";
			
			System.err.println("01_TEMP2批量刪除>>>>>"+Sql1);
			System.err.println("01批量刪除>>>>>"+Sql2);
			try {
				PreparedStatement pstm1 = conn.prepareStatement(Sql1);
				pstm1.executeUpdate();
				pstm1.close();
				PreparedStatement pstm2 = conn.prepareStatement(Sql2);
				pstm2.executeUpdate();
				pstm2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Messagebox.show(Format.format(Dateorder_date.getValue())+" 資料批量刪除成功！！！");
		}else{
			Messagebox.show("選擇的日期為空，請核查！！！");
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
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap getReturnMap() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
