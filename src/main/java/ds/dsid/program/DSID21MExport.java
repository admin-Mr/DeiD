package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.SimpleDateFormat;

import ds.common.services.CRUDService;

import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID21MExport extends OpenWinCRUD{

	@Wire private Window windowMaster;
	@Wire private CRUDService CRUDService;
	@Wire private Button btnexport;
	@Wire private Listbox List_Group_na, List_Model_na ;
	
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
//		doSearch();
		
		Connection conn = Common.getDbConnection();
		ResultSet  rs = null;
		PreparedStatement  ps = null;
		List<String> Grnolist = new ArrayList<String>();
		Grnolist.add("");
		
		String sql = "SELECT DISTINCT MODEL_NA FROM DSID04 WHERE IS_DROP!='Y' ORDER BY MODEL_NA";
		System.out.println(" ----- 測試獲取  Grno : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Grnolist.add(rs.getString("MODEL_NA"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0133"));
		} finally {	
			Common.closeConnection(conn);	
		}

		List_Model_na.setModel(new ListModelList<Object>(Grnolist));
		
	}
	
	@Listen("onClick = #btnexport")
	public void onClickbtnexport(){
		
		HSSFWorkbook wb = new HSSFWorkbook();
		Connection conn = Common.getDbConnection();
		
		// 抓取 Listbox 文本框內選定的值 .
		String Modelna = "";

		for(Listitem ltAll : List_Model_na.getItems()){
			if(ltAll.isSelected()){
				Modelna = ltAll.getValue().toString();
			}
		}
		
		try { // 部位名稱與型體名稱不能為空
			if( Modelna != null || "".equals(Modelna)){
				
				filterHeader(wb, Modelna, conn); // 導出主體方法
				
				Messagebox.show(Labels.getLabel("DSID.MSG0135"));
			
			}else{
				Messagebox.show(Labels.getLabel("DSID.MSG0134"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0136"));
		}
		
		windowMaster.detach();
	}
	
	
	private void filterHeader(HSSFWorkbook wb, String Modelna, Connection conn) {
		// TODO Auto-generated method stub
		
		HSSFSheet sheet = wb.createSheet();
		ByteArrayOutputStream  stream = new ByteArrayOutputStream();
		
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
		try {
			
			SetSheet1(wb, sheet, conn, style1, style2, Modelna);
	        
			wb.write(stream);
	        Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
			
			byte[] content = stream.toByteArray();
		    InputStream is = new ByteArrayInputStream(content);

		    //儲存位置、名稱
			Filedownload.save(is, "application/xls", Modelna);
			is.close();
			stream.flush();
			stream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Common.closeConnection(conn);
			System.err.println(" —————————— : 結束  : —————————— ");
		}
	}

	private void SetSheet1(HSSFWorkbook wb, HSSFSheet sheet, Connection conn, HSSFCellStyle style1,
			HSSFCellStyle style2, String Modelna) {
		// TODO Auto-generated method stub
		
		HSSFRow row = null;
		HSSFCell cell = null;
		
		ResultSet  rs = null;	
		PreparedStatement  ps = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");	
		
		int irow = 1;

		// 表頭設定, 大小表頭
		Header1(sheet,style1,row,cell);			

		String sql = "SELECT * FROM (\n" +
						"SELECT MODEL_NA,'UPPER' MT_USAGE,GROUP_NO,GR_NA,COLOR,EL_NO,EL_NA FROM DSID04_1\n" + 
						"UNION ALL\n" + 
						"SELECT MODEL_NA,'VAMP' MT_USAGE,'GROUP1' GROUP_NO,'' GR_NA,COLOR,EL_NO,EL_NA FROM DSID04_2\n" + 
						"UNION ALL\n" + 
						"SELECT MODEL_NA,'LABEL' MT_USAGE,'' GROUP_NO,'' GR_NA,COLOR,EL_NO,EL_NA FROM DSID04_3\n" + 
						"UNION ALL\n" + 
						"SELECT MODEL_NA,'LACE' MT_USAGE,'' GROUP_NO,'' GR_NA,COLOR,EL_NO,EL_NA FROM DSID04_4\n" + 
						"UNION ALL\n" +
						"SELECT MODEL_NA,'HEEL CLIP' MT_USAGE,'' GROUP_NO,'' GR_NA,COLOR,EL_NO,EL_NA FROM DSID04_5"+
						") WHERE MODEL_NA='"+Modelna+"'";
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
				cell.setCellValue(rs.getString("MT_USAGE"));
				
				cell = row.createCell(2);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("GROUP_NO"));
				
				cell = row.createCell(3);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("GR_NA"));
				
				cell = row.createCell(4);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("COLOR"));
				
				cell = row.createCell(5);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("EL_NO"));
				
				cell = row.createCell(6);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue(rs.getString("EL_NA"));
				
				cell = row.createCell(7);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				
				cell = row.createCell(8);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				cell.setCellValue("");
				
				cell = row.createCell(9);
				cell.setCellType(1);
				cell.setCellStyle(style2);
				if("LACE".equals(rs.getString("MT_USAGE"))){
					cell.setCellValue("1");
				}else{
					cell.setCellValue("");
				}
				
				irow++;
			}
			
			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Messagebox.show(Labels.getLabel("DSID.MSG0170"));
		}
	}

	/**
	 * 表頭打印
	 */
	private void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell) {
		// TODO Auto-generated method stub
		
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 10* 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 35 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 18 * 256);
		sheet.setColumnWidth(6, 35 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		sheet.setColumnWidth(8, 20 * 256);
		
		row = sheet.createRow(0);
		row.setHeightInPoints(25);
		
		cell = row.createCell(0);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID01M.MODEL_NA"));
		
		
		cell = row.createCell(1);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID04_1.MT_USAGE"));
		
		
		cell = row.createCell(2);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0171"));
		
		
		cell = row.createCell(3);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0099"));
		
		
		cell = row.createCell(4);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID.MSG0096"));
		
		
		cell = row.createCell(5);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID04_1.EL_NO"));
		
		
		cell = row.createCell(6);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("DSID03.EL_NAME"));
		
		
		cell = row.createCell(7);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("Size "+Labels.getLabel("DSID.MSG0116"));
		
		
		cell = row.createCell(8);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue(Labels.getLabel("PUBLIC.MSG0040"));
		
		cell = row.createCell(9);
		cell.setCellType(1);
		cell.setCellStyle(style1);
		cell.setCellValue("type");
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
