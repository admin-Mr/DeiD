package ds.dspb.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

public class CATEGORY implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String CA_ID; // 類別編號
	private java.lang.String CA_NAME; // 類別名稱

	private boolean ISADD;
	private boolean ISEDIT;

	public java.lang.String getCA_ID() {
		return CA_ID;
	}

	public void setCA_ID(java.lang.String cA_ID) {
		CA_ID = cA_ID;
	}

	public java.lang.String getCA_NAME() {
		return CA_NAME;
	}

	public void setCA_NAME(java.lang.String cA_NAME) {
		CA_NAME = cA_NAME;
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
