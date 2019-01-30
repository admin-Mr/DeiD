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
@Table(name = "DSID13")
public class DSID13 {

	private String PG_NAME ;
	private String PG_TYPE ;
	private String NIKE_SH_ARITCLE ;
	private String GROUP_NOS ;
	private String UP_USER ;
	private Date UP_DATE ;
	
	@Id
	@Column(name = "PG_NAME")
	public String getPG_NAME() {
		return PG_NAME;
	}
	public void setPG_NAME(String pG_NAME) {
		PG_NAME = pG_NAME;
	}

	@Column(name = "PG_TYPE")
	public String getPG_TYPE() {
		return PG_TYPE;
	}
	public void setPG_TYPE(String pG_TYPE) {
		PG_TYPE = pG_TYPE;
	}

	@Column(name = "NIKE_SH_ARITCLE")
	public String getNIKE_SH_ARITCLE() {
		return NIKE_SH_ARITCLE;
	}
	public void setNIKE_SH_ARITCLE(String nIKE_SH_ARITCLE) {
		NIKE_SH_ARITCLE = nIKE_SH_ARITCLE;
	}

	@Column(name = "GROUP_NOS")
	public String getGROUP_NOS() {
		return GROUP_NOS;
	}
	public void setGROUP_NOS(String gROUP_NOS) {
		GROUP_NOS = gROUP_NOS;
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
		
	
}
