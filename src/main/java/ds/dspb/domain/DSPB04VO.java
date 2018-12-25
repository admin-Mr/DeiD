package ds.dspb.domain;


import java.lang.String;
import java.util.Date;


public class DSPB04VO {
    private String PB_GROUPID;
    private String PB_GROUPNA;
    private String PB_USERID;
    private String UP_USER;
    private Date UP_DATE;
	private boolean ISEDIT;
    
	public DSPB04VO(){
		PB_GROUPID = "";
		PB_GROUPNA = "";
		PB_USERID = "";
		UP_USER = "";
		UP_DATE = null;
		ISEDIT = true;
	}
	
    public String getPB_GROUPID() {
        return PB_GROUPID;
    }
    
    public void setPB_GROUPID(String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }
    	
    public String getPB_GROUPNA() {
        return PB_GROUPNA;
    }
    
    public void setPB_GROUPNA(String PB_GROUPNA) {
        this.PB_GROUPNA = PB_GROUPNA;
    }    

    public String getPB_USERID() {
        return PB_USERID;
    }
    
    public void setPB_USERID(String PB_USERID) {
        this.PB_USERID = PB_USERID;
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
	
	public boolean getISEDIT(){
		return this.ISEDIT;
	}
	
	public void setISEDIT(boolean ISEDIT){
		this.ISEDIT = ISEDIT;
	}    
}
