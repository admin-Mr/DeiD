package ds.dspb.domain;

import java.lang.String;
import java.util.Date;

public class DSPB01_GRPVO {	
    private String PB_GROUPID;
	private String PB_ID;
	private String PB_LANGTAG;
    private String PB_RH01;
    private String PB_RH02;
    private String PB_RH03;
    private String PB_RH04;
    private String PB_RH08;
    private String PB_RH09;
    private String PB_RH10;
    private String UP_USER;
    private Date UP_DATE;
	private boolean ISADD;
	private boolean ISEDIT;
    
	public DSPB01_GRPVO(){
		PB_GROUPID = "";
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
	
    public String getPB_GROUPID() {
        return PB_GROUPID;
    }

    public void setPB_GROUPID(String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
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

    public void setPB_RH01(String PB_RH01) {
        this.PB_RH01 = PB_RH01;
    }

    public String getPB_RH02() {
        return PB_RH02;
    }

    public void setPB_RH02(String PB_RH02) {
        this.PB_RH02 = PB_RH02;
    }

    public String getPB_RH03() {
        return PB_RH03;
    }

    public void setPB_RH03(String PB_RH03) {
        this.PB_RH03 = PB_RH03;
    }

    public String getPB_RH04() {
        return PB_RH04;
    }

    public void setPB_RH04(String PB_RH04) {
        this.PB_RH04 = PB_RH04;
    }

    public String getPB_RH08() {
        return PB_RH08;
    }

    public void setPB_RH08(String PB_RH08) {
        this.PB_RH08 = PB_RH08;
    }    

    public String getPB_RH09() {
        return PB_RH09;
    }

    public void setPB_RH09(String PB_RH09) {
        this.PB_RH09 = PB_RH09;
    }

    public String getPB_RH10() {
        return PB_RH10;
    }

    public void setPB_RH10(String PB_RH10) {
        this.PB_RH10 = PB_RH10;
    }

    public String getUP_USER() {
        return UP_USER;
    }

    public void setUP_USER(String UP_USER) {
        this.UP_USER = UP_USER;
    }

    public java.util.Date getUP_DATE() {
        return UP_DATE;
    }

    public void setUP_DATE(java.util.Date UP_DATE) {
        this.UP_DATE = UP_DATE;
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
