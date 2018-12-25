package ds.dspb.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import ds.dspb.domain.pk.DSPB01_GRPPk;
import java.io.Serializable;
import javax.persistence.Column;
import ds.dspb.domain.DSPB03;

@IdClass(DSPB01_GRPPk.class)
@Entity
@Table(name = "DSPB01_GRP")
public class DSPB01_GRP implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private java.lang.String PB_ID;
    private java.lang.String PB_GROUPID;
    private java.lang.String PB_RH01;
    private java.lang.String PB_RH02;
    private java.lang.String PB_RH03;
    private java.lang.String PB_RH04;
    private java.lang.String PB_RH08;
    private java.lang.String PB_RH09;
    private java.lang.String PB_RH10;
    private java.lang.String UP_USER;
    private java.util.Date UP_DATE;
	private boolean ISADD;
	private boolean ISEDIT;
	
    private DSPB00_NEW DSPB00_NEW;
    private DSPB03 DSPB03;
    
    @ManyToOne
    @JoinColumn(name="PB_ID", referencedColumnName="PB_ID", insertable = false, updatable= false)
    @NotFound(action = NotFoundAction.IGNORE)
    public DSPB00_NEW getDSPB00_NEW(){
    	return this.DSPB00_NEW;
    }
    
    public void setDSPB00_NEW(DSPB00_NEW DSPB00_NEW){
    	this.DSPB00_NEW = DSPB00_NEW;
    }
   
    @ManyToOne
    @JoinColumn(name="PB_GROUPID", referencedColumnName="PB_GROUPID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public DSPB03 getDSPB03(){
    	return this.DSPB03;
    }
    
    public void setDSPB03(DSPB03 DSPB03){
    	this.DSPB03 = DSPB03;
    }
 
    /**
     * @return PB_ID 
     */
    @Id
    @Column(name = "PB_ID")
    public java.lang.String getPB_ID() {
        return PB_ID;
    }
    /**
     * @param PB_ID 
     */
    public void setPB_ID(java.lang.String PB_ID) {
        this.PB_ID = PB_ID;
    }
    /**
     * @return PB_GROUPID 
     */
    @Id
    @Column(name = "PB_GROUPID")
    public java.lang.String getPB_GROUPID() {
        return PB_GROUPID;
    }
    /**
     * @param PB_GROUPID 
     */
    public void setPB_GROUPID(java.lang.String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }
    /**
     * @return PB_RH01 
     */
    @Column(name = "PB_RH01")
    public java.lang.String getPB_RH01() {
        return PB_RH01;
    }
    /**
     * @param PB_RH01 
     */
    public void setPB_RH01(java.lang.String PB_RH01) {
        this.PB_RH01 = PB_RH01;
    }
    /**
     * @return PB_RH02 
     */
    @Column(name = "PB_RH02")
    public java.lang.String getPB_RH02() {
        return PB_RH02;
    }
    /**
     * @param PB_RH02 
     */
    public void setPB_RH02(java.lang.String PB_RH02) {
        this.PB_RH02 = PB_RH02;
    }
    /**
     * @return PB_RH03 
     */
    @Column(name = "PB_RH03")
    public java.lang.String getPB_RH03() {
        return PB_RH03;
    }
    /**
     * @param PB_RH03 
     */
    public void setPB_RH03(java.lang.String PB_RH03) {
        this.PB_RH03 = PB_RH03;
    }
    /**
     * @return PB_RH04 
     */
    @Column(name = "PB_RH04")
    public java.lang.String getPB_RH04() {
        return PB_RH04;
    }
    /**
     * @param PB_RH04 
     */
    public void setPB_RH04(java.lang.String PB_RH04) {
        this.PB_RH04 = PB_RH04;
    }
    /**
     * @return PB_RH08 
     */
    @Column(name = "PB_RH08")
    public java.lang.String getPB_RH08() {
        return PB_RH08;
    }
    /**
     * @param PB_RH08 
     */
    public void setPB_RH08(java.lang.String PB_RH08) {
        this.PB_RH08 = PB_RH08;
    }    
    /**
     * @return PB_RH09 
     */
    @Column(name = "PB_RH09")
    public java.lang.String getPB_RH09() {
        return PB_RH09;
    }
    /**
     * @param PB_RH09 
     */
    public void setPB_RH09(java.lang.String PB_RH09) {
        this.PB_RH09 = PB_RH09;
    }
    /**
     * @return PB_RH10 
     */
    @Column(name = "PB_RH10")
    public java.lang.String getPB_RH10() {
        return PB_RH10;
    }
    /**
     * @param PB_RH10 
     */
    public void setPB_RH10(java.lang.String PB_RH10) {
        this.PB_RH10 = PB_RH10;
    }
    /**
     * @return UP_USER 
     */
    @Column(name = "UP_USER")
    public java.lang.String getUP_USER() {
        return UP_USER;
    }
    /**
     * @param UP_USER 
     */
    public void setUP_USER(java.lang.String UP_USER) {
        this.UP_USER = UP_USER;
    }
    /**
     * @return UP_DATE 
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "UP_DATE")
    public java.util.Date getUP_DATE() {
        return UP_DATE;
    }
    /**
     * @param UP_DATE 
     */
    public void setUP_DATE(java.util.Date UP_DATE) {
        this.UP_DATE = UP_DATE;
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
