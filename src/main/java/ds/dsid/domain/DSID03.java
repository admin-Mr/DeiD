package ds.dsid.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DSID03")
public class DSID03 {
	//Lgc Grp
	private String PART_NAME;
    //Material Usage
	private String MU;
	//Fct Unique ID (FUI)
	private String FU_ID;
	// Vendor
	private String VENDOR;
	// Component Name
	private String CP_NAME;
	//  Material Name (included size breaks if any)
	private String EL_NAME;
	//Color Code
	private String COLOR_CODE;
	// 異動日期
	private String UP_DATE;
	// 創建日期
	private String CREATE_DATE;
	// 創建人員
	private String CREATE_USER;
	// 使用狀態
	private String STATUS;
	//形體model
	private String MODEL_NAME;
	//用量Yield
	private String YIELD;
	// uniqueid
	private String UNIQUEID;
	
	public String getUNIQUEID() {
		return UNIQUEID;
	}
	public void setUNIQUEID(String uNIQUEID) {
		UNIQUEID = uNIQUEID;
	}
	@Column(name = "PART_NAME")
	public String getPART_NAME() {
		return PART_NAME;
	}
	public void setPART_NAME(String pART_NAME) {
		PART_NAME = pART_NAME;
	}
	
	@Column(name = "MU")
	public String getMU() {
		return MU;
	}
	public void setMU(String mU) {
		MU = mU;
	}
	
	@Id
	@Column(name = "FU_ID")
	public String getFU_ID() {
		return FU_ID;
	}
	public void setFU_ID(String fU_ID) {
		FU_ID = fU_ID;
	}
	
	@Column(name = "VENDOR")
	public String getVENDOR() {
		return VENDOR;
	}
	public void setVENDOR(String vENDOR) {
		VENDOR = vENDOR;
	}
	
	@Column(name = "CP_NAME")
	public String getCP_NAME() {
		return CP_NAME;
	}
	public void setCP_NAME(String cP_NAME) {
		CP_NAME = cP_NAME;
	}
	
	@Column(name = "EL_NAME")
	public String getEL_NAME() {
		return EL_NAME;
	}
	public void setEL_NAME(String eL_NAME) {
		EL_NAME = eL_NAME;
	}
	
	@Column(name = "COLOR_CODE")
	public String getCOLOR_CODE() {
		return COLOR_CODE;
	}
	public void setCOLOR_CODE(String cOLOR_CODE) {
		COLOR_CODE = cOLOR_CODE;
	}
	
	@Column(name = "UP_DATE")
	public String getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(String uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
	@Column(name = "CREATE_DATE")
	public String getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(String cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
	
	@Column(name = "CREATE_USER")
	public String getCREATE_USER() {
		return CREATE_USER;
	}
	public void setCREATE_USER(String cREATE_USER) {
		CREATE_USER = cREATE_USER;
	}
	
	@Column(name = "STATUS")
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	@Column(name = "MODEL_NAME")
	public String getMODEL_NAME() {
		return MODEL_NAME;
	}
	public void setMODEL_NAME(String mODEL_NAME) {
		MODEL_NAME = mODEL_NAME;
	}
	@Column(name = "YIELD")
	public String getYIELD() {
		return YIELD;
	}
	public void setYIELD(String yIELD) {
		YIELD = yIELD;
	}
	
	
	
	
}
