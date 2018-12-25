package ds.dspb.domain.pk;
import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang.builder.*;
import org.hibernate.validator.*;
@Embeddable
public class DSPB01_GRPPk implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String PB_ID;
    private java.lang.String PB_GROUPID;
    public DSPB01_GRPPk() {
        super();
    }
    public DSPB01_GRPPk(java.lang.String PB_ID, java.lang.String PB_GROUPID) {
        super();
        this.PB_ID = PB_ID;
        this.PB_GROUPID = PB_GROUPID;
    }

    /**
     * @returnPB_ID 
     */
    @Column(name = "PB_ID", length = 15)
    public java.lang.String getPB_ID() {
        return PB_ID;
    }
    /**
     * @param PB_ID 
     */
    public void setPB_ID(java.lang.String PB_ID) {
        this.PB_ID = PB_ID;
    }
    /**
     * @returnPB_GROUPID 
     */
    @Column(name = "PB_GROUPID", length = 20)
    public java.lang.String getPB_GROUPID() {
        return PB_GROUPID;
    }
    /**
     * @param PB_GROUPID 
     */
    public void setPB_GROUPID(java.lang.String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSPB01_GRPPk))
            return false;
        DSPB01_GRPPk castOther = (DSPB01_GRPPk) other;
        return new EqualsBuilder().append(PB_ID, castOther.PB_ID).append(PB_GROUPID, castOther.PB_GROUPID).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(PB_ID).append(PB_GROUPID).toHashCode();
    }
}
