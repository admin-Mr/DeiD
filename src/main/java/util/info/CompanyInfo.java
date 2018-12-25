/**
 * Project:		Web應用系統開發平台
 * Filename: 	CompanyInfo.java
 * Author: 		
 * Create Date:	
 * Version: 	
 * Description:	登入公司相關資訊
 */

package util.info;

import java.io.Serializable;

/**
 * CompanyInfo 公司別各項資訊。
 */
@SuppressWarnings("serial")
public class CompanyInfo implements Serializable {

	private String	companyID		= "";

	private String	companyName		= "";

	private String	connectionName	= "";
	
	private String	country	= "";

	public CompanyInfo() {
		super();
	}

	/**
	 * @param companyID 公司別代號，系統內部使用
	 * @param companyName 公司名稱
	 * @param connectionName 資料庫連結定義名稱，參考<code>ConnectionInfo</code>
	 */
	public CompanyInfo(String companyID, String companyName, String connectionName,String country) {
		super();
		this.companyID = companyID;
		this.companyName = companyName;
		this.connectionName = connectionName;
		this.country = country;
	}
	
	public CompanyInfo(String companyID, String companyName, String connectionName) {
		super();
		this.companyID = companyID;
		this.companyName = companyName;
		this.connectionName = connectionName;
	}

	/**
	 * 取得公司代號(系統定義代號，而非資料庫裡的公司代號)
	 *
	 * @return Return 公司代號.
	 */
	public String getCompanyID() {
		return companyID;
	}

	/**
	 * 取得公司名稱(系統定義名稱，而非資料庫裡的公司名)
	 *
	 * @return Return 公司名稱.
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 取得公司所使用資料庫連結名稱
	 *
	 * @return Return 公司所使用資料庫連結名稱.
	 */
	public String getConnectionName() {
		return connectionName;
	}
	
	/**
	 * 取得公司所使用國家
	 *
	 * @return Return 取得公司所使用國家.
	 */
	public String getCountry() {
		return country;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
}
