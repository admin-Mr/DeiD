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

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
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
import ds.common.services.CRUDService;
import javax.persistence.Query;

public class DSID24RProgram extends OpenWinCRUD{

	@Wire
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnExport,btnQuery1,btnQuery2;
	@Wire
	private Datebox po_date;
	@Wire
//	private Textbox txtMODEL_NA,txtOD_NO;
	private Listbox List_Model_na,List_od_no;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
//		doSearch();
		
	}
	
	@Listen("onClick = #btnQuery2")
	public void onClickbtnQuery2(Event event) throws SQLException{
		String DATE=po_date.getValue().toString();
		if(!"".equals(DATE)&&DATE!=null){
			DATE=sdf.format(po_date.getValue());
			String ExSql="";
			if(List_Model_na.getSelectedItem()!=null){
				for(Listitem ltAll : List_Model_na.getItems()){
					if (ltAll.isSelected()){
						if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
							ExSql+="  AND NIKE_SH_ARITCLE LIKE '%"+(Object)ltAll.getValue()+"' ";					
						}
					}
				}
			}
			
			Connection conn = Common.getDbConnection();
			PreparedStatement  ps1 = null;
			ResultSet  rs1 = null;
			List<String> name_list = new ArrayList<String>();
			name_list.add("");
			String Sql="SELECT WORK_ORDER_ID FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+DATE+"' "+ExSql;
			System.err.println(">>>"+Sql);
			try {
				ps1 = conn.prepareStatement(Sql);
				rs1 = ps1.executeQuery();			
				while(rs1.next()){

					name_list.add(rs1.getString("WORK_ORDER_ID"));
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
			
			List_od_no.setModel(new ListModelList<Object>(name_list));
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0204"));
		}
		

	  }
	
	@Listen("onClick = #btnQuery1")
	public void onClickbtnQuery1(Event event) throws SQLException{
		String DATE=po_date.getValue().toString();
		if(!"".equals(DATE)&&DATE!=null){
			DATE=sdf.format(po_date.getValue());
			Connection conn = Common.getDbConnection();
			PreparedStatement  ps1 = null;
			ResultSet  rs1 = null;
			List<String> name_list = new ArrayList<String>();
			name_list.add("");
			String Sql="SELECT DISTINCT NIKE_SH_ARITCLE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+DATE+"' ORDER BY NIKE_SH_ARITCLE";
			System.err.println(">>>"+Sql);
			try {
				ps1 = conn.prepareStatement(Sql);
				rs1 = ps1.executeQuery();			
				while(rs1.next()){

					name_list.add(rs1.getString("NIKE_SH_ARITCLE"));
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
			
			List_Model_na.setModel(new ListModelList<Object>(name_list));
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0204"));
		}
		

	  }
	  
	@Listen("onClick = #btnExport")
	public void onClickbtnExport(Event event) throws SQLException{		

		
		Date Date=po_date.getValue();
		
		if(!"".equals(Date)&&Date!=null){
			String DATE=sdf.format(po_date.getValue());
			
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
			
			String WORK_ORDER_ID="";
			if(List_od_no.getSelectedItem()!=null){
				for(Listitem ltAll : List_od_no.getItems()){
					if (ltAll.isSelected()){
						if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
							WORK_ORDER_ID=(String)ltAll.getValue();				
						}
					}
				}
			}
			
			DSID24_1RTask.ExcelExport(DATE,MODEL_NA,WORK_ORDER_ID);
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0204"));
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
		return  MSMode.MASTER;
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
