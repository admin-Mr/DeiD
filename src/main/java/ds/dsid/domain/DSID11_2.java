package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ds.dsid.domain.pk.DSID11_1Pk;
import ds.dsid.domain.pk.DSID11_2Pk;

@Entity
@Table(name = "DSID11_2")
@IdClass(DSID11_2Pk.class)
public class DSID11_2 {

	private String MODEL_NA;	// 型體名稱
	private String POINTS_NB;	// 分段數量
	private String EL_SEQ;		// 項次
	private String MODEL_GENDER;	// MS男鞋&WS女鞋
	private String PERIOD1;		// 分段1
	private String PERIOD2;		// 
	private String PERIOD3;		// 
	private String PERIOD4;		// 
	private String PERIOD5;		// 
	private String PERIOD6;		// 
	private String PERIOD7;		// 
	private String PERIOD8;		// 
	private String PERIOD9;		// 
	private String PERIOD10;	// 
	private String UP_USER;		// 異動人員
	private Date 	UP_DATE;	// 異動日期
	
	@Id
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Id
	@Column(name = "EL_SEQ")
	public String getEL_SEQ() {
		return EL_SEQ;
	}
	public void setEL_SEQ(String eL_SEQ) {
		EL_SEQ = eL_SEQ;
	}
	
	@Column(name = "POINTS_NB")
	public String getPOINTS_NB() {
		return POINTS_NB;
	}
	public void setPOINTS_NB(String pOINTS_NB) {
		POINTS_NB = pOINTS_NB;
	}
	
	@Column(name = "MODEL_GENDER")
	public String getMODEL_GENDER() {
		return MODEL_GENDER;
	}
	public void setMODEL_GENDER(String mODEL_GENDER) {
		MODEL_GENDER = mODEL_GENDER;
	}
	
	@Column(name = "PERIOD1")
	public String getPERIOD1() {
		return PERIOD1;
	}
	public void setPERIOD1(String pERIOD1) {
		PERIOD1 = pERIOD1;
	}
	
	@Column(name = "PERIOD2")
	public String getPERIOD2() {
		return PERIOD2;
	}
	public void setPERIOD2(String pERIOD2) {
		PERIOD2 = pERIOD2;
	}
	
	@Column(name = "PERIOD3")
	public String getPERIOD3() {
		return PERIOD3;
	}
	public void setPERIOD3(String pERIOD3) {
		PERIOD3 = pERIOD3;
	}
	
	@Column(name = "PERIOD4")
	public String getPERIOD4() {
		return PERIOD4;
	}
	public void setPERIOD4(String pERIOD4) {
		PERIOD4 = pERIOD4;
	}
	
	@Column(name = "PERIOD5")
	public String getPERIOD5() {
		return PERIOD5;
	}
	public void setPERIOD5(String pERIOD5) {
		PERIOD5 = pERIOD5;
	}
	
	@Column(name = "PERIOD6")
	public String getPERIOD6() {
		return PERIOD6;
	}
	public void setPERIOD6(String pERIOD6) {
		PERIOD6 = pERIOD6;
	}
	
	@Column(name = "PERIOD7")
	public String getPERIOD7() {
		return PERIOD7;
	}
	public void setPERIOD7(String pERIOD7) {
		PERIOD7 = pERIOD7;
	}
	
	@Column(name = "PERIOD8")
	public String getPERIOD8() {
		return PERIOD8;
	}
	public void setPERIOD8(String pERIOD8) {
		PERIOD8 = pERIOD8;
	}
	
	@Column(name = "PERIOD9")
	public String getPERIOD9() {
		return PERIOD9;
	}
	public void setPERIOD9(String pERIOD9) {
		PERIOD9 = pERIOD9;
	}
	
	@Column(name = "PERIOD10")
	public String getPERIOD10() {
		return PERIOD10;
	}
	public void setPERIOD10(String pERIOD10) {
		PERIOD10 = pERIOD10;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
	

	
}
