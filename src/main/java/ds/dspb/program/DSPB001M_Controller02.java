package ds.dspb.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.QueryWindow;

public class DSPB001M_Controller02 extends QueryWindow {
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Execution exec = Executions.getCurrent();
		txtQuery.setValue((String) exec.getArg().get("sysid"));		
	}
	
	@Override
	protected Window getRootWindow() {		
		return windowQuery;
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		listColumn.add(Labels.getLabel("Operation.PB_SYSID"));
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
		return " Where t.PB_SYSID = '" + txtQuery.getValue() + "' and t.PB_MUNODE is null ";
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
	protected String getEntityName() {
		return "DSPB00_NEW";
	}

	@Override
	protected String getTextBoxValue() {
		return txtQuery.getValue();
	}

	@Override
	protected String getPagingId() {
		return "pagingMenuNode";
	}

	@Override
	protected Combobox getcboColumn() {
		return cboColumn;
	}

	@Override
	protected Combobox getcboCondition() {
		return cboCondition;
	}

	@Override
	protected HashMap getMap() {
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

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
