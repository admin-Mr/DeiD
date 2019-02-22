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
import java.util.Date;

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


public class DSID23_1RTask  {
	
	static String EL_PO="";
	
	public static void ExcelExport(String MODEL_NA,String START) throws SQLException {
		// TODO Auto-generated method stub
		String title=Labels.getLabel("DSID.MSG0193");
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
			ExSql=" AND MODEL_NA LIKE '%"+MODEL_NA+"'";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
			
		
		String 	sql="SELECT DISTINCT MODEL_NA ,COUNT(*) COU FROM ( SELECT CASE WHEN MODEL_NA LIKE 'W%' THEN SUBSTR(MODEL_NA,3) ELSE MODEL_NA END AS MODEL_NA FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+ExSql+" )  GROUP BY MODEL_NA";
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				HSSFSheet sheet = wb.createSheet(rs.getString("MODEL_NA"));
				SetColumnWidth(sheet);	
				
				Exportexcel1(wb,sheet,rs.getString("MODEL_NA"),START,rs.getString("COU"),Conn,conn,style1,style2,style3,style4);
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

			Common.closeConnection(conn);
			Common.closeConnection(Conn);
		}
			
		
//		HSSFSheet sheet1 = wb.createSheet("sheet1");
//		SetColumnWidth(sheet1);
//		
//		Exportexcel1(wb,sheet1,MODEL_NA,START,Conn,conn,style1,style2,style3,style4);
//		Exportexcel2(wb,sheet2,MODEL_NA,START,END,Conn,conn,style1,style2,style3,style4);

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
	
	
	private static void SetColumnWidth(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 10*256);
		sheet1.setColumnWidth(1, 25*256);
		sheet1.setColumnWidth(2, 15*256);
		sheet1.setColumnWidth(3, 30*256);
		sheet1.setColumnWidth(4, 15*256);
		sheet1.setColumnWidth(5, 15*256);
		sheet1.setColumnWidth(6, 10*256);
		sheet1.setColumnWidth(7, 10*256);
		sheet1.setColumnWidth(8, 10*256);
		sheet1.setColumnWidth(9, 10*256);
		sheet1.setColumnWidth(10, 10*256);
		sheet1.setColumnWidth(11, 10*256);	
		sheet1.setColumnWidth(12, 15*256);	
	}


	private static void Exportexcel1(HSSFWorkbook wb, HSSFSheet sheet1, String MODEL_NA, String START, String COU, Connection Conn, Connection conn, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) throws SQLException {
		// TODO Auto-generated method stub

		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;	
		
		Header1(sheet1,style1,row,cell);
		
		row = sheet1.getRow(0);
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(MODEL_NA);
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(START);
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Double.valueOf(COU));
		
		int rowNum = 2;

		String sql2="SELECT GROUP_NO,GR_NA,EL_NO,(SELECT EL_NA FROM DSEL00 WHERE EL_NO=DSID04_1.EL_NO) EL_NA,SUBSTR(COLOR,0,3) COLOR,SUM(YIELD) SYIELD,MIN(EL_UNIT) EL_UNIT FROM DSID04_1 WHERE MODEL_NA='"+MODEL_NA+"' AND EL_NO IS NOT NULL AND GROUP_NO LIKE 'GROUP%' GROUP BY MODEL_NA,GR_NA,GROUP_NO,EL_NO,EL_NA,COLOR ORDER BY GROUP_NO,EL_NO";
		System.err.println(">>>>>"+sql2);
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){

					row = sheet1.createRow(rowNum);
					row.setHeightInPoints(25);
					
					cell = row.createCell(0);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GROUP_NO"));
					
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("GR_NA"));
					
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("EL_NO"));
						
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("EL_NA"));
					
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("COLOR"));
								
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(Double.valueOf(rs2.getString("SYIELD")));
					
					cell = row.createCell(6);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("EL_UNIT"));
					
					//材料庫存
					cell = row.createCell(7);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(Double.valueOf(GetKc(MODEL_NA,rs2.getString("EL_NO"),Conn)));
					
					cell = row.createCell(8);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(Double.valueOf(GetOd_noNum(START,MODEL_NA,rs2.getString("GROUP_NO"),rs2.getString("COLOR"),conn)));
					
					cell = row.createCell(9);
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellFormula("I"+(rowNum+1)+"*F"+(rowNum+1));
					
					cell = row.createCell(10);
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellFormula("H"+(rowNum+1)+"-J"+(rowNum+1));
					
					 Double zt_qty=getbuy(rs2.getString("EL_NO"),MODEL_NA,Conn)-getacc(rs2.getString("EL_NO"),MODEL_NA,conn);
					
					cell = row.createCell(11);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(zt_qty);
					
					cell = row.createCell(12);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(zt_qty>0){	
						cell.setCellValue(getbuyDate(rs2.getString("EL_NO"),MODEL_NA,Conn));
					}else{
						cell.setCellValue("");
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
	
	private static String getbuyDate(String el_no, String MODEL_NA, Connection Conn) throws SQLException {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String PO_REDATE="";
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");	
		
		String 	sql="SELECT MIN(B.PO_REDATE) PO_REDATE FROM DSPO05 A,DSPO06 B WHERE A.PO_NO=B.PO_NO AND A.PO_NO LIKE 'IGM%'\n" +
				"AND B.PO_CLOSE!='T' AND PO_QTY!=0 AND EL_NO='"+el_no+"' AND STOCK_MARK='"+MODEL_NA+"' AND B.PO_REDATE IS NOT NULL";
	
//		System.out.println(">>>>>"+sql);
		try {
			ps = Conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				PO_REDATE=format.format(rs.getDate("PO_REDATE"));
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

		return PO_REDATE;
	}
	
	private static Double getbuy(String el_no, String MODEL_NA, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Double qty=0.0;

		String 	sql="SELECT A.PO_NO,A.STOCK_MARK,EL_NO,PO_QTY,PO_ACQTY FROM DSPO05 A,DSPO06 B WHERE A.PO_NO=B.PO_NO AND A.PO_NO LIKE 'IGM%'\n" +
				"AND B.PO_CLOSE!='T' AND PO_QTY!=0 AND EL_NO='"+el_no+"' AND STOCK_MARK='"+MODEL_NA+"'";
	
//		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				qty+=Double.valueOf(rs.getString("PO_QTY"));
				EL_PO+=rs.getString("PO_NO")+",";
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

		if(EL_PO.length()>0){
			EL_PO=EL_PO.substring(0, EL_PO.length()-1);
		}
		
			return qty;
	}

	private static Double getacc(String EL_NO, String MODEL_NA, Connection conn) throws SQLException {
		// TODO Auto-generated method stub	
		PreparedStatement ps = null;
		ResultSet rs = null;		
		Double qty=0.0;

		String 	sql="SELECT SUM(PC_QTY) SPC_QTY FROM DSIDN08 WHERE PO_NO IN ('"+EL_PO.replace(",", "','")+"') AND EL_NO='"+EL_NO+"'";
	
//		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				if(!"".equals(rs.getString("SPC_QTY"))&&rs.getString("SPC_QTY")!=null){
				qty+=Double.valueOf(rs.getString("SPC_QTY"));
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

		EL_PO="";
		
		return qty;
	}
	
	private static String GetOd_noNum(String START, String MODEL_NA, String GROUP, String COLOR, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String num="";
		
		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' AND MODEL_NA LIKE '%"+MODEL_NA+"' AND "+GROUP.replace("GROUP ", "GROUP")+"='"+COLOR+"'";
//		System.out.println(">>>>>"+sql);
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
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
		}


		return num;
	}


	private static String GetKc(String MODEL_NA, String EL_NO, Connection Conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String num="";
		
		String 	sql="SELECT MT_QTY FROM DSID77 WHERE MODEL_NA='"+MODEL_NA+"' AND EL_NO='"+EL_NO+"'";
//		System.out.println(">>>>>"+sql);
		try {
			ps = Conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				num=rs.getString("MT_QTY");
			}else{
				num="0";
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


		return num;
	}
	
	private static void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
			
		row = sheet.createRow(0);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID01M.MODEL_NA"));		
		
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0194"));	
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0195"));
		
		row = sheet.createRow(1);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0171"));		
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0099"));		
		
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
		cell.setCellValue(Labels.getLabel("DSID22R.COLOR"));
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0196"));		
			
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0197"));		
			
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0198"));
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0199"));
//		cell.setCellValue("庫存可斬雙數");
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0200"));
	
		cell = row.createCell(10);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0201"));
		
		cell = row.createCell(11);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0202"));
		
		cell = row.createCell(12);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0203"));
		
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
