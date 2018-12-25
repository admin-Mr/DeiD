package util.info;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import util.menu.ProgramInfo;

public class LoginInfo implements Serializable {
	
	private static Locale locale;
	
	private static HashMap programList = new HashMap();
	
	public LoginInfo(String language) {
		super();
		if (language.endsWith("_CN")) {
			locale = locale.SIMPLIFIED_CHINESE;
		} else if (language.endsWith("_TW")) {
			locale = locale.TRADITIONAL_CHINESE;
		} else {
			locale = new Locale(language);
		}
	}

	public static Locale getLocale() {
		return locale;
	}
	
	public static ProgramInfo findProgramInfo(String id) {
		ProgramInfo programInfo = (ProgramInfo) programList.get(id);
		return programInfo;
	}
	
}
