package ds.dsid.program;

/**
 * 返修登記
 */
import java.awt.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sun.org.apache.xpath.internal.operations.Div;

import util.Common;
import util.QueryWindow;

public class ReadIDPic03 extends QueryWindow{

	@Wire private Window Arrangewindow;
	@Wire private Button btnSwitch; // 測試按鈕
	@Wire private Textbox Barcode, choice;
	@Wire private Div Fanxiu;
	
	private String Bar = "", Cho = "";
	private int ErrorJs = 0;
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return Arrangewindow;
	}
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		
		System.err.println(" ----- 1.0");
		choice.setVisible(false); // 隱藏選擇狀態顯示
		System.err.println(" ----- 2.0");
	}
	
	@Listen("onOK = #Fanxiu")
	public void onOKBarcode(){
		
		Bar = Barcode.getValue().toString();
		Cho = choice.getValue();

		System.err.println(" ----- Barcode : " + Bar + " ----- Choice : " + Cho);
		
		UpdataBar(); // 資料更新方法
		
		if(ErrorJs <= 0){
			Messagebox.show("登記成功 ! Barcod: " + Bar);
		}
	}

	/**
	 * 按選擇項 更新資料庫數據
	 */
	public void UpdataBar(){
		
		PreparedStatement ps = null;
		Connection Conn = Common.getDB01Conn();
		
		String sql = "update dsid65 set is_repair = 'Y', repair_type = '"+Cho+"' where work_order_id = '"+Bar+"'" ;
		System.out.println(" ----- Updata : " + sql);
		
		try {
			ps = Conn.prepareStatement(sql);
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorJs++;
		}finally{
			Common.closeConnection(Conn);
		}
		
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
		return null;
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
		return null;
	}

	@Override
	protected String getTextBoxValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOrderby() {
		// TODO Auto-generated method stub
		return null;
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
		return 0;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}
}
