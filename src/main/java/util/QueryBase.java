package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

import ds.common.services.CRUDService;
import ds.common.services.ProgramAuth;
import ds.common.services.UserCredential;

@SuppressWarnings("rawtypes")
public abstract class QueryBase extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	/**	記錄log */
	protected Logger logger = BlackBox.getLogger(this);
	
	@WireVariable
	protected CRUDService CRUDService;
	@Wire
	protected Listbox queryListbox;

	/**	listbox model */
	protected ListModelList queryModel;
	
	/**	所選取之資料物件 */
	protected static Object querySel;
	
	/** 作業之Listbox Component */
	private Listbox listbox;
	
	/** listbox資料清單 */
	protected List queryList;
	
	/** 登入者資訊 */
	protected final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential"); // 登入者資訊
	
	/** 作業權限 */
	ProgramAuth prgAuth;
	
	/** queryResultSize 計算資料筆數, queryPaging 查詢資料, queryOneCol 查詢PK欄位, queryPagingbySize 設定查詢資料區間, queryAllPaging 查詢全部資料*/
	Query queryResultSize, queryPaging, queryOneCol, queryPagingbySize, queryAllPaging;

	/** 取得ResultSizeBase SQL */
	String sSqlResultSizeBase;
	
	/** 取得PagingBase SQL */
	String sSqlPagingBase;
	
	/** 取得OneColBase SQL */
	String sSqlOneColBase;
	
	/** 分頁 */
	Paging pagingQuery;
	
	/** 分頁筆數 */
	int MY_PAGE_SIZE;
	
	/** java.class名(@program), 用來判斷作業權限(DSPB00_NEW.PB_PRGNAME) */
	public String QUERY_CLASSNAME;
	
	/** java.class名(@domain), 為作業的主要entity name */
	public String ENTITY_CLASSNAME;
	
	/** listbox多選物件List */
	ArrayList<Object> arrMulitSelect = new ArrayList<Object>();
	
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
	 * 取得java.class名(@program) => QUERY_CLASSNAME
	 * @return java class
	 */
	abstract protected Class getQueryClass();
	
	/**
	 * 取得java.class名(@domain) => ENTITY_CLASSNAME
	 * @return entity class
	 */
	abstract protected Class getEntityClass();
	
	/** 設定SQL查詢條件 */
	abstract protected String getWhereConditionals();
	
	/**
	 * 將取得的資料內容放到控件上
	 * @param entityMaster 資料物件
	 */
	abstract protected void resetEditArea(Object entity);
	
	/**
	 * 自訂SQL語法使用, 需搭配自訂資料物件
	 * @return 自訂的List集合
	 */
	abstract protected List getCustList();

	/**QueryWindow設定**/
