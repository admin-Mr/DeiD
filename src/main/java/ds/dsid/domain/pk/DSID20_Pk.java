package ds.dsid.domain.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID20_Pk implements Serializable{

	private String MODEL_NAS;		//型體
	private String SEGM_TYPE;		//分段類型
	
	public DSID20_Pk() {
        super();
    }
	
    public DSID20_Pk(String MODEL_NAS, String SEGM_TYPE) {
        super();
        
        this.MODEL_NAS = MODEL_NAS;
        this.SEGM_TYPE = SEGM_TYPE;
    }
    
	@Column(name = "MODEL_NAS")
	public String getMODEL_NAS() {
		return MODEL_NAS;
	}
	public void setMODEL_NAS(String mODEL_NAS) {
		this.MODEL_NAS = mODEL_NAS;
	}

	@Column(name = "SEGM_TYPE")
	public String getSEGM_TYPE() {
		return SEGM_TYPE;
	}
	public void setSEGM_TYPE(String sEGM_TYPE) {
		this.SEGM_TYPE = sEGM_TYPE;
	}
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID20_Pk))
            return false;
        DSID20_Pk castOther = (DSID20_Pk) other;
        return new EqualsBuilder().append(MODEL_NAS, castOther.MODEL_NAS).append(SEGM_TYPE, castOther.SEGM_TYPE).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NAS).append(SEGM_TYPE).toHashCode();
    }
    
}
