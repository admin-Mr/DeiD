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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import util.BlackBox;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID04MDelete extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btndelete;
	@Wire
	private Textbox txtMODEL_NA;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	

	}
	
	// 確認批量刪除
	@Listen("onClick =#btndelete")
	public void onClickbtnConfirm_delete(Event event) throws Exception{
		
		doDelete();
	}

	
	
	private void doDelete() throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = Common.getDbConnection();
		PreparedStatement pstm1=null,pstm2=null,pstm3=null,pstm4=null,pstm5=null;
		String MODEL_NA=txtMODEL_NA.getValue();
		if(!"".equals(MODEL_NA)){
			String sql1 ="DELETE DSID04 WHERE MODEL_NA='"+MODEL_NA+"'";
			String sql2 ="DELETE DSID04_1 WHERE MODEL_NA='"+MODEL_NA+"'";
			String sql3 ="DELETE DSID04_2 WHERE MODEL_NA='"+MODEL_NA+"'";
			String sql4 ="DELETE DSID04_3 WHERE MODEL_NA='"+MODEL_NA+"'";
			String sql5 ="DELETE DSID04_4 WHERE MODEL_NA='"+MODEL_NA+"'";
			System.out.println(">>>刪除重複資料>>>");
			try {
				pstm1 = conn.prepareStatement(sql1);
				pstm1.executeUpdate();
				pstm1.close();
				pstm2 = conn.prepareStatement(sql2);
				pstm2.executeUpdate();
				pstm2.close();
				pstm3 = conn.prepareStatement(sql3);
				pstm3.executeUpdate();
				pstm3.close();
				pstm4 = conn.prepareStatement(sql4);
				pstm4.executeUpdate();
				pstm4.close();
				pstm5 = conn.prepareStatement(sql5);
				pstm5.executeUpdate();
				pstm5.close();
				Messagebox.show("型體："+MODEL_NA+"資料全部刪除完成！！！");
			} catch (Exception e) {
				Messagebox.show("型體："+MODEL_NA+"資料全部刪除失敗,"+e);
				e.printStackTrace();
			}finally{
				if(pstm1!=null){
					pstm1.close();
				}
				if(pstm2!=null){
					pstm2.close();
				}
				if(pstm3!=null){
					pstm3.close();
				}
				if(pstm4!=null){
					pstm4.close();
				}
				if(pstm5!=null){
					pstm5.close();
				}
				Common.closeConnection(conn);	
			}
		}else{
			Messagebox.show("型體資料為空,刪除失敗!");
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

	

}
