package ds.dspb.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import ds.dspb.domain.CATEGORY;
import ds.dspb.domain.GROUP;
import util.QueryWindow;

public class DSPB006_USERID_02 extends QueryWindow {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQuery;

	/** RethinkDB連線物件 */
	private Connection conn;

	/** RethinkDB操作物件 */
	private RethinkDB r = RethinkDB.r;

	HashMap<String, Object> mapReturn = new HashMap<String, Object>();

	@Override
	public void doAfterCompose(Component window) throws Exception {
		// rethinkdb連線
		ServletContext sc = Sessions.getCurrent().getWebApp().getServletContext();
		if (sc.getInitParameter("RethinkDB") != null) {
			String[] rethinkDB = sc.getInitParameter("RethinkDB").split(";");
			conn = r.connection().hostname(rethinkDB[0]).user(rethinkDB[1], rethinkDB[2]).port(Integer.valueOf(rethinkDB[3])).connect();
		}
		super.doAfterCompose(window);
		Execution execution = Executions.getCurrent();
		doFillListbox(0);

	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected String getEntityName() {
		return "GROUP";
	}

	@Override
	protected List<String> setComboboxColumn() {
		List<String> listColumn = new ArrayList<String>();
		listColumn.add(Labels.getLabel("RETHINKDB.GROUP.GROUP_ID"));
		listColumn.add(Labels.getLabel("RETHINKDB.GROUP.GROUP_NAME"));
		listColumn.add(Labels.getLabel("RETHINKDB.GROUP.FACTORY"));
		return listColumn;
	}

	@Override
	protected List<String> setComboboxCondition() {
		List<String> listCondition = new ArrayList<String>();
		listCondition.add("LIKE");
		listCondition.add("=");
		return listCondition;
	}

	@Override
	protected String getSQLWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCustomSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCustomCountSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTextBoxValue() {
		return txtQuery.getValue();
	}

	@Override
	protected String getPagingId() {
		return "pagingOperation";
	}

	@Override
	protected String getOrderby() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Combobox getcboColumn() {
		return cboColumn;
	}

	@Override
	protected Combobox getcboCondition() {
		return cboCondition;
	}

	@Override
	protected HashMap getMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List getCustList() {
		List list;

		list = r.db("TODO").table("GROUP").orderBy("GROUP_ID").run(conn);
		if (!txtQuery.getValue().equals("")) {
			switch (cboCondition.getSelectedItem().getLabel()) {
			case "LIKE":
				switch (cboColumn.getSelectedIndex()) {
				case 0:
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("GROUP_ID").match(txtQuery.getValue())).orderBy("GROUP_ID").run(conn);
					break;
				case 1:
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("GROUP_NAME").match(txtQuery.getValue())).orderBy("GROUP_ID").run(conn);
					break;
				case 2:
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("FACTORY").match(txtQuery.getValue())).orderBy("GROUP_ID").run(conn);
					break;
				}
				break;
			case "=":
				switch (cboColumn.getSelectedIndex()) {
				case 0:
					list = r.db("TODO").table("GROUP").filter(r.hashMap("GROUP_ID", txtQuery.getValue())).orderBy("GROUP_ID").run(conn);
					break;
				case 1:
					list = r.db("TODO").table("GROUP").filter(r.hashMap("GROUP_NAME", txtQuery.getValue())).orderBy("GROUP_ID").run(conn);
					break;
				case 2:
					list = r.db("TODO").table("GROUP").filter(r.hashMap("FACTORY", txtQuery.getValue())).orderBy("GROUP_ID").run(conn);
					break;
				}
				break;
			}
		}

		List returnList = new ArrayList<CATEGORY>();

		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> data = ((HashMap<String, Object>) list.get(i));
			GROUP entity = new GROUP(); // DOMAIN
			entity.setGROUP_ID((String) data.get("GROUP_ID"));
			entity.setGROUP_NAME((String) data.get("GROUP_NAME"));
			entity.setFACTORY((String) data.get("FACTORY"));
			returnList.add(entity);
		}
		return returnList;
	}

	/** listbox */
	@Override
	@SuppressWarnings({ "unchecked" })
	public void doFillListbox(int index) {
		try {
			queryList = getCustList();
			queryModel = new ListModelList(queryList, true);
			getQueryLisbBox().invalidate();
			getQueryLisbBox().setModel(queryModel);
			getQueryLisbBox().renderAll();
			queryModel.setMultiple(getListboxMulit());
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}

	}

	@Override
	@Listen("onClick = #btnSearch")
	public void onClickbtnSearch(Event event) {
		try {
			doSearch();
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	@Override
	public void doSearch() {
		
		try {
			doFillListbox(0);
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnConfirm")
	public void onClickbtnConfirm(Event event) {
		try {
			if (getListboxMulitSelect().size() <= 0 && queryListBox.getSelectedItem() == null) {
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}

			if (parentWindow != null)
				getRootWindow().detach();

			mapReturn.put("selectedRecord", getListboxMulitSelect().get(0));

			Events.sendEvent(new Event(returnMethodName, parentWindow, mapReturn));
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}

	}

}
