package ds.dsid.program;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.zkoss.image.AImage;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.ibm.icu.text.SimpleDateFormat;
import com.sun.org.apache.bcel.internal.generic.NEW;

import ds.common.services.UserCredential;
import ds.dsid.domain.ReadIDPic03Dao;
import oracle.net.aso.s;
import util.Common;
import util.QueryWindow;
import util.urlDownload;

public class ReadIDPicProgram extends QueryWindow {

	private static final long serialVersionUID = 1L;
	protected final UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");
	@Wire
	private Window windowMaster;
	@Wire
	private Textbox Barcode;
	@Wire
	private Datebox BarcodeDate;
	@Wire
	private Image imageMax, image0, image1, image2, image3;
	@Wire
	private Button Dayscanning, Anumberof, Sumowe, imageHome;
	@Wire
	private Button btnConfirm, btnRefresh, Networkimg, Serverimg;
	@Wire
	private Textbox Odno, OdDate, ShGroupID, WorkID, ModelM, LeftSize, SMdate;
	@Wire
	private Textbox WTNot_Neat, WTNot_Cancel, WTNot_Redo, WTNot_Repair, VIP_ID, Scnin_Date;
	@Wire
	private Textbox Cz1, Cz2, Cz3, Cz4, Zc1, Zc2, Zc3, Zc4, Cx1, Cx2, Cx3, Cx4, Zd1, Zd2, Zd3, Zd4, Ch1, Ch2, Ch3, Ch4;
	@Wire
	private Img img;

	private int JsMax = 0;
	private Queue<String> queue = new LinkedList<String>();
	private String BcDate = "";
	private String user = "";
	private boolean Img_SN = true; // True = 網絡圖片 || False = 服務器本地圖片
	private boolean Jsstate = false;
	private String Idname, sDate;
	private String zcname = "";
	
	private AImage Aimg0, Aimg1, Aimg2, Aimg3;
	private AImage Durl0, Durl1, Durl2, Durl3;
	private AImage[] ServerImg = new AImage[5];

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat Strfor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	String strBc = "";
	String[] url = new String[5];
	String[] Data = new String[10];

