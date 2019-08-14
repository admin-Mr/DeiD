package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFDataFormat;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import com.ibm.icu.text.SimpleDateFormat;

import util.Common;

public class DSID21_1RTask {

	public static void ExcelExport(String MODEL_NA, String START, String END, Boolean up_date) throws SQLException {
		// TODO Auto-generated method stub
		
		String title="Color Pre Report";
		
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		
		// 字體	
		HSSFFont font1 = wb.createFont();
		font1.setFontName("新細明體");    				//设置字體  
		font1.setFontHeightInPoints((short)12);    		//设置字体高度  
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//设置字體樣式 
		
		HSSFFont font2 = wb.createFont();
		font2.setFontName("新細明體");    				//设置字體  
		font2.setFontHeightInPoints((short)10);    		//设置字体高度  
		
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
		
		//正文格式
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
		style3.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

		HSSFSheet sheet = wb.createSheet(MODEL_NA);
		
		Exportexcel1(wb,sheet,MODEL_NA,START,END, up_date,style1,style2,style3);

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

	private static void Exportexcel1(HSSFWorkbook wb, HSSFSheet sheet, String MODEL_NA, String START,
			String END,  Boolean up_date,HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3) throws SQLException {
		
		Connection conn = Common.getDbConnection();
		HSSFRow row = null;
		HSSFCell cell = null;
		
		ResultSet  rs = null;	
		PreparedStatement  ps = null;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");	
		DecimalFormat df = new DecimalFormat("#.00000");
		
		int irow = 1;
		int Total=GetNum(START,END,MODEL_NA,conn);
		System.err.println("總數"+Total);
		// 表頭設定, 大小表頭
		Header1(sheet,style1,row,cell);			

		String sql = "SELECT * FROM DSID21 WHERE MODEL_NA='"+MODEL_NA+"'";
		System.out.println(" ----- 資料查詢 : "+sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();	
			while(rs.next()){				
				row = sheet.createRow(irow);
				row.setHeightInPoints(17);
				
				cell = row.createCell(0);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("MODEL_NA"));				
				
				cell = row.createCell(1);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("GR_NO"));
				
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("GR_NA"));
				
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("COLOR"));
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("EL_NO"));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("EL_NA"));
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("SIZE_FD"));
				
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("NOTE"));
				
				cell = row.createCell(8);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("TYPE"));
				
				cell = row.createCell(9);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				Double Size_total=0.0;
				if("0".equals(rs.getString("TYPE"))){ //由GROUP、color、size_fd 查詢
					Size_total=Getpre0(START,END,MODEL_NA,rs.getString("GR_NO"),rs.getString("COLOR"),rs.getString("SIZE_FD"),conn);
				}else if("1".equals(rs.getString("TYPE"))){  //由 left_size_run color,size_fd查詢（女鞋-1.5）
					Size_total=Getpre1(START,END,MODEL_NA,rs.getString("GR_NO"),rs.getString("COLOR"),rs.getString("SIZE_FD"),conn);
				}else if("2".equals(rs.getString("TYPE"))){  //移印部分
					Size_total=Getpre2(START,END,MODEL_NA,rs.getString("GR_NO"),rs.getString("COLOR"),rs.getString("SIZE_FD"),rs.getString("NOTE"),conn);
				}else if("4".equals(rs.getString("TYPE"))){  //由size_fd 查詢 （tooling_size）
					Size_total=Getpre4(START,END,MODEL_NA,rs.getString("GR_NO"),rs.getString("SIZE_FD"),conn);
					}else if("5".equals(rs.getString("TYPE"))){  //移印合計百分比
					Size_total=Getpre5(START,END,MODEL_NA,rs.getString("GR_NO"),rs.getString("COLOR"),rs.getString("NOTE"),conn);
					}else if("6".equals(rs.getString("TYPE"))){  //非本組group判斷
					Size_total=Getpre6(START,END,MODEL_NA,rs.getString("GR_NO"),rs.getString("COLOR"),rs.getString("SIZE_FD"),rs.getString("NOTE"),conn);
					}
				cell.setCellValue(Size_total);
				
				String COLOR_PRE="";
				cell = row.createCell(10);
				cell.setCellType(1);
				cell.setCellStyle(style3);
				if("3".equals(rs.getString("TYPE"))){
					if(!"".equals(rs.getString("NOTE"))&&rs.getString("NOTE")!=null){
						cell.setCellValue(Double.valueOf(rs.getString("NOTE")));
						COLOR_PRE=df.format(Double.valueOf(rs.getString("NOTE")));
					}else{
						cell.setCellValue(0);
						COLOR_PRE="0.0";
					}
				}else{
					cell.setCellValue(Double.valueOf(Size_total/Total));
					COLOR_PRE=df.format(Size_total/Total);
					System.out.println("百分比啊"+COLOR_PRE);
					System.out.println("数量"+Size_total);
				}
				irow++;
			
				String InSql=" ";
				if(up_date==true &&!"".equals(rs.getString("TYPE"))&&rs.getString("TYPE")!=null){
					if("UPPER".equals(rs.getString("MT_USAGE"))){
						if(rs.getString("EL_NO")==null){
							System.err.println("进入汇总..................");//型体,GROUP,颜色
							InSql="UPDATE DSID04_1 SET COLOR_PRE='"+COLOR_PRE+"' WHERE MODEL_NA='"+rs.getString("MODEL_NA")+"' "
									+ "AND GROUP_NO='"+rs.getString("GR_NO")+"' AND COLOR='"+rs.getString("COLOR")+"' AND GR_NA='"+rs.getString("GR_NA")+"'";
						}else{
							System.out.println("进入普通...................");//型体,GROUP,材料编号,颜色
						InSql="UPDATE DSID04_1 SET COLOR_PRE='"+COLOR_PRE+"' WHERE MODEL_NA='"+rs.getString("MODEL_NA")+"' "
								+ "AND GROUP_NO='"+rs.getString("GR_NO")+"' AND EL_NO='"+rs.getString("EL_NO")+"' AND COLOR='"+rs.getString("COLOR")+"' AND GR_NA='"+rs.getString("GR_NA")+"'";
						}
					}else if("VAMP".equals(rs.getString("MT_USAGE"))){//型体,材料编号,颜色
						InSql="UPDATE DSID04_2 SET COLOR_PRE='"+COLOR_PRE+"' WHERE MODEL_NA='"+rs.getString("MODEL_NA")+"' AND EL_NO='"+rs.getString("EL_NO")+"' AND COLOR='"+rs.getString("COLOR")+"'";
					}else if("LABEL".equals(rs.getString("MT_USAGE"))){//型体,材料编号,颜色
						InSql="UPDATE DSID04_3 SET COLOR_PRE='"+COLOR_PRE+"' WHERE MODEL_NA='"+rs.getString("MODEL_NA")+"' AND EL_NO='"+rs.getString("EL_NO")+"' AND COLOR='"+rs.getString("COLOR")+"'";
					}else if("LACE".equals(rs.getString("MT_USAGE"))){//型体,材料编号,颜色
						InSql="UPDATE DSID04_4 SET COLOR_PRE='"+COLOR_PRE+"' WHERE MODEL_NA='"+rs.getString("MODEL_NA")+"' AND EL_NO='"+rs.getString("EL_NO")+"' AND COLOR='"+rs.getString("COLOR")+"'";
					}else if("HEEL CLIP".equals(rs.getString("MT_USAGE"))){//型体,材料编号,颜色
						InSql="UPDATE DSID04_5 SET COLOR_PRE='"+COLOR_PRE+"' WHERE MODEL_NA='"+rs.getString("MODEL_NA")+"' AND EL_NO='"+rs.getString("EL_NO")+"' AND COLOR='"+rs.getString("COLOR")+"'";
					}
					System.out.println(" ----- 匯入 : " + InSql);
					try {
						PreparedStatement pstm = conn.prepareStatement(InSql);
						pstm.executeUpdate();
						pstm.close();
						conn.commit();
					} catch (Exception e) {
						e.printStackTrace();
						conn.rollback();
						return;
					}
					
				}
			}
			
			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0170"));
		}
	}


	private static Double Getpre4(String sTART, String eND, String mODEL_NA, String GROUP_NO, String SIZE_FD,
			Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String min_size="",max_size="";
		if(!"".equals(SIZE_FD)){
			min_size=SIZE_FD.substring(0,SIZE_FD.indexOf("-"));
			max_size=SIZE_FD.substring(SIZE_FD.indexOf("-")+1,SIZE_FD.length()-1);
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
			
		Double Size_Total=0.0;

		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"' AND TO_NUMBER(TOOLING_SIZE) BETWEEN "+min_size+" AND "+max_size+" ";	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Size_Total=Double.valueOf(rs.getString("COU"));
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
		
		return Size_Total;
	}

	private static int GetNum(String sTART, String eND, String mODEL_NA, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Total=0;
		
		String 	sql="SELECT COUNT(*) total FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"'";
	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Total=Integer.valueOf(rs.getString("total"));
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
		
		return Total;
	}

	private static Double Getpre0(String sTART, String eND, String mODEL_NA,String GROUP_NO, String COLOR, String SIZE_FD, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String min_size="",max_size="";
		if(!"".equals(SIZE_FD)){
			min_size=SIZE_FD.substring(0,SIZE_FD.indexOf("-"));
			max_size=SIZE_FD.substring(SIZE_FD.indexOf("-")+1,SIZE_FD.length()-1);
			System.err.println("size "+min_size+" - "+max_size);
		}
		if(COLOR.contains("/")){
			COLOR=COLOR.substring(0,3);
			System.out.println("进入此处：："+COLOR);
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Double Size_Total=0.0;
		
		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"' AND "+GROUP_NO+" LIKE '"+COLOR+"%' AND TO_NUMBER(TOOLING_SIZE) BETWEEN "+min_size+" AND "+max_size+" ";	
		System.out.println("型體："+mODEL_NA+"   COLOR:"+COLOR+"   最小碼："+min_size+"   最大嗎："+max_size+"    GROUP_NO:"+GROUP_NO);
		System.out.println(">>>>>薩達"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Size_Total=Double.valueOf(rs.getString("COU"));
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
		
		return Size_Total;
	}

	private static Double Getpre1(String sTART, String eND, String mODEL_NA,String GROUP_NO, String COLOR, String SIZE_FD, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String min_size="",max_size="";
		if(!"".equals(SIZE_FD)){
			min_size=SIZE_FD.substring(0,SIZE_FD.indexOf("-"));
			max_size=SIZE_FD.substring(SIZE_FD.indexOf("-")+1,SIZE_FD.length()-1);
			System.err.println("size "+min_size+" - "+max_size);
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Double Size_Total=0.0;
		if(COLOR.contains("/")){
			COLOR=COLOR.substring(0, 3);
		}
		
		String 	sql=" SELECT COUNT(*) COU FROM ( SELECT CASE INSTR(MODEL_NA,'W ') WHEN 1 THEN TO_NUMBER(LEFT_SIZE_RUN)-1.5 ELSE TO_NUMBER(LEFT_SIZE_RUN) END LEFT_SIZE  FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"' AND "+GROUP_NO+" LIKE '"+COLOR+"' ) WHERE TO_NUMBER(LEFT_SIZE) BETWEEN "+min_size+" AND "+max_size+"";	

			System.out.println("----------"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			if(rs.next()){
				Size_Total=Double.valueOf(rs.getString("COU"));
				System.out.println("数量1："+Size_Total);
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
		
		return Size_Total;
	}
	
	private static Double Getpre2(String sTART, String eND, String mODEL_NA,String GROUP_NO, String COLOR, String SIZE_FD, String Note, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String min_size="",max_size="";
		if(!"".equals(SIZE_FD)){
			min_size=SIZE_FD.substring(0,SIZE_FD.indexOf("-"));
			max_size=SIZE_FD.substring(SIZE_FD.indexOf("-")+1,SIZE_FD.length()-1);
			System.err.println("size "+min_size+" - "+max_size);
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
			
		Double Size_Total=0.0;

		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"' AND "+GROUP_NO+" NOT IN('"+Note.replace("!", "").replace("','", ",")+"') AND TO_NUMBER(TOOLING_SIZE) BETWEEN "+min_size+" AND "+max_size+" ";	
		System.out.println(">>>>>我在這裡"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Size_Total=Double.valueOf(rs.getString("COU"));
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
		
		return Size_Total;
	}
	
	private static Double Getpre6(String sTART, String eND, String mODEL_NA,String GROUP_NO, String COLOR, String SIZE_FD, String Note, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String min_size="",max_size="";
		if(!"".equals(SIZE_FD)){
			min_size=SIZE_FD.substring(0,SIZE_FD.indexOf("-"));
			max_size=SIZE_FD.substring(SIZE_FD.indexOf("-")+1,SIZE_FD.length()-1);
//			System.err.println("size "+min_size+" - "+max_size);
		}
		String Excon1="",Excon2="";
		if(!"".equals(Note)){
			Excon1=Note.substring(0,Note.indexOf("/"));
			Excon2=Note.substring(Note.indexOf("/")+1);
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
			
		Double Size_Total=0.0;

		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"' AND "+GROUP_NO+" LIKE '"+Excon1+"' AND "+Excon2+" LIKE '"+COLOR+"'  AND TO_NUMBER(TOOLING_SIZE) BETWEEN "+min_size+" AND "+max_size+" ";	
		System.out.println(">>>>>是我啊"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Size_Total=Double.valueOf(rs.getString("COU"));
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
		
		return Size_Total;
	}
	
	private static Double Getpre5(String sTART, String eND, String mODEL_NA,String GROUP_NO, String COLOR, String Note, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String exSql="";
		if(!"".equals(COLOR)){
			COLOR=COLOR.substring(0,COLOR.indexOf("/"));
		}
		if(!"".equals(Note)&&Note!=null){
			if(Note.contains("!")){
				exSql=" NOT ";
				COLOR=Note.replace("!", "").replace("','", ",");
			}
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
			
		Double Size_Total=0.0;

		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD') BETWEEN '"+sTART+"' AND '"+eND+"' AND MODEL_NA LIKE '%"+mODEL_NA+"' AND "+GROUP_NO+" "+exSql+" IN ('"+COLOR+"')";	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				Size_Total=Double.valueOf(rs.getString("COU"));
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
		
		return Size_Total;
	}
	
	private static void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 10* 256);
		sheet.setColumnWidth(2, 35 * 256);
		sheet.setColumnWidth(3, 15 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 20 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		sheet.setColumnWidth(9, 15 * 256);
		
		row = sheet.createRow(0);
		row.setHeightInPoints(25);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID01M.MODEL_NA"));	
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0171"));
		
		
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
		
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0096"));
		
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID04_1.EL_NO"));
		
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID03.EL_NAME"));
		
		
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Size "+Labels.getLabel("DSID.MSG0116"));
		
		
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("PUBLIC.MSG0040"));
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID21.TYPE"));
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Num");
		
		cell = row.createCell(10);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Color Pre");
		
	}
	
}
