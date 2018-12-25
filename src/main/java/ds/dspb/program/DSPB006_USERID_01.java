package ds.dspb.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
import ds.dspb.domain.CATEGORY;
import ds.dspb.domain.GROUP;
import ds.dspb.domain.USERID;
import util.Common;
import util.DataMode;
import util.Master;

public class DSPB006_USERID_01 extends Master {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window windowMaster;
	@Wire
	private Button btnCreateMaster, btnQuery, btnPB_ID;
	@Wire
	private Textbox txtQUSER_ID, txtQGROUP_ID, txtQFACTORY, txtUSER_ID, txtGROUP_ID, txtFACTORY;
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
		return USERID.class;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void doCreateDefault() {
		// TODO Auto-generated method stub
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getPagingId() {
		return "";
	}

	@Override
	protected String getEntityName() {
		return "USERID";
	}

	@Listen("onPBID=#masterListbox")
	public void onClickbtnPB_ID(ForwardEvent evt) {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("DSPB006_USERID_01", this);
		Executions.createComponents("/ds/dspb/DSPB006_USERID_02.zul", null, map);
	}

	// 閱覽模式時
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton) {
		mapButton = new HashMap<String, Object>();
		mapButton.put("btncreatemaster", btnCreateMaster); // 新增
		mapButton.put("btnquery", btnQuery); // 查詢
		super.masterReadMode(mapButton);
	}

