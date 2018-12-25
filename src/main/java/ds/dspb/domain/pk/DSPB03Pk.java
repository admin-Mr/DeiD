package ds.dspb.domain.pk;
import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang.builder.*;
import org.hibernate.validator.*;
@Embeddable
public class DSPB03Pk implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String PB_GROUPID;	//群組代號
    private java.lang.String PB_USERID;	//使用者代號
    public DSPB03Pk() {
        super();
    }
    public DSPB03Pk(java.lang.String PB_GROUPID, java.lang.String PB_USERID) {
        super();
        this.PB_GROUPID = PB_GROUPID;
        this.PB_USERID = PB_USERID;
    }

    /**
     * 取得群組代號
     * @returnPB_GROUPID 群組代號
     */
    @Column(name = "PB_GROUPID", length = 10)
    public java.lang.String getPB_GROUPID() {
        return PB_GROUPID;
    }
    /**
     * 設定群組代號
     * @param PB_GROUPID 群組代號
     */
    public void setPB_GROUPID(java.lang.String PB_GROUPID) {
        this.PB_GROUPID = PB_GROUPID;
    }
    /**
     * 取得使用者代號
     * @returnPB_USERID 使用者代號
     */
    @Column(name = "PB_USERID", length = 10)
    public java.lang.String getPB_USERID() {
        return PB_USERID;
    }
    /**
     * 設定使用者代號
     * @param PB_USERID 使用者代號
     */
    public void setPB_USERID(java.lang.String PB_USERID) {
        this.PB_USERID = PB_USERID;
    }
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSPB03Pk))
            return false;
        DSPB03Pk castOther = (DSPB03Pk) other;
        return new EqualsBuilder().append(PB_GROUPID, castOther.PB_GROUPID).append(PB_USERID, castOther.PB_USERID).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(PB_GROUPID).append(PB_USERID).toHashCode();
    }
}
