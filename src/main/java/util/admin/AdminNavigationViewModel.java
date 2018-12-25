package util.admin;

import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dspb.domain.DSPB04;
import ds.dspb.domain.DSPB47;
import ds.dspb.domain.DSPB48;
import util.DataMode;
import util.anonymous.MenuNode;


public class AdminNavigationViewModel {
	
	public AdminNavigationViewModel () {
		Sessions.getCurrent().setAttribute("AdminNavigationViewModel", this);
	}
	
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	private String home = dashboard(); //設定首頁
	private String includeSrc = home;

	
	@WireVariable
	private CRUDService CRUDService;
		
	@GlobalCommand("onNavigate")
	@NotifyChange("includeSrc")
	public void onNavigate(@ContextParam(ContextType.TRIGGER_EVENT) MouseEvent event, @BindingParam("menunode") MenuNode menunode) {
		if (menunode != null) {
			String locationUri = menunode.getUri();
			String name = menunode.getName();
			MenuNode temp = new MenuNode(menunode.getLabel(), menunode.getIconSclass());
			Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.TITLE, temp);
			
			//johnny 初始登入或按f5時回到首頁，設定DataMode為read_mode
			if (Sessions.getCurrent().getAttribute("DataMode") == null || includeSrc.equals(home)){
				Sessions.getCurrent().setAttribute("DataMode", DataMode.READ_MODE);
			}

			//johnny 判斷切換作業時是否為編輯模式，若是則跳出提醒訊息
			if (Sessions.getCurrent().getAttribute("DataMode").equals(DataMode.CREATE_MODE) ||
				Sessions.getCurrent().getAttribute("DataMode").equals(DataMode.UPDATE_MODE)){
				Messagebox.show(Labels.getLabel("PUBLIC.MSG0087"));
				return;			
			}		

			//redirect current url to new location
			if (locationUri != null) {
				if(locationUri.startsWith("http")){
					//open a new browser tab
					Executions.getCurrent().sendRedirect(locationUri);
				} else {
					includeSrc = locationUri;

					//advance bookmark control, 
					//bookmark with a prefix
					if(name!=null){
						Executions.getCurrent().getDesktop().setBookmark("p_"+name);
					}
				}			
			}
		}
	}
    
	//帳號>角色>預設
	private String dashboard() {				
		if (CRUDService == null) {
			CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		}
		
		//帳號
		Query qryDSPB48 = CRUDService.getBetweenByLimit2("SELECT t FROM DSPB48 t WHERE t.PB_USERID = :PB_USERID");
		qryDSPB48.setParameter("PB_USERID", _userInfo.getAccount());

		if (qryDSPB48.getResultList().size() > 0) {
			DSPB48 dspb48 = (DSPB48) qryDSPB48.getResultList().get(0);
			if (dspb48.getPB_DASHBOARD() != null && dspb48.getDSPB00_NEW().getPB_FILEPATH() != null) {
				return dspb48.getDSPB00_NEW().getPB_FILEPATH();
			}
		}
		
		//角色
		Query qryDSPB04 = CRUDService.getBetweenByLimit2("SELECT t FROM DSPB04 t WHERE PB_USERID = :PB_USERID");
		qryDSPB04.setParameter("PB_USERID", _userInfo.getAccount());
		
		if (qryDSPB04.getResultList().size() > 0) {
			DSPB04 dspb04 = (DSPB04) qryDSPB04.getResultList().get(0);
			
			Query qryDSPB47 = CRUDService.getBetweenByLimit2("SELECT t FROM DSPB47 t WHERE t.PB_GROUPID = :PB_GROUPID ORDER BY t.PB_GROUPID");
			qryDSPB47.setParameter("PB_GROUPID", dspb04.getPB_GROUPID());

			if (qryDSPB47.getResultList().size() > 0) {
				DSPB47 dspb47 = (DSPB47) qryDSPB47.getResultList().get(0);
				if (dspb47.getPB_DASHBOARD() != null && dspb47.getDSPB00_NEW().getPB_FILEPATH() != null) {
					return dspb47.getDSPB00_NEW().getPB_FILEPATH();
				}			
			}
		}
		
		//default home page
		ServletContext sc = Sessions.getCurrent().getWebApp().getServletContext();
		return sc.getInitParameter("DefaultHomePage");
	}

	public String getIncludeSrc() {
		return includeSrc; 
	}
	
	public void setIncludeSrc(String sUrl) {
		includeSrc = sUrl;
		BindUtils.postNotifyChange(null, null, this, "includeSrc");
	}
}
