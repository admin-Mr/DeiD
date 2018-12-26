package ds.dsid.domain;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Column;
/**
* 蝟餌絞銝餅��
**/
@Entity
@Table(name = "DSID10")
public class DSID10 {

	private java.lang.String NIKE_SH_ARITCLE;
	private java.lang.String PID_GROUP;
	private java.lang.String PID_TXT;
	private java.lang.String GROUP1_STATUS;
	private java.lang.String GROUP2_STATUS;
	private java.lang.String GROUP3_STATUS;
	private java.lang.String GROUP4_STATUS;
	private java.lang.String GROUP5_STATUS;
	private java.lang.String GROUP6_STATUS;
	private java.lang.String GROUP7_STATUS;
	private java.lang.String GROUP8_STATUS;
	private java.lang.String GROUP9_STATUS;
	private java.lang.String GROUP10_STATUS;
	private java.lang.String GROUP11_STATUS;
	private java.lang.String GROUP12_STATUS;
	private java.lang.String GROUP13_STATUS;
	private java.lang.String GROUP14_STATUS;
	private java.lang.String GROUP15_STATUS;
	private java.lang.String GROUP16_STATUS;
	private java.lang.String GROUP17_STATUS;
	private java.lang.String GROUP18_STATUS;
	private java.lang.String GROUP19_STATUS;
	private java.lang.String GROUP20_STATUS;
	private java.lang.String PID01_STATUS;
	private java.lang.String PID02_STATUS;
	private java.lang.String PID03_STATUS;
	private java.lang.String PID04_STATUS;
	private java.lang.String GROUP1_NA;
	private java.lang.String GROUP2_NA;
	private java.lang.String GROUP3_NA;
	private java.lang.String GROUP4_NA;
	private java.lang.String GROUP5_NA;
	private java.lang.String GROUP6_NA;
	private java.lang.String GROUP7_NA;
	private java.lang.String GROUP8_NA;
	private java.lang.String GROUP9_NA;
	private java.lang.String GROUP10_NA;
	private java.lang.String GROUP11_NA;
	private java.lang.String GROUP12_NA;
	private java.lang.String GROUP13_NA;
	private java.lang.String GROUP14_NA;
	private java.lang.String GROUP15_NA;
	private java.lang.String GROUP16_NA;
	private java.lang.String GROUP17_NA;
	private java.lang.String GROUP18_NA;
	private java.lang.String GROUP19_NA;
	private java.lang.String GROUP20_NA;
    
    private java.lang.String UP_USER;	
    private java.util.Date UP_DATE;	
    
	private java.lang.String ORDER_NUM;
	private java.lang.String MODEL_NAS;
    
	private boolean ISADD;
	private boolean ISEDIT;

    @Id
    @Column(name = "NIKE_SH_ARITCLE")
	public java.lang.String getNIKE_SH_ARITCLE() {
		return NIKE_SH_ARITCLE;
	}
	public void setNIKE_SH_ARITCLE(java.lang.String nIKE_SH_ARITCLE) {
		NIKE_SH_ARITCLE = nIKE_SH_ARITCLE;
	}
	
    @Column(name = "PID_GROUP")
	public java.lang.String getPID_GROUP() {
		return PID_GROUP;
	}
	public void setPID_GROUP(java.lang.String pID_GROUP) {
		PID_GROUP = pID_GROUP;
	}		
	
    @Column(name = "PID_TXT")
	public java.lang.String getPID_TXT() {
		return PID_TXT;
	}
	public void setPID_TXT(java.lang.String pID_TXT) {
		PID_TXT = pID_TXT;
	}	
	
    @Column(name = "GROUP1_STATUS")
	public java.lang.String getGROUP1_STATUS() {
		return GROUP1_STATUS;
	}
	public void setGROUP1_STATUS(java.lang.String gROUP1_STATUS) {
		GROUP1_STATUS = gROUP1_STATUS;
	}	
	
    @Column(name = "GROUP2_STATUS")
	public java.lang.String getGROUP2_STATUS() {
		return GROUP2_STATUS;
	}
	public void setGROUP2_STATUS(java.lang.String gROUP2_STATUS) {
		GROUP2_STATUS = gROUP2_STATUS;
	}	
	
