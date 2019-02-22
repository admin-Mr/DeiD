package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.poi.hssf.usermodel.HSSFCell;
import org.zkoss.poi.hssf.usermodel.HSSFCellStyle;
import org.zkoss.poi.hssf.usermodel.HSSFFont;
import org.zkoss.poi.hssf.usermodel.HSSFRow;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.SimpleDateFormat;

import ds.common.services.CRUDService;

import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

public class DSID21MBatDelete extends OpenWinCRUD{

	@Wire private Window windowMaster;
	@Wire private CRUDService CRUDService;
	@Wire private Button iMportDelete, btnQuery1;
	@Wire private Datebox Import_Date1, Import_Date2;
	@Wire private Listbox List_Model_na ;
	
	
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		//doSearch();
		
	}
	
	@Listen("onClick = #iMportDelete")
	public void onClickiMportDelete(){
		
		Connection conn = Common.getDbConnection();
		String Modelna = "", Batdate1 = "", Batdate2 = "";
		SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
		
		Batdate1 = Format.format(Import_Date1.getValue());	
		Batdate2 = Format.format(Import_Date2.getValue());
		
		// 抓取 Listbox 文本框內選定的值 .
		for(Listitem ltAll : List_Model_na.getItems()){
			if(ltAll.isSelected()){
				Modelna = ltAll.getValue().toString();
			}
		}
		
		//Messagebox.show(" 測試抓取文本資料 ↓ \n 型體名稱 : "+Modelna+"\n 匯入日期 : " +Batdate1+ " - " + Batdate2);
		
		if(Modelna != null || !"".equals(Modelna)){
			
			BatchDelete(conn, Modelna, Batdate1, Batdate2);
			
		}else{
			Messagebox.show(Labels.getLabel("DSID.MSG0127"));
		}
		
	}

	private void BatchDelete(Connection conn, String modelna, String batdate1, String batdate2) {
		// TODO Auto-generated method stub
		
		ResultSet  selectRs = null, deleteRs = null;	
		PreparedStatement  selectPs = null, deletePs;
		
		String AndModel = "";
		String AndDate = "and to_char(import_date,'YYYY/MM/DD') between '"+batdate1+"' and '"+batdate2+"'";
		
		if(modelna != "All"){
			AndModel = " and model_na = '"+modelna+"'";
		}
		
		// 刪除資料前 查詢該資料是否存在, 如無資料則提示使用者
		String selectSql = "select * from dsid21 where 1 = 1 "+ AndDate + AndModel;
		System.out.println(" ----- select Sql : "+selectSql);
		
		try {
			selectPs = conn.prepareStatement(selectSql);
			selectRs = selectPs.executeQuery();
			
			if(selectRs.next()){
				
				String deleteSql = "delete from dsid21 where 1 = 1 " +AndDate + AndModel;
				System.err.println(" ----- Delete Sql "+deleteSql);
				
				try {
					deletePs = conn.prepareStatement(deleteSql);
					deletePs.executeUpdate();
					deletePs.close();
					Messagebox.show(batdate1+" - "+batdate2+Labels.getLabel("DSID.MSG0128")+"！");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show(batdate1+" - "+batdate2+Labels.getLabel("DSID.MSG0129")+"！");
				}
				
			}else {
				Messagebox.show(Labels.getLabel("DSID.MSG0130")+": \n "+batdate1+" - "+batdate2+" \n "+Labels.getLabel("DSID.MSG0131"));
			}
			
			selectRs.close();
			selectPs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 抓取 型體名稱 放入Listbox 供使用者選擇
	 */
	@Listen("onClick = #btnQuery1")
	public void onClickbtnQuery2(){
		
		ResultSet  rs = null;
		PreparedStatement  ps = null;
		Connection conn = Common.getDbConnection();
		
		if(List_Model_na.getSelectedItem() != null){
			for(Listitem ltAll : List_Model_na.getItems()){
				if (ltAll.isSelected()){
					if(!"".equals((Object)ltAll.getValue())&&(Object)ltAll.getValue()!=null){
						
						Messagebox.show(Labels.getLabel("DSID.MSG0132"));		
						
					}
				}
			}
		}else{

			List<String> Grnolist = new ArrayList<String>();
			Grnolist.add("");
			Grnolist.add("All");
			
			String sql = "select distinct MODEL_NA from dsid21 order by MODEL_NA asc";
			System.out.println(" ----- 測試獲取  Grno : " + sql);
			
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					Grnolist.add(rs.getString("MODEL_NA"));
				}
				rs.close();
				ps.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
//				Messagebox.show("好像沒有找到任何部位名稱 ... 找人吧 ！");
				Messagebox.show(Labels.getLabel("DSID.MSG0133"));
			} finally {	
				Common.closeConnection(conn);	
			}
			/*for(String s : Grnolist){
				System.out.println(" ----- Gtnolist : "+s);
			}*/
			List_Model_na.setModel(new ListModelList<Object>(Grnolist));
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
	protected void addDetailPrograms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap getReturnMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
