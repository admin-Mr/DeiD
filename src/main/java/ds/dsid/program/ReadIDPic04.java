package ds.dsid.program;
/**
 * 今日詳單 掃描
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID65;
import util.QueryWindow;

public class ReadIDPic04 extends QueryWindow{

	@WireVariable private CRUDService CRUDService;
	@Wire Window Dayscanning;
	@Wire private Button btnSearch;
	
	protected final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	private String State = "";
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService1");
		setCRUDService(CRUDService);
	}
	
	@Listen("onClick = #btnSearch")
	public void setCustomData() throws IOException{
		
		String userid = _userInfo.getAccount(); // 獲取登陸賬戶
		userid = "FORMING_DATE";
		State = userid;
		System.out.println(" ----- 04 State: " + State);
		
	}
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return Dayscanning;
	}

	@Override
	protected List<String> setComboboxColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> setComboboxCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSQLWhere() {
		// TODO Auto-generated method stub
		String Str = " where "+State+" = trunc(sysdate)";
		return Str;
	}

	@Override
	protected String getCustomSQL() {
		// TODO Auto-generated method stub	
		//return "SELECT substr(WORK_ORDER_ID, 14, 19) WORK_ORDER_ID,PG_DATE,MODEL_NA,DECIDE_DATE,SEWING_DATE,FORMING_DATE,BOTTOM_TIME,SCAN_DATE "
		//		+ "FROM DSID65 ";
		return null;
	}

	@Override
	protected String getCustomCountSQL() {
		// TODO Auto-generated method stub
//		return "SELECT count(*)"
//		+ "FROM DSID65 where "+State+" =trunc(sysdate)";
		return null;
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID65";
	}

	@Override
	protected String getTextBoxValue() {
		// TODO Auto-generated method stub
		return "aa";
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "paging";
	}

	@Override
	protected String getOrderby() {
		// TODO Auto-generated method stub
		String Ord = " ORDER BY PG_DATE DESC";
		return Ord;
	}

	@Override
	protected Combobox getcboColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Combobox getcboCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

}
