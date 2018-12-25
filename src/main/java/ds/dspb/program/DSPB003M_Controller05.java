package ds.dspb.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;
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

import ds.dspb.domain.DSPB00_NEW;
import util.QueryWindow;

public class DSPB003M_Controller05 extends QueryWindow {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;
	private String strPB_SYSID, sUSER_ID;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Execution execution = Executions.getCurrent();
		sUSER_ID = (String) execution.getArg().get("USER_ID");
		
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected String getEntityName() {
		return "";
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		listColumn.add(Labels.getLabel("DSPB.DSPB00_NEW.PB_LANGTAG"));
		return listColumn;
	}

	@Override
	protected String getSQLWhere() {
		String strSQL = "Where 1=1 ";
		String strWhere = txtQuery.getValue();
		String strField = "";
		if (strWhere.isEmpty() && !cboCondition.getSelectedItem().getLabel().equals("LIKE")) {
			return null;
		} else {
			switch (cboColumn.getSelectedIndex()) {
			case 0:
				strField += " and PB_LANGTAG ";
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
		String sSQL = ""
				+ "SELECT DISTINCT PB_ID, PB_LANGTAG, PB_FILEPATH "
				+ "           FROM (SELECT A.PB_ID, B.PB_LANGTAG, B.PB_FILEPATH "
				+ "                   FROM DSPB01_GRP A LEFT JOIN DSPB00_NEW B ON A.PB_ID = B.PB_ID "
				+ "                  WHERE A.PB_GROUPID IN (SELECT PB_GROUPID FROM DSPB04 WHERE 1=1 AND PB_GROUPID LIKE '"+strPB_SYSID+"%' AND PB_USERID = '"+sUSER_ID+"') "
				+ "                 UNION ALL "
				+ "                 SELECT A.PB_ID, B.PB_LANGTAG, B.PB_FILEPATH "
				+ "                   FROM DSPB01 A LEFT JOIN DSPB00_NEW B ON A.PB_ID = B.PB_ID "
				+ "                  WHERE A.PB_ID LIKE '"+strPB_SYSID+"%' AND A.PB_USERID = '"+sUSER_ID+"') ";
		
		return sSQL;
	}

	@Override
	protected String getCustomCountSQL() {
		String sSQL = "SELECT COUNT(*) FROM (" + getCustomSQL() + ")";		
		return sSQL;
	}

	@Override
	protected String getTextBoxValue() {
		return txtQuery.getValue();
	}

	@Override
	protected String getOrderby() {
		return " Order by PB_ID ";
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
		List<DSPB00_NEW> lst = new ArrayList<DSPB00_NEW>();
		Query qry = super.getQueryPagingbySize();
		if (qry != null) {
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				Object[] obj = (Object[])i.next();
				DSPB00_NEW e = new DSPB00_NEW();
				e.setPB_ID((String) obj[0]);
				e.setPB_LANGTAG((String) obj[1]);
				lst.add(e);
			}
		}
		
		return lst;
	}
}
