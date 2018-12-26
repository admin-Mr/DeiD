package ds.dsid.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DSID11_7")
public class DSID11_7 {

	private String MODEL_NA;	//型體名稱
	private String BLOCK1;	
	private String BLOCK2;	
	private String BLOCK3;	
	private String BLOCK4;	
	private String BLOCK5;	
	private String BLOCK6;	
	private String BLOCK7;
	private String BLOCK8;
	private String UP_USER;	//異動人員
	private Date UP_DATE;	//異動日期

	@Id
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "BLOCK1")
	public String getBLOCK1() {
		return BLOCK1;
	}
	public void setBLOCK1(String bLOCK1) {
		BLOCK1 = bLOCK1;
	}
	
	@Column(name = "BLOCK2")
	public String getBLOCK2() {
		return BLOCK2;
	}
	public void setBLOCK2(String bLOCK2) {
		BLOCK2 = bLOCK2;
	}
	
	@Column(name = "BLOCK3")
	public String getBLOCK3() {
		return BLOCK3;
	}
	public void setBLOCK3(String bLOCK3) {
		BLOCK3 = bLOCK3;
	}
	
	@Column(name = "BLOCK4")
	public String getBLOCK4() {
		return BLOCK4;
	}
	public void setBLOCK4(String bLOCK4) {
		BLOCK4 = bLOCK4;
	}
	
	@Column(name = "BLOCK5")
	public String getBLOCK5() {
		return BLOCK5;
	}
	public void setBLOCK5(String bLOCK5) {
		BLOCK5 = bLOCK5;
	}
	
	@Column(name = "BLOCK6")
	public String getBLOCK6() {
		return BLOCK6;
	}
	public void setBLOCK6(String bLOCK6) {
		BLOCK6 = bLOCK6;
	}
	
	@Column(name = "BLOCK7")
	public String getBLOCK7() {
		return BLOCK7;
	}
	public void setBLOCK7(String bLOCK7) {
		BLOCK7 = bLOCK7;
	}
	
	@Column(name = "BLOCK8")
	public String getBLOCK8() {
		return BLOCK8;
	}
	public void setBLOCK8(String bLOCK8) {
		BLOCK8 = bLOCK8;
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
