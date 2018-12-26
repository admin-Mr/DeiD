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

import ds.dsid.domain.pk.DSID04_1Pk;
import ds.dsid.domain.pk.DSID20_Pk;

@Entity(name = "DSID20")
@Table(name = "dsid11")
@IdClass(DSID20_Pk.class)
public class DSID20{

	private java.lang.String   MODEL_NAS;		//型體
	private java.lang.String   SEGM_TYPE;		//分段類型
	private java.lang.String   MODEL_GENDER;	//鞋款(Ms男/Ws女)
	private java.lang.String   SECTION1;		//分段數值1
	private java.lang.String   SECTION2;		//分段數值2
	private java.lang.String   SECTION3;		//分段數值3
	private java.lang.String   SECTION4;		//分段數值4
	private java.lang.String   SECTION5;		//分段數值5
	private java.lang.String   SECTION6;		//分段數值6
	private java.lang.String   SECTION7;		//分段數值7
	private java.lang.String   SECTION8;		//分段數值8
	private java.lang.String   SECTION9;		//分段數值9
	private java.lang.String   SECTION10;		//分段數值10
	private java.lang.String   SECTION11_1;		//分段數值11_1
	private java.lang.String   SECTION11_2;		//分段數值11_2
	private java.lang.String   SECTION12_1;		//分段數值12_1
	private java.lang.String   SECTION12_2;		//分段數值12_2
	
	private String POINTS1; // 分段1
	private String POINTS2; // 分段2
	private String POINTS3; // 分段3
	private String POINTS4; // 分段4
	private String POINTS5; // 分段5
	private String POINTS6; // 分段6
	private String POINTS7; // 分段7
	private String POINTS8; // 分段8
	private String POINTS9; // 分段9
	private String POINTS10; // 分段10
	private String POINTS11; // 分段11
	private String POINTS12; // 分段12
	
	private java.lang.String   UP_USER;			//異動人員
	private java.util.Date 		UP_DATE;		//異動日期
	
	@Id
	@Column(name = "MODEL_NAS")
	public java.lang.String  getMODEL_NAS() {
		return MODEL_NAS;
	}
	public void setMODEL_NAS(java.lang.String  mODEL_NAS) {
		MODEL_NAS = mODEL_NAS;
	}
	
	@Id
	@Column(name = "SEGM_TYPE")
	public java.lang.String  getSEGM_TYPE() {
		return SEGM_TYPE;
	}
	public void setSEGM_TYPE(java.lang.String  sEGM_TYPE) {
		SEGM_TYPE = sEGM_TYPE;
	}
	
	@Column(name = "MODEL_GENDER")
	public java.lang.String  getMODEL_GENDER() {
		return MODEL_GENDER;
	}
	public void setMODEL_GENDER(java.lang.String  mODEL_GENDER) {
		MODEL_GENDER = mODEL_GENDER;
	}
	
	@Column(name = "SECTION1")
	public java.lang.String  getSECTION1() {
		return SECTION1;
	}
	public void setSECTION1(java.lang.String  sECTION1) {
		SECTION1 = sECTION1;
	}
	
	@Column(name = "SECTION2")
	public java.lang.String  getSECTION2() {
		return SECTION2;
	}
	public void setSECTION2(java.lang.String  sECTION2) {
		SECTION2 = sECTION2;
	}
	
	@Column(name = "SECTION3")
	public java.lang.String  getSECTION3() {
		return SECTION3;
	}
	public void setSECTION3(java.lang.String  sECTION3) {
		SECTION3 = sECTION3;
	}
	
	@Column(name = "SECTION4")
	public java.lang.String  getSECTION4() {
		return SECTION4;
	}
	public void setSECTION4(java.lang.String  sECTION4) {
		SECTION4 = sECTION4;
	}
	
	@Column(name = "SECTION5")
	public java.lang.String  getSECTION5() {
		return SECTION5;
	}
	public void setSECTION5(java.lang.String  sECTION5) {
		SECTION5 = sECTION5;
	}
	
	@Column(name = "SECTION6")
	public java.lang.String  getSECTION6() {
		return SECTION6;
	}
	public void setSECTION6(java.lang.String  sECTION6) {
		SECTION6 = sECTION6;
	}
	
	@Column(name = "SECTION7")
	public java.lang.String  getSECTION7() {
		return SECTION7;
	}
	public void setSECTION7(java.lang.String  sECTION7) {
		SECTION7 = sECTION7;
	}
	
	@Column(name = "SECTION8")
	public java.lang.String  getSECTION8() {
		return SECTION8;
	}
	public void setSECTION8(java.lang.String  sECTION8) {
		SECTION8 = sECTION8;
	}
	
	@Column(name = "SECTION9")
	public java.lang.String  getSECTION9() {
		return SECTION9;
	}
	public void setSECTION9(java.lang.String  sECTION9) {
		SECTION9 = sECTION9;
	}
	
	@Column(name = "SECTION10")
	public java.lang.String  getSECTION10() {
		return SECTION10;
	}
	public void setSECTION10(java.lang.String  sECTION10) {
		SECTION10 = sECTION10;
	}
	
	@Column(name = "SECTION11_1")
	public java.lang.String getSECTION11_1() {
		return SECTION11_1;
	}
	public void setSECTION11_1(java.lang.String sECTION11_1) {
		SECTION11_1 = sECTION11_1;
	}
	
	@Column(name = "SECTION11_2")
	public java.lang.String getSECTION11_2() {
		return SECTION11_2;
	}
	public void setSECTION11_2(java.lang.String sECTION11_2) {
		SECTION11_2 = sECTION11_2;
	}
	
