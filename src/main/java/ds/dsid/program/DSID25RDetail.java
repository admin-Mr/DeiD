package ds.dsid.program;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID25_LOG;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.OperationMode;

public class DSID25RDetail extends Detail{

	@Wire
	protected Div detail1;
	@Wire
	private Textbox TXT_REMAIN;
	@Wire
	private Label TXT_MODEL_NA1,TXT_GROUP_NO,TXT_EL_NO1,TXT_RE_DATE;
	@Wire
	private Button btnDeleteDetail;
	@Wire
	protected Listbox detailListbox;
	SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		detailComponentColumns.add(new ComponentColumn<String>("TXT_MODEL_NA1", "MODEL_NA",null , null, null,false));
		detailComponentColumns.add(new ComponentColumn<String>("TXT_GROUP_NO", "GROUP_NO", null, null, null,false));
		detailComponentColumns.add(new ComponentColumn<String>("TXT_EL_NO1", "EL_NO", null, null, null,false));
		detailComponentColumns.add(new ComponentColumn<String>("TXT_REMAIN", "REMAIN", null, null, null,false));
		detailComponentColumns.add(new ComponentColumn<String>("TXT_RE_DATE", "RE_DATE", null, null, null,false));
	
		detailComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		detailComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));

	}
	
	@Listen("onAfterRender = #detailListbox")
	public void onAfterRenderDetailListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {
			TXT_EL_NO1 = (Label) getParentWindow().getFellow("TXT_EL_NO1");
			TXT_MODEL_NA1 = (Label) getParentWindow().getFellow("TXT_MODEL_NA1");
		}
	}
	
	@Override
	protected Div getRootDiv() {
		// TODO Auto-generated method stub
		return detail1;
	}

	@Override
	public Class getDetailClass() {
		// TODO Auto-generated method stub
		return DSID25_LOG.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}

	@Override
	protected Listbox getDetailListbox() {
		// TODO Auto-generated method stub
		return detailListbox;
	}

	@Override
	protected ArrayList<String> getDetailKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> detailKey = new ArrayList<String>();
		detailKey.add("RE_DATE");
		detailKey.add("EL_NO");
		detailKey.add("GROUP_NO");
		detailKey.add("MODEL_NA");
		return detailKey;
	}

	@Override
	protected ArrayList<String> getDetailKeyValue(Object entityDetail) {
		// TODO Auto-generated method stub
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");	
		DSID25_LOG detail = (DSID25_LOG) entityDetail;
		ArrayList<String> detailValue = new ArrayList<String>();
		detailValue.add(format.format(detail.getRE_DATE()));
		detailValue.add(detail.getEL_NO());
		detailValue.add(detail.getGROUP_NO());
		detailValue.add(detail.getMODEL_NA());
		return detailValue;
	}

	@Override
	protected String getPagingIdDetail() {
		// TODO Auto-generated method stub
		return "pagingDetailCourse";
	}

	@Override
	protected String getDetailCreateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getDetailCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailUpdateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String Sql=" AND RE_DATE IN (SELECT MAX(RE_DATE) FROM DSID25 WHERE MODEL_NA=t.MODEL_NA)";
		
		return Sql;
	}

	@Override
	protected boolean beforeDetailSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean beforeDetailDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void resetEditAreaDetail(Object entityDetail) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object doSaveDefault(String columnName) {
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

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return null;
	}

}
