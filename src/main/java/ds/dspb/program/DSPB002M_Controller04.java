package ds.dspb.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.QueryWindow;

public class DSPB002M_Controller04 extends QueryWindow {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;
	private String strPB_GROUPID;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Execution execution = Executions.getCurrent();
		strPB_GROUPID = (String) execution.getArg().get("PB_GROUPID");
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected String getEntityName() {
		return "DSPB01_GRP";
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		listColumn.add(Labels.getLabel("DSPB.DSPB00_NEW.PB_LANGTAG"));
		return listColumn;
	}

	@Override
	protected String getSQLWhere() {
		String strSQL = "Where 1=1 and t.PB_GROUPID = '" + strPB_GROUPID + "'";
		String strWhere = txtQuery.getValue();
		String strField = "";
		if (strWhere.isEmpty() && !cboCondition.getSelectedItem().getLabel().equals("LIKE")) {
			return null;
		} else {
			switch (cboColumn.getSelectedIndex()) {
			case 0:
				strField += " and t.DSPB00_NEW.PB_LANGTAG ";
				break;
			}
			switch (cboCondition.getSelectedItem().getLabel()) {
			case "LIKE":
				strSQL += strField + " LIKE :keyword ";
				break;
			case "=":
				strSQL += strField + " = '" + strWhere + " ";
				break;
			}
		}
		return strSQL;
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
		return txtQuery.getValue();
	}

	@Override
	protected String getOrderby() {
		return " Order by t.PB_ID ";
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
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
}
