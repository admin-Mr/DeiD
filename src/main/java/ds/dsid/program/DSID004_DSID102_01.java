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
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID102;
import util.Common;
import util.ComponentColumn;
import util.Master;
import util.OperationMode;
import util.function.TableToExcel;
import util.openwin.ReturnBox;

public class DSID004_DSID102_01 extends Master {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window windowMaster;
	@Wire
	private Listbox queryListbox;
	@Wire
	private Button btnQuery, btnCreateMaster, btnSaveMaster, btnCancelMaster, btnImportMaster;
	@Wire
	private Button onqryEL_NO, ontxtEL_NO;
	@Wire
	private Textbox qry_EL_NO, qry_EL_NA;
	@Wire
	private Textbox txt_EL_NO, txt_EL_NA;
	@Wire
	private Label lb_SIZE1,lb_SIZE2,lb_SIZE3,lb_SIZE4,lb_SIZE5,lb_SIZE6,lb_SIZE7,lb_SIZE8,lb_SIZE9,lb_SIZE10,
					lb_SIZE11,lb_SIZE12,lb_SIZE13,lb_SIZE14,lb_SIZE15,lb_SIZE16,lb_SIZE17,lb_SIZE18,lb_SIZE19,lb_SIZE20,
					lb_SIZE21,lb_SIZE22,lb_SIZE23,lb_SIZE24,lb_SIZE25,lb_SIZE26,lb_SIZE27,lb_SIZE28,lb_SIZE29,lb_SIZE30,
					lb_SIZE31,lb_SIZE32,lb_SIZE33,lb_SIZE34,lb_SIZE35,lb_SIZE36,lb_SIZE37,lb_SIZE38,lb_SIZE39,lb_SIZE40;
	@Wire
	private Doublebox txt_QTY1,txt_QTY2,txt_QTY3,txt_QTY4,txt_QTY5,txt_QTY6,txt_QTY7,txt_QTY8,txt_QTY9,txt_QTY10,
					txt_QTY11,txt_QTY12,txt_QTY13,txt_QTY14,txt_QTY15,txt_QTY16,txt_QTY17,txt_QTY18,txt_QTY19,txt_QTY20,
					txt_QTY21,txt_QTY22,txt_QTY23,txt_QTY24,txt_QTY25,txt_QTY26,txt_QTY27,txt_QTY28,txt_QTY29,txt_QTY30,
					txt_QTY31,txt_QTY32,txt_QTY33,txt_QTY34,txt_QTY35,txt_QTY36,txt_QTY37,txt_QTY38,txt_QTY39,txt_QTY40;
	
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NO, "EL_NO", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txt_EL_NA, "EL_NA", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE1,  "S1",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE2,  "S2",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE3,  "S3",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE4,  "S4",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE5,  "S5",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE6,  "S6",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE7,  "S7",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE8,  "S8",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE9,  "S9",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE10, "S10", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE11, "S11", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE12, "S12", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE13, "S13", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE14, "S14", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE15, "S15", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE16, "S16", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE17, "S17", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE18, "S18", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE19, "S19", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE20, "S20", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE21, "S21", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE22, "S22", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE23, "S23", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE24, "S24", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE25, "S25", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE26, "S26", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE27, "S27", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE28, "S28", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE29, "S29", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE30, "S30", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE31, "S31", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE32, "S32", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE33, "S33", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE34, "S34", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE35, "S35", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE36, "S36", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE37, "S37", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE38, "S38", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE39, "S39", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(lb_SIZE40, "S40", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY1,  "Q1",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY2,  "Q2",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY3,  "Q3",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY4,  "Q4",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY5,  "Q5",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY6,  "Q6",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY7,  "Q7",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY8,  "Q8",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY9,  "Q9",  null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY10, "Q10", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY11, "Q11", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY12, "Q12", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY13, "Q13", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY14, "Q14", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY15, "Q15", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY16, "Q16", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY17, "Q17", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY18, "Q18", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY19, "Q19", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY20, "Q20", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY21, "Q21", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY22, "Q22", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY23, "Q23", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY24, "Q24", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY25, "Q25", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY26, "Q26", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY27, "Q27", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY28, "Q28", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY29, "Q29", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY30, "Q30", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY31, "Q31", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY32, "Q32", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY33, "Q33", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY34, "Q34", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY35, "Q35", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY36, "Q36", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY37, "Q37", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY38, "Q38", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY39, "Q39", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Double>(txt_QTY40, "Q40", null, null, null));
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
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub
		return DSID004_DSID102_01.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return DSID102.class;
	}
	
	@Override
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}
	
	@Override
	protected ArrayList<String> getMasterKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("EL_NO");
		return masterKey;
	}
	
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSID102 entity = (DSID102) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getEL_NO());
		return masterKeyValue;
	}
	
	@Override
	protected String getPagingId() {
		return "pagingCourse";
	}
	
	@Override
	protected String getWhereConditionals() {
		String strSQL = "";
		if(!qry_EL_NO.getText().equals("")){
			strSQL += " AND t.EL_NO='" + qry_EL_NO.getText() + "' ";
		}
		if(!qry_EL_NA.getText().equals("")){
			strSQL += " AND t.EL_NA LIKE '%" + qry_EL_NA.getText() + "%' ";
		}
		return strSQL;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
		DSID102 entity = (DSID102) entityMaster;
		if(entity == null){
			return;
		}
		txt_EL_NO.setValue((entity.getEL_NO() == null ? "" : entity.getEL_NO()));
		txt_EL_NA.setValue((entity.getEL_NA() == null ? "" : entity.getEL_NA()));
		txt_QTY1.setValue((entity.getQ1() == null ? 0 : entity.getQ1()));
		txt_QTY2.setValue((entity.getQ2() == null ? 0 : entity.getQ2()));
		txt_QTY3.setValue((entity.getQ3() == null ? 0 : entity.getQ3()));
		txt_QTY4.setValue((entity.getQ4() == null ? 0 : entity.getQ4()));
		txt_QTY5.setValue((entity.getQ5() == null ? 0 : entity.getQ5()));
		txt_QTY6.setValue((entity.getQ6() == null ? 0 : entity.getQ6()));
		txt_QTY7.setValue((entity.getQ7() == null ? 0 : entity.getQ7()));
		txt_QTY8.setValue((entity.getQ8() == null ? 0 : entity.getQ8()));
		txt_QTY9.setValue((entity.getQ9() == null ? 0 : entity.getQ9()));
		txt_QTY10.setValue((entity.getQ10() == null ? 0 : entity.getQ10()));
		txt_QTY11.setValue((entity.getQ11() == null ? 0 : entity.getQ11()));
		txt_QTY12.setValue((entity.getQ12() == null ? 0 : entity.getQ12()));
		txt_QTY13.setValue((entity.getQ13() == null ? 0 : entity.getQ13()));
		txt_QTY14.setValue((entity.getQ14() == null ? 0 : entity.getQ14()));
		txt_QTY15.setValue((entity.getQ15() == null ? 0 : entity.getQ15()));
		txt_QTY16.setValue((entity.getQ16() == null ? 0 : entity.getQ16()));
		txt_QTY17.setValue((entity.getQ17() == null ? 0 : entity.getQ17()));
		txt_QTY18.setValue((entity.getQ18() == null ? 0 : entity.getQ18()));
		txt_QTY19.setValue((entity.getQ19() == null ? 0 : entity.getQ19()));
		txt_QTY20.setValue((entity.getQ20() == null ? 0 : entity.getQ20()));
		txt_QTY21.setValue((entity.getQ21() == null ? 0 : entity.getQ21()));
		txt_QTY22.setValue((entity.getQ22() == null ? 0 : entity.getQ22()));
		txt_QTY23.setValue((entity.getQ23() == null ? 0 : entity.getQ23()));
		txt_QTY24.setValue((entity.getQ24() == null ? 0 : entity.getQ24()));
		txt_QTY25.setValue((entity.getQ25() == null ? 0 : entity.getQ25()));
		txt_QTY26.setValue((entity.getQ26() == null ? 0 : entity.getQ26()));
		txt_QTY27.setValue((entity.getQ27() == null ? 0 : entity.getQ27()));
		txt_QTY28.setValue((entity.getQ28() == null ? 0 : entity.getQ28()));
		txt_QTY29.setValue((entity.getQ29() == null ? 0 : entity.getQ29()));
		txt_QTY30.setValue((entity.getQ30() == null ? 0 : entity.getQ30()));
		txt_QTY31.setValue((entity.getQ31() == null ? 0 : entity.getQ31()));
		txt_QTY32.setValue((entity.getQ32() == null ? 0 : entity.getQ32()));
		txt_QTY33.setValue((entity.getQ33() == null ? 0 : entity.getQ33()));
		txt_QTY34.setValue((entity.getQ34() == null ? 0 : entity.getQ34()));
		txt_QTY35.setValue((entity.getQ35() == null ? 0 : entity.getQ35()));
		txt_QTY36.setValue((entity.getQ36() == null ? 0 : entity.getQ36()));
		txt_QTY37.setValue((entity.getQ37() == null ? 0 : entity.getQ37()));
		txt_QTY38.setValue((entity.getQ38() == null ? 0 : entity.getQ38()));
		txt_QTY39.setValue((entity.getQ39() == null ? 0 : entity.getQ39()));
		txt_QTY40.setValue((entity.getQ40() == null ? 0 : entity.getQ40()));
		
	}
	
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btnQuery", btnQuery);
		mapButton.put("btnCreateMaster", btnCreateMaster);
		mapButton.put("btnSaveMaster", btnSaveMaster);
		mapButton.put("btnCancelMaster", btnCancelMaster);
		super.masterReadMode(mapButton);
		/*按鈕*/
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		/*過濾區*/
		qry_EL_NO.setReadonly(false);
		qry_EL_NA.setReadonly(false);
		onqryEL_NO.setDisabled(false);
		/*編輯區*/
		txt_EL_NO.setReadonly(true);
		txt_EL_NA.setReadonly(true);
		txt_QTY1.setReadonly(true);
		txt_QTY2.setReadonly(true);
		txt_QTY3.setReadonly(true);
		txt_QTY4.setReadonly(true);
		txt_QTY5.setReadonly(true);
		txt_QTY6.setReadonly(true);
		txt_QTY7.setReadonly(true);
		txt_QTY8.setReadonly(true);
		txt_QTY9.setReadonly(true);
		txt_QTY10.setReadonly(true);
		txt_QTY11.setReadonly(true);
		txt_QTY12.setReadonly(true);
		txt_QTY13.setReadonly(true);
		txt_QTY14.setReadonly(true);
		txt_QTY15.setReadonly(true);
		txt_QTY16.setReadonly(true);
		txt_QTY17.setReadonly(true);
		txt_QTY18.setReadonly(true);
		txt_QTY19.setReadonly(true);
		txt_QTY20.setReadonly(true);
		txt_QTY21.setReadonly(true);
		txt_QTY22.setReadonly(true);
		txt_QTY23.setReadonly(true);
		txt_QTY24.setReadonly(true);
		txt_QTY25.setReadonly(true);
		txt_QTY26.setReadonly(true);
		txt_QTY27.setReadonly(true);
		txt_QTY28.setReadonly(true);
		txt_QTY29.setReadonly(true);
		txt_QTY30.setReadonly(true);
		txt_QTY31.setReadonly(true);
		txt_QTY32.setReadonly(true);
		txt_QTY33.setReadonly(true);
		txt_QTY34.setReadonly(true);
		txt_QTY35.setReadonly(true);
		txt_QTY36.setReadonly(true);
		txt_QTY37.setReadonly(true);
		txt_QTY38.setReadonly(true);
		txt_QTY39.setReadonly(true);
		txt_QTY40.setReadonly(true);
		ontxtEL_NO.setDisabled(true);
	}
	
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		mapButton = new HashMap<String, Object>();
		mapButton.put("btnquery", btnQuery);
		mapButton.put("btncreatemaster", btnCreateMaster);
		super.masterCreateMode(mapButton);
		/*按鈕*/
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		/*過濾區*/
		qry_EL_NO.setReadonly(true);
		qry_EL_NA.setReadonly(true);
		onqryEL_NO.setDisabled(true);
		/*編輯區*/
		txt_EL_NO.setReadonly(false);txt_EL_NO.setText("");
		txt_EL_NA.setReadonly(true);txt_EL_NA.setText("");
		txt_QTY1.setReadonly(false);txt_QTY1.setText("0");
		txt_QTY2.setReadonly(false);txt_QTY2.setText("0");
		txt_QTY3.setReadonly(false);txt_QTY3.setText("0");
		txt_QTY4.setReadonly(false);txt_QTY4.setText("0");
		txt_QTY5.setReadonly(false);txt_QTY5.setText("0");
		txt_QTY6.setReadonly(false);txt_QTY6.setText("0");
		txt_QTY7.setReadonly(false);txt_QTY7.setText("0");
		txt_QTY8.setReadonly(false);txt_QTY8.setText("0");
		txt_QTY9.setReadonly(false);txt_QTY9.setText("0");
		txt_QTY10.setReadonly(false);txt_QTY10.setText("0");
		txt_QTY11.setReadonly(false);txt_QTY11.setText("0");
		txt_QTY12.setReadonly(false);txt_QTY12.setText("0");
		txt_QTY13.setReadonly(false);txt_QTY13.setText("0");
		txt_QTY14.setReadonly(false);txt_QTY14.setText("0");
		txt_QTY15.setReadonly(false);txt_QTY15.setText("0");
		txt_QTY16.setReadonly(false);txt_QTY16.setText("0");
		txt_QTY17.setReadonly(false);txt_QTY17.setText("0");
		txt_QTY18.setReadonly(false);txt_QTY18.setText("0");
		txt_QTY19.setReadonly(false);txt_QTY19.setText("0");
		txt_QTY20.setReadonly(false);txt_QTY20.setText("0");
		txt_QTY21.setReadonly(false);txt_QTY21.setText("0");
		txt_QTY22.setReadonly(false);txt_QTY22.setText("0");
		txt_QTY23.setReadonly(false);txt_QTY23.setText("0");
		txt_QTY24.setReadonly(false);txt_QTY24.setText("0");
		txt_QTY25.setReadonly(false);txt_QTY25.setText("0");
		txt_QTY26.setReadonly(false);txt_QTY26.setText("0");
		txt_QTY27.setReadonly(false);txt_QTY27.setText("0");
		txt_QTY28.setReadonly(false);txt_QTY28.setText("0");
		txt_QTY29.setReadonly(false);txt_QTY29.setText("0");
		txt_QTY30.setReadonly(false);txt_QTY30.setText("0");
		txt_QTY31.setReadonly(false);txt_QTY31.setText("0");
		txt_QTY32.setReadonly(false);txt_QTY32.setText("0");
		txt_QTY33.setReadonly(false);txt_QTY33.setText("0");
		txt_QTY34.setReadonly(false);txt_QTY34.setText("0");
		txt_QTY35.setReadonly(false);txt_QTY35.setText("0");
		txt_QTY36.setReadonly(false);txt_QTY36.setText("0");
		txt_QTY37.setReadonly(false);txt_QTY37.setText("0");
		txt_QTY38.setReadonly(false);txt_QTY38.setText("0");
		txt_QTY39.setReadonly(false);txt_QTY39.setText("0");
		txt_QTY40.setReadonly(false);txt_QTY40.setText("0");
		ontxtEL_NO.setDisabled(false);
	}

	@Override
	@Listen("onUpdate = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void editMasterListbox(ForwardEvent evt) throws Exception {
		super.editMasterListbox(evt);
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
         
         
         if(event.getData().equals("onqryEL_NO")){
        	 map.put("xmlQryID","dsid/DSID102");   //XML ID
        	 ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
        	 returnBoxList.add(new ReturnBox("EL_NO",qry_EL_NO));
        	 returnBoxList.add(new ReturnBox("EL_NA",qry_EL_NA));
        	 map.put("returnTextBoxList", returnBoxList);
         }else if(event.getData().equals("ontxtEL_NO")){
        	 map.put("xmlQryID","dsid/DSID102");   //XML ID
        	 ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
             returnBoxList.add(new ReturnBox("EL_NO",txt_EL_NO));
             returnBoxList.add(new ReturnBox("EL_NA",txt_EL_NA));
             map.put("returnTextBoxList", returnBoxList);	 
         }
         
         Executions.createComponents("/util/openwin/QueryField.zul", null, map);
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
	
	/**
	 * @param sheet
	 * @param dateFormat
	 * @throws SQLException
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void getValueFromXls(HSSFSheet sheet, HSSFDataFormat dateFormat) throws SQLException {
		PreparedStatement ps = null;
		Connection conn = null;
		String vEL_NO="",vEL_NA="";
		String vSIZE[] = new String[40],vQTY[] = new String[40];
		for(int i=0;i<40;i++){
			vSIZE[i]="";
			vQTY[i]="";
		}
		/***** 取得匯入Excel的總筆數 *****/
		int TotalRows=0;
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {				
	    	HSSFRow row = sheet.getRow(i);
    		if (row==null)break;
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
			conn.setAutoCommit(false);//取消自動Commit，待全部成功後在進行Commit
			for (int RowIndex = 1; RowIndex <= TotalRows; RowIndex++) {
				int CellIndex=0;
				row = sheet.getRow(RowIndex);
				/***** 取得EL_NO資料 Start *****/
				vEL_NO = String.valueOf(getCellValues(row, CellIndex, dateFormat));//材料編號
				if(vEL_NO.equals(""))throw new Exception("Row"+(RowIndex+1)+"：EL_NO Is Null.Please Check Data Again.");
				/***** 取得EL_NO資料 End *****/
				/***** 取得EL_CNAME資料 Start *****/
				final Query qEL_NA=CRUDService.createSQL("SELECT EL_CNAME FROM DSEL00 WHERE EL_NO='" + vEL_NO + "'");
		    	List lEL_NA = qEL_NA.getResultList();
		    	if(lEL_NA.size()>0){
		    		vEL_NA=lEL_NA.get(0).toString();//材料名稱
		    	}
				/***** 取得EL_CNAME資料 End *****/
				/***** 取得SIZE資料 Start *****/
				for(int i=0;i<40;i++){
					CellIndex++;
					vSIZE[i] = getCellValues(row, CellIndex, dateFormat).toString();//SIZE
				}
				/***** 取得SIZE資料 End *****/
				/***** 取得QTY資料 Start *****/
				RowIndex++;
				CellIndex=0;
				row = sheet.getRow(RowIndex);
				for(int i=0;i<40;i++){
					CellIndex++;
					vQTY[i] = getCellValues(row, CellIndex, dateFormat).toString();//QTY
				}
				/***** 取得QTY資料 End *****/
				/***** 判斷是否已有資料 Start *****/
				/***** 若有資料則「UPDATE」,若沒有資料則「INSERT」*****/
				final Query q=CRUDService.createSQL("SELECT * FROM DSID102 WHERE EL_NO='" + vEL_NO + "'");
		    	List list = q.getResultList(); 
		    	/***** 有資料則「UPDATE」 Start *****/
		    	if(list.size()>0){
		    		String UPDATE_SQL=	"UPDATE DSID102 SET EL_NA=?,S1=?,S2=?,S3=?,S4=?,S5=?,S6=?,S7=?,S8=?,S9=?,S10=?," +
		    							"							   S11=?,S12=?,S13=?,S14=?,S15=?,S16=?,S17=?,S18=?,S19=?,S20=?," +
		    							"							   S21=?,S22=?,S23=?,S24=?,S25=?,S26=?,S27=?,S28=?,S29=?,S30=?," +
		    							"							   S31=?,S32=?,S33=?,S34=?,S35=?,S36=?,S37=?,S38=?,S39=?,S40=?," +
		    							"							   Q1=?,Q2=?,Q3=?,Q4=?,Q5=?,Q6=?,Q7=?,Q8=?,Q9=?,Q10=?," +
		    							"							   Q11=?,Q12=?,Q13=?,Q14=?,Q15=?,Q16=?,Q17=?,Q18=?,Q19=?,Q20=?," +
		    							"							   Q21=?,Q22=?,Q23=?,Q24=?,Q25=?,Q26=?,Q27=?,Q28=?,Q29=?,Q30=?," +
		    							"							   Q31=?,Q32=?,Q33=?,Q34=?,Q35=?,Q36=?,Q37=?,Q38=?,Q39=?,Q40=?," +
		    							"					UP_USER=?,UP_DATE=SYSDATE WHERE EL_NO=?";
		    		ps = conn.prepareStatement(UPDATE_SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ps.setString(1, vEL_NA);
					for(int index=0;index<40;index++){
						ps.setString(2+index , vSIZE[index]);
						ps.setString(42+index, vQTY[index]);
					}
					ps.setString(82, _userInfo.getAccount());
					ps.setString(83, vEL_NO);
			    	ps.execute();
			    	ps.close();
		    	/***** 有資料則「UPDATE」 End *****/
		    	/***** 沒資料則「INSERT」 Start *****/
		    	}else{
		    		String INSERT_SQL="INSERT INTO DSID102 SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,? FROM DUAL";
		    		ps = conn.prepareStatement(INSERT_SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		    		ps.setString(1, vEL_NO);
		    		for(int index=0;index<40;index++){
						ps.setString(2+index , vSIZE[index]);
						ps.setString(42+index, vQTY[index]);
					}
		    		ps.setString(82, _userInfo.getAccount());
		    		ps.setString(83, vEL_NA);
		    		ps.execute();
			    	ps.close();
		    	}
		    	/***** 沒資料則「INSERT」 End *****/
		    	/***** 判斷是否已有資料 End *****/
			}
			/***** 匯入成功將進行Commit，並顯示「Import Data Success!!」 *****/
			conn.commit();
			Messagebox.show("Import Data Success!!");
		} catch (Exception e) {
			// TODO: handle exception
			/***** 匯入不成功將進行Rollback *****/
			e.getStackTrace();
			conn.rollback();
		}finally{
			/***** 不管匯入成不成功都需要重新查詢，並將「Connection」關閉 *****/
			executeQuery();
			Common.closeConnection(conn);
		}
	}
	/***** 取得欄位資料 *****/
	/**
	 * @param row 行數
	 * @param CellIndex 欄位Index
	 * @param dateFormat 日期格式
	 * @return 欄位值(Object格式)
	 */
	private Object getCellValues(HSSFRow row,int CellIndex,HSSFDataFormat dateFormat){
		Object CellValues="";
		HSSFCell cell = row.getCell((short)CellIndex);
		Object obj = TableToExcel.getCellValue(cell, dateFormat);
		CellValues = (Object) getCellValue(obj,cell);
		return CellValues;
	}
	/***** 轉換欄位資料格式 *****/
	/**
	 * @param objvalue 欄位值
	 * @param cell	欄位格式
	 * @return 欄位值
	 */
	private String getCellValue(Object objvalue,HSSFCell cell){
		String value="";
		if(objvalue!=null){
			if(objvalue instanceof Date){//格式：日期
        	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        	    value=sdf.format(objvalue);
        	}else{
        		switch (cell.getCellType()) {//取得Cell欄位格式
		    	case HSSFCell.CELL_TYPE_NUMERIC://格式：數字(Double)
					DecimalFormat decimalFormat = new DecimalFormat("####.####");
					value =(decimalFormat.format(cell.getNumericCellValue()));
					break;
				case HSSFCell.CELL_TYPE_STRING://格式：文字
					value=cell.getStringCellValue();
					break;
				default://格式定義：文字
					value=cell.getStringCellValue();
					break;
				}
        	}
		}else{//若為Null，則改為空值
			value="";
		}
		return value;
	}

}
