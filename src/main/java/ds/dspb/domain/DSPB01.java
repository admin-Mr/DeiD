package ds.dspb.domain;
import javax.persistence.IdClass;

import ds.dspb.domain.pk.DSPB01Pk;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
* 帳號權限檔
**/
@IdClass(DSPB01Pk.class)
@Entity
@Table(name = "DSPB01")
public class DSPB01 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String PB_USERID;	// 使用者ID
    private java.lang.String PB_ID;		// 作業ID
    private java.lang.String PB_PRG;	// 程式名稱
    private java.lang.String PB_RH01;	// 權限-新增
    private java.lang.String PB_RH02;	// 權限-查詢
    private java.lang.String PB_RH03;	// 權限-刪除
    private java.lang.String PB_RH04;	// 權限-修改
    private java.lang.String PB_RH05;	// 權限-確認
    private java.lang.String PB_RH06;	// 權限-取消確認
    private java.lang.String PB_RH07;	// 權限-成本
    private java.lang.String UP_USER;
    private java.util.Date UP_DATE;
    private java.lang.String PB_RH08;	// 權限-EMail
    private java.lang.String PB_RH09;	// 權限-列印
    private java.lang.String PB_RH10;	// 權限-匯出
    private DSPB00_NEW DSPB00_NEW;
    private DSPB02 DSPB02;
    private DSPB00 PB_ID_Object;
    private boolean ISADD;
	private boolean ISEDIT;
	
    @ManyToOne
    @JoinColumn(name = "PB_ID", referencedColumnName = "PB_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public DSPB00_NEW getDSPB00_NEW(){
    	return this.DSPB00_NEW;
    }
    
    public void setDSPB00_NEW(DSPB00_NEW DSPB00_NEW){
    	this.DSPB00_NEW = DSPB00_NEW;
    }

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
    @JoinColumn(name="PB_ID", referencedColumnName="PB_ID", insertable=false, updatable=false)
    @NotFound(action=NotFoundAction.IGNORE)
    public DSPB00 getPB_ID_Object() {
        return PB_ID_Object;
    }
    
    public void setPB_ID_Object(DSPB00 PB_ID_Object) {
        this.PB_ID_Object = PB_ID_Object;
    }    
    
    @Id
    @Column(name = "PB_USERID")
    public java.lang.String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(java.lang.String PB_USERID) {
        this.PB_USERID = PB_USERID;
    }
    
    @Id
    @Column(name = "PB_ID")
    public java.lang.String getPB_ID() {
        return PB_ID;
    }
    
    public void setPB_ID(java.lang.String PB_ID) {
        this.PB_ID = PB_ID;
    }
    
    @Column(name = "PB_PRG")
    public java.lang.String getPB_PRG() {
        return PB_PRG;
    }
    
    public void setPB_PRG(java.lang.String PB_PRG) {
        this.PB_PRG = PB_PRG;
    }
    
    @Column(name = "PB_RH01")
    public java.lang.String getPB_RH01() {
        return PB_RH01;
    }
    
    public void setPB_RH01(java.lang.String PB_RH01) {
        this.PB_RH01 = PB_RH01;
    }
    
    @Column(name = "PB_RH02")
    public java.lang.String getPB_RH02() {
        return PB_RH02;
    }
    
    public void setPB_RH02(java.lang.String PB_RH02) {
        this.PB_RH02 = PB_RH02;
    }
    
    @Column(name = "PB_RH03")
    public java.lang.String getPB_RH03() {
        return PB_RH03;
    }
    
    public void setPB_RH03(java.lang.String PB_RH03) {
        this.PB_RH03 = PB_RH03;
    }
    
    @Column(name = "PB_RH04")
    public java.lang.String getPB_RH04() {
        return PB_RH04;
    }
    
    public void setPB_RH04(java.lang.String PB_RH04) {
        this.PB_RH04 = PB_RH04;
    }
    
    @Column(name = "PB_RH05")
    public java.lang.String getPB_RH05() {
        return PB_RH05;
    }
    
    public void setPB_RH05(java.lang.String PB_RH05) {
        this.PB_RH05 = PB_RH05;
    }
    
    @Column(name = "PB_RH06")
    public java.lang.String getPB_RH06() {
        return PB_RH06;
    }
    
    public void setPB_RH06(java.lang.String PB_RH06) {
        this.PB_RH06 = PB_RH06;
    }
    
    @Column(name = "PB_RH07")
    public java.lang.String getPB_RH07() {
        return PB_RH07;
    }
    
    public void setPB_RH07(java.lang.String PB_RH07) {
        this.PB_RH07 = PB_RH07;
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
    
    @Column(name = "PB_RH08")
    public java.lang.String getPB_RH08() {
        return PB_RH08;
    }
    
    public void setPB_RH08(java.lang.String PB_RH08) {
        this.PB_RH08 = PB_RH08;
    }
    
    @Column(name = "PB_RH09")
    public java.lang.String getPB_RH09(){
    	return this.PB_RH09;
    }
    
    public void setPB_RH09(java.lang.String PB_RH09){
    	this.PB_RH09 = PB_RH09;
    }
    
    @Column(name = "PB_RH10")
    public java.lang.String getPB_RH10(){
    	return this.PB_RH10;
    }
    
    public void setPB_RH10(java.lang.String PB_RH10){
    	this.PB_RH10 = PB_RH10;
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
