package util.anonymous;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;

public class AnonymousNavigationViewModel {
	private String includeSrc = "/util/admin/index.zul";

	@GlobalCommand("onNavigate")
	@NotifyChange("includeSrc")
	public void onNavigate(@ContextParam(ContextType.TRIGGER_EVENT) MouseEvent event, @BindingParam("menunode") MenuNode menunode) {
		if(menunode == null)
			return;
		String locationUri = menunode.getUri();
		String name = menunode.getName();
		
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

	//先檢查SESSION是否有資料,若有就清除
	public String getIncludeSrc() {
		 if (Sessions.getCurrent().getAttribute("userCredential") != null)
			   Sessions.getCurrent().removeAttribute("userCredential");  
		return includeSrc;
	}
	
}
