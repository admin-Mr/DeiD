package ds.common.program;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import ds.common.domain.Appusers;
import ds.common.services.CRUDService;

@SuppressWarnings({ "rawtypes", "serial", "deprecation" })
public class UserCRUDController extends GenericForwardComposer {

	@WireVariable
	private CRUDService CRUDService;
	private Appusers selectedUser;
	private String recordMode;
	private AnnotateDataBinder binder;
	protected Window userCRUD; // autowired
	protected Button save ,cancel; // autowired
	private Window parentWindow;
	private boolean makeAsReadOnly;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		this.self.setAttribute("controller", this, false);

		final Execution execution = Executions.getCurrent();
		setSelectedUser((Appusers) execution.getArg().get("selectedRecord"));
		setRecordMode((String) execution.getArg().get("recordMode"));
		setParentWindow((Window) execution.getArg().get("parentWindow"));
		setMakeAsReadOnly(false);
		if (recordMode.equals("NEW")) {
			this.selectedUser = new Appusers();
		}
		if (recordMode.equals("EDIT")) {
			this.selectedUser = getSelectedUser();
		}
		if (recordMode == "READ") {
			setMakeAsReadOnly(true);
			save.setVisible(false);
			cancel.setLabel("Ok");
			this.selectedUser = getSelectedUser();
			userCRUD.setTitle(userCRUD.getTitle() + " (READ)");
		}
	}

	public void onCreate(Event event) {
		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
	}

	public void onClick$save(Event event) {
		Map<String, Object> args = new HashMap<String, Object>();
		binder.saveAll();
		CRUDService.save(this.selectedUser);
		userCRUD.detach();
		args.put("selectedRecord", getSelectedUser());
		args.put("recordMode", this.recordMode);
		Events.sendEvent(new Event("onSaved", parentWindow, args));
	}

	public void onClick$cancel(Event event) {
		userCRUD.detach();
	}

	public Appusers getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Appusers selectedUser) {
		this.selectedUser = selectedUser;
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public Window getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}

	public boolean isMakeAsReadOnly() {
		return makeAsReadOnly;
	}

	public void setMakeAsReadOnly(boolean makeAsReadOnly) {
		this.makeAsReadOnly = makeAsReadOnly;
	}
}
