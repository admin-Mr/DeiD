package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import ds.dsid.domain.pk.DSID06N_PK;

@Entity
@IdClass(DSID06N_PK.class)
@Table(name = "DSID06N")
public class DSID06N {
	private String MODEL_NA;
	private String ITEM;
	private String EL_NO;
	private String EL_CNAME;
	private String PLACE;
	private String CUPBOARD;
	private String NOTE;
	private String UP_USER;
//	private Date UP_DATE;		//異動日期
	
	
	@Id
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Id
	@Column(name = "ITEM")
	public String getITEM() {
		return ITEM;
	}
	public void setITEM(String iTEM) {
		ITEM = iTEM;
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
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	
}
