package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
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

public class DSID25RSetTime extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnConfirm;
	@Wire
	private Listbox List_Model_na;
	@Wire
	private Datebox DateStart_date;

	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	

		Connection conn = Common.getDbConnection();
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		List<String> name_list = new ArrayList<String>();
		String 	Sql="SELECT DISTINCT MODEL_NA FROM DSID25 ORDER BY MODEL_NA";
		System.err.println(">>>"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			while(rs1.next()){

				name_list.add(rs1.getString("MODEL_NA"));
			}
			rs1.close();
			ps1.close();
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
		
		List_Model_na.setModel(new ListModelList<Object>(name_list));

	}
	
	// 確認批量刪除
	@Listen("onClick =#btnConfirm")
	public void onClickbtnConfirm(Event event) throws Exception{
		// TODO Auto-generated method stub
		
		Connection conn=Common.getDbConnection();
		PreparedStatement pstm1=null;
		String MODEL_NA="";
		if(List_Model_na.getSelectedItem()!=null){
			for(Listitem ltAll : List_Model_na.getItems()){
				if (ltAll.isSelected()){
					if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
						MODEL_NA=(String)ltAll.getValue();				
					}
				}
			}
		}
		
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		String  Sql1="";
		
		
		if(!"".equals(MODEL_NA)&&MODEL_NA!=null&&DateStart_date.getValue()!=null){
			Sql1="UPDATE DSID25 SET START_DATE=TO_DATE('"+Format.format(DateStart_date.getValue())+"','YYYY/MM/DD') WHERE MODEL_NA='"+MODEL_NA+"' ";			
			System.err.println("SetTime>>>>>"+Sql1);

			try {
				pstm1 = conn.prepareStatement(Sql1);
				pstm1.executeUpdate();
				pstm1.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(pstm1!=null){
					pstm1.close();
				}
				
				Common.closeConnection(conn);
			}
			
			Messagebox.show("SetTime OK！！");
			windowMaster.detach();
		}else{
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0011"));
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
