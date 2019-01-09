package ds.dsid.domain.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSID25_LOG_PK implements Serializable {
	private static final long serialVersionUID = 1L;
    private String MODEL_NA;	
    private String EL_NO;
    private String GROUP_NO;	
    private Date RE_DATE;
    
	public DSID25_LOG_PK(String mODEL_NA, String eL_NO) {
		super();
		this.MODEL_NA = mODEL_NA;
		this.EL_NO = eL_NO;
	}
	public DSID25_LOG_PK() {
		super();
	}
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	} 
	
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "GROUP_NO")
	public String getGROUP_NO() {
		return GROUP_NO;
	}
	public void setGROUP_NO(String gROUP_NO) {
		GROUP_NO = gROUP_NO;
	}
	
	@Column(name = "EL_NO")
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "RE_DATE")
	public Date getRE_DATE() {
		return RE_DATE;
	}
	public void setRE_DATE(Date rE_DATE) {
		RE_DATE = rE_DATE;
	}
	
	@Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSID25_LOG_PK))
            return false;
        DSID25_LOG_PK castOther = (DSID25_LOG_PK) other;
        return new EqualsBuilder().append(MODEL_NA, castOther.MODEL_NA).append(EL_NO, castOther.EL_NO)
        		.append(GROUP_NO, castOther.GROUP_NO).append(RE_DATE, castOther.RE_DATE).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(MODEL_NA).append(EL_NO).append(GROUP_NO).append(RE_DATE).toHashCode();
    }
    
	
}
