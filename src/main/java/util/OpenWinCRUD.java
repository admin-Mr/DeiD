package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import bitronix.tm.TransactionManagerServices;
import ds.common.services.CRUDService;
import ds.common.services.ProgramAuth;
import ds.common.services.UserCredential;

@SuppressWarnings({"rawtypes"})
public abstract class OpenWinCRUD extends SelectorComposer<Component> {	
	private static final long serialVersionUID = 1L;
	
	/**	記錄log */
	protected Logger logger = BlackBox.getLogger(this);
	
	@WireVariable 
	protected CRUDService CRUDService;	
	
	/**	parentSel為所選取之資料物件,  beforenewSel為所選取前之資料物件 */
	protected Object parentSel, beforenewSel;
	
	/** 開窗編輯時所傳入的model*/
	protected ListModelList modelMaster, modelDetail;
	
	/**	對應元件資料與欄位名稱，組SQL使用 */
	protected List<ComponentColumn> componentColumns = new ArrayList<ComponentColumn>();
	
	/** 次檔啟始index */
	protected int indexDetail = 0;
	
	/** 次檔的數量 */
	protected int DetailCount = -1;
	
	/** 資料模式 */
	private DataMode recordMode;
	
	/** 父視窗 */
	private Window windowParent;
	
	/** 作業按鈕清單 */
	private HashMap<String, Object> buttons = null;
	
	/** 登入者資訊 */
	protected final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential"); //登入者資訊
	
	/** java.class名(@program), 用來判斷作業權限(DSPB00_NEW.PB_PRGNAME) */
	protected String MASTER_CLASSNAME;
	
	/** java.class名(@domain), 為作業的主要entity name */
	protected String ENTITY_CLASSNAME;
	
	/** java.class名(@domain)檔案路徑 */
	protected String ENTITY_PATH;
	
	/** 作業權限 */
	public ProgramAuth prgAuth;
	
	boolean bRegular = true;
	
	/** 次檔程式List */
	List<Detail> arrDetailPrograms = new ArrayList<Detail>();
	
	/** 接收開窗編輯時所傳入的參數 */
	Execution execution;
	
	/** 返回之視窗Method名稱 */
	String returnMethodName;	
	
	/** 存檔時, 寫入之欄位 */
	HashMap<String, Object[]> mapColumns = new HashMap<String, Object[]>();
	
	/** 檢查PK時, PK欄位 */
	HashMap<String, Object[]> mapPKColumns = new HashMap<String, Object[]>();
	
	/** domain 查詢條件 */
	String sWhere = "";	
	
	/** 是否自動關閉視窗 */
	protected boolean bIsCloseWindow = true;
	
	/**
	 * 取得java.class名(@domain) => ENTITY_CLASSNAME
	 * @return entity class
	 */
	abstract protected Class getEntityClass();
	
	/**
	 * 取得此作業的window id
	 * @return ZK Window Component
	 */
	abstract protected Window getRootWindow();
	
	/**
	 * 呼叫視窗為底層Master或Detail, MSMode.MASTER/MSMode.DETAIL
	 * @return
	 */
	abstract protected MSMode getMSMode();
	

	
	/** 切始化及設定權限 */
	abstract protected void init();	
	
	/**
	 * 預設儲存資料, 無控件使用
	 * @param columnName 欄位名稱
	 * @return 該欄位之資料
	 */
	abstract protected Object doSaveDefault(String columnName);
	
	/** 開窗後帶回之資料 */
	abstract protected HashMap getReturnMap();

	/**OpenWinCRUD設定**/
//	abstract protected ArrayList<String> getKeyName();	//取得PK Column Name
//	abstract protected ArrayList<String> getKeyValue(Object objectEntity);  //取得PK Column Value
//	abstract protected boolean beforeSave(Object entityMaster);
//	abstract protected boolean doCustomSave(Connection conn);
//	abstract protected void addDetailPrograms();	// 用來設定所擁有的次檔
	
	
	public OpenWinCRUD(){
		super();
	}

