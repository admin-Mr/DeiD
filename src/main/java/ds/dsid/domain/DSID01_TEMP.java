package ds.dsid.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ds.dsid.domain.pk.DSID01_TEMP_PK;



@Entity
@Table(name="DSID01_TEMP")
@SuppressWarnings("serial")
@IdClass(DSID01_TEMP_PK.class)
public class DSID01_TEMP implements Serializable, Comparable<DSID01_TEMP>{

	private Date ORDER_DATE; 		// 接單日期
	private String WORK_ORDER_ID; 	// 客戶訂單ID
	private String TYPE; 			// 訂單類型
	private String SHIP_GROUP_ID;
	private String ORDER_ID;
	private String NIKE_SH_ARITCLE; // nike型體名稱
	private String MODEL_NA; 		// 型體顏色
	private String SH_STYLENO; 		// 配色
	private String ORDER_NUM; 		// 訂單數
	private String LEFT_SIZE_RUN; 	// 左腳碼數
	private String RIGHT_SIZE_RUN; 	// 右腳碼數
	private Date SHIPING_DATE; 		// 出貨日期
	private String REGION; 			// 出貨地區
	private String EXP_QTY; 		// 出貨數量
	private String EXP_STATUS; 		// 出貨狀態
	private String COUNTRY; 		// 國假
	private String ITEMNUMBER;		// 項目號
	private String PRIORITY; 		// 優先度
	private Date FACTACCPDATE; 		// 工廠接受日期
	private Date REQUSHIPDATE; 		// 重新裝運日期
	private String EXOTIC; 			// 是否外來
	private String REMAKE; 			// 是否再制
	private String SHIPPER; 		// 托運人
	private String BILLTOREGION; 	// 匯票到地區
	private String SHIPTOSTUDIO; 
	private String POSTALCODE; 		// 郵寄編碼
	private String URL1; 
	private String URL2; 
	private String URL3; 
	private String URL4; 
	private String URL5; 
	private String URL6; 
	private String URL7; 
	private String URL8; 
	private String URL9;
	private String URL10; 

	private String UP_USER; 		// 更新人員
	private Date UP_DATE; 			// 更新日期
	
