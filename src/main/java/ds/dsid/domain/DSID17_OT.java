package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DSID17_OT")
public class DSID17_OT {
	private String OT_NO ;
	private String ADH_ELNO ;
	private String OT_QTY ;
	private String UP_USER ;
	private Date UP_DATE ;
	
	@Id
	@Column(name = "OT_NO")
	public String getOT_NO() {
		return OT_NO;
	}
	public void setOT_NO(String oT_NO) {
		OT_NO = oT_NO;
	}

	@Column(name = "ADH_ELNO")
	public String getADH_ELNO() {
		return ADH_ELNO;
	}
	public void setADH_ELNO(String aDH_ELNO) {
		ADH_ELNO = aDH_ELNO;
	}
	
	@Column(name = "OT_QTY")
	public String getOT_QTY() {
		return OT_QTY;
	}
	public void setOT_QTY(String oT_QTY) {
		OT_QTY = oT_QTY;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}

	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}

}
