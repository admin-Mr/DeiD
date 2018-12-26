package ds.dsid.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

//@Entity
//@Table(name = "DSPO06")
public class DSIDN08_T {
	private String PO_NO;
	private String PO_SEQ;
//	@Transient
	private String EL_NO;
//	@Transient
	private String EL_CNAME;
//	@Transient
	private Double PO_QTY;
//	@Transient
	private String PC_QTY;
	
//	@Id
//	@Column(name = "PO_NO")
	public String getPO_NO() {
		return PO_NO;
	}
	public void setPO_NO(String pO_NO) {
		PO_NO = pO_NO;
	}
	public String getPO_SEQ() {
		return PO_SEQ;
	}
	public void setPO_SEQ(String pO_SEQ) {
		PO_SEQ = pO_SEQ;
	}
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	public Double getPO_QTY() {
		return PO_QTY;
	}
	public void setPO_QTY(Double pO_QTY) {
		PO_QTY = pO_QTY;
	}
	public String getPC_QTY() {
		return PC_QTY;
	}
	public void setPC_QTY(String pC_QTY) {
		PC_QTY = pC_QTY;
	}
	public String getEL_CNAME() {
		return EL_CNAME;
	}
	public void setEL_CNAME(String eL_CNAME) {
		EL_CNAME = eL_CNAME;
	}

}
