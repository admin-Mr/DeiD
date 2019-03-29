package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID06N_PK implements Serializable {
	private static final long serialVersionUID = 1L;
    private String MODEL_NA;	
    private String ITEM;
    
	public DSID06N_PK(String mODEL_NA, String iTEM) {
		super();
		this.MODEL_NA = mODEL_NA;
		this.ITEM = iTEM;
	}
	public DSID06N_PK() {
		super();
	}
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	} 
	
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	@Column(name = "ITEM")
	public String getITEM() {
		return ITEM;
	}
	public void setITEM(String iTEM) {
		ITEM = iTEM;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID06N_PK))
            return false;
        DSID06N_PK castOther = (DSID06N_PK) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(ITEM, castOther.ITEM).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(ITEM).toHashCode();
    }
    
	
}
