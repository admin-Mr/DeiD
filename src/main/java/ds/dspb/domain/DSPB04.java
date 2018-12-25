package ds.dspb.domain;

import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import ds.dspb.domain.pk.DSPB04Pk;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import java.io.Serializable;
import javax.persistence.Column;

@IdClass(DSPB04Pk.class)
@Entity
@Table(name = "DSPB04")
public class DSPB04 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String PB_GROUPID;	// 群組代號
    private java.lang.String PB_USERID;		// 使用者代號
    private java.lang.String UP_USER;
    private java.util.Date UP_DATE;
    
    private boolean ISADD;
    private boolean ISEDIT;
    private DSPB02 DSPB02;
    private DSPB03 DSPB03;
    
    @ManyToOne
    @JoinColumn(name = "PB_USERID", referencedColumnName = "PB_USERID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public DSPB02 getDSPB02(){
    	return this.DSPB02;
    }
    
    public void setDSPB02(DSPB02 DSPB02){
    	this.DSPB02 = DSPB02;
    }
    
    @ManyToOne
    @JoinColumn(name = "PB_GROUPID", referencedColumnName = "PB_GROUPID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public DSPB03 getDSPB03(){
    	return this.DSPB03;
    }
        
    public void setDSPB03(DSPB03 DSPB03){
    	this.DSPB03 = DSPB03;
    }
    
    @Id
    @Column(name = "PB_GROUPID")
    public java.lang.String getPB_GROUPID() {
        return PB_GROUPID;
    }
    
    public void setPB_GROUPID(java.lang.String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }
    
    @Id
    @Column(name = "PB_USERID")
    public java.lang.String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(java.lang.String PB_USERID) {
        this.PB_USERID = PB_USERID;
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
	public boolean isISADD() {
		return ISADD;
	}

	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}

	@Transient
	public boolean isISEDIT() {
		return ISEDIT;
	}

	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}

}
