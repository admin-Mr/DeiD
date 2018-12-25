package ds.dshr.program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import util.Common;

@SuppressWarnings("rawtypes")
public class DSHR_EmpDirect_Controller extends GenericForwardComposer {
	@WireVariable
	private ds.common.services.CRUDService CRUDService;
	private static final long serialVersionUID = 1L;
	private Datebox years;
	Listbox dataListbox;
	ArrayList<String> title_data = new ArrayList<String>();

	public void onClick$btnQuery(Event event) {
		dataListbox.getItems().clear();// 清空資料
		HashMap<String, String> mapMcid_totalvalue = new HashMap<String, String>();
		HashMap<String, String> mapNo6id_totalvalue = new HashMap<String, String>();
		HashMap<String, String> mapNo7id_totalvalue = new HashMap<String, String>();
		HashMap<String, String> mapNo8id_totalvalue = new HashMap<String, String>();
		HashMap<String, String> mapAirid_totalvalue = new HashMap<String, String>();
		HashMap<String, String> mapGasid_totalvalue = new HashMap<String, String>();

		HashMap<String, String> mapMcid_value = new HashMap<String, String>();
		HashMap<String, String> mapNo6id_value = new HashMap<String, String>();
		HashMap<String, String> mapNo7id_value = new HashMap<String, String>();
		HashMap<String, String> mapNo8id_value = new HashMap<String, String>();
		HashMap<String, String> mapAirid_value = new HashMap<String, String>();
		HashMap<String, String> mapGasid_value = new HashMap<String, String>();

		// ip 10.8.1.121 dbid shdhv1 dsit
		dataListbox.getItems().clear();
		PreparedStatement pstmtData = null;
		ResultSet rs = null;
		Connection conn = null;
		String sql = "", yearsM = null, selectM = null;
		datedata();
		// 月份搜尋
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		if (years.getValue() != null && years.getValue() != null) {
			yearsM = dateFormat.format((years.getValue()));
		}

		// 查詢月份的1號 yyyy/MM/01
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/01");
		if (years.getValue() != null && years.getValue() != null) {
			selectM = dateFormat1.format((years.getValue()));
		}

		try {
			conn = Common.getMySQLDbConnection_mysql();
			ArrayList<String> date_value = title_data;
			for (int j = 0; j < date_value.size(); j++) {
				// MC
				sql = "select user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime,max(user_datahour.read_value) * min(user_info.beilv) as read_value1,max(user_datahour.read_value) as read_value2 from user_datahour_"
						+ yearsM
						+ " user_datahour inner join user_info on user_info.id = user_datahour.meter_terminal_id where user_datahour.meter_terminal_id = '473b9dce-95c0-4236-913e-96b72d19ca5e' and user_datahour.read_time <> '"
						+ selectM
						+ " 00:00:00'  and user_datahour.before_value > 0 group by user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')"
						// #6
						+ " UNION ALL select user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime,max(user_datahour.read_value) * min(user_info.beilv) as read_value1,max(user_datahour.read_value) as read_value2 from user_datahour_"
						+ yearsM
						+ " user_datahour inner join user_info on user_info.id = user_datahour.meter_terminal_id where user_datahour.meter_terminal_id = 'e99cbaf0-3fc9-4e96-a20a-7b7400381718' and user_datahour.read_time <> '"
						+ selectM
						+ " 00:00:00'  and user_datahour.before_value > 0 group by user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')"
						// #7
						+ " UNION ALL select user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime,max(user_datahour.read_value) * min(user_info.beilv) as read_value1,max(user_datahour.read_value) as read_value2 from user_datahour_"
						+ yearsM
						+ " user_datahour inner join user_info on user_info.id = user_datahour.meter_terminal_id where user_datahour.meter_terminal_id = 'af6c3312-20cc-45ae-8545-c03bec4b73af' and user_datahour.read_time <> '"
						+ selectM
						+ " 00:00:00'  and user_datahour.before_value > 0 group by user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')"
						// #8
						+ " UNION ALL select user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime,max(user_datahour.read_value) * min(user_info.beilv) as read_value1,max(user_datahour.read_value) as read_value2 from user_datahour_"
						+ yearsM
						+ " user_datahour inner join user_info on user_info.id = user_datahour.meter_terminal_id where user_datahour.meter_terminal_id = '53f059e5-9242-475f-ba4f-b265db283305' and user_datahour.read_time <> '"
						+ selectM
						+ " 00:00:00'  and user_datahour.before_value > 0 group by user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')"
						// Air
						+ " UNION ALL select user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime,max(user_datahour.read_value) * min(user_info.beilv) as read_value1,max(user_datahour.read_value) as read_value2 from user_datahour_"
						+ yearsM
						+ " user_datahour inner join user_info on user_info.id = user_datahour.meter_terminal_id where user_datahour.meter_terminal_id = '3316862c-8fdb-4fb6-a36e-8cc6510afc1f' and user_datahour.read_time <> '"
						+ selectM
						+ " 00:00:00'  and user_datahour.before_value > 0 group by user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')"
						// Barometric pressure
						+ " UNION ALL select user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime,max(user_datahour.read_value) * min(user_info.beilv) as read_value1,max(user_datahour.read_value) as read_value2 from user_datahour_"
						+ yearsM
						+ " user_datahour inner join user_info on user_info.id = user_datahour.meter_terminal_id where user_datahour.meter_terminal_id = '41fe86af-9fda-4ab8-a58c-91a3f00ff3e6' and user_datahour.read_time <> '"
						+ selectM
						+ " 00:00:00'  and user_datahour.before_value > 0 group by user_datahour.meter_terminal_id,DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')";

				pstmtData = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = pstmtData.executeQuery();

				while (rs.next()) {
					String id = rs.getString("meter_terminal_id");
					String date = rs.getString("read_daytime");
					if (id != null && !"".equalsIgnoreCase(id)) {
						String totalValue = (rs.getString("read_value1") == null) ? (" ")
								: (String.valueOf(rs.getString("read_value1")));
						String value = (rs.getString("read_value2") == null) ? (" ")
								: (String.valueOf(rs.getString("read_value2")));
						if ("473b9dce-95c0-4236-913e-96b72d19ca5e".equalsIgnoreCase(id)) {
							mapMcid_totalvalue.put(date, totalValue);
							mapMcid_value.put(date, value);
						} else if ("e99cbaf0-3fc9-4e96-a20a-7b7400381718".equalsIgnoreCase(id)) {
							mapNo6id_totalvalue.put(date, totalValue);
							mapNo6id_value.put(date, value);
						} else if ("af6c3312-20cc-45ae-8545-c03bec4b73af".equalsIgnoreCase(id)) {
							mapNo7id_totalvalue.put(date, totalValue);
							mapNo7id_value.put(date, value);
						} else if ("53f059e5-9242-475f-ba4f-b265db283305".equalsIgnoreCase(id)) {
							mapNo8id_totalvalue.put(date, totalValue);
							mapNo8id_value.put(date, value);
						} else if ("3316862c-8fdb-4fb6-a36e-8cc6510afc1f".equalsIgnoreCase(id)) {
							mapAirid_totalvalue.put(date, totalValue);
							mapAirid_value.put(date, value);
						} else if ("41fe86af-9fda-4ab8-a58c-91a3f00ff3e6".equalsIgnoreCase(id)) {
							mapGasid_totalvalue.put(date, totalValue);
							mapGasid_value.put(date, value);
						}
					}
				}

				for (int i = 0; i <= date_value.size(); i++) {
					String[] sub1 = date_value.get(i).split(",");
					Listitem listItem = new Listitem();
					Listcell listCell1 = new Listcell();
					Listcell listCell2 = new Listcell();
					Listcell listCell3 = new Listcell();
					Listcell listCell4 = new Listcell();
					Listcell listCell5 = new Listcell();
					Listcell listCell6 = new Listcell();
					Listcell listCell7 = new Listcell();

					listCell1.setLabel(sub1[j]);

					String mc_totalvalue = mapMcid_totalvalue.get(sub1[j]);
					String mc_value = mapMcid_value.get(sub1[j]);
					listCell2.setLabel(mc_totalvalue + " (" + mc_value + ")");

					String no6_totalvalue = mapNo6id_totalvalue.get(sub1[j]);
					String no6_value = mapNo6id_value.get(sub1[j]);
					listCell3.setLabel(no6_totalvalue + " (" + no6_value + ")");

					String no7_totalvalue = mapNo7id_totalvalue.get(sub1[j]);
					String no7_value = mapNo7id_value.get(sub1[j]);
					listCell4.setLabel(no7_totalvalue + " (" + no7_value + ")");

					String no8_totalvalue = mapNo8id_totalvalue.get(sub1[j]);
					String no8_value = mapNo8id_value.get(sub1[j]);
					listCell5.setLabel(no8_totalvalue + " (" + no8_value + ")");

					String air_totalvalue = mapAirid_totalvalue.get(sub1[j]);
					String air_value = mapAirid_value.get(sub1[j]);
					listCell6.setLabel(air_totalvalue + " (" + air_value + ")");

					String gas_totalvalue = mapGasid_totalvalue.get(sub1[j]);
					String gas_value = mapGasid_value.get(sub1[j]);
					listCell7.setLabel(gas_totalvalue + " (" + gas_value + ")");

					listItem.appendChild(listCell1);
					listItem.appendChild(listCell2);
					listItem.appendChild(listCell3);
					listItem.appendChild(listCell4);
					listItem.appendChild(listCell5);
					listItem.appendChild(listCell6);
					listItem.appendChild(listCell7);

					dataListbox.appendChild(listItem);
				}
			}
			pstmtData.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			Common.closeConnection(conn);
		}
	}

