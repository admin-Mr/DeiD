package ds.dsid.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "DSID05")
public class DSID05 {
	private String UNIQUEID;
	private String OD_NO;
	private String MT_CODE;
	private String MT_PONO;
	private String MT_SEQ;
	private String EL_NO;
	private String MODEL_NA;
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
	public String getOD_NO() {
		return OD_NO;
	}
	public void setOD_NO(String oD_NO) {
		OD_NO = oD_NO;
	}
	public String getMT_CODE() {
		return MT_CODE;
	}
	public void setMT_CODE(String mT_CODE) {
		MT_CODE = mT_CODE;
	}
	public String getMT_PONO() {
		return MT_PONO;
	}
	public void setMT_PONO(String mT_PONO) {
		MT_PONO = mT_PONO;
	}
	public String getMT_SEQ() {
		return MT_SEQ;
	}
	public void setMT_SEQ(String mT_SEQ) {
		MT_SEQ = mT_SEQ;
	}
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
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
	

}
