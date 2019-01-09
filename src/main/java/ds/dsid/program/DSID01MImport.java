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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
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

public class DSID01MImport extends OpenWinCRUD{

	@WireVariable
	private CRUDService CRUDService;
	@Wire
	private Fileupload btnImport2;	
	@Wire
	private Window windowMaster;
	@Wire
	private Radio N_radioButton,S_radioButton,V_radioButton;	
	@Wire
	private Datebox txtorder_date;
	@Wire
	private Label ShowResult;
	
	private int Index=0;
	private boolean isunique = true; //WORK_ORDER_ID是否是唯一的?
	
	
	Object [][] importdata=null;
	ArrayList<String> sampleList=new ArrayList();
	ArrayList<String> partnaList=new ArrayList();
	Connection connHead = null;
	String Errmessage="";
	String TABLE="";
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
				
		Execution execution = Executions.getCurrent();
		TABLE =  (String) execution.getArg().get("TABLE"); 
		System.err.println(">>>>>"+TABLE);
//		if("DSID01".equals(TABLE)){
//			V_radioButton.setVisible(false);	
//		}else{
//			N_radioButton.setVisible(false);
//			S_radioButton.setVisible(false);
//			V_radioButton.setChecked(true);
//		}
		
		btnImport2 = (Fileupload) window.getFellow("btnImport2");
		btnImport2.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>() {
			@SuppressWarnings("unused")
			public void onEvent(UploadEvent event) throws SQLException {
				createSpec(event);
			}
		});	
	}
	

	
	private void createSpec(UploadEvent event) throws SQLException {
		// TODO Auto-generated method stub
		importdata=null;
		sampleList.clear();
		partnaList.clear();
		String msg="";
		connHead = Common.getDbConnection();
		try{
			connHead.setAutoCommit(false);
			org.zkoss.util.media.Media media[] = event.getMedias();
			
			importdata=new Object[media.length][2];
			for (int i = 0; i < media.length; i++) {
				
				importdata[i][0]=media[i].getName();
				importdata[i][1]=false;
				
				File file = new File("temp.xml");
				FileWriter fw = new FileWriter(file);
				fw.write(media[i].getStringData());

				fw.flush();
				fw.close();
				write(file,i,connHead);

			}
			for(int n=0;n<importdata.length;n++){
				if((boolean)importdata[n][1]==false){
					msg+=((String)importdata[n][0]).toString()+"\n";
				}
			}
			if(msg.length()>0){
				connHead.rollback();
				Messagebox.show("XML文件導入失敗！！！"+msg);
			}
			
		}catch(Exception e){
			connHead.rollback();
			Messagebox.show(Labels.getLabel("DSPLM.DSPLM101M.MSG009"));
			e.printStackTrace();
		}finally{
			BlackBox.connectionClose(connHead);
		}
	}
	
	private void write(File file, int i, Connection conn) {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		Document document = null;
		
		try {
			document = reader.read(file);
			importXMLToDB(document,conn);
			importdata[i][1]=true;
			ShowMessage();			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void importXMLToDB(Document document, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
				
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
//		DSID01_TEMP DSID01_TEMP=new DSID01_TEMP();
		
		List workOrder = document.selectNodes("//workOrder");
		Iterator workOrderIt = workOrder.iterator();
		
		while(workOrderIt.hasNext()){
			Index++;

			String OD_NO="",WORK_ORDER_ID="",TYPE="",SHIP_GROUP_ID="",ORDER_ID="",NIKE_SH_ARITCLE="",MODEL_NA="",SH_STYLENO="",ORDER_NUM="",LEFT_SIZE_RUN="",RIGHT_SIZE_RUN="";
			String REGION="",EXP_QTY="",EXP_STATUS="",COUNTRY="",ITEMNUMBER="",PRIORITY="",EXOTIC="",REMAKE="",BILLTOREGION="",SHIPTOSTUDIO="",POSTALCODE="",SHIPPER="";
			String URL1="",URL2="",URL3="",URL4="",URL5="",URL6="",URL7="",URL8="",URL9="",URL10="";
			String FACTACCPDATE="",REQUSHIPDATE="",ORDER_DATE="",FACTRECDATE="";
			
			//接單日期---可選

			ORDER_DATE =format.format(txtorder_date.getValue());
			System.err.println(">>>>>ORDER_DATE :"+ORDER_DATE);
			
			
			//workOrderID			
			Element workOrderElement = (Element)workOrderIt.next();			
			Iterator workOrderIdIt = workOrderElement.elementIterator("workOrderId");
			while(workOrderIdIt.hasNext()){
				Element workOrderIdElement = (Element)workOrderIdIt.next();
				WORK_ORDER_ID=workOrderIdElement.getText();
				System.err.println(">>>>>WORK_ORDER_ID :"+WORK_ORDER_ID);
//				DSID01_TEMP.setWORK_ORDER_ID(workOrderIdElement.getText());

			}
			
			//檢查work_order_id是否唯一
			Boolean Exist = Check(WORK_ORDER_ID,conn);
			if(Exist==true){
				System.out.println(">>>："+WORK_ORDER_ID+"已經匯入。");
				continue;
			}	
	
			//type
			
			 if(N_radioButton.isSelected()){ 		//普通接單  normal 
				TYPE="0"; 
			}else if(S_radioButton.isSelected()){   //特殊訂單  Special
				TYPE="1";
			}
//			else if(V_radioButton.isSelected()){	//虛擬訂單  Virtual
//				TYPE="2";
//			}
			 
			System.err.println(">>>>>TYPE :"+TYPE);			
			
			//shipGroupId			 
			Iterator shipGroupIdIt = workOrderElement.elementIterator("shipGroupId");
			while(shipGroupIdIt.hasNext()){
				Element shipGroupIdElement = (Element)shipGroupIdIt.next();
				SHIP_GROUP_ID=shipGroupIdElement.getText();
				System.err.println(">>>>>SHIP_GROUP_ID :"+SHIP_GROUP_ID);
//				DSID01_TEMP.setSHIP_GROUP_ID(shipGroupIdElement.getText());
			}
			
			//orderId			 
			Iterator  orderIdIt = workOrderElement.elementIterator("orderId");
			while(orderIdIt.hasNext()){
				Element orderIdElement = (Element)orderIdIt.next();
				ORDER_ID=orderIdElement.getText();
				System.err.println(">>>>>ORDER_ID :"+ORDER_ID);
//				DSID01_TEMP.setORDER_ID(orderIdElement.getText());
			}	
			
			//packingInfo 			 
			Iterator  packingInfoIt = workOrderElement.elementIterator("packingInfo");
			while(packingInfoIt.hasNext()){
				Element packingInfoElement = (Element)packingInfoIt.next();
				
				ORDER_NUM = packingInfoElement.attributeValue("total");
				System.err.println(">>>>>ORDER_NUM :"+ORDER_NUM);
//				DSID01_TEMP.setORDER_NUM(ORDER_NUM);				
				ITEMNUMBER=packingInfoElement.attributeValue("itemNumber");
				System.err.println(">>>>>ITEMNUMBER :"+ITEMNUMBER);
//				DSID01_TEMP.setITEMNUMBER(ITEMNUMBER);
			}	
			
			//priority		 
			Iterator priorityIt = workOrderElement.elementIterator("priority");
			while(priorityIt.hasNext()){
				Element priorityElement = (Element)priorityIt.next();
				PRIORITY=priorityElement.getText();
				System.err.println(">>>>>PRIORITY :"+PRIORITY);
//				DSID01_TEMP.setPRIORITY(priorityElement.getText());

			}
				
			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
			//factoryReceivedDate	  客戶下單日期		 
			Iterator factoryReceivedDate = workOrderElement.elementIterator("factoryReceivedDate");
			while(factoryReceivedDate.hasNext()){
				Element factoryReceivedDateElement = (Element)factoryReceivedDate.next();
				try{
//				 FACTACCPDATE = format.parse(factoryAcceptDateElement.getText());				 
				 FACTRECDATE=factoryReceivedDateElement.getText();
				 System.err.println(">>>>>FACTRECDATE :"+FACTRECDATE);
//				 DSID01_TEMP.setFACTACCPDATE(acceptDate);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			//factoryAcceptDate			 
			Iterator factoryAcceptDateIt = workOrderElement.elementIterator("factoryAcceptDate");
			while(factoryAcceptDateIt.hasNext()){
				Element factoryAcceptDateElement = (Element)factoryAcceptDateIt.next();
				try{
//				 FACTACCPDATE = format.parse(factoryAcceptDateElement.getText());				 
				 FACTACCPDATE=factoryAcceptDateElement.getText();
				 if("".equals(FACTACCPDATE)||FACTACCPDATE==null||"null".equals(FACTACCPDATE)){
					 FACTACCPDATE=format1.format(new Date());
				 }
				 System.err.println(">>>>>FACTACCPDATE :"+FACTACCPDATE);
//				 DSID01_TEMP.setFACTACCPDATE(acceptDate);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
						
			//exotic
			Iterator exoticIt = workOrderElement.elementIterator("exotic");
			while(exoticIt.hasNext()){
				Element exoticElement = (Element)exoticIt.next();
				EXOTIC=exoticElement.getText();
				System.err.println(">>>>>EXOTIC :"+EXOTIC);
//				DSID01_TEMP.setEXOTIC(exoticElement.getText());
			}
			

			//remake
			Iterator remakeIt = workOrderElement.elementIterator("remake");
			while(remakeIt.hasNext()){
				Element remakeElement = (Element)remakeIt.next();
				REMAKE=remakeElement.getText();
				System.err.println(">>>>>REMAKE :"+REMAKE);				
//				DSID01_TEMP.setREMAKE(remakeElement.getText());
			}
			
			
			//shipToRegion			
			Iterator shipToRegionIt = workOrderElement.elementIterator("shipToRegion");	
			while(shipToRegionIt.hasNext()){
				Element shipToRegionElement = (Element)shipToRegionIt.next();
				REGION=shipToRegionElement.getText();
				System.err.println(">>>>>REGION :"+REGION);	
//				DSID01_TEMP.setREGION(shipToRegionElement.getText());
			}
						
			//shipToCountry			
			Iterator shipToCountryIt = workOrderElement.elementIterator("shipToRegion");
			while(shipToCountryIt.hasNext()){
				Element shipToCountryElement = (Element)shipToCountryIt.next();
				COUNTRY=shipToCountryElement.getText();
				System.err.println(">>>>>COUNTRY :"+COUNTRY);					
//				DSID01_TEMP.setCOUNTRY(shipToCountryElement.getText());
			}
					
			//shipper
			Iterator shipperIt = workOrderElement.elementIterator("shipper");
			while(remakeIt.hasNext()){
				Element shipperElement = (Element)shipperIt.next();
				SHIPPER=shipperElement.getText();
				System.err.println(">>>>>SHIPPER :"+shipperElement.getText());	
//				DSID01_TEMP.setSHIPPER(shipperElement.getText());
			}	
			

			//billToRegion
			Iterator billToRegionIt = workOrderElement.elementIterator("billToRegion");
			while(billToRegionIt.hasNext()){
				Element billToRegionElement = (Element)billToRegionIt.next();
				BILLTOREGION=billToRegionElement.getText();
				System.err.println(">>>>>BILLTOREGION :"+BILLTOREGION);					
//				DSID01_TEMP.setBILLTOREGION(billToRegionElement.getText());
			}	
			
			//requiredShipDate
			Iterator requiredShipDateIt = workOrderElement.elementIterator("requiredShipDate");
			while(requiredShipDateIt.hasNext()){
				Element requiredShipDateElement = (Element)requiredShipDateIt.next();

				try {
//					REQUSHIPDATE = format.parse(requiredShipDateElement.getText());
					REQUSHIPDATE = requiredShipDateElement.getText();
//					if("".equals(REQUSHIPDATE)||REQUSHIPDATE==null||REQUSHIPDATE=="null"){
//						REQUSHIPDATE="";
//					 }
					System.err.println(">>>>>REQUSHIPDATE :"+REQUSHIPDATE);	
//					DSID01_TEMP.setREQUSHIPDATE(shipDate);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			
			//postalCode
			Iterator POSTALCODEIt = workOrderElement.elementIterator("postalCode");
			while(POSTALCODEIt.hasNext()){
				Element factoryAcceptDateElement = (Element)POSTALCODEIt.next();
				try{
				 POSTALCODE = factoryAcceptDateElement.getText();
				 System.err.println(">>>>>POSTALCODE :"+POSTALCODE);	
//				 DSID01_TEMP.setPOSTALCODE(postalCode);
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
				
			//shipToStudio
			Iterator shipToStudioIt = workOrderElement.elementIterator("shipToStudio");
			while(shipToStudioIt.hasNext()){
				Element shipToStudioElement = (Element)shipToStudioIt.next();
				SHIPTOSTUDIO=shipToStudioElement.getText();
				System.err.println(">>>>>SHIPTOSTUDIO :"+shipToStudioElement.getText());	
//				DSID01_TEMP.setSHIPTOSTUDIO(SHIPTOSTUDIO);
			}
			
			//images  ---URL1-URL10
			String allUrl = "";
			Iterator imagesIt = workOrderElement.elementIterator("images");
			while(imagesIt.hasNext()){
				Element imagesElement = (Element)imagesIt.next();
				Iterator imageIt = imagesElement.elementIterator("image");
				while(imageIt.hasNext()){
					Element imageElement = (Element)imageIt.next();
					String url = imageElement.getText();
					allUrl += url + ";";
				}
			}
			String oldUrlArr[] = allUrl.split(";");
			String newUrlArr[]=new String[10];
			for (int i = 0; i < 10; i++) {
				if(i<oldUrlArr.length){
					newUrlArr[i]=oldUrlArr[i]==null?"":oldUrlArr[i];
				}else{
					newUrlArr[i]="";
				}
			}
//			DSID01_TEMP.setURL1(newUrlArr[0]);
//			DSID01_TEMP.setURL2(newUrlArr[1]);
//			DSID01_TEMP.setURL3(newUrlArr[2]);
//			DSID01_TEMP.setURL4(newUrlArr[3]);
//			DSID01_TEMP.setURL5(newUrlArr[4]);
//			DSID01_TEMP.setURL6(newUrlArr[5]);
//			DSID01_TEMP.setURL7(newUrlArr[6]);
//			DSID01_TEMP.setURL8(newUrlArr[7]);
//			DSID01_TEMP.setURL9(newUrlArr[8]);
//			DSID01_TEMP.setURL10(newUrlArr[9]);
			
			URL1=newUrlArr[0];
			URL2=newUrlArr[1];
			URL3=newUrlArr[2];
			URL4=newUrlArr[3];
			URL5=newUrlArr[4];
			URL6=newUrlArr[5];
			URL7=newUrlArr[6];
			URL8=newUrlArr[7];
			URL9=newUrlArr[8];
			URL10=newUrlArr[9];
			           
			int deindex=1;
			//buildInfo
			Iterator buildInfoIt = workOrderElement.elementIterator("buildInfo");
			while(buildInfoIt.hasNext()){
				Element buildInfoElement = (Element)buildInfoIt.next();			
				//bom
					Iterator GetBOMConfigurationIt1 = buildInfoElement.elementIterator("GetBOMConfigurationService");
					Iterator bomIt=null;
					
					if(GetBOMConfigurationIt1.hasNext()){
						
					Element GetBOMConfigurationElement1 = (Element)GetBOMConfigurationIt1.next();
					Iterator GetBOMConfigurationIt = GetBOMConfigurationElement1.elementIterator("GetBOMConfigurationService");
						
					if(GetBOMConfigurationIt.hasNext()){
							Element GetBOMConfigurationElement = (Element)GetBOMConfigurationIt.next();
							bomIt = GetBOMConfigurationElement.elementIterator("bom");
						}
					}else{
						bomIt = buildInfoElement.elementIterator("bom");
					}
							
					    	while(bomIt.hasNext()){
					    		Element bomElement = (Element)bomIt.next();					    
					    		
								// product
								Iterator productIt = bomElement.elementIterator("product");
								while(productIt.hasNext()){
									Element productElement = (Element)productIt.next();
									//name ---NIKE_SH_ARITCLE
									String name = productElement.attributeValue("name");
									System.err.println(">>>>>NIKE_SH_ARITCLE :"+name.replace("'", " "));
									NIKE_SH_ARITCLE=name.replace("'", " ");
//									DSID01_TEMP.setNIKE_SH_ARITCLE(name.replace("'", " "));						
									
									//style and color  ---SH_STYLENO
									String style_NO = productElement.attributeValue("style");
									String color_NO = productElement.attributeValue("color");
									System.err.println(">>>>>SH_STYLENO :"+style_NO+"-"+color_NO);
									SH_STYLENO=style_NO+"-"+color_NO;
//									DSID01_TEMP.setSH_STYLENO(SH_STYLENO);
									
									//MODEL_NA
//									DSID01_TEMP.setMODEL_NA(getmodel_na(style_NO+"-"+color_NO));
//									MODEL_NA=(getmodel_na(style_NO+"-"+color_NO);
								}
									
								//Item
								Iterator itemIt = bomElement.elementIterator("item");
								while(itemIt.hasNext()){
									Element itemElement = (Element)itemIt.next();
									
									//SizeLeft
									String sizeLeft = itemElement.attributeValue("SizeLeft");
									System.err.println(">>>>>LEFT_SIZE_RUN :"+sizeLeft);
									LEFT_SIZE_RUN=sizeLeft;
									if(!LEFT_SIZE_RUN.contains(".5")&&!LEFT_SIZE_RUN.contains(".0")){
										LEFT_SIZE_RUN=LEFT_SIZE_RUN+".0";
									}
//									DSID01_TEMP.setLEFT_SIZE_RUN(sizeLeft);
										
									//SizeRight
									String sizeRight = itemElement.attributeValue("SizeRight");
									System.err.println(">>>>>RIGHT_SIZE_RUN :"+sizeRight);	
									RIGHT_SIZE_RUN=sizeRight;
									if(!RIGHT_SIZE_RUN.contains(".5")&&!RIGHT_SIZE_RUN.contains(".0")){
										RIGHT_SIZE_RUN=RIGHT_SIZE_RUN+".0";
									}
									
//									DSID01_TEMP.setRIGHT_SIZE_RUN(sizeRight);
								}
								
								System.out.println(">>>>>GROUP & GROUP_COLOR");	
								
								//comps
								Iterator compsIt = bomElement.elementIterator("comps");

						        while(compsIt.hasNext()){
									Element compsElement = (Element)compsIt.next();
										
									// comp
									Iterator compIt = compsElement.elementIterator("comp");
										
									while(compIt.hasNext()){
										Element compElement = (Element)compIt.next();
										
										// grp
										String group = compElement.attributeValue("grp").trim();
										System.err.println(">>>>>GROUP ："+group);
								     	group = "GROUP"+group.substring(group.lastIndexOf(" ")+1);
										String partName = "";
											
										Element partName_Element = compElement.element("name");
										partName = partName_Element.getText() ;
										partName=partName.replace("'", " ");
										
							            System.err.println(">>>>>GROUP_NAME ：" + partName);
										Iterator valuesIt = compElement.elementIterator("values");
											while(valuesIt.hasNext()){
												Element valuesElement = (Element)valuesIt.next();
												Iterator valueIt2 = valuesElement.elementIterator("value");
												while(valueIt2.hasNext()){													
																															
								                    Element valueElement = (Element)valueIt2.next();
								                    String type = "";
								                    String code = "";
								                    String name = "";
								                    String remarks = "";
								                    
								                    type = valueElement.attributeValue("type");
								                    code = valueElement.attributeValue("code");
								                    if(code!=null && !"".equals(code)){
								                    	code = code.trim();
								                    	 }
								                    
								                    if(type.equals("ID")){
								                    	code = valueElement.getText();
								                    	type = "PID";
								                    	remarks = valueElement.attributeValue("name");
								                    	remarks=remarks.replace("'", " ");
								                    }else {
								                    	name = valueElement.attributeValue("name");
								                    	name=name.replace("'", " ");
								                    }								                    
								                    
								                    String SEQ="0000"+String.valueOf(deindex);
								                    SEQ=SEQ.substring(SEQ.length()-4, SEQ.length());
								                    System.err.println(">>>>>SEQ :"+SEQ);
								                    
								            		String 	sql2="INSERT INTO DSID01_TEMP2 (WORK_ORDER_ID,SEQ,GROUP_NO,PART_NA,REMARKS,TYPE,CODE,NAME,UP_USER,UP_DATE) VALUES ('"+WORK_ORDER_ID+"','"+SEQ+"','"+group+"','"+partName+"','"+remarks+"','"+type+"','"+code+"','"+name+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";	
								            		System.out.println(sql2);		
								            		try {
														PreparedStatement pstm = conn.prepareStatement(sql2);
														pstm.executeUpdate();
														pstm.close();
														deindex++;
														System.out.println(WORK_ORDER_ID+" :"+group+"訂單明細信息匯入成功！！！");
								    				} catch (Exception e) {
														e.printStackTrace();
														Errmessage+=WORK_ORDER_ID+" :"+group+"信息有誤！！"+e;
														conn.rollback();
														return;
													}
								                    System.out.println("DSID01_TEMP2資料已匯入");
												}
										}
									}
								}
						        						    		
					    }
					 }  
					
			
			            
    		
    		
    		//獲取最新指令
    		OD_NO=GetOd_no(conn);
    		
    		String 	sql="INSERT INTO "+TABLE+" (OD_NO,ORDER_DATE,WORK_ORDER_ID,TYPE,SHIP_GROUP_ID,ORDER_ID,NIKE_SH_ARITCLE,"
    				+ "MODEL_NA,SH_STYLENO,ORDER_NUM,LEFT_SIZE_RUN,RIGHT_SIZE_RUN,REGION,EXP_QTY,EXP_STATUS,COUNTRY,"
    				+ "ITEMNUMBER,PRIORITY,FACTRECDATE,FACTACCPDATE,REQUSHIPDATE,EXOTIC,REMAKE,SHIPPER,BILLTOREGION,SHIPTOSTUDIO,POSTALCODE,"
    				+ "URL1,URL2,URL3,URL4,URL5,URL6,URL7,URL8,URL9,URL10,UP_USER,UP_DATE) VALUES('"+OD_NO+"',TO_DATE('"+ORDER_DATE+"','YYYY/MM/DD'),'"+WORK_ORDER_ID+"','"+TYPE+"','"+SHIP_GROUP_ID+"','"+ORDER_ID+"','"+NIKE_SH_ARITCLE
    				+"','"+MODEL_NA+"','"+SH_STYLENO+"','"+ORDER_NUM+"','"+LEFT_SIZE_RUN+"','"+RIGHT_SIZE_RUN+"','"+REGION+"','"+EXP_QTY+"','"+EXP_STATUS+"','"+COUNTRY
    				+"','"+ITEMNUMBER+"','"+PRIORITY+"',TO_DATE('"+FACTRECDATE+"','MM/dd/yyyy'),TO_DATE('"+FACTACCPDATE+"','MM/dd/yyyy'),TO_DATE('"+REQUSHIPDATE+"','MM/dd/yyyy'),'"+EXOTIC+"','"+REMAKE+"','"+SHIPPER+"','"+BILLTOREGION+"','"+SHIPTOSTUDIO+"','"+POSTALCODE
    				+"','"+URL1+"','"+URL2+"','"+URL3+"','"+URL4+"','"+URL5+"','"+URL6+"','"+URL7+"','"+URL8+"','"+URL9+"','"+URL10+"','"+_userInfo.getAccount()+"',TO_DATE('"+Format.format(new Date())+"','YYYY/MM/DD'))";	
    		System.out.println(sql);		
    		try {
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.executeUpdate();
				pstm.close();
				System.out.println(WORK_ORDER_ID+"訂單主信息匯入成功！！！");
			} catch (Exception e) {
				e.printStackTrace();
				Errmessage+=WORK_ORDER_ID+"訂單主信息有誤！！"+e;
				conn.rollback();
				return;
			}
		}			
	 }

	//判斷是否是重複導單 如主單中存在，則不再重複導入，如果主檔不存在，則刪除明細檔的資料，無論有沒有。
	private Boolean Check(String WORK_ORDER_ID, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null,pstm1=null;
		ResultSet rs = null;
		Boolean Exist=false;
		
		String 	sql="SELECT * FROM "+TABLE+" WHERE WORK_ORDER_ID='"+WORK_ORDER_ID+"'";	
		
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				System.out.println(TABLE+">>>>>"+sql);
				Exist=true;
			}else{
				String Sql1="DELETE DSID01_TEMP2 WHERE WORK_ORDER_ID='"+WORK_ORDER_ID+"' ";
				System.err.println("DSID01_TEMP2刪除>>>>>"+Sql1);
				try {
					pstm1 = conn.prepareStatement(Sql1);
					pstm1.executeUpdate();
					pstm1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Exist=false;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(pstm1!=null){
			pstm1.close();
		}


		return Exist;
	}



	private String GetOd_no(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String OD_NO="",Str1 = "FNJ-",Str2 = "";

//		String 	sql="SELECT MAX(OD_NO) MOD_NO FROM DSID01 WHERE OD_NO LIKE '"+Str1+"%'";	
		//整合虛擬單和正式單的訂單號
		String 	sql="SELECT MAX(OD_NO) MOD_NO FROM (SELECT OD_NO FROM DSID01 UNION SELECT OD_NO FROM DSID01_TEMP) WHERE OD_NO LIKE 'FNJ-%'";	

		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				if(!"".equals(rs.getString("MOD_NO")) && rs.getString("MOD_NO")!=null){
					Str2 =("000000"+(Integer.valueOf(rs.getString("MOD_NO").substring(4))+1));
				}else{
					Str2="000001";
				}
			}else{
				Str2="000001";
			}
			
//			rs.close();
//			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
		}

		OD_NO=Str1+Str2.substring(Str2.length()-6, Str2.length());
		System.err.println(">>>>>OD_NO :"+OD_NO);
		return OD_NO;
	}



	private void ShowMessage() {
		// TODO Auto-generated method stub
		if(Errmessage.length()>0){
			Messagebox.show("xml文件匯入失敗！！！"+Errmessage);
		}else{
			Messagebox.show("xml文件匯入成功！！！");
			ShowResult.setValue(Index+"筆資料已匯入！！");
		}	
		Errmessage="";
		Index=0;
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
