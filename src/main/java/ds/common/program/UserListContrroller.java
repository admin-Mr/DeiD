package ds.common.program;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ds.common.domain.Appusers;
import ds.common.services.CRUDService;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class UserListContrroller extends GenericForwardComposer {

	@WireVariable
	private CRUDService CRUDService;
	private static final long serialVersionUID = 1L;
	protected Listbox UserListbox; // autowired
	protected Window userList; // autowired
	// Databinding
	private AnnotateDataBinder binder;
	private BindingListModelList<Appusers> appUsersList;
	private Appusers appUsers ,selectedUser;

	public UserListContrroller() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		this.self.setAttribute("controller", this, false);
	}

	public void onCreate$userList(Event event) throws Exception {
		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		setSelectedUser(null);
		doFillListbox();

		this.binder.loadAll();
	}

	public void doFillListbox() {
		CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		List<Appusers> list = CRUDService.getAll(Appusers.class);
		appUsersList = new BindingListModelList<Appusers>(list, true);
		setAppUsersList(appUsersList);
		this.UserListbox.setModel(appUsersList);
	}
	/*
	 * This method will be called when user press the new button in the Listing
	 * screen
	 */
	public void onClick$btnNew(Event event) {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", null);
		map.put("recordMode", "NEW");
		map.put("parentWindow", userList);
		Executions.createComponents("apps/user/UserCRUD.zul", null, map);
	}

	public void onEdit$UserListbox(ForwardEvent evt) {
		Event origin = Events.getRealOrigin(evt);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent().getParent();
		Appusers curUser = (Appusers) litem.getValue();
		setSelectedUser(curUser);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", curUser);
		map.put("recordMode", "EDIT");
		map.put("parentWindow", userList);
		Executions.createComponents("apps/user/UserCRUD.zul", null, map);
	}

	public void onView$UserListbox(ForwardEvent evt) {
		Event origin = Events.getRealOrigin(evt);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent().getParent();
		Appusers curUser = (Appusers) litem.getValue();
		setSelectedUser(curUser);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("selectedRecord", curUser);
		map.put("recordMode", "READ");
		map.put("parentWindow", userList);
		Executions.createComponents("apps/user/UserCRUD.zul", null, map);
	}
	/**
	 * This method is actualy an event handler triggered only from the edit
	 * dialog and it is responsible to reflect the data changes made to the
	 * list.
	 */
	@SuppressWarnings({ "unchecked" })
	public void onSaved(Event event) {
		Map<String, Object> args = (Map<String, Object>) event.getData();
		String recordMode = (String) args.get("recordMode");
		Appusers curUser = (Appusers) args.get("selectedRecord");
		if (recordMode.equals("NEW")) {
			appUsersList.add(curUser);
		}
		if (recordMode.equals("EDIT")) {
			appUsersList.set(appUsersList.indexOf(selectedUser), curUser);
		}
	}

	@SuppressWarnings("unchecked")
	public void onDelete$UserListbox(ForwardEvent evt) {
		Event origin = Events.getRealOrigin(evt);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent().getParent();
		Appusers curUser = (Appusers) litem.getValue();
		setAppUsers(curUser);
		Messagebox.show(Labels.getLabel("PUBLIC.MSG0016"), Labels.getLabel("PUBLIC.MSG0001"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
			public void onEvent(Event e) {
				if (Messagebox.ON_OK.equals(e.getName())) {
					CRUDService.delete(appUsers);
					// update the model of listbox
					appUsersList.remove(appUsers);
					setAppUsers(null);
				} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
					// Cancel is clicked
				}
			}
		});
	}

	public BindingListModelList<Appusers> getAppUsersList() {
		return appUsersList;
	}

	public void setAppUsersList(BindingListModelList<Appusers> appUsersList) {
		this.appUsersList = appUsersList;
	}

	public Appusers getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Appusers selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Appusers getAppUsers() {
		return appUsers;
	}

	public void setAppUsers(Appusers appUsers) {
		this.appUsers = appUsers;
	}

}
