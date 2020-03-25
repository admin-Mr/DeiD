package ds.dsid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DSID214")
public class DSID214 {
	public String BOX_ID;
	public String BOX_NAME;
	public String UP_USER;
	private Date UP_DATE;
	
	@Id
    @Column(name = "BOX_ID")
	public String getBOX_ID() {
		return BOX_ID;
	}
	public void setBOX_ID(String bOX_ID) {
		BOX_ID = bOX_ID;
	}
	
	@Column(name = "BOX_NAME")
	public String getBOX_NAME() {
		return BOX_NAME;
	}
	public void setBOX_NAME(String bOX_NAME) {
		BOX_NAME = bOX_NAME;
	}
	
	@Column(name = "UP_USER")
	public String getUP_USER() {
		return UP_USER;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	
}
