package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.GENERIC;
import util.Common;
import util.QueryWindow;

public class DSID13MQryMODEL_NA extends QueryWindow {
	
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnConfirmN, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;
	@Wire
	private Listbox queryListBox;
	
	@Override
	public void doAfterCompose(Component Window) throws Exception {
		super.doAfterCompose(Window);
		doSearch();
	}
	
	@Override
	protected Window getRootWindow() {		
		// TODO Auto-generated method stub
		return windowQuery;
	}
	
	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> setComboboxColumn() {
		// TODO Auto-generated method stub
		List<String> listColumn = new ArrayList<String>();
		listColumn.add(Labels.getLabel("Operation.MODEL_NA"));
		return listColumn;
	}
	@Override
	protected List<String> setComboboxCondition() {
		List<String> listCondition = new ArrayList<String>();
		listCondition.add("=");
		return listCondition;
	}

	@Override
	protected String getSQLWhere() {
		// TODO Auto-generated method stub
		String Sql="";
		return Sql;
	}

	@Override
	protected String getCustomSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCustomCountSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTextBoxValue() {
		// TODO Auto-generated method stub
		return txtQuery.getValue();
	}

	@Override
	protected String getPagingId(){
		return "pagingOperation";
	}
	@Override
	protected Combobox getcboColumn() {
		// TODO Auto-generated method stub
		return cboCondition;
	}

	@Override
	protected Combobox getcboCondition() {
		// TODO Auto-generated method stub
		return cboCondition;
	}

	@Override
	protected HashMap getMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String getOrderby() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void doFillListbox(int index) {
		List<GENERIC> list = new ArrayList<GENERIC>();
		Connection DB00 = Common.getDbConnection();
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;	
		String sql = "SELECT DISTINCT MODEL_NA,UP_DATE FROM DSID12 WHERE MODEL_NA NOT LIKE 'W%' ORDER BY UP_DATE DESC";
		System.out.println(sql);
			try {
				ps1 = DB00.prepareStatement(sql);
				rs1 = ps1.executeQuery();	
				while(rs1.next()){
				GENERIC GENER=new GENERIC();
				GENER.setAA(rs1.getString("MODEL_NA"));
				list.add(GENER);
				
				}
				ps1.close();
				rs1.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			
			}
		
		Common.closeConnection(DB00);
		ListModelList queryMod = new ListModelList(list, true);
		queryListBox.invalidate();
		queryListBox.setModel(queryMod);
		queryMod.setMultiple(true);
	}
	
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnConfirmn")
	public void onClickbtnConfirmn(Event event) {

		ArrayList<Object> arrList = new ArrayList<Object>();
		ArrayList<Object> arrList2 = new ArrayList<Object>();	
		arrList.clear();
		String  MODEL_NA="";
		try{
			if (queryListBox.getSelectedItem() == null){
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);				
				return;
			}else{
				
				for(Listitem ltAll : queryListBox.getItems()){
					if (ltAll.isSelected()){		
						arrList.add((Object)ltAll.getValue());
					}
				}	
				for(int i=0; i<arrList.size(); i++){
					MODEL_NA=((GENERIC)arrList.get(i)).getAA();
				}
			}	
		}catch (NullPointerException e){			
			e.getStackTrace();
		} 
		if(MODEL_NA.length()>0){
			MODEL_NA=MODEL_NA.substring(0,MODEL_NA.length());
		}
		arrList2.add(MODEL_NA);
		Events.sendEvent(new Event(returnMethodName, parentWindow, arrList2));
		getRootWindow().detach();
		
	}
}
