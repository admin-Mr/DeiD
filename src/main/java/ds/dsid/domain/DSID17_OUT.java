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
@Table(name = "DSID17_OUT")
public class DSID17_OUT {
	private String OUT_NO ;
	private String ADH_ELNO ;
	private String OUT_QTY ;
	private String MT_PONO ;
	private String MT_SEQ ;
	private String UP_USER ;
	private Date UP_DATE ;
	
	@Id
	@Column(name = "OUT_NO")
	public String getOUT_NO() {
		return OUT_NO;
	}
	public void setOUT_NO(String oUT_NO) {
		OUT_NO = oUT_NO;
	}

	@Column(name = "ADH_ELNO")
	public String getADH_ELNO() {
		return ADH_ELNO;
	}
	public void setADH_ELNO(String aDH_ELNO) {
		ADH_ELNO = aDH_ELNO;
	}
	
	@Column(name = "OUT_QTY")
	public String getOUT_QTY() {
		return OUT_QTY;
	}
	public void setOUT_QTY(String oUT_QTY) {
		OUT_QTY = oUT_QTY;
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
	
	@Column(name = "MT_PONO")
	public String getMT_PONO() {
		return MT_PONO;
	}
	public void setMT_PONO(String mT_PONO) {
		MT_PONO = mT_PONO;
	}
	
	@Column(name = "MT_SEQ")
	public String getMT_SEQ() {
		return MT_SEQ;
	}
	public void setMT_SEQ(String mT_SEQ) {
		MT_SEQ = mT_SEQ;
	}

}
