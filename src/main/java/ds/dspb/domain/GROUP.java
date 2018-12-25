package ds.dspb.domain;

import java.io.Serializable;

import javax.persistence.Transient;

public class GROUP implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String GROUP_ID; // 群組編號
	private java.lang.String FACTORY; // 廠別
	private java.lang.String GROUP_NAME; // 群組名稱
	private java.lang.String UP_DATE; // 修改日期
	private java.lang.String UP_USER; // 修改者

	private boolean ISADD;
	private boolean ISEDIT;

	public java.lang.String getGROUP_ID() {
		return GROUP_ID;
	}

	public void setGROUP_ID(java.lang.String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}

	public java.lang.String getGROUP_NAME() {
		return GROUP_NAME;
	}

	public void setGROUP_NAME(java.lang.String gROUP_NAME) {
		GROUP_NAME = gROUP_NAME;
	}

	public java.lang.String getUP_DATE() {
		return UP_DATE;
	}

	public void setUP_DATE(java.lang.String uP_DATE) {
		UP_DATE = uP_DATE;
	}

	public java.lang.String getUP_USER() {
		return UP_USER;
	}

	public void setUP_USER(java.lang.String uP_USER) {
		UP_USER = uP_USER;
	}

	public java.lang.String getFACTORY() {
		return FACTORY;
	}

	public void setFACTORY(java.lang.String fACTORY) {
		FACTORY = fACTORY;
	}

	@Transient
	public boolean getISADD() {
		return this.ISADD;
	}

	public void setISADD(boolean ISADD) {
		this.ISADD = ISADD;
	}

	@Transient
	public boolean getISEDIT() {
		return this.ISEDIT;
	}

	public void setISEDIT(boolean ISEDIT) {
		this.ISEDIT = ISEDIT;
	}

}
