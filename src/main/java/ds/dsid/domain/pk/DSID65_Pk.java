package ds.dsid.domain.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID65_Pk implements Serializable {
	
	private String DP_NO;
	private String DP_SEQ;

	public DSID65_Pk() {
        super();
    }

	public DSID65_Pk(String DP_NO, String DP_SEQ) {
        super();
        
        this.DP_NO = DP_NO;
        this.DP_SEQ = DP_SEQ;
    }

	@Column(name = "DP_NO")
	public String getDP_NO() {
		return DP_NO;
	}

	public void setDP_NO(String DP_NO) {
		this.DP_NO = DP_NO;
	}

	@Column(name = "DP_SEQ")
	public String getDP_SEQ() {
		return DP_SEQ;
	}

	public void setDP_SEQ(String DP_SEQ) {
		this.DP_SEQ = DP_SEQ;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof DSID65_Pk))
			return false;
		DSID65_Pk castOther = (DSID65_Pk) other;
		return new EqualsBuilder().append(DP_NO, castOther.DP_NO).append(DP_SEQ, castOther.DP_SEQ)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(DP_NO).append(DP_SEQ).toHashCode();
	}

}
