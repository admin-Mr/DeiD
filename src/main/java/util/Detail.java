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
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

import bitronix.tm.TransactionManagerServices;
import ds.common.services.CRUDService;
import ds.common.services.ProgramAuth;
import ds.common.services.UserCredential;

@SuppressWarnings({ "rawtypes" })
public abstract class Detail extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	/**	記錄log */
	protected Logger logger = BlackBox.getLogger(this);
	
	@WireVariable
	protected CRUDService CRUDService;
	
	/** 開窗編輯所傳入之map */
	protected HashMap<String, Object> mapOpenWin = new HashMap<String, Object>();
	
	/**	listbox model */
	protected ListModelList detailModel; // ListModelList<?> don't use
	
	/**	detailSel為所選取之資料物件,  detailSelBeforeNew為為所選取前之資料物件 */
	protected Object detailSel, detailSelBeforeNew;
	
	/**	對應元件資料與欄位名稱，組SQL使用 */
	protected List<ComponentColumn> detailComponentColumns = new ArrayList<ComponentColumn>();
	
	/** java.class名(@domain), 為作業的主要entity name */
	protected String ENTITY_CLASSNAME;
	
	/** java.class名(@domain)檔案路徑 */
	protected String ENTITY_PATH;
	
	/** 資料模式 */
	private DataMode data_Mode = DataMode.READ_MODE;
	
	/** 作業之Listbox Component */
	protected Listbox listbox;
	
	/** listbox資料清單 */
	protected List detailList;
	
	/** 作業按鈕清單 */
	private HashMap<String, Object> buttons = null;

	/** 登入者資訊 */
	protected final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");  //登入者資訊
	
	/** 作業權限 */
	public ProgramAuth prgAuth;
	
	/** 主檔window */
	Window parentWindow;
	
	/** 主檔程式(Master) */
	Master masterProgram;
	
	/** 主檔程式(OpenWinCRUD) */
	OpenWinCRUD opencrudProgram;
	
	/** 分頁 */
	Paging detailPaging;
	
	/** queryResultSizeDetail 計算資料筆數, queryPagingDetail 查詢資料, queryOneColDetail 查詢PK欄位, queryPagingbySize 設定查詢資料區間*/
	Query queryResultSizeDetail, queryPagingDetail, queryOneColDetail, queryPagingbySize;

	/** 取得ResultSizeBase SQL */
	String sSqlResultSizeBase;
	
	/** 取得PagingBase SQL */
	String sSqlPagingBase;
	
	/** 取得OneColBase SQL */
	String sSqlOneColBase;
	
	/** listbox多選物件List */
	ArrayList<Object> arrMulitSelect = new ArrayList<Object>();

	/** 主檔是否為底層openWinCRUD */
	boolean IsOpenWinCRUD = false;
	
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
	
	/** 是否為多選 */
	private Boolean bMultiple = false;
	
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
	 * 取得java.class名(@program) => DETAIL_CLASSNAME
	 * @return java class
	 */
	abstract public Class getDetailClass();
	
	/**
	 * 將取得的資料內容放到控件上
	 * @param entityDetail 資料物件
	 */
	abstract protected void resetEditAreaDetail(Object entityDetail);
	
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

	/**Detail設定**/
