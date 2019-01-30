package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DSID17_ACP")
public class DSID17_ACP {
	
	private String AC_NO ;
	private String OT_NO ;
	private String ADH_ELNO ;
	private String AC_QTY ;
	
	private String UP_USER ;
	private Date UP_DATE ;
	
	@Id
	@Column(name = "AC_NO")
	public String getAC_NO() {
		return AC_NO;
	}
	public void setAC_NO(String aC_NO) {
		AC_NO = aC_NO;
	}
	
	@Column(name = "AC_QTY")
	public String getAC_QTY() {
		return AC_QTY;
	}
	public void setAC_QTY(String aC_QTY) {
		AC_QTY = aC_QTY;
	}
	
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
