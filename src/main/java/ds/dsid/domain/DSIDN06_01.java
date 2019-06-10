package ds.dsid.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "DSIDN06_01")
public class DSIDN06_01 {
	private String MODEL_NA;
	private String EL_NO;
	private String EL_CNAME;
	private String PLACE;
	private String UP_USER;
	private Date UP_DATE;		//異動日期
	private String CUPBOARD;
	private String NOTE;
	  
	private boolean ISADD;
	private boolean ISEDIT;
	

	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "EL_NO")
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	
	@Column(name = "EL_CNAME")
	public String getEL_CNAME() {
		return EL_CNAME;
	}
	public void setEL_CNAME(String eL_CNAME) {
		EL_CNAME = eL_CNAME;
	}
	
	@Column(name = "PLACE")
	public String getPLACE() {
		return PLACE;
	}
	public void setPLACE(String pLACE) {
		PLACE = pLACE;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	@Column(name = "UP_DATE")
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
	@Column(name = "CUPBOARD")
	public String getCUPBOARD() {
		return CUPBOARD;
	}
	public void setCUPBOARD(String cUPBOARD) {
		CUPBOARD = cUPBOARD;
	}
	
	@Column(name = "NOTE")
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	@Transient 
	public boolean getISEDIT() {
		return ISEDIT;
	}
	 
	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}
	
	@Transient
	public boolean getISADD() {
		return ISADD;
	}

	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}


}
