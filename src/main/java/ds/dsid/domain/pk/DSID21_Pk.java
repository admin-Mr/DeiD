package ds.dsid.domain.pk;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DSID21_Pk  implements Serializable{



	private String MODEL_NA;		//型體
	private String ITEMS;		
	
	public DSID21_Pk() {
        super();
    }
	
    public DSID21_Pk(String MODEL_NA, String ITEMS) {
        super();
        
        this.MODEL_NA = MODEL_NA;
        this.ITEMS = ITEMS;
    }
    
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		this.MODEL_NA = mODEL_NA;
	}

	@Column(name = "ITEMS")
	public String getITEMS() {
		return ITEMS;
	}
	public void setITEMS(String iTEMS) {
		this.ITEMS = iTEMS;
	}
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID21_Pk))
            return false;
        DSID21_Pk castOther = (DSID21_Pk) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(ITEMS, castOther.ITEMS).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(ITEMS).toHashCode();
    }
    

	
}