    @Id
    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.DATE)
	public Date getORDER_DATE() {
		return ORDER_DATE;
	}
	public void setORDER_DATE(Date oRDER_DATE) {
		ORDER_DATE = oRDER_DATE;
	}
	
    @Id
    @Column(name = "WORK_ORDER_ID")
	public String getWORK_ORDER_ID() {
		return WORK_ORDER_ID;
	}
	public void setWORK_ORDER_ID(String wORK_ORDER_ID) {
		WORK_ORDER_ID = wORK_ORDER_ID;
	}
	
    @Column(name = "TYPE")
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	
    @Column(name = "SHIP_GROUP_ID")
	public String getSHIP_GROUP_ID() {
		return SHIP_GROUP_ID;
	}
	public void setSHIP_GROUP_ID(String sHIP_GROUP_ID) {
		SHIP_GROUP_ID = sHIP_GROUP_ID;
	}
	
	@Column(name = "ORDER_ID")
	public String getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	
	@Column(name = "NIKE_SH_ARITCLE")
	public String getNIKE_SH_ARITCLE() {
		return NIKE_SH_ARITCLE;
	}
	public void setNIKE_SH_ARITCLE(String nIKE_SH_ARITCLE) {
		NIKE_SH_ARITCLE = nIKE_SH_ARITCLE;
	}
	
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "SH_STYLENO")
	public String getSH_STYLENO() {
		return SH_STYLENO;
	}
	public void setSH_STYLENO(String sH_STYLENO) {
		SH_STYLENO = sH_STYLENO;
	}
	
	@Column(name = "ORDER_NUM")
	public String getORDER_NUM() {
		return ORDER_NUM;
	}
	public void setORDER_NUM(String oRDER_NUM) {
		ORDER_NUM = oRDER_NUM;
	}
	
	@Column(name = "LEFT_SIZE_RUN")
	public String getLEFT_SIZE_RUN() {
		return LEFT_SIZE_RUN;
	}
	public void setLEFT_SIZE_RUN(String lEFT_SIZE_RUN) {
		LEFT_SIZE_RUN = lEFT_SIZE_RUN;
	}
	
	@Column(name = "RIGHT_SIZE_RUN")
	public String getRIGHT_SIZE_RUN() {
		return RIGHT_SIZE_RUN;
	}
	public void setRIGHT_SIZE_RUN(String rIGHT_SIZE_RUN) {
		RIGHT_SIZE_RUN = rIGHT_SIZE_RUN;
	}
	
	@Column(name = "SHIPING_DATE")
	@Temporal(TemporalType.DATE)
	public Date getSHIPING_DATE() {
		return SHIPING_DATE;
	}
	public void setSHIPING_DATE(Date sHIPING_DATE) {
		SHIPING_DATE = sHIPING_DATE;
	}
	
	@Column(name = "REGION")
	public String getREGION() {
		return REGION;
	}
	public void setREGION(String rEGION) {
		REGION = rEGION;
	}
	
	@Column(name = "EXP_QTY")
	public String getEXP_QTY() {
		return EXP_QTY;
	}
	public void setEXP_QTY(String eXP_QTY) {
		EXP_QTY = eXP_QTY;
	}
	
	@Column(name = "EXP_STATUS")
	public String getEXP_STATUS() {
		return EXP_STATUS;
	}
	public void setEXP_STATUS(String eXP_STATUS) {
		EXP_STATUS = eXP_STATUS;
	}
	
	@Column(name = "COUNTRY")
	public String getCOUNTRY() {
		return COUNTRY;
	}
	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY;
	}
	
	@Column(name = "ITEMNUMBER")
	public String getITEMNUMBER() {
		return ITEMNUMBER;
	}
	public void setITEMNUMBER(String iTEMNUMBER) {
		ITEMNUMBER = iTEMNUMBER;
	}
	
	@Column(name = "PRIORITY")
	public String getPRIORITY() {
		return PRIORITY;
	}
	public void setPRIORITY(String pRIORITY) {
		PRIORITY = pRIORITY;
	}
	
	@Column(name = "FACTACCPDATE")
	@Temporal(TemporalType.DATE)
	public Date getFACTACCPDATE() {
		return FACTACCPDATE;
	}
	public void setFACTACCPDATE(Date fACTACCPDATE) {
		FACTACCPDATE = fACTACCPDATE;
	}
	
	@Column(name = "REQUSHIPDATE")
	@Temporal(TemporalType.DATE)
	public Date getREQUSHIPDATE() {
		return REQUSHIPDATE;
	}
	public void setREQUSHIPDATE(Date rEQUSHIPDATE) {
		REQUSHIPDATE = rEQUSHIPDATE;
	}
	
	@Column(name = "EXOTIC")
	public String getEXOTIC() {
		return EXOTIC;
	}
	public void setEXOTIC(String eXOTIC) {
		EXOTIC = eXOTIC;
	}
	
	@Column(name = "REMAKE")
	public String getREMAKE() {
		return REMAKE;
	}
	public void setREMAKE(String rEMAKE) {
		REMAKE = rEMAKE;
	}
	
	@Column(name = "SHIPPER")
	public String getSHIPPER() {
		return SHIPPER;
	}
	public void setSHIPPER(String sHIPPER) {
		SHIPPER = sHIPPER;
	}
	
	@Column(name = "BILLTOREGION")
	public String getBILLTOREGION() {
		return BILLTOREGION;
	}
	public void setBILLTOREGION(String bILLTOREGION) {
		BILLTOREGION = bILLTOREGION;
	}
	
	@Column(name = "SHIPTOSTUDIO")
	public String getSHIPTOSTUDIO() {
		return SHIPTOSTUDIO;
	}
	public void setSHIPTOSTUDIO(String sHIPTOSTUDIO) {
		SHIPTOSTUDIO = sHIPTOSTUDIO;
	}
	
	@Column(name = "POSTALCODE")
	public String getPOSTALCODE() {
		return POSTALCODE;
	}
	public void setPOSTALCODE(String pOSTALCODE) {
		POSTALCODE = pOSTALCODE;
	}
	
	@Column(name = "URL1")
	public String getURL1() {
		return URL1;
	}
	public void setURL1(String uRL1) {
		URL1 = uRL1;
	}
	
	@Column(name = "URL2")
	public String getURL2() {
		return URL2;
	}
	public void setURL2(String uRL2) {
		URL2 = uRL2;
	}
	
	@Column(name = "URL3")
	public String getURL3() {
		return URL3;
	}
	public void setURL3(String uRL3) {
		URL3 = uRL3;
	}
	
	@Column(name = "URL4")
	public String getURL4() {
		return URL4;
	}
	public void setURL4(String uRL4) {
		URL4 = uRL4;
	}
	
	@Column(name = "URL5")
	public String getURL5() {
		return URL5;
	}
	public void setURL5(String uRL5) {
		URL5 = uRL5;
	}
	
	@Column(name = "URL6")
	public String getURL6() {
		return URL6;
	}
	public void setURL6(String uRL6) {
		URL6 = uRL6;
	}
	
	@Column(name = "URL7")
	public String getURL7() {
		return URL7;
	}
	public void setURL7(String uRL7) {
		URL7 = uRL7;
	}
	
	@Column(name = "URL8")
	public String getURL8() {
		return URL8;
	}
	public void setURL8(String uRL8) {
		URL8 = uRL8;
	}
	
	@Column(name = "URL9")
	public String getURL9() {
		return URL9;
	}
	public void setURL9(String uRL9) {
		URL9 = uRL9;
	}
	
	@Column(name = "URL10")
	public String getURL10() {
		return URL10;
	}
	public void setURL10(String uRL10) {
		URL10 = uRL10;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	@Column(name = "UP_DATE")
	@Temporal(TemporalType.DATE)
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	@Override
	public int compareTo(DSID01_TEMP o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
