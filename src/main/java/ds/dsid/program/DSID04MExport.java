package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.hssf.record.CFRuleBase.ComparisonOperator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFFontFormatting;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.SimpleDateFormat;

import ds.common.services.CRUDService;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID04MExport extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Textbox txtMODEL_NA;
	static String EL_PO="",EL_NO_LIST="";;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");		
	
	}
	
	@Listen("onClick =#btnexport")
	public void onClickbtnexport(Event event) throws Exception{

		filterHeader();
	}
	
	private  void filterHeader() {			
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		
		String  MODEL_NA = txtMODEL_NA.getValue(); // 型體名稱
		System.err.println(">>>>" + MODEL_NA );
		
		if(!"".equals(MODEL_NA)){
			Connection conn = Common.getDbConnection();
			Connection Conn = getDB01Conn();
			
			HSSFWorkbook wb = new HSSFWorkbook();		

			// 字體	
			HSSFFont font1 = wb.createFont();
			font1.setFontName("Calibri");    				//设置字體  
			font1.setFontHeightInPoints((short)9);    		//设置字体高度  
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);		//设置字體樣式 
			
			HSSFFont font2 = wb.createFont();
			font2.setFontName("Calibri");    				//设置字體  
			font2.setFontHeightInPoints((short)8);    		//设置字体高度  
			
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
			
			//正文格式
			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setFont(font2);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style2.setFillPattern((short) 0);
			style2.setWrapText(true);
			
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setFont(font2);
			style3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style3.setFillPattern((short) 0);
			style3.setWrapText(true);
		    style3.setDataFormat(wb.createDataFormat().getFormat("0"));
		    
			HSSFCellStyle style4 = wb.createCellStyle();
			style4.setFont(font2);
			style4.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style4.setFillPattern((short) 0);
			style4.setWrapText(true);
			style4.setDataFormat(wb.createDataFormat().getFormat("$0.00"));
			
			HSSFCellStyle style5 = wb.createCellStyle();
			style5.setFont(font2);
			style5.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style5.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style5.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style5.setFillPattern((short) 0);
			style5.setWrapText(true);
			style5.setDataFormat(wb.createDataFormat().getFormat("0%"));
			
			HSSFCellStyle style6 = wb.createCellStyle();
			style6.setFont(font2);
			style6.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style6.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style6.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style6.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style6.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style6.setFillPattern((short) 0);
			style6.setWrapText(true);
			style6.setDataFormat(wb.createDataFormat().getFormat("0.00000"));
			
			HSSFCellStyle style7 = wb.createCellStyle();
			style7.setFont(font2);
			style7.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style7.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style7.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style7.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style7.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style7.setFillPattern((short) 0);
			style7.setWrapText(true);
			style7.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			
			HSSFCellStyle style8 = wb.createCellStyle();
			style8.setFont(font2);
			style8.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style8.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style8.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style8.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style8.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
			style8.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style8.setFillPattern((short) 0);
			style8.setWrapText(true);
			style8.setDataFormat(wb.createDataFormat().getFormat("0.0"));
			
			HSSFCellStyle style9 = wb.createCellStyle();
			style9.setFont(font1);
			style9.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			style9.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			style9.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			style9.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			style9.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平居中    
			style9.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style9.setFillPattern((short) 0);
			style9.setWrapText(true);
			
			//分配材料合理庫存值
			SetEl_fp(MODEL_NA,conn);
			
			HSSFSheet sheet1 = wb.createSheet(MODEL_NA);
			HSSFSheet sheet2 = wb.createSheet("VAMP");
			HSSFSheet sheet3 = wb.createSheet("label");
			HSSFSheet sheet4 = wb.createSheet("lace");
//			HSSFSheet sheet5 = wb.createSheet("Sockliner");
			
			SetColumnWidth(sheet1,sheet2,sheet3,sheet4);
			
			try {
				SetSheet1(wb,sheet1,style1,style2,style3,style4,style5,style6 ,conn,Conn,MODEL_NA);
				SetSheet2(wb,sheet2,style1,style2,style3,style4,style7,style6 ,conn,Conn,MODEL_NA);
				SetSheet3(wb,sheet3,style1,style2,style3,style4,style5,style6 ,conn,Conn,MODEL_NA);
				SetSheet4(wb,sheet4,style9,style2,style3,style4,style7,style8 ,conn,Conn,MODEL_NA);
//				SetSheet5(wb,sheet5,style1,style2,style3,style4,style5,style6 ,conn,Conn,MODEL_NA);	
		        wb.write(stream);
		        Date date = new Date();
				SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
				
				byte[] content = stream.toByteArray();
			    InputStream is = new ByteArrayInputStream(content);

			    //儲存位置、名稱
				Filedownload.save(is, "application/xls", format1.format(date));
				is.close();
				stream.flush();
				stream.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Common.closeConnection(conn);
				
				try {
					if (!Conn.isClosed())
						Conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.err.println(" —————————— : 結束  : —————————— ");
			}
		}else{
			Messagebox.show("型體為空，請核查輸入！！！");
		}		
	}
	
	private void SetColumnWidth(HSSFSheet sheet1, HSSFSheet sheet2, HSSFSheet sheet3, HSSFSheet sheet4) {
		// TODO Auto-generated method stub
		
		//設置欄位寬度   X*256 → X字符的寬度
		sheet1.setColumnWidth(0, 15*256);
		sheet1.setColumnWidth(1, 15*256);
		sheet1.setColumnWidth(2, 15*256);
		sheet1.setColumnWidth(3, 15*256);
		sheet1.setColumnWidth(4, 20*256);
		sheet1.setColumnWidth(5, 25*256);
		
		sheet2.setColumnWidth(1, 12*256);
		
		sheet3.setColumnWidth(1, 12*256);
		sheet3.setColumnWidth(2, 30*256);
		
		sheet4.setColumnWidth(0, 35*256);
		for(int i=2;i<22;i++){
			sheet4.setColumnWidth(i, 12*256);
		}
	}

	private void SetSheet4(HSSFWorkbook wb, HSSFSheet sheet4, HSSFCellStyle style9, HSSFCellStyle style2, HSSFCellStyle style3,
			HSSFCellStyle style4, HSSFCellStyle style7, HSSFCellStyle style8, Connection conn, Connection Conn, String MODEL_NA) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement  ps1 = null, ps2 = null;
		ResultSet  rs1 = null, rs2 = null;
		
		String sql1 = "SELECT * FROM DSID04 WHERE MODEL_NA='"+MODEL_NA+"'";
		
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				row = sheet4.createRow(2);
				row.setHeightInPoints(25);
				
