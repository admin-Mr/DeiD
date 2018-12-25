package util.function;

import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import util.anonymous.MenuNode;

@SuppressWarnings({ "rawtypes", "serial" })
public class BugReturnWinPane extends GenericForwardComposer {
	@SuppressWarnings("unused")
	private CRUDService CRUDService;
	protected Window bugList;
	// private CourseListContrroller mCourseListContrroller;
	private Textbox BugText, subject, questions, commenter, tel;
	private Combobox qType_combobox, dep_combobox;
	private ListModel typeInfos;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		this.self.setAttribute("controller", this, false);
		// 問題類型下拉
		comboboxData();
		// 取menu名稱
		MenuNode catchTemp = (MenuNode) Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.TITLE);
		if (catchTemp != null) {
			BugText.setValue(catchTemp.getLabel());
		}
	}

	public void mySend() {
		try {
			SimpleEmail email = new SimpleEmail(); // 構造一個mail對象
			email.setHostName("10.1.1.7");// 設置主機名
			email.addTo("dsgcaresystem@deanshoes.com"); // 設置發對象
			email.setFrom("dsgcaresystem@deanshoes.com", "DSG CARE"); // 設置發送人
			email.setSubject(qType_combobox.getValue() + subject.getValue());// 設置主題
			email.setCharset("GBK");// 設置發送使用的字符集
			String content = "作業名稱:" + BugText.getValue() + "\n" + "問題描述:" + questions.getValue() + "\n" + "提出單位:"
					+ dep_combobox.getValue() + "\n" + "提出人員:" + commenter.getValue() + "\n" + "聯絡分機:" + tel.getValue();// 內容

			email.setContent(content, "text/plain;charset=GBK");// 設置內容
			email.send();// 發送
		} catch (EmailException e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void onClick$searchQuery(Event e) {
		if (BugText.getValue() == null || BugText.getValue().equals("")) {
			alert("作業不可為空");
			return;
		} else if (qType_combobox.getValue() == null || qType_combobox.getValue().equals("")) {
			alert("問題類別不可為空");
			return;
		} else if (subject.getValue() == null || subject.getValue().equals("")) {
			alert("主旨不可為空");
			return;
		} else if (questions.getValue() == null || questions.getValue().equals("")) {
			alert("問題描述不可為空");
			return;
		} else if (dep_combobox.getValue() == null || dep_combobox.getValue().equals("")) {
			alert("提出單位不可為空");
			return;
		} else if (commenter.getValue() == null || commenter.getValue().equals("")) {
			alert("提出人員不可為空");
			return;
		} else if (tel.getValue() == null || tel.getValue().equals("")) {
			alert("提出人員不可為空");
			return;
		}
		Messagebox.show("送出的作業為" + "<<<" + BugText.getValue() + ">>>" + Labels.getLabel("PUBLIC.MSG0016"),
				Labels.getLabel("PUBLIC.MSG0001"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e) {
						if (Messagebox.ON_OK.equals(e.getName())) {
							mySend();	// mail
							bugList.detach();
						} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
							// Cancel is clicked
						}
					}
				});
	}

	String[] langs = { "【系統問題單-既有功能修正】", "【系統問題單-新增功能】" };

	@SuppressWarnings({ "unchecked" })
	private List<String> comboboxData() {
		List<String> data = new ListModelList<String>();
		for (int i = 0; i < 2; i++) {
			data.add(langs[i]);
		}
		setTypeInfos(new ListModelArray(data)); // 把值匯入COMBOBOX
		qType_combobox.setModel(typeInfos);
		qType_combobox.setValue(langs[0]); // 選擇第一筆
		return data;
	}

	public void setTypeInfos(ListModel typeInfos) {
		this.typeInfos = typeInfos;
	}

	public ListModel getTypeInfos() {
		return typeInfos;
	}

}
