
package ds.common.services;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AuthenticationInit implements Initiator {

	//services
	AuthenticationService authService = new AuthenticationServiceAdminImpl();
	
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		
		UserCredential cre = authService.getUserCredential();
		if(cre==null || cre.isAnonymous()){
			Executions.sendRedirect("/util/admin/login.zul");
			return;
		}
	}
}