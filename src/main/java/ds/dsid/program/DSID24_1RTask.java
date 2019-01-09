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
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFPrintSetup;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.zul.Filedownload;

import util.Common;

public class DSID24_1RTask {

	public static void ExcelExport(String dATE, String NIKE_SH_ARITCLE, String WORK_ORDER_ID) throws SQLException {
		// TODO Auto-generated method stub
		String title="小票派工單";
		Connection Conn = getDB01Conn();
		Connection conn = Common.getDbConnection();
		
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		
		// 字體	
		HSSFFont font1 = wb.createFont();
		font1.setFontName("新細明體");    				//设置字體  
		font1.setFontHeightInPoints((short) 12);    		//设置字体高度  
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 			
		
		HSSFFont font2 = wb.createFont();
		font2.setFontName("新細明體");    				//设置字體  
		font2.setFontHeightInPoints((short) 10);    		//设置字体高度  
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 	
		
		HSSFFont font3 = wb.createFont();
		font3.setFontName("新細明體");    				//设置字體  
		font3.setFontHeightInPoints((short) 9);    		//设置字体高度  
//		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 	
		
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFont(font1);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style1.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style1.setFillPattern((short) 0);
		style1.setWrapText(true);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setFont(font2);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style2.setFillPattern((short) 0);
		style2.setWrapText(true);
		
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setFont(font3);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style3.setFillPattern((short) 0);
		style3.setWrapText(true);
		
		HSSFFont font4 = wb.createFont();
		font4.setFontName("新細明體");    				//设置字體  
		font4.setFontHeightInPoints((short) 10.5);    		//设置字体高度  
		font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 	
		
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setFont(font4);
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style4.setFillPattern((short) 0);
		style4.setWrapText(true);
		style4.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style4.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		HSSFFont font5 = wb.createFont();
		font5.setFontName("新細明體");    				//设置字體  
		font5.setFontHeightInPoints((short) 20);    		//设置字体高度  
		font5.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 	
		
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFont(font5);
		style5.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style5.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style5.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style5.setFillPattern((short) 0);

		HSSFFont font6 = wb.createFont();
		font6.setFontName("C39HrP48DhTt");    				//设置字體  
		font6.setFontHeightInPoints((short) 48);    		//设置字体高度  
		font6.setBoldweight(HSSFCellStyle.BORDER_THIN);
//		font6.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 	
		
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setFont(font6);
		style6.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style6.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style6.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style6.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style6.setFillPattern((short) 0);
		
		HSSFCellStyle style7 = wb.createCellStyle();
		style7.setFont(font3);
		style7.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style7.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style7.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style7.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style7.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
		style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style7.setFillPattern((short) 0);
		style7.setWrapText(true);
		style7.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style7.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		HSSFCellStyle style8 = wb.createCellStyle();
		style8.setFont(font2);
		style8.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style8.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style8.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		style8.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style8.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
		style8.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style8.setFillPattern((short) 0);
		style8.setWrapText(true);
		style8.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style8.setFillPattern(CellStyle.SOLID_FOREGROUND);
				
		HSSFSheet sheet = wb.createSheet("Sheet1");
		SetColumnWidth(sheet);	
				
		Exportexcel(wb,sheet,NIKE_SH_ARITCLE,dATE,WORK_ORDER_ID,conn,Conn,style1,style2,style3,style4,style5,style6,style7,style8);
		
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


	private static void SetColumnWidth(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		
		
		sheet1.setColumnWidth(0, 31*256);
		sheet1.setColumnWidth(1, 20*256);
		sheet1.setColumnWidth(2, 4*256);
		sheet1.setColumnWidth(3, 31*256);
		sheet1.setColumnWidth(4, 20*256);
		
		HSSFPrintSetup print = sheet1.getPrintSetup();//得到打印对象
//		print.setLandscape(false);//true，则表示页面方向为横向；否则为纵向
		print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//纸张设置
		print.setScale((short)95);//缩放比例80%(设置为0-100之间的值)
		sheet1.setMargin(HSSFSheet.TopMargin, (double)0.39); //页边距（上）
		sheet1.setMargin(HSSFSheet.BottomMargin, (double)0); //页边距（下）
		sheet1.setMargin(HSSFSheet.LeftMargin, (double)0.21); //页边距（左）
		sheet1.setMargin(HSSFSheet.RightMargin, (double)0); //页边距（右）


	}

	private static void Exportexcel(HSSFWorkbook wb, HSSFSheet sheet, String NIKE_SH_ARITCLE, String DATE, String WORK_ORDER_ID,
			Connection conn,Connection Conn, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4,
			HSSFCellStyle style5, HSSFCellStyle style6, HSSFCellStyle style7, HSSFCellStyle style8) throws SQLException {
		// TODO Auto-generated method stub
		
		HSSFRow row = null;
		HSSFCell cell = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");	
		
		PreparedStatement ps1 = null,ps2 = null;
		ResultSet rs1 = null,rs2 = null;
		
		String ExSql="";		
		if(!"".equals(NIKE_SH_ARITCLE)){
			ExSql+=" AND NIKE_SH_ARITCLE = '"+NIKE_SH_ARITCLE+"'";
		}
		if(!"".equals(WORK_ORDER_ID)){
			ExSql+=" AND WORK_ORDER_ID IN ( '"+WORK_ORDER_ID+"')";
		}
		
		int rowNum = 0;
		int cou=0,cellNum=0;
		String sql1="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+DATE+"' "+ExSql+" ORDER BY ALL_ROUND_NUM,MODEL_ROUND_NUM";
		System.err.println(">>>>>"+sql1);
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			while(rs1.next()){				
				cou++;
				//調整行數
				rowNum=(int) (24*(Math.floor((cou-1)/2)));			
				
				if(cou%2==1){
					cellNum=0;
				}else{
					cellNum=3;
				}
				System.err.println("cou:"+cou+"  rowNum:"+rowNum+" cellNum:"+cellNum);
				
				String sql2="SELECT * FROM DSID01 WHERE WORK_ORDER_ID='"+rs1.getString("WORK_ORDER_ID")+"'";
				System.err.println(">>>>>"+sql2);
				try {
					ps2 = conn.prepareStatement(sql2);
					rs2 = ps2.executeQuery();			
					if(rs2.next()){
						
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(16);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							String type="";
							if("1".equals(rs2.getString("TYPE"))){
								type="聖誕";
							}
							
							cell.setCellValue("次序號:"+rs2.getString("ROUND")+"     "+type);
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style1);
							cell.setCellValue("地區:"+rs2.getString("REGION"));
							
							List<String> Sh_list = GetSh(rs2.getString("SH_STYLENO"),conn);
							
							rowNum++;
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(24);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue("型體:"+rs2.getString("MODEL_NA")+"       "+Sh_list.get(0));
							
							cell = row.createCell(cellNum+1);
							sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, cellNum+1, cellNum+1));	
							cell.setCellType(1);
							cell.setCellStyle(style5);
							cell.setCellValue(rs2.getString("TOOLING_SIZE"));
							
							rowNum++;
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(14);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue("配色:"+rs2.getString("SH_STYLENO")+"      楦型:"+Sh_list.get(1));
						
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							
							rowNum++;
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(14);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue("尺寸(左:"+rs2.getString("LEFT_SIZE_RUN")+"  右:"+rs2.getString("RIGHT_SIZE_RUN")+")   ");
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							cell.setCellValue("派工: "+format.format(rs2.getDate("PG_DATE")));
							
							rowNum++;
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(14);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							cell.setCellValue("Ship Group ID:"+rs2.getString("SHIP_GROUP_ID"));
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							String modelnum=rs2.getString("MODEL_ROUND_NUM");
							if(modelnum.length()==1){
								modelnum="000"+modelnum;
							}else if(modelnum.length()==2){
								modelnum="00"+modelnum;
							}else if(modelnum.length()==3){
								modelnum="0"+modelnum;
							}
							cell.setCellValue("接单: "+format.format(rs2.getDate("PG_DATE")).replaceAll("/", "")+"_"+modelnum);
							
							rowNum++;
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(24);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style4);
							String Pid01="",Pid02="";
							if(!"".equals(rs2.getString("PID01"))&&rs2.getString("PID01")!=null){
								Pid01=rs2.getString("PID01").replace("null", "");
							}
							if(!"".equals(rs2.getString("PID02"))&&rs2.getString("PID02")!=null){
								Pid02=rs2.getString("PID02").replace("null", "");
							}
							cell.setCellValue("PID(左:"+Pid01+"  右:"+Pid02+")");
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style4);
							String PidColor=GetPidColor(rs2.getString("WORK_ORDER_ID"),conn);
							cell.setCellValue(PidColor);
									