    @Column(name = "GROUP3_STATUS")
	public java.lang.String getGROUP3_STATUS() {
		return GROUP3_STATUS;
	}
	public void setGROUP3_STATUS(java.lang.String gROUP3_STATUS) {
		GROUP3_STATUS = gROUP3_STATUS;
	}	
	
    @Column(name = "GROUP4_STATUS")
	public java.lang.String getGROUP4_STATUS() {
		return GROUP4_STATUS;
	}
	public void setGROUP4_STATUS(java.lang.String gROUP4_STATUS) {
		GROUP4_STATUS = gROUP4_STATUS;
	}	
	
    @Column(name = "GROUP5_STATUS")
	public java.lang.String getGROUP5_STATUS() {
		return GROUP5_STATUS;
	}
	public void setGROUP5_STATUS(java.lang.String gROUP5_STATUS) {
		GROUP5_STATUS = gROUP5_STATUS;
	}	
	
    @Column(name = "GROUP6_STATUS")
	public java.lang.String getGROUP6_STATUS() {
		return GROUP6_STATUS;
	}
	public void setGROUP6_STATUS(java.lang.String gROUP6_STATUS) {
		GROUP6_STATUS = gROUP6_STATUS;
	}	
	
    @Column(name = "GROUP7_STATUS")
	public java.lang.String getGROUP7_STATUS() {
		return GROUP7_STATUS;
	}
	public void setGROUP7_STATUS(java.lang.String gROUP7_STATUS) {
		GROUP7_STATUS = gROUP7_STATUS;
	}	
	
    @Column(name = "GROUP8_STATUS")
	public java.lang.String getGROUP8_STATUS() {
		return GROUP8_STATUS;
	}
	public void setGROUP8_STATUS(java.lang.String gROUP8_STATUS) {
		GROUP8_STATUS = gROUP8_STATUS;
	}	
	
    @Column(name = "GROUP9_STATUS")
	public java.lang.String getGROUP9_STATUS() {
		return GROUP9_STATUS;
	}
	public void setGROUP9_STATUS(java.lang.String gROUP9_STATUS) {
		GROUP9_STATUS = gROUP9_STATUS;
	}	
	
    @Column(name = "GROUP10_STATUS")
	public java.lang.String getGROUP10_STATUS() {
		return GROUP10_STATUS;
	}
	public void setGROUP10_STATUS(java.lang.String gROUP10_STATUS) {
		GROUP10_STATUS = gROUP10_STATUS;
	}	
	
    @Column(name = "GROUP11_STATUS")
	public java.lang.String getGROUP11_STATUS() {
		return GROUP11_STATUS;
	}
	public void setGROUP11_STATUS(java.lang.String gROUP11_STATUS) {
		GROUP11_STATUS = gROUP11_STATUS;
	}	
	
    @Column(name = "GROUP12_STATUS")
	public java.lang.String getGROUP12_STATUS() {
		return GROUP12_STATUS;
	}
	public void setGROUP12_STATUS(java.lang.String gROUP12_STATUS) {
		GROUP12_STATUS = gROUP12_STATUS;
	}	
	
    @Column(name = "GROUP13_STATUS")
	public java.lang.String getGROUP13_STATUS() {
		return GROUP13_STATUS;
	}
	public void setGROUP13_STATUS(java.lang.String gROUP13_STATUS) {
		GROUP13_STATUS = gROUP13_STATUS;
	}	
	
    @Column(name = "GROUP14_STATUS")
	public java.lang.String getGROUP14_STATUS() {
		return GROUP14_STATUS;
	}
	public void setGROUP14_STATUS(java.lang.String gROUP14_STATUS) {
		GROUP14_STATUS = gROUP14_STATUS;
	}	
	
    @Column(name = "GROUP15_STATUS")
	public java.lang.String getGROUP15_STATUS() {
		return GROUP15_STATUS;
	}
	public void setGROUP15_STATUS(java.lang.String gROUP15_STATUS) {
		GROUP15_STATUS = gROUP15_STATUS;
	}	
	
    @Column(name = "GROUP16_STATUS")
	public java.lang.String getGROUP16_STATUS() {
		return GROUP16_STATUS;
	}
	public void setGROUP16_STATUS(java.lang.String gROUP16_STATUS) {
		GROUP16_STATUS = gROUP16_STATUS;
	}	
	
