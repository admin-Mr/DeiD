package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID01_TEMP_PK implements Serializable{

	private static final long serialVersionUID = 1L;
    private java.util.Date ORDER_DATE;		//接單日期
    private String WORK_ORDER_ID;	//客戶訂單ID
    public DSID01_TEMP_PK() {
        super();
    }
    
    public DSID01_TEMP_PK(Date ORDER_DATE, String WORK_ORDER_ID) {
        super();
        this.ORDER_DATE = ORDER_DATE;
        this.WORK_ORDER_ID = WORK_ORDER_ID;
    }

    @Column(name = "ORDER_DATE", length = 6)
	public Date getORDER_DATE() {
		return ORDER_DATE;
	}

	public void setORDER_DATE(Date oRDER_DATE) {
		ORDER_DATE = oRDER_DATE;
	}

	@Column(name = "WORK_ORDER_ID", length = 6)
	public String getWORK_ORDER_ID() {
		return WORK_ORDER_ID;
	}

	public void setWORK_ORDER_ID(String wORK_ORDER_ID) {
		WORK_ORDER_ID = wORK_ORDER_ID;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID01_TEMP_PK))
            return false;
        DSID01_TEMP_PK castOther = (DSID01_TEMP_PK) other;
        return new EqualsBuilder().append(ORDER_DATE, castOther.ORDER_DATE).append(WORK_ORDER_ID, castOther.WORK_ORDER_ID).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ORDER_DATE).append(WORK_ORDER_ID).toHashCode();
    }
    
}
