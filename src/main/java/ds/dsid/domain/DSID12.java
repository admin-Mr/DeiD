package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ds.dsid.domain.pk.DSID10_1PK;
import ds.dsid.domain.pk.DSID12_PK;

import javax.persistence.Column;

@Entity
@Table(name = "DSID12")
@IdClass(DSID12_PK.class)
public class DSID12 {
	  private String MODEL_NA;
	  private String  SH_STYLENO;
	  private String  SH_LAST;
	  private String  SH_TYPE;
	  private String  SH_COLOR;
	  private String  SH_REMARK;
	  private String  UPPER_CLASS;
	  private String  SOLE_CLASS;
	  private String  UP_USER;
	  private Date  UP_DATE;
	  
	private boolean ISADD;
	private boolean ISEDIT;
	@Id
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	@Id
	@Column(name = "SH_STYLENO")
	public String getSH_STYLENO() {
		return SH_STYLENO;
	}
	public void setSH_STYLENO(String sH_STYLENO) {
		SH_STYLENO = sH_STYLENO;
	}
	@Column(name = "SH_LAST")
	public String getSH_LAST() {
		return SH_LAST;
	}
	public void setSH_LAST(String sH_LAST) {
		SH_LAST = sH_LAST;
	}
	@Column(name = "SH_TYPE")
	public String getSH_TYPE() {
		return SH_TYPE;
	}
	public void setSH_TYPE(String sH_TYPE) {
		SH_TYPE = sH_TYPE;
	}
	@Column(name = "SH_COLOR")
	public String getSH_COLOR() {
		return SH_COLOR;
	}
	public void setSH_COLOR(String sH_COLOR) {
		SH_COLOR = sH_COLOR;
	}
	@Column(name = "SH_REMARK")
	public String getSH_REMARK() {
		return SH_REMARK;
	}
	public void setSH_REMARK(String sH_REMARK) {
		SH_REMARK = sH_REMARK;
	}
	@Column(name = "UPPER_CLASS")
	public String getUPPER_CLASS() {
		return UPPER_CLASS;
	}
	public void setUPPER_CLASS(String uPPER_CLASS) {
		UPPER_CLASS = uPPER_CLASS;
	}
	@Column(name = "SOLE_CLASS")
	public String getSOLE_CLASS() {
		return SOLE_CLASS;
	}
	public void setSOLE_CLASS(String sOLE_CLASS) {
		SOLE_CLASS = sOLE_CLASS;
	}
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
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
