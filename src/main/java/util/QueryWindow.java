package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

import ds.common.services.CRUDService;

@SuppressWarnings({ "rawtypes" })
public abstract class QueryWindow extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	/**	記錄log */
	protected Logger logger = BlackBox.getLogger(this);
	
	@WireVariable
	protected CRUDService CRUDService;
	@Wire
	protected Listbox queryListBox;
	
	/** 父視窗 */
	protected Window parentWindow;
	
	/** listbox model */
	protected ListModelList queryModel;
	
	/** listbox list */
	protected List queryList;
	
	/** itemColumn:條件欄位, itemCondition:條件判斷式 */
	protected ListModel itemColumn, itemCondition;
	
	/**	所選取之資料物件 */
	protected Object querySel;
	
	/** ENTITY_CLASSNAME:java.class名(@domain), 為作業的主要entity name, 
	 * returnMethodName: 回直父視窗時，接收的Fucntion Name*/
	protected String ENTITY_CLASSNAME, returnMethodName;
	
	/** 是否為多選 */
	private Boolean bMultiple = false;
	
	/** 分頁筆數 */
	int MY_PAGE_SIZE;
	
	/** 分頁 */
	Paging queryPaging;
	
	/** queryPaging 查詢資料, queryPagingbySize 設定查詢資料區間, qryTotalSize 查詢總筆數 */
	Query qryPaging, queryPagingbySize, qryTotalSize;

	/** 取得ResultSizeBase SQL */
	String sSqlResultSizeBase;
	
	/** 取得PagingBase SQL */
	String sSqlPagingBase;
	
	/** 取得OneColBase SQL */
	String sSqlOneColBase;
	
	/** listbox多選物件List */
	ArrayList<Object> arrMulitSelect = new ArrayList<Object>();
	
	/** 回傳時所帶回之資料 */
	HashMap<String, Object> mapReturn = new HashMap<String, Object>();
	
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
	
	/** 取得給予Entity的名稱 */
	abstract protected String getEntityName();
	
	/** 設定Combobox查詢欄位 */
	abstract protected List<String> setComboboxColumn();
	
	/** 設定SQL查詢條件 */
	abstract protected String getSQLWhere();

	/**
	 * 自訂SQL語法使用, 需搭配自訂資料物件
	 * @return
	 */
	abstract protected String getCustomSQL();
	
	/**
	 * 自訂SQL語法資料筆數
	 * @return
	 */
	abstract protected String getCustomCountSQL();
	
	/**
	 * 查詢條件輸入框
	 * @return
	 */
	abstract protected String getTextBoxValue();
	
	/**
	 * SQL排序條件
	 * @return
	 */
	abstract protected String getOrderby();
	
	/**
	 * 查詢欄位元件combobox
	 * @return
	 */
	abstract protected Combobox getcboColumn();
	
	/**
	 * 查詢條件元件combobox, ex: >, <, >=...
	 * @return
	 */
	abstract protected Combobox getcboCondition();
	
	/**
	 * 回傳時所帶回之資料
	 * @return
	 */
	abstract protected HashMap getMap();
	
	/**
	 * 自訂SQL語法使用, 需搭配自訂資料物件
	 * @return 自訂的List集合
	 */
	abstract protected List getCustList();

	/**QueryWindow設定**/
