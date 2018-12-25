package ds.dspb.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import ds.dspb.domain.pk.DSPB00_NEWPk;

@IdClass(DSPB00_NEWPk.class)
@Entity
@Table(name="DSPB00_NEW")
public class DSPB00_NEW implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PB_NO")
	private String PB_NO;
	@Column(name = "PB_SYSID")
	private String PB_SYSID;
	@Column(name = "PB_ID")
	private String PB_ID;
	@Column(name = "PB_LANGTAG")
	private String PB_LANGTAG;
	@Column(name = "PB_MUNODE")
	private String PB_MUNODE;
	@Column(name = "PB_MUITEM")
	private String PB_MUITEM;
	@Column(name = "PB_FILEPATH")
	private String PB_FILEPATH;
	@Column(name = "PB_ICONPATH")
	private String PB_ICONPATH;
	@Column(name = "PB_ICONSCLASS")
	private String PB_ICONSCLASS;
	@Column(name = "PB_ADMIN")
	private String PB_ADMIN;
	@Column(name = "PB_PRGNAME")
	private String PB_PRGNAME;
	@Column(name = "UP_USER")
	private String UP_USER;
	@Temporal(TemporalType.DATE)
	@Column(name = "UP_DATE")
	private Date UP_DATE;
	
	public String getPB_NO(){
		return this.PB_NO;
	}
	
	public void setPB_NO(String PB_NO){
		this.PB_NO = PB_NO;
	}
	
	public String getPB_SYSID(){
		return this.PB_SYSID;
	}
	
	public void setPB_SYSID(String PB_SYSID){
		this.PB_SYSID = PB_SYSID;
	}
	
	public String getPB_ID(){
		return this.PB_ID;
	}
	
	public void setPB_ID(String PB_ID){
		this.PB_ID = PB_ID;
	}
	
	public String getPB_LANGTAG(){
		return this.PB_LANGTAG;
	}
	
	public void setPB_LANGTAG(String PB_LANGTAG){
		this.PB_LANGTAG = PB_LANGTAG;
	}
	
	public String getPB_MUNODE(){
		return this.PB_MUNODE;
	}
	
	public void setPB_MUNODE(String PB_MUNODE){
		this.PB_MUNODE = PB_MUNODE;
	}
	
	public String getPB_MUITEM(){
		return this.PB_MUITEM;
	}
	
	public void setPB_MUITEM(String PB_MUITEM){
		this.PB_MUITEM = PB_MUITEM;
	}
	
	public String getPB_FILEPATH(){
		return this.PB_FILEPATH;
	}
	
	public void setPB_FILEPATH(String PB_FILEPATH){
		this.PB_FILEPATH = PB_FILEPATH;
	}
	
	public String getPB_ICONPATH(){
		return this.PB_ICONPATH;
	}
	
	public void setPB_ICONPATH(String PB_ICONPATH){
		this.PB_ICONPATH = PB_ICONPATH;
	}
	
	public String getPB_ICONSCLASS(){
		return this.PB_ICONSCLASS;
	}
	
	public void setPB_ICONSCLASS(String PB_ICONSCLASS){
		this.PB_ICONSCLASS = PB_ICONSCLASS;
	}
	
	public String getPB_ADMIN(){
		return this.PB_ADMIN;
	}
	
	public void setPB_ADMIN(String PB_ADMIN){
		this.PB_ADMIN = PB_ADMIN;				
	}
	
	public String getPB_PRGNAME(){
		return this.PB_PRGNAME;
	}
	
	public void setPB_PRGNAME(String PB_PRGNAME){
		this.PB_PRGNAME = PB_PRGNAME;
	}
	
	public String getUP_USER(){
		return this.UP_USER;
	}
	
	public void setUP_USER(String UP_USER){
		this.UP_USER = UP_USER;
	}
	
	public Date getUP_DATE(){
		return this.UP_DATE;
	}
	
	public void setUP_DATE(Date UP_DATE){
		this.UP_DATE = UP_DATE;
	}
	
}
