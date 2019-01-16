package ds.dsid.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import util.MSMode;
import util.OpenWinCRUD;


public class DSIDN10M_Program extends OpenWinCRUD {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire private Datebox querystartdate,queryenddate;
	@Wire
	private Textbox txt_UNIQUEID ,txt_FU_ID    ,txt_MU         ,txt_PART_NAME,
					txt_VENDOR   ,txt_CP_NAME  ,txt_EL_NAME    ,txt_COLOR_CODE,
					txt_STATUS   ,txt_FU_IDSQL ,txt_MODEL_NAME ,txt_YIELD,txt_MODEL_NA;
					
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnExport,btnClear, btnPB_MUNODE,aaa;

	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		
	}
	
	@Listen("onClick = #btnExport")
	public void onClickbtnExport(Event event) throws Exception{
		
		String MODEL_NA=txt_MODEL_NA.getValue().trim();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String starttime=sdf.format(querystartdate.getValue());
		String endtime =sdf.format(queryenddate.getValue());
		DSIDN10M_ExcelUtil.ExcelExport( "出庫統計",MODEL_NA,starttime,endtime);

	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSIDN10M_Program.class;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected MSMode getMSMode() {
		// TODO Auto-generated method stub
		return MSMode.MASTER;
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
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap getReturnMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
