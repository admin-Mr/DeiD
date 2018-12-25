package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import ds.common.services.UserCredential;
import ds.rethinkdb.PushNoticeThreed;
import ds.rethinkdb.RethinkDB_Listener;
import ds.rethinkdb.domain.NOTICE_LIST;
import ds.rethinkdb.domain.TODO_LIST;

public class NavbarComposer extends SelectorComposer<Component> implements RethinkDB_Listener{
	private static final long serialVersionUID = 1L;
	/**	記錄log */
	protected Logger logger = BlackBox.getLogger(this);
	
	@Wire
	Div anonymous_banner, admin_navbar, divTodoList, divTodoMessage, divInsert, divReturn, divUpdate, 
		divSave, divHeaderTodo, divHeaderToDoEdit, divHeaderCategory, divHeaderNoticeList, divCategory,
		divNoticeList, divReturnNotice;
	@Wire
	A atask, anoti, amsg, taskppCount, notippCount;
	@Wire
	Label askCount, anotiCount, labReturnNotice;
	@Wire
	Listbox lsbTodoList, lsbCategory, lsbNoticeList;
	@Wire
	Grid gdMessage;
	@Wire
	Textbox txtCreateUser, txtCreateDate, txtFinishUser, txtFinishDate, txtContent;
	@Wire
	Datebox dtCreateDate, dtEstimatedDate, dtFinishDate;
	@Wire
	Image imgReturn, imgInsert, imgUpdate, imgSave, imgReturnNotice;
	
	private Desktop desktop;
	private final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	private String userEmail = "";
	private Long lTM_ID;
	private SimpleDateFormat sdf;
	private DataMode data_Mode = DataMode.READ_MODE;
	private ArrayList<NOTICE_LIST> listCATEGORY = new ArrayList<>();
	private HashMap<String, ArrayList<NOTICE_LIST>> mapNOTICE_LIST = new HashMap<>();
	private String sFactoryArea;
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		desktop = Executions.getCurrent().getDesktop();
		desktop.enableServerPush(true);
		PushNoticeThreed.addListener(this);	//設定推播對象
		
