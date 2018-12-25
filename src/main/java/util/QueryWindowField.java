package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.zkoss.json.JSONObject;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
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
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

import ds.common.services.CRUDService;
import util.component.dscQueryField;
import util.openwin.QryField;
import util.openwin.ReturnBox;

@SuppressWarnings({ "rawtypes" })
public abstract class QueryWindowField extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	/** 記錄log */
	protected Logger logger = BlackBox.getLogger(this);

	@WireVariable
	protected CRUDService CRUDService;
	@Wire
	protected Listbox queryListBox;
	@Wire
	protected Listhead queryListhead;
	@Wire
	protected Listitem queryListcell;
	@Wire
	protected Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQueryFrom, txtQueryTo;
	@Wire
	private Window windowQuery;

	/** 父視窗 */
	protected Window parentWindow;

	/** listbox model */
	protected ListModelList queryModel;

	/** listbox list */
	protected List queryList;

	/** itemColumn:條件欄位, itemCondition:條件判斷式 */
	protected ListModel itemColumn, itemCondition;

	/** 所選取之資料物件 */
	protected static Object querySel;
	
	/**
	 *  entityName: ,
	 *  returnMethodName: 回直父視窗時，接收的Fucntion Name,
	 *  msg: 傳入的訊息,
	 *  parameter: 傳入的參數,
	 *  returnField: 回傳的欄位,
	 *  xmlQryID: XML檔名,
	 *  autoPaging: 自動分頁,
	 *  CustomCondition: 自訂條件,
	 *  BaseCondition: 預設條件
	 */
	protected String entityName, returnMethodName, msg, parameter, returnField, xmlQryID, autoPaging, CustomCondition, BaseCondition;
	
	/** 經由ForwardEvent觸發，並返回該Event */
	protected ForwardEvent ForwardEvent;
	
	/** 傳入的參數, Event */
	protected Event Event;
	
	/** 傳入的參數, 過濾條件 */
	protected HashMap<String, Object> FilterMap = null;
	
	/** 選定資料後，將資料填入Textbox */
	protected Textbox returnTextBox;
	
	/** 選定資料後，將資料填入Textbox List */
	ArrayList<ReturnBox> returnTextBoxList;
	
	/** 是否為多選 */
	private Boolean bMultiple;
	
	/** 分頁筆數 */
	private int MY_PAGE_SIZE = 10;
	
	/** 分頁 */
	Paging queryPaging;
	
	/** queryPaging 查詢資料, queryPagingbySize 設定查詢資料區間, qryTotalSize 查詢總筆數 */
	Query qryPaging, queryPagingbySize, qryTotalSize;
	
	/** listbox多選物件List */
	ArrayList<Object> arrMulitSelect = new ArrayList<Object>();
	
	JSONObject parameterObj;
	
	/** 回傳時所帶回之資料 */
	HashMap<String, Object> mapReturn = new HashMap<String, Object>();
	
	/** 自訂開窗元件 */
	dscQueryField dscQueryField;
	
	/** 取得分頁開始位置 */
	private int iStartPosition = 0;
	
	/**
	 * 取得此作業的window id
	 * @return ZK Window Component
	 */
	abstract protected Window getRootWindow();

	/** 取得給予Entity的名稱 */
	abstract protected String getEntityName();

	
	abstract protected String getTextBoxValue();
	
	/** 取得頁籤ID*/
	abstract protected String getPagingId();

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

	/** 執行中傳參 */
	private Execution exec;
	
	/** 開窗xml檔案 */
	private XmlDAO xml = null;
	
	/** 欄位條件 */
	private List<String> listColumn;

	public QueryWindowField() {
		super();
	}

	
	/**
	 * 作業的初始
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");

		exec = Executions.getCurrent();
		if (exec.getArg().get("parentWindow") != null) {
			setParentWindow((Window) exec.getArg().get("parentWindow"));
		} else {
			setParentWindow(windowQuery);
		}
		if (exec.getArg().get("ForwardEvent") != null) {
			if (exec.getArg().get("ForwardEvent").getClass() == ForwardEvent.class) {
				this.ForwardEvent = (ForwardEvent) exec.getArg().get("ForwardEvent");
			}
		}
		if (exec.getArg().get("Event") != null) {
			if (exec.getArg().get("Event").getClass() == Event.class) {
				this.Event = (Event) exec.getArg().get("Event");
			}
		}

		if (exec.getArg().get("dscQueryField") != null) {
			dscQueryField = (dscQueryField) exec.getArg().get("dscQueryField");
		}

		if (exec.getArg().get("CustomCondition") != null) {
			CustomCondition = exec.getArg().get("CustomCondition").toString();
		}
		if (exec.getArg().get("BaseCondition") != null) {
			BaseCondition = exec.getArg().get("BaseCondition").toString();
		}

		if (exec.getArg().get("msg") != null) {
			msg = exec.getArg().get("msg").toString();
		}
		if (exec.getArg().get("parameter") != null) {
			parameter = exec.getArg().get("parameter").toString();
		}
		if (exec.getArg().get("returnField") != null) {
			returnField = exec.getArg().get("returnField").toString();
		}
		if (exec.getArg().get("xmlQryID") != null) {
			xmlQryID = exec.getArg().get("xmlQryID").toString();
		}
		if (exec.getArg().get("PageSize") != null) {
			MY_PAGE_SIZE = (Integer) exec.getArg().get("PageSize");
		} else {
			MY_PAGE_SIZE = 10;
		}
		if (exec.getArg().get("returnTextBox") == null) {
			returnTextBox = null;
		} else {
			returnTextBox = (Textbox) exec.getArg().get("returnTextBox");
		}

		if (exec.getArg().get("returnTextBoxList") == null) {
			returnTextBoxList = null;
		} else {
			returnTextBoxList = (ArrayList<ReturnBox>) exec.getArg().get("returnTextBoxList");
		}
		/*
		 * if (exec.getArg().get("Autopaging") != null) autoPaging =
		 * exec.getArg().get("Autopaging").toString();
		 */

		if (exec.getArg().get("returnMethod") == null)
			returnMethodName = "onQueryWindowSend";
		else
			returnMethodName = (String) exec.getArg().get("returnMethod");

		if (exec.getArg().get("multiple") == null)
			bMultiple = false;
		else {
			bMultiple = exec.getArg().get("multiple").toString().toUpperCase().equals("TRUE");
		}

		xml = new XmlDAO(getXmlQryID());
		if (this.BaseCondition != null) {
			xml.setSqlBaseCondition(this.BaseCondition);
		}

		doInitColumn();
		if (exec.getArg().get("setupFilter") != null)
			FilterMap = (HashMap<String, Object>) exec.getArg().get("setupFilter");

		if (getcboColumn() != null && listColumn != null) {
			setItemColumn(new ListModelArray(listColumn));
			getcboColumn().setModel(itemColumn);
		}

		if (getcboCondition() != null && setComboboxCondition() != null) {
			setItemCondition(new ListModelArray(setComboboxCondition()));
			getcboCondition().setModel(itemCondition);
		}

		/*
		 * Listheader a=new Listheader(); a.setLabel("123"); a.setValue("123");
		 * queryListhead.appendChild(a);
		 */
		_init(window);
	}
	
	/**
	 * 切始化及設定權限
	 */
	void _init(Component comp) {
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
	}

	/**
	 * 設定Listheader顯示欄位
	 */
	protected void doInitColumn() {
		Listheader listheader = null;
		listColumn = new ArrayList<String>();
		for (QryField qField : xml.getQryField()) {
			listheader = new Listheader();
			if (qField.getWidth() != null && !qField.getWidth().equals("")) {
				listheader.setWidth(qField.getWidth());
			} else {
				listheader.setHflex("max");
				// listheader.setVflex("max");
				// listheader.setWidth("150px");
			}
			// listheader.setHeight("5px");
			listheader.setLabel(Labels.getLabel(qField.getShowText()));
			//
			//
			queryListhead.appendChild(listheader);
			listColumn.add(Labels.getLabel(qField.getShowText()));

		}
	}

	/** 設定查詢條件Combobox */
	protected List<String> setComboboxCondition() {
		List<String> listCondition = new ArrayList<String>();
		listCondition.add("=");
		listCondition.add(">=");
		listCondition.add(">");
		listCondition.add("<=");
		listCondition.add("<");
		listCondition.add("<>");
		listCondition.add("between");
		listCondition.add("like");
		listCondition.add("is null");
		listCondition.add("is not null");
		return listCondition;
	}

	/** 設定SQL查詢條件 */
	protected String getSQLWhere() {
		String sWere = " WHERE 1=1 AND ROWNUM<100000 ";
		if (this.CustomCondition != null) {
			sWere += CustomCondition;
		}
		/*
		 * for (String key : FilterMap.keySet()) {
		 * if(!key.toUpperCase().equals("EXISTS")&&!key.toUpperCase().equals(
		 * "BETWEEN")){ sWere+=" AND "+ key; }else
		 * if(key.toUpperCase().equals("EXISTS")){ sWere+=" AND "+
		 * FilterMap.get(key); }else if(key.toUpperCase().equals("BETWEEN")){
		 * sWere+=" AND "+ FilterMap.get(key); } }
		 */
		if (txtQueryFrom.getValue().equals(""))
			return sWere;

		switch (cboCondition.getSelectedItem().getLabel()) {
		case "between":
			sWere += " AND " + xml.getQryField().get(cboColumn.getSelectedIndex()).getFieldName() + " "
					+ cboCondition.getSelectedItem().getLabel() + " '" + txtQueryFrom.getValue() + "' AND " + "'"
					+ txtQueryTo.getValue() + "'";
			break;
		case "like":
			sWere += " AND " + xml.getQryField().get(cboColumn.getSelectedIndex()).getFieldName() + " "
					+ cboCondition.getSelectedItem().getLabel() + " '%" + txtQueryFrom.getValue() + "%'";
			break;
		case "is null":
			sWere += " AND " + xml.getQryField().get(cboColumn.getSelectedIndex()).getFieldName() + "IS NULL";
			break;
		case "is not null":
			sWere += " AND " + xml.getQryField().get(cboColumn.getSelectedIndex()).getFieldName() + "IS NOT NULL";
			break;

		default:
			sWere += " AND " + xml.getQryField().get(cboColumn.getSelectedIndex()).getFieldName()
					+ cboCondition.getSelectedItem().getLabel() + "'" + txtQueryFrom.getValue() + "'";
		}
		// sWere+=" "+ CustomCondition;
		/*
		 * if (cboColumn.getSelectedIndex() == 0 &&
		 * cboCondition.getSelectedIndex() == 0 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_NO='" + txtQueryFrom.getValue() + "' "; }
		 * else if (cboColumn.getSelectedIndex() == 0 &&
		 * cboCondition.getSelectedIndex() == 1 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_NO like '%" + txtQueryFrom.getValue() +
		 * "%' "; } else if (cboColumn.getSelectedIndex() == 1 &&
		 * cboCondition.getSelectedIndex() == 0 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_CNA='" + txtQueryFrom.getValue() + "' ";
		 * } else if (cboColumn.getSelectedIndex() == 1 &&
		 * cboCondition.getSelectedIndex() == 1 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_CNA like '%" + txtQueryFrom.getValue() +
		 * "%' "; } else if (cboColumn.getSelectedIndex() == 2 &&
		 * cboCondition.getSelectedIndex() == 0 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_ENA ='" + txtQueryFrom.getValue() + "' ";
		 * } else if (cboColumn.getSelectedIndex() == 2 &&
		 * cboCondition.getSelectedIndex() == 1 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_ENA like '%" + txtQueryFrom.getValue() +
		 * "%' "; } else if (cboColumn.getSelectedIndex() == 3 &&
		 * cboCondition.getSelectedIndex() == 0 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_TYPE ='" + txtQueryFrom.getValue() +
		 * "' "; } else if (cboColumn.getSelectedIndex() == 3 &&
		 * cboCondition.getSelectedIndex() == 1 && txtQueryFrom.getValue() !=
		 * "") { sWere += " AND t.PROC_TYPE like '%" + txtQueryFrom.getValue() +
		 * "%' "; } else { sWere += ""; }
		 */
		return sWere;
	}

	/** 初始化預設查詢欄位 */
	@Listen("onInitRenderLater = #cboColumn")
	public void onInitRenderLatercboColumn(Event event) throws Exception {
		Combobox cbo = (Combobox) event.getTarget();
		cbo.setSelectedIndex(0);
	}

	/** 初始化預設查詢條件 */
	@Listen("onInitRenderLater = #cboCondition")
	public void onInitRenderlatercboCondition(Event event) throws Exception {
		Combobox cbo = (Combobox) event.getTarget();
		txtQueryTo.setDisabled(true);
		cbo.setSelectedIndex(0);
	}

	/** 變更條件時, 顯示元件 */
	@Listen("onChange = #cboCondition")
	public void ononChangecboCondition(Event event) throws Exception {
		Combobox cbo = (Combobox) event.getTarget();
		switch (cbo.getSelectedItem().getLabel()) {
		case "between":
			txtQueryFrom.setDisabled(false);
			txtQueryTo.setDisabled(false);
			break;
		case "is null":
			txtQueryFrom.setDisabled(true);
			txtQueryTo.setDisabled(true);
			break;
		case "is not null":
			txtQueryFrom.setDisabled(true);
			txtQueryTo.setDisabled(true);
			break;
		default:
			txtQueryFrom.setDisabled(false);
			txtQueryTo.setDisabled(true);
		}
	}

	/** 點擊查詢鈕 */
	@Listen("onClick = #btnSearch")
	public void onClickbtnSearch(Event event) {
		try {
			doSearch();
		} catch (Exception e) {
			logger.error(null, e);
		}
	}

	/** 查詢 */
	public void doSearch() throws Exception {
		try {
			if (FilterMap != null) {
				for (String key : FilterMap.keySet()) {
					xml.setFilterCondition(key);
				}
			}
			exec.setAttribute("ColumnSize", xml.getQryField().size());
			doFillListbox(0);
		} catch (Exception e) {
			logger.error("setupFilter設定錯誤", e);
			throw (new Exception(e.getMessage()));
		}
	}
	
	/** 設定SQL查詢條件 */
	private void setSqlConditions() {
		try {
			qryPaging = CRUDService.createSQL("SELECT * FROM (" + xml.getSqlQueryString() + ") T" + getSQLWhere());
			qryTotalSize = CRUDService
					.createSQL("SELECT COUNT(*) FROM (" + xml.getSqlQueryString() + ") T" + getSQLWhere());

			setQueryPagingbySize(iStartPosition);

			int i = 1;
			if (FilterMap != null) {
				for (String key : FilterMap.keySet()) {
					if (FilterMap.get(key) != null) {
						for (Object o : ((Object[]) FilterMap.get(key))) {
							// if (o != null && !o.equals("")) {
							qryPaging.setParameter(i, Common.getValue(o));
							qryTotalSize.setParameter(i, Common.getValue(o));
							i++;
							// }
						}
					}
				}
			}
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
		int activePage = (int) (index / MY_PAGE_SIZE);
		iStartPosition = activePage * MY_PAGE_SIZE;

		setSqlConditions();
		queryList = getQueryPagingbySize().getResultList();
		String n = new String("");
		for (ListIterator<Object[]> itr = queryList.listIterator(); itr.hasNext();) {
			Object[] o = itr.next();
			for (int i = 0; i < o.length; i++) {
				if (o[i] == null) {
					o[i] = n;
				}
			}
			itr.remove();
			itr.add(o);
		}
		queryModel = new ListModelList(queryList, true);
		queryListBox.invalidate();
		queryListBox.setModel(queryModel);
		queryListBox.renderAll();
		queryModel.setMultiple(bMultiple);

		queryPaging = (Paging) queryListBox.getFellow(getPagingId());
		queryPaging.setPageSize(MY_PAGE_SIZE);

		Long iTotalSize;
		iTotalSize = ((BigDecimal) this.qryTotalSize.getSingleResult()).longValue();
		queryPaging.setTotalSize((int) (long) iTotalSize);

		Iterable<EventListener<? extends Event>> eventListeners = queryPaging.getEventListeners(ZulEvents.ON_PAGING);
		for (EventListener<? extends Event> eventListener : eventListeners) {
			queryPaging.removeEventListener(ZulEvents.ON_PAGING, eventListener);
		}

		// 此**listener**只會在，換頁時執行。
		EventListener e1 = new EventListener() {
			public void onEvent(Event event) throws Exception {
				try {
					exec.setAttribute("ColumnSize", xml.getQryField().size());
					List<Object> item = new ArrayList<Object>();
					PagingEvent pagingEvt = (PagingEvent) event;
					iStartPosition = pagingEvt.getActivePage() * MY_PAGE_SIZE;

					setSqlConditions();
					queryList = getQueryPagingbySize().getResultList();

					for (ListIterator<Object[]> itr = queryList.listIterator(); itr.hasNext();) {
						Object[] o = itr.next();
						for (int i = 0; i < o.length; i++) {
							if (o[i] == null) {
								o[i] = n;
							}
						}
						itr.remove();
						itr.add(o);
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
		queryPaging.addEventListener(ZulEvents.ON_PAGING, e1);

		if (iTotalSize >= 1)
			queryPaging.setActivePage(activePage);
	}

	/** 點擊確定鈕 */
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnConfirm")
	public void onClickbtnConfirm(Event event) {
		try {
			HashMap<String, Object> returnFieldMap = new HashMap<String, Object>();
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
					mapReturn.put("selectedRecord",
							(Object) queryListBox.getModel().getElementAt(queryListBox.getSelectedIndex()));
				else
					mapReturn.put("selectedRecord", (Object) arrMulitSelect.get(0));
			}

			mapReturn.put("msg", msg);
			mapReturn.put("parameter", parameter);
			mapReturn.put("ForwardEvent", ForwardEvent);
			mapReturn.put("Event", Event);

			int index = 0;
			if (parentWindow != null) {
				// if (returnTextBoxList != null) {
				index = 0;
				for (QryField field : xml.getQryField()) {
					// returnFieldMap.put(field.getFieldName(),String.valueOf(((Object[])
					// mapReturn.get("selectedRecord"))[index]));

					List<Object[]> dataList = new ArrayList<>();
					if (bMultiple)
						dataList = (List) mapReturn.get("selectedRecord");
					else
						dataList.add((Object[]) mapReturn.get("selectedRecord"));

					String combine = "";
					for (Object[] obj : dataList) {
						combine += "," + obj[index];
					}
					returnFieldMap.put(field.getFieldName(), combine.substring(1));
					if (returnTextBoxList != null)
						for (ReturnBox o : returnTextBoxList) {
							if (o.getFieldName() != null && o.getFieldName().equals(field.getFieldName())) {

								if (getListboxMulit()) {
									String ss = "";
									for (int i = 0; i < arrMulitSelect.size(); i++) {
										ss += "," + String.valueOf(((Object[]) arrMulitSelect.get(i))[index]);
									}
									if (field.getFieldName().equals(returnField)) {
										mapReturn.put("returnValue", ss.substring(1));
										if (field.getType().endsWith("N")) {
											o.getDoubleboxName().setValue(Double.parseDouble(ss.substring(1)));
											Events.postEvent("onChange", o.getDoubleboxName(), null);
										} else if (o.getObj() instanceof Combobox) {
											o.getComboboxName().setValue(ss.substring(1));
											Events.postEvent("onChange", o.getComboboxName(), null);
										} else {
											o.getTextboxName().setValue(ss.substring(1));
											Events.postEvent("onChange", o.getTextboxName(), null);
										}

									} else {

										if (o.getObj() instanceof Combobox) {
											o.getComboboxName().setValue(ss.substring(1));
											Events.postEvent("onChange", o.getComboboxName(), null);
										} else if (o.getTextboxName() != null) {
											o.getTextboxName().setValue(ss.substring(1));
											Events.postEvent("onChange", o.getTextboxName(), null);
										} else if (o.getLabel() != null) {
											o.getLabel().setValue(ss.substring(1));
											Events.postEvent("onChange", o.getLabel(), null);
										}
									}
								} else {
									if (field.getFieldName().equals(returnField)) {
										mapReturn.put("returnValue",
												String.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
										if (o.getObj() instanceof Combobox) {
											o.getComboboxName().setValue(String
													.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
											Events.postEvent("onChange", o.getComboboxName(), null);
										} else if (field.getType().endsWith("N")) {
											o.getDoubleboxName().setValue(Double.parseDouble(String
													.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index])));
											Events.postEvent("onChange", o.getDoubleboxName(), null);
										} else {
											o.getTextboxName().setValue(String
													.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
											Events.postEvent("onChange", o.getTextboxName(), null);
										}
									} else {
										if (o.getObj() instanceof Combobox && o.getComboboxName() != null) {
											o.getComboboxName().setValue(String
													.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
											Events.postEvent("onChange", o.getComboboxName(), null);
										} else if (o.getTextboxName() != null) {
											o.getTextboxName().setValue(String
													.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
											Events.postEvent("onChange", o.getTextboxName(), null);
										} else if (o.getLabel() != null) {
											o.getLabel().setValue(String
													.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
											Events.postEvent("onChange", o.getLabel(), null);
										}
									}
								}
								// o.getTextboxName().setValue(String.valueOf(((ArrayList)
								// mapReturn.get("selectedRecord"))[index]));
								// else if (o.getLabel() != null)
								// o.getLabel().setValue(String.valueOf(((Object[])
								// mapReturn.get("selectedRecord"))[index]));

								// returnTextBoxList.remove(o);
								// if(returnTextBoxList.size()==0){
								// break;
								// }
							}
						}
					index++;
				}

				mapReturn.put("returnFieldMap", returnFieldMap);
				if (returnTextBox != null && returnField != null) {
					index = 0;
					for (QryField field : xml.getQryField()) {
						if (returnField.equals(field.getFieldName())) {
							returnTextBox.setValue(String.valueOf(((Object[]) mapReturn.get("selectedRecord"))[index]));
							Events.postEvent("onChange", returnTextBox, null);
						}

						index++;
					}

				} else {
					Events.sendEvent(new Event(returnMethodName, parentWindow, mapReturn));

				}

				if (getDscQueryField() != null) {
					Events.sendEvent(new Event(returnMethodName, getDscQueryField(), mapReturn));
					Events.sendEvent(new Event(returnMethodName, getDscQueryField().getParent().getParent().getParent(),
							mapReturn));
				}
			}

		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}
	
	/** 點擊取消鈕 */
	@Listen("onClick = #btnCancel")
	public void onClickbtnCancel(Event event) {
		getRootWindow().detach();
	}

	/** 選擇ListBox資料 */
	@Listen("onSelect = #queryListBox")
	public void onSelectqueryListBox(Event evt) {
		BlackBox.listboxMulitSel(this, evt);
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
	
	/** 設定多選 */
	public void setMultiple(boolean bMult) {
		this.bMultiple = bMult;
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
	
	/** 取得XML檔名 */
	public String getXmlQryID() {
		return xmlQryID;
	}
	
	/** 設定XML檔名 */
	public void setXmlQryID(String xmlQryID) {
		this.xmlQryID = xmlQryID;
	}
	
	/** 取得是否為多選 */
	public boolean getListboxMulit() {
		return bMultiple;
	}
	
	/** 取得dscQueryField元件 */
	public dscQueryField getDscQueryField() {
		return dscQueryField;
	}
	
	/** 設定dscQueryField元件 */
	public void setDscQueryField(dscQueryField dscQueryField) {
		this.dscQueryField = dscQueryField;
	}

}
