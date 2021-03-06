package ds.dsid.program;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Entity;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.runner.Request;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFDataFormat;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.hssf.util.Region;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Workbook;
import org.zkoss.poi.util.IOUtils;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import util.Common;
import util.MSMode;
import util.OpenWinCRUD;
import util.UUID;

@SuppressWarnings("all")
public class DSID21MRExcel extends OpenWinCRUD{

	private static final long serialVersionUID = -8684835641273770044L;
	
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnExport, btnExcel;
	@Wire
	private Datebox txtorder_date1, txtorder_date2, txtorder_date;
	@Wire
	private Textbox Model_no, Group_na; // , Segm_type 分段類型
	@Wire
	Combobox ModelNa;
	@Wire
	Checkbox ModelNa1, ModelNa2, ModelNa3, ModelNa4, ModelNa5;

	
	private  boolean bselfDefinedFilename = false;
	private  String selffilename = null;
	
	private  HSSFCellStyle CellStype_10C;
	private  HSSFCellStyle CellStype_Fill;
	private  HSSFCellStyle CellStype_Color10;
	private  HSSFCellStyle CellStype_Baifb;

	private DecimalFormat DecFormat = new DecimalFormat("#.##%");	
	private DecimalFormat dFormat= new DecimalFormat("#.0000");
	private DecimalFormat EFormat= new DecimalFormat("#.0000");
	private HttpServletResponse response = null;  
	private OutputStream out = null;
	private String ORDER_DATE1 = "", ORDER_DATE2 = "", MODEL_NO = "", GROUP_NA = "", SEMGTYPE = "";
	private String Points = "";
	private int Odno = 0; // 訂單總數量
	
	
	@Listen("onClick =#btnExcel")
	public void onClickbtnExcel(Event event) throws Exception{
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSID21MRExcel", this);
		
		Executions.createComponents("ds/dsid/DSID21MRExcel.zul", null, map);
	}
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
	}
	
	@Listen("onClick =#btnExport")
	public void onClickbtnExport(Event event) throws Exception{
		
		Connection conn = Common.getDbConnection();
		filterHeader(conn);
	}
	
	/**
	 * 入口
	 */
	private  void filterHeader(Connection conn) {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		
		MODEL_NO = Model_no.getValue(); // 型體名稱
//		SEMGTYPE = (Segm_type.getValue() == null ? "" : Segm_type.getValue()); //分段類型
		ORDER_DATE1 = format.format(txtorder_date1.getValue()); // 起始時間
		ORDER_DATE2 = format.format(txtorder_date2.getValue()) ;// 截止時間
		
		if(Group_na.getValue() != null || "".equals(Group_na.getValue())){
			GROUP_NA = Group_na.getValue(); // 鞋帶部位名稱
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0179"));
		}
		
		System.out.println(" —————————— ModelNo : " + MODEL_NO + " —————————— ");
		System.out.println(" —————————— OrderDate : " + ORDER_DATE1 + " —————————— ");
		System.out.println(" —————————— OrderDate : " + ORDER_DATE2 + " —————————— ");
		System.out.println(" —————————— GroupNa : " + GROUP_NA + " —————————— ");
		System.out.println(" —————————— Segm_type : " + SEMGTYPE + " —————————— ");
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFSheet sheet1 = wb.createSheet(Labels.getLabel("DSID.MSG0069"));
		HSSFSheet sheet2 = wb.createSheet(Labels.getLabel("DSID.MSG0070"));
		HSSFSheet sheet = wb.createSheet(Labels.getLabel("DSID.MSG0071"));
		HSSFSheet sheet3 = wb.createSheet(Labels.getLabel("DSID.MSG0072"));
		
		// 字體
//		HSSFFont font = wb.createFont();
		
		HSSFFont fontStyle_10 = wb.createFont();
		fontStyle_10.setFontName("新明細體");    					//设置字體  
		fontStyle_10.setFontHeightInPoints((short)10);    		//设置字体高度  
		fontStyle_10.setBoldweight(HSSFFont.COLOR_NORMAL);		//设置字體樣式 
		
		HSSFFont fontStyle_10c = wb.createFont();
		fontStyle_10c.setFontName("新明細體");    				//设置字體  
		fontStyle_10c.setFontHeightInPoints((short)10);    		//设置字体高度  
		fontStyle_10c.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//设置字體樣式 
		
		// 單元格設定
		HSSFCellStyle CellStype = wb.createCellStyle();
		CellStype.setFont(fontStyle_10);
		CellStype.setFillPattern((short) 0);
		CellStype.setWrapText(true);
		CellStype.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		CellStype.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		CellStype.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		CellStype.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		CellStype.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中    
		CellStype.setVerticalAlignment(CellStype.ALIGN_CENTER);// 垂直居中
		
		// 單元格字體居左對齊
		CellStype_Fill = wb.createCellStyle();
		CellStype_Fill.setFont(fontStyle_10c);
		CellStype_Fill.setFillPattern((short) 0);
		CellStype_Fill.setWrapText(true);
		CellStype_Fill.setBorderTop(HSSFCellStyle.BORDER_THIN);
		CellStype_Fill.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		CellStype_Fill.setBorderRight(HSSFCellStyle.BORDER_THIN);
		CellStype_Fill.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		CellStype_Fill.setAlignment(HSSFCellStyle.ALIGN_FILL);// 居左
		
		// 四周邊框 字體加粗
		CellStype_10C = wb.createCellStyle();
		CellStype_10C.setFont(fontStyle_10c);
		CellStype_10C.setFillPattern((short) 0);
		CellStype_10C.setWrapText(true);
		CellStype_10C.setBorderTop(HSSFCellStyle.BORDER_THIN);
		CellStype_10C.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		CellStype_10C.setBorderRight(HSSFCellStyle.BORDER_THIN);
		CellStype_10C.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		CellStype_10C.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		CellStype_10C.setVerticalAlignment(CellStype.VERTICAL_CENTER);// 垂直居中
		
		// 四周邊框 單元格類型為 百分比
		CellStype_Baifb = wb.createCellStyle();
		CellStype_Baifb.setFont(fontStyle_10c);
		CellStype_Baifb.setFillPattern((short) 0);
		CellStype_Baifb.setWrapText(true);
		CellStype_Baifb.setBorderTop(HSSFCellStyle.BORDER_THIN);
		CellStype_Baifb.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		CellStype_Baifb.setBorderRight(HSSFCellStyle.BORDER_THIN);
		CellStype_Baifb.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		CellStype_Baifb.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		CellStype_Baifb.setVerticalAlignment(CellStype.VERTICAL_CENTER);// 垂直居中
		CellStype_Baifb.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
		
		
		try {
			Odno = QueryOdno(conn);
			
			long startTime=System.currentTimeMillis();//记录开始时间
			piecewise(sheet1, CellStype, conn);		// 第一頁
			long endTime=System.currentTimeMillis();//记录结束时间
			float excTime=(float)(endTime-startTime)/1000;
			
			
			
			long startTime1=System.currentTimeMillis();
			NogroupJS(sheet2, conn);				// 第二頁
			long endTime1=System.currentTimeMillis();
			float excTime1=(float)(endTime1-startTime1)/1000;
			
			
			
			long startTime2=System.currentTimeMillis();
			FrontPiece(sheet, CellStype, conn);		// 第三頁
			long endTime2=System.currentTimeMillis();
			float excTime2=(float)(endTime2-startTime2)/1000;
			
			
			
			long startTime3=System.currentTimeMillis();
			Cushion(sheet3, conn, CellStype);		// 第四頁
			long endTime3=System.currentTimeMillis();
			float excTime3=(float)(endTime3-startTime3)/1000;
			
			
			System.out.println("执行时间："+excTime+"s");
			System.out.println("执行时间："+excTime1+"s");
			System.out.println("执行时间："+excTime2+"s");
			System.out.println("执行时间："+excTime3+"s");
			
	        wb.write(stream);
	        Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd-HHmmss");

			
			byte[] content = stream.toByteArray();
		    InputStream is = new ByteArrayInputStream(content);

			Filedownload.save(is, "application/xls", format1.format(date));
			is.close();
			stream.flush();
			stream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
			System.err.println(" —————————— : 結束  : —————————— ");
		}
	}
	
	/**
	 *  前面片分段Size計數
	 */
	private void piecewise(HSSFSheet sheet1, HSSFCellStyle CellStype, Connection conn){
		System.err.println(" —————————— : 第一頁 第一表格開始 : —————————— ");
		
		HSSFRow row1 = sheet1.createRow(0);
		HSSFCell cell1 = row1.createCell(0);
		PreparedStatement pss = null, ps = null, ps2 = null, ps3 = null;
		ResultSet rss = null, rs = null, rs2 = null, rs3 = null;
		Map<Object, Integer> SizeMap = new HashMap<Object, Integer>();
		Map<Double, Object> CellMap = new HashMap<Double, Object>();
		String Subgroup = "";
		Double Sizerun = 0.0;
		int Sumsize = 0;
		int rowNum = 1; // 行數基數
		int CellNum = 1; // 列基數
		Double Sizes = 3.0; // Size疊加
		
		/**
		 * 表頭設定, 大小表頭
		 */
		// 設置單元格內容

		
		for(int i = 0; i < 27; i++){ // 空單元格打印
			cell1 = row1.createCell((short) i);
			cell1.setCellType(1);
			cell1.setCellValue("");
			cell1.setCellStyle(CellStype_10C);
		}
		
		row1.setHeightInPoints(15);
		cell1 = row1.createCell((short) 0);
		cell1.setCellType(1);
		cell1.setCellValue("Tooling Size "+Labels.getLabel("DSID.MSG0180"));
		cell1.setCellStyle(CellStype_10C);
		sheet1.addMergedRegion(new Region((short)0, (short)0, (short)0, (short) 5));
		
		cell1 = row1.createCell((short) 7);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID01M.MODEL_NA"));
		cell1.setCellStyle(CellStype_10C);
		
		cell1 = row1.createCell((short) 8);
		cell1.setCellType(1);
		cell1.setCellValue(MODEL_NO);
		cell1.setCellStyle(CellStype_10C);
		sheet1.addMergedRegion(new Region((short)0, (short)8, (short)0, (short) 15));
		
		cell1 = row1.createCell((short) 17);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("COMM.DATE"));
		cell1.setCellStyle(CellStype_10C);
		
		cell1 = row1.createCell((short) 18);
		cell1.setCellType(1);
		cell1.setCellValue(ORDER_DATE1 +" -- "+ ORDER_DATE2);
		cell1.setCellStyle(CellStype_10C);
		sheet1.addMergedRegion(new Region((short)0, (short)18, (short)0, (short) 22));
		
		row1 = sheet1.createRow(rowNum++);
		row1.setHeightInPoints(30);
		
		// 小標題
		cell1 = row1.createCell((short) 0);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID.MSG0082")+"/"+Labels.getLabel("DSID.MSG0083")+"/"+Labels.getLabel("DSID.MSG0084")+"/"+Labels.getLabel("DSID.MSG0085"));
		cell1.setCellStyle(CellStype_10C);
		sheet1.setColumnWidth((short) 0, (short)(16.9 * 260));
		
		for(int i = 1; i < 27; i++){
			
			cell1 = row1.createCell((short) i);
			cell1.setCellType(1);
			cell1.setCellValue(Sizes);
			cell1.setCellStyle(CellStype_10C);
			sheet1.setColumnWidth((short) i, (short)(7.4 * 260));
			Sizes+=0.5;
		}
		
		cell1 = row1.createCell((short) 27);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID.MSG0077"));
		cell1.setCellStyle(CellStype_10C);
		sheet1.setColumnWidth((short) 27, (short)(9.5 * 260));
		
		for(Double i = 3.0; i <= 15.5; i += 0.5){
			CellMap.put(i, CellNum++);
		}
		
		// 查詢資料筆數 用於循環控制
		
			// 獲取 配色名稱
			List<String> Grouplist = new ArrayList<String>();
			String Judge = "";
			Grouplist = QueryColor(conn, Judge);
			
			for(int x = 0; x < Grouplist.size(); x++){
//				Grouplist.add(rss.getString("subgro")); // group1去重后的數量
				
				row1 = sheet1.createRow(rowNum++);
				
				// 單元格邊框線打印
				for(int i = 1; i < 27; i++){
					cell1 = row1.createCell((short) i);
					cell1.setCellStyle(CellStype_10C);
					cell1.setCellType(1);
//					sheet1.setColumnWidth((short) 27, (short)(10 * 260));
				}
				
				// Group1 截取后的配色名稱打印
				cell1 = row1.createCell((short) 0);
				cell1.setCellStyle(CellStype);
				cell1.setCellType(1);
				cell1.setCellValue(Grouplist.get(x));
				
				// 查詢個配色 總數量
				String sql3 = "SELECT COUNT(tooling_size)AS sizerun FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND group1 LIKE '"+Grouplist.get(x)+"%' AND model_na like '%"+MODEL_NO+"'";
//				System.out.println(" ———————— Group : " + sql3);
				
				try {
					ps3 = conn.prepareStatement(sql3);
					rs3 = ps3.executeQuery();
					
					while(rs3.next()){ // 右側打印個配色總數量
						
						String SizeRun = rs3.getString("sizerun");
						
						cell1 = row1.createCell((short) 27);
						cell1.setCellType(1);
						cell1.setCellStyle(CellStype);
						cell1.setCellValue(Integer.valueOf(SizeRun)); 
//						System.out.println(" ———————— 右側總計結果 : " + rs3.getInt("sizerun"));
					}
					ps3.close();
					rs3.close();
				} catch (SQLException e) {
					Messagebox.show(Labels.getLabel("DSID.MSG0078"));
					e.printStackTrace();
				} 
			
				
				// 配色各個尺碼 數據查詢
				String sql = "SELECT subgr, sizerun, SUM(leftsize) AS sumsize FROM (SELECT substr(group1,1,3)AS subgr, tooling_size AS sizerun, COUNT(tooling_size) leftsize FROM dsid01 WHERE tooling_size IN (3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5) AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND model_na like '%"+MODEL_NO+"' GROUP BY tooling_size, group1) GROUP BY subgr, sizerun order BY subgr";
//				System.out.println(" —————— 資料查詢 : " + sql);

				
				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					
					while(rs.next()){
						
						Subgroup 	= rs.getString("subgr");	// 顏色編號
						Sizerun		= rs.getDouble("sizerun");	// Size碼
						Sumsize		= rs.getInt("sumsize");		// Size數量
						
//						System.err.println(" ———————— 顏色 : " + Subgroup + " Size碼: " + Sizerun + " Size數量: " + Sumsize);
						
						if(Subgroup  ==  Grouplist.get(x) || Subgroup.equals(Grouplist.get(x))){
							for(Double j = 3.0; j <= 15.5; j+=0.5){	// Size碼編號循環		
								if(Sizerun - j == 0.0){
									
									int Celll = (int) CellMap.get(j);
									cell1 = row1.createCell((short) Celll);
									cell1.setCellStyle(CellStype);
									cell1.setCellType(1);
									cell1.setCellValue(Sumsize);
								}
							}
						}
					}
					ps.close();
					rs.close();
				} catch (SQLException e) {
					Messagebox.show(Labels.getLabel("DSID.MSG0052"));
					e.printStackTrace();
				}
			}
		
		row1 = sheet1.createRow(rowNum++);
		
		cell1 = row1.createCell((short) 0);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID.MSG0077"));
		cell1.setCellStyle(CellStype_10C);
		
		// 空單元格邊框打印
		for(int i = 1; i < 28; i++){
			cell1 = row1.createCell((short) i);
			cell1.setCellStyle(CellStype_10C);
			cell1.setCellType(1);
		}
		
		// 底部總計結果
		String sql2 = "SELECT COUNT(group1)as sizes, tooling_size FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND tooling_size IN (3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5) AND model_na like '%"+MODEL_NO+"' GROUP BY tooling_size";
