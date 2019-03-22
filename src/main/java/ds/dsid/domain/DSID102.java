package ds.dsid.domain;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import java.util.Date;

import javax.persistence.Column;
/**
* 系統主檔
**/
@Entity
@Table(name = "DSID102")
public class DSID102 {
    private String EL_NO;	//材料編號
    private String EL_NA;	//材料名稱
    private String S1;		//SIZE1
    private String S2;		//SIZE2
    private String S3;		//SIZE3
    private String S4;		//SIZE4
    private String S5;		//SIZE5
    private String S6;		//SIZE6
    private String S7;		//SIZE7
    private String S8;		//SIZE8
    private String S9;		//SIZE9
    private String S10;		//SIZE10
    private String S11;		//SIZE11
    private String S12;		//SIZE12
    private String S13;		//SIZE13
    private String S14;		//SIZE14
    private String S15;		//SIZE15
    private String S16;		//SIZE16
    private String S17;		//SIZE17
    private String S18;		//SIZE18
    private String S19;		//SIZE19
    private String S20;		//SIZE20
    private String S21;		//SIZE21
    private String S22;		//SIZE22
    private String S23;		//SIZE23
    private String S24;		//SIZE24
    private String S25;		//SIZE25
    private String S26;		//SIZE26
    private String S27;		//SIZE27
    private String S28;		//SIZE28
    private String S29;		//SIZE29
    private String S30;		//SIZE30
    private String S31;		//SIZE31
    private String S32;		//SIZE32
    private String S33;		//SIZE33
    private String S34;		//SIZE34
    private String S35;		//SIZE35
    private String S36;		//SIZE36
    private String S37;		//SIZE37
    private String S38;		//SIZE38
    private String S39;		//SIZE39
    private String S40;		//SIZE40
    private Double Q1;		//QTY1
    private Double Q2;		//QTY2
    private Double Q3;		//QTY3
    private Double Q4;		//QTY4
    private Double Q5;		//QTY5
    private Double Q6;		//QTY6
    private Double Q7;		//QTY7
    private Double Q8;		//QTY8
    private Double Q9;		//QTY9
    private Double Q10;		//QTY10
    private Double Q11;		//QTY11
    private Double Q12;		//QTY12
    private Double Q13;		//QTY13
    private Double Q14;		//QTY14
    private Double Q15;		//QTY15
    private Double Q16;		//QTY16
    private Double Q17;		//QTY17
    private Double Q18;		//QTY18
    private Double Q19;		//QTY19
    private Double Q20;		//QTY20
    private Double Q21;		//QTY21
    private Double Q22;		//QTY22
    private Double Q23;		//QTY23
    private Double Q24;		//QTY24
    private Double Q25;		//QTY25
    private Double Q26;		//QTY26
    private Double Q27;		//QTY27
    private Double Q28;		//QTY28
    private Double Q29;		//QTY29
    private Double Q30;		//QTY30
    private Double Q31;		//QTY31
    private Double Q32;		//QTY32
    private Double Q33;		//QTY33
    private Double Q34;		//QTY34
    private Double Q35;		//QTY35
    private Double Q36;		//QTY36
    private Double Q37;		//QTY37
    private Double Q38;		//QTY38
    private Double Q39;		//QTY39
    private Double Q40;		//QTY40
    private String UP_USER;	//異動人員
    private Date UP_DATE;	//異動日期
	private boolean ISADD;
	private boolean ISEDIT;

    @Id
    @Column(name = "EL_NO")
    public java.lang.String getEL_NO() {
        return EL_NO;
    }
    public void setEL_NO(java.lang.String EL_NO) {
        this.EL_NO = EL_NO;
    }
    
    @Column(name = "EL_NA")
    public java.lang.String getEL_NA() {
        return EL_NA;
    }
    public void setEL_NA(java.lang.String EL_NA) {
        this.EL_NA = EL_NA;
    }
    
    @Column(name = "S1")
    public String getS1() {
        return S1;
    }
    public void setS1(String S1) {
        this.S1 = S1;
    }
    
    @Column(name = "S2")
    public String getS2() {
        return S2;
    }
    public void setS2(String S2) {
        this.S2 = S2;
    }
    
    @Column(name = "S3")
    public String getS3() {
        return S3;
    }
    public void setS3(String S3) {
        this.S3 = S3;
    }
    
