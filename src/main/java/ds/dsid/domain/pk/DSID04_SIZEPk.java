package ds.dsid.domain.pk;
import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang.builder.*;
import org.hibernate.validator.*;
@Embeddable
public class DSID04_SIZEPk implements Serializable {
    private java.lang.String MODEL_NA;	//型體
    private java.lang.String SH_LAST;	//楦寬
    private java.lang.String EL_NO;	//材料編號
    public DSID04_SIZEPk() {
        super();
    }
    public DSID04_SIZEPk(java.lang.String MODEL_NA, java.lang.String SH_LAST, java.lang.String EL_NO) {
        super();
        this.MODEL_NA = MODEL_NA;
        this.SH_LAST = SH_LAST;
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
     * 取得楦寬
     * @return SH_LAST 楦寬
     */
    @Column(name = "SH_LAST", length = 20)
    public java.lang.String getSH_LAST() {
        return SH_LAST;
    }
    /**
     * 設定楦寬
     * @param SH_LAST 楦寬
     */
    public void setSH_LAST(java.lang.String SH_LAST) {
        this.SH_LAST = SH_LAST;
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
        if (!(other instanceof DSID04_SIZEPk))
            return false;
        DSID04_SIZEPk castOther = (DSID04_SIZEPk) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(SH_LAST, castOther.SH_LAST).append(EL_NO, castOther.EL_NO).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(SH_LAST).append(EL_NO).toHashCode();
    }
}
