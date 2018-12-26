package ds.dsid.program;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.validation.constraints.Null;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import oracle.net.aso.i;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID21MImport extends OpenWinCRUD{

	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Button btnCancelabc;
	@Wire
	private Fileupload btnImport2;	
	@Wire
	private Window windowMaster;

	String Errmessage = "";
	
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
				System.out.println(" ----- fileToRead : " + fileToRead);
				InputStream input = null;
				media.isBinary();
				String sss = media.getFormat();
				input = media.getStreamData();// 獲得輸入流
				importFromExcel(input);
				
				ShowMessage();
			}
		});
	}
	
	public void importFromExcel(InputStream input) throws Exception {
		System.out.println("进入excel 读取内容");
		Connection conn = Common.getDbConnection();
		HSSFWorkbook wb = new HSSFWorkbook(input);
		//型體
		//Model_na = txtMODEL_NA.getValue();
		String MODEL_NA = "", GR_NO = "", GR_NA = "", COLOR = "", EL_NO = "", EL_NA = "", SIZE_FD = "", NOTE = "";
		String ITEMS = "";
		
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		HSSFFormulaEvaluator Formul = new HSSFFormulaEvaluator(wb);		
		HSSFSheet sheet = wb.getSheetAt(0);	
		HSSFRow row = null;
		
		PreparedStatement insertps = null, selectps = null, updateps = null;
		ResultSet selectrs = null;

		try {
			//conn.setAutoCommit(false); // 數據庫事務控制, 批量提交數據庫操作.
			
			for(int x = 1;x < sheet.getPhysicalNumberOfRows();x++){
				
				row = sheet.getRow(x);
				if(row.getCell(0) == null || "".equals(row.getCell(0))){
					
					break;
				}
				
				MODEL_NA = getCellValue(row.getCell(0));
				ITEMS = getCellValue(row.getCell(1));
				if(ITEMS == null || "".equals(ITEMS)){
					ITEMS = String.valueOf(AutoSeq(MODEL_NA, conn));
				}
				GR_NO = getCellValue(row.getCell(2));
				GR_NA = getCellValue(row.getCell(3));
				COLOR = getCellValue(row.getCell(4));
				EL_NO = getCellValue(row.getCell(5));
				EL_NA = getCellValue(row.getCell(6));
				SIZE_FD = getCellValue(row.getCell(7));
				NOTE = getCellValue(row.getCell(8));
				
				/* System.out.println(" ----- 行數 : "+x+" --- 型體: " + MODEL_NA + " --- 編號: " + ITEMS + " --- 部位: " + GR_NO +
				" --- 部位名稱: " + GR_NA + " --- 顏色: " + COLOR + " --- 材料編號: " + EL_NO + 
				" --- 材料名稱: " + EL_NA + " --- Size分段: " + SIZE_FD + " --- 備註: " + NOTE);*/
				System.err.println(" ----- items : " + ITEMS);
		
				// 原有資料查詢
				String selectsql = "select * from dsid21 where model_na = '"+MODEL_NA+"' and items = '"+ITEMS+"' and color = '"+COLOR+"' and el_no = '"+EL_NO+"'";
				System.out.println(" ----- select sql : " + selectsql);
				try {
					selectps = conn.prepareStatement(selectsql);
					selectrs = selectps.executeQuery();
					
					if(selectrs.next()){ // 更新已存在的資料
						if(MODEL_NA != null || !"".equals(MODEL_NA) && ITEMS != null || !"".equals(ITEMS)){
							try {
								String updatesql = "update dsid21 set gr_no = '"+GR_NO+"', gr_na = '"+GR_NA+"', color = '"+COLOR+"', el_no = '"+EL_NO+"', el_na = '"+EL_NA+"', size_fd = '"+SIZE_FD+"', note  = '"+NOTE+"', "
										+ "up_user = '"+_userInfo.getAccount()+"', up_date = to_date('"+Format.format(new Date())+"','YYYY/MM/DD')"
										+ "where model_na = '"+MODEL_NA+"' and items = '"+ITEMS+"'";
								System.err.println(" ----- updatesel : " + updatesql);
								updateps = conn.prepareStatement(updatesql);
								updateps.executeUpdate();
								updateps.close();
							} catch (Exception e) {
								// TODO: handle exception
								Errmessage = "資料更改失敗 !";
							}
						}
					}else{ // 插入未存在的資料
						try {
							String insertsql = "insert into dsid21 (model_na, items, gr_no, gr_na,  color, el_no, el_na, size_fd, note, up_user, up_date) "
									+ "values "
									+ "('"+MODEL_NA+"', '"+ITEMS+"', '"+GR_NO+"',  '"+GR_NA+"', '"+COLOR+"', '"+EL_NO+"', '"+EL_NA+"', '"+SIZE_FD+"', '"+NOTE+"', '"+_userInfo.getAccount()+"', to_date('"+Format.format(new Date())+"','YYYY/MM/DD'))";
							System.out.println(" ----- insert sql : "+ insertsql);
						
							insertps = conn.prepareStatement(insertsql);
							insertps.executeUpdate();
							
							insertps.close();	
						} catch (Exception e) {
							// TODO: handle exception
							Errmessage = "資料匯入失敗 !";
							e.printStackTrace();
						}
					}
					selectrs.close();
					selectps.close();
				} catch (Exception e) {
					// TODO: handle exception
					Errmessage = "查詢原有資料失敗 !";
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			conn.rollback();
			e.printStackTrace();
		} finally {
			Common.closeConnection(conn);
		}
	}
	
	/**
	 * 取消按鍵事件
	 * 
	 * @param event
	 */
	@Listen("onClick = #btnCancelabc")
	public void onClickbtnCancel(Event event){
		getRootWindow().detach();
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
			default:
				break;
			}
		}
//		System.out.println(">>>"+cellValue);
		return cellValue;
	}
	
	private void ShowMessage() {
		// TODO Auto-generated method stub
		if(Errmessage.length() > 0){
			Messagebox.show("文件匯入失敗！！！"+Errmessage);
		}else{
			Messagebox.show("文件匯入成功！！！");
		}	
		Errmessage="";
	}
	
	// 生成序號
	private Object AutoSeq(String model_na, Connection conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int seq = 0;
		
		String sql = "select LPAD(NVL(MAX(items),0)+1,4,'0') items from dsid21 where model_na = '"+model_na+"'";
		System.out.println(" ----- Auto : " + sql);
		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()){
				seq = rs.getInt("items");
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seq;
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
