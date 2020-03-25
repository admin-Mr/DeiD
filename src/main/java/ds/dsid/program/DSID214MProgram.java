package ds.dsid.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ds.common.services.UserCredential;
import ds.dsid.domain.DSID214;
import util.Common;
import util.ComponentColumn;
import util.Detail;
import util.Master;
import util.OperationMode;
import util.openwin.ReturnBox;

public class DSID214MProgram extends Master{

	@Wire
	Window windowMaster;
	
	@Wire							//按钮
	private Button btnQuery,btnCreateMaster,btnEditMaster,btnSaveMaster,
	btnCancelMaster,btnDeleteMaster,onqryDSID04;
	
	@Wire							//显示框
	Textbox txtFRSTGID,txtMODEL_NA,txtUP_USER,query_ac_na;
	
	@Wire							//時間顯示框
	Datebox txtSH_LASTNO1;
	
	
	private UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	
	@Override
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		setWhereConditionals(getWhereConditionals());
		masterComponentColumns.add(new ComponentColumn<String>(txtMODEL_NA, "BOX_NAME", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtFRSTGID, "BOX_ID", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtUP_USER, "UP_USER", null, null, null));
		masterComponentColumns.add(new ComponentColumn<String>(txtSH_LASTNO1, "UP_DATE", null, null, null));
	}
	

	@Listen("onOpenQueryField = #windowMaster")
	public void onOpenQueryField(ForwardEvent event) {
		final HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("parentWindow", windowMaster);
		map.put("WindoWidth", "60%");
		map.put("WindoHeight", "80%");
		map.put("Sizable", true);
		map.put("Closable", true);
		map.put("Maximizable", true);
		map.put("multiple", false);

		map.put("xmlQryID", "dsid/DSID214"); 
		ArrayList<ReturnBox> returnBoxList = new ArrayList<ReturnBox>();
		returnBoxList.add(new ReturnBox("box_name", query_ac_na));
		map.put("returnTextBoxList", returnBoxList);
		Executions.createComponents("/util/openwin/QueryField.zul", null, map);
	}


	private  String GetSec() {
		// TODO Auto-generated method stub
			Connection conn = Common.getDbConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String SEC="";
			String sql="SELECT MAX(BOX_ID) BOX_ID FROM DSID214";	
			try {
				ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = ps.executeQuery();	
				if(rs.next()){
					
					if(!"".equals(rs.getString("BOX_ID"))&&rs.getString("BOX_ID")!=null){
						SEC=""+(Integer.valueOf(rs.getString("BOX_ID"))+1);
						SEC=SEC.substring(SEC.length()-6, SEC.length());
					}else{
						SEC="";
					}
				}
				rs.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SEC;
	}
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub//获取根窗口
		return windowMaster;
	}
	
	
	@Override
	protected Class getMasterClass() {
		// TODO Auto-generated method stub//获取主程式
		return DSID214MProgram.class;
	}

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub//获取实体类
		return DSID214.class;
	}
	
	@Override
	protected OperationMode getOperationMode() {
		// TODO Auto-generated method stub//获取操作模式
		return OperationMode.NORMAL;
	}

	@Override
	protected Class getDetailClass(int indexDetail) {
		// TODO Auto-generated method stub//获取细节的类
		return getDetailProgram(indexDetail).getDetailClass();
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		// TODO Auto-generated method stub//获取详细程式
		return getArrDetailPrograms().get(indexDetail);
	}


	@Override
	protected ArrayList<String> getMasterKeyName() {//获得主键的名称
		// TODO Auto-generated method stub
		ArrayList<String> masterKey = new ArrayList<String>();
		masterKey.add("BOX_ID");
		return masterKey;
	}
	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {//获取实体类的内容
		// TODO Auto-generated method stub
		DSID214 entity = (DSID214) entityMaster;
		ArrayList<String> masterKeyValue = new ArrayList<String>();
		masterKeyValue.add(entity.getBOX_ID());
		return masterKeyValue;
	}

	@Override
	protected String getPagingId() {//获得页面的id
		// TODO Auto-generated method stub
		return "pagingCourse";
	}

	@Override
	protected String getWhereConditionals() {//获得查询后的数据
		// TODO Auto-generated method stub
		String sql="";
		if(!query_ac_na.getValue().isEmpty()){
			sql+=" AND BOX_NAME='"+query_ac_na.getValue()+"'";
		}
		
		return sql;
	}

	@Override
	protected String getDetailConditionals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterCreateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterUpdateZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getMasterQueryZul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap getMasterCreateMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {//重新编辑的程式
		// TODO Auto-generated method stub
		DSID214 entity = (DSID214) entityMaster;
		txtMODEL_NA.setValue(entity == null ? "" : entity.getBOX_NAME());
		txtUP_USER.setValue(entity == null ? "" : entity.getUP_USER());
		txtSH_LASTNO1.setValue(entity == null ? null : entity.getUP_DATE());
		txtFRSTGID.setValue(entity == null ? "" : entity.getBOX_ID());
	}

	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean beforeMasterDel(Object entityMaster) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void doCreateDefault() {//创建默认值
		// TODO Auto-generated method stub
		String SEC=GetSec();
		txtFRSTGID.setValue(SEC);
		txtUP_USER.setValue(_userInfo.getAccount());
		txtSH_LASTNO1.setValue(new Date());
	}

	@Override
	protected Object doSaveDefault(String columnName) {//设置默认值
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEntityName() {//获得实体类的名称
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageSize() {//获取页面，0表示5页
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	protected List getCustList() {//获得列表返回值是空
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void masterReadMode(HashMap<String, Object> mapButton){//设置按钮与资料框是否禁用与只读
			mapButton = new HashMap<String, Object>();
			mapButton.put("btncreatemaster", btnCreateMaster);
			mapButton.put("btnquery", btnQuery);		
			super.masterReadMode(mapButton);		
			btnCancelMaster.setDisabled(true);
			btnEditMaster.setDisabled(false);
			btnDeleteMaster.setDisabled(false);
			btnSaveMaster.setDisabled(false);
			
			txtMODEL_NA.setReadonly(true);
			/*txtFRSTGID.setReadonly(true);
			txtSH_LASTNO1.setReadonly(true);
			txtUP_USER.setReadonly(true);*/
		}
	@Override
	public void masterCreateMode(HashMap<String, Object> mapButton){//设置按钮与资料框是否禁用与只读
			mapButton = new HashMap<String, Object>();
			mapButton.put("btncreatemaster", btnCreateMaster);
			mapButton.put("btnquery", btnQuery);
			super.masterCreateMode(mapButton);
			super.masterCreateMode(mapButton);
			btnSaveMaster.setDisabled(false);
			btnCancelMaster.setDisabled(false);
			btnEditMaster.setDisabled(true);
			btnDeleteMaster.setDisabled(true);
			
			txtMODEL_NA.setReadonly(false);
			/*txtUP_USER.setReadonly(false);
			txtSH_LASTNO1.setReadonly(false);
			txtFRSTGID.setReadonly(false);*/
		}

}