//				cell = row.createCell(0);
//				cell.setCellType(1);
//				cell.setCellStyle(style1);
//				cell.setCellValue(MODEL_NA);
//				sheet4.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
//				
//				cell = row.createCell(1);
//				cell.setCellStyle(style1);
//				
//				cell = row.createCell(2);
//				cell.setCellStyle(style1);
				
				cell = row.createCell(10);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("UPD");
				
				cell = row.createCell(11);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs1.getString("VAMP_UPD")));
				
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		int rowNum = 14,cellNum=2;
		String Last_color="";
		int c_Num=cellNum;
		String sql2 = "SELECT * FROM DSID04_4 WHERE MODEL_NA='"+MODEL_NA+"' ORDER BY EL_SEQ";
		
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){
				if(cellNum%20==2){
											
					if(cellNum>20){
						rowNum=rowNum+17;
						cellNum=2;
					}
					// 表頭設定, 大小表頭		
					Header4(sheet4,style9,row,cell,rowNum);
					//統計欄
					
					row = sheet4.getRow(rowNum);				
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					
					row = sheet4.getRow(rowNum+1);				
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					
					row = sheet4.getRow(rowNum+2);				
					cell = row.createCell(1);
					cell.setCellStyle(style2);
					
					//庫存
					row = sheet4.getRow(rowNum+3);				
					cell = row.createCell(1);
					cell.setCellStyle(style3);
					
					row = sheet4.getRow(rowNum+4);				
					cell = row.createCell(1);
					cell.setCellStyle(style3);
					
					row = sheet4.getRow(rowNum+5);				
					cell = row.createCell(1);
					cell.setCellStyle(style7);
					
					//在途
					row = sheet4.getRow(rowNum+6);				
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style3);
					cell.setCellFormula("SUM(C"+(rowNum+7)+":V"+(rowNum+7)+")");

					row = sheet4.getRow(rowNum+7);				
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style8);
					cell.setCellFormula("SUM(C"+(rowNum+8)+":V"+(rowNum+8)+")");
					
					row = sheet4.getRow(rowNum+8);				
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style8);
					cell.setCellFormula("SUM(C"+(rowNum+9)+":V"+(rowNum+9)+")");
					
					row = sheet4.getRow(rowNum+9);				
					cell = row.createCell(1);
					cell.setCellStyle(style3);
					
					row = sheet4.getRow(rowNum+10);				
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style3);
					cell.setCellFormula("SUM(C"+(rowNum+11)+":V"+(rowNum+11)+")");
					
					row = sheet4.getRow(rowNum+11);				
					cell = row.createCell(1);
					cell.setCellStyle(style3);
					
					row = sheet4.getRow(rowNum+12);				
					cell = row.createCell(1);
					cell.setCellStyle(style3);
					
					row = sheet4.getRow(rowNum+13);				
					cell = row.createCell(1);
					cell.setCellStyle(style3);
					
					row = sheet4.getRow(rowNum+14);				
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style3);
					cell.setCellFormula("SUM(C"+(rowNum+15)+":V"+(rowNum+15)+")");					
				}
				
				row = sheet4.getRow(rowNum);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("COLOR"));				
				if(Last_color.compareTo(rs2.getString("COLOR"))==0){
//					System.err.println(">>>合併  行"+rowNum+" >>> 行 "+ rowNum+" >>>列 "+  c_Num+" >>>列  "+ cellNum);
					sheet4.addMergedRegion(new CellRangeAddress(rowNum, rowNum, c_Num, cellNum));					
				}else{					
					c_Num=cellNum;
//					System.err.println(">>>顏色轉換");
				}
				Last_color=rs2.getString("COLOR");
				
				row = sheet4.getRow(rowNum+1);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NA"));
				
				row = sheet4.getRow(rowNum+2);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NO"));
				
				//庫存
				row = sheet4.getRow(rowNum+3);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(getkc(rs2.getString("EL_NO"), MODEL_NA, Conn));
				
				row = sheet4.getRow(rowNum+4);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue("");
				
				row = sheet4.getRow(rowNum+5);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style7);
				cell.setCellValue(Double.valueOf(rs2.getString("COLOR_PRE")));
				
				//在途
				row = sheet4.getRow(rowNum+6);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(getbuy(rs2.getString("EL_NO"), MODEL_NA, Conn)-getacc(rs2.getString("EL_NO"), MODEL_NA, conn));

				row = sheet4.getRow(rowNum+7);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style8);
				cell.setCellFormula("$L$3*"+((char) (cellNum+65))+(rowNum+6));
				
				row = sheet4.getRow(rowNum+8);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style8);
				cell.setCellFormula(""+((char) (cellNum+65))+(rowNum+8)+"*45");
				
				row = sheet4.getRow(rowNum+9);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula(""+((char) (cellNum+65))+(rowNum+9)+"-("+((char) (cellNum+65))+(rowNum+4)+"+"+((char) (cellNum+65))+(rowNum+7)+")");
				
				row = sheet4.getRow(rowNum+10);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue("");
				
				row = sheet4.getRow(rowNum+11);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula(""+((char) (cellNum+65))+(rowNum+4)+"/("+((char) (cellNum+65))+((rowNum+6))+"*$L$3)");
				
				row = sheet4.getRow(rowNum+12);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs2.getString("MIN_DAY")));
				
				row = sheet4.getRow(rowNum+13);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula(""+((char) (cellNum+65))+(rowNum+5));
				
				row = sheet4.getRow(rowNum+14);				
				cell = row.createCell(cellNum);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs2.getString("INI_BUY")));
				
				cellNum++;
				
			}
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	private void Header4(HSSFSheet sheet4, HSSFCellStyle style9, HSSFRow row, HSSFCell cell, int rowNum) {
		// TODO Auto-generated method stub
		
		row = sheet4.createRow(rowNum);
		row.setHeightInPoints(20);		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("");
		
		row = sheet4.createRow(rowNum+1);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("長度Length");
		
		row = sheet4.createRow(rowNum+2);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Fct Unique ID (FUI)");
		
		row = sheet4.createRow(rowNum+3);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("庫存inventory");
		
		row = sheet4.createRow(rowNum+4);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Monthly usage每月用量");
		
		row = sheet4.createRow(rowNum+5);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Size Percentage");
		
		row = sheet4.createRow(rowNum+6);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Materials in transit在途量");
		
		row = sheet4.createRow(rowNum+7);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("average UPD for the coming 45days(1/1 – 2/15), data from FEB production plan");
		
		row = sheet4.createRow(rowNum+8);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Total demand for the coming 45days(1/1-2/15), data from FEB production plan");
		
		row = sheet4.createRow(rowNum+9);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Q'ty to buy需補料");
		
		row = sheet4.createRow(rowNum+10);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("adjust buy需補料");
		
		row = sheet4.createRow(rowNum+11);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("可用天數 Material after buy days");
		
		row = sheet4.createRow(rowNum+12);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("安全天數 safty days");
		
		row = sheet4.createRow(rowNum+13);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("安全庫存量 safety inventory");
		
		row = sheet4.createRow(rowNum+14);
		row.setHeightInPoints(20);
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style9);
		cell.setCellValue("Order History Initial buy");
		
	}

	private void SetSheet3(HSSFWorkbook wb, HSSFSheet sheet3, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3,
			HSSFCellStyle style4, HSSFCellStyle style5, HSSFCellStyle style6, Connection conn, Connection Conn, String MODEL_NA) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement  ps1 = null, ps2 = null;
		ResultSet  rs1 = null, rs2 = null;			

		// 表頭設定, 大小表頭
		Header3(sheet3,style1,row,cell);		
		
		String sql1 = "SELECT * FROM DSID04 WHERE MODEL_NA='"+MODEL_NA+"'";
		
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				row = sheet3.createRow(0);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(MODEL_NA);
				sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
				
				cell = row.createCell(1);
				cell.setCellStyle(style1);
				
				cell = row.createCell(2);
				cell.setCellStyle(style1);
				
				cell = row.createCell(8);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("UPD");
				
				cell = row.createCell(9);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs1.getString("MODEL_UPD")));
				
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		int rowNum = 2;
		int num=rowNum;
		String Last_color="";
		String sql2 = "SELECT * FROM DSID04_3 WHERE MODEL_NA='"+MODEL_NA+"' ORDER BY EL_SEQ";
		
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){
				
				row = sheet3.getRow(1);
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("預計用量 (estimated usage "+rs2.getString("ET_US")+")");
				
				row = sheet3.createRow(rowNum);
				row.setHeightInPoints(20);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("COLOR"));
				if(Last_color.compareTo(rs2.getString("COLOR"))==0){
					sheet3.addMergedRegion(new CellRangeAddress(num, rowNum, 0, 0));					
				}else{					
					num=rowNum;
//					System.err.println(">>>顏色轉換");
				}
				Last_color=rs2.getString("COLOR");
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NO"));
				
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NA"));
				
				//庫存
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(getkc(rs2.getString("EL_NO"), MODEL_NA, Conn));
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style5);
				cell.setCellValue(Double.valueOf(rs2.getString("COLOR_PRE")));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(Double.valueOf(rs2.getString("YIELD")));
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("E"+(rowNum+1)+"*F"+(rowNum+1)+"*$J$1*"+rs2.getString("ET_US"));
				
				//在途
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(getbuy(rs2.getString("EL_NO"), MODEL_NA, Conn)-getacc(rs2.getString("EL_NO"), MODEL_NA, conn));
				
				cell = row.createCell(8);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("G"+(rowNum+1)+"-(D"+(rowNum+1)+"+H"+(rowNum+1)+")");
				
				cell = row.createCell(9);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue("");
				
				cell = row.createCell(10);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs2.getString("INI_BUY")));
				
				rowNum++;
			}
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}

	private void Header3(HSSFSheet sheet2, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
		row = sheet2.createRow(1);
		row.setHeightInPoints(40);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("顏色\n color");

		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("材料料號\n material #");
	
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("分段");
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("庫存量\n inventory");
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("百分比\n %");
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("用量/碼\n yield/ yard");
		
//		cell = row.createCell(6);
//		cell.setCellType(1);
//		cell.setCellStyle(style1);
//		cell.setCellValue("");
		
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("在途\n in transit");
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Est PO Qty");
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Actual PO Qty");
		
		cell = row.createCell(10);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Order History\n Initial buy");
		
	}
	
	private void SetSheet2(HSSFWorkbook wb, HSSFSheet sheet2, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3,
			HSSFCellStyle style4, HSSFCellStyle style7, HSSFCellStyle style6, Connection conn, Connection Conn, String MODEL_NA) {
		// TODO Auto-generated method stub
		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement  ps1 = null, ps2 = null;
		ResultSet  rs1 = null, rs2 = null;			

		// 表頭設定, 大小表頭
		Header2(sheet2,style1,row,cell);		
		
		String sql1 = "SELECT * FROM DSID04 WHERE MODEL_NA='"+MODEL_NA+"'";
		
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				row = sheet2.createRow(0);
				row.setHeightInPoints(30);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(MODEL_NA);
				sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
				
				cell = row.createCell(1);
				cell.setCellStyle(style1);
				
				cell = row.createCell(2);
				cell.setCellStyle(style1);
				
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("UPD");
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs1.getString("VAMP_UPD")));
				
				cell = row.createCell(10);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("MOQ: 30Y");
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		int rowNum = 2;
		int num=rowNum;
		String Last_color="";
		String sql2 = "SELECT * FROM DSID04_2 WHERE MODEL_NA='"+MODEL_NA+"' ORDER BY EL_SEQ";
		
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){
				
				row = sheet2.getRow(1);
				
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue("預計用量 (estimated usage "+rs2.getString("ET_US")+")");
				
				row = sheet2.createRow(rowNum);
				row.setHeightInPoints(20);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("COLOR"));
				if(Last_color.compareTo(rs2.getString("COLOR"))==0){
					sheet2.addMergedRegion(new CellRangeAddress(num, rowNum, 0, 0));					
				}else{					
					num=rowNum;
//					System.err.println(">>>顏色轉換");
				}
				Last_color=rs2.getString("COLOR");
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NO"));
				
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NA"));
				
				//庫存
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(getkc(rs2.getString("EL_NO"), MODEL_NA, Conn));
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style7);
				cell.setCellValue(Double.valueOf(rs2.getString("COLOR_PRE")));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style6);
				cell.setCellFormula("$E$1*E"+(rowNum+1)+"*G"+(rowNum+1)+"");
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style6);
				cell.setCellValue(Double.valueOf(rs2.getString("YIELD")));
				
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellFormula("F"+(rowNum+1)+"*"+rs2.getString("ET_US"));
				
				//在途
				cell = row.createCell(8);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(getbuy(rs2.getString("EL_NO"), MODEL_NA, Conn)-getacc(rs2.getString("EL_NO"), MODEL_NA, conn));
				
				cell = row.createCell(9);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("H"+(rowNum+1)+"-(D"+(rowNum+1)+"+I"+(rowNum+1)+")");
				
				cell = row.createCell(10);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue("");
				
				cell = row.createCell(11);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				if(!"".equals(rs2.getString("INI_BUY"))&&rs2.getString("INI_BUY")!=null){
					cell.setCellValue(Double.valueOf(rs2.getString("INI_BUY")));
				}else{
					cell.setCellValue("");
				}			
				
				rowNum++;
			}
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}

	private void Header2(HSSFSheet sheet2, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
		row = sheet2.createRow(1);
		row.setHeightInPoints(40);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("顏色\n color");

		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("材料料號\n material #");

		
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("材料品名\n material");
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("庫存量\n inventory");
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("百分比\n %");
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("用量/天\n(Yield /day)");
		
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Yield");
		
		//
//		cell = row.createCell(7);
//		cell.setCellType(1);
//		cell.setCellStyle(style1);
//		cell.setCellValue("");
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("在途\n in transit");
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Qty Needed");
		
		cell = row.createCell(10);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("adjust buy");
		
		cell = row.createCell(11);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Order History\n Initial buy");
		
	}

	private void SetSheet1(HSSFWorkbook wb, HSSFSheet sheet1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4, HSSFCellStyle style5, HSSFCellStyle style6, Connection conn, Connection Conn, String MODEL_NA) {
		// TODO Auto-generated method stub
		
		//區域條件格式設置
		HSSFSheetConditionalFormatting sheetCF = sheet1.getSheetConditionalFormatting();
		
		HSSFConditionalFormattingRule ruleRed = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"YES\"");
		HSSFFontFormatting fill = ruleRed.createFontFormatting(); //字體色
		fill.setFontColorIndex(IndexedColors.RED.index);		
		CellRangeAddress[] regions = { CellRangeAddress.valueOf( "$AH$14:$AH$1000") };
		sheetCF.addConditionalFormatting(regions,ruleRed);
		
		HSSFRow row = null;
		HSSFCell cell = null;
		
		PreparedStatement  ps1 = null, ps2 = null;
		ResultSet  rs1 = null, rs2 = null;	
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");	

		// 表頭設定, 大小表頭
		Header1(sheet1,style1,row,cell);		
		
		String sql1 = "SELECT * FROM DSID04 WHERE MODEL_NA='"+MODEL_NA+"'";
		
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				row = sheet1.getRow(2);
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(MODEL_NA);
				
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(format.format(rs1.getDate("LA_DATE")));
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(format.format(rs1.getDate("DR_DATE")));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(format.format(new Date()));
				
				row = sheet1.getRow(11);
				
				cell = row.createCell(28);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs1.getString("MODEL_UPD")));
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		int rowNum = 5;
		
		String sql3 = "SELECT * FROM DSID12 WHERE MODEL_NA='"+MODEL_NA+"'";
		int codecell=1;
		try {
			ps1 = conn.prepareStatement(sql3);
			rs1 = ps1.executeQuery();			
			while(rs1.next()){
				if(codecell>5){
					rowNum++;
					codecell=1;
				}
				row = sheet1.getRow(rowNum);
				
				cell = row.createCell(codecell);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(rs1.getString("SH_STYLENO"));
				codecell++;


			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rowNum++;
		String sql4 = "SELECT * FROM DSID12 WHERE MODEL_NA='W "+MODEL_NA+"'";
		codecell=1;
		try {
			ps1 = conn.prepareStatement(sql4);
			rs1 = ps1.executeQuery();			
			while(rs1.next()){				
				if(codecell>5){
					rowNum++;
					codecell=1;
				}
				row = sheet1.getRow(rowNum);
				
				cell = row.createCell(codecell);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(rs1.getString("SH_STYLENO"));
				codecell++;
			}
			ps1.close();
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		rowNum = 13;//正文從14行開始
		//分配庫存&在途參數
		Double vtkc_qty=0.0;
		Double vtzt_qty=0.0;
		String LAST_EL_NO="";
		int Cel_no=0,Cou=1;
		
		String sql2 = "SELECT * FROM DSID04_1 WHERE MODEL_NA='"+MODEL_NA+"' ORDER BY EL_NO,TO_NUMBER(EL_FP)";
		
		try {
			ps2 = conn.prepareStatement(sql2);
			rs2 = ps2.executeQuery();			
			while(rs2.next()){
				row = sheet1.createRow(rowNum);
				row.setHeightInPoints(24);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("GROUP_NO"));
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("MT_USAGE"));
				
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NO"));
				
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("SU_NA"));
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("GR_NA"));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_NA"));
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("COLOR"));
				
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(format.format(rs2.getDate("ELLA_DATE")));
				
				cell = row.createCell(8);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(format.format(rs2.getDate("ELDR_DATE")));
				
				cell = row.createCell(9);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellFormula("I"+(rowNum+1)+"-F$3");
				
				cell = row.createCell(10);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellFormula("I"+(rowNum+1)+"-H"+(rowNum+1)+"");
				
				cell = row.createCell(11);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(format.format(rs2.getDate("LA_TIME")));
				
				cell = row.createCell(12);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellFormula("IF($F$3<H"+(rowNum+1)+",\"Initial buy\",IF((J"+(rowNum+1)+"-O"+(rowNum+1)+")>AI"+(rowNum+1)+",\"Replenishment\",\"Ramp down\"))");
				
				cell = row.createCell(13);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs2.getString("EL_UNIT"));
				
				cell = row.createCell(14);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs2.getString("MTLDAY")));
				
				cell = row.createCell(15);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs2.getString("SU_MOQ")));
				
				cell = row.createCell(16);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellValue(Double.valueOf(rs2.getString("PO_PRICE")));
				
				cell = row.createCell(17);
				cell.setCellType(1);
				cell.setCellStyle(style6);
				cell.setCellValue(Double.valueOf(rs2.getString("YIELD")));
				
				if(!"".equals(rs2.getString("EL_NO"))&&rs2.getString("EL_NO")!=null){
					

				 Double kc_qty=Double.valueOf(getkc(rs2.getString("EL_NO"),MODEL_NA,Conn));
				 Double zt_qty=getbuy(rs2.getString("EL_NO"),MODEL_NA,Conn)-getacc(rs2.getString("EL_NO"),MODEL_NA,conn);

				  if(rs2.getString("EL_NO").compareTo(LAST_EL_NO)!=0){
					  vtkc_qty=kc_qty;//臨時庫存賦值
					  vtzt_qty=zt_qty;//臨時在途賦值
					  Cel_no=GetCel_no(MODEL_NA,rs2.getString("EL_NO"),conn);//同材料筆數
				  }else{
					  Cou++;//重複次數
				  }
				  LAST_EL_NO=rs2.getString("EL_NO");
				
				
				//庫存
				cell = row.createCell(18);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				if(Cou==Cel_no){
					  cell.setCellValue(vtkc_qty);
				  }else{
					  if(Double.valueOf(rs2.getString("EL_FP"))<=vtkc_qty){
						  cell.setCellValue(Double.valueOf(rs2.getString("EL_FP")));
						  
					  }else{
						  cell.setCellValue(vtkc_qty);
					  }
				  }
				
				//在途
				cell = row.createCell(21);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				if(Cel_no==Cou){
					  cell.setCellValue(vtzt_qty);
					  Cou=1; //重置重複次數
				  }else{
					  if(Double.valueOf(rs2.getString("EL_FP"))<=vtkc_qty){
						  cell.setCellValue(0);
						  vtkc_qty=vtkc_qty-Double.valueOf(rs2.getString("EL_FP"));
					  }else {
						  if(Double.valueOf(rs2.getString("EL_FP"))<=vtzt_qty&&vtkc_qty<Double.valueOf(rs2.getString("EL_FP"))){
							  cell.setCellValue(Double.valueOf(rs2.getString("EL_FP"))-vtkc_qty);
							  vtzt_qty=vtzt_qty-(Double.valueOf(rs2.getString("EL_FP"))-vtkc_qty);
							  vtkc_qty=0.0;
						  }else{
							  cell.setCellValue(vtzt_qty);
						  }
					  }
				  }	
				}else{
					//VAMP前面片
					if(rs2.getString("COLOR").contains("/")&&rs2.getString("COLOR").length()>1){
						cell = row.createCell(18);
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(getkc2("DSID04_2", MODEL_NA, rs2.getString("COLOR").substring(0, 3), conn, Conn));
						
						cell = row.createCell(21);
						cell.setCellType(1);
						cell.setCellStyle(style3);
						cell.setCellValue(getbuy2(MODEL_NA,Conn)-getacc2(MODEL_NA,conn));
					}else if(rs2.getString("COLOR").contains("/")&&rs2.getString("COLOR").length()==1){
						//標籤
						cell = row.createCell(18);
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(getkc2("DSID04_3", MODEL_NA, rs2.getString("COLOR"), conn, Conn));
						
						cell = row.createCell(21);
						cell.setCellType(1);
						cell.setCellStyle(style3);
						cell.setCellValue(getbuy2(MODEL_NA,Conn)-getacc2(MODEL_NA,conn));
					}else{
						//鞋帶
						cell = row.createCell(18);
						cell.setCellType(1);
						cell.setCellStyle(style2);
						cell.setCellValue(getkc2("DSID04_4", MODEL_NA, rs2.getString("COLOR"), conn, Conn));
						
						cell = row.createCell(21);
						cell.setCellType(1);
						cell.setCellStyle(style3);
						cell.setCellValue(getbuy2(MODEL_NA,Conn)-getacc2(MODEL_NA,conn));
					}

				}
				
				cell = row.createCell(19);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("S"+(rowNum+1)+"*Q"+(rowNum+1)+"");
				
				cell = row.createCell(20);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF(ISERROR((S"+(rowNum+1)+"/R"+(rowNum+1)+")/($AC$12*AC"+(rowNum+1)+")),\"-\",(S"+(rowNum+1)+"/R"+(rowNum+1)+")/($AC$12*AC"+(rowNum+1)+"))");  
				
				cell = row.createCell(22);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("V"+(rowNum+1)+"*Q"+(rowNum+1)+"");
				
				cell = row.createCell(23);
				cell.setCellType(1);
				cell.setCellStyle(style2);
