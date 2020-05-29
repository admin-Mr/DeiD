package ds.dsid.domain;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "DSID10SE")
public class DSID10SE {
    private java.lang.String NIKE_SH_ARITCLE;
    private java.lang.String SH_ST1;
    private java.lang.String SH_ST2;
    private java.lang.String SH_ST3;
    private java.lang.String SH_ST4;
    private java.lang.String SH_ST5;
    private java.lang.String SH_ST6;
    private java.lang.String SH_ST7;
    private java.lang.String SH_ST8;
    private java.lang.String SH_ST9;
    private java.lang.String SH_ST10;
    private java.lang.String SH_ST11;
    private java.lang.String SH_ST12;
    private java.lang.String MODEL_NAS;

    @Id
    @Column(name = "NIKE_SH_ARITCLE")
    public String getNIKE_SH_ARITCLE() {
        return NIKE_SH_ARITCLE;
    }

    public void setNIKE_SH_ARITCLE(String NIKE_SH_ARITCLE) {
        this.NIKE_SH_ARITCLE = NIKE_SH_ARITCLE;
    }

    @Column(name = "SH_ST1")
    public String getSH_ST1() {
        return SH_ST1;
    }

    public void setSH_ST1(String SH_ST1) {
        this.SH_ST1 = SH_ST1;
    }

    @Column(name = "SH_ST2")
    public String getSH_ST2() {
        return SH_ST2;
    }

    public void setSH_ST2(String SH_ST2) {
        this.SH_ST2 = SH_ST2;
    }

    @Column(name = "SH_ST3")
    public String getSH_ST3() {
        return SH_ST3;
    }

    public void setSH_ST3(String SH_ST3) {
        this.SH_ST3 = SH_ST3;
    }

    @Column(name = "SH_ST4")
    public String getSH_ST4() {
        return SH_ST4;
    }

    public void setSH_ST4(String SH_ST4) {
        this.SH_ST4 = SH_ST4;
    }

    @Column(name = "SH_ST5")
    public String getSH_ST5() {
        return SH_ST5;
    }

    public void setSH_ST5(String SH_ST5) {
        this.SH_ST5 = SH_ST5;
    }

    @Column(name = "SH_ST6")
    public String getSH_ST6() {
        return SH_ST6;
    }

    public void setSH_ST6(String SH_ST6) {
        this.SH_ST6 = SH_ST6;
    }

    @Column(name = "SH_ST7")
    public String getSH_ST7() {
        return SH_ST7;
    }

    public void setSH_ST7(String SH_ST7) {
        this.SH_ST7 = SH_ST7;
    }

    @Column(name = "SH_ST8")
    public String getSH_ST8() {
        return SH_ST8;
    }

    public void setSH_ST8(String SH_ST8) {
        this.SH_ST8 = SH_ST8;
    }

    @Column(name = "SH_ST9")
    public String getSH_ST9() {
        return SH_ST9;
    }

    public void setSH_ST9(String SH_ST9) {
        this.SH_ST9 = SH_ST9;
    }

    @Column(name = "SH_ST10")
    public String getSH_ST10() {
        return SH_ST10;
    }

    public void setSH_ST10(String SH_ST10) {
        this.SH_ST10 = SH_ST10;
    }

    @Column(name = "SH_ST11")
    public String getSH_ST11() {
        return SH_ST11;
    }

    public void setSH_ST11(String SH_ST11) {
        this.SH_ST11 = SH_ST11;
    }

    @Column(name = "SH_ST12")
    public String getSH_ST12() {
        return SH_ST12;
    }

    public void setSH_ST12(String SH_ST12) {
        this.SH_ST12 = SH_ST12;
    }

    @Column(name = "MODEL_NAS")
    public String getMODEL_NAS() {
        return MODEL_NAS;
    }

    public void setMODEL_NAS(String MODEL_NAS) {
        this.MODEL_NAS = MODEL_NAS;
    }
}
