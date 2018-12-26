package ds.dsid.domain;

// 針車組底配套資料查詢

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

public class ReadIDPic03Dao {

	private String MODEL_NA;
	private String URL1;
	private String ROUND;
	private String TOOLING_SIZE;
	private String PG_DATE;
	private String SEWING_DATE;
	private String BOTTOM_TIME;
	private String BOO;
	
	
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	public String getURL1() {
		return URL1;
	}
	public void setURL1(String uRL1) {
		URL1 = uRL1;
	}
	
	public String getROUND() {
		return ROUND;
	}
	public void setROUND(String rOUND) {
		ROUND = rOUND;
	}
	
	public String getTOOLING_SIZE() {
		return TOOLING_SIZE;
	}
	public void setTOOLING_SIZE(String tOOLING_SIZE) {
		TOOLING_SIZE = tOOLING_SIZE;
	}
	public String getPG_DATE() {
		return PG_DATE;
	}
	public void setPG_DATE(String pG_DATE) {
		PG_DATE = pG_DATE;
	}
	public String getSEWING_DATE() {
		return SEWING_DATE;
	}
	public void setSEWING_DATE(String sEWING_DATE) {
		SEWING_DATE = sEWING_DATE;
	}
	public String getBOTTOM_TIME() {
		return BOTTOM_TIME;
	}
	public void setBOTTOM_TIME(String bOTTOM_TIME) {
		BOTTOM_TIME = bOTTOM_TIME;
	}
	public String getBOO() {
		return BOO;
	}
	public void setBOO(String bOO) {
		BOO = bOO;
	}
	

}
