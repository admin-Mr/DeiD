package ds.dsid.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ds.dsid.domain.pk.DSID01_TEMP2_PK;
import ds.dsid.domain.pk.DSID01_TEMP_PK;

@Entity
@Table(name="DSID01_TEMP2")
@SuppressWarnings("serial")
@IdClass(DSID01_TEMP2_PK.class)
public class DSID01_TEMP2 implements Serializable, Comparable<DSID01_TEMP2>{

	private String WORK_ORDER_ID; 	// 客戶訂單ID
	private String SEQ;
	private String GROUP_NO;
	private String PART_NA;
	private String REMARKS;
	private String TYPE;
	private String CODE;
	private String NAME;

	private String UP_USER; 		// 更新人員
	private Date UP_DATE; 			// 更新日期
	
	
    @Id
    @Column(name = "WORK_ORDER_ID")
	public String getWORK_ORDER_ID() {
		return WORK_ORDER_ID;
	}
	public void setWORK_ORDER_ID(String wORK_ORDER_ID) {
		WORK_ORDER_ID = wORK_ORDER_ID;
	}
	
    @Column(name = "SEQ")
	public String getSEQ() {
		return SEQ;
	}
	public void setSEQ(String sEQ) {
		SEQ = sEQ;
	}
	
    @Column(name = "GROUP_NO")
	public String getGROUP_NO() {
		return GROUP_NO;
	}
	public void setGROUP_NO(String gROUP_NO) {
		GROUP_NO = gROUP_NO;
	}
	
    @Column(name = "PART_NA")
	public String getPART_NA() {
		return PART_NA;
	}
	public void setPART_NA(String pART_NA) {
		PART_NA = pART_NA;
	}
	
    @Column(name = "REMARKS")
	public String getREMARKS() {
		return REMARKS;
	}
	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
	}
	
    @Column(name = "TYPE")
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	
    @Column(name = "CODE")
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	
    @Column(name = "NAME")
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
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
	@Override
	public int compareTo(DSID01_TEMP2 o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