//	abstract protected List<String> setComboboxCondition();
//	abstract protected String getPagingId();	//分頁元件Id
//	abstract protected int getPageSize();	//自訂分頁筆數
	
	public QueryWindow() {
		super();
	}

	/**
	 * 作業的初始
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component window) throws Exception {
		try {
			super.doAfterCompose(window);
			CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
			final Execution exec = Executions.getCurrent();
			setParentWindow((Window) exec.getArg().get("parentWindow"));
			
			if (exec.getArg().get("returnMethod") == null)
				returnMethodName = "onQueryWindowSend";
			else
				returnMethodName = (String) exec.getArg().get("returnMethod");
			
			if (exec.getArg().get("multiple") == null)
				setListboxMulit(false);
			else {
				setListboxMulit(exec.getArg().get("multiple").toString().toUpperCase().equals("TRUE"));
			}

			if (getcboColumn() != null && setComboboxColumn() != null) {
				setItemColumn(new ListModelArray(setComboboxColumn()));
				getcboColumn().setModel(itemColumn);
			}
			
			if (getcboCondition() != null && setComboboxCondition() != null) {
				setItemCondition(new ListModelArray(setComboboxCondition()));
				getcboCondition().setModel(itemCondition);
			}
			_init(window);

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
	void _init(Component comp) {
		try {
			if (comp instanceof Combobox) {
				Combobox cbo = (Combobox) comp;
				switch (cbo.getId()) {
				case "cboColumn":
				case "cboCondition":
					cbo.setReadonly(true);
					break;
				}
			}
			
			List<Component> listComp = comp.getChildren();
			for (Component child : listComp) {
				_init(child);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 預設選擇查詢欄位第一筆資料 */
	@Listen("onInitRenderLater = #cboColumn")
	public void onInitRenderLatercboColumn(Event event) {
		Combobox cbo = (Combobox) event.getTarget();
		cbo.setSelectedIndex(0);
	}
	
	/** 預設選擇查詢條件第一筆資料 */
	@Listen("onInitRenderLater = #cboCondition")
	public void onInitRenderlatercboCondition(Event event) {
		Combobox cbo = (Combobox) event.getTarget();
		cbo.setSelectedIndex(0);
	}

	/** 點擊查詢鈕 */
	@Listen("onClick = #btnSearch")
	public void onClickbtnSearch(Event event) {		
		try {
			doSearch();
		} catch (Exception e) {
			logger.error(null, e);
			throw e;			
		}
	}
	
	/** 查詢 */
	public void doSearch() {
		try {
			if (getSQLWhere() == null)
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0099"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			else {
				if (queryListBox != null)
					doFillListbox(0);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 設定SQL查詢條件 */
	private void setSqlConditions() {
		try {
			String strSQLWhere = getSQLWhere();
			String strOrderBy = getOrderby();

			if (strSQLWhere == null)
				strSQLWhere = "";

			if (bIsCustList) {
				sSqlResultSizeBase = getCustomCountSQL() + strSQLWhere + (strOrderBy == null ? "" : strOrderBy);
				qryTotalSize = CRUDService.createSQL(sSqlResultSizeBase);

				sSqlPagingBase = getCustomSQL() + strSQLWhere + (strOrderBy == null ? "" : strOrderBy);
				qryPaging = CRUDService.createSQL(sSqlPagingBase);
			} else {
				sSqlResultSizeBase = "Select Count(t) From " + getEntityName() + " t " + strSQLWhere + (strOrderBy == null ? "" : strOrderBy);
				qryTotalSize = CRUDService.getBetweenByLimit2(sSqlResultSizeBase);

				sSqlPagingBase = "Select t From " + getEntityName() + " t " + strSQLWhere + (strOrderBy == null ? "" : strOrderBy);
				qryPaging = CRUDService.getBetweenByLimit2(sSqlPagingBase);
			}

			if (strSQLWhere.indexOf("keyword") > -1) {
				qryTotalSize.setParameter("keyword", "%" + getTextBoxValue() + "%");
				qryPaging.setParameter("keyword", "%" + getTextBoxValue() + "%");
			}

			setQueryPagingbySize(iStartPosition);
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
			MY_PAGE_SIZE = (getPageSize() <= 0 ? 10 : getPageSize());
			int activePage = (int) (index / MY_PAGE_SIZE);
			iStartPosition = activePage * MY_PAGE_SIZE;

			setSqlConditions();
			if (bIsCustList)
				queryList = getCustList();
			else {
				queryList = getQueryPagingbySize().getResultList();
			}

			queryModel = new ListModelList(queryList, true);
			queryListBox.invalidate();
			queryListBox.setModel(queryModel);
			queryListBox.renderAll();
			queryModel.setMultiple(getListboxMulit());
			
			queryPaging = (Paging) queryListBox.getFellow(getPagingId());
			queryPaging.setPageSize(MY_PAGE_SIZE);

			Long iTotalSize;
			if (bIsCustList)
				iTotalSize = ((BigDecimal) this.qryTotalSize.getSingleResult()).longValue();
			else
				iTotalSize = (Long) qryTotalSize.getSingleResult();

			queryPaging.setTotalSize((int) (long) iTotalSize);
			
			Iterable<EventListener<? extends Event>> eventListeners = queryPaging.getEventListeners(ZulEvents.ON_PAGING);
			for (EventListener<? extends Event> eventListener : eventListeners) {
				queryPaging.removeEventListener(ZulEvents.ON_PAGING, eventListener);
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
						queryListBox.invalidate();
						queryListBox.setModel(queryModel);
						queryListBox.renderAll();
					} catch (Exception e) {
						logger.error(null, e);
						throw e;
					}
				}
			};
			queryPaging.addEventListener(ZulEvents.ON_PAGING, el);

			if (iTotalSize >= 1)
				queryPaging.setActivePage(activePage);

		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 點擊確定鈕 */
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnConfirm")
	public void onClickbtnConfirm(Event event) {
		try {
			if (arrMulitSelect.size() <= 0 && queryListBox.getSelectedItem() == null) {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}

			if (getMap() != null) {
				mapReturn = new HashMap<String, Object>(getMap());
			}
			
			if (parentWindow != null)
				getRootWindow().detach();
			
			if (getListboxMulit())
				mapReturn.put("selectedRecord", arrMulitSelect);
			else {
				if (arrMulitSelect.size() <= 0)
					mapReturn.put("selectedRecord", (Object) queryListBox.getModel().getElementAt(queryListBox.getSelectedIndex()));
				else
					mapReturn.put("selectedRecord", (Object) arrMulitSelect.get(0));
			}
				
			if (parentWindow != null)
				Events.sendEvent(new Event(returnMethodName, parentWindow, mapReturn));

		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 點擊取消鈕 */
	@Listen("onClick = #btnCancel")
	public void onClickbtnCancel(Event event) {
		if (getRootWindow() != null)
			getRootWindow().detach();
	}

	/** 取得查詢資料 */
	public Query getQryPaging() {
		return qryPaging;
	}
	
	/** 設定查詢資料 */
	public void setQryPaging(Query qryPaging) {
		this.qryPaging = qryPaging;
	}
	
	/** 取得查詢資料區間 */
	public Query getQueryPagingbySize() {
		return queryPagingbySize;
	}
	
	/** 設定查詢資料區間 */
	private void setQueryPagingbySize(int iStart) {
		this.queryPagingbySize = this.qryPaging.setFirstResult(iStart).setMaxResults(MY_PAGE_SIZE);
	}
	
	/** 取得查詢總筆數 */
	public Query getQryTotalSize() {
		return qryTotalSize;
	}
	
	/** 設定查詢總筆數 */
	public void setQryTotalSize(Query qryTotalSize) {
		this.qryTotalSize = qryTotalSize;
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

	/** 取得父視窗 */
	public Window getParentWindow() {
		return parentWindow;
	}
	
	/** 設定父視窗 */
	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}

	/** 取得LisbBox */
	public Listbox getQueryLisbBox() {
		return this.queryListBox;
	}

	/** 設定LisbBox */
	public void setQueryListBox(Listbox queryListBox) {
		this.queryListBox = queryListBox;
	}

	/** 取得條件欄位 */
	public ListModel getItemColumn() {
		return this.itemColumn;
	}

	/** 設定條件欄位 */
	public void setItemColumn(ListModel itemColumn) {
		this.itemColumn = itemColumn;
	}

	/** 取得條件判斷式 */
	public ListModel getItemCondition() {
		return this.itemCondition;
	}

	/** 設定條件判斷式 */
	public void setItemCondition(ListModel itemCondition) {
		this.itemCondition = itemCondition;
	}

	/** 取得多選資料 */
	public ArrayList<Object> getSelectedRecord() {
		return getListboxMulitSelect();
	}
	
	/** 設定多選資料 */
	public void setSelectedRecord(ArrayList<Object> arrList) {
		setListboxMulitSelect(arrList);
	}
	
	/** 清除多選資料 */
	public void doClearRecord() {
		this.arrMulitSelect.clear();
	}
	
	/** 取得回傳參數 */
	public HashMap getMapReturn() {
		return mapReturn;
	}
	
	/** 取得回傳方法名稱 */
	public String getReturnMethodName() {
		return returnMethodName;
	}
	
	/** 設定回傳方法名稱 */
	public void setReturnMethodName(String returnMethodName) {
		this.returnMethodName = returnMethodName;
	}

	/** 取得多選資料 */
	public ArrayList<Object> getListboxMulitSelect() {
		return arrMulitSelect;
	}
	
	/** 設定多選資料 */
	public void setListboxMulitSelect(ArrayList<Object> arrList) {
		this.arrMulitSelect = arrList;
	}
	
	/** 選擇ListBox資料 */
	@Listen("onSelect = #queryListBox")
	public void onSelectqueryListBox(Event evt) {
		BlackBox.listboxMulitSel(this, evt);
	}
	
	/** 取得是否為多選 */
	public boolean getListboxMulit(){
		return bMultiple;
	}
	
	/** 設定是否為多選 */
	public void setListboxMulit(Boolean bMultiple) {
		this.bMultiple = bMultiple;
	}
	
	/** 取得sSqlResultSizeBase */
	public String getsSqlResultSizeBase() {
		return sSqlResultSizeBase;
	}
	
	/** 取得sSqlPagingBase */
	public String getsSqlPagingBase() {
		return sSqlPagingBase;
	}
	
	/** 取得sSqlOneColBase */
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
	
	/** 設定查詢條件Combobox */
	protected List<String> setComboboxCondition() {
		List<String> listCondition = new ArrayList<String>();
		listCondition.add("LIKE");
		listCondition.add("=");
		return listCondition;
	}
	
	/** 取得頁籤ID, 預設為pagingQueryWindow */
	protected String getPagingId() {
		return "pagingQueryWindow";
	}
	
	/** 取得分頁筆數, 預設為0=一頁5筆資料*/
	protected int getPageSize() {
		return 0;
	}
	public CRUDService getCRUDService() {
		return CRUDService;
	}
	public void setCRUDService(CRUDService cRUDService) {
		CRUDService = cRUDService;
	}
}