	/**
	 * 作業的初始
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
			
			setExecution(Executions.getCurrent());
			setParentSel((Object) execution.getArg().get("selectedRecord"));		
			setMasterModel((ListModelList) execution.getArg().get("masterModel"));
			setDetailModel((ListModelList) execution.getArg().get("detailModel"));
			setRecordMode((DataMode) execution.getArg().get("recordMode"));
			setWindowParent((Window) execution.getArg().get("parentWindow"));

			if (getRecordMode() == null) {
				recordMode = DataMode.READ_MODE;
			}

			// 底層設定entity ENTITY_CLASSNAME and ENTITY_PATH
			BlackBox.setEntityClassPath(this);

			if (execution.getArg().get("returnMethod") == null)			
				returnMethodName = getMSMode().equals(MSMode.MASTER)? "onMasterSaved" : "onDetailSaved";
			else
				returnMethodName = (String)execution.getArg().get("returnMethod");
			
			init();
			if (this.MASTER_CLASSNAME != null && this.MASTER_CLASSNAME.trim().isEmpty() == false){
				_getAuthority();
				addDetailPrograms();
				masterReadMode(null);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 設定權限
	 */
	private void _getAuthority() {
		try {
			if (_userInfo == null || _userInfo.getAuth().size() == 0) {
				prgAuth = null;
			} else {
				prgAuth = BlackBox.getPRGAuth(_userInfo, MASTER_CLASSNAME);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 瀏覽模式
	 * @param mapButton 控管之元件
	 */
	public void masterReadMode(HashMap<String, Object> mapButton) {
		try {
			// johnny add 用以離開作業時，判斷data_mode
			Sessions.getCurrent().setAttribute("DataMode", recordMode);

			if (mapButton != null) {
				buttons = mapButton;
				BlackBox.setButtonAuth(this, false);
			}

			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				arrDetailPrograms.get(i).detailReadMode(null);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}	
	
	/**
	 * 編輯模式, 包含新增及修改
	 * @param mapButton 控管之元件
	 */
	public void masterCreateMode(HashMap<String, Object> mapButton) {
		try {
			// johnny add 用以離開作業時，判斷data_mode
			Sessions.getCurrent().setAttribute("DataMode", recordMode);

			BlackBox.setButtonAuth(this, false);
			BlackBox.setOtherDetailButton(this, null);
			
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				Detail det = arrDetailPrograms.get(i);
				det.doFillListboxDetail(0);
			}			
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}		
	
	/**
	 * 將取得的資料內容放到控件上
	 * @param objSel 資料物件
	 */
	public void resetComponent(Object objSel) {
		try {
			setParentSel(objSel);
			// 20170607 johnny 重新查詢，使得有join欄位取得資料
			if (objSel != null) {
				Table table = (Table) objSel.getClass().getAnnotation(Table.class);
				if (table != null && !table.name().isEmpty()) {
					BlackBox.entityRefresh(objSel);
				}
			}
			
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))
					return;
			}

			if (getRecordMode().equals(DataMode.READ_MODE) && DetailCount > 0) {
				for (int i = 0; i < arrDetailPrograms.size(); i++) {
					Detail det = arrDetailPrograms.get(i);
					det.doFillListboxDetail(0);
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * Tabbox切換, 變更次檔indexDetail
	 */
	@Listen("onClick = #tllTabbox")
	public void clickTabbox(Event e) {
		Tabbox tabbox = (Tabbox)e.getTarget();
		indexDetail = tabbox.getSelectedIndex();
	}	
	
	/** 點擊取消鈕(新增/修改) */
	@Listen("onClick = #btnCancel,#btnCancel2,#btnCancel3,#btnCancel4,#btnCancel5,#btnCancel6,#btnCancel7,#btnCancel8,#btnCancel9,#btnCancel10,#btnCancel11,#btnCancel12,#btnCancel13,#btnCancel14,#btnCancel15,#btnCancel16,#btnCancel17,#btnCancel18,#btnCancel19,#btnCancel20")
	public void onClickbtnCancel(Event event) {
		try {
			if (getRootWindow() != null && getIsCloseWindow()) {
				getRootWindow().detach();

				Map<String, Object> args;
				if (getReturnMap() == null)
					args = new HashMap<String, Object>();
				else
					args = getReturnMap();

				args.put("recordMode", this.recordMode);

				if (windowParent != null)
					Events.sendEvent(new Event(returnMethodName, windowParent, args));
			}

			setParentSel(beforenewSel);
			setRecordMode(DataMode.READ_MODE);
			masterReadMode(null);
			resetComponent(parentSel);
			
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				Detail det = arrDetailPrograms.get(i);
				det.doFillListboxDetail(0);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}	

	/** 關閉視窗 */
	@Listen("onClose = Window")
	public void onCloseWindow(Event event){
		onClickbtnCancel(event);
	}
	
	/** 點擊新增鈕 */
	@Listen("onClick = #btnCreateMaster,#btnCreateMaster2,#btnCreateMaster3,#btnCreateMaster4,#btnCreateMaster5,#btnCreateMaster6,#btnCreateMaster7,#btnCreateMaster8,#btnCreateMaster9,#btnCreateMaster10,#btnCreateMaster11,#btnCreateMaster12,#btnCreateMaster13,#btnCreateMaster14,#btnCreateMaster15,#btnCreateMaster16,#btnCreateMaster17,#btnCreateMaster18,#btnCreateMaster19,#btnCreateMaster20")
	public void onClickbtnCreatemaster(Event evnet) {
		recordMode = DataMode.CREATE_MODE;
		beforenewSel = parentSel;
		setParentSel(null);
		masterCreateMode(null);
	}
	
	/** 點擊修改鈕 */
	@Listen("onClick = #btnEditMaster,#btnEditMaster2,#btnEditMaster3,#btnEditMaster4,#btnEditMaster5,#btnEditMaster6,#btnEditMaster7,#btnEditMaster8,#btnEditMaster9,#btnEditMaster10,#btnEditMaster11,#btnEditMaster12,#btnEditMaster13,#btnEditMaster14,#btnEditMaster15,#btnEditMaster16,#btnEditMaster17,#btnEditMaster18,#btnEditMaster19,#btnEditMaster20")
	public void onClickbtnEditMaster(Event evnet) throws Exception {
		try {
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE)) {
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					return;
				}
			}
			if (recordMode.equals(DataMode.UPDATE_MODE) || recordMode.equals(DataMode.CREATE_MODE))
				return;

			// 複製物件
			Object objEntity;
			if (getEntityClass() != null) {
				objEntity = getEntityClass().newInstance();
				BeanUtils.copyProperties(parentSel, objEntity);
				beforenewSel = objEntity;
			}

			recordMode = DataMode.UPDATE_MODE;
			masterCreateMode(null);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/** 點擊刪除鈕 */
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnDeleteMaster,#btnDeleteMaster2,#btnDeleteMaster3,#btnDeleteMaster4,#btnDeleteMaster5,#btnDeleteMaster6,#btnDeleteMaster7,#btnDeleteMaster8,#btnDeleteMaster9,#btnDeleteMaster10,#btnDeleteMaster11,#btnDeleteMaster12,#btnDeleteMaster13,#btnDeleteMaster14,#btnDeleteMaster15,#btnDeleteMaster16,#btnDeleteMaster17,#btnDeleteMaster18,#btnDeleteMaster19,#btnDeleteMaster20")
	public void onClickbtnDeleteMaster(Event event) {
		try {
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE)) {
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					return;
				}
			}
			if (recordMode.equals(DataMode.UPDATE_MODE) || recordMode.equals(DataMode.CREATE_MODE))
				return;

			if (beforeDel(getParentSel()) == false)
				return;

			Messagebox.show(Labels.getLabel("COMM.DELETE"), Labels.getLabel("PUBLIC.MSG0001"),
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event e) throws Exception {
					if (Messagebox.ON_OK.equals(e.getName())) {
						UserTransaction ut = TransactionManagerServices.getTransactionManager();
						ut.begin();
						Connection conn = BlackBox.getDbConnection(CRUDService.getEmf());
						try {							
							conn.setAutoCommit(false);
							if (doCustomerDel(conn)) {
								doDelete(getParentSel(), conn, ut);
							} else {
								throw new Exception();
							}
						} catch (Exception ex) {
							conn.rollback();
							logger.error(null, ex);
							Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "Error", Messagebox.OK, Messagebox.ERROR);
							throw ex;
						} finally {
							BlackBox.close(conn);
						}
					} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
						// Cancel is clicked
					}
				}
			});
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/** 儲存鈕(新增/編輯), 會先檢查再做存檔 */
	@Listen("onClick = #btnSave,#btnSave2,#btnSave3,#btnSave4,#btnSave5,#btnSave6,#btnSave7,#btnSave8,#btnSave9,#btnSave10,#btnSave11,#btnSave12,#btnSave13,#btnSave14,#btnSave15,#btnSave16,#btnSave17,#btnSave18,#btnSave19,#btnSave20")
	public void onClickbtnSave(Event event) throws Exception {
		try {
			OpenWinSave();
			setRecordMode(DataMode.READ_MODE);
			masterReadMode(null);
			resetComponent(getParentSel());
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
	
	/** 執行存檔 */
	public void OpenWinSave() throws Exception {
		UserTransaction ut = TransactionManagerServices.getTransactionManager();
		try {
			ut.begin();
			Connection conn = BlackBox.getDbConnection(CRUDService.getEmf());
			conn.setAutoCommit(false);
			OpenWinSave(conn);
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			try {
				ut.rollback();
			} catch (Exception e2) {
				if (!e2.toString().equals("java.lang.IllegalStateException: no transaction started on this thread")) {
					logger.error(null, e2);
				}
			}
		}
	}
	
	/** 執行存檔 */
	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	public void OpenWinSave(Connection conn) throws Exception {
		UserTransaction ut = null;
		
		try {
			ut = TransactionManagerServices.getTransactionManager();
			/* 檢查控件 */
			if (!BlackBox.checkComponent(this))
				return;

			Map<String, Object> args;
			if (getReturnMap() == null)
				args = new HashMap<String, Object>();
			else
				args = getReturnMap();

			Object selectedMaster = null;
			boolean bSuccess = true;
			selectedMaster = getParentSel();

			if (recordMode.equals(DataMode.CREATE_MODE)) {
				Class param[] = new Class[0];
				Constructor masterConstructor = null;
				masterConstructor = getEntityClass().getConstructor(param);
				Object masterEntity = masterConstructor.newInstance(param);

				try {
					if (writeMethod(masterEntity) && beforeSave(masterEntity) && BlackBox.CheckPK(this, masterEntity) && BlackBox.validate(masterEntity) && doCustomSave(conn)) {	
						BlackBox.executeSave(this, masterEntity, conn, ut); 
						if (getMSMode().equals(MSMode.DETAIL))
							getDetailModel().add(masterEntity);
						args.put("selectedRecord", masterEntity);
						args.put("entityModel", masterEntity);	
						setParentSel(masterEntity);
					} else {
						logger.info("onClickbtnSave(Create) : writeMethod(masterEntity) && beforeSave(masterEntity) && BlackBox.CheckPK(this, masterEntity) && BlackBox.validate(masterEntity) && doCustomSave(conn) is false");
						return;
					}
				} catch (Exception ex) {
					conn.rollback();
					logger.error("onClickbtnSave(Create) : ", ex);
					bSuccess = false;
				} finally {
					BlackBox.close(conn);
				}			
			} else if (recordMode.equals(DataMode.UPDATE_MODE)) {	
				try {
					if (writeMethod(selectedMaster) && beforeSave(selectedMaster) && BlackBox.CheckPK(this, selectedMaster) && BlackBox.validate(selectedMaster) && doCustomSave(conn)) {
						BlackBox.executeSave(this, parentSel, conn, ut); 
						if (modelDetail != null)
							modelDetail.set(modelDetail.indexOf(parentSel), parentSel);
						args.put("selectedRecord", this.parentSel);	
						setParentSel(parentSel);
					} else {
						logger.info("onClickbtnSave(Update) : writeMethod(selectedMaster) && beforeSave(selectedMaster) && BlackBox.CheckPK(this, selectedMaster) && BlackBox.validate(selectedMaster) && doCustomSave(conn) is false");
						return;
					}
				} catch (Exception ex) {
					conn.rollback();
					logger.error("onClickbtnSave(Update) : ", ex);
					bSuccess = false;
				} finally {
					BlackBox.close(conn);
				}
			}

			if (bSuccess) {
				if (recordMode.equals(DataMode.CREATE_MODE))
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"), "Information", Messagebox.OK, Messagebox.INFORMATION);
				else if (recordMode.equals(DataMode.UPDATE_MODE))
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"), "Information", Messagebox.OK, Messagebox.INFORMATION);			
				
				if (getRootWindow() != null && getIsCloseWindow())
					getRootWindow().detach();
				
				args.put("recordMode", this.recordMode);				
				if (windowParent != null)
					Events.sendEvent(new Event(returnMethodName, windowParent, args));
			} else {
				if (recordMode.equals(DataMode.CREATE_MODE))
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0089"), "Error", Messagebox.OK, Messagebox.ERROR);
				else if (recordMode.equals(DataMode.UPDATE_MODE))
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0088"), "Error", Messagebox.OK, Messagebox.ERROR);
				
				throw new Exception();
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param objOpenWinSel 資料物件
	 * @return 成功回傳true, 失敗回傳false
	 */
	public boolean writeMethod(Object objOpenWinSel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		if (componentColumns.size() > 0) {
			return writeMethod(objOpenWinSel, componentColumns);
		}
		return false;
	}
	
	/**
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param objSel 資料物件
	 * @param comColumns 寫入資料之欄位
	 * @return 成功回傳true, 失敗回傳false
	 */
	public boolean writeMethod(Object objSel, List<ComponentColumn> comColumns) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		return writeMethod(ENTITY_CLASSNAME, objSel, comColumns);
	}
	
	/**
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param className
	 * @param objSel 資料物件
	 * @param comColumns 寫入資料之欄位
	 * @return 成功回傳true, 失敗回傳false
	 */
	public boolean writeMethod(String className, Object objSel, List<ComponentColumn> comColumns) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		return BlackBox.writeMethod(this, className, objSel, comColumns);
	}	
		
	/** 取得PK欄位名稱 */
	protected ArrayList<String> getKeyName() {
		return BlackBox.getKeyName(getEntityClass());
	}
	
	/** 取得PK欄位資料 */
	protected ArrayList<String> getKeyValue(Object objectEntity) {
		return BlackBox.getKeyValue(this, objectEntity);
	}
	
	/** 設定傳入時的Execution */
	public void setExecution(Execution execution){
		this.execution = execution;
	}
	
	/** 取得傳入時的Execution */
	public Execution getExecution(){
		return execution;
	}
	
	/** 取得開窗時所傳入的資料物件 */
	public Object getParentSel(){
		return parentSel;
	}
	
	/** 設定開窗時所傳入的資料物件 */
	public void setParentSel(Object parentSel){
		this.parentSel = parentSel;
	}
	
	/** 取得資料模式  */
	public DataMode getRecordMode(){
		return this.recordMode;
	}
	
	/** 設定資料模式  */
	public void setRecordMode(DataMode recordMode){
		this.recordMode = recordMode;
	}
	
	/** 設定父視窗  */
	public void setWindowParent(Window windowParent){
		this.windowParent = windowParent;
	}
	
	/** 取得開窗編輯時所傳入的Master model */
	public ListModelList getMasterModel() {
		return modelMaster;
	}
	
	/** 設定開窗編輯時所傳入的Master model */
	public void setMasterModel(ListModelList modelMaster) {
		this.modelMaster = modelMaster;
	}

	/** 取得開窗編輯時所傳入的Detail model */
	public ListModelList getDetailModel(){
		return this.modelDetail;
	}
	
	/** 設定開窗編輯時所傳入的Detail model */
	public void setDetailModel(ListModelList modelDetail){
		this.modelDetail = modelDetail;
	}
	
	/** 取得給予Entity的名稱 */
	public String getClassName(){
		return MASTER_CLASSNAME;
	}
	
	/** 設定給予Entity的名稱 */
	public void setClassName(String className){
		this.MASTER_CLASSNAME = className;
	}
	
	/** 取得次檔作業List */
	public List<Detail> getArrDetailPrograms() {
		return arrDetailPrograms;
	}
	
	/** 設定次檔作業List */
	public void setArrDetailPrograms(List<Detail> arrDetailPrograms) {
		this.arrDetailPrograms = arrDetailPrograms;
	}	
	
	/** 取得次檔的CLASS(@program) */
	protected Class getDetailClass(int indexDetail) {
		return getDetailProgram(indexDetail).getClass();
	}
	
	/** 取得次檔的Detail */
	protected Detail getDetailProgram(int indexDetail) {
		return getArrDetailPrograms().get(indexDetail);
	}

	/** 設定次檔的Detail */
	protected void addDetailPrograms() {

	}
	
	/** 取得按鈕Component */
	public HashMap<String, Object> getButtons(){
		return this.buttons;
	}
	
	/** 設定按鈕Component */
	public void setButtons(HashMap<String, Object> mapButton){
		this.buttons = mapButton;
	}
	
	/** 取得目前次檔Index */
	public int getIndexDetail() {
		return indexDetail;
	}
	
	/** 設定目前次檔Index */
	public void setIndexDetail(int indexDetail) {
		this.indexDetail = indexDetail;
	}	
	
	/** 設定次檔index及次檔數量 */
	public void setDetail(int DetailCount){
		this.indexDetail = DetailCount - 1;
		this.DetailCount = DetailCount;
	}
	
	/** 取得所選取前之資料物件  */
	public Object getBeforeNewSel(){
		return this.beforenewSel;
	}
	
	public void setRegular(boolean pRegular){
		this.bRegular = pRegular;
	}
	
	/** 取得是否自動關閉視窗 */
	public boolean getIsCloseWindow() {
		return bIsCloseWindow;
	}
	
	/** 設定是否自動關閉視窗 */
	public void setIsCloseWindow(boolean bIsCloseWindow) {
		this.bIsCloseWindow = bIsCloseWindow;
	}	
	
	/**
	 * 存檔前檢查
	 * @param entity 資料物件
	 * @return 檢查通過:True, 不通過:False
	 */
	protected boolean beforeSave(Object entity) {
		return BlackBox.beforeSave(this, entity);
	}
	
	/**
	 * 刪除前的檢查 
	 * @param entity 資料物件
	 * @return 檢查通過:True, 不通過:False
	 */
	protected boolean beforeDel(Object entity) {
		return true;
	}
	
	/** 若有其他table需一併儲存 */
	protected boolean doCustomSave(Connection conn) {
		return true;
	}
	
	/** 存檔完後若有table需更新使用 */
	protected boolean doSaveAfter(Connection conn) {
		return true;
	}
	
	/** 刪除完後若有table需更新使用 */
	protected boolean doDeleteAfter(Connection conn) {
		return true;
	}
	
	/**
	 * 判斷是否跟資料列一起刪除
	 * @param conn 連線
	 * @return 刪除成功:True, 不成功:False
	 */
	public boolean doCustomerDel(Connection conn) {
		return true;
	}
	
	/**
	 * 執行刪除功能
	 * @param parentSel 資料物件
	 * @param conn 連線
	 * @throws Exception
	 */
	public void doDelete(Object parentSel, Connection conn, UserTransaction ut) throws Exception {
		BlackBox.doDelete(this, parentSel, conn, ut);
	}
}
