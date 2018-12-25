package ds.dspb.program;

import java.util.ArrayList;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import ds.dspb.domain.CATEGORY;
import util.Common;
import util.DataMode;
import util.Master;

public class DSPB005_CATEGORY_01 extends Master {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnCreateMaster, btnQuery;
	@Wire
	private Textbox txtQCA_ID, txtQCA_NAME, txtCA_ID, txtCA_NAME;

	/** RethinkDB連線物件 */
	private Connection conn;

	/** RethinkDB操作物件 */
	private RethinkDB r = RethinkDB.r;

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

	// window id → windowMaster
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected Class getMasterClass() {
		return DSPB005_CATEGORY_01.class;
	}

	// DataObject
	@Override
	protected Class getEntityClass() {
		return CATEGORY.class;
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

	// 給予Entity的名稱-->domain檔
	@Override
	protected String getEntityName() {
		return "CATEGORY";
	}

	// 閱覽模式時
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
	protected void resetEditAreaMaster(Object entityMaster) {
		// TODO Auto-generated method stub
	}

	/** 取得Listbox SQL 自訂SQL語法使用, 需搭配自訂資料物件 */
	@Override
	protected List getCustList() {
		List list;
		if (!txtQCA_ID.getValue().isEmpty()) {
			if (!txtQCA_NAME.getValue().isEmpty()) {
				list = r.db("TODO").table("CATEGORY").filter(doc -> doc.g("CA_ID").match(txtQCA_ID.getValue()).and(doc.g("CA_NAME").match(txtQCA_NAME.getValue()))).orderBy("CA_ID").run(conn);
			} else {
				list = r.db("TODO").table("CATEGORY").filter(doc -> doc.g("CA_ID").match(txtQCA_ID.getValue())).orderBy("CA_ID").run(conn);
			}
		} else {
			if (!txtQCA_NAME.getValue().isEmpty()) {
				list = r.db("TODO").table("CATEGORY").filter(doc -> doc.g("CA_NAME").match(txtQCA_NAME.getValue())).orderBy("CA_ID").run(conn);
			} else {
				list = r.db("TODO").table("CATEGORY").orderBy("CA_ID").run(conn);
			}
		}
		List returnList = new ArrayList<CATEGORY>();

		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> data = ((HashMap<String, Object>) list.get(i));
			CATEGORY entity = new CATEGORY(); // DOMAIN
			entity.setCA_ID((String) data.get("CA_ID"));
			entity.setCA_NAME((String) data.get("CA_NAME"));
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
		((Textbox) masterListbox.getFellow("txtCA_ID")).setDisabled(true);
	}

	@Override
	public void setSqlConditions() {

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
							r.db("TODO").table("CATEGORY").filter(r.hashMap("CA_ID", ((CATEGORY) masterEntity).getCA_ID()).with("CA_NAME", ((CATEGORY) masterEntity).getCA_NAME())).delete().run(conn);
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

	/** 點擊儲存鈕 */
	@Override
	@Listen("onSave = #masterListbox")
	public boolean onSaveMasterListbox(ForwardEvent evt) {
		try {
			txtCA_ID = (Textbox) masterListbox.getFellow("txtCA_ID");
			txtCA_NAME = (Textbox) masterListbox.getFellow("txtCA_NAME");
			return super.onSaveMasterListbox(evt);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 點擊取消鈕 */
	@Listen("onCancel = #masterListbox")
	public void onCancelmasterListbox(Event event) {
		setData_Mode(DataMode.READ_MODE);
		masterReadMode(null);
		executeQuery();// 重整資料
		masterListbox.getPagingChild().setDisabled(false); // 開啟分頁
	}

	/** 存檔前的檢查 */
	public boolean beforeMasterSave() {
		Boolean reSave = true;
		String sMsg = "";

		if (txtCA_ID.getValue().isEmpty()) {
			sMsg += Labels.getLabel("RETHINKDB.CATEGORY.CA_ID") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtCA_NAME.getValue().isEmpty()) {
			sMsg += Labels.getLabel("RETHINKDB.CATEGORY.CA_NAME") + Labels.getLabel("PUBLIC.MSG0003") + " \n";
		}
		if (txtCA_ID.getValue().indexOf(" ") != -1) {
			sMsg += Labels.getLabel("RETHINKDB.CATEGORY.CA_ID") + Labels.getLabel("DSPB005_CATEGORY_01.MSG5") + " \n";
		}
		if (Integer.valueOf(r.db("TODO").table("CATEGORY").filter(r.hashMap("CA_NAME", txtCA_NAME.getValue())).orderBy("CA_ID").count().run(conn).toString()) > 0) {
			sMsg += Labels.getLabel("DSPB005_CATEGORY_01.MSG2")
					+ r.db("TODO").table("CATEGORY").filter(r.hashMap("CA_NAME", txtCA_NAME.getValue())).orderBy("CA_ID").getField("CA_ID").run(conn).toString() + " \n";
		}
		if (getData_Mode().equals(DataMode.CREATE_MODE)
				&& Integer.valueOf(r.db("TODO").table("CATEGORY").filter(r.hashMap("CA_ID", txtCA_ID.getValue())).orderBy("CA_ID").count().run(conn).toString()) > 0) {
			sMsg += Labels.getLabel("DSPB005_CATEGORY_01.MSG1");
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
				r.db("TODO").table("CATEGORY").insert(r.hashMap("CA_ID", txtCA_ID.getValue()).with("CA_NAME", txtCA_NAME.getValue())).run(conn);
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0081"));
			} else if (getData_Mode().equals(DataMode.UPDATE_MODE)) {
				r.db("TODO").table("CATEGORY").filter(r.hashMap("CA_ID", txtCA_ID.getValue())).update(r.hashMap("CA_NAME", txtCA_NAME.getValue())).run(conn);
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0080"));
			}

			// 儲存 End
			setData_Mode(DataMode.READ_MODE);
			masterReadMode(null);
			executeQuery();// 重整資料
			masterListbox.getPagingChild().setDisabled(false); // 開啟分頁
		}
	}

	@Override
	public String getOrderBy() {
		return "";
	}

}