package ds.dshr.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import javax.persistence.Column;

/**
* 
**/
@Entity
@Table(name = "DSHR20")
public class DSHR20 {

	private java.lang.String SEC;
	private java.lang.String IS_DEL;
	private java.lang.String ANO;
	private java.lang.String PNL;
	private java.lang.String NN_NO;
	private java.lang.String NAME;
	private java.lang.String IMARK;
	private java.lang.String ZZB;
	private java.lang.String ZZW;
	private java.lang.String JB;
	private java.lang.String OPE;
	private java.lang.String IDNO;
	private java.lang.String PROV;
	private java.util.Date BDATE;
	private java.lang.String MALE;
	private java.lang.String EDCA;
	private java.lang.String MARRY;
	private java.util.Date IN_DATE;
	private java.lang.String ADS;
	private java.lang.String POSTNUM;
	private java.lang.String TEL;
	private java.lang.String HH_NO;
	private java.lang.String JS_NAME;
	private java.lang.String GGS;
	private java.util.Date UNDEL;
	private java.lang.String OUSER;
	private java.lang.String SAVEOLD;
	private java.util.Date TURN_D;
	private java.lang.String BEG_D;
	private java.util.Date END_D;
	private java.util.Date UP_DATE;
	private java.lang.String DEL_MK;
	private java.lang.String DELSEN_MK;
	private java.util.Date OUTDATE;
	private java.lang.String CLAS;
	private java.util.Date ODATE;
	private java.lang.String TTM;
	private java.lang.String SAVE_NO;
	private java.util.Date FIN_DATE;
	private java.util.Date WIN_DATE;
	private java.lang.String PP_DEP;
	private java.lang.String PHONE;
	private java.lang.String PERSON;
	private java.lang.String ZY_NUMBER;
	private java.lang.String MMZ;
	private java.lang.String JS_NAME1;
	private java.lang.String FIX_CODE;
	private java.lang.String PB_FANO;
	private java.util.Date SY_B_DATE;
	private java.util.Date SY_E_DATE;
	private java.lang.String SSH;
	private java.lang.String OLDPNL;
	private java.lang.String FAC;
	private java.lang.String ADS_NOW;
	private java.lang.String IS_DOCT;
	private java.lang.String PHONEME;
	private java.lang.String OO_NAME;
	private java.lang.String XZB;
	private java.lang.String CARD_NO;
	private java.lang.String ISOK;
	private java.lang.String OPEC;
	private java.lang.String HOUSING;
	private java.lang.String HOUSE_NO;
	private java.lang.String KKT;
	private java.lang.String TAXCARD;
	private java.util.Date CON_DATE;
	private java.lang.String WORKERTY;
	private java.lang.String RELI_NO;
	private java.lang.String TAX_NO;
	private java.lang.String ATTRI;
	private java.lang.String LONGT;
	private java.lang.String IS_RCB;
	private java.lang.Long BONUSB;
	private java.lang.String MBS_FREE;
	private java.lang.String BIRTH_P;
	private java.lang.String RCB_F;
	private java.lang.String GROU;
	private java.lang.String LZ_NO;
	private java.util.Date DELDATE;
	private java.lang.String MM_NM;
	private java.lang.String UNEM;
	private java.lang.String GROUP_ID;
	private java.lang.String TRIAL_FLAG;
	private java.util.Date IDNO_DATE;
	private java.lang.String DOCT_NO;
	private java.lang.Long TRIAL;
	private java.lang.Long SALARY_1;
	private java.lang.Long SALARY_2;
	private java.lang.String WNO;
	private java.util.Date SOUTDATE;
	private java.lang.Long II_ZWJ;
	private java.util.Date TAXNO_DATE;
 	
	private DSHR04 DSHR04;
	private DSHR19 DSHR19;
	