	void datedata() {
		title_data = new ArrayList<String>();
		PreparedStatement pstmtData2 = null;
		ResultSet rs2 = null;
		Connection conn2 = null;
		String yearsM = null, selectM = null;

		// 月份搜尋
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		if (years.getValue() != null && years.getValue() != null) {
			yearsM = dateFormat.format((years.getValue()));
		}
		// 查詢月份的1號 yyyy/MM/01
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/01");
		if (years.getValue() != null && years.getValue() != null) {
			selectM = dateFormat1.format((years.getValue()));
		}
		try {
			conn2 = Common.getMySQLDbConnection_mysql();
			// date
			String sql_date = "select DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d') as read_daytime from user_datahour_"
					+ yearsM + " user_datahour where user_datahour.read_time <> '" + selectM
					+ " 00:00:00'  and user_datahour.before_value > 0 group by DATE_FORMAT(if(HOUR(user_datahour.read_time) = 0, DATE_SUB(user_datahour.read_time,INTERVAL 1 DAY), user_datahour.read_time),'%Y%m%d')";
			pstmtData2 = conn2.prepareStatement(sql_date, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs2 = pstmtData2.executeQuery();

			while (rs2.next()) {
				title_data.add(rs2.getString("read_daytime"));
			}
			pstmtData2.close();
			rs2.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			Common.closeConnection(conn2);
		}
	}
}
