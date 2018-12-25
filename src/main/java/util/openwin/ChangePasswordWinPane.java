package util.openwin;

import java.util.ArrayList;
import javax.persistence.Query;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import util.BlackBox;
import util.Common;
import util.admin.Convert;
import ds.common.services.AuthenticationService;
import ds.common.services.AuthenticationServiceAdminImpl;
import ds.common.services.CRUDService;
import ds.common.services.UserCredential;

@SuppressWarnings({"serial" })
public class ChangePasswordWinPane extends SelectorComposer<Component> {
	@WireVariable
	private CRUDService CRUDService;
	@Wire private Window changeList;
	@Wire private Textbox oldPass ,newPass ,confirmPass;
	@Wire private Label message;
	// services
	AuthenticationService authService = new AuthenticationServiceAdminImpl();
	String ss ="";

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
	}

	@Listen("onClick =#btnSave")
	public void clickBtnSave() throws InterruptedException {
		//String getAccount = UserCredential.getAccount();
		UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
		String getAccount = _userInfo.getAccount();
		
		String oldpd_encry = Convert.process(oldPass.getValue());
		String oldpd = oldpd_encry.substring(0, 8);
		
		Query q = CRUDService.getBetweenByLimit2("select t from DSPB02 t where t.PB_USERID = '"+getAccount+"' and t.PB_PASSENCRY ='"+ oldpd +"'");
		ArrayList<String> checkPass = (ArrayList<String>) q.setFirstResult(0).setMaxResults(Common.LISTBOX_TOTAL_1000).getResultList();
		if (checkPass.size() ==0){
			message.setValue(Labels.getLabel("PUBLIC.MSG0100"));
			return;
		}

		if (!newPass.getValue().equals(confirmPass.getValue()) || //新密碼是否相同
				newPass.getValue()==null || newPass.getValue().equals("") || //新密碼不可空
				confirmPass.getValue() ==null || confirmPass.getValue().equals("")){ // 密碼確認不可空
			message.setValue(Labels.getLabel("PUBLIC.MSG0101"));
			return;
		}

		String newpd_encry = Convert.process(confirmPass.getValue());
		String newpd = newpd_encry.substring(0, 8);
		
		String updatePass = "UPDATE DSPB02 SET PB_PASSENCRY = '"+ newpd +"' WHERE PB_USERID='" + getAccount + "'";
		
		try {
			if (BlackBox.executeSQLStatement(updatePass, null)) {
				message.setValue(Labels.getLabel("PUBLIC.MSG0102")); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		message.setSclass("");
		authService.logout();		
		Executions.sendRedirect("");
	}
	
	@Listen("onClick =#btnCancel")
	public void clickBtnCancel() {
		changeList.detach();
	}
}
