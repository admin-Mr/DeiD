package ds.dsid.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ds.dsid.domain.pk.DSID01_TEMP_PK;
import ds.dsid.domain.pk.DSID10_1PK;

@Entity
@Table(name = "DSID10_1")
@IdClass(DSID10_1PK.class)
public class DSID10_1 {

	private java.lang.String NIKE_SH_ARITCLE;
	private java.lang.String SEQ;
	private java.lang.String GROUP_NO;
	private java.lang.String ORI_INFO;
	private java.lang.String TYPE;
	private java.lang.String REP_INFO;
	private java.lang.String SPL_INFO1;
	private java.lang.String SPL_GROUP;
	private java.lang.String SPL_INFO2;
	private java.lang.String UP_USER;
	private java.util.Date  UP_DATE;
	private java.lang.String IS_REP;
	private java.lang.String IS_SPL;
	
	private boolean ISADD;
	private boolean ISEDIT;
	
	@Id
    @Column(name = "NIKE_SH_ARITCLE")
	public java.lang.String getNIKE_SH_ARITCLE() {
		return NIKE_SH_ARITCLE;
	}
	public void setNIKE_SH_ARITCLE(java.lang.String nIKE_SH_ARITCLE) {
		NIKE_SH_ARITCLE = nIKE_SH_ARITCLE;
	}
	
	@Id
    @Column(name = "SEQ")
	public java.lang.String getSEQ() {
		return SEQ;
	}
	public void setSEQ(java.lang.String sEQ) {
		SEQ = sEQ;
	}
	
    @Column(name = "GROUP_NO")
	public java.lang.String getGROUP_NO() {
		return GROUP_NO;
	}
	public void setGROUP_NO(java.lang.String gROUP_NO) {
		GROUP_NO = gROUP_NO;
	}
	
    @Column(name = "ORI_INFO")
	public java.lang.String getORI_INFO() {
		return ORI_INFO;
	}
	public void setORI_INFO(java.lang.String oRI_INFO) {
		ORI_INFO = oRI_INFO;
	}
	
    @Column(name = "TYPE")
	public java.lang.String getTYPE() {
		return TYPE;
	}
	public void setTYPE(java.lang.String tYPE) {
		TYPE = tYPE;
	}
	
    @Column(name = "REP_INFO")
	public java.lang.String getREP_INFO() {
		return REP_INFO;
	}
	public void setREP_INFO(java.lang.String rEP_INFO) {
		REP_INFO = rEP_INFO;
	}
	
    @Column(name = "SPL_INFO1")
	public java.lang.String getSPL_INFO1() {
		return SPL_INFO1;
	}
	public void setSPL_INFO1(java.lang.String sPL_INFO1) {
		SPL_INFO1 = sPL_INFO1;
	}
	
    @Column(name = "SPL_GROUP")
	public java.lang.String getSPL_GROUP() {
		return SPL_GROUP;
	}
	public void setSPL_GROUP(java.lang.String sPL_GROUP) {
		SPL_GROUP = sPL_GROUP;
	}
	
    @Column(name = "SPL_INFO2")
	public java.lang.String getSPL_INFO2() {
		return SPL_INFO2;
	}
	public void setSPL_INFO2(java.lang.String sPL_INFO2) {
		SPL_INFO2 = sPL_INFO2;
	}
	
	@Column(name = "IS_REP")
    public java.lang.String getIS_REP() {
		return IS_REP;
	}
	public void setIS_REP(java.lang.String iS_REP) {
		IS_REP = iS_REP;
	}
	
	@Column(name = "IS_SPL")
	public java.lang.String getIS_SPL() {
		return IS_SPL;
	}
	public void setIS_SPL(java.lang.String iS_SPL) {
		IS_SPL = iS_SPL;
	}
	
	@Column(name = "UP_USER")
    public java.lang.String getUP_USER() {
        return UP_USER;
    }
    public void setUP_USER(java.lang.String UP_USER) {
        this.UP_USER = UP_USER;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name = "UP_DATE")
    public java.util.Date getUP_DATE() {
        return UP_DATE;
    }

    public void setUP_DATE(java.util.Date UP_DATE) {
        this.UP_DATE = UP_DATE;
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
