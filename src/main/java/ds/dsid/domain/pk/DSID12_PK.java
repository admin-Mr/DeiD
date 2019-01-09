package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID12_PK implements Serializable {
	private static final long serialVersionUID = 1L;
    private String MODEL_NA;	
    private String SH_STYLENO;
    
	public DSID12_PK(String mODEL_NA, String sH_STYLENO) {
		super();
		this.MODEL_NA = mODEL_NA;
		this.SH_STYLENO = sH_STYLENO;
	}
	public DSID12_PK() {
		super();
	}
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	@Column(name = "SH_STYLENO")
	public String getSH_STYLENO() {
		return SH_STYLENO;
	}
	public void setSH_STYLENO(String sH_STYLENO) {
		SH_STYLENO = sH_STYLENO;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID12_PK))
            return false;
        DSID12_PK castOther = (DSID12_PK) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(SH_STYLENO, castOther.SH_STYLENO).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(SH_STYLENO).toHashCode();
    }
    
	
}
