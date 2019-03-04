package ds.dsid.program;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.DSID07;
import util.Common;
import util.ComponentColumn;
import util.Master;


public class DSID07MProgram extends Master{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;	
	@Wire
	private Textbox TADH_ELNO,TPRO_TYPE,TEL_UNIT,TCOLOR,TMODEL_NA,TADH_ELNA,TRAW_ELNO1,TRAW_PRO1,TRAW_ELNO2,TRAW_PRO2,TRAW_ELNO3,TRAW_PRO3,query_ADH_ELNO;
	@Wire
	private Fileupload btnImport;
	Object [][] importdata=null;

	String Errmessage="";
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>(TADH_ELNO, "ADH_ELNO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TPRO_TYPE, "PRO_TYPE", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TEL_UNIT, "EL_UNIT", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TCOLOR, "COLOR", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TMODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TADH_ELNA, "ADH_ELNA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_ELNO1, "RAW_ELNO1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_PRO1, "RAW_PRO1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_ELNO2, "RAW_ELNO2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_PRO2, "RAW_PRO2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_ELNO3, "RAW_ELNO3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(TRAW_PRO3, "RAW_PRO3", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
	
		btnImport = (Fileupload) window.getFellow("btnImport");
		btnImport.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>() {
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
		
		try{
			ImportSheet(wb,conn);//主檔

//		ShowMessage();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			Common.closeConnection(conn);
		}

	}

	private void ImportSheet(HSSFWorkbook wb, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println(">>>讀取工作表>>>>>>>>>>>>>>");

        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
//		HSSFFormulaEvaluator Formul= new HSSFFormulaEvaluator(wb);		
		HSSFSheet sheet =wb.getSheetAt(0);	
		HSSFRow row=null;
		PreparedStatement pstm =null;
		row = sheet.getRow(0);		
		String sql ="";
		
		try {
			conn.setAutoCommit(false);	
			//材料明細	導入	
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				
				row = sheet.getRow(i);
				System.out.println(">>>行数"+(i+1));
				String ADH_ELNO = getCellValue(row.getCell(0));
				if("".equals(ADH_ELNO)){
					break;
				}
				String ADH_ELNA = getCellValue(row.getCell(1));

				String PRO_TYPE = getCellValue(row.getCell(2));
				String EL_UNIT = getCellValue(row.getCell(3));
				String COLOR = getCellValue(row.getCell(4));				
				String MODEL_NA = getCellValue(row.getCell(5));
				String RAW_ELNO1 = getCellValue(row.getCell(6));
				String RAW_PRO1 = getCellValue(row.getCell(7));	
				String RAW_ELNO2 = getCellValue(row.getCell(8));
				String RAW_PRO2 = getCellValue(row.getCell(9));	
				String RAW_ELNO3 = getCellValue(row.getCell(10));
				String RAW_PRO3 = getCellValue(row.getCell(11));	
				
				sql +="INTO DSID07 (ADH_ELNO,ADH_ELNA,PRO_TYPE,EL_UNIT,COLOR,MODEL_NA,RAW_ELNO1,RAW_PRO1,RAW_ELNO2,RAW_PRO2,RAW_ELNO3,RAW_PRO3,UP_USER,UP_DATE) VALUES ('"+ADH_ELNO+"','"+ADH_ELNA+"','"+PRO_TYPE+"','"+EL_UNIT+"','"+COLOR+"','"+MODEL_NA+"','"+RAW_ELNO1+"','"+RAW_PRO1+"','"+RAW_ELNO2+"','"+RAW_PRO2+"','"+RAW_ELNO3+"','"+RAW_PRO3+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";
					
			}
			if(!"".equals(sql)){
				sql ="INSERT ALL "+sql+" SELECT * FROM DUAL";
				System.out.println(">>>導入>>>"+sql);	
				try {
					pstm = conn.prepareStatement(sql);
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {	
					Errmessage=e.getMessage();
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
		}finally{
			if(pstm!=null){
				pstm.close();
			}
		
		}
		
		if(Errmessage.length()>0){
			Messagebox.show(Labels.getLabel("DSID.MSG0019")+Errmessage);
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0021"));
		}	
	}
	
	private static String getCellValue(HSSFCell cell) {
		String cellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_BOOLEAN:
//	        	System.err.println("Cell.CELL_TYPE_BOOLEAN:"+Cell.CELL_TYPE_BOOLEAN);
	            return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
	        case Cell.CELL_TYPE_FORMULA://公式格式
//	        	System.err.println("Cell.CELL_TYPE_FORMULA:"+Cell.CELL_TYPE_FORMULA);
	            return cell.getCellFormula();
	        case Cell.CELL_TYPE_NUMERIC://数字格式
//	        	System.err.println("Cell.CELL_TYPE_NUMERIC:"+Cell.CELL_TYPE_NUMERIC);
	            cell.setCellType(Cell.CELL_TYPE_STRING);
	            return cell.getStringCellValue();
	        case Cell.CELL_TYPE_STRING:
//	        	System.err.println("Cell.CELL_TYPE_STRING:"+Cell.CELL_TYPE_STRING);
	            return cell.getStringCellValue();
//	        case Cell.CELL_TYPE_ERROR:
//	        	System.err.println("Cell.CELL_TYPE_ERROR:"+Cell.CELL_TYPE_ERROR);
//	            return String.valueOf(cell.getErrorCellValue());
			default:
//				System.err.println("Cell.default");
				break;
			}
		}else{
			System.err.println("Cell.NULL");
		}
//		System.out.println(">>>"+cellValue);
		return cellValue;
	}
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID07MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID07.class;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID07 entity = (DSID07) entityMaster;
		TADH_ELNO.setValue(entity == null ? "" : entity.getADH_ELNO());
		TPRO_TYPE.setValue(entity == null ? "" : entity.getPRO_TYPE());
		TEL_UNIT.setValue(entity == null ? "" : entity.getEL_UNIT());
		TCOLOR.setValue(entity == null ? "" : entity.getCOLOR());
		TMODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());
		TADH_ELNA.setValue(entity == null ? "" : entity.getADH_ELNA());	
		TRAW_ELNO1.setValue(entity == null ? "" : entity.getRAW_ELNO1());
		TRAW_PRO1.setValue(entity == null ? "" : entity.getRAW_PRO1());
		TRAW_ELNO2.setValue(entity == null ? "" : entity.getRAW_ELNO2());
		TRAW_PRO2.setValue(entity == null ? "" : entity.getRAW_PRO2());
		TRAW_ELNO3.setValue(entity == null ? "" : entity.getRAW_ELNO3());
		TRAW_PRO3.setValue(entity == null ? "" : entity.getRAW_PRO3());
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String sql="";
		if(!query_ADH_ELNO.getValue().isEmpty()){
			sql+=" AND ADH_ELNO LIKE '%"+query_ADH_ELNO.getValue()+"%'";
		}
		
