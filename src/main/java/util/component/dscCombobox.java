package util.component;

import org.zkoss.zul.Combobox;

public class dscCombobox extends Combobox {
	private Boolean multiLanguage=false;

	public Boolean isMultiLanguage() {
		return this.multiLanguage;	
	}

	public void setMultiLanguage(Boolean multiLanguage) {
		this.multiLanguage = multiLanguage;
	}
	public dscCombobox(){
		
	}
}
