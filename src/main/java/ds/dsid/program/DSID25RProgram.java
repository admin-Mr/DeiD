package ds.dsid.program;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import ds.dsid.domain.DSID01;
import ds.dsid.domain.DSID25;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Detail;
import util.MSMode;
import util.Master;
import util.OpenWinCRUD;
import util.OperationMode;

public class DSID25RProgram extends Master{

	@Wire
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnexport1,btnQuery,btnQuery1,btnImport,btnChangeRe;
	@Wire
	private Datebox po_date1;
	@Wire
	private Listbox List_Model_na;
	@Wire
	private Textbox txtEL_NO,TXT_BA_MU;
	@Wire
	private Label TXT_MODEL_NA,TXT_EL_NO;
	@Wire
	private Listbox masterListbox;
	@Wire
	Tabpanel Detail1;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	String Errmessage="";
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		doFillListbox(0);
		
		masterComponentColumns.add(new ComponentColumn<String>("TXT_MODEL_NA", "MODEL_NA", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TXT_EL_NO", "EL_NO", null, null, null,false));
		masterComponentColumns.add(new ComponentColumn<String>("TXT_BA_MU", "BA_MU", null, null, null,false));
		
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
		//型體	
		try{
			ImportSheet(wb,conn);//主檔
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
	
		String MODEL_NA="",LAST_GR_NA="";

		try {
			conn.setAutoCommit(false);	
			
			row = sheet.getRow(0);
			MODEL_NA = getCellValue(row.getCell(0));
			//導入前刪除
			Delete(MODEL_NA,conn);
			//材料明細	導入	
	
			for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
				
				row = sheet.getRow(i);
				System.out.println(">>>行数"+(i+1));

				String EL_NO = getCellValue(row.getCell(1));
				if("".equals(EL_NO)){
					break;
				}
				String GR_NA = getCellValue(row.getCell(0));
				if("".equals(GR_NA)||GR_NA==null){
					GR_NA=LAST_GR_NA;
				}else{
					LAST_GR_NA=GR_NA;
				}
				String EL_NA = getCellValue(row.getCell(2)).replace("'", " ");
				String BA_MU = getCellValue(row.getCell(3));
				if("".equals(BA_MU)){
					BA_MU="1"; 	// 為空時 默認為1
				}
				String EL_UNIT = getCellValue(row.getCell(4));				
									
				
				String sql2 ="INSERT INTO DSID25 (MODEL_NA,EL_NO,EL_NA,GR_NA,BA_MU,EL_UNIT,UP_USER,UP_DATE) VALUES('"+MODEL_NA+"','"+EL_NO+"','"+EL_NA+"','"+GR_NA+"','"+BA_MU+"','"+EL_UNIT+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";

				System.out.println(">>>導入>>>"+sql2);				
					
				try {
					pstm = conn.prepareStatement(sql2);
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {
					Errmessage ="材料"+EL_NO+"資料導入失敗！"+e;
					conn.rollback();
					e.printStackTrace();
					
				}					
				if(Errmessage.length()<=0){
					conn.commit();
				}else{
					conn.rollback();
				}
			}		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstm!=null){
				pstm.close();
			}
		
		}

		
		
		if(Errmessage.length()>0){
			Messagebox.show("文件匯入失敗！！！"+Errmessage);
		}else{
			Messagebox.show("匯入成功！！！");
		}	
	}
		
	private void Delete(String MODEL_NA, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstm1 =null;
		String sql1 ="DELETE DSID25 WHERE MODEL_NA='"+MODEL_NA+"'";
//		System.out.println(">>>刪除資料>>>");
		try {
			pstm1 = conn.prepareStatement(sql1);
			pstm1.executeUpdate();
			pstm1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstm1!=null){
				pstm1.close();
			}
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
	
	@Listen("onClick = #btnQuery1")
	public void onClickbtnQuery1(Event event) throws SQLException{
		Date Date1=po_date1.getValue();
//		Date Date2=po_date2.getValue();
		
		if(Date1!=null){
			String START=sdf.format(po_date1.getValue());
//			String END=sdf.format(po_date2.getValue());
			Connection conn = Common.getDbConnection();
			PreparedStatement  ps1 = null;
			ResultSet  rs1 = null;
			List<String> name_list = new ArrayList<String>();
			name_list.add("");
			String 	Sql=" SELECT DISTINCT CASE WHEN MODEL_NA LIKE 'W%' THEN SUBSTR(MODEL_NA,3) ELSE MODEL_NA END AS MODEL_NA FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+START+"'";
			System.err.println(">>>"+Sql);
			try {
				ps1 = conn.prepareStatement(Sql);
				rs1 = ps1.executeQuery();			
				while(rs1.next()){

					name_list.add(rs1.getString("MODEL_NA"));
				}
				rs1.close();
				ps1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {	
				if(rs1!=null){
					rs1.close();
				}
				if(ps1!=null){
					ps1.close();
				}

				Common.closeConnection(conn);	
			}
			
			List_Model_na.setModel(new ListModelList<Object>(name_list));
		}else{
			Messagebox.show("請選擇正確的日期區間！！！");
		}
	  }

	@Listen("onClick = #btnexport1")
	public void onClickbtnExport(Event event) throws SQLException{		
		Date Date1=po_date1.getValue();
		
		if(Date1!=null){
			String START=sdf.format(po_date1.getValue());
			String MODEL_NA="";
			if(List_Model_na.getSelectedItem()!=null){
				for(Listitem ltAll : List_Model_na.getItems()){
					if (ltAll.isSelected()){
						if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
							MODEL_NA=(String)ltAll.getValue();				
						}
					}
				}
			}
			
			
			DSID25_1RTask.ExcelExport(START,MODEL_NA);
		}else{
			Messagebox.show("日期不能為空！！！");
		}
		

	}

	// create listbox裡面的cell，將資料及元件建立
	@Listen("onAfterRender = #masterListbox")
	public void onAfterRenderMasterListbox(Event event) {
			if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE)) {				
				TXT_MODEL_NA = (Label) windowMaster.getFellow("TXT_MODEL_NA");
				TXT_EL_NO = (Label) windowMaster.getFellow("TXT_EL_NO");
				TXT_BA_MU = (Textbox) windowMaster.getFellow("TXT_BA_MU");				
			}
	
		}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID25RProgram.class;
	}


	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID25.class;
	}


	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub
		return OperationMode.NORMAL;
	}


	@Override
	protected Class getDetailClass(int indexDetail) {
		// TODO Auto-generated method stub
		return getDetailProgram(indexDetail).getDetailClass();
	}


	@Override
	protected Detail getDetailProgram(int indexDetail) {
		// TODO Auto-generated method stub
		return getArrDetailPrograms().get(indexDetail);
	}


	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("masterProgram", this);
		map.put("indexDetailProgram", 0);
		super.addDetailPrograms("/ds/dsid/DSID25RDetail.zul", Detail1, map);
		setDetail(1);
	}


	@Override
	protected ArrayList<String> getMasterKeyName() {
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("EL_NO");
		masterKey.add("MODEL_NA");
		return masterKey;
	}


	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID25 entity = (DSID25) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getEL_NO());
		masterKeyValue.add(entity.getMODEL_NA());
		return masterKeyValue;
	}


	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
	}


	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		String Sql="";
		String MODEL_NA="";
		if(List_Model_na.getSelectedItem()!=null){
			for(Listitem ltAll : List_Model_na.getItems()){
				if (ltAll.isSelected()){
					if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
						MODEL_NA=(String)ltAll.getValue();				
					}
				}
			}
		}
		if(!"".equals(MODEL_NA)){
			Sql+=" AND MODEL_NA='"+MODEL_NA+"'";
		}
		if(!txtEL_NO.getValue().isEmpty()){
			Sql+=" AND EL_NO ='"+txtEL_NO.getValue()+"'";
		}
		return Sql;
	}


	@Override
	protected String getDetailConditionals() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String getMasterCreateZul() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String getMasterUpdateZul() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String getMasterQueryZul() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected HashMap getMasterCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
//		DSID25 entity = (DSID25) entityMaster;
//		txtod_no.setValue(entity == null ? "" : entity.getOD_NO());
//		txtorder_date.setValue(entity == null ? null : entity.getORDER_DATE());
	}


	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	protected boolean beforeMasterDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
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
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID25";
	}


	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 10;
	}


	@Override
	protected boolean doCustomSave() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
//		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
//		btnSaveMaster.setDisabled(true);
//		btnCancelMaster.setDisabled(true);

	}
	
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
//		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
//		btnSaveMaster.setDisabled(false);
//		btnCancelMaster.setDisabled(false);
	
	}

}
