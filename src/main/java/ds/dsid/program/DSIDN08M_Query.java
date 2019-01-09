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
import ds.dsid.domain.DSIDN08;
import ds.dsid.domain.DSIDN08_T;
import ds.dspb.domain.DSPB03VO;
import util.QueryWindow;

public class DSIDN08M_Query extends QueryWindow {
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
		System.out.println("====qqqqqqqqqqqqqqqqqqCRUDServicdddddddde===="+window);
		Execution execution = Executions.getCurrent();
		ServletContext sc = Sessions.getCurrent().getWebApp().getServletContext();
		strPB_SYSID = sc.getInitParameter("projectID");
		strPB_GROUPID = (String) execution.getArg().get("pbgroupid");
		strAccount = (String) execution.getArg().get("account");
		System.out.println("=======strPB_GROUPID========="+strPB_GROUPID);
		System.out.println("========strAccount==========="+strAccount);
		
		
		/**
		CRUDService = (ds.common.services.CRUDService) RegisterJavaBean.getJavaBean("CRUDService2");//SpringUtil.getBean("CRUDService2");
		//System.out.println("===================abc================="+ RegisterJavaBean.getJavaBean("CRUDDao2"));
		//System.out.println("===================abc================="+ RegisterJavaBean.getJavaBean("CRUDService2"));
		setCRUDService(CRUDService);
		**/
		
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService2");
		setCRUDService(CRUDService);
		PO_NO =  (String) execution.getArg().get("PO_NO");  
		PO_NO_CONDITION =  (String) execution.getArg().get("PO_NO_CONDITION");  
		System.out.println("========PO_NO============="+PO_NO);
		System.out.println("========PO_NO_CONDITION============="+PO_NO_CONDITION);
		doSearch();
		
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		//listColumn.add(Labels.getLabel("DSPB.DSPB00_NEW.PB_PRGNAME"));
		//listColumn.add(Labels.getLabel("DSPB.DSPB00_NEW.PB_LANGTAG"));
		//listColumn.add("編號LIKE");
		listColumn.add("訂購單號等於");
		return listColumn;
	}

	@Override
	protected List<String> setComboboxCondition() {
		List<String> listCondition = new ArrayList<String>();
		listCondition.add("=");
		
		/*
		 * List<String> listCondition = new ArrayList<String>();
		listCondition.add("LIKE");
		listCondition.add("=");
		 */
		return listCondition;
	}

	@Override
	protected String getSQLWhere() {
		String strSQL = "Where 1=1   ";
		String strWhere =PO_NO;// txtQuery.getValue();
		//String strField = " and (SELECT PO_NO FROM DSPO05 WHERE DSPO05.Po_Purno='IGM1804120006' or PO_NO='IGM1804120006')";
		strSQL += " and t.PO_NO in (SELECT PO_NO FROM DSPO05 WHERE DSPO05.Po_Purno='"+strWhere+"' or PO_NO='"+strWhere+"') ";
		if(PO_NO_CONDITION != null && PO_NO_CONDITION.length()>1){
			strSQL +=" and  t.PO_SEQ NOT IN "+PO_NO_CONDITION;
		}
		System.out.println("=====strSQL==="+strSQL);
		/*
		if (strWhere.isEmpty() && !cboCondition.getSelectedItem().getLabel().equals("LIKE")) {
			return null;
		} else {
			switch (cboColumn.getSelectedIndex()) {
			case 0:
				strField += " and t.PO_NO";
				break;
			}
			switch (cboCondition.getSelectedItem().getLabel()) {
			case "=":
				strSQL += strField + " = '" + strWhere + "' ";
				break;
			}*/
		/*
		String strSQL = "Where 1=1  and  (t.PO_NO is null ";
		String strWhere = txtQuery.getValue();
		String strField = "";
		if (strWhere.isEmpty() && !cboCondition.getSelectedItem().getLabel().equals("LIKE")) {
			return null;
		} else {
			switch (cboColumn.getSelectedIndex()) {
			case 0:
				strField += " OR t.PO_NO";
				break;
			case 1:
				strField += " OR t.PO_NO";
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
*/
//			if (strPB_GROUPID != null) {
//				strSQL += " AND t.PB_ID  NOt IN (SELECT PB_ID FROM DSPB01_GRP WHERE PB_GROUPID='" + strPB_GROUPID
//						+ "')";
//			} else {
//				strSQL += " AND t.PB_ID  NOt IN (SELECT PB_ID FROM DSPB01 WHERE PB_USERID='" + strAccount + "')";
//			}
//		}
		return strSQL;
	}

	@Override
	protected String getCustomSQL() {
		return " select t.PO_NO,t.PO_SEQ,t.EL_NO,t.PO_QTY,DSEL00.EL_CNAME "+
			   "from DSPO06 t left join DSEL00  on t.EL_NO=DSEL00.EL_NO  ";
	}

	@Override
	protected String getCustomCountSQL() {
		return " select count(*) "+
				   "from DSPO06 t left join DSEL00  on t.EL_NO=DSEL00.EL_NO  ";
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
		return " Order by to_number(t.PO_SEQ)  ";
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
		List<DSIDN08_T> lst = new ArrayList<DSIDN08_T>();
		Query qry = super.getQueryPagingbySize();
		// t.PO_NO,t.PO_SEQ,t.EL_NO,t.PO_QTY,DSEL00.EL_CNAME 
		if (qry != null) {
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				Object[] obj = (Object[]) i.next();
				DSIDN08_T e = new DSIDN08_T();
				e.setPO_NO((String) obj[0]);
				e.setPO_SEQ((String) obj[1]);
				e.setEL_NO((String) obj[2]);
				e.setPO_QTY(((BigDecimal) obj[3]).doubleValue());
				e.setEL_CNAME((String) obj[4]);
				lst.add(e);
			}
		}
		return lst;
	}

}
