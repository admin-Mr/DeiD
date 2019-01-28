package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.BlackBox;
import util.Common;
import util.QueryBase;
import util.openwin.ReturnBox;

public class DSID003_DSID04_1_01 extends QueryBase {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Window windowQuery;
	@Wire
	private Listbox queryListbox;
	@Wire
	private Button btnQuery, btnExport,onqryDSID04;
	@Wire
	private Textbox qry_MODEL_NA;

	Common Common = (Common) Sessions.getCurrent().getAttribute("Common");

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
	}

	@Override
	@Listen("onCreate = Listbox")
	public void onCreateListbox(Event event) {
		// 覆寫此method，進行程式時不直接查詢
	}

	@Listen("onOpenQueryField = #windowQuery")
	public void onOpenQueryField(ForwardEvent event) {
		 final HashMap<String, Object> map = new HashMap<String, Object>();

         map.put("parentWindow", windowQuery);
         map.put("WindoWidth", "60%");    //開窗長度
         map.put("WindoHeight", "80%");   //開窗高度
         map.put("Sizable", true);                //是否可手動調整大小
         map.put("Closable", true);              //是否可關閉視窗
         map.put("Maximizable",true);       //是否可最大化視窗
         map.put("multiple", false);    // false:單選,true:多選目前不支援共用
         map.put("xmlQryID","dsid/DSID04");   //XML ID
         ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
         returnBoxList.add(new ReturnBox("MODEL_NA",qry_MODEL_NA));
         map.put("returnTextBoxList", returnBoxList);
         Executions.createComponents("/util/openwin/QueryField.zul", null, map);
	}

	@Override
	@Listen("onClick = #btnQuery")
	public void onClickbtnQuery(Event event) {
		super.executeQuery();
	}

	@Listen("onClick = #btnExport")
	public void onClickBtnExport() {
		if(qry_MODEL_NA.getText().equals("")){
			Messagebox.show("請先進行查詢!!");
			return;
		}
		HSSFWorkbook wb = null;
		Connection conn = null;
		PreparedStatement ps = null,ps1 = null,ps2 = null;	
		ResultSet rs = null,rs1 = null,rs2 = null;	
		String SQL="";
		try {
			wb = new HSSFWorkbook();
			conn = Common.getDbConnection();
			
			HSSFFont contentFont = wb.createFont();
			contentFont.setFontHeightInPoints((short)10);
			contentFont.setFontName("微軟正黑體");
			contentFont.setBoldweight((short)10);
			
			HSSFCellStyle contentStyle = wb.createCellStyle();
			contentStyle.setFont(contentFont);	
			contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
			contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			contentStyle.setTopBorderColor(HSSFColor.BLACK.index);
			
			HSSFSheet sheet = wb.createSheet("非前面片SIZE");
			sheet.setColumnWidth(0, 30*256);
			sheet.setColumnWidth(1, 15*256);
			HSSFRow row = null;
			HSSFCell cell = null;
			/*******************第一頁籤*******************/
			SQL = 	"SELECT DISTINCT DSID04.MODEL_NA,DSID04.MODEL_UPD,TO_CHAR(DSID04.LA_DATE,'YYYY/MM/DD') ELLA_DATE,TO_CHAR(DSID04.DR_DATE,'YYYY/MM/DD') ELDR_DATE,DSID04_SIZE.MAX_DAY,DSID04_SIZE.MIN_DAY "+ 
					"  FROM DSID04 "+
					"  JOIN DSID04_SIZE ON (DSID04.MODEL_NA=DSID04_SIZE.MODEL_NA) "+
					" WHERE DSID04.MODEL_NA='"+qry_MODEL_NA.getText()+"'";
			ps = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			if(rs.next()){
				/*第一行*/
				//sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
				row = sheet.createRow(0);
				cell = row.createCell(0);
				cell.setCellValue(rs.getString("MODEL_NA"));
				cell.setCellStyle(contentStyle);
				/*第二行*/
				row = sheet.createRow(1);
				cell = row.createCell(0);
				cell.setCellValue("Launch:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("ELLA_DATE"));
				cell.setCellStyle(contentStyle);
				/*第三行*/
				row = sheet.createRow(2);
				cell = row.createCell(0);
				cell.setCellValue("Drop:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("ELDR_DATE"));
				cell.setCellStyle(contentStyle);
				/*第四行*/
				row = sheet.createRow(3);
				cell = row.createCell(0);
				cell.setCellValue("UPD:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MODEL_UPD"));
				cell.setCellStyle(contentStyle);
				/*第五行*/
				row = sheet.createRow(4);
				cell = row.createCell(0);
				cell.setCellValue("Max Days");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MAX_DAY"));
				cell.setCellStyle(contentStyle);
				/*第六行*/
				row = sheet.createRow(5);
				cell = row.createCell(0);
				cell.setCellValue("Min Days");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MIN_DAY"));
				cell.setCellStyle(contentStyle);
			}
			rs.close();
			ps.close();
			
			String size="";
			for(int j=1;j<=40;j++){
				size+="S"+j+",Q"+j+",";
			}
			size=size.substring(0, size.length()-1);
			
			int i=8;
			SQL = 	"SELECT DISTINCT EL_NO,EL_NA,COLOR "+
					"  FROM DSID04_1 "+
					" WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' "+
					"   AND EXISTS(SELECT * FROM DSID04_SIZE WHERE MODEL_NA=DSID04_1.MODEL_NA AND EL_NO=DSID04_1.EL_NO) "+ 
					" ORDER BY EL_NO";
			ps = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			while(rs.next()){
				String EL_NO=rs.getString("EL_NO");
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(rs.getString("EL_NO")+":"+rs.getString("EL_NA"));
				cell.setCellStyle(contentStyle);
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("Color："+rs.getString("COLOR"));
				cell.setCellStyle(contentStyle);
				
				/*男鞋SIZE與占比*/
				i++;
				SQL = 	"SELECT "+size+" FROM DSID04_SIZE WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND SH_LAST='M' AND EL_NO='"+EL_NO+"'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("男鞋SIZE：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						cell = row.createCell(j);
						cell.setCellValue(rs1.getString("S"+j));
						cell.setCellStyle(contentStyle);
						
					}
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("男鞋預計百分比：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						cell = row.createCell(j);
						cell.setCellValue(rs1.getString("Q"+j));
						cell.setCellStyle(contentStyle);
					}
				}
				rs1.close();
				ps1.close();
				/*女鞋SIZE與占比*/
				i++;
				SQL = 	"SELECT "+size+" FROM DSID04_SIZE WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND SH_LAST='W' AND EL_NO='"+EL_NO+"'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("女鞋SIZE：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						cell = row.createCell(j);
						cell.setCellValue(rs1.getString("S"+j));
						cell.setCellStyle(contentStyle);
					}
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("女鞋預計百分比：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						cell = row.createCell(j);
						cell.setCellValue(rs1.getString("Q"+j));
						cell.setCellStyle(contentStyle);
					}
					
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("合計百分比：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						if(j<26){
							cell = row.createCell(j);
							cell.setCellFormula("sum("+Character.toString((char)(65+j))+String.valueOf(i-2)+"+"+Character.toString((char)(65+j))+String.valueOf(i)+")/2");
							cell.setCellStyle(contentStyle);
						}else{
							cell = row.createCell(j);
							cell.setCellFormula("sum(A"+Character.toString((char)(65+j-26))+String.valueOf(i-2)+"+A"+Character.toString((char)(65+j-26))+String.valueOf(i)+")/2");
							cell.setCellStyle(contentStyle);
						}
						
					}
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("最大庫存：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						if(j<26){
							cell = row.createCell(j);
							cell.setCellFormula("("+Character.toString((char)(65+j))+String.valueOf(i)+"*B4*B5)/100");
							cell.setCellStyle(contentStyle);
						}else{
							cell = row.createCell(j);
							cell.setCellFormula("(A"+Character.toString((char)(65+j-26))+String.valueOf(i)+"*B4*B5)/100");
							cell.setCellStyle(contentStyle);
						}
						
					}
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue("最小庫存：");
					cell.setCellStyle(contentStyle);
					for(int j=1;j<=40;j++){
						if(j<26){
							cell = row.createCell(j);
							cell.setCellFormula("("+Character.toString((char)(65+j))+String.valueOf(i-1)+"*B4*B6)/100");
							cell.setCellStyle(contentStyle);
						}else{
							cell = row.createCell(j);
							cell.setCellFormula("(A"+Character.toString((char)(65+j-26))+String.valueOf(i-1)+"*B4*B6)/100");
							cell.setCellStyle(contentStyle);
						}
						
					}
				}
				rs1.close();
				ps1.close();
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("實際庫存：");
				cell.setCellStyle(contentStyle);
				
				SQL = 	"SELECT * FROM DSID77 WHERE EL_NO='"+EL_NO+"' AND MODEL_NA = '"+qry_MODEL_NA.getText()+"'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					for(int j=1;j<=40;j++){
						if(j<26){
							cell = row.createCell(j);
							cell.setCellFormula("("+Character.toString((char)(65+j))+String.valueOf(i-2)+"*"+rs1.getDouble("MT_QTY")+")/100");
							cell.setCellStyle(contentStyle);
						}else{
							cell = row.createCell(j);
							cell.setCellFormula("(A"+Character.toString((char)(65+j-26))+String.valueOf(i-2)+"*"+rs1.getDouble("MT_QTY")+")/100");
							cell.setCellStyle(contentStyle);
						}
						
					}
				}else{
					for(int j=1;j<=40;j++){
						cell = row.createCell(j);
						cell.setCellValue("0");
						cell.setCellStyle(contentStyle);
					}
				}
				rs1.close();
				ps1.close();
				
				i+=2;
			}
			rs.close();
			ps.close();
			/*******************第一頁籤*******************/
			/*******************第二頁籤*******************/
			sheet = wb.createSheet("非前面片SIZE區段");
			sheet.setColumnWidth(0, 30*256);
			sheet.setColumnWidth(1, 15*256);
			
			SQL = 	"SELECT DISTINCT DSID04.MODEL_NA,DSID04.MODEL_UPD,TO_CHAR(DSID04.LA_DATE,'YYYY/MM/DD') ELLA_DATE,TO_CHAR(DSID04.DR_DATE,'YYYY/MM/DD') ELDR_DATE,DSID04_SIZE.MAX_DAY,DSID04_SIZE.MIN_DAY "+ 
					"  FROM DSID04 "+
					"  JOIN DSID04_SIZE ON (DSID04.MODEL_NA=DSID04_SIZE.MODEL_NA) "+
					" WHERE DSID04.MODEL_NA='"+qry_MODEL_NA.getText()+"'";
			ps = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			if(rs.next()){
				/*第一行*/
				//sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
				row = sheet.createRow(0);
				cell = row.createCell(0);
				cell.setCellValue(rs.getString("MODEL_NA"));
				cell.setCellStyle(contentStyle);
				/*第二行*/
				row = sheet.createRow(1);
				cell = row.createCell(0);
				cell.setCellValue("Launch:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("ELLA_DATE"));
				cell.setCellStyle(contentStyle);
				/*第三行*/
				row = sheet.createRow(2);
				cell = row.createCell(0);
				cell.setCellValue("Drop:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("ELDR_DATE"));
				cell.setCellStyle(contentStyle);
				/*第四行*/
				row = sheet.createRow(3);
				cell = row.createCell(0);
				cell.setCellValue("UPD:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MODEL_UPD"));
				cell.setCellStyle(contentStyle);
				/*第五行*/
				row = sheet.createRow(4);
				cell = row.createCell(0);
				cell.setCellValue("Max Days");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MAX_DAY"));
				cell.setCellStyle(contentStyle);
				/*第六行*/
				row = sheet.createRow(5);
				cell = row.createCell(0);
				cell.setCellValue("Min Days");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MIN_DAY"));
				cell.setCellStyle(contentStyle);
			}
			
			i=8;
			SQL = 	"SELECT DISTINCT EL_NO,EL_NA,COLOR "+ 
					"  FROM DSID04_1 "+
					" WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' "+ 
					"   AND EXISTS(SELECT * FROM DSID04_NFPSIZE WHERE MODEL_NA=DSID04_1.MODEL_NA AND EL_NO=DSID04_1.EL_NO) "+  
					" ORDER BY EL_NO";
			ps = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			while(rs.next()){
				String EL_NO=rs.getString("EL_NO");
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(rs.getString("EL_NO")+":"+rs.getString("EL_NA"));
				cell.setCellStyle(contentStyle);
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("Color："+rs.getString("COLOR"));
				cell.setCellStyle(contentStyle);
				
				SQL="SELECT COUNT(*) COUNT FROM DSID04_NFPSIZE WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND EL_NO='"+EL_NO+"'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				String SIZE[]=null;
				int Count=0;
				if(rs1.next()){
					Count=rs1.getInt("COUNT");
					SIZE=new String[Count];
				}
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("SIZE區段");
				cell.setCellStyle(contentStyle);
				
				SQL="SELECT SIZES FROM DSID04_NFPSIZE WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND EL_NO='"+EL_NO+"' ORDER BY TO_NUMBER(REGEXP_SUBSTR(SIZES,'[^-]+'))";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				int j=1;
				while(rs1.next()){
					SIZE[j-1]=rs1.getString("SIZES");
					cell = row.createCell(j);
					cell.setCellValue(SIZE[j-1]);
					cell.setCellStyle(contentStyle);
					j++;
				}
				rs1.close();
				ps1.close();
				
				Double MODEL_SIZE[]=new Double[40];
				Double MODEL_QTY[]=new Double[40];
				/*抓取SIZE資料*/
				SQL=	"SELECT S1,S2,S3,S4,S5,S6,S7,S8,S9,S10, "+
						"       S11,S12,S13,S14,S15,S16,S17,S18,S19,S20, "+
						"       S21,S22,S23,S24,S25,S26,S27,S28,S29,S30, "+
						"       S31,S32,S33,S34,S35,S36,S37,S38,S39,S40 "+
						"  FROM DSID04_SIZE "+
						" WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' "+ 
						"   AND EL_NO='"+EL_NO+"' "+
						"   AND SH_LAST='M'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					for(int k=0;k<40;k++){
						MODEL_SIZE[k]=rs1.getDouble("S"+(k+1));
					}
				}
				rs1.close();
				ps1.close();
				/*抓取SIZE資料*/
				/*抓取QTY資料*/
				SQL=	"SELECT SUM(Q1)/2 Q1,SUM(Q2)/2 Q2,SUM(Q3)/2 Q3,SUM(Q4)/2 Q4,SUM(Q5)/2 Q5,SUM(Q6)/2 Q6,SUM(Q7)/2 Q7,SUM(Q8)/2 Q8,SUM(Q9)/2 Q9,SUM(Q10)/2 Q10, "+
						"       SUM(Q11)/2 Q11,SUM(Q12)/2 Q12,SUM(Q13)/2 Q13,SUM(Q14)/2 Q14,SUM(Q15)/2 Q15,SUM(Q16)/2 Q16,SUM(Q17)/2 Q17,SUM(Q18)/2 Q18,SUM(Q19)/2 Q19,SUM(Q20)/2 Q20, "+
						"       SUM(Q21)/2 Q21,SUM(Q22)/2 Q22,SUM(Q23)/2 Q23,SUM(Q24)/2 Q24,SUM(Q25)/2 Q25,SUM(Q26)/2 Q26,SUM(Q27)/2 Q27,SUM(Q28)/2 Q28,SUM(Q29)/2 Q29,SUM(Q30)/2 Q30, "+
						"       SUM(Q31)/2 Q31,SUM(Q32)/2 Q32,SUM(Q33)/2 Q33,SUM(Q34)/2 Q34,SUM(Q35)/2 Q35,SUM(Q36)/2 Q36,SUM(Q37)/2 Q37,SUM(Q38)/2 Q38,SUM(Q39)/2 Q39,SUM(Q40)/2 Q40 "+
						"  FROM DSID04_SIZE "+
						" WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' "+ 
						"   AND EL_NO='"+EL_NO+"'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					for(int k=0;k<40;k++){
						MODEL_QTY[k]=rs1.getDouble("Q"+(k+1));
					}
				}
				rs1.close();
				ps1.close();
				/*抓取QTY資料*/
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("Size區段百分比：");
				cell.setCellStyle(contentStyle);
			
				for(int k=0;k<Count;k++){
					Double SIZE_PRE=0.0;
					for(int l=0;l<40;l++){
						if(Double.valueOf(SIZE[k].split("-")[0])<=MODEL_SIZE[l] && Double.valueOf(SIZE[k].split("-")[1])>=MODEL_SIZE[l]){
							SIZE_PRE+=MODEL_QTY[l];
						}
					}
					cell = row.createCell(k+1);
					cell.setCellValue(SIZE_PRE);
					cell.setCellStyle(contentStyle);
				}
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("最大庫存：");
				cell.setCellStyle(contentStyle);
				for(int l=1;l<=Count;l++){
					cell = row.createCell(l);
					cell.setCellFormula("(B4*B5*"+Character.toString((char)(65+l))+i+")/100");
					cell.setCellStyle(contentStyle);
				}
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("最小庫存：");
				cell.setCellStyle(contentStyle);
				for(int l=1;l<=Count;l++){
					cell = row.createCell(l);
					cell.setCellFormula("(B4*B6*"+Character.toString((char)(65+l))+(i-1)+")/100");
					cell.setCellStyle(contentStyle);
				}
			
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("實際庫存：");
				cell.setCellStyle(contentStyle);
				
				SQL = 	"SELECT * FROM DSID77 WHERE EL_NO='"+EL_NO+"' AND MODEL_NA = '"+qry_MODEL_NA.getText()+"'";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					for(int l=1;l<=Count;l++){
							cell = row.createCell(l);
							cell.setCellFormula("("+Character.toString((char)(65+l))+String.valueOf(i-2)+"*"+rs1.getDouble("MT_QTY")+")/100");
							cell.setCellStyle(contentStyle);
					}
				}else{
					for(int l=1;l<=Count;l++){
						cell = row.createCell(l);
						cell.setCellValue("0");
						cell.setCellStyle(contentStyle);
					}
				}
				rs1.close();
				ps1.close();
				
				i+=2;
			}
			rs.close();
			ps.close();
			/*******************第二頁籤*******************/
			/*******************第三頁籤*******************/
			sheet = wb.createSheet("前面片");
			sheet.setColumnWidth(0, 30*256);
			sheet.setColumnWidth(1, 15*256);
			
			SQL = 	"SELECT DISTINCT DSID04.MODEL_NA,DSID04.MODEL_UPD,TO_CHAR(DSID04.LA_DATE,'YYYY/MM/DD') ELLA_DATE,TO_CHAR(DSID04.DR_DATE,'YYYY/MM/DD') ELDR_DATE,DSID04_SIZE.MAX_DAY,DSID04_SIZE.MIN_DAY "+ 
					"  FROM DSID04 "+
					"  JOIN DSID04_SIZE ON (DSID04.MODEL_NA=DSID04_SIZE.MODEL_NA) "+
					" WHERE DSID04.MODEL_NA='"+qry_MODEL_NA.getText()+"'";
			ps = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			if(rs.next()){
				/*第一行*/
				//sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
				row = sheet.createRow(0);
				cell = row.createCell(0);
				cell.setCellValue(rs.getString("MODEL_NA"));
				cell.setCellStyle(contentStyle);
				/*第二行*/
				row = sheet.createRow(1);
				cell = row.createCell(0);
				cell.setCellValue("Launch:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("ELLA_DATE"));
				cell.setCellStyle(contentStyle);
				/*第三行*/
				row = sheet.createRow(2);
				cell = row.createCell(0);
				cell.setCellValue("Drop:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("ELDR_DATE"));
				cell.setCellStyle(contentStyle);
				/*第四行*/
				row = sheet.createRow(3);
				cell = row.createCell(0);
				cell.setCellValue("UPD:");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MODEL_UPD"));
				cell.setCellStyle(contentStyle);
				/*第五行*/
				row = sheet.createRow(4);
				cell = row.createCell(0);
				cell.setCellValue("Max Days");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MAX_DAY"));
				cell.setCellStyle(contentStyle);
				/*第六行*/
				row = sheet.createRow(5);
				cell = row.createCell(0);
				cell.setCellValue("Min Days");
				cell.setCellStyle(contentStyle);

				cell = row.createCell(1);
				cell.setCellValue(rs.getString("MIN_DAY"));
				cell.setCellStyle(contentStyle);
			}
			rs.close();
			ps.close();
			
			i=8;
			
			
			SQL="SELECT DISTINCT MODEL_NA,COLOR FROM DSID04_2 WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"'";
			ps = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			while(rs.next()){
				int j=1;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("Color："+rs.getString("COLOR"));
				cell.setCellStyle(contentStyle);
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("上線前");
				cell.setCellStyle(contentStyle);
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("SIZE區段");
				cell.setCellStyle(contentStyle);
				
				String COLOR=rs.getString("COLOR");
				SQL="SELECT REPLACE(EL_NA,'#') SIZES FROM DSID04_2 WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND COLOR='"+COLOR+"' ORDER BY TO_NUMBER(REGEXP_SUBSTR(EL_NA,'[^-]+'))";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				while(rs1.next()){
					cell = row.createCell(j);
					cell.setCellValue(rs1.getString("SIZES"));
					cell.setCellStyle(contentStyle);
					j++;
				}
				rs1.close();
				ps1.close();
				
				Boolean IsFirst=true;
				int m=0;
				SQL="SELECT EL_NO,REPLACE(EL_NA,'#') SIZES FROM DSID04_2 WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND COLOR='"+COLOR+"' ORDER BY TO_NUMBER(REGEXP_SUBSTR(EL_NA,'[^-]+'))";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				while(rs1.next()){
					Double MODEL_SIZE[]=new Double[40];
					Double MODEL_QTY[]=new Double[40];
					/*抓取SIZE資料*/
					SQL=	"SELECT S1,S2,S3,S4,S5,S6,S7,S8,S9,S10, "+
							"       S11,S12,S13,S14,S15,S16,S17,S18,S19,S20, "+
							"       S21,S22,S23,S24,S25,S26,S27,S28,S29,S30, "+
							"       S31,S32,S33,S34,S35,S36,S37,S38,S39,S40 "+
							"  FROM DSID04_SIZE "+
							" WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' "+ 
							"   AND EL_NO='"+rs1.getString("EL_NO")+"' "+
							"   AND SH_LAST='M'";
					ps2 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs2 = ps2.executeQuery();
					if(rs2.next()){
						for(int k=0;k<40;k++){
							MODEL_SIZE[k]=rs2.getDouble("S"+(k+1));
						}
					}
					rs2.close();
					ps2.close();
					/*抓取SIZE資料*/
					/*抓取QTY資料*/
					SQL=	"SELECT SUM(Q1)/2 Q1,SUM(Q2)/2 Q2,SUM(Q3)/2 Q3,SUM(Q4)/2 Q4,SUM(Q5)/2 Q5,SUM(Q6)/2 Q6,SUM(Q7)/2 Q7,SUM(Q8)/2 Q8,SUM(Q9)/2 Q9,SUM(Q10)/2 Q10, "+
							"       SUM(Q11)/2 Q11,SUM(Q12)/2 Q12,SUM(Q13)/2 Q13,SUM(Q14)/2 Q14,SUM(Q15)/2 Q15,SUM(Q16)/2 Q16,SUM(Q17)/2 Q17,SUM(Q18)/2 Q18,SUM(Q19)/2 Q19,SUM(Q20)/2 Q20, "+
							"       SUM(Q21)/2 Q21,SUM(Q22)/2 Q22,SUM(Q23)/2 Q23,SUM(Q24)/2 Q24,SUM(Q25)/2 Q25,SUM(Q26)/2 Q26,SUM(Q27)/2 Q27,SUM(Q28)/2 Q28,SUM(Q29)/2 Q29,SUM(Q30)/2 Q30, "+
							"       SUM(Q31)/2 Q31,SUM(Q32)/2 Q32,SUM(Q33)/2 Q33,SUM(Q34)/2 Q34,SUM(Q35)/2 Q35,SUM(Q36)/2 Q36,SUM(Q37)/2 Q37,SUM(Q38)/2 Q38,SUM(Q39)/2 Q39,SUM(Q40)/2 Q40 "+
							"  FROM DSID04_SIZE "+
							" WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' "+ 
							"   AND EL_NO='"+rs1.getString("EL_NO")+"'";
					ps2 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs2 = ps2.executeQuery();
					if(rs2.next()){
						for(int k=0;k<40;k++){
							MODEL_QTY[k]=rs2.getDouble("Q"+(k+1));
						}
					}else{
						for(int k=0;k<40;k++){
							MODEL_QTY[k]=0.0;
						}
					}
					rs2.close();
					ps2.close();
					/*抓取QTY資料*/
					
					i++;
					if(IsFirst){
						row = sheet.createRow(i);
						cell = row.createCell(0);
						cell.setCellValue("Size區段百分比：");
						cell.setCellStyle(contentStyle);
					}else{
						row = sheet.getRow(i);
					}
				
					Double SIZE_PRE=0.0;
					for(int l=0;l<40;l++){
						if(MODEL_SIZE[l]!=null && Double.valueOf(rs1.getString("SIZES").split("-")[0])<=MODEL_SIZE[l] && Double.valueOf(rs1.getString("SIZES").split("-")[1])>=MODEL_SIZE[l]){
							SIZE_PRE+=MODEL_QTY[l];
						}
					}
					cell = row.createCell(m+1);
					cell.setCellValue(SIZE_PRE);
					cell.setCellStyle(contentStyle);
					
					i++;
					if(IsFirst){
						row = sheet.createRow(i);
						cell = row.createCell(0);
						cell.setCellValue("最大庫存：");
						cell.setCellStyle(contentStyle);
					}else{
						row = sheet.getRow(i);
					}
						
					cell = row.createCell(m+1);
					cell.setCellFormula("(B4*B5*"+Character.toString((char)(65+m+1))+i+")/100");
					cell.setCellStyle(contentStyle);
					
					i++;
					if(IsFirst){
						row = sheet.createRow(i);
						cell = row.createCell(0);
						cell.setCellValue("最小庫存：");
						cell.setCellStyle(contentStyle);
					}else{
						row = sheet.getRow(i);
					}
					cell = row.createCell(m+1);
					cell.setCellFormula("(B4*B6*"+Character.toString((char)(65+m+1))+(i-1)+")/100");
					cell.setCellStyle(contentStyle);
				
					i++;
					if(IsFirst){
						row = sheet.createRow(i);
						cell = row.createCell(0);
						cell.setCellValue("實際庫存：");
						cell.setCellStyle(contentStyle);
					}else{
						row = sheet.getRow(i);
					}
					
					SQL = 	"SELECT * FROM DSID77 WHERE EL_NO='"+rs1.getString("EL_NO")+"' AND MODEL_NA = '"+qry_MODEL_NA.getText()+"'";
					ps2 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs2 = ps2.executeQuery();
					if(rs2.next()){
						cell = row.createCell(m+1);
						cell.setCellFormula("("+Character.toString((char)(65+m+1))+String.valueOf(i-2)+"*"+rs2.getDouble("MT_QTY")+")/100");
						cell.setCellStyle(contentStyle);
					}else{
						cell = row.createCell(m+1);
						cell.setCellValue("0");
						cell.setCellStyle(contentStyle);
					}
					rs2.close();
					ps2.close();
					IsFirst=false;
					m++;
					i-=4;
				}
				i+=6;
				
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("上線後");
				cell.setCellStyle(contentStyle);
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("SIZE區段");
				cell.setCellStyle(contentStyle);
				
				int count=0;
				j=1;
				SQL="SELECT REPLACE(EL_NA,'#') SIZES FROM DSID04_2 WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND COLOR='"+COLOR+"' ORDER BY TO_NUMBER(REGEXP_SUBSTR(EL_NA,'[^-]+'))";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				while(rs1.next()){
					cell = row.createCell(j);
					cell.setCellValue(rs1.getString("SIZES"));
					cell.setCellStyle(contentStyle);
					j++;
					count++;
				}
				rs1.close();
				ps1.close();
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("Size區段百分比：");
				cell.setCellStyle(contentStyle);
				
				j=1;
				SQL="SELECT COLOR_PRE FROM DSID04_2 WHERE MODEL_NA='"+qry_MODEL_NA.getText()+"' AND COLOR='"+COLOR+"' ORDER BY TO_NUMBER(REGEXP_SUBSTR(EL_NA,'[^-]+'))";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				while(rs1.next()){
					cell = row.createCell(j);
					cell.setCellValue(rs1.getDouble("COLOR_PRE")*100);
					cell.setCellStyle(contentStyle);
					j++;
				}
				rs1.close();
				ps1.close();
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("最大庫存：");
				cell.setCellStyle(contentStyle);
				for(int l=1;l<=count;l++){
					cell = row.createCell(l);
					cell.setCellFormula("(B4*B5*"+Character.toString((char)(65+l))+i+")/100");
					cell.setCellStyle(contentStyle);
				}
				
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("最小庫存：");
				cell.setCellStyle(contentStyle);
				for(int l=1;l<=count;l++){
					cell = row.createCell(l);
					cell.setCellFormula("(B4*B6*"+Character.toString((char)(65+l))+(i-1)+")/100");
					cell.setCellStyle(contentStyle);
				}
			
				i++;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue("實際庫存：");
				cell.setCellStyle(contentStyle);
				
				int l=1;
				SQL = 	"SELECT DSID04_2.EL_NO,NVL(DSID77.MT_QTY,0) MT_QTY "+
						"  FROM DSID04_2 "+
						"  LEFT JOIN DSID77 ON (DSID04_2.MODEL_NA=DSID77.MODEL_NA AND DSID04_2.EL_NO=DSID77.EL_NO) "+
						" WHERE DSID04_2.MODEL_NA='"+qry_MODEL_NA.getText()+"' AND DSID04_2.COLOR='"+COLOR+"' "+
						" ORDER BY TO_NUMBER(REGEXP_SUBSTR(DSID04_2.EL_NA,'[^-]+'))";
				ps1 = conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs1 = ps1.executeQuery();
				while(rs1.next()){
					cell = row.createCell(l);
					cell.setCellFormula("("+Character.toString((char)(65+l))+String.valueOf(i-2)+"*"+rs1.getDouble("MT_QTY")+")/100");
					cell.setCellStyle(contentStyle);
					l++;
				}
				rs1.close();
				ps1.close();
				
				i+=2;
				
			}
			rs.close();
			ps.close();
			/*******************第三頁籤*******************/
			ByteArrayOutputStream  stream = new ByteArrayOutputStream();
			wb.setForceFormulaRecalculation(true);
	        wb.write(stream);
	        Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
			
			byte[] content = stream.toByteArray();
		    InputStream is = new ByteArrayInputStream(content);

		    //儲存位置、名稱
			Filedownload.save(is, "application/xls", format1.format(date));
			is.close();
			stream.flush();
			stream.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}finally{
			BlackBox.connectionClose(conn);
		}
	}

	/********************************************************basic setting********************************************************/
	@Override
	protected Window getRootWindow() {
		return windowQuery;
	}

	@Override
	protected Class getQueryClass() {
		return DSID003_DSID04_1_01.class;
	}

	@Override
	protected Class getEntityClass() {
		return null;
	}

	@Override
	protected ArrayList<String> getKeyName() {
		return null;
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
	protected void resetEditArea(Object entity) {
		// Don't display value
	}
	
	private String getSQL() {
		String sSql = 	"WITH cte1 AS ( "+
						" SELECT DSID04_1.MODEL_NA,DSID04_1.EL_NO,DSID04_NFPSIZE.SEQ,DSID04_1.YIELD,DSID04_1.COLOR_PRE,DSID04_NFPSIZE.SIZES "+ 
						"   FROM DSID04_1 "+
						"   JOIN DSID04_NFPSIZE ON (DSID04_1.MODEL_NA=DSID04_NFPSIZE.MODEL_NA AND DSID04_1.EL_NO=DSID04_NFPSIZE.EL_NO) "+
						"  WHERE DSID04_1.MODEL_NA='"+qry_MODEL_NA.getText()+"' "+
						" )";
		
		System.out.println("sSql = " + sSql);
		return sSql;
	}

	@Override
	public String getOrderBy() {
		return " ORDER BY MODEL_NA ,EL_NO, SEQ";
	}

	@Override
	public String getQueryPagingBase() {
		return getSQL() + "select * from cte1 ";
	}

	@Override
	public String getQueryOneColBase() {
		return getSQL() + "SELECT SPEC_NO from cte1 ";
	}

	@Override
	public String getQueryResultSizeBase() {
		return getSQL() + " SELECT count(1) FROM cte1 ";
	}

	@Override
	protected String getWhereConditionals() {
		String sWhere = "";
		return sWhere;
	}

	/** Query SQL **/

	@Override
	protected List getCustList() {
		List<temp> volist = new ArrayList<temp>();
		Query qry = getQueryPagingbySize();

		if (qry != null) {
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {

				Object[] values = (Object[]) i.next();
				temp entity = new temp();
				entity.setMODEL_NA((String) values[0]);
				entity.setCOLOR((String) values[1]);
				entity.setEL_NO((String) values[2]);
				entity.setYIELD((String) values[3]);
				entity.setCOLOR_PRE((String) values[4]);
				entity.setSIZES((String) values[5]);
				volist.add(entity);
			}
		}
		return volist;
	}

	public class temp implements Serializable {
		private static final long serialVersionUID = 1L;
		private String MODEL_NA;
		private String COLOR;
		private String EL_NO;
		private String YIELD;
		private String COLOR_PRE;
		private String SIZES;
		/*型體*/
		public void setMODEL_NA(String MODEL_NA) {
			this.MODEL_NA = MODEL_NA;
		}
		public String getMODEL_NA() {
			return MODEL_NA;
		}
		/*顏色*/
		public void setCOLOR(String COLOR) {
			this.COLOR = COLOR;
		}
		public String getCOLOR() {
			return COLOR;
		}
		/*材料編號*/
		public void setEL_NO(String EL_NO) {
			this.EL_NO = EL_NO;
		}
		public String getEL_NO() {
			return EL_NO;
		}
		/*一雙鞋用量*/
		public void setYIELD(String YIELD) {
			this.YIELD = YIELD;
		}
		public String getYIELD() {
			return YIELD;
		}
		/*顏色百分比*/
		public void setCOLOR_PRE(String COLOR_PRE) {
			this.COLOR_PRE = COLOR_PRE;
		}
		public String getCOLOR_PRE() {
			return COLOR_PRE;
		}
		/*SIZE區段*/
		public void setSIZES(String SIZES) {
			this.SIZES = SIZES;
		}
		public String getSIZES() {
			return SIZES;
		}

	}
}
