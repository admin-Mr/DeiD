package ds.dsid.domain;

import javax.persistence.IdClass;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ds.dsid.domain.pk.DSID04_1Pk;

import javax.persistence.Column;

/**
* 預警報表材料主檔
**/
@IdClass(DSID04_1Pk.class)
@Entity
@Table(name = "DSID04_1")
public class DSID04_1 {
    private java.lang.String MODEL_NA;	//型體
    private java.lang.String EL_SEQ;	//項次
    private java.lang.String EL_NO;	//材料編號
    private java.lang.String GROUP_NO;	//group
    private java.lang.String MT_USAGE;	//Material Usage
    private java.lang.String SU_NA;	//廠商名稱
    private java.lang.String GR_NA;	//部位名稱
    private java.lang.String EL_NA;	//材料名稱
    private java.lang.String COLOR;	//顏色
    private java.util.Date ELLA_DATE;	//材料上線日期
    private java.util.Date ELDR_DATE;	//材料下線日期
    private java.util.Date LA_TIME;	//材料最晚下日期
    private java.lang.String EL_UNIT;	//材料單位
    private java.lang.String MTLDAY;	//廠商生產週期
    private java.lang.String SU_MOQ;	//最小訂購量
    private java.lang.String PO_PRICE;	//單價
    private java.lang.String YIELD;	//一雙鞋用量
    private java.lang.String COLOR_PRE;	//顏色百分比
    private java.lang.String DAILY_UOM;	//每天預計用量
    private java.lang.String SAFE_DAY;	//安全庫存天數
    private java.lang.String MIN_UOM;	//最小庫存值
    private java.lang.String MIN_DAY;	//最小庫存天數
    private java.lang.String MAX_UOM;	//最大庫存值
    private java.lang.String MAX_DAY;	//最大庫存天數
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
     * 取得group
     * @return GROUP_NO group
     */
    @Column(name = "GROUP_NO")
    public java.lang.String getGROUP_NO() {
        return GROUP_NO;
    }
    /**
     * 設定group
     * @param GROUP_NO group
     */
    public void setGROUP_NO(java.lang.String GROUP_NO) {
        this.GROUP_NO = GROUP_NO;
    }
    /**
     * 取得Material Usage
     * @return MT_USAGE Material Usage
     */
    @Column(name = "MT_USAGE")
    public java.lang.String getMT_USAGE() {
        return MT_USAGE;
    }
    /**
     * 設定Material Usage
     * @param MT_USAGE Material Usage
     */
    public void setMT_USAGE(java.lang.String MT_USAGE) {
        this.MT_USAGE = MT_USAGE;
    }
    /**
     * 取得廠商名稱
     * @return SU_NA 廠商名稱
     */
    @Column(name = "SU_NA")
    public java.lang.String getSU_NA() {
        return SU_NA;
    }
    /**
     * 設定廠商名稱
     * @param SU_NA 廠商名稱
     */
    public void setSU_NA(java.lang.String SU_NA) {
        this.SU_NA = SU_NA;
    }
    /**
     * 取得部位名稱
     * @return GR_NA 部位名稱
     */
    @Column(name = "GR_NA")
    public java.lang.String getGR_NA() {
        return GR_NA;
    }
    /**
     * 設定部位名稱
     * @param GR_NA 部位名稱
     */
    public void setGR_NA(java.lang.String GR_NA) {
        this.GR_NA = GR_NA;
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
     * 取得材料上線日期
     * @return ELLA_DATE 材料上線日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "ELLA_DATE")
    public java.util.Date getELLA_DATE() {
        return ELLA_DATE;
    }
    /**
     * 設定材料上線日期
     * @param ELLA_DATE 材料上線日期
     */
    public void setELLA_DATE(java.util.Date ELLA_DATE) {
        this.ELLA_DATE = ELLA_DATE;
    }
    /**
     * 取得材料下線日期
     * @return ELDR_DATE 材料下線日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "ELDR_DATE")
    public java.util.Date getELDR_DATE() {
        return ELDR_DATE;
    }
    /**
     * 設定材料下線日期
     * @param ELDR_DATE 材料下線日期
     */
    public void setELDR_DATE(java.util.Date ELDR_DATE) {
        this.ELDR_DATE = ELDR_DATE;
    }
    /**
     * 取得材料最晚下日期
     * @return LA_TIME 材料最晚下日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "LA_TIME")
    public java.util.Date getLA_TIME() {
        return LA_TIME;
    }
    /**
     * 設定材料最晚下日期
     * @param LA_TIME 材料最晚下日期
     */
    public void setLA_TIME(java.util.Date LA_TIME) {
        this.LA_TIME = LA_TIME;
    }
    /**
     * 取得材料單位
     * @return EL_UNIT 材料單位
     */
    @Column(name = "EL_UNIT")
    public java.lang.String getEL_UNIT() {
        return EL_UNIT;
    }
    /**
     * 設定材料單位
     * @param EL_UNIT 材料單位
     */
    public void setEL_UNIT(java.lang.String EL_UNIT) {
        this.EL_UNIT = EL_UNIT;
    }
    /**
     * 取得廠商生產週期
     * @return MTLDAY 廠商生產週期
     */
    @Column(name = "MTLDAY")
    public java.lang.String getMTLDAY() {
        return MTLDAY;
    }
    /**
     * 設定廠商生產週期
     * @param MTLDAY 廠商生產週期
     */
    public void setMTLDAY(java.lang.String MTLDAY) {
        this.MTLDAY = MTLDAY;
    }
    /**
     * 取得最小訂購量
     * @return SU_MOQ 最小訂購量
     */
    @Column(name = "SU_MOQ")
    public java.lang.String getSU_MOQ() {
        return SU_MOQ;
    }
    /**
     * 設定最小訂購量
     * @param SU_MOQ 最小訂購量
     */
    public void setSU_MOQ(java.lang.String SU_MOQ) {
        this.SU_MOQ = SU_MOQ;
    }
    /**
     * 取得單價
     * @return PO_PRICE 單價
     */
    @Column(name = "PO_PRICE")
    public java.lang.String getPO_PRICE() {
        return PO_PRICE;
    }
    /**
     * 設定單價
     * @param PO_PRICE 單價
     */
    public void setPO_PRICE(java.lang.String PO_PRICE) {
        this.PO_PRICE = PO_PRICE;
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
     * 取得安全庫存天數
     * @return SAFE_DATE 安全庫存天數
     */
    @Column(name = "SAFE_DAY")
    public java.lang.String getSAFE_DAY() {
        return SAFE_DAY;
    }
    /**
     * 設定安全庫存天數
     * @param SAFE_DATE 安全庫存天數
     */
    public void setSAFE_DAY(java.lang.String SAFE_DAY) {
        this.SAFE_DAY = SAFE_DAY;
    }
    /**
     * 取得最小庫存值
     * @return MIN_UOM 最小庫存值
     */
    @Column(name = "MIN_UOM")
    public java.lang.String getMIN_UOM() {
        return MIN_UOM;
    }
    /**
     * 設定最小庫存值
     * @param MIN_UOM 最小庫存值
     */
    public void setMIN_UOM(java.lang.String MIN_UOM) {
        this.MIN_UOM = MIN_UOM;
    }
    /**
     * 取得最小庫存天數
     * @return MIN_DATE 最小庫存天數
     */
    @Column(name = "MIN_DAY")
    public java.lang.String getMIN_DAY() {
        return MIN_DAY;
    }
    /**
     * 設定最小庫存天數
     * @param MIN_DATE 最小庫存天數
     */
    public void setMIN_DAY(java.lang.String MIN_DAY) {
        this.MIN_DAY = MIN_DAY;
    }
    /**
     * 取得最大庫存值
     * @return MAX_UOM 最大庫存值
     */
    @Column(name = "MAX_UOM")
    public java.lang.String getMAX_UOM() {
        return MAX_UOM;
    }
    /**
     * 設定最大庫存值
     * @param MAX_UOM 最大庫存值
     */
    public void setMAX_UOM(java.lang.String MAX_UOM) {
        this.MAX_UOM = MAX_UOM;
    }
    /**
     * 取得最大庫存天數
     * @return MAX_DATE 最大庫存天數
     */
    @Column(name = "MAX_DAY")
    public java.lang.String getMAX_DAY() {
        return MAX_DAY;
    }
    /**
     * 設定最大庫存天數
     * @param MAX_DATE 最大庫存天數
     */
    public void setMAX_DAY(java.lang.String MAX_DAY) {
        this.MAX_DAY = MAX_DAY;
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
