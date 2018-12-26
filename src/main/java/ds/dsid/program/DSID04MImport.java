package ds.dsid.program;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID04MImport extends OpenWinCRUD{

	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Fileupload btnImport2;	
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txtMODEL_NA;
	String MODEL_NA="";
	String Errmessage="";
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");		
		
		btnImport2 = (Fileupload) window.getFellow("btnImport2");
		btnImport2.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>() {
			@SuppressWarnings("unused")
			public void onEvent(UploadEvent event) throws Exception {
				String fileToRead = "";
				org.zkoss.util.media.Media media = event.getMedia();
				if (!media.getName().toLowerCase().endsWith(".xls")) {
					//"格式有誤！"
					Messagebox.show(Labels.getLabel("COMM.XLSFILE"));
					return;
				}
				System.out.println("-------- fileToRead : " + fileToRead);
				InputStream input = null;
				media.isBinary();
				String sss = media.getFormat();
				input = media.getStreamData();// 獲得輸入流
				importFromExcel(input);
				
			}
		});
		
	}

	@SuppressWarnings("resource")
	public void importFromExcel(InputStream input) throws Exception {
		System.out.println("进入excel 读取内容");
		Connection conn = Common.getDbConnection();
		HSSFWorkbook wb = new HSSFWorkbook(input);
		//型體
		MODEL_NA=txtMODEL_NA.getValue();
		
		ImportSheet0(wb,conn);//主檔
		ImportSheet1(wb,conn);// VAMP 鞋面
		ImportSheet2(wb,conn);//label 標籤
		ImportSheet3(wb,conn);//lace 鞋帶
//		ImportSheet4(wb,conn);//Sockliner鞋墊
		 
		ShowMessage();

		Common.closeConnection(conn);	
	}
		
	private void ImportSheet3(HSSFWorkbook wb, Connection conn) {
		// TODO Auto-generated method stub
		System.out.println(">>>讀取工作表4>>>>>>>>>>>>>>");
		int Start_Row = 0;
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		HSSFFormulaEvaluator Formul= new HSSFFormulaEvaluator(wb);		
		HSSFSheet sheet =wb.getSheetAt(3);	
		
		HSSFRow row=null;
		int SEQ=0;
		String LAST_COLOR="";

		try {
			conn.setAutoCommit(false);
			
			for(int x=0;x<sheet.getPhysicalNumberOfRows();x++){
				System.out.println(">>>行数"+x);
				row = sheet.getRow(x);
				
				String Str1=getCellValue(row.getCell(0));
				
				if(Str1.contains("Length")){
					Start_Row=x;
					System.err.println(">>>開始行数"+Start_Row);
					for(int j=2;j < 22 ;j++){
						SEQ++;
						
						row = sheet.getRow(Start_Row-1);			
						String COLOR = getCellValue(row.getCell(j));
						
						if(!"".equals(COLOR)){
							LAST_COLOR=COLOR;
						}else{
							COLOR=LAST_COLOR;
						}
						System.err.println(">>>顏色"+COLOR);
						row = sheet.getRow(Start_Row);
						String EL_NA = getCellValue(row.getCell(j)).replace("'", " ");
						
						row = sheet.getRow(Start_Row+1);
						String EL_NO = getCellValue(row.getCell(j));
						if(EL_NO.length()>12){
							EL_NO=EL_NO.substring(EL_NO.length()-12);
						}
						
						row = sheet.getRow(Start_Row+4);
						String COLOR_PRE = getCellValue2(Formul.evaluateInCell(row.getCell(j)));
						
						row = sheet.getRow(Start_Row+11);
						String MIN_DAY = getCellValue(Formul.evaluateInCell(row.getCell(j)));
							
						row = sheet.getRow(Start_Row+13);
						String INI_BUY = getCellValue(Formul.evaluateInCell(row.getCell(j)));
						
						String Str="";
						for(int k=0;k<4-String.valueOf(SEQ).length();k++){
							Str+="0";
						}
						String EL_SEQ=Str+String.valueOf(SEQ);
						
						if( !"".equals(EL_NO)&&EL_NO != null){
				
						String sql2 ="INSERT INTO DSID04_4 (MODEL_NA,EL_SEQ,EL_NO,COLOR,EL_NA,COLOR_PRE,MIN_DAY,INI_BUY,UP_USER,UP_DATE) VALUES ('"+MODEL_NA+"','"+EL_SEQ+"','"+EL_NO+"','"+COLOR+"','"+EL_NA+"','"+COLOR_PRE+"','"+MIN_DAY+"','"+INI_BUY+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";


						System.out.println(">>>導入>>>"+sql2);				
							
						try {
							PreparedStatement pstm = conn.prepareStatement(sql2);
							pstm.executeUpdate();
							pstm.close();
						} catch (Exception e) {
							Errmessage ="工作表4材料"+EL_NO+"資料導入失敗！"+e;
							conn.rollback();
							e.printStackTrace();
							
						}
					}
			
			if(Errmessage.length()<=0){
				conn.commit();
			}else{
				conn.rollback();
			}				
			}}}
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void ImportSheet2(HSSFWorkbook wb, Connection conn) {
		// TODO Auto-generated method stub
		System.out.println(">>>讀取工作表3>>>>>>>>>>>>>>");
		int Start_Row=0;
		String ET_US="";
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		HSSFFormulaEvaluator Formul= new HSSFFormulaEvaluator(wb);		
		HSSFSheet sheet =wb.getSheetAt(2);	
		
		HSSFRow row=null;

		try {
			conn.setAutoCommit(false);
			
			for(int x=0;x<10;x++){
				System.out.println(">>>行数"+x);
				row = sheet.getRow(x);
				
				String Str1=getCellValue(row.getCell(0));
				if(Str1.contains("顏色")){
					Start_Row=x+1;
				}
				
				String Str2=getCellValue(row.getCell(6));
				if(Str2.contains("estimated usage")){
					ET_US=Str2.replace("預計用量 (estimated usage ", "").replace("預計用量 (estimated usage", "").replace(")", "");
				}
			}	
					
			System.out.println(">>>明細開始行"+(Start_Row+1));
			
			//材料明細	導入	
			int SEQ=0;
			String LAST_COLOR="";
			for (int i = Start_Row; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				SEQ++;	
				String COLOR = getCellValue(row.getCell(0));
				if(!"".equals(COLOR)){
					LAST_COLOR=COLOR;
				}else{
					COLOR=LAST_COLOR;
				}
				String EL_NO = getCellValue(row.getCell(1));
				if(EL_NO.length()>12){
					EL_NO=EL_NO.substring(EL_NO.length()-12);
				}				
				String EL_NA = getCellValue(row.getCell(2)).replace("'", " ");
				String COLOR_PRE = getCellValue2(Formul.evaluateInCell(row.getCell(4)));				
				String YIELD = getCellValue(Formul.evaluateInCell(row.getCell(5)));
				String INI_BUY = getCellValue(Formul.evaluateInCell(row.getCell(10)));
						
				if(EL_NO == null|| "".equals(EL_NO)){
					break;
				}
				
				String Str="";
				for(int j=0;j<4-String.valueOf(SEQ).length();j++){
					Str+="0";
				}
				String EL_SEQ=Str+String.valueOf(SEQ);
				
				String sql2 ="INSERT INTO DSID04_3 (MODEL_NA,EL_SEQ,COLOR,EL_NO,EL_NA,YIELD,COLOR_PRE,INI_BUY,ET_US,UP_USER,UP_DATE) VALUES ('"+MODEL_NA+"','"+EL_SEQ+"','"+COLOR+"','"+EL_NO+"','"+EL_NA+"','"+YIELD+"','"+COLOR_PRE+"','"+INI_BUY+"','"+ET_US+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";


				System.out.println(">>>導入>>>"+sql2);				
					
				try {
					PreparedStatement pstm = conn.prepareStatement(sql2);
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {
					Errmessage ="工作表3材料"+EL_NO+"資料導入失敗！"+e;
					conn.rollback();
					e.printStackTrace();
					
				}
						
				}		
			
			if(Errmessage.length()<=0){
				conn.commit();
			}else{
				conn.rollback();
			}				
				
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void ImportSheet1(HSSFWorkbook wb, Connection conn) {
		// TODO Auto-generated method stub
		System.out.println(">>>讀取工作表2>>>>>>>>>>>>>>");
		int Start_Row=0;
		String ET_US="";
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		HSSFFormulaEvaluator Formul= new HSSFFormulaEvaluator(wb);		
		HSSFSheet sheet =wb.getSheetAt(1);	
		
		HSSFRow row=null;
		String VAMP_UPD="";

		try {
			conn.setAutoCommit(false);
			
			row = sheet.getRow(0);
			VAMP_UPD=getCellValue(Formul.evaluateInCell(row.getCell(4)));	
			
			for(int x=0;x<10;x++){
				System.out.println(">>>行数"+x);
				row = sheet.getRow(x);
				
				String Str1=getCellValue(row.getCell(0));
				if(Str1.contains("color")){
					Start_Row=x+1;
				}
				
				String Str2=getCellValue(row.getCell(7));
				if(Str2.contains("estimated usage")){
					ET_US=Str2.replace("預計用量 (estimated usage ", "").replace("預計用量 (estimated usage", "").replace(")", "");
				}
				
			}	

			String sql1 = "UPDATE DSID04 SET VAMP_UPD='"+VAMP_UPD+"' WHERE MODEL_NA='"+MODEL_NA+"'";

			System.out.println(">>>導入>>>"+sql1);
			
			try {
				PreparedStatement pstm = conn.prepareStatement(sql1);	
				pstm.executeUpdate();
				pstm.close();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();						
			}
					
			System.out.println(">>>明細開始行"+(Start_Row+1));
			
			//材料明細	導入	
			int SEQ=0;
			String LAST_COLOR="";
			for (int i = Start_Row; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				System.out.println(">>>行数"+(i+1));
				SEQ++;	
				String EL_NO = getCellValue(row.getCell(1));
				if(EL_NO.length()>12){
					EL_NO=EL_NO.substring(EL_NO.length()-12);
				}
				if(EL_NO == null|| "".equals(EL_NO)){
					break;
				}
				String COLOR = getCellValue(row.getCell(0));
				if(!"".equals(COLOR)){
					LAST_COLOR=COLOR;
				}else{
					COLOR=LAST_COLOR;
				}
				
				String EL_NA = getCellValue(row.getCell(2)).replace("'", " ");
				String COLOR_PRE = getCellValue2(Formul.evaluateInCell(row.getCell(4)));
				String DAILY_UOM = getCellValue(Formul.evaluateInCell(row.getCell(5)));				
				String YIELD = getCellValue2(Formul.evaluateInCell(row.getCell(6)));
				String INI_BUY = getCellValue(Formul.evaluateInCell(row.getCell(11)));
						

				
				String Str="";
				for(int j=0;j<4-String.valueOf(SEQ).length();j++){
					Str+="0";
				}
				String EL_SEQ=Str+String.valueOf(SEQ);
				
				String sql2 ="INSERT INTO DSID04_2 (MODEL_NA,EL_SEQ,COLOR,EL_NO,EL_NA,YIELD,COLOR_PRE,DAILY_UOM,INI_BUY,ET_US,UP_USER,UP_DATE) VALUES ('"+MODEL_NA+"','"+EL_SEQ+"','"+COLOR+"','"+EL_NO+"','"+EL_NA+"','"+YIELD+"','"+COLOR_PRE+"','"+DAILY_UOM+"','"+INI_BUY+"','"+ET_US+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";


				System.out.println(">>>導入>>>"+sql2);				
					
				try {
					PreparedStatement pstm = conn.prepareStatement(sql2);
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {
					Errmessage ="工作表2材料"+EL_NO+"資料導入失敗！"+e;
					conn.rollback();
					e.printStackTrace();
					
				}
						
				}		
			
			if(Errmessage.length()<=0){
				conn.commit();
			}else{
				conn.rollback();
			}				
				
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void ImportSheet0(HSSFWorkbook wb, Connection conn) {
			// TODO Auto-generated method stub
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		int Start_Row=12;
		
		HSSFFormulaEvaluator Formul= new HSSFFormulaEvaluator(wb);		
		HSSFSheet sheet =wb.getSheetAt(0);	
		
		HSSFRow row=null;
		String LA_DATE="",DR_DATE="",MODEL_UPD="";

		try {
			conn.setAutoCommit(false);
			
			for(int x=0;x<15;x++){
				System.out.println(">>>行数"+x);
				row = sheet.getRow(x);
				
				String Str1=getCellValue(row.getCell(0));
				if(Str1.contains("MODEL NAME")){
					if("".equals(txtMODEL_NA.getValue())||txtMODEL_NA.getValue()==null){
						MODEL_NA=getCellValue(row.getCell(1));
					}
					LA_DATE=getCellValue(row.getCell(3));
					DR_DATE=getCellValue(row.getCell(4));
				}
				if(Str1.contains("Lgc Grp")){
					Start_Row=x+1;
				}
				
				String Str2=getCellValue(row.getCell(28));
				if(Str2.contains("UPD")){
					
					row = sheet.getRow(x+1);
					MODEL_UPD=getCellValue(Formul.evaluateInCell(row.getCell(28)));
					
				}
						
			}
			System.out.println(">>>型體>>>"+MODEL_NA);
			
			//導入前先刪除
			DeleteModel_na(MODEL_NA,conn);
			
			String sql1 = "INSERT INTO DSID04 (MODEL_NA,LA_DATE,DR_DATE,MODEL_UPD,UP_USER,UP_DATE) " +
					"VALUES ('"+MODEL_NA+"',TO_DATE('"+LA_DATE+"','YYYY/MM/DD'),TO_DATE('"+DR_DATE+"','YYYY/MM/DD'),'"+MODEL_UPD+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";

					System.out.println(">>>導入>>>"+sql1);
						
					try {
						PreparedStatement pstm = conn.prepareStatement(sql1);	
						pstm.executeUpdate();
						pstm.close();
					} catch (Exception e) {
						Errmessage ="型體"+MODEL_NA+"資料導入失敗！"+e;
						conn.rollback();
						e.printStackTrace();						
					}
					
			System.out.println(">>>明細開始行"+(Start_Row+1));
			
			//材料明細	導入	
			int SEQ=0;
			
			for (int i = Start_Row; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				System.out.println(">>>行数"+(i+1));
				SEQ++;
				String GROUP_NO = getCellValue(row.getCell(0));
				String MT_USAGE = getCellValue(row.getCell(1));
				String EL_NO = getCellValue(row.getCell(2));
				if(EL_NO.length()>12){
					EL_NO=EL_NO.substring(EL_NO.length()-12);
				}				
				String SU_NA = getCellValue(row.getCell(3));
				String GR_NA = getCellValue(row.getCell(4)).replace("'", " ");
				String EL_NA = getCellValue(row.getCell(5)).replace("'", " ");
				String COLOR = getCellValue(row.getCell(6));
				String ELLA_DATE = getCellValue(Formul.evaluateInCell(row.getCell(7)));
				String ELDR_DATE = getCellValue(Formul.evaluateInCell(row.getCell(8)));
				String LA_TIME = getCellValue(Formul.evaluateInCell(row.getCell(11)));
				String EL_UNIT = getCellValue(row.getCell(13));				
				String MTLDAY = getCellValue(row.getCell(14));
				String SU_MOQ = getCellValue(row.getCell(15));
				String PO_PRICE = getCellValue2(row.getCell(16));
				String YIELD = getCellValue2(row.getCell(17));
				
				String COLOR_PRE = getCellValue2(Formul.evaluateInCell(row.getCell(28)));
				String SAFE_DAY = getCellValue(Formul.evaluateInCell(row.getCell(29)));
				String MIN_DAY = getCellValue(Formul.evaluateInCell(row.getCell(30)));
				String MIN_UOM = getCellValue(Formul.evaluateInCell(row.getCell(31)));
				String MAX_DAY = getCellValue(Formul.evaluateInCell(row.getCell(34)));
				String DAILY_UOM = getCellValue2(Formul.evaluateInCell(row.getCell(35)));
				String MAX_UOM = getCellValue(Formul.evaluateInCell(row.getCell(36)));
						
				if(GROUP_NO == null|| "".equals(GROUP_NO)){
					break;
				}
				
				String Str="";
				for(int j=0;j<4-String.valueOf(SEQ).length();j++){
					Str+="0";
				}
				String EL_SEQ=Str+String.valueOf(SEQ);
				
				String sql2 ="INSERT INTO DSID04_1 ( MODEL_NA,EL_SEQ,EL_NO,GROUP_NO,MT_USAGE,SU_NA,EL_NA,GR_NA,COLOR,ELLA_DATE,ELDR_DATE,LA_TIME,EL_UNIT,MTLDAY,SU_MOQ,PO_PRICE,YIELD,COLOR_PRE,\n" +
								"DAILY_UOM,SAFE_DAY,MIN_UOM,MIN_DAY,MAX_UOM,MAX_DAY,UP_USER,UP_DATE ) VALUES "
							+ "('"+MODEL_NA+"','"+EL_SEQ+"','"+EL_NO+"','"+GROUP_NO+"','"+MT_USAGE+"','"+SU_NA+"','"+EL_NA+"','"+GR_NA+"','"+COLOR+"',TO_DATE('"+ELLA_DATE+"','YYYY/MM/DD'),TO_DATE('"+ELDR_DATE+"','YYYY/MM/DD'),TO_DATE('"+LA_TIME+"','YYYY/MM/DD'),'"+EL_UNIT+"','"+MTLDAY+"','"+SU_MOQ+"','"+PO_PRICE+"','"+YIELD+"','"+COLOR_PRE+"',\n" + 
							"'"+DAILY_UOM+"','"+SAFE_DAY+"','"+MIN_UOM+"','"+MIN_DAY+"','"+MAX_UOM+"','"+MAX_DAY+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";


				System.out.println(">>>導入>>>"+sql2);				
					
				try {
					PreparedStatement pstm = conn.prepareStatement(sql2);
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {
					Errmessage ="工作表1材料"+EL_NO+"資料導入失敗！"+e;
					conn.rollback();
					e.printStackTrace();
					
				}
						
				}		
			
			if(Errmessage.length()<=0){
				conn.commit();
			}else{
				conn.rollback();
			}				
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	private void DeleteModel_na(String MODEL_NA, Connection conn) {
		// TODO Auto-generated method stub
		String sql1 ="DELETE DSID04 WHERE MODEL_NA='"+MODEL_NA+"'";
		String sql2 ="DELETE DSID04_1 WHERE MODEL_NA='"+MODEL_NA+"'";
		String sql3 ="DELETE DSID04_2 WHERE MODEL_NA='"+MODEL_NA+"'";
		String sql4 ="DELETE DSID04_3 WHERE MODEL_NA='"+MODEL_NA+"'";
		String sql5 ="DELETE DSID04_4 WHERE MODEL_NA='"+MODEL_NA+"'";
		System.out.println(">>>刪除重複資料>>>");
		try {
			PreparedStatement pstm1 = conn.prepareStatement(sql1);
			pstm1.executeUpdate();
			pstm1.close();
			PreparedStatement pstm2 = conn.prepareStatement(sql2);
			pstm2.executeUpdate();
			pstm2.close();
			PreparedStatement pstm3 = conn.prepareStatement(sql3);
			pstm3.executeUpdate();
			pstm3.close();
			PreparedStatement pstm4 = conn.prepareStatement(sql4);
			pstm4.executeUpdate();
			pstm4.close();
			PreparedStatement pstm5 = conn.prepareStatement(sql5);
			pstm5.executeUpdate();
			pstm5.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getCellValue(HSSFCell cell) {
		String cellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if(DateUtil.isCellDateFormatted(cell) && DateUtil.isValidExcelDate(cell.getNumericCellValue())){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date dt= DateUtil.getJavaDate(cell.getNumericCellValue());
					cellValue = sdf.format(dt);
				}else{
					try{
						BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
						cellValue =String.valueOf(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
					}catch(Exception ex){
					}
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				cellValue = cellValue.trim();
				break;
//			case Cell.CELL_TYPE_FORMULA://獲取公式的計算結果
//				System.out.println("*******公式？*******");
//				cellValue = cell.getCellFormula();
//				break;
			default:
				break;
			}
		}
//		System.out.println(">>>"+cellValue);
		return cellValue;
	}

	private static String getCellValue2(HSSFCell cell) {
		String cellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if(DateUtil.isCellDateFormatted(cell) && DateUtil.isValidExcelDate(cell.getNumericCellValue())){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date dt= DateUtil.getJavaDate(cell.getNumericCellValue());
					cellValue = sdf.format(dt);
				}else{
					try{
						BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
						cellValue =String.valueOf(bd.setScale(5, BigDecimal.ROUND_HALF_UP));
					}catch(Exception ex){
					}
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				cellValue = cellValue.trim();
				break;

			default:
				break;
			}
		}
//		System.out.println(">>>"+cellValue);
		return cellValue;
	}

	private void ShowMessage() {
		// TODO Auto-generated method stub
		if(Errmessage.length()>0){
			Messagebox.show("文件匯入失敗！！！"+Errmessage);
		}else{
			Messagebox.show("文件匯入成功！！！");
		}	
		Errmessage="";
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
