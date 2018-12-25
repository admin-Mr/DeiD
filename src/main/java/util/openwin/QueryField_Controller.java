package util.openwin;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.QueryWindowField;

@SuppressWarnings("serial")
public class QueryField_Controller extends QueryWindowField {
	@Wire
	private Window windowQuery;
	@Wire
	private Button btnSearch, btnConfirm, btnCancel;
	@Wire
	private Combobox cboColumn, cboCondition;
	@Wire
	private Textbox txtQueryFrom,txtQueryTo;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		doSearch();
		txtQueryFrom.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				doSearch();
			}
		});
		txtQueryTo.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				doSearch();
			}
		});
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}
	@Override
	protected String getEntityName() {
		return "";
	}

	@Override
	protected String getTextBoxValue() {
		return txtQueryFrom.getValue();
	}

	@Override
	protected String getPagingId() {
		return "pagingMenuNode";
	}

	@Override
	protected Combobox getcboColumn() {
		return cboColumn;
	}

	@Override
	protected Combobox getcboCondition() {
		return cboCondition;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected HashMap getMap() {
		// TODO Auto-generated method stub
		return null;
	}


}