    @Column(name = "GROUP17_STATUS")
	public java.lang.String getGROUP17_STATUS() {
		return GROUP17_STATUS;
	}
	public void setGROUP17_STATUS(java.lang.String gROUP17_STATUS) {
		GROUP17_STATUS = gROUP17_STATUS;
	}	
	
    @Column(name = "GROUP18_STATUS")
	public java.lang.String getGROUP18_STATUS() {
		return GROUP18_STATUS;
	}
	public void setGROUP18_STATUS(java.lang.String gROUP18_STATUS) {
		GROUP18_STATUS = gROUP18_STATUS;
	}	
	
    @Column(name = "GROUP19_STATUS")
	public java.lang.String getGROUP19_STATUS() {
		return GROUP19_STATUS;
	}
	public void setGROUP19_STATUS(java.lang.String gROUP19_STATUS) {
		GROUP19_STATUS = gROUP19_STATUS;
	}	
	
    @Column(name = "GROUP20_STATUS")
	public java.lang.String getGROUP20_STATUS() {
		return GROUP20_STATUS;
	}
	public void setGROUP20_STATUS(java.lang.String gROUP20_STATUS) {
		GROUP20_STATUS = gROUP20_STATUS;
	}	
	
    @Column(name = "PID01_STATUS")
	public java.lang.String getPID01_STATUS() {
		return PID01_STATUS;
	}
	public void setPID01_STATUS(java.lang.String pID01_STATUS) {
		PID01_STATUS = pID01_STATUS;
	}	
	
    @Column(name = "PID02_STATUS")
	public java.lang.String getPID02_STATUS() {
		return PID02_STATUS;
	}
	public void setPID02_STATUS(java.lang.String pID02_STATUS) {
		PID02_STATUS = pID02_STATUS;
	}	
	
    @Column(name = "PID03_STATUS")
	public java.lang.String getPID03_STATUS() {
		return PID03_STATUS;
	}
	public void setPID03_STATUS(java.lang.String pID03_STATUS) {
		PID03_STATUS = pID03_STATUS;
	}	
	
    @Column(name = "PID04_STATUS")
	public java.lang.String getPID04_STATUS() {
		return PID04_STATUS;
	}
	public void setPID04_STATUS(java.lang.String pID04_STATUS) {
		PID04_STATUS = pID04_STATUS;
	}	
	
    @Column(name = "GROUP1_NA")
	public java.lang.String getGROUP1_NA() {
		return GROUP1_NA;
	}
	public void setGROUP1_NA(java.lang.String gROUP1_NA) {
		GROUP1_NA = gROUP1_NA;
	}	
	
    @Column(name = "GROUP2_NA")
	public java.lang.String getGROUP2_NA() {
		return GROUP2_NA;
	}
	public void setGROUP2_NA(java.lang.String gROUP2_NA) {
		GROUP2_NA = gROUP2_NA;
	}	
	
    @Column(name = "GROUP3_NA")
	public java.lang.String getGROUP3_NA() {
		return GROUP3_NA;
	}
	public void setGROUP3_NA(java.lang.String gROUP3_NA) {
		GROUP3_NA = gROUP3_NA;
	}	
	
    @Column(name = "GROUP4_NA")
	public java.lang.String getGROUP4_NA() {
		return GROUP4_NA;
	}
	public void setGROUP4_NA(java.lang.String gROUP4_NA) {
		GROUP4_NA = gROUP4_NA;
	}	
	
    @Column(name = "GROUP5_NA")
	public java.lang.String getGROUP5_NA() {
		return GROUP5_NA;
	}
	public void setGROUP5_NA(java.lang.String gROUP5_NA) {
		GROUP5_NA = gROUP5_NA;
	}	
	
    @Column(name = "GROUP6_NA")
	public java.lang.String getGROUP6_NA() {
		return GROUP6_NA;
	}
	public void setGROUP6_NA(java.lang.String gROUP6_NA) {
		GROUP6_NA = gROUP6_NA;
	}	
	
    @Column(name = "GROUP7_NA")
	public java.lang.String getGROUP7_NA() {
		return GROUP7_NA;
	}
	public void setGROUP7_NA(java.lang.String gROUP7_NA) {
		GROUP7_NA = gROUP7_NA;
	}	
	
