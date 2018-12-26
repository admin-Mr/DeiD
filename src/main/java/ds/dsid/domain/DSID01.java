package ds.dsid.domain;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Column;
/**
* 系統主檔
**/
@Entity
@Table(name = "DSID01")
public class DSID01 {
    private java.lang.String OD_NO;	
    private java.util.Date ORDER_DATE;	
    private java.lang.String TYPE;	
    private java.lang.String STATUS;	
    private java.lang.String SHIP_GROUP_ID;	
    private java.lang.String WORK_ORDER_ID;	
    private java.lang.String ORDER_ID;	
    private java.lang.String SH_LAST;	
    private java.lang.String SH_STYLENO;	
    private java.lang.String ORDER_NUM;	
    private java.lang.String LEFT_SIZE_RUN;	
    private java.lang.String RIGHT_SIZE_RUN;	
    private java.lang.String TOOLING_SIZE;	
    private java.lang.String PID01;	
    private java.lang.String PID02;	
    private java.lang.String PID03;	
    private java.lang.String PID04;	
    private java.lang.String GROUP1;	
    private java.lang.String GROUP2;
	private java.lang.String GROUP3;	
    private java.lang.String GROUP4;	
    private java.lang.String GROUP5;	
    private java.lang.String GROUP6;	
    private java.lang.String GROUP7;	
    private java.lang.String GROUP8;	
    private java.lang.String GROUP9;
    private java.lang.String GROUP10;	
    private java.lang.String GROUP11;	
    private java.lang.String GROUP12;	
    private java.lang.String NIKE_SH_ARITCLE;	
    private java.lang.String MODEL_NA;
    private java.util.Date PG_DATE;	
    private java.lang.String ROUND;	
    private java.util.Date SHIPING_DATE;  
    private java.lang.String REGION;
    
    private java.lang.String UP_USER;	//異動人員
    private java.util.Date UP_DATE;	//異動日期
    
	private boolean ISADD;
	private boolean ISEDIT;

    @Id
    @Column(name = "OD_NO")
    public java.lang.String getOD_NO() {
        return OD_NO;
    }
    public void setOD_NO(java.lang.String OD_NO) {
        this.OD_NO = OD_NO;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name = "ORDER_DATE")
    public java.util.Date getORDER_DATE() {
        return ORDER_DATE;
    }

    public void setORDER_DATE(java.util.Date ORDER_DATE) {
        this.ORDER_DATE = ORDER_DATE;
    }


    @Column(name = "TYPE")
    public java.lang.String getTYPE() {
        return TYPE;
    }

    public void setTYPE(java.lang.String TYPE) {
        this.TYPE = TYPE;
    }
    
    @Column(name = "STATUS")
    public java.lang.String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(java.lang.String STATUS) {
        this.STATUS = STATUS;
    }
    
    @Column(name = "SHIP_GROUP_ID")
    public java.lang.String getSHIP_GROUP_ID() {
        return SHIP_GROUP_ID;
    }
    public void setSHIP_GROUP_ID(java.lang.String SHIP_GROUP_ID) {
        this.SHIP_GROUP_ID = SHIP_GROUP_ID;
    }
    
    @Column(name = "WORK_ORDER_ID")
    public java.lang.String getWORK_ORDER_ID() {
        return WORK_ORDER_ID;
    }
    public void setWORK_ORDER_ID(java.lang.String WORK_ORDER_ID) {
        this.WORK_ORDER_ID = WORK_ORDER_ID;
    }
    
    @Column(name = "ORDER_ID")
    public java.lang.String getORDER_ID() {
        return ORDER_ID;
    }
    public void setORDER_ID(java.lang.String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }
    
    @Column(name = "SH_LAST")
    public java.lang.String getSH_LAST() {
        return SH_LAST;
    }
    public void setSH_LAST(java.lang.String SH_LAST) {
        this.SH_LAST = SH_LAST;
    }
    
    @Column(name = "SH_STYLENO")
    public java.lang.String getSH_STYLENO() {
        return SH_STYLENO;
    }
    public void setSH_STYLENO(java.lang.String SH_STYLENO) {
        this.SH_STYLENO = SH_STYLENO;
    }
    
    @Column(name = "ORDER_NUM")
    public java.lang.String getORDER_NUM() {
        return ORDER_NUM;
    }
    public void setORDER_NUM(java.lang.String ORDER_NUM) {
        this.ORDER_NUM = ORDER_NUM;
    }
    
    @Column(name = "LEFT_SIZE_RUN")
    public java.lang.String getLEFT_SIZE_RUN() {
        return LEFT_SIZE_RUN;
    }
    public void setLEFT_SIZE_RUN(java.lang.String LEFT_SIZE_RUN) {
        this.LEFT_SIZE_RUN = LEFT_SIZE_RUN;
    }
    
    @Column(name = "RIGHT_SIZE_RUN")
    public java.lang.String getRIGHT_SIZE_RUN() {
        return RIGHT_SIZE_RUN;
    }
    public void setRIGHT_SIZE_RUN(java.lang.String RIGHT_SIZE_RUN) {
        this.RIGHT_SIZE_RUN = RIGHT_SIZE_RUN;
    }
    
    @Column(name = "NIKE_SH_ARITCLE")
    public java.lang.String getNIKE_SH_ARITCLE() {
        return NIKE_SH_ARITCLE;
    }
    public void setNIKE_SH_ARITCLE(java.lang.String NIKE_SH_ARITCLE) {
        this.NIKE_SH_ARITCLE = NIKE_SH_ARITCLE;
    }
    
