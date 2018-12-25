package ds.dspb.domain;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

import java.io.Serializable;

import javax.persistence.Column;
/**
* ??????
**/
@Entity
@Table(name = "DSPB04_NEW")
public class DSPB04_NEW implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private java.lang.String PB_SYSID;	//????
    private java.lang.String PB_SYSNA;	//??????
    private java.lang.String PB_SYSCNA;	//??????
    /**
     * 取得????
     * @return PB_SYSID ????
     */
    @Id
    @Column(name = "PB_SYSID")
    public java.lang.String getPB_SYSID() {
        return PB_SYSID;
    }
    /**
     * 設定????
     * @param PB_SYSID ????
     */
    public void setPB_SYSID(java.lang.String PB_SYSID) {
        this.PB_SYSID = PB_SYSID;
    }
    /**
     * 取得??????
     * @return PB_SYSNA ??????
     */
    @Column(name = "PB_SYSNA")
    public java.lang.String getPB_SYSNA() {
        return PB_SYSNA;
    }
    /**
     * 設定??????
     * @param PB_SYSNA ??????
     */
    public void setPB_SYSNA(java.lang.String PB_SYSNA) {
        this.PB_SYSNA = PB_SYSNA;
    }
    /**
     * 取得??????
     * @return PB_SYSCNA ??????
     */
    @Column(name = "PB_SYSCNA")
    public java.lang.String getPB_SYSCNA() {
        return PB_SYSCNA;
    }
    /**
     * 設定??????
     * @param PB_SYSCNA ??????
     */
    public void setPB_SYSCNA(java.lang.String PB_SYSCNA) {
        this.PB_SYSCNA = PB_SYSCNA;
    }
}
