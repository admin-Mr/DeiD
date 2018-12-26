package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID10_1PK implements Serializable{

	private static final long serialVersionUID = 1L;
    private String NIKE_SH_ARITCLE;	
    private String SEQ;
    public DSID10_1PK() {
        super();
    }
    
    public DSID10_1PK(String SEQ, String NIKE_SH_ARITCLE) {
        super();
        this.SEQ = SEQ;
        this.NIKE_SH_ARITCLE = NIKE_SH_ARITCLE;
    }

    @Column(name = "SEQ")
	public String getSEQ() {
		return SEQ;
	}

	public void setSEQ(String sEQ) {
		SEQ = sEQ;
	}

	@Column(name = "NIKE_SH_ARITCLE")
	public String getNIKE_SH_ARITCLE() {
		return NIKE_SH_ARITCLE;
	}

	public void setNIKE_SH_ARITCLE(String nIKE_SH_ARITCLE) {
		NIKE_SH_ARITCLE = nIKE_SH_ARITCLE;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID10_1PK))
            return false;
        DSID10_1PK castOther = (DSID10_1PK) other;
        return new EqualsBuilder().append(SEQ, castOther.SEQ).append(NIKE_SH_ARITCLE, castOther.NIKE_SH_ARITCLE).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(SEQ).append(NIKE_SH_ARITCLE).toHashCode();
    }
    
}
