package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.dsid.domain.DSID04_SIZE;
import ds.dsid.domain.DSID215;
import util.Common;
import util.QueryBase;
import util.openwin.ReturnBox;
import util.BlackBox;
import util.BlackBox.temp;

public class DSID215MProgram extends QueryBase {

	@Wire
	private Window windowQuery;
	@Wire
	private Combobox cboREP_CNAMEQ,cboREP_CNAMEQ1;// 下拉選
	@Wire
	private Textbox BOX_NAME;// 盒號
	@Wire
	private Button onqryEL_NO1, onqryDSID04;
	@Wire
	private Textbox MODEL_NA1, qry_MODEL_NA;
	@Wire
	private Listbox masterListboxX;
	@Wire
	private Button btnQuery, btnCreateMaster, btnEditMaster, btnDeleteMaster,btnClear;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onOpen = #cboREP_CNAMEQ")
	public void onChangeREP_CNAMEQ() { // 
		List<temp> lstREP_CNAME;		
		 lstREP_CNAME = BlackBox.getListModelList("BOX_NAME",
				 "BOX_NAME", "DSID214", 
				 "", null, CRUDService);
		 cboREP_CNAMEQ.setModel(new ListModelList(lstREP_CNAME, true));	 
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		dofili();
	}
	
	@Listen("onClick = #btnQuery")
	public void onOKTextbox1() {
		if(!qry_MODEL_NA.getValue().trim().isEmpty()){
			doSearch1();
		}else {
			dofili();
		}
	}
	
	@Listen("onClick = #btnCreateMaster")
	public void onOKCreateMaster() {
		added();
	}
	
	@Listen("onClick = #btnEditMaster")
	public void onOKEditMaster() {
		modify();
	}
	
