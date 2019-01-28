package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import ds.dsid.domain.pk.DSID04_NFPSIZEPk;

@IdClass(DSID04_NFPSIZEPk.class)
@Entity
@Table(name = "DSID04_NFPSIZE")
public class DSID04_NFPSIZE {
	private String MODEL_NA;
	private String SEQ;
	private String EL_NO;
	private String SIZES;
	private String UP_USER;
	private Date UP_DATE;
	
	 
	/* MODEL_NA */
	@Id
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String MODEL_NA) {
		this.MODEL_NA = MODEL_NA;
	}
	/* COLOR */
	@Id
	public String getSEQ() {
		return SEQ;
	}
	public void setSEQ(String SEQ) {
		this.SEQ = SEQ;
	}
	/* EL_NO */
	@Id
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String EL_NO) {
		this.EL_NO = EL_NO;
	}
	/* SIZES */
	@Id
	public String getSIZES() {
		return SIZES;
	}
	public void setSIZES(String SIZES) {
		this.SIZES = SIZES;
	}
	/* UP_USER */
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String UP_USER) {
		this.UP_USER = UP_USER;
	}
	/* UP_DATE */
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date UP_DATE) {
		this.UP_DATE = UP_DATE;
	}
}