    @Column(name = "GROUP8_NA")
	public java.lang.String getGROUP8_NA() {
		return GROUP8_NA;
	}
	public void setGROUP8_NA(java.lang.String gROUP8_NA) {
		GROUP8_NA = gROUP8_NA;
	}	
	
    @Column(name = "GROUP9_NA")
	public java.lang.String getGROUP9_NA() {
		return GROUP9_NA;
	}
	public void setGROUP9_NA(java.lang.String gROUP9_NA) {
		GROUP9_NA = gROUP9_NA;
	}	
	
    @Column(name = "GROUP10_NA")
	public java.lang.String getGROUP10_NA() {
		return GROUP10_NA;
	}
	public void setGROUP10_NA(java.lang.String gROUP10_NA) {
		GROUP10_NA = gROUP10_NA;
	}	
	
    @Column(name = "GROUP11_NA")
	public java.lang.String getGROUP11_NA() {
		return GROUP11_NA;
	}
	public void setGROUP11_NA(java.lang.String gROUP11_NA) {
		GROUP11_NA = gROUP11_NA;
	}	
	
    @Column(name = "GROUP12_NA")
	public java.lang.String getGROUP12_NA() {
		return GROUP12_NA;
	}
	public void setGROUP12_NA(java.lang.String gROUP12_NA) {
		GROUP12_NA = gROUP12_NA;
	}	
	
    @Column(name = "GROUP13_NA")
	public java.lang.String getGROUP13_NA() {
		return GROUP13_NA;
	}
	public void setGROUP13_NA(java.lang.String gROUP13_NA) {
		GROUP13_NA = gROUP13_NA;
	}	
	
    @Column(name = "GROUP14_NA")
	public java.lang.String getGROUP14_NA() {
		return GROUP14_NA;
	}
	public void setGROUP14_NA(java.lang.String gROUP14_NA) {
		GROUP14_NA = gROUP14_NA;
	}	
	
    @Column(name = "GROUP15_NA")
	public java.lang.String getGROUP15_NA() {
		return GROUP15_NA;
	}
	public void setGROUP15_NA(java.lang.String gROUP15_NA) {
		GROUP15_NA = gROUP15_NA;
	}	
	
    @Column(name = "GROUP16_NA")
	public java.lang.String getGROUP16_NA() {
		return GROUP16_NA;
	}
	public void setGROUP16_NA(java.lang.String gROUP16_NA) {
		GROUP16_NA = gROUP16_NA;
	}	
	
    @Column(name = "GROUP17_NA")
	public java.lang.String getGROUP17_NA() {
		return GROUP17_NA;
	}
	public void setGROUP17_NA(java.lang.String gROUP17_NA) {
		GROUP17_NA = gROUP17_NA;
	}	
	
    @Column(name = "GROUP18_NA")
	public java.lang.String getGROUP18_NA() {
		return GROUP18_NA;
	}
	public void setGROUP18_NA(java.lang.String gROUP18_NA) {
		GROUP18_NA = gROUP18_NA;
	}	
	
    @Column(name = "GROUP19_NA")
	public java.lang.String getGROUP19_NA() {
		return GROUP19_NA;
	}
	public void setGROUP19_NA(java.lang.String gROUP19_NA) {
		GROUP19_NA = gROUP19_NA;
	}	
	
    @Column(name = "GROUP20_NA")
	public java.lang.String getGROUP20_NA() {
		return GROUP20_NA;
	}
	public void setGROUP20_NA(java.lang.String gROUP20_NA) {
		GROUP20_NA = gROUP20_NA;
	}
 
    @Column(name = "ORDER_NUM")
	public java.lang.String getORDER_NUM() {
		return ORDER_NUM;
	}
	public void setORDER_NUM(java.lang.String oRDER_NUM) {
		ORDER_NUM = oRDER_NUM;
	}
	
    @Column(name = "MODEL_NAS")	
	public java.lang.String getMODEL_NAS() {
		return MODEL_NAS;
	}
	public void setMODEL_NAS(java.lang.String mODEL_NAS) {
		MODEL_NAS = mODEL_NAS;
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
