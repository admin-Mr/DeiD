package ds.dspb.domain;

import java.lang.String;
import java.util.Date;
import javax.persistence.Column;

public class DSPB02VO {
	private String PB_EMUSER;
	private String PB_NAME;
	private String PB_CNAME;
	private String PB_EMAIL;
	private String PB_USERID;
	private String PB_PASS;
	private String PB_PASSENCRY;
	private String PB_LOCK;
	private String UP_USER;
	private Date UP_DATE;
	private boolean ISADD;
	private boolean ISEDIT;
	
	public DSPB02VO(){
		PB_EMUSER = "";
		PB_NAME = "";
		PB_CNAME = "";
		PB_EMAIL = "";
		PB_USERID = "";
		PB_PASS = "";
		PB_PASSENCRY = "";
		PB_LOCK = "";
		UP_USER = "";
		UP_DATE = null;
		ISEDIT = false;
		ISADD = true;
	}
	
	public String getPB_USERID() {
		return PB_USERID;
	}
	
	public void setPB_USERID(String PB_USERID) {
		this.PB_USERID = PB_USERID;
	}	

	public String getPB_EMAIL() {
		return PB_EMAIL;
	}

	public void setPB_EMAIL(String PB_EMAIL) {
		this.PB_EMAIL = PB_EMAIL;
	}		

	public java.lang.String getPB_EMUSER() {
		return PB_EMUSER;
	}	

	public void setPB_EMUSER(String PB_EMUSER) {
		this.PB_EMUSER = PB_EMUSER;
	}	

	public String getPB_NAME() {
		return PB_NAME;
	}
	
	public void setPB_NAME(String PB_NAME) {
		this.PB_NAME = PB_NAME;
	}
	
	public String getPB_CNAME() {
		return PB_CNAME;
	}
	
	public void setPB_CNAME(String PB_CNAME) {
		this.PB_CNAME = PB_CNAME;
	}		

	public String getPB_PASS() {
		return PB_PASS;
	}
	
	public void setPB_PASS(String PB_PASS) {
		this.PB_PASS = PB_PASS;
	}	

	public Date getUP_DATE() {
		return UP_DATE;
	}
		
	public void setUP_DATE(Date UP_DATE) {
		this.UP_DATE = UP_DATE;
	}	
	
	public java.lang.String getUP_USER() {
		return UP_USER;
	}
	
	public void setUP_USER(String UP_USER) {
		this.UP_USER = UP_USER;
	}

    public String getPB_PASSENCRY() {
        return PB_PASSENCRY;
    }
    public void setPB_PASSENCRY(String PB_PASSENCRY) {
        this.PB_PASSENCRY = PB_PASSENCRY;
    }

    public String getPB_LOCK() {
        return PB_LOCK;
    }
    public void setPB_LOCK(String PB_LOCK) {
        this.PB_LOCK = PB_LOCK;
    }
	
	public boolean getISEDIT() {
		return ISEDIT;
	}

	public void setISEDIT(boolean ISEDIT) {
		this.ISEDIT = ISEDIT;
	}
	
	public boolean getISADD() {
		return ISADD;
	}

	public void setISADD(boolean ISADD) {
		this.ISADD = ISADD;
	}		
}
