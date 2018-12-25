package ds.dshr.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;

@Entity
@Table(name = "DSHR19")
public class DSHR19 {
    private java.lang.Long COEFF;
    private java.lang.String COST_KIND;
    private java.lang.String DEPT_NO;
    private java.lang.String DESC_CH;
    private java.lang.String DESC_VN;
    private java.lang.String FANO_P;
    private java.lang.String FA_NO;
    private java.lang.String IS_DIS;
    private java.lang.String JJB;
    private java.lang.String KQN;
    private java.lang.String OLDSEC;
    private java.lang.String PID;
    private java.lang.String PROCID;
    private java.lang.String REMARK;
    private java.lang.String SEC;
    private java.lang.String SECD;
    private java.lang.String SEC_FA;
    private java.lang.String SEC_NM;
    private java.lang.String SEC_NMC;
    private java.lang.String SUBJ_KIND;
    private java.util.Date UP_DATE;
    private java.lang.String UP_USER;
    private java.lang.String YYN;
    private java.lang.String ZZZ;
    /**
     * @return COEFF 
     */
    @Column(name = "COEFF")
    public java.lang.Long getCOEFF() {
        return COEFF;
    }
    /**
     * @param COEFF 
     */
    public void setCOEFF(java.lang.Long COEFF) {
        this.COEFF = COEFF;
    }
    /**
     * @return COST_KIND 
     */
    @Column(name = "COST_KIND")
    public java.lang.String getCOST_KIND() {
        return COST_KIND;
    }
    /**
     * @param COST_KIND 
     */
    public void setCOST_KIND(java.lang.String COST_KIND) {
        this.COST_KIND = COST_KIND;
    }
    /**
     * @return DEPT_NO 
     */
    @Column(name = "DEPT_NO")
    public java.lang.String getDEPT_NO() {
        return DEPT_NO;
    }
    /**
     * @param DEPT_NO 
     */
    public void setDEPT_NO(java.lang.String DEPT_NO) {
        this.DEPT_NO = DEPT_NO;
    }
    /**
     * @return DESC_CH 
     */
    @Column(name = "DESC_CH")
    public java.lang.String getDESC_CH() {
        return DESC_CH;
    }
    /**
     * @param DESC_CH 
     */
    public void setDESC_CH(java.lang.String DESC_CH) {
        this.DESC_CH = DESC_CH;
    }
    /**
     * @return DESC_VN 
     */
    @Column(name = "DESC_VN")
    public java.lang.String getDESC_VN() {
        return DESC_VN;
    }
    /**
     * @param DESC_VN 
     */
    public void setDESC_VN(java.lang.String DESC_VN) {
        this.DESC_VN = DESC_VN;
    }
    /**
     * @return FANO_P 
     */
    @Column(name = "FANO_P")
    public java.lang.String getFANO_P() {
        return FANO_P;
    }
    /**
     * @param FANO_P 
     */
    public void setFANO_P(java.lang.String FANO_P) {
        this.FANO_P = FANO_P;
    }
    /**
     * @return FA_NO 
     */
    @Column(name = "FA_NO")
    public java.lang.String getFA_NO() {
        return FA_NO;
    }
    /**
     * @param FA_NO 
     */
    public void setFA_NO(java.lang.String FA_NO) {
        this.FA_NO = FA_NO;
    }
    /**
     * @return IS_DIS 
     */
    @Column(name = "IS_DIS")
    public java.lang.String getIS_DIS() {
        return IS_DIS;
    }
    /**
     * @param IS_DIS 
     */
    public void setIS_DIS(java.lang.String IS_DIS) {
        this.IS_DIS = IS_DIS;
    }
    /**
     * @return JJB 
     */
    @Column(name = "JB")
    public java.lang.String getJJB() {
        return JJB;
    }
    /**
     * @param JJB 
     */
    public void setJJB(java.lang.String JJB) {
        this.JJB = JJB;
    }
    /**
     * @return KQN 
     */
    @Column(name = "KQN")
    public java.lang.String getKQN() {
        return KQN;
    }
    /**
     * @param KQN 
     */
    public void setKQN(java.lang.String KQN) {
        this.KQN = KQN;
    }
    /**
     * @return OLDSEC 
     */
    @Column(name = "OLDSEC")
    public java.lang.String getOLDSEC() {
        return OLDSEC;
    }
    /**
     * @param OLDSEC 
     */
    public void setOLDSEC(java.lang.String OLDSEC) {
        this.OLDSEC = OLDSEC;
    }
    /**
     * @return PID 
     */
    @Column(name = "PID")
    public java.lang.String getPID() {
        return PID;
    }
    /**
     * @param PID 
     */
    public void setPID(java.lang.String PID) {
        this.PID = PID;
    }
    /**
     * @return PROCID 
     */
    @Column(name = "PROCID")
    public java.lang.String getPROCID() {
        return PROCID;
    }
    /**
     * @param PROCID 
     */
    public void setPROCID(java.lang.String PROCID) {
        this.PROCID = PROCID;
    }
    /**
     * @return REMARK 
     */
    @Column(name = "REMARK")
    public java.lang.String getREMARK() {
        return REMARK;
    }
    /**
     * @param REMARK 
     */
    public void setREMARK(java.lang.String REMARK) {
        this.REMARK = REMARK;
    }
    /**
     * @return SEC 
     */
    @Id
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
     * @return SECD 
     */
    @Column(name = "SECD")
    public java.lang.String getSECD() {
        return SECD;
    }
    /**
     * @param SECD 
     */
    public void setSECD(java.lang.String SECD) {
        this.SECD = SECD;
    }
    /**
     * @return SEC_FA 
     */
    @Column(name = "SEC_FA")
    public java.lang.String getSEC_FA() {
        return SEC_FA;
    }
    /**
     * @param SEC_FA 
     */
    public void setSEC_FA(java.lang.String SEC_FA) {
        this.SEC_FA = SEC_FA;
    }
    /**
     * @return SEC_NM 
     */
    @Column(name = "SEC_NM")
    public java.lang.String getSEC_NM() {
        return SEC_NM;
    }
    /**
     * @param SEC_NM 
     */
    public void setSEC_NM(java.lang.String SEC_NM) {
        this.SEC_NM = SEC_NM;
    }
    /**
     * @return SEC_NMC 
     */
    @Column(name = "SEC_NMC")
    public java.lang.String getSEC_NMC() {
        return SEC_NMC;
    }
    /**
     * @param SEC_NMC 
     */
    public void setSEC_NMC(java.lang.String SEC_NMC) {
        this.SEC_NMC = SEC_NMC;
    }
    /**
     * @return SUBJ_KIND 
     */
    @Column(name = "SUBJ_KIND")
    public java.lang.String getSUBJ_KIND() {
        return SUBJ_KIND;
    }
    /**
     * @param SUBJ_KIND 
     */
    public void setSUBJ_KIND(java.lang.String SUBJ_KIND) {
        this.SUBJ_KIND = SUBJ_KIND;
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
     * @return UP_USER 
     */
    @Column(name = "UP_USER")
    public java.lang.String getUP_USER() {
        return UP_USER;
    }
    /**
     * @param UP_USER 
     */
    public void setUP_USER(java.lang.String UP_USER) {
        this.UP_USER = UP_USER;
    }
    /**
     * @return YYN 
     */
    @Column(name = "YN")
    public java.lang.String getYYN() {
        return YYN;
    }
    /**
     * @param YYN 
     */
    public void setYYN(java.lang.String YYN) {
        this.YYN = YYN;
    }
    /**
     * @return ZZZ 
     */
    @Column(name = "ZZ")
    public java.lang.String getZZZ() {
        return ZZZ;
    }
    /**
     * @param ZZZ 
     */
    public void setZZZ(java.lang.String ZZZ) {
        this.ZZZ = ZZZ;
    }
}