							rowNum++;
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(14);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							cell.setCellValue("底部分類:"+Sh_list.get(2));
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							cell.setCellValue("客戶需求日:"+format.format(rs2.getDate("REQUSHIPDATE")));
							
							//Group 信息
							String LACE_GROUP="",GROUP_NAME="",LACE_COLOR="";
							for(int gr=1;gr<14 ;gr++){
								rowNum++;
								
								if(cou%2==1){
									row = sheet.createRow(rowNum);
								}else{
									row = sheet.getRow(rowNum);
								}
								if(gr==1){
									row.setHeightInPoints(24);
								}else{
									row.setHeightInPoints(16);
								}								
								
								cell = row.createCell(cellNum);
								cell.setCellType(1);
								if(gr==6){
									cell.setCellStyle(style7);
								}else{
									cell.setCellStyle(style3);
								}							
								GROUP_NAME=GetGroupName(rs2.getString("NIKE_SH_ARITCLE"),"GROUP"+gr,conn);
								cell.setCellValue(GROUP_NAME);
								
								cell = row.createCell(cellNum+1);
								cell.setCellType(1);
								if(gr==6){
									cell.setCellStyle(style8);
								}else{
									cell.setCellStyle(style2);
								}	
								cell.setCellValue(rs2.getString("GROUP"+gr));
								
								if(!"".equals(GROUP_NAME)&&GROUP_NAME!=null){
									if("鞋帶".equals(GROUP_NAME)){
										LACE_GROUP="GROUP"+gr;
										LACE_COLOR=rs2.getString("GROUP"+gr);
									}
								}

							}	
							
