package util;

import java.util.Locale;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;

public class MultilingualController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	Div divBanner;
	//wire components
	@Wire
	Selectbox selLanguage;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		Messagebox.show("doAfterCompose first");
		super.doAfterCompose(comp);
		divBanner = (Div) comp;
		selLanguage.setModel(Common.createSelectboxModel(Common.langs));
			
		if(BlackBox.getLocale().equals("en"))
			((ListModelList)selLanguage.getModel()).addToSelection(Common.langs[1]);
		if(BlackBox.getLocale().equals("vn"))
			((ListModelList)selLanguage.getModel()).addToSelection(Common.langs[2]);	
		if(BlackBox.getLocale().equals("tw"))
			((ListModelList)selLanguage.getModel()).addToSelection(Common.langs[3]);
		if(BlackBox.getLocale().equals("cn"))
			((ListModelList)selLanguage.getModel()).addToSelection(Common.langs[4]);	
		if(BlackBox.getLocale().equals("in"))
			((ListModelList)selLanguage.getModel()).addToSelection(Common.langs[5]);
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Listen("onSelect = #selLanguage")
	public void changeLocale(){
		String ss = (String) (((ListModelList)selLanguage.getModel()).getSelection().iterator().next());

		int idxL = selLanguage.getSelectedIndex();
		if(idxL >= 0){
			Locale locale = new Locale(Common.lOCALE_ALL_LANGUAGES[idxL], Common.lOCALE_ALL_COUNTRYS[idxL]);  
			Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, locale);
		}
		if(isAnonymous())
			Executions.sendRedirect("");
		else
			Executions.sendRedirect("");
	}

	boolean isAnonymous(){
		int ii = divBanner.getChildren().size();
		if(ii==1)
			return true;
		if(ii==2)
			return false;
		return true;
	}
}



