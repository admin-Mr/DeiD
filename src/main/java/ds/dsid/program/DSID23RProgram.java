package ds.dsid.program;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID23RProgram extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnExport;
	@Wire
	private Datebox po_date1,po_date2;
	@Wire
	private Textbox txtMODEL_NA;
	
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

//		doSearch();
	}
	
	@Listen("onClick = #btnExport")
	public void onClickbtnExport(Event event) throws SQLException{		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		Date Date=po_date1.getValue();
		String MODEL_NA=txtMODEL_NA.getValue().trim();
		
		if(!"".equals(Date)&&Date!=null){
			String START=sdf.format(po_date1.getValue());
			DSID23_1RTask.ExcelExport(MODEL_NA,START);
		}else{
			Messagebox.show("日期不能為空！！！");
		}
		
//		START="2018/10/09";
//		MODEL_NA="PEGASUS+35 ESS SU18 ID";
//		DSID23_1RTask.ExcelExport(MODEL_NA,START);
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