		//登入時載入
		ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		if (sc.getInitParameter("RethinkDB") != null && _userInfo != null && !_userInfo.getAccount().equals("anonymous")) {
			if (atask != null && anoti != null) {
				atask.setVisible(true);
				anoti.setVisible(true);
			}
			
			userEmail = _userInfo.getEmail();
			sFactoryArea = BlackBox.getDSPB02Info("PB_FANO");
			setTODOListbox(BlackBox.ptToDoList.askResponse("TODO_LIST", userEmail));
			setNOTICEListbox(BlackBox.ptNotice.askResponse("NOTICE_LIST", userEmail));
			sdf = BlackBox.ptToDoList.getSimpleDateFormat();
		} else {
			if (atask != null && anoti != null) {
				atask.setVisible(false);
				anoti.setVisible(false);
			}
		}
		
	}
	

	@Listen("onOpen = #taskpp")
	public void toggleTaskPopup(OpenEvent event) {
		toggleOpenClass(event.isOpen(), atask);
	}
	
	@Listen("onOpen = #notipp")
	public void toggleNotiPopup(OpenEvent event) {
		toggleOpenClass(event.isOpen(), anoti);
	}
	
	@Listen("onOpen = #msgpp")
	public void toggleMsgPopup(OpenEvent event) {
		toggleOpenClass(event.isOpen(), amsg);
	}

	// Toggle open class to the component
	public void toggleOpenClass(Boolean open, Component component) {
		HtmlBasedComponent comp = (HtmlBasedComponent) component;
		String scls = comp.getSclass();
		if (open) {
			comp.setSclass(scls + " open");
		} else {
			comp.setSclass(scls.replace(" open", ""));
		}
	}

	//server push功能
	public void doAsyncMethod(Callable<Object> call) {
		try {
			Executions.schedule(desktop, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					call.call();
				}
			}, null);
		} catch (DesktopUnavailableException e) {
			PushNoticeThreed.removeListener(this);
		} catch (Exception e) {
			logger.error(null, e);
		}
	}	
	
	//接收RethinkDB API回傳物件, server push功能
	@Override
	public void rethinkDBResponse(String sType, Object data) {
		doAsyncMethod(new Callable<Object>(){
			private Object obj;
			@Override
			public Object call() throws Exception {				
				System.out.println("rethinkDBResponse data:" + data);
				
				if (((HashMap<String, Object>) data).get("new_val") == null) {
					if (((HashMap<String, Object>) data).get("USER_ID") != null && !((HashMap<String, Object>) data).get("USER_ID").equals(userEmail)) {
						return null;
					}
				} else if (!((HashMap<String, Object>)((HashMap<String, Object>) data).get("new_val")).get("USER_ID").equals(userEmail)) 
					return null;
				
				if (admin_navbar != null) {
					//TODO_LIST:待辦事項, NOTICE_LIST:通知
					switch (sType) {
					case "TODO_LIST":
						ArrayList<Object> listTODO = BlackBox.ptToDoList.askResponse("TODO_LIST", userEmail);
						setTODOListbox(listTODO);
						break;
					case "NOTICE_LIST":
						listCATEGORY.clear();
						mapNOTICE_LIST.clear();
						ArrayList<Object> listNOTICE = BlackBox.ptNotice.askResponse("NOTICE_LIST", userEmail);
						setNOTICEListbox(listNOTICE);
						break;
					}
				}
				return null;
			}
			
			public Callable<Object> init(Object obj) {
				this.obj = obj;
				return this;
			}
			
		}.init(data));
	}

	private void setTODOListbox(ArrayList<Object> listTODO) {
		if (listTODO != null) {			
			if (askCount != null && taskppCount != null) {
				askCount.setValue(String.valueOf(listTODO.size()));
				taskppCount.setLabel((String.valueOf(listTODO.size()) + " " + Labels.getLabel("COMM.TODOLIST")));

				ListModelList modelTODO = new ListModelList<>(listTODO, true);
				lsbTodoList.setModel(modelTODO);

				if (listTODO.size() > 10) {		//超過10筆資料，設定顯示筆數
					lsbTodoList.setRows(10);
				} else {
					lsbTodoList.setRows(0);
				}
			}
		}
	}
	
	private void setNOTICEListbox(ArrayList<Object> listNOTICE) {
		if (listNOTICE != null) {
			if (anotiCount != null && notippCount != null) {
				anotiCount.setValue(String.valueOf(listNOTICE.size()));
				notippCount.setLabel(String.valueOf(listNOTICE.size()) + " " + Labels.getLabel("COMM.NOTICELIST"));

				ArrayList<String> temp = new ArrayList<>();
				HashMap<String, String> mapCategory = new HashMap<>();
				HashMap<String, Integer> mapNoticeCount = new HashMap<>();
				temp.add("SYS");  //SYS設定為第一筆

				for (Object obj : listNOTICE) {
					NOTICE_LIST notice_LIST = (NOTICE_LIST) obj;
					if (mapCategory.get(notice_LIST.getCA_ID()) == null) {
						if (!notice_LIST.getCA_ID().equals("SYS")) {								
							temp.add(notice_LIST.getCA_ID());
						}

						mapCategory.put(notice_LIST.getCA_ID(), notice_LIST.getCA_NAME());								
					}

					int i = mapNoticeCount.get(notice_LIST.getCA_ID()) == null ? 0 : mapNoticeCount.get(notice_LIST.getCA_ID());
					mapNoticeCount.put(notice_LIST.getCA_ID(), i + 1);
					notice_LIST.setCONTENT(notice_LIST.getCONTENT()
							.replaceAll("COMM.ADD", Labels.getLabel("COMM.ADD"))
							.replaceAll("COMM.UPDATE", Labels.getLabel("COMM.UPDATE"))
							.replaceAll("COMM.FINISH", Labels.getLabel("COMM.FINISH")));
					ArrayList<NOTICE_LIST> list = mapNOTICE_LIST.get(notice_LIST.getCA_ID());

					if (list == null) {
						list = new ArrayList<>();
					}

					list.add(notice_LIST);
					mapNOTICE_LIST.put(notice_LIST.getCA_ID(), list);	//記錄各類別訊息
				}

				for (String object : temp) {
					if (mapNoticeCount.get(object) != null) {
						NOTICE_LIST category = new NOTICE_LIST();
						category.setCA_ID(object);
						category.setCA_NAME(mapCategory.get(object));							
						category.setNOTICE_COUNT(mapNoticeCount.get(object) == null ? "0" : mapNoticeCount.get(object) < 99 ? mapNoticeCount.get(object).toString() : "99+"); //超過99筆資料，則以99+表示
						listCATEGORY.add(category);
					}
				}

				ListModelList modelCATEGORY = new ListModelList<>(listCATEGORY, true);
				lsbCategory.setModel(modelCATEGORY);
			}
		}
	}
	
	@Listen("onView = #lsbTodoList")
	public void onViewlsbTodoList(ForwardEvent event) {
		divTodoList.setVisible(false);
		divTodoMessage.setVisible(true);
		divHeaderTodo.setVisible(false);
		divHeaderToDoEdit.setVisible(true);
		divReturn.setVisible(true);
		divUpdate.setVisible(true);

		try {
			Event origin = Events.getRealOrigin(event);
			Listitem listitem = (Listitem) origin.getTarget();
			TODO_LIST entity = (TODO_LIST) listitem.getValue();
			lTM_ID = entity.getTM_ID();
			txtCreateUser.setValue(entity.getCREATE_USER());			
			dtCreateDate.setValue(entity.getCREATE_DATE() != null && !entity.getCREATE_DATE().isEmpty() ? sdf.parse(entity.getCREATE_DATE()) : null);
			dtEstimatedDate.setValue(entity.getESTIMATED_DATE() != null && !entity.getESTIMATED_DATE().isEmpty() ? sdf.parse(entity.getESTIMATED_DATE()) : null);
			txtFinishUser.setValue(entity.getFINISH_USER());
			dtFinishDate.setValue(entity.getFINISH_DATE() != null && !entity.getFINISH_DATE().isEmpty() ? sdf.parse(entity.getFINISH_DATE()) : null);
			txtContent.setValue(entity.getCONTENT());
			
			if (entity.getFINISH_USER().isEmpty() || entity.getFINISH_USER().equals(userEmail))
				divUpdate.setVisible(true);
			else 
				divUpdate.setVisible(false);
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
	
	
	@Listen("onClick = #imgReturn")
	public void onClickdivReturn(Event event) {
		divTodoList.setVisible(true);
		divTodoMessage.setVisible(false);

		divHeaderTodo.setVisible(true);
		divHeaderToDoEdit.setVisible(false);
		divReturn.setVisible(false);
		divUpdate.setVisible(false);
		divSave.setVisible(false);
		
		dtFinishDate.setDisabled(true);
		dtEstimatedDate.setDisabled(true);
		txtContent.setReadonly(true);
		
		txtCreateUser.setValue(null);
		dtCreateDate.setValue(null);
		txtFinishUser.setValue(null);
		dtFinishDate.setValue(null);
		dtEstimatedDate.setValue(null);
		txtContent.setValue(null);
	}
	
	
	@Listen("onClick = #imgInsert")
	public void onClickdivInsert(Event event) {
		data_Mode = DataMode.CREATE_MODE;
		
		divTodoList.setVisible(false);
		divTodoMessage.setVisible(true);
		divHeaderTodo.setVisible(false);
		divHeaderToDoEdit.setVisible(true);
		divReturn.setVisible(true);
		divSave.setVisible(true);
		
		txtCreateUser.setValue(userEmail);
		dtCreateDate.setValue(new Date());
		dtEstimatedDate.setDisabled(false);
		txtContent.setReadonly(false);
	}
	
	@Listen("onClick = #imgUpdate")
	public void onClickdivUpdate(Event event) { 
		data_Mode = DataMode.UPDATE_MODE;
		
		divUpdate.setVisible(false);
		divSave.setVisible(true);		
		dtFinishDate.setDisabled(false);
		
		if (txtCreateUser.getValue().equals(userEmail)) {
			dtEstimatedDate.setDisabled(false);
			txtContent.setReadonly(false);
		}		
	}
	
	@Listen("onClick = #imgSave")
	public void onClickdivSave(Event event) {
		if (userEmail == null) {
			Messagebox.show(Labels.getLabel("NavbarComposer.MSG3"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		} else if (txtContent.getValue() == null || txtContent.getValue().isEmpty()) {
			Messagebox.show(Labels.getLabel("NavbarComposer.MSG1"));
			return;
		} else {
			divUpdate.setVisible(true);
			divSave.setVisible(false);
			
			dtFinishDate.setDisabled(true);
			dtEstimatedDate.setDisabled(true);
			txtContent.setReadonly(true);

			try {
				 if (data_Mode.equals(DataMode.CREATE_MODE)) {
					 /*_userInfo.getEmail().toString()*/
					 BlackBox.ptToDoList.addListToUser(userEmail, sFactoryArea, "SYS", txtContent.getValue(), userEmail, dtEstimatedDate.getValue(), null);
				 } else if (data_Mode.equals(DataMode.UPDATE_MODE)) {
					 BlackBox.ptToDoList.updateToDoMessage(userEmail, lTM_ID, dtEstimatedDate.getValue(), dtFinishDate.getValue(), txtContent.getValue());	 
				 }
				
			} catch (Exception e) {
				logger.error(null, e);
				//存檔失敗
			}			
		}

	}
	
	@Listen("onCheck = #lsbTodoList")
	public void onChecklsbTodoList(ForwardEvent event) {
		try {
			Event origin = Events.getRealOrigin(event);
			Checkbox chk = (Checkbox) origin.getTarget();
			Listitem listitem = (Listitem) origin.getTarget().getParent().getParent();
			TODO_LIST entity = (TODO_LIST) listitem.getValue();
			lTM_ID = entity.getTM_ID();

			if (chk.isChecked()) {
				BlackBox.ptToDoList.updateToDoMessage(userEmail, lTM_ID, entity.getESTIMATED_DATE() == null || entity.getESTIMATED_DATE().isEmpty() ? null : sdf.parse(entity.getESTIMATED_DATE()), new Date(), entity.getCONTENT());
			} else {
				BlackBox.ptToDoList.updateToDoMessage(userEmail, lTM_ID, entity.getESTIMATED_DATE() == null || entity.getESTIMATED_DATE().isEmpty() ? null : sdf.parse(entity.getESTIMATED_DATE()), null, entity.getCONTENT());
			}
			
		} catch (Exception e) {
			logger.error(null, e);
			//修改完成日失敗
			Messagebox.show(Labels.getLabel("NavbarComposer.MSG2"), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@Listen("onView = #lsbCategory")
	public void onViewlsbNoticeList(ForwardEvent event) {
		divHeaderCategory.setVisible(false);
		divHeaderNoticeList.setVisible(true);
		divCategory.setVisible(false);
		divNoticeList.setVisible(true);
		
		Event origin = Events.getRealOrigin(event);
		Listitem item = (Listitem) origin.getTarget();
		NOTICE_LIST entity = (NOTICE_LIST) item.getValue();
		
		labReturnNotice.setValue(entity.getCA_NAME());
		ListModelList listmodellist = new ListModelList(mapNOTICE_LIST.get(entity.getCA_ID()), true);
		lsbNoticeList.setModel(listmodellist);
		
		List keys = mapNOTICE_LIST.get(entity.getCA_ID());
		
		if (keys.size() > 10) {		//超過10筆資料，設定顯示筆數
			lsbNoticeList.setRows(10);
		} else {
			lsbNoticeList.setRows(0);
		}
	}
	
//	@Listen("onClick = #divReturnNotice")
	@Listen("onClick = #imgReturnNotice")
	public void onClickdivReturnNotice(Event event) {
		divHeaderCategory.setVisible(true);
		divHeaderNoticeList.setVisible(false);
		divCategory.setVisible(true);
		divNoticeList.setVisible(false);
	}
	
	@Listen("onCheck = #lsbNoticeList")
	public void onChecklsbNoticeList(ForwardEvent event) {
		Event origin = Events.getRealOrigin(event);
		Checkbox chk = (Checkbox) origin.getTarget();
		Listitem listitem = (Listitem) origin.getTarget().getParent().getParent();
		NOTICE_LIST entity = (NOTICE_LIST) listitem.getValue();
		
		if (entity != null) {
			if (chk.isChecked()) {
				BlackBox.ptNotice.updateFlag("NOTICE", sFactoryArea, userEmail, entity.getNM_ID(), "Y");
			} else {
				BlackBox.ptNotice.updateFlag("NOTICE", sFactoryArea, userEmail, entity.getNM_ID(), "N");
			}
		}
	}
	
	@Listen("onLink = #lsbTodoList")
	public void onLinklsbTodoList(ForwardEvent event) {
		Event origin = Events.getRealOrigin(event);
		Listitem listitem = (Listitem) origin.getTarget().getParent().getParent();
		TODO_LIST entity = (TODO_LIST) listitem.getValue();
		Executions.getCurrent().sendRedirect(entity.getURL(), "_blank");
	}
}
