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
@Table(name = "DSID25_LOG")
@IdClass(DSID25_LOG_PK.class)
public class DSID25_LOG {

	private String MODEL_NA ;
	private String EL_NO ;
	private String GROUP_NO ;
	private String REMAIN ;
	private Date RE_DATE ;
	private String UP_USER ;
	private Date UP_DATE ;
	
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
	@Column(name = "GROUP_NO")
	public String getGROUP_NO() {
		return GROUP_NO;
	}
	public void setGROUP_NO(String gROUP_NO) {
		GROUP_NO = gROUP_NO;
	}
	@Id
	@Column(name = "EL_NO")
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	
	@Column(name = "REMAIN")
	public String getREMAIN() {
		return REMAIN;
	}
	public void setREMAIN(String rEMAIN) {
		REMAIN = rEMAIN;
	}
	
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "RE_DATE")
	public Date getRE_DATE() {
		return RE_DATE;
	}
	public void setRE_DATE(Date rE_DATE) {
		RE_DATE = rE_DATE;
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