	// 部門名稱
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "JB", referencedColumnName = "JB", insertable = false, updatable = false)
	public DSHR04 getDSHR04() {
		return DSHR04;
	}

	public void setDSHR04(DSHR04 dSHR04) {
		DSHR04 = dSHR04;
	}

	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "SEC", referencedColumnName = "SEC", insertable = false, updatable = false)
	public DSHR19 getDSHR19() {
		return DSHR19;
	}

	public void setDSHR19(DSHR19 dSHR19) {
		DSHR19 = dSHR19;
	}

	/**
	 * @return SEC
	 */

	@Column(name = "SEC")
	public java.lang.String getSEC() {
		return SEC;
	}

	/**
	 * @param SEC
	 */
	public void setSEC(java.lang.String SEC) {
		this.SEC = SEC;
	}

	/**
	 * @return IS_DEL
	 */
	@Column(name = "IS_DEL")
	public java.lang.String getIS_DEL() {
		return IS_DEL;
	}

	/**
	 * @param IS_DEL
	 */
	public void setIS_DEL(java.lang.String IS_DEL) {
		this.IS_DEL = IS_DEL;
	}

	/**
	 * @return ANO
	 */
	@Column(name = "ANO")
	public java.lang.String getANO() {
		return ANO;
	}

	/**
	 * @param ANO
	 */
	public void setANO(java.lang.String ANO) {
		this.ANO = ANO;
	}

	/**
	 * @return PNL
	 */
	@Id
	@Column(name = "PNL")
	public java.lang.String getPNL() {
		return PNL;
	}

	/**
	 * @param PNL
	 */
	public void setPNL(java.lang.String PNL) {
		this.PNL = PNL;
	}

	/**
	 * @return NN_NO
	 */
	@Column(name = "N_NO")
	public java.lang.String getNN_NO() {
		return NN_NO;
	}

	/**
	 * @param NN_NO
	 */
	public void setNN_NO(java.lang.String NN_NO) {
		this.NN_NO = NN_NO;
	}

	/**
	 * @return NAME
	 */
	@Column(name = "NAME")
	public java.lang.String getNAME() {
		return NAME;
	}

	/**
	 * @param NAME
	 */
	public void setNAME(java.lang.String NAME) {
		this.NAME = NAME;
	}

	/**
	 * @return IMARK
	 */
	@Column(name = "IMARK")
	public java.lang.String getIMARK() {
		return IMARK;
	}

	/**
	 * @param IMARK
	 */
	public void setIMARK(java.lang.String IMARK) {
		this.IMARK = IMARK;
	}

	/**
	 * @return ZZB
	 */
	@Column(name = "ZB")
	public java.lang.String getZZB() {
		return ZZB;
	}

	/**
	 * @param ZZB
	 */
	public void setZZB(java.lang.String ZZB) {
		this.ZZB = ZZB;
	}

	/**
	 * @return ZZW
	 */
	@Column(name = "ZW")
	public java.lang.String getZZW() {
		return ZZW;
	}

	/**
	 * @param ZZW
	 */
	public void setZZW(java.lang.String ZZW) {
		this.ZZW = ZZW;
	}

	/**
	 * @return JB
	 */
	@Column(name = "JB")
	public java.lang.String getJB() {
		return JB;
	}

	/**
	 * @param JB
	 */
	public void setJB(java.lang.String JB) {
		this.JB = JB;
	}

	/**
	 * @return OPE
	 */
	@Column(name = "OPE")
	public java.lang.String getOPE() {
		return OPE;
	}

	/**
	 * @param OPE
	 */
	public void setOPE(java.lang.String OPE) {
		this.OPE = OPE;
	}

	/**
	 * @return IDNO
	 */
	@Column(name = "IDNO")
	public java.lang.String getIDNO() {
		return IDNO;
	}

	/**
	 * @param IDNO
	 */
	public void setIDNO(java.lang.String IDNO) {
		this.IDNO = IDNO;
	}

	/**
	 * @return PROV
	 */
	@Column(name = "PROV")
	public java.lang.String getPROV() {
		return PROV;
	}

	/**
	 * @param PROV
	 */
	public void setPROV(java.lang.String PROV) {
		this.PROV = PROV;
	}

	/**
	 * @return BDATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "BDATE")
	public java.util.Date getBDATE() {
		return BDATE;
	}

	/**
	 * @param BDATE
	 */
	public void setBDATE(java.util.Date BDATE) {
		this.BDATE = BDATE;
	}

	/**
	 * @return MALE
	 */
	@Column(name = "MALE")
	public java.lang.String getMALE() {
		return MALE;
	}

	/**
	 * @param MALE
	 */
	public void setMALE(java.lang.String MALE) {
		this.MALE = MALE;
	}

	/**
	 * @return EDCA
	 */
	@Column(name = "EDCA")
	public java.lang.String getEDCA() {
		return EDCA;
	}

	/**
	 * @param EDCA
	 */
	public void setEDCA(java.lang.String EDCA) {
		this.EDCA = EDCA;
	}

	/**
	 * @return MARRY
	 */
	@Column(name = "MARRY")
	public java.lang.String getMARRY() {
		return MARRY;
	}

	/**
	 * @param MARRY
	 */
	public void setMARRY(java.lang.String MARRY) {
		this.MARRY = MARRY;
	}

	/**
	 * @return IN_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "IN_DATE")
	public java.util.Date getIN_DATE() {
		return IN_DATE;
	}

	/**
	 * @param IN_DATE
	 */
	public void setIN_DATE(java.util.Date IN_DATE) {
		this.IN_DATE = IN_DATE;
	}

	/**
	 * @return ADS
	 */
	@Column(name = "ADS")
	public java.lang.String getADS() {
		return ADS;
	}

	/**
	 * @param ADS
	 */
	public void setADS(java.lang.String ADS) {
		this.ADS = ADS;
	}

	/**
	 * @return POSTNUM
	 */
	@Column(name = "POSTNUM")
	public java.lang.String getPOSTNUM() {
		return POSTNUM;
	}

	/**
	 * @param POSTNUM
	 */
	public void setPOSTNUM(java.lang.String POSTNUM) {
		this.POSTNUM = POSTNUM;
	}

	/**
	 * @return TEL
	 */
	@Column(name = "TEL")
	public java.lang.String getTEL() {
		return TEL;
	}

	/**
	 * @param TEL
	 */
	public void setTEL(java.lang.String TEL) {
		this.TEL = TEL;
	}

	/**
	 * @return HH_NO
	 */
	@Column(name = "H_NO")
	public java.lang.String getHH_NO() {
		return HH_NO;
	}

	/**
	 * @param HH_NO
	 */
	public void setHH_NO(java.lang.String HH_NO) {
		this.HH_NO = HH_NO;
	}

	/**
	 * @return JS_NAME
	 */
	@Column(name = "JS_NAME")
	public java.lang.String getJS_NAME() {
		return JS_NAME;
	}

	/**
	 * @param JS_NAME
	 */
	public void setJS_NAME(java.lang.String JS_NAME) {
		this.JS_NAME = JS_NAME;
	}

	/**
	 * @return GGS
	 */
	@Column(name = "GS")
	public java.lang.String getGGS() {
		return GGS;
	}

	/**
	 * @param GGS
	 */
	public void setGGS(java.lang.String GGS) {
		this.GGS = GGS;
	}

	/**
	 * @return UNDEL
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "UNDEL")
	public java.util.Date getUNDEL() {
		return UNDEL;
	}

	/**
	 * @param UNDEL
	 */
	public void setUNDEL(java.util.Date UNDEL) {
		this.UNDEL = UNDEL;
	}

	/**
	 * @return OUSER
	 */
	@Column(name = "OUSER")
	public java.lang.String getOUSER() {
		return OUSER;
	}

	/**
	 * @param OUSER
	 */
	public void setOUSER(java.lang.String OUSER) {
		this.OUSER = OUSER;
	}

	/**
	 * @return SAVEOLD
	 */
	@Column(name = "SAVEOLD")
	public java.lang.String getSAVEOLD() {
		return SAVEOLD;
	}

	/**
	 * @param SAVEOLD
	 */
	public void setSAVEOLD(java.lang.String SAVEOLD) {
		this.SAVEOLD = SAVEOLD;
	}

	/**
	 * @return TURN_D
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "TURN_D")
	public java.util.Date getTURN_D() {
		return TURN_D;
	}

	/**
	 * @param TURN_D
	 */
	public void setTURN_D(java.util.Date TURN_D) {
		this.TURN_D = TURN_D;
	}

	/**
	 * @return BEG_D
	 */
	@Column(name = "BEG_D")
	public java.lang.String getBEG_D() {
		return BEG_D;
	}

	/**
	 * @param BEG_D
	 */
	public void setBEG_D(java.lang.String BEG_D) {
		this.BEG_D = BEG_D;
	}

	/**
	 * @return END_D
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "END_D")
	public java.util.Date getEND_D() {
		return END_D;
	}

	/**
	 * @param END_D
	 */
	public void setEND_D(java.util.Date END_D) {
		this.END_D = END_D;
	}

	/**
	 * @return UP_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	public java.util.Date getUP_DATE() {
		return UP_DATE;
	}

	/**
	 * @param UP_DATE
	 */
	public void setUP_DATE(java.util.Date UP_DATE) {
		this.UP_DATE = UP_DATE;
	}

	/**
	 * @return DEL_MK
	 */
	@Column(name = "DEL_MK")
	public java.lang.String getDEL_MK() {
		return DEL_MK;
	}

	/**
	 * @param DEL_MK
	 */
	public void setDEL_MK(java.lang.String DEL_MK) {
		this.DEL_MK = DEL_MK;
	}

	/**
	 * @return DELSEN_MK
	 */
	@Column(name = "DELSEN_MK")
	public java.lang.String getDELSEN_MK() {
		return DELSEN_MK;
	}

	/**
	 * @param DELSEN_MK
	 */
	public void setDELSEN_MK(java.lang.String DELSEN_MK) {
		this.DELSEN_MK = DELSEN_MK;
	}

	/**
	 * @return OUTDATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "OUTDATE")
	public java.util.Date getOUTDATE() {
		return OUTDATE;
	}

	/**
	 * @param OUTDATE
	 */
	public void setOUTDATE(java.util.Date OUTDATE) {
		this.OUTDATE = OUTDATE;
	}

	/**
	 * @return CLAS
	 */
	@Column(name = "CLAS")
	public java.lang.String getCLAS() {
		return CLAS;
	}

	/**
	 * @param CLAS
	 */
	public void setCLAS(java.lang.String CLAS) {
		this.CLAS = CLAS;
	}

	/**
	 * @return ODATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ODATE")
	public java.util.Date getODATE() {
		return ODATE;
	}

	/**
	 * @param ODATE
	 */
	public void setODATE(java.util.Date ODATE) {
		this.ODATE = ODATE;
	}

	/**
	 * @return TTM
	 */
	@Column(name = "TM")
	public java.lang.String getTTM() {
		return TTM;
	}

	/**
	 * @param TTM
	 */
	public void setTTM(java.lang.String TTM) {
		this.TTM = TTM;
	}

	/**
	 * @return SAVE_NO
	 */
	@Column(name = "SAVE_NO")
	public java.lang.String getSAVE_NO() {
		return SAVE_NO;
	}

	/**
	 * @param SAVE_NO
	 */
	public void setSAVE_NO(java.lang.String SAVE_NO) {
		this.SAVE_NO = SAVE_NO;
	}

	/**
	 * @return FIN_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "FIN_DATE")
	public java.util.Date getFIN_DATE() {
		return FIN_DATE;
	}

	/**
	 * @param FIN_DATE
	 */
	public void setFIN_DATE(java.util.Date FIN_DATE) {
		this.FIN_DATE = FIN_DATE;
	}

	/**
	 * @return WIN_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "WIN_DATE")
	public java.util.Date getWIN_DATE() {
		return WIN_DATE;
	}

	/**
	 * @param WIN_DATE
	 */
	public void setWIN_DATE(java.util.Date WIN_DATE) {
		this.WIN_DATE = WIN_DATE;
	}

	/**
	 * @return PP_DEP
	 */
	@Column(name = "P_DEP")
	public java.lang.String getPP_DEP() {
		return PP_DEP;
	}

	/**
	 * @param PP_DEP
	 */
	public void setPP_DEP(java.lang.String PP_DEP) {
		this.PP_DEP = PP_DEP;
	}

	/**
	 * @return PHONE
	 */
	@Column(name = "PHONE")
	public java.lang.String getPHONE() {
		return PHONE;
	}

	/**
	 * @param PHONE
	 */
	public void setPHONE(java.lang.String PHONE) {
		this.PHONE = PHONE;
	}

	/**
	 * @return PERSON
	 */
	@Column(name = "PERSON")
	public java.lang.String getPERSON() {
		return PERSON;
	}

	/**
	 * @param PERSON
	 */
	public void setPERSON(java.lang.String PERSON) {
		this.PERSON = PERSON;
	}

	/**
	 * @return ZY_NUMBER
	 */
	@Column(name = "ZY_NUMBER")
	public java.lang.String getZY_NUMBER() {
		return ZY_NUMBER;
	}

	/**
	 * @param ZY_NUMBER
	 */
	public void setZY_NUMBER(java.lang.String ZY_NUMBER) {
		this.ZY_NUMBER = ZY_NUMBER;
	}

	/**
	 * @return MMZ
	 */
	@Column(name = "MZ")
	public java.lang.String getMMZ() {
		return MMZ;
	}

	/**
	 * @param MMZ
	 */
	public void setMMZ(java.lang.String MMZ) {
		this.MMZ = MMZ;
	}

	/**
	 * @return JS_NAME1
	 */
	@Column(name = "JS_NAME1")
	public java.lang.String getJS_NAME1() {
		return JS_NAME1;
	}

	/**
	 * @param JS_NAME1
	 */
	public void setJS_NAME1(java.lang.String JS_NAME1) {
		this.JS_NAME1 = JS_NAME1;
	}

	/**
	 * @return FIX_CODE
	 */
	@Column(name = "FIX_CODE")
	public java.lang.String getFIX_CODE() {
		return FIX_CODE;
	}

	/**
	 * @param FIX_CODE
	 */
	public void setFIX_CODE(java.lang.String FIX_CODE) {
		this.FIX_CODE = FIX_CODE;
	}

	/**
	 * @return PB_FANO
	 */
	@Column(name = "PB_FANO")
	public java.lang.String getPB_FANO() {
		return PB_FANO;
	}

	/**
	 * @param PB_FANO
	 */
	public void setPB_FANO(java.lang.String PB_FANO) {
		this.PB_FANO = PB_FANO;
	}

	/**
	 * @return SY_B_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "SY_B_DATE")
	public java.util.Date getSY_B_DATE() {
		return SY_B_DATE;
	}

	/**
	 * @param SY_B_DATE
	 */
	public void setSY_B_DATE(java.util.Date SY_B_DATE) {
		this.SY_B_DATE = SY_B_DATE;
	}

	/**
	 * @return SY_E_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "SY_E_DATE")
	public java.util.Date getSY_E_DATE() {
		return SY_E_DATE;
	}

	/**
	 * @param SY_E_DATE
	 */
	public void setSY_E_DATE(java.util.Date SY_E_DATE) {
		this.SY_E_DATE = SY_E_DATE;
	}

	/**
	 * @return SSH
	 */
	@Column(name = "SH")
	public java.lang.String getSSH() {
		return SSH;
	}

	/**
	 * @param SSH
	 */
	public void setSSH(java.lang.String SSH) {
		this.SSH = SSH;
	}

	/**
	 * @return OLDPNL
	 */
	@Column(name = "OLDPNL")
	public java.lang.String getOLDPNL() {
		return OLDPNL;
	}

	/**
	 * @param OLDPNL
	 */
	public void setOLDPNL(java.lang.String OLDPNL) {
		this.OLDPNL = OLDPNL;
	}

	/**
	 * @return FAC
	 */
	@Column(name = "FAC")
	public java.lang.String getFAC() {
		return FAC;
	}

	/**
	 * @param FAC
	 */
	public void setFAC(java.lang.String FAC) {
		this.FAC = FAC;
	}

	/**
	 * @return ADS_NOW
	 */
	@Column(name = "ADS_NOW")
	public java.lang.String getADS_NOW() {
		return ADS_NOW;
	}

	/**
	 * @param ADS_NOW
	 */
	public void setADS_NOW(java.lang.String ADS_NOW) {
		this.ADS_NOW = ADS_NOW;
	}

	/**
	 * @return IS_DOCT
	 */
	@Column(name = "IS_DOCT")
	public java.lang.String getIS_DOCT() {
		return IS_DOCT;
	}

	/**
	 * @param IS_DOCT
	 */
	public void setIS_DOCT(java.lang.String IS_DOCT) {
		this.IS_DOCT = IS_DOCT;
	}

	/**
	 * @return PHONEME
	 */
	@Column(name = "PHONEME")
	public java.lang.String getPHONEME() {
		return PHONEME;
	}

	/**
	 * @param PHONEME
	 */
	public void setPHONEME(java.lang.String PHONEME) {
		this.PHONEME = PHONEME;
	}

	/**
	 * @return OO_NAME
	 */
	@Column(name = "O_NAME")
	public java.lang.String getOO_NAME() {
		return OO_NAME;
	}

	/**
	 * @param OO_NAME
	 */
	public void setOO_NAME(java.lang.String OO_NAME) {
		this.OO_NAME = OO_NAME;
	}

	/**
	 * @return XZB
	 */
	@Column(name = "XZB")
	public java.lang.String getXZB() {
		return XZB;
	}

	/**
	 * @param XZB
	 */
	public void setXZB(java.lang.String XZB) {
		this.XZB = XZB;
	}

	/**
	 * @return CARD_NO
	 */
	@Column(name = "CARD_NO")
	public java.lang.String getCARD_NO() {
		return CARD_NO;
	}

	/**
	 * @param CARD_NO
	 */
	public void setCARD_NO(java.lang.String CARD_NO) {
		this.CARD_NO = CARD_NO;
	}

	/**
	 * @return ISOK
	 */
	@Column(name = "ISOK")
	public java.lang.String getISOK() {
		return ISOK;
	}

	/**
	 * @param ISOK
	 */
	public void setISOK(java.lang.String ISOK) {
		this.ISOK = ISOK;
	}

	/**
	 * @return OPEC
	 */
	@Column(name = "OPEC")
	public java.lang.String getOPEC() {
		return OPEC;
	}

	/**
	 * @param OPEC
	 */
	public void setOPEC(java.lang.String OPEC) {
		this.OPEC = OPEC;
	}

	/**
	 * @return HOUSING
	 */
	@Column(name = "HOUSING")
	public java.lang.String getHOUSING() {
		return HOUSING;
	}

	/**
	 * @param HOUSING
	 */
	public void setHOUSING(java.lang.String HOUSING) {
		this.HOUSING = HOUSING;
	}

	/**
	 * @return HOUSE_NO
	 */
	@Column(name = "HOUSE_NO")
	public java.lang.String getHOUSE_NO() {
		return HOUSE_NO;
	}

	/**
	 * @param HOUSE_NO
	 */
	public void setHOUSE_NO(java.lang.String HOUSE_NO) {
		this.HOUSE_NO = HOUSE_NO;
	}

	/**
	 * @return KKT
	 */
	@Column(name = "KT")
	public java.lang.String getKKT() {
		return KKT;
	}

	/**
	 * @param KKT
	 */
	public void setKKT(java.lang.String KKT) {
		this.KKT = KKT;
	}

	/**
	 * @return TAXCARD
	 */
	@Column(name = "TAXCARD")
	public java.lang.String getTAXCARD() {
		return TAXCARD;
	}

	/**
	 * @param TAXCARD
	 */
	public void setTAXCARD(java.lang.String TAXCARD) {
		this.TAXCARD = TAXCARD;
	}

	/**
	 * @return CON_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "CON_DATE")
	public java.util.Date getCON_DATE() {
		return CON_DATE;
	}

	/**
	 * @param CON_DATE
	 */
	public void setCON_DATE(java.util.Date CON_DATE) {
		this.CON_DATE = CON_DATE;
	}

	/**
	 * @return WORKERTY
	 */
	@Column(name = "WORKERTY")
	public java.lang.String getWORKERTY() {
		return WORKERTY;
	}

	/**
	 * @param WORKERTY
	 */
	public void setWORKERTY(java.lang.String WORKERTY) {
		this.WORKERTY = WORKERTY;
	}

	/**
	 * @return RELI_NO
	 */
	@Column(name = "RELI_NO")
	public java.lang.String getRELI_NO() {
		return RELI_NO;
	}

	/**
	 * @param RELI_NO
	 */
	public void setRELI_NO(java.lang.String RELI_NO) {
		this.RELI_NO = RELI_NO;
	}

	/**
	 * @return TAX_NO
	 */
	@Column(name = "TAX_NO")
	public java.lang.String getTAX_NO() {
		return TAX_NO;
	}

	/**
	 * @param TAX_NO
	 */
	public void setTAX_NO(java.lang.String TAX_NO) {
		this.TAX_NO = TAX_NO;
	}

	/**
	 * @return ATTRI
	 */
	@Column(name = "ATTRI")
	public java.lang.String getATTRI() {
		return ATTRI;
	}

	/**
	 * @param ATTRI
	 */
	public void setATTRI(java.lang.String ATTRI) {
		this.ATTRI = ATTRI;
	}

	/**
	 * @return LONGT
	 */
	@Column(name = "LONGT")
	public java.lang.String getLONGT() {
		return LONGT;
	}

	/**
	 * @param LONGT
	 */
	public void setLONGT(java.lang.String LONGT) {
		this.LONGT = LONGT;
	}

	/**
	 * @return IS_RCB
	 */
	@Column(name = "IS_RCB")
	public java.lang.String getIS_RCB() {
		return IS_RCB;
	}

	/**
	 * @param IS_RCB
	 */
	public void setIS_RCB(java.lang.String IS_RCB) {
		this.IS_RCB = IS_RCB;
	}

	/**
	 * @return BONUSB
	 */
	@Column(name = "BONUSB")
	public java.lang.Long getBONUSB() {
		return BONUSB;
	}

	/**
	 * @param BONUSB
	 */
	public void setBONUSB(java.lang.Long BONUSB) {
		this.BONUSB = BONUSB;
	}

	/**
	 * @return MBS_FREE
	 */
	@Column(name = "MBS_FREE")
	public java.lang.String getMBS_FREE() {
		return MBS_FREE;
	}

	/**
	 * @param MBS_FREE
	 */
	public void setMBS_FREE(java.lang.String MBS_FREE) {
		this.MBS_FREE = MBS_FREE;
	}

	/**
	 * @return BIRTH_P
	 */
	@Column(name = "BIRTH_P")
	public java.lang.String getBIRTH_P() {
		return BIRTH_P;
	}

	/**
	 * @param BIRTH_P
	 */
	public void setBIRTH_P(java.lang.String BIRTH_P) {
		this.BIRTH_P = BIRTH_P;
	}

	/**
	 * @return RCB_F
	 */
	@Column(name = "RCB_F")
	public java.lang.String getRCB_F() {
		return RCB_F;
	}

	/**
	 * @param RCB_F
	 */
	public void setRCB_F(java.lang.String RCB_F) {
		this.RCB_F = RCB_F;
	}

	/**
	 * @return GROU
	 */
	@Column(name = "GROU")
	public java.lang.String getGROU() {
		return GROU;
	}

	/**
	 * @param GROU
	 */
	public void setGROU(java.lang.String GROU) {
		this.GROU = GROU;
	}

	/**
	 * @return LZ_NO
	 */
	@Column(name = "LZ_NO")
	public java.lang.String getLZ_NO() {
		return LZ_NO;
	}

	/**
	 * @param LZ_NO
	 */
	public void setLZ_NO(java.lang.String LZ_NO) {
		this.LZ_NO = LZ_NO;
	}

	/**
	 * @return DELDATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DELDATE")
	public java.util.Date getDELDATE() {
		return DELDATE;
	}

	/**
	 * @param DELDATE
	 */
	public void setDELDATE(java.util.Date DELDATE) {
		this.DELDATE = DELDATE;
	}

	/**
	 * @return MM_NM
	 */
	@Column(name = "MM_NM")
	public java.lang.String getMM_NM() {
		return MM_NM;
	}

	/**
	 * @param MM_NM
	 */
	public void setMM_NM(java.lang.String MM_NM) {
		this.MM_NM = MM_NM;
	}

	/**
	 * @return UNEM
	 */
	@Column(name = "UNEM")
	public java.lang.String getUNEM() {
		return UNEM;
	}

	/**
	 * @param UNEM
	 */
	public void setUNEM(java.lang.String UNEM) {
		this.UNEM = UNEM;
	}

	/**
	 * @return GROUP_ID
	 */
	@Column(name = "GROUP_ID")
	public java.lang.String getGROUP_ID() {
		return GROUP_ID;
	}

	/**
	 * @param GROUP_ID
	 */
	public void setGROUP_ID(java.lang.String GROUP_ID) {
		this.GROUP_ID = GROUP_ID;
	}

	/**
	 * @return TRIAL_FLAG
	 */
	@Column(name = "TRIAL_FLAG")
	public java.lang.String getTRIAL_FLAG() {
		return TRIAL_FLAG;
	}

	/**
	 * @param TRIAL_FLAG
	 */
	public void setTRIAL_FLAG(java.lang.String TRIAL_FLAG) {
		this.TRIAL_FLAG = TRIAL_FLAG;
	}

	/**
	 * @return IDNO_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "IDNO_DATE")
	public java.util.Date getIDNO_DATE() {
		return IDNO_DATE;
	}

	/**
	 * @param IDNO_DATE
	 */
	public void setIDNO_DATE(java.util.Date IDNO_DATE) {
		this.IDNO_DATE = IDNO_DATE;
	}

	/**
	 * @return DOCT_NO
	 */
	@Column(name = "DOCT_NO")
	public java.lang.String getDOCT_NO() {
		return DOCT_NO;
	}

	/**
	 * @param DOCT_NO
	 */
	public void setDOCT_NO(java.lang.String DOCT_NO) {
		this.DOCT_NO = DOCT_NO;
	}

	/**
	 * @return TRIAL
	 */
	@Column(name = "TRIAL")
	public java.lang.Long getTRIAL() {
		return TRIAL;
	}

	/**
	 * @param TRIAL
	 */
	public void setTRIAL(java.lang.Long TRIAL) {
		this.TRIAL = TRIAL;
	}

	/**
	 * @return SALARY_1
	 */
	@Column(name = "SALARY_1")
	public java.lang.Long getSALARY_1() {
		return SALARY_1;
	}

	/**
	 * @param SALARY_1
	 */
	public void setSALARY_1(java.lang.Long SALARY_1) {
		this.SALARY_1 = SALARY_1;
	}

	/**
	 * @return SALARY_2
	 */
	@Column(name = "SALARY_2")
	public java.lang.Long getSALARY_2() {
		return SALARY_2;
	}

	/**
	 * @param SALARY_2
	 */
	public void setSALARY_2(java.lang.Long SALARY_2) {
		this.SALARY_2 = SALARY_2;
	}

	/**
	 * @return WNO
	 */
	@Column(name = "WNO")
	public java.lang.String getWNO() {
		return WNO;
	}

	/**
	 * @param WNO
	 */
	public void setWNO(java.lang.String WNO) {
		this.WNO = WNO;
	}

	/**
	 * @return SOUTDATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "SOUTDATE")
	public java.util.Date getSOUTDATE() {
		return SOUTDATE;
	}

	/**
	 * @param SOUTDATE
	 */
	public void setSOUTDATE(java.util.Date SOUTDATE) {
		this.SOUTDATE = SOUTDATE;
	}

	/**
	 * @return II_ZWJ
	 */
	@Column(name = "I_ZWJ")
	public java.lang.Long getII_ZWJ() {
		return II_ZWJ;
	}

	/**
	 * @param II_ZWJ
	 */
	public void setII_ZWJ(java.lang.Long II_ZWJ) {
		this.II_ZWJ = II_ZWJ;
	}

	/**
	 * @return TAXNO_DATE
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "TAXNO_DATE")
	public java.util.Date getTAXNO_DATE() {
		return TAXNO_DATE;
	}

	/**
	 * @param TAXNO_DATE
	 */
	public void setTAXNO_DATE(java.util.Date TAXNO_DATE) {
		this.TAXNO_DATE = TAXNO_DATE;
	}
}
