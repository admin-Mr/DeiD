package ds.dsid.program;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID04;
import ds.dsid.domain.DSID21;
import util.Common;
import util.ComponentColumn;
import util.Detail;
import util.Master;
import util.OperationMode;

public class DSID21MProgram extends Master {

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDelete, btnExport,
			btnCustomExr, btnBatdelete;
	@Wire
	private Textbox txtModel_na, txtItems, txtGr_no, txtGr_na, txtColor, txtEl_no, txtEl_na, txtSize_fd, txtNote,
			txtType, txtUp_user;
	@Wire
	private Datebox txtUp_date;
	String Errmessage = "";
	@Wire
	private Fileupload btnImport;

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());

		super.doFillListbox(0);

		masterComponentColumns.add(new ComponentColumn<String>(txtModel_na, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtItems, "ITEMS", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtType, "TYPE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtGr_no, "GR_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtGr_na, "GR_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtColor, "COLOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtEl_na, "EL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtEl_no, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtSize_fd, "SEZI_FD", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtNote, "NOTE", null, null, null));
		masterComponentColumns
				.add(new ComponentColumn<String>(txtUp_user, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(txtUp_date, "UP_DATE", new Date(), null, null));

		btnImport = (Fileupload) window.getFellow("btnImport");
		btnImport.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>() {
			@SuppressWarnings("unused")
			public void onEvent(UploadEvent event) throws Exception {
				String fileToRead = "";
				org.zkoss.util.media.Media media = event.getMedia();
				if (!media.getName().toLowerCase().endsWith(".xls")) {
					// "格式有誤！"
					Messagebox.show(Labels.getLabel("COMM.XLSFILE"));
					return;
				}
				System.out.println(" ----- fileToRead : " + fileToRead);
				InputStream input = null;
				media.isBinary();
				String sss = media.getFormat();
				input = media.getStreamData();// 獲得輸入流
				importFromExcel(input);
				
				ShowMessage();
			}
		});
	}

	public void importFromExcel(InputStream input) throws Exception {
		System.out.println("进入excel 读取内容");
		Connection conn = Common.getDbConnection();
		HSSFWorkbook wb = new HSSFWorkbook(input);
		PreparedStatement ps=null;
		ResultSet rs=null;
		// 型體
		// Model_na = txtMODEL_NA.getValue();
		String MODEL_NA = "", MT_USAGE = "", GR_NO = "", GR_NA = "", COLOR = "", EL_NO = "", EL_NA = "", SIZE_FD = "",
				NOTE = "";
		String TYPE = "";
		int ITEMS = 0;

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		String Upsql = "";

		try {
			conn.setAutoCommit(false); // 數據庫事務控制, 批量提交數據庫操作.

			for (int x = 1; x < sheet.getPhysicalNumberOfRows(); x++) {

				row = sheet.getRow(x);
				// 型體
				MODEL_NA = getCellValue(row.getCell(0)); 
//				System.out.println("型體"+MODEL_NA);
				if (MODEL_NA == null || "".equals(MODEL_NA)) {
					break;
				}
				
				ITEMS++;
				MT_USAGE = getCellValue(row.getCell(1)); // 使用部位
//				System.out.println("使用部位"+MT_USAGE);
				GR_NO = getCellValue(row.getCell(2)); // 部位
				GR_NA = getCellValue(row.getCell(3)); // 部位名稱
				COLOR = getCellValue(row.getCell(4)); // 顏色
				EL_NO = getCellValue(row.getCell(5)); // 材料編號
				EL_NA = getCellValue(row.getCell(6)); // 材料名稱
				SIZE_FD = getCellValue(row.getCell(7)); // 分段
				NOTE = getCellValue(row.getCell(8)); // 備註
				TYPE = getCellValue(row.getCell(9)); // 類型
			
				Upsql += " INTO DSID21 (MODEL_NA,ITEMS,MT_USAGE,TYPE,GR_NO,GR_NA,COLOR,EL_NO,EL_NA,SIZE_FD,NOTE,UP_USER,UP_DATE) VALUES('"
						+ MODEL_NA + "','" + ITEMS + "','" + MT_USAGE + "','" + TYPE + "','" + GR_NO + "','" + GR_NA
						+ "','" + COLOR + "','" + EL_NO + "','" + EL_NA + "','" + SIZE_FD + "','" + NOTE + "','"
						+ _userInfo.getAccount() + "',SYSDATE)";
			}
			
			if(SIZE_FD!=null || TYPE!=null || COLOR!=null){
				String Delsql = "DELETE DSID21 WHERE MODEL_NA='" + MODEL_NA + "'";
				System.out.println(" ----- 刪除 : " + Delsql);

				try {
					PreparedStatement pstm = conn.prepareStatement(Delsql);
					pstm.executeUpdate();
					pstm.close();
					conn.commit();
				} catch (Exception e) {
					Errmessage = "Delete false!"+e;
					e.printStackTrace();
					conn.rollback();
					return;
			}
			
			String InSql = " INSERT ALL " + Upsql + " SELECT * FROM DUAL";
			System.out.println(" ----- 匯入 : " + InSql);

			try {
				PreparedStatement pstm = conn.prepareStatement(InSql);
				pstm.executeUpdate();
				pstm.close();
				conn.commit();
			} catch (Exception e) {
				Errmessage = "Insert false!"+e;
				e.printStackTrace();
				conn.rollback();
				return;
			}
			}else{
				Messagebox.show("size分段、状态码、颜色,不能为空,请检查！！！！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			Errmessage="Input false!"+e;
			conn.rollback();
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
		}
	}

	private static String getCellValue(HSSFCell cell) {
		String cellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell) && DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date dt = DateUtil.getJavaDate(cell.getNumericCellValue());
					cellValue = sdf.format(dt);
				} else {
					try {
						BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
						cellValue = String.valueOf(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
					} catch (Exception ex) {
					}
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				cellValue = cellValue.trim();
				break;
			default:
				break;
			}
		}
		// System.out.println(">>>"+cellValue);
		return cellValue;
	}

	private void ShowMessage() {
		// TODO Auto-generated method stub
		if (Errmessage.length() > 0) {
			Messagebox.show(Labels.getLabel("DSID.MSG0175") + Errmessage);
		} else {
			Messagebox.show(Labels.getLabel("DSID.MSG0176"));
		}
		Errmessage = "";
	}

	// 匯出
	@Listen("onClick =#btnExport")
	public void onClickbtnExport(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MExport.zul", null, map);
	}

	// 批量刪除
	@Listen("onClick =#btnBatdelete")
	public void onClickbtnBatdelete(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MBatDelete.zul", null, map);
	}

	//
	@Listen("onClick =#btnCustomExr")
	public void onClickbtnCustomExr(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MProgram", this);
		Executions.createComponents("/ds/dsid/DSID21MCustomExr.zul", null, map);
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID21MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID21.class;
	}

	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}

	@Override
	protected Class getDetailClass(int indexDetail) {
		// TODO Auto-generated method stub
		return getDetailProgram(indexDetail).getDetailClass();
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		// TODO Auto-generated method stub
		return getArrDetailPrograms().get(indexDetail);
	}

	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub

	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("MODEL_NA");
		masterKey.add("ITEMS");
		return masterKey;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID21 entity = (DSID21) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getITEMS());

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
		String string = "";
		return string;
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
		DSID21 entity = (DSID21) entityMaster;
		txtModel_na.setValue(entity == null ? "" : entity.getMODEL_NA());
		txtItems.setValue(entity == null ? "" : entity.getITEMS());
		txtGr_no.setValue(entity == null ? "" : entity.getGR_NO());
		txtGr_na.setValue(entity == null ? "" : entity.getGR_NA());
		txtColor.setValue(entity == null ? "" : entity.getCOLOR());
		txtEl_no.setValue(entity == null ? "" : entity.getEL_NO());
		txtEl_na.setValue(entity == null ? "" : entity.getEL_NA());
		txtSize_fd.setValue(entity == null ? "" : entity.getSIZE_FD());
		txtNote.setValue(entity == null ? "" : entity.getNOTE());
		txtUp_user.setValue(entity == null ? "" : entity.getUP_USER());
		txtUp_date.setValue(entity == null ? null : entity.getUP_DATE());
		txtType.setValue(entity == null ? "" : entity.getTYPE());
		
