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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.hibernate.loader.custom.Return;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import ds.common.services.UserCredential;
import util.Common;

public class DSID25_1RTask {

	protected final static UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");

	public static void ExcelExport(String START, String MODEL_NA) throws SQLException {
		// TODO Auto-generated method stub
		String title=Labels.getLabel("DSID.MSG0216");
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
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中    
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style3.setFillPattern((short) 0);
		style3.setWrapText(true);
		style3.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		
		String ExSql="";		
		if(!"".equals(MODEL_NA)){
			ExSql=" AND MODEL_NA LIKE '%"+MODEL_NA+"'";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		String Dates="",ErrMess="";
		
		String 	sql="SELECT DISTINCT MODEL_NA ,COUNT(*) COU FROM ( SELECT CASE WHEN MODEL_NA LIKE 'W%' THEN SUBSTR(MODEL_NA,3) ELSE MODEL_NA END AS MODEL_NA FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"' "+ExSql+" )  GROUP BY MODEL_NA";
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				Dates=CheckDate(START,rs.getString("MODEL_NA"),conn);
				if("".equals(Dates)){
					HSSFSheet sheet = wb.createSheet(rs.getString("MODEL_NA"));
					SetColumnWidth(sheet);	
					
					Exportexcel1(wb,sheet,rs.getString("MODEL_NA"),START,rs.getString("COU"),conn,style1,style2,style3);		
				}else{
					ErrMess=ErrMess+Labels.getLabel("DSID01M.MODEL_NA")+"："+rs.getString("MODEL_NA")+" "+Labels.getLabel("DSID.MSG0217")+" "+Dates+" "+Labels.getLabel("DSID.MSG0218")+"！！！\n ";
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
			Common.closeConnection(conn);
		}

		
		if(!"".equals(ErrMess)){
			Messagebox.show(ErrMess);
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
		}
		
		
	}
	
	
	private static String CheckDate(String START, String MODEL_NA, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		String Dates="";
		String sql1 = "SELECT DISTINCT ORDER_DATE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')>'2019/01/01'\n" +
				"AND ORDER_DATE NOT IN(SELECT DISTINCT RE_DATE FROM DSID25_LOG WHERE MODEL_NA ='"+MODEL_NA+"')\n" + 
				"AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')<'"+START+"' AND MODEL_NA ='"+MODEL_NA+"' ORDER BY ORDER_DATE";
		System.out.println("Check>>>"+sql1);	
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			while(rs1.next()){
					Dates=Dates+rs1.getDate("ORDER_DATE")+",";
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(rs1!=null){
					rs1.close();
				}
				if(ps1!=null){
					ps1.close();
				}
			}

		if(!"".equals(Dates)){
			Dates=Dates.substring(0, Dates.length()-1);
		}
		return Dates;
	}


	private static void SetColumnWidth(HSSFSheet sheet1) {
		// TODO Auto-generated method stub
		sheet1.setColumnWidth(0, 15*256);
		sheet1.setColumnWidth(1, 40*256);
		sheet1.setColumnWidth(2, 15*256);
		sheet1.setColumnWidth(3, 15*256);
		sheet1.setColumnWidth(4, 10*256);
		sheet1.setColumnWidth(5, 10*256);
		sheet1.setColumnWidth(6, 10*256);
		sheet1.setColumnWidth(7, 10*256);
		sheet1.setColumnWidth(8, 10*256);
		sheet1.setColumnWidth(9, 10*256);
		sheet1.setColumnWidth(10, 10*256);
	}


	private static void Exportexcel1(HSSFWorkbook wb, HSSFSheet sheet1, String MODEL_NA, String START, String COU,  Connection conn, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3) throws SQLException {
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

		Boolean exist=CheckData(START,conn);
		
		String sql2="SELECT B.GROUP_NO,A.EL_NO,A.EL_NA,B.COLOR,SUM(B.YIELD) YIELD,A.BA_MU FROM DSID25 A,DSID04_1 B\n" +
						"        WHERE A.MODEL_NA=B.MODEL_NA AND A.EL_NO=B.EL_NO AND B.GROUP_NO LIKE 'GROUP%' AND A.MODEL_NA ='"+MODEL_NA+"'\n" + 
						"        GROUP BY B.GROUP_NO,A.EL_NO,A.EL_NA,B.COLOR,A.BA_MU";

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
					cell.setCellValue(rs2.getString("EL_NO"));
					
					cell = row.createCell(1);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(rs2.getString("EL_NA"));
					
					cell = row.createCell(2);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					int Od_noNum=GetOd_noNum(START,MODEL_NA,rs2.getString("GROUP_NO"),rs2.getString("COLOR"),conn);
					cell.setCellValue(Od_noNum);
						
					cell = row.createCell(3);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(Double.valueOf(rs2.getString("YIELD")));
					
					cell = row.createCell(4);
					cell.setCellType(1);
					cell.setCellStyle(style3);
					cell.setCellFormula("C"+(rowNum+1)+"*D"+(rowNum+1));
					
					cell = row.createCell(5);
					cell.setCellType(1);
					cell.setCellStyle(style1);

					
					//獲取歷史剩餘料 
					String Old_Remain=FindLastRemain(MODEL_NA,rs2.getString("GROUP_NO"),rs2.getString("EL_NO"),START,conn);
					System.err.println(rs2.getString("EL_NO")+" 歷史剩餘料獲取>>>>"+Old_Remain);

//					if(!"".equals(rs2.getString("REMAIN"))&&rs2.getString("REMAIN")!=null){
//						REMAIN=Double.valueOf(rs2.getString("REMAIN"));
//					}else{
//						REMAIN=0.0;
//					}
					cell.setCellValue(Double.valueOf(Old_Remain));
								
					cell = row.createCell(6);
					cell.setCellType(1);
					cell.setCellStyle(style2);
					cell.setCellFormula("F"+(rowNum+1)+"/D"+(rowNum+1));
										
					cell = row.createCell(7);
					cell.setCellType(1);
					cell.setCellStyle(style3);
					cell.setCellFormula("IF(E"+(rowNum+1)+">=F"+(rowNum+1)+",E"+(rowNum+1)+"-F"+(rowNum+1)+",0)");
				
					cell = row.createCell(8);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					cell.setCellValue(Double.valueOf(rs2.getString("BA_MU")));
					
					cell = row.createCell(9);
					cell.setCellType(1);
					cell.setCellStyle(style1);
					//實際要領料
					Double ACT_LL=0.0;
					if(Double.valueOf(Old_Remain)>Od_noNum*Double.valueOf(rs2.getString("YIELD"))){
						ACT_LL=0.0;
					}else{
						ACT_LL=Math.ceil((Od_noNum*Double.valueOf(rs2.getString("YIELD"))-Double.valueOf(Old_Remain))/Double.valueOf(rs2.getString("BA_MU")))*Double.valueOf(rs2.getString("BA_MU"));				
					}
					cell.setCellValue(ACT_LL);
					
					//剩餘料
					cell = row.createCell(10);
					cell.setCellType(1);
					cell.setCellStyle(style3);
					cell.setCellFormula("IF(E"+(rowNum+1)+">=F"+(rowNum+1)+",J"+(rowNum+1)+"-H"+(rowNum+1)+",F"+(rowNum+1)+"-E"+(rowNum+1)+")");
					
					
					if(exist==false){
//						System.err.println("歷史資料開始更新");
						String NREMAIN="";
						DecimalFormat df   = new DecimalFormat("#0.00"); 
						if(Double.valueOf(Old_Remain)>Od_noNum*Double.valueOf(rs2.getString("YIELD"))){
							NREMAIN=df.format(Double.valueOf(Old_Remain)-Od_noNum*Double.valueOf(rs2.getString("YIELD")));
						}else{
							NREMAIN=df.format(ACT_LL-(Od_noNum*Double.valueOf(rs2.getString("YIELD"))-Double.valueOf(Old_Remain)));							
						}					
//						System.err.println(rs2.getString("EL_NO")+" 新  剩餘料獲取>>>>"+NREMAIN);
						
						//更新剩餘料資料
						UpdateRemain(MODEL_NA,rs2.getString("EL_NO"),START,conn);
						//更新剩餘料歷史資料
						UpdateRemainLog(MODEL_NA,rs2.getString("GROUP_NO"),rs2.getString("EL_NO"),NREMAIN,START,rs2.getString("BA_MU"),conn);
					}else{	
						//歷史領料倍數
						cell = row.createCell(8);
						cell.setCellType(1);
						cell.setCellStyle(style1);
						String HisBamu=FindHisBamu(MODEL_NA,rs2.getString("GROUP_NO"),rs2.getString("EL_NO"),START,conn);
						
						cell.setCellValue(Double.valueOf(HisBamu));
						
//						System.err.println("型體當天資料已更新！！！");
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
	

	private static String FindHisBamu(String MODEL_NA, String GROUP_NO, String EL_NO, String START, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		String HisBamn="";
		String sql1 = "SELECT * FROM DSID25_LOG WHERE MODEL_NA='"+MODEL_NA+"' AND GROUP_NO='"+GROUP_NO+"' AND EL_NO='"+EL_NO+"' AND TO_CHAR(RE_DATE,'YYYY/MM/DD')='"+START+"'";				
		System.out.println("Last_remain>>>"+sql1);	
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
				HisBamn=rs1.getString("BA_MU");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(rs1!=null){
					rs1.close();
				}
				if(ps1!=null){
					ps1.close();
				}
			}

				
		return HisBamn;
	}


	private static boolean CheckData(String START, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		Boolean exist=false;
		String sql1 = "SELECT * FROM DSID25_LOG WHERE TO_CHAR(RE_DATE,'YYYY/MM/DD')='"+START+"'";	
		System.out.println("Check>>>"+sql1);	
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
					exist=true;
				}else{
					exist=false;
				}
			}catch (Exception e) {
				exist=false;
				e.printStackTrace();
			}finally{
				if(rs1!=null){
					rs1.close();
				}
				if(ps1!=null){
					ps1.close();
				}
			}

		
		return exist;
	}


