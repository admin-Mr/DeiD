package ds.dspb.domain.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class DSPB00_NEWPk implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String PB_NO;
	private String PB_SYSID;
	
	public DSPB00_NEWPk(){
		super();
	}
	
	public DSPB00_NEWPk(String PB_NO, String PB_SYSID){
		super();
		this.PB_NO = PB_NO;
		this.PB_SYSID = PB_SYSID;
	}
	
	@Column(name = "PB_NO")
	public String getPB_NO(){
		return this.PB_NO;
	}
	
	public void setPB_NO(String PB_NO){
		this.PB_NO = PB_NO;
	}
	
	@Column(name = "PB_SYSID")
	public String getPB_SYSID(){
		return this.PB_SYSID;
	}
	
	public void setPB_SYSID(String PB_SYSID){
		this.PB_SYSID = PB_SYSID;
	}
	
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DSPB00_NEWPk))
            return false;
        DSPB00_NEWPk castOther = (DSPB00_NEWPk) other;
        return new EqualsBuilder().append(PB_NO, castOther.PB_NO).append(PB_SYSID, castOther.PB_SYSID).isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(PB_NO).append(PB_SYSID).toHashCode();
    }
}