		return sql;
	}
	
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		btnEditMaster.setDisabled(false);
		btnDeleteMaster.setDisabled(false);
		
		TADH_ELNO.setReadonly(true);
		TPRO_TYPE.setReadonly(true);	
		TEL_UNIT.setReadonly(true);
		TCOLOR.setReadonly(true);
		TMODEL_NA.setReadonly(true);
		TADH_ELNA.setReadonly(true);
		TRAW_ELNO1.setReadonly(true);
		TRAW_PRO1.setReadonly(true);	
		TRAW_ELNO2.setReadonly(true);
		TRAW_PRO2.setReadonly(true);
		TRAW_ELNO3.setReadonly(true);
		TRAW_PRO3.setReadonly(true);
	}
	
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		btnEditMaster.setDisabled(true);
		btnDeleteMaster.setDisabled(true);
		
		TADH_ELNO.setReadonly(false);
		TPRO_TYPE.setReadonly(false);	
		TEL_UNIT.setReadonly(false);
		TCOLOR.setReadonly(false);
		TMODEL_NA.setReadonly(false);
		TADH_ELNA.setReadonly(false);
		TRAW_ELNO1.setReadonly(false);
		TRAW_PRO1.setReadonly(false);	
		TRAW_ELNO2.setReadonly(false);
		TRAW_PRO2.setReadonly(false);
		TRAW_ELNO3.setReadonly(false);
		TRAW_PRO3.setReadonly(false);
		
		TRAW_PRO1.setValue("1");
		TRAW_PRO2.setValue("1");
		TRAW_PRO3.setValue("1");
	}
		
}
