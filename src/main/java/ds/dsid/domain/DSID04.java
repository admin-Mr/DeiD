package ds.dsid.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;
/**
* 預警報表型體主檔
**/
@Entity
@Table(name = "DSID04")
public class DSID04 {
    private java.lang.String MODEL_NA;	//型體
    private java.util.Date LA_DATE;	//型體上線日期
    private java.util.Date DR_DATE;	//型體下線日期
    private java.lang.String MODEL_UPD;	//型體UPD
    private java.lang.String VAMP_UPD;	//前面片UPD
    private java.lang.String IS_DROP;	//是否已下線
    private java.lang.String UP_USER;	//更新人員
    private java.util.Date UP_DATE;	//更新日期
    /**
     * 取得型體
     * @return MODEL_NA 型體
     */
    @Id
    @Column(name = "MODEL_NA")
    public java.lang.String getMODEL_NA() {
        return MODEL_NA;
    }
    /**
     * 設定型體
     * @param MODEL_NA 型體
     */
    public void setMODEL_NA(java.lang.String MODEL_NA) {
        this.MODEL_NA = MODEL_NA;
    }
    /**
     * 取得型體上線日期
     * @return LA_DATE 型體上線日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "LA_DATE")
    public java.util.Date getLA_DATE() {
        return LA_DATE;
    }
    /**
     * 設定型體上線日期
     * @param LA_DATE 型體上線日期
     */
    public void setLA_DATE(java.util.Date LA_DATE) {
        this.LA_DATE = LA_DATE;
    }
    /**
     * 取得型體下線日期
     * @return DR_DATE 型體下線日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DR_DATE")
    public java.util.Date getDR_DATE() {
        return DR_DATE;
    }
    /**
     * 設定型體下線日期
     * @param DR_DATE 型體下線日期
     */
    public void setDR_DATE(java.util.Date DR_DATE) {
        this.DR_DATE = DR_DATE;
    }
    /**
     * 取得型體UPD
     * @return MODEL_UPD 型體UPD
     */
    @Column(name = "MODEL_UPD")
    public java.lang.String getMODEL_UPD() {
        return MODEL_UPD;
    }
    /**
     * 設定型體UPD
     * @param MODEL_UPD 型體UPD
     */
    public void setMODEL_UPD(java.lang.String MODEL_UPD) {
        this.MODEL_UPD = MODEL_UPD;
    }
    /**
     * 取得前面片UPD
     * @return VAMP_UPD 前面片UPD
     */
    @Column(name = "VAMP_UPD")
    public java.lang.String getVAMP_UPD() {
        return VAMP_UPD;
    }
    /**
     * 設定前面片UPD
     * @param VAMP_UPD 前面片UPD
     */
    public void setVAMP_UPD(java.lang.String VAMP_UPD) {
        this.VAMP_UPD = VAMP_UPD;
    }
    /**
     * 取得是否已下線
     * @return IS_DROP 是否已下線
     */
    @Column(name = "IS_DROP")
    public java.lang.String getIS_DROP() {
        return IS_DROP;
    }
    /**
     * 設定是否已下線
     * @param IS_DROP 是否已下線
     */
    public void setIS_DROP(java.lang.String IS_DROP) {
        this.IS_DROP = IS_DROP;
    }
    /**
     * 取得更新人員
     * @return UP_USER 更新人員
     */
    @Column(name = "UP_USER")
    public java.lang.String getUP_USER() {
        return UP_USER;
    }
    /**
     * 設定更新人員
     * @param UP_USER 更新人員
     */
    public void setUP_USER(java.lang.String UP_USER) {
        this.UP_USER = UP_USER;
    }
    /**
     * 取得更新日期
     * @return UP_DATE 更新日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "UP_DATE")
    public java.util.Date getUP_DATE() {
        return UP_DATE;
    }
    /**
     * 設定更新日期
     * @param UP_DATE 更新日期
     */
    public void setUP_DATE(java.util.Date UP_DATE) {
        this.UP_DATE = UP_DATE;
    }
}
