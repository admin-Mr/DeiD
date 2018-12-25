
package util.admin;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.impl.LabelElement;

import ds.common.services.AuthenticationService;
import ds.common.services.AuthenticationServiceAdminImpl;

public class LogoutController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	//services
	AuthenticationService authService = new AuthenticationServiceAdminImpl();
	
	@Listen("onClick=#logout")
	public void doLogout(){
		authService.logout();
		Executions.sendRedirect("/");
	}
	
	@Listen("onClick = menuitem")
    public void menuAction(MouseEvent event){
		switch (((LabelElement)event.getTarget()).getLabel()) {
			case "Logout":
				authService.logout();
				Executions.sendRedirect("/");
				break;
			case "English":
			default:
				break;
		}
    }
	
	@Listen("onClick=#changePass")
	public void doChangePass(){
		Executions.createComponents("ChangePassword.zul", null, null);
	}
	
}
