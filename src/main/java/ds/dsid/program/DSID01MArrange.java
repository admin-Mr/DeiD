package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ds.common.services.CRUDService;
import ds.dsid.domain.DSID01;
import util.Common;
import util.QueryWindow;

public class DSID01MArrange extends QueryWindow{

	@WireVariable
	private CRUDService CRUDService;
	@Wire
	Window ArrangeWindow;
	@Wire
	Datebox query_order_date;
	@Wire
	Textbox query_model_na,query_size_min,query_size_max,query_region,query_sh_styleno,Max_num;
	@Wire
	Button btnSearch,btnConfirm_id,btnArrange,btnReset,btnSwitch,btnConfirm_All;
	@Wire
	Listbox queryListBox;
	Boolean IS_PG=false;
	@Wire
	Label tip_label;


	DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
	int C_num=0;
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

//		doSearch();
	}
	
	//清空查詢條件
	@Listen("onClick = #btnEmpty")
	public void onClickbtnEmpty(Event event){		
		query_model_na.setText("");
		query_size_min.setText("");
		query_size_max.setText("");
		query_region.setText("");
		query_sh_styleno.setText("");
		Max_num.setText("");
//		System.out.println("清空完成！");
	}
	
	
	//切換派工狀態。
	@Listen("onClick = #btnSwitch")
	public void onClickbtnSwitch(Event event){
		if(IS_PG==false){
			btnSwitch.setLabel(Labels.getLabel("DSID.MSG0001"));
			btnConfirm_All.setLabel(Labels.getLabel("DSID.MSG0003"));
			IS_PG=true;
			btnConfirm_id.setVisible(false);
			btnArrange.setVisible(false);
			btnReset.setVisible(true);
			doSearch();
		}else if(IS_PG==true){
			btnSwitch.setLabel(Labels.getLabel("DSID.MSG0002"));
			btnConfirm_All.setLabel(Labels.getLabel("DSID.MSG0004"));
			IS_PG=false;
			btnConfirm_id.setVisible(true);
			btnArrange.setVisible(true);
			btnReset.setVisible(false);
			doSearch();
		}
		
	}
	
	//確認選中
	@Listen("onClick = #btnConfirm_id")
	public void onClickbtnConfirm(Event event){
		
		if("".equals(query_order_date.getValue())||query_order_date.getValue()==null){
			Messagebox.show(Labels.getLabel("DSID.MSG0005"));
		}else{
			ArrayList<Object> arrList = new ArrayList<Object>();		
			arrList.clear();
			try{
				if (queryListBox.getSelectedItem() == null){
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);				
					return;
				}else{
					
					for(Listitem ltAll : queryListBox.getItems()){
						if (ltAll.isSelected()){						
							arrList.add((Object)ltAll.getValue());
						}
					}
					
					String  work_id="",work_id_group="";
					for(int i=0; i<arrList.size(); i++){
						work_id=((DSID01)arrList.get(i)).getWORK_ORDER_ID();
						work_id_group+="'"+work_id+"',";
					}
					System.err.println(">>>work_id_group "+work_id_group);
					ChangeStatus(work_id_group.substring(0, work_id_group.length()-1),"7");
					
					doSearch();
				}	
			}catch (NullPointerException e){			
				e.getStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ShowTip();
		}
	}
	
	//確認全部
	@Listen("onClick = #btnConfirm_All")
	public void onClickbtnConfirm_All(Event event) throws SQLException{
		doConfirm_All();
	}
	
	//
	private void doConfirm_All() throws SQLException {
		// TODO Auto-generated method stub
		String status="",woi_sql="";
		
		if("".equals(query_order_date.getValue())||query_order_date.getValue()==null){
			Messagebox.show(Labels.getLabel("DSID.MSG0005"));
		}else{
			if(queryListBox.getItemCount()<=0){
				Messagebox.show(Labels.getLabel("DSID.MSG0006"));
			}else{
				if(IS_PG==false){
					status="7";
				}else if(IS_PG==true){
					status="0";
				}
				
				woi_sql="SELECT t.WORK_ORDER_ID FROM DSID01 t "+getSQLWhere();
				
				System.out.println(">>>>"+woi_sql);
				ChangeStatus(woi_sql ,status);
				
				Messagebox.show(Labels.getLabel("DSID.MSG0007"));
				doSearch();
				ShowTip();
			}
		}

	}

	//派工
	@Listen("onClick = #btnArrange")
	public void onClickbtnArrange(Event event){
		doArrange();
	}
	
	//取消派工
	@Listen("onClick = #btnReset")
	public void onClickbtnReset(Event event) throws SQLException{
		doReset();
	}	
	
	//更改選中的訂單狀態，已方便派工
	private void ChangeStatus(String work_id_group,String status) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement pstm=null;	
		String 	sql="UPDATE DSID01 SET STATUS='"+status+"', UP_DATE=SYSDATE WHERE WORK_ORDER_ID IN ("+work_id_group+")";	
		System.out.println(sql);		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();
			pstm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pstm!=null){
				pstm.close();
			}
			Common.closeConnection(conn);
		}
			
	}
	
	/*
	 * 訂單排序
	 * 1、特殊訂單 、普通訂單
	 * 2、型體排序(DSID10)
	 * 3、Tooling_size 大到小
	 * 4、8雙一輪
	 * 
	 */
	private void doArrange() {
		// TODO Auto-generated method stub
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null,ps2 = null,ps3 = null;
		ResultSet rs = null,rs2 = null,rs3 = null;
		DateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		
		List<String> Typelist=new ArrayList<>();
		String Errmessage="";
		

		if("".equals(query_order_date.getValue())||query_order_date.getValue()==null){
			Messagebox.show(Labels.getLabel("DSID.MSG0005"));
		}else{
			
			if(!"".equals(Max_num.getValue())&&Max_num.getValue()!=null){
				if(CheckMax_num(conn,query_order_date.getValue(),Max_num.getValue())==false){
					return;
				}
			}
			
			
			String 	sql1="SELECT DISTINCT TYPE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(query_order_date.getValue())+"' AND STATUS='7'  ORDER BY TYPE DESC";	
			System.out.println(">>> ："+sql1);		
			try {
				ps = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = ps.executeQuery();	
				while(rs.next()){
					Typelist.add(rs.getString("TYPE"));
				}
				rs.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//輪次
			int Tolal_num=0;
			int ON_ORDER=1;
			System.out.println(">>>Typelist.size() ："+Typelist.size());	
			
			for(int i=0 ;i< Typelist.size();i++){
				
				String 	sql2="SELECT DISTINCT A.NIKE_SH_ARITCLE,B.ORDER_NUM FROM DSID01 A,DSID10 B WHERE A.NIKE_SH_ARITCLE=B.NIKE_SH_ARITCLE AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(query_order_date.getValue())+"' AND TYPE='"+Typelist.get(i)+"' AND STATUS='7'  ORDER BY ORDER_NUM ASC";	
				System.out.println(">>> ："+sql2);		
				try {
					ps2 = conn.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					rs2 = ps2.executeQuery();	
					while(rs2.next()){
						String Extrasql="";
						if(rs2.getString("NIKE_SH_ARITCLE").contains("NIKE AIR ZOOM PEGASUS 35")){
							Extrasql=" GROUP2,";
						}
						String 	sql3="SELECT A.OD_NO,ROWNUM MODEL_NUM,MOD(ROWNUM,8) ALL_NUM ,TOOLING_SIZE FROM ( SELECT * FROM DSID01 A WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(query_order_date.getValue())+"' AND TYPE='"+Typelist.get(i)+"' AND NIKE_SH_ARITCLE='"+rs2.getString("NIKE_SH_ARITCLE")+"' AND STATUS='7' ORDER BY "+Extrasql+" TO_NUMBER(TOOLING_SIZE) DESC) A";	
						System.out.println(">>> ："+sql3);		
						try {
							ps3 = conn.prepareStatement(sql3, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							rs3 = ps3.executeQuery();	
							while(rs3.next()){
								String MODEL_NUM=rs3.getString("MODEL_NUM");
								String ALL_NUM=rs3.getString("ALL_NUM");
								
								if("1".equals(ALL_NUM)||ALL_NUM=="1"){
									Tolal_num+=1;
								}
								
								String ROUND=String.valueOf(Tolal_num).replace(".0", "")+"-"+ALL_NUM.replace("0", "8");
								System.out.println(">>>指令："+rs3.getString("OD_NO")+" 輪次："+ROUND);	
								
								String 	Upsql="UPDATE DSID01 SET ON_ORDER='"+ON_ORDER+"',IS_PG='Y',MODEL_ROUND_NUM='"+MODEL_NUM+"', ALL_ROUND_NUM='"+Tolal_num+"', ROUND='"+ROUND+"',PG_DATE=TO_DATE('"+Format.format(query_order_date.getValue())+"','YYYY/MM/DD') WHERE OD_NO='"+rs3.getString("OD_NO")+"'";	
								System.out.println(">>>輪次更新："+Upsql);		
								try {
									PreparedStatement pstm = conn.prepareStatement(Upsql);
									pstm.executeUpdate();
									pstm.close();
									ON_ORDER++;
								} catch (Exception e) {
									Errmessage=Labels.getLabel("DSID.MSG0008")+rs3.getString("OD_NO")+", "+e;
									e.printStackTrace();
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
			
			if(Errmessage.length()>0){
				Messagebox.show(Labels.getLabel("DSID.MSG0009")+Errmessage);
			}else{
				Messagebox.show(Labels.getLabel("DSID.MSG0010"));
			}	
			Errmessage="";
		}
		Common.closeConnection(conn);	
	}

	
	private Boolean CheckMax_num(Connection conn, Date date, String Max_num) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean status=true;

		String 	sql="SELECT COUNT(*) COU FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+Format.format(date)+"' AND STATUS='7'  ORDER BY TYPE DESC";	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){

				if(!"".equals(rs.getString("COU"))){
					if(Double.valueOf(rs.getString("COU"))>Double.valueOf(Max_num)){
						status=false;
						Messagebox.show(Labels.getLabel("DSID.MSG0011")+rs.getString("COU")+Labels.getLabel("DSID.MSG0012")+Max_num+"，"+Labels.getLabel("DSID.MSG0013"));
					}
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}

	private void doReset() throws SQLException {
		// TODO Auto-generated method stub
		
		if("".equals(query_order_date.getValue())||query_order_date.getValue()==null){
			Messagebox.show(Labels.getLabel("DSID.MSG0005"));
		}else{
			ArrayList<Object> arrList = new ArrayList<Object>();		
			arrList.clear();
			try{
				if (queryListBox.getSelectedItem() == null){
					Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);				
					return;
				}else{
					
					for(Listitem ltAll : queryListBox.getItems()){
						if (ltAll.isSelected()){						
							arrList.add((Object)ltAll.getValue());
						}
					}

					String  work_id="",work_id_group="";
					for(int i=0; i<arrList.size(); i++){
						work_id=((DSID01)arrList.get(i)).getWORK_ORDER_ID();
//						System.err.println(">>>work_id "+work_id);
						work_id_group+="'"+work_id+"',";
					}
					System.err.println(">>>work_id_group "+work_id_group);
					ChangeStatus(work_id_group.substring(0, work_id_group.length()-1),"0");
					doSearch();
				}	
			}catch (NullPointerException e){			
				e.getStackTrace();
			}
			ShowTip();
		}
	}
	
	private void ShowTip() {
		Connection conn=Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String order_date=Format.format(query_order_date.getValue());
		String 	sql="SELECT * FROM (SELECT COUNT(*) A_NUM FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+order_date+"' ) A,(SELECT COUNT(*) C_NUM FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+order_date+"' AND STATUS='7') B";	
		System.out.println(">>>>>"+sql);
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();	
			if(rs.next()){
				tip_label.setValue(order_date+" "+Labels.getLabel("DSID.MSG0014")+rs.getString("A_NUM")+Labels.getLabel("DSID.MSG0015")+"，"+Labels.getLabel("DSID.MSG0016")+rs.getString("C_NUM")+Labels.getLabel("DSID.MSG0015")+"。");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Common.closeConnection(conn);
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return ArrangeWindow;
	}

	@Override
	protected List<String> setComboboxColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> setComboboxCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSQLWhere() {
		// TODO Auto-generated method stub
		String SQL = " WHERE 1=1 ";
//		String SQL = " WHERE 1=1 AND TYPE!='2'";   --虛擬訂單 不派工
		
		if(IS_PG==true){
			SQL+=" AND t.STATUS ='7'";
		}else{
			SQL+=" AND t.STATUS !='7'";
		}
				
		if(query_order_date.getValue()!=null){		
			SQL+=" AND TO_CHAR(t.ORDER_DATE,'YYYY/MM/DD')='"+Format.format(query_order_date.getValue())+"'";
		}
		if(!query_model_na.getValue().isEmpty()){
			SQL+=" AND t.MODEL_NA LIKE '%"+query_model_na.getValue()+"%'";
		}
		if(!query_size_min.getValue().isEmpty()){
			SQL+=" AND t.LEFT_SIZE_RUN >= TO_NUMBER('"+query_size_min.getValue()+"')";
		}
		if(!query_size_max.getValue().isEmpty()){
			SQL+=" AND t.LEFT_SIZE_RUN <= TO_NUMBER('"+query_size_max.getValue()+"')";
		}
		if(!query_region.getValue().isEmpty()){
			SQL+=" AND t.REGION='"+query_region.getValue()+"'";
		}
		if(!query_sh_styleno.getValue().isEmpty()){
			SQL+=" AND t.SH_STYLENO='"+query_sh_styleno.getValue()+"'";
		}

		System.out.println(">>>query id "+SQL);
		
		return SQL;
	}

	@Override
	protected String getCustomSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCustomCountSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return "DSID01";
	}

	@Override
	protected String getTextBoxValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingSuppiler";
	}

	@Override
	protected String getOrderby() {	
		String Sql="";
		if(IS_PG==true){
			Sql=" ORDER BY t.WORK_ORDER_ID ";
		}else{
			Sql=" ORDER BY t.UP_DATE DESC";
		}
		System.out.println(">>>query id "+Sql);
		return Sql;
	}


	@Override
	protected Combobox getcboColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Combobox getcboCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}



}
