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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.QueryWindow;

public class DSPB002M_Controller03 extends QueryWindow {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;
	private String strPB_SYSID, strPB_GROUPID, strAccount;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Execution execution = Executions.getCurrent();
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		strPB_GROUPID = (String) execution.getArg().get("pbgroupid");
		strAccount = (String) execution.getArg().get("account");
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		listColumn.add(Labels.getLabel("DSPB.DSPB00_NEW.PB_PRGNAME"));
		listColumn.add(Labels.getLabel("DSPB.DSPB00_NEW.PB_LANGTAG"));
		return listColumn;
	}

	@Override
	protected List<String> setComboboxCondition() {
		List<String> listCondition = new ArrayList<String>();
		listCondition.add("LIKE");
		listCondition.add("=");
		return listCondition;
	}

	@Override
	protected String getSQLWhere() {
		String strSQL = "Where t.PB_SYSID = '" + strPB_SYSID + "' and t.PB_ADMIN = 'Y' and (t.PB_PRGNAME is null ";
		String strWhere = txtQuery.getValue();
		String strField = "";
		if (strWhere.isEmpty() && !cboCondition.getSelectedItem().getLabel().equals("LIKE")) {
			return null;
		} else {
			switch (cboColumn.getSelectedIndex()) {
			case 0:
				strField += " OR t.PB_PRGNAME";
				break;
			case 1:
				strField += " OR t.PB_LANGTAG";
				break;
			}
			switch (cboCondition.getSelectedItem().getLabel()) {
			case "LIKE":
				strSQL += strField + " LIKE :keyword) ";
				break;
			case "=":
				strSQL += strField + " = '" + strWhere + "') ";
				break;
			}

			if (strPB_GROUPID != null) {
				strSQL += " AND t.PB_ID  NOt IN (SELECT PB_ID FROM DSPB01_GRP WHERE PB_GROUPID='" + strPB_GROUPID
						+ "')";
			} else {
				strSQL += " AND t.PB_ID  NOt IN (SELECT PB_ID FROM DSPB01 WHERE PB_USERID='" + strAccount + "')";
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
	protected String getEntityName() {
		return "DSPB00_NEW";
	}

	@Override
	protected String getTextBoxValue() {
		return txtQuery.getValue();
	}

	@Override
	protected String getPagingId() {
		return "pagingOperation";
	}

	@Override
	protected String getOrderby() {
		return " Order by NVL(t.PB_MUNODE,'0') , t.PB_MUITEM ";
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
