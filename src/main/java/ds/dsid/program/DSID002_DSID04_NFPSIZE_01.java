package ds.dsid.program;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
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
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID04_NFPSIZE;
import util.BlackBox;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Master;
import util.OperationMode;
import util.function.TableToExcel;
import util.openwin.ReturnBox;

public class DSID002_DSID04_NFPSIZE_01 extends Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox qry_MODEL_NA;
	@Wire
	private Textbox txt_MODEL_NA,txt_EL_NO,txt_SIZES;
	@Wire
	private Button btnQuery, btnSaveMaster, btnCancelMaster, btnCreateMaster,btnImportMaster;
	@Wire
	private Button onqryDSID04,ontxtDSID04,ontxtDSID04ELNO;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
//	@Wire
	//private Intbox txtUNIQUEIDa;
	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		masterComponentColumns.add(new ComponentColumn<String>(txt_MODEL_NA, "MODEL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZES, "SIZES", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "SEQ", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", new Date(), null, null));
		
		btnImportMaster = (Fileupload) window.getFellow("btnImportMaster");
		btnImportMaster.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>() {
			@SuppressWarnings("unused")
			public void onEvent(UploadEvent event) throws SQLException {
				String fileToRead = "";
				org.zkoss.util.media.Media media = event.getMedia();
				if (!media.getName().toLowerCase().endsWith(".xls")) {
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0082")+Labels.getLabel("COMM.XLSFILE")); //Excel格式不對,請檢查!檔案(.xls)
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
	public void importFromExcel(InputStream input) throws SQLException {
		try {
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFDataFormat dateFormat = wb.createDataFormat();
			getValueFromXls(sheet,dateFormat);

		} catch (FileNotFoundException e) {
			//"文件不存在!"
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0083"));
			e.printStackTrace();
		} catch (Exception e) {
			//"Excel格式不對,請檢查!"
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0082"));
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void getValueFromXls(HSSFSheet sheet, HSSFDataFormat dateFormat) throws SQLException {
		PreparedStatement ps = null;
		Connection conn = null;
		String	sql1="", sql2="";
		String vMODEL_NA="",vEL_NO="",vSEQ="",vSIZES="";
		/***** 取得匯入Excel的總筆數 *****/
		int TotalRows=0;
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {				
	    	HSSFRow row = sheet.getRow(i);
    		if (row==null){
	    		break;
    		}
	    	HSSFCell cell = row.getCell((short)0);
    		Object objvalue = TableToExcel.getCellValue(cell, dateFormat);
	    	if(objvalue==null){
			    TotalRows=i;
    			break;
	    	}else{
		    	TotalRows=i;
		    }
		}	
		/***** 取得匯入Excel的總筆數 *****/
		HSSFRow row =null;
		try {
			conn = Common.getDbConnection();
			conn.setAutoCommit(false);
			for (int index = 1; index <= TotalRows; index++) {
				int j=0;
				row = sheet.getRow(index);
				/***** 取得MODEL_NA資料 *****/
				vMODEL_NA = getCellValues(row, j, dateFormat);//型體
				if(vMODEL_NA.equals(""))throw new Exception("Row"+(index+1)+"：MODEL_NA Is Null.Please Check Data Again.");
				/***** 取得MODEL_NA資料 *****/
				/***** 取得EL_NO資料 *****/
				j++;
				vEL_NO = getCellValues(row, j, dateFormat);//材料編號
				if(vEL_NO.equals(""))throw new Exception("Row"+(index+1)+"：EL_NO Is Null.Please Check Data Again.");
				/***** 取得EL_NO資料 *****/
				/***** 取得SEQ資料 *****/
				j++;
				vSEQ = getCellValues(row, j, dateFormat);//項次
				if(vSEQ.equals(""))throw new Exception("Row"+(index+1)+"：SEQ Is Null.Please Check Data Again.");
				/***** 取得SEQ資料 *****/
				/***** 取得SIZE區段資料 *****/
				j++;
				vSIZES = getCellValues(row, j, dateFormat);//SIZE區段
				if(vSIZES.equals(""))throw new Exception("Row"+(index+1)+"：SIZES Is Null.Please Check Data Again.");
				/***** 刪除DSID04_NFPSIZE的MODEL_NA資料 *****/
				if(index == 1){
					sql1="DELETE DSID04_NFPSIZE WHERE MODEL_NA=?";
					ps = conn.prepareStatement(sql1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ps.setString(1, vMODEL_NA);
			    	ps.execute();
			    	ps.close();
				}
		    	/***** 刪除DSID04_NFPSIZE的MODEL_NA資料 *****/
		    	/***** 新增DSID04_NFPSIZE的資料 *****/
		    	sql2="INSERT INTO DSID04_NFPSIZE (MODEL_NA,EL_NO,SEQ,SIZES,UP_USER,UP_DATE) VALUES (?,?,LPAD(?,3,0),?,?,SYSDATE)";
				ps = conn.prepareStatement(sql2,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, vMODEL_NA);
				ps.setString(2, vEL_NO);
				ps.setString(3, vSEQ);
				ps.setString(4, vSIZES);
				ps.setString(5, _userInfo.getAccount());
		    	ps.execute();
		    	ps.close();
		    	/***** 新增DSID04_NFPSIZE的資料 *****/
			}
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
			conn.rollback();
		}finally{
			Messagebox.show("Import Data Success!!");
			executeQuery();
			Common.closeConnection(conn);
		}
	}
	
	private String getCellValues(HSSFRow row,int Cellindex,HSSFDataFormat dateFormat){
		String CellValues="";
		HSSFCell cell = row.getCell((short)Cellindex);
		Object obj = TableToExcel.getCellValue(cell, dateFormat);
		CellValues = getCellValue(obj,cell);
		return CellValues;
	}

	
	private String getCellValue(Object objvalue,HSSFCell cell){
		String value="";
		if(objvalue!=null){
			if(objvalue instanceof Date){
        	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        	    value=sdf.format(objvalue);
        	}else{
        		switch (cell.getCellType()) {
		    	case HSSFCell.CELL_TYPE_NUMERIC:
					DecimalFormat decimalFormat = new DecimalFormat("####.####");
					value =(decimalFormat.format(cell.getNumericCellValue()));
					break;
				case HSSFCell.CELL_TYPE_STRING:
					value=cell.getStringCellValue();
					break;
				default:
					value=cell.getStringCellValue();
					break;
				}
        	}
		}else{
			value="";
		}
		return value;
	}
	
	/**
	 * 必須複寫方法二 getRootWindow
	 */
	@Override
	protected Window getRootWindow() {
		return windowMaster;
	}

	/**
	 * 必須複寫方法三 getMasterClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMasterClass() {
		return DSID002_DSID04_NFPSIZE_01.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID04_NFPSIZE.class;
	}

	/**
	 * 必須複寫方法五 getOperationMode
	 */
	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}
	
	/**
	 * 必須複寫方法六 getMasterKeyName
	 */
	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("MODEL_NA");
		masterKey.add("EL_NO");
		masterKey.add("SEQ");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID04_NFPSIZE entity = (DSID04_NFPSIZE) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getEL_NO());
		masterKeyValue.add(entity.getSEQ());
		return masterKeyValue;
	}

	/**
	 * 必須複寫方法八
	 */
	@Override
	protected String getPagingId() {
		return "pagingCourse";
	}

	/**
	 * 必須複寫方法九
	 */
	@Override
	protected String getWhereConditionals() {
		String strSQL = "";
		
		if (!qry_MODEL_NA.getValue().trim().isEmpty()){
			strSQL += " AND  t.MODEL_NA ='"+qry_MODEL_NA.getValue()+"'";
		}
		
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		DSID04_NFPSIZE entity = (DSID04_NFPSIZE) entityMaster;
		if(entity == null){
			return;
		}
		txt_MODEL_NA.setValue((entity == null ? "" : entity.getMODEL_NA()));
		txt_EL_NO.setValue((entity == null ? "" : entity.getEL_NO()));
		txt_SIZES.setValue((entity == null ? "" : entity.getSIZES()));
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btnCreateMaster", btnCreateMaster);
		mapButton.put("btnQuery", btnQuery);
		mapButton.put("btnSaveMaster", btnSaveMaster);
		mapButton.put("btnCancelMaster", btnCancelMaster);
		super.masterReadMode(mapButton);
		
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		
		/*過濾區*/
		onqryDSID04.setDisabled(false);
		qry_MODEL_NA.setReadonly(false);
		/*編輯區*/
		txt_MODEL_NA.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_SIZES.setReadonly(true);
		ontxtDSID04.setDisabled(true);
		ontxtDSID04ELNO.setDisabled(true);
	}
	
	/**
	 * 點擊新增按鍵時作業
	 */
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		/*過濾區*/
		onqryDSID04.setDisabled(true);
		qry_MODEL_NA.setReadonly(true);
		/*編輯區*/
		txt_MODEL_NA.setReadonly(true);
		txt_EL_NO.setReadonly(true);
		txt_SIZES.setReadonly(false);
		ontxtDSID04.setDisabled(false);
		ontxtDSID04ELNO.setDisabled(false);
	}
	
	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		
		return true;
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		txt_MODEL_NA.setText("");
		txt_EL_NO.setText("");
		txt_SIZES.setText("");
	}
	
	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		DSID04_NFPSIZE DSID04_NFPSIZE= (DSID04_NFPSIZE)getMasterSel();
		switch (columnName) {
		case "SEQ":
			if (getData_Mode().equals(DataMode.CREATE_MODE))
				return getSEQ();
			else
				return DSID04_NFPSIZE.getSEQ();
		default:
			break;
		}
		return null;
	}
	
	private String getSEQ(){
		final Query qry = CRUDService.createSQL("SELECT LPAD(NVL(MAX(SEQ)+1,'001'),3,0) SEQ FROM DSID04_NFPSIZE WHERE MODEL_NA='"+txt_MODEL_NA.getText()+"'");
		return BlackBox.getValue(qry.getSingleResult());
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Listen("onOpenQueryField = #windowMaster")
	public void onOpenQueryField(ForwardEvent event) {
		 final HashMap<String, Object> map = new HashMap<String, Object>();

         map.put("parentWindow", windowMaster);
         map.put("WindoWidth", "60%");	//開窗長度
         map.put("WindoHeight", "80%");	//開窗高度
         map.put("Sizable", true);		//是否可手動調整大小
         map.put("Closable", true);		//是否可關閉視窗
         map.put("Maximizable",true);	//是否可最大化視窗
         map.put("multiple", false);    // false:單選,true:多選目前不支援共用
         /**********查詢MODEL_NA開窗**********/
         if(event.getData().equals("onqryDSID04")){
	         map.put("xmlQryID","dsid/DSID04");
	         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
	         returnBoxList.add(new ReturnBox("MODEL_NA",qry_MODEL_NA));
	         map.put("returnTextBoxList", returnBoxList);
	     /**********編輯MODEL_NA開窗**********/
         }else if(event.getData().equals("ontxtDSID04")){
        	 map.put("xmlQryID","dsid/DSID04");
	         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
	         returnBoxList.add(new ReturnBox("MODEL_NA",txt_MODEL_NA));
        	 map.put("returnTextBoxList", returnBoxList);	
    	 /**********編輯EL_NO開窗**********/	 
         }else if(event.getData().equals("ontxtDSID04ELNO")){
        	 map.put("xmlQryID","dsid/DSID_ELNO");
	         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
	         returnBoxList.add(new ReturnBox("EL_NO",txt_EL_NO));
        	 map.put("returnTextBoxList", returnBoxList);
         }
         
         Executions.createComponents("/util/openwin/QueryField.zul", null, map);
	}
	
	@Override
	@Listen("onUpdate = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void editMasterListbox(ForwardEvent evt) throws Exception {
		super.editMasterListbox(evt);
	}
}
