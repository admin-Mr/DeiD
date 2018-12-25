package ds.dspb.domain;

import java.lang.String;
import java.util.Date;

public class DSPB03VO {
    private String PB_GROUPID;	// 角色ID	
    private String PB_GROUPNA;	// 角色名稱
    private String PB_SYSID;		// 系統ID
    private String PB_USERID;	    
    private String UP_USER;	
    private Date UP_DATE;
    private boolean ISEDIT;
    private boolean ISADD;
    
    public DSPB03VO(){
    	PB_GROUPID = "";
    	PB_GROUPNA = "";
    	PB_SYSID = "";
    	PB_USERID = "";
    	UP_USER = "";
    	UP_DATE = null;
    	ISEDIT = true;
    	ISADD= false;
    }

    public String getPB_GROUPID() {
        return PB_GROUPID;
    }
    
    public void setPB_GROUPID(String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }

    public String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(String PB_USERID) {
        this.PB_USERID = PB_USERID;
    }    

    public String getPB_GROUPNA() {
        return PB_GROUPNA;
    }

    public void setPB_GROUPNA(String PB_GROUPNA) {
        this.PB_GROUPNA = PB_GROUPNA;
    }

    public String getUP_USER() {
        return UP_USER;
    }

    public void setUP_USER(String UP_USER) {
        this.UP_USER = UP_USER;
    }

    public Date getUP_DATE() {
        return UP_DATE;
    }

    public void setUP_DATE(Date UP_DATE) {
        this.UP_DATE = UP_DATE;
    }
    
    public String getPB_SYSID(){
    	return this.PB_SYSID;
    }
    
    public void setPB_SYSID(String PB_SYSID){
    	this.PB_SYSID = PB_SYSID;
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
