package ds.dsid.program;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.UploadContext;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dsid.domain.DSID03;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import util.BlackBox;
import util.Common;
import util.ComponentColumn;
import util.QueryBase;
/**
 * 色卡封面作業
 * @author johnny.huang
 *
 */
@SuppressWarnings("serial")
public class DSID03M_Import01 extends QueryBase {
	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Window windowQuery;
	@Wire
	private Vlayout pics;
	@Wire
	private Listbox queryListbox;
	@Wire
	private Button btnQuery, btnPrint, btnExport, btnMODEL_NAME, btnSEASON, btnSTAGE, btnSPEC_NO,btnConfirmabc,btnCancelabc,btnUpload,btnuploadimg;
	@Wire
	private Textbox  txtSEASON, txtSTAGE, txtSPEC_NO,txt_MODEL_NAME;
	@Wire
	private Label lbSTAGE;
	protected Window parentWindow;
	public String filename1="tesatabc";
	org.zkoss.util.media.Media mediatemp = null;
	final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");	
	SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy/MM/dd");
	String siReportPath, sfileName;
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	
	
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		Window ww = (Window) window;
		ww.setWidth("400px");
		ww.setHeight("300px");
		
		final Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
		Map.Entry<String, Integer> entry = it.next();
		String key = entry.getKey();
		Object value = entry.getValue();
		System.out.println("key = " + key + "; value = " + value);
		} 
		setParentWindow((Window) exec.getArg().get("parentWindow"));
	}
	

	@Listen("onUpload = #btnUpload")
	public void onClickbtnUpload(Event event){
		System.out.println("=============btnUpload================");
		 Desktop desktop = this.getRootWindow().getDesktop(); 
	      
		 org.zkoss.zk.ui.util.Configuration conf = desktop.getWebApp().getConfiguration();  
		conf.setMaxUploadSize(1024); 
		//解决中文问题，如果上传的文件中有中文 
		conf.setUploadCharset("utf-8"); 
		//如果文本文件的话，返回的实际是AMedia类型 
		//Media media = Fileupload.get();  
		//btnUpload.get
		org.zkoss.util.media.Media media =((UploadEvent)event).getMedia(); 
		String fileName = media.getName();
		filename1 = fileName;
		setFilename1("sdddddddddddddddddddddd="+filename1);
		System.out.println(fileName+"============"+filename1);
		//((Textbox)getRootWindow().getFellow("txtMODEL_NAME")).setValue(fileName);
		((Label)getRootWindow().getFellow("showfilename")).setValue(fileName);
		
		mediatemp = media;
		//importExcel(media);
//        try {
//			//this.uploadFileMedia(media);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 
	 * 確定按鍵事件
	 * 
	 * @param event 
	 */
	@Listen("onClick = #btnConfirmabc")
	public void onClickbtnComfig(Event event){
		String MODEL_NAME = txt_MODEL_NAME.getValue();
		if(mediatemp == null){
			Messagebox.show(Labels.getLabel("DSID.MSG0048")+Labels.getLabel("PUBLIC.MSG0003")+"！！", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);	
			return;
		}
		if(MODEL_NAME == null || "".equals(MODEL_NAME)){
			Messagebox.show(Labels.getLabel("DSID01M.MODEL_NA")+Labels.getLabel("PUBLIC.MSG0003")+"！！", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);	
			return;
		}
		
		
		/**
		 * //Lgc Grp
private String PART_NAME;
//Material Usage
private String MU;
//Fct Unique ID (FUI)
private String FU_ID;
// Vendor
private String VENDOR;
// Component Name
private String CP_NAME;
//  Material Name (included size breaks if any)
private String EL_NAME;
//Color Code
private String COLOR_CODE;
		 */
		
		/*

		filename1="asdffffffffffffff";
		//((Textbox)getRootWindow().getFellow("txtMODEL_NAME")).setValue("eeeeeeeeeeeeeeeeeeeeeeeeeee");
		System.out.println("========abcdefg========"+mediatemp);
		List<List<String>> inlist = importExcel(mediatemp);
		for(int i=0;i<inlist.size();i++){
			List templist = inlist.get(i);
			
			String sql ="insert into DSID03(UNIQUEID,PART_NAME,MU,FU_ID,VENDOR,CP_NAME,EL_NAME,COLOR_CODE,CREATE_DATE,UP_DATE,CREATE_USER,STATUS) values(seq_dsid03.Nextval,?,?,?,?,?,?,?,sysdate,sysdate,'"+_userInfo.getAccount()+"',100)";
			try {
				boolean flag = Common.executeSQLStatement(sql, templist);
				System.out.println("===="+i+"==="+flag);
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Common.executeSQLStatement(sql, params)
		 * 
		 * */
		List<List<Object>> inlist = importExcel(mediatemp);
		String sql ="insert into DSID03(UNIQUEID,PART_NAME,MU,FU_ID,VENDOR,CP_NAME,EL_NAME,COLOR_CODE,YIELD,MODEL_NAME,CREATE_DATE,UP_DATE,CREATE_USER,STATUS)"
		+" values(seq_dsid03.Nextval,?,?,?,?,?,?,?,?,'"+MODEL_NAME+"',sysdate,sysdate,'"+_userInfo.getAccount()+"',100)";
		DSIDN08M_DBManger.batchUpdate(sql,inlist);
		
		System.out.println("============parentWindow=========="+parentWindow);
		if (parentWindow != null)
			Events.sendEvent(new Event("onQueryWindowSenddef", parentWindow,mediatemp.getName()));
	//	Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);		
		Messagebox.show(Labels.getLabel("DSID.MSG0047"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);				
		getRootWindow().detach();
	}
	

	/**
	 * 取消按鍵事件
	 * 
	 * @param event
	 */
	@Listen("onClick = #btnCancelabc")
	public void onClickbtnCancel(Event event){
		getRootWindow().detach();
	}

	@Override
	protected String getWhereConditionals() {
		
		String sWhere = " AND SPEC_CFM = 'Y' ";
		
		return sWhere;
	}

	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}
	
	
	@Override
	protected Class getQueryClass() {
		return DSID03M_Import01.class;
	}
	
	
	@Override
	protected Class getEntityClass() {
		return DSID03.class;
	}
	
	
	@Override
	protected ArrayList<String> getKeyName() {
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("ZK_ID");	
		return masterKey;
	}
	
	
	@Override
	protected ArrayList<String> getKeyValue(Object entity) {
		return null;
	}
	
	
	@Override
	protected String getPagingId() {
		return "pagingCourse";
	}
	
	
	@Override
	protected int getPageSize() {
		return 10;
	}
	


	
	@Override
	protected List getCustList() {
		return null;
	}
	

	@Override
	public String getOrderBy() {
		return " ORDER BY ZK_ID ";
	}
	
	public void uploadFileMedia(Object fileMedia) throws MalformedURLException {  
        
        
        if (fileMedia != null) {  
  
            Media media = (Media) fileMedia;  
            String fileName = media.getName();  
            String path = "c:\\";//System.getProperty("catalina.home");   
            String filePath=path+ "/TempFiles"; //文件保存路徑  
              
            java.io.File file = new java.io.File(filePath);  
            if (!file.exists()){  
                file.mkdir();  
            }  
              
            filePath=filePath+"/" + fileName;  
            uploadFile(filePath,media);
        }
    }  
  
    public static long uploadFile(String filePath, Media media) {  
        long intRet = 0;  
        try {  
            if (media.getContentType().toLowerCase().startsWith("text")) {  
                Reader r = media.getReaderData();  
                File f = new File(filePath);  
                if (!f.exists()) {  
                    f.createNewFile();  
                }  
                Files.copy(f, r, null);  
                Files.close(r);  
                intRet = f.length();  
  
            } else {  
                java.io.FileOutputStream os = new java.io.FileOutputStream(  
                        filePath);  
                int bytesRead = 0;  
                byte[] buffer = new byte[1024];  
  
                java.io.InputStream ins = media.getStreamData();  
                while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {  
                    os.write(buffer, 0, bytesRead);  
                    intRet += bytesRead;  
                }  
                os.flush();  
                os.close();  
                ins.close();  
            }  
        } catch (Exception e) {  
            String msg = "Failed to upload " + filePath;  
            throw new RuntimeException(msg);  
        }  
  
        return intRet;  
  
    }     
    
    public  List<List<Object>>  importExcel(Media media){
     List<Object[]> list = 	DSID03M_Import01_Excel.importExcel(media.getStreamData(), 0,13);
     List<List<Object>> relist = new ArrayList<List<Object>>();
     for(int i=0;i<list.size();i++){
    	 List<Object> strlist = new ArrayList<Object>();
    	 System.out.println("========="+i+"==========");
    	 Object [] obj = list.get(i);
    	 for(int x=0;x<obj.length;x++){
    		 strlist.add(String.valueOf(obj[x]));
    		 System.out.println(x+"----"+String.valueOf(obj[x]));
    	 }
    	 relist.add(strlist);
    	 System.out.println();
    	 System.out.println();
     }
     return relist;
    }


	public String getFilename1() {
		System.out.println("---------getFileName---------------------");
		return filename1;
	}


	public void setFilename1(String filename1) {
		System.out.println("---------setFileName---------------------");
		this.filename1 = filename1;
	}


	public Window getParentWindow() {
		return parentWindow;
	}


	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}


	@Override
	protected void resetEditArea(Object entity) {
		// TODO Auto-generated method stub
		
	}
    
    
}