	/** 取得Listbox SQL 自訂SQL語法使用, 需搭配自訂資料物件 */
	@Override
	protected List getCustList() {
		List list;

		if (!txtQUSER_ID.getValue().isEmpty()) {
			if (!txtQGROUP_ID.getValue().isEmpty()) {
				if (!txtQFACTORY.getValue().isEmpty()) {
					list = r.db("TODO").table("USERID")
							.filter(doc -> doc.g("USER_ID").match(txtQUSER_ID.getValue()).and(doc.g("FACTORY").match(txtQFACTORY.getValue())).and(doc.g("FACTORY").match(txtQFACTORY.getValue())))
							.orderBy("USER_ID").run(conn);
				} else {
					list = r.db("TODO").table("USERID").filter(doc -> doc.g("USER_ID").match(txtQUSER_ID.getValue()).and(doc.g("GROUP_ID").match(txtQGROUP_ID.getValue()))).orderBy("USER_ID")
							.run(conn);
				}
			} else {
				if (!txtQFACTORY.getValue().isEmpty()) {
					list = r.db("TODO").table("USERID").filter(doc -> doc.g("USER_ID").match(txtQUSER_ID.getValue()).and(doc.g("FACTORY").match(txtQFACTORY.getValue()))).orderBy("USER_ID").run(conn);
				} else {
					list = r.db("TODO").table("USERID").filter(doc -> doc.g("USER_ID").match(txtQUSER_ID.getValue())).orderBy("USER_ID").run(conn);
				}
			}
		} else {
			if (!txtQGROUP_ID.getValue().isEmpty()) {
				if (!txtQFACTORY.getValue().isEmpty()) {
					list = r.db("TODO").table("USERID").filter(doc -> doc.g("GROUP_ID").match(txtQGROUP_ID.getValue()).and(doc.g("FACTORY").match(txtQFACTORY.getValue()))).orderBy("USER_ID")
							.run(conn);
				} else {
					list = r.db("TODO").table("USERID").filter(doc -> doc.g("GROUP_ID").match(txtQGROUP_ID.getValue())).orderBy("USER_ID").run(conn);
				}
			} else {
				if (!txtQFACTORY.getValue().isEmpty()) {
					list = r.db("TODO").table("USERID").filter(doc -> doc.g("FACTORY").match(txtQFACTORY.getValue())).orderBy("USER_ID").run(conn);
				} else {
					list = r.db("TODO").table("USERID").orderBy("USER_ID").run(conn);
				}
			}
		}

		List returnList = new ArrayList<CATEGORY>();

		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> data = ((HashMap<String, Object>) list.get(i));
			USERID entity = new USERID(); // DOMAIN
			entity.setGROUP_ID((String) data.get("GROUP_ID"));
			entity.setUSER_ID((String) data.get("USER_ID"));
			entity.setFACTORY((String) data.get("FACTORY"));
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

	/** 點擊修改鈕 */
	@Override
	@SuppressWarnings("unchecked")
	@Listen("onUpdate = #masterListbox")
	public void editMasterListbox(ForwardEvent evt) throws Exception {
		super.editMasterListbox(evt);
		masterListbox.getPagingChild().setDisabled(true); // 編輯時，不能翻頁
		((Textbox) masterListbox.getFellow("txtUSER_ID")).setDisabled(true);
	}

	/** 點擊取消鈕 */
	@Listen("onCancel = #masterListbox")
	public void onCancelmasterListbox(Event event) {
		setData_Mode(DataMode.READ_MODE);
		masterReadMode(null);
		executeQuery();// 重整資料
		masterListbox.getPagingChild().setDisabled(false); // 開啟分頁
	}

	@Override
	public void setSqlConditions() {

	}

	/** 點擊儲存鈕 */
	@Override
	@Listen("onSave = #masterListbox")
	public boolean onSaveMasterListbox(ForwardEvent evt) {
		try {
			txtUSER_ID = (Textbox) masterListbox.getFellow("txtUSER_ID");
			txtGROUP_ID = (Textbox) masterListbox.getFellow("txtGROUP_ID");
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

		if (txtUSER_ID.getValue().equals("")) {
			sMsg += Labels.getLabel("RETHINKDB.USERID.USER_ID") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtGROUP_ID.getValue().equals("")) {
			sMsg += Labels.getLabel("RETHINKDB.USERID.GROUP_ID") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtFACTORY.getValue().equals("")) {
			sMsg += Labels.getLabel("RETHINKDB.USERID.FACTORY") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtUSER_ID.getValue().indexOf(" ") != -1) {
			sMsg += Labels.getLabel("RETHINKDB.USERID.USER_ID") + Labels.getLabel("DSPB005_CATEGORY_01.MSG5") + " \n";
		}
		if (Integer.valueOf(
				r.db("TODO").table("USERID").filter(r.hashMap("USER_ID", txtUSER_ID.getValue()).with("GROUP_ID", txtGROUP_ID.getValue())).orderBy("USER_ID").count().run(conn).toString()) > 0) {
			sMsg += Labels.getLabel("DSPB005_CATEGORY_01.MSG7") + " \n";
		}
		if (getData_Mode().equals(DataMode.CREATE_MODE)
				&& Integer.valueOf(r.db("TODO").table("USERID").filter(r.hashMap("USER_ID", txtUSER_ID.getValue())).orderBy("USER_ID").count().run(conn).toString()) > 0) {
			sMsg += Labels.getLabel("DSPB005_CATEGORY_01.MSG4") + " \n";
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
				r.db("TODO").table("USERID").insert(r.hashMap("GROUP_ID", txtGROUP_ID.getValue()).with("USER_ID", txtUSER_ID.getValue()).with("FACTORY", txtFACTORY.getValue())
						.with("UP_DATE", sdf.format(new Date())).with("UP_USER", cre.getName())).run(conn);
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"));
			} else if (getData_Mode().equals(DataMode.UPDATE_MODE)) {
				r.db("TODO").table("USERID").filter(r.hashMap("USER_ID", txtUSER_ID.getValue()))
						.update(r.hashMap("GROUP_ID", txtGROUP_ID.getValue()).with("FACTORY", txtFACTORY.getValue()).with("UP_DATE", sdf.format(new Date())).with("UP_USER", cre.getName())).run(conn);
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"));
			}

			// 儲存 End
			setData_Mode(DataMode.READ_MODE);
			masterReadMode(null);
			executeQuery();// 重整資料
			masterListbox.getPagingChild().setDisabled(false); // 開啟分頁
		}
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
							r.db("TODO").table("USERID").filter(r.hashMap("GROUP_ID", ((USERID) masterEntity).getGROUP_ID()).with("FACTORY", ((USERID) masterEntity).getFACTORY()).with("USER_ID",
									((USERID) masterEntity).getUSER_ID())).delete().run(conn);
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

	@SuppressWarnings({ "unchecked" })
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event) {
		Map<String, Object> map = (Map<String, Object>) event.getData();
		GROUP e = (GROUP) map.get("selectedRecord");
		((Textbox) masterListbox.getFellow("txtGROUP_ID")).setValue(e.getGROUP_ID());
		((Textbox) masterListbox.getFellow("txtFACTORY")).setValue(e.getFACTORY());
	}
}
