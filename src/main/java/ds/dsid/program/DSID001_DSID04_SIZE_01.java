package ds.dsid.program;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID04_SIZE;
import util.BlackBox;
import util.Common;
import util.ComponentColumn;
import util.DataMode;
import util.Master;
import util.OperationMode;
import util.function.TableToExcel;
import util.openwin.ReturnBox;

public class DSID001_DSID04_SIZE_01 extends Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox qry_MODEL_NA;
	@Wire
	private Textbox txt_MODEL_NA,txt_EL_NO;
	@Wire
	private Combobox cboqrySH_LAST,cboSH_LAST;
	@Wire
	private Textbox txt_SIZE1,txt_SIZE2,txt_SIZE3,txt_SIZE4,txt_SIZE5,txt_SIZE6,txt_SIZE7,txt_SIZE8,txt_SIZE9,txt_SIZE10,
					txt_SIZE11,txt_SIZE12,txt_SIZE13,txt_SIZE14,txt_SIZE15,txt_SIZE16,txt_SIZE17,txt_SIZE18,txt_SIZE19,txt_SIZE20,
					txt_SIZE21,txt_SIZE22,txt_SIZE23,txt_SIZE24,txt_SIZE25,txt_SIZE26,txt_SIZE27,txt_SIZE28,txt_SIZE29,txt_SIZE30,
					txt_SIZE31,txt_SIZE32,txt_SIZE33,txt_SIZE34,txt_SIZE35,txt_SIZE36,txt_SIZE37,txt_SIZE38,txt_SIZE39,txt_SIZE40;
	@Wire
	private Doublebox txt_PER1,txt_PER2,txt_PER3,txt_PER4,txt_PER5,txt_PER6,txt_PER7,txt_PER8,txt_PER9,txt_PER10,
					txt_PER11,txt_PER12,txt_PER13,txt_PER14,txt_PER15,txt_PER16,txt_PER17,txt_PER18,txt_PER19,txt_PER20,
					txt_PER21,txt_PER22,txt_PER23,txt_PER24,txt_PER25,txt_PER26,txt_PER27,txt_PER28,txt_PER29,txt_PER30,
					txt_PER31,txt_PER32,txt_PER33,txt_PER34,txt_PER35,txt_PER36,txt_PER37,txt_PER38,txt_PER39,txt_PER40;
	@Wire
	private Doublebox txt_MAX_DAY,txt_MIN_DAY;
	@Wire
	private Radiogroup sv1;
	@Wire
	private Radio radMale,radFemale;
	@Wire
	private Button btnQuery, btnSaveMaster, btnCancelMaster, btnCreateMaster, btnImportMaster;
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
		masterComponentColumns.add(new ComponentColumn<String>(cboSH_LAST, "SH_LAST", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MAX_DAY, "MAX_DAY", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_MIN_DAY, "MIN_DAY", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE1,  "S1",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE2,  "S2",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE3,  "S3",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE4,  "S4",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE5,  "S5",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE6,  "S6",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE7,  "S7",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE8,  "S8",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE9,  "S9",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE10, "S10", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE11, "S11", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE12, "S12", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE13, "S13", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE14, "S14", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE15, "S15", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE16, "S16", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE17, "S17", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE18, "S18", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE19, "S19", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE20, "S20", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE21, "S21", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE22, "S22", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE23, "S23", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE24, "S24", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE25, "S25", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE26, "S26", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE27, "S27", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE28, "S28", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE29, "S29", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE30, "S30", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE31, "S31", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE32, "S32", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE33, "S33", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE34, "S34", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE35, "S35", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE36, "S36", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE37, "S37", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE38, "S38", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE39, "S39", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_SIZE40, "S40", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER1,  "Q1",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER2,  "Q2",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER3,  "Q3",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER4,  "Q4",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER5,  "Q5",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER6,  "Q6",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER7,  "Q7",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER8,  "Q8",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER9,  "Q9",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER10, "Q10", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER11, "Q11", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER12, "Q12", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER13, "Q13", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER14, "Q14", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER15, "Q15", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER16, "Q16", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER17, "Q17", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER18, "Q18", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER19, "Q19", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER20, "Q20", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER21, "Q21", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER22, "Q22", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER23, "Q23", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER24, "Q24", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER25, "Q25", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER26, "Q26", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER27, "Q27", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER28, "Q28", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER29, "Q29", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER30, "Q30", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER31, "Q31", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER32, "Q32", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER33, "Q33", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER34, "Q34", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER35, "Q35", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER36, "Q36", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER37, "Q37", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER38, "Q38", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER39, "Q39", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_PER40, "Q40", null, null, null));
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
		return DSID001_DSID04_SIZE_01.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSID04_SIZE.class;
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
		masterKey.add("SH_LAST");
		masterKey.add("EL_NO");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID04_SIZE entity = (DSID04_SIZE) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getMODEL_NA());
		masterKeyValue.add(entity.getSH_LAST());
		masterKeyValue.add(entity.getEL_NO());
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
		if(cboqrySH_LAST.getSelectedIndex() == 1){
			strSQL += " AND  t.SH_LAST ='M'";
		}else if(cboqrySH_LAST.getSelectedIndex() == 2){
			strSQL += " AND  t.SH_LAST ='W'";
		}
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		DSID04_SIZE entity = (DSID04_SIZE) entityMaster;
		if(entity == null){
			return;
		}
		txt_MODEL_NA.setValue((entity.getMODEL_NA() == null ? "" : entity.getMODEL_NA()));
		cboSH_LAST.setSelectedIndex(entity.getSH_LAST().equals("M") ? 0 : 1);
		txt_EL_NO.setValue((entity == null ? "" : entity.getEL_NO()));
		txt_MAX_DAY.setValue((entity.getMAX_DAY() == null ? 0 : entity.getMAX_DAY()));
		txt_MIN_DAY.setValue((entity.getMIN_DAY() == null ? 0 : entity.getMIN_DAY()));
		txt_SIZE1.setValue((entity == null ? "" : entity.getS1()));
		txt_SIZE2.setValue((entity == null ? "" : entity.getS2()));
		txt_SIZE3.setValue((entity == null ? "" : entity.getS3()));
		txt_SIZE4.setValue((entity == null ? "" : entity.getS4()));
		txt_SIZE5.setValue((entity == null ? "" : entity.getS5()));
		txt_SIZE6.setValue((entity == null ? "" : entity.getS6()));
		txt_SIZE7.setValue((entity == null ? "" : entity.getS7()));
		txt_SIZE8.setValue((entity == null ? "" : entity.getS8()));
		txt_SIZE9.setValue((entity == null ? "" : entity.getS9()));
		txt_SIZE10.setValue((entity == null ? "" : entity.getS10()));
		txt_SIZE11.setValue((entity == null ? "" : entity.getS11()));
		txt_SIZE12.setValue((entity == null ? "" : entity.getS12()));
		txt_SIZE13.setValue((entity == null ? "" : entity.getS13()));
		txt_SIZE14.setValue((entity == null ? "" : entity.getS14()));
		txt_SIZE15.setValue((entity == null ? "" : entity.getS15()));
		txt_SIZE16.setValue((entity == null ? "" : entity.getS16()));
		txt_SIZE17.setValue((entity == null ? "" : entity.getS17()));
		txt_SIZE18.setValue((entity == null ? "" : entity.getS18()));
		txt_SIZE19.setValue((entity == null ? "" : entity.getS19()));
		txt_SIZE20.setValue((entity == null ? "" : entity.getS20()));
		txt_SIZE21.setValue((entity == null ? "" : entity.getS21()));
		txt_SIZE22.setValue((entity == null ? "" : entity.getS22()));
		txt_SIZE23.setValue((entity == null ? "" : entity.getS23()));
		txt_SIZE24.setValue((entity == null ? "" : entity.getS24()));
		txt_SIZE25.setValue((entity == null ? "" : entity.getS25()));
		txt_SIZE26.setValue((entity == null ? "" : entity.getS26()));
		txt_SIZE27.setValue((entity == null ? "" : entity.getS27()));
		txt_SIZE28.setValue((entity == null ? "" : entity.getS28()));
		txt_SIZE29.setValue((entity == null ? "" : entity.getS29()));
		txt_SIZE30.setValue((entity == null ? "" : entity.getS30()));
		txt_SIZE31.setValue((entity == null ? "" : entity.getS31()));
		txt_SIZE32.setValue((entity == null ? "" : entity.getS32()));
		txt_SIZE33.setValue((entity == null ? "" : entity.getS33()));
		txt_SIZE34.setValue((entity == null ? "" : entity.getS34()));
		txt_SIZE35.setValue((entity == null ? "" : entity.getS35()));
		txt_SIZE36.setValue((entity == null ? "" : entity.getS36()));
		txt_SIZE37.setValue((entity == null ? "" : entity.getS37()));
		txt_SIZE38.setValue((entity == null ? "" : entity.getS38()));
		txt_SIZE39.setValue((entity == null ? "" : entity.getS39()));
		txt_SIZE40.setValue((entity == null ? "" : entity.getS40()));
		txt_PER1.setValue((entity == null ? 0 : entity.getQ1()));
		txt_PER2.setValue((entity == null ? 0 : entity.getQ2()));
		txt_PER3.setValue((entity == null ? 0 : entity.getQ3()));
		txt_PER4.setValue((entity == null ? 0 : entity.getQ4()));
		txt_PER5.setValue((entity == null ? 0 : entity.getQ5()));
		txt_PER6.setValue((entity == null ? 0 : entity.getQ6()));
		txt_PER7.setValue((entity == null ? 0 : entity.getQ7()));
		txt_PER8.setValue((entity == null ? 0 : entity.getQ8()));
		txt_PER9.setValue((entity == null ? 0 : entity.getQ9()));
		txt_PER10.setValue((entity == null ? 0 : entity.getQ10()));
		txt_PER11.setValue((entity == null ? 0 : entity.getQ11()));
		txt_PER12.setValue((entity == null ? 0 : entity.getQ12()));
		txt_PER13.setValue((entity == null ? 0 : entity.getQ13()));
		txt_PER14.setValue((entity == null ? 0 : entity.getQ14()));
		txt_PER15.setValue((entity == null ? 0 : entity.getQ15()));
		txt_PER16.setValue((entity == null ? 0 : entity.getQ16()));
		txt_PER17.setValue((entity == null ? 0 : entity.getQ17()));
		txt_PER18.setValue((entity == null ? 0 : entity.getQ18()));
		txt_PER19.setValue((entity == null ? 0 : entity.getQ19()));
		txt_PER20.setValue((entity == null ? 0 : entity.getQ20()));
		txt_PER21.setValue((entity == null ? 0 : entity.getQ21()));
		txt_PER22.setValue((entity == null ? 0 : entity.getQ22()));
		txt_PER23.setValue((entity == null ? 0 : entity.getQ23()));
		txt_PER24.setValue((entity == null ? 0 : entity.getQ24()));
		txt_PER25.setValue((entity == null ? 0 : entity.getQ25()));
		txt_PER26.setValue((entity == null ? 0 : entity.getQ26()));
		txt_PER27.setValue((entity == null ? 0 : entity.getQ27()));
		txt_PER28.setValue((entity == null ? 0 : entity.getQ28()));
		txt_PER29.setValue((entity == null ? 0 : entity.getQ29()));
		txt_PER30.setValue((entity == null ? 0 : entity.getQ30()));
		txt_PER31.setValue((entity == null ? 0 : entity.getQ31()));
		txt_PER32.setValue((entity == null ? 0 : entity.getQ32()));
		txt_PER33.setValue((entity == null ? 0 : entity.getQ33()));
		txt_PER34.setValue((entity == null ? 0 : entity.getQ34()));
		txt_PER35.setValue((entity == null ? 0 : entity.getQ35()));
		txt_PER36.setValue((entity == null ? 0 : entity.getQ36()));
		txt_PER37.setValue((entity == null ? 0 : entity.getQ37()));
		txt_PER38.setValue((entity == null ? 0 : entity.getQ38()));
		txt_PER39.setValue((entity == null ? 0 : entity.getQ39()));
		txt_PER40.setValue((entity == null ? 0 : entity.getQ40()));
	
	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btnCreateMaster", btnCreateMaster);
		mapButton.put("btnQuery", btnQuery);
		mapButton.put("btnImportMaster", btnImportMaster);
		mapButton.put("btnSaveMaster", btnSaveMaster);
		mapButton.put("btnCancelMaster", btnCancelMaster);
		super.masterReadMode(mapButton);
		
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		
		/*過濾區*/
		onqryDSID04.setDisabled(false);
		qry_MODEL_NA.setReadonly(false);
		cboqrySH_LAST.setDisabled(false);
		/*編輯區*/
		txt_MODEL_NA.setReadonly(true);
		ontxtDSID04.setDisabled(true);
		cboSH_LAST.setDisabled(true);
		txt_EL_NO.setReadonly(true);
		ontxtDSID04ELNO.setDisabled(true);
		txt_MAX_DAY.setReadonly(true);
		txt_MIN_DAY.setReadonly(true);
		txt_SIZE1.setReadonly(true);
		txt_SIZE2.setReadonly(true);
		txt_SIZE3.setReadonly(true);
		txt_SIZE4.setReadonly(true);
		txt_SIZE5.setReadonly(true);
		txt_SIZE6.setReadonly(true);
		txt_SIZE7.setReadonly(true);
		txt_SIZE8.setReadonly(true);
		txt_SIZE9.setReadonly(true);
		txt_SIZE10.setReadonly(true);
		txt_SIZE11.setReadonly(true);
		txt_SIZE12.setReadonly(true);
		txt_SIZE13.setReadonly(true);
		txt_SIZE14.setReadonly(true);
		txt_SIZE15.setReadonly(true);
		txt_SIZE16.setReadonly(true);
		txt_SIZE17.setReadonly(true);
		txt_SIZE18.setReadonly(true);
		txt_SIZE19.setReadonly(true);
		txt_SIZE20.setReadonly(true);
		txt_SIZE21.setReadonly(true);
		txt_SIZE22.setReadonly(true);
		txt_SIZE23.setReadonly(true);
		txt_SIZE24.setReadonly(true);
		txt_SIZE25.setReadonly(true);
		txt_SIZE26.setReadonly(true);
		txt_SIZE27.setReadonly(true);
		txt_SIZE28.setReadonly(true);
		txt_SIZE29.setReadonly(true);
		txt_SIZE30.setReadonly(true);
		txt_SIZE31.setReadonly(true);
		txt_SIZE32.setReadonly(true);
		txt_SIZE33.setReadonly(true);
		txt_SIZE34.setReadonly(true);
		txt_SIZE35.setReadonly(true);
		txt_SIZE36.setReadonly(true);
		txt_SIZE37.setReadonly(true);
		txt_SIZE38.setReadonly(true);
		txt_SIZE39.setReadonly(true);
		txt_SIZE40.setReadonly(true);
		txt_PER1.setReadonly(true);
		txt_PER2.setReadonly(true);
		txt_PER3.setReadonly(true);
		txt_PER4.setReadonly(true);
		txt_PER5.setReadonly(true);
		txt_PER6.setReadonly(true);
		txt_PER7.setReadonly(true);
		txt_PER8.setReadonly(true);
		txt_PER9.setReadonly(true);
		txt_PER10.setReadonly(true);
		txt_PER11.setReadonly(true);
		txt_PER12.setReadonly(true);
		txt_PER13.setReadonly(true);
		txt_PER14.setReadonly(true);
		txt_PER15.setReadonly(true);
		txt_PER16.setReadonly(true);
		txt_PER17.setReadonly(true);
		txt_PER18.setReadonly(true);
		txt_PER19.setReadonly(true);
		txt_PER20.setReadonly(true);
		txt_PER21.setReadonly(true);
		txt_PER22.setReadonly(true);
		txt_PER23.setReadonly(true);
		txt_PER24.setReadonly(true);
		txt_PER25.setReadonly(true);
		txt_PER26.setReadonly(true);
		txt_PER27.setReadonly(true);
		txt_PER28.setReadonly(true);
		txt_PER29.setReadonly(true);
		txt_PER30.setReadonly(true);
		txt_PER31.setReadonly(true);
		txt_PER32.setReadonly(true);
		txt_PER33.setReadonly(true);
		txt_PER34.setReadonly(true);
		txt_PER35.setReadonly(true);
		txt_PER36.setReadonly(true);
		txt_PER37.setReadonly(true);
		txt_PER38.setReadonly(true);
		txt_PER39.setReadonly(true);
		txt_PER40.setReadonly(true);
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
		cboqrySH_LAST.setDisabled(true);
		/*編輯區*/
		txt_MODEL_NA.setReadonly(false);
		ontxtDSID04.setDisabled(false);
		cboSH_LAST.setDisabled(false);
		txt_EL_NO.setReadonly(false);
		ontxtDSID04ELNO.setDisabled(false);
		txt_MAX_DAY.setReadonly(false);
		txt_MIN_DAY.setReadonly(false);
		txt_SIZE1.setReadonly(false);
		txt_SIZE2.setReadonly(false);
		txt_SIZE3.setReadonly(false);
		txt_SIZE4.setReadonly(false);
		txt_SIZE5.setReadonly(false);
		txt_SIZE6.setReadonly(false);
		txt_SIZE7.setReadonly(false);
		txt_SIZE8.setReadonly(false);
		txt_SIZE9.setReadonly(false);
		txt_SIZE10.setReadonly(false);
		txt_SIZE11.setReadonly(false);
		txt_SIZE12.setReadonly(false);
		txt_SIZE13.setReadonly(false);
		txt_SIZE14.setReadonly(false);
		txt_SIZE15.setReadonly(false);
		txt_SIZE16.setReadonly(false);
		txt_SIZE17.setReadonly(false);
		txt_SIZE18.setReadonly(false);
		txt_SIZE19.setReadonly(false);
		txt_SIZE20.setReadonly(false);
		txt_SIZE21.setReadonly(false);
		txt_SIZE22.setReadonly(false);
		txt_SIZE23.setReadonly(false);
		txt_SIZE24.setReadonly(false);
		txt_SIZE25.setReadonly(false);
		txt_SIZE26.setReadonly(false);
		txt_SIZE27.setReadonly(false);
		txt_SIZE28.setReadonly(false);
		txt_SIZE29.setReadonly(false);
		txt_SIZE30.setReadonly(false);
		txt_SIZE31.setReadonly(false);
		txt_SIZE32.setReadonly(false);
		txt_SIZE33.setReadonly(false);
		txt_SIZE34.setReadonly(false);
		txt_SIZE35.setReadonly(false);
		txt_SIZE36.setReadonly(false);
		txt_SIZE37.setReadonly(false);
		txt_SIZE38.setReadonly(false);
		txt_SIZE39.setReadonly(false);
		txt_SIZE40.setReadonly(false);
		txt_PER1.setReadonly(false);
		txt_PER2.setReadonly(false);
		txt_PER3.setReadonly(false);
		txt_PER4.setReadonly(false);
		txt_PER5.setReadonly(false);
		txt_PER6.setReadonly(false);
		txt_PER7.setReadonly(false);
		txt_PER8.setReadonly(false);
		txt_PER9.setReadonly(false);
		txt_PER10.setReadonly(false);
		txt_PER11.setReadonly(false);
		txt_PER12.setReadonly(false);
		txt_PER13.setReadonly(false);
		txt_PER14.setReadonly(false);
		txt_PER15.setReadonly(false);
		txt_PER16.setReadonly(false);
		txt_PER17.setReadonly(false);
		txt_PER18.setReadonly(false);
		txt_PER19.setReadonly(false);
		txt_PER20.setReadonly(false);
		txt_PER21.setReadonly(false);
		txt_PER22.setReadonly(false);
		txt_PER23.setReadonly(false);
		txt_PER24.setReadonly(false);
		txt_PER25.setReadonly(false);
		txt_PER26.setReadonly(false);
		txt_PER27.setReadonly(false);
		txt_PER28.setReadonly(false);
		txt_PER29.setReadonly(false);
		txt_PER30.setReadonly(false);
		txt_PER31.setReadonly(false);
		txt_PER32.setReadonly(false);
		txt_PER33.setReadonly(false);
		txt_PER34.setReadonly(false);
		txt_PER35.setReadonly(false);
		txt_PER36.setReadonly(false);
		txt_PER37.setReadonly(false);
		txt_PER38.setReadonly(false);
		txt_PER39.setReadonly(false);
		txt_PER40.setReadonly(false);

	}
	
	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		
		return true;
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
		txt_MODEL_NA.setText("");
		cboSH_LAST.setSelectedIndex(0);
		txt_EL_NO.setText("");
		txt_MAX_DAY.setText("");
		txt_MIN_DAY.setText("");
		txt_SIZE1.setText("1");
		txt_SIZE2.setText("1.5");
		txt_SIZE3.setText("2");
		txt_SIZE4.setText("2.5");
		txt_SIZE5.setText("3");
		txt_SIZE6.setText("3.5");
		txt_SIZE7.setText("4");
		txt_SIZE8.setText("4.5");
		txt_SIZE9.setText("5");
		txt_SIZE10.setText("5.5");
		txt_SIZE11.setText("6");
		txt_SIZE12.setText("6.5");
		txt_SIZE13.setText("7");
		txt_SIZE14.setText("7.5");
		txt_SIZE15.setText("8");
		txt_SIZE16.setText("8.5");
		txt_SIZE17.setText("9");
		txt_SIZE18.setText("9.5");
		txt_SIZE19.setText("10");
		txt_SIZE20.setText("10.5");
		txt_SIZE21.setText("11");
		txt_SIZE22.setText("11.5");
		txt_SIZE23.setText("12");
		txt_SIZE24.setText("12.5");
		txt_SIZE25.setText("13");
		txt_SIZE26.setText("13.5");
		txt_SIZE27.setText("14");
		txt_SIZE28.setText("14.5");
		txt_SIZE29.setText("15");
		txt_SIZE30.setText("15.5");
		txt_SIZE31.setText("16");
		txt_SIZE32.setText("16.5");
		txt_SIZE33.setText("17");
		txt_SIZE34.setText("17.5");
		txt_SIZE35.setText("18");
		txt_SIZE36.setText("18.5");
		txt_SIZE37.setText("19");
		txt_SIZE38.setText("19.5");
		txt_SIZE39.setText("20");
		txt_SIZE40.setText("20.5");
		txt_PER1.setText("0");
		txt_PER2.setText("0");
		txt_PER3.setText("0");
		txt_PER4.setText("0");
		txt_PER5.setText("0");
		txt_PER6.setText("0");
		txt_PER7.setText("0");
		txt_PER8.setText("0");
		txt_PER9.setText("0");
		txt_PER10.setText("0");
		txt_PER11.setText("0");
		txt_PER12.setText("0");
		txt_PER13.setText("0");
		txt_PER14.setText("0");
		txt_PER15.setText("0");
		txt_PER16.setText("0");
		txt_PER17.setText("0");
		txt_PER18.setText("0");
		txt_PER19.setText("0");
		txt_PER20.setText("0");
		txt_PER21.setText("0");
		txt_PER22.setText("0");
		txt_PER23.setText("0");
		txt_PER24.setText("0");
		txt_PER25.setText("0");
		txt_PER26.setText("0");
		txt_PER27.setText("0");
		txt_PER28.setText("0");
		txt_PER29.setText("0");
		txt_PER30.setText("0");
		txt_PER31.setText("0");
		txt_PER32.setText("0");
		txt_PER33.setText("0");
		txt_PER34.setText("0");
		txt_PER35.setText("0");
		txt_PER36.setText("0");
		txt_PER37.setText("0");
		txt_PER38.setText("0");
		txt_PER39.setText("0");
		txt_PER40.setText("0");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void masterSave(Connection conn) throws Exception{
		DataMode datemode = getData_Mode();
		super.masterSave(conn);
		CallableStatement cs = null;
		Connection conn1=null;
		String  tMODEL_NA=txt_MODEL_NA.getText(),
				tSH_LAST="",
				tEL_NO=txt_EL_NO.getText();
		if(cboSH_LAST.getSelectedIndex()==0){
			tSH_LAST="M";
		}else{
			tSH_LAST="W";
		}
		try {
			conn1 = Common.getDbConnection();
			if(datemode.equals(DataMode.CREATE_MODE)){
				/*設定 CallableStatement */
			    cs  = conn1.prepareCall("{call IORUDSID04_SIZE(?,?,?)}"); 
			    /*設定 IN 參數的 Index 及值*/
			    cs.setString(1, tMODEL_NA);
			    cs.setString(2, tSH_LAST);
			    cs.setString(3, tEL_NO);
			    cs.execute();
			    cs.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}finally{
			Common.closeConnection(conn1);
			executeQuery();
		}
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
	
	@Listen("onOpenQueryField = #windowMaster")
	public void onOpenQueryField(ForwardEvent event) {
		 final HashMap<String, Object> map = new HashMap<String, Object>();

         map.put("parentWindow", windowMaster);
         map.put("WindoWidth", "60%");    //開窗長度
         map.put("WindoHeight", "80%");   //開窗高度
         map.put("Sizable", true);                //是否可手動調整大小
         map.put("Closable", true);              //是否可關閉視窗
         map.put("Maximizable",true);       //是否可最大化視窗
         map.put("multiple", false);    // false:單選,true:多選目前不支援共用
         
         
         if(event.getData().equals("onqryDSID04")){
        	 map.put("xmlQryID","dsid/DSID04");   //XML ID
        	 ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
        	 returnBoxList.add(new ReturnBox("MODEL_NA",qry_MODEL_NA));
        	 map.put("returnTextBoxList", returnBoxList);
         }else if(event.getData().equals("ontxtDSID04")){
        	 map.put("xmlQryID","dsid/DSID04");   //XML ID
        	 ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
             returnBoxList.add(new ReturnBox("MODEL_NA",txt_MODEL_NA));
             map.put("returnTextBoxList", returnBoxList);	 
         }else if(event.getData().equals("ontxtDSID04ELNO")){
        	 map.put("xmlQryID","dsid/DSID_ELNO");   //XML ID
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
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void getValueFromXls(HSSFSheet sheet, HSSFDataFormat dateFormat) throws SQLException {
		PreparedStatement ps = null;
		Connection conn = Common.getDbConnection();
		conn.setAutoCommit(false);
		String 	tMODEL_NA,tSH_LAST,tEL_NO,
				tS[] = new String[40],tQ[] = new String[40],tMAX_DAY,tMIN_DAY;
		
		for(int i=0;i<40;i++){
			tS[i]="";
			tQ[i]="";
		}
		
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
		StringBuilder sb = new StringBuilder();
		HSSFRow row =null;
		HSSFCell cell = null;
		String sql1="", sql2="";
		try{	
		conn.setAutoCommit(false);
		for (int i = 1; i <= TotalRows; i++) {
			sql1="INSERT INTO DSID04_SIZE (MODEL_NA,SH_LAST,EL_NO,MAX_DAY,MIN_DAY,S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,S11,S12,S13,S14,S15,S16,S17,S18,S19,S20,S21,S22,S23,S24,S25,S26,S27,S28,S29,S30,S31,S32,S33,S34,S35,S36,S37,S38,S39,S40,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,Q16,Q17,Q18,Q19,Q20,Q21,Q22,Q23,Q24,Q25,Q26,Q27,Q28,Q29,Q30,Q31,Q32,Q33,Q34,Q35,Q36,Q37,Q38,Q39,Q40,UP_USER,UP_DATE) VALUES (";
			sql2="UPDATE DSID04_SIZE SET ";
			
			int j=0;
			Object objvalue ="";
			row= sheet.getRow(i);
			
			cell = row.getCell((short)j);
			objvalue = TableToExcel.getCellValue(cell, dateFormat);
			tMODEL_NA = getCellValue(objvalue,cell);//型體
			if(tMODEL_NA.equals("")){
				continue;
			}else{
				sql1 += "'" + tMODEL_NA + "',";
				sql2 += "MODEL_NA='" + tMODEL_NA + "',";
			}
			
			j++;
			cell = row.getCell((short)j);
			objvalue = TableToExcel.getCellValue(cell, dateFormat);
			tSH_LAST = getCellValue(objvalue,cell);//男/女
			if(tSH_LAST.equals("")){
				continue;
			}else{
				sql1 += "'"+tSH_LAST+"',";
				sql2 += "SH_LAST='"+tSH_LAST+"',";
			}
			j++;
			cell = row.getCell((short)j);
			objvalue = TableToExcel.getCellValue(cell, dateFormat);
			tEL_NO = getCellValue(objvalue,cell);//材料編號
			if(tSH_LAST.equals("")){
				continue;
			}else{
				sql1 += "'"+tEL_NO+"',";
				sql2 += "EL_NO='"+tEL_NO+"',";
			}
			
			j++;
			cell = row.getCell((short)j);
			objvalue = TableToExcel.getCellValue(cell, dateFormat);
			tMAX_DAY = getCellValue(objvalue,cell);//最大天數
			if(tMAX_DAY.equals("")){
				continue;
			}else{
				sql1 += "'"+tMAX_DAY+"',";
				sql2 += "MAX_DAY='"+tMAX_DAY+"',";
			}
			
			j++;
			cell = row.getCell((short)j);
			objvalue = TableToExcel.getCellValue(cell, dateFormat);
			tMIN_DAY = getCellValue(objvalue,cell);//最小天數
			if(tMIN_DAY.equals("")){
				continue;
			}else{
				sql1 += "'"+tMIN_DAY+"',";
				sql2 += "MIN_DAY='"+tMIN_DAY+"',";
			}
			
			for(int k=0;k<40;k++){
				j++;
				cell = row.getCell((short)j);
			    objvalue = TableToExcel.getCellValue(cell, dateFormat);
			    tS[k] = getCellValue(objvalue,cell);//SIZE
			    sql1 += "'" + tS[k] + "',";
				sql2 += "S"+(k+1)+"='" + tS[k] + "',";
			    
			}
			
			for(int k=0;k<40;k++){
				j++;
				cell = row.getCell((short)j);
			    objvalue = TableToExcel.getCellValue(cell, dateFormat);
			    tQ[k] = getCellValue(objvalue,cell);//Percentage
			    if(tQ[k].equals("")){
			    	tQ[k] = "0";
			    }
			    sql1 += tQ[k] + ",";
			    sql2 += "Q"+(k+1)+"="+ tQ[k] + ",";
			}
		    
		    try {		    	
		    	final Query q=CRUDService.createSQL("select * from DSID04_SIZE  where MODEL_NA='" + tMODEL_NA + "' and SH_LAST='" + tSH_LAST + "'");
		    	List list = q.getResultList(); 
		    	CallableStatement cs = null;
		    	if(list.size()>0){
			    	sql2+="UP_USER='"+_userInfo.getAccount()+"',UP_DATE=SYSDATE WHERE MODEL_NA='" + tMODEL_NA + "' and SH_LAST='" + tSH_LAST + "'";
			    	ps = conn.prepareStatement(sql2,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			    	ps.execute();
			    	ps.close();
		    	}else{
		    		sql1+="'"+_userInfo.getAccount()+"',SYSDATE)";
		    		ps = conn.prepareStatement(sql1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			    	ps.execute();
			    	ps.close();

			    	Query q1=null;
			    	if(tSH_LAST.equals("M")){
			    		q1=CRUDService.createSQL("select * from DSID04_SIZE  where MODEL_NA='" + tMODEL_NA + "' and SH_LAST='W'");
			    	}else{
			    		q1=CRUDService.createSQL("select * from DSID04_SIZE  where MODEL_NA='" + tMODEL_NA + "' and SH_LAST='M'");
			    	}
			    	
			    	List list1 = q1.getResultList();
			    	if(list.size()<=0){
				    /*設定 CallableStatement */
					    cs  = conn.prepareCall("{call IORUDSID04_SIZE(?,?,?)}"); 
					    /*設定 IN 參數的 Index 及值*/
					    cs.setString(1, tMODEL_NA);
					    cs.setString(2, tSH_LAST);
					    cs.setString(2, tEL_NO);
					    cs.execute();
					    cs.close();
			    	}
		    	}
			}catch(Exception e){
				e.printStackTrace();
			}
		}}catch(Exception e){
				e.printStackTrace();
				conn.rollback();
		}finally{
			Messagebox.show("Success!!");
			executeQuery();
			conn.commit();
			Common.closeConnection(conn);
		}		
		
	}
}
