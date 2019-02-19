package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ds.dsid.domain.pk.DSID21_Pk;

@Entity
@Table(name = "DSID21")
@IdClass(DSID21_Pk.class)
public class DSID21 {

	private String MODEL_NA;	//型體名稱
	private String ITEMS;		//序號
	private String TYPE;		//類型
	private String GR_NO;		//部位
	private String GR_NA;		//部位名稱
	private String COLOR;		//顏色
	private String EL_NO;		//材料編號
	private String EL_NA;		//材料名稱
	private String SIZE_FD;		//Size分段
	private String NOTE	;		//備註
	private Date IMPORT_DATE;	//匯入日期
	private String UP_USER;		//異動人員
	private Date UP_DATE;		//異動日期
	
	@Id
	@Column(name = "MODEL_NAS")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Id
	@Column(name = "ITEMS")
	public String getITEMS() {
		return ITEMS;
	}
	public void setITEMS(String iTEMS) {
		ITEMS = iTEMS;
	}
	
	@Column(name = "TYPE")
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	
	@Column(name = "GR_NO")
	public String getGR_NO() {
		return GR_NO;
	}
	public void setGR_NO(String gR_NO) {
		GR_NO = gR_NO;
	}
	
	@Column(name = "GR_NA")
	public String getGR_NA() {
		return GR_NA;
	}
	public void setGR_NA(String gR_NA) {
		GR_NA = gR_NA;
	}
	
	@Column(name = "COLOR")
	public String getCOLOR() {
		return COLOR;
	}
	public void setCOLOR(String cOLOR) {
		COLOR = cOLOR;
	}
	
	@Column(name = "EL_NO")
	public String getEL_NO() {
		return EL_NO;
	}
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}
	
	@Column(name = "EL_NA")
	public String getEL_NA() {
		return EL_NA;
	}
	public void setEL_NA(String eL_NA) {
		EL_NA = eL_NA;
	}
	
	@Column(name = "SIZE_FD")
	public String getSIZE_FD() {
		return SIZE_FD;
	}
	public void setSIZE_FD(String sIZE_FD) {
		SIZE_FD = sIZE_FD;
	}
	
	@Column(name = "NOTE")
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	
	@Column(name = "IMPORT_DATE")
	public Date getIMPORT_DATE() {
		return IMPORT_DATE;
	}
	public void setIMPORT_DATE(Date iMPORT_DATE) {
		IMPORT_DATE = iMPORT_DATE;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	@Column(name = "UP_DATE")
	@Temporal(TemporalType.DATE)
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
	
}