	private static String FindLastRemain(String MODEL_NA, String GROUP_NO, String EL_NO,String START, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement  ps1 = null;
		ResultSet  rs1 = null;
		String Old_Remain="";
		String sql1 = "SELECT * FROM DSID25_LOG WHERE MODEL_NA='"+MODEL_NA+"' AND GROUP_NO='"+GROUP_NO+"' AND EL_NO='"+EL_NO+"' "
				+ "AND TO_CHAR(RE_DATE,'YYYY/MM/DD')<'"+START+"' ORDER BY RE_DATE DESC";				
		System.out.println("Last_remain>>>"+sql1);	
		try {
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();			
			if(rs1.next()){
					Old_Remain=rs1.getString("REMAIN");
				}else{
					Old_Remain="0.0";
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(rs1!=null){
					rs1.close();
				}
				if(ps1!=null){
					ps1.close();
				}
			}

				
		return Old_Remain;
	}


	private static void UpdateRemain(String MODEL_NA, String EL_NO, String START, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstm=null;
		String 	sql="UPDATE DSID25 SET RE_DATE=TO_DATE('"+START+"','YYYY/MM/DD') WHERE MODEL_NA='"+MODEL_NA+"' AND EL_NO='"+EL_NO+"'";	
		System.out.println("updsid25>>>"+sql);
		try {
			pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();
			pstm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstm!=null){
				pstm.close();
			}
		}

	}

