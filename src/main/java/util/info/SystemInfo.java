package util.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SystemInfo 此物件類別它提供了系統平台內部所有各項參數設定。 
 * 此物件可透過{@link dsc.echo2app.Application#getSystemInfo() getSystemInfo}方法取得。
 * 此物件的各項資訊定義在<b>conf/System.xml</b>檔案裡。包含系統使用的各種資訊。如公司別資訊，資料連結資訊。
 */
public class SystemInfo {
	private int					browserMaxRecord	= 100;	//系統資料瀏覽時， 資料最大Catch資料筆數

	private int					browserPageRecord	= 10;	//系統資料瀏覽時， 每頁資料筆數

	private List<CompanyInfo>	companies			= new ArrayList<CompanyInfo>();
	private List<CompanyInfo>	hiddencompanies			= new ArrayList<CompanyInfo>();
	private Map<String,String>	langs				= new TreeMap<String,String>();

	private int					queryMaxRecord		= 1000;	//系統查詢後， 資料最大Catch資料筆數
	
	private int					queryPageRecord		= 10;	//系統查詢後， 每頁資料筆數
    
    private String              reportOutputFolder  = "";   //報表產生的輸出路徑
    
    private String              reportTempFolder    = "";   //報表暫時輸出的路徑
//  tigereye..20100406
    private String              encryFolder    = "";   //路徑
    
    private int                 executeTaskMaxWaitingTime = 30;//即時執行後端作業的前端最大等待秒數

    private Map<String,String>  optionalParams      = new TreeMap<String,String>();
	public int getBrowserMaxRecord() {
		return browserMaxRecord;
	}

	public int getBrowserPageRecord() {
		return browserPageRecord;
	}

	public List<CompanyInfo> getCompanies() {
		return companies;
	}

	//2011/09/20 ... tigereye ...
	public List<CompanyInfo> getHiddencompanies() {
		return hiddencompanies;
	}
	public void setHiddencompanies(List<CompanyInfo> hiddencompanies) {
		this.hiddencompanies = hiddencompanies;
	}
	//2011/09/20 ... tigereye ...
	
	public int getQueryMaxRecord() {
		return queryMaxRecord;
	}
	
	public int getQueryPageRecord() {
		return queryPageRecord;
	}

	public void setBrowserMaxRecord(int browserMaxRecord) {
		this.browserMaxRecord = browserMaxRecord;
	}

	public void setBrowserPageRecord(int browserPageRecord) {
		this.browserPageRecord = browserPageRecord;
	}

	public void setCompanies(List<CompanyInfo> companies) {
		this.companies = companies;
	}

	public void setQueryMaxRecord(int queryMaxRecord) {
		this.queryMaxRecord = queryMaxRecord;
	}
	
	public void setQueryPageRecord(int queryPageRecord) {
		this.queryPageRecord = queryPageRecord;
	}

    public String getReportOutputFolder() {
        return reportOutputFolder;
    }

    public void setReportOutputFolder(String reportOutputFolder) {
        this.reportOutputFolder = reportOutputFolder;
    }

    public String getReportTempFolder() {
        return reportTempFolder;
    }

    public void setReportTempFolder(String reportTempFolder) {
        this.reportTempFolder = reportTempFolder;
    }
//  tigereye..20100406
    public String getEncryFolder() {
        return encryFolder;
    }
//tigereye..20100406
    public void setEncryFolder(String encryFolder) {
        this.encryFolder = encryFolder;
    }
    
    public int getExecuteTaskMaxWaitingTime() {
        return executeTaskMaxWaitingTime;
    }

    public void setExecuteTaskMaxWaitingTime(int executeTaskMaxWaitingTime) {
        this.executeTaskMaxWaitingTime = executeTaskMaxWaitingTime;
    }

	public Map<String, String> getLangs() {
		return langs;
	}

	public void setLangs(Map<String, String> langs) {
		this.langs = langs;
	}

	public Map<String, String> getOptionalParams() {
		return optionalParams;
	}

	public void setOptionalParams(Map<String, String> optionalParams) {
		this.optionalParams = optionalParams;
	}

	
}