//	abstract protected Div getRootDiv();	// 取得此作業的Div id
//	abstract protected ArrayList<String> getDetailKeyName();	// 取得次檔的PK Column Name
//	abstract protected ArrayList<String> getDetailKeyValue(Object entityDetail);	// 取得次檔的 PK Column Value
//	abstract protected OperationMode getOperationMode();	// 取得作業模式
//	abstract protected String getPagingIdDetail();	// 取得分頁ID
//	abstract protected String getDetailCreateZul();	// 新增開窗的ZUL檔路徑
//	abstract protected HashMap getDetailCreateMap();	// 取得自訂的開窗參數
//	abstract protected String getDetailUpdateZul();	// 修改開窗的ZUL檔路徑
//	abstract protected String getWhereConditionals();	// 取得次檔的查詢條件(SQL)
//	abstract protected boolean beforeDetailSave(Object entityDetail);	// 存檔前的檢查
//	abstract protected boolean beforeDetailDel(Object entityDetail);	// 刪除前的檢查
//	abstract protected int getPageSize();	// 自訂Listbox的分頁數
//	abstract protected boolean doCustomSave(Connection conn);	//若有其他table需一併儲存使用
	
	public Detail() {
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
			Execution execution = Executions.getCurrent();
			_init(execution);
			_initAuthority(window);
			sOrderByListhead = getOrderByDetail();

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
			if (comp.getId().indexOf("detail") == 0)
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
	public void _init(Execution execution) {
		try {
			if (execution.getArg().get("masterType") == null) {
				IsOpenWinCRUD = false;
				Master masterProgram = (Master) execution.getArg().get("masterProgram");
				setMasterProgram(masterProgram);
			} else if (((String) execution.getArg().get("masterType")).equals("OPENWIN")) {
				IsOpenWinCRUD = true;
				setOpenWinProgram((OpenWinCRUD) execution.getArg().get("masterProgram"));
			}

			Window windowParent = (Window) execution.getArg().get("parentWindow");
			setParentWindow(windowParent);
			int indexDetailProgram = (Integer) execution.getArg().get("indexDetailProgram");

			if (IsOpenWinCRUD)
				opencrudProgram.getArrDetailPrograms().add(indexDetailProgram, this);
			else
				masterProgram.getArrDetailPrograms().add(indexDetailProgram, this);

			// 底層設定entity ENTITY_CLASSNAME and ENTITY_PATH
			BlackBox.setEntityClassPath(this);

			if (!getPagingIdDetail().equals("")) {
				if (getRootWindow() != null)
					detailPaging = (Paging) getRootWindow().getFellow(getPagingIdDetail());
				else
					detailPaging = (Paging) getRootDiv().getFellow(getPagingIdDetail()); //舊的用法
			}

			prgAuth = (ProgramAuth) execution.getArg().get("prgAuth");
			Session sessAuth = Sessions.getCurrent();
			sessAuth.setAttribute("RAuth", prgAuth == null ? false : prgAuth.getAuthQuery());
			sessAuth.setAttribute("UAuth", prgAuth == null ? false : prgAuth.getAuthEdit());
			sessAuth.setAttribute("DAuth", prgAuth == null ? false : prgAuth.getAuthDelete());
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 瀏覽模式
	 * @param objButton
	 */
	public void detailReadMode(Object objButton) {
		if (objButton != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("btncreatedetail", objButton);
			detailReadMode(map);
		}
	}
	
	/**
	 * 瀏覽模式
	 * @param mapButton 控管之元件
	 */
	public void detailReadMode(HashMap<String, Object> mapButton) {
		try {
			// johnny add 用以離開作業時，判斷data_mode
			Sessions.getCurrent().setAttribute("DataMode", data_Mode);

			if (mapButton != null) {
				buttons = mapButton;
			}

			// johnny 20171031 設定次檔按鈕
			DataMode dataMode = null;
			if (IsOpenWinCRUD) {
				dataMode = getOpenWinProgram().getRecordMode() == null ? DataMode.READ_MODE : getOpenWinProgram().getRecordMode();
			} else {
				dataMode = getMasterProgram().getData_Mode() == null ? DataMode.READ_MODE : getMasterProgram().getData_Mode();
			}

			if (dataMode.equals(DataMode.READ_MODE)) {
				BlackBox.setButtonAuth(this, false);
			} else {
				BlackBox.setButtonAuth(this, true);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 編輯模式, 包含新增及修改
	 * @param objButton
	 */
	public void detailCreateMode(Object objButton) {
		if (objButton != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("btncreatedetail", objButton);
			detailCreateMode(map);
		}
	}
	
	/**
	 *  編輯模式, 包含新增及修改
	 * @param mapButton 控管之元件
	 */
	public void detailCreateMode(HashMap<String, Object> mapButton) {
		try {
			// johnny add 用以離開作業時，判斷data_mode
			Sessions.getCurrent().setAttribute("DataMode", data_Mode);

			BlackBox.setButtonAuth(this, false);

			if (IsOpenWinCRUD) {
				// johnny 20171031 設定主檔按鈕
				BlackBox.setButtonAuth(opencrudProgram, true);
				BlackBox.setOtherDetailButton(opencrudProgram, this);
			} else {
				// johnny 20171031 設定主檔按鈕
				BlackBox.setButtonAuth(masterProgram, true);
				BlackBox.setOtherDetailButton(masterProgram, this);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}	


	/**
	 * listbox onSelect, 原本為onClick事件，改更onSelect，因此function為public，改原fucntion name 不變更
	 */
	@Listen("onSelect = #detailListbox, #detail2Listbox, #detail3Listbox, #detail4Listbox, #detail5Listbox, #detail6Listbox, #detail7Listbox, #detail8Listbox, #detail9Listbox,#detail10Listbox,#detail11Listbox, #detail12Listbox, #detail13Listbox, #detail14Listbox, #detail15Listbox, #detail16Listbox, #detail17Listbox, #detail18Listbox, #detail19Listbox, #detail20Listbox")
	public void clickDetailListbox(Event event) {
		try {
			Listitem listitem = (Listitem) ((SelectEvent) event).getReference();
			if (listbox.getSelectedItem() != null) {
				if (data_Mode.equals(DataMode.READ_MODE)) {
					onClickDetailListbox(listitem.getValue());
				}
			}
			BlackBox.listboxMulitSel(this, event);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * listbox onClick action 把主檔的資料放到detailSel, 並記錄選擇資料
	 */
	public void onClickDetailListbox(Object ocvo) {
		try {
			setDetailSel(ocvo);
			
			if (getData_Mode().equals(DataMode.READ_MODE)) {
				detailReadMode(null);
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
	 * 重新整理listbox
	 * @param masterEntity
	 * @throws Exception 
	 */
	public void refreshListbox(Object detailEntity) throws Exception {
		try {
			// 20170607 johnny 重新查詢，使得有join欄位取得資料
			setWhereConditionals(getWhereConditionals());
			BlackBox.setPosition(this, detailEntity);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}	
	
	/*********************************************************************************************************/

	/** LISTBOX INIT Start */
	/*********************************************************************************************************/
	
	/** 計算資料筆數SQL */
	public String getQueryResultSizeDetailBase() {
		// johnny modify 避免因多個key值，造成需override
		return "Select COUNT(1) From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
	}

	/** 查詢資料SQL */
	public String getQueryPagingDetailBase() {
		if (bIsCustList) // johnny 20170926
			return "Select * From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
		else
			return "Select t From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
	}

	/** 查詢PK欄位SQL */
	public String getQueryOneColDetailBase() {
		String strFields = "";
		for (int i = 0; i < getDetailKeyName().size(); i++) {
			strFields += ",t." + getDetailKeyName().get(i);
		}
		// johnny 20170926 modify
		return "Select " + strFields.substring(1) + " From " + ENTITY_CLASSNAME.replace("VO", "") + " t ";
	}

	/** 排序條件 */
	@SuppressWarnings("unused")
	public String getOrderByDetail() {
		String strFields = "";
		for (int i = 0; i < getDetailKeyName().size(); i++) {
			strFields += ",t." + getDetailKeyName().get(i);
		}
		return " Order by " + strFields.substring(1);
	}
	
	/**	執行查詢 */
	public void executeQuery() {
		arrMulitSelect.clear();
		doFillListboxDetail(0);
	}
	
	/** 設定SQL查詢條件 */
	private void setSqlConditions() {
		setWhereConditionals(getWhereConditionals());
		setQueryPagingbySize(iStartPosition);		
	}
	
	/** 取得主次檔Join欄位名稱 */
	protected HashMap<String, String> getJoinColumn() {
		return null;
	}
	
	/** 設定SQL查詢條件 */
	public void setWhereConditionals(String sWhere) {
		try {
			String sQueryResultSizeBase = getQueryResultSizeDetailBase();
			String sQueryPagingBase = getQueryPagingDetailBase();
			String sQueryOneColBase = getQueryOneColDetailBase();
			String sOrderBy = getOrderByDetail(), sKeyField = "";

			Object ocvo = null;
			String masterKey = "";

			// 判斷master是否有資料，有的話則將detail帶入，無則否
			if (IsOpenWinCRUD) {
				if (opencrudProgram.getParentSel() != null) {
					ocvo = opencrudProgram.getParentSel();
					for (int i = 0; i < opencrudProgram.getKeyName().size(); i++) {
						if (getJoinColumn() == null) {
							masterKey += " and t." + opencrudProgram.getKeyName().get(i) + " = '"
									+ opencrudProgram.getKeyValue(ocvo).get(i) + "'";						
						} else {
							masterKey += " and t." + getJoinColumn().get(opencrudProgram.getKeyName().get(i)) + " = '"
									+ opencrudProgram.getKeyValue(ocvo).get(i) + "'";							
						}
					}
				}
			} else {
				if (masterProgram.getMasterSel() != null) {
					ocvo = masterProgram.getMasterSel();
					for (int i = 0; i < masterProgram.getMasterKeyName().size(); i++) {
						if (getJoinColumn() == null) {
							masterKey += " and t." + masterProgram.getMasterKeyName().get(i) + " = '"
									+ masterProgram.getMasterKeyValue(ocvo).get(i) + "'";
						} else {
							masterKey += " and t." + getJoinColumn().get(masterProgram.getMasterKeyName().get(i)) + " = '"
									+ masterProgram.getMasterKeyValue(ocvo).get(i) + "'";
						}
					}
				}
			}

			for (int i = 0; i < getDetailKeyName().size(); i++) {
				sKeyField += ",t." + getDetailKeyName().get(i);
			}

			if (sWhere == null)
				sWhere = "";

			if (sOrderBy.equals("") || sOrderBy == null) {
				sOrderBy = " Order by " + sKeyField.substring(1);
			}

			String[] ss = new String[3];
			// 沒有主檔
			if (masterKey.isEmpty()) {
				ss[0] = sQueryResultSizeBase + " where 1=0 ";
				ss[1] = sQueryPagingBase + " where 1=0 ";
				ss[2] = sQueryOneColBase + " where 1=0 ";
			} else {
				ss[0] = sQueryResultSizeBase + " where 1=1 " + masterKey + sWhere;
				ss[1] = sQueryPagingBase + " where 1=1 " + masterKey + sWhere + sOrderBy;
				ss[2] = sQueryOneColBase + " where 1=1 " + masterKey + sWhere + sOrderBy;
			}

			sSqlResultSizeBase = ss[0];
			sSqlPagingBase = ss[1];
			sSqlOneColBase = ss[2];

			if (bIsCustList) { // johnny 20170926
				queryResultSizeDetail = CRUDService.createSQL(ss[0]);
				queryPagingDetail = CRUDService.createSQL(ss[1]);
				queryOneColDetail = CRUDService.createSQL(ss[2]);
			} else {
				queryResultSizeDetail = CRUDService.getBetweenByLimit2(ss[0]);
				queryPagingDetail = CRUDService.getBetweenByLimit2(ss[1]);
				queryOneColDetail = CRUDService.getBetweenByLimit2(ss[2]);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 將查詢資料，使用listbox呈現
	 * model最多只有5筆(預設由Common.PAGE_SIZE設定，可自行設定MY_PAGE_SIZE)資料。 次檔listbox : 同上。
	 * @param index 在整個資料物件集合中的索引位置。
	 */
	@SuppressWarnings("unchecked")
	public void doFillListboxDetail(int index) {
		try {
			arrMulitSelect.clear();
			MY_PAGE_SIZE = (getPageSize() <= 0 ? Common.PAGE_SIZE : getPageSize());

			int activePage = (int) (index / MY_PAGE_SIZE);
			iStartPosition = activePage * MY_PAGE_SIZE;

			setSqlConditions();
			if (bIsCustList) {
				detailList = getCustList();
			} else {
				detailList = getQueryPagingbySize().getResultList();
			}

			detailModel = new ListModelList(detailList, true);
			getDetailListbox().invalidate();
			getDetailListbox().setModel(detailModel);
			getDetailListbox().renderAll();
			detailModel.setMultiple(getListboxMulit());

			detailPaging.setPageSize(MY_PAGE_SIZE);
			Long totalSize;
			if (bIsCustList) {
				totalSize = ((BigDecimal) this.queryResultSizeDetail.getSingleResult()).longValue();
			} else {
				totalSize = (Long) this.queryResultSizeDetail.getSingleResult();
			}

			detailPaging.setTotalSize((int) (long) totalSize);
			// 移除listeners
			Iterable<EventListener<? extends Event>> eventListenersDetail = detailPaging.getEventListeners(ZulEvents.ON_PAGING);
			for (EventListener<? extends Event> eventListener : eventListenersDetail) {
				detailPaging.removeEventListener(ZulEvents.ON_PAGING, eventListener);
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

						if (bIsCustList) {
							detailList = getCustList();
						} else {
							detailList = getQueryPagingbySize().getResultList();
						}

						detailModel = new ListModelList(detailList, true);
						detailModel.setMultiple(bMultiple);

						if (arrMulitSelect.size() > 0) {
							for (int i = 0; i < arrMulitSelect.size(); i++) {
								for (int j = 0; j < detailModel.size(); j++) {
									if (BlackBox.getDiffField(arrMulitSelect.get(i), detailModel.get(j)) == null) {
										item.add(detailModel.get(j));
									}
								}
							}
						}

						detailModel.setSelection(item);
						// 每次分頁都抓5筆資料到model 20160324 tiger
						getDetailListbox().invalidate();
						getDetailListbox().setModel(detailModel);
						getDetailListbox().renderAll();
						
						if (getListboxMulit() == false)
							programmacticallySelectListbox(0);

						detailReadMode(null);
					} catch (Exception e) {
						logger.error(null, e);
						throw e;
					}
				}
			};

			detailPaging.addEventListener(ZulEvents.ON_PAGING, el);
			detailPaging.setActivePage(activePage);

			try {
				programmacticallySelectListbox(index % MY_PAGE_SIZE);
			} catch (Exception e) {
				logger.error(null, e);
			}
			
			DataMode dataMode = null;
			if (IsOpenWinCRUD) {
				dataMode = getOpenWinProgram().getRecordMode() == null ? DataMode.READ_MODE : getOpenWinProgram().getRecordMode();
			} else {
				dataMode = getMasterProgram().getData_Mode() == null ? DataMode.READ_MODE : getMasterProgram().getData_Mode();
			}
			
			if (dataMode.equals(DataMode.READ_MODE)) {
				if (IsOpenWinCRUD) {
					opencrudProgram.masterReadMode(null);
				} else {
					masterProgram.masterReadMode(null);
				}
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
	@Listen("onClick = #btnQueryDetail, #btnQueryDetail2, #btnQueryDetail3, #btnQueryDetail4, #btnQueryDetail5, #btnQueryDetail6, #btnQueryDetail7, #btnQueryDetail8, #btnQueryDetail9,#btnQueryDetail10,#btnQueryDetail11, #btnQueryDetail12, #btnQueryDetail13, #btnQueryDetail14, #btnQueryDetail15, #btnQueryDetail16, #btnQueryDetail17, #btnQueryDetail18, #btnQueryDetail19,#btnQueryDetail20")
	public void onClickbtnQuery(Event event) {
		try {
			switch (getOperationMode()) {
			case NORMAL:
			default:
				data_Mode = DataMode.READ_MODE;
				executeQuery();
				break;
			case OPENWIN:
				if (getDetailCreateMap() != null) {
					mapOpenWin = new HashMap<String, Object>(getDetailCreateMap());
				}
				mapOpenWin.put("selectedRecord", detailModel);
				mapOpenWin.put("recordMode", DataMode.READ_MODE);
				mapOpenWin.put("parentWindow", getRootWindow());
				Executions.createComponents(getDetailQueryZul(), null, mapOpenWin);
				break;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	
	/** 新增鈕,有區分作業模式執行 */
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnCreateDetail, #btnCreateDetail2, #btnCreateDetail3, #btnCreateDetail4, #btnCreateDetail5, #btnCreateDetail6, #btnCreateDetail7, #btnCreateDetail8, #btnCreateDetail9,#btnCreateDetail10,#btnCreateDetail11, #btnCreateDetail12, #btnCreateDetail13, #btnCreateDetail14, #btnCreateDetail15, #btnCreateDetail16, #btnCreateDetail17, #btnCreateDetail18, #btnCreateDetail19,#btnCreateDetail20")
	public void detailCreate(Event event) {
		try {
			if (IsOpenWinCRUD) {
				if (opencrudProgram.getParentSel() == null) {
					//請選擇要新增的資料列				
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					return;			
				}			
			} else {
				if (masterProgram.getMasterSel() == null) {
					//請選擇要新增的資料列				
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					return;			
				}
			}

			data_Mode = DataMode.CREATE_MODE;
			switch (getOperationMode()) {
			case NORMAL:
				if (detailModel.indexOf(getDetailSel()) != -1)
					detailSelBeforeNew = detailModel.get(detailModel.indexOf(getDetailSel()));
				else
					detailSelBeforeNew = null;

				if (bInLineEdit) {				
					try {
						int index = 0;
						if (bInsertCurrent) {
							index = detailModel.indexOf(detailSel)+1;
							detailModel.add(index, Class.forName(ENTITY_PATH).newInstance());
						} else {
							detailModel.add(index, Class.forName(ENTITY_PATH).newInstance());							
						}						

						BlackBox.propertyWriteMethod(detailModel.get(index), "ISADD", true);  //johnny 20170926
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException e) {
						System.out.println("[ERROR] btnCreateDetail on Click: " + e.getMessage());
						e.printStackTrace();
					}				
				}

				setDetailSel(null);			
				doCreateDefault();
				detailCreateMode(null);
				break;
			case OPENWIN:
				if (getDetailCreateMap() != null) {
					mapOpenWin = new HashMap<String, Object>(getDetailCreateMap());
				}
				mapOpenWin.put("recordMode", DataMode.CREATE_MODE);
				//由mapOpenWin取得ParentWindow，避免資料互蓋
				//mapOpenWin.put("parentWindow", getParentWindow());  

				if (IsOpenWinCRUD)
					mapOpenWin.put("masterModel", opencrudProgram.modelDetail);
				else
					mapOpenWin.put("masterModel", masterProgram.masterModel);
				
				mapOpenWin.put("detailModel", getDetailModel());
				Executions.createComponents(getDetailCreateZul(), null, mapOpenWin);
				break;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 點擊修改鈕 */
	@Listen("onClick = #btnEditDetail,#btnEditDetail2,#btnEditDetail3,#btnEditDetail4,#btnEditDetail5,#btnEditDetail6,#btnEditDetail7,#btnEditDetail8,#btnEditDetail9,#btnEditDetail10,#btnEditDetail11,#btnEditDetail12,#btnEditDetail13,#btnEditDetail14,#btnEditDetail15,#btnEditDetail16,#btnEditDetail17,#btnEditDetail18,#btnEditDetail19,#btnEditDetail20")
	public void detailUpdate(Event event) throws Exception {			
		updateDetailListbox(null);
	}	
	
	/** 點擊刪除鈕 */
	@Listen("onClick = #btnDeleteDetail,#btnDeleteDetail2,#btnDeleteDetail3,#btnDeleteDetail4,#btnDeleteDetail5,#btnDeleteDetail6,#btnDeleteDetail7,#btnDeleteDetail8,#btnDeleteDetail9,#btnDeleteDetail10,#btnDeleteDetail11,#btnDeleteDetail12,#btnDeleteDetail13,#btnDeleteDetail14,#btnDeleteDetail15,#btnDeleteDetail16,#btnDeleteDetail17,#btnDeleteDetail18,#btnDeleteDetail19,#btnDeleteDetail20")
	public void masterDelete(Event event) throws Exception {		
		onDeleteDetailListbox(null);
	}
	
	
	/** 取消鈕(新增/編輯),把狀態回復到瀏覽狀態(READ_MODE) */
	@Listen("onClick = #btnCancelDetail, #btnCancelDetail2, #btnCancelDetail3, #btnCancelDetail4, #btnCancelDetail5, #btnCancelDetail6, #btnCancelDetail7, #btnCancelDetail8, #btnCancelDetail9,#btnCancelDetail10,#btnCancelDetail11, #btnCancelDetail12, #btnCancelDetail13, #btnCancelDetail14, #btnCancelDetail15, #btnCancelDetail16, #btnCancelDetail17, #btnCancelDetail18, #btnCancelDetail19, #btnCancelDetail20")
	public void detailCancel(Event event) throws Exception {
		onCancelDetailListbox(null);
	}

	/** 儲存鈕(新增/編輯), 會先檢查再做存檔 */
	@Listen("onClick = #btnSaveDetail, #btnSaveDetail2, #btnSaveDetail3, #btnSaveDetail4, #btnSaveDetail5, #btnSaveDetail6, #btnSaveDetail7, #btnSaveDetail8, #btnSaveDetail9,#btnSaveDetail10,#btnSaveDetail11, #btnSaveDetail12, #btnSaveDetail13, #btnSaveDetail14, #btnSaveDetail15, #btnSaveDetail16, #btnSaveDetail17, #btnSaveDetail18, #btnSaveDetail19, #btnSaveDetail20")
	public boolean detailSave(Event event) {		
		return onSaveDetailListbox(null);
	}
	
	
	/** 執行存檔 */
	public void detailSave() throws Exception {
		UserTransaction ut = TransactionManagerServices.getTransactionManager();
		try {
			ut.begin();
			Connection conn = BlackBox.getDbConnection(CRUDService.getEmf());
			conn.setAutoCommit(false);
			detailSave(conn);
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
	@SuppressWarnings("unchecked")
	public void detailSave(Connection conn) throws Exception {
		UserTransaction ut = null;
		
		try {
			ut = TransactionManagerServices.getTransactionManager();
			/* 檢查控件  Start */
			if (!BlackBox.checkComponent(this))
				return;
			/* 檢查控件  End */
			/* 儲存 Start */	

			if (data_Mode.equals(DataMode.CREATE_MODE)) {
				Class param[] = new Class[0];
				Constructor detailConstructor = null;
				detailConstructor = getDetailClass().getConstructor(param);
				Object detailEntity = detailConstructor.newInstance(param);

				try{
					if (writeMethod(detailEntity) && beforeDetailSave(detailEntity) && BlackBox.CheckPK(this, detailEntity) && BlackBox.validate(detailEntity) && doCustomSave(conn)) {						
						BlackBox.executeSave(this, detailEntity, conn, ut);						
						detailModel.add(detailEntity);
						refreshListbox(detailEntity);
						Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"), "Information", Messagebox.OK, Messagebox.INFORMATION);
					} else {
						logger.info("onClickbtnSave(Create) : writeMethod(detailEntity) && beforeDetailSave(detailEntity) && BlackBox.CheckPK(this, detailEntity) && BlackBox.validate(detailEntity) && doCustomSave(conn) is false");
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
				try{
					if (writeMethod(detailSel) && beforeDetailSave(detailSel) && BlackBox.CheckPK(this, detailSel) && BlackBox.validate(detailSel) && doCustomSave(conn)) {
						BlackBox.executeSave(this, detailSel, conn, ut);
						if (bInLineEdit == true) {
							setSqlConditions();
							BlackBox.setInLineListbox(this, detailSel, false);
						}
						detailModel.set(detailModel.indexOf(detailSel), detailSel);
						refreshListbox(detailSel);
						Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"), "Information", Messagebox.OK, Messagebox.INFORMATION);
					} else {
						logger.info("onClickbtnSave(Update) : writeMethod(detailSel) && beforeDetailSave(detailSel) && BlackBox.CheckPK(this, detailSel) && BlackBox.validate(detailSel) && doCustomSave(conn) is false");
						return;
					}
				} catch (Exception ex) {
					conn.rollback();
					logger.error("onClickbtnSave(Update) : ", ex);
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0088"), "Error", Messagebox.OK, Messagebox.ERROR);
					throw ex;
				} finally {
					BlackBox.close(conn);
				}
			}
			/* 儲存 End */

			data_Mode = DataMode.READ_MODE;
			if (IsOpenWinCRUD) {
				opencrudProgram.masterReadMode(null);
			} else {
				masterProgram.masterReadMode(null);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * 讀取畫面上的控件的Value及於doSaveDefault()裡的value存入物件
	 * @param objDetailSel 資料物件
	 * @return 成功回傳true, 失敗回傳false
	 */	
	public boolean writeMethod(Object objDetailSel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		if (detailComponentColumns.size() > 0){
			return writeMethod(objDetailSel, detailComponentColumns);
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
	
	
	/*********************************************************************************************************/
	/** LISTBOX ACTION Start */
	/*********************************************************************************************************/

	/** listbox 上的瀏覽功能, 大都會撘配開窗 */
	@SuppressWarnings("unchecked")
	@Listen("onView=#detailListbox, #detail2Listbox, #detail3Listbox, #detail4Listbox, #detail5Listbox, #detail6Listbox, #detail7Listbox, #detail8Listbox, #detail9Listbox,#detail10Listbox,#detail11Listbox, #detail12Listbox, #detail13Listbox, #detail14Listbox, #detail15Listbox, #detail16Listbox, #detail17Listbox, #detail18Listbox, #detail19Listbox, #detail20Listbox")
	public void viewDetailListbox(ForwardEvent event) {
		try {
			boolean isEdit = false;
			DataMode dataMode = null;
			if (IsOpenWinCRUD){			
				dataMode = opencrudProgram.getRecordMode();
				for (int i=0; i<opencrudProgram.arrDetailPrograms.size(); i++) {
					if (!opencrudProgram.arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))					
						isEdit = true;				
				}
			}else{
				dataMode = masterProgram.getData_Mode();
				for(int i=0; i<masterProgram.arrDetailPrograms.size(); i++) {
					if (!masterProgram.arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE)){
						dataMode = masterProgram.arrDetailPrograms.get(i).getData_Mode();
						isEdit = true;
					}
				}			
			}

			if (dataMode.equals(DataMode.READ_MODE) && isEdit == false) {
				if (!getDetailUpdateZul().isEmpty() && getDetailUpdateZul() != null) {
					Object detailEntity = listbox.getSelectedItem().getValue();
					setDetailSel(detailEntity);

					if (getDetailCreateMap() != null) {
						mapOpenWin = new HashMap<String, Object>(getDetailCreateMap());
					}
					mapOpenWin.put("selectedRecord", detailEntity);
					mapOpenWin.put("recordMode", DataMode.READ_MODE);
					mapOpenWin.put("parentWindow", getParentWindow());
					mapOpenWin.put("parentModel", null);
					mapOpenWin.put("selfModel", getDetailModel());
					Executions.createComponents(getDetailUpdateZul(), null, mapOpenWin);
				}
			} else {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** listbox 修改鈕, 有區分作業模式 */	
	@SuppressWarnings("unchecked")
	@Listen("onUpdate = #detailListbox, #detail2Listbox, #detail3Listbox, #detail4Listbox, #detail5Listbox, #detail6Listbox, #detail7Listbox, #detail8Listbox, #detail9Listbox, #detail10Listbox,#detail11Listbox, #detail12Listbox, #detail13Listbox, #detail14Listbox, #detail15Listbox, #detail16Listbox, #detail17Listbox, #detail18Listbox, #detail19Listbox, #detail20Listbox")
	public void updateDetailListbox(ForwardEvent evt) throws Exception {
		try {
			// 複製物件
			Object objEntity;
			if (getDetailClass() != null) {
				objEntity = getDetailClass().newInstance();
				BeanUtils.copyProperties(detailSel, objEntity);
				detailSelBeforeNew = objEntity;
			}

			boolean isEdit = false;
			DataMode MasterdataMode = null;
			if (IsOpenWinCRUD) {
				MasterdataMode = opencrudProgram.getRecordMode();
				for (int i = 0; i < opencrudProgram.arrDetailPrograms.size(); i++) {
					if (!opencrudProgram.arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))
						isEdit = true;
				}
			} else {
				MasterdataMode = masterProgram.getData_Mode();
				for (int i = 0; i < masterProgram.arrDetailPrograms.size(); i++) {
					if (!masterProgram.arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))
						isEdit = true;
				}
			}
			if (MasterdataMode.equals(DataMode.READ_MODE) && isEdit == false) {
				if (data_Mode.equals(DataMode.CREATE_MODE) || data_Mode.equals(DataMode.UPDATE_MODE))
					return;

				Object detailEntity = listbox.getSelectedItem().getValue();

				data_Mode = DataMode.UPDATE_MODE;
				switch (getOperationMode()) {
				case NORMAL:
					detailCreateMode(null);
					if (bInLineEdit == true) {
						setSqlConditions();
						BlackBox.setInLineListbox(this, detailEntity, true);
					}
					break;
				case OPENWIN:
					if (getDetailCreateMap() != null) {
						mapOpenWin = new HashMap<String, Object>(getDetailCreateMap());
					}
					mapOpenWin.put("selectedRecord", detailEntity);
					mapOpenWin.put("recordMode", DataMode.UPDATE_MODE);
					mapOpenWin.put("parentWindow", getParentWindow());

					if (IsOpenWinCRUD)
						mapOpenWin.put("masterModel", opencrudProgram.getMasterModel());
					else
						mapOpenWin.put("masterModel", masterProgram.getMasterModel());

					mapOpenWin.put("detailModel", getDetailModel());
					Executions.createComponents(getDetailUpdateZul(), null, mapOpenWin);
					break;
				}
			} else {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** listbox 刪除鈕 */
	@SuppressWarnings("unchecked")
	@Listen("onDelete = #detailListbox, #detail2Listbox, #detail3Listbox, #detail4Listbox, #detail5Listbox, #detail6Listbox, #detail7Listbox, #detail8Listbox, #detail9Listbox, #detail10Listbox,#detail11Listbox, #detail12Listbox, #detail13Listbox, #detail14Listbox, #detail15Listbox, #detail16Listbox, #detail17Listbox, #detail18Listbox, #detail19Listbox, #detail20Listbox")
	public void onDeleteDetailListbox(ForwardEvent evt) {
		try {
			DataMode masterDataMode = null;
			boolean isEdit = false;

			if (IsOpenWinCRUD) {
				masterDataMode = opencrudProgram.getRecordMode();
				for (int i = 0; i < opencrudProgram.arrDetailPrograms.size(); i++) {
					if (!opencrudProgram.arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))
						isEdit = true;
				}
			} else {
				masterDataMode = masterProgram.getData_Mode();
				for (int i = 0; i < masterProgram.arrDetailPrograms.size(); i++) {
					if (!masterProgram.arrDetailPrograms.get(i).getData_Mode().equals(DataMode.READ_MODE))
						isEdit = true;
				}
			}

			if (masterDataMode.equals(DataMode.READ_MODE) && isEdit == false) {
				if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE))
					return;
				Object detailEntity = listbox.getSelectedItem().getValue();
				setDetailSel(detailEntity);
				if (beforeDetailDel(detailEntity) == false)
					return;

				Messagebox.show(Labels.getLabel("COMM.DELETE") + "?", Labels.getLabel("PUBLIC.MSG0001"),
						Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							UserTransaction ut = TransactionManagerServices.getTransactionManager();
							ut.begin();
							Connection conn = BlackBox.getDbConnection(CRUDService.getEmf());
							try {
								conn.setAutoCommit(false);
								if (doCustomerDel(conn)) {
									doDelete(detailEntity, conn, ut);
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
			} else {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}
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
	public void doDelete(Object pDeletetDetail, Connection conn, UserTransaction ut) throws Exception {
		BlackBox.doDelete(this, pDeletetDetail, conn, ut);
	}
	
	/** 點擊存檔按鈕 */
	@Listen("onSave = #detailListbox, #detail2Listbox, #detail3Listbox, #detail4Listbox, #detail5Listbox, #detail6Listbox, #detail7Listbox, #detail8Listbox, #detail9Listbox, #detail10Listbox,#detail11Listbox, #detail12Listbox, #detail13Listbox, #detail14Listbox, #detail15Listbox, #detail16Listbox, #detail17Listbox, #detail18Listbox, #detail19Listbox, #detail20Listbox")
	public boolean onSaveDetailListbox(ForwardEvent evt) {
		try {
			detailSave();
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
	@Listen("onCancel = #detailListbox, #detail2Listbox, #detail3Listbox, #detail4Listbox, #detail5Listbox, #detail6Listbox, #detail7Listbox, #detail8Listbox, #detail9Listbox, #detail10Listbox,#detail11Listbox, #detail12Listbox, #detail13Listbox, #detail14Listbox, #detail15Listbox, #detail16Listbox, #detail17Listbox, #detail18Listbox, #detail19Listbox, #detail20Listbox")
	public void onCancelDetailListbox(ForwardEvent evt) {
		try {
			if (data_Mode.equals(DataMode.CREATE_MODE)) {
				int index = 0;
				if (bInLineEdit == true) {
					if (bInsertCurrent) {				
						index = detailModel.indexOf(detailSelBeforeNew)+1;
						detailModel.remove(index);						
					} else {
						detailModel.remove(index);
					}
				}
				setDetailSel(detailSelBeforeNew);
			}
			
			data_Mode = DataMode.READ_MODE;
			refreshListbox(getDetailSel());
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
	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	@Listen("onDetailSaved = Window")
	public void onDetailSaved(Event event) throws Exception {
		try{
			Map<String, Object> args = (Map<String, Object>) event.getData();
			DataMode recordMode = (DataMode) args.get("recordMode");
			Object selectedEntity = args.get("selectedRecord");

			if (selectedEntity != null) {
				refreshListbox(selectedEntity);
			}

			data_Mode = DataMode.READ_MODE;

			if (IsOpenWinCRUD) {
				opencrudProgram.masterReadMode(null);
			} else {
				masterProgram.masterReadMode(null);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/** 執行選擇litstbox之index動作 */
	public void programmacticallySelectListbox(int index) throws Exception {
		try {
			BlackBox.programmacticallySelectListbox(this, index);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	
	/** 點擊listbox listheader排序 */
	@Listen("onClick = #detailListbox listheader, #detail2Listbox listheader, #detail3Listbox listheader, #detail4Listbox listheader, #detail5Listbox listheader, #detail6Listbox listheader, #detail7Listbox listheader, #detail8Listbox listheader, #detail9Listbox listheader, #detail10Listbox listheader, #detail11Listbox listheader, #detail12Listbox listheader, #detail13Listbox listheader, #detail14Listbox listheader, #detail15Listbox listheader, #detail16Listbox listheader, #detail17Listbox listheader, #detail18Listbox listheader, #detail19Listbox listheader, #detail20Listbox listheader")
	public void onClickListheader(Event event) {
		BlackBox.orderByListheader(this, event);
	}
	
	/**
	 * 存檔前檢查
	 * @param entityDetail 資料物件
	 * @return 檢查通過:True, 不通過:False
	 */
	protected boolean beforeDetailSave(Object entityDetail) {
		return BlackBox.beforeSave(this, entityDetail);
	}	
	
	/**
	 * 刪除前的檢查 
	 * @param entityDetail 資料物件
	 * @return 檢查通過:True, 不通過:False
	 */
	protected boolean beforeDetailDel(Object entityDetail) {
		return true;
	}
	
	/**
	 * 判斷是否跟資料列一起刪除
	 * @param conn 連線
	 * @return 刪除成功:True, 不成功:False
	 */
	public boolean doCustomerDel(Connection conn){
		return true;
	}

	/** 取得作業之Listbox Component */
	protected Listbox getDetailListbox() {
		return listbox;
	}
	
	/************************************get/set area************************************/
	/** 取得父層Window Component */
	public Window getParentWindow() {
		return this.parentWindow;
	}

	/** 設定父層Window Component */
	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}

	/** 取得主檔程式(Master) */
	public Master getMasterProgram() {
		return masterProgram;
	}

	/** 設定主檔程式 */
	public void setMasterProgram(Master masterProgram) {
		this.masterProgram = masterProgram;
	}	

	/** 取得主檔程式(OpenWinCRUD) */
	public OpenWinCRUD getOpenWinProgram(){
		return this.opencrudProgram;
	}
	
	/** 設定主檔程式(OpenWinCRUD) */
	public void setOpenWinProgram(OpenWinCRUD OpenWinProgram){
		this.opencrudProgram = OpenWinProgram;
	}

	/** 取得次檔作業Listbox model */
	public ListModelList getDetailModel() {
		return detailModel;
	}
	
	/** 設定次檔作業Listbox model */
	public void setDetailModel(ListModelList detailModel) {
		this.detailModel = detailModel;
	}

	/** 取得次檔選擇之資料物件 */
	public Object getDetailSel() {
		return detailSel;
	}

	/** 設定次檔選擇之資料物件, 並將取得的資料內容放到控件上 */
	public void setDetailSel(Object detailSel) {
		this.detailSel = detailSel;
		resetEditAreaDetail(detailSel);
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
	public Query getQueryResultSizeDetail() {
		return queryResultSizeDetail;
	}
	
	/** 設定計算資料筆數Query */
	public void setQueryResultSizeDetail(Query queryResultSizeDetail) {
		this.queryResultSizeDetail = queryResultSizeDetail;
	}
	
	/** 取得查詢資料Query */
	public Query queryPagingDetail(){
		return this.queryPagingDetail;
	}
	
	/** 設定查詢資料Query */
	public void setQueryPagingDetail(Query queryPagingDetail) {
		this.queryPagingDetail = queryPagingDetail;
	}
	
	/** 取得查詢PK欄位Query */
	public Query getQueryOneColDetail() {
		return queryOneColDetail;
	}
	
	/** 設定查詢PK欄位Query */
	public void setQueryOneColDetail(Query queryOneColDetail) {
		this.queryOneColDetail = queryOneColDetail;
	}
	
	/** 取得查詢資料區間Query */
	public Query getQueryPagingbySize(){
		return this.queryPagingbySize;
	}
	
	/** 設定查詢資料區間 */
	public void setQueryPagingbySize(int iStart) {
		this.queryPagingbySize = this.queryPagingDetail.setFirstResult(iStart).setMaxResults(MY_PAGE_SIZE);
	}
	
	/** 取得按鈕Component */
	public HashMap<String, Object> getButtons(){
		return buttons;
	}
	
	public void setRegular(boolean pRegular){
		this.bRegular = pRegular;
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
	
	/** 取得查詢開窗的ZUL檔路徑 */
	protected String getDetailQueryZul() {
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
	
	/** 取得PK欄位名稱 */
	protected ArrayList<String> getDetailKeyName() {
		return BlackBox.getKeyName(getDetailClass());
	}
	
	/** 取得PK欄位資料 */
	protected ArrayList<String> getDetailKeyValue(Object entityMaster) {
		return BlackBox.getKeyValue(this, entityMaster);
	}
	
	/** 取得新增開窗的ZUL檔路徑 */
	protected String getDetailCreateZul(){
		return null;
	}

	/** 取得編輯開窗的ZUL檔路徑 */
	protected String getDetailUpdateZul(){
		return null;
	}
	
	/** 取得新增開窗所傳入的參數 */
	protected HashMap getDetailCreateMap(){
		return null;
	}

	/** 取得分頁ID, 預設為pagingDetail */
	protected String getPagingIdDetail() {
		return "pagingDetail";
	}
	
	/** 取得where條件 */
	protected String getWhereConditionals() {
		String sWere=" AND 1=1 ";
		return sWere;
	}
	
	/** detail Div, 已廢除, 改使用getRootWindow() */
	protected Div getRootDiv() {
		return null;
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
	public Paging getDetailPaging() {
		return detailPaging;
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
