package ds.dsid.domain;

import javax.persistence.IdClass;
import ds.dsid.domain.pk.DSID04_1Pk;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;

/**
* 預警報表前面片材料檔
**/
@IdClass(DSID04_1Pk.class)
@Entity
@Table(name = "DSID04_2")
public class DSID04_2 {
    private java.lang.String MODEL_NA;	//型體
    private java.lang.String EL_SEQ;	//項次
    private java.lang.String COLOR;	//顏色
    private java.lang.String EL_NO;	//材料編號
    private java.lang.String EL_NA;	//材料名稱
    private java.lang.String YIELD;	//一雙鞋用量
    private java.lang.String COLOR_PRE;	//顏色百分比
    private java.lang.String DAILY_UOM;	//每天預計用量
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
     * 取得項次
     * @return EL_SEQ 項次
     */
    @Id
    @Column(name = "EL_SEQ")
    public java.lang.String getEL_SEQ() {
        return EL_SEQ;
    }
    /**
     * 設定項次
     * @param EL_SEQ 項次
     */
    public void setEL_SEQ(java.lang.String EL_SEQ) {
        this.EL_SEQ = EL_SEQ;
    }
    /**
     * 取得顏色
     * @return COLOR 顏色
     */
    @Column(name = "COLOR")
    public java.lang.String getCOLOR() {
        return COLOR;
    }
    /**
     * 設定顏色
     * @param COLOR 顏色
     */
    public void setCOLOR(java.lang.String COLOR) {
        this.COLOR = COLOR;
    }
    /**
     * 取得材料編號
     * @return EL_NO 材料編號
     */
    @Column(name = "EL_NO")
    public java.lang.String getEL_NO() {
        return EL_NO;
    }
    /**
     * 設定材料編號
     * @param EL_NO 材料編號
     */
    public void setEL_NO(java.lang.String EL_NO) {
        this.EL_NO = EL_NO;
    }
    /**
     * 取得材料名稱
     * @return EL_NA 材料名稱
     */
    @Column(name = "EL_NA")
    public java.lang.String getEL_NA() {
        return EL_NA;
    }
    /**
     * 設定材料名稱
     * @param EL_NA 材料名稱
     */
    public void setEL_NA(java.lang.String EL_NA) {
        this.EL_NA = EL_NA;
    }
    /**
     * 取得一雙鞋用量
     * @return YIELD 一雙鞋用量
     */
    @Column(name = "YIELD")
    public java.lang.String getYIELD() {
        return YIELD;
    }
    /**
     * 設定一雙鞋用量
     * @param YIELD 一雙鞋用量
     */
    public void setYIELD(java.lang.String YIELD) {
        this.YIELD = YIELD;
    }
    /**
     * 取得顏色百分比
     * @return COLOR_PRE 顏色百分比
     */
    @Column(name = "COLOR_PRE")
    public java.lang.String getCOLOR_PRE() {
        return COLOR_PRE;
    }
    /**
     * 設定顏色百分比
     * @param COLOR_PRE 顏色百分比
     */
    public void setCOLOR_PRE(java.lang.String COLOR_PRE) {
        this.COLOR_PRE = COLOR_PRE;
    }
    /**
     * 取得每天預計用量
     * @return DAILY_UOM 每天預計用量
     */
    @Column(name = "DAILY_UOM")
    public java.lang.String getDAILY_UOM() {
        return DAILY_UOM;
    }
    /**
     * 設定每天預計用量
     * @param DAILY_UOM 每天預計用量
     */
    public void setDAILY_UOM(java.lang.String DAILY_UOM) {
        this.DAILY_UOM = DAILY_UOM;
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
