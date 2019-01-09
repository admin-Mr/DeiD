package ds.dsid.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

public class DSIDN06_01 {
	private String MODEL_NA;
	private String EL_NO;
	private String EL_CNAME;
	private String PLACE;
	private String UP_USER;
	private String CUPBOARD;
	private String NOTE;
	private boolean ISCHECKED;
	
	
	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	/**
	 * @return the uP_USER
	 */
	public String getUP_USER() {
		return UP_USER;
	}

	/**
	 * @param uP_USER the uP_USER to set
	 */
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}

	/**
	 * @return the mODEL_NA
	 */
	public String getMODEL_NA() {
		return MODEL_NA;
	}

	/**
	 * @param mODEL_NA the mODEL_NA to set
	 */
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}

	/**
	 * @return the eL_NO
	 */
	public String getEL_NO() {
		return EL_NO;
	}

	/**
	 * @param eL_NO the eL_NO to set
	 */
	public void setEL_NO(String eL_NO) {
		EL_NO = eL_NO;
	}

	/**
	 * @return the eL_CNAME
	 */
	public String getEL_CNAME() {
		return EL_CNAME;
	}

	/**
	 * @param eL_CNAME the eL_CNAME to set
	 */
	public void setEL_CNAME(String eL_CNAME) {
		EL_CNAME = eL_CNAME;
	}

	/**
	 * @return the pLACE
	 */
	public String getPLACE() {
		return PLACE;
	}

	/**
	 * @param pLACE the pLACE to set
	 */
	public void setPLACE(String pLACE) {
		PLACE = pLACE;
	}

	/**
	 * @return the cUPBOARD
	 */
	public String getCUPBOARD() {
		return CUPBOARD;
	}

	/**
	 * @param cUPBOARD the cUPBOARD to set
	 */
	public void setCUPBOARD(String cUPBOARD) {
		CUPBOARD = cUPBOARD;
	}

	public String toString(){
		return "MODEL_NA="+MODEL_NA+"EL_NO="+EL_NO+"EL_CNAME="+EL_CNAME+"PLACE="+PLACE+"CUPBOARD="+CUPBOARD+"ISCHECKED="+ISCHECKED;
	}

	/**
	 * @return the iSCHECKED
	 */
	public boolean isISCHECKED() {
		return ISCHECKED;
	}

	/**
	 * @param iSCHECKED the iSCHECKED to set
	 */
	public void setISCHECKED(boolean iSCHECKED) {
		ISCHECKED = iSCHECKED;
	}

}
