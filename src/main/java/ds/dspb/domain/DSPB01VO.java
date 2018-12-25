package ds.dspb.domain;

import java.util.Date;
import java.lang.String;
/**
* 帳號權限檔
**/
public class DSPB01VO {
    private String PB_USERID;	// 使用者ID
    private String PB_ID;		// 作業ID
    private String PB_LANGTAG;	// 語系 
    private String PB_RH01;	// 權限-新增
    private String PB_RH02;	// 權限-查詢
    private String PB_RH03;	// 權限-刪除
    private String PB_RH04;	// 權限-修改
    private String PB_RH08;	// 權限-EMail
    private String PB_RH09;	// 權限-列印
    private String PB_RH10;	// 權限-匯出
    private String UP_USER;
    private Date UP_DATE;
	private boolean ISADD;
	private boolean ISEDIT;
    
	public DSPB01VO(){
		PB_USERID = "";
		PB_ID = "";
		PB_LANGTAG = "";
		PB_RH01 = "N";
		PB_RH02 = "N";
		PB_RH03 = "N";
		PB_RH04 = "N";
		PB_RH08 = "N";
		PB_RH09 = "N";
		PB_RH10 = "N";
		UP_USER = "";
		UP_DATE = null;
		ISADD = true;
		ISEDIT = false;
	}	
	
    public String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(java.lang.String PB_USERID) {
        this.PB_USERID = PB_USERID;
    }
    
    public String getPB_ID() {
        return PB_ID;
    }
    
    public void setPB_ID(String PB_ID) {
        this.PB_ID = PB_ID;
    }
        
    public String getPB_LANGTAG() {
        return PB_LANGTAG;
    }
    
    public void setPB_LANGTAG(String PB_LANGTAG) {
        this.PB_LANGTAG = PB_LANGTAG;
    }  
    
    public String getPB_RH01() {
        return PB_RH01;
    }
    
    public void setPB_RH01(java.lang.String PB_RH01) {
        this.PB_RH01 = PB_RH01;
    }

    public String getPB_RH02() {
        return PB_RH02;
    }
    
    public void setPB_RH02(java.lang.String PB_RH02) {
        this.PB_RH02 = PB_RH02;
    }

    public String getPB_RH03() {
        return PB_RH03;
    }
    
    public void setPB_RH03(java.lang.String PB_RH03) {
        this.PB_RH03 = PB_RH03;
    }

    public String getPB_RH04() {
        return PB_RH04;
    }
    
    public void setPB_RH04(java.lang.String PB_RH04) {
        this.PB_RH04 = PB_RH04;
    }

    public java.lang.String getUP_USER() {
        return UP_USER;
    }
    
    public void setUP_USER(java.lang.String UP_USER) {
        this.UP_USER = UP_USER;
    }

    public java.util.Date getUP_DATE() {
        return UP_DATE;
    }
    
    public void setUP_DATE(java.util.Date UP_DATE) {
        this.UP_DATE = UP_DATE;
    }

    public java.lang.String getPB_RH08() {
        return PB_RH08;
    }
    
    public void setPB_RH08(java.lang.String PB_RH08) {
        this.PB_RH08 = PB_RH08;
    }

    public java.lang.String getPB_RH09(){
    	return this.PB_RH09;
    }
    
    public void setPB_RH09(java.lang.String PB_RH09){
    	this.PB_RH09 = PB_RH09;
    }

    public java.lang.String getPB_RH10(){
    	return this.PB_RH10;
    }
    
    public void setPB_RH10(java.lang.String PB_RH10){
    	this.PB_RH10 = PB_RH10;
    }
	
	public boolean getISEDIT(){
		return this.ISEDIT;
	}
	
	public void setISEDIT(boolean ISEDIT){
		this.ISEDIT = ISEDIT;
	}
	
	public boolean getISADD(){
		return this.ISADD;
	}
	
	public void setISADD(boolean ISADD){
		this.ISADD = ISADD;
	}	    
}
