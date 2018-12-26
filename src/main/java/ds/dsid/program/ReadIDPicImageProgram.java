package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.SimpleDateFormat;

import ds.common.services.CRUDService;
import ds.dsid.domain.DSID10;
import ds.dsid.domain.DSID30_PIC;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.Master;
import util.OperationMode;

public class ReadIDPicImageProgram extends COMM_Master {

	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowMaster;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Textbox MODEL_NA, WORK_ORDER_ID, UP_USER;
	@Wire
	private Textbox URL1, URL2, URL3, URL4;
	@Wire
	private Datebox UP_DATE;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;

	private boolean Add = false;
	private static Connection Conn = null;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		masterComponentColumns.add(new ComponentColumn<String>(MODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(WORK_ORDER_ID, "WORK_ORDER_ID", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(URL1, "URL1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(URL2, "URL2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(URL3, "URL3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(URL4, "URL4", null, null, null));

		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService2");
		setCRUDService(CRUDService);

	}

	// create listbox裡面的cell，將資料及元件建立
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {

			MODEL_NA = (Textbox) windowMaster.getFellow("MODEL_NA");
			WORK_ORDER_ID = (Textbox) windowMaster.getFellow("WORK_ORDER_ID");
			URL1 = (Textbox) windowMaster.getFellow("URL1");
			URL2 = (Textbox) windowMaster.getFellow("URL2");
			URL3 = (Textbox) windowMaster.getFellow("URL3");
			URL4 = (Textbox) windowMaster.getFellow("URL4");
			UP_USER = (Textbox) windowMaster.getFellow("UP_USER");
			UP_DATE = (Datebox) windowMaster.getFellow("TGROUP5");

		}
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return ReadIDPicImageProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID30_PIC.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}

	@Override
	protected Class getDetailClass(int indexDetail) {
		// TODO Auto-generated method stub
		// return getDetailProgram(indexDetail).getDetailClass();
		return null;
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		// TODO Auto-generated method stub
		// return getArrDetailPrograms().get(indexDetail);
		return null;
	}

	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub

	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("WORK_ORDER_ID");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID30_PIC entity = (DSID30_PIC) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getWORK_ORDER_ID());
		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
	}

	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String Str = "";
		return Str;
	}

	@Override
	protected String getDetailConditionals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterCreateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterUpdateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterQueryZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getMasterCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID30_PIC entity = (DSID30_PIC) entityMaster;
		MODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		WORK_ORDER_ID.setValue(entity == null ? "" : entity.getWORK_ORDER_ID());
		URL1.setValue(entity == null ? "" : entity.getURL1());
		URL2.setValue(entity == null ? "" : entity.getURL2());
		URL3.setValue(entity == null ? "" : entity.getURL3());
		URL4.setValue(entity == null ? "" : entity.getURL4());
		UP_USER.setValue(entity == null ? "" : entity.getUP_USER());
		UP_DATE.setValue(entity == null ? null : entity.getUP_DATE());

	}

	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean beforeMasterDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
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
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void masterReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnEditMaster.setDisabled(false);
		btnDeleteMaster.setDisabled(false);

		MODEL_NA.setReadonly(true);
		WORK_ORDER_ID.setReadonly(true);
		URL1.setDisabled(true);
		URL2.setDisabled(true);
		URL3.setDisabled(true);
		URL4.setDisabled(true);
		UP_USER.setDisabled(true);
		UP_DATE.setDisabled(true);

	}

	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnEditMaster.setDisabled(true);
		btnDeleteMaster.setDisabled(true);

		MODEL_NA.setReadonly(false);
		WORK_ORDER_ID.setReadonly(false);
		URL1.setDisabled(false);
		URL2.setDisabled(false);
		URL3.setDisabled(false);
		URL4.setDisabled(false);
		UP_USER.setDisabled(false);
		UP_DATE.setDisabled(false);

	}

	@Listen("onClick = #btnSaveMaster")
	@Override
	public boolean onClickbtnSaveMaster(Event event) throws Exception {
		
		boolean isok = false;
		Conn = Common.getDB01Conn();
		ResultSet insertrs = null;
		PreparedStatement upDateps = null, inserps = null;
		
		String Modelna = MODEL_NA.getValue();
		String Workorder = WORK_ORDER_ID.getValue();
		String Url1 = URL1.getValue();
		String Url2 = URL2.getValue();
		String Url3 = URL3.getValue();
		String Url4 = URL4.getValue();
		String Upuser = _userInfo.getAccount();
		String Update = format.format(new Date());
		
		// 編輯狀態 該4個欄位為不可編輯狀態
		MODEL_NA.setReadonly(false);
		WORK_ORDER_ID.setReadonly(true);
		UP_USER.setReadonly(true);
		UP_DATE.setReadonly(true);
		
		if (!Add) {
			
			System.out.println(" ----- Up date ----- ");
			
			String updatesql = "update dsid30_Pic set url1 = '"+Url1+"', "
					+ "url2 = '"+Url2+"', url3 = '"+Url3+"', url4 = '"+Url4+"', "
					+ "up_user = '"+Upuser+"', up_date = to_date('"+Update+"','yyyy/mm/dd') where work_order_id like '%"+Workorder+"'";
			System.out.println(" ----- Update Sql: " + updatesql);
			
			try {
				upDateps = Conn.prepareStatement(updatesql);
				upDateps.executeUpdate();
				
				upDateps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Messagebox.show(" 資訊更新失敗 !");
				e.printStackTrace();
				
			} finally {
				Common.closeConnection(Conn);
			}
			super.executeQuery();
			super.masterCancel(event);
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"), "Information", Messagebox.OK, Messagebox.INFORMATION);
			return false;
		}
		
		String insertSql = "INSERT INTO DSID30_PIC(MODEL_NA, WORK_ORDER_ID, URL1, URL2, URL3, URL4, up_user, up_date)"
				+ " values('"+Modelna+"', '"+Workorder+"', '"+Url1+"', '"+Url2+"', '"+Url3+"', '"+Url4+"', '"+Upuser+"', '"+Update+"')";
		System.out.println(" ----- insert Sql: " + insertSql);
		
		try {
			inserps = Conn.prepareStatement(insertSql);
			inserps.executeUpdate();
			
			inserps.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Common.closeConnection(Conn);
		}

		super.executeQuery();
		super.masterCancel(event); // 取消新增或編輯, 回復瀏覽狀態
		Add = false;
		MODEL_NA.setValue("");
		WORK_ORDER_ID.setValue("");
		URL1.setValue("");
		URL2.setValue("");
		URL3.setValue("");
		URL4.setValue("");
		Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"), "Information", Messagebox.OK, Messagebox.INFORMATION);
		return true;
	}

	@Listen("onClick = #btnCreateMaster")
	@Override
	public void masterCreate(Event event) {
		Add = true;
		super.masterCreate(event);

	}

}