//		if (entity == null || "".equals(entity)) {
//			txtType.setValue("");
//		} else {
//
//			// System.out.println(" ----- 類型判斷 ！" + entity.getTYPE());
//			switch (entity.getTYPE()) {
//			case "0":
//				txtType.setValue(Labels.getLabel("DSID.MSG0160"));
//				break;
//			case "1":
//				txtType.setValue(Labels.getLabel("DSID.MSG0082"));
//				break;
//			case "2":
//				txtType.setValue(Labels.getLabel("DSID.MSG0159"));
//				break;
//			case "3":
//				txtType.setValue(Labels.getLabel("DSID.MSG0177"));
//				break;
//			case "4":
//				txtType.setValue(Labels.getLabel("DSID.MSG0178"));
//				break;
//			case "5":
//				txtType.setValue(Labels.getLabel("DSID.MSG0102"));
//				break;
//			case "6":
//				txtType.setValue(Labels.getLabel("DSID.MSG0103"));
//				break;
//			case "7":
//				txtType.setValue(Labels.getLabel("DSID.MSG0159"));
//				break;
//			default:
//				txtType.setValue("/");
//				break;
//			}
//		}
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
		txtItems.setValue(AutoSeq());
	}

	private String AutoSeq() {
		// TODO Auto-generated method stub
		DSID21 entity = new DSID21();
		Connection conn = Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String items = "";

		String sql = "SELECT LPAD(NVL(MAX(items),0)+1,4,'0')items FROM DSID21 where model_no = '" + entity.getMODEL_NA()
				+ "'";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				items = rs.getString("items");
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
		}

		return items;
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID21";
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	// btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster,
	// btnDelete, btnImport, btnExport, btnCustomExr
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterReadMode(mapButton);
		btnQuery.setDisabled(false);
		btnEditMaster.setDisabled(false);
		btnCreateMaster.setDisabled(false);
		btnDelete.setDisabled(false);
		btnImport.setDisabled(false);
		btnExport.setDisabled(false);
		btnCustomExr.setDisabled(false);
		btnEditMaster.setDisabled(false);
		btnDelete.setDisabled(false);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnBatdelete.setDisabled(false);
		btnCustomExr.setDisabled(false);

		txtModel_na.setReadonly(true);
		txtItems.setReadonly(true);
		txtGr_no.setReadonly(true);
		txtGr_na.setReadonly(true);
		txtColor.setReadonly(true);
		txtEl_no.setReadonly(true);
		txtEl_na.setReadonly(true);
		txtSize_fd.setReadonly(true);
		txtNote.setReadonly(true);
		txtType.setReadonly(true);
		txtUp_user.setReadonly(true);
		txtUp_date.setReadonly(true);

	}

	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnQuery.setDisabled(true);
		btnEditMaster.setDisabled(true);
		btnCreateMaster.setDisabled(true);
		btnDelete.setDisabled(true);
		btnImport.setDisabled(true);
		btnExport.setDisabled(true);
		btnCustomExr.setDisabled(true);
		btnEditMaster.setDisabled(true);
		btnDelete.setDisabled(true);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnBatdelete.setDisabled(true);
		btnCustomExr.setDisabled(true);

		txtModel_na.setReadonly(false);
		txtItems.setReadonly(false);
		txtGr_no.setReadonly(false);
		txtGr_na.setReadonly(false);
		txtColor.setReadonly(false);
		txtEl_no.setReadonly(false);
		txtEl_na.setReadonly(false);
		txtSize_fd.setReadonly(false);
		txtNote.setReadonly(false);
		txtType.setReadonly(false);
		txtUp_user.setReadonly(false);
		txtUp_date.setReadonly(false);

	}
}