    @Column(name = "S4")
    public String getS4() {
        return S4;
    }
    public void setS4(String S4) {
        this.S4 = S4;
    }
    
    @Column(name = "S5")
    public String getS5() {
        return S5;
    }
    public void setS5(String S5) {
        this.S5 = S5;
    }
    
    @Column(name = "S6")
    public String getS6() {
        return S6;
    }
    public void setS6(String S6) {
        this.S6 = S6;
    }
    
    @Column(name = "S7")
    public String getS7() {
        return S7;
    }
    public void setS7(String S7) {
        this.S7 = S7;
    }
    
    @Column(name = "S8")
    public String getS8() {
        return S8;
    }
    public void setS8(String S8) {
        this.S8 = S8;
    }
    
    @Column(name = "S9")
    public String getS9() {
        return S9;
    }
    public void setS9(String S9) {
        this.S9 = S9;
    }
    
    @Column(name = "S10")
    public String getS10() {
        return S10;
    }
    public void setS10(String S10) {
        this.S10 = S10;
    }
    
    @Column(name = "S11")
    public String getS11() {
        return S11;
    }
    public void setS11(String S11) {
        this.S11 = S11;
    }
    
    @Column(name = "S12")
    public String getS12() {
        return S12;
    }
    public void setS12(String S12) {
        this.S12 = S12;
    }
    
    @Column(name = "S13")
    public String getS13() {
        return S13;
    }
    public void setS13(String S13) {
        this.S13 = S13;
    }
    
    @Column(name = "S14")
    public String getS14() {
        return S14;
    }
    public void setS14(String S14) {
        this.S14 = S14;
    }
    
    @Column(name = "S15")
    public String getS15() {
        return S15;
    }
    public void setS15(String S15) {
        this.S15 = S15;
    }
    
    @Column(name = "S16")
    public String getS16() {
        return S16;
    }
    public void setS16(String S16) {
        this.S16 = S16;
    }
    
    @Column(name = "S17")
    public String getS17() {
        return S17;
    }
    public void setS17(String S17) {
        this.S17 = S17;
    }
    
    @Column(name = "S18")
    public String getS18() {
        return S18;
    }
    public void setS18(String S18) {
        this.S18 = S18;
    }
    
    @Column(name = "S19")
    public String getS19() {
        return S19;
    }
    public void setS19(String S19) {
        this.S19 = S19;
    }
    
    @Column(name = "S20")
    public String getS20() {
        return S20;
    }
    public void setS20(String S20) {
        this.S20 = S20;
    }
    
    @Column(name = "S21")
    public String getS21() {
        return S21;
    }
    public void setS21(String S21) {
        this.S21 = S21;
    }
    
    @Column(name = "S22")
    public String getS22() {
        return S22;
    }
    public void setS22(String S22) {
        this.S22 = S22;
    }
    
    @Column(name = "S23")
    public String getS23() {
        return S23;
    }
    public void setS23(String S23) {
        this.S23 = S23;
    }
    
    @Column(name = "S24")
    public String getS24() {
        return S24;
    }
    public void setS24(String S24) {
        this.S24 = S24;
    }
    
    @Column(name = "S25")
    public String getS25() {
        return S25;
    }
    public void setS25(String S25) {
        this.S25 = S25;
    }
    
    @Column(name = "S26")
    public String getS26() {
        return S26;
    }
    public void setS26(String S26) {
        this.S26 = S26;
    }
    
    @Column(name = "S27")
    public String getS27() {
        return S27;
    }
    public void setS27(String S27) {
        this.S27 = S27;
    }
    
    @Column(name = "S28")
    public String getS28() {
        return S28;
    }
    public void setS28(String S28) {
        this.S28 = S28;
    }
    
    @Column(name = "S29")
    public String getS29() {
        return S29;
    }
    public void setS29(String S29) {
        this.S29 = S29;
    }
    
    @Column(name = "S30")
    public String getS30() {
        return S30;
    }
    public void setS30(String S30) {
        this.S30 = S30;
    }
    
    @Column(name = "S31")
    public String getS31() {
        return S31;
    }
    public void setS31(String S31) {
        this.S31 = S31;
    }
    
    @Column(name = "S32")
    public String getS32() {
        return S32;
    }
    public void setS32(String S32) {
        this.S32 = S32;
    }
    
    @Column(name = "S33")
    public String getS33() {
        return S33;
    }
    public void setS33(String S33) {
        this.S33 = S33;
    }
    
