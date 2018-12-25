package util.openwin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import util.anonymous.MenuNode;
import util.Common;

@SuppressWarnings({ "rawtypes", "serial", "deprecation" })
public class BugReturnWinPane extends GenericForwardComposer {
	private CRUDService CRUDService;
	protected Window bugList;
	//	private CourseListContrroller mCourseListContrroller;
	private Textbox BugText ,IssueMain ,IssueDetail ,Proposer;
	private Combobox IssueItem_combobox ,ProposeUnit_combobox;
	private Decimalbox Extension;
	private ListModel itemInfos, unitInfos;

	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		this.self.setAttribute("controller", this, false);
		//		final Execution execution = Executions.getCurrent();
		MenuNode catchTemp = (MenuNode)Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.TITLE);
		if(catchTemp != null){
			BugText.setValue(catchTemp.getLabel());
		}

		Query q = CRUDService.getBetweenByLimit2("Select distinct t.PB_FANO from DSPB02 t where t.PB_FANO is not null");
		ArrayList<String> arrTra_ID = (ArrayList<String>) q.setFirstResult(0).setMaxResults(Common.LISTBOX_TOTAL_1000)
				.getResultList();
		List<String> courseReason = new ArrayList<String>();
		for (int i = 0; i < arrTra_ID.size(); i++) {
			courseReason.add(arrTra_ID.get(i));
		}
		setUnitInfos(new ListModelArray(courseReason)); // 把值匯入COMBOBOX
		ProposeUnit_combobox.setModel(unitInfos);
		ProposeUnit_combobox.setValue(arrTra_ID.get(0));
		
		comboboxData();
	}
	@SuppressWarnings("unchecked")
	public void onClick$searchQuery(Event e) {
		if (BugText.getValue() == null || BugText.getValue().equals("") ){
			alert("作業名稱不可為空");
			return;
		}
		if (IssueMain.getValue() == null || IssueMain.getValue().equals("") ){
			alert("問題主旨不可為空");
			return;
		}
		if (IssueDetail.getValue() == null || IssueDetail.getValue().equals("") ){
			alert("問題描述不可為空");
			return;
		}
		if (Proposer.getValue() == null || Proposer.getValue().equals("") ){
			alert("提出人員不可為空");
			return;
		}
		if (Extension.getValue() == null || Extension.getValue().equals("") ){
			alert("聯絡分機不可為空");
			return;
		}
		Messagebox.show("送出的作業為"+"<<<"+BugText.getValue()+">>>"+Labels.getLabel("PUBLIC.MSG0016"), Labels.getLabel("PUBLIC.MSG0001"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
			public void onEvent(Event e) {
				if (Messagebox.ON_OK.equals(e.getName())) {
					 try {
					      SimpleEmail email = new SimpleEmail(); // 構造一個mail對象
//					      email.getHostName();
					      email.setHostName("10.1.1.7");// 設置主機名
					      email.addTo("dsglearningmaster@deanshoes.com");// 設置發對象
					      email.setFrom("dsglearningmaster@deanshoes.com", "LearningMaster");// 設置發送人
					      
					      email.setSubject(IssueItem_combobox.getValue()+ IssueMain.getValue());// 設置主題
					      email.setCharset("GBK");// 設置發送使用的字符集
					      String content = 	"作業名稱:" + BugText.getValue() + "\n" +
					    		  			"問題描述:" + IssueDetail.getValue() + "\n" +
					    		  			"提出單位:"+ ProposeUnit_combobox.getValue() + "\n"+
					    		  			"提出人員:"+ Proposer.getValue() +"\n"+
					    		  			"聯絡分機:"+ Extension.getValue();// 內容
					      email.setContent(content, "text/plain;charset=GBK");// 設置內容
					      email.send();// 發送
					     } catch (EmailException e1) {
					      e1.printStackTrace();
					     }
					bugList.detach();
				} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
					// Cancel is clicked
				}
			}
		});
	}
	
	final String[] item = {"【系統問題單-既有功能修正】","【系統問題單-新增功能】" };
	@SuppressWarnings("unchecked")
	private List<String> comboboxData() {
		List<String> data = new ListModelList<String>();
		for (int i = 0; i < 2; i++) {
			data.add(item[i]);
		}
		setItemInfos(new ListModelArray(data)); // 把值匯入COMBOBOX
		IssueItem_combobox.setModel(itemInfos);
		IssueItem_combobox.setValue(item[0]); // 選擇第一筆
		return data;
	}

	public static void mySend() {
	     try {
	      SimpleEmail email = new SimpleEmail(); // 構造一個mail對象
	      email.getHostName();
	      email.setHostName("li72-pc");// 設置主機名
	      email.addTo("hsiaoying369@gmail.com", "li");// 設置發對象
	      email.setFrom("hsiaoying369@gmail.com", "my");// 設置發送人
	      email.setSubject("郵件測試");// 設置主題
	      email.setCharset("GBK");// 設置發送使用的字符集
	      String content = "測試內容是我自己的";// 內容
	  
	      email.setContent(content, "text/plain;charset=GBK");// 設置內容
	  
	      email.send();// 發送
	     } catch (EmailException e) {
	      e.printStackTrace();
	     }
	  
	    }
	
	public void setItemInfos(ListModel itemInfos) {
		this.itemInfos = itemInfos;
	}

	public ListModel getItemInfos() {
		return itemInfos;
	}
	
	public void setUnitInfos(ListModel unitInfos) {
		this.unitInfos = unitInfos;
	}

	public ListModel getUnitInfos() {
		return unitInfos;
	}

}
