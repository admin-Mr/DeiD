package ds.dsid.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DSID30")
public class DSID30 {

	private String OD_NO; // 訂單號碼

	private String URL2	; // URL2
	private String URL3; // URL3
	private String URL4	; // URL4
	private String URL5	; // URL5
	private String URL6	; // URL6
	private String URL7; // URL7
	private String URL8; // URL8
	private String URL9	; // URL9
	private String URL10; // URL10
	
    @Id
    @Column(name = "OD_NO")
	public String getOD_NO() {
		return OD_NO;
	}
	public void setOD_NO(String oD_NO) {
		OD_NO = oD_NO;
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
	
    @Column(name = "URL5")
	public String getURL5() {
		return URL5;
	}
	public void setURL5(String uRL5) {
		URL5 = uRL5;
	}
	
    @Column(name = "URL6")
	public String getURL6() {
		return URL6;
	}
	public void setURL6(String uRL6) {
		URL6 = uRL6;
	}
	
    @Column(name = "URL7")
	public String getURL7() {
		return URL7;
	}
	public void setURL7(String uRL7) {
		URL7 = uRL7;
	}
	
    @Column(name = "URL8")
	public String getURL8() {
		return URL8;
	}
	public void setURL8(String uRL8) {
		URL8 = uRL8;
	}
	
    @Column(name = "URL9")
	public String getURL9() {
		return URL9;
	}
	public void setURL9(String uRL9) {
		URL9 = uRL9;
	}
	
    @Column(name = "URL10")
	public String getURL10() {
		return URL10;
	}
	public void setURL10(String uRL10) {
		URL10 = uRL10;
	}

	
	
}
