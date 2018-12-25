package util.admin;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;

import ds.common.domain.User;
import ds.common.services.AuthenticationService;
import ds.common.services.AuthenticationServiceAdminImpl;
import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.common.services.UserInfoService;
import ds.common.services.UserInfoServiceAnonymousImpl;
import util.BlackBox;
import util.Common;
import util.SendEmail;
import util.info.SystemInfo;

public class LoginController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	// wire components
	@Wire
	Textbox account;
	@Wire
	Textbox password;
	@Wire
	Label message;
	@Wire
	Selectbox selLanguage, selCompany;

	HashMap<Integer, String> mapCRUDService = new HashMap<>();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		selLanguage.setModel(Common.createSelectboxModel(Common.langs));
		((ListModelList) selLanguage.getModel()).addToSelection(Common.maplangs.get(BlackBox.getLocale()));
		
		SystemInfo SystemInfo = (SystemInfo) SpringUtil.getBean("systemInfo");
		String[] Companies = new String[SystemInfo.getCompanies().size()];
		for (int i = 0; i < SystemInfo.getCompanies().size(); i++) {
			Companies[i] = SystemInfo.getCompanies().get(i).getCompanyID();
			mapCRUDService.put(i, SystemInfo.getCompanies().get(i).getConnectionName());
		}
		
		selCompany.setModel(Common.createSelectboxModel(Companies));
		((ListModelList) selCompany.getModel()).addToSelection(Companies[0]);
	}

	@Listen("onClick=#login; onOK=#loginWin")
	public void doLogin() {
		try {
			CRUDService CRUDService = (CRUDService) SpringUtil.getBean(mapCRUDService.get(selCompany.getSelectedIndex()));			
			//將登入的CRUDService記錄於Session裡
			Sessions.getCurrent().setAttribute("CRUDService", CRUDService);
			
			String getURL = CRUDService.getEmf().getProperties().get("hibernate.connection.url").toString();
			String[] arrURL = getURL.split(":");
			Common Common = new Common();
			Common.GETDBIP = arrURL[3].substring(1);
			
			Common.USED_DATABASE = arrURL[5];	// DB name
			Common.GETDBIP = arrURL[3].substring(1); // ip位置
			Common.DBConnInfo[0] = Common.GETDBIP; // ip位置
			Common.DBConnInfo[1] = arrURL[4]; // port
			Common.DBConnInfo[2] = Common.USED_DATABASE; // DB name
			
			//將記錄各自的Common物件於Session裡
			Sessions.getCurrent().setAttribute("Common", Common);

			// 抓client的IP
			String clientIP = BlackBox.getClientIp();

			AuthenticationService authService = new AuthenticationServiceAdminImpl();
			String nm = account.getValue().toUpperCase();

			String pd_encry = Convert.process(password.getValue());
			String pd = pd_encry.substring(0, 8);

			if (!authService.loginDB(nm, pd)) {
				message.setValue(Labels.getLabel("COMM.MSG_INVALIDLOGIN"));
				return;
			}
			UserCredential cre = authService.getUserCredential();
			message.setValue("Welcome,  " + cre.getName());
			message.setSclass("");

			System.out.println(cre.getName() + " Login Success!");

			int idxL = selLanguage.getSelectedIndex();
			if (idxL >= 1) {
				Locale locale = new Locale(Common.lOCALE_ALL_LANGUAGES[idxL], Common.lOCALE_ALL_COUNTRYS[idxL]);
				Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, locale);
			}

			// 按下登入時同時寫入DSPB02.LOGINIP與DSPB02.LOGINDATE
			String sql = "UPDATE DSPB02 SET PB_LOGINIP = '" + clientIP + "',PB_LOGINDATE=SYSDATE WHERE PB_USERID='"
					+ account.getValue().toUpperCase() + "'";
			Common.executeSQLStatement(sql, null);

			Executions.sendRedirect("/util/admin/");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@Listen("onClick=#forgetPassword")
	public void onClickForgetPassword(Event event){
		if (account.getValue().trim().isEmpty()){
			alert(Labels.getLabel("LoginWindow.CTL_UserIDNotExist"));
			return;
		}
		UserInfoService userInfoService = new UserInfoServiceAnonymousImpl();
		User user = userInfoService.findUserDB(account.getValue().toUpperCase());
		if (user.getEmail()==null || user.getEmail().trim().isEmpty()){
			alert(Labels.getLabel("LoginWindow.CTL_EmailInfoEmpty"));
			return;			
		}
		String strContent = user.getFullName() + " " + Labels.getLabel("LoginWindow.PasswordMailBody") + user.getUnencrypass();
		ServletContext sc =
			    (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		String[] arrMailSer = sc.getInitParameter("mailServer").split(";");		
		SendEmail sendMail = new SendEmail();
		boolean bReturn = sendMail.send(arrMailSer[0],user.getEmail(), arrMailSer[4], Labels.getLabel("LoginWindow.PasswordMailSubject"), strContent);
		if (bReturn){
			alert(Labels.getLabel("LoginWindow.CTL_PasswordEmailSend") + user.getEmail());
		}else{
			Messagebox.show(Labels.getLabel("LoginWindow.CTL_PasswordEmailError"), "DSISU", 0, Messagebox.ERROR);
		}
	}
}