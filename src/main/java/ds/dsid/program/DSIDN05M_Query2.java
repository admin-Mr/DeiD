package ds.dsid.program;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;
import javax.servlet.ServletContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.dsid.domain.DSID05;
import ds.dsid.domain.DSIDN08;
import ds.dsid.domain.DSIDN08_T;
import ds.dspb.domain.DSPB03VO;
import util.QueryWindow;

public class DSIDN05M_Query2 extends QueryWindow {
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;
	private String strPB_SYSID, strPB_GROUPID, strAccount;
	String PO_NO = "",PO_NO_CONDITION="";
	@Wire
	private Listbox queryListBox;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Execution execution = Executions.getCurrent();
		ServletContext sc = Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		strPB_GROUPID = (String) execution.getArg().get("pbgroupid");
		strAccount = (String) execution.getArg().get("account");
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService1");
		setCRUDService(CRUDService);
		PO_NO =  (String) execution.getArg().get("PO_NO");  
		PO_NO_CONDITION =  (String) execution.getArg().get("PO_NO_CONDITION");  
		doSearch();
		
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		listColumn.add("指令號等於");
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
		String strSQL = "Where 1=1   ";
		String strWhere =txtQuery.getValue();
		strSQL += " and t.OD_NO ='"+strWhere+"' ";
		return strSQL;
	}

	@Override
	protected String getCustomSQL() {
		return " SELECT  t.OD_NO,t.MODEL_NA "+
			   " FROM DSID30 t  ";
	}

	@Override
	protected String getCustomCountSQL() {
		return " SELECT COUNT(*)  FROM DSID30 t   ";
	}

	@Override
	protected String getEntityName() {
		return null;
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
		return " Order by t.OD_NO  ";
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
		return null;
	}

	@Override
	protected int getPageSize() {
		return 0;
	}

	@Override
	protected List getCustList() {
		List<DSID05> lst = new ArrayList<DSID05>();
		Query qry = super.getQueryPagingbySize();
		//    OD_NO  MODEL_NA
		if (qry != null) {
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				Object[] obj = (Object[]) i.next();
				DSID05 e = new DSID05();
				e.setOD_NO((String) obj[0]);
				e.setMODEL_NA((String) obj[1]);
				lst.add(e);
			}
		}
		return lst;
	}

}
