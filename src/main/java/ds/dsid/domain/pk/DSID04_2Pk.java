package ds.dsid.domain.pk;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang.builder.*;

@Embeddable
public class DSID04_2Pk implements Serializable {
    private java.lang.String MODEL_NA;	//型體
    private java.lang.String EL_SEQ;	//項次
    public DSID04_2Pk() {
        super();
    }
    public DSID04_2Pk(java.lang.String MODEL_NA, java.lang.String EL_SEQ) {
        super();
        this.MODEL_NA = MODEL_NA;
        this.EL_SEQ = EL_SEQ;
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
     * 取得項次
     * @returnEL_SEQ 項次
     */
    @Column(name = "EL_SEQ", length = 4)
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
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID04_2Pk))
            return false;
        DSID04_2Pk castOther = (DSID04_2Pk) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(EL_SEQ, castOther.EL_SEQ).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(EL_SEQ).toHashCode();
    }
}
