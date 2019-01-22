package ds.dsid.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.web.context.request.SessionScope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID02;
import ds.dsid.domain.DSIDN06;
import ds.dspb.domain.DSPB00_NEW;
import util.ComponentColumn;
import util.OperationMode;

public class DSID06M_Program extends COMM_Master {
	
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox txt_EL_NOSQL,txt_UNIQUEID,txt_EL_NO,txt_EL_MODEL,
					txt_PART_NAME,txt_COLOR,txt_STATUS,txt_createtime;
	@Wire
	private Textbox txt_AREANUM,
					txt_A1,txt_A2,txt_A3,
	                txt_B1,txt_B2,txt_B3,
	                txt_B4,txt_B5,txt_B6,
	                txt_B7,txt_B8,txt_B9,
	                txt_B10,txt_B11,txt_B12,
	                txt_C1,txt_C2,txt_C3,
	                txt_D1,txt_D2,txt_D3,
	                txt_E1,txt_E2,txt_E3,
	                txt_F1,txt_F2,txt_F3,
	                txt_G1,txt_G2,txt_G3,
	                txt_G4,txt_G5,
	                txt_H1,txt_H2,txt_H3,
	                txt_H4,txt_H5;
	@Wire
	private Button btnSaveMaster, btnCancelMaster, btnCreateMaster, btnQuery, btnClear, btnPB_MUNODE,btnImport;
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
//	@Wire
	//private Intbox txtUNIQUEIDa;
	private String uniqueid="";
	/**
	 * 必須複寫方法一doAfterCompose
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		masterComponentColumns.add(new ComponentColumn<String>(txt_AREANUM, "AREANUM",null , null, null));
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_A1, "A1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_A2, "A2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_A3, "A3", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B1, "B1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B2, "B2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B3, "B3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B4, "B4", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B5, "B5", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B6, "B6", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B7, "B7", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B8, "B8", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B9, "B9", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B10, "B10", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B11, "B11", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_B12, "B12", null, null, null));

		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_C1, "C1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_C2, "C2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_C3, "C3", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_D1, "D1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_D2, "D2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_D3, "D3", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_E1, "E1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_E2, "E2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_E3, "E3", null, null, null));
		
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_F1, "F1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_F2, "F2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_F3, "F3", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_G1, "G1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_G2, "G2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_G3, "G3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_G4, "G4", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_G5, "G5", null, null, null));
		
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_H1, "H1", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_H2, "H2", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_H3, "H3", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_H4, "H4", null, null, null));
		masterComponentColumns.add(new ComponentColumn<Integer>(txt_H5, "H5", null, null, null));
		
		
		
		
		
		masterComponentColumns.add(new ComponentColumn<String>(null, "UP_USER", _userInfo.getAccount(), null, null));
		masterComponentColumns.add(new ComponentColumn<Date>(null, "UP_DATE", sdf.parse(sdf.format(new Date())), null, null));	
		masterComponentColumns.add(new ComponentColumn<String>(txt_createtime, "CREATEDAY", null, null, null));

//		System.out.println(Sessions.getCurrent().getAttribute("UAuth")+"=====uauth===");
//		System.out.println(Sessions.getCurrent().getAttribute("DAuth")+"=====dauth===");;
		System.out.println("_userInfo"+_userInfo.getAuth());
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
		return DSID06M_Program.class;
	}

	/**
	 * 必須複寫方法四  getEntityClass
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return DSIDN06.class;
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
		masterKey.add("AREANUM");
		return masterKey;
	}

	/**
	 * 必須複寫方法七 getMasterKeyValue
	 */
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		DSIDN06 entity = (DSIDN06) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getAREANUM());
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
		String strSQL = " and AREANUM is not null ";
		
		if (!txt_EL_NOSQL.getValue().trim().isEmpty()){
			strSQL += " and  AREANUM ='"+txt_EL_NOSQL.getValue()+"'";
		}
		return strSQL;
	}

