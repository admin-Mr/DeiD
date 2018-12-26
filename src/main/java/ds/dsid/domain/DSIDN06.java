package ds.dsid.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "DSIDN06")
public class DSIDN06 {
	private String AREANUM;
	// A 區
	private String A1;
	private String A2;
	private String A3;
	// B 區
	private String B1;
	private String B2;
	private String B3;
	private String B4;
	private String B5;
	private String B6;
	private String B7;
	private String B8;
	private String B9;
	private String B10;
	private String B11;
	private String B12;
	// C 區
	private String C1;
	private String C2;
	private String C3;
	// D 區
	private String D1;
	private String D2;
	private String D3;
	// E 區
	private String E1;
	private String E2;
	private String E3;
	// F 區
	private String F1;
	private String F2;
	private String F3;
	// G區
	private String G1;
	private String G2;
	private String G3;
	private String G4;
	private String G5;
	// H 區
	private String H1;
	private String H2;
	private String H3;
	private String H4;
	private String H5;
	
	private String CREATEDAY = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());;
	private String UP_USER;
	private Date UP_DATE = new Date();
	
	
	
	private String STATUS;
    private boolean ISEDIT;
	private boolean ISADD;
	 
	
	 
	
	@Id
	public String getAREANUM() {
		return AREANUM;
	}
	public String getA1() {
		return A1;
	}
	public String getA2() {
		return A2;
	}
	public String getA3() {
		return A3;
	}
	public String getB1() {
		return B1;
	}
	public String getB2() {
		return B2;
	}
	public String getB3() {
		return B3;
	}
	public String getB4() {
		return B4;
	}
	public String getB5() {
		return B5;
	}
	public String getB6() {
		return B6;
	}
	public String getB7() {
		return B7;
	}
	public String getB8() {
		return B8;
	}
	public String getB9() {
		return B9;
	}
	public String getB10() {
		return B10;
	}
	public String getB11() {
		return B11;
	}
	public String getB12() {
		return B12;
	}
	public String getC1() {
		return C1;
	}
	public String getC2() {
		return C2;
	}
	public String getC3() {
		return C3;
	}
	public String getD1() {
		return D1;
	}
	public String getD2() {
		return D2;
	}
	public String getD3() {
		return D3;
	}
	public String getE1() {
		return E1;
	}
	public String getE2() {
		return E2;
	}
	public String getE3() {
		return E3;
	}
	public String getF1() {
		return F1;
	}
	public String getF2() {
		return F2;
	}
	public String getF3() {
		return F3;
	}
	public String getG1() {
		return G1;
	}
	public String getG2() {
		return G2;
	}
	public String getG3() {
		return G3;
	}
	public String getG4() {
		return G4;
	}
	public String getG5() {
		return G5;
	}
	public String getH1() {
		return H1;
	}
	public String getH2() {
		return H2;
	}
	public String getH3() {
		return H3;
	}
	public String getH4() {
		return H4;
	}
	public String getH5() {
		return H5;
	}
	public String getCREATEDAY() {
		return CREATEDAY;
	}
	public String getUP_USER() {
		return UP_USER;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUP_DATE() {
		return UP_DATE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setUP_DATE(Date uP_DATE) {
		UP_DATE = uP_DATE;
	}
	public DSIDN06() {
		ISEDIT = true;
		ISADD = false;
	}
	@Transient
	public boolean isISADD() {
		return ISADD;
	}
	@Transient
	public boolean isISEDIT() {
		return ISEDIT;
	}
	
	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}
	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}
	
	public void setAREANUM(String aREANUM) {
		AREANUM = aREANUM;
	}
	public void setA1(String a1) {
		A1 = a1;
	}
	public void setA2(String a2) {
		A2 = a2;
	}
	public void setA3(String a3) {
		A3 = a3;
	}
	public void setB1(String b1) {
		B1 = b1;
	}
	public void setB2(String b2) {
		B2 = b2;
	}
	public void setB3(String b3) {
		B3 = b3;
	}
	public void setB4(String b4) {
		B4 = b4;
	}
	public void setB5(String b5) {
		B5 = b5;
	}
	public void setB6(String b6) {
		B6 = b6;
	}
	public void setB7(String b7) {
		B7 = b7;
	}
	public void setB8(String b8) {
		B8 = b8;
	}
	public void setB9(String b9) {
		B9 = b9;
	}
	public void setB10(String b10) {
		B10 = b10;
	}
	public void setB11(String b11) {
		B11 = b11;
	}
	public void setB12(String b12) {
		B12 = b12;
	}
	public void setC1(String c1) {
		C1 = c1;
	}
	public void setC2(String c2) {
		C2 = c2;
	}
	public void setC3(String c3) {
		C3 = c3;
	}
	public void setD1(String d1) {
		D1 = d1;
	}
	public void setD2(String d2) {
		D2 = d2;
	}
	public void setD3(String d3) {
		D3 = d3;
	}
	public void setE1(String e1) {
		E1 = e1;
	}
	public void setE2(String e2) {
		E2 = e2;
	}
	public void setE3(String e3) {
		E3 = e3;
	}
	public void setF1(String f1) {
		F1 = f1;
	}
	public void setF2(String f2) {
		F2 = f2;
	}
	public void setF3(String f3) {
		F3 = f3;
	}
	public void setG1(String g1) {
		G1 = g1;
	}
	public void setG2(String g2) {
		G2 = g2;
	}
	public void setG3(String g3) {
		G3 = g3;
	}
	public void setG4(String g4) {
		G4 = g4;
	}
	public void setG5(String g5) {
		G5 = g5;
	}
	public void setH1(String h1) {
		H1 = h1;
	}
	public void setH2(String h2) {
		H2 = h2;
	}
	public void setH3(String h3) {
		H3 = h3;
	}
	public void setH4(String h4) {
		H4 = h4;
	}
	public void setH5(String h5) {
		H5 = h5;
	}
	public void setCREATEDAY(String cREATEDAY) {
		CREATEDAY = cREATEDAY;
	}
	public void setUP_USER(String uP_USER) {
		UP_USER = uP_USER;
	}
	
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
}