							rowNum++;
							
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(14);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							cell.setCellValue("鞋帶長度                "+get_lace_length(rs2.getString("MODEL_NA"),Sh_list,rs2.getString("LEFT_SIZE_RUN"),LACE_GROUP,LACE_COLOR,Conn)+"    "+LACE_COLOR);
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							cell.setCellValue(LACE_COLOR);
							
							rowNum++;
							
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(14);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							cell.setCellType(1);
							cell.setCellStyle(style3);
							cell.setCellValue("型體內次序號");
							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style2);
							
							int a=rs2.getString("ON_ORDER").length();
							String Num="";
							for (int k=0;k<4-a;k++){
								Num+="0";
							}
							SimpleDateFormat df = new SimpleDateFormat("MM/dd");
							cell.setCellValue(df.format(rs2.getDate("PG_DATE")).toString()+"     "+Num+rs2.getString("ON_ORDER"));
							
							rowNum++;
							
							if(cou%2==1){
								row = sheet.createRow(rowNum);
								row.setHeightInPoints(48);
							}else{
								row = sheet.getRow(rowNum);
							}
							
							cell = row.createCell(cellNum);
							sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, cellNum, cellNum+1));	
							cell.setCellType(1);
							cell.setCellStyle(style6);
							cell.setCellValue("*"+rs2.getString("WORK_ORDER_ID")+"*");

							
							cell = row.createCell(cellNum+1);
							cell.setCellType(1);
							cell.setCellStyle(style6);
							
					}					
					ps2.close();
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}

		
			ps1.close();
			rs1.close();
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
		}

		
	}


	private static String GetPidColor(String WORK_ORDER_ID, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String PidColor="";
		PreparedStatement ps = null,ps2 = null;
		ResultSet rs = null,rs2 = null;
		String sql="SELECT DISTINCT GROUP_NO FROM DSID01_TEMP2 WHERE WORK_ORDER_ID='"+WORK_ORDER_ID+"' AND TYPE='PID'";
//		System.out.println(">>>sql"+sql);		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				String sql2="SELECT "+rs.getString("GROUP_NO")+" COLOR FROM DSID01 WHERE WORK_ORDER_ID='"+WORK_ORDER_ID+"'";
//				System.out.println(">>>sql2"+sql2);		
				try {
					ps2 = conn.prepareStatement(sql2);
					rs2 = ps2.executeQuery();
					if(rs2.next()){
						PidColor=rs2.getString("COLOR");					  
					}
					rs2.close();
					ps2.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			  
			}
			rs.close();
			ps.close();
		} catch (SQLException e){
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(rs2!=null){
				rs2.close();
			}
			if(ps2!=null){
				ps2.close();
			}
		}
	
		
		return PidColor;
	}


	private static String get_lace_length(String MODEL_NA, List<String> sh_list, String LEFT_SIZE_RUN, String LACE_GROUP, String LACE_COLOR, Connection Conn) throws SQLException {
		// TODO Auto-generated method stub
		String lace_len = "";
		
		int size=Integer.valueOf((int) (Double.valueOf(LEFT_SIZE_RUN)*2))-1;
		String sql = "SELECT GROUP_NO,COLOR,(SELECT EL_CNAME FROM DSEL00 WHERE EL_NO=ID26.EL_NO)EL_CNAME FROM DSID26 ID26 "+
			"WHERE (MODEL_NA, VERSION, SH_LAST, PART_NO1, PART_NO, GROUP_NO) IN (SELECT ID35.MODEL_NA,ID35.VERSION,"+
			"ID35.SH_LASTNO,ID35.PART_NO1,ID35.PART_NO2,ID35.GROUP_NO FROM DSID35 ID35, DSID36 ID36 WHERE ID35.MODEL_NA = '"
			+MODEL_NA+"' AND ID35.VERSION = '"+sh_list.get(0)+"-"+sh_list.get(2)+"' AND ID35.SH_LASTNO = '"+sh_list.get(1)+"' AND ID35.PART_NO2 IN (SELECT "+
			"PART_NO FROM DSEL10 WHERE PART_CNA LIKE '鞋帶%') AND ID35.MODEL_NA = ID36.MODEL_NA AND ID35.VERSION = ID36.VERSION "
           +"AND ID35.SH_LASTNO = ID36.SH_LASTNO AND ID35.PART_NO1 = ID36.PART_NO1 AND ID35.PART_NO2 = ID36.PART_NO2 AND ID36.Q"+size+" > 0) AND GROUP_NO='"+LACE_GROUP.replace("GROUP", "GROUP ")+"' AND COLOR='"+LACE_COLOR+"'";

		PreparedStatement pstm = null;
		ResultSet rs = null;
//		System.out.println(">>>sql"+sql);
		
		try {
			pstm = Conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next())
			{
				lace_len=rs.getString("EL_CNAME").substring(rs.getString("EL_CNAME").length()-5);
			  
			}
			rs.close();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}

			if(pstm!=null){
				pstm.close();
			}
			
		}

		
		return lace_len;
	}


	private static List<String> GetSh(String SH_STYLENO, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		List<String> list = new ArrayList<String>();
		String Sql="SELECT * FROM DSID12 WHERE SH_STYLENO='"+SH_STYLENO+"'";
//		System.err.println(">>>"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				list.add(rs1.getString("UPPER_CLASS"));
				list.add(rs1.getString("SH_LAST"));
				list.add(rs1.getString("SOLE_CLASS"));
			}else{
				list.add("");
				list.add("");
				list.add("");
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
		}

		
		return list;
	}


	private static String GetGroupName(String nIKE_SH_ARITCLE, String GROUP, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		String name="";
		String Sql="SELECT "+GROUP+"_NA NAME FROM DSID10 WHERE NIKE_SH_ARITCLE='"+nIKE_SH_ARITCLE+"' ";
//		System.err.println(">>>"+Sql);
		try {
			ps1 = conn.prepareStatement(Sql);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				name=rs1.getString("NAME");
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
		}
		
		return name;
	}
	
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