	@Column(name = "SECTION12_1")
	public java.lang.String getSECTION12_1() {
		return SECTION12_1;
	}
	public void setSECTION12_1(java.lang.String sECTION12_1) {
		SECTION12_1 = sECTION12_1;
	}
	
	@Column(name = "SECTION12_2")
	public java.lang.String getSECTION12_2() {
		return SECTION12_2;
	}
	public void setSECTION12_2(java.lang.String sECTION12_2) {
		SECTION12_2 = sECTION12_2;
	}
	
/*	@Column(name = "LENGTH1")
	public java.lang.String  getLENGTH1() {
		return LENGTH1;
	}
	public void setLENGTH1(java.lang.String  lENGTH1) {
		LENGTH1 = lENGTH1;
	}
	
	@Column(name = "LENGTH2")
	public java.lang.String  getLENGTH2() {
		return LENGTH2;
	}
	public void setLENGTH2(java.lang.String  lENGTH2) {
		LENGTH2 = lENGTH2;
	}
	
	@Column(name = "LENGTH3")
	public java.lang.String  getLENGTH3() {
		return LENGTH3;
	}
	public void setLENGTH3(java.lang.String  lENGTH3) {
		LENGTH3 = lENGTH3;
	}
	
	@Column(name = "LENGTH4")
	public java.lang.String  getLENGTH4() {
		return LENGTH4;
	}
	public void setLENGTH4(java.lang.String  lENGTH4) {
		LENGTH4 = lENGTH4;
	}
	
	@Column(name = "LENGTH5")
	public java.lang.String  getLENGTH5() {
		return LENGTH5;
	}
	public void setLENGTH5(java.lang.String  lENGTH5) {
		LENGTH5 = lENGTH5;
	}
	
	@Column(name = "LENGTH6")
	public java.lang.String  getLENGTH6() {
		return LENGTH6;
	}
	public void setLENGTH6(java.lang.String  lENGTH6) {
		LENGTH6 = lENGTH6;
	}
	
	@Column(name = "LENGTH7")
	public java.lang.String  getLENGTH7() {
		return LENGTH7;
	}
	public void setLENGTH7(java.lang.String  lENGTH7) {
		LENGTH7 = lENGTH7;
	}
	
	@Column(name = "LENGTH8")
	public java.lang.String  getLENGTH8() {
		return LENGTH8;
	}
	public void setLENGTH8(java.lang.String  lENGTH8) {
		LENGTH8 = lENGTH8;
	}
	
	@Column(name = "LENGTH9")
	public java.lang.String  getLENGTH9() {
		return LENGTH9;
	}
	public void setLENGTH9(java.lang.String  lENGTH9) {
		LENGTH9 = lENGTH9;
	}
	
	@Column(name = "LENGTH10")
	public java.lang.String  getLENGTH10() {
		return LENGTH10;
	}
	public void setLENGTH10(java.lang.String  lENGTH10) {
		LENGTH10 = lENGTH10;
	}*/
	
	@Column(name = "UP_USER")
	public java.lang.String  getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(java.lang.String  uP_USER) {
		UP_USER = uP_USER;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	public java.util.Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(java.util.Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
	@Column(name = "POINTS1")
	public String getPOINTS1() {
		return POINTS1;
	}
	public void setPOINTS1(String pOINTS1) {
		POINTS1 = pOINTS1;
	}
	
	@Column(name = "POINTS2")
	public String getPOINTS2() {
		return POINTS2;
	}
	public void setPOINTS2(String pOINTS2) {
		POINTS2 = pOINTS2;
	}
	
	@Column(name = "POINTS3")
	public String getPOINTS3() {
		return POINTS3;
	}
	public void setPOINTS3(String pOINTS3) {
		POINTS3 = pOINTS3;
	}
	
	@Column(name = "POINTS4")
	public String getPOINTS4() {
		return POINTS4;
	}
	public void setPOINTS4(String pOINTS4) {
		POINTS4 = pOINTS4;
	}
	
	@Column(name = "POINTS5")
	public String getPOINTS5() {
		return POINTS5;
	}
	public void setPOINTS5(String pOINTS5) {
		POINTS5 = pOINTS5;
	}
	
	@Column(name = "POINTS6")
	public String getPOINTS6() {
		return POINTS6;
	}
	public void setPOINTS6(String pOINTS6) {
		POINTS6 = pOINTS6;
	}
	
	@Column(name = "POINTS7")
	public String getPOINTS7() {
		return POINTS7;
	}
	public void setPOINTS7(String pOINTS7) {
		POINTS7 = pOINTS7;
	}
	
	@Column(name = "POINTS8")
	public String getPOINTS8() {
		return POINTS8;
	}
	public void setPOINTS8(String pOINTS8) {
		POINTS8 = pOINTS8;
	}
	
	@Column(name = "POINTS9")
	public String getPOINTS9() {
		return POINTS9;
	}
	public void setPOINTS9(String pOINTS9) {
		POINTS9 = pOINTS9;
	}
	
	@Column(name = "POINTS10")
	public String getPOINTS10() {
		return POINTS10;
	}
	public void setPOINTS10(String pOINTS10) {
		POINTS10 = pOINTS10;
	}
	
	@Column(name = "POINTS11")
	public String getPOINTS11() {
		return POINTS11;
	}
	public void setPOINTS11(String pOINTS11) {
		POINTS11 = pOINTS11;
	}
	
	@Column(name = "POINTS12")
	public String getPOINTS12() {
		return POINTS12;
	}
	public void setPOINTS12(String pOINTS12) {
		POINTS12 = pOINTS12;
	}
	
}