	private void modify() {
		// TODO Auto-generated method stub
		Connection DB00 = Common.getDbConnection();
		if(!MODEL_NA1.getValue().trim().isEmpty()&&!cboREP_CNAMEQ1.getValue().trim().isEmpty()){
			List<String> listsize = new ArrayList<String>();
			List<String> listsizerun = new ArrayList<String>();
			int a=0;
			for (double j = 1; j <=20.5 ; j+=0.5) {
				a++;
				listsize.add(String.valueOf(j).replace(".0", ""));
				listsizerun.add(String.valueOf(a));
			}
			
			for(int i=0;i<listsize.size();i++){
				if(cboREP_CNAMEQ1.getValue().equals(listsize.get(i))){
					System.out.println("YYYYYYYYYyyy:"+listsizerun.get(i));
					String sql="UPDATE DSID215 SET BOX_NA"+listsizerun.get(i)+"='"+cboREP_CNAMEQ.getValue().trim()+"' "
							+ "WHERE MODEL_NA = '"+MODEL_NA1.getValue()+"'";				
					try {
						PreparedStatement  uppstm = DB00.prepareStatement(sql,
								ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						uppstm.executeUpdate();
						uppstm.close();
						dofili();
						Messagebox.show("修改成功！");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Messagebox.show("未找到該形體的資料！！！");
					}				
				}
			}	
		}else{
			Messagebox.show("形體、SIZE不可為空！！！");
		}
	}

	private void added() {
		// TODO Auto-generated method stub
		Connection DB00 = Common.getDbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		if(!MODEL_NA1.getValue().trim().isEmpty()){
			String sql="SELECT * FROM DSID215 WHERE MODEL_NA='"+MODEL_NA1.getValue()+"'";
			try {
				ps = DB00.prepareStatement(sql);
				rs = ps.executeQuery();
				if(rs.next()){
					Messagebox.show("該形體:"+MODEL_NA1.getValue()+"已存在！！！");
				}else {
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
					String dString = df.format(new Date()); 
					String hql="INSERT INTO DSID215 (MODEL_NA,SZ1,SZ2,SZ3,SZ4,SZ5,SZ6,SZ7,SZ8,SZ9,SZ10,"
							+ "SZ11,SZ12,SZ13,SZ14,SZ15,SZ16,SZ17,SZ18,SZ19,SZ20,"
							+ "SZ21,SZ22,SZ23,SZ24,SZ25,SZ26,SZ27,SZ28,SZ29,SZ30,"
							+ "SZ31,SZ32,SZ33,SZ34,SZ35,SZ36,SZ37,SZ38,SZ39,SZ40,UP_USER,UP_DATE) VALUES "
							+ "('"+MODEL_NA1.getValue()+"',1,1.5,2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,"
									+ "9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,"
									+ "17.5,18,18.5,19,19.5,20,20.5,'"+ _userInfo.getAccount()+"',to_date ( '" + dString + "', 'YYYY/MM/DD' ))";
					PreparedStatement uppstm = DB00.prepareStatement(hql,
							ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					uppstm.executeUpdate();
					uppstm.close();
					dofili();
					Messagebox.show("添加成功");
				}
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}else{
			Messagebox.show("形體不可為空！！！");
		}
	}

	private void doSearch1() {
		// TODO Auto-generated method stub
		List<DSID215> list = new ArrayList<DSID215>();
		Connection DB00 = Common.getDbConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String sql="SELECT * FROM DSID215 WHERE MODEL_NA='"+qry_MODEL_NA.getValue()+"'";
		try {
			ps1 = DB00.prepareStatement(sql);
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				System.out.println("YYYYYYYYYYYY");
			DSID215 dsid04_SIZE=new DSID215();
			dsid04_SIZE.setMODEL_NA(rs1.getString("MODEL_NA"));
			dsid04_SIZE.setS1(rs1.getString("Box_Na1"));
			dsid04_SIZE.setS2(rs1.getString("Box_Na2"));
			dsid04_SIZE.setS3(rs1.getString("Box_Na3"));
			dsid04_SIZE.setS4(rs1.getString("Box_Na4"));
			dsid04_SIZE.setS5(rs1.getString("Box_Na5"));
			dsid04_SIZE.setS6(rs1.getString("Box_Na6"));
			dsid04_SIZE.setS7(rs1.getString("Box_Na7"));
			dsid04_SIZE.setS8(rs1.getString("Box_Na8"));
			dsid04_SIZE.setS9(rs1.getString("Box_Na9"));
			dsid04_SIZE.setS10(rs1.getString("Box_Na10"));
			dsid04_SIZE.setS11(rs1.getString("Box_Na11"));
			dsid04_SIZE.setS12(rs1.getString("Box_Na12"));
			dsid04_SIZE.setS13(rs1.getString("Box_Na13"));
			dsid04_SIZE.setS14(rs1.getString("Box_Na14"));
			dsid04_SIZE.setS15(rs1.getString("Box_Na15"));
			dsid04_SIZE.setS16(rs1.getString("Box_Na16"));
			dsid04_SIZE.setS17(rs1.getString("Box_Na17"));
			dsid04_SIZE.setS18(rs1.getString("Box_Na18"));
			dsid04_SIZE.setS19(rs1.getString("Box_Na19"));
			dsid04_SIZE.setS20(rs1.getString("Box_Na20"));
			dsid04_SIZE.setS21(rs1.getString("Box_Na21"));
			dsid04_SIZE.setS22(rs1.getString("Box_Na22"));
			dsid04_SIZE.setS23(rs1.getString("Box_Na23"));
			dsid04_SIZE.setS24(rs1.getString("Box_Na24"));
			dsid04_SIZE.setS25(rs1.getString("Box_Na25"));
			dsid04_SIZE.setS26(rs1.getString("Box_Na26"));
			dsid04_SIZE.setS27(rs1.getString("Box_Na27"));
			dsid04_SIZE.setS28(rs1.getString("Box_Na28"));
			dsid04_SIZE.setS29(rs1.getString("Box_Na29"));
			dsid04_SIZE.setS30(rs1.getString("Box_Na30"));
			dsid04_SIZE.setS31(rs1.getString("Box_Na31"));
			dsid04_SIZE.setS32(rs1.getString("Box_Na32"));
			dsid04_SIZE.setS33(rs1.getString("Box_Na33"));
			dsid04_SIZE.setS34(rs1.getString("Box_Na34"));
			dsid04_SIZE.setS35(rs1.getString("Box_Na35"));
			dsid04_SIZE.setS36(rs1.getString("Box_Na36"));
			dsid04_SIZE.setS37(rs1.getString("Box_Na37"));
			dsid04_SIZE.setS38(rs1.getString("Box_Na38"));
			dsid04_SIZE.setS39(rs1.getString("Box_Na39"));
			dsid04_SIZE.setS40(rs1.getString("Box_Na40"));
			list.add(dsid04_SIZE);
			}
			ps1.close();
			rs1.close();
		masterListboxX.setModel(new ListModelList<Object>(list));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dofili(){
		List<DSID215> list = new ArrayList<DSID215>();
		Connection DB00 = Common.getDbConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String sql="SELECT * FROM DSID215";
		try {
			ps1 = DB00.prepareStatement(sql);
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
			DSID215 dsid04_SIZE=new DSID215();
			dsid04_SIZE.setMODEL_NA(rs1.getString("MODEL_NA"));
			dsid04_SIZE.setS1(rs1.getString("Box_Na1"));
			dsid04_SIZE.setS2(rs1.getString("Box_Na2"));
			dsid04_SIZE.setS3(rs1.getString("Box_Na3"));
			dsid04_SIZE.setS4(rs1.getString("Box_Na4"));
			dsid04_SIZE.setS5(rs1.getString("Box_Na5"));
			dsid04_SIZE.setS6(rs1.getString("Box_Na6"));
			dsid04_SIZE.setS7(rs1.getString("Box_Na7"));
			dsid04_SIZE.setS8(rs1.getString("Box_Na8"));
			dsid04_SIZE.setS9(rs1.getString("Box_Na9"));
			dsid04_SIZE.setS10(rs1.getString("Box_Na10"));
			dsid04_SIZE.setS11(rs1.getString("Box_Na11"));
			dsid04_SIZE.setS12(rs1.getString("Box_Na12"));
			dsid04_SIZE.setS13(rs1.getString("Box_Na13"));
			dsid04_SIZE.setS14(rs1.getString("Box_Na14"));
			dsid04_SIZE.setS15(rs1.getString("Box_Na15"));
			dsid04_SIZE.setS16(rs1.getString("Box_Na16"));
			dsid04_SIZE.setS17(rs1.getString("Box_Na17"));
			dsid04_SIZE.setS18(rs1.getString("Box_Na18"));
			dsid04_SIZE.setS19(rs1.getString("Box_Na19"));
			dsid04_SIZE.setS20(rs1.getString("Box_Na20"));
			dsid04_SIZE.setS21(rs1.getString("Box_Na21"));
			dsid04_SIZE.setS22(rs1.getString("Box_Na22"));
			dsid04_SIZE.setS23(rs1.getString("Box_Na23"));
			dsid04_SIZE.setS24(rs1.getString("Box_Na24"));
			dsid04_SIZE.setS25(rs1.getString("Box_Na25"));
			dsid04_SIZE.setS26(rs1.getString("Box_Na26"));
			dsid04_SIZE.setS27(rs1.getString("Box_Na27"));
			dsid04_SIZE.setS28(rs1.getString("Box_Na28"));
			dsid04_SIZE.setS29(rs1.getString("Box_Na29"));
			dsid04_SIZE.setS30(rs1.getString("Box_Na30"));
			dsid04_SIZE.setS31(rs1.getString("Box_Na31"));
			dsid04_SIZE.setS32(rs1.getString("Box_Na32"));
			dsid04_SIZE.setS33(rs1.getString("Box_Na33"));
			dsid04_SIZE.setS34(rs1.getString("Box_Na34"));
			dsid04_SIZE.setS35(rs1.getString("Box_Na35"));
			dsid04_SIZE.setS36(rs1.getString("Box_Na36"));
			dsid04_SIZE.setS37(rs1.getString("Box_Na37"));
			dsid04_SIZE.setS38(rs1.getString("Box_Na38"));
			dsid04_SIZE.setS39(rs1.getString("Box_Na39"));
			dsid04_SIZE.setS40(rs1.getString("Box_Na40"));
			list.add(dsid04_SIZE);
			}
			masterListboxX.setModel(new ListModelList<Object>(list));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ps1.close();
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	@Listen("onSelect = #masterListboxX")
	public void clickMasterListbox(Event event) {
		Listitem listitem = (Listitem) ((SelectEvent) event).getReference();
		if (masterListboxX.getSelectedItem() != null) {
			DSID215 detailEntity = (DSID215) listitem.getValue();
			MODEL_NA1.setValue(detailEntity.getMODEL_NA());
		}
	}
	
	@Listen("onClick = #btnClear")
	public void onClickbtnClear(Event evnet) { // 清空
		cboREP_CNAMEQ.setValue("");
		cboREP_CNAMEQ1.setValue("");
		MODEL_NA1.setValue(""); 
		qry_MODEL_NA.setValue("");
	}
	
	@Listen("onOpenQueryField = #windowQuery")
	public void onOpenQueryField(ForwardEvent event) {
		final HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("parentWindow", windowQuery);
		map.put("WindoWidth", "60%");
		map.put("WindoHeight", "80%");
		map.put("Sizable", true);
		map.put("Closable", true);
		map.put("Maximizable", true);
		map.put("multiple", false);

		if (event.getData().equals("onqryDSID04")) {
			map.put("xmlQryID", "dsid/DSID215"); 
			ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
			returnBoxList.add(new ReturnBox("MODEL_NA", qry_MODEL_NA));
			map.put("returnTextBoxList", returnBoxList);
		} else if (event.getData().equals("onqryEL_NO1")) {
			map.put("xmlQryID", "dsid/DSID21_old"); 
			ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
			returnBoxList.add(new ReturnBox("MODEL_NA", MODEL_NA1));
			map.put("returnTextBoxList", returnBoxList);
		}

		Executions.createComponents("/util/openwin/QueryField.zul", null, map);
	}

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowQuery;
	}

	@Override
	protected Class getQueryClass() {
		// TODO Auto-generated method stub
		return DSID215MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWhereConditionals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void resetEditArea(Object entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getPageSize() {
		// TODO Auto-generated method stub
		return 10;
	}
	
	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

}
