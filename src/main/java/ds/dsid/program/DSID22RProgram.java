package ds.dsid.program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.dsid.domain.GENERIC;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID22RProgram extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnExport,btnQuery;
	@Wire
	private Datebox po_date1,po_date2,po_date3,po_date4;
	@Wire
	private Textbox txtMODEL_NA,txtPO_NO;
	@Wire
	private Radio type_radio1,type_radio2,type_radio3,type_radio4;	
	@Wire
	private Radio type1,type2,type3;
	@Wire
	Listbox ListBox;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

//		doSearch();
	}
	
	@Listen("onClick = #btnExport")
	public void onClickbtnExport(Event event) throws SQLException{		

		String type="";
		if(type1.isSelected()){
			type="1";
		}
		if(type2.isSelected()){
			type="2";
		}
		if(type3.isSelected()){
			type="3";
		}
		
		
		String Sql1=Getsql1();
		String Sql2=Getsql2();

		DSID22_1RTask.ExcelExport(type,Sql1,Sql2);
		
	}

	 private String Getsql2() {
		// TODO Auto-generated method stub
			String ExSql="",ExSql2="";
			if(type_radio2.isSelected()){
				ExSql2=" AND PC_REDATE IS NOT NULL AND PO_QTY-PC_QTY =0";
			}
			if(type_radio3.isSelected()){
				ExSql2=" AND PC_REDATE IS NULL ";
			}
			if(type_radio4.isSelected()){
				ExSql2=" AND PC_REDATE IS NOT NULL AND PO_QTY-PC_QTY >0  ";
			}
			
			String MODEL_NA=txtMODEL_NA.getValue().trim();
			Date START=po_date1.getValue();
			Date END =po_date2.getValue();
			Date START2=po_date3.getValue();
			Date END2 =po_date4.getValue();
			String PO_NO=txtPO_NO.getValue().trim();
			
			if(START!=null&&END!=null){
				ExSql=" AND TO_CHAR(A.PO_DATE,'YYYY/MM/DD') BETWEEN '"+sdf.format(START)+"' AND '"+sdf.format(END)+"'";
			}
			if(START2!=null&&END2!=null){
				ExSql2=" AND PC_REDATE BETWEEN '"+sdf.format(START2)+"' AND '"+sdf.format(END2)+"'";
			}
			if(!"".equals(MODEL_NA)){
				ExSql=" AND A.STOCK_MARK='"+MODEL_NA+"'";
			}
			if(!"".equals(PO_NO)){
				ExSql=" AND B.PO_NO='"+PO_NO+"'";
			}
		 
			String Sql2="SELECT A.*,PO_QTY-PC_QTY QTY FROM  (SELECT B.PO_NO,C.SU_NA,B.EL_NO,D.EL_CNAME,A.STOCK_MARK,D.CLR,B.PO_QTY,A.PO_DATE,B.PO_REDATE,\n" +
					"(SELECT TO_CHAR(MAX(PC_REDATE),'YYYY/MM/DD') FROM DSIDN08@FTLDB00.DEANSHOES.COM WHERE PO_NO=A.PO_NO AND EL_NO=B.EL_NO ) PC_REDATE,\n" + 
					"NVL((SELECT SUM(PC_QTY) FROM DSIDN08@FTLDB00.DEANSHOES.COM WHERE PO_NO=A.PO_NO AND EL_NO=B.EL_NO),'0') PC_QTY\n" + 
					"FROM DSPO05 A,DSPO06 B,DSSU01 C,DSEL00 D WHERE A.PO_NO=B.PO_NO AND A.SU_NO=C.SU_NO AND B.EL_NO=D.EL_NO AND C.SU_NA NOT LIKE '%臺照%' \n" + 
					"AND A.PO_PURNO IS NULL  AND A.PO_NO LIKE 'IGM%' AND B.PO_QTY!=0 "+ExSql+"\n" + 
					"ORDER BY PO_NO,B.PO_REDATE) A WHERE 1=1 "+ExSql2;
		return Sql2;
			
	}

	private String Getsql1() {
		// TODO Auto-generated method stub
		String ExSql="",ExSql2="";
		if(type_radio2.isSelected()){
			ExSql2=" AND PC_REDATE IS NOT NULL AND PO_QTY-PC_QTY =0";
		}
		if(type_radio3.isSelected()){
			ExSql2=" AND PC_REDATE IS NULL ";
		}
		if(type_radio4.isSelected()){
			ExSql2=" AND PC_REDATE IS NOT NULL AND PO_QTY-PC_QTY >0  ";
		}
		
		String MODEL_NA=txtMODEL_NA.getValue().trim();
		Date START=po_date1.getValue();
		Date END =po_date2.getValue();
		Date START2=po_date3.getValue();
		Date END2 =po_date4.getValue();
		String PO_NO=txtPO_NO.getValue().trim();
		
		if(START!=null&&END!=null){
			ExSql=" AND TO_CHAR(A.PO_DATE,'YYYY/MM/DD') BETWEEN '"+sdf.format(START)+"' AND '"+sdf.format(END)+"'";
		}
		if(START2!=null&&END2!=null){
			ExSql2=" AND PC_REDATE BETWEEN '"+sdf.format(START2)+"' AND '"+sdf.format(END2)+"'";
		}
		if(!"".equals(MODEL_NA)){
			ExSql=" AND A.STOCK_MARK='"+MODEL_NA+"'";
		}
		if(!"".equals(PO_NO)){
			if(PO_NO.contains("NKID")){
				ExSql=" AND A.PO_PURNO='"+PO_NO+"'";
			}else{
				ExSql=" AND B.PO_NO='"+PO_NO+"'";
			}
		}
		String Sql1="SELECT A.*,PO_QTY-PC_QTY QTY FROM  (SELECT A.PO_PURNO,B.PO_NO,C.SU_NA,B.EL_NO,D.EL_CNAME,A.STOCK_MARK,D.CLR,B.PO_QTY,A.PO_DATE,B.PO_REDATE,\n" +
						"(SELECT TO_CHAR(MAX(PC_REDATE),'YYYY/MM/DD') FROM DSIDN08@FTLDB00.DEANSHOES.COM WHERE PO_NO=A.PO_NO AND EL_NO=B.EL_NO ) PC_REDATE,\n" + 
						"NVL((SELECT SUM(PC_QTY) FROM DSIDN08@FTLDB00.DEANSHOES.COM WHERE PO_NO=A.PO_NO AND EL_NO=B.EL_NO),'0') PC_QTY\n" + 
						"FROM DSPO05 A,DSPO06 B,DSSU01 C,DSEL00 D WHERE A.PO_NO=B.PO_NO AND A.SU_NO=C.SU_NO AND B.EL_NO=D.EL_NO AND C.SU_NA NOT LIKE '%臺照%' \n" + 
						"AND A.PO_PURNO IS NOT NULL  AND A.PO_NO LIKE 'IGM%' AND B.PO_QTY!=0 "+ExSql+"\n" + 
						"ORDER BY PO_PURNO,PO_NO,B.PO_REDATE) A WHERE 1=1 "+ExSql2;
		
		return Sql1;
	}

	@Listen("onClick = #btnQuery")
	  public void onClickbtnQuery(Event event) throws SQLException
	  {
		String sql = "";

		if(type1.isSelected()){
			Messagebox.show("查詢時請選定物料類型！！！");
		}else{
			if(type2.isSelected()){
				sql = Getsql1();
			}
			if(type3.isSelected()){
				sql = Getsql2();
			}
			
			 List<GENERIC> list = new ArrayList<GENERIC>();
			 Connection Conn = getDB01Conn();
			 PreparedStatement  ps1 = null;
			 ResultSet  rs1 = null;	
			 System.out.println(sql);
			 String last_date="";
			 
					try {
						ps1 = Conn.prepareStatement(sql);
						rs1 = ps1.executeQuery();			
						while(rs1.next()){	
							GENERIC GENERIC=new GENERIC();
							if(type2.isSelected()){
								GENERIC.setAA(rs1.getString("PO_PURNO"));
							}else{
								GENERIC.setAA("");
							}
							GENERIC.setBB(rs1.getString("PO_NO"));
							GENERIC.setCC(rs1.getString("SU_NA"));
							GENERIC.setDD(rs1.getString("EL_NO"));
							GENERIC.setEE(rs1.getString("EL_CNAME"));
							GENERIC.setFF(rs1.getString("STOCK_MARK"));
							GENERIC.setGG(rs1.getString("CLR"));
							GENERIC.setHH(rs1.getString("PO_QTY"));
							GENERIC.setII(rs1.getString("PO_DATE"));
							if(!"".equals(rs1.getString("PO_REDATE"))&&rs1.getString("PO_REDATE")!=null){
								GENERIC.setJJ(rs1.getString("PO_REDATE"));
								last_date=rs1.getString("PO_REDATE");
							}else{
								GENERIC.setJJ(last_date);
							}
							GENERIC.setKK(rs1.getString("PC_REDATE"));
							list.add(GENERIC);

						}
						rs1.close();
						ps1.close();
					
					} catch (SQLException e) {				
						e.printStackTrace();
					}finally{
						if(rs1!=null){
							rs1.close();
						}
						if(ps1!=null){
							ps1.close();
						}

						Common.closeConnection(Conn);	
					}
					
				ListBox.setModel(new ListModelList<Object>(list));
		}
		
	  }
	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
	}

	@Override
	protected MSMode getMSMode() {
		// TODO Auto-generated method stub
		return  MSMode.MASTER;
	}

	@Override
	protected ArrayList<String> getKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<String> getKeyValue(Object objectEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean beforeSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap getReturnMap() {
		// TODO Auto-generated method stub
		return null;
	}
	public static Connection getDB01Conn(){
		Connection  conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.8.1.32:1521:ftldb1";
		String user = "DSOD";
		String pwd = "ora@it2013";
		try{
	        Class.forName(driver);
	     }catch(Exception e){
	        e.printStackTrace();
	     }
	    try{
	    	conn=DriverManager.getConnection(url,user,pwd);
	    	System.err.println(">>>鏈接DB01數據庫");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return conn;
	}
	
}
