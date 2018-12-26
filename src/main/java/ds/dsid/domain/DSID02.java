package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "DSID02")
public class DSID02 {
	private String UNIQUEID;
	private String EL_NO;
	private String EL_MODEL;
	private String PART_NAME;
	private String COLOR;
	private Date CREATE_DATE;
	private String CREATE_USER;
	private Date UP_DATE;
	private String STATUS;
    private boolean ISEDIT;
	private boolean ISADD;
	 
	
	 
	
	public String getUNIQUEID() {
		return UNIQUEID;
	}
	public void setUNIQUEID(String uNIQUEID) {
		UNIQUEID = uNIQUEID;
	}
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	public DSID02() {
		ISEDIT = true;
		ISADD = false;
	}
	@Transient
	public boolean isISEDIT() {
		return ISEDIT;
	}
	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}
	@Transient
	public boolean isISADD() {
		return ISADD;
	}
	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}
	@Id
	@Column(name = "EL_NO")
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}

	@Column(name = "EL_MODEL")
	public String getEL_MODEL() {
		return EL_MODEL;
	}
	public void setEL_MODEL(String eL_MODEL) {
		EL_MODEL = eL_MODEL;
	}
	
	@Column(name = "PART_NAME")
	public String getPART_NAME() {
		return PART_NAME;
	}
	public void setPART_NAME(String pART_NAME) {
		PART_NAME = pART_NAME;
	}
	
	@Column(name = "COLOR")
	public String getCOLOR() {
		return COLOR;
	}
	public void setCOLOR(String cOLOR) {
		COLOR = cOLOR;
	}

	@Column(name = "CREATE_DATE")
	public Date getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(Date cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	@Column(name = "CREATE_USER")
	public String getCREATE_USER() {
		return CREATE_USER;
	}
	public void setCREATE_USER(String cREATE_USER) {
		CREATE_USER = cREATE_USER;
	}

	@Column(name = "STATUS")
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
}
