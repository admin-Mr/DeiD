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
import java.text.DecimalFormat;
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


public class DSID22_1RTask  {
	
//	public static void ExcelExport(String MODEL_NA,String START,String END, String TYPE, String PO_NO) {
	public static void ExcelExport(String type ,String sql1, String sql2) throws SQLException {	
		// TODO Auto-generated method stub
		String title="材料進度報表";
		Connection Conn = Common.getService1Conn();
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
		style2.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
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
		style3.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
		style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
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
		
		if("1".equals(type)){
			HSSFSheet sheet1 = wb.createSheet(Labels.getLabel("DSID22R.EL_FOR"));
			HSSFSheet sheet2 = wb.createSheet(Labels.getLabel("DSID22R.EL_DOM"));
			SetColumnWidth1(sheet1);
			SetColumnWidth2(sheet2);
			Exportexcel1(wb,sheet1,sql1,Conn,conn,style1,style2,style3,style4);
			Exportexcel2(wb,sheet2,sql2,Conn,conn,style1,style2,style3,style4);
		}
		if("2".equals(type)){
			HSSFSheet sheet1 = wb.createSheet(Labels.getLabel("DSID22R.EL_FOR"));
			SetColumnWidth1(sheet1);
			Exportexcel1(wb,sheet1,sql1,Conn,conn,style1,style2,style3,style4);

		}
		if("3".equals(type)){
			HSSFSheet sheet2 = wb.createSheet(Labels.getLabel("DSID22R.EL_DOM"));
			SetColumnWidth2(sheet2);
			Exportexcel2(wb,sheet2,sql2,Conn,conn,style1,style2,style3,style4);
		}

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
		}finally{
			Common.closeConnection(conn);
			Common.closeConnection(Conn);
		}
	}
	
	
	private static void SetColumnWidth1(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 10*256);
		sheet1.setColumnWidth(1, 15*256);
		sheet1.setColumnWidth(2, 15*256);
		sheet1.setColumnWidth(3, 15*256);
		sheet1.setColumnWidth(4, 30*256);
		sheet1.setColumnWidth(5, 25*256);
		sheet1.setColumnWidth(6, 10*256);
		sheet1.setColumnWidth(7, 10*256);
		sheet1.setColumnWidth(8, 15*256);
		sheet1.setColumnWidth(9, 15*256);
		sheet1.setColumnWidth(10, 18*256);	
	}

	private static void SetColumnWidth2(HSSFSheet sheet2) {
		// TODO Auto-generated method stub
		
		sheet2.setColumnWidth(0, 15*256);
		sheet2.setColumnWidth(1, 15*256);
		sheet2.setColumnWidth(2, 15*256);
		sheet2.setColumnWidth(3, 30*256);
		sheet2.setColumnWidth(4, 25*256);
		sheet2.setColumnWidth(5, 10*256);
		sheet2.setColumnWidth(6, 10*256);
		sheet2.setColumnWidth(7, 15*256);
		sheet2.setColumnWidth(8, 15*256);
		sheet2.setColumnWidth(9, 18*256);		
	}

	private static void Exportexcel1(HSSFWorkbook wb, HSSFSheet sheet1, String sql1, Connection Conn, Connection conn, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) throws SQLException {
		// TODO Auto-generated method stub

		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;	
		
		Header1(sheet1,style1,row,cell);
		
		int rowNum = 1;
		String last_date="";

		System.out.println(">>>>>"+sql1);
		try {
			ps2 = Conn.prepareStatement(sql1);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){

					row = sheet1.createRow(rowNum);
					row.setHeightInPoints(25);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(1));
								
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(2));
								
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(3));
								
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(4));
					
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(5));
					
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(6));
					
					cell = row.createCell(6);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(!"".equals(rs2.getString(7))&&rs2.getString(7)!=null){
						cell.setCellValue(rs2.getString(7));
					}else{
						cell.setCellValue("/");
					}
					
					
					cell = row.createCell(7);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(!"".equals(rs2.getString(8))){
						cell.setCellValue(Double.valueOf(rs2.getString(8)));
					}else{
						cell.setCellValue(0);
					}

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");       
					cell = row.createCell(8);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(format.format(rs2.getDate(9)));
					
					cell = row.createCell(9);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(!"".equals(rs2.getString("PO_REDATE"))&&rs2.getString("PO_REDATE")!=null){
						cell.setCellValue(format.format(rs2.getDate("PO_REDATE")));
						last_date=format.format(rs2.getDate("PO_REDATE"));
					}else{
						cell.setCellValue(last_date);
					}
					
					
					cell = row.createCell(10);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					String Date=GetDate(rs2.getString(2),rs2.getString(4),conn);
					cell.setCellValue(Date);
					
					if(!"".equals(Date)&&Date!=null){
						for(int i=0;i<11;i++){
							cell = row.getCell(i);							
							if(i==10&&Date.contains("差額")){
								cell.setCellStyle(style4);
							}else if(i==10&&Date.contains("驗收")){
								cell.setCellStyle(style3);
							}else{
								cell.setCellStyle(style2);
							}
						}						
					}
					
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

	}
	
	private static String GetDate(String po_no, String el_no,Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
		String DATE="";
		Double PO_QTY=0.0,Spc_qty=0.0;
		int status=0;
		String 	sql="SELECT * FROM DSIDN08 WHERE PO_NO='"+po_no+"' AND EL_NO='"+el_no+"'";
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				PO_QTY=Double.valueOf(rs.getString("PO_QTY"));
				if(PO_QTY-Double.valueOf(rs.getString("PC_QTY"))==0){
					DATE=format.format(rs.getDate("PC_REDATE"));
				}else{
					status=1;
					DATE+=format.format(rs.getDate("PC_REDATE"))+"  "+rs.getString("PC_QTY")+"\n";
					Spc_qty+=Double.valueOf(rs.getString("PC_QTY"));
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
		}

		
	      DecimalFormat df = new DecimalFormat("0.00");

		if(status==1){
			if(PO_QTY-Spc_qty>0){
				DATE="總驗收：" +Spc_qty+"\n"+"差額:"+df.format((PO_QTY-Spc_qty))+"\n"+DATE;
			}else{
				DATE="總驗收：" +Spc_qty+"\n"+DATE;
			}	
		}	
		return DATE;
	}

	private static void Exportexcel2(HSSFWorkbook wb, HSSFSheet sheet1, String sql2, Connection Conn, Connection conn, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) throws SQLException {
		// TODO Auto-generated method stub

		HSSFRow row = null;
		HSSFCell cell = null;
			
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;	
		
		Header2(sheet1,style1,row,cell);
		
		int rowNum = 1;
		String last_date="";

		System.out.println(">>>>>"+sql2);
		try {
			ps2 = Conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){

					row = sheet1.createRow(rowNum);	
					row.setHeightInPoints(25);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(1));
								
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(2));
								
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(3));
								
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(4));
					
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(5));
					
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(!"".equals(rs2.getString(6))&&rs2.getString(6)!=null){
						cell.setCellValue(rs2.getString(6));
					}else{
						cell.setCellValue("/");
					}

					
					cell = row.createCell(6);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(!"".equals(rs2.getString(7))){
						cell.setCellValue(Double.valueOf(rs2.getString(7)));
					}else{
						cell.setCellValue(0);
					}					
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
					
					cell = row.createCell(7);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(format.format(rs2.getDate(8)));
					
					cell = row.createCell(8);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(!"".equals(rs2.getString(9))&&rs2.getString(9)!=null){
						cell.setCellValue(format.format(rs2.getDate(9)));
						last_date=format.format(rs2.getDate(9));
					}else{
						cell.setCellValue(last_date);
					}
					
					
					cell = row.createCell(9);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					String Date=GetDate(rs2.getString(1),rs2.getString(3),conn);
					cell.setCellValue(Date);
					if(!"".equals(Date)&&Date!=null){
						for(int i=0;i<10;i++){
							cell = row.getCell(i);
							if(i==9&&Date.contains("差額")){
								cell.setCellStyle(style4);
							}else if(i==9&&Date.contains("驗收")){
								cell.setCellStyle(style3);
							}else{
								cell.setCellStyle(style2);
							}
						}						
					}
					
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

	}
	
	private static void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
			
		row = sheet.createRow(0);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.FOR_ORD"));		
			
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.PO_NO"));		
		
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.MANU"));		
			
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.EL_NO"));
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.EL_NA"));		
			
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0190"));		
			
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.COLOR"));
			
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.QTY"));
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.PO_DATE"));
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0191"));
		
		cell = row.createCell(10);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.STO_DATE"));
	}

	private static void Header2(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
			
		row = sheet.createRow(0);
		row.setHeightInPoints(30);	
			
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.PO_NO"));		
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.MANU"));		
			
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.EL_NO"));
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.EL_NA"));		
			
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0190"));		
			
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.COLOR"));
			
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.QTY"));
		
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.PO_DATE"));
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0191"));
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID22R.STO_DATE"));
	}
	
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
