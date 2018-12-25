package ds.dspb.domain;
import javax.persistence.IdClass;

import ds.dspb.domain.pk.DSPB01Pk;
import ds.dspb.domain.pk.DSPB48Pk;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import java.io.Serializable;

import javax.persistence.Column;
/**
* 角色DashBoard
**/
@IdClass(DSPB48Pk.class)
@Entity
@Table(name = "DSPB48") 
public class DSPB48 implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private java.lang.String PB_USERID;	    // 使用者ID
    private java.lang.String PB_DASHBOARD;	// DASHBOARD
    private java.lang.String PB_THEME;      // ZK主題
    
    private DSPB02 DSPB02;
    private DSPB00_NEW DSPB00_NEW;
    
    private boolean ISADD;
	private boolean ISEDIT;

	
    @ManyToOne
	@JoinColumns({ @JoinColumn(name = "PB_USERID", referencedColumnName = "PB_USERID", insertable = false, updatable = false) })
	@NotFound(action = NotFoundAction.IGNORE)
    public DSPB02 getDSPB02(){
    	return this.DSPB02;
    }
    
    public void setDSPB02(DSPB02 DSPB02){
    	this.DSPB02 = DSPB02;
    }    
    
	@ManyToOne
	@JoinColumn(name = "PB_DASHBOARD", referencedColumnName = "PB_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)  
	public DSPB00_NEW getDSPB00_NEW() {
		return DSPB00_NEW;
	}

	public void setDSPB00_NEW(DSPB00_NEW dSPB00_NEW) {
		DSPB00_NEW = dSPB00_NEW;
	}
   
    @Id
    @Column(name = "PB_USERID") 
    public java.lang.String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(java.lang.String PB_USERID) {
        this.PB_USERID = PB_USERID;
    }
    
    @Column(name = "PB_DASHBOARD")
    public java.lang.String getPB_DASHBOARD() {
        return PB_DASHBOARD;
    }
    
    public void setPB_DASHBOARD(java.lang.String PB_DASHBOARD) {
        this.PB_DASHBOARD = PB_DASHBOARD;
    }

	public java.lang.String getPB_THEME() {
		return PB_THEME;
	}

	public void setPB_THEME(java.lang.String pB_THEME) {
		PB_THEME = pB_THEME;
	}
	
	@Transient
	public boolean getISADD(){
		return this.ISADD;
	}
	
	public void setISADD(boolean ISADD){
		this.ISADD = ISADD;
	}	
	
	@Transient
	public boolean getISEDIT(){
		return this.ISEDIT;
	}
	
	public void setISEDIT(boolean ISEDIT){
		this.ISEDIT = ISEDIT;
	}

}