//		System.out.println(" ———————— SizeMap : " + sql2);
		
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();
			
			while(rs2.next()){
				
				double ToSize = rs2.getDouble("tooling_size");
				int Sizesum = rs2.getInt("sizes");
				
				SizeMap.put(ToSize, Sizesum);
			}
		//	System.out.println(" ————————  SizeMap : " + SizeMap);
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			Messagebox.show(Labels.getLabel("DSID.MSG0079"));
			e.printStackTrace();
		}
		
		int SizeNumber = 0, Cells = 0;
		Map<Integer, Object> foMap = new HashMap<Integer, Object>(); // foMap 裝入與Size碼對應 列號 便於打印
		int cells = 0;
		for(Double i = 3.0; i < 15.5; i+=0.5){
			foMap.put(cells+=1, i);
		}
		
		Set<Entry<Integer, Object>> foentries = foMap.entrySet(); 	
		Set<Entry<Object, Integer>> entries = SizeMap.entrySet(); 	// 遍歷Map集合, 拿到Key值, Key為Size碼
		
		for(Entry<Object, Integer> entry : entries){				// 遍歷SizeMap<Size碼, 總數量 > 取到Size碼與 總數量
			for(Entry<Integer, Object> foentry : foentries){		// 遍歷循環控制Map<1-26cell, Size碼>
				Double enKey = (Double) entry.getKey();
				Double foValue = (Double) foentry.getValue();
				if( enKey == foValue || enKey.equals(foValue)){		// 判斷SizeMap的Size 等於 foMap的Size
					
					SizeNumber = entry.getValue();					// 獲得對應Size下的總數量
					Cells = foentry.getKey();						// 獲取列號
					// 打印底部總數量
					cell1 = row1.createCell((short) Cells);
					cell1.setCellStyle(CellStype_10C);
					cell1.setCellType(1);
					cell1.setCellValue(SizeNumber);
				}
			}
		}
		
		cell1 = row1.createCell((short) 27);
		cell1.setCellStyle(CellStype_10C);
		cell1.setCellType(1);
		cell1.setCellFormula("SUM(AB3:AB"+(rowNum-1)+")"); // Excel計算公式
		
		piecewise2(row1, rowNum, cell1, sheet1, conn); // 百分比計算
		System.err.println(" —————————— : 第一頁 第一表格結束 : —————————— ");
		
	}
	
	/**
	 * 比分比計算 與上一方法是一體的
	 */
	private void piecewise2(HSSFRow row1, int rowNum, HSSFCell cell1, HSSFSheet sheet1, Connection conn){
		
		System.err.println(" —————————— : 第一頁 第二表格開始 : —————————— ");
		
		rowNum += 2;
		int iCell = 0;
		
		double  Sizenum = 0.0, Odnonum = 0.0;
		String sql = "";
		String[] st = null;
		String Jtool = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> Grouplist = new ArrayList<String>();
		
		row1 = sheet1.createRow(rowNum);
		row1.setHeightInPoints(30); // 行高設定
		cell1 = row1.createCell((short) 0);
		cell1.setCellStyle(CellStype_10C);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID.MSG0080"));
		
		cell1 = row1.createCell((short) 1);
		cell1.setCellStyle(CellStype_10C);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID.MSG0081"));
		sheet1.addMergedRegion(new Region((short)rowNum, (short)1, (short)rowNum, (short) 2));
		
		cell1 = row1.createCell((short) 2);
		cell1.setCellStyle(CellStype_10C);
		cell1.setCellType(1);
		cell1.setCellValue("");
		
		cell1 = row1.createCell((short) 3);
		cell1.setCellStyle(CellStype_10C);
		cell1.setCellType(1);
		cell1.setCellValue(Odno);
		
		rowNum++;
		row1 = sheet1.createRow(rowNum);
		
		cell1 = row1.createCell((short) iCell);
		cell1.setCellStyle(CellStype_10C);
		cell1.setCellType(1);
		cell1.setCellValue(Labels.getLabel("DSID.MSG0076"));
		iCell++;
		
		Points = "前面片";
		List<String> toolinglist = QueryPoints(conn, Points); // 分段 名稱抓取
		
		/* 打印表頭 */
		for(int i = 0; i < toolinglist.size(); i ++){
			
			st = toolinglist.get(i).split("-");
			Jtool = st[0]; // 獲取分段
			// 拼接表頭字符串
			String[] CJtool = Jtool.split(",");
			String Cname0 = CJtool[0];
			String Cname1 = CJtool[CJtool.length - 1];
			String CName = Cname0 +"-"+ Cname1;
//			System.out.println("------------------------ 表頭字符串" + CName);
			
			if(i < 0){
				iCell += 2;
			}
			
			cell1 = row1.createCell((short) iCell);
			cell1.setCellStyle(CellStype_10C);
			cell1.setCellType(1);
			cell1.setCellValue(CName);
			sheet1.addMergedRegion(new Region((short)rowNum, (short)iCell, (short)rowNum, (short) (iCell+1)));
			iCell++;
			
			cell1 = row1.createCell((short) iCell);
			cell1.setCellStyle(CellStype_10C);
			cell1.setCellType(1);
			cell1.setCellValue("");
			iCell++;
		}
		
		rowNum++;
		
		
		
		// 轉印10A 顏色
		if(MODEL_NO == "PEGASUS+35 MIN FA18 ID" || "PEGASUS+35 MIN FA18 ID".equals(MODEL_NO) || MODEL_NO == "PEGASUS+35 ESS SU18 ID" || "PEGASUS+35 ESS SU18 ID".equals(MODEL_NO)){
			
			Grouplist = QueryZdps(conn); // 抓取配色名稱
			TD_Model(row1, rowNum, cell1, sheet1, conn, Grouplist, toolinglist);
			
		}else{ // 非轉印 顏色打印
			
			System.out.println("----- 非轉印 配色-----");
			
			String Judge = "";
			Grouplist = QueryColor(conn, Judge);
			
			for(int i = 0; i < Grouplist.size(); i ++){// 遍歷顏色名稱
				
				// 打印顏色名稱
				row1 = sheet1.createRow(rowNum);
				cell1 = row1.createCell((short) 0);
				cell1.setCellStyle(CellStype_10C);
				cell1.setCellType(1);
				cell1.setCellValue(Grouplist.get(i));
				
				for(int io = 0; io < toolinglist.size(); io++){ // 遍歷分段
					
					st = toolinglist.get(io).split("-");
					Jtool = st[0]; // 獲取分段
					
					/** 查詢個Size分段 顏色數量 */
					
					sql = "SELECT group1, COUNT(*) tooling FROM dsid01 WHERE group1 LIKE '"+Grouplist.get(i)+"%' AND tooling_size IN ("+Jtool+") AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"' GROUP BY group1";
					System.out.println(" —————————— 常規分段數量 : " + sql);

					try {
						ps = conn.prepareStatement(sql);
						rs = ps.executeQuery();
						
						if(rs.next()){ // 打印個配色數量
							
							int Toling = rs.getInt("tooling");
							
							cell1 = row1.createCell((short) i+1);
							cell1.setCellStyle(CellStype_10C);
							cell1.setCellType(1);
								
							if( Toling == 0){
								cell1.setCellValue("");
							}else{
								cell1.setCellValue(Toling);
							}
						}
						ps.close();
						rs.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				rowNum ++;
			}
		}
		System.err.println(" —————————— : 第一頁 第二表格結束 : —————————— ");
	}
	
	/**
	 * 特定型體 百分比打印 如(顏色轉印)
	 */
	private void TD_Model(HSSFRow row1, int rowNum, HSSFCell cell1, HSSFSheet sheet1, Connection conn, List<String> Grouplist, List<String> toolinglist){
		
		System.err.println(" —————————— 轉印10A");
		
		int iRow = rowNum;
		String[] str = null;
		String Jtool = "";
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		for(int i = 0; i < Grouplist.size(); i ++){// 遍歷顏色名稱
			
			// 打印顏色名稱
			row1 = sheet1.createRow(rowNum);
			
			cell1 = row1.createCell((short) 0);
			cell1.setCellStyle(CellStype_10C);
			cell1.setCellType(1);
			cell1.setCellValue(Grouplist.get(i));
		
			rowNum ++;
		}
		
		int iCell = 1;
		for(int io = 0; io < toolinglist.size(); io++){ // 遍歷分段
			
			str = toolinglist.get(io).split("-");
			Jtool = str[0]; // 獲取分段
			
			/** 查詢個Size分段 顏色數量 */
			// 特定顏色 轉印型體 分段顏色數量查詢
			String Peise = "";
			if(MODEL_NO == "PEGASUS+35 MIN FA18 ID" || "PEGASUS+35 MIN FA18 ID".equals(MODEL_NO)){
				Peise = "73P";
			}else{
				Peise = "85Z";
			}
			
			String sql = "SELECT group1, tooling, qvsum FROM( SELECT group1, COUNT(*)AS tooling FROM dsid01 WHERE group1 LIKE '%"+Grouplist.get(0)+"%' AND to_number(tooling_size) IN ("+Jtool+") AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"%' GROUP BY group1), (SELECT COUNT(*)AS qvsum FROM dsid01 WHERE group1 NOT like '%"+Peise+"%' AND to_number(tooling_size) IN ("+Jtool+") AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"%')";
//			System.out.println(" —————————— 轉印10A : " + sql);
			
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				if(rs.next()){
					
					int Specific = rs.getInt("tooling"); // 指定配色數量
					int SumColor = rs.getInt("qvsum");	// 其他配色合併
					
					row1 = sheet1.getRow(iRow);
					cell1 = row1.createCell((short) iCell);
					cell1.setCellStyle(CellStype_Baifb);
					cell1.setCellType(1);
					cell1.setCellFormula(Specific +"/D"+(iRow-1));
					sheet1.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short)(iCell+1)));
					
					cell1 = row1.createCell((short) (iCell+1));
					cell1.setCellStyle(CellStype_Baifb);
					cell1.setCellType(1);
					cell1.setCellValue("");
					
					row1 = sheet1.getRow(iRow+1);
					cell1 = row1.createCell((short) iCell);
					cell1.setCellStyle(CellStype_Baifb);
					cell1.setCellType(1);
					cell1.setCellFormula(SumColor +"/D"+(iRow-1));
					sheet1.addMergedRegion(new Region((short)(iRow+1), (short)iCell, (short)(iRow+1), (short)(iCell+1)));
					//System.err.println(" —————————— 列印資料 : " + "行:"+sheet1.getRow(iRow+2) + "列:"+ (io+1) +" 指定配色: "+ SumColor);
					
					cell1 = row1.createCell((short) (iCell+1));
					cell1.setCellStyle(CellStype_Baifb);
					cell1.setCellType(1);
					cell1.setCellValue("");
					
					iCell += 2;
				}
				
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 不區分Group 統計個碼數數量
	 * 第二頁 
	 */
	private void NogroupJS(HSSFSheet sheet2, Connection conn){
		
		System.err.println(" —————————— : 第二頁開始 : —————————— ");
		
		int number = 0, Numsize = 0;
		int rowNum = 0;
		int Tag = 1, Tag2 = 1;
		String sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		HSSFRow row2 = sheet2.createRow(0);
		HSSFCell cell2 = row2.createCell(0);
		
		List<String> GroupSL = new ArrayList<String>();
		String Judge = "";
		GroupSL = QueryColor(conn, Judge);
		
		row2 = sheet2.createRow(rowNum++);
		
		cell2 = row2.createCell((short) 0);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID01M.MODEL_NA"));
		cell2.setCellStyle(CellStype_10C);
		
		for(int i = 1; i < 10; i++){ // 空單元格打印
			cell2 = row2.createCell((short) i);
			cell2.setCellType(1);
			cell2.setCellValue("");
			cell2.setCellStyle(CellStype_10C);
		}
		
		cell2 = row2.createCell((short) 1);
		cell2.setCellType(1);
		cell2.setCellValue(MODEL_NO);
		cell2.setCellStyle(CellStype_10C);
		sheet2.addMergedRegion(new Region((short)0, (short)1, (short)0, (short) 10));
		
		row2 = sheet2.createRow(rowNum++);
		
		row2.setHeightInPoints(30); // 行高設定
		cell2 = row2.createCell((short) 0);
		cell2.setCellType(1);
		cell2.setCellValue("Tooling Size");
		cell2.setCellStyle(CellStype_10C);
		sheet2.setColumnWidth((short) 0, (short)(15.4 * 260));
		
		for(Double i = 3.0; i <= 15.5; i+=0.5){
			cell2 = row2.createCell((short) Tag);
			cell2.setCellType(1);
			cell2.setCellValue(i);
			cell2.setCellStyle(CellStype_10C);
			sheet2.setColumnWidth((short) Tag, (short)(7.3 * 260));
			
			Tag++;
		}
		
		cell2 = row2.createCell((short) 27);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0087"));
		cell2.setCellStyle(CellStype_10C);
		sheet2.setColumnWidth((short) 27, (short)(9.8 * 260));
		
			// 打印配色名稱
			row2 = sheet2.createRow(rowNum++);
			
			cell2 = row2.createCell((short) 0);
			cell2.setCellType(1);
			cell2.setCellValue(Labels.getLabel("DSID.MSG0087"));
			cell2.setCellStyle(CellStype_10C);
			
			for(Double s = 3.0; s <= 15.5; s+=0.5){
				
				// 查詢個碼數數量
				sql = "SELECT count(*)AS Sl  FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND tooling_size = '"+s+"' AND model_na like '%"+MODEL_NO+"'";
				
				if(s <= 15.5){ // 最後查詢一次 總雙數
					sql = "SELECT * FROM("+sql+"),( SELECT COUNT(*)as sumsize FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"')";
				}
				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					if(rs.next()){
						
						int Sl = rs.getInt("Sl");
						
						if(s <= 15.5){ // 最後一次循環的 數值獲取
							Numsize = rs.getInt("sumsize");
							
							if( Sl == 0 || "".equals(Sl)){
								number = 0;
							}else{
								number = Sl;
							}
						}else{
							if(Sl == 0 || "".equals(Sl)){
								number = 0;
							}else{
								number = Sl;
							}
						}
					}
					
					cell2 = row2.createCell((short) Tag2);
					cell2.setCellType(1);
					cell2.setCellValue(number);
					cell2.setCellStyle(CellStype_10C);
					
					ps.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					Messagebox.show(Labels.getLabel("DSID.MSG0088"));
				}
				Tag2++;
			}
		
		cell2 = row2.createCell((short) 27);
		cell2.setCellType(1);
		cell2.setCellValue(Numsize);
		cell2.setCellStyle(CellStype_10C);
		
		SecondTable(sheet2, row2, cell2, rowNum, conn);
		System.err.println(" —————————— : 第二頁 第一表格 結束 : —————————— ");
	}
	
	/**
	 * 第二頁
	 * 第二張表
	 */
	private void SecondTable(HSSFSheet sheet2, HSSFRow row2, HSSFCell cell2, int rowNum, Connection conn){
		
		System.err.println(" —————————— : 第二頁 第二表格 開始 : —————————— ");
		
		ResultSet rs = null, rs2 = null;
		PreparedStatement ps = null, ps2 = null;
		
		rowNum = rowNum + 2 ;
		row2 = sheet2.createRow(rowNum);
		

		cell2 = row2.createCell((short) 0);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0089"));
		cell2.setCellStyle(CellStype_10C);
		
		cell2 = row2.createCell((short) 1);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0090"));
		cell2.setCellStyle(CellStype_10C);
		
		rowNum++;
		row2 = sheet2.createRow(rowNum);	
		row2.setHeightInPoints(30); // 行高設定
		cell2 = row2.createCell((short) 0);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0091"));
		cell2.setCellStyle(CellStype_10C);
		
		int j = 1;
		for(Double i = 3.0; i <= 15.5; i+=0.5){ // 小表頭打印
			
			cell2 = row2.createCell((short) j);
			cell2.setCellType(1);
			cell2.setCellValue(i);
			cell2.setCellStyle(CellStype_10C);
			j++;
		}
		
		cell2 = row2.createCell((short) 27);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0087"));
		cell2.setCellStyle(CellStype_10C);
		
		rowNum++;
		int celll2 = 1;
		
		String Judge = "";
		List<String> QueryCo = QueryColor(conn, Judge);
		
		for(String s : QueryCo){
			int Sumsize = 0;
			row2 = sheet2.createRow(rowNum);
			
			cell2 = row2.createCell((short) 0);
			cell2.setCellType(1);
			cell2.setCellValue(s);
			cell2.setCellStyle(CellStype_10C);
			

			for(Double i = 3.0; i <= 15.5; i+=0.5){
				// 查詢左腳各配色 各尺碼數量
				// 查詢左腳各配色 全部尺碼的總和
				String sql = "SELECT * FROM("
						+ "SELECT COUNT(*)AS MsCount FROM dsid01 WHERE model_na = '"+MODEL_NO+"' AND group1 LIKE '%"+s+"%' AND left_size_run IN ("+i+") AND to_char(order_date,'yyyy/mm/dd') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'),("
						+ "SELECT COUNT(*)AS WsCount FROM dsid01 WHERE model_na = 'W "+MODEL_NO+"' AND group1 LIKE '%"+s+"%' AND left_size_run IN ("+(i + 1.5)+") AND to_char(order_date,'yyyy/mm/dd') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'),("
						+ "SELECT count(Left_size_run)AS Sumsize FROM dsid01 WHERE group1 LIKE '"+s+"%' AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND to_number(left_size_run) IN ('3.0','3.5','4.0','4.5','5.0','5.5','6.0','6.5','7.0','7.5','8.0','8.5','9.0','9.5','10.0','10.5','11.0','11.5','12.0','12.5','13.0','13.5','14.0','14.5','15.0','15.5') AND model_na like '%"+MODEL_NO+"')";
//				System.out.println(" —————————— W&Left 查詢  : " + sql);
				
				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					if(rs.next()){
						
						int Left = rs.getInt("MsCount");
						int WLeft = rs.getInt("WsCount");
						Sumsize = rs.getInt("Sumsize");

							cell2 = row2.createCell((short) celll2);
							cell2.setCellType(1);
							if(Left == 0 && WLeft == 0 || "".equals(Left) && "".equals(WLeft)){ // 數量等於零 則 打印空白單元格
								cell2.setCellValue("");
							}else{
								cell2.setCellValue(Left + WLeft);
							}
							cell2.setCellStyle(CellStype_10C);
					}
					ps.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				celll2 ++;	
			}
			
			cell2 = row2.createCell((short) 27);
			cell2.setCellType(1);
			cell2.setCellValue(Sumsize);
			cell2.setCellStyle(CellStype_10C);
			
			celll2 = 1;
			rowNum++;
		}
		
		List<String> Plist = new ArrayList<String>();
		Plist = QueryColor(conn, Judge);
		
		row2 = sheet2.createRow(rowNum);
		cell2 = row2.createCell((short) 0);
		cell2.setCellType(1);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0087"));
		cell2.setCellStyle(CellStype_10C);
		
		for(Double i = 3.0; i <= 15.5; i+=0.5){
			
			int Cellsum = 0;
			// 查詢左腳碼數 各尺碼總和
			String sql2 = "SELECT COUNT(left_size_run)AS Cellsum FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND to_number(left_size_run) = '"+i+"' AND model_na like '%"+MODEL_NO+"'";
//			System.out.println(" —————————— 列總和數據 : " + sql2);
			
			try {
				ps2 = conn.prepareStatement(sql2);
				rs2 = ps2.executeQuery();
				if(rs2.next()){
					Cellsum = rs2.getInt("Cellsum");
				}
				ps2.close();
				rs2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			cell2 = row2.createCell((short) celll2);
			cell2.setCellType(1);
			cell2.setCellValue(Cellsum);
			cell2.setCellStyle(CellStype_10C);
			
			celll2++;
		}
		
		// 第27列 統計結果
		cell2 = row2.createCell((short) 27);
		cell2.setCellType(1);
		cell2.setCellStyle(CellStype_10C);
		cell2.setCellFormula("SUM(AB6:AB"+(rowNum)+")"); // Excel計算公式
		
		Percentage0(sheet2, row2, cell2, rowNum, conn);

		System.err.println(" —————————— : 第二頁 第二表格結束 : —————————— ");
	}
	
	/**
	 * 男女鞋帶 合併統計, 分段配色百分比
	 * 第二頁 第三表格
	 */
	private void Percentage0(HSSFSheet sheet2, HSSFRow row2, HSSFCell cell2, int rowNum, Connection conn){
		
		System.err.println(" —————————— : 第二頁 第三表格 開始 : —————————— ");
		
		String sql = "";
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null;
		Double WCount = 0.0, SCount = 0.0;
		Double Sumtooling = 0.0;
		
		int Msj = 0, Wsj = 0, MWs = 0, ibaiRow = 0, ExcelRow = 0;
		int Odno = QueryOdno(conn);
		
		String name = "";
		
		List<String> Mlist = new ArrayList<String>();
		Mlist = QueryPercen(conn);	// 獲取鞋帶分段信息
		
		// 表頭設定
		
		int icell = 0;
		rowNum += 3; // 起始行
		row2 = sheet2.createRow(rowNum);
		
		for(int i = 0; i <= 3; i++){ // 空白單元格子打印
			cell2 = row2.createCell((short) i);
			cell2.setCellValue("");
			cell2.setCellType(1);
			cell2.setCellStyle(CellStype_10C);
		}
		
		cell2 = row2.createCell((short) 0);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0092"));
		cell2.setCellType(1);
		cell2.setCellStyle(CellStype_10C);
		sheet2.addMergedRegion(new Region((short)rowNum, (short)0, (short)rowNum, (short) 1));
		
		cell2 = row2.createCell((short) 2);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0081"));
		cell2.setCellType(1);
		cell2.setCellStyle(CellStype_10C);
		sheet2.addMergedRegion(new Region((short)rowNum, (short)2, (short)rowNum, (short) 3));
		
		cell2 = row2.createCell((short) 4);
		cell2.setCellValue(Odno);
		cell2.setCellType(1);
		cell2.setCellStyle(CellStype_10C);

		rowNum ++;
		row2 = sheet2.createRow(rowNum);
		
		cell2 = row2.createCell((short) 0);
		cell2.setCellValue(Labels.getLabel("DSID.MSG0093"));
		cell2.setCellType(1);
		cell2.setCellStyle(CellStype_10C);
		
		int s = 1, j = 0;
		for(String i : Mlist){ // 遍歷分段數量, 每個分段打印一次.
			
			// 截取前三位與后三位數字 拼接成表頭名稱
			String[] io = i.split("-");
			String[] oi = io[0].split(",");
			String name1 = oi[0];
			String name2 = oi[oi.length - 1];
			name = name1 +"-"+ name2;
			
			cell2 = row2.createCell((short) s);
			cell2.setCellType(1);
			cell2.setCellStyle(CellStype_10C);
			cell2.setCellValue(name);
			sheet2.addMergedRegion(new Region((short)rowNum, (short)s, (short)rowNum, (short)(s+1)));
			
			cell2 = row2.createCell((short) s+1);
			cell2.setCellType(1);
			cell2.setCellStyle(CellStype_10C);
			
			s += 2;
			
		}
		
		String Judge = "Percen";
		List<String> QueryCo = QueryColor(conn, Judge);
		for(int i = 0; i < QueryCo.size(); i++){ // 循環配色名稱
			
			s = 0;
			rowNum ++;
			row2 = sheet2.createRow(rowNum);
			
			cell2 = row2.createCell((short) s);
			cell2.setCellType(1);
			cell2.setCellStyle(CellStype_10C);
			cell2.setCellValue(QueryCo.get(i));
			
			ibaiRow = rowNum;
			s++;
			for(int z = 0; z < Mlist.size(); z++){
				
				String zx = Mlist.get(z);
				String[] zxx = zx.split("-");
				String MsSize = zxx[0];
				
				//System.out.println(" —————————— 顏色名稱 : " + i);
				//System.out.println(" —————————— Ms 分段码 : " + MsSize);

				sql = "SELECT sum(cou) Ms FROM ("
						+ "SELECT COUNT(*) COU,LEFT_SIZE_RUN,GROUP4 FROM ("
							+ "SELECT TO_NUMBER(LEFT_SIZE_RUN) LEFT_SIZE_RUN,GROUP4 FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND MODEL_NA LIKE '"+MODEL_NO+"' "
							+ "UNION ALL "
							+ "SELECT LEFT_SIZE_RUN-1.5 LEFT_SIZE_RUN,GROUP4 FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND MODEL_NA LIKE 'W "+MODEL_NO+"' "
						+ ") WHERE TO_NUMBER(left_size_run) IN("+MsSize+") AND "+GROUP_NA+" LIKE '%"+QueryCo.get(i)+"%' GROUP BY GROUP4,LEFT_SIZE_RUN)";
//				System.out.println(" —————————— 鞋帶合計數量查詢 : " + sql);
				
				
				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					
					if(rs.next()){
						
						int Ms = rs.getInt("Ms");
						
						if(i == 0){
							ExcelRow = rowNum;
						}
						
						cell2 = row2.createCell((short) s);
						cell2.setCellType(1);
						cell2.setCellStyle(CellStype_Baifb);
						cell2.setCellFormula(Ms +"/E"+(ExcelRow-1));
						sheet2.addMergedRegion(new Region((short)rowNum, (short)s, (short)rowNum, (short)(s+1)));
						
						cell2 = row2.createCell((short) s+1);
						cell2.setCellType(1);
						cell2.setCellStyle(CellStype_10C);
						
						s += 2;
					}
					
					ps.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * 第三頁
	 * Group統計
	 */
	private void FrontPiece(HSSFSheet sheet, HSSFCellStyle CellStype, Connection conn){
		
		System.err.println(" —————————— : 第三頁 開始 : —————————— ");
		
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		
		ResultSet rs = null, rs1 = null;
		PreparedStatement ps = null, ps1 = null;
		String sql = null, sql1 = null;
		List<String> Frontlist = null;
		
		Double Cogr = 0.0, Sumgr = 0.0;
		String Gro = "", Group = "";
		int rowNum = 0, iRow = 0, iiRow = 3, iCell = 0, iiCell = 3, rsRow = 0, MaxiRow = 0;
		
		/**
		 * 表頭設定, 大小表頭
		 */
		// 設置單元格內容
		rowNum ++;
		row = sheet.createRow(rowNum);
		
		cell = row.createCell((short) iCell);
		cell.setCellValue("Group "+Labels.getLabel("DSID.MSG0094"));
		cell.setCellType(1);
		cell.setCellStyle(CellStype);
//		sheet.addMergedRegion(new Region((short)rowNum, (short)iCell, (short)rowNum, (short)(iCell + 2)));
		
		iRow = rowNum;
		
		// 預先固定打印 60行 之後不再新建行
		for(int i = 1; i <= 60; i++ ){
			row = sheet.createRow(i);
		}
		
		for(int i = 1; i < 20; i++){
			
			
			sql = "SELECT group"+i+"_na AS group_na FROM dsid10 WHERE nike_sh_aritcle = (SELECT distinct nike_sh_aritcle FROM dsid01 WHERE model_na = '"+MODEL_NO+"' AND nike_sh_aritcle is not null)";
//			System.out.println(" —————————— Group_na 查詢 : " + sql);
			
			try {
				
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				if(rs.next()){
					
					String Groupna = rs.getString("group_na");
					
					if(Groupna != null){
						
						// 打印Group各個部位名稱
						row = sheet.getRow(iRow);
						cell = row.createCell((short) iCell);
						cell.setCellType(1);
						cell.setCellValue(Groupna);
						cell.setCellStyle(CellStype);
						sheet.addMergedRegion(new Region((short)rowNum, (short)iCell, (short)rowNum, (short) (iCell + 2) ));
						
						cell = row.createCell((short) (iCell + 1));
						cell.setCellType(1);
						cell.setCellStyle(CellStype);
						
						cell = row.createCell((short) (iCell + 2));
						cell.setCellType(1);
						cell.setCellStyle(CellStype);
						
						
						// Group 標題打印
						row = sheet.getRow((iRow + 1));
						
						cell = row.createCell((short) iCell);
						cell.setCellType(1);
						cell.setCellValue("Group" + i + "-"+Labels.getLabel("DSID.MSG0095")+":");
						cell.setCellStyle(CellStype);
						sheet.addMergedRegion(new Region((short) (rowNum + 1), (short)iCell, (short) (rowNum + 1), (short) (iCell + 1) ));
						
						cell = row.createCell((short) (iCell + 1));
						cell.setCellType(1);
						cell.setCellStyle(CellStype);
						
						cell = row.createCell((short) (iCell + 2));
						cell.setCellType(1);
						cell.setCellValue(Odno);
						cell.setCellStyle(CellStype);
						
						
						// 固定標題
						row = sheet.getRow((iRow + 2));

						cell = row.createCell((short) iCell);
						cell.setCellType(1);
						cell.setCellValue(Labels.getLabel("DSID.MSG0096"));
						cell.setCellStyle(CellStype);
						
						cell = row.createCell((short) (iCell + 1));
						cell.setCellType(1);
						cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
						cell.setCellStyle(CellStype);
						
						cell = row.createCell((short) (iCell + 2));
						cell.setCellType(1);
						cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
						cell.setCellStyle(CellStype);
						
						
						sql1 = "SELECT gro , sum(sgro)AS ro FROM (SELECT substr(group"+i+",1,3)AS gro, Count(group"+i+")AS sgro FROM dsid01 where to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"' AND group"+i+" IS NOT NULL GROUP BY group"+i+")a GROUP BY gro";
//						System.out.println(" ———————— Group 數量查詢 : " + sql1);
						
						try {
							
							ps1 = conn.prepareStatement(sql1);
							rs1 = ps1.executeQuery();
							
							while(rs1.next()){
								
								Gro = rs1.getString("gro"); // 顏色名稱
								Cogr = rs1.getDouble("ro"); // 數量
								
//								System.out.println(" ----- Group"+i+" ----- Gro:" + Gro + " ----- Corg:" + Cogr);
								
								// 配色、數量、百分比 列印
								row = sheet.getRow((iRow + iiRow));
								
								cell = row.createCell((short) iCell);
								cell.setCellType(1);
								cell.setCellStyle(CellStype);
								cell.setCellValue(Gro);
								
								cell = row.createCell((short) (iCell + 1));
								cell.setCellType(1);
								cell.setCellStyle(CellStype);
								cell.setCellValue(Cogr);
								
								cell = row.createCell((short) (iCell + 2));
								cell.setCellType(1);
								cell.setCellStyle(CellStype_Baifb);
								cell.setCellFormula(Cogr+"/"+Odno); // 顏色數量 / 總接單數 = 百分比
								
								iiRow ++;
							}
							
							iiRow = 3;
							rs1.close();
							ps1.close();
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						iCell += 4;
						if(iCell%20 == 0){ // 1組5個Group統計表格, 20列后 換下一行第二組開始
							iCell = 0;
							rowNum += 15;
							row = sheet.createRow(rowNum);
							iRow = rowNum;
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		HookToo( sheet, conn, rowNum, rs, rs1, ps, ps1);
	}
	
	/**
	 * 第四頁 前後氣墊 港寶 tooling_size
	 */
	private void Cushion(HSSFSheet sheet3, Connection conn, HSSFCellStyle CellStype){
		
		HSSFRow RowNew = sheet3.createRow(0);
		HSSFCell Cell = RowNew.createCell(0);
		
		int iRow = 0, iCell = 0;
		
		
		Cushion1(conn);
		
		List<Map<String, Integer>> Cus = new ArrayList<Map<String, Integer>>();
		Cus = Cushion1(conn); // 獲取分段  與分段數量
		
		Map<String, Integer> Before = Cus.get(0);	// 前掌氣墊
		Map<String, Integer> After = Cus.get(1);	// 後掌氣墊
		Map<String, Integer> Port = Cus.get(2);		// 港寶
		Map<String, Integer> Cloth = Cus.get(3);	// 布標
		
		for(int i = 1; i < 25; i++){
			RowNew = sheet3.createRow(i);
		}
		
		// 前掌氣墊 小標題
		RowNew = sheet3.getRow(iRow);
		Cell = RowNew.createCell((short) iCell);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
		Cell.setCellStyle(CellStype);
		sheet3.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 2)));
					
		Cell = RowNew.createCell((short) (iCell + 1));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 2));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
				
		Cell = RowNew.createCell((short) (iCell + 3));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 4));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
		Cell.setCellStyle(CellStype);
		
		/* 前掌氣墊 */
		int JiRow = 1;
		for(Entry<String, Integer> Entry : Before.entrySet()){
			
			String[] Cent = Entry.getKey().split(",");
			String Enname = Labels.getLabel("DSID.MSG0100")+"#Ms " + Cent[0] +"-"+ Cent[Cent.length - 1];
			
			//System.out.println("Enname : " + Enname + " ----- Entry.getKey() : " + Entry.getKey() + " ----- Entry.getValue() : " + Entry.getValue());
			
			// 資料列印
			RowNew = sheet3.getRow(iRow + JiRow);
			Cell = RowNew.createCell((short) iCell);
			Cell.setCellType(1);
			Cell.setCellValue(Enname);
			Cell.setCellStyle(CellStype_Fill);
			sheet3.addMergedRegion(new Region((short)(iRow + JiRow), (short)iCell, (short)(iRow + JiRow), (short) (iCell + 2)));
			
			Cell = RowNew.createCell((short) (iCell + 1));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 2));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 3));
			Cell.setCellType(1);
			Cell.setCellValue(Entry.getValue());
			Cell.setCellStyle(CellStype);
			System.out.println(" ----- Value : " + Entry.getValue());
			
			Cell = RowNew.createCell((short) (iCell + 4));
			Cell.setCellType(1);
			Cell.setCellFormula(Entry.getValue()+"/"+Odno);
			Cell.setCellStyle(CellStype_Baifb);
			
			JiRow ++;
			
		}
		
		// 後掌氣墊 小標題
		RowNew = sheet3.getRow(iRow);
		Cell = RowNew.createCell((short) iCell + 7);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
		Cell.setCellStyle(CellStype);
		sheet3.addMergedRegion(new Region((short)iRow, (short)(iCell + 7), (short)iRow, (short) (iCell + 9)));
					
		Cell = RowNew.createCell((short) (iCell + 8));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 9));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
				
		Cell = RowNew.createCell((short) (iCell + 10));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 11));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
		Cell.setCellStyle(CellStype);
		
		JiRow = 1;
		for(Entry<String, Integer> Entry : After.entrySet()){
			
			String[] Cent = Entry.getKey().split(",");
			String Enname = Labels.getLabel("DSID.MSG0101")+"#Ms " + Cent[0] +"-"+ Cent[Cent.length - 1];
			
			//System.out.println("Enname : " + Enname + " ----- Entry.getKey() : " + Entry.getKey() + " ----- Entry.getValue() : " + Entry.getValue());
			
			// 資料列印
			RowNew = sheet3.getRow(iRow + JiRow);
			Cell = RowNew.createCell((short) (iCell + 7));
			Cell.setCellType(1);
			Cell.setCellValue(Enname);
			Cell.setCellStyle(CellStype_Fill);
			sheet3.addMergedRegion(new Region((short)(iRow + JiRow), (short)(iCell + 7), (short)(iRow + JiRow), (short) (iCell + 9)));
			
			Cell = RowNew.createCell((short) (iCell + 8));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 9));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 10));
			Cell.setCellType(1);
			Cell.setCellValue(Entry.getValue());
			Cell.setCellStyle(CellStype);
			//System.out.println(" ----- Value : " + Entry.getValue());
			
			Cell = RowNew.createCell((short) (iCell + 11));
			Cell.setCellType(1);
			Cell.setCellFormula(Entry.getValue()+"/"+Odno);
			Cell.setCellStyle(CellStype_Baifb);
			
			JiRow ++;
		}
		
		
		// 港寶 小標題
		RowNew = sheet3.getRow(iRow);
		Cell = RowNew.createCell((short) iCell + 14);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
		Cell.setCellStyle(CellStype);
		sheet3.addMergedRegion(new Region((short)iRow, (short)(iCell + 14), (short)iRow, (short) (iCell + 16)));
					
		Cell = RowNew.createCell((short) (iCell + 15));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 16));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
				
		Cell = RowNew.createCell((short) (iCell + 17));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 18));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
		Cell.setCellStyle(CellStype);
		
		JiRow = 1;
		for(Entry<String, Integer> Entry : Port.entrySet()){
			
			String[] Cent = Entry.getKey().split(",");
			String Enname = Labels.getLabel("DSID.MSG0102")+"#Ms " + Cent[0] + "-" + Cent[Cent.length - 1];
			//System.out.println("Enname : " + Enname + " ----- Entry.getKey() : " + Entry.getKey() + " ----- Entry.getValue() : " + Entry.getValue());
			
			// 資料列印
			RowNew = sheet3.getRow(iRow + JiRow);
			Cell = RowNew.createCell((short) (iCell + 14));
			Cell.setCellType(1);
			Cell.setCellValue(Enname);
			Cell.setCellStyle(CellStype_Fill);
			sheet3.addMergedRegion(new Region((short)(iRow + JiRow), (short)(iCell + 14), (short)(iRow + JiRow), (short) (iCell + 16)));
			
			Cell = RowNew.createCell((short) (iCell + 15));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 16));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 17));
			Cell.setCellType(1);
			Cell.setCellValue(Entry.getValue());
			Cell.setCellStyle(CellStype);
			System.out.println(" ----- Value : " + Entry.getValue());
			
			Cell = RowNew.createCell((short) (iCell + 18));
			Cell.setCellType(1);
			Cell.setCellFormula(Entry.getValue()+"/"+Odno);
			Cell.setCellStyle(CellStype_Baifb);
			
			JiRow ++;
			
		}
		
		// 布標 小標題
		RowNew = sheet3.getRow(iRow + 15);
		Cell = RowNew.createCell((short) iCell);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
		Cell.setCellStyle(CellStype);
		sheet3.addMergedRegion(new Region((short)(iRow + 15), (short) iCell, (short)(iRow + 15), (short) (iCell + 2)));
					
		Cell = RowNew.createCell((short) (iCell + 1));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 2));
		Cell.setCellType(1);
		Cell.setCellStyle(CellStype);
				
		Cell = RowNew.createCell((short) (iCell + 3));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
		Cell.setCellStyle(CellStype);
					
		Cell = RowNew.createCell((short) (iCell + 4));
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0097"));
		Cell.setCellStyle(CellStype);
		
		JiRow = 16;
		for(Entry<String, Integer> Entry : Cloth.entrySet()){
			
			String[] Cent = Entry.getKey().split(",");
			String Enname = Labels.getLabel("DSID.MSG0103")+"#Ms " + Cent[0] + "-" + Cent[Cent.length - 1];
			//System.out.println("Enname : " + Enname + " ----- Entry.getKey() : " + Entry.getKey() + " ----- Entry.getValue() : " + Entry.getValue());
			
			// 資料列印
			RowNew = sheet3.getRow(iRow + JiRow);
			Cell = RowNew.createCell((short) iCell);
			Cell.setCellType(1);
			Cell.setCellValue(Enname);
			Cell.setCellStyle(CellStype_Fill);
			sheet3.addMergedRegion(new Region((short)(iRow + JiRow), (short) iCell, (short)(iRow + JiRow), (short) (iCell + 2)));
			
			Cell = RowNew.createCell((short) (iCell + 1));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 2));
			Cell.setCellType(1);
			Cell.setCellStyle(CellStype);
			
			Cell = RowNew.createCell((short) (iCell + 3));
			Cell.setCellType(1);
			Cell.setCellValue(Entry.getValue());
			Cell.setCellStyle(CellStype);
			System.out.println(" ----- Value : " + Entry.getValue());
			
			Cell = RowNew.createCell((short) (iCell + 4));
			Cell.setCellType(1);
			Cell.setCellFormula(Entry.getValue()+"/"+Odno);
			Cell.setCellStyle(CellStype_Baifb);
			
			JiRow ++;
			
		}

		long startTime4=System.currentTimeMillis();

		WovenCount(sheet3, conn, RowNew, Cell, iRow); // 布標分色統計
		
		long endTime4=System.currentTimeMillis();
		float excTime4=(float)(endTime4-startTime4)/1000;
		System.out.println("执行时间 布標分色："+excTime4+"s");
	}
	
	
	/**
	 * 布標分顏色統計
	 */
	private void WovenCount(HSSFSheet sheet3, Connection conn, HSSFRow RowNew, HSSFCell Cell, int iRow){
		
		ResultSet rs = null, rs2 = null;
		PreparedStatement ps = null, ps2 = null;
		int s = 1;
		
		iRow += 26;
		
		RowNew = sheet3.createRow(iRow);
		Cell = RowNew.createCell((short) 0);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		Cell.setCellValue("ToolingSize "+Labels.getLabel("DSID.MSG0181"));
		
		iRow ++;
		
		RowNew = sheet3.createRow(iRow);
		Cell = RowNew.createCell((short) 0);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0093"));
		
		// 尺碼表頭打印
		for(Double i = 3.0; i <= 15.5; i += 0.5){
			
			Cell = RowNew.createCell((short) s);
			Cell.setCellStyle(CellStype_10C);
			Cell.setCellType(1);
			Cell.setCellValue(i);
			
			s ++;
		}
		
		Cell = RowNew.createCell((short) s);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0182"));
		sheet3.addMergedRegion(new Region((short)iRow, (short)s, (short)iRow, (short) (s + 1)));
		
		Cell = RowNew.createCell((short) s+1);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		
		
		// 抓取配色名稱
		String Judge = "";
		List<String> QuerCo = QueryColor(conn, Judge);
		
		for(String Colon : QuerCo){
			
			iRow ++;
			RowNew = sheet3.createRow(iRow);
			s = 0;
			
			Cell = RowNew.createCell((short) s);
			Cell.setCellStyle(CellStype_10C);
			Cell.setCellType(1);
			Cell.setCellValue(Colon);
			s ++;
			
			for(Double i = 3.0; i <= 15.5; i += 0.5){
				
				String sql = "SELECT * FROM("
						+ "SELECT COUNT(*)AS coloc FROM dsid01 WHERE model_na like '%"+MODEL_NO+"' AND group1 LIKE '%"+Colon+"%' AND to_number(tooling_size) = "+i+" AND to_char(order_date,'yyyy/mm/dd') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'),("
						+ "SELECT count(*)AS sumcoloc FROM dsid01 WHERE model_na like '%"+MODEL_NO+"' AND group1 LIKE '%"+Colon+"%' AND to_char(order_date,'yyyy/mm/dd') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND to_number(tooling_size) IN (3.0,3.5,4.0,4.5,5.0,5.5,6.0,6.5,7.0,7.5,8.0,8.5,9.0,9.5,10.0,10.5,11.0,11.5,12.0,12.5,13.0,13.5,14.0,14.5,15.0,15.5))";
				//System.out.println(" —————————— 布標分色統計 : " + sql);
				
				try {
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					
					if(rs.next()){
						
						int Coloc = rs.getInt("coloc");
						int SumCo = rs.getInt("sumcoloc");
						
						//System.out.println(" ----- Coloc : " + Coloc + " ----- SumCo : " + SumCo);
						
						Cell = RowNew.createCell((short) s);
						Cell.setCellStyle(CellStype_10C);
						Cell.setCellType(1);
						if(rs.getInt("coloc") == 0){
							Cell.setCellValue("");
						}else{
							Cell.setCellValue(Coloc);
						}
						
						s ++;
						
						if( i >= 15.5){
							Cell = RowNew.createCell((short) s);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(SumCo);
							sheet3.addMergedRegion(new Region((short)iRow, (short)s, (short)iRow, (short) (s + 1)));
							
							Cell = RowNew.createCell((short) s+1);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							
						}
					}
					ps.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		s = 0;
		iRow ++;
		RowNew = sheet3.createRow(iRow);
		
		Cell = RowNew.createCell((short) s);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		Cell.setCellValue(Labels.getLabel("DSID.MSG0183"));
		
		s ++;
		
		for(Double i = 3.0; i <= 15.5; i += 0.5){
			
			String sql1 = "select count(*)as sizesum from dsid01 where model_na like '%"+MODEL_NO+"' and to_number(tooling_size) = "+i+" and to_char(order_date,'yyyy/mm/dd') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'";
//			System.out.println(" —————————— 布標底部統計 : " + sql1);
			
			try {
				ps2 = conn.prepareStatement(sql1);
				rs2 = ps2.executeQuery();
				
				if(rs2.next()){
					
					int SizeS = rs2.getInt("sizesum");
					
					Cell = RowNew.createCell((short) s);
					Cell.setCellStyle(CellStype_10C);
					Cell.setCellType(1);
					Cell.setCellValue(SizeS);
					
					s ++;
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Cell = RowNew.createCell((short) s);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		Cell.setCellFormula("SUM(B"+(iRow+1)+":AA"+(iRow+1)+")"); // Excel計算公式`
		sheet3.addMergedRegion(new Region((short)iRow, (short)s, (short)iRow, (short) (s + 1)));
		
		Cell = RowNew.createCell((short) s+1);
		Cell.setCellStyle(CellStype_10C);
		Cell.setCellType(1);
		
		
	}
	
	
	/**
	 * 勾勾分配色、分大中小 統計
	 */
	private void HookToo(HSSFSheet sheet, Connection conn, int rowNum, ResultSet rs, ResultSet rs1, PreparedStatement ps, PreparedStatement ps1){
		
		HSSFRow Row = sheet.createRow(0);
		HSSFCell Cell = Row.createCell(0);
		
		int iCell = 0, iRow = rowNum + 15 ;
		int Max = 0, Mln = 0, Min = 0;
		int Qsh = iRow; // 起始行變量
		String GGsize = "", GGcolor = "";
		
		String sql = "SELECT group1_na, group2_na, group3_na, group4_na, group5_na, group6_na, group7_na, group8_na, group9_na, group10_na, group11_na, group12_na, group13_na, group14_na, group15_na, group16_na, group17_na, group18_na, group19_na, group20_na FROM dsid10 WHERE nike_sh_aritcle = (SELECT distinct nike_sh_aritcle FROM dsid01 WHERE model_na = 'PEGASUS+35 MIN FA18 ID' AND nike_sh_aritcle IS NOT NULL) AND model_nas like '%PEGASUS+35 MIN FA18 ID%'";
//		System.out.println(" —————————— 勾勾查詢 : " + sql);
		
		try {
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()){ // 循環結果集
//				System.out.println(" —————————— 進入結果集 ");
				
				for(int i = 1; i <= 20; i++ ){ // 循環 20個Group_na
					
					if(rs.getString("group"+i+"_na") != null || !"".equals(rs.getString("group"+i+"_na"))){ // 否空判斷
						
//						System.out.println(" —————————— 結果集 Group"+i+"_na : " + rs.getString("group"+i+"_na"));
						
						if(rs.getString("group"+i+"_na") == "勾勾大小(MIN版)" || "勾勾大小(MIN版)".equals(rs.getString("group"+i+"_na"))){ // 判斷是否為需要的值
							
//							System.out.println(" —————————— Group - 2 : " + rs.getString("group"+i+"_na"));
							GGsize = rs.getString("group"+i+"_na"); // 勾勾大小
							
							Row = sheet.createRow(iRow);
							
							for(int y = 0; y < 10; y++){
								Cell = Row.createCell((short) y);
								Cell.setCellStyle(CellStype_10C);
								Cell.setCellType(1);
							}
							
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0105"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(GGsize);
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 6)));
							
							iRow++;
							Row = sheet.createRow(iRow);
							iCell = 0;
							
							for(int y = 0; y < 10; y++){
								Cell = Row.createCell((short) y);
								Cell.setCellStyle(CellStype_10C);
								Cell.setCellType(1);
							}
							
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0106"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0107"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0108"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0109"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0110"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							String Judge = "GGcolor";
							List<String> QueryCo = QueryColor(conn, Judge);
							
							// 遍歷配色
							for(String u : QueryCo){
								
								String sql1 = "SELECT * FROM ("
										+ "SELECT count(*)AS Max FROM dsid01 WHERE group2 = '大' AND group3 like '%"+u+"%' AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND model_na like '%"+MODEL_NO+"%'),("
										+ "SELECT count(*)AS Mln FROM dsid01 WHERE group2 = '中' AND group3 like '%"+u+"%' AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND model_na like '%"+MODEL_NO+"%'),("
										+ "SELECT count(*)AS Min FROM dsid01 WHERE group2 = '小' AND group3 like '%"+u+"%' AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"' AND model_na like '%"+MODEL_NO+"%')";
//								System.out.println(" —————————— 大中小數量查詢 : "+u+ " - " + sql);
								
								try {
									ps1 = conn.prepareStatement(sql1);
									rs1 = ps1.executeQuery();
									
									if(rs1.next()){
										
										iRow++;
										Row = sheet.createRow(iRow);
										iCell = 0;
										
										int Max1 = rs1.getInt("Max");
										int Mln1 = rs1.getInt("Mln");
										int Min1 = rs1.getInt("Min");
										
										for(int y = 0; y < 10; y++){// 單元格子打印
											Cell = Row.createCell((short) y);
											Cell.setCellStyle(CellStype_10C);
											Cell.setCellType(1);
										}
										
										Cell = Row.createCell((short) iCell);
										Cell.setCellStyle(CellStype_10C);
										Cell.setCellType(1);
										Cell.setCellValue(u);
										sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
										
										iCell += 2;
										Cell = Row.createCell((short) iCell);
										Cell.setCellStyle(CellStype_10C);
										Cell.setCellType(1);
										Cell.setCellValue(Max1);
										sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
										
										iCell += 2;
										Cell = Row.createCell((short) iCell);
										Cell.setCellStyle(CellStype_10C);
										Cell.setCellType(1);
										Cell.setCellValue(Mln1);
										sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
										
										iCell += 2;
										Cell = Row.createCell((short) iCell);
										Cell.setCellStyle(CellStype_10C);
										Cell.setCellType(1);
										Cell.setCellValue(Min1);
										sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
										
										iCell += 2;
										Cell = Row.createCell((short) iCell);
										Cell.setCellStyle(CellStype_10C);
										Cell.setCellType(1);
										Cell.setCellFormula("SUM(C"+(iRow +1)+":H"+(iRow +1)+")"); // Excel計算公式
										sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
										
									}
									
									ps1.close();
									rs1.close();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
									Messagebox.show(Labels.getLabel("DSID.MSG0111"));
								}
							}
							
							/** 底部統計 */
							iRow++;
							Row = sheet.createRow(iRow);
							iCell = 0;
							
							for(int c = 0; c < 10; c++){ // 空單元格打印
								Cell = Row.createCell((short) c);
								Cell.setCellStyle(CellStype_10C);
								Cell.setCellType(1);
							}
							
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellValue(Labels.getLabel("DSID.MSG0110"));
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellFormula("SUM(C"+(Qsh +3)+":D"+(iRow)+")");
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellFormula("SUM(E"+(Qsh +3)+":F"+(iRow)+")");
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellFormula("SUM(G"+(Qsh +3)+":H"+(iRow)+")");
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
							iCell += 2;
							Cell = Row.createCell((short) iCell);
							Cell.setCellStyle(CellStype_10C);
							Cell.setCellType(1);
							Cell.setCellFormula("SUM(I"+(Qsh +3)+":J"+(iRow)+")");
							sheet.addMergedRegion(new Region((short)iRow, (short)iCell, (short)iRow, (short) (iCell + 1)));
							
						}else{
//							System.err.println(" —————————— 查詢 無勾勾 —————————— ");
						}
					}
				}
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0113"));
		}
		
	}
	
	
	/**
	 * 查詢 配色名稱  去重
	 * @throws Exception
	 */
	private List<String> QueryColor (Connection conn, String Judge){
		
		String sqlJ = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> Colorlist = new ArrayList<String>();
		String Andsql0 = "", Andsql1 = "";
		String turn = "";
		String Subgro = "";
		
		if(Judge == "GGcolor" || "GGcolor".equals(Judge)){
			sqlJ = "group3";
			
		}else if(Judge == "Percen" || "Percen".equals(Judge)){
			sqlJ = GROUP_NA;
			
		}else{
			sqlJ = "group1";
			
		}
		
		String sql = "SELECT Distinct substr("+sqlJ+",1,3)as subgro FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"' ORDER BY subgro";
//		System.out.println(" ———————— 資料筆數查詢 : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()){
					
					Subgro = rs.getString("subgro");
					
					if(Subgro == null || "".equals(Subgro)){
						Messagebox.show(Labels.getLabel("DSID.MSG0121"));
					}
					
					Colorlist.add(Subgro);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0122"));
		}
		
		return Colorlist;
	}
	

	/**
	 * 當月接單總數量查詢
	 * @return Sumsize
	 */
	private int QueryOdno(Connection conn){
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		int Sumsize = 0;
		
		String sql = "SELECT COUNT(*)AS Sumsize FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"'";
//		System.out.println(" —————————— 月接單總數查詢 : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()){
				Sumsize = rs.getInt("Sumsize");
			}
					
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0123"));
		}
		return Sumsize;
	}
	
	
	protected void resetEditAreaMaster(Connection conn) {
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String Model;
		int Na = 1;
		
		String sql = "SELECT DISTINCT MODEL_NA FROM DSID01 WHERE MODEL_NA NOT LIKE 'W%'";
		System.out.println(" —————————— 型體名稱查詢 : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()){
				
				String Modelna = rs.getString("MODEL_NA");
				
				if(Na == 1){
					ModelNa1.setValue(Modelna);
				}else if(Na == 2){
					ModelNa2.setValue(Modelna);
				}else if(Na == 3){
					ModelNa3.setValue(Modelna);
				}else if(Na == 4){
					ModelNa4.setValue(Modelna);
				}else if(Na == 5){
					ModelNa5.setValue(Modelna);
				}
				
				Na++;
			}
			
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 型體分段信息查詢
	 */
	private List<String> QueryPoints(Connection conn, String Points){
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> Mslist = new ArrayList<String>();
		
		String Msder = "", Mssection = "", Msleng = "", Sms = "";
		String Andsql = "", Andsql1 = "";
		
		
		for(int i = 1; i <= 10; i++){

			if(Points == "前面片" || "前面片".equals(Points)){
				Andsql = "section"+i+" AS Mssection";
				Andsql1 = "AND segm_type = '前面片'";
//				System.out.println("-------------------------- 前麵片");
				
			}else{
				Andsql = "model_gender AS Msder, section"+i+" AS Mssection, length"+i+" AS Msleng";
				Andsql1 = "AND model_gender IS NOT NULL";
				
			}
			
			String sql = "SELECT "+Andsql+" FROM dsid11 WHERE model_nas = '"+MODEL_NO+"' "+Andsql1+"";
			//System.out.println(" —————————— 型體分段信息查詢 : " + sql);
			
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				if(rs.next()){
					
					if(Points == "前面片" || "前面片".equals(Points)){
						
						Mssection = rs.getString("Mssection");
						
						if(Mssection != null || "".equals(Mssection)){
							Mslist.add(Mssection);
						}
						
					}else{
						// 男鞋
						Mssection = rs.getString("Mssection");
						Msder = rs.getString("Msder"); 
						Msleng = rs.getString("Msleng");
						Sms = Mssection +"-"+ Msder +"-"+ Msleng;
						
						if(Msder != null && Mssection != null && Msleng != null){
							Mslist.add(Sms);
						}
					}
				}
			
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
/*		for(String o : Mslist){
			System.err.println(" Mslist : " + o);
		}*/
		Points = "";
		return Mslist;
	}
	
	
	/**
	 * 特定型體 配色查詢
	 * @return List<String>
	 */
	private List<String> QueryZdps(Connection conn){
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> Zdps = new ArrayList<String>();
		
		String Andsql = "", Subgro = "";
		
		if(MODEL_NO == "PEGASUS+35 MIN FA18 ID" || "PEGASUS+35 MIN FA18 ID".equals(MODEL_NO)){
			Andsql = "73P";
		}else if(MODEL_NO == "PEGASUS+35 ESS SU18 ID" || "PEGASUS+35 ESS SU18 ID".equals(MODEL_NO)){
			Andsql = "85Z";
		}
		
		String sql = "SELECT Distinct substr(group1,1,3)as subgro FROM dsid01 WHERE group1 LIKE '%"+Andsql+"%' AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"%' ORDER BY subgro";
//		System.out.println(" ——————————— 特定顏色名稱 " + sql );
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()){
				Subgro = rs.getString("subgro");
			}
			
			Zdps.add(Subgro);
			Zdps.add("10A");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		for(String s : Zdps){
			System.out.println("------------------- Zdps " + s);
		}*/
		return Zdps;
	}
	
	
	/**
	 * 鞋帶分段信息 查詢
	 * @return List<String>
	 */
	private List<String> QueryPercen(Connection conn){
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> Percen = new ArrayList<String>();
		String Msder = "", Mssection = "", Msleng = "", Sms = "";
		
		for(int i = 1; i <= 10; i++){
			
			String sql = "SELECT model_gender AS Msder, section"+i+" AS Mssection, length"+i+" AS Msleng FROM dsid11 WHERE model_nas = '"+MODEL_NO+"' AND model_gender IS NOT NULL";
//			System.out.println(" —————————— 鞋帶分段查詢");
			
			try {
				
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				if(rs.next()){
					Mssection = rs.getString("Mssection");
					Msder = rs.getString("Msder"); 
					Msleng = rs.getString("Msleng");
					Sms = Mssection +"-"+ Msder +"-"+ Msleng;
					
					if(Msder != null && Mssection != null && Msleng != null){
						Percen.add(Sms);
					}
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
/*	for(String o : Percen){
		System.err.println(" Percen : " + o);
	}*/
		return Percen;
	}
	
	/**
	 * 前掌氣墊、後掌氣墊、港寶、布標查詢
	 * @param conn
	 * @return
	 */
	private List<Map<String, Integer>> Cushion1(Connection conn){
		
		ResultSet rs = null, rs1 = null;
		PreparedStatement ps = null, ps1 = null;
		
		List<Map<String, Integer>> Cus = new ArrayList<Map<String, Integer>>();
		
		Map<String, Integer> BeforeMap = new HashMap<String, Integer>();
		Map<String, Integer> AfterMap = new HashMap<String, Integer>();
		Map<String, Integer> PortMap = new HashMap<String, Integer>();
		Map<String, Integer> ClothMap = new HashMap<String, Integer>();
		
		String Typena = "", Duan = "";
		int Toolsum = 0;
		
		for(int i = 1; i<= 10; i++){
			// 查詢固定 部件的分段信息
			String sql = "SELECT segm_type,section"+i+" FROM dsid11 WHERE segm_type IN ('前掌氣墊', '後掌氣墊', '港寶', '布標') AND section"+i+" IS NOT NULL";
//			System.out.println(" 第四頁 分段查詢 ----- : " + sql);
			
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				while(rs.next()){
					
					String Segm = rs.getString("segm_type");
					String Section = rs.getString("section" + i);
					
					if(Segm != null || "".equals(Segm) && Section != null || "".equals(Section)){
						
						String sql1 = "SELECT COUNT(tooling_size)AS tooling FROM dsid01 WHERE to_number(tooling_size) IN ("+Section+") AND to_char(order_date,'YYYY/MM/DD') BETWEEN '"+ORDER_DATE1+"' AND '"+ORDER_DATE2+"'  AND model_na like '%"+MODEL_NO+"%'";
//						System.out.println(" 分段數量查詢 ----- : " + sql1);
						
						try {
							
							ps1 = conn.prepareStatement(sql1);
							rs1 = ps1.executeQuery();
							
							if(rs1.next()){
								
								Toolsum = rs1.getInt("tooling");
//								System.out.println(" 數量 ----- : " + Toolsum + " 類型 ----- : " + Segm);
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						if(Segm == "前掌氣墊" || "前掌氣墊".equals(Segm)){
							BeforeMap.put(Section, Toolsum);
//							System.out.println(" 前掌氣墊封裝 Map ----- ");
						}
						
						if(Segm == "後掌氣墊" || "後掌氣墊".equals(Segm)){
							AfterMap.put(Section, Toolsum);
//							System.out.println(" 後掌氣墊封裝 Map ----- ");
						}
						
						if(Segm == "港寶" || "港寶".equals(Segm)){
							PortMap.put(Section, Toolsum);
//							System.out.println(" 港寶 Map ----- ");
						}
						
						if(Segm == "布標" || "布標".equals(Segm)){
							ClothMap.put(Section, Toolsum);
//							System.out.println(" 布標 Map ----- ");
						}
					}
				}
				rs1.close();
				ps1.close();
				rs.close();
				ps.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
/*		System.out.println(" 7 ----- ");
		for(Entry<String, Integer> s : BeforeMap.entrySet()){System.out.println(" BeforeMap ----- : " + s.getKey() + " --- " + s.getValue());}
		for(Entry<String, Integer> s : AfterMap.entrySet()){ System.out.println(" AfterMap ----- : "  + s.getKey() + " --- " + s.getValue());}
		for(Entry<String, Integer> s : PortMap.entrySet()){  System.out.println(" PortMap ----- : "   + s.getKey() + " --- " + s.getValue());}
		for(Entry<String, Integer> s : ClothMap.entrySet()){ System.out.println(" ClothMap ----- : "  + s.getKey() + " --- " + s.getValue());}
		System.out.println(" 8 ----- ");*/
		
		Cus.add(BeforeMap);
		Cus.add(AfterMap);
		Cus.add(PortMap);
		Cus.add(ClothMap);

		return Cus;
	}
	
	/** 繼承父類后,覆寫的方法 */
	
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
	
    public boolean isBselfDefinedFilename() {
		return bselfDefinedFilename;
	}
    
	public String getFilename() {
		return selffilename;
	}
	
}
