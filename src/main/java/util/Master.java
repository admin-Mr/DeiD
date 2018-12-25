package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

import bitronix.tm.TransactionManagerServices;
import ds.common.services.CRUDService;
import ds.common.services.ProgramAuth;
import ds.common.services.UserCredential;

@SuppressWarnings({ "rawtypes" })
public abstract class Master extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	/**	記錄log */
	protected Logger logger = BlackBox.getLogger(this);
	
	@WireVariable
	protected CRUDService CRUDService;
	@Wire
	protected Listbox masterListbox;
	
	/** 開窗編輯所傳入之map */
	protected HashMap<String, Object> mapOpenWin = new HashMap<String, Object>();
	
	/**	listbox model */
	protected ListModelList masterModel; // ListModelList<?> don't use
	
	/**	masterSel為所選取之資料物件,  masterSelBeforeNew為為所選取前之資料物件 */
	protected Object masterSel, masterSelBeforeNew;
	
	/**	對應元件資料與欄位名稱，組SQL使用 */
	protected List<ComponentColumn> masterComponentColumns = new ArrayList<ComponentColumn>();
	
	/** java.class名(@program), 用來判斷作業權限(DSPB00_NEW.PB_PRGNAME) */
	protected String MASTER_CLASSNAME;
	
	/** java.class名(@domain), 為作業的主要entity name */
	protected String ENTITY_CLASSNAME;
	
	/** java.class名(@domain)檔案路徑 */
	protected String ENTITY_PATH;
	
	/** 資料模式 */
	private DataMode data_Mode = DataMode.READ_MODE;
	
	/** 作業之Listbox Component */
	protected Listbox listbox;
	
	/** listbox資料清單 */
	protected List masterList;
	
	/** 作業按鈕清單 */
	private HashMap<String, Object> buttons = null;
	
	/** 登入者資訊 */
	protected final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	
	/** 作業權限 */
	public ProgramAuth prgAuth;
	
	/** 次檔啟始index */
	protected int indexDetail = 0;
	
	/** 次檔的數量 */
	protected int DetailCount = -1;
	
	/** 是否為多選 */
	private Boolean bMultiple = false;
	
	/** 分頁 */
	Paging masterPaging;
	
	/** queryResultSize 計算資料筆數, queryPaging 查詢資料, queryOneCol 查詢PK欄位, queryPagingbySize 設定查詢資料區間*/
	Query queryResultSize, queryPaging, queryOneCol, queryPagingbySize;

	/** 取得ResultSizeBase SQL */
	String sSqlResultSizeBase;
	
	/** 取得PagingBase SQL */
	String sSqlPagingBase;
	
	/** 取得OneColBase SQL */
	String sSqlOneColBase;
	
	/** 次檔程式List */
	List<Detail> arrDetailPrograms = new ArrayList<Detail>();
	
	/** listbox多選物件List */
	ArrayList<Object> arrMulitSelect = new ArrayList<Object>();

	/** 分頁筆數 */
	int MY_PAGE_SIZE;
	
	boolean bRegular = true;
	
	/** 使用InLineEdit */
	boolean bInLineEdit = false;
	
	/** 設定新增資料時, 資料插入位置, true為所選取資料之下方 */
	boolean bInsertCurrent = false;
	
	/** 存檔時, 寫入之欄位 */
	HashMap<String, Object[]> mapColumns = new HashMap<String, Object[]>();
	
	/** 檢查PK時, PK欄位 */
	HashMap<String, Object[]> mapPKColumns = new HashMap<String, Object[]>();
	
	/** domain 查詢條件 */
	String sWhere = "";
	
	/** Listbox標題欄位排序 */
	String sOrderByListhead = "";
	
	/** 取得分頁開始位置 */
	private int iStartPosition = 0;
	
	/** 是否為自訂SQL語法 */
	private boolean bIsCustList = false;

	
	/**controller設定**/
	/**
	 * 取得此作業的window id
	 * @return ZK Window Component
	 */
	abstract protected Window getRootWindow();
	
	/**
	 * 取得java.class名(@program) => MASTER_CLASSNAME
	 * @return java class
	 */
	abstract protected Class getMasterClass();
	
	/**
	 * 取得java.class名(@domain) => ENTITY_CLASSNAME
	 * @return entity class
	 */
	abstract protected Class getEntityClass();
	
	/**
	 * 將取得的資料內容放到控件上
	 * @param entityMaster 資料物件
	 */
	abstract protected void resetEditAreaMaster(Object entityMaster);
	
	/**
	 * 按下新增鈕後, 要取得的預設值
	 */
	abstract protected void doCreateDefault();
	
	/**
	 * 預設儲存資料, 無控件使用
	 * @param columnName 欄位名稱
	 * @return 該欄位之資料
	 */
	abstract protected Object doSaveDefault(String columnName);
	
	/**
	 * 自訂SQL語法使用, 需搭配自訂資料物件
	 * @return 自訂的List集合
	 */
	abstract protected List getCustList();
	
	/**Master設定**/
