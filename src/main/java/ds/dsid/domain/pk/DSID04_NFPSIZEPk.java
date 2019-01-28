package ds.dsid.domain.pk;
import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang.builder.*;
import org.hibernate.validator.*;
@Embeddable
public class DSID04_NFPSIZEPk implements Serializable {
    private java.lang.String MODEL_NA;	//型體
    private java.lang.String EL_NO;	//型體
    private java.lang.String SEQ;	//型體
    public DSID04_NFPSIZEPk() {
        super();
    }
    public DSID04_NFPSIZEPk(java.lang.String MODEL_NA, java.lang.String SEQ, java.lang.String EL_NO) {
        super();
        this.MODEL_NA = MODEL_NA;
        this.SEQ = SEQ;
        this.EL_NO = EL_NO;
    }

    /**
     * 取得型體
     * @returnMODEL_NA 型體
     */
    @Column(name = "MODEL_NA", length = 50)
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
     * 取得顏色
     * @return SEQ 顏色
     */
    @Column(name = "SEQ", length = 3)
    public java.lang.String getSEQ() {
        return SEQ;
    }
    /**
     * 設定顏色
     * @param COLOR 顏色
     */
    public void setSEQ(java.lang.String SEQ) {
        this.SEQ = SEQ;
    }
    /**
     * 取得材料編號
     * @return EL_NO 材料編號
     */
    @Column(name = "EL_NO", length = 13)
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
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID04_NFPSIZEPk))
            return false;
        DSID04_NFPSIZEPk castOther = (DSID04_NFPSIZEPk) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(SEQ, castOther.SEQ).append(EL_NO, castOther.EL_NO).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(SEQ).append(EL_NO).toHashCode();
    }
}
