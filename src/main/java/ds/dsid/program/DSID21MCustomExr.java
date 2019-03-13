package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.validation.constraints.Min;

import org.bouncycastle.jce.provider.JDKKeyFactory.RSA;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFDataFormat;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.hssf.util.Region;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.mysql.fabric.xmlrpc.base.Data;
import com.phloc.css.decl.ECSSSelectorCombinator;
import com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper;

import ds.common.services.CRUDService;
import javafx.css.PseudoClass;
import javafx.util.converter.PercentageStringConverter;
import net.sf.jasperreports.renderers.Graphics2DRenderable;
import oracle.net.aso.s;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

@SuppressWarnings("deprecation")
public class DSID21MCustomExr extends OpenWinCRUD {

	@Wire private Window windowMaster;
	@Wire private CRUDService CRUDService;
	@Wire private Button btnexport,btnExport2;
	@Wire private Listbox List_Model_na;
	@Wire private Datebox Odno_Date1, Odno_Date2;
	@Wire Checkbox Chbox;

	private Double Odno = 0.0, Group1_8sum; // 訂單總數
	private String Odate1 = "", Odate2 = ""; // 接單日期
	private String ifQueryColor = "";
	private Double Maxtool = 0.0, Mintool = 0.0; // 最大最小 tooling_size

	HSSFCellStyle style3_B = null;
	DecimalFormat format2 = new DecimalFormat("#.00000");
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		// doSearch();

		Connection conn = Common.getDbConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<String> Grnolist = new ArrayList<String>();
		Grnolist.add("");

		String sql = "select distinct MODEL_NA from dsid21 order by MODEL_NA asc";
		// System.out.println(" ----- 測試獲取 Grno : " + sql);

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Grnolist.add(rs.getString("MODEL_NA"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0133"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			Common.closeConnection(conn);
		}
		
		List_Model_na.setModel(new ListModelList<Object>(Grnolist));
	}

	@Listen("onClick = #btnExport2")
	public void onClickbtnExport2() throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		Date Start=Odno_Date1.getValue();
		Date End=Odno_Date2.getValue();
		String Modelna="";
		Boolean Up_date=false;
		if(Chbox.isChecked()){ // 判斷 使用者 是否選擇更新資料
			Up_date=true;
		}
		
