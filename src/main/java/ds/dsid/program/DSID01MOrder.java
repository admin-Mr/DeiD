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
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
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

public class DSID01MOrder extends OpenWinCRUD{

	@Wire
	private Window windowMaster;
	@Wire
	private Button btnOrder;
	@Wire
	private Datebox txtorder_date;
	DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
	String TABLE="";
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);	
		Execution execution = Executions.getCurrent();
		TABLE =  (String) execution.getArg().get("TABLE"); 
		System.err.println(">>>>>"+TABLE);
	}
	
	// 確認批量刪除
	@Listen("onClick =#btnOrder")
	public void onClickbtnOrder(Event event) throws Exception{
		
		doOrder();
	}

	private void doOrder() throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn=Common.getDbConnection();
		PreparedStatement ps1 = null,ps2=null,ps3=null,ps4=null,ps5=null;
		ResultSet rs1 = null,rs2=null,rs3=null,rs4=null,rs5=null;
		String Errmess="";
		try{
			
		if(txtorder_date.getValue()!=null){
		
		//因是提前接單，故此時會處理當前日期之後得訂單的信息
		String 	sql1="SELECT * FROM "+TABLE+" WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"' ORDER BY OD_NO";	
		System.out.println(">>>>>"+sql1);
		try {
			ps1 = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs1 = ps1.executeQuery();	
			while(rs1.next()){
				System.out.println(">>>>>"+rs1.getString("WORK_ORDER_ID"));
				System.out.println(">>>>>"+rs1.getString("NIKE_SH_ARITCLE"));
				//處理同NIKE型體派工問題
				JudegNike_sh(conn,rs1.getString("SH_STYLENO"),rs1.getString("OD_NO"));				
				
				String PID_GROUP="",PID_TXT="";
				
				//取得pid_group				
				String 	sql5="SELECT * FROM DSID10 WHERE NIKE_SH_ARITCLE='"+rs1.getString("NIKE_SH_ARITCLE")+"'";	
				System.out.println(">>>>>"+sql5);
				try {
					ps5 = conn.prepareStatement(sql5, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					rs5 = ps5.executeQuery();	
					if(rs5.next()){
						if(rs5.getString("PID_GROUP")!=null){
							PID_GROUP=rs5.getString("PID_GROUP");
						}else{
							PID_GROUP="";
						}
						if(rs5.getString("PID_TXT")!=null){
							PID_TXT=rs5.getString("PID_TXT");
						}else{
							PID_TXT="";
						}						
					}						
					rs5.close();
					ps5.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
							
				
				//處理tooling_size 升降碼
				//資料來源   
				//SELECT MODEL_NA,SH_STYLENO,SH_LAST,SH_TYPE,SH_COLOR,SH_REMARK,UPPER_CLASS,SOLE_CLASS,UP_USER,UP_DATE FROM DSID23  WHERE MODEL_NA LIKE '%PEGASUS+35 ESS SU18 ID'
				String 	sql4="SELECT * FROM DSID12 WHERE SH_STYLENO='"+rs1.getString("SH_STYLENO")+"'";	
				System.out.println(">>>>>"+sql4);
				try {
					ps4 = conn.prepareStatement(sql4, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					rs4 = ps4.executeQuery();	
					while(rs4.next()){
						
						String MODEL_NA=rs4.getString("MODEL_NA");
						String SH_LAST=rs4.getString("SH_LAST");
						String LEFT_SIZE=rs1.getString("LEFT_SIZE_RUN");
						String TOOLING_SIZE="";
						
						
						if(MODEL_NA.startsWith(("W"))){
							if(SH_LAST.equals("M")){
								TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE)-1.5);
							}else if(SH_LAST.equals("W")){
								TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE)-1.0);
							}else if(SH_LAST.equals("N")){
								TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE)-2.0);
							}else {
								TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE));
							}
						}else {
							 if(SH_LAST.equals("W")){
								 TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE)+0.5);
							}else if(SH_LAST.equals("N")){
								TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE)-0.5);
							}else {
								TOOLING_SIZE=String.valueOf(Double.valueOf(LEFT_SIZE));
							}
						}
						if(!TOOLING_SIZE.contains(".5")&&!TOOLING_SIZE.contains(".0")){
							TOOLING_SIZE=TOOLING_SIZE+".0";
						}
						//先將為空時的PID值傳入，再處理group時再複寫
	            		String 	Updatesql2="UPDATE "+TABLE+" SET MODEL_NA='"+MODEL_NA+"',TOOLING_SIZE='"+TOOLING_SIZE+"' ,PID01='"+ PID_TXT+"' , PID02='"+PID_TXT +"' WHERE WORK_ORDER_ID='"+rs1.getString("WORK_ORDER_ID")+"'";	
