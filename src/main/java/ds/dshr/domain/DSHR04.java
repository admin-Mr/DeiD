package ds.dshr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 級別名稱設定
 **/
@Entity
@Table(name = "DSHR04")
public class DSHR04 {
	private java.lang.String JJB;
	private java.lang.String JB_NM; // 級別名稱
	private java.lang.String UP_USER; // 更新人員
	private java.util.Date UP_DATE; // 更新日期

	@Id
	@NotEmpty(message = "JB cannot be empty")
	@Column(name = "JB")
	public java.lang.String getJJB() {
		return JJB;
	}

	public void setJJB(java.lang.String jJB) {
		JJB = jJB;
	}
	
	@Column(name = "JB_NM") //JHE
	public java.lang.String getJB_NM() {
		return JB_NM;
	}

	public void setJB_NM(java.lang.String jB_NM) {
		JB_NM = jB_NM;
	}
	@Column(name = "UP_USER")
	public java.lang.String getUP_USER() {
		return UP_USER;
	}

	public void setUP_USER(java.lang.String uP_USER) {
		UP_USER = uP_USER;
	}
	@Column(name = "UP_DATE")
	public java.util.Date getUP_DATE() {
		return UP_DATE;
	}

	public void setUP_DATE(java.util.Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
}