    @Column(name = "MODEL_NA")
    public java.lang.String getMODEL_NA() {
        return MODEL_NA;
    }
    public void setMODEL_NA(java.lang.String MODEL_NA) {
        this.MODEL_NA = MODEL_NA;
    }
    
    @Column(name = "TOOLING_SIZE")
    public java.lang.String getTOOLING_SIZE() {
		return TOOLING_SIZE;
	}
	public void setTOOLING_SIZE(java.lang.String tOOLING_SIZE) {
		TOOLING_SIZE = tOOLING_SIZE;
	}
	
	@Column(name = "PID01")
	public java.lang.String getPID01() {
		return PID01;
	}
	public void setPID01(java.lang.String pID01) {
		PID01 = pID01;
	}
	
	 @Column(name = "PID02")
	public java.lang.String getPID02() {
		return PID02;
	}
	public void setPID02(java.lang.String pID02) {
		PID02 = pID02;
	}
	
	 @Column(name = "PID03")
	public java.lang.String getPID03() {
		return PID03;
	}
	public void setPID03(java.lang.String pID03) {
		PID03 = pID03;
	}
	
	 @Column(name = "PID04")
	public java.lang.String getPID04() {
		return PID04;
	}
	public void setPID04(java.lang.String pID04) {
		PID04 = pID04;
	}
	
	 @Column(name = "GROUP1")
	public java.lang.String getGROUP1() {
		return GROUP1;
	}
	public void setGROUP1(java.lang.String gROUP1) {
		GROUP1 = gROUP1;
	}
	
	 @Column(name = "GROUP2")
	public java.lang.String getGROUP2() {
		return GROUP2;
	}
	public void setGROUP2(java.lang.String gROUP2) {
		GROUP2 = gROUP2;
	}
	
	 @Column(name = "GROUP3")
	public java.lang.String getGROUP3() {
		return GROUP3;
	}
	public void setGROUP3(java.lang.String gROUP3) {
		GROUP3 = gROUP3;
	}
	
	 @Column(name = "GROUP4")
	public java.lang.String getGROUP4() {
		return GROUP4;
	}
	public void setGROUP4(java.lang.String gROUP4) {
		GROUP4 = gROUP4;
	}
	
	 @Column(name = "GROUP5")
	public java.lang.String getGROUP5() {
		return GROUP5;
	}
	public void setGROUP5(java.lang.String gROUP5) {
		GROUP5 = gROUP5;
	}
	
	 @Column(name = "GROUP6")
	public java.lang.String getGROUP6() {
		return GROUP6;
	}
	public void setGROUP6(java.lang.String gROUP6) {
		GROUP6 = gROUP6;
	}
	
	 @Column(name = "GROUP7")
	public java.lang.String getGROUP7() {
		return GROUP7;
	}
	public void setGROUP7(java.lang.String gROUP7) {
		GROUP7 = gROUP7;
	}
	
	 @Column(name = "GROUP8")
	public java.lang.String getGROUP8() {
		return GROUP8;
	}
	public void setGROUP8(java.lang.String gROUP8) {
		GROUP8 = gROUP8;
	}
	
	 @Column(name = "GROUP9")
	public java.lang.String getGROUP9() {
		return GROUP9;
	}
	public void setGROUP9(java.lang.String gROUP9) {
		GROUP9 = gROUP9;
	}
	
	 @Column(name = "GROUP10")
	public java.lang.String getGROUP10() {
		return GROUP10;
	}
	public void setGROUP10(java.lang.String gROUP10) {
		GROUP10 = gROUP10;
	}
	
	 @Column(name = "GROUP11")
	public java.lang.String getGROUP11() {
		return GROUP11;
	}
	public void setGROUP11(java.lang.String gROUP11) {
		GROUP11 = gROUP11;
	}
	
	 @Column(name = "GROUP12")
	public java.lang.String getGROUP12() {
		return GROUP12;
	}
	public void setGROUP12(java.lang.String gROUP12) {
		GROUP12 = gROUP12;
	}
	
    @Temporal(TemporalType.DATE)
	 @Column(name = "PG_DATE")
	public java.util.Date getPG_DATE() {
		return PG_DATE;
	}
	public void setPG_DATE(java.util.Date pG_DATE) {
		PG_DATE = pG_DATE;
	}
	
	 @Column(name = "ROUND")
	public java.lang.String getROUND() {
		return ROUND;
	}
	public void setROUND(java.lang.String rOUND) {
		ROUND = rOUND;
	}
	
    @Temporal(TemporalType.DATE)
	 @Column(name = "SHIPING_DATE")
	public java.util.Date getSHIPING_DATE() {
		return SHIPING_DATE;
	}
	public void setSHIPING_DATE(java.util.Date sHIPING_DATE) {
		SHIPING_DATE = sHIPING_DATE;
	}
	
	 @Column(name = "REGION")
	public java.lang.String getREGION() {
		return REGION;
	}
	public void setREGION(java.lang.String rEGION) {
		REGION = rEGION;
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
    
	@Transient 
	public boolean getISEDIT() {
		return ISEDIT;
	}
	 
	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}
	
	@Transient
	public boolean getISADD() {
		return ISADD;
	}

	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}
}
