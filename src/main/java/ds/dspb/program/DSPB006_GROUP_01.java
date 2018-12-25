package ds.dspb.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.SimpleDateFormat;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import ds.common.services.AuthenticationService;
import ds.common.services.AuthenticationServiceAdminImpl;
import ds.common.services.UserCredential;
import ds.dspb.domain.GROUP;
import util.Common;
import util.DataMode;
import util.Master;

public class DSPB006_GROUP_01 extends Master {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnCreateMaster, btnQuery;
	@Wire
	private Textbox txtQGROUP_ID, txtQGROUP_NAME, txtQFACTORY, txtGROUP_ID, txtGROUP_NAME, txtFACTORY;
	@Wire
	private Label labUP_DATE, labUP_USER;

	/** RethinkDB連線物件 */
	private Connection conn;

	/** RethinkDB操作物件 */
	private RethinkDB r = RethinkDB.r;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	AuthenticationService authService = new AuthenticationServiceAdminImpl();
	UserCredential cre = authService.getUserCredential();

	@Override
	public void doAfterCompose(Component window) throws Exception {
		// rethinkdb連線
		ServletContext sc = Sessions.getCurrent().getWebApp().getServletContext();
		if (sc.getInitParameter("RethinkDB") != null) {
			String[] rethinkDB = sc.getInitParameter("RethinkDB").split(";");
			conn = r.connection().hostname(rethinkDB[0]).user(rethinkDB[1], rethinkDB[2]).port(Integer.valueOf(rethinkDB[3])).connect();
		}
		super.doAfterCompose(window); // doinit
	}

	@Override
	protected Window getRootWindow() {
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		return DSPB005_CATEGORY_01.class;
	}

