package ds.dspb.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "DSPB02")
public class DSPB02 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String PB_CNAME;
	private java.lang.String PB_DPNO;
	private java.lang.String PB_EMAIL;
	private java.lang.String PB_EMPASSWD;
	private java.lang.String PB_EMUSER;
	private java.lang.String PB_FANO;
	private java.lang.String PB_NAME;
	private java.lang.String PB_PASS;
	private java.lang.String PB_USERID;
	private java.util.Date UP_DATE;
	private java.lang.String UP_USER;
	private java.lang.String PB_PASSENCRY;
	private java.lang.String PB_LOCK;
	private java.lang.String PB_CHANGEYN;
	private java.util.Date PB_VALIDDATE;
	private java.lang.String PB_USERNO;
	//private java.lang.String PB_CODE;
	
	private boolean ISADD;
	private boolean ISEDIT;
	private Set<DSPB01> DSPB01;
	private Set<DSPB04> DSPB04;
    private DSPB48 DSPB48;
	
	@OneToMany
	@JoinColumn(name = "PB_USERID", referencedColumnName = "PB_USERID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<DSPB01> getDSPB01(){
		return this.DSPB01;
	}
	public void setDSPB01(Set<DSPB01> DSPB01){
		this.DSPB01 = DSPB01;
	}
	
    @ManyToOne
	@JoinColumn(name = "PB_USERID", referencedColumnName = "PB_USERID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public DSPB48 getDSPB48() {
		return this.DSPB48;
	}

	public void setDSPB48(DSPB48 DSPB48) {
		this.DSPB48 = DSPB48;
	}
	
	@OneToMany
	@JoinColumn(name = "PB_USERID", referencedColumnName = "PB_USERID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<DSPB04> getDSPB04(){
		return this.DSPB04;
	}
	
	public void setDSPB04(Set<DSPB04> DSPB04){
		this.DSPB04 = DSPB04;
	}
		
	@Id
	@Column(name = "PB_USERID")
	public java.lang.String getPB_USERID() {
		return PB_USERID;
	}
	
	public void setPB_USERID(java.lang.String PB_USERID) {
		this.PB_USERID = PB_USERID;
	}	
	
	@Column(name = "PB_CNAME")
	public java.lang.String getPB_CNAME() {
		return PB_CNAME;
	}

	public void setPB_CNAME(java.lang.String PB_CNAME) {
		this.PB_CNAME = PB_CNAME;
	}	
	
	@Column(name = "PB_DPNO")
	public java.lang.String getPB_DPNO() {
		return PB_DPNO;
	}
	
	public void setPB_DPNO(java.lang.String PB_DPNO) {
		this.PB_DPNO = PB_DPNO;
	}	

	@Column(name = "PB_EMAIL")
	public java.lang.String getPB_EMAIL() {
		return PB_EMAIL;
	}

	public void setPB_EMAIL(java.lang.String PB_EMAIL) {
		this.PB_EMAIL = PB_EMAIL;
	}		

	@Column(name = "PB_EMPASSWD")
	public java.lang.String getPB_EMPASSWD() {
		return PB_EMPASSWD;
	}

	public void setPB_EMPASSWD(java.lang.String PB_EMPASSWD) {
		this.PB_EMPASSWD = PB_EMPASSWD;
	}	
	
	@Column(name = "PB_EMUSER")
	public java.lang.String getPB_EMUSER() {
		return PB_EMUSER;
	}	

	public void setPB_EMUSER(java.lang.String PB_EMUSER) {
		this.PB_EMUSER = PB_EMUSER;
	}	

	@Column(name = "PB_FANO")
	public java.lang.String getPB_FANO() {
		return PB_FANO;
	}	

	public void setPB_FANO(java.lang.String PB_FANO) {
		this.PB_FANO = PB_FANO;
	}	

	@Column(name = "PB_NAME")
	public java.lang.String getPB_NAME() {
		return PB_NAME;
	}
	
	public void setPB_NAME(java.lang.String PB_NAME) {
		this.PB_NAME = PB_NAME;
	}	

	@Column(name = "PB_PASS")
	public java.lang.String getPB_PASS() {
		return PB_PASS;
	}
	
	public void setPB_PASS(java.lang.String PB_PASS) {
		this.PB_PASS = PB_PASS;
	}	

	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	public java.util.Date getUP_DATE() {
		return UP_DATE;
	}
		
	public void setUP_DATE(java.util.Date UP_DATE) {
		this.UP_DATE = UP_DATE;
	}	
	
	@Column(name = "UP_USER")
	public java.lang.String getUP_USER() {
		return UP_USER;
	}
	
	public void setUP_USER(java.lang.String UP_USER) {
		this.UP_USER = UP_USER;
	}
		
    @Column(name = "PB_PASSENCRY")
    public java.lang.String getPB_PASSENCRY() {
        return PB_PASSENCRY;
    }
    public void setPB_PASSENCRY(java.lang.String PB_PASSENCRY) {
        this.PB_PASSENCRY = PB_PASSENCRY;
    }
    
    @Column(name = "PB_LOCK")
    public java.lang.String getPB_LOCK() {
        return PB_LOCK;
    }
    public void setPB_LOCK(java.lang.String PB_LOCK) {
        this.PB_LOCK = PB_LOCK;
    }
    
    @Column(name = "PB_CHANGEYN")
    public java.lang.String getPB_CHANGEYN() {
        return PB_CHANGEYN;
    }
    public void setPB_CHANGEYN(java.lang.String PB_CHANGEYN) {
        this.PB_CHANGEYN = PB_CHANGEYN;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name = "PB_VALIDDATE")
    public java.util.Date getPB_VALIDDATE() {
        return PB_VALIDDATE;
    }
    public void setPB_VALIDDATE(java.util.Date PB_VALIDDATE) {
        this.PB_VALIDDATE = PB_VALIDDATE;
    }
    
    @Column(name = "PB_USERNO")
    public java.lang.String getPB_USERNO() {
        return PB_USERNO;
    }
    public void setPB_USERNO(java.lang.String PB_USERNO) {
        this.PB_USERNO = PB_USERNO;
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
    /*
    @Column(name = "PB_CODE")
    public java.lang.String getPB_CODE(){
    	return this.PB_CODE;
    }
    
    public void setPB_CODE(java.lang.String PB_CODE){
    	this.PB_CODE = PB_CODE;
    }
    */
}