//	            		System.out.println(Updatesql2);		
	            		try {
							PreparedStatement pstm = conn.prepareStatement(Updatesql2);
							pstm.executeUpdate();
							pstm.close();
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}								
					}
						
					rs4.close();
					ps4.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				//處理group信息
				String 	sql2="SELECT GROUP_NO , MIN(SEQ) SEQ FROM DSID01_TEMP2 WHERE WORK_ORDER_ID='"+rs1.getString("WORK_ORDER_ID")+"' GROUP BY GROUP_NO ORDER BY SEQ";	
//				System.out.println(">>>>>"+sql2);
				try {
					ps2 = conn.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					rs2 = ps2.executeQuery();	
					while(rs2.next()){
						String Str1="",Str2="",Str3="",Str4="",Str5="";
						String Group_Color="";
						String pidsql="";
						String pid1="",pid2="",pi1="",pi2="",pi3="",id1="",id2="",id3="";
						String S="",S1="";
						//
						String 	sql3="SELECT * FROM DSID01_TEMP2 WHERE WORK_ORDER_ID='"+rs1.getString("WORK_ORDER_ID")+"' AND GROUP_NO='"+rs2.getString("GROUP_NO")+"'";	
						System.out.println(">>>>>"+sql3);
						try {
							ps3 = conn.prepareStatement(sql3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							rs3 = ps3.executeQuery();
							while(rs3.next()){
								S=rs3.getString("CODE");
								S1=S.trim().replaceAll("\n", "");
								//額外設定的pid_group部分判斷
								if(!"".equals(PID_GROUP) && PID_GROUP!=null&&PID_GROUP.equals(rs3.getString("GROUP_NO"))&&"PATTERN".equals(rs3.getString("TYPE"))){
									System.out.println(">>>含PID——GROUP");
									pidsql=", PID01='"+S1+"', PID02='"+S1+"' ";
								}else{
									System.out.println(">>>這是"+rs3.getString("TYPE")+">>>"+S1);
									if("COLOR".equals(rs3.getString("TYPE"))&& Str1!=rs3.getString("CODE")){
										Str1=S1+"_";
									}
									if("PATTERN".equals(rs3.getString("TYPE"))&&Str2!=rs3.getString("CODE")){
										Str2=S1+"_";
									}
									if("TEXT".equals(rs3.getString("TYPE")) && Str3!=rs3.getString("CODE")){
										Str3=S1+"_";
									}
									if("FORMULA".equals(rs3.getString("TYPE"))&&Str4!=rs3.getString("CODE")){
										Str4=S1+"_";
									}
									
									if("PID".equals(rs3.getString("TYPE"))){
										if(rs3.getString("PART_NA").startsWith("RIGHT SOCK") || rs3.getString("PART_NA").startsWith("LEFT SOCK")){
											System.out.println("進入時間=============");
										if(rs3.getString("PART_NA").contains("RIGHT SOCK PID HOURS")){
											pi1+=S1;//時
										}else if(rs3.getString("PART_NA").contains("LEFT SOCK PID HOURS")){
											id1+=S1;//時
										}else if(rs3.getString("PART_NA").contains("RIGHT SOCK PID MINUTES")){
											pi2+=S1;//分
										}else if(rs3.getString("PART_NA").contains("LEFT SOCK PID MINUTES")){
											id2+=S1;//分
										}else if(rs3.getString("PART_NA").contains("RIGHT SOCK PID SECONDS")){
											pi3+=S1;//秒
										}else if(rs3.getString("PART_NA").contains("LEFT SOCK PID SECONDS")){
											id3+=S1;//秒	
										}
											pidsql=", PID03='"+pi1+":"+pi2+":"+pi3+"' , PID04='"+id1+":"+id2+":"+id3+"'";
										}else if(rs3.getString("PART_NA").startsWith("SHORT") || rs3.getString("PART_NA").startsWith("LONG")){
											System.out.println("進入新形體PID。。。。。。。。。。。。。。");
											if(rs3.getString("PART_NA").contains("SHORT PID LEFT")){
											id1+="下字體："+S1;
											}else if(rs3.getString("PART_NA").contains("LONG PID LEFT")){
											id2+="上字體："+S1+"/";
											}else if(rs3.getString("PART_NA").contains("SHORT PID RIGHT")){
											pi1+="下字體："+S1;
											}else if(rs3.getString("PART_NA").contains("LONG PID RIGHT")){
											pi2+="上字體："+S1+"/";
											}
											pidsql=", PID01='"+id2+id1+"' , PID02='"+pi2+pi1+"' ";
											}else{
											
											if(rs3.getString("PART_NA").contains("LEFT")&&!rs3.getString("PART_NA").startsWith("SHORT")){
												pidsql+=", PID01='"+S1+"' ";
											}else if(rs3.getString("PART_NA").contains("RIGHT")){
												pidsql+=", PID02='"+S1+"'";
											}else if(rs3.getString("PART_NA").contains("SIGNATUREFILEID")){
												Str5=rs3.getString("CODE")+"_";
											}else if(rs3.getString("PART_NA").startsWith("PID")){
												pidsql=" ";
												
												if("PID 3".equals(rs3.getString("PART_NA"))){
													pid1="上：" +S1;
												}else if("PID 3A".equals(rs3.getString("PART_NA"))){
													pid2=" 下："+S1;
												}else{
													pidsql+=", PID01='"+S1+"' , PID02='"+S1+"'";
												}
												if(!"".equals(pid1)){
													pidsql+=", PID01='"+pid1+pid2+"' , PID02='"+pid1+pid2+"'";
												}
											}
										}
									}
								}
							Group_Color=Str1+Str2+Str3+Str4+Str5;

							if(Group_Color.length()>0){
								Group_Color=Group_Color.substring(0, Group_Color.length());
								Group_Color=Group_Color.substring(0, Group_Color.length()-1);
							}
							System.out.println(">>>>>COLOR :"+Group_Color); 
							
		            		String 	Updatesql="UPDATE "+TABLE+" SET "+rs2.getString("GROUP_NO")+"='"+Group_Color+"'"+pidsql+" WHERE WORK_ORDER_ID='"+rs1.getString("WORK_ORDER_ID")+"'";	
		            		System.err.println(">>>>>GROUP COLOR&PID UPDATESQL :"+Updatesql);
		            		try {
								PreparedStatement pstm = conn.prepareStatement(Updatesql);
								pstm.executeUpdate();
								pstm.close();
							} catch (Exception e) {
								e.printStackTrace();
								Errmess+=rs1.getString("WORK_ORDER_ID")+" "+rs2.getString("GROUP_NO")+" "+e;
								return;
							}
						  }
							rs3.close();
							ps3.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
						
					rs2.close();
					ps2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}				
			rs1.close();
			ps1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Group信息拆分&替換
		Replace(conn);
		
		//PEGASUS+35 ESS SU18 ID  GROUP1 拼接 GROUP8
		SpliceGroup(Format.format(txtorder_date.getValue()),conn);
		
		//HO18 PEGASUS 35 SHIELD 中底筒  GROUP1 拼接 GROUP8
		ReplaceGroup5(Format.format(txtorder_date.getValue()),conn);
		
		
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0023"));
		}
		Common.closeConnection(conn);
		
		if(Errmess.length()>0){
			Messagebox.show(Labels.getLabel("DSID.MSG0024")+Errmess);
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0025"));
		}	
		Errmess="";
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(rs1!=null){
				rs1.close();
			}
			if(ps1!=null){
				ps1.close();
			}
			if(rs2!=null){
				rs2.close();
			}
			if(ps2!=null){
				ps2.close();
			}
			if(rs3!=null){
				rs3.close();
			}
			if(ps3!=null){
				ps3.close();
			}
			if(rs4!=null){
				rs4.close();
			}
			if(ps4!=null){
				ps4.close();
			}
			if(rs5!=null){
				rs5.close();
			}
			if(ps5!=null){
				ps5.close();
			}
		}

		
	}

	private void ReplaceGroup5(String DATE, Connection conn) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql="SELECT A.WORK_ORDER_ID,B.TYPE,CODE FROM "+TABLE+" A,DSID01_TEMP2 B WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+DATE+"' AND A.MODEL_NA LIKE '%HO18 PEGASUS 35 SHIELD%' AND A.GROUP5 LIKE '01B%'\n" +
					"AND A.WORK_ORDER_ID=B.WORK_ORDER_ID AND B.GROUP_NO='GROUP5'";
		System.out.println("--ReplaceGroup5--"+sql);
		try {

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				String color="";
				if("PATTERN".equals(rs.getString("TYPE"))){
					color="01B,"+Labels.getLabel("DSID.MSG0026");
				}else if("COLOR".equals(rs.getString("TYPE"))){
					color="01B,"+Labels.getLabel("DSID.MSG0027");
				}
				String 	resql="UPDATE "+TABLE+" SET GROUP5='"+color+"' WHERE WORK_ORDER_ID='"+rs.getString("WORK_ORDER_ID")+"'";	
				System.err.println(">>>>>區分中底筒的G5:"+resql);		
				try {
					PreparedStatement pstm = conn.prepareStatement(resql);
					pstm.executeUpdate();
					pstm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void SpliceGroup(String ORDER_DATE, Connection conn) {
		// TODO Auto-generated method stub
		
		String 	splsql="UPDATE "+TABLE+" SET GROUP8=GROUP1||'-'||GROUP8 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+ORDER_DATE+"' AND MODEL_NA LIKE '%PEGASUS+35 ESS SU18 ID'";	
		System.err.println(">>>>>PEG35 拼接G1&gG8 :"+splsql);		
		try {
			PreparedStatement pstm = conn.prepareStatement(splsql);
			pstm.executeUpdate();
			pstm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
				
	}

	private void JudegNike_sh(Connection conn, String SH_STYLENO,String OD_NO) {
		// TODO Auto-generated method stub
		String nike_sh = "";
		PreparedStatement ps = null,ps2 = null;
		ResultSet rs = null,rs2 = null;
		
		String sql="SELECT MODEL_NA FROM DSID12 WHERE SH_STYLENO='"+SH_STYLENO+"'";
		try {
			System.out.println("----model_na--sql--"+sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				String sql2="SELECT NIKE_SH_ARITCLE FROM DSID10 WHERE MODEL_NAS LIKE '%"+rs.getString("MODEL_NA")+"%'";
				try {
					System.out.println("----model_na--sql--"+sql2);
					ps2 = conn.prepareStatement(sql2);
					rs2 = ps2.executeQuery();
					if(rs2.next()){
						nike_sh=rs2.getString("NIKE_SH_ARITCLE");
					}
					rs2.close();
					ps2.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//區分型體
		if(!"".equals(nike_sh)||nike_sh!=null){
			String 	repsql="UPDATE "+TABLE+" SET NIKE_SH_ARITCLE='"+nike_sh+"' WHERE OD_NO='"+OD_NO+"'";	
    		System.err.println(">>>>> 區分型體:"+repsql);		
    		try {
				PreparedStatement pstm = conn.prepareStatement(repsql);
				pstm.executeUpdate();
				pstm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

	private void Replace(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null,pstm=null;
		ResultSet rs = null;
		try {

		String 	sql="SELECT * FROM DSID10_1 WHERE NIKE_SH_ARITCLE IN (SELECT DISTINCT A.NIKE_SH_ARITCLE FROM "+TABLE+" A WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"') ORDER BY NIKE_SH_ARITCLE ,SEQ ";	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			while(rs.next()){
				if("Y".equals(rs.getString("IS_SPL"))){
					//拆分
	        		String 	splsql="UPDATE "+TABLE+" SET "+rs.getString("GROUP_NO")+"='"+rs.getString("SPL_INFO1")+"' ,"+rs.getString("SPL_GROUP")+"='"+rs.getString("SPL_INFO2")+"' WHERE NIKE_SH_ARITCLE = '"+rs.getString("NIKE_SH_ARITCLE")+"' AND "+rs.getString("GROUP_NO")+" ='"+rs.getString("ORI_INFO")+"' AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"'";	
	        		System.err.println(">>>>>拆分 :"+splsql);
	        		try {
						pstm = conn.prepareStatement(splsql);
						pstm.executeUpdate();
						pstm.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if("Y".equals(rs.getString("IS_REP"))){
		       		//翻譯
	        		String  repsql="UPDATE "+TABLE+" SET "+rs.getString("GROUP_NO")+"=REPLACE("+rs.getString("GROUP_NO")+",'"+rs.getString("ORI_INFO")+"','"+rs.getString("REP_INFO")+"'),PID01=REPLACE(PID01,'"+rs.getString("IS_LEFT1_PID")+"','"+rs.getString("IS_LEFT_PID")+"'),PID02=REPLACE(PID02,'"+rs.getString("IS_RIGHT1_PID")+"','"+rs.getString("IS_RIGHT_PID")+"') WHERE NIKE_SH_ARITCLE='"+rs.getString("NIKE_SH_ARITCLE")+"' AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"'";
//	        		String  repsql="UPDATE "+TABLE+" SET "+rs.getString("GROUP_NO")+"=REPLACE("+rs.getString("GROUP_NO")+",'"+rs.getString("ORI_INFO")+"','"+rs.getString("REP_INFO")+"') AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(txtorder_date.getValue())+"'";

	        		System.err.println(">>>>>翻譯 :"+repsql);
	        		try {
						pstm = conn.prepareStatement(repsql);
						pstm.executeUpdate();
						pstm.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(pstm!=null){
				pstm.close();
			}
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
	protected boolean doCustomSave(Connection conn) {
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