	// 入口
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		user = _userInfo.getAccount();
		setReadonly(); // 設置 Textbox 為不可編輯狀態
		setImgReadonly(); // 設置 Imnage 組件 點擊時間
		getUsetname(user); // 獲取 賬號 與 製程關聯
		System.out.println(" ----- 結束 2 ----- ");
	}

	// 開始
	@Listen("onOK = #Barcode")
	public void onClickbtnConfirm() throws IOException, SQLException {

		strBc = Barcode.getValue();
		String Bc = strBc.toString();
		String User = "";
		url = getUrl(Bc);
		Connection Conn = Common.getDB01Conn(); // DB連接
		try {
			// Bc = Barcode.getValue();
			if (Bc == null || "".equals(Bc)) {
				Messagebox.show(" 條碼欄不能為空 或 未检测到條碼扫描 !! ");
				return;
			} else {
				System.out.println(" ----- Bc.length() : " + Barcode.getValue().length());
				System.out.println(" ----- User " + user);
				if (!"XEID9".equals(user)) {
					if (Bc.toString().length() < 0 && Bc.toString().length() > 8) {
						Messagebox.show("條碼不正確, 請核對!!");
						return;
					}
				}
			}

			Jsstate = true;
			sDate = Strfor.format(new Date()); // 獲取掃描時間

			// 组底专有 日期选择空间, 组底账户:XEID9
			if (_userInfo.getAccount() == "XEID9" || "XEID9".equals(_userInfo.getAccount())) {
				BcDate = format.format(BarcodeDate.getValue());
				String BcDemo = Barcode.getValue();
				Bc = SetBarDate(Bc, BcDemo, BcDate);
				System.out.println(" ----- 組底Bc : " + Bc);
			}

			System.err.println(" ----- 開始 ----- Bc碼: " + Bc + " ----- Bc日期: " + BcDate + " ----- ID: " + Idname
					+ " ----- 掃描時間: " + sDate);

			Data = getData(Bc, BcDate, zcname); // 獲取訂單資料
			String[] Data2 = getState(Bc, user, BcDate); // 獲取警示資料

			// 恢復 Image 為顯示狀態
			image0.setVisible(true);
			image1.setVisible(true);
			image2.setVisible(true);
			image3.setVisible(true);
			imageMax.setVisible(true);

			// 訂單資訊顯示
			Odno.setValue(Data[0]);
			OdDate.setValue(Data[1]);
			ShGroupID.setValue(Data[2]);
			WorkID.setValue(Data[3]);
			ModelM.setValue(Data[4]);
			LeftSize.setValue(Data[5]);

			// 警示信息顯示
			WTNot_Neat.setValue(Data2[0]);
			WTNot_Cancel.setValue(Data2[1]);
			WTNot_Redo.setValue(Data2[2]);
			WTNot_Repair.setValue(Data2[3]);
			VIP_ID.setValue(Data2[4]);
			// 如不是组底与程序制程 可不显示该位置资料.
			if (_userInfo.getAccount() == "XEID6" || "XEID6".equals(_userInfo.getAccount())) {
				Scnin_Date.setValue(Data2[5]);
			}
			SMdate.setValue(sDate);

			Erimage(Bc); // 圖片顯示
			setReidDate(Bc, user); // 會寫掃描時間
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			Barcode.setText("");
			Bc = "";
			System.out.println(" ----- 結束 ----- ");
			Common.closeConnection(Conn);
		}
	}

	// 默認圖片顯示
	public void Erimage(String Bc) throws IOException {

		try {
			// onClickNetworkimg(Bc); // 網絡圖片
			onClickServerimg(); // 服务器图片
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 網絡圖片Set
	@Listen("onClick = #Networkimg")
	public void onClickNetworkimg() throws Exception {

		System.out.println(" 進入 網絡圖片! ");
		String Bcstr = strBc;

		Img_SN = true;// 備註控制, 未使用

		// String[] url = getUrl(Bcstr); // 默認為網絡圖片
		System.err.println(" ----- Net workimg");
		imageMax.setSrc("");
		String urls = "";
		AImage aImg = null;
		List<AImage> Imglist = new ArrayList<AImage>();
		Imglist = urlDownload.toImage(url);

		for (int s = 0; s < Imglist.size(); s++) {
			if (s == 0) {
				image0.setContent(Imglist.get(s));
				Durl0 = Imglist.get(s);
			} else if (s == 1) {
				image1.setContent(Imglist.get(s));
				Durl1 = Imglist.get(s);
			} else if (s == 2) {
				image2.setContent(Imglist.get(s));
				Durl2 = Imglist.get(s);
			} else if (s == 3) {
				image3.setContent(Imglist.get(s));
				Durl3 = Imglist.get(s);
			}
		}
		// Messagebox.show(" 計數 s : " + s);
		System.err.println(" ----- 圖片切換完畢!");
	}

	// 服務器圖片Set
	@Listen("onClick = #Serverimg")
	public AImage[] onClickServerimg() throws IOException, SQLException {

		System.out.println(" 進入 服務器圖片! ");
		String imgName = "", imgTail = "";
		String Bcstr = strBc;
		Img_SN = false;
		imageMax.setSrc("");

		String[] OdnoDate = getOdnoDate(Bcstr, BcDate); // 订单日期文件夹名, 用于拼凑根据日期选择文件夹

		// url = getUrl(Bcstr); // Url尾數獲取
		imgTail = "-vw-"; // 尾戳
		imgName = getData(Bcstr, BcDate, zcname)[2]; // Ship_group_id
		String[] Uwdata = url[4].split(",");

		System.err.println(" ----- Data[2] :" + imgName);
		System.out.println(" 服務器圖片測試  : " + Uwdata.length);

		for (int i = 0; i < Uwdata.length; i++) {

			Execution execution = Executions.getCurrent();
			Desktop desktop = execution.getDesktop();
			WebApp wb = desktop.getWebApp();

			String Srcws = "", path = "";
			String[] Srcs = null;
			AImage aImg = null;
			try {

				 path = wb.getRealPath(File.separator+ "util" +File.separator+ "idimage" +File.separator+ OdnoDate[0]);

				// ---------- 測試圖片鏈接 -----------
				//path = "//\\10.8.1.207\\erp_ftl_id\\ftl\\WEB-INF\\classes\\dsc\\echo2app\\resource\\idimage-1\\" + OdnoDate + "\\";
				// ---------- 測試圖片鏈接 -----------
				// Messagebox.show(path);

				Srcs = Uwdata[i].split("/"); // 尾數
				String str = Srcs[Srcs.length - 1];
				Srcws = str.substring(str.length() - 1); // 截取末尾數字
				aImg = new AImage(path + File.separator + imgName + imgTail + Srcws + ".jpg");
				//System.out.println(path + File.separator + imgName + imgTail + Srcws + ".jpg" + " ----- 1 -----"); // 路徑圖示
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(" ----- 未在新版图片资料夹找到图片, 切换至旧版图片资料夹!");
				// e.printStackTrace();
				try {

					path = wb.getRealPath(File.separator+ "util" +File.separator+ "idimage");

					// ---------- 測試圖片鏈接 -----------
					//path = "//\\10.8.1.207\\erp_ftl_id\\ftl\\WEB-INF\\classes\\dsc\\echo2app\\resource\\idimage-1\\";
					// ---------- 測試圖片鏈接 -----------
					// Messagebox.show(path);

					Srcs = Uwdata[i].split("/"); // 尾數
					String str = Srcs[Srcs.length - 1];
					Srcws = str.substring(str.length() - 1); // 截取末尾數字
					aImg = new AImage(path + File.separator + imgName + imgTail + Srcws + ".jpg");
					//System.out.println(path + File.separator + imgName + imgTail + Srcws + ".jpg" + " ----- 2 -----"); // 路徑圖示
				} catch (Exception e2) {
					// TODO: handle exception
					System.err.println(" ----- 未在旧版图片资料夹找到图片, 查找最後資料夾!!");
					// e2.printStackTrace();
					try {

						path = wb.getRealPath(File.separator+ "util" +File.separator+ "idimage" +File.separator+ "old");

						// ---------- 測試圖片鏈接 -----------
						//path = "//\\10.8.1.207\\erp_ftl_id\\ftl\\WEB-INF\\classes\\dsc\\echo2app\\resource\\idimage-1\\old\\";
						// ---------- 測試圖片鏈接 -----------
						// Messagebox.show(path);

						Srcs = Uwdata[i].split("/"); // 尾數
						String str = Srcs[Srcs.length - 1];
						Srcws = str.substring(str.length() - 1); // 截取末尾數字
						aImg = new AImage(path + File.separator + imgName + imgTail + Srcws + ".jpg");
						//System.out.println(path + File.separator + imgName + imgTail + Srcws + ".jpg" + " ----- 3 -----"); // 路徑圖示
					} catch (Exception e3) {
						// TODO: handle exception
						System.out.println("未在所有位置查找到圖片, 請核查圖片下載情況及下載文件夾!");
					}
				}
			}

			// System.out.println(" ----- Path: " + path + imgName + imgTail +
			// Srcws + ".jpg");

			if (i == 0) {
				image0.setContent(aImg);
				ServerImg[0] = aImg;
			}
			if (i == 1) {
				image1.setContent(aImg);
				ServerImg[1] = aImg;
			}
			if (i == 2) {
				image2.setContent(aImg);
				ServerImg[2] = aImg;
			}
			if (i == 3) {
				image3.setContent(aImg);
				ServerImg[3] = aImg;
			}
		}

		/*
		 * for(int i=0; i<ServerImg.length; i++){
		 * System.out.println(" ----- Server Img: " + ServerImg[i]); }
		 */
		// Messagebox.show("已切換至服務器圖片 ! ");
		System.err.println(" ----- 圖片切換完畢!");
		return ServerImg;
	}

	// 第三部分數據顯示
	@Listen("onClick = #btnRefresh")
	public void AccordingData() throws SQLException {

		System.out.println(" ----- 進入第三部分!");
		List<String[]> listdata03 = getData3();
		String[] Cz = null, Zc = null, Cx = null, Zd = null, Ch = null;

		for (int i = 0; i < listdata03.size(); i++) {
			if (i == 0 && listdata03.get(0) != null) {
				Cz = listdata03.get(i);
			}
			if (i == 1 && listdata03.get(1) != null) {
				Zc = listdata03.get(i);
			}
			if (i == 2 && listdata03.get(2) != null) {
				Cx = listdata03.get(i);
			}
			if (i == 3 && listdata03.get(3) != null) {
				Zd = listdata03.get(i);
			}
			if (i == 4 && listdata03.get(4) != null) {
				Ch = listdata03.get(i);
			}
		}

		Cz1.setValue(Cz[0]);
		Cz2.setValue(Cz[1]);
		Cz3.setValue(Cz[2]);
		Cz4.setValue(Cz[3]);
		Zc1.setValue(Zc[0]);
		Zc2.setValue(Zc[1]);
		Zc3.setValue(Zc[2]);
		Zc4.setValue(Zc[3]);
		Cx1.setValue(Cx[0]);
		Cx2.setValue(Cx[1]);
		Cx3.setValue(Cx[2]);
		Cx4.setValue(Cx[3]);
		Zd1.setValue(Zd[0]);
		Zd2.setValue(Zd[1]);
		Zd3.setValue(Zd[2]);
		Zd4.setValue(Zd[3]);
		Ch1.setValue(Ch[0]);
		Ch2.setValue(Ch[1]);
		Ch3.setValue(Ch[2]);
		Ch4.setValue(Ch[3]);
	}

	// 根據 work_order_id 查詢订单數據
	public String[] getData(String Bc, String BcDate, String zcname) throws SQLException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection Conn = Common.getDB01Conn(); // DB連接
		String sql = "";
		String bcda = "";
		// String Bcstr = String.valueOf(Bc);

		String[] SuData = new String[6];
		String[] Bdate = BcDate.split("-");
		for (String str : Bdate) {
			bcda += str;
		}
		if (zcname == "BOTTOM_TIME") {

			String Bcs = Barcode.getValue();

			sql = "select dsid30.OD_NO,dsid30.ORDER_DATE,dsid30.SHIP_GROUP_ID,dsid30.WORK_ORDER_ID,dsid30.SH_STYLENO,dsid30.LEFT_SIZE_RUN,dsid30.MODEL_NA,dsid30.MODEL_ROUND_NUM,dsid30.PG_DATE "
					+ "from dsid30 left join dsid23 "
					+ "on dsid23.SH_STYLENO = dsid30.SH_STYLENO and dsid23.MODEL_NA = dsid30.MODEL_NA "
					+ "where  dsid30.WORK_ORDER_ID like '%" + bcda + "%' and url1= '" + Bcs + "'";
			//System.out.println(" ----- User Xeid9 : " + sql);
		} else {

			sql = "SELECT od_no, order_date, ship_group_id, work_order_id, sh_styleno, left_size_run FROM dsid30 "
					+ "WHERE work_order_id LIKE '%" + Bc + "'";
			//System.out.println(" ----- User 常规 ID  : " + sql);
		}

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {

				String Odno = rs.getString("od_no"); // 訂單號碼
				String Ordate = format.format(rs.getDate("order_date")); // 訂購日期
				String ShipID = rs.getString("ship_group_id"); // Ship_ID
				String WorkID = rs.getString("work_order_id"); // Work_ID
				String Shstyl = rs.getString("sh_styleno"); // 型體編號(配色)
				String Leftsize = rs.getString("left_size_run"); // 左腳碼數

				SuData[0] = Odno;
				SuData[1] = Ordate;
				SuData[2] = ShipID;
				SuData[3] = WorkID;
				SuData[4] = Shstyl;
				SuData[5] = Leftsize;

				//System.out.println(" ----- SHIP_ID : " + ShipID);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Messagebox.show("訂單資訊查詢失敗!");
			e.printStackTrace();
		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}
			Common.closeConnection(Conn);
		}
		return SuData;
	}

	// 判斷屬於哪個製程, 返回想對應的 警示信息
	public String[] getState(String Bc, String user, String BcDate) throws IOException, SQLException {
		// System.out.println(" ----- Bc 測試2 : " + Bc);
		String[] Data = new String[7];
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection Conn = Common.getDB01Conn();
		user = _userInfo.getAccount(); // 獲取登陸賬戶
		String Userid = ""; // getUsetname(user);
		// System.out.println(" ----- user ------ : " + user);

		// user 賬戶名稱, 對應固定製程 .
		if (user == "XEID10" || "XEID10".equals(user)) {
			Userid = "SCAN_DATE";
		} else if (user == "XEID9" || "XEID9".equals(user)) {
			Userid = "SEWING_DATE";
		} else if (user == "XEID8" || user == "XEID7" || "XEID8".equals(user) || "XEID7".equals(user)) {
			Userid = "FORMING_DATE";
		} else if (user == "XEID6" || "XEID6".equals(user)) {
			Userid = "BOTTOM_TIME";
		} else if (user == "XEID3" || user == "XEID5" || "XEID3".equals(user) || "XEID5".equals(user)) {
			Userid = "DECIDE_DATE";
		} else if (user == "XEID1" || user == "XEID2" || "XEID1".equals(user) || "XEID2".equals(user)) {
			Userid = "DECIDE_DATE";
		} else if (user == "DSGPI") {
			Userid = "DECIDE_DATE";
			Messagebox.show("該為測試賬戶, 請使用現場賬號作測試！");
		}

		// System.out.println(" ----- Userid : " + Userid);
		// 掃描日 組底掃描日 是否取消 是否重做 是否返修 返修狀態 郵編
		String state = "", Okdate = "", Isdel = "", Isre = "", IsRepair = "", RepairType = "", Postal = "";

		if (_userInfo.getAccount() == "XEID9" || "XEID9".equals(_userInfo.getAccount())) {
			String[] code = getOdnoDate(Bc, BcDate);
			Bc = code[1];
		}

		String sql = "select dsid65." + Userid + " as state, "
				+ "dsid65.bottom_ok_date, dsid65.repair_type, dsid65.is_del, dsid65.is_re, dsid65.is_repair, dsid30.postalcode "
				+ "from dsid65, dsid30 " + "where dsid65.work_order_id like '%" + Bc
				+ "' and dsid65.od_no = dsid30.od_no";
		// System.out.println(" ----- State Sql : " + sql);

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {

				state = rs.getString("state"); // 是否配齊
				Isdel = rs.getString("is_del"); // 是否取消
				Isre = rs.getString("is_re"); // 是否為重做單
				IsRepair = rs.getString("is_repair"); // 是否返修
				RepairType = rs.getString("repair_type"); // 返修類型
				Postal = rs.getString("postalcode"); // 郵編
				Okdate = rs.getString("bottom_ok_date"); // 組底掃描完成時間

				// System.out.println(" ----- Data: " + state + " --- " + Isdel
				// + " --- " + Isre + " --- " + IsRepair + " --- " + Postal + "
				// --- " + Okdate);

				if (user == "XEID9" || "XEID9".equals(user)) {
					if (state == null || "".equals(state)) {
						state = "無鞋面";
					} else {
						state = "已有鞋面";
					}
				} else if (user == "XEID6" || "XEID6".equals(user)) {
					if (state == null || "".equals(state)) {
						state = "無組底";
					} else {
						state = "已有組底";
					}
				} else {
					state = ""; // 掃描日
				}
				Data[0] = state;
				// Data[1] = Isdel == "N" ? "" : Isdel;
				if (Isdel == "N" || "N".equals(Isdel)) {// 是否取消
					Data[1] = "";
				} else {
					Data[1] = Isdel;
				}
				System.err.println("是否取消 : " + Data[1]);

				if (Isre == "N" || "N".equals(Isre)) {
					Data[2] = "";
				} else {
					Data[2] = "重做單";
				}
				// Data[2] = Isre == "N" ? "" : "重做"; // 是否重做

				Data[3] = IsRepair == "Y" ? RepairType : ""; // 返修類型

				if (Postal == "97005" || "97005".equals(Postal)) {
					Data[4] = "VIP單請注意!";
				} else {
					Data[4] = "";
				}
				// Data[4] = Postal; // VIP

				if (state != "" || !"".equals(state)) {
					Data[5] = state;
				} else {
					Data[5] = "";
				}
				// Data[5] = Okdate; // 掃描完成日期
				// System.out.println(" ----- Data Date : " + Data[5]);

			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Userid = Userid == "" ? null : Userid;
			Messagebox.show(" 警示資訊查詢失敗 ! \n User : " + Userid + " \n User ID 不能為空, 請使用現場賬戶 !");
			e.printStackTrace();

		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}
			Common.closeConnection(Conn);
		}

		return Data;
	}

	// 第三部分 數據查詢
	public List<String[]> getData3() throws SQLException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection Conn = Common.getDB01Conn(); // DB連接
		List<String[]> list = new ArrayList<String[]>();
		String[] Cz = new String[4];
		String[] Zc = new String[4];
		String[] Cx = new String[4];
		String[] Zd = new String[4];
		String[] Ch = new String[4];

		String sql = "SELECT COUNT(CASE  WHEN t.DECIDE_DATE =trunc(sysdate) THEN 'DECIDE_DATE' END) DECIDE_DATE, "
				+ "COUNT(CASE  WHEN t.DECIDE_OK_DATE =trunc(sysdate) AND t.DECIDE_DATE IS NOT NULL THEN 'DECIDE_DATE1' END) DECIDE_DATE1, "
				+ "COUNT(CASE  WHEN t.BOTTOM_OK_DATE =trunc(sysdate) AND t.BOTTOM_TIME IS NOT NULL THEN 'BOTTOM_TIME1' END) BOTTOM_TIME1, "
				+ "COUNT(CASE  WHEN t.SCAN_OK_DATE =trunc(sysdate) AND t.SCAN_DATE IS NOT NULL THEN 'SCAN_DATE1' END) SCAN_DATE1, "
				+ "COUNT(CASE  WHEN t.SEWING_OK_DATE =trunc(sysdate) AND t.SEWING_DATE IS NOT NULL THEN 'SEWING_DATE1' END) SEWING_DATE1, "
				+ "COUNT(CASE  WHEN t.FORMING_OK_DATE =trunc(sysdate) AND t.FORMING_DATE IS NOT NULL THEN  'FORMING_DATE1' END) FORMING_DATE1, "
				+ "COUNT(CASE  WHEN t.DECIDE_OK_DATE =trunc(sysdate) AND t.DECIDE_DATE IS NULL THEN 'DECIDE_DATE2' END) DECIDE_DATE2, "
				+ "COUNT(CASE  WHEN t.BOTTOM_OK_DATE =trunc(sysdate) AND t.BOTTOM_TIME IS NULL THEN 'BOTTOM_TIME2' END) BOTTOM_TIME2, "
				+ "COUNT(CASE  WHEN t.SCAN_OK_DATE =trunc(sysdate) AND t.SCAN_DATE IS NULL THEN 'SCAN_DATE2' END) SCAN_DATE2, "
				+ "COUNT(CASE  WHEN t.SEWING_OK_DATE =trunc(sysdate) AND t.SEWING_DATE IS NULL THEN 'SEWING_DATE2' END) SEWING_DATE2, "
				+ "COUNT(CASE  WHEN t.FORMING_OK_DATE =trunc(sysdate) AND t.FORMING_DATE IS NULL THEN  'FORMING_DATE2' END) FORMING_DATE2, "
				+ "COUNT(CASE  WHEN t.DECIDE_OK_DATE < trunc(sysdate - 100) AND t.DECIDE_DATE IS NULL THEN 'DECIDE_DATE3' END) DECIDE_DATE3, "
				+ "COUNT(CASE  WHEN t.BOTTOM_OK_DATE < trunc(sysdate - 100) AND t.BOTTOM_TIME IS NULL THEN 'BOTTOM_TIME3' END) BOTTOM_TIME3, "
				+ "COUNT(CASE  WHEN t.SCAN_OK_DATE < trunc(sysdate - 100) AND t.SCAN_DATE IS NULL THEN 'SCAN_DATE3' END) SCAN_DATE3, "
				+ "COUNT(CASE  WHEN t.SEWING_OK_DATE < trunc(sysdate - 100) AND t.SEWING_DATE IS NULL THEN 'SEWING_DATE3' END) SEWING_DATE3, "
				+ "COUNT(CASE  WHEN t.FORMING_OK_DATE < trunc(sysdate - 100) AND t.FORMING_DATE IS NULL THEN  'FORMING_DATE3' END) FORMING_DATE3, "
				+ "COUNT(CASE  WHEN t.BOTTOM_TIME =trunc(sysdate) THEN 'BOTTOM_TIME' END) BOTTOM_TIME, "
				+ "COUNT(CASE  WHEN t.SCAN_DATE =trunc(sysdate) THEN 'SCAN_DATE' END) SCAN_DATE, "
				+ "COUNT(CASE  WHEN t.SEWING_DATE =trunc(sysdate) THEN 'SEWING_DATE' END) SEWING_DATE, "
				+ "COUNT(CASE  WHEN t.FORMING_DATE =trunc(sysdate) THEN  'FORMING_DATE' END) FORMING_DATE "
				+ "FROM DSID65 t WHERE IS_DEL ='N'";
		// System.out.println(" ----- Data3 : " + sql);

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {

				// 裁準
				Cz[0] = rs.getString("decide_date");
				Cz[1] = rs.getString("decide_date1");
				Cz[2] = rs.getString("decide_date2");
				Cz[3] = rs.getString("decide_date3");

				// 針車
				Zc[0] = rs.getString("sewing_date");
				Zc[1] = rs.getString("sewing_date1");
				Zc[2] = rs.getString("sewing_date2");
				Zc[3] = rs.getString("sewing_date3");

				// 出貨
				Ch[0] = rs.getString("scan_date");
				Ch[1] = rs.getString("scan_date1");
				Ch[2] = rs.getString("scan_date2");
				Ch[3] = rs.getString("scan_date3");

				// 組底
				Zd[0] = rs.getString("bottom_time");
				Zd[1] = rs.getString("bottom_time1");
				Zd[2] = rs.getString("bottom_time2");
				Zd[3] = rs.getString("bottom_time3");

				// 成型
				Cx[0] = rs.getString("forming_date");
				Cx[1] = rs.getString("forming_date1");
				Cx[2] = rs.getString("forming_date2");
				Cx[3] = rs.getString("forming_date3");

				list.add(Cz);
				list.add(Zc);
				list.add(Cx);
				list.add(Zd);
				list.add(Ch);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Messagebox.show("各製程產量查詢失敗!");
			e.printStackTrace();
		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}
			Common.closeConnection(Conn);
		}
		return list;
	}

	// 獲取掃描圖片鏈接
	public String[] getUrl(Object Bcstr) throws SQLException {

		ResultSet rs = null, rs1 = null;
		PreparedStatement ps = null, ps1 = null;
		Connection Conn = Common.getDB01Conn();
		String[] url = new String[5];
		String sqlurl1 = null, sqlurl2 = null, sqlurl3 = null, sqlurl4 = null;
		// String Bcstr = Bc.toString();

		System.out.println(" Bc 測試輸出 : " + Bcstr);

		String sql = "select url1, url2, url3, url4 from dsid30_pic where work_order_id like '%" + Bcstr + "'";
		System.out.println(" ----- Dsid30 Pic: \n " + sql);

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (!rs.next()) {

				sqlurl1 = "url2";
				sqlurl2 = "url3";
				sqlurl3 = "url4";
				sqlurl4 = "url5";
				System.err.println(" Url 為空!");
			} else {

				sqlurl1 = rs.getString("url1");
				sqlurl2 = rs.getString("url2");
				sqlurl3 = rs.getString("url3");
				sqlurl4 = rs.getString("url4");
				System.err.println(" Url 非空!");
			}

			String sql1 = "select " + sqlurl1 + " url1, " + sqlurl2 + " url2, " + sqlurl3 + " url3, " + sqlurl4
					+ " url4 " + "from dsid30 where work_order_id like '%" + Bcstr + "' ";
			System.out.println(" ----- Dsid30 Url: " + sql1);

			try {
				ps1 = Conn.prepareStatement(sql1);
				rs1 = ps1.executeQuery();

				if (rs1.next()) {

					url[0] = rs1.getString("url1");
					url[1] = rs1.getString("url2");
					url[2] = rs1.getString("url3");
					url[3] = rs1.getString("url4");
					url[4] = url[0] + "," + url[1] + "," + url[2] + "," + url[3];

				}
				rs1.close();
				ps1.close();
			} catch (Exception e) {
				// TODO: handle exception
				Messagebox.show("Dsid30 圖片鏈接查詢失敗!");
				e.printStackTrace();
			}

			for (int i = 0; i < url.length; i++) {
				System.out.println(" ----- Url Data: " + url[i]);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Messagebox.show("Dsid Pic 圖片基礎資料查詢失敗!");
			e.printStackTrace();
		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}
			if(ps1 != null){
				ps1.close();
			}
			if(rs1 != null){
				rs1.close();
			}
			Common.closeConnection(Conn);
		}

		return url;
	}

	// 設定TextBox 為不可編輯狀態
	public void setReadonly() {

		// 訂單資訊
		Odno.setReadonly(true);
		OdDate.setReadonly(true);
		ShGroupID.setReadonly(true);
		WorkID.setReadonly(true);
		ModelM.setReadonly(true);
		LeftSize.setReadonly(true);

		// 警示資訊
		WTNot_Neat.setReadonly(true);
		WTNot_Cancel.setReadonly(true);
		WTNot_Redo.setReadonly(true);
		WTNot_Repair.setReadonly(true);
		VIP_ID.setReadonly(true);
		Scnin_Date.setReadonly(true);
		SMdate.setReadonly(true);

		// 個製程詳單
		Textbox[] StrCz = { Cz1, Cz2, Cz3, Cz4 }; // 裁準
		Textbox[] StrZc = { Zc1, Zc2, Zc3, Zc4 }; // 針車
		Textbox[] StrCx = { Cx1, Cx2, Cx3, Cx4 }; // 成型
		Textbox[] StrZd = { Zd1, Zd2, Zd3, Zd4 }; // 組底
		Textbox[] StrCh = { Ch1, Ch2, Ch3, Ch4 }; // 出貨
		for (int i = 0; i < StrCz.length; i++) {
			StrCz[i].setReadonly(true);
		}
		for (int i = 0; i < StrCz.length; i++) {
			StrZc[i].setReadonly(true);
		}
		for (int i = 0; i < StrCz.length; i++) {
			StrCx[i].setReadonly(true);
		}
		for (int i = 0; i < StrCz.length; i++) {
			StrZd[i].setReadonly(true);
		}
		for (int i = 0; i < StrCz.length; i++) {
			StrCh[i].setReadonly(true);
		}
	}

	// 設定 放大圖片 Button啟用時間
	public void setImgReadonly() {

		if (Jsstate) {
			image0.setVisible(true);
			image1.setVisible(true);
			image2.setVisible(true);
			image3.setVisible(true);
		} else {
			image0.setVisible(false);
			image1.setVisible(false);
			image2.setVisible(false);
			image3.setVisible(false);
		}
	}

	// 回寫掃描時間至對應製程 欄位.
	public void setReidDate(String Bc, String user) throws SQLException {

		PreparedStatement upDateps = null;
		Connection Conn = Common.getDB01Conn();
		String Strsql = "";
		String Zcname = getUsetname(user);
		// System.out.println(" ----- Zcname : " + Zcname);
		// 添加對應 製程掃描時間 對應的數據庫表格欄位
		String date1 = format.format(new Date());

		if (Bc != null && !"".equals(Bc) && Bc != "") {
			String upDatesql = "update dsid65 set " + Zcname + " = to_date('" + date1
					+ "','yyyy/mm/dd') where work_order_id like '%" + Bc + "'";
			// System.err.println(" ----- upDate Sql: " + upDatesql);
			// Messagebox.show( upDatesql );`

			try {
				upDateps = Conn.prepareStatement(upDatesql);
				upDateps.executeUpdate();
				upDateps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Messagebox.show("更新掃描時間失敗 ! ");
			} finally {
				if(upDateps != null){
					upDateps.close();
				}
				Common.closeConnection(Conn);
			}
		}

	}

	// 賬號對應製程名稱獲取
	public String getUsetname(String user) {

		// user = _userInfo.getAccount(); // 獲取登陸賬戶
		System.out.println(" ----- User ID : " + user);

		if (user == "XEID1" || user == "XEID2" || "XEID1".equals(user) || "XEID2".equals(user)) {
			zcname = "DECIDE_DATE"; // 裁準 需要查詢的對應製程資料
			System.out.println(" ----- 准备 - 制程账号 : " + zcname);
		} else if (user == "XEID3" || user == "XEID5" || "XEID3".equals(user) || "XEID5".equals(user)) {
			zcname = "SEWING_DATE"; // 針車
			System.out.println(" ----- 針車 - 制程账号 : " + zcname);
		} else if (user == "XEID6" || "XEID6".equals(user)) {
			zcname = "FORMING_DATE"; // 成型
			System.out.println(" ----- 成型 - 制程账号 : " + zcname);
		} else if (user == "XEID7" || user == "XEID8" || "XEID7".equals(user) || "XEID8".equals(user)) {
			zcname = "SCAN_DATE"; // 出貨
			System.out.println(" ----- 出貨 - 制程账号 : " + zcname);
		} else if (user == "XEID9" || "XEID9".equals(user)) {
			zcname = "BOTTOM_TIME"; // 組底
			System.out.println(" ----- 組底 - 制程账号 : " + zcname);
		} else if (user == "XEID10" || "XEID10".equals(user)) {
			zcname = "DELIBER_DATE";
		}

		// 如为组底账户, 显示日期选择控件
		if (user == "XEID9" || "XEID9".equals(user)) {
			BarcodeDate.setVisible(true);
		} else {
			BarcodeDate.setVisible(false);
		}
		// System.out.println(" ----- User id : " + zcname);
		return zcname;
	}

	// 获取订单日期
	public String[] getOdnoDate(String Bc, String BcDate) throws SQLException {
		// System.out.println(" ----- Bc 測試1 : " + Bc);
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection Conn = Common.getDB01Conn();

		Date date = new Date();
		String Strdate = format.format(date);
		String sql = "";
		String OdnoDate = "", Zbcdate = "", Work = "";
		String[] Oredr = new String[2];

		// 日期格式处理
		String[] Bdate = BcDate.split("-");
		for (String str : Bdate) {
			Zbcdate += str;
		}
		// String Bcs = Barcode.getValue();

		// if(_userInfo.getAccount() == "XEID9" ||
		// "XEID9".equals(_userInfo.getAccount())){
		// sql = "select work_order_id, order_date from dsid30
		// to_char(order_date,'YYYY/MM/DD') = '"+BcDate+"' and url1 =
		// '"+Bcs+"'";
		// }else{
		sql = "select work_order_id, order_date from dsid30 where work_order_id like '%" + Bc + "'";
		// }

		// System.out.println(" ----- Select Order_date : " + sql);

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				String[] str = rs.getString("order_date").substring(0, 10).split("-");
				for (String stry : str) {
					OdnoDate = OdnoDate + stry;
				}
				Work = rs.getString("work_order_id");
			}
			Oredr[0] = OdnoDate;
			Oredr[1] = Work;
			// System.out.println(" ----- Date : " +
			// rs.getString("order_date").substring(0,10).split("-"));
			// System.out.println(" ----- OdnoDate : " + OdnoDate);

			rs.close();
			ps.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}
			Common.closeConnection(Conn);
		}
		return Oredr;
	}

	// 獲取組底製程Work_order_id
	private String SetBarDate(String Bc, String BcDemo, String BcDate) throws SQLException {
		// TODO Auto-generated method stub
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection Conn = Common.getDB01Conn();
		String ZDbc = "";

		String sql = "select work_order_id from dsid30 where url1 = '" + BcDemo
				+ "' and to_char(order_date,'YYYY/MM/DD') = '" + BcDate.replace("-", "/") + "'";
		// System.out.println(" ----- 組底Work id : " + sql);

		try {
			ps = Conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				String[] s = rs.getString("work_order_id").split("-");
				ZDbc = s[1];
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}
			Common.closeConnection(Conn);
		}

		return ZDbc;
	}

	@SuppressWarnings("unchecked")
	@Listen("onQueryWindowSend = #windowMaster")
	public void onQueryWindowSend(Event event) {
		Map<String, Object> map = (Map<String, Object>) event.getData();
		ReadIDPic03Dao e = (ReadIDPic03Dao) map.get("selectedRecord");
	}

	// 組底針車是否配套
	@Listen("onClick =#btnPt")
	public void onClickbtnExport(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("ReadIDPicProgram", this);
		Executions.createComponents("/ds/dsid/ReadIDPic02.zul", null, map);
	}

	// 返修登記
	@Listen("onClick =#btnFx")
	public void onClickbtnFx(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("ReadIDPicProgram", this);
		Executions.createComponents("/ds/dsid/ReadIDPic03.zul", null, map);
	}

	// 今日掃描詳單
	@Listen("onClick = #Dayscanning")
	public void onClickbtnArrange(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("returnMethod", null);
		map.put("multiple", true);
		Executions.createComponents("/ds/dsid/ReadIDPic04.zul", null, map);
	}

	// 未完成詳單
	@Listen("onClick = #Anumberof")
	public void onClickbtnAnumberof(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("returnMethod", null);
		map.put("multiple", true);
		Executions.createComponents("/ds/dsid/ReadIDPic05.zul", null, map);
	}

	// 總欠數
	@Listen("onClick = #Sumowe")
	public void onClickbtnSumowe(Event event) throws Exception {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentWindow", windowMaster);
		map.put("returnMethod", null);
		map.put("multiple", true);
		Executions.createComponents("/ds/dsid/ReadIDPic06.zul", null, map);
	}

	// ---------- ---------- 圖片比例放大規則 ---------- ----------
	@Listen("onClick = #image0")
	public void noClickimage0() throws IOException {

		if (Img_SN) {
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(Durl0);
			} else {
				image0.setContent(Durl0);
				image1.setContent(Durl1);
				image2.setContent(Durl2);
				image3.setContent(Durl3);
				imageMax.setSrc("");
			}
			JsMax++;
		} else {
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(ServerImg[0]);
			} else {
				image0.setContent(ServerImg[0]);
				image1.setContent(ServerImg[1]);
				image2.setContent(ServerImg[2]);
				image3.setContent(ServerImg[3]);
				imageMax.setSrc("");
			}
			JsMax++;
			// System.out.println(" ----- 服務器放大 0 ----- ");
		}
	}

	@Listen("onClick = #image1")
	public void noClickimage1() throws IOException {

		if (Img_SN) {
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(Durl1);
			} else {
				image0.setContent(Durl0);
				image1.setContent(Durl1);
				image2.setContent(Durl2);
				image3.setContent(Durl3);
				imageMax.setSrc("");
			}
			JsMax++;
		} else {
			// AImage[] Serverimg = onClickServerimg(); // 服務器 本地 圖片
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(ServerImg[1]);
			} else {
				image0.setContent(ServerImg[0]);
				image1.setContent(ServerImg[1]);
				image2.setContent(ServerImg[2]);
				image3.setContent(ServerImg[3]);
				imageMax.setSrc("");
			}
			JsMax++;
			// System.out.println(" ----- 服務器放大 1 ----- ");
		}
	}

	@Listen("onClick = #image2")
	public void noClickimage2() throws IOException {

		if (Img_SN) {
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(Durl2);
			} else {
				image0.setContent(Durl0);
				image1.setContent(Durl1);
				image2.setContent(Durl2);
				image3.setContent(Durl3);
				imageMax.setSrc("");
			}
			JsMax++;
		} else {
			// AImage[] Serverimg = onClickServerimg(); // 服務器 本地 圖片
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(ServerImg[2]);
			} else {
				image0.setContent(ServerImg[0]);
				image1.setContent(ServerImg[1]);
				image2.setContent(ServerImg[2]);
				image3.setContent(ServerImg[3]);
				imageMax.setSrc("");
			}
			JsMax++;
			// System.out.println(" ----- 服務器放大 2 ----- ");
		}
	}

	@Listen("onClick = #image3")
	public void noClickimage3() throws IOException {

		if (Img_SN) {
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(Durl3);
			} else {
				image0.setContent(Durl0);
				image1.setContent(Durl1);
				image2.setContent(Durl2);
				image3.setContent(Durl3);
				imageMax.setSrc("");
			}
			JsMax++;
		} else {
			// AImage[] Serverimg = onClickServerimg(); // 服務器 本地 圖片
			if (JsMax % 2 == 0) {
				image0.setSrc("");
				image1.setSrc("");
				image2.setSrc("");
				image3.setSrc("");
				imageMax.setSrc("");
				imageMax.setContent(ServerImg[3]);
			} else {
				image0.setContent(ServerImg[0]);
				image1.setContent(ServerImg[1]);
				image2.setContent(ServerImg[2]);
				image3.setContent(ServerImg[3]);
				imageMax.setSrc("");
			}
			JsMax++;
			// System.out.println(" ----- 服務器放大 3 ----- ");
		}
	}
	// ---------- ---------- 圖片比例放大規則 ---------- ----------

	/*
	 * public static String getBc() { return Bc; }
	 * 
	 * public static void setBc(String bc) { Bc = bc; }
	 */

	@Override
	protected Window getRootWindow() {
		// TODO Auto-generated method stub
		return windowMaster;
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
		String Where = " where work_order_id like '%" + Barcode.getValue() + "'";
		return Where;
	}

	@Override
	protected String getCustomSQL() {
		// TODO Auto-generated method stub
		// return "select url2, url3, url4, url5 from dsid30 ";
		return null;
	}

	@Override
	protected String getCustomCountSQL() {
		// TODO Auto-generated method stub
		// return "select cuont(*) from dsid30";
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
		// return "xo";
		return null;
	}

	@Override
	protected String getPagingId() {
		// TODO Auto-generated method stub
		return "pagingCourse";
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
		return 5;
	}

	@Override
	protected List getCustList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Image getImage0() {
		return image0;
	}

	public void setImage0(Image image0) {
		this.image0 = image0;
	}

	public Image getImage1() {
		return image1;
	}

	public void setImage1(Image image1) {
		this.image1 = image1;
	}

	public Image getImage2() {
		return image2;
	}

	public void setImage2(Image image2) {
		this.image2 = image2;
	}

	public Image getImage3() {
		return image3;
	}

	public void setImage3(Image image3) {
		this.image3 = image3;
	}

}