    @Column(name = "S34")
    public String getS34() {
        return S34;
    }
    public void setS34(String S34) {
        this.S34 = S34;
    }
    
    @Column(name = "S35")
    public String getS35() {
        return S35;
    }
    public void setS35(String S35) {
        this.S35 = S35;
    }
    
    @Column(name = "S36")
    public String getS36() {
        return S36;
    }
    public void setS36(String S36) {
        this.S36 = S36;
    }
    
    @Column(name = "S37")
    public String getS37() {
        return S37;
    }
    public void setS37(String S37) {
        this.S37 = S37;
    }
    
    @Column(name = "S38")
    public String getS38() {
        return S38;
    }
    public void setS38(String S38) {
        this.S38 = S38;
    }
    
    @Column(name = "S39")
    public String getS39() {
        return S39;
    }
    public void setS39(String S39) {
        this.S39 = S39;
    }
    
    @Column(name = "S40")
    public String getS40() {
        return S40;
    }
    public void setS40(String S40) {
        this.S40 = S40;
    }
    
    @Column(name = "Q1")
    public Double getQ1() {
        return Q1;
    }
    public void setQ1(Double Q1) {
        this.Q1 = Q1;
    }
    
    @Column(name = "Q2")
    public Double getQ2() {
        return Q2;
    }
    public void setQ2(Double Q2) {
        this.Q2 = Q2;
    }
    
    @Column(name = "Q3")
    public Double getQ3() {
        return Q3;
    }
    public void setQ3(Double Q3) {
        this.Q3 = Q3;
    }
    
    @Column(name = "Q4")
    public Double getQ4() {
        return Q4;
    }
    public void setQ4(Double Q4) {
        this.Q4 = Q4;
    }
    
    @Column(name = "Q5")
    public Double getQ5() {
        return Q5;
    }
    public void setQ5(Double Q5) {
        this.Q5 = Q5;
    }
    
    @Column(name = "Q6")
    public Double getQ6() {
        return Q6;
    }
    public void setQ6(Double Q6) {
        this.Q6 = Q6;
    }
    
    @Column(name = "Q7")
    public Double getQ7() {
        return Q7;
    }
    public void setQ7(Double Q7) {
        this.Q7 = Q7;
    }
    
    @Column(name = "Q8")
    public Double getQ8() {
        return Q8;
    }
    public void setQ8(Double Q8) {
        this.Q8 = Q8;
    }
    
    @Column(name = "Q9")
    public Double getQ9() {
        return Q9;
    }
    public void setQ9(Double Q9) {
        this.Q9 = Q9;
    }
    
    @Column(name = "Q10")
    public Double getQ10() {
        return Q10;
    }
    public void setQ10(Double Q10) {
        this.Q10 = Q10;
    }
    
    @Column(name = "Q11")
    public Double getQ11() {
        return Q11;
    }
    public void setQ11(Double Q11) {
        this.Q11 = Q11;
    }
    
    @Column(name = "Q12")
    public Double getQ12() {
        return Q12;
    }
    public void setQ12(Double Q12) {
        this.Q12 = Q12;
    }
    
    @Column(name = "Q13")
    public Double getQ13() {
        return Q13;
    }
    public void setQ13(Double Q13) {
        this.Q13 = Q13;
    }
    
    @Column(name = "Q14")
    public Double getQ14() {
        return Q14;
    }
    public void setQ14(Double Q14) {
        this.Q14 = Q14;
    }
    
    @Column(name = "Q15")
    public Double getQ15() {
        return Q15;
    }
    public void setQ15(Double Q15) {
        this.Q15 = Q15;
    }
    
    @Column(name = "Q16")
    public Double getQ16() {
        return Q16;
    }
    public void setQ16(Double Q16) {
        this.Q16 = Q16;
    }
    
    @Column(name = "Q17")
    public Double getQ17() {
        return Q17;
    }
    public void setQ17(Double Q17) {
        this.Q17 = Q17;
    }
    
    @Column(name = "Q18")
    public Double getQ18() {
        return Q18;
    }
    public void setQ18(Double Q18) {
        this.Q18 = Q18;
    }
    
    @Column(name = "Q19")
    public Double getQ19() {
        return Q19;
    }
    public void setQ19(Double Q19) {
        this.Q19 = Q19;
    }
    
