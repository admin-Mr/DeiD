package ds.dsid.program;

/**
 * 組底針車可否配套
 */

import java.math.BigDecimal;
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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import bsh.org.objectweb.asm.Label;
import ds.common.services.CRUDService;
import ds.dsid.domain.ReadIDPic03Dao;
import util.Common;
import util.QueryWindow;

public class ReadIDPic02 extends QueryWindow{

	@WireVariable private CRUDService CRUDService;
	@Wire Window WindowQuery;
	@Wire private Button btnSearch;
	@Wire private Datebox Querydate1, Querydate2;
	@Wire Listbox queryListBox;
	@Wire private Label msgLabel;
	@Wire private Combobox cboColumn, cboCondition;

	private String QDate1, QDate2;
	private int i = 0;
//	private String PG_DATE, MODEL_NA, URL1, ROUND, TOOLING_SIZE, SEWING_DATE, BOTTOM_TIME, BOO;
	SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
	
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService2");
		setCRUDService(CRUDService);
		System.out.println("0.5");
		
	}
	
	@Listen("onClick = #btnSearch")
	public void onClickbtnCancel(Event event) {
		
//		List<String> listData = GetData();
		
		try {
			
			QDate1 = Format.format(Querydate1.getValue());
			QDate2 = Format.format(Querydate2.getValue());
			System.out.println(" ----- Date : " + QDate1 +"<"+ i++ +">"+ QDate2);
			
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("未選擇時間區間!");
			e.printStackTrace();
			return;
		}
	}
	
	@Override 
	protected List getCustList() {
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy/MM/dd");
		List<ReadIDPic03Dao> lst = new ArrayList<ReadIDPic03Dao>(); //創建 泛型為實體類型 集合
		Query qry = super.getQueryPagingbySize();
		if (qry != null) {
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				Object[] obj = (Object[]) i.next();
				ReadIDPic03Dao e = new ReadIDPic03Dao();
				
				e.setPG_DATE(sdf.format((Date) obj[0]));
				e.setMODEL_NA((String) obj[1]);
				e.setURL1((String) obj[2]);
				e.setROUND((String) obj[3]);
				e.setTOOLING_SIZE(String.valueOf((BigDecimal) obj[4]));
				e.setSEWING_DATE((String) obj[5]);
				e.setBOTTOM_TIME((String) obj[6]);
				e.setBOO((String) obj[7]);
				lst.add(e);
			}
		}
		return lst;
	}
	
	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return WindowQuery;
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

	@Override // 查詢條件
	protected String getSQLWhere() { 
		// TODO Auto-generated method stub
		return " and 1=1 ";
	}

	@Override // 查詢語句
	protected String getCustomSQL() {
		
		return "select t.pg_date,q.model_na,q.url1,q.round,q.tooling_size,"
				+ "decode(t.sewing_date,null,'','√')sewing_date,decode(t.bottom_time,null,'','√')bottom_time,"
				+ "(CASE  WHEN t.SEWING_DATE IS NOT NULL AND t.BOTTOM_TIME IS NOT NULL THEN 'OK' END) boo "
				+ "from dsid30 q,dsid65 t "
				+ "where q.od_no = t.od_no  and t.pg_date = q.pg_date and is_del = 'N'  "
				+ " and to_char(t.pg_date,'yyyy/MM/dd')between '"+QDate1+"' and '"+QDate1+"' ";
//		
//		return "select t.pg_date,q.model_na,q.url1,q.round,q.tooling_size,"
//		+ "decode(t.sewing_date,null,'','√')sewing_date,decode(t.bottom_time,null,'','√')bottom_time,"
//		+ "(CASE  WHEN t.SEWING_DATE IS NOT NULL AND t.BOTTOM_TIME IS NOT NULL THEN 'OK' END) boo "
//		+ "from dsid30 q,dsid65 t "
//		+ "where q.od_no = t.od_no  and t.pg_date = q.pg_date and is_del = 'N'  "
//		+ " and to_char(t.pg_date,'yyyy/MM/dd')between '"+QDate1+"' and '"+QDate2+"' "
//		+ " order by to_number(q.url1) asc";
		
	}

	@Override
	protected String getCustomCountSQL() {
		return "select count(*) "
				+ "from dsid30 q,dsid65 t "
				+ "where q.od_no = t.od_no  and t.pg_date = q.pg_date and is_del = 'N'  "
				+ " and to_char(t.pg_date,'yyyy/MM/dd')between '"+QDate1+"' and '"+QDate1+"' ";
	}

	@Override
	protected String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTextBoxValue() {
		// TODO Auto-generated method stub
		return "aa";
	}

	@Override 
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingOperation";
	}

	@Override // 排序
	protected String getOrderby() {
		// TODO Auto-generated method stub
		
		return " order by to_number(q.url1) asc  ";
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
}
