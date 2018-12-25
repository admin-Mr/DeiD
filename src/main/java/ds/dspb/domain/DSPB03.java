package ds.dspb.domain;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
/**
* 角色資料檔
**/
@Entity
@Table(name = "DSPB03")
public class DSPB03 implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private java.lang.String PB_GROUPID;	// 角色ID	
    private java.lang.String PB_GROUPNA;	// 角色名稱
    private java.lang.String PB_USERID;	
    private java.lang.String PB_CNAME;	
    private java.lang.String UP_USER;	
    private java.util.Date UP_DATE;    
    private java.lang.String PB_SYSID;		// 系統ID

    //private Set<DSPB01_GRP> DSPB01_GRP;
    
    private boolean ISADD;
	private boolean ISEDIT;
    private DSPB04_NEW DSPB04_NEW;
    private DSPB47 DSPB47;

    /*
    @OneToMany
    @JoinColumn(name = "PB_GROUPID", referencedColumnName = "PB_GROUPID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Set<DSPB01_GRP> getDSPB01_GRP(){
    	return this.DSPB01_GRP;
    }
    
    public void setDSPB01_GRP(Set<DSPB01_GRP> DSPB01_GRP){
    	this.DSPB01_GRP = DSPB01_GRP;    	
    }
	*/
    @ManyToOne
	@JoinColumns({ 
			@JoinColumn(name = "PB_GROUPID", referencedColumnName = "PB_GROUPID", insertable = false, updatable = false),
			@JoinColumn(name = "PB_USERID", referencedColumnName = "PB_USERID", insertable = false, updatable = false) })
	@NotFound(action = NotFoundAction.IGNORE)
	public DSPB47 getDSPB47() {
		return this.DSPB47;
	}

	public void setDSPB47(DSPB47 DSPB47) {
		this.DSPB47 = DSPB47;
	}
    
    @ManyToOne
    @JoinColumn(name = "PB_SYSID", referencedColumnName = "PB_SYSID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public DSPB04_NEW getDSPB04_NEW(){
    	return this.DSPB04_NEW;
    }
    
    public void setDSPB04_NEW(DSPB04_NEW DSPB04_NEW){
    	this.DSPB04_NEW = DSPB04_NEW;
    }
    
     
    @Id
    @Column(name = "PB_GROUPID")
    public java.lang.String getPB_GROUPID() {
        return PB_GROUPID;
    }
    
    public void setPB_GROUPID(java.lang.String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }
    

    @Column(name = "PB_USERID")
    public java.lang.String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(java.lang.String PB_USERID) {
        this.PB_USERID = PB_USERID;
    }    
    
    @Column(name = "PB_GROUPNA")
    public java.lang.String getPB_GROUPNA() {
        return PB_GROUPNA;
    }

    public void setPB_GROUPNA(java.lang.String PB_GROUPNA) {
        this.PB_GROUPNA = PB_GROUPNA;
    }

    @Column(name = "PB_CNAME")
    public java.lang.String getPB_CNAME() {
        return PB_CNAME;
    }

    public void setPB_CNAME(java.lang.String PB_CNAME) {
        this.PB_CNAME = PB_CNAME;
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
    
    @Column(name = "PB_SYSID")
    public String getPB_SYSID(){
    	return this.PB_SYSID;
    }
    
    public void setPB_SYSID(String PB_SYSID){
    	this.PB_SYSID = PB_SYSID;
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
