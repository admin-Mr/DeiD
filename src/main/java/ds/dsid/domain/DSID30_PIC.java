package ds.dsid.domain;
// ID線掃描程式 Url 關聯表格

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "dsid30_pic")
public class DSID30_PIC {

	private String OD_NO;			// 訂單號碼
	private String DP_NO;			// 派工單號
	private Date 	ORDER_DATE;		// 業務接單日期
	private String MODEL_NA;		// 型體名稱
	private String WORK_ORDER_ID;	// Work Order ID
	private String URL1;			// URL1
	private String URL2	;			// URL2
	private String URL3	;			// URL3
	private String URL4	;			// URL4
	private Date	UP_DATE;		// 更新日期
	private String UP_USER;			// 更新賬戶
	
	
	@Id
	@Column(name = "WORK_ORDER_ID")
	public String getWORK_ORDER_ID() {
		return WORK_ORDER_ID;
	}
	public void setWORK_ORDER_ID(String wORK_ORDER_ID) {
		WORK_ORDER_ID = wORK_ORDER_ID;
	}
	
	@Column(name = "OD_NO")
	public String getOD_NO() {
		return OD_NO;
	}
	public void setOD_NO(String oD_NO) {
		OD_NO = oD_NO;
	}
	
	@Column(name = "DP_NO")
	public String getDP_NO() {
		return DP_NO;
	}
	public void setDP_NO(String dP_NO) {
		DP_NO = dP_NO;
	}
	
	@Column(name = "ORDER_DATE")
	@Temporal(TemporalType.DATE)
	public Date getORDER_DATE() {
		return ORDER_DATE;
	}
	public void setORDER_DATE(Date oRDER_DATE) {
		ORDER_DATE = oRDER_DATE;
	}
	
	@Column(name = "MODEL_NA")
	public String getMODEL_NA() {
		return MODEL_NA;
	}
	public void setMODEL_NA(String mODEL_NA) {
		MODEL_NA = mODEL_NA;
	}
	
	@Column(name = "URL1")
	public String getURL1() {
		return URL1;
	}
	public void setURL1(String uRL1) {
		URL1 = uRL1;
	}
	
	@Column(name = "URL2")
	public String getURL2() {
		return URL2;
	}
	public void setURL2(String uRL2) {
		URL2 = uRL2;
	}
	
	@Column(name = "URL3")
	public String getURL3() {
		return URL3;
	}
	public void setURL3(String uRL3) {
		URL3 = uRL3;
	}
	
	@Column(name = "URL4")
	public String getURL4() {
		return URL4;
	}
	public void setURL4(String uRL4) {
		URL4 = uRL4;
	}
	
	@Column(name = "UP_DATE")
	@Temporal(TemporalType.DATE)
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	
}