		if(!"".equals(Start)&&Start!=null&&!"".equals(End)&&End!=null){
			String START=sdf.format(Start);
			String END=sdf.format(End);
			
			for (Listitem ltAll : List_Model_na.getItems()) {
				if (ltAll.isSelected()) {
					Modelna = ltAll.getValue().toString();
				}
			}
			DSID21_1RTask.ExcelExport(Modelna,START,END,Up_date);
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0204"));
		}
		
		
	}
	
	
	@Listen("onClick = #btnexport")
	public void onClickbtnexport() {

		HSSFWorkbook wb = new HSSFWorkbook();

		// 抓取 Listbox 文本框內選定的值 .
		String Modelna = "";
		Odate1 = format.format(Odno_Date1.getValue()); // 訂單日期
		Odate2 = format.format(Odno_Date2.getValue());

		for (Listitem ltAll : List_Model_na.getItems()) {
			if (ltAll.isSelected()) {
				Modelna = ltAll.getValue().toString();
			}
		}
		// 測試
		/*Odate1 = "2019/01/01";
		Odate2 = "2019/01/10";
		Modelna = "PEGASUS+35 ESS SU18 ID";*/
		// Modelna = "PEGASUS+35 MIN FA18 ID";
		
		//Messagebox.show(" 測試抓取文本資料 ↓ \n 型體名稱 : "+Modelna+"\n 接單日期 : "+Odate1+" - "+Odate2);

		try { // 部位名稱與型體名稱不能為空
			if (Modelna == null || "".equals(Modelna)) {
				Messagebox.show(Labels.getLabel("DSID.MSG0134"));
			} else {
				
				filterHeader(wb, Modelna, Odate1, Odate2); // 導出主體方法
				Messagebox.show(Labels.getLabel("DSID.MSG0135"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0136"));
		}

	}

	// 樣式設定
	private void filterHeader(HSSFWorkbook wb, String Modelna, String Odate1, String Odate2) throws SQLException{
		// TODO Auto-generated method stub

		// 字體
		HSSFFont font1 = wb.createFont();
		font1.setFontName("新細明體"); // 设置字體
		font1.setFontHeightInPoints((short) 12); // 设置字体高度
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 设置字體樣式

		HSSFFont font2 = wb.createFont();
		font2.setFontName("新細明體"); // 设置字體
		font2.setFontHeightInPoints((short) 10); // 设置字体高度

		// 標題格式
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFont(font1);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style1.setFillPattern((short) 0);
		style1.setWrapText(true);

		// 正文格式
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setFont(font2);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style2.setFillPattern((short) 0);
		style2.setWrapText(true);

		// 四周邊框 單元格類型為 百分比
		style3_B = wb.createCellStyle();
		style3_B = wb.createCellStyle();
		style3_B.setFont(font2);
		style3_B.setFillPattern((short) 0);
		style3_B.setWrapText(true);
		style3_B.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style3_B.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3_B.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3_B.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3_B.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		style3_B.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style3_B.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

		Connection conn = Common.getDbConnection();

		try {

			HSSFSheet sheet = wb.createSheet("Size "+Labels.getLabel("DSID.MSG0137"));
			HSSFSheet sheet2 = wb.createSheet("Group "+Labels.getLabel("DSID.MSG0137"));
			HSSFSheet sheet3 = wb.createSheet(Labels.getLabel("DSID.MSG0097")+Labels.getLabel("DSID.MSG0138"));
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			QueryOdno(conn, Modelna); // 查詢訂單總數
			QueryGroup1_8_Odno(conn, Modelna); // 查詢Group1 ~ 8 總訂單數
			
			// 時間記錄 ↓
			
			 long startTime = System.currentTimeMillis(); 
			 SetSheet1(conn, wb, sheet, style1, style2, Modelna); // 主體方法 
			 long endTime = System.currentTimeMillis(); 
			 float excTime = (float) (endTime-startTime) / 1000;
			 

		  /**
			* 用於回寫百分比數據至採購報表 List<List<String>>
			* 
			* Sheetlist2.get1 > List<String> > 各部位資料 			= 顏色名稱 - Gr_no - 顏色百分比(字符串結構)
			* Sheetlist2.get2 > List<String> > Group1 ~ 8 資料 	= 顏色名稱 - 顏色數量 - 顏色百分比(字符串結構)
			*/
			
			long startTime2 = System.currentTimeMillis(); 
			List<List<String>> Sheetlist2 = SetSheet2(conn, wb, sheet2, style1, style2, Modelna);// 主體方法 
			long endTime2 = System.currentTimeMillis(); float
			excTime2 = (float) (endTime2 - startTime2) / 1000;
			 
			/**
			 * Sheelist3.get1 > List<String> > 前面片 	= 顏色 / 數量 / 百分比
			 * Sheelist3.get2 > List<String> > 鞋帶 		= 顏色 / 數量 / 百分比
			 * Sheelist3.get3 > List<String> > 鞋舌布標 	= 顏色 / 數量 / 百分比
			 */
			long startTime3 = System.currentTimeMillis();
			List<List<String>> Sheetlist3 = SetSheet3(conn, wb, sheet3, style1, style2, Modelna); // 主體方法
			long endTime3 = System.currentTimeMillis();
			float excTime3 = (float) (endTime3 - startTime3) / 1000;
			
			
			float excTime4 = 0;
			if(Chbox.isChecked()){ // 判斷 使用者 是否選擇更新資料
				System.out.println(" 更新 ！ ");
				long startTime4 = System.currentTimeMillis();
				updateColorPerc(conn, Modelna, Sheetlist2, Sheetlist3);	// 数据更新 方法
				long endTime4 = System.currentTimeMillis();
				excTime4 = (float) (endTime4 - startTime4) / 1000;
			}
			
			

			/*System.err.println("执行时间："+excTime+"s");
			System.err.println("执行时间："+excTime2+"s");
			System.err.println("执行时间："+excTime3+"s");*/
			System.err.println("最終耗時："+(excTime+excTime2+excTime3+excTime4)+"s");

			wb.write(stream);
			Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");

			byte[] content = stream.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			// 儲存位置、名稱
			Filedownload.save(is, "application/xls", format1.format(date));
			is.close();
			stream.flush();
			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
		}
		System.err.println(" —————————— : 結束  : —————————— ");

	}

	/**
	 * 颜色更新方法
	 * @param modelna
	 * @param sheetlist2	各部位 / Group1~8 List
	 * @param sheetlist3	前面片 / 鞋帶 / 鞋舌布標 List 
	 * @throws SQLException 
	 */
	private void updateColorPerc(Connection conn, String modelna, List<List<String>> sheetlist2,
			List<List<String>> sheetlist3) throws SQLException {
		// TODO Auto-generated method stub
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List<String> Grlist = sheetlist2.get(0);	// 各部位數據
		List<String> Gr1_8list = sheetlist2.get(1);	// Group1 ~ 8 數據
		
		List<String> Vamplist = sheetlist3.get(0);	// 前面片數據
		List<String> Lacelist =  sheetlist3.get(1); // 鞋带数据
		List<String> Tonguelist =  sheetlist3.get(2); // 鞋舌布標数据
		
		for(String str : Grlist){
			String[] data = str.split("/");
			//System.out.println(" --- 顏色 : " + data[0] + " --- 部位 : " + data[1] + " --- 百分比 : " + data[2]);
			
			if(data[0] != null && data[1] != null && data[2] != null || !"".equals(data[0]) && !"".equals(data[1]) && !"".equals(data[2])){
				
				String sql = "update dsid04_1 set color_pre = "+data[2]+" where model_na like '%"+modelna+"' and color = '"+data[0]+"' and group_no = '"+data[1]+"' ";
				//System.out.println(" upDate 全部部位 Per : " + sql);
				
				/*try {
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					conn.rollback();
					Messagebox.show(" 各部位 Per數據更新失敗！");
				}*/
			}
		}
		
		for(String str : Gr1_8list){
			
		}
		
		for(String str : Vamplist){
			String[] data = str.split("/");
			//System.out.println(" --- 顏色 : " + data[0] + " --- 分段 : " + data[1] + " --- 百分比 : " + data[2]);
			String sql = "update dsid04_2 set color_pre = '"+data[2]+"' where MODEL_NA LIKE '%"+modelna+"' and color like '%"+data[0]+"' and el_na like '"+data[1]+"%'";
			//System.out.println(" ----- update Vamp : " + sql);
			
			/*try {
				ps = conn.prepareStatement(sql);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				conn.rollback();
				Messagebox.show(" 各部位 Per數據更新失敗！");
			}*/
		}
		
		for(String str : Lacelist){
			
			String[] data = str.split("/");
			
			if(data[0] != null && data[1] != null && data[2] != null || !"".equals(data[0]) && !"".equals(data[1]) && !"".equals(data[2])){
				
				String sql = "update dsid04_4 set color_pre = '"+data[2]+"' "
						+ "where model_na like '%PEGASUS+35 ESS SU18 ID' and color = '"+data[0]+"' and el_no = (select el_no from dsid21 where model_na like '%"+modelna+"' and gr_na like '%鞋帶%' and color = '"+data[0]+"')";
				//System.out.println(" ----- 鞋帶數據更新 : "+ sql);

			}
						
		}
	}

	/**
	 * 第一頁 打印 顏色尺碼統計
	 * 
	 * @param conn
	 *            數據連接
	 * @param style1
	 *            打印樣式
	 * @param style2
	 * @param Modelna
	 *            型體名稱
	 * @throws SQLException
	 */
	private void SetSheet1(Connection conn, HSSFWorkbook wb, HSSFSheet sheet, HSSFCellStyle style1,
			HSSFCellStyle style2, String Modelna) throws SQLException {

		System.out.println(" —————————— : 第一頁 開始 : —————————— ");

		HSSFRow row = null;
		HSSFCell cell = null;

		ResultSet rs = null, Wrs = null, Qdrs = null;
		PreparedStatement ps = null, Wps = null, Qdps = null;

		int irow = 4; // 起始行數
		int Getirow = irow; // 獲取起始行
		int SUMrow = 5; // Excel 公式行数
		int iCell = 1; // 起始列
		int Xdicell = 1;

		Map<Double, Object> CellMap = new HashMap<Double, Object>();
		Map<Double, Object> CellMap0 = new HashMap<Double, Object>();

		String Tableif = "1"; // 表格標題打印判斷值.
		// 標題設定
		Header1(sheet, style1, style2, row, cell, Modelna);

		// size 小表頭打印
		SetHeadLabel(conn, sheet, style2, row, irow, cell, Modelna, Tableif);

		// 根據最大,最小size獲取toolingsize 拼接字符串 SQL查詢使用
		String tooling = "";
		for (Double Min = Mintool; Min <= Maxtool; Min += 0.5) {
			if (Min < Maxtool) {
				tooling = tooling + Min + ",";
			} else {
				tooling = tooling + Min; // 最後一個不添加 , 號
			}
			CellMap.put(Min, iCell++); // Size碼 對應 Cell Map
			
			if(Min != 3.0 && Min != 13.0 && Min != 14.0 && Min != 15.0){
				CellMap0.put(Min, Xdicell++); // 氣墊 尺碼表格 Map`
			}
		}

		/*for(Entry<Double, Object> entry : CellMap0.entrySet()){
			System.out.println("Key :"+entry.getKey()+"\nValue :"+entry.getValue());
		}*/
		
		// 顏色名稱查詢
		ifQueryColor = "Vamp"; // 查詢前面片顏色名稱
		List<String> Colorlist = QueryColorName(conn, Modelna, ifQueryColor);

		//System.err.println("* —————————— ↓ 前面片 ↓ —————————— —————————— ↓ 前面片  ↓ —————————— —————————— ↓ 前面片  ↓ ——————————");

		for (int i = 0; i < Colorlist.size(); i++) {

			row = sheet.createRow(irow);

			// 顏色名稱
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Colorlist.get(i));

			// 邊框欄打印
			for (double Dan = Mintool; Dan <= Maxtool; Dan += 0.5) {
				int ice = (int) CellMap.get(Dan);
				cell = row.createCell((short) ice);
				cell.setCellStyle(style2);
				cell.setCellType(1);
				cell.setCellValue("");
			}

			String sql = "SELECT strcolor, sizerun, SUM(tolisize) tolisize FROM "
					+ " (SELECT substr(group1,1,3) strcolor, tooling_size AS sizerun, COUNT(tooling_size) tolisize "
					+ " FROM dsid01 WHERE to_number(tooling_size) IN (" + tooling + ") "
					+ " AND to_char(order_date,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2
					+ "' AND model_na like '%" + Modelna + "' " + " GROUP BY tooling_size, group1) "
					+ " GROUP BY strcolor, sizerun order BY strcolor, to_number(sizerun) asc";
			// System.out.println(" ----- 每個顏色數量查詢 : \n" + sql);

			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {

					String color = rs.getString("strcolor");
					double size = rs.getDouble("sizerun");
					int number = rs.getInt("tolisize");

					int Celll = 1;

					if (color == Colorlist.get(i) || Colorlist.get(i).equals(color)) {
						for (double Min = Mintool; Min <= Maxtool; Min += 0.5) {
							if (size - Min == 0.0) {

								Celll = (int) CellMap.get(Min);
								cell = row.createCell((short) Celll);
								cell.setCellStyle(style2);
								cell.setCellType(1);
								cell.setCellValue(Integer.valueOf(number));

								// System.err.println(" 列 Cell " + Celll + " 數量
								// : "+ number);

								cell = row.createCell((short) ((Maxtool - Mintool) * 2 + 2));
								cell.setCellStyle(style2);
								cell.setCellType(1);
								cell.setCellFormula("SUM(B" + (irow + 1) + ":" + getExcelLabel(Celll + 1) + (irow + 1) + ")");
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show(Labels.getLabel("DSID.MSG0139"));
			} finally {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				// Common.closeConnection(conn);
			}
			irow++;
		}

		// 底部統計
		row = sheet.createRow(irow);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0140"));

		for (int Min = 1; Min <= ((Maxtool - Mintool) * 2 + 2); Min++) {

			// int Mcell = (int) CellMap.get(Min);
			cell = row.createCell(Min);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellFormula(
					"SUM(" + getExcelLabel(Min + 1) + (Getirow + 1) + ":" + getExcelLabel(Min + 1) + (irow) + ")");
			// System.err.println("SUM("+getExcelLabel(Min+1)+(Getirow+1)+":"+getExcelLabel(Min+1)+(irow)+")");
		}
		
		//System.out.println(" 完成 ！");
		//System.err.println("* —————————— ↑ 前面片 ↑ —————————— —————————— ↑ 前面片 ↑ —————————— —————————— ↑ 前面片 ↑ ——————————");
		
		//System.err.println("* —————————— ↓ 鞋帶  ↓ —————————— —————————— ↓ 鞋帶  ↓ —————————— —————————— ↓ 鞋帶  ↓ ——————————");

		// size 小表頭打印
		Tableif = "2"; // 表格標題打印判斷值.
		irow += 5; // 間隔行數設定
		SetHeadLabel(conn, sheet, style2, row, irow, cell, Modelna, Tableif);
		irow += 2;
		int getiRow = irow + 1;
		for (int i = 0; i < Colorlist.size(); i++) {
			// System.out.println(" irow : " + irow);
			row = sheet.createRow(irow); // 小表頭佔用兩行

			// 邊框欄打印
			for (double Dan = Mintool; Dan <= Maxtool; Dan += 0.5) {
				int ice = (int) CellMap.get(Dan);
				cell = row.createCell((short) ice);
				cell.setCellStyle(style2);
				cell.setCellType(1);
				cell.setCellValue("");
			}

			// 顏色名稱
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Colorlist.get(i));

			String sql = "SELECT COUNT(*) numbers, LEFT_SIZE_RUN FROM ("
					+ "SELECT (CASE INSTR(MODEL_NA,'W ') WHEN 1 THEN TO_NUMBER(LEFT_SIZE_RUN)-1.5 ELSE TO_NUMBER(LEFT_SIZE_RUN) END) LEFT_SIZE_RUN "
					+ "FROM DSID01 WHERE MODEL_NA LIKE '%" + Modelna + "' AND GROUP1 LIKE '%" + Colorlist.get(i) + "%' "
					+ "AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2 + "'"
					+ ")GROUP BY LEFT_SIZE_RUN ORDER BY TO_NUMBER(LEFT_SIZE_RUN)";
			// System.out.println(" ----- W or M Select : "+sql);

			try {
				Wps = conn.prepareStatement(sql);
				Wrs = Wps.executeQuery();

				while (Wrs.next()) {

					int numBer = Wrs.getInt("numbers"); // 數量
					double leftSize = Wrs.getDouble("LEFT_SIZE_RUN"); // Size

					int MapCell = 0;

					for (Entry<Double, Object> vo : CellMap.entrySet()) {
						if (leftSize == vo.getKey()) {

							MapCell = (int) vo.getValue();

							cell = row.createCell((short) MapCell);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue(numBer);
						}
					}

					cell = row.createCell((short) ((Maxtool - Mintool) * 2 + 2));
					cell.setCellStyle(style2);
					cell.setCellType(1);
					cell.setCellFormula("SUM(B" + (irow + 1) + ":" + getExcelLabel(MapCell + 1) + (irow + 1) + ")");
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show(Labels.getLabel("DSID.MSG0141"));
			} finally {
				if (Wrs != null)
					Wrs.close();
				if (Wps != null)
					Wps.close();
				// Common.closeConnection(conn);
			}
			irow++;
		}

		// 底部統計
		row = sheet.createRow(irow);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0140"));

		for (int Min = 1; Min <= ((Maxtool - Mintool) * 2 + 2); Min++) {

			// int Mcell = (int) CellMap.get(Min);
			cell = row.createCell(Min);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellFormula(
					"SUM(" + getExcelLabel(Min + 1) + getiRow + ":" + getExcelLabel(Min + 1) + (irow) + ")");
			// System.err.println("SUM("+getExcelLabel(Min+1)+(Getirow+1)+":"+getExcelLabel(Min+1)+(irow)+")");
		}

		//System.out.println(" 完成 ！");
		//System.err.println("* —————————— ↑ 鞋帶 ↑ —————————— —————————— ↑ 鞋帶 ↑ —————————— —————————— ↑ 鞋帶 ↑ ——————————");
		
		System.err.println("* —————————— ↓ 鞋墊  ↓ —————————— —————————— ↓ 鞋墊  ↓ —————————— —————————— ↓ 鞋墊  ↓ ——————————");

		/**
		 * 氣墊統計, 有合併統計的尺碼. 13.0 + 13.5 = 13.0 倆數量相疊加, 以下同理 14.0 + 14.5 = 14.0
		 * 15.0 + 15.5 = 15.0
		 */

		// size 小表頭打印
		Tableif = "3"; // 表格標題打印判斷值.
		irow += 5; // 間隔行數設定
		getiRow = irow + 1;
		SetHeadLabel(conn, sheet, style2, row, irow, cell, Modelna, Tableif);
		irow += 2;

		row = sheet.createRow(irow);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID01M.QTY"));

		int Mins = 1; // 空单元格打印
		for (double Min = Mintool; Min <= Maxtool; Min += 0.5) {
			if (Min != 13.5 && Min != 14.5 && Min != 15.5) { // 跳過 該三個尺碼 不打印
				cell = row.createCell(Mins);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				Mins++;
			}
		}
		
		String sql = "SELECT COUNT(*) numbers, TOOLING_SIZE from dsid01 where model_na like '%" + Modelna + "' "
				+ "AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2 + "' "
				+ "group by TOOLING_SIZE ORDER BY TO_NUMBER(TOOLING_SIZE)";
		//System.out.println(" ----- 氣墊數量查詢 : " + sql);

		try {
			Qdps = conn.prepareStatement(sql);
			Qdrs = Qdps.executeQuery();
			int Values = 0;
			int MapCell = 0;
			
			while (Qdrs.next()) {
				
				
				int numBer = Qdrs.getInt("numbers"); // 数量
				double toolSize = Qdrs.getDouble("TOOLING_SIZE"); // Size
				
				if(toolSize == 3.0 || "3.0".equals(toolSize)){
					Values += numBer;
					
				}else if(toolSize == 13.0 || "13.0".equals(toolSize)){
					Values += numBer;
					
				}else if(toolSize == 14.0 || "14.0".equals(toolSize)){
					Values += numBer;
					
				}else if(toolSize == 15.0 || "15.0".equals(toolSize)){
					Values += numBer;
				}
				
				MapCell = 0;

				for (Entry<Double, Object> entry : CellMap0.entrySet()) {
					if (toolSize == entry.getKey()) {
						MapCell = (int) entry.getValue();

							cell = row.createCell((short) MapCell);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue(numBer + Values);
							MapCell++;
							Values = 0;
							//jsDouble = 0.0;
						//}
					}
				}
			}
			
			cell = row.createCell((short) ((Maxtool - Mintool) * 2 - 2 ));
			cell.setCellStyle(style2);
			cell.setCellType(1);
			cell.setCellFormula("SUM(B" + (irow + 1) + ":" + getExcelLabel((MapCell)) + (irow + 1) + ")");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0142"));
		} finally {
			if (Qdrs != null)
				Qdrs.close();
			if (Qdps != null)
				Qdps.close();
			// Common.closeConnection(conn);
		}
		System.out.println(" 完成 ！");
		System.err.println("* —————————— ↑ 鞋墊 ↑ —————————— —————————— ↑ 鞋墊 ↑ —————————— —————————— ↑ 鞋墊 ↑ ——————————");
		
		System.out.println(" —————————— : 第一頁 結束 : —————————— ");
	}

	/**
	 * 第二頁 打印 各Group 數量/百分比
	 * 
	 * @param conn
	 * @param style1
	 * @param style2
	 * @param modelna
	 * @throws SQLException
	 */
	private List<List<String>> SetSheet2(Connection conn, HSSFWorkbook wb, HSSFSheet sheet2, HSSFCellStyle style1,
			HSSFCellStyle style2, String Modelna) throws SQLException {
		// TODO Auto-generated method stub

		System.out.println(" —————————— : 第二頁 開始 : —————————— ");

		HSSFRow row = sheet2.createRow(0);
		HSSFCell cell = row.createCell(0);

		ResultSet rs = null, rs2 = null, rs3 = null, rs4 = null;
		PreparedStatement ps = null, ps2 = null, ps3 = null, ps4 = null;

		int irow = 0, Myrow = 3;
		int icell = 0;
		String Grna = "";
		String Andsql2 = null;

		List<String> Grouplist = new ArrayList<String>();
		List<String> Group1_8 = new ArrayList<String>();
		List<List<String>> Sheet2list = new ArrayList<List<String>>();

		row = sheet2.createRow(irow);
		row.setHeightInPoints(20);

		for (int i = 0; i <= 2; i++) { // 表頭打印

			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			if (i == 0) {
				cell.setCellValue("Group "+Labels.getLabel("DSID.MSG0094"));
			} else if (i == 2) {
				cell.setCellValue("");
				sheet2.addMergedRegion(new Region((short) irow, (short) (icell - 2), (short) irow, (short) icell));
			} else {
				cell.setCellValue("");
			}
			icell++;
		}
		
		//System.err.println("* —————————— ↓ 子表各部位查詢 ↓ —————————— —————————— ↓ 子表各部位查詢 ↓ —————————— —————————— ↓ 子表各部位查詢 ↓ ——————————");

		// 預先固定打印 60行 之後不再新建行
		for (int i = 1; i <= 60; i++) {
			row = sheet2.createRow(i);
		}

		irow++;
		icell = 0;
		int Mrow = irow;
		row = sheet2.createRow(irow);

		for (int i = 1; i <= QuerGroupNumber(conn, Modelna); i++) {

			// 查詢 部位全稱
			String sql = "SELECT group" + i + "_na FROM dsid10 WHERE model_nas like '%" + Modelna + "'";
			//System.out.println(" ----- Group 名称查询 : " + sql);

			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();

				if (rs.next()) {
					Grna = rs.getString("Group" + i + "_na");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show(Labels.getLabel("DSID.MSG0143"));
			} finally {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				// Common.closeConnection(conn);
			}

			// 打印Group各個部位名稱
			row = sheet2.getRow(irow);
			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellValue(Grna);
			cell.setCellStyle(style2);
			sheet2.addMergedRegion(new Region((short) Mrow, (short) icell, (short) Mrow, (short) (icell + 2)));

			cell = row.createCell((short) (icell + 1));
			cell.setCellType(1);
			cell.setCellStyle(style2);

			cell = row.createCell((short) (icell + 2));
			cell.setCellType(1);
			cell.setCellStyle(style2);

			// Group 標題打印
			row = sheet2.getRow((irow + 1));

			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellValue("Group" + i + "-"+Labels.getLabel("DSID.MSG0095")+":");
			cell.setCellStyle(style2);
			sheet2.addMergedRegion(
					new Region((short) (Mrow + 1), (short) icell, (short) (Mrow + 1), (short) (icell + 1)));

			cell = row.createCell((short) (icell + 1));
			cell.setCellType(1);
			cell.setCellStyle(style2);

			cell = row.createCell((short) (icell + 2));
			cell.setCellType(1);
			cell.setCellValue(Odno);
			cell.setCellStyle(style2);

			// 固定標題
			row = sheet2.getRow((irow + 2));

			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0096"));
			cell.setCellStyle(style2);

			cell = row.createCell((short) (icell + 1));
			cell.setCellType(1);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
			cell.setCellStyle(style2);

			cell = row.createCell((short) (icell + 2));
			cell.setCellType(1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
			cell.setCellStyle(style2);

			// 顏色數量查詢
			if ("HO18 PEGASUS 35 SHIELD LOW ID" == Modelna || "HO18 PEGASUS 35 SHIELD LOW ID".equals(Modelna)
					|| "HO18 PEGASUS 35 SHIELD MID ID" == Modelna || "HO18 PEGASUS 35 SHIELD MID ID".equals(Modelna)) {
				if (i == 3 || i == 5) {
					Andsql2 = "group" + i + " ";
				} else {
					Andsql2 = "substr(group" + i + ",1,3)";
				}
			} else {
				Andsql2 = "substr(group" + i + ",1,3)";
			}

			String sql2 = "SELECT grname , sum(grsum) grsum FROM (" + "SELECT " + Andsql2
					+ " grname, Count(*) grsum FROM dsid01 " + "where to_char(order_date,'YYYY/MM/DD') BETWEEN '"
					+ Odate1 + "' AND '" + Odate2 + "' AND model_na like '%" + Modelna + "' group BY group" + i + " "
					+ ") group BY grname ORDER BY grsum asc";
			//System.out.println(" ----- 各顏色數量查詢 : " + sql2);

			try {
				ps2 = conn.prepareStatement(sql2);
				rs2 = ps2.executeQuery();

				while (rs2.next()) {

					String Grname = rs2.getString("grname");
					Double Grsum = rs2.getDouble("grsum");

					// 配色、數量、百分比 列印
					row = sheet2.getRow((Mrow + Myrow));

					cell = row.createCell((short) icell);
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue(Grname);

					cell = row.createCell((short) (icell + 1));
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue(Grsum);

					cell = row.createCell((short) (icell + 2));
					cell.setCellType(1);
					cell.setCellStyle(style3_B);
					cell.setCellFormula(Grsum+"/"+Odno); // 顏色數量 / 總接單數 = 百分比
					//cell.setCellFormula(Grsum + "/" + Odno); 
					
					Myrow++;

					// 抓取百分比数据 回写
					String GroupData = Grname + "/" + "GROUP"+i + "/" + Double.valueOf((format2.format(Grsum / Odno)));
					Grouplist.add(GroupData);
					// System.out.println(" Group list : " + GroupData);
				}
				Myrow = 3;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show("Group "+Labels.getLabel("DSID.MSG0137")+" "+Labels.getLabel("DSID.MSG0139")+" ！");
			} finally {
				if (rs2 != null)
					rs2.close();
				if (ps2 != null)
					ps2.close();
				// Common.closeConnection(conn);
			}

			icell += 4;
			if (icell % 20 == 0) { // 1組5個Group統計表格, 20列后 換下一行第二組開始
				icell = 0;
				Mrow += 30; // 控制上下兩排表格間隔
				row = sheet2.getRow(Mrow);
				irow = Mrow;
			}
		}
		
		//System.out.println(" 完成 ！");
		//System.err.println("* —————————— ↑ 子表各部位查詢 ↑ —————————— —————————— ↑ 子表各部位查詢 ↑ —————————— —————————— ↑ 子表各部位查詢 ↑ ——————————");
		
		//System.err.println("* —————————— ↓ Group1 ~ 8 ↓ —————————— —————————— ↓ Group1 ~ 8 ↓ —————————— —————————— ↓ Group1 ~ 8 ↓ ——————————");

		
		/**
		 * Group1 ~ 8 统计表格打印
		 */
		irow += 24; // 間隔至55行 打印 其他表格
		int onrow = irow;
		//System.out.println(" irow : " + irow);

		row = sheet2.createRow(irow);
		cell.setCellType(1);
		cell = row.createCell(0);
		cell.setCellStyle(style2);
		cell.setCellValue("Group1 - Group8 "+Labels.getLabel("DSID.MSG0098"));
		sheet2.addMergedRegion(new Region((short) irow, (short) 0, (short) irow, (short) 2));

		for (int i = 1; i <= 2; i++) { // 打印空单元格
			cell.setCellType(1);
			cell = row.createCell(i);
			cell.setCellStyle(style2);
			cell.setCellValue("");
		}
		irow++;

		String sql3 = "SELECT COUNT(*) AMOUNT,NGROUP FROM ("
				+ "SELECT CASE LENGTH(GROUP8) WHEN 7 THEN GROUP8 ELSE SUBSTR(GROUP8,0,10) END NGROUP " + "FROM DSID01 "
				+ "WHERE TO_CHAR(UP_DATE,'YYYY/MM/DD') between '" + Odate1 + "' and '" + Odate2
				+ "' AND MODEL_NA LIKE '%" + Modelna + "'" + ") GROUP BY NGROUP ORDER BY NGROUP";
		//System.out.println(" ----- Group1 - 8 : " + sql3);

		try {
			ps3 = conn.prepareStatement(sql3);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {

				String nGroup = rs3.getString("NGROUP"); // 顏色
				Double Amount = rs3.getDouble("AMOUNT"); // 數量

				// System.err.println(" ----- group1 ~ 8 " + i++);
				row = sheet2.createRow(irow);
				cell = row.createCell((short) 0);
				cell.setCellType(1);
				cell.setCellValue(nGroup);
				cell.setCellStyle(style2);

				cell = row.createCell((short) 1);
				cell.setCellType(1);
				cell.setCellValue(Amount);
				cell.setCellStyle(style2);

				cell = row.createCell((short) 2);
				cell.setCellType(1);
				cell.setCellValue(Amount / Group1_8sum);
				cell.setCellStyle(style3_B);

				irow++;
				String Data = nGroup + "/" + Amount + "/" + Double.valueOf((format2.format(Amount / Odno)));
				//System.out.println(" Data Group1 ~ 8 : " + Data);
				Group1_8.add(Data);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(" Group1 ~ 8 "+Labels.getLabel("DSID.MSG0144")+" ！");
		} finally {
			if (rs3 != null)
				rs3.close();
			if (ps3 != null)
				ps3.close();
			// Common.closeConnection(conn);
		}
		//System.out.println(" 完成 ！");
		//System.err.println("* —————————— ↑ Group1 ~ 8 ↑ —————————— —————————— ↑ Group1 ~ 8 ↑ —————————— —————————— ↑ Group1 ~ 8 ↑ ——————————");
		
		//System.err.println("* —————————— ↓ 勾勾大小查分統計 ↓ —————————— —————————— ↓ 勾勾大小查分統計 ↓ —————————— —————————— ↓ 勾勾大小查分統計 ↓ ——————————");

		/**
		 * 勾勾大小拆分查询
		 */
		row = sheet2.getRow(onrow);
		icell = 4;

		String sql4 = "SELECT group1_na, group2_na, group3_na, group4_na, group5_na, group6_na, group7_na, group8_na, group9_na, group10_na, group11_na, group12_na, group13_na, group14_na, group15_na, group16_na, group17_na, group18_na, group19_na, group20_na "
				+ "FROM dsid10 WHERE model_nas like '%" + Modelna + "'";
		//System.out.println(" —————————— 勾勾查詢 : " + sql4);

		try {
			ps4 = conn.prepareStatement(sql4);
			rs4 = ps4.executeQuery();

			if (rs4.next()) {
				
				for (int i = 1; i <= QuerGroupNumber(conn, Modelna); i++) {
					
					if (rs4.getString("group" + i + "_na") == "勾勾大小(MIN版)"
							|| "勾勾大小(MIN版)".equals(rs4.getString("group" + i + "_na"))) {
						System.err.println("3");
						String GGname = rs4.getString("group" + i + "_na");

						// 下架型體 需要表格, 後期維護可去除 <勾勾大小拆分查询> 整個模塊
						SetGGtable(GGname, wb, sheet2, style1, style2, Modelna, onrow, icell, GGname);

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0145"));
		}
		//System.out.println(" 完成 ！");
		//System.err.println("* —————————— ↑ 勾勾大小拆分統計 ↑ —————————— —————————— ↑ 勾勾大小拆分統計 ↑ —————————— —————————— ↑ 勾勾大小拆分統計 ↑ ——————————");

		Sheet2list.add(Grouplist);
		Sheet2list.add(Group1_8);

		System.out.println(" —————————— : 第二頁 結束 : —————————— ");
		return Sheet2list;
	}

	private List<List<String>> SetSheet3(Connection conn, HSSFWorkbook wb, HSSFSheet sheet3, HSSFCellStyle style1,
			HSSFCellStyle style2, String Modelna) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println(" —————————— : 第三頁 開始 : —————————— ");

		HSSFRow row = sheet3.createRow(0);
		HSSFCell cell = row.createCell(0);

		ResultSet rs = null, rs2 = null, rs3 = null;
		PreparedStatement ps = null, ps2 = null, ps3 = null;

		int irow = 0; // 初始行數
		int icell = 0; // 初始列數
		int Maxcell = 0, Mincell = 0;
		int Maxrow = 0, Minrow = 0;
		String type = ""; // 类型判斷
		String FDsize = ""; // 分段变量
		String Grno = ""; // Group_no
		String Judge = null; // 前后气垫,港宝,布标 类型判断变量
		String[] FData = null; // 分段数组
		List<String> Colornamelist = new ArrayList<String>();
		
		List<List<String>> listdata = new ArrayList<List<String>>();
		List<String> Vamplist = new ArrayList<String>(); // 前面片
		List<String> Lacelist = new ArrayList<String>(); // 鞋带
		List<String> Tonguelist = new ArrayList<String>(); // 鞋舌布標
		
		type = "1"; // 前面片
		// 分段信息獲取, 及小表頭打印
		FData = QueryParagraphData(sheet3, row, cell, conn, irow, icell, style1, style2, Modelna, type);

		irow = 3; // 表頭占兩格
		icell = 1; // 列一格

		row = sheet3.getRow(irow - 1);
		for (int i = 0; i < FData.length; i++) { // 分段表頭打印

			// System.out.println(" FData : " + FData[i]);

			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(FData[i]);
			icell++;
		}

		cell = row.createCell((short) icell);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0146"));
		icell++;

		icell = 0;

		//System.err.println("* —————————— ↓ 前面片 ↓ —————————— —————————— ↓ 前面片 ↓ —————————— —————————— ↓ 前面片 ↓ ——————————");
		// 轉印10A 顏色
		//  
		if (Modelna == "PEGASUS+35 MIN FA18 ID" || "PEGASUS+35 MIN FA18 ID".equals(Modelna) || Modelna == "PEGASUS+35 ESS SU18 ID" || "PEGASUS+35 ESS SU18 ID".equals(Modelna) )
		{

			ifQueryColor = "Vamp";
			Colornamelist = QueryColorName(conn, Modelna, ifQueryColor); // 抓取配色名稱

			for (int i = 0; i < Colornamelist.size(); i++) { // 颜色打印

				if (Colornamelist.get(i) == "10A" || "10A".equals(Colornamelist.get(i))) {

					row = sheet3.createRow(irow);
					cell = row.createCell((short) icell);
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue(Colornamelist.get(i));

				}
			}

			/** 查詢個Size分段 顏色數量 */
			// 特定顏色 轉印型體 分段顏色數量查詢
			String Color = "";
			if (Modelna == "PEGASUS+35 MIN FA18 ID" || "PEGASUS+35 MIN FA18 ID".equals(Modelna)) {
				Color = "73P";
			} else {
				Color = "85Z";
			}

			row = sheet3.createRow(irow + 1);
			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Color);

			icell++;

			for (int i = 0; i < FData.length; i++) {

				// 拆分 拼接分段size 字符串
				String[] Data = FData[i].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}
				
				int union = 1; // 控制上下 换位打印
				String sql = "SELECT COUNT(*)AS counts FROM dsid01 " + "WHERE group1 = '" + Color
						+ "' AND to_number(tooling_size) IN (" + FDsize
						+ ") AND to_char(order_date,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2
						+ "'  AND model_na like '%" + Modelna + "%' " + "UNION ALL "
						+ "SELECT COUNT(*)AS qvsum FROM dsid01 " + "WHERE group1 != '" + Color
						+ "' AND to_number(tooling_size) IN (" + FDsize
						+ ") AND to_char(order_date,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2
						+ "'  AND model_na like '%" + Modelna + "%'";
				//System.out.println(" ----- 轉印10A : " + sql);

				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();

					while (rs.next()) {

						Double Count = rs.getDouble("counts"); // 指定配色數量

						if (union == 1) {

							row = sheet3.getRow(irow + 1);
							cell = row.createCell((short) icell);
							cell.setCellStyle(style3_B);
							cell.setCellType(1);
							cell.setCellFormula(Count + "/" + Odno);
							union++;
							// 收集數據 返回 顏色 / 尺碼 / 百分比
							String pecr = "10A" +"/"+ FData[i] +"/"+ Double.valueOf(format2.format(Count/Odno));
							//System.out.println(" 10A : " + Count+"/"+Odno);
							Vamplist.add(pecr);
						} else {

							// System.out.println(" irow icell 2 : " + (irow+1) +" --- "+ icell);
							row = sheet3.getRow(irow);
							cell = row.createCell((short) icell);
							cell.setCellStyle(style3_B);
							cell.setCellType(1);
							cell.setCellFormula(Count + "/" + Odno);

							union = 1;
							// 收集數據 返回 顏色 / 尺碼 / 百分比
							String pecr = Color +"/"+ FData[i] +"/"+ Double.valueOf(format2.format(Count/Odno));
							Vamplist.add(pecr);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0147"));
				} finally {
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					// Common.closeConnection(conn);
				}
				icell++;
				FDsize = "";
			}

			row = sheet3.getRow(irow);
			cell = row.createCell((short) icell);
			cell.setCellStyle(style3_B);
			cell.setCellType(1);
			cell.setCellFormula("SUM(B" + ((irow + 1)) + ":" + getExcelLabel(icell) + "" + ((irow + 1)) + ")");
			irow++;

			row = sheet3.getRow(irow);
			cell = row.createCell((short) icell);
			cell.setCellStyle(style3_B);
			cell.setCellType(1);
			cell.setCellFormula("SUM(B" + ((irow + 1)) + ":" + getExcelLabel(icell) + "" + ((irow + 1)) + ")");

			/**
			 * 非轉印 顏色打印
			 */
		} else {

			ifQueryColor = "Vamp"; // 傳入前面片判斷
			Colornamelist = QueryColorName(conn, Modelna, ifQueryColor); // 抓取配色名稱

			for (int i = 0; i < Colornamelist.size(); i++) { // 颜色打印
				
				// 打印顏色名稱
				row = sheet3.createRow(irow);
				cell = row.createCell((short) icell);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(Colornamelist.get(i));

				icell = 1;
				
				for (int f = 0; f < FData.length; f++) {

					// 拆分 拼接分段size 字符串
					String[] Data = FData[f].split("-");
					Double Min = Double.valueOf(Data[0]);
					Double Max = Double.valueOf(Data[1]);
					for (; Min <= Max; Min += 0.5) {
						if (Min < Max) {
							FDsize = FDsize + Min + ",";
						} else {
							FDsize = FDsize + Min;
						}
					}
					
					String sql = "SELECT group1, COUNT(*) tooling FROM dsid01 " + "WHERE group1 LIKE '"
							+ Colornamelist.get(i) + "%' AND to_number(tooling_size) IN (" + FDsize + ") "
							+ "AND to_char(order_date,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2 + "' "
							+ "AND model_na like '%" + Modelna + "' GROUP BY group1";
					//System.out.println(" ----- 非轉印百分比數量查詢 : " + sql);
					
					try {
						ps = conn.prepareStatement(sql);
						rs = ps.executeQuery();
						
						if(rs.next()){
							
							Double sizesum = rs.getDouble("tooling");
							
							cell = row.createCell((short) (icell));
							cell.setCellStyle(style3_B);
							cell.setCellType(1);
							cell.setCellFormula(sizesum+"/"+Odno);
							icell++;
							
							// 收集數據 返回 顏色 / 尺碼 / 百分比
							String Perc = Colornamelist.get(i) +"/"+ FData[i] +"/"+ Double.valueOf(format2.format(sizesum/Odno));
							//System.out.println(Colornamelist.get(i) +"/"+ FData[f] +"/"+ format2.format(sizesum/Odno));
							Vamplist.add(Perc);
							
						}else{
							// 无資料打印空單元格
							cell = row.createCell((short) (icell));
							cell.setCellStyle(style3_B);
							cell.setCellType(1);
							cell.setCellValue("/");
							icell++;
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show(Labels.getLabel("DSID.MSG0148"));
					} finally {
						if (rs != null)
							rs.close();
						if (ps != null)
							ps.close();
						// Common.closeConnection(conn);
					}
					FDsize = ""; // 分段清空 .
				}
				
				row = sheet3.getRow(irow);
				cell = row.createCell((short) icell);
				cell.setCellStyle(style3_B);
				cell.setCellType(1);
				cell.setCellFormula("SUM(B" + (irow+1) + ":" + getExcelLabel(icell) + "" + (irow+1) + ")");
				
				Maxcell = icell;
				icell = 0;
				irow++;
			}

			row = sheet3.createRow(irow);
			
			cell = row.createCell((short) (icell));
			cell.setCellStyle(style3_B);
			cell.setCellType(1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0149"));
			icell++;
			
			for(int i = 1; i <= Maxcell; i++){
				//System.out.println(" Maxcell : " + Maxcell +" i : " +i+" cell : "+ icell);
				
				cell = row.createCell((short) icell);
				cell.setCellStyle(style3_B);
				cell.setCellType(1);
				cell.setCellFormula("SUM(" + getExcelLabel((icell+1)) + 4 + ":" + getExcelLabel((icell+1)) + "" + (irow) + ")");
				icell++;
			}
		}
		Maxcell = 0;
		ifQueryColor = "";
		//System.out.println(" 完成 ！ ");
		//System.err.println("* —————————— ↑ 前面片 ↑ —————————— —————————— ↑ 前面片 ↑ —————————— —————————— ↑ 前面片 ↑ ——————————");
		
		//System.err.println("* —————————— ↓ 鞋帶 ↓ —————————— —————————— ↓ 鞋帶 ↓ —————————— —————————— ↓ 鞋帶 ↓ ——————————");
		type = "2"; // 鞋带
		irow += 4;
		icell = 0;
		
		// 分段信息獲取, 及小表頭打印
		FData = QueryParagraphData(sheet3, row, cell, conn, irow, icell, style1, style2, Modelna, type);
		
		irow ++;
		row = sheet3.createRow(irow);
		
		cell = row.createCell((short) icell);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0150"));
		icell++;
		
		for (int i = 0; i < FData.length; i++) { // 分段表頭打印

			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(FData[i]);
			icell++;
		}
		
		cell = row.createCell((short) icell);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0146"));
		irow++;
		Maxcell = icell;
		Minrow = irow;
		
		ifQueryColor = "Lace"; // 傳入鞋帶判斷
		Colornamelist = QueryColorName(conn, Modelna, ifQueryColor); // 抓取配色名稱
		Grno = getGroupno(conn, Modelna, ifQueryColor);
		
		for(int i = 0; i < Colornamelist.size(); i++){
			
			// 打印顏色名稱
			icell = 0;
			row = sheet3.createRow(irow);
			//System.out.println("  irow : " + irow + " icell :" + icell);
			
			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Colornamelist.get(i));
			icell++;
			
			for (int f = 0; f < FData.length; f++) {
				
				
				// 拆分 拼接分段size 字符串
				String[] Data = FData[f].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}
				
				String sql = "SELECT COUNT(*) numbers FROM ("
								+ "SELECT (CASE INSTR(MODEL_NA,'W ') WHEN 1 THEN TO_NUMBER(LEFT_SIZE_RUN)-1.5 ELSE TO_NUMBER(LEFT_SIZE_RUN) END) LEFT_SIZE_RUN "
								+ "FROM DSID01 WHERE MODEL_NA LIKE '%"+Modelna+"%' AND "+Grno+" LIKE '%"+Colornamelist.get(i)+"' "
								+ "AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+Odate1+"' AND '"+Odate2+"' "
							+ ") where to_number(LEFT_SIZE_RUN) in ("+FDsize+")";
				
				//System.out.println(" ----- 鞋帶百分比 數量查詢 : " + sql);
				
				try {
					ps2 = conn.prepareStatement(sql);
					rs2 = ps2.executeQuery();
					
					if(rs2.next()){
						Double numBer = rs2.getDouble("numbers");
						
						cell = row.createCell((short) icell);
						cell.setCellType(1);
						cell.setCellStyle(style3_B);
						cell.setCellFormula(numBer+"/"+Odno);
						icell++;
						
						//数据收集返回, 颜色/尺码/百分比
						Lacelist.add(Colornamelist.get(i) + "/" + FData[f] + "/" + Double.valueOf(format2.format(numBer/Odno)));
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0151"));
				} finally {
					if (rs2 != null)
						rs2.close();
					if (ps2 != null)
						ps2.close();
					// Common.closeConnection(conn);
				}
				FDsize = "";
			}
			
			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(B" + (irow+1) + ":" + getExcelLabel(icell) + "" + (irow+1) + ")");
			icell++;
			
			irow++;
		}
		
		icell = 0;
		row = sheet3.createRow(irow);
		
		cell = row.createCell((short) (icell));
		cell.setCellStyle(style3_B);
		cell.setCellType(1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0149"));
		icell++;
		
		for(int i = 1; i <= Maxcell; i++){
			//System.out.println(" Maxcell : " + Maxcell +" i : " +i+" cell : "+ icell);
			
			cell = row.createCell((short) icell);
			cell.setCellStyle(style3_B);
			cell.setCellType(1);
			cell.setCellFormula("SUM(" + getExcelLabel((icell+1)) + Minrow + ":" + getExcelLabel((icell+1)) + "" + (irow) + ")");
			icell++;
		}
		//System.out.println(" 完成 ！ ");
		//System.err.println("* —————————— ↑ 鞋帶 ↑ —————————— —————————— ↑ 鞋帶 ↑ —————————— —————————— ↑ 鞋帶 ↑ ——————————");
		
		//System.err.println("* —————————— ↓ 鞋舌布標 ↓ —————————— —————————— ↓ 鞋舌布標 ↓ —————————— —————————— ↓ 鞋舌布標 ↓ ——————————");
		
		icell++;
		Maxcell = icell;
		Maxrow = Minrow;
		row = sheet3.getRow(Maxrow - 2);
		
		cell = row.createCell((short) (icell + 1));
		cell.setCellStyle(style2);
		cell.setCellType(1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0152"));
		
		cell = row.createCell((short) (icell + 2));
		cell.setCellStyle(style2);
		cell.setCellType(1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0014"));
		
		cell = row.createCell((short) (icell + 3));
		cell.setCellStyle(style2);
		cell.setCellType(1);
		cell.setCellValue(Odno);
		Mincell = icell + 3;
		
		row = sheet3.getRow(Maxrow - 1);
		
		cell = row.createCell((short) (icell + 1));
		cell.setCellStyle(style2);
		cell.setCellType(1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0150"));
		
		// 分段信息獲取, 及小表頭打印
		type = "6"; // 鞋舌布標
		FData = QueryParagraphData(sheet3, row, cell, conn, irow, icell, style1, style2, Modelna, type);
		int Mcell = 2;
		for (int i = 0; i < FData.length; i++) { // 分段表頭打印

			cell = row.createCell((short) (icell + Mcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(FData[i]);
			Mcell++;
		}
		
		cell = row.createCell((short) (icell + Mcell));
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0146"));
		
		Maxcell = icell + Mcell;
		
		ifQueryColor = "Tongue"; // 傳入鞋舌布標判斷
		Colornamelist = QueryColorName(conn, Modelna, ifQueryColor); // 抓取配色名稱
		Grno = getGroupno(conn, Modelna, ifQueryColor); // Group_no 抓取
		
		for(int i = 0; i < Colornamelist.size(); i++){
			
			// 打印顏色名稱
			Mcell = 1;
			row = sheet3.getRow(Maxrow);
			//System.out.println("  irow : " + irow + " icell :" + icell);
			
			cell = row.createCell((short) (icell + Mcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Colornamelist.get(i));
			Mcell++;
			
			for (int f = 0; f < FData.length; f++) {
				
				// 拆分 拼接分段size 字符串
				String[] Data = FData[f].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}
				
				String sql = "SELECT count(*) numbers FROM ( "
								+ "SELECT (CASE INSTR(MODEL_NA,'W ') WHEN 1 THEN TO_NUMBER(LEFT_SIZE_RUN)-1.5 ELSE TO_NUMBER(LEFT_SIZE_RUN) END) LEFT_SIZE_RUN "
								+ "FROM DSID01 WHERE MODEL_NA LIKE '%"+Modelna+"%' AND "+Grno+" LIKE '%"+Colornamelist.get(i)+"' "
								+ "AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+Odate1+"' AND '"+Odate2+"'"
							+ ") where to_number(LEFT_SIZE_RUN) in ("+FDsize+")";
				//System.out.println(" ----- 鞋舌數量查詢 : " + sql);
				
				try {
					
					ps3 = conn.prepareStatement(sql);
					rs3 = ps3.executeQuery();
					
					if(rs3.next()){
						
						Double numBer = rs3.getDouble("numbers");
						
						cell = row.createCell((short) (icell + Mcell));
						cell.setCellType(1);
						cell.setCellStyle(style3_B);
						cell.setCellFormula(numBer+"/"+Odno);
						
						Tonguelist.add(Colornamelist.get(i) +"/"+ FData[f] +"/"+ Double.valueOf(format2.format(numBer/Odno)));
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if (rs3 != null)
						rs3.close();
					if (ps3 != null)
						ps3.close();
					// Common.closeConnection(conn);
				}
				Mcell++;
				FDsize = "";
			}
			
			cell = row.createCell((short) (icell + Mcell));
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(" + getExcelLabel(Mincell) + "" + (Maxrow+1) + ":" + getExcelLabel(icell + Mcell) + "" + (Maxrow+1) + ")");
			
			Maxrow++;
		}
		
		row = sheet3.getRow(Maxrow);
		cell = row.createCell((short) (icell + 1));
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0149"));
		
		for(;Mincell <= (Maxcell + 1); Mincell++){

			cell = row.createCell((short) (Mincell-1));
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(" + getExcelLabel(Mincell) + (Minrow+1) + ":" + getExcelLabel(Mincell) + Maxrow + ")");
			
		}
		
		// 数据回收. 
		listdata.add(Vamplist);
		listdata.add(Lacelist);
		listdata.add(Tonguelist);
		
		//System.out.println(" 完成 ！ ");
		//System.err.println("* —————————— ↑ 鞋舌布標 ↑ —————————— —————————— ↑ 鞋舌布標 ↑ —————————— —————————— ↑ 鞋舌布標 ↑ ——————————");
		
		//System.err.println("* —————————— ↓ 氣墊、港寶、布標 ↓ —————————— —————————— ↓ 氣墊、港寶、布標 ↓ —————————— —————————— ↓ 氣墊、港寶、布標 ↓ ——————————");
		
		ResultSet rs4 = null, rs5 = null, rs6 = null, rs7 = null;
		PreparedStatement ps4 = null, ps5 = null, ps6 = null, ps7 = null;
		
		// 行列歸零
		Mincell = 0;
		Maxcell = 0;
		irow += 4;	// 間隔 三行 開始打印下一個表格
		icell = 0;
		Minrow = 0;
		Maxcell = 0;
		
		
		// 查询是否存在 後掌气垫数据
		Judge = "3";
		String FDdata = Querydata(conn, Modelna, Judge); // 获取分段信息
		
		if(FDdata != null){  // 存在
			
			// 前掌氣墊
			row = sheet3.createRow(irow);
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
			sheet3.addMergedRegion(new Region((short)irow, (short) icell, (short)irow, (short) (icell+2)));
			icell++;
			
			for(int i = 0; i < 2; i++){
				cell = row.createCell((short) (icell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				icell++;
			}
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
			icell++;
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
			icell++;
			irow++; 
			Maxcell = icell;
			Minrow = irow;
			
			FData = FDdata.split("/"); // 查分尺码分段 字符串
			
			for(int i = 0; i < FData.length; i++){
				
				// 拆分 拼接分段size 字符串
				String[] Data = FData[i].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}
				
				row = sheet3.createRow(irow);
				icell = 0;
				
				cell = row.createCell((short) (icell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(Labels.getLabel("DSID.MSG0100")+"Ms　" + FData[i]);
				sheet3.addMergedRegion(new Region((short)irow, (short) icell, (short)irow, (short) (icell+2)));
				icell++;
				
				for(int a = 0; a < 2; a++){
					cell = row.createCell((short) (icell));
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue("");
					icell++;
				}
				
				// 查询分段數量 及計算百分比
				String sql0 = "select count(*) numbers from dsid01 where to_number(tooling_size) in ("+FDsize+") and model_na like '%"+Modelna+"' and TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+Odate1+"' and '"+Odate2+"'";
				//System.out.println(" ----- 查询前脚氣墊數量 : " + sql0);
				
				try {
					ps4 = conn.prepareStatement(sql0);
					rs4 = ps4.executeQuery();
					
					if(rs4.next()){
						
						Double Number = rs4.getDouble("numbers");
						
						cell = row.createCell((short) (icell));
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(Number);
						icell++;
						
						cell = row.createCell((short) (icell));
						cell.setCellType(1);
						cell.setCellStyle(style3_B);
						cell.setCellFormula(Number+"/"+Odno);
						icell++;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0153"));
				} finally {
					if (rs4 != null)
						rs4.close();
					if (ps4 != null)
						ps4.close();
					// Common.closeConnection(conn);
				}
				irow++;
				FDsize = "";
			}
			
			row = sheet3.createRow(irow);
			icell = 0;
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0154"));
			sheet3.addMergedRegion(new Region((short)irow, (short) icell, (short)irow, (short) (icell+2)));
			icell++;
			
			for(int a = 0; a < 2; a++){
				cell = row.createCell((short) (icell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				icell++;
			}
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellFormula("SUM(" + getExcelLabel(icell+1) + Minrow + ":" + getExcelLabel(icell+1) + irow + ")");
			icell++;
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(" + getExcelLabel(icell+1) + Minrow + ":" + getExcelLabel(icell+1) + irow + ")");
			icell++;
			FDdata = "";
		
		}
		
		
		Maxrow = Minrow - 1; // 右侧表格行数 
		Maxcell = Maxcell + 4; // 右侧表格 间隔5列
		Mincell = Maxcell;

		// 查询是否存在 後掌气垫数据
		Judge = "4";
		FDdata = Querydata(conn, Modelna, Judge); // 获取分段信息
		
		if(FDdata != null){

			FData = FDdata.split("/"); // 查分尺码分段 字符串
			
			// 後掌氣墊
			row = sheet3.getRow(Maxrow);
		
			cell = row.createCell((short) (Maxcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
			sheet3.addMergedRegion(new Region((short)Maxrow, (short) Maxcell, (short)Maxrow, (short) (Maxcell+2)));
			Maxcell++;
			
			for(int a = 0; a < 2; a++){
				cell = row.createCell((short) (Maxcell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				Maxcell++;
			}
		
			cell = row.createCell((short) (Maxcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
			Maxcell++;
			
			cell = row.createCell((short) (Maxcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
			Maxcell++;
			Maxrow++; 
			
			for(int i = 0; i < FData.length; i++){
				
				// 拆分 拼接分段size 字符串
				String[] Data = FData[i].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}
				
				row = sheet3.getRow(Maxrow);
				Maxcell = Mincell;
				
				cell = row.createCell((short) (Maxcell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(Labels.getLabel("DSID.MSG0101")+"Ms　" + FData[i]);
				sheet3.addMergedRegion(new Region((short)Maxrow, (short) Maxcell, (short)Maxrow, (short) (Maxcell+2)));
				Maxcell++;

				for(int a = 0; a < 2; a++){
					cell = row.createCell((short) (Maxcell));
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue("");
					Maxcell++;
				}

				// 查询分段數量 及計算百分比
				String sql0 = "select count(*) numbers from dsid01 where to_number(tooling_size) in ("+FDsize+") and model_na like '%"+Modelna+"' and TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+Odate1+"' and '"+Odate2+"'";
				//System.out.println(" ----- 查询後脚氣墊數量 : " + sql0);
				
				try {
					ps5 = conn.prepareStatement(sql0);
					rs5 = ps5.executeQuery();
					
					if(rs5.next()){
						
						Double Number = rs5.getDouble("numbers");
						
						cell = row.createCell((short) (Maxcell));
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(Number);
						Maxcell++;
						
						cell = row.createCell((short) (Maxcell));
						cell.setCellType(1);
						cell.setCellStyle(style3_B);
						cell.setCellFormula(Number+"/"+Odno);
						Maxcell++;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0155"));
				} finally {
					if (rs5 != null)
						rs5.close();
					if (ps5 != null)
						ps5.close();
					// Common.closeConnection(conn);
				}
				
				Maxrow++;
				Maxcell = Mincell;
				FDsize = "";
			}
			
			row = sheet3.getRow(Maxrow);
			
			cell = row.createCell((short) (Mincell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0154"));
			sheet3.addMergedRegion(new Region((short)Maxrow, (short) Mincell, (short)Maxrow, (short) (Mincell+2)));
			Mincell++;
			
			for(int a = 0; a < 2; a++){
				cell = row.createCell((short) (Mincell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				Mincell++;
			}
			
			cell = row.createCell((short) (Mincell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellFormula("SUM(" + getExcelLabel(Mincell+1) + Minrow + ":" + getExcelLabel(Mincell+1) + Maxrow + ")");
			Mincell++;
			
			cell = row.createCell((short) (Mincell));
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(" + getExcelLabel(Mincell+1) + Minrow + ":" + getExcelLabel(Mincell+1) + Maxrow + ")");
			Mincell++;
			FDdata = "";
		}
		
		
		/*System.err.println(" irow : " + irow );
		System.err.println(" Maxrow : " + Maxrow );
		System.err.println(" Minrow : " + Minrow );
		System.err.println(" icell : " + icell );
		System.err.println(" Maxcell : " + Maxcell );
		System.err.println(" Mincell : " + Mincell );*/
		
		irow += 4; // 间隔4行, 打印下一个表格
		icell = 0; // 重置列值
		
		// 查询是否存在 港宝数据
		Judge = "5";
		FDdata = Querydata(conn, Modelna, Judge); // 获取分段信息
		
		if(FDdata != null){
			
			// 港宝
			row = sheet3.createRow(irow);
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
			sheet3.addMergedRegion(new Region((short)irow, (short) icell, (short)irow, (short) (icell+2)));
			icell++;
			
			for(int i = 0; i < 2; i++){
				cell = row.createCell((short) (icell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				icell++;
			}
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
			icell++;
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
			icell++;
			irow++; 
			Maxcell = icell;
			Minrow = irow;
			
			FData = FDdata.split("/"); // 查分尺码分段 字符串
			
			for(int i = 0; i < FData.length; i++){
				
				// 拆分 拼接分段size 字符串
				String[] Data = FData[i].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}
				
				row = sheet3.createRow(irow);
				icell = 0;
				
				cell = row.createCell((short) (icell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(Labels.getLabel("DSID.MSG0102")+"Ms　" + FData[i]);
				sheet3.addMergedRegion(new Region((short)irow, (short) icell, (short)irow, (short) (icell+2)));
				icell++;
				
				for(int a = 0; a < 2; a++){
					cell = row.createCell((short) (icell));
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue("");
					icell++;
				}
				
				String sql0 = "select count(*) numbers from dsid01 where to_number(tooling_size) in ("+FDsize+") and model_na like '%"+Modelna+"' and TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+Odate1+"' and '"+Odate2+"'";
				//System.out.println(" ----- 查询港寶數量 : " + sql0);
				
				try {
					ps6 = conn.prepareStatement(sql0);
					rs6 = ps6.executeQuery();
					if(rs6.next()){
						
						Double Number = rs6.getDouble("numbers");
						
						cell = row.createCell((short) (icell));
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(Number);
						icell++;
						
						cell = row.createCell((short) (icell));
						cell.setCellType(1);
						cell.setCellStyle(style3_B);
						cell.setCellFormula(Number+"/"+Odno);
						icell++;
						
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0156"));
				} finally {
					if (rs6 != null)
						rs6.close();
					if (ps6 != null)
						ps6.close();
					// Common.closeConnection(conn);
				}
				
				irow++;
				FDsize = "";
			}
			
			row = sheet3.createRow(irow);
			icell = 0;
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0154"));
			sheet3.addMergedRegion(new Region((short)irow, (short) icell, (short)irow, (short) (icell+2)));
			icell++;
			
			for(int a = 0; a < 2; a++){
				cell = row.createCell((short) (icell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				icell++;
			}
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellFormula("SUM(" + getExcelLabel(icell+1) + Minrow + ":" + getExcelLabel(icell+1) + irow + ")");
			icell++;
			
			cell = row.createCell((short) (icell));
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(" + getExcelLabel(icell+1) + Minrow + ":" + getExcelLabel(icell+1) + irow + ")");
			icell++;
		}
		
		// 查询是否存在 布標数据
		Judge = "8";
		FDdata = Querydata(conn, Modelna, Judge); // 获取分段信息
		
		if(FDdata != null){
			
			Maxrow = Minrow - 1; // 右侧表格行数 
			Maxcell = Maxcell + 4; // 右侧表格 间隔5列
			Mincell = Maxcell;
			
			FData = FDdata.split("/"); // 查分尺码分段 字符串
			
			// 布標
			row = sheet3.getRow(Maxrow);
		
			cell = row.createCell((short) (Maxcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
			sheet3.addMergedRegion(new Region((short)Maxrow, (short) Maxcell, (short)Maxrow, (short) (Maxcell+2)));
			Maxcell++;
			
			for(int a = 0; a < 2; a++){
				cell = row.createCell((short) (Maxcell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				Maxcell++;
			}
		
			cell = row.createCell((short) (Maxcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
			Maxcell++;
			
			cell = row.createCell((short) (Maxcell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
			Maxcell++;
			Maxrow++; 
			
			for(int i = 0; i < FData.length; i++){
				
				// 拆分 拼接分段size 字符串
				String[] Data = FData[i].split("-");
				Double Min = Double.valueOf(Data[0]);
				Double Max = Double.valueOf(Data[1]);
				for (; Min <= Max; Min += 0.5) {
					if (Min < Max) {
						FDsize = FDsize + Min + ",";
					} else {
						FDsize = FDsize + Min;
					}
				}

				row = sheet3.getRow(Maxrow);
				Maxcell = Mincell;
				
				cell = row.createCell((short) (Maxcell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(Labels.getLabel("DSID.MSG0103")+"Ms　" + FData[i]);
				sheet3.addMergedRegion(new Region((short)Maxrow, (short) Maxcell, (short)Maxrow, (short) (Maxcell+2)));
				Maxcell++;

				for(int a = 0; a < 2; a++){
					cell = row.createCell((short) (Maxcell));
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue("");
					Maxcell++;
				}
				
				// 查询分段數量 及計算百分比
				String sql0 = "select count(*) numbers from dsid01 where to_number(tooling_size) in ("+FDsize+") and model_na like '%"+Modelna+"' and TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+Odate1+"' and '"+Odate2+"'";
				//System.out.println(" ----- 查询布標數量 : " + sql0);
				
				try {
					ps7 = conn.prepareStatement(sql0);
					rs7 = ps7.executeQuery();
					
					if(rs7.next()){
						
						Double Number = rs7.getDouble("numbers");
						
						cell = row.createCell((short) (Maxcell));
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(Number);
						Maxcell++;
						
						cell = row.createCell((short) (Maxcell));
						cell.setCellType(1);
						cell.setCellStyle(style3_B);
						cell.setCellFormula(Number+"/"+Odno);
						Maxcell++;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0157"));
				}
				
				Maxrow++;
				Maxcell = Mincell;
				FDsize = "";
				
			}
			row = sheet3.getRow(Maxrow);
			
			cell = row.createCell((short) (Mincell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0154"));
			sheet3.addMergedRegion(new Region((short)Maxrow, (short) Mincell, (short)Maxrow, (short) (Mincell+2)));
			Mincell++;
			
			for(int a = 0; a < 2; a++){
				cell = row.createCell((short) (Mincell));
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				Mincell++;
			}
			
			cell = row.createCell((short) (Mincell));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellFormula("SUM(" + getExcelLabel(Mincell+1) + Minrow + ":" + getExcelLabel(Mincell+1) + Maxrow + ")");
			Mincell++;
			
			cell = row.createCell((short) (Mincell));
			cell.setCellType(1);
			cell.setCellStyle(style3_B);
			cell.setCellFormula("SUM(" + getExcelLabel(Mincell+1) + Minrow + ":" + getExcelLabel(Mincell+1) + Maxrow + ")");
			Mincell++;
			FDdata = "";
		}
		//System.out.println(" 完成 ！ ");
		//System.err.println("* —————————— ↑ 氣墊、港寶、布標 ↑ —————————— —————————— ↑ 氣墊、港寶、布標 ↑ —————————— —————————— ↑ 氣墊、港寶、布標 ↑ ——————————");
		
		System.out.println(" —————————— : 第三頁 結束 : —————————— ");
		return listdata;
	}

	/**
	 * 
	 * @param modelna	型體變量
	 * @param judge 	判断變量
	 * @throws SQLException 
	 */
	private String Querydata(Connection conn, String Modelna, String Judge) throws SQLException {
		// TODO Auto-generated method stub
		
		if(Judge == "3"){
			Judge = "前掌氣墊";
		}else if(Judge == "4"){
			Judge = "後掌氣墊";
		}else if(Judge == "5"){
			Judge = "港寶";
		}else if(Judge == "8"){
			Judge = "布標";
		}
		
		String Judgestr = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String sql = "select * from dsid21 where gr_na like '%"+Judge+"%' and model_na = '"+Modelna+"'";
		//System.out.println(" ----- 查詢四小表分段及數據 : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if (!rs.next()) {
				
				return null;
			} else {
				Judgestr = rs.getString("SIZE_FD");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0158"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}
		return Judgestr;
	}

	/**
	 * 打印表頭 及 返回段落資料
	 * 
	 * @throws SQLException
	 */
	private String[] QueryParagraphData(HSSFSheet sheet3, HSSFRow row, HSSFCell cell, Connection conn, int irow,
			int icell, HSSFCellStyle style1, HSSFCellStyle style2, String Modelna, String type) throws SQLException {
		// TODO Auto-generated method stub

		//System.out.println(" 單純的粧 ! ");
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String[] Data = new String[20];

		if(type != "6" || !"6".equals(type)){ // 鞋舌標單獨打印表頭 只抓取分段信息
			
			String Tabelname = "";
			
			switch (type) {
			case "1":
				Tabelname = Labels.getLabel("DSID.MSG0082");
				break;
			case "2":
				Tabelname = Labels.getLabel("DSID.MSG0159");
				break;
			default:
				Tabelname = Labels.getLabel("DSID.MSG0160");
				break;
			}
			
			if(type == "1" || "1".equals(type)){ // 只打印一次 總表頭
				row = sheet3.createRow(irow);
				row.setHeightInPoints(18);

				cell = row.createCell((short) icell);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID.MSG0161"));
				sheet3.addMergedRegion(new Region((short) irow, (short) icell, (short) irow, (short) (icell + 2)));

				for (int i = 1; i <= 2; i++) {

					cell = row.createCell((short) i);
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellValue("");

				}
				irow++;
			}

			row = sheet3.createRow(irow);
			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Tabelname);

			cell = row.createCell((short) (icell + 1));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0014"));

			cell = row.createCell((short) (icell + 2));
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Odno);

			irow++;

			row = sheet3.createRow(irow);

			cell = row.createCell((short) icell);
			cell.setCellType(1);
			cell.setCellStyle(style2);
			cell.setCellValue(Labels.getLabel("DSID.MSG0150"));
		}
		

		// 抓取型體分段 資料
		String sql = "select distinct size_fd from dsid21 where model_na like '%" + Modelna + "%' and type = " + type + "";
		//System.out.println(" ----- 分段查詢 : " + sql);

		try {
			ps = conn.prepareCall(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				String sizefd = rs.getString("size_fd");
				Data = sizefd.split("/");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}

		return Data;
	}

	/**
	 * Size 標頭, 說明打印
	 * 
	 * @throws SQLException
	 */
	private void SetHeadLabel(Connection Conn, HSSFSheet sheet, HSSFCellStyle style2, HSSFRow row, int irow,
			HSSFCell cell, String Modelna, String Tableif) throws SQLException {
		// TODO Auto-generated method stub

		int iRow = 0;
		int iCell = 0;

		ResultSet rs = null;
		PreparedStatement ps = null;

		String Tname = "", TableName = "", insts = "";

		switch (Tableif) {
		case "1":
			TableName = Labels.getLabel("DSID.MSG0162");
			insts = "Tooling_Size_Run";
			Tname = Labels.getLabel("DSID.MSG0150");
			iRow = 2;
			break;
		case "2":
			TableName = Labels.getLabel("DSID.MSG0163");
			insts = "Left_Size_Run";
			Tname = Labels.getLabel("DSID.MSG0150");
			iRow = irow; // 間隔行數設定
			break;
		case "3":
			TableName = Labels.getLabel("DSID.MSG0164");
			insts = "Tooling_Size_Run";
			iRow = irow; // 間隔行數設定
			Tname = Labels.getLabel("DSID.MSG0150");
			break;
		}

		row = sheet.createRow(iRow);
		row.setHeightInPoints(20);

		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(TableName);

		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue("");
		sheet.addMergedRegion(new Region((short) iRow, (short) 0, (short) iRow, (short) 1));

		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(insts);

		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(insts);
		sheet.addMergedRegion(new Region((short) iRow, (short) 2, (short) iRow, (short) 3));

		iRow++;
		row = sheet.createRow(iRow);
		row.setHeightInPoints(20);

		sheet.setColumnWidth(iCell, 15 * 260);
		cell = row.createCell(iCell);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Tname);

		iCell++;

		// 表头size 最大与最小查询
		String sql = "select min(to_number(tooling_size)) Minto, max(to_number(tooling_size)) Maxto "
				+ "from dsid01 where to_char(order_date,'yyyy/MM/dd') between '" + Odate1 + "' and '" + Odate2
				+ "' and " + "model_na like '%" + Modelna + "%'";
		//System.out.println(" ----- 最大最小 尺碼查詢 : " + sql);

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			Double Values = 0.0;

			if (rs.next()) {
				Maxtool = rs.getDouble("Maxto");
				Mintool = rs.getDouble("Minto");

				// size 表頭打印
				for (Double Min = Mintool; Min <= Maxtool; Min += 0.5) {
					// System.out.println(" Max " + Mintool+ " | " +Maxtool);
					if (Tableif == "3") {
						if( Min != 3.0 && Min != 13.5 && Min != 14.5 && Min != 15.5){
							
							cell = row.createCell(iCell);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue(Min);
							iCell++;
						}
					} else {

						cell = row.createCell(iCell);
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(Min);
						iCell++;

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0165"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}

		cell = row.createCell(iCell);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID.MSG0087"));
	}

	/**
	 * 表頭打印
	 */
	private void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFCellStyle style2, HSSFRow row, HSSFCell cell,
			String Modelna) {

		// sheet.setColumnWidth(0, 25 * 256);

		row = sheet.createRow(0);
		row.setHeightInPoints(20);

		// 邊框打印
		for (int i = 0; i <= 10; i++) {
			cell = row.createCell(i);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("");
		}

		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Modelna);
		sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0, (short) 10));

		row = sheet.createRow(1);
		row.setHeightInPoints(16);

		for (int i = 0; i <= 5; i++) {
			cell = row.createCell(i);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("");
		}

		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Labels.getLabel("DSID01M.ORDER_DATE"));
		sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 1, (short) 1));

		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style2);
		cell.setCellValue(Odate1 + " - " + Odate2);
		sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 1, (short) 5));

	}
	

	/**
	 * 訂單總數
	 * 
	 * @param modelna
	 *            型體名稱
	 * @param odate1,
	 *            odate2 接單日期
	 * @throws SQLException
	 */
	private void QueryOdno(Connection conn, String Modelna) throws SQLException {
		// TODO Auto-generated method stub

		ResultSet rs = null;
		PreparedStatement ps = null;
		// Connection conn = Common.getDbConnection();

		String sql = "select count(*) Odnober from dsid01 where to_char(order_date,'YYYY/MM/DD') between '" + Odate1
				+ "' and '" + Odate2 + "' and model_na like '%" + Modelna + "%'";
		System.out.println(" ----- Query Odno : " + sql);

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				Odno = rs.getDouble("Odnober");
			}

			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0166"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}
	}
	
	private void QueryGroup1_8_Odno(Connection conn, String Modelna) throws SQLException {
		// TODO Auto-generated method stub

		ResultSet rs = null;
		PreparedStatement ps = null;
		// Connection conn = Common.getDbConnection();

		String sql = "select sum(AMOUNT) sumam from( SELECT COUNT(*) AMOUNT,NGROUP FROM (SELECT CASE LENGTH(GROUP8) WHEN 7 THEN GROUP8 ELSE SUBSTR(GROUP8,0,10) END NGROUP FROM DSID01 WHERE TO_CHAR(UP_DATE,'YYYY/MM/DD') between '"+Odate1+"' and '"+Odate2+"' AND MODEL_NA LIKE '%"+Modelna+"') GROUP BY NGROUP ORDER BY NGROUP)";
		System.out.println(" ----- Query Group1 ~ 8 Odno : " + sql);

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				Group1_8sum = rs.getDouble("sumam");
			}

			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0166"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}
	}
	

	/**
	 * 顏色名稱查詢
	 * 
	 * @param modelna
	 * @throws SQLException
	 */
	private List<String> QueryColorName(Connection conn, String Modelna, String ifQueryColor) throws SQLException {
		// TODO Auto-generated method stub
		ResultSet rs = null;
		PreparedStatement ps = null;
		// Connection conn = Common.getDbConnection();
		List<String> list = new ArrayList<String>();

		String ifColor = "";
		String substr = "";

		if (ifQueryColor == "Vamp") {
			ifColor = "GROUP1";
		}else if(ifQueryColor == "Lace" || ifQueryColor == "Tongue"){
			ifColor = getGroupno(conn, Modelna, ifQueryColor);
		}
		
		String sql = "SELECT Distinct substr(" + ifColor + ",1,3) strcolor "
				+ "FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '" + Odate1 + "' AND '" + Odate2 + "'"
				+ "AND model_na like '%" + Modelna + "%' ORDER BY strcolor";
		//System.out.println(" ----- 顏色名稱查詢 : " + sql);

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("strcolor"));
			}
			/*
			 * for(String s : list){ System.out.println(" ----- 顏色名稱 list 測試 : "
			 * + s); }
			 */
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show(Labels.getLabel("DSID.MSG0167"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}
		return list;
	}

	/**
	 * 获取鞋帶 位於 group 幾 
	 * @param conn
	 * @param modelna
	 * @throws SQLException 
	 */
	private String getGroupno(Connection conn, String modelna, String ifQueryColor) throws SQLException {
		// TODO Auto-generated method stub
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String grno = "", Grnalike = "";
		
		if(ifQueryColor == "Lace" || "Lace".equals(ifQueryColor)){
			Grnalike = "鞋帶";
		}else if(ifQueryColor == "Tongue" || "Tongue".equals(ifQueryColor)){
			Grnalike = "鞋舌";
		}
		//System.out.println(" ifQueryColor : " + ifQueryColor);
		
		String sql = "select distinct gr_no from dsid21 where gr_na like '%"+Grnalike+"%'";
		//System.out.println(" ----- Lace Group? : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				grno = rs.getString("gr_no");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0168"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			// Common.closeConnection(conn);
		}
		
		return grno;
	}


	/**
	 * 查询Group 数量
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private int QuerGroupNumber(Connection conn, String Modelna) throws SQLException {
		// TODO Auto-generated method stub
		int index = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;

		String sql = "select * from dsid10 where model_nas like '%" + Modelna + "'";

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				for (int i = 1; i < 20; i++) {
					if ("Y".equals(rs.getString("group" + i + "_status"))) {
						index++;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0169"));
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}

		return index;
	}

	/**
	 * 列數 轉換為對應的 Excel字母
	 * 
	 * @param index
	 * @return
	 */
	public static String getExcelLabel(int index) {
		String rs = "";
		do {
			index--;
			rs = ((char) (index % 26 + (int) 'A')) + rs;
			index = (int) ((index - index % 26) / 26);
		} while (index > 0);
		// System.out.println(" ----- Excel : " + rs);
		return rs;
	}

	/**
	 * 特定型體, 不一定啟用 勾勾表格打印
	 */
	private void SetGGtable(String gGname, HSSFWorkbook wb, HSSFSheet sheet2, HSSFCellStyle style1,
			HSSFCellStyle style2, String Modelna, int onrow, int icell, String GGname) {
		// TODO Auto-generated method stub
		HSSFRow row = sheet2.createRow(0);
		HSSFCell cell = row.createCell(0);

		row = sheet2.createRow(onrow);

		for (int y = 4; y < 11; y++) { // 空單元格打印
			cell = row.createCell((short) y);
			cell.setCellStyle(style2);
			cell.setCellType(1);
		}

		cell = row.createCell((short) icell);
		cell.setCellStyle(style2);
		cell.setCellType(1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0105"));
		sheet2.addMergedRegion(new Region((short) onrow, (short) icell, (short) onrow, (short) (icell + 1)));

		icell += 2;
		cell = row.createCell((short) icell);
		cell.setCellStyle(style2);
		cell.setCellType(1);
		cell.setCellValue(GGname);
		sheet2.addMergedRegion(new Region((short) onrow, (short) icell, (short) onrow, (short) (icell + 3)));

		onrow++;
		row = sheet2.createRow(onrow);
		icell = 0;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
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
