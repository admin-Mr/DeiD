package ds.common.services;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import ds.common.domain.User;
import ds.common.services.AuthenticationServiceAnonymousImpl;
import ds.common.services.UserCredential;
import ds.common.services.UserInfoService;
import ds.common.services.UserInfoServiceAnonymousImpl;

public class AuthenticationServiceAdminImpl extends AuthenticationServiceAnonymousImpl {
	private static final long serialVersionUID = 1L;

	UserInfoService userInfoService = new UserInfoServiceAnonymousImpl();

	@Override
	public boolean loginDB(String nm, String pd) {
		User user = userInfoService.findUserDB(nm);//****************
		// a simple plan text password verification
		if (user == null || !user.getPassword().equals(pd)) {
			return false;
		}

		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(user.getAccount(), user.getFullName());
		// just in case for this demo.
		if (cre.isAnonymous()) {// not Anonymous  $$$$$$ *****************
			return false;
		}
		sess.setAttribute("userCredential", cre);

		// TODO handle the role here for authorization
		return true;
	}
	
	@Override
	public boolean login(String nm, String pd) {
		User user = userInfoService.findUser(nm);
		// a simple plan text password verification
		if (user == null || !user.getPassword().equals(pd)) {
			return false;
		}

		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(user.getAccount(), user.getFullName());
		// just in case for this demo.
		if (cre.isAnonymous()) {
			return false;
		}
		sess.setAttribute("userCredential", cre);

		// TODO handle the role here for authorization
		return true;
	}

	@Override
	public void logout() {
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("userCredential");
	}
}