//	abstract protected ArrayList<String> getKeyName(); // table pk name
//	abstract protected ArrayList<String> getKeyValue(Object entity); // table pk value
//	abstract protected String getPagingId(); // 分頁ID
//	abstract protected int getPageSize(); // 自訂分頁筆數	
	
	public QueryBase() {
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
			_initAuthority(window);
			setQuerySel(null);

			if (getCustList() != null) {
				bIsCustList = true;
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * 切始化及設定權限
	 */
	void _init() {
		try {
			if (getEntityClass() == null) {
				ENTITY_CLASSNAME = "";
			} else {
				String eClass[] = getEntityClass().getName().split("\\.");
				ENTITY_CLASSNAME = eClass[eClass.length - 1];
			}
			String qClass[] = getQueryClass().getName().split("\\.");
			QUERY_CLASSNAME = qClass[qClass.length - 1];

			if (Sessions.getCurrent() != null) {
				if (Sessions.getCurrent().getAttribute("programAuthority") != null) {
					Sessions.getCurrent().removeAttribute("programAuthority");
					Sessions.getCurrent().removeAttribute("RAuth"); // Query
					Sessions.getCurrent().removeAttribute("PAuth"); // Print
					Sessions.getCurrent().removeAttribute("XAuth"); // Export
				}
			}

			Session sessImageAuth = Sessions.getCurrent();
			if (_userInfo == null || _userInfo.getAuth().size() == 0) {
				sessImageAuth.setAttribute("RAuth", false);
				sessImageAuth.setAttribute("PAuth", false);
				sessImageAuth.setAttribute("XAuth", false);
			} else {
				prgAuth = BlackBox.getPRGAuth(_userInfo, QUERY_CLASSNAME);
				sessImageAuth.setAttribute("RAuth", prgAuth.getAuthQuery());
				sessImageAuth.setAttribute("PAuth", prgAuth.getAuthPrint());
				sessImageAuth.setAttribute("XAuth", prgAuth.getAuthExport());
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
		try {
			if (comp instanceof Button) {
				Button btn = (Button) comp;
				if (btn.getId().indexOf("btnQuery") > -1) {
					btn.setDisabled(!prgAuth.getAuthQuery());
				}
				if (btn.getId().indexOf("btnExport") > -1) {
					btn.setDisabled(!prgAuth.getAuthExport());
				}
				if (btn.getId().indexOf("btnPrint") > -1) {
					btn.setDisabled(!prgAuth.getAuthPrint());
				}
			}
			// 找到頁面上的listbox
			if (comp instanceof Listbox) {
				if (comp.getId().indexOf("query") > -1)
					listbox = (Listbox) comp;
			}
			List<Component> listComp = comp.getChildren();
			for (Component child : listComp) {
				_initAuthority(child);
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
	public void onCreateWindowQuery(Event event) throws Exception {
		try {
			if (listbox != null) {
				if (listbox.getSelectedItem() != null) {
					onClickQueryListbox(listbox.getSelectedItem().getValue());
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * Listbox建立時, 先取查詢條件, 再把查詢結果放進listbox
	 */
	@Listen("onCreate = #queryListbox")
	public void onCreateListbox(Event event) {
		try {
			if (listbox != null)
				doFillListbox(0);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * listbox onSelect, 原本為onClick事件，改更onSelect，因此function為public，改原fucntion name 不變更
	 */
	@Listen("onSelect = #queryListbox")
	public void clickQueryListbox(Event event) {
		try {
			Listitem listitem = (Listitem) ((SelectEvent) event).getReference();
			if (listbox != null) {
				if (listbox.getSelectedItem() != null)
					onClickQueryListbox(listitem.getValue());
			}
			BlackBox.listboxMulitSel(this, event);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/**
	 * listbox onClick action 把資料放到querySel, 並記錄選擇資料
	 */
	public void onClickQueryListbox(Object ocvo) {
		setQuerySel(ocvo);
		
		if (ocvo != null) {
			//預設選定的資料加到listbox多選物件List
			ArrayList<Object> arrList = new ArrayList<>();
			arrList.add(ocvo);
			setListboxMulitSelect(arrList);
		}
	}
	
	/** 計算資料筆數SQL */
	public String getQueryResultSizeBase() {
		// johnny modify 避免因多個key值，造成需override
		return "Select COUNT(1) From " + ENTITY_CLASSNAME + " t ";
	}
	
	/** 查詢資料SQL */
	public String getQueryPagingBase() {
		return "Select t From " + ENTITY_CLASSNAME + " t ";
	}

	/** 查詢PK欄位SQL */
	public String getQueryOneColBase() {
		String strFields = "";
		for (int i = 0; i < getKeyName().size(); i++) {
			strFields += ",t." + getKeyName().get(i);
		}
		return "Select " + strFields.substring(1) + " From " + ENTITY_CLASSNAME + " t ";
	}

	/** 排序條件 */
	public String getOrderBy() {
		String strFields = "";
		for (int i = 0; i < getKeyName().size(); i++) {
			strFields += ",t." + getKeyName().get(i);
		}
		return " Order by " + strFields.substring(1);
	}

	/** 設定SQL查詢條件 */
	public void setSqlConditions() {
		try {
			String sWhere = getWhereConditionals();

			if (sWhere == null)
				sWhere = "";

			setWhereConditionals(sWhere);
			setQueryPagingbySize(iStartPosition);

		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/** 設定SQL查詢條件 */
	public void setWhereConditionals(String sWhere) {
		try {
			String[] ss = new String[3];
			ss[0] = getQueryResultSizeBase() + " Where 1=1 " + sWhere;
			ss[1] = getQueryPagingBase() + " Where 1=1 " + sWhere + getOrderBy();
			ss[2] = getQueryOneColBase() + " Where 1=1 " + sWhere + getOrderBy();
			if (bIsCustList) {
				queryResultSize = CRUDService.createSQL(ss[0]);
				queryPaging = CRUDService.createSQL(ss[1]);
				queryAllPaging = CRUDService.createSQL(ss[1]);
				queryOneCol = CRUDService.createSQL(ss[2]);
			} else {
				queryResultSize = CRUDService.getBetweenByLimit2(ss[0]);
				queryPaging = CRUDService.getBetweenByLimit2(ss[1]);
				queryAllPaging = CRUDService.getBetweenByLimit2(ss[1]);
				queryOneCol = CRUDService.getBetweenByLimit2(ss[2]);
			}

			sSqlResultSizeBase = ss[0];
			sSqlPagingBase = ss[1];
			sSqlOneColBase = ss[2];
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/**
	 * 將查詢資料，使用listbox呈現
	 * model最多只有5筆(預設由Common.PAGE_SIZE設定，可自行MY_PAGE_SIZE)資料。 
	 * @param index 在整個資料物件集合中的索引位置。
	 */
	@SuppressWarnings("unchecked")
	public void doFillListbox(int index) {
		try {
			MY_PAGE_SIZE = (getPageSize() <= 0 ? Common.PAGE_SIZE : getPageSize());
			int activePage = (int) (index / MY_PAGE_SIZE);
			iStartPosition = activePage * MY_PAGE_SIZE;
			
			setSqlConditions();
			if (bIsCustList) {
				queryList = getCustList();
			} else {
				queryList = getQueryPagingbySize().getResultList();
			}

			queryModel = new ListModelList(queryList, true);
			listbox.invalidate();
			listbox.setModel(queryModel);
			listbox.renderAll();
			queryModel.setMultiple(getListboxMulit());
			pagingQuery = (Paging) getRootWindow().getFellow(getPagingId());
			pagingQuery.setPageSize(MY_PAGE_SIZE);

			Long totalSize;
			if (bIsCustList) {
				totalSize = ((BigDecimal) this.queryResultSize.getSingleResult()).longValue();
			} else {
				totalSize = (Long) this.queryResultSize.getSingleResult();
			}

			pagingQuery.setTotalSize((int) (long) totalSize);
			Iterable<EventListener<? extends Event>> eventListeners = pagingQuery
					.getEventListeners(ZulEvents.ON_PAGING);
			for (EventListener<? extends Event> eventListener : eventListeners) {
				pagingQuery.removeEventListener(ZulEvents.ON_PAGING, eventListener);
			}

			// 此**listener**只會在，換頁時執行。
			EventListener el = new EventListener() {
				public void onEvent(Event event) throws Exception {
					try {
						List<Object> item = new ArrayList<Object>();
						PagingEvent pagingEvt = (PagingEvent) event;
						iStartPosition = pagingEvt.getActivePage() * MY_PAGE_SIZE;
						setSqlConditions();

						if (bIsCustList) {
							queryList = getCustList();
						} else {
							queryList = getQueryPagingbySize().getResultList();
						}

						queryModel = new ListModelList(queryList, true);
						queryModel.setMultiple(getListboxMulit());

						if (arrMulitSelect.size() > 0) {
							for (int i = 0; i < arrMulitSelect.size(); i++) {
								for (int j = 0; j < queryModel.size(); j++) {
									if (BlackBox.getDiffField(arrMulitSelect.get(i), queryModel.get(j)) == null) {
										item.add(queryModel.get(j));
									}
								}
							}
						}

						queryModel.setSelection(item);
						listbox.invalidate();
						listbox.setModel(queryModel);
						listbox.renderAll();

						if (queryList.size() >= 1)
							programmacticallySelectQueryListbox(0);
					} catch (Exception e) {
						logger.error(null, e);
						throw e;
					}
				}
			};

			pagingQuery.addEventListener(ZulEvents.ON_PAGING, el);

			if (totalSize >= 1) {
				pagingQuery.setActivePage(activePage);
				try {
					programmacticallySelectQueryListbox(index % MY_PAGE_SIZE);
				} catch (Exception e) {
					logger.error(null, e);
				}
			} else {
				querySel = null;
			}
			
			resetEditArea(querySel);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 執行選擇litstbox之index動作 */
	@SuppressWarnings("unchecked")
	public void programmacticallySelectQueryListbox(int index) {
		Object ocv = queryModel.get(index);
		setQuerySel(ocv);
		queryModel.addToSelection(ocv);
	}
	
	/** 執行查詢 */
	public void executeQuery() {
		arrMulitSelect.clear();
		if (listbox != null) {
			doFillListbox(0);
		}
	}

	/** 點擊查詢鈕 */
	@Listen("onClick = #btnQuery")
	public void onClickbtnQuery(Event event) {
		executeQuery();
	}
	
	/** 取得Listbox model */
	public ListModelList getQueryModel() {
		return this.queryModel;
	}

	/** 設定Listbox model */
	public void setQueryModel(ListModelList queryModel) {
		this.queryModel = queryModel;
	}

	/** 取得querySel資料物件 */
	@SuppressWarnings("static-access")
	public Object getQuerySel() {
		return this.querySel;
	}

	/** 設定querySel資料物件 */
	@SuppressWarnings("static-access")
	public void setQuerySel(Object querySel) {
		this.querySel = querySel;
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
	
	/** 取得查詢PK欄位 */
	public Query getQueryOneCol() {
		return queryOneCol;
	}
	
	/** 設定查詢PK欄位 */
	public void setQueryOneCol(Query queryOneCol) {
		this.queryOneCol = queryOneCol;
	}
	
	/** 取得查詢資料區間 */
	public Query getQueryPagingbySize() {
		return queryPagingbySize;
	}
	
	/** 設定查詢資料區間 */
	private void setQueryPagingbySize(int iStart) {
		this.queryPagingbySize = this.queryPaging.setFirstResult(iStart).setMaxResults(MY_PAGE_SIZE);
	}
	
	/** 取得查詢全部資料 */
	public Query getQueryAllPaging() {
		return queryAllPaging;
	}
	
	/** 設定查詢全部資料 */
	public void setQueryAllPaging(Query queryAllPaging) {
		this.queryAllPaging = queryAllPaging;
	}
	
	/** 取得是否為多選Listbox */
	public boolean getListboxMulit() {
		return bMultiple;
	}

	/** 設定是否為多選Listbox */
	public void setListboxMulit(Boolean bMultiple) {
		this.bMultiple = bMultiple;
	}

	/** 取得listbox多選物件List */
	public ArrayList<Object> getListboxMulitSelect() {
		return arrMulitSelect;
	}
	
	/** 設定listbox多選物件List */
	public void setListboxMulitSelect(ArrayList<Object> arrList) {
		this.arrMulitSelect = arrList;
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

	/** 取得分頁開始位置 */
	public int getStartPosition() {
		return iStartPosition;
	}

	/** 取得是否為自訂SQL語法 */
	public boolean getIsCustList() {
		return bIsCustList;
	}
	
	/** 取得PK欄位名稱 */
	protected ArrayList<String> getKeyName() {
		return BlackBox.getKeyName(getEntityClass());
	}
	
	/** 取得PK欄位資料 */
	protected ArrayList<String> getKeyValue(Object entityMaster) {
		return BlackBox.getKeyValue(this, entityMaster);
	}
	
	/** 取得分頁ID, 預設為pagingQueryBase */
	protected String getPagingId() {
		return "pagingQueryBase";
	}
	
	/** 取得給予Entity的名稱 */
	protected String getEntityName() {
		return ENTITY_CLASSNAME;
	}
	
	/** 取得分頁筆數, 預設為0=一頁5筆資料*/
	protected int getPageSize() {
		return 0;
	}
}