	@Override
	protected Class getEntityClass() {
		return GROUP.class;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {
		return "GROUP";
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
	}

	@Override
	public void masterReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster);
		mapButton.put("btnquery", btnQuery);
		super.masterReadMode(mapButton);
	}

	// 新增模式
	// @Override
	// public void masterCreateMode(HashMap<String, Object> mapButton) {
	// super.masterCreateMode(mapButton);
	// }

	@Override
	protected String getPagingId() {
		return "";
	}

	@Override
	protected List getCustList() {
		List list;
		if (!txtQGROUP_ID.getValue().isEmpty()) {
			if (!txtQFACTORY.getValue().isEmpty()) {
				if (!txtQGROUP_NAME.getValue().isEmpty()) {
					list = r.db("TODO").table("GROUP").filter(
							doc -> doc.g("GROUP_ID").match(txtQGROUP_ID.getValue()).and(doc.g("FACTORY").match(txtQFACTORY.getValue())).and(doc.g("GROUP_NAME").match(txtQGROUP_NAME.getValue())))
							.orderBy("GROUP_ID").run(conn);
				} else {
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("GROUP_ID").match(txtQGROUP_ID.getValue()).and(doc.g("FACTORY").match(txtQFACTORY.getValue()))).orderBy("GROUP_ID")
							.run(conn);
				}
			} else {
				if (!txtQGROUP_NAME.getValue().isEmpty()) {
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("GROUP_ID").match(txtQGROUP_ID.getValue()).and(doc.g("GROUP_NAME").match(txtQGROUP_NAME.getValue()))).orderBy("GROUP_ID")
							.run(conn);
				} else {
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("GROUP_ID").match(txtQGROUP_ID.getValue())).orderBy("GROUP_ID").run(conn);
				}
			}
		} else {
			if (!txtQFACTORY.getValue().isEmpty()) {
				if (!txtQGROUP_NAME.getValue().isEmpty()) {
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("FACTORY").match(txtQFACTORY.getValue()).and(doc.g("GROUP_NAME").match(txtQGROUP_NAME.getValue()))).orderBy("GROUP_ID")
							.run(conn);
				} else {
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("FACTORY").match(txtQFACTORY.getValue())).orderBy("GROUP_ID").run(conn);
				}
			} else {
				if (!txtQGROUP_NAME.getValue().isEmpty()) {
					list = r.db("TODO").table("GROUP").filter(doc -> doc.g("GROUP_NAME").match(txtQGROUP_NAME.getValue())).orderBy("GROUP_ID").run(conn);
				} else {
					list = r.db("TODO").table("GROUP").orderBy("GROUP_ID").run(conn);
				}
			}
		}

		List returnList = new ArrayList<GROUP>();

		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> data = ((HashMap<String, Object>) list.get(i));
			GROUP entity = new GROUP(); // DOMAIN
			entity.setGROUP_ID((String) data.get("GROUP_ID"));
			entity.setFACTORY((String) data.get("FACTORY"));
			entity.setGROUP_NAME((String) data.get("GROUP_NAME"));
			entity.setUP_DATE((String) data.get("UP_DATE"));
			entity.setUP_USER((String) data.get("UP_USER"));
			entity.setISADD(false);
			entity.setISEDIT(false);

			returnList.add(entity);
		}

		return returnList;
	}

	/** listbox */
	@Override
	@SuppressWarnings({ "unchecked" })
	public void doFillListbox(int index) {
		try {
			setMY_PAGE_SIZE(getPageSize() <= 0 ? Common.PAGE_SIZE : getPageSize());
			masterList = getCustList();
			masterModel = new ListModelList(masterList, true);
			getmasterListbox().invalidate();
			getmasterListbox().setModel(masterModel);
			getmasterListbox().renderAll();
			masterModel.setMultiple(getListboxMulit());

			try {
				programmacticallySelectListbox(index % getMY_PAGE_SIZE());
			} catch (Exception e) {
				logger.error(null, e);
			}
		} catch (Exception e) {
			logger.error(null, e);
			throw e;
		}
	}

	/** 點擊新增鈕 */
	@Override
	@SuppressWarnings("unchecked")
	@Listen("onClick = #btnCreateMaster")
	public void masterCreate(Event event) {
		super.masterCreate(event);
		masterListbox.getPagingChild().setDisabled(true);
	}

	/** 點擊儲存鈕 */
	@Override
	@Listen("onSave = #masterListbox")
	public boolean onSaveMasterListbox(ForwardEvent evt) {
		try {
			txtGROUP_ID = (Textbox) masterListbox.getFellow("txtGROUP_ID");
			txtGROUP_NAME = (Textbox) masterListbox.getFellow("txtGROUP_NAME");
			txtFACTORY = (Textbox) masterListbox.getFellow("txtFACTORY");
			return super.onSaveMasterListbox(evt);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 存檔前的檢查 */
	public boolean beforeMasterSave() {
		Boolean reSave = true;
		String sMsg = "";

		if (txtGROUP_ID.getValue().equals("")) {
			sMsg += Labels.getLabel("RETHINKDB.GROUP.GROUP_ID") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtGROUP_NAME.getValue().equals("")) {
			sMsg += Labels.getLabel("RETHINKDB.GROUP.GROUP_NAME") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtFACTORY.getValue().equals("")) {
			sMsg += Labels.getLabel("RETHINKDB.GROUP.FACTORY") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtGROUP_ID.getValue().indexOf(" ") != -1) {
			sMsg += Labels.getLabel("RETHINKDB.GROUP.GROUP_ID") + Labels.getLabel("DSPB005_CATEGORY_01.MSG5") + " \n";
		}
		if (txtFACTORY.getValue().indexOf(" ") != -1) {
			sMsg += Labels.getLabel("RETHINKDB.GROUP.FACTORY") + Labels.getLabel("DSPB005_CATEGORY_01.MSG5") + " \n";
		}
		if (getData_Mode().equals(DataMode.CREATE_MODE) && Integer.valueOf(
				r.db("TODO").table("GROUP").filter(r.hashMap("GROUP_ID", txtGROUP_ID.getValue()).with("FACTORY", txtFACTORY.getValue())).orderBy("GROUP_ID").count().run(conn).toString()) > 0) {
			sMsg += Labels.getLabel("DSPB005_CATEGORY_01.MSG6");
		}

		if (sMsg == "") {
			reSave = true;
		} else {
			Messagebox.show(sMsg, "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			reSave = false;
		}
		return reSave;
	}

	/** 執行存檔 */
	@Override
	@SuppressWarnings("unchecked")
	public void masterSave() throws Exception {

		if (beforeMasterSave() == true) {
			if (getData_Mode().equals(DataMode.CREATE_MODE)) {
				r.db("TODO").table("GROUP").insert(r.hashMap("GROUP_ID", txtGROUP_ID.getValue()).with("FACTORY", txtFACTORY.getValue()).with("GROUP_NAME", txtGROUP_NAME.getValue())
						.with("UP_DATE", sdf.format(new Date())).with("UP_USER", cre.getName())).run(conn);
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"));
			} else if (getData_Mode().equals(DataMode.UPDATE_MODE)) {
				r.db("TODO").table("GROUP").filter(r.hashMap("GROUP_ID", txtGROUP_ID.getValue()).with("FACTORY", txtFACTORY.getValue()))
						.update(r.hashMap("GROUP_NAME", txtGROUP_NAME.getValue()).with("UP_DATE", sdf.format(new Date())).with("UP_USER", cre.getName())).run(conn);
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"));
			}

			// 儲存 End
			setData_Mode(DataMode.READ_MODE);
			masterReadMode(null);
			executeQuery();// 重整資料
			masterListbox.getPagingChild().setDisabled(false); // 開啟分頁
		}
	}

	/** 點擊修改鈕 */
	@Override
	@SuppressWarnings("unchecked")
	@Listen("onUpdate = #masterListbox")
	public void editMasterListbox(ForwardEvent evt) throws Exception {
		super.editMasterListbox(evt);
		masterListbox.getPagingChild().setDisabled(true); // 編輯時，不能翻頁
		((Textbox) masterListbox.getFellow("txtGROUP_ID")).setDisabled(true);
		((Textbox) masterListbox.getFellow("txtFACTORY")).setDisabled(true);
	}

	@Override
	public void setSqlConditions() {

	}

	/** 點擊取消鈕 */
	@Listen("onCancel = #masterListbox")
	public void onCancelmasterListbox(Event event) {
		setData_Mode(DataMode.READ_MODE);
		masterReadMode(null);
		executeQuery();// 重整資料
		masterListbox.getPagingChild().setDisabled(false); // 開啟分頁
	}

	/** 點擊刪除鈕 */
	@Override
	@SuppressWarnings("unchecked")
	@Listen("onDelete = #masterListbox")
	public void onDeleteMasterListbox(ForwardEvent evt) {

		if (getData_Mode().equals(DataMode.CREATE_MODE) || getData_Mode().equals(DataMode.UPDATE_MODE))
			return;

		Object masterEntity = listbox.getSelectedItem().getValue();

		if (beforeMasterDel(masterEntity) == false)
			return;

		try {
			Messagebox.show(Labels.getLabel("COMM.DELETE"), Labels.getLabel("PUBLIC.MSG0001"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
				@Override
				public void onEvent(Event e) throws Exception {
					if (Messagebox.ON_OK.equals(e.getName())) {
						try {
							r.db("TODO").table("GROUP").filter(r.hashMap("GROUP_ID", ((GROUP) masterEntity).getGROUP_ID()).with("FACTORY", ((GROUP) masterEntity).getFACTORY()).with("GROUP_NAME",
									((GROUP) masterEntity).getGROUP_NAME())).delete().run(conn);
							executeQuery(); // 重整資料
						} catch (Exception ex) {
							Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "Error", Messagebox.OK, Messagebox.ERROR);
							throw ex;
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

	@Override
	public String getOrderBy() {
		return "";
	}

}
