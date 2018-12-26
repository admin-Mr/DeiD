package ds.dsid.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "DSIDN08")
public class DSIDN08 {
	private String UNIQUEID;//
	private String PO_NO;
	private String PO_SEQ;
	private String EL_NO;
	private String EL_CNAME;
	private Double PO_QTY;
	private String PC_QTY;
	private Date PC_REDATE = new Date();
	private String UP_DATE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());
	private Date CREATEDATE = new Date();
	private String UP_USER;
	private String DAY_NO;
	
	private boolean ISEDIT;
	private boolean ISADD;
	
	
	public String getDAY_NO() {
		return DAY_NO;
	}
	public void setDAY_NO(String dAY_NO) {
		DAY_NO = dAY_NO;
	}
	@Transient
	public boolean isISEDIT() {
		return ISEDIT;
	}
	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}
	@Transient
	public boolean isISADD() {
		return ISADD;
	}
	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPC_REDATE() {
		return PC_REDATE;
	}
	public void setPC_REDATE(Date pC_REDATE) {
		PC_REDATE = pC_REDATE;
	}
	public String getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(String uP_DATE) {
		UP_DATE = uP_DATE;
	}
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	public String getEL_CNAME() {
		return EL_CNAME;
	}
	public void setEL_CNAME(String eL_CNAME) {
		EL_CNAME = eL_CNAME;
	}
	 @Id
	// @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_DSIDN08")
	// @SequenceGenerator(name="seq_DSIDN08",sequenceName="seq_DSIDN08",allocationSize=1)
	public String getUNIQUEID() {
		return UNIQUEID;
	}
	public void setUNIQUEID(String uNIQUEID) {
		UNIQUEID = uNIQUEID;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCREATEDATE() {
		return CREATEDATE;
	}
	public void setCREATEDATE(Date cREATEDATE) {
		CREATEDATE = cREATEDATE;
	}
	

}
