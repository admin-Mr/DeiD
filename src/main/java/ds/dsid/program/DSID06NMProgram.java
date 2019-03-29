package ds.dsid.program;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.SessionScope;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID05;
import ds.dsid.domain.DSID06N;
import ds.dsid.domain.DSIDN06_01;
import ds.dsid.domain.DSIDN06_02;
import ds.dsid.domain.DSIDN08;
import util.Common;
import util.ComponentColumn;
import util.Master;
import util.OperationMode;

public class DSID06NMProgram extends Master {

	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txt_EL_NO_SQL, txt_MODEL_NA_SQL, txt_EL_CNAME_SQL, txt_PLACE_SQL, txt_MODEL_NA, txt_ITEM, txt_EL_NO,
			txt_EL_CNAME, txt_PLACE, txt_QTY, txt_NOTE, txt_CUPBOARD;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnDeleteMaster, btnQuery, btnImport, btnExport;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	String Errmessage = "";

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		masterComponentColumns.add(new ComponentColumn<String>(txt_MODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_ITEM, "ITEM", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_CNAME, "EL_CNAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_PLACE, "PLACE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_CUPBOARD, "CUPBOARD", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_NOTE, "NOTE", null, null, null));

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
				InputStream input = null;
				media.isBinary();
				String sss = media.getFormat();
				input = media.getStreamData();// 獲得輸入流
				importFromExcel(input);
			}
		});
	}

	public void importFromExcel(InputStream input) throws Exception {
		System.out.println("进入excel 读取内容");
		Connection conn = Common.getDbConnection();
		HSSFWorkbook wb = new HSSFWorkbook(input);

		try {
			ImportSheet0(wb, conn);// 主檔

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
		}

	}

	private void ImportSheet0(HSSFWorkbook wb, Connection Conn) throws Exception {
		System.out.println(">>>>>>>>>读取报表");
		// TODO Auto-generated method stub
		SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		String Sql = "", MODEL_NA = "";
		try {
			Conn.setAutoCommit(false);// 设置自动提交
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				System.out.println(">>>行数" + i);
				row = sheet.getRow(i);
				// 把从表中get到的值放入栏位
				String EL_NO = getCellValue(row.getCell(0));
				String EL_CNAME = getCellValue(row.getCell(1));
				MODEL_NA = getCellValue(row.getCell(2));
				String ITEM = "0000" + i;
				ITEM = ITEM.substring(ITEM.length() - 4);
				// 需要一个if判断主键是否一样与是否为空，是直接break跳出
				if ("".equals(MODEL_NA) || MODEL_NA == null) {
					break;
				}
				String PLACE = getCellValue(row.getCell(3));
				String CUPBOARD = getCellValue(row.getCell(4));
				String NOTE = getCellValue(row.getCell(5));

				Sql += "INTO DSID06N (MODEL_NA,ITEM,EL_NO,EL_CNAME,PLACE,UP_DATE,UP_USER,CUPBOARD,NOTE) " + "VALUES ('"
						+ MODEL_NA + "','" + ITEM + "','" + EL_NO + "','" + EL_CNAME + "','" + PLACE + "',TO_DATE('"
						+ Format.format(new Date()) + "','yyyy/MM/dd'),'" + _userInfo.getAccount() + "','"
						+ CUPBOARD + "','" + NOTE + "')";
			}
			// 導入前先刪除
			String DeSql = "DELETE DSID06N WHERE MODEL_NA='" + MODEL_NA + "'";
			System.out.println(">>>刪除>>>" + DeSql);
			try {
				PreparedStatement pstm = Conn.prepareStatement(DeSql);
				pstm.executeUpdate();
				pstm.close();
			} catch (Exception e) {
				Conn.rollback();
				e.printStackTrace();
			}

			// 将值添加到数据库表中
			Sql = "INSERT ALL " + Sql + " SELECT * FROM DUAL";
			System.out.println(">>>導入>>>" + Sql);
			try {
				PreparedStatement pstm = Conn.prepareStatement(Sql);
				pstm.executeUpdate();
				pstm.close();
			} catch (Exception e) {
				Conn.rollback();
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Errmessage.length() <= 0) {
			Conn.commit();
			Messagebox.show("文件匯入成功！！！");
		} else {
			Conn.rollback();
			Messagebox.show("文件匯入失敗！！！" + Errmessage);
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
				cellValue = cell.getStringCellValue().trim();
				break;

			case HSSFCell.CELL_TYPE_FORMULA:
				try {
					cellValue = cell.getStringCellValue();
				} catch (IllegalStateException e) {
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			default:
				break;
			}
		}
		// System.out.println(">>>"+cellValue);
		return cellValue;
	}

	private String GetQTY(String MODEL_NA, String EL_NO) {
		// TODO Auto-generated method stub
		Connection conn = Common.getDbConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String MT_QTY = "";
		String sql1 = "SELECT MT_QTY FROM DSID77 WHERE MODEL_NA='" + MODEL_NA + "' AND EL_NO='" + EL_NO + "'";
		// System.err.println(">>>>>"+sql1);
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				if (!"".equals(rs1.getString("MT_QTY")) && rs1.getString("MT_QTY") != null) {
					MT_QTY = rs1.getString("MT_QTY");
				} else {
					MT_QTY = "";
				}
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
		}

		return MT_QTY;
	}

	/**
	 * 必須複寫方法二 getRootWindow
	 */
	@Override
	protected Window getRootWindow() {
		return windowMaster;
	}

	/**
	 * 必須複寫方法三 getMasterClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMasterClass() {
		return DSID06NMProgram.class;
	}

	/**
	 * 必須複寫方法四 getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID06N.class;
	}

	/**
	 * 必須複寫方法五 getOperationMode
	 */
	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}

	/**
	 * 必須複寫方法六 getMasterKeyName
	 */
	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("ITEM");
		masterKey.add("MODEL_NA");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID06N entity = (DSID06N) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getITEM());
		masterKeyValue.add(entity.getMODEL_NA());
		return masterKeyValue;
	}

	/**
	 * 必須複寫方法九
	 */
	@Override
	protected String getWhereConditionals() {
		String strSQL = "";// " and MT_PONO is not null ";

		if (!txt_EL_CNAME_SQL.getValue().trim().isEmpty()) {
			strSQL += " and  EL_CNAME  like '%" + txt_EL_CNAME_SQL.getValue() + "%'";
		}
		if (!txt_PLACE_SQL.getValue().trim().isEmpty()) {
			strSQL += " and  PLACE  like '%" + txt_PLACE_SQL.getValue() + "%'";
		}

		if (!txt_MODEL_NA_SQL.getValue().trim().isEmpty()) {
			strSQL += " and  MODEL_NA  like '%" + txt_MODEL_NA_SQL.getValue() + "%'";
		}

		if (!txt_EL_NO_SQL.getValue().trim().isEmpty()) {
			strSQL += " and  EL_NO  like '%" + txt_EL_NO_SQL.getValue() + "%'";
		}

		return strSQL;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		DSID06N entity = (DSID06N) entityMaster;
		txt_MODEL_NA.setValue((entity == null ? "" : entity.getMODEL_NA()));
		txt_ITEM.setValue((entity == null ? "" : entity.getITEM()));
		txt_EL_NO.setValue((entity == null ? "" : entity.getEL_NO()));
		txt_EL_CNAME.setValue((entity == null ? "" : entity.getEL_CNAME()));
		txt_PLACE.setValue(entity == null ? "" : entity.getPLACE());
		txt_CUPBOARD.setValue(entity == null ? "" : entity.getCUPBOARD());
		txt_NOTE.setValue(entity == null ? "" : entity.getNOTE());
		txt_QTY.setValue(GetQTY(txt_MODEL_NA.getValue(), txt_EL_NO.getValue()));
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		// btnExport.setDisabled(false);

		txt_MODEL_NA.setReadonly(true);
		txt_ITEM.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_EL_CNAME.setReadonly(true);
		txt_PLACE.setReadonly(true);
		txt_QTY.setReadonly(true);

	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
	}

	/**
	 * 點擊新增按鍵時作業
	 */
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		// btnExport.setDisabled(true);

		txt_MODEL_NA.setReadonly(false);
		txt_ITEM.setReadonly(false);
		txt_EL_NO.setReadonly(false);
		txt_EL_CNAME.setReadonly(false);
		txt_PLACE.setReadonly(false);
		txt_QTY.setReadonly(true);

	}

	@Listen("onOK=#txt_EL_NO_SQL,#txt_EL_CNAME_SQL,#txt_PLACE_SQL,#txt_MODEL_NA_SQL")
	public void onOKPO_NOTextbox() {
		super.executeQuery();
	}

	@Listen("onClick = #btnShowPlace")
	public void onClickbtnShowPlace(Event event) throws Exception {

		String place = txt_PLACE.getText();
		String model_na = txt_MODEL_NA.getText();
		System.err.println(model_na + "位置：" + place);
		if (!"".equals(place)) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("parentWindow", windowMaster);
			map.put("PLACE", place);
			map.put("MODEL_NA", model_na);
			Executions.createComponents("/ds/dsid/DSIDN09M_Program_show.zul", null, map);
		} else {
			Messagebox.show("請確認改材料已設定位置！！", "Warning", Messagebox.OK, Messagebox.QUESTION);
		}
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
}