    @Column(name = "Q20")
    public Double getQ20() {
        return Q20;
    }
    public void setQ20(Double Q20) {
        this.Q20 = Q20;
    }
    
    @Column(name = "Q21")
    public Double getQ21() {
        return Q21;
    }
    public void setQ21(Double Q21) {
        this.Q21 = Q21;
    }
    
    @Column(name = "Q22")
    public Double getQ22() {
        return Q22;
    }
    public void setQ22(Double Q22) {
        this.Q22 = Q22;
    }
    
    @Column(name = "Q23")
    public Double getQ23() {
        return Q23;
    }
    public void setQ23(Double Q23) {
        this.Q23 = Q23;
    }
    
    @Column(name = "Q24")
    public Double getQ24() {
        return Q24;
    }
    public void setQ24(Double Q24) {
        this.Q24 = Q24;
    }
    
    @Column(name = "Q25")
    public Double getQ25() {
        return Q25;
    }
    public void setQ25(Double Q25) {
        this.Q25 = Q25;
    }
    
    @Column(name = "Q26")
    public Double getQ26() {
        return Q26;
    }
    public void setQ26(Double Q26) {
        this.Q26 = Q26;
    }
    
    @Column(name = "Q27")
    public Double getQ27() {
        return Q27;
    }
    public void setQ27(Double Q27) {
        this.Q27 = Q27;
    }
    
    @Column(name = "Q28")
    public Double getQ28() {
        return Q28;
    }
    public void setQ28(Double Q28) {
        this.Q28 = Q28;
    }
    
    @Column(name = "Q29")
    public Double getQ29() {
        return Q29;
    }
    public void setQ29(Double Q29) {
        this.Q29 = Q29;
    }
    
    @Column(name = "Q30")
    public Double getQ30() {
        return Q30;
    }
    public void setQ30(Double Q30) {
        this.Q30 = Q30;
    }
    
    @Column(name = "Q31")
    public Double getQ31() {
        return Q31;
    }
    public void setQ31(Double Q31) {
        this.Q31 = Q31;
    }
    
    @Column(name = "Q32")
    public Double getQ32() {
        return Q32;
    }
    public void setQ32(Double Q32) {
        this.Q32 = Q32;
    }
    
    @Column(name = "Q33")
    public Double getQ33() {
        return Q33;
    }
    public void setQ33(Double Q33) {
        this.Q33 = Q33;
    }
    
    @Column(name = "Q34")
    public Double getQ34() {
        return Q34;
    }
    public void setQ34(Double Q34) {
        this.Q34 = Q34;
    }
    
    @Column(name = "Q35")
    public Double getQ35() {
        return Q35;
    }
    public void setQ35(Double Q35) {
        this.Q35 = Q35;
    }
    
    @Column(name = "Q36")
    public Double getQ36() {
        return Q36;
    }
    public void setQ36(Double Q36) {
        this.Q36 = Q36;
    }
    
    @Column(name = "Q37")
    public Double getQ37() {
        return Q37;
    }
    public void setQ37(Double Q37) {
        this.Q37 = Q37;
    }
    
    @Column(name = "Q38")
    public Double getQ38() {
        return Q38;
    }
    public void setQ38(Double Q38) {
        this.Q38 = Q38;
    }
    
    @Column(name = "Q39")
    public Double getQ39() {
        return Q39;
    }
    public void setQ39(Double Q39) {
        this.Q39 = Q39;
    }
    
    @Column(name = "Q40")
    public Double getQ40() {
        return Q40;
    }
    public void setQ40(Double Q40) {
        this.Q40 = Q40;
    }
    
    @Column(name = "UP_USER")
    public java.lang.String getUP_USER() {
        return UP_USER;
    }
    public void setUP_USER(java.lang.String UP_USER) {
        this.UP_USER = UP_USER;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name = "UP_DATE")
    public java.util.Date getUP_DATE() {
        return UP_DATE;
    }

    public void setUP_DATE(java.util.Date UP_DATE) {
        this.UP_DATE = UP_DATE;
    }
    
	@Transient 
	public boolean getISEDIT() {
		return ISEDIT;
	}
	 
	public void setISEDIT(boolean iSEDIT) {
		ISEDIT = iSEDIT;
	}
	
	@Transient
	public boolean getISADD() {
		return ISADD;
	}

	public void setISADD(boolean iSADD) {
		ISADD = iSADD;
	}
}
