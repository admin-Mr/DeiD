package ds.dsid.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ds.dsid.domain.pk.DSID25_LOG_PK;
import ds.dsid.domain.pk.DSID25_PK;

@Entity
@Table(name = "DSID07")
public class DSID07 {

	private String ADH_ELNO ;
	private String ADH_ELNA ;
	private String PRO_TYPE ;
	private String EL_UNIT ;
	private String COLOR ;
	private String MODEL_NA;
	private String RAW_ELNO1 ;
	private String RAW_PRO1 ;
	private String RAW_ELNO2 ;
	private String RAW_PRO2 ;
	private String RAW_ELNO3 ;
	private String RAW_PRO3 ;
	private String UP_USER ;
	private Date UP_DATE ;
	
	@Id
	@Column(name = "ADH_ELNO")
	public String getADH_ELNO() {
		return ADH_ELNO;
	}
	public void setADH_ELNO(String aDH_ELNO) {
		ADH_ELNO = aDH_ELNO;
	}
	
	@Column(name = "ADH_ELNA")
	public String getADH_ELNA() {
		return ADH_ELNA;
	}
	public void setADH_ELNA(String aDH_ELNA) {
		ADH_ELNA = aDH_ELNA;
	}
	
	@Column(name = "PRO_TYPE")
	public String getPRO_TYPE() {
		return PRO_TYPE;
	}
	public void setPRO_TYPE(String pRO_TYPE) {
		PRO_TYPE = pRO_TYPE;
	}
	
	@Column(name = "EL_UNIT")
	public String getEL_UNIT() {
		return EL_UNIT;
	}
	public void setEL_UNIT(String eL_UNIT) {
		EL_UNIT = eL_UNIT;
	}
	
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "RAW_ELNO1")
	public String getRAW_ELNO1() {
		return RAW_ELNO1;
	}
	public void setRAW_ELNO1(String rAW_ELNO1) {
		RAW_ELNO1 = rAW_ELNO1;
	}
	
	@Column(name = "RAW_PRO1")
	public String getRAW_PRO1() {
		return RAW_PRO1;
	}
	public void setRAW_PRO1(String rAW_PRO1) {
		RAW_PRO1 = rAW_PRO1;
	}
	
	@Column(name = "RAW_ELNO2")
	public String getRAW_ELNO2() {
		return RAW_ELNO2;
	}
	public void setRAW_ELNO2(String rAW_ELNO2) {
		RAW_ELNO2 = rAW_ELNO2;
	}
	
	@Column(name = "RAW_PRO2")
	public String getRAW_PRO2() {
		return RAW_PRO2;
	}
	public void setRAW_PRO2(String rAW_PRO2) {
		RAW_PRO2 = rAW_PRO2;
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
	
	@Column(name = "COLOR")
	public String getCOLOR() {
		return COLOR;
	}
	public void setCOLOR(String cOLOR) {
		COLOR = cOLOR;
	}
	
	@Column(name = "RAW_ELNO3")
	public String getRAW_ELNO3() {
		return RAW_ELNO3;
	}
	public void setRAW_ELNO3(String rAW_ELNO3) {
		RAW_ELNO3 = rAW_ELNO3;
	}
	
	@Column(name = "RAW_PRO3")
	public String getRAW_PRO3() {
		return RAW_PRO3;
	}
	public void setRAW_PRO3(String rAW_PRO3) {
		RAW_PRO3 = rAW_PRO3;
	}
	
	
}
