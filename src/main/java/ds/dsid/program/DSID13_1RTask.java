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
import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Filedownload;

import ds.dsid.domain.DSID10;
import util.Common;

public class DSID13_1RTask{
	
	public static void ExcelExport(String NIKE_SH_ARITCLE, String START, String SARTY,String PG_NAME) {
		
		String title=Labels.getLabel("DSID.MSG0249");
		Connection Conn = Common.getService1Conn();
		Connection conn = Common.getDbConnection();
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		
		// 设置字体，表格的长宽高，
		HSSFFont font1 = wb.createFont();
		font1.setFontName("Calibri");    				//设置字體  
		font1.setFontHeightInPoints((short)10);    		//设置字体高度  
//		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//设置字體樣式 			
		
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
		if(!"".equals(NIKE_SH_ARITCLE)){
			ExSql+=" NIKE_SH_ARITCLE LIKE '%"+NIKE_SH_ARITCLE+"'";
		}
		String ExSql1="";
		if(!"".equals(PG_NAME)){
			ExSql1+=""+PG_NAME+"";
		}
		PreparedStatement ps = null,ps1=null;
		ResultSet rs = null,rs1=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String 	sql="SELECT NIKE_SH_ARITCLE FROM DSID01  WHERE "+ExSql+" AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+START+"' AND '"+SARTY+"' GROUP BY NIKE_SH_ARITCLE";
		System.out.println(">>>>>"+sql);
		String sql1="SELECT * FROM DSID13 WHERE PG_NAME='"+ExSql1+"'";
		System.out.println(">>>>>>"+sql1);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			ps1 = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();
			while(rs1.next() && rs.next()){
				//不選擇時全部導出
				System.out.println(rs1.getString("PG_TYPE"));
				if("".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth(sheet);
					Exportexcel1(wb,sheet,NIKE_SH_ARITCLE, START,SARTY,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				
					HSSFSheet sheet2 = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth2(sheet2);
					Exportexcel2(wb,sheet2,NIKE_SH_ARITCLE, START,SARTY,rs1.getString("PG_NAME"), Conn, conn,style1,style2,style3,style4);
				}
				if("製成".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth(sheet);
					Exportexcel1(wb,sheet,NIKE_SH_ARITCLE, START,SARTY,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("材料".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet2 = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth2(sheet2);
					Exportexcel2(wb,sheet2,NIKE_SH_ARITCLE, START,SARTY,rs1.getString("PG_NAME"), Conn, conn,style1,style2,style3,style4);
				}
				if("底部".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth3(sheet);
					Exportexcel3(wb,sheet,NIKE_SH_ARITCLE, START,SARTY,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
				}
				if("部位".equals(rs1.getString("PG_TYPE"))){
					HSSFSheet sheet = wb.createSheet(rs1.getString("PG_NAME"));
					SetColumnWidth3(sheet);
					Exportexcel4(wb,sheet,NIKE_SH_ARITCLE, START,SARTY,rs1.getString("PG_NAME"), Conn,conn,style1,style2,style3,style4);
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
	private static void Exportexcel1(HSSFWorkbook wb,HSSFSheet sheet1, String NIKE_SH_ARITCLE,String START,String SARTY,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		PreparedStatement ps1=null;
		ResultSet rs1=null;
		List<String> list=GROUPSTATUS(NIKE_SH_ARITCLE,conn);
		Header1(sheet1,style1,row,NIKE_SH_ARITCLE,PG_NAME, cell, conn,list);
		
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
		cell.setCellValue(START+"—"+SARTY);
		
		//數據從第三行開始
		int rowNum = 3;
		
		//判斷形體是否相同
		String SQLI="";
		if(!"".equals(NIKE_SH_ARITCLE)){
			SQLI="AND NIKE_SH_ARITCLE LIKE '%"+NIKE_SH_ARITCLE+"'";
		}
		//查詢DSID01表与时间的內容
		String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+START+"' AND '"+SARTY+"'"+SQLI+" ORDER BY TOOLING_SIZE+0 DESC";
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
					cell.setCellValue(rs1.getString("ALL_ROUND_NUM"));
					
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("OD_NO"));
						
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("MODEL_NA"));
					
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("ORDER_NUM"));
								
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("TOOLING_SIZE"));

					for(int i=1;i<list.size()+1;i++){
						cell = row.createCell(4+i);
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
	private static void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String NIKE_SH_ARITCLE,String PG_NAME, HSSFCell cell,Connection conn, List<String> list) {
		// TODO Auto-generated method stub
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
		
		//設置表頭的形體
		row = sheet.createRow(0);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(NIKE_SH_ARITCLE+" "+PG_NAME);
		
		//查詢DSID13表中的GROUP內容
		String sql3="SELECT * FROM DSID10 WHERE NIKE_SH_ARITCLE='"+NIKE_SH_ARITCLE+"'";
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
			cell.setCellValue(Labels.getLabel("DSID01M.ORDER_DATE"));
			
			row = sheet.createRow(2);
			row.setHeightInPoints(30);
		
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0250"));
		
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0251"));					
		
			cell = row.createCell(2);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSIDN06_01.MODEL_NA"));	
		
			cell = row.createCell(3);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
		
			cell = row.createCell(4);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("TOOLING_SIZE");
			
			for(int i=1;i<list.size()+1;i++){
				cell = row.createCell(4+i);
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
	private static void Exportexcel2(HSSFWorkbook wb,HSSFSheet sheet2, String NIKE_SH_ARITCLE,String START,String SARTY,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		PreparedStatement ps1=null;
		ResultSet rs1=null;
		Header2(sheet2,style1,row,NIKE_SH_ARITCLE,PG_NAME,START,SARTY,cell, conn);
		
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
		cell.setCellValue(START+"—"+SARTY);
		
		//數據從第三行開始
		int rowNum = 3;
		//判斷形體是否相同
		String SQLI="";
		if(!"".equals(NIKE_SH_ARITCLE)){
			SQLI="AND NIKE_SH_ARITCLE LIKE'%"+NIKE_SH_ARITCLE+"'";
		}
		//查詢DSID01表与时间的內容
		String sql1="SELECT DISTINCT TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+START+"' AND '"+SARTY+"'"+SQLI+" ORDER BY cast(TOOLING_SIZE as float)";
		System.err.println(">>>>>"+sql1);
		try {
			ps1=conn.prepareStatement(sql1);
			rs1=ps1.executeQuery();
			//把需要的內容get到excel表中
			while(rs1.next()){
					row = sheet2.createRow(rowNum);
					row.setHeightInPoints(25);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs1.getString("TOOLING_SIZE"));
					
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
	//泡棉派工單格式
	private static void Header2(HSSFSheet sheet2, HSSFCellStyle style1, HSSFRow row,String NIKE_SH_ARITCLE,String PG_NAME,String START,String SARTY,HSSFCell cell,Connection conn) {
		// TODO Auto-generated method stub
		List<String> list2=TOOLINGSIZE1(NIKE_SH_ARITCLE,conn,START,SARTY);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String num="";
		//相加鞋子的總數
		String 	sql="SELECT COUNT(ORDER_NUM) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')BETWEEN '"+START+"' AND '"+SARTY+"' AND NIKE_SH_ARITCLE LIKE '%"+NIKE_SH_ARITCLE+"'";
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				num=rs.getString("COU");
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
		cell.setCellValue(NIKE_SH_ARITCLE+PG_NAME);
		//把需要的欄位名稱設置到excle中
			row = sheet2.createRow(1);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID01M.ORDER_DATE"));
			
			row = sheet2.createRow(2);
			row.setHeightInPoints(30);
		
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("TOOLING_SIZE");
		
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID01M.QTY"));					
		
			cell = row.createCell(2);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0252"));	
			
			cell = row.createCell(3);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(num+Labels.getLabel("DSID.MSG0015"));
			
			row = sheet2.createRow(3);
			row.setHeightInPoints(30);
			for(int i=0;i<(Integer.valueOf (list2.get(0).toString()));i++){
				cell = row.createCell(1+i);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(list2.get(i));
			}
			//關閉流
		}
	 
	//底部派工单 类型  ,导出至表中
	private static void Exportexcel3(HSSFWorkbook wb,HSSFSheet sheet1, String NIKE_SH_ARITCLE,String START,String SARTY,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
			// TODO Auto-generated method stub
			HSSFRow row = null;
			HSSFCell cell = null;
			PreparedStatement ps1=null;
			ResultSet rs1=null;
			List<String> list=GROUPSTATUS(NIKE_SH_ARITCLE,conn);
			Header3(sheet1,style1,row,NIKE_SH_ARITCLE,PG_NAME, cell, conn,list);
			
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
			cell.setCellValue(START+"—"+SARTY);
			
			//數據從第三行開始
			int rowNum = 6;
			
			//判斷形體是否相同
			String SQLI="";
			if(!"".equals(NIKE_SH_ARITCLE)){
				SQLI="AND NIKE_SH_ARITCLE LIKE '%"+NIKE_SH_ARITCLE+"'";
			}
			//查詢DSID01表与时间的內容
			String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+START+"' AND '"+SARTY+"'" +SQLI;
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
						cell.setCellValue(rs1.getString("ALL_ROUND_NUM"));
						
						cell = row.createCell(1);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						cell.setCellValue(rs1.getString("ORDER_ID"));
						
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
	private static void Header3(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String NIKE_SH_ARITCLE,String PG_NAME, HSSFCell cell,Connection conn, List<String> list) {
			// TODO Auto-generated method stub
			PreparedStatement ps3 = null;
			ResultSet rs3 = null;
			int rowNum = 6;
			//設置表頭的形體
			row = sheet.createRow(0);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(NIKE_SH_ARITCLE+PG_NAME);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0253"));
			
			row = sheet.createRow(1);
			row.setHeightInPoints(30);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0254"));
			
			row = sheet.createRow(2);
			row.setHeightInPoints(30);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0255"));
			
			row = sheet.createRow(3);
			row.setHeightInPoints(30);
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(Labels.getLabel("DSID.MSG0256"));
			
			//查詢DSID13表中的GROUP內容
			String sql3="SELECT * FROM DSID10 WHERE NIKE_SH_ARITCLE='"+NIKE_SH_ARITCLE+"'";
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
				cell.setCellValue(Labels.getLabel("DSID01M.ORDER_DATE"));
				
				row = sheet.createRow(5);
				row.setHeightInPoints(30);
			
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID.MSG0250"));
			
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("ORDER_ID");					
			
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID.MSG0257"));	
			
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID.MSG0258"));
			
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSIDN06_01.MODEL_NA"));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID01M.QTY"));
				
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
	private static void Exportexcel4(HSSFWorkbook wb,HSSFSheet sheet1, String NIKE_SH_ARITCLE,String START,String SARTY,String PG_NAME, Connection Conn, Connection conn, HSSFCellStyle style1,HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
			// TODO Auto-generated method stub
			HSSFRow row = null;
			HSSFCell cell = null;
			PreparedStatement ps1=null;
			ResultSet rs1=null;
			
			List<String> list=GROUPSTATUS(NIKE_SH_ARITCLE,conn);
			List<String> list1=TOOLINGSIZE(NIKE_SH_ARITCLE,conn,START,SARTY);
			Header4(sheet1,style1,row,NIKE_SH_ARITCLE,PG_NAME, cell, conn,START,SARTY);
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
			cell.setCellValue(START+"—"+SARTY);
			
			String sty=null;
			for(int i=0;i<list.size();i++){
				sty=list.get(i);
			}
			//數據從第三行開始
			int rowNum = 4;
			
			//判斷形體是否相同
			String SQLI="";
			if(!"".equals(NIKE_SH_ARITCLE)){
				SQLI="AND NIKE_SH_ARITCLE LIKE '%"+NIKE_SH_ARITCLE+"'";
			}
			//查詢DSID01表与时间的內容
			String sql1="SELECT DISTINCT "+sty+" FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+START+"' AND '"+SARTY+"'" +SQLI;
			System.err.println(">>>>>"+sql1);
			try {
				ps1=conn.prepareStatement(sql1);
				rs1=ps1.executeQuery();
				//把需要的內容get到excel表中
				while(rs1.next()){
						row = sheet1.createRow(rowNum);
						row.setHeightInPoints(25);
						
						for(int i=1;i<list.size()+1;i++){
							cell = row.createCell(0);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue(rs1.getString(list.get(i-1)));
						}
//						cell = row.createCell(0);
//						cell.setCellType(1);
//						cell.setCellStyle(style1);
//						cell.setCellValue(rs1.getString("ALL_ROUND_NUM"));
//						
//						cell = row.createCell(1);
//						cell.setCellType(1);
//						cell.setCellStyle(style1);
//						cell.setCellValue(rs1.getString("OD_NO"));

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
	//部位派工单格式
	private static void Header4(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row,String NIKE_SH_ARITCLE,String PG_NAME, HSSFCell cell,Connection conn,String START,String SARTY) {
			// TODO Auto-generated method stub
			PreparedStatement ps3 = null;
			ResultSet rs3 = null;
			List<String> list=GROUPSTATUS(NIKE_SH_ARITCLE,conn);
			List<String> list1=TOOLINGSIZE(NIKE_SH_ARITCLE,conn,START,SARTY);
			
			//設置表頭的形體
			row = sheet.createRow(0);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(NIKE_SH_ARITCLE+PG_NAME);
			int sby=0;
			//查詢DSID13表中的GROUP內容
			String sql3="SELECT * FROM DSID10 WHERE NIKE_SH_ARITCLE='"+NIKE_SH_ARITCLE+"'";
			System.out.println("@@@@@@@@@@"+sql3);
			try {
			ps3= conn.prepareStatement(sql3);
			rs3= ps3.executeQuery();
			//把需要的欄位名稱設置到excle中
			if(rs3.next()){
				row = sheet.createRow(1);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID01M.ORDER_DATE"));
				
				row = sheet.createRow(2);
				row.setHeightInPoints(30);
			
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID.MSG0259"));
			
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(Labels.getLabel("DSID.MSG0258"));					
				
				row = sheet.createRow(3+sby);
				row.setHeightInPoints(30);
				
				for(int i=0;i<list.size();i++){
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs3.getString(list.get(i)+"_NA"));
				}
				System.err.println("GRUOP_NOA"+list);
				for(int i=1;i<list1.size()+1;i++){
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(list1.get(i-1));
				}
				System.err.println("@@@@@@@@@"+list1);
				sby++;
			}
				//關閉流
				ps3.close();
				rs3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	
	//用list集合存放GROUP_NOS的值
	public static  List<String> GROUPSTATUS(String NIKE_SH_ARITCLE, Connection conn){
			List<String> list=new ArrayList<String>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql="SELECT * FROM DSID13 WHERE NIKE_SH_ARITCLE='"+NIKE_SH_ARITCLE+"'";
			System.out.println(sql);
			
			try {
				ps= conn.prepareStatement(sql);
				rs= ps.executeQuery();
				if(rs.next()){
					String[] str=rs.getString("GROUP_NOS").split(",");
					System.out.println("@@@@@@@"+str);
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
	public static  List<String> TOOLINGSIZE(String NIKE_SH_ARITCLE, Connection conn ,String START,String SARTY){
			List<String> list1=new ArrayList<String>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql="SELECT DISTINCT TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')BETWEEN '"+START+"' AND '"+SARTY+"'AND NIKE_SH_ARITCLE='"+NIKE_SH_ARITCLE+"' ORDER BY cast(TOOLING_SIZE as float)";
			System.out.println(sql);
			
			try {
				ps= conn.prepareStatement(sql);
				rs= ps.executeQuery();
				while(rs.next()){
					String[] str=rs.getString("TOOLING_SIZE").split(",");
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
	public static  List<String> TOOLINGSIZE1(String NIKE_SH_ARITCLE, Connection conn ,String START,String SARTY){
		List<String> list2=new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT SUM(ORDER_NUM) SUM,TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD')BETWEEN '"+START+"'AND '"+SARTY+"'AND NIKE_SH_ARITCLE LIKE '%"+NIKE_SH_ARITCLE+"' GROUP BY TOOLING_SIZE ORDER BY CAST(TOOLING_SIZE AS FLOAT)";
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
	private static void SetColumnWidth(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 10*256);
		sheet1.setColumnWidth(1, 30*256);
		sheet1.setColumnWidth(2, 30*256);
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
		sheet1.setColumnWidth(1, 25*256);
		sheet1.setColumnWidth(4, 30*256);
		sheet1.setColumnWidth(5, 20*256);
	}
	
	//连接数据库
//	public static Connection getDB01Conn(){
//		Connection  conn = null;
//		String driver = "oracle.jdbc.driver.OracleDriver";
//		String url = "jdbc:oracle:thin:@10.8.1.32:1521:ftldb1";
//		String user = "DSOD";
//		String pwd = "ora@it2013";
//		try{
//	        Class.forName(driver);
//	     }catch(Exception e){
//	        e.printStackTrace();
//	     }
//	    try{
//	    	conn=DriverManager.getConnection(url,user,pwd);
//	    	System.err.println(">>>鏈接DB01數據庫");
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
//	    return conn;
//	}
}