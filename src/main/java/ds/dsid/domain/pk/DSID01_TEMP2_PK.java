package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID01_TEMP2_PK implements Serializable{

	private static final long serialVersionUID = 1L;
    private String WORK_ORDER_ID;	//客戶訂單ID
    private String SEQ;
    public DSID01_TEMP2_PK() {
        super();
    }
    
    public DSID01_TEMP2_PK(String SEQ, String WORK_ORDER_ID) {
        super();
        this.SEQ = SEQ;
        this.WORK_ORDER_ID = WORK_ORDER_ID;
    }

    @Column(name = "SEQ")
	public String getSEQ() {
		return SEQ;
	}

	public void setSEQ(String sEQ) {
		SEQ = sEQ;
	}

	@Column(name = "WORK_ORDER_ID")
	public String getWORK_ORDER_ID() {
		return WORK_ORDER_ID;
	}

	public void setWORK_ORDER_ID(String wORK_ORDER_ID) {
		WORK_ORDER_ID = wORK_ORDER_ID;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID01_TEMP2_PK))
            return false;
        DSID01_TEMP2_PK castOther = (DSID01_TEMP2_PK) other;
        return new EqualsBuilder().append(SEQ, castOther.SEQ).append(WORK_ORDER_ID, castOther.WORK_ORDER_ID).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(SEQ).append(WORK_ORDER_ID).toHashCode();
    }
    
}