/**
 * 選擇資料行時作業
 */
	@Override
	protected void resetEditAreaMaster(Object entityMaster) {

//		System.out.println("=========onclick=Editor===========");

		DSIDN06 entity = (DSIDN06) entityMaster;
		System.out.println(entity == null);
		txt_AREANUM.setValue((entity == null ? "" : entity.getAREANUM()));

		txt_A1.setValue((entity == null ? "" : entity.getA1()));
		txt_A2.setValue((entity == null ? "" : entity.getA2()));
		txt_A3.setValue((entity == null ? "" : entity.getA3()));

		txt_B1.setValue((entity == null ? "" : entity.getB1()));
		txt_B2.setValue((entity == null ? "" : entity.getB2()));
		txt_B3.setValue((entity == null ? "" : entity.getB3()));
		txt_B4.setValue((entity == null ? "" : entity.getB4()));
		txt_B5.setValue((entity == null ? "" : entity.getB5()));
		txt_B6.setValue((entity == null ? "" : entity.getB6()));
		txt_B7.setValue((entity == null ? "" : entity.getB7()));
		txt_B8.setValue((entity == null ? "" : entity.getB8()));
		txt_B9.setValue((entity == null ? "" : entity.getB9()));
		txt_B10.setValue((entity == null ? "" : entity.getB10()));
		txt_B11.setValue((entity == null ? "" : entity.getB11()));
		txt_B12.setValue((entity == null ? "" : entity.getB12()));

		txt_C1.setValue((entity == null ? "" : entity.getC1()));
		txt_C2.setValue((entity == null ? "" : entity.getC2()));
		txt_C3.setValue((entity == null ? "" : entity.getC3()));

		txt_D1.setValue((entity == null ? "" : entity.getD1()));
		txt_D2.setValue((entity == null ? "" : entity.getD2()));
		txt_D3.setValue((entity == null ? "" : entity.getD3()));

		txt_E1.setValue((entity == null ? "" : entity.getE1()));
		txt_E2.setValue((entity == null ? "" : entity.getE2()));
		txt_E3.setValue((entity == null ? "" : entity.getE3()));

		txt_F1.setValue((entity == null ? "" : entity.getF1()));
		txt_F2.setValue((entity == null ? "" : entity.getF2()));
		txt_F3.setValue((entity == null ? "" : entity.getF3()));

		txt_G1.setValue((entity == null ? "" : entity.getG1()));
		txt_G2.setValue((entity == null ? "" : entity.getG2()));
		txt_G3.setValue((entity == null ? "" : entity.getG3()));
		txt_G4.setValue((entity == null ? "" : entity.getG4()));
		txt_G5.setValue((entity == null ? "" : entity.getG5()));

		txt_H1.setValue((entity == null ? "" : entity.getH1()));
		txt_H2.setValue((entity == null ? "" : entity.getH2()));
		txt_H3.setValue((entity == null ? "" : entity.getH3()));
		txt_H4.setValue((entity == null ? "" : entity.getH4()));
		txt_H5.setValue((entity == null ? "" : entity.getH5()));

	}

	/**
	 * 新增，編輯保存后時作業
	 */
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){
		
//		System.out.println("========readDate====================");
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);		
		super.masterReadMode(mapButton);
		btnSaveMaster.setDisabled(true);
		btnCancelMaster.setDisabled(true);
		txt_AREANUM.setReadonly(true);
		
		txt_A1.setReadonly(true);
		txt_A2.setReadonly(true);
		txt_A3.setReadonly(true);
		
		txt_B1.setReadonly(true);
		txt_B2.setReadonly(true);
		txt_B3.setReadonly(true);
		txt_B4.setReadonly(true);
		txt_B5.setReadonly(true);
		txt_B6.setReadonly(true);
		txt_B7.setReadonly(true);
		txt_B8.setReadonly(true);
		txt_B9.setReadonly(true);
		txt_B10.setReadonly(true);
		txt_B11.setReadonly(true);
		
		txt_C1.setReadonly(true);
		txt_C2.setReadonly(true);
		txt_C3.setReadonly(true);
		
		txt_D3.setReadonly(true);
		txt_D3.setReadonly(true);
		txt_D3.setReadonly(true);
		
		txt_E1.setReadonly(true);
		txt_E2.setReadonly(true);
		txt_E3.setReadonly(true);
		
		txt_F1.setReadonly(true);
		txt_F2.setReadonly(true);
		txt_F3.setReadonly(true);
		
		txt_G1.setReadonly(true);
		txt_G2.setReadonly(true);
		txt_G3.setReadonly(true);
		txt_G4.setReadonly(true);
		txt_G5.setReadonly(true);
		
		txt_H1.setReadonly(true);
		txt_H2.setReadonly(true);
		txt_H3.setReadonly(true);
		txt_H4.setReadonly(true);
		txt_H5.setReadonly(true);
		
		
		
	}
	
	/**
	 * 點擊新增按鍵時作業
	 */
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){
		//uniqueid = getSequenceid();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("=====masterwindow==show =========");
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterCreateMode(mapButton);
		btnSaveMaster.setDisabled(false);
		btnCancelMaster.setDisabled(false);
		txt_AREANUM.setReadonly(true);
		txt_AREANUM.setText(getSequenceid());
		txt_createtime.setValue(sdf.format(new Date()));
		
		txt_A1.setReadonly(false);
		txt_A2.setReadonly(false);
		txt_A3.setReadonly(false);
		
		txt_B1.setReadonly(false);
		txt_B2.setReadonly(false);
		txt_B3.setReadonly(false);
		txt_B4.setReadonly(false);
		txt_B5.setReadonly(false);
		txt_B6.setReadonly(false);
		txt_B7.setReadonly(false);
		txt_B8.setReadonly(false);
		txt_B9.setReadonly(false);
		txt_B10.setReadonly(false);
		txt_B11.setReadonly(false);
		
		txt_C1.setReadonly(false);
		txt_C2.setReadonly(false);
		txt_C3.setReadonly(false);
		
		txt_D3.setReadonly(false);
		txt_D3.setReadonly(false);
		txt_D3.setReadonly(false);
		
		txt_E1.setReadonly(false);
		txt_E2.setReadonly(false);
		txt_E3.setReadonly(false);
		
		txt_F1.setReadonly(false);
		txt_F2.setReadonly(false);
		txt_F3.setReadonly(false);
		
		txt_G1.setReadonly(false);
		txt_G2.setReadonly(false);
		txt_G3.setReadonly(false);
		txt_G4.setReadonly(false);
		txt_G5.setReadonly(false);
		
		txt_H1.setReadonly(false);
		txt_H2.setReadonly(false);
		txt_H3.setReadonly(false);
		txt_H4.setReadonly(false);
		txt_H5.setReadonly(false);
	}
	@Listen("onOK=#txt_EL_NOSQL")
	public void onOKTextbox(){
		super.executeQuery();
	}
	
	/**
	 * 查詢按鈕, 也可以做開窗查詢
	 */
	@Listen("onClick = #btnImport")
	public void onClickbtnQuerya(Event event) {
		
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("pbgroupid", "abcedd");
		//Component con=  Executions.createComponents("/ds/test/DSPLM102M03.zul", null, map);
		Component con=  Executions.createComponents("/ds/dsid/DSID02M_Import01.zul", null, map);
		
	}
	
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event){
		executeQuery();
		//Map<String, Object> map = (Map<String, Object>) event.getData();
		//DSPB00_NEW e = (DSPB00_NEW) map.get("selectedRecord");
		//txtPB_MUNODE.setValue(e.getPB_MUITEM());
		//txtPB_MUITEM.setValue(getNewItem());
	}

	/**
	 * 獲取序列號
	 * 
	 * @return 返回序列
	 */
	private String getSequenceid(){
		CRUDService CRUDServicetemp = (CRUDService) SpringUtil.getBean("CRUDService");
		String sql ="select  seq_DSIDN06.Nextval from dual";
		Query  tempquery = CRUDServicetemp.createSQL(sql);
		Object	tempList = tempquery.getSingleResult();
		
		System.out.println("===sql=="+tempList);
	    
	    return String.valueOf(tempList);
	}


}