//				cell.setCellFormula("");
				cell.setCellValue("");
				
				cell = row.createCell(24);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("S"+(rowNum+1)+"+V"+(rowNum+1)+"");
				
				cell = row.createCell(25);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF(ISERROR((Y"+(rowNum+1)+"/R"+(rowNum+1)+")/($AC$12*AC"+(rowNum+1)+")),\"-\",(Y"+(rowNum+1)+"/R"+(rowNum+1)+")/($AC$12*AC"+(rowNum+1)+"))");
				
				cell = row.createCell(26);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("Y"+(rowNum+1)+"/R"+(rowNum+1)+"");
				
				cell = row.createCell(27);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("Y"+(rowNum+1)+"*Q"+(rowNum+1)+"");
				
				cell = row.createCell(28);
				cell.setCellType(1);
				cell.setCellStyle(style5);
				cell.setCellValue(Double.valueOf(rs2.getString("COLOR_PRE")));
				
				cell = row.createCell(29);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF($F$3<H"+(rowNum+1)+",O"+(rowNum+1)+"*$AB$3,O"+(rowNum+1)+"*$AB$4)");
				
				cell = row.createCell(30);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF($J"+(rowNum+1)+"<(O"+(rowNum+1)+"+AD"+(rowNum+1)+"),$J"+(rowNum+1)+",O"+(rowNum+1)+"+AD"+(rowNum+1)+")");
				
				cell = row.createCell(31);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("CEILING(AJ"+(rowNum+1)+"*AE"+(rowNum+1)+",1)");
				
				cell = row.createCell(32);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("AF"+(rowNum+1)+"*Q"+(rowNum+1)+"");
				
				cell = row.createCell(33);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellFormula("IF(Y"+(rowNum+1)+"<AF"+(rowNum+1)+",\"YES\",\"NO\")");
				
				cell = row.createCell(34);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF(AND($F$3<$H"+(rowNum+1)+",$K"+(rowNum+1)+"<($O"+(rowNum+1)+"+$AD"+(rowNum+1)+")),$K"+(rowNum+1)+"+$AD"+(rowNum+1)+",IF(J"+(rowNum+1)+"<O"+(rowNum+1)+"+AD"+(rowNum+1)+"+10,J"+(rowNum+1)+",O"+(rowNum+1)+"+AD"+(rowNum+1)+"+20))");
				
				cell = row.createCell(35);
				cell.setCellType(1);
				cell.setCellStyle(style6);
				cell.setCellFormula("$AC$12*R"+(rowNum+1)+"*AC"+(rowNum+1)+"");
				
				cell = row.createCell(36);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("AI"+(rowNum+1)+"*AJ"+(rowNum+1)+"");
				
				cell = row.createCell(37);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("AK"+(rowNum+1)+"*Q"+(rowNum+1)+"");
				
				cell = row.createCell(38);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF(AH"+(rowNum+1)+"=\"YES\",IF(M"+(rowNum+1)+"=\"initial buy\",(AI"+(rowNum+1)+"*AJ"+(rowNum+1)+")-Y"+(rowNum+1)+",IF(M"+(rowNum+1)+"=\"ramp down\",J"+(rowNum+1)+"*AJ"+(rowNum+1)+"-Y"+(rowNum+1)+",(AI"+(rowNum+1)+"+O"+(rowNum+1)+")*AJ"+(rowNum+1)+"-Y"+(rowNum+1)+")),0)");
				
				cell = row.createCell(39);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("IF(AM"+(rowNum+1)+">0,AM"+(rowNum+1)+"*Q"+(rowNum+1)+",0)");
				
				cell = row.createCell(40);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("AM"+(rowNum+1));
				
				cell = row.createCell(41);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF(ISERROR(AO"+(rowNum+1)+"/AJ"+(rowNum+1)+"),\"-\",AO"+(rowNum+1)+"/AJ"+(rowNum+1)+")");
				
				cell = row.createCell(42);
				cell.setCellType(1);
				cell.setCellStyle(style4);
				cell.setCellFormula("AO"+(rowNum+1)+"*Q"+(rowNum+1)+"");
				
				cell = row.createCell(43);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellFormula("IF(ISERROR((Y"+(rowNum+1)+"+AO"+(rowNum+1)+")/AJ"+(rowNum+1)+"),\"-\",(Y"+(rowNum+1)+"+AO"+(rowNum+1)+")/AJ"+(rowNum+1)+")");
					
				cell = row.createCell(44);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				cell.setCellValue(Double.valueOf(rs2.getString("EL_SEQ")));

				rowNum++;
			}
			ps2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		row = sheet1.getRow(10);
		
		cell = row.createCell(27);
		cell.setCellType(1);
		cell.setCellStyle(style4);
		cell.setCellFormula("SUM(AB14:AB"+rowNum+")");
		
		row = sheet1.getRow(11);
		
		cell = row.createCell(39);
		cell.setCellType(1);
		cell.setCellStyle(style4);
		cell.setCellFormula("SUM(AN14:AN"+rowNum+")");
		
		cell = row.createCell(42);
		cell.setCellType(1);
		cell.setCellStyle(style4);
		cell.setCellFormula("SUM(AQ14:AQ"+rowNum+")");
		
	}
	
	private void Header1(HSSFSheet sheet1, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
			// TODO Auto-generated method stub	
			
			//邊框格式
			for(int i=1;i<7;i++){
				row = sheet1.createRow(i);
				row.setHeightInPoints(20);
				
				for(int j=1;j<6;j++){
					cell = row.createCell(j);
					cell.setCellType(1);
					cell.setCellStyle(style1);
				}
			}
			
			for(int i=7;i<13;i++){
				row = sheet1.createRow(i);
				row.setHeightInPoints(20);
				if(i<9){
					for(int j=1;j<6;j++){
						cell = row.createCell(j);
						cell.setCellType(1);
						cell.setCellStyle(style1);
					}
				}
				
				for(int j=27;j<44;j++){
					cell = row.createCell(j);
					cell.setCellType(1);
					cell.setCellStyle(style1);
				}
			}
			
			
			row = sheet1.createRow(0);
			row.setHeightInPoints(30);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Material Planning Worksheet");
			sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
			
			row = sheet1.getRow(1);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("FACTORY");

			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("XE");
			sheet1.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
			
			cell = row.createCell(3);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("LAUNCH");
			
			cell = row.createCell(4);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("DROP DATE");
			
			cell = row.createCell(5);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("TODAY'S DATE");
			
			row = sheet1.getRow(2);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("MODEL NAME");
			sheet1.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
			
			cell = row.createCell(24);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Saftety Stock Ratio: Initial buy");
			sheet1.addMergedRegion(new CellRangeAddress(2, 2, 24, 26));
			
			cell = row.createCell(25);
			cell.setCellStyle(style1);
			
			cell = row.createCell(26);
			cell.setCellStyle(style1);
			
			cell = row.createCell(27);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(0.6);
			
			row = sheet1.getRow(3);
			row.setHeightInPoints(20);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("GENDER");
			sheet1.addMergedRegion(new CellRangeAddress(3, 3, 1, 5));
			
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("M/W");
			
			cell = row.createCell(24);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Saftety Stock Ratio: Replen/Ramp down");
			sheet1.addMergedRegion(new CellRangeAddress(3, 3, 24, 26));
			
			cell = row.createCell(25);
			cell.setCellStyle(style1);
			
			cell = row.createCell(26);
			cell.setCellStyle(style1);
			
			cell = row.createCell(27);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue(0.4);
			
			row = sheet1.getRow(4);
			row.setHeightInPoints(20);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("SIZE RANGE");
			
			cell = row.createCell(1);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("6 ~ 15 / 5 ~ 12 ");
			sheet1.addMergedRegion(new CellRangeAddress(4, 4, 1, 5));
			
			row = sheet1.getRow(5);
			row.setHeightInPoints(20);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Product code");
			
			row = sheet1.getRow(6);
			row.setHeightInPoints(20);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Product code");
			
			row = sheet1.getRow(7);
			row.setHeightInPoints(20);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Product code");
			
			cell = row.createCell(27);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Total Inventory $");
			sheet1.addMergedRegion(new CellRangeAddress(7, 9 , 27, 27));
			
			cell = row.createCell(28);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Min/Max");
			sheet1.addMergedRegion(new CellRangeAddress(7, 9 , 28, 39));
			
			cell = row.createCell(40);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Factory Adjusted Buy");
			sheet1.addMergedRegion(new CellRangeAddress(7, 9 , 40, 43));		
			
			row = sheet1.getRow(8);
			row.setHeightInPoints(20);
			
			cell = row.createCell(0);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Product code");
			
			row = sheet1.getRow(10);
			row.setHeightInPoints(40);
			
			cell = row.createCell(28);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Forecast Avg UPD");
			
			cell = row.createCell(39);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Total buy qty($)");
			
			cell = row.createCell(42);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Total adjusted buy($)");
			
			row = sheet1.getRow(11);
			row.setHeightInPoints(35);
			
			for(int j=18;j<44;j++){
				cell = row.createCell(j);
				cell.setCellType(1);
				cell.setCellStyle(style1);
			}
			
			cell = row.createCell(18);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Materials in warehouse");
			sheet1.addMergedRegion(new CellRangeAddress(11, 11 , 18, 20));
			
			cell = row.createCell(21);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Materials in transit");
			sheet1.addMergedRegion(new CellRangeAddress(11, 11 , 21, 23));
			
			cell = row.createCell(24);
			cell.setCellType(1);
			cell.setCellStyle(style1);
			cell.setCellValue("Total inventory");
			sheet1.addMergedRegion(new CellRangeAddress(11, 11 , 24, 27));
			
			row = sheet1.createRow(12);
			row.setHeightInPoints(40);
			
			int cellnum=0;
			 List<String> heartname = Arrays.asList("Lgc Grp","	Material Usage","Fct Unique ID\n(FUI)","Vendor","Component Name","Material Name \n(included size breaks if any)","Color Code","Launch date","Drop date","Days to drop from Todays date","total lifespan days","Starting date for initial buy","Buying stages","UOM","MLT Days","MOQ","Unit Price","Yield",
					 "In Stock\n(UOM)","In Stock\n($)",	"In Stock\n(day)","In Transit\n(UOM)","In Transit\n($)","ETA","Current inventory\n(UOM)","Current inventory\n(day)","Current inventory\n(unit)","Current inventory\n($)","Order % by color or size","Safety Days","MIN Days","MIN\n(UOM)","MIN\n($)","Order\n(Y/N)","MAX Days","Daily Demand\n(UOM)	","MAX\n(UOM)","Max\n($)","Order Quantity\n(UOM)",
					 " Order Quantity\n(USD)","Factory Buy\n(UOM)",	"Factory buy\n(days)","Factory Buy\n(USD)","Inventory Days After Buy");
			
//			System.err.println(">>>"+heartname.size());
			for(int i=0;i<heartname.size();i++){
				
				cell = row.createCell(cellnum);
				cell.setCellType(1);
				cell.setCellStyle(style1);
				cell.setCellValue(heartname.get(i));
				cellnum++;
			}
				
		}

	private static int GetCel_no(String mODEL_NA, String string, Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int Cel_no=0;

		String 	sql="SELECT COUNT(*) COU FROM DSID04_1 WHERE MODEL_NA='"+mODEL_NA+"' AND EL_NO='"+string+"'";	
//		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Cel_no=Integer.valueOf(rs.getString("COU"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
			return Cel_no;
	}

	private void SetEl_fp(String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement  ps = null;
		ResultSet  rs = null;	
		Double para=0.0;
		
		String sql1 = "SELECT A.MODEL_UPD,B.* FROM DSID04 A,DSID04_1 B WHERE A.MODEL_NA='"+MODEL_NA+"' AND A.MODEL_NA=B.MODEL_NA";
		
		try {
			ps = conn.prepareStatement(sql1);
			rs = ps.executeQuery();			
			while(rs.next()){
				/*
				 * 判斷最小庫存下材料能使用天數與最小陳坤天數比值
				 * 不小於1.3→維持最小庫存值的數量
				 * 小於1.3 →單位庫存量*1.3
				 */
				Double XX=(Double.valueOf(rs.getString("MIN_UOM"))/Double.valueOf(rs.getString("YIELD")))
								/(Double.valueOf(rs.getString("MODEL_UPD"))*Double.valueOf(rs.getString("COLOR_PRE")))/Double.valueOf(rs.getString("MIN_DAY"));				
				if(XX>=1.3){					
					para=Math.ceil(Double.valueOf(rs.getString("MIN_UOM")));	
				}else{					
					para=Math.ceil(Double.valueOf(rs.getString("MIN_UOM"))/XX*1.3);					
				}
//				System.err.println("材料： "+rs.getString("EL_NO")+"最小庫存  "+rs.getString("MIN_UOM")+"計算結果：  "+para);
				
				String Upsql = "UPDATE DSID04_1 SET EL_FP='"+para+"' WHERE MODEL_NA='"+MODEL_NA+"' AND EL_SEQ='"+rs.getString("EL_SEQ")+"'";				
//				System.out.println(">>>更新>>>"+Upsql);				
				try {
					PreparedStatement pstm = conn.prepareStatement(Upsql);	
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {
					conn.rollback();
					e.printStackTrace();						
				}
				
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	private static Double getacc(String EL_NO, String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub	
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Double qty=0.0;

		String 	sql="SELECT SUM(PC_QTY) SPC_QTY FROM DSIDN08 WHERE PO_NO IN ('"+EL_PO.replace(",", "','")+"') AND EL_NO='"+EL_NO+"'";
	
		System.out.println(">>>>>"+sql);
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
		}
		EL_PO="";
		
		return qty;
	}

	private static Double getacc2(String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub	
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Double qty=0.0;

		String 	sql="SELECT SUM(PC_QTY) SPC_QTY FROM DSIDN08 WHERE PO_NO IN ('"+EL_PO.replace(",", "','")+"') AND EL_NO IN ("+EL_NO_LIST+")";
	
		System.out.println(">>>>>"+sql);
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
		}
		EL_PO="";
		EL_NO_LIST="";
		
		return qty;
	}
	
	private static Double getbuy(String el_no, String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Double qty=0.0;

		String 	sql="SELECT A.PO_NO,A.STOCK_MARK,EL_NO,PO_QTY,PO_ACQTY FROM DSPO05 A,DSPO06 B WHERE A.PO_NO=B.PO_NO AND A.PO_NO LIKE 'IGM%'\n" +
				"AND B.PO_CLOSE!='T' AND PO_QTY!=0 AND EL_NO='"+el_no+"' AND STOCK_MARK='"+MODEL_NA+"'";
	
		System.out.println(">>>>>"+sql);
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
		}
		if(EL_PO.length()>0){
			EL_PO=EL_PO.substring(0, EL_PO.length()-1);
		}
		
			return qty;
	}
	
	private static Double getbuy2(String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Double qty=0.0;

		String 	sql="SELECT A.PO_NO,A.STOCK_MARK,EL_NO,PO_QTY,PO_ACQTY FROM DSPO05 A,DSPO06 B WHERE A.PO_NO=B.PO_NO AND A.PO_NO LIKE 'IGM%'\n" +
				"AND B.PO_CLOSE!='T' AND PO_QTY!=0 AND EL_NO IN ("+EL_NO_LIST+") AND STOCK_MARK='"+MODEL_NA+"'";
	
		System.out.println(">>>>>"+sql);
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
		}
		if(EL_PO.length()>0){
			EL_PO=EL_PO.substring(0, EL_PO.length()-1);
		}
		
			return qty;
	}

	private static Double getkc(String el_no, String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Double qty=0.0;

		String 	sql="SELECT * FROM DSID77 WHERE EL_NO='"+el_no+"' AND MODEL_NA = '"+MODEL_NA+"'";	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				qty=Double.valueOf(rs.getString("MT_QTY"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

			return qty;
	}
	
	private static Double getkc2(String TABLE, String MODEL_NA,String COLOR, Connection conn, Connection Conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql="SELECT * FROM "+TABLE+" WHERE MODEL_NA = '"+MODEL_NA+"'";	
		if(!"DSID04_3".equals(TABLE)){
			sql+=" AND COLOR='"+COLOR+"'";
		}
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				EL_NO_LIST+="'"+rs.getString("EL_NO")+"',";
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(EL_NO_LIST.length()>0){
			EL_NO_LIST=EL_NO_LIST.substring(0, EL_NO_LIST.length()-1);
		}
		
		Double qty=0.0;

		sql="SELECT SUM(MT_QTY) MT_QTY FROM DSID77 WHERE EL_NO IN ("+EL_NO_LIST+") AND MODEL_NA = '"+MODEL_NA+"'";	
		System.out.println(">>>>>"+sql);
		try {
			ps = Conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				qty=Double.valueOf(rs.getString("MT_QTY"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qty;
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