//	abstract protected ArrayList<String> getMasterKeyName(); //取得主檔的PK Column Name
//	abstract protected ArrayList<String> getMasterKeyValue(Object entityMaster); //取得主檔的 PK Column Value
//	abstract protected OperationMode getOperationMode(); // 取得作業模式
//	abstract protected String getMasterCreateZul(); // 新增開窗的ZUL檔路徑
//	abstract protected boolean beforeMasterSave(Object entityMaster); // 存檔前的檢查
//	abstract protected boolean beforeMasterDel(Object entityMaster);	// 刪除前的檢查
//	abstract protected String getPagingId();	//設定分頁ID
//	abstract protected String getEntityName(); // 取得entity name
//	abstract protected int getPageSize(); // 設定一頁筆數，預設為0=一頁5筆資料
//	abstract protected boolean doCustomSave(Connection conn);  //若有其他table需一併儲存使用
	
	public Master() {
		super();
	}

	/**
	 * 作業的初始
	 */
	@Override
	public void doAfterCompose(Component window) throws Exception {
		try {
			super.doAfterCompose(window);
			CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
			_init();
			setMasterSel(null);
			_initAuthority(window);
			masterReadMode(null);
			sOrderByListhead = getOrderBy();

			if (getCustList() != null) {
				bIsCustList = true;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * 取得zul檔元件
	 * @param comp zul檔元件
	 */
	void _initAuthority(Component comp) {
		// 找到頁面上的listbox
		if (comp instanceof Listbox) {
			if (comp.getId().indexOf("master") == 0)
				listbox = (Listbox) comp;
		}
		
		List<Component> listComp = comp.getChildren();
		for (Component child : listComp) {
			_initAuthority(child);
		}
	}

	/**
	 * 切始化及設定權限
	 */
	void _init() {
		try {
			String mClass[] = getMasterClass().getName().split("\\.");
			MASTER_CLASSNAME = mClass[mClass.length - 1];

			// 底層設定entity ENTITY_CLASSNAME and ENTITY_PATH
			BlackBox.setEntityClassPath(this);

			if (!getPagingId().equals("")) {
				masterPaging = (Paging) getRootWindow().getFellow(getPagingId());
			}

			Session sessImageAuth = Sessions.getCurrent();
			if (_userInfo == null || _userInfo.getAuth().size() == 0) {
				prgAuth = null;
				sessImageAuth.setAttribute("RAuth", false);
				sessImageAuth.setAttribute("UAuth", false);
				sessImageAuth.setAttribute("DAuth", false);
			} else {
				prgAuth = BlackBox.getPRGAuth(_userInfo, MASTER_CLASSNAME);
				sessImageAuth.setAttribute("RAuth", prgAuth.getAuthQuery());
				sessImageAuth.setAttribute("UAuth", prgAuth.getAuthEdit());
				sessImageAuth.setAttribute("DAuth", prgAuth.getAuthDelete());
			}
			
			addDetailPrograms();
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
			Sessions.getCurrent().setAttribute("DataMode", data_Mode);

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
			Sessions.getCurrent().setAttribute("DataMode", data_Mode);

			BlackBox.setButtonAuth(this, false);
			BlackBox.setOtherDetailButton(this, null);
			
			if (data_Mode.equals(DataMode.CREATE_MODE)) {
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
	 * 作業建立時, 只要Listboxk有資枓且作業在瀏覽狀態(READ_MODE), 對listbox 模擬onClick
	 */
	@Listen("onCreate = Window")
	public void onCreateWindowMaster(Event event) {
		if (listbox != null && listbox.getSelectedItem() != null) {  
			if (data_Mode.equals(DataMode.READ_MODE))
				onClickMasterListbox(listbox.getSelectedItem().getValue());
		}
	}

	/**
	 * Listbox建立時, 先取查詢條件, 再把查詢結果放進listbox
	 */
	@Listen("onCreate = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void onCreateListbox(Event event) {
		executeQuery();
	}

	/**
	 * listbox onSelect, 原本為onClick事件，改更onSelect，因此function為public，改原fucntion name 不變更
	 */
	@Listen("onSelect = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void clickMasterListbox(Event event) {
		try {
			Listitem listitem = (Listitem) ((SelectEvent) event).getReference();
			if (listbox.getSelectedItem() != null) {
				if (data_Mode.equals(DataMode.READ_MODE)) {
					for (int i = 0; i < arrDetailPrograms.size(); i++) {
						if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))
							return;
					}
					onClickMasterListbox(listitem.getValue());
				}
			}
			BlackBox.listboxMulitSel(this, event);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * listbox onClick action 把主檔的資料放到masterSel, 並記錄選擇資料, 若有次檔, 將次檔資料放至次檔Listbox
	 */
	public void onClickMasterListbox(Object ocvo) {
		try {
			setMasterSel(ocvo);
			
			if (getData_Mode().equals(DataMode.READ_MODE)) {
				masterReadMode(null);
			}
			
			if (DetailCount > 0) {
				for (int i = 0; i < arrDetailPrograms.size(); i++) {
					Detail det = arrDetailPrograms.get(i);
					det.doFillListboxDetail(0);
				}
			}

			if (ocvo != null) {
				//預設選定的資料加到listbox多選物件List
				ArrayList<Object> arrList = new ArrayList<>();
				arrList.add(ocvo);
				setListboxMulitSelect(arrList);
			}
			
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * 設定次檔內容
	 * @param zulPath 次檔的zul檔路徑
	 * @param tabPanel 次檔所對應的Panel
	 * @param mapMaster 傳入次檔的參數, 底層預設會傳權限及window id
	 */
	public void addDetailPrograms(String zulPath, Component tabPanel, HashMap<String, Object> mapMaster) {
		mapMaster.put("prgAuth", prgAuth);
		mapMaster.put("parentWindow", getRootWindow());
		Executions.createComponents(zulPath, tabPanel, mapMaster);
	}

	/**
	 * 重新整理listbox
	 * @param masterEntity
	 * @throws Exception 
	 */
	public void refreshListbox(Object masterEntity) throws Exception {
		try {
			// 20170607 johnny 重新查詢，使得有join欄位取得資料
			setWhereConditionals(getWhereConditionals());
			BlackBox.setPosition(this, masterEntity);
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
		Tabbox tabbox = (Tabbox) e.getTarget();
		indexDetail = tabbox.getSelectedIndex();
	}

	/*********************************************************************************************************/

	/** LISTBOX INIT Start */
	/*********************************************************************************************************/
	
	/** 計算資料筆數SQL */
	public String getQueryResultSizeBase() {
		// johnny modify 避免因多個key值，造成需override
		return "Select COUNT(1) From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
	}

	/** 查詢資料SQL */
	public String getQueryPagingBase() {
		if (bIsCustList) // johnny 20170926
			return "Select * From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
		else
			return "Select t From " + ENTITY_CLASSNAME + " t ";
	}
	
	/** 查詢PK欄位SQL */
	public String getQueryOneColBase() {
		String strFields = "";
		for (int i = 0; i < getMasterKeyName().size(); i++) {
			strFields += ",t." + getMasterKeyName().get(i);
		}
		// johnny 20170926 modify
		return "Select " + strFields.substring(1) + " From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
	}

	/** 排序條件 */
	public String getOrderBy() {
		String strFields = "";
		for (int i = 0; i < getMasterKeyName().size(); i++) {
			strFields += ",t." + getMasterKeyName().get(i);
		}
		return " Order by " + strFields.substring(1);
	}

	/** 次檔 n ( 主檔是 t ) 不用於PagingBase
	  * getDetailProgram(indexDetail).DETAIL_CLASSNAME */
	public String getFromDetailRelation() {
		String strRelation = "";
		int iArrCount = arrDetailPrograms.size();
		String strBeforeAlias = "", strNowAlias = "";
		for (int i = 0; i < iArrCount; i++) {
			if (i == 0) {
				strRelation = "Left Join t." + getDetailProgram(i).ENTITY_CLASSNAME.replace("VO", "") + " n ";
				strBeforeAlias = "n";
			} else {
				strBeforeAlias = (i > 1) ? "n" + (i - 1) : strBeforeAlias;
				strNowAlias = "n" + i;
				strRelation += " Left Join t." + getDetailProgram(i).ENTITY_CLASSNAME.replace("VO", "") + " "
						+ strNowAlias;
			}
		}
		return strRelation;
	}
	
	/** 執行查詢 */
	public void executeQuery() {
		arrMulitSelect.clear();
		doFillListbox(0);
	}

	/** 設定SQL查詢條件 */
	public void setSqlConditions() {
		String strSQL = getWhereConditionals();
		String strDetailSQL = getDetailConditionals();
		
		if (strSQL == null)
			strSQL = "";
		if (strDetailSQL == null || strDetailSQL.isEmpty())
			setWhereConditionals(strSQL);
		else
			setWhereConditionals(strSQL, strDetailSQL);
		
		setQueryPagingbySize(iStartPosition);
	}
	
	/** 設定SQL查詢條件 */
	public void setWhereConditionals(String sWhereMaster) {
		setWhereConditionals(sWhereMaster, "");
	}
	
	/** 設定SQL查詢條件 */
	public void setWhereConditionals(String sWhereMaster, String sWhereDetail) {
		try {
			String sQueryResultSizeBase = getQueryResultSizeBase();
			String sQueryPagingBase = getQueryPagingBase();
			String sQueryOneColBase = getQueryOneColBase();
			String sOrderBy = getOrderBy(), sKeyField = "";

			for (int i = 0; i < getMasterKeyName().size(); i++) {
				sKeyField += ",t." + getMasterKeyName().get(i);
			}

			if (sOrderBy.equals("") || sOrderBy == null) {
				sOrderBy = " Order by " + sKeyField.substring(1);
			}

			String[] ss = new String[3];
			// 沒有次檔查詢條件
			if (sWhereDetail.isEmpty()) {
				ss[0] = sQueryResultSizeBase + " where 1=1 " + sWhereMaster;
				ss[1] = sQueryPagingBase + " where 1=1 " + sWhereMaster + sOrderBy;
				ss[2] = sQueryOneColBase + " where 1=1 " + sWhereMaster + sOrderBy;
			} else {
				String sDetailRelation = getFromDetailRelation();
				ss[0] = sQueryResultSizeBase + sDetailRelation + " Where 1=1 " + sWhereMaster + sWhereDetail;
				ss[1] = sQueryPagingBase + sDetailRelation + " where 1=1 " + sWhereMaster + sWhereDetail + sOrderBy;
				ss[2] = sQueryOneColBase + sDetailRelation + " where 1=1 " + sWhereMaster + sWhereDetail + sOrderBy;
			}

			sSqlResultSizeBase = ss[0];
			sSqlPagingBase = ss[1];
			sSqlOneColBase = ss[2];

			if (bIsCustList) { // johnny 20170926
				queryResultSize = CRUDService.createSQL(ss[0]);
				queryPaging = CRUDService.createSQL(ss[1]);
				queryOneCol = CRUDService.createSQL(ss[2]);
			} else {
				queryResultSize = CRUDService.getBetweenByLimit2(ss[0]);
				queryPaging = CRUDService.getBetweenByLimit2(ss[1]);
				queryOneCol = CRUDService.getBetweenByLimit2(ss[2]);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * 將查詢資料，使用listbox呈現
	 * model最多只有5筆(預設由Common.PAGE_SIZE設定，可自行MY_PAGE_SIZE)資料。 次檔listbox : 同上。
	 * @param index 在整個資料物件集合中的索引位置。
	 */
	@SuppressWarnings({ "unchecked" })
	public void doFillListbox(int index) {
		try {
			MY_PAGE_SIZE = (getPageSize() <= 0 ? Common.PAGE_SIZE : getPageSize());
			int activePage = (int) (index / MY_PAGE_SIZE);
			iStartPosition = activePage * MY_PAGE_SIZE;
			// 第一頁
			setSqlConditions();
			if (bIsCustList) {
				masterList = getCustList();
			} else {
				masterList = getQueryPagingbySize().getResultList();
			}

			masterModel = new ListModelList(masterList, true);
			getmasterListbox().invalidate();
			getmasterListbox().setModel(masterModel);
			getmasterListbox().renderAll();
			masterModel.setMultiple(getListboxMulit());
			masterPaging.setPageSize(MY_PAGE_SIZE);

			Long totalSize;
			if (bIsCustList)
				totalSize = ((BigDecimal) this.queryResultSize.getSingleResult()).longValue();
			else
				totalSize = (Long) this.queryResultSize.getSingleResult();

			masterPaging.setTotalSize((int) (long) totalSize);
			// 移除listeners
			Iterable<EventListener<? extends Event>> eventListeners = masterPaging
					.getEventListeners(ZulEvents.ON_PAGING);
			for (EventListener<? extends Event> eventListener : eventListeners) {
				masterPaging.removeEventListener(ZulEvents.ON_PAGING, eventListener);
			}
			// 此**listener**只會在，換頁時執行。
			EventListener el = new EventListener() {
				public void onEvent(Event event) throws Exception {
					try {
						List<Object> item = new ArrayList<Object>();
						boolean bMultiple = getListboxMulit();

						PagingEvent pagingEvt = (PagingEvent) event;
						iStartPosition = pagingEvt.getActivePage() * MY_PAGE_SIZE;
						setSqlConditions();

						if (bIsCustList) { // johnny 20170926
							masterList = getCustList();
						} else {
							masterList = getQueryPagingbySize().getResultList();
						}
						masterModel = new ListModelList(masterList, true);
						masterModel.setMultiple(bMultiple);

						
						if (arrMulitSelect.size() > 0) {
							for (int i = 0; i < arrMulitSelect.size(); i++) {
								for (int j = 0; j < masterModel.size(); j++) {
									if (BlackBox.getDiffField(arrMulitSelect.get(i), masterModel.get(j)) == null) {
										item.add(masterModel.get(j));
									}
								}
							}
						}

						masterModel.setSelection(item);
						// 每次分頁都抓5筆資料到model 20160324 tiger
						getmasterListbox().invalidate();
						getmasterListbox().setModel(masterModel);
						getmasterListbox().renderAll();
						
						if (getListboxMulit() == false)
							programmacticallySelectListbox(0);
					} catch (Exception e) {
						logger.error(null, e);
						throw e;
					}
				}
			};

			masterPaging.addEventListener(ZulEvents.ON_PAGING, el);
			masterPaging.setActivePage(activePage);
			
			try {
				programmacticallySelectListbox(index % MY_PAGE_SIZE);
			} catch (Exception e) {
				logger.error(null, e);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/*********************************************************************************************************/
	/** LISTBOX INIT End */
	/*********************************************************************************************************/

	/** 查詢按鈕, 也可以做開窗查詢 */
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnQuery, #btnQuery2, #btnQuery3, #btnQuery4, #btnQuery5, #btnQuery6, #btnQuery7, #btnQuery8, #btnQuery9, #btnQuery10, #btnQuery11, #btnQuery12, #btnQuery13, #btnQuery14, #btnQuery15, #btnQuery16, #btnQuery17, #btnQuery18, #btnQuery19, #btnQuery20")
	public void onClickbtnQuery(Event event) {
		try {
			switch (getOperationMode()) {
			case NORMAL:
			default:
				data_Mode = DataMode.READ_MODE;
				executeQuery();
				break;
			case OPENWIN:
				if (getMasterCreateMap() != null) {
					mapOpenWin = new HashMap<String, Object>(getMasterCreateMap());
				}
				mapOpenWin.put("selectedRecord", masterModel);
				mapOpenWin.put("recordMode", DataMode.READ_MODE);
				mapOpenWin.put("parentWindow", getRootWindow());
				Executions.createComponents(getMasterQueryZul(), null, mapOpenWin);
				break;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 點擊新增鈕, 有區分作業模式執行 */
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnCreateMaster,#btnCreateMaster2,#btnCreateMaster3,#btnCreateMaster4,#btnCreateMaster5,#btnCreateMaster6,#btnCreateMaster7,#btnCreateMaster8,#btnCreateMaster9,#btnCreateMaster10,#btnCreateMaster11,#btnCreateMaster12,#btnCreateMaster13,#btnCreateMaster14,#btnCreateMaster15,#btnCreateMaster16,#btnCreateMaster17,#btnCreateMaster18,#btnCreateMaster19,#btnCreateMaster20")
	public void masterCreate(Event event) {
		try {
			data_Mode = DataMode.CREATE_MODE;
			switch (getOperationMode()) {
			case NORMAL:
				if (masterModel.indexOf(getMasterSel()) != -1)
					masterSelBeforeNew = masterModel.get(masterModel.indexOf(getMasterSel()));
				else
					masterSelBeforeNew = null;

				if (bInLineEdit) {
					try {
						int index = 0;
						if (bInsertCurrent) {
							index = masterModel.indexOf(masterSel) + 1;
							masterModel.add(index, Class.forName(ENTITY_PATH).newInstance());
						} else {
							masterModel.add(index, Class.forName(ENTITY_PATH).newInstance());
						}

						BlackBox.propertyWriteMethod(masterModel.get(index), "ISADD", true); // johnny
																								// 20170926
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
							| IllegalArgumentException | InvocationTargetException e) {
						System.out.println("[ERROR] btnCreateMaster on Click: " + e.getMessage());
						e.printStackTrace();
					}
				}

				setMasterSel(null);
				doCreateDefault();
				masterCreateMode(null);
				break;
			case OPENWIN:
				if (getMasterCreateMap() != null) {
					mapOpenWin = new HashMap<String, Object>(getMasterCreateMap());
				}
				mapOpenWin.put("recordMode", DataMode.CREATE_MODE);
				mapOpenWin.put("parentWindow", getRootWindow());
				mapOpenWin.put("masterModel", getMasterModel());
				mapOpenWin.put("detailModel", null);
				Executions.createComponents(getMasterCreateZul(), null, mapOpenWin);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 點擊修改鈕 */
	@Listen("onClick = #btnEditMaster,#btnEditMaster2,#btnEditMaster3,#btnEditMaster4,#btnEditMaster5,#btnEditMaster6,#btnEditMaster7,#btnEditMaster8,#btnEditMaster9,#btnEditMaster10,#btnEditMaster11,#btnEditMaster12,#btnEditMaster13,#btnEditMaster14,#btnEditMaster15,#btnEditMaster16,#btnEditMaster17,#btnEditMaster18,#btnEditMaster19,#btnEditMaster20")
	public void masterUpdate(Event event) throws Exception {
		editMasterListbox(null);
	}
	
	/** 點擊刪除鈕 */
	@Listen("onClick = #btnDeleteMaster,#btnDeleteMaster2,#btnDeleteMaster3,#btnDeleteMaster4,#btnDeleteMaster5,#btnDeleteMaster6,#btnDeleteMaster7,#btnDeleteMaster8,#btnDeleteMaster9,#btnDeleteMaster10,#btnDeleteMaster11,#btnDeleteMaster12,#btnDeleteMaster13,#btnDeleteMaster14,#btnDeleteMaster15,#btnDeleteMaster16,#btnDeleteMaster17,#btnDeleteMaster18,#btnDeleteMaster19,#btnDeleteMaster20")
	public void masterDelete(Event event) throws Exception  {
		onDeleteMasterListbox(null);
	}

	/** 點擊取消鈕(新增/修改) */
	@Listen("onClick = #btnCancelMaster,#btnCancelMaster2,#btnCancelMaster3,#btnCancelMaster4,#btnCancelMaster5,#btnCancelMaster6,#btnCancelMaster7,#btnCancelMaster8,#btnCancelMaster9,#btnCancelMaster10,#btnCancelMaster11,#btnCancelMaster12,#btnCancelMaster13,#btnCancelMaster14,#btnCancelMaster15,#btnCancelMaster16,#btnCancelMaster17,#btnCancelMaster18,#btnCancelMaster19,#btnCancelMaster20")
	public void masterCancel(Event event) throws Exception {
		onCancelMasterListbox(null);
	}

	/** 儲存鈕(新增/編輯), 會先檢查再做存檔 */
	@Listen("onClick = #btnSaveMaster,#btnSaveMaster2,#btnSaveMaster3,#btnSaveMaster4,#btnSaveMaster5,#btnSaveMaster6,#btnSaveMaster7,#btnSaveMaster8,#btnSaveMaster9,#btnSaveMaster10,#btnSaveMaster11,#btnSaveMaster12,#btnSaveMaster13,#btnSaveMaster14,#btnSaveMaster15,#btnSaveMaster16,#btnSaveMaster17,#btnSaveMaster18,#btnSaveMaster19,#btnSaveMaster20")
	public boolean onClickbtnSaveMaster(Event event) {
		return onSaveMasterListbox(null);
	}
	
	/** 執行存檔 */
	public void masterSave() throws Exception {
		UserTransaction ut = TransactionManagerServices.getTransactionManager();
		try {
			ut.begin();
			Connection conn = BlackBox.getDbConnection(CRUDService.getEmf());
			conn.setAutoCommit(false);
			masterSave(conn);
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			try {
				ut.rollback();
			} catch (IllegalStateException e2) {
				if (!e2.toString().equals("java.lang.IllegalStateException: no transaction started on this thread")) {
					logger.error(null, e2);
				}
			}
		}
		
	}
	
	/** 執行存檔 */
	@SuppressWarnings("unchecked")
	public void masterSave(Connection conn) throws Exception {
		UserTransaction ut = null;		
		try {	
			ut = TransactionManagerServices.getTransactionManager();
			/* 檢查控件 */
			if (!BlackBox.checkComponent(this))
				return;

			/* 儲存 Start */
			Object selectedMaster = null;
			selectedMaster = getMasterSel();

			if (data_Mode.equals(DataMode.CREATE_MODE)) {
				Class param[] = new Class[0];
				Constructor masterConstructor = null;
				masterConstructor = getEntityClass().getConstructor(param);
				Object masterEntity = masterConstructor.newInstance(param); 

				try {
					if (writeMethod(masterEntity) && beforeMasterSave(masterEntity) && BlackBox.CheckPK(this, masterEntity) && BlackBox.validate(masterEntity) && doCustomSave(conn)) {
						BlackBox.executeSave(this, masterEntity, conn, ut);
						masterModel.add(masterEntity);
						refreshListbox(masterEntity);	// 定位到此筆資料。
						Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"), "Information", Messagebox.OK, Messagebox.INFORMATION);
					} else {
						logger.info("masterSave() : writeMethod(masterEntity) && beforeMasterSave(masterEntity) && BlackBox.CheckPK(this, masterEntity) && BlackBox.validate(masterEntity) && doCustomSave(conn) is false");
						return;
					}
				} catch (Exception ex) {
					conn.rollback();
					logger.error("onClickbtnSave(Create) : ", ex);
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0089"), "Error", Messagebox.OK, Messagebox.ERROR);
					throw ex;
				} finally {
					BlackBox.close(conn);
				}
			} else if (data_Mode.equals(DataMode.UPDATE_MODE)) {
				try {
					if (writeMethod(masterSel) && beforeMasterSave(masterSel) && BlackBox.CheckPK(this, masterSel) && BlackBox.validate(masterSel) && doCustomSave(conn)) {		
						BlackBox.executeSave(this, masterSel, conn, ut);
						if (bInLineEdit == true) {
							setSqlConditions();
							BlackBox.setInLineListbox(this, masterSel, false);
						}

						masterModel.set(masterModel.indexOf(masterSel), masterSel);
						refreshListbox(masterSel);	// 定位到此筆資料。
						Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"), "Information", Messagebox.OK, Messagebox.INFORMATION);
					} else {
						logger.info("onClickbtnSave(Update) : writeMethod(masterSel) && beforeMasterSave(masterSel) && BlackBox.CheckPK(this, masterSel) && BlackBox.validate(masterSel) && doCustomSave(conn) is false");
						return;
					}
				} catch (Exception ex) {
					conn.rollback();
					logger.error(null, ex);
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0088"), "Error", Messagebox.OK, Messagebox.ERROR);
					throw ex;
				} finally {
					BlackBox.close(conn);
				}
			}
			/* 儲存 End */

			data_Mode = DataMode.READ_MODE;
			masterReadMode(null);
			if (data_Mode.equals(DataMode.CREATE_MODE)) {
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
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param objMasterSel 資料物件
	 * @return 成功回傳true, 失敗回傳false
	 */
	public boolean writeMethod(Object objMasterSel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		if (masterComponentColumns.size() > 0){
			return writeMethod(objMasterSel, masterComponentColumns);
		}
		return false;
	}
	
	/**
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param objSel 資料物件
	 * @param comColumns 寫入資料之欄位
	 * @return 成功回傳true, 失敗回傳false
	 */
	public boolean writeMethod(Object objSel, List<ComponentColumn> comColumns) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		return writeMethod(ENTITY_CLASSNAME, objSel, comColumns);
	}

	/**
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param className
	 * @param objSel 資料物件
	 * @param comColumns 寫入資料之欄位
	 * @return 成功回傳true, 失敗回傳false
	 */
	public boolean writeMethod(String className, Object objSel, List<ComponentColumn> comColumns) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		return BlackBox.writeMethod(this, className, objSel, comColumns);
	}
	

	/*********************************************************************************************************/
	/** LISTBOX ACTION Start */
	/*********************************************************************************************************/

	/** listbox 上的瀏覽功能, 大都會撘配開窗 */
	@SuppressWarnings("unchecked")
	@Listen("onView = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void viewMasterlistbox(ForwardEvent event) {
		try {
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE)) {
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				}
			}

			if (data_Mode.equals(DataMode.UPDATE_MODE) || data_Mode.equals(DataMode.CREATE_MODE))
				return;

			if (!getMasterUpdateZul().isEmpty() && getMasterUpdateZul() != null) {
				Object masterEntity = listbox.getSelectedItem().getValue();
				setMasterSel(masterEntity);

				if (getMasterCreateMap() != null) {
					mapOpenWin = new HashMap<String, Object>(getMasterCreateMap());
				}
				mapOpenWin.put("selectedRecord", masterEntity);
				mapOpenWin.put("recordMode", DataMode.READ_MODE);
				mapOpenWin.put("parentWindow", getRootWindow());
				mapOpenWin.put("masterModel", null);
				mapOpenWin.put("detailModel", getMasterModel());
				Executions.createComponents(getMasterUpdateZul(), null, mapOpenWin);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** listbox 修改鈕, 有區分作業模式 */
	@SuppressWarnings("unchecked")
	@Listen("onUpdate = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void editMasterListbox(ForwardEvent evt) throws Exception {
		try {
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE)) {
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				}
			}
			if (data_Mode.equals(DataMode.CREATE_MODE) || data_Mode.equals(DataMode.UPDATE_MODE))
				return;

			Object masterEntity = getMasterSel();

			// 複製物件
			Object objEntity;
			if (getEntityClass() != null) {
				objEntity = getEntityClass().newInstance();
				BeanUtils.copyProperties(masterSel, objEntity);
				masterSelBeforeNew = objEntity;
			}

			data_Mode = DataMode.UPDATE_MODE;
			switch (getOperationMode()) {
			case NORMAL:
				masterCreateMode(null);
				if (bInLineEdit == true) {
					setSqlConditions();
					BlackBox.setInLineListbox(this, masterEntity, true);
				}
				break;
			case OPENWIN:
				if (getMasterCreateMap() != null) {
					mapOpenWin = new HashMap<String, Object>(getMasterCreateMap());
				}
				mapOpenWin.put("selectedRecord", masterEntity);
				mapOpenWin.put("recordMode", DataMode.UPDATE_MODE);
				mapOpenWin.put("parentWindow", getRootWindow());
				mapOpenWin.put("masterModel", getMasterModel());
				mapOpenWin.put("detailModel", null);
				Executions.createComponents(getMasterUpdateZul(), null, mapOpenWin);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** listbox 刪除鈕 */
	@SuppressWarnings("unchecked")
	@Listen("onDelete = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void onDeleteMasterListbox(ForwardEvent evt) {
		try {
			for (int i = 0; i < arrDetailPrograms.size(); i++) {
				if (!arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE)) {
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				}
			}
			if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE))
				return;

			Object masterEntity = listbox.getSelectedItem().getValue();
			setMasterSel(masterEntity);
			if (beforeMasterDel(masterEntity) == false)
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
								doDelete(masterEntity, conn, ut);
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

	/**
	 * 執行刪除功能
	 * @param pDeletetMaster 資料物件
	 * @param conn 連線
	 * @param UserTransaction 
	 * @throws Exception
	 */
	public void doDelete(Object pDeletetMaster, Connection conn, UserTransaction ut) throws Exception {
		BlackBox.doDelete(this, pDeletetMaster, conn, ut);
	}

	/** 點擊存檔按鈕 */
	@Listen("onSave = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public boolean onSaveMasterListbox(ForwardEvent evt) {
		try {
			masterSave();
			return true;
		} catch (Exception e) {
			logger.error(null, e);
			return false;
		} finally {
			arrMulitSelect.clear();
		}
	}

	/** listbox 取消鈕 */
	@SuppressWarnings("incomplete-switch")
	@Listen("onCancel = #masterListbox,#master2Listbox,#master3Listbox,#master4Listbox,#master5Listbox,#master6Listbox,#master7Listbox,#master8Listbox,#master9Listbox,#master10Listbox,#master11Listbox,#master12Listbox,#master13Listbox,#master14Listbox,#master15Listbox,#master16Listbox,#master17Listbox,#master18Listbox,#master19Listbox,#master20Listbox")
	public void onCancelMasterListbox(ForwardEvent evt) {
		try {
			switch (data_Mode) {
			case CREATE_MODE:
				int index = 0;
				if (bInLineEdit == true) {
					if (bInsertCurrent == true) {
						index = masterModel.indexOf(masterSelBeforeNew)+1;
						masterModel.remove(index);		
					} else {
						masterModel.remove(index);
					}
				}

				setMasterSel(masterSelBeforeNew);
				data_Mode = DataMode.READ_MODE;
				BlackBox.programmacticallySelectListbox(this, masterModel.indexOf(masterSelBeforeNew));
				break;
			case UPDATE_MODE:
				data_Mode = DataMode.READ_MODE;
				refreshListbox(masterSel);
				masterReadMode(null);
				break;
			}
			
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			arrMulitSelect.clear();
		}
	}


	/*********************************************************************************************************/
	/** LISTBOX ACTION End */
	/*********************************************************************************************************/

	/** 編輯開窗回到父視窗後, 重新定位listbox */
	@SuppressWarnings({ "unchecked" })
	@Listen("onMasterSaved = Window")
	public void onMasterSaved(Event event) {
		try{
			Map<String, Object> args = (Map<String, Object>) event.getData();
			DataMode recordMode = (DataMode) args.get("recordMode");
			Object masterEntity = args.get("selectedRecord");

			if (masterEntity != null) {
				switch (recordMode) {
				case CREATE_MODE:
					masterModel.add(masterEntity);
					break;
				case UPDATE_MODE:
					masterModel.set(masterModel.indexOf(masterSel), masterEntity);
					break;
				default:
					break;
				}
				refreshListbox(masterEntity);
			}

			data_Mode = DataMode.READ_MODE;
			masterReadMode(null);
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
	
	/** 執行選擇litstbox之index動作 */
	public void programmacticallySelectListbox(int index) throws Exception {
		try {
			BlackBox.programmacticallySelectListbox(this, index);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	
	/** 點擊listbox listheader排序 */
	@Listen("onClick = #masterListbox listheader, #master2Listbox listheader, #master3Listbox listheader, #master4Listbox listheader, #master5Listbox listheader, #master6Listbox listheader, #master7Listbox listheader, #master8Listbox listheader, #master9Listbox listheader, #master10Listbox listheader, #master11Listbox listheader, #master12Listbox listheader, #master13Listbox listheader, #master14Listbox listheader, #master15Listbox listheader, #master16Listbox listheader, #master17Listbox listheader, #master18Listbox listheader, #master19Listbox listheader, #master20Listbox listheader")
	public void onClickListheader(Event event) {
		BlackBox.orderByListheader(this, event);
	}
	
	/**
	 * 存檔前檢查
	 * @param entityMaster 資料物件
	 * @return 檢查通過:True, 不通過:False
	 */
	protected boolean beforeMasterSave(Object entityMaster) {
		return BlackBox.beforeSave(this, entityMaster);
	}
	
	/**
	 * 刪除前的檢查 
	 * @param entityMaster 資料物件
	 * @return 檢查通過:True, 不通過:False
	 */
	protected boolean beforeMasterDel(Object entityMaster) {
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
	
	
	/************************************get/set area************************************/
	/** 取得目前次檔Index */
	public int getIndexDetail() {
		return indexDetail;
	}

	/** 設定次檔index */
	public void setIndexDetail(int indexDetail) {
		this.indexDetail = indexDetail;
	}

	/** 設定次檔index及次檔數量 */
	public void setDetail(int DetailCount) {
		this.indexDetail = DetailCount - 1;
		this.DetailCount = DetailCount;
	}
	
	/** 取得次檔作業List */
	public List<Detail> getArrDetailPrograms() {
		return arrDetailPrograms;
	}

	/** 設定次檔作業List */
	public void setArrDetailPrograms(List<Detail> arrDetailPrograms) {
		this.arrDetailPrograms = arrDetailPrograms;
	}
	
	/** 取得主檔作業Listbox model */
	public ListModelList getMasterModel() {
		return masterModel;
	}
	
	/** 設定主檔作業Listbox model */
	public void setMasterModel(ListModelList masterModel) {
		this.masterModel = masterModel;
	}

	/** 取得主檔選擇之資料物件 */
	public Object getMasterSel() {
		return masterSel;
	}
	
	/** 設定主檔選擇之資料物件, 並將取得的資料內容放到控件上 */
	public void setMasterSel(Object masterSel) {
		this.masterSel = masterSel;
		resetEditAreaMaster(masterSel);
	}

	/** 取得資料模式 */
	public DataMode getData_Mode() {
		return data_Mode;
	}
	
	/** 設定資料模式 */
	public void setData_Mode(DataMode data_Mode) {
		this.data_Mode = data_Mode;
	}	

	/** 取得計算資料筆數Query */
	public Query getQueryResultSize() {
		return queryResultSize;
	}
	
	/** 設定計算資料筆數Query */
	public void setQueryResultSize(Query queryResultSize) {
		this.queryResultSize = queryResultSize;
	}	
	
	/** 取得查詢資料Query */
	public Query getQueryPaging() {
		return queryPaging;
	}
	
	/** 設定查詢資料Query */
	public void setQueryPaging(Query queryPaging) {
		this.queryPaging = queryPaging;
	}
	
	/** 取得查詢PK欄位Query */
	public Query getQueryOneCol() {
		return queryOneCol;
	}
	
	/** 設定查詢PK欄位Query */
	public void setQueryOneCol(Query queryOneCol) {
		this.queryOneCol = queryOneCol;
	}
	
	/** 取得查詢資料區間Query */
	public Query getQueryPagingbySize() {
		return this.queryPagingbySize;
	}
	
	/** 設定查詢資料區間 */
	public void setQueryPagingbySize(int iStart) {
		this.queryPagingbySize = this.queryPaging.setFirstResult(iStart).setMaxResults(MY_PAGE_SIZE);
	}
	
	/** 取得按鈕Component */
	public HashMap<String, Object> getButtons() {
		return buttons;
	}	

	public void setRegular(boolean pRegular) {
		this.bRegular = pRegular;
	}
	
	/** 取得作業之Listbox Component */
	public Listbox getmasterListbox() {
		return listbox;
	}
	
	/** 取得新增資料時, 資料插入位置, true為所選取資料之下方  */
	public boolean getInsertCurrent() {
		return bInsertCurrent;
	}
	
	/** 設定新增資料時, 資料插入位置, true為所選取資料之下方  */
	public void setInsertCurrent(boolean bInsertCurrent) {
		this.bInsertCurrent = bInsertCurrent;
	}

	/** 取得Listbox欄位標題排序String */
	public String getOrderByListhead() {
		return sOrderByListhead;
	}
	
	/** 取得新增開窗的ZUL檔路徑 */
	protected String getMasterCreateZul(){
		return null;
	}
	
	/** 取得編輯開窗的ZUL檔路徑 */
	protected String getMasterUpdateZul(){
		return null;
	}
	
	/** 取得新增開窗所傳入的參數 */
	protected HashMap getMasterCreateMap(){
		return null;
	}
	
	/** 取得where條件 */
	protected String getWhereConditionals() {
		String sWere=" AND 1=1 ";
		return sWere;
	}
	
	/** 取得查詢開窗的ZUL檔路徑 */
	protected String getMasterQueryZul() {
		return null;
	}
	
	/** 取得分頁ID, 預設為pagingMaster */
	protected String getPagingId() {
		return "pagingMaster";
	}
	
	/** 取得作業型態 */
	protected OperationMode getOperationMode() {
		return OperationMode.NORMAL;
	}
	
	/** 取得給予Entity的名稱 */
	protected String getEntityName() {
		return ENTITY_CLASSNAME;
	}
	
	/** 取得分頁筆數, 預設為0=一頁5筆資料*/
	protected int getPageSize() {
		return 0;
	}
	
	/** 取得次檔的CLASS(@program) */
	protected Class getDetailClass(int indexDetail) {
		return getDetailProgram(indexDetail).getClass();
	}

	/** 取得次檔的Detail */
	protected Detail getDetailProgram(int indexDetail) {
		return getArrDetailPrograms().get(indexDetail);
	}

	/** 設定所擁有的次檔 */
	protected void addDetailPrograms() {

	}

	/** 取得次檔的查詢條件(SQL) */
	protected String getDetailConditionals() {
		return null;
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

	/** 取得PK欄位名稱 */
	protected ArrayList<String> getMasterKeyName() {
		return BlackBox.getKeyName(getEntityClass());
	}
	
	/** 取得PK欄位資料 */
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		return BlackBox.getKeyValue(this, entityMaster);
	}
	
	/** 取得listbox多選物件List*/
	public ArrayList<Object> getListboxMulitSelect() {
		return arrMulitSelect;
	}
	
	/** 設定listbox多選物件List*/
	public void setListboxMulitSelect(ArrayList<Object> arrList) {
		this.arrMulitSelect = arrList;
	}
	
	/** 取得是否為多選Listbox */
	public boolean getListboxMulit(){
		return bMultiple;
	}
	
	/** 設定是否為多選Listbox */
	public void setListboxMulit(Boolean bMultiple) {
		this.bMultiple = bMultiple;
	}
	
	/** 取得ResultSizeBase SQL */
	public String getsSqlResultSizeBase() {
		return sSqlResultSizeBase;
	}
	
	/** 取得PagingBase SQL */
	public String getsSqlPagingBase() {
		return sSqlPagingBase;
	}
	
	/** 取得OneColBase SQL */
	public String getsSqlOneColBase() {
		return sSqlOneColBase;
	}
	
	/** 取得分頁Compoment */
	public Paging getMasterPaging() {
		return masterPaging;
	}
	
	/** 取得分頁開始位置 */
	public int getStartPosition() {
		return iStartPosition;
	}
	
	/** 取得是否為自訂SQL語法 */
	public boolean getIsCustList() {
		return bIsCustList;
	}
	
	/** 取得自訂分頁筆數 */
	public int getMY_PAGE_SIZE() {
		return MY_PAGE_SIZE;
	}
	
	/** 設定自訂分頁筆數 */
	public void setMY_PAGE_SIZE(int mY_PAGE_SIZE) {
		MY_PAGE_SIZE = mY_PAGE_SIZE;
	}

}
