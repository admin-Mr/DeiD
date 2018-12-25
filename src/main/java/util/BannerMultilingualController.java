package util;

import java.util.Locale;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.impl.LabelElement;

public class BannerMultilingualController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	@Listen("onClick = menuitem")
	public void menuAction(MouseEvent event) {
		int idxL = 1;
		switch (((LabelElement) event.getTarget()).getId()) {
		case "eng":
			idxL = 1;
			System.out.println("Press English :" + idxL);
			break;
		case "viet":
			idxL = 2;
			System.out.println("Press Vietnam :" + idxL);
			break;
		case "tc":
			idxL = 3;
			System.out.println("Press Traditional Chinese :" + idxL);
			break;
		case "sc":
			idxL = 4;
			System.out.println("Press Simplied Chinese :" + idxL);
			break;
		case "indo":
			idxL = 5;
			System.out.println("Press Indonesia :" + idxL);
			break;
		default:
			idxL = 1;
			break;
		}
		Locale locale = new Locale(Common.lOCALE_ALL_LANGUAGES[idxL], Common.lOCALE_ALL_COUNTRYS[idxL]);
		Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, locale);
		Executions.sendRedirect("");
	}
}