package ds.dsid.domain;
/**
 * DSID65 實體類 字段少於數據庫表格字段.
 * 如需使用請添加需要字段
 */
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ds.dsid.domain.pk.DSID04_1Pk;
import ds.dsid.domain.pk.DSID65_Pk;

@Entity(name = "DSID65")
@Table(name = "DSID65")
@IdClass(DSID65_Pk.class)
public class DSID65 {

	private String DP_NO;
	private String DP_SEQ;
	private String WORK_ORDER_ID;
	private String PG_DATE;
	private String MODEL_NA;
	private String DECIDE_DATE;
	private String SEWING_DATE;
	private String FORMING_DATE;
	private String BOTTOM_TIME;
	private String SCAN_DATE;
	private String REPAIR_TYPE;
	private String FORMING_OK_DATE;
	
	@Id
	@Column(name = "WORK_ORDER_ID")
	public String getDP_NO() {
		return DP_NO;
	}
	
	public void setDP_NO(String dP_NO) {
		DP_NO = dP_NO;
	}
	
	@Id
	@Column(name = "WORK_ORDER_ID")
	public String getDP_SEQ() {
		return DP_SEQ;
	}
	public void setDP_SEQ(String dP_SEQ) {
		DP_SEQ = dP_SEQ;
	}
	
	@Column(name = "WORK_ORDER_ID")
	public String getWORK_ORDER_ID() {
		return WORK_ORDER_ID;
	}
	public void setWORK_ORDER_ID(String wORK_ORDER_ID) {
		WORK_ORDER_ID = wORK_ORDER_ID;
	}
	
	@Column(name = "PG_DATE")
	public String getPG_DATE() {
		return PG_DATE;
	}
	public void setPG_DATE(String pG_DATE) {
		PG_DATE = pG_DATE;
	}
	
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "DECIDE_DATE")
	public String getDECIDE_DATE() {
		return DECIDE_DATE;
	}
	public void setDECIDE_DATE(String dECIDE_DATE) {
		DECIDE_DATE = dECIDE_DATE;
	}
	
	@Column(name = "SEWING_DATE")
	public String getSEWING_DATE() {
		return SEWING_DATE;
	}
	public void setSEWING_DATE(String sEWING_DATE) {
		SEWING_DATE = sEWING_DATE;
	}
	
	@Column(name = "FORMING_DATE")
	public String getFORMING_DATE() {
		return FORMING_DATE;
	}
	public void setFORMING_DATE(String fORMING_DATE) {
		FORMING_DATE = fORMING_DATE;
	}
	
	@Column(name = "BOTTOM_TIME")
	public String getBOTTOM_TIME() {
		return BOTTOM_TIME;
	}
	public void setBOTTOM_TIME(String bOTTOM_TIME) {
		BOTTOM_TIME = bOTTOM_TIME;
	}
	
	@Column(name = "SCAN_DATE")
	public String getSCAN_DATE() {
		return SCAN_DATE;
	}
	public void setSCAN_DATE(String sCAN_DATE) {
		SCAN_DATE = sCAN_DATE;
	}
	
	@Column(name = "REPAIR_TYPE")
	public String getREPAIR_TYPE() {
		return REPAIR_TYPE;
	}
	public void setREPAIR_TYPE(String rEPAIR_TYPE) {
		REPAIR_TYPE = rEPAIR_TYPE;
	}
	
	@Column(name = "FORMING_OK_DATE")
	public String getFORMING_OK_DATE() {
		return FORMING_OK_DATE;
	}
	public void setFORMING_OK_DATE(String fORMING_OK_DATE) {
		FORMING_OK_DATE = fORMING_OK_DATE;
	}
	
}
