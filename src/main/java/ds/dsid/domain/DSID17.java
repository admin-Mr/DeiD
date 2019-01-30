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
@Table(name = "DSID17")
public class DSID17 {

	private String ADH_ELNO ;
	private String ADH_ELNA ;
	private String PRO_TYPE ;
	private String EL_UNIT ;
	private String COLOR ;
	private String ADH_QTY ;
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
	
	@Column(name = "ADH_QTY")	
	public String getADH_QTY() {
		return ADH_QTY;
	}
	public void setADH_QTY(String aDH_QTY) {
		ADH_QTY = aDH_QTY;
	}
	
	@Column(name = "COLOR")
	public String getCOLOR() {
		return COLOR;
	}
	public void setCOLOR(String cOLOR) {
		COLOR = cOLOR;
	}
	
	
}