	private static void UpdateRemainLog(String MODEL_NA, String GROUP_NO, String EL_NO,String REMAIN,String START,String BA_MU, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstm=null;
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		String 	sql2="INSERT INTO DSID25_LOG (MODEL_NA,GROUP_NO,EL_NO,REMAIN,RE_DATE,BA_MU,UP_USER,UP_DATE) "
				+ "VALUES ('"+MODEL_NA+"','"+GROUP_NO+"','"+EL_NO+"','"+REMAIN+"',TO_DATE('"+START+"','YYYY/MM/DD'),'"+BA_MU+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";	
		System.out.println("updsid25_log>>>"+sql2);		
		try {
			pstm = conn.prepareStatement(sql2);
			pstm.executeUpdate();
			pstm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstm!=null){
				pstm.close();
			}
		}
	}


	private static int GetOd_noNum(String START, String MODEL_NA, String GROUP,  String COLOR, Connection conn) throws SQLException {
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


		return Integer.valueOf(num);
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
		cell.setCellValue(Labels.getLabel("DSID01M.ORDER_DATE"));	
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0219")+"：");
		
		row = sheet.createRow(1);
		row.setHeightInPoints(30);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID25R.EL_NO"));		
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID25R.EL_NA"));		
		
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0220"));					
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0221"));	
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0222"));	
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0223"));
		
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0224"));		
			
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0225"));		
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID25R.MULTIPLE"));	
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0226"));
		
		cell = row.createCell(10);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0227"));
	
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
////	    	System.err.println(">>>鏈接DB01數據庫");
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
//	    return conn;
//	}

}
