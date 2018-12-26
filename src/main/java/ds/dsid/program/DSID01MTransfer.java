package ds.dsid.program;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import util.BlackBox;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID01MTransfer extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnTrans;
	@Wire
	private Datebox txtorder_date;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	

	}
	
	// 確認批量刪除
	@Listen("onClick =#btnTrans")
	public void onClickbtnTrans(Event event) throws Exception{
		
		doTransfer();
	}

	
	
	private void doTransfer() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		Connection Conn=Common.getOraDbConnection("10.8.1.32", "FTLDB1", "DSOD", "ORA@IT2013");
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		PreparedStatement ps = null,ps2 = null;
		ResultSet rs = null,rs2 = null;
		int num=1;
		String err="";
		
		if(txtorder_date.getValue()!=null){
			
			Delete(Conn);
			
			String sql="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"' AND STATUS='7' AND IS_PG='Y' ORDER BY OD_NO";
			System.out.println(">>>："+sql);	
			try {
				ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = ps.executeQuery();	
				while(rs.next()){
					//指令同步dsid30的
//					String NOD_NO=GetOd_no(Conn);
					String seq="0000"+num;
					String WOI=format.format(txtorder_date.getValue())+seq.substring(seq.length()-4, seq.length())+"-"+rs.getString("WORK_ORDER_ID");
					String SHIP_GROUP_ID="",ORDER_ID="",SH_STYLENO="",ORDER_NUM="",COUNTRY="",ITEMNUMBER="";
					String PID01="",PID02="",GROUP1="",GROUP2="",GROUP3="",GROUP4="",GROUP5="",GROUP6="",GROUP7="",GROUP8="",GROUP9="",GROUP10="",GROUP11="",GROUP12="",GROUP13="",GROUP14="";
					String PRIORITY="",EXOTIC="",REMAKE="",SHIPPER="",BILLTOREGION="",SHIPTOSTUDIO="",REGION="",ON_ORDER="",POSTALCODE="";
					String URL1="",URL2="",URL3="",URL4="",URL5="",URL6="",URL7="",URL8="",URL9="",URL10="";
					Date FACTACCPDATE=null;
					Date REQUSHIPDATE=null;

					if(!"".equals(rs.getString("SHIP_GROUP_ID"))&&rs.getString("SHIP_GROUP_ID")!=null){
						SHIP_GROUP_ID=rs.getString("SHIP_GROUP_ID");
					}
					if(!"".equals(rs.getString("ORDER_ID"))&&rs.getString("ORDER_ID")!=null){
						ORDER_ID=rs.getString("ORDER_ID");
					}
					if(!"".equals(rs.getString("SH_STYLENO"))&&rs.getString("SH_STYLENO")!=null){
						SH_STYLENO=rs.getString("SH_STYLENO");
					}
					if(!"".equals(rs.getString("ORDER_NUM"))&&rs.getString("ORDER_NUM")!=null){
						ORDER_NUM=rs.getString("ORDER_NUM");
					}
					if(!"".equals(rs.getString("PID01"))&&rs.getString("PID01")!=null){
						PID01=rs.getString("PID01");
					}
					if(!"".equals(rs.getString("PID02"))&&rs.getString("PID02")!=null){
						PID02=rs.getString("PID02");
					}					
					if(!"".equals(rs.getString("GROUP1"))&&rs.getString("GROUP1")!=null){
						GROUP1=rs.getString("GROUP1");
					}					
					if(!"".equals(rs.getString("GROUP2"))&&rs.getString("GROUP2")!=null){
						GROUP2=rs.getString("GROUP2");
					}
					if(!"".equals(rs.getString("GROUP3"))&&rs.getString("GROUP3")!=null){
						GROUP3=rs.getString("GROUP3");
					}
					if(!"".equals(rs.getString("GROUP4"))&&rs.getString("GROUP4")!=null){
						GROUP4=rs.getString("GROUP4");
					}
					if(!"".equals(rs.getString("GROUP5"))&&rs.getString("GROUP5")!=null){
						GROUP5=rs.getString("GROUP5");
					}
					if(!"".equals(rs.getString("GROUP6"))&&rs.getString("GROUP6")!=null){
						GROUP6=rs.getString("GROUP6");
					}
					if(!"".equals(rs.getString("GROUP7"))&&rs.getString("GROUP7")!=null){
						GROUP7=rs.getString("GROUP7");
					}
					if(!"".equals(rs.getString("GROUP8"))&&rs.getString("GROUP8")!=null){
						GROUP8=rs.getString("GROUP8");
					}
					if(!"".equals(rs.getString("GROUP9"))&&rs.getString("GROUP9")!=null){
						GROUP9=rs.getString("GROUP9");
					}
					if(!"".equals(rs.getString("GROUP10"))&&rs.getString("GROUP10")!=null){
						GROUP10=rs.getString("GROUP10");
					}
					if(!"".equals(rs.getString("GROUP11"))&&rs.getString("GROUP11")!=null){
						GROUP11=rs.getString("GROUP11");
					}
					
					if(!"".equals(rs.getString("GROUP12"))&&rs.getString("GROUP12")!=null){					
						GROUP12=rs.getString("GROUP12");
					}
					if(!"".equals(rs.getString("GROUP13"))&&rs.getString("GROUP13")!=null){
						GROUP13=rs.getString("GROUP13");
					}
					if(!"".equals(rs.getString("GROUP14"))&&rs.getString("GROUP14")!=null){
						GROUP14=rs.getString("GROUP14");
					}
					if(!"".equals(rs.getString("COUNTRY"))&&rs.getString("COUNTRY")!=null){
						COUNTRY=rs.getString("COUNTRY");
					}
					if(!"".equals(rs.getString("ITEMNUMBER"))&&rs.getString("ITEMNUMBER")!=null){
						ITEMNUMBER=rs.getString("ITEMNUMBER");
					}
					if(!"".equals(rs.getString("PRIORITY"))&&rs.getString("PRIORITY")!=null){
						PRIORITY=rs.getString("PRIORITY");
					}
					if(!"".equals(rs.getString("EXOTIC"))&&rs.getString("EXOTIC")!=null){
						EXOTIC=rs.getString("EXOTIC");
					}
					if(!"".equals(rs.getString("REMAKE"))&&rs.getString("REMAKE")!=null){
						REMAKE=rs.getString("REMAKE");
					}
					if(!"".equals(rs.getString("SHIPPER"))&&rs.getString("SHIPPER")!=null){
						SHIPPER=rs.getString("SHIPPER");
					}
					if(!"".equals(rs.getString("BILLTOREGION"))&&rs.getString("BILLTOREGION")!=null){
						BILLTOREGION=rs.getString("BILLTOREGION");
					}
					if(!"".equals(rs.getString("REGION"))&&rs.getString("REGION")!=null){
						REGION=rs.getString("REGION");
					}
					if(!"".equals(rs.getString("ON_ORDER" ))&&rs.getString("ON_ORDER")!=null){
						ON_ORDER=rs.getString("ON_ORDER");
					}
					if(!"".equals(rs.getString("POSTALCODE" ))&&rs.getString("POSTALCODE")!=null){
						POSTALCODE=rs.getString("POSTALCODE");
					}
					
					if(!"".equals(rs.getString("URL1" ))&&rs.getString("URL1")!=null){
						URL1=rs.getString("URL1");
					}
					if(!"".equals(rs.getString("URL2"))&&rs.getString("URL2")!=null){
						URL2=rs.getString("URL2");
					}
					if(!"".equals(rs.getString("URL3"))&&rs.getString("URL3")!=null){
						URL3=rs.getString("URL3");
					}
					if(!"".equals(rs.getString("URL4"))&&rs.getString("URL4")!=null){
						URL4=rs.getString("URL4");
					}
					if(!"".equals(rs.getString("URL5"))&&rs.getString("URL5")!=null){
						URL5=rs.getString("URL5");
					}
					if(!"".equals(rs.getString("URL6"))&&rs.getString("URL6")!=null){
						URL6=rs.getString("URL6");
					}
					if(!"".equals(rs.getString("URL7"))&&rs.getString("URL7")!=null){
						URL7=rs.getString("URL7");
					}
					if(!"".equals(rs.getString("URL8"))&&rs.getString("URL8")!=null){
						URL8=rs.getString("URL8");
					}
					if(!"".equals(rs.getString("URL9"))&&rs.getString("URL9")!=null){
						URL9=rs.getString("URL9");
					}
					if(!"".equals(rs.getString("URL10"))&&rs.getString("URL10")!=null){
						URL10=rs.getString("URL10");
					}
					if(!"".equals(rs.getString("FACTACCPDATE"))&&rs.getString("FACTACCPDATE")!=null){
						FACTACCPDATE=rs.getDate("FACTACCPDATE");
					}
					if(!"".equals(rs.getString("REQUSHIPDATE"))&&rs.getString("REQUSHIPDATE")!=null){
						REQUSHIPDATE=rs.getDate("REQUSHIPDATE");
					}	
					
					String UpSql="INSERT INTO DSID30 (OD_NO,ORDER_DATE,SHIP_GROUP_ID,WORK_ORDER_ID,ORDER_ID,SH_STYLENO,ORDER_NUM,LEFT_SIZE_RUN,RIGHT_SIZE_RUN,PID01,"+
							"PID02,GROUP1,GROUP2,GROUP3,GROUP4,GROUP5,GROUP6,GROUP7,GROUP8,GROUP9,GROUP10,GROUP11,GROUP12,GROUP13,GROUP14,COUNTRY,ITEMNUMBER,"+
							"PRIORITY,FACTACCPDATE,REQUSHIPDATE,EXOTIC,REMAKE,SHIPPER,BILLTOREGION,SHIPTOSTUDIO,NIKE_SH_ARITCLE,REGION,POSTALCODE,URL1,URL2,URL3,URL4,"+
							"URL5,URL6,URL7,URL8,URL9,URL10,MODEL_NA,IS_PG,PG_DATE,TOOLING_SIZE,MODEL_ROUND_NUM,ALL_ROUND_NUM,ROUND,UP_USER,UP_DATE) "
							+ "VALUES ('"+rs.getString("OD_NO")+"',TO_DATE('"+rs.getDate("ORDER_DATE")+"','YYYY/MM/DD'),'"+SHIP_GROUP_ID+"','"+WOI+"','"+ORDER_ID+"','"+SH_STYLENO+"','"+ORDER_NUM+"','"+rs.getString("LEFT_SIZE_RUN")+"','"+rs.getString("RIGHT_SIZE_RUN")+"','"+PID01+"',"+
							"'"+PID02+"','"+GROUP1+"','"+GROUP2+"','"+GROUP3+"','"+GROUP4+"','"+GROUP5+"','"+GROUP6+"','"+GROUP7+"','"+GROUP8+"','"+GROUP9+"','"+GROUP10+"','"+GROUP11+"','"+GROUP12+"','"+GROUP13+"','"+GROUP14+"','"+COUNTRY+"','"+ITEMNUMBER+"',"+
							"'"+PRIORITY+"',TO_DATE('"+FACTACCPDATE+"','YYYY/MM/DD'),TO_DATE('"+REQUSHIPDATE+"','YYYY/MM/DD'),'"+EXOTIC+"','"+REMAKE+"','"+SHIPPER+"','"+BILLTOREGION+"','"+SHIPTOSTUDIO+"',"
									+ "'"+rs.getString("NIKE_SH_ARITCLE")+"','"+REGION+"','"+POSTALCODE+"','"+ON_ORDER+"','"+URL2+"','"+URL3+"','"+URL4+"',"+
							"'"+URL5+"','"+URL6+"','"+URL7+"','"+URL8+"','"+URL9+"','"+URL10+"','"+rs.getString("MODEL_NA")+"','N',TO_DATE('"+rs.getDate("PG_DATE")+"','YYYY/MM/DD'),"
									+ "'"+rs.getString("TOOLING_SIZE")+"','"+rs.getString("MODEL_ROUND_NUM")+"','"+rs.getString("ALL_ROUND_NUM")+"','"+rs.getString("ROUND")+"','"+rs.getString("UP_USER")+"',SYSDATE)";
					
					System.out.println(">>>主檔資料轉移 ："+UpSql);		
					try {
						PreparedStatement pstm = Conn.prepareStatement(UpSql);
						pstm.executeUpdate();
						pstm.close();
						num++;
					} catch (Exception e) {
						err=rs.getString("OD_NO")+"資料轉移失敗！"+e;
						e.printStackTrace();
					}
					
					
					
					
				}						
				rs.close();
				ps.close();
			} catch (Exception e) {
				err="錯誤！！"+e;
				e.printStackTrace();
			}
			
			
			
			//明細檔轉移
			String sql2="SELECT A.OD_NO,B.* FROM DSID01 A,DSID01_TEMP2 B WHERE A.WORK_ORDER_ID=B.WORK_ORDER_ID AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"' AND A.STATUS='7' AND A.IS_PG='Y'ORDER BY OD_NO,SEQ";
			System.out.println(">>>："+sql2);	
			try {
				ps2 = conn.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs2 = ps2.executeQuery();	
				while(rs2.next()){

					String GROUP_NO2="",PART_NA="",CODE="",NAME="",REMARKS="",TYPE="",UP_USER="",PART_NO="";

					if(!"".equals(rs2.getString("GROUP_NO"))&&rs2.getString("GROUP_NO")!=null){
						GROUP_NO2=rs2.getString("GROUP_NO").replace("GROUP", "GROUP ");
					}
					if(!"".equals(rs2.getString("PART_NA"))&&rs2.getString("PART_NA")!=null){
						PART_NA=rs2.getString("PART_NA");
						PART_NO=Getpart_no(rs2.getString("PART_NA"),Conn);
					}
					if(!"".equals(rs2.getString("CODE"))&&rs2.getString("CODE")!=null){
						CODE=rs2.getString("CODE");
					}
					if(!"".equals(rs2.getString("NAME"))&&rs2.getString("NAME")!=null){
						NAME=rs2.getString("NAME");
					}
					if(!"".equals(rs2.getString("REMARKS"))&&rs2.getString("REMARKS")!=null){
						REMARKS=rs2.getString("REMARKS");
					}				
					if(!"".equals(rs2.getString("TYPE"))&&rs2.getString("TYPE")!=null){
						TYPE=rs2.getString("TYPE");
					}					
					if(!"".equals(rs2.getString("UP_USER"))&&rs2.getString("UP_USER")!=null){
						UP_USER=rs2.getString("UP_USER");
					}
					
					PreparedStatement pstm2=null;
					String UpSql2="INSERT INTO DSID31 (OD_NO,GROUP_NO,PART_NA,PART_NO,CODE,NAME,REMARKS,TYPE_,UP_USER,UP_DATE) VALUES ('"+rs2.getString("OD_NO")+"','"+GROUP_NO2+"','"+PART_NA+"','"+PART_NO+"','"+CODE+"','"+NAME+"','"+REMARKS+"','"+TYPE+"','"+UP_USER+"',SYSDATE)";					
					System.out.println(">>>明細檔資料轉移 ："+UpSql2);		
					try {
						pstm2= Conn.prepareStatement(UpSql2);
						pstm2.executeUpdate();
						pstm2.close();
					} catch (Exception e) {
						err=rs2.getString("OD_NO")+"資料轉移失敗！"+e;
						e.printStackTrace();
					}
					try{
						 if (pstm2 != null){
							 pstm2.close();   
						 }						
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}						
				rs2.close();
				ps2.close();
			} catch (Exception e) {
				err="明細錯誤！！"+e;
				e.printStackTrace();
			}
			
		}else{
			Messagebox.show("接單日期為空，請重新選擇！！！");
		}
		
		if(err.length()>0){
			Messagebox.show(err);
		}else{
			Messagebox.show(Format.format(txtorder_date.getValue())+" 資料轉移成功！！！");
		}
		Common.closeConnection(Conn);
		Common.closeConnection(conn);	
	}

	private String Getpart_no(String part_na, Connection Conn) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String PART_NO="";

		String 	sql="SELECT * FROM DSID02 WHERE PART_NA='"+part_na+"'";	
		System.out.println(">>>>>"+sql);
		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();	
			if(rs.next()){
				PART_NO=rs.getString("PART_NO");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.err.println(">>>>>PART_NO :"+PART_NO);
		return PART_NO;
	}

	private void Delete( Connection conn) {
		// TODO Auto-generated method stub
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");

		String DeSql="DELETE DSID31 WHERE OD_NO IN (SELECT OD_NO FROM DSID30 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"')";
		System.out.println(">>>DSID31資料刪除 ："+DeSql);		
		try {
			PreparedStatement pstm = conn.prepareStatement(DeSql);
			pstm.executeUpdate();
			pstm.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String DeSql2="DELETE DSID30 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"'";
		System.out.println(">>>DSID30資料刪除 ："+DeSql2);		
		try {
			PreparedStatement pstm = conn.prepareStatement(DeSql2);
			pstm.executeUpdate();
			pstm.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


//	private String GetOd_no(Connection conn) {
//		// TODO Auto-generated method stub
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		
//		String OD_NO="",Str1 = "FNJ-",Str2 = "";
//
//		String 	sql="SELECT MAX(OD_NO) MOD_NO FROM DSID30_VT WHERE OD_NO LIKE '"+Str1+"%'";	
//		System.out.println(">>>>>"+sql);
//		try {
//			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			rs = ps.executeQuery();	
//			if(rs.next()){
//				if(!"".equals(rs.getString("MOD_NO")) && rs.getString("MOD_NO")!=null){
//					Str2 =("000000"+(Integer.valueOf(rs.getString("MOD_NO").substring(4))+1));
//				}else{
//					Str2="000001";
//				}
//			}else{
//				Str2="000001";
//			}
//			rs.close();
//			ps.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		OD_NO=Str1+Str2.substring(Str2.length()-6, Str2.length());
//		System.err.println(">>>>>OD_NO :"+OD_NO);
//		return OD_NO;
//	}
	
	
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
		return MSMode.MASTER;
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

	

}
