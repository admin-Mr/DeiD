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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Filedownload;
import util.Common;


public class DSIDN10M_ExcelUtil  {
	
	public static void ExcelExport( String title, String model_na,String starttime,String endtime) throws SQLException {
		// TODO Auto-generated method stub
		
		
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet4 = wb.createSheet("SHEET1");
		sheet4.setColumnWidth(0, 30*256);
		sheet4.setColumnWidth(1, 15*256);
		sheet4.setColumnWidth(2, 35*256);
		sheet4.setColumnWidth(3, 35*256);
		sheet4.setColumnWidth(4, 10*256);
		sheet4.setColumnWidth(5, 10*256);
		output(wb,sheet4,model_na,starttime,endtime);
		
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
	
	
	private static void output(HSSFWorkbook wb, HSSFSheet sheet4, String model_na, String starttime, String endtime) throws SQLException {
		// TODO Auto-generated method stub
		
	
		Connection Conn = Common.getService1Conn();
		Connection conn = Common.getDbConnection();

		HSSFRow row = null;
		HSSFCell cell = null;
		
		// 字體	
		HSSFFont font1 = wb.createFont();
		font1.setFontName("Calibri");    				//设置字體  
		font1.setFontHeightInPoints((short)10);    		//设置字体高度  
//		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 
					
					// 標題格式
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
					
		
		PreparedStatement  ps1 = null, ps2 = null;
		ResultSet  rs1 = null, rs2 = null;	
		
		int rowNum = 2;

		Header4(sheet4,style1,row,cell);
		
		List<Object>  MODEL_NALIST=new ArrayList<Object>();

		String ExSql="";
		if(!"".equals(model_na)){
			ExSql=" AND MODEL_NA='"+model_na+"'";
		}
		String 	sql1="SELECT DISTINCT MODEL_NA FROM DSID76 WHERE TO_CHAR(UP_DATE,'YYYY/MM/DD')  BETWEEN '"+starttime+"' AND  '"+endtime+"' "+ExSql+" AND MODEL_NA IS NOT NULL";	
		System.out.println(">>>>>"+sql1);
		try {
			ps1 = Conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();	
			while(rs1.next()){
				MODEL_NALIST.add(rs1.getString("MODEL_NA"));
			}
			rs1.close();
			ps1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<MODEL_NALIST.size();i++){
				
		List<Object>  His=getod_no(MODEL_NALIST.get(i).toString(),starttime,endtime,Conn);

		String sql2="SELECT MODEL_NA,A.EL_NO,B.EL_CNAME,A.MT_QTY FROM DSID76 A,DSEL00 B WHERE A.EL_NO=B.EL_NO AND MODEL_NA='"+MODEL_NALIST.get(i).toString()+"' AND  TO_CHAR(A.UP_DATE,'YYYY/MM/DD') BETWEEN '"+starttime+"' AND '"+endtime+"'  ORDER BY MODEL_NA ";
		int num=rowNum;
		try {
			ps2 = Conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){

					row = sheet4.createRow(rowNum);				
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
					if(His.size()!=0){
					cell.setCellValue(His.get(0).toString());
					}
					sheet4.addMergedRegion(new CellRangeAddress(num, rowNum, 3, 3));
					
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					if(His.size()!=0){
					cell.setCellValue(getSumyield(rs2.getString(1),rs2.getString(2),His.get(1).toString(),conn));
					}
					
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString(4));
					
					rowNum++;
			}
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
			if(rs2!=null){
				rs2.close();
			}
			if(ps2!=null){
				ps2.close();
			}

			Common.closeConnection(conn);
			Common.closeConnection(Conn);
		}

			num=rowNum;
		}
	}
	


	private static Double getSumyield(String model_na,  String EL_NO, String qty, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;		
		
		Double Syield=0.0;
		String 	sql="SELECT * FROM (SELECT SUM(YIELD) YIELD FROM DSID04_1 WHERE  MODEL_NA='"+model_na+"' AND EL_NO='"+EL_NO+"'\n" +
				"UNION\n" + 
				"SELECT SUM(YIELD) YIELD FROM DSID04_2 WHERE  MODEL_NA='"+model_na+"' AND EL_NO='"+EL_NO+"'\n" + 
				"UNION\n" + 
				"SELECT SUM(YIELD) YIELD FROM DSID04_3 WHERE  MODEL_NA='"+model_na+"' AND EL_NO='"+EL_NO+"') WHERE YIELD >0";

//		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Syield=Double.valueOf(rs.getString("YIELD"))*Double.valueOf(qty);
			}else{
				Syield=0.0;
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

	
			return Syield;
	}


	private static List<Object> getod_no( String model_na,String starttime, String endtime, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Object>  His=new ArrayList<Object>();
		String OD_NOLIST="";
		int qty=0;
		String ExSql="";
		if(!"".equals(model_na)){
			ExSql=" AND MODEL_NA='"+model_na+"'";
		}
		String 	sql="SELECT * FROM DSID65 WHERE TO_CHAR(PG_DATE,'YYYY/MM/DD')  BETWEEN '"+starttime+"' AND  '"+endtime+"' "+ExSql;	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				qty++;
				OD_NOLIST+=rs.getString("OD_NO")+",";
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

		if(OD_NOLIST.length()>0){
			OD_NOLIST=OD_NOLIST.substring(0,OD_NOLIST.length()-1);
			His.add(OD_NOLIST);
			His.add(qty);
		}
		
		
		
		return His;
	}


	private static void Header4(HSSFSheet sheet4, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
		
		row = sheet4.createRow(0);
		row.setHeightInPoints(30);		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0230"));	
		sheet4.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		
		row = sheet4.createRow(1);
		row.setHeightInPoints(20);		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSIDN10.MODEL_NA"));		
			
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSIDN10.EL_NO"));		
			
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSIDN10.EL_CNAME"));		
			
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0231"));		
			
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSIDN10.MT_QTY"));
			
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0232"));
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
