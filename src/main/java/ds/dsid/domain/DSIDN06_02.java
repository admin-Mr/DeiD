package ds.dsid.domain;

public class DSIDN06_02 {
	private String SEQ;
	private String PLACE;
	/**
	 * @return the sEQ
	 */
	public String getSEQ() {
		return SEQ;
	}
	/**
	 * @param sEQ the sEQ to set
	 */
	public void setSEQ(String sEQ) {
		SEQ = sEQ;
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
	
	public String toString(){
		return "SEQ="+SEQ+"PLACE="+PLACE;
	}
}
