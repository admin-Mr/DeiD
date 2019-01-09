package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID25_PK implements Serializable {
	private static final long serialVersionUID = 1L;
    private String MODEL_NA;	
    private String EL_NO;
    
	public DSID25_PK(String mODEL_NA, String eL_NO) {
		super();
		this.MODEL_NA = mODEL_NA;
		this.EL_NO = eL_NO;
	}
	public DSID25_PK() {
		super();
	}
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	} 
	
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	@Column(name = "EL_NO")
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID25_PK))
            return false;
        DSID25_PK castOther = (DSID25_PK) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(EL_NO, castOther.EL_NO).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(EL_NO).toHashCode();
    }
    
	
}
