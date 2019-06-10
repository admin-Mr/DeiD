package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.hssf.util.Region;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import ds.dsid.domain.DSID01;
import util.Common;

public class DSID13_1RTask{
	static String model_naTemp = "";
	
	public static void ExcelExport(String MODEL_NA, String START,String PG_NAME) {
		
		String title="派工單報表";
		Connection Conn = getDB01Conn();
		Connection conn = Common.getDbConnection();
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		
		// 设置字体，表格的长宽高，
		HSSFFont font1 = wb.createFont();
		font1.setFontName("新細明體");    				//设置字體  
		font1.setFontHeightInPoints((short)10);    		//设置字体高度  
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//设置字體樣式 		style1	
		
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFont(font1);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style1.setFillPattern((short) 0);
		style1.setWrapText(true);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setFont(font1);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style2.setFillPattern((short) 0);
		style2.setWrapText(true);
		style2.setDataFormat(wb.createDataFormat().getFormat("0.0"));
		
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setFont(font1);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中    
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style3.setFillPattern((short) 0);
		style3.setWrapText(true);
		style3.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setFont(font1);
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中    
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style4.setFillPattern((short) 0);
		style4.setWrapText(true);
		style4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style4.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		
		String ExSql="";		
		if(!"".equals(MODEL_NA)){
			ExSql+=" AND MODEL_NA LIKE '%"+MODEL_NA+"'";
		}
		String ExSql1="";
		if(!"".equals(PG_NAME)){
			ExSql1+=""+PG_NAME+"";
		}
		PreparedStatement ps = null,ps1=null;
		ResultSet rs = null,rs1=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String 	sql="SELECT MODEL_NA FROM DSID01  WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+ExSql+" GROUP BY MODEL_NA";
		System.out.println(">>>>>"+sql);
		String sql1="SELECT * FROM DSID13 WHERE PG_NAME='"+ExSql1+"'";
		System.out.println(">>>>>>"+sql1);
		
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			ps1 = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();
			while(rs1.next() && rs.next()){
				if("轉印".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth(sheet);
					Exportexcel5(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("非轉印".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth(sheet);
					Exportexcel6(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("製成".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth(sheet);
					Exportexcel1(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				
				if("材料".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet2 = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth2(sheet2);
					Exportexcel2(wb,sheet2,MODEL_NA, START,rs1.getString("PG_NAME"), Conn, conn,style1,style2,style3,style4);
				}
				if("底部".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth3(sheet);
					Exportexcel3(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("部位".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth4(sheet);
					Exportexcel4(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("鞋帶".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					Exportexcel7(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("裁斷".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					Exportexcel8(wb,sheet,MODEL_NA, START,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
			}
			rs1.close();
			ps1.close();
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			wb.write(stream);
			byte[] content = stream.toByteArray();
		    InputStream is = new ByteArrayInputStream(content);

		    //儲存位置、名稱
			Filedownload.save(is,"application/xls",title);
			is.close();
			stream.flush();
			stream.close();
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Common.closeConnection(conn);
		Common.closeConnection(Conn);
	}
	//制成派工单 类型  ,导出至表中
	private static void Exportexcel1(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START, String PG_NAME,Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		PreparedStatement ps1=null;
		ResultSet rs1=null;
		List<String> list=GROUPSTATUS(PG_NAME,conn);
		Header1(sheet1,style1,row,MODEL_NA,PG_NAME, cell, conn,list);
		
		//設置表頭格式
		row = sheet1.getRow(0);
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		
		//接單的日期
		row = sheet1.getRow(1);
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(START);
		
		//數據從第三行開始
		int rowNum = 3;
		int str=1;
		//判斷形體是否相同
		String SQLI="";
		if(!"".equals(MODEL_NA)){
			SQLI="AND MODEL_NA LIKE '%"+MODEL_NA+"'";
		}
		//查詢DSID01表与时间的內容
		String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+SQLI+" ORDER BY ALL_ROUND_NUM,MODEL_ROUND_NUM";
		System.err.println(">>>>>"+sql1);
		try {
			ps1=conn.prepareStatement(sql1);
			rs1=ps1.executeQuery();
			//把需要的內容get到excel表中
			while(rs1.next()){
					row = sheet1.createRow(rowNum);
					row.setHeightInPoints(25);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(str++);
					
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("ROUND"));
					
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("OD_NO"));
						
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("MODEL_NA"));
					
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("ORDER_NUM"));
								
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("TOOLING_SIZE"));

					for(int i=1;i<list.size()+1;i++){
						cell = row.createCell(5+i);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString(list.get(i-1)));
					}
					//每加一條數據就自增一欄
					rowNum++;
					}
					//關閉流
					ps1.close();
					rs1.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	//制成派工单格式
	private static void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String MODEL_NA,String PG_NAME, HSSFCell cell,Connection conn, List<String> list) {
		// TODO Auto-generated method stub
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
		
		//設置表頭的形體
		row = sheet.createRow(0);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(MODEL_NA+"+"+PG_NAME);
		
		//查詢DSID13表中的GROUP內容
		String sql3="SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%"+MODEL_NA+"'";
		System.out.println("@@@@@@@@@@"+sql3);
		try {
		ps3= conn.prepareStatement(sql3);
		rs3= ps3.executeQuery();
		//把需要的欄位名稱設置到excle中
		while(rs3.next()){
			row = sheet.createRow(1);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("接單日期");
			
			row = sheet.createRow(2);
			row.setHeightInPoints(30);
		
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("項次");
			
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("輪次號");
		
			cell = row.createCell(2);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("訂單號");					
		
			cell = row.createCell(3);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("形體");	
		
			cell = row.createCell(4);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("數量");
		
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("TOOLING_SIZE");
			
			for(int i=1;i<list.size()+1;i++){
				cell = row.createCell(5+i);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(rs3.getString(list.get(i-1)+"_NA"));
			}
			System.err.println("GRUOP_NOA"+list);
			}
			//關閉流
			ps3.close();
			rs3.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	//泡棉派工單类型,导出至表中
	private static void Exportexcel2(HSSFWorkbook wb,HSSFSheet sheet2, String MODEL_NA,String START,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Header2(sheet2,style1,row,MODEL_NA,PG_NAME,START, cell, conn);
		
		//設置表頭格式
		row = sheet2.getRow(0);
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		//接單的日期
		row = sheet2.getRow(1);
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(START);
		
		//數據從第三行開始
		int rowNum = 3;
		//判斷形體是否相同
		String SQLI="";
		if(!"".equals(MODEL_NA)){
			SQLI="AND MODEL_NA LIKE'%"+MODEL_NA+"'";
		}
		//查詢DSID01表与时间的內容
		String sql="SELECT SUM(ORDER_NUM) SUM,TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD')='"+START+"'"+SQLI+" GROUP BY TOOLING_SIZE ORDER BY CAST(TOOLING_SIZE AS FLOAT)";
		System.err.println(">>>>>"+sql);
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			//把需要的內容get到excel表中
			while(rs.next()){
					row = sheet2.createRow(rowNum);
					row.setHeightInPoints(25);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs.getString("TOOLING_SIZE"));
					
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs.getString("SUM"));
					rowNum ++;
					}
					//關閉流
					ps.close();
					rs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	//泡棉派工單格式
	private static void Header2(HSSFSheet sheet2, HSSFCellStyle style1, HSSFRow row,String MODEL_NA,String PG_NAME,String START,HSSFCell cell,Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String num="";
		//相加鞋子的總數
		String 	sql="SELECT SUM(COU) COT FROM (SELECT SUM(ORDER_NUM) COU,TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD')= '"+START+"' AND MODEL_NA LIKE '%"+MODEL_NA+"' GROUP BY TOOLING_SIZE ORDER BY CAST(TOOLING_SIZE AS FLOAT))";
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				num=rs.getString("COT");
			}else{
				num="0";
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//設置表頭的形體
		row = sheet2.createRow(0);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(MODEL_NA+"+"+PG_NAME);
			//把需要的欄位名稱設置到excle中
			row = sheet2.createRow(1);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("接單日期");
			
			row = sheet2.createRow(2);
			row.setHeightInPoints(30);
		
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("TOOLING_SIZE");
			
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("數量");
			
			cell = row.createCell(2);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("总计：");
			
			cell = row.createCell(3);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(num+"雙");
			
			//關閉流
		}
	//底部派工单 类型  ,导出至表中
	private static void Exportexcel3(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
			// TODO Auto-generated method stub
			HSSFRow row = null;
			HSSFCell cell = null;
			PreparedStatement ps1=null;
			ResultSet rs1=null;
			List<String> list=GROUPSTATUS(PG_NAME,conn);
			Header3(sheet1,style1,row,MODEL_NA,PG_NAME, cell, conn,list);
			
			//設置表頭格式
			row = sheet1.getRow(0);
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			//接單的日期
			row = sheet1.getRow(4);
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(START);
			int str=1;
			//數據從第三行開始
			int rowNum = 6;
			//判斷形體是否相同
			String SQLI="";
			if(!"".equals(MODEL_NA)){
				SQLI="AND MODEL_NA LIKE '%"+MODEL_NA+"'";
			}
			//查詢DSID01表与时间的內容
			String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')= '"+START+"'" +SQLI+" ORDER BY TOOLING_SIZE+0 DESC";
			System.err.println(">>>>>"+sql1);
			try {
				ps1=conn.prepareStatement(sql1);
				rs1=ps1.executeQuery();
				//把需要的內容get到excel表中
				while(rs1.next()){
						row = sheet1.createRow(rowNum);
						row.setHeightInPoints(25);
						
						cell = row.createCell(0);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(str++);
						
						cell = row.createCell(1);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("WORK_ORDER_ID"));
						
						cell = row.createCell(2);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("ON_ORDER"));
							
						cell = row.createCell(3);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("TOOLING_SIZE"));
						
						cell = row.createCell(4);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("MODEL_NA"));
						
						cell = row.createCell(5);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("ORDER_NUM"));

						for(int i=1;i<list.size()+1;i++){
							cell = row.createCell(5+i);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString(list.get(i-1)));
						}
						//每加一條數據就自增一欄
						rowNum++;
						}
						//關閉流
						ps1.close();
						rs1.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	//底部派工单格式
	private static void Header3(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String MODEL_NA,String PG_NAME, HSSFCell cell,Connection conn, List<String> list) {
			// TODO Auto-generated method stub
			PreparedStatement ps3 = null;
			ResultSet rs3 = null;
			//設置表頭的形體
			row = sheet.createRow(0);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(MODEL_NA+"+"+PG_NAME);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("實際交貨日期");
			
			row = sheet.createRow(1);
			row.setHeightInPoints(30);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("預計交貨日期");
			
			row = sheet.createRow(2);
			row.setHeightInPoints(30);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("噴漆預交貨日期");
			
			row = sheet.createRow(3);
			row.setHeightInPoints(30);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("大底交貨日期");
			
			//查詢DSID13表中的GROUP內容
			String sql3="SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%"+MODEL_NA+"'";
			System.out.println("@@@@@@@@@@"+sql3);
			try {
			ps3= conn.prepareStatement(sql3);
			rs3= ps3.executeQuery();
			//把需要的欄位名稱設置到excle中
			while(rs3.next()){
				row = sheet.createRow(4);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("接單日期");
				
				row = sheet.createRow(5);
				row.setHeightInPoints(30);
			
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("項次");
			
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("ORDER_ID");					
			
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("流水號");	
			
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("模具碼");
			
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("形體");
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("數量");
				
				for(int i=1;i<list.size()+1;i++){
					cell = row.createCell(5+i);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs3.getString(list.get(i-1)+"_NA"));
				}
				System.err.println("GRUOP_NOA"+list);
				}
				//關閉流
				ps3.close();
				rs3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	//部位派工单 类型  ,导出至表中
	@SuppressWarnings("deprecation")
	private static void Exportexcel4(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4)throws IOException {

		HSSFRow row1 = sheet1.createRow(0);
		HSSFCell cell1 = row1.createCell(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		PreparedStatement ps = null, ps2 = null, ps3 = null, ps1=null,ps4=null;
		ResultSet rs = null, rs2 = null, rs3 = null,rs1=null,rs4=null;
		Map<Object, Integer> SizeMap = new HashMap<Object, Integer>();
		Map<Double, Object> CellMap = new HashMap<Double, Object>();
		List<String> list=GROUPSTATUS(PG_NAME,conn);
		String Subgroup = "";
		Double Sizerun = 0.0;
		int Sumsize = 0;
		int rowNum = 3; // 行數基數
		int CellNum = 1; // 列基數
		Double Sizes = 3.0; // Size疊加

		//設置表頭的形體
		row = sheet1.createRow(0);
		row.setHeightInPoints(25);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(MODEL_NA+"+"+PG_NAME);
		
		// 設置單元格內容
		row = sheet1.createRow(1);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("接單日期");
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(START);
		
		row = sheet1.createRow(2);
		row.setHeightInPoints(30);
	
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("求和項：數量");
	
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("模具碼");

		row1 = sheet1.createRow(rowNum++);
		row1.setHeightInPoints(25);
//		判斷形體是否相同
		String SQLI=" ";
		if(!"".equals(MODEL_NA)){
			SQLI="AND MODEL_NA LIKE '%"+MODEL_NA+"'";
		}
		String sty= null;
		for(int i=0;i<list.size();i++){
			sty=list.get(i);
		}
//		查詢DSID01表与时间的內容
		String sql1="SELECT DISTINCT "+sty+" FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')= '"+START+"'"+SQLI;
		System.err.println(">>>>>@@@@@@我在这里"+sql1);
			try {
				ps1= conn.prepareStatement(sql1);
				rs1= ps1.executeQuery();
				while(rs1.next()){
					String[] str=rs1.getString(sty).split(" ");
					for(int i=0;i<str.length;i++){
						list.add(str[i]);
					}
				
				}
					System.out.println("list@@@@@@@"+list);
				rs1.close();
				ps1.close();
			} catch (Exception e){
			}
			
			String sql4="SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%"+MODEL_NA+"'";
			try {
				ps4= conn.prepareStatement(sql4);
				rs4= ps4.executeQuery();
				//把需要的欄位名稱設置到excle中
				while(rs4.next()){
						row = sheet1.createRow(3);
						row.setHeightInPoints(30);
					
						cell = row.createCell(0);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs4.getString(sty+"_NA"));
					}
					//關閉流
					ps4.close();
					rs4.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

		for (int i = 1; i < 27; i++) {
			cell1 = row1.createCell(i);
			cell1.setCellType(1);
			cell1.setCellValue(Sizes);
			cell1.setCellStyle(style1);
			sheet1.setColumnWidth((short) i, (short) (7.4 * 260));
			Sizes += 0.5;
		}

		cell1 = row1.createCell((short) 27);
		cell1.setCellType(1);
		cell1.setCellValue("總計結果");
		cell1.setCellStyle(style1);
		sheet1.setColumnWidth((short) 27, (short) (9.5 * 260));

			
			
		for (Double i = 3.0; i <= 15.5; i += 0.5) {
			CellMap.put(i, CellNum++);
		}

		// 查詢資料筆數 用於循環控制
		// 獲取 配色名稱
		for (int x = 1; x < list.size(); x++) {
			row1 = sheet1.createRow(rowNum++);
			row1.setHeightInPoints(25);
			// 單元格邊框線打印
			for (int i = 1; i < 27; i++) {
				cell1 = row1.createCell((short) i);
				cell1.setCellStyle(style1);
				cell1.setCellType(1);
			}
			cell = row1.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(list.get(x));
			// 查詢個配色 總數量
			String sql3 = "SELECT COUNT(tooling_size)AS sizerun "
					+ "FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD')= '" + START + "'"
					+ "AND "+sty+" LIKE '" + list.get(x) + "%' AND model_na like '%" + MODEL_NA
					+ "'";
			 System.out.println(" ———————— Group : " + sql3);

			try {
				ps3 = conn.prepareStatement(sql3);
				rs3 = ps3.executeQuery();

				while (rs3.next()) { // 右側打印個配色總數量
					String SizeRun = rs3.getString("sizerun");

					cell1 = row1.createCell((short) 27);
					cell1.setCellType(1);
					cell1.setCellStyle(style1);
					cell1.setCellValue(Integer.valueOf(SizeRun));
					// System.out.println(" ———————— 右側總計結果 : " +
					// rs3.getInt("sizerun"));
				}
				ps3.close();
				rs3.close();
			} catch (SQLException e) {
				Messagebox.show("右側總計結果失敗！！！");
				e.printStackTrace();
			}
			// 配色各個尺碼 數據查詢
			String sql = "SELECT DISTINCT COUNT(*)COU ,"+sty+",TOOLING_SIZE FROM DSID01 WHERE tooling_size IN (3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5)AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' AND "
					+"MODEL_NA LIKE '%"+MODEL_NA+"' GROUP BY "+sty+",TOOLING_SIZE ORDER BY "+sty+",TO_NUMBER(TOOLING_SIZE)";
			 System.out.println(" @@@@@—————— 資料查詢 : " + sql);

			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();

				while (rs.next()) {

					Subgroup = rs.getString(sty); // 顏色編號
					Sizerun = rs.getDouble("TOOLING_SIZE"); // Size碼
					Sumsize = rs.getInt("COU"); // Size數量

					if (Subgroup == list.get(x) || Subgroup.equals(list.get(x))) {
						for (Double j = 3.0; j <= 15.5; j += 0.5) { // Size碼編號循環
							if (Sizerun - j == 0.0) {
								int Celll = (int) CellMap.get(j);
								cell1 = row1.createCell((short) Celll);
								cell1.setCellStyle(style1);
								cell1.setCellType(1);
								cell1.setCellValue(Sumsize);
							}
						}
					}
				}
				ps.close();
				rs.close();
			} catch (SQLException e) {
				Messagebox.show("資料導出錯誤！！！");
				e.printStackTrace();
			}
		}

		row1 = sheet1.createRow(rowNum++);
		row1.setHeightInPoints(25);
		cell1 = row1.createCell((short) 0);
		cell1.setCellType(1);
		cell1.setCellValue("總計結果");
		cell1.setCellStyle(style1);

		// 空單元格邊框打印
		for (int i = 1; i < 28; i++) {
			cell1 = row1.createCell((short) i);
			cell1.setCellStyle(style1);
			cell1.setCellType(1);
		}
		// 底部總計結果
		String sql2 = "SELECT COUNT(group1)as sizes, tooling_size "
				+ "FROM dsid01 WHERE to_char(order_date,'YYYY/MM/DD')= '" +START+ "'"
				+ "AND tooling_size IN (3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5) "
				+ "AND model_na like '%" + MODEL_NA + "' GROUP BY tooling_size";
		// System.out.println(" ———————— SizeMap : " + sql2);
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				double ToSize = rs2.getDouble("tooling_size");
				int Sizesum = rs2.getInt("sizes");

				SizeMap.put(ToSize, Sizesum);
			}
			// System.out.println(" ———————— SizeMap : " + SizeMap);
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			Messagebox.show("底部總計結果失敗！！！");
			e.printStackTrace();
		}

		int SizeNumber = 0, Cells = 0;
		Map<Integer, Object> foMap = new HashMap<Integer, Object>(); // foMap
																		// 裝入與Size碼對應
																		// 列號
																		// 便於打印
		int cells = 0;
		for (Double i = 3.0; i <= 15.5; i += 0.5) {
			foMap.put(cells += 1, i);
		}

		Set<Entry<Integer, Object>> foentries = foMap.entrySet();
		Set<Entry<Object, Integer>> entries = SizeMap.entrySet(); // 遍歷Map集合,
																	// 拿到Key值,
																	// Key為Size碼

		for (Entry<Object, Integer> entry : entries) { // 遍歷SizeMap<Size碼, 總數量 >
														// 取到Size碼與 總數量
			for (Entry<Integer, Object> foentry : foentries) { // 遍歷循環控制Map<1-26cell,
																// Size碼>
				Double enKey = (Double) entry.getKey();
				Double foValue = (Double) foentry.getValue();
				if (enKey == foValue || enKey.equals(foValue)) { // 判斷SizeMap的Size

					SizeNumber = entry.getValue(); // 獲得對應Size下的總數量
					Cells = foentry.getKey(); // 獲取列號
					// 打印底部總數量
					cell1 = row1.createCell((short) Cells);
					cell1.setCellStyle(style1);
					cell1.setCellType(1);
					cell1.setCellValue(SizeNumber);
				}
			}
		}
		cell1 = row1.createCell((short) 27);
		cell1.setCellStyle(style1);
		cell1.setCellType(1);
		cell1.setCellFormula("SUM(AB3:AB" + (rowNum - 1) + ")"); // Excel計算公式

		}
	//制成派工单 类型 (帶PID信息+帶值),导出至表中
	private static void Exportexcel5(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START, String PG_NAME,Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
			// TODO Auto-generated method stub
			HSSFRow row = null;
			HSSFCell cell = null;
			PreparedStatement ps1=null;
			ResultSet rs1=null;
			List<String> list=GROUPSTATUS(PG_NAME,conn);
			Header5(sheet1,style1,row,MODEL_NA,PG_NAME, cell, conn,list);
			
			//設置表頭格式
			row = sheet1.getRow(0);
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			//接單的日期
			row = sheet1.getRow(1);
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(START);
			
			//數據從第三行開始
			int rowNum = 3;
			int str=1;
			//判斷形體是否相同
			String SQLI="";
			if(!"".equals(MODEL_NA)){
				SQLI="AND MODEL_NA LIKE '%"+MODEL_NA+"'";
			}
			//查詢DSID01表与时间的內容
			String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+SQLI+"AND PID01 IS NOT NULL ORDER BY ALL_ROUND_NUM,MODEL_ROUND_NUM";
			System.err.println(">>>>>"+sql1);
			try {
				ps1=conn.prepareStatement(sql1);
				rs1=ps1.executeQuery();
				//把需要的內容get到excel表中
				while(rs1.next()){
						row = sheet1.createRow(rowNum);
						row.setHeightInPoints(25);
						
						cell = row.createCell(0);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(str++);
						
						cell = row.createCell(1);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("ROUND"));
						
						cell = row.createCell(2);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("OD_NO"));
							
						cell = row.createCell(3);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("MODEL_NA"));
						
						cell = row.createCell(4);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("ORDER_NUM"));
									
						cell = row.createCell(5);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("TOOLING_SIZE"));
						
						cell = row.createCell(6);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("PID01"));
						
						cell = row.createCell(7);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("PID02"));

						for(int i=1;i<list.size()+1;i++){
							cell = row.createCell(7+i);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString(list.get(i-1)));
						}
						//每加一條數據就自增一欄
						rowNum++;
						}
						//關閉流
						ps1.close();
						rs1.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	//制成派工单格式 (帶PID信息+帶值)
	private static void Header5(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String MODEL_NA,String PG_NAME, HSSFCell cell,Connection conn, List<String> list) {
			// TODO Auto-generated method stub
			PreparedStatement ps3 = null;
			ResultSet rs3 = null;
			
			//設置表頭的形體
			row = sheet.createRow(0);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(MODEL_NA+"+"+PG_NAME);
			
			//查詢DSID13表中的GROUP內容
			String sql3="SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%"+MODEL_NA+"'";
			System.out.println("@@@@@@@@@@"+sql3);
			try {
			ps3= conn.prepareStatement(sql3);
			rs3= ps3.executeQuery();
			//把需要的欄位名稱設置到excle中
			while(rs3.next()){
				row = sheet.createRow(1);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("接單日期");
				
				row = sheet.createRow(2);
				row.setHeightInPoints(30);
			
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("項次");
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("輪次號");
			
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("訂單號");					
			
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("形體");	
			
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("數量");
			
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("TOOLING_SIZE");
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("LEFT PID");
				
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("RIGHT PID");
				
				for(int i=1;i<list.size()+1;i++){
					cell = row.createCell(7+i);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs3.getString(list.get(i-1)+"_NA"));
				}
				System.err.println("GRUOP_NOA"+list);
				}
				//關閉流
				ps3.close();
				rs3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	//制成派工单 类型 (帶PID信息+不帶值),导出至表中
	private static void Exportexcel6(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START, String PG_NAME,Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
				// TODO Auto-generated method stub
				HSSFRow row = null;
				HSSFCell cell = null;
				PreparedStatement ps1=null;
				ResultSet rs1=null;
				List<String> list=GROUPSTATUS(PG_NAME,conn);
				Header6(sheet1,style1,row,MODEL_NA,PG_NAME, cell, conn,list);
				
				//設置表頭格式
				row = sheet1.getRow(0);
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
				
				//接單的日期
				row = sheet1.getRow(1);
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(START);
				
				//數據從第三行開始
				int rowNum = 3;
				int str=1;
				//判斷形體是否相同
				String SQLI="";
				if(!"".equals(MODEL_NA)){
					SQLI="AND MODEL_NA LIKE '%"+MODEL_NA+"'";
				}
				//查詢DSID01表与时间的內容
				String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+SQLI+" AND PID01 IS NULL ORDER BY ALL_ROUND_NUM,MODEL_ROUND_NUM";
				System.err.println(">>>>>"+sql1);
				try {
					ps1=conn.prepareStatement(sql1);
					rs1=ps1.executeQuery();
					//把需要的內容get到excel表中
					while(rs1.next()){
							row = sheet1.createRow(rowNum);
							row.setHeightInPoints(25);
							
							cell = row.createCell(0);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(str++);
							
							cell = row.createCell(1);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("ROUND"));
							
							cell = row.createCell(2);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("OD_NO"));
								
							cell = row.createCell(3);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("MODEL_NA"));
							
							cell = row.createCell(4);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("ORDER_NUM"));
										
							cell = row.createCell(5);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("TOOLING_SIZE"));
							
							cell = row.createCell(6);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("PID01"));
							
							cell = row.createCell(7);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("PID02"));

							for(int i=1;i<list.size()+1;i++){
								cell = row.createCell(7+i);
								cell.setCellType(1);
								cell.setCellStyle(style1);
								cell.setCellValue(rs1.getString(list.get(i-1)));
							}
							//每加一條數據就自增一欄
							rowNum++;
							}
							//關閉流
							ps1.close();
							rs1.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
			}
	//制成派工单格式 (帶PID信息+不帶值)
	private static void Header6(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String MODEL_NA,String PG_NAME, HSSFCell cell,Connection conn, List<String> list) {
				// TODO Auto-generated method stub
				PreparedStatement ps3 = null;
				ResultSet rs3 = null;
				
				//設置表頭的形體
				row = sheet.createRow(0);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(MODEL_NA+"+"+PG_NAME);
				
				//查詢DSID13表中的GROUP內容
				String sql3="SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%"+MODEL_NA+"'";
				System.out.println("@@@@@@@@@@"+sql3);
				try {
				ps3= conn.prepareStatement(sql3);
				rs3= ps3.executeQuery();
				//把需要的欄位名稱設置到excle中
				while(rs3.next()){
					row = sheet.createRow(1);
					row.setHeightInPoints(30);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("接單日期");
					
					row = sheet.createRow(2);
					row.setHeightInPoints(30);
				
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("項次");
					
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("輪次號");
				
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("訂單號");					
				
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("形體");	
				
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("數量");
				
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("TOOLING_SIZE");
					
					cell = row.createCell(6);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("LEFT PID");
					
					cell = row.createCell(7);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue("RIGHT PID");
					
					for(int i=1;i<list.size()+1;i++){
						cell = row.createCell(7+i);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs3.getString(list.get(i-1)+"_NA"));
					}
					System.err.println("GRUOP_NOA"+list);
					}
					//關閉流
					ps3.close();
					rs3.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
	//鞋帶派工单 类型  ,导出至表中
	private static void Exportexcel7(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START, String PG_NAME,Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
		// TODO Auto-generated method stub
				HSSFRow row = null;
				HSSFCell cell = null;
				PreparedStatement ps1=null;
				ResultSet rs1=null;
				List<String> list=GROUPSTATUS(PG_NAME,conn);
				List<String> list1=TOOLINGSIZE(MODEL_NA,conn,START);
				List<String> list3=length(MODEL_NA,conn,START);
//				Header7(sheet1,style1,row,MODEL_NA,PG_NAME, cell, conn,list);
				System.out.println(">>>>>2"+list3);
				String sty= null;
				for(int i=0;i<list.size();i++){
					sty=list.get(i);
				}
				
				row = sheet1.createRow(0);
				row.setHeightInPoints(30);
				//設置表頭格式
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellValue(MODEL_NA+"+"+PG_NAME);
				cell.setCellStyle(style1);
				sheet1.addMergedRegion(new CellRangeAddress(0,0,0,4));
				
				//接單的日期
				row = sheet1.createRow(1);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("接單日期");
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(START);
				
				int mun=-2,mun1=0,mun2=0,mun3=1;
				
				row = sheet1.createRow(2);
				row.setHeightInPoints(30);
				
				for(int i=0;i<list3.size();i++){
					mun+=2;
					mun1+=3;
					mun2+=3;
					mun3+=3;
					cell = row.createCell(mun+i);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(list3.get(i)+" CM");
					sheet1.addMergedRegion(new CellRangeAddress(2,2,mun2-3,mun3-3));
					
					cell = row.createCell(mun3-3);
					cell.setCellStyle(style1);
				}
				
				
				for (int i = 3; i <= 30; i++) {
					row = sheet1.createRow(i);
				}
				//數據從第三行開始
				int rowNum1 =3,cellNum=-3;
				//判斷形體是否相同
				String SQLI="",last_ty="";
				if(!"".equals(MODEL_NA)){
					SQLI="AND MODEL_NA IN ('"+MODEL_NA+"','W "+MODEL_NA+"')";
				}
				//查詢DSID01表与时间的內容
				String exsql="";
				for (int i=0;i<list1.size();i++){
					exsql+="WHEN LEFT_SIZE >="+list1.get(i).substring(0,list1.get(i).indexOf("-"))+" AND LEFT_SIZE <= "+list1.get(i).substring(list1.get(i).indexOf("-")+1,list1.get(i).indexOf("#"))+" THEN '"+(i+1)+"'";
					
				}
//				System.out.println("少了這個sql"+exsql);
				String sql="SELECT B.COLOR,B.TY,COUNT(*) COU FROM ("
							+ "SELECT A.*,("
							+ "CASE "+exsql
							+ "END) AS TY  FROM ("
							+ "SELECT CASE INSTR(MODEL_NA,'W ') WHEN 1 THEN TO_NUMBER(LEFT_SIZE_RUN)-1.5 "
							+ "ELSE  TO_NUMBER(LEFT_SIZE_RUN) END LEFT_SIZE,"+sty+" COLOR "
							+ "FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+SQLI+" )A ) B "
							+ "GROUP BY B.COLOR,B.TY ORDER BY B.TY,B.COLOR";
				System.out.println("sssssss>>>>>"+sql);
				try {
					ps1=conn.prepareStatement(sql);
					rs1=ps1.executeQuery();
					//把需要的內容get到excel表中
					while(rs1.next()){
						if(last_ty.compareTo(rs1.getString("TY"))!=0){			
							rowNum1=3;
							cellNum+=3;
						}
						
							last_ty=rs1.getString("TY");
							
							row = sheet1.getRow((rowNum1));
							row.setHeightInPoints(25);

							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("COLOR"));
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString("COU"));
							rowNum1++;
					}
							//關閉流
							ps1.close();
							rs1.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
				

				}
	//裁斷派工單
	private static void Exportexcel8(HSSFWorkbook wb,HSSFSheet sheet1, String MODEL_NA,String START, String PG_NAME,Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) throws SQLException {
		//對應關係（X*1270）~~ 5*X-0.75
		boolean saveOrUpdate=false;
		int rowNum1 = 0,rowNum2 = 0,rowNum3 = 0,rowNum4 = 0;
		int index=0;
		sheet1.setColumnWidth((short)0, (short)(4.35*1270));
		sheet1.setColumnWidth((short)1, (short)(1.35*1270));
		sheet1.setColumnWidth((short)2, (short)(2.35*1270));
		sheet1.setColumnWidth((short)3, (short)(0.35*1270));
		
		sheet1.setColumnWidth((short)4, (short)(4.35*1270));
		sheet1.setColumnWidth((short)5, (short)(1.35*1270));
		sheet1.setColumnWidth((short)6, (short)(2.35*1270));
		sheet1.setColumnWidth((short)7, (short)(0.35*1270));
		
		sheet1.setColumnWidth((short)8, (short)(4.35*1270));
		sheet1.setColumnWidth((short)9, (short)(1.35*1270));
		sheet1.setColumnWidth((short)10, (short)(2.35*1270));
		sheet1.setColumnWidth((short)11, (short)(0.35*1270));
		
		sheet1.setColumnWidth((short)12, (short)(4.35*1270));
		sheet1.setColumnWidth((short)13, (short)(1.35*1270));
		sheet1.setColumnWidth((short)14, (short)(2.35*1270));
		
		short thin=HSSFCellStyle.BORDER_THIN;
		HSSFFont Fout1=wb.createFont();
		Fout1.setFontHeightInPoints((short)16);
		Fout1.setFontName("新細明體");
		Fout1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle Style1 = wb.createCellStyle();
		Style1.setFont(Fout1);
		Style1.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style1.setBorderBottom(thin);
		Style1.setBorderRight(thin);
		Style1.setBorderTop(thin);
		Style1.setBorderLeft(thin);
		Style1.setWrapText(true);
		
		HSSFFont Fout2=wb.createFont();
		Fout2.setFontHeightInPoints((short)10);
		Fout2.setFontName("新細明體");
		Fout2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle Style2 = wb.createCellStyle();
		Style2.setFont(Fout2);
		Style2.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style2.setBorderBottom(thin);
		Style2.setBorderRight(thin);
		Style2.setBorderTop(thin);
		Style2.setBorderLeft(thin);
		Style2.setWrapText(true);
		
		HSSFFont Fout3=wb.createFont();
		Fout3.setFontHeightInPoints((short)9);
		Fout3.setFontName("新細明體");
		Fout3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFCellStyle Style3 = wb.createCellStyle();
		Style3.setFont(Fout3);
		Style3.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style3.setBorderBottom(thin);
		Style3.setBorderRight(thin);
		Style3.setBorderTop(thin);
		Style3.setBorderLeft(thin);
		Style3.setWrapText(true);
		// TODO Auto-generated method stub
		PreparedStatement ps1=null;
		ResultSet rs1=null;
		List<String> list=GROUPSTATUS(PG_NAME,conn);
		List<String> list3=length(MODEL_NA,conn,START);
		List<String> filterHeader_list = getSheet2(MODEL_NA,START, conn);
		System.out.println(">>>>>2"+list3);
		String sty= null,sry=null;
		for(int i=0;i<list.size();i++){
			sty=list.get(i);
		}
		String sql1="SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%"+MODEL_NA+"'";
		try {
			ps1= conn.prepareStatement(sql1);
			rs1= ps1.executeQuery();
			//把需要的欄位名稱設置到excle中
			while(rs1.next()){
					sry=rs1.getString(sty+"_NA");
				}
				//關閉流
				ps1.close();
				rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		int cellIndex=0;
		int check=0;
			for (int j =0; j <filterHeader_list.size(); j++) {
				check++;
				if(check%4==1){
					cellIndex=0;
					
					index++;
					
					System.out.println(">>>"+START+","+START+"倫次號"+filterHeader_list.get(j).toString()+","+sty);
					List<String> listr1 =getID30List(START, filterHeader_list.get(j).toString(), sty,conn);
					
					HSSFRow row = null;
					HSSFCell cell = null;	
					System.err.println(">>>"+rowNum1);
					
					row = sheet1.createRow(rowNum1);
					row.setHeight((short)(36*20));
					String model_na =getmodel_na(filterHeader_list.get(j).toString(), START, START, conn);
					cell = row.createCell((short)cellIndex);
					sheet1.addMergedRegion(new Region(rowNum1,(short)cellIndex,rowNum1,(short)(cellIndex+2))); 
					cell.setCellStyle(Style1);
					cell.setCellValue(model_na+" 第"+filterHeader_list.get(j).toString()+"輪");
					
					cell = row.createCell((short)(cellIndex+1));
					cell.setCellStyle(Style1);
					cell.setCellValue("");
					
					cell = row.createCell((short)(cellIndex+2));
					cell.setCellStyle(Style1);
					cell.setCellValue("");
					
					rowNum1++;
					row = sheet1.createRow(rowNum1);
					sheet1.addMergedRegion(new Region(rowNum1,(short)cellIndex,rowNum1,(short)(cellIndex+1))); 
					row.setHeight((short)(30*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue("部位: "+sry);
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style2);
//						String groupna=getgroupna(group, model_na);						
//						cell.setCellValue(groupna);
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("資料日期：\n"+START);
					
					rowNum1++;
					row = sheet1.createRow(rowNum1);
					row.setHeight((short)(24*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue("顏色");
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style3);
					cell.setCellValue("Tooling SIZE");
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("數量");
					for (int i = 0; i < listr1.size(); i++) {
						String[] s = listr1.get(i).split("=");
						String color=s[0];
						color =changegroup(sty, model_na, color);
					
						rowNum1++;
						row = sheet1.createRow(rowNum1);
						row.setHeight((short)(24*20));
						cell = row.createCell((short)cellIndex);
						
						cell.setCellStyle(Style2);
						cell.setCellValue(color.replace("--", ""));
						System.out.println(">>>"+color);
					
						cell = row.createCell((short)(cellIndex+1));
						
						cell.setCellStyle(Style1);
						cell.setCellValue(s[1]);
						System.out.println(">>>"+s[1]);
						
						cell = row.createCell((short)(cellIndex+2));
						
						cell.setCellStyle(Style1);
						cell.setCellValue(s[2]);
						System.out.println(s[0]+s[1]+s[2]+s[3]);
						System.out.println(">>>"+s[2]);
						
					
					}
					//固定行高
					for (int i = 0; i < 8-listr1.size(); i++) {
					
						rowNum1++;
						row = sheet1.createRow(rowNum1);
						row.setHeight((short)(24*20));

					}
					

				rowNum1=rowNum1+(12-rowNum1%12);
				System.out.println("======="+rowNum1);
				
					    
				}
				if(check%4==2){
					
				cellIndex=4;
				
				index++;
				
				System.out.println(">>>"+START+","+START+"倫次號"+filterHeader_list.get(j).toString()+","+sty);
				List<String> listr2 =getID30List(START, filterHeader_list.get(j).toString(), sty,conn);
				
				HSSFRow row = null;
				HSSFCell cell = null;	
				System.err.println(">>>"+rowNum2);
				
				row = sheet1.createRow(rowNum2);
				row.setHeight((short)(48*20));
				String model_na =getmodel_na(filterHeader_list.get(j).toString(),START, START, conn);
				cell = row.createCell((short)cellIndex);
				
				sheet1.addMergedRegion(new Region(rowNum2,(short)cellIndex,rowNum2,(short)(cellIndex+2))); 
				cell.setCellStyle(Style1);
				cell.setCellValue(model_na+" 第"+filterHeader_list.get(j).toString()+"輪");
				
				cell = row.createCell((short)(cellIndex+1));
				
				cell.setCellStyle(Style2);
				cell.setCellValue("");
				
				cell = row.createCell((short)(cellIndex+2));
				
				cell.setCellStyle(Style2);
				cell.setCellValue("");
				
				rowNum2++;
				row = sheet1.createRow(rowNum2);
				sheet1.addMergedRegion(new Region(rowNum2,(short)cellIndex,rowNum2,(short)(cellIndex+1))); 
				row.setHeight((short)(30*20));
				cell = row.createCell((short)cellIndex);
				
				cell.setCellStyle(Style2);
				cell.setCellValue("部位: "+sry);
				
				cell = row.createCell((short)(cellIndex+1));
				
				cell.setCellStyle(Style2);
//					String groupna=getgroupna(group, model_na);
//					cell.setCellValue(groupna);
				
				cell = row.createCell((short)(cellIndex+2));
				
				cell.setCellStyle(Style2);
				cell.setCellValue("資料日期：\n"+START);
				
				rowNum2++;
				row = sheet1.createRow(rowNum2);
				row.setHeight((short)(24*20));
				cell = row.createCell((short)cellIndex);
				
				cell.setCellStyle(Style2);
				cell.setCellValue("顏色");
				
				cell = row.createCell((short)(cellIndex+1));
				
				cell.setCellStyle(Style3);
				cell.setCellValue("Tooling SIZE");
				
				cell = row.createCell((short)(cellIndex+2));
				
				cell.setCellStyle(Style2);
				cell.setCellValue("數量");
				for (int i = 0; i < listr2.size(); i++) {
					String[] s = listr2.get(i).split("=");
					String color=s[0];;
					color =changegroup(sty, model_na, color);
					
					rowNum2++;
					row = sheet1.createRow(rowNum2);
					row.setHeight((short)(24*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue(color.replace("--", ""));
					System.out.println(">>>"+s[0]);
				
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style1);
					cell.setCellValue(s[1]);
					System.out.println(">>>"+s[1]);
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style1);
					cell.setCellValue(s[2]);
					System.out.println(s[0]+s[1]+s[2]+s[3]);
					System.out.println(">>>"+s[2]);
					
				
				}
				for (int i = 0; i < 8-listr2.size(); i++) {
				
					rowNum2++;
					row = sheet1.createRow(rowNum2);
					row.setHeight((short)(24*20));

				}
				

			rowNum2=rowNum2+(12-rowNum2%12);
				}
				
				if(check%4==3){
					cellIndex=8;
					
					index++;
					
					System.out.println(">>>"+START+","+START+"倫次號"+filterHeader_list.get(j).toString()+","+sty);
					List<String> listr3 =getID30List(START, filterHeader_list.get(j).toString(), sty,conn);
					
					HSSFRow row = null;
					HSSFCell cell = null;	
					System.err.println(">>>"+rowNum3);
					
					row = sheet1.createRow(rowNum3);
					row.setHeight((short)(48*20));
					String model_na =getmodel_na(filterHeader_list.get(j).toString(),START,START, conn);
					cell = row.createCell((short)cellIndex);
					
					sheet1.addMergedRegion(new Region(rowNum3,(short)cellIndex,rowNum3,(short)(cellIndex+2))); 
					cell.setCellStyle(Style1);
					cell.setCellValue(model_na+" 第"+filterHeader_list.get(j).toString()+"輪");
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("");
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("");
					
					rowNum3++;
					row = sheet1.createRow(rowNum3);
					sheet1.addMergedRegion(new Region(rowNum3,(short)cellIndex,rowNum3,(short)(cellIndex+1))); 
					row.setHeight((short)(30*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue("部位: "+sry);
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style2);
//						String groupna=getgroupna(group, model_na);						
//						cell.setCellValue(groupna);
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("資料日期：\n"+START);
					
					rowNum3++;
					row = sheet1.createRow(rowNum3);
					row.setHeight((short)(24*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue("顏色");
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style3);
					cell.setCellValue("Tooling SIZE");
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("數量");
					for (int i = 0; i < listr3.size(); i++) {
						String[] s = listr3.get(i).split("=");
						String color=s[0];
						color =changegroup(sty, model_na, color);
					
						rowNum3++;
						row = sheet1.createRow(rowNum3);
						row.setHeight((short)(24*20));
						cell = row.createCell((short)cellIndex);
						
						cell.setCellStyle(Style2);
						cell.setCellValue(color.replace("--", ""));
						System.out.println(">>>"+color);
					
						cell = row.createCell((short)(cellIndex+1));
						
						cell.setCellStyle(Style1);
						cell.setCellValue(s[1]);
						System.out.println(">>>"+s[1]);
						
						cell = row.createCell((short)(cellIndex+2));
						
						cell.setCellStyle(Style1);
						cell.setCellValue(s[2]);
						System.out.println(s[0]+s[1]+s[2]+s[3]);
						System.out.println(">>>"+s[2]);
						
					
					}
					for (int i = 0; i < 8-listr3.size(); i++) {
					
						rowNum3++;
						row = sheet1.createRow(rowNum3);
						row.setHeight((short)(24*20));

					}
					

				rowNum3=rowNum3+(12-rowNum3%12);
				}
				
				if(check%4==0){
					cellIndex=12;
					
					index++;
					
					System.out.println(">>>"+START+","+START+"倫次號"+filterHeader_list.get(j).toString()+","+sty);
					List<String> listr4 =getID30List(START, filterHeader_list.get(j).toString(), sty,conn);
					
					HSSFRow row = null;
					HSSFCell cell = null;	
					System.err.println(">>>"+rowNum4);
					
					row = sheet1.createRow(rowNum4);
					row.setHeight((short)(48*20));
					String model_na =getmodel_na(filterHeader_list.get(j).toString(), START, START, conn);
					cell = row.createCell((short)cellIndex);
					
					sheet1.addMergedRegion(new Region(rowNum4,(short)cellIndex,rowNum4,(short)(cellIndex+2))); 
					cell.setCellStyle(Style1);
					cell.setCellValue(model_na+" 第"+filterHeader_list.get(j).toString()+"輪");
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("");
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("");
					
					rowNum4++;
					row = sheet1.createRow(rowNum4);
					sheet1.addMergedRegion(new Region(rowNum4,(short)cellIndex,rowNum4,(short)(cellIndex+1))); 
					row.setHeight((short)(30*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue("部位: "+sry);
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style2);
//						String groupna=getgroupna(group, model_na);						
//						cell.setCellValue(groupna);
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("資料日期：\n"+START);
					
					rowNum4++;
					row = sheet1.createRow(rowNum4);
					row.setHeight((short)(24*20));
					cell = row.createCell((short)cellIndex);
					
					cell.setCellStyle(Style2);
					cell.setCellValue("顏色");
					
					cell = row.createCell((short)(cellIndex+1));
					
					cell.setCellStyle(Style3);
					cell.setCellValue("Tooling SIZE");
					
					cell = row.createCell((short)(cellIndex+2));
					
					cell.setCellStyle(Style2);
					cell.setCellValue("數量");
					for (int i = 0; i < listr4.size(); i++) {
						String[] s = listr4.get(i).split("=");
						String color=s[0];
						color =changegroup(sty, model_na, color);
					
						rowNum4++;
						row = sheet1.createRow(rowNum4);
						row.setHeight((short)(24*20));
						cell = row.createCell((short)cellIndex);
						
						cell.setCellStyle(Style2);
						cell.setCellValue(color.replace("--", ""));
						System.out.println(">>>"+color);
					
						cell = row.createCell((short)(cellIndex+1));
						
						cell.setCellStyle(Style1);
						cell.setCellValue(s[1]);
						System.out.println(">>>"+s[1]);
						
						cell = row.createCell((short)(cellIndex+2));
						
						cell.setCellStyle(Style1);
						cell.setCellValue(s[2]);
						System.out.println(s[0]+s[1]+s[2]+s[3]);
						System.out.println(">>>"+s[2]);
						
					
					}						
					for (int i = 0; i < 8-listr4.size(); i++) {
					
						rowNum4++;
						row = sheet1.createRow(rowNum4);
						row.setHeight((short)(24*20));

					}

				rowNum4=rowNum4+(12-rowNum4%12);
					    
				}

				}
			
			saveOrUpdate = true;
	}
	//用list集合存放GROUP_NOS的值
	public static  List<String> GROUPSTATUS(String PG_NAME, Connection conn){
			List<String> list=new ArrayList<String>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql="SELECT * FROM DSID13 WHERE PG_NAME ='"+PG_NAME+"'";
			System.out.println("@@@@@@@@@@我在这里"+sql);
			try {
				ps= conn.prepareStatement(sql);
				rs= ps.executeQuery();
				if(rs.next()){
					String[] str=rs.getString("GROUP_NOS").split(",");
					for(int i=0;i<str.length;i++){
						list.add(str[i]);
					}
				}
					System.out.println("list@@@@@@@"+list);
				rs.close();
				ps.close();
			} catch (Exception e){
			}
			System.err.println("Glist>>>>"+list);
			return list;
		}
	//用list集合存放模具碼的值
	public static  List<String> TOOLINGSIZE(String MODEL_NA, Connection conn ,String START){
			List<String> list1=new ArrayList<String>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql="SELECT DISTINCT REPLACE(EL_NA,'CM','') EL_NA,SIZE_FD FROM DSID21 WHERE MT_USAGE='LACE' AND MODEL_NA LIKE '%"+MODEL_NA+"' ORDER BY TO_NUMBER(REPLACE(EL_NA,'CM',''))";
			System.out.println(sql);
			try {
				ps= conn.prepareStatement(sql);
				rs= ps.executeQuery();
				while(rs.next()){
					String[] str=rs.getString("SIZE_FD").split(",");
					for(int i=0;i<str.length;i++){
						list1.add(str[i]);
					}
				}
					System.out.println("list@@@@@@@"+list1);
				rs.close();
				ps.close();
			} catch (Exception e){
			}
			System.err.println("Glist>>>>"+list1);
			return list1;
		}
	public static  List<String> length(String MODEL_NA, Connection conn ,String START){
		List<String> list3=new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT DISTINCT REPLACE(EL_NA,'CM','') EL_NA,SIZE_FD FROM DSID21 WHERE MT_USAGE='LACE' AND MODEL_NA LIKE '%"+MODEL_NA+"' ORDER BY TO_NUMBER(REPLACE(EL_NA,'CM',''))";
		System.out.println(sql);
		try {
			ps= conn.prepareStatement(sql);
			rs= ps.executeQuery();
			while(rs.next()){
				String[] str=rs.getString("EL_NA").split(",");
				for(int i=0;i<str.length;i++){
					list3.add(str[i]);
				}
			}
				System.out.println("list@@@@@@@"+list3);
			rs.close();
			ps.close();
		} catch (Exception e){
		}
		System.err.println("Glist>>>>"+list3);
		return list3;
	}
	public static  List<String> TOOLINGSIZE1(String MODEL_NA, Connection conn ,String START){
		List<String> list2=new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT SUM(ORDER_NUM) SUM,TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD')= '"+START+"' AND MODEL_NA LIKE '%"+MODEL_NA+"' GROUP BY TOOLING_SIZE ORDER BY CAST(TOOLING_SIZE AS FLOAT)";
		System.out.println(sql);
		try {
			ps= conn.prepareStatement(sql);
			rs= ps.executeQuery();
			while(rs.next()){
				String[] str=rs.getString("SUM").split(",");
				for(int i=0;i<str.length;i++){
					list2.add(str[i]);
				}
			}
			
			System.out.println("啊啊啊啊list2@@@@@@@"+list2);
			rs.close();
			ps.close();
		} catch (Exception e){
		}
		System.err.println("Glist>>>>"+list2);
		return list2;
	}
	//设置製成派工單表格的长宽
	public static  List<String> getSheet2(String MODEL_NA,String START,Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlgetsheet = "select distinct all_round_num from dsid01 where TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"'and MODEL_NA ='"+MODEL_NA+"' ORDER BY all_round_num ";
		if (!"".equals(model_naTemp)) {
			sqlgetsheet = "select distinct all_round_num from dsid01 where TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"'";
				
			List<String> aa=getIDSH(conn);
			String sh=null;
			for (int i = 0; i < aa.size(); i++) {
				sh+=aa.get(i)+",";
			}
			sqlgetsheet += " and MODEL_NA in('"
					+sh.replace(",","','").replace("null","")+"') and MODEL_NA ='"+MODEL_NA+"' ORDER BY all_round_num  ";
		}
		System.out.println("---------------123456--------------------------"
				+ sqlgetsheet);
		List<String> list = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sqlgetsheet);
			rs = ps.executeQuery();
			while (rs.next()) {
				String obj = "";
				obj = rs.getString("ALL_ROUND_NUM");
				list.add(obj);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
	}
		return list;
	}
	public static  List<String> getIDSH(Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String > listr = new ArrayList<String>();
		String sql12 = "select distinct MODEL_NA A from dsid01 where model_na in('"
					+ model_naTemp.replace(",", "','") + "') ";
		List<DSID01> id01List = new ArrayList<DSID01>();
		System.out.println("getID30List..........sql..................."+ sql12);
		try {
			
			
			 ps= conn.prepareStatement(sql12);
			 rs = ps.executeQuery();
			while (rs.next()) {
				String gro= rs.getString("A");
				listr.add(gro);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listr;
	}
	private static String getmodel_na(String num,String begindate,String endDate, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String graphic = "";
		String sql = "SELECT distinct model_na A FROM DSID30 WHERE all_round_num ='"+num+"' and order_date BETWEEN to_date('"+begindate+"','YYYY/MM/DD') and  to_date('"+endDate+"','YYYY/MM/DD')";// AND GROUP_NO='GROUP4'";
		System.err.println(">>>"+sql);
		try {
			ps= conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){	
				graphic = rs.getString("A");	
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graphic;
	}
	private static String changegroup(String sty,String model_na,String color){
		if(model_na.contains("FREE RUN 2 SP15")&&sty.equals("2")){
			color=color.replace("77704","實色").replace("78878","反光銀").replace("71326","真皮點").replace("01V-00A,01V-00A","01V-00A").replace("00A-01E,00A-01E","00A-01E").replace("01V-10A,01V-10A","01V-10A");
		}else if(model_na.contains("HUARACHE RUN FA15")&&sty.equals("1")){
			color=color.replace("null_","").replace("96083","羊毛").replace("PRIME","羊毛").replace("191","單層網").replace("98320","轉印");
		}else if(model_na.contains("HUARACHE RUN HO15")&&sty.equals("2")){
			if(color.contains("_")){
				if(color.contains("101298.625.1")){
					color="毛料1";
				}else if(color.contains("101297.625.1")){
					color="毛料2";
				}else if(color.contains("101299.625.1")){
					color="毛料3";
				}else if(color.contains("101300.625.1")){
					color="毛料4";
				}else if(color.contains("101301.625.1")){
					color="毛料5";
				}else if(color.contains("101302.625.1")){
					color="毛料6";
				}else if(color.contains("101303.625.1")){
					color="毛料7";
				}else if(color.contains("101304.625.1")){
					color="毛料8";
				}else if(color.contains("101038.966.2")){
					color="真皮1";
				}else if(color.contains("102633.966.4")){
					color="真皮2";
				}else if(color.contains("102659.105.1")){
					color="牛仔";
				}else if(color.contains("103145.625.1")){
					color="帆布";
				}else if(color.contains("191")){
					color="單層網";
				}
			}else {
				color=color.replace("101298.625.1","毛料1").replace("101297.625.1","毛料2").replace("101299.625.1","毛料3").replace("101300.625.1","毛料4").replace("101301.625.1","毛料5").replace("101302.625.1","毛料6").replace("101303.625.1","毛料7").replace("101304.625.1","毛料8").replace("101038.966.2","真皮1").replace("102633.966.4","真皮2").replace("102659.105.1","牛仔").replace("103145.625.1","帆布").replace("191","單層網");
			}
		}

		else if(model_na.contains("HUARACHE RUN HO15")&&sty.equals("5")){
			color=color.replace("SOLIDSTRAP","實色").replace("CLEARSTRAP","透明膠").replace("OGSTRAP","黃黑");
		}else if(model_na.contains("HUARACHE RUN HO15")&&sty.equals("3")){
			color=color.replace("00A-L","真皮,00A").replace("27K-L","真皮,27K");
		}else if(model_na.contains("HUARACHE RUN HO15")&&sty.equals("6")){
			color=color.replace("SOLID","實色").replace("WAX","寬狀");
		}else if(model_na.contains("HUARACHE RUN HO15")&&sty.equals("11")){
			color=color.replace("SOLID","實色").replace("CLEAR","透明");
		}
//		else if(model_na.contains("Free 5.0 Flash iD")&&group.equals("9")){
//			color=color.replace("VENOM","蛇紋").replace("SLASH","斜紋").replace("null", "");
//		}
		else if(model_na.contains("Structure 19 Flash iD")&&sty.equals("1")){
			color=color.replace("SLASHFLASH","斜紋").replace("VENOMFLASH","蛇紋").replace("_YES--","");
		}else if(model_na.contains("Structure 19 iD")&&sty.equals("1")){
			color=color.replace("STRUC19GW","灰白").replace("STRUC19HAZ","條紋").replace("STRUC19MULTI","黃橙").replace("STRUC19BW","黑白").replace("FADE","漸層").replace("SOLID","實色");
		}else if(model_na.contains("Structure 19 iD")&&sty.equals("2")){
			color=color.replace("_YES","");
		}else if(model_na.contains("Structure 19 iD")&&sty.equals("3")){
			color=color.replace("_YES","");
		}else if(model_na.contains("Structure 19 iD")&&sty.equals("5")){
			color=color.replace("REFLECTIVE","反光銀").replace("SOLID","實色");
		}else if(model_na.contains("PEGASUS+32 iD")&&sty.equals("1")){
			color=color.replace("FADE","漸層").replace("VIPER","蛇紋").replace("FADE","漸層").replace("FRACTUAL","裂痕").replace("HEATHER","實色").replace("PHOTOSYN-----FLOR","花朵").replace("VIP-","").replace("FRAC-","");
		}else if(model_na.contains("PEGASUS+32 Flash iD")&&sty.equals("1")){
			color=color.replace("01B","01B-00A");
		}else if(model_na.contains("PEGASUS+32 Flash iD")&&sty.equals("10")){
			color=color.replace("null","");
		}else if(model_na.contains("PEGASUS+32 iD")&&sty.equals("10")){
			color=color.replace("null","");
		}else if(model_na.contains("PEGASUS+33 SU16 iD")&&sty.equals("11")){
			color=color.replace("null","");
		}
		else if(model_na.contains("PEGASUS+32 Flash iD")&&sty.equals("3")){
			String c=color;
			String str=c.substring(0, 3);
			String str1="";
			if(c.length()>5){	
				str1=c.substring(4, 7);
				color="後跟"+str+",鞋頭"+str1;
				System.out.println("this is color"+color);
			}else {
				color="後跟"+str+",鞋頭"+str;
				System.out.println("this is color"+color);
			}
		}else if(model_na.contains("PEGASUS+32")&&!model_na.contains("Flash")&&!model_na.contains("SP16")&&sty.equals("3")){
			String c=color;
			
			if(c.length()<5){
				color="後跟"+c+",鞋頭"+c;
			}else {
			String str=c.substring(0, 3);
			String str1=c.substring(4, 7);
			if(str1.equals("SOL")){
				color="後跟"+str+",鞋頭"+str;
				System.out.println("this is color"+color);
			}else{
				color="後跟"+str+",鞋頭"+str1;
				System.out.println("this is color"+color);
			}
			
			}
		}else if(model_na.contains("HUARACHE RUN FA15")&&sty.equals("2")){
			if(color.contains("74411")){
				String str=color.substring(0,color.indexOf("-"));
				color="人造皮"+","+str;
			}else if(color.contains("85772")){
				String str=color.substring(0,color.indexOf("-"));
				color="蛇皮"+","+str;
			}else if(color.contains("98320")){
				String str=color.substring(0,color.indexOf("-"));
				color="轉印"+","+str;
			}else if(color.contains("96083")){
				String str=color.substring(0,color.indexOf("-"));
				color="羊毛"+","+str;
			}else if(color.contains("PRIME")){
				String str=color.substring(0,color.indexOf("-"));
				color="羊毛"+","+str;
			}
		}else if(model_na.contains("HUARACHE RUN FA15")&&sty.equals("5")){
			if(color.contains("SOLIDSTRAP")){
				String str=color.substring(0,color.indexOf("-"));
				color="實色"+","+str;
			}else if(color.contains("CLEARSTRAP")){
				String str=color.substring(0,color.indexOf("-"));
				color="透明膠"+","+str;
			}else if(color.contains("OGSTRAP")){
				String str=color.substring(0,color.indexOf("-"));
				color="黃黑"+","+str;
			}
		}else if(model_na.contains("HUARACHE RUN FA15")&&sty.equals("3")){
				color=color.replace("SOLIDSTRAP", "實色").replace("CLEARSTRAP", "透明膠").replace("OGSTRAP", "黃黑");
		}
		
		return color;
	}
	private static List<String> getID30List(String START,String round ,String sty, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="";
		List<String > listr = new ArrayList<String>();
		String model_na=getmodel_na(round, START, conn);
		System.out.println(sty+">>>>"+model_na);
		if(model_na.contains("HUARACHE RUN SP17 ID")&&"4".equals(sty)){
			sty="14";
		}

		sql = "SELECT "+sty+" A, TOOLING_SIZE B, COUNT("+sty+") C ,ALL_ROUND_NUM D FROM DSID01 WHERE " +
				"ORDER_DATE BETWEEN TO_DATE('"+START+"','YYYY/MM/DD') AND TO_DATE('"+START+"','YYYY/MM/DD') " +
				"AND ALL_ROUND_NUM ='"+round+"' GROUP BY "+sty+",TOOLING_SIZE,ALL_ROUND_NUM ORDER BY  TOOLING_SIZE DESC" ;
		
		System.out.println(">>>"+ sql);
		
		try {			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String gro= rs.getString("A");
				String count= rs.getString("B");
				String arn= rs.getString("C");
				String size= rs.getString("D");
				String str=gro+"="+count+"="+arn+"="+size;
				System.out.println("STR IS --"+str);
				listr.add(str);

			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listr;
	}
	private static String getmodel_na(String num,String START, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String graphic = "";
		String sql = "SELECT distinct model_na A FROM DSID30 WHERE all_round_num ='"+num+"' and order_date BETWEEN to_date('"+START+"','YYYY/MM/DD') and  to_date('"+START+"','YYYY/MM/DD')";// AND GROUP_NO='GROUP4'";
		System.err.println(">>>"+sql);
		try {
			ps= conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){	
				graphic = rs.getString("A");	
			}
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graphic;
	}
	private static void SetColumnWidth(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 10*256);
		sheet1.setColumnWidth(1, 15*256);
		sheet1.setColumnWidth(2, 10*256);
		sheet1.setColumnWidth(3, 30*256);
	}
	//設置泡棉派工單表格的長寬
	private static void SetColumnWidth2(HSSFSheet sheet2) {
		sheet2.setColumnWidth(0, 10*256);
		sheet2.setColumnWidth(1, 30*256);
		sheet2.setColumnWidth(2, 10*256);
	}
	//設置底部派工單表格的長寬
	private static void SetColumnWidth3(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 10*256);
		sheet1.setColumnWidth(1, 15*256);
		sheet1.setColumnWidth(4, 30*256);
		sheet1.setColumnWidth(5, 20*256);
	}
	//設置部位派工單表格的長寬
	private static void SetColumnWidth4(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 10*256);
		sheet1.setColumnWidth(1, 25*256);
		sheet1.setColumnWidth(4, 10*256);
		sheet1.setColumnWidth(5, 10*256);
	}
	private static void SetColumnWidth5(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(1, 25*256);
		sheet1.setColumnWidth(2, 20*256);
	}
	//连接数据库
	public static Connection getDB01Conn(){
		Connection  conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.8.1.32:1521:ftldb1";
		String user = "DSOD";
		String pwd = "ora@it2013";
		try{
	        Class.forName(driver);
	     }catch(Exception e){
	        e.printStackTrace();
	     }
	    try{
	    	conn=DriverManager.getConnection(url,user,pwd);
	    	System.err.println(">>>鏈接DB01數據庫");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return conn;
	}
}