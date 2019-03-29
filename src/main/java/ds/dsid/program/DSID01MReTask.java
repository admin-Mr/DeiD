package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Filedownload;

import util.Common;

public class DSID01MReTask {

	public static void ExcelExport(String od_noList) throws SQLException {
		// TODO Auto-generated method stub
		String title="Order Detail";
		Connection conn = Common.getDbConnection();
		
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		
		// 字體	
		HSSFFont font1 = wb.createFont();
		font1.setFontName("Calibri");    				//设置字體  
		font1.setFontHeightInPoints((short)10);    		//设置字体高度  
//		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 			
		
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
		

		HSSFSheet sheet = wb.createSheet("Sheet1");
//		SetColumnWidth(sheet);	
				
		Exportexcel(wb,sheet,od_noList,conn,style1,style2,style3,style4);
			
		Common.closeConnection(conn);
		
		System.err.println("od_noList>>>>"+od_noList);
		try {
			wb.write(stream);

			byte[] content = stream.toByteArray();
		    InputStream is = new ByteArrayInputStream(content);

		    //儲存位置、名稱
			Filedownload.save(is, "application/xls",title);
			is.close();
			stream.flush();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void Exportexcel(HSSFWorkbook wb, HSSFSheet sheet1, String od_noList, Connection conn,
			HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) throws SQLException {
		// TODO Auto-generated method stub
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		
		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;	
		
		Header1(sheet1,style1,row,cell);
		
		int rowNum = 1;

		String sql2="SELECT * FROM DSID01_TEMP WHERE WORK_ORDER_ID IN ('"+od_noList.replace(",", "','")+"')";
		System.err.println(">>>>>"+sql2);
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){

					row = sheet1.createRow(rowNum);
					row.setHeightInPoints(25);
					
					int CellNum=0;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("OD_NO"));	
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(format.format(rs2.getDate("ORDER_DATE")));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("WORK_ORDER_ID"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("LEFT_SIZE_RUN"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("TOOLING_SIZE"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("NIKE_SH_ARITCLE"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("MODEL_NA"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("PID01"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("PID02"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP1"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP2"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP3"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP4"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP5"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP6"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP7"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP8"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP9"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP10"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP11"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP12"));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(format.format(rs2.getDate("REQUSHIPDATE")));
					
					CellNum++;
					cell = row.createCell(CellNum);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(format.format(rs2.getDate("FACTRECDATE")));
								
					rowNum++;
			}
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs2!=null){
				rs2.close();
			}
			if(ps2!=null){
				ps2.close();
			}
		}
		for(int i=0;i< 23;i++){
			sheet1.autoSizeColumn((short) i);
		}
		
	}

	private static void Header1(HSSFSheet sheet1, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub

		row = sheet1.createRow(0);
		row.setHeightInPoints(30);
		
		int CellNum=0;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("OD_NO");	
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("ORDER_DATE");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("WORK_ORDER_ID");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("LEFT/RIGHR_SIZE");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("TOOLING_SIZE");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("NIKE_SH_ARITCLE");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("MODEL_NA");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("PID01");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("PID02");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP1");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP2");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP3");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP4");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP5");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP6");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP7");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP8");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP9");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP10");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP11");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("GROUP12");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("RSD");
		
		CellNum++;
		cell = row.createCell(CellNum);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Receive date");	
		
	}


	

}
