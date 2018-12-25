package util.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;

public class KeyNumberGenerator {
	protected static final Logger logger = Logger.getLogger(KeyNumberGenerator.class);

	private static final String[] varName = { "$Y", "$M", "$D", "$S", "$C" };
	private static final int serialVarIndex = 3;//流水號在varName中的index

	private Connection con;
	private SerialInfo serialInfo;
	private GenerationInfo generationInfo;

	public KeyNumberGenerator() {
	}

	 /**
     * 建立並初始化一個產生器，流水號以0為起始號碼，年月日以系統日期帶入
     * @param con 資料庫連線
     * @param format 編號格式由
     * 				$Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     */
	public KeyNumberGenerator(Connection con, String format, String table, String column, Map<String, Object> keys,
			Map<String, Object> filters, boolean isCompositedColumn, boolean updateTable) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		init(con, year, month, day, format, 0, table, column, keys, filters, isCompositedColumn, updateTable);
	}

	/**
     * 建立並初始化一個產生器，年月日以系統日期帶入
     * @param con 資料庫連線
     * @param format 編號格式由
     * 				$Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param serialStart 流水號的初始值
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     */
	public KeyNumberGenerator(Connection con, String format, long serialStart, String table, String column,
			Map<String, Object> keys, Map<String, Object> filters, boolean isCompositedColumn, boolean updateTable) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		init(con, year, month, day, format, serialStart, table, column, keys, filters, isCompositedColumn, updateTable);
	}

	 /**
     * 建立並初始化一個產生器，流水號以0為起始號碼
     * @param con 資料庫連線
     * @param year 產生的年
     * @param month 產生的月
     * @param day 產生的日
     * @param format 編號格式由
     *              $Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     */
	public KeyNumberGenerator(Connection con, int year, int month, int day, String format, String table, String column,
			Map<String, Object> keys, Map<String, Object> filters, boolean isCompositedColumn, boolean updateTable) {
		init(con, year, month, day, format, 0, table, column, keys, filters, isCompositedColumn, updateTable);
	}

	 /**
     * 建立並初始化一個產生器，流水號以0為起始號碼
     * @param con 資料庫連線
     * @param year 產生的年
     * @param month 產生的月
     * @param day 產生的日
     * @param format 編號格式由
     *              $Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param serialStart 流水號的初始值
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     */
	public KeyNumberGenerator(Connection con, int year, int month, int day, String format, long serialStart,
			String table, String column, Map<String, Object> keys, Map<String, Object> filters,
			boolean isCompositedColumn, boolean updateTable) {
		init(con, year, month, day, format, serialStart, table, column, keys, filters, isCompositedColumn, updateTable);
	}

	/**
     * 初始化產生器
     * @param con 資料庫連線
     * @param year 產生的年
     * @param month 產生的月
     * @param day 產生的日
     * @param format 編號格式由
     *              $Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param serialStart 流水號的初始值
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     */
	protected void init(Connection con, int year, int month, int day, String format, long serialStart, String table,
			String column, Map<String, Object> keys, Map<String, Object> filters, boolean isCompositedColumn,
			boolean updateTable) {
		this.con = con;
		serialInfo = new SerialInfo();
		generationInfo = new GenerationInfo();
		prepare(generationInfo, serialInfo, con, year, month, day, format, serialStart, table, column, keys, filters,
				isCompositedColumn, updateTable);
	}


/**
 * 產生編號
 * @return
 */
	public String generate() {
		return generate(generationInfo, serialInfo, con);
	}

	/**
     * 取得最新編號，流水號以0為起始號碼，年月日以系統日期帶入
     * @param con 資料庫連線
     * @param format 編號格式由
     * 				$Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     * @return 取得的最新編號
     */
	public static String generateNumber(Connection con, String format, String table, String column,
			Map<String, Object> keys, Map<String, Object> filters, boolean isCompositedColumn, boolean updateTable) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return generateNumber(con, year, month, day, format, 0, table, column, keys, filters, isCompositedColumn,
				updateTable);
	}

	/**
     * 取得最新編號，流水號以0為起始號碼
     * @param con 資料庫連線
     * @param year 產生的年
     * @param month 產生的月
     * @param day 產生的日
     * @param format 編號格式由
     * 				$Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     * @return 取得的最新編號
     */
	public static String generateNumber(Connection con, int year, int month, int day, String format, String table,
			String column, Map<String, Object> keys, Map<String, Object> filters, boolean isCompositedColumn,
			boolean updateTable) {
		return generateNumber(con, year, month, day, format, 0, table, column, keys, filters, isCompositedColumn,
				updateTable);
	}

	/**
     * 取得最新編號，年月日以系統日期帶入
     * @param con 資料庫連線
     * @param format 編號格式由
     *              $Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param serialStart 流水號的初始值
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     * @return 取得的最新編號
     */
	public static String generateNumber(Connection con, String format, long serialStart, String table, String column,
			Map<String, Object> keys, Map<String, Object> filters, boolean isCompositedColumn, boolean updateTable) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return generateNumber(con, year, month, day, format, serialStart, table, column, keys, filters,
				isCompositedColumn, updateTable);
	}

	/**
     * 取得最新編號
     * @param con 資料庫連線
     * @param year 產生的年
     * @param month 產生的月
     * @param day 產生的日
     * @param format 編號格式由
     *              $Y{length,[radix]}：年
     *              $M{length,[radix]}：月
     *              $D{length,[radix]}：日
     *              $S{length,[radix]}：流水號
     *              $C{name}：資料表的欄位，通常要檢查的資料表欄位僅存流水號時才能使用
     *              五種變數及靜態文字構成，例：
     *              "HZA$Y{2}$M{2}$D{2}$S{4}" -> "HZA0603300010"
     *              "HZA$Y{4}$M{2}$D{2}$S{4}" -> "HZA200603300010"
     *              "HZA$Y{2}$M{2}$D{2}$S{4,16}" -> "HZA060330000A"
     * @param serialStart 流水號的初始值
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @param updateTable 是否將取得的編號儲存入該資料表，適用於資料表為序號檔的狀況
     * @return 取得的最新編號
     */
	public static String generateNumber(Connection con, int year, int month, int day, String format, long serialStart,
			String table, String column, Map<String, Object> keys, Map<String, Object> filters,
			boolean isCompositedColumn, boolean updateTable) {
		//流水號資訊
		SerialInfo serialInfo = new SerialInfo();
		//產生器資訊
		GenerationInfo generationInfo = new GenerationInfo();
		//準備流水號資訊及產生器資訊
		prepare(generationInfo, serialInfo, con, year, month, day, format, serialStart, table, column, keys, filters,
				isCompositedColumn, updateTable);
		//產生
		return generate(generationInfo, serialInfo, con);
	}

	private static String generate(GenerationInfo generationInfo, SerialInfo serialInfo, Connection con) {
		String serialWildcard = serialInfo.getWildcard();
		int serialRadix = serialInfo.getRadix();
		int serialLength = serialInfo.getLength();
		long serialStart = serialInfo.getStart();

		String table = generationInfo.getTable();
		String column = generationInfo.getColumn();
		Map<String, Object> keys = generationInfo.getKeys();
		Map<String, Object> filters = generationInfo.getFilters();
		String columnLike = generationInfo.getColumnLike();
		boolean isCompositedColumn = generationInfo.isCompositedColumn();
		boolean updateTable = generationInfo.isUpdateTable();

		String result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String maxColumn = findWithKeysAndFilters(table, column, con, keys, filters, columnLike,
					isCompositedColumn);
			boolean foundByKeys = findWithKeys(table, column, con, keys, columnLike, isCompositedColumn) != null;
			logger.debug(Labels.getLabel("COMM.QRY") + Labels.getLabel("FLOW.flowWords24") + maxColumn);

			String maxSerial;
			if (maxColumn != null) { // 有最大值
				int x = columnLike.indexOf(serialWildcard);
				String s1 = columnLike.substring(0, x);
				String s2 = columnLike.substring(x + serialWildcard.length());
				if (isCompositedColumn) { //column除了流水號之外還有別的字串所組合而成
					maxSerial = maxColumn.substring(s1.length(), maxColumn.length() - s2.length());
				} else {
					maxSerial = maxColumn;
				}
				String newMaxSerial = formatNumber(parse(maxSerial, serialRadix) + 1, serialLength, serialRadix);
				String newMaxColumn = newMaxSerial;
				if (updateTable) { //更新資料至此table，通常使用對象為table是序號檔時
					if (isCompositedColumn) {
						newMaxColumn = s1 + newMaxColumn + s2; //儲存組合後的欄位
						if (updateWithKeysAndFilters(table, column, con, keys, filters, maxColumn, newMaxColumn))
							result = newMaxColumn;
					} else {
						if (updateWithKeysAndFilters(table, column, con, keys, filters, maxColumn, newMaxColumn))
							result = s1 + newMaxColumn + s2;
					}
				} else
					result = s1 + newMaxColumn + s2;
			} else {
				int x = columnLike.indexOf(serialWildcard);
				String s1 = columnLike.substring(0, x);
				String s2 = columnLike.substring(x + serialWildcard.length());
				String newMaxSerial = formatNumber(serialStart, serialLength, serialRadix);
				String newMaxColumn = newMaxSerial;
				if (updateTable) { //更新資料至此table，通常使用對象為table是序號檔時
					if (isCompositedColumn) {
						newMaxColumn = s1 + newMaxColumn + s2; //儲存組合後的欄位
						if (insertOrUpdate(table, column, con, keys, filters, newMaxColumn, foundByKeys))
							result = newMaxColumn;
					} else {
						if (insertOrUpdate(table, column, con, keys, filters, newMaxColumn, foundByKeys))
							result = s1 + newMaxColumn + s2;
					}
				} else
					result = s1 + newMaxColumn + s2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		logger.debug(Labels.getLabel("SYS_TASK_SCHEDULE.TASK_RESULT") + result);
		return result;
	}

	private static void prepare(GenerationInfo generationInfo, SerialInfo serialInfo, Connection con, int year,
			int month, int day, String format, long serialStart, String table, String column, Map<String, Object> keys,
			Map<String, Object> filters, boolean isCompositedColumn, boolean updateTable) {
		//將format字串換成MessageFormat的pattern以及該pattern變數的值的描述
		parseFormat(format, generationInfo);

		//設定流水號資訊：起始流水號
		serialInfo.setStart(serialStart);

		//設定產生器資訊
		generationInfo.setYear(year);
		generationInfo.setMonth(year);
		generationInfo.setDay(year);
		generationInfo.setTable(table);
		generationInfo.setColumn(column);
		generationInfo.setKeys(keys);
		generationInfo.setFilters(filters);
		generationInfo.setCompositedColumn(isCompositedColumn);
		generationInfo.setUpdateTable(updateTable);

		String pattern = generationInfo.getPattern();
		List metadata = generationInfo.getMetadata();
		try {
			// --------------------------------------準備替換的變數值---------------------------
			Map columnValues = findColumnValues(table, con, keys, filters, isCompositedColumn);
			long[] varValue = { year, month, day, 0 };
			int serialIndexInMetadata = -1;//第幾個metadata指的是serial
			String serialWildcard = "";

			List<String> formattedValues = new ArrayList<String>();
			String tmp;
			int[] itmp;
			StringBuffer stmp;
			for (int i = 0, n = metadata.size(); i < n; i++) {
				tmp = (String) metadata.get(i);
				if (tmp.indexOf(",") > -1) {   //$Y, $M, $D, $S變數
					itmp = parseNumberMetadata(tmp);
					formattedValues.add(formatNumber(varValue[itmp[0]], itmp[1], itmp[2]));
					if (serialVarIndex == itmp[0]) {
						stmp = new StringBuffer();
						for (int a = 0; a < itmp[1]; a++)
							stmp.append('_');
						serialIndexInMetadata = i;
						serialWildcard = stmp.toString();
						//設定流水號資訊：流水號長度
						serialInfo.setLength(itmp[1]);
						//設定流水號資訊：流水號進制
						serialInfo.setRadix(itmp[2]);
						//設定流水號資訊：查詢用的流水號wildcard
						serialInfo.setWildcard(serialWildcard);
					}
				} else { //$C變數
					if (isCompositedColumn) {
						 //檢查的欄位不止有流水號時，會使用like條件取得column
                        //因無法以$C組成like的檢查字串，所以過濾掉$C變數
						formattedValues.add("");
					} else {
						if (columnValues.containsKey(tmp))
							formattedValues.add(columnValues.get(tmp).toString());
					}
				}
			}
			// --------------------------------------準備替換的變數值----------------------------
			// logger.debug("pattern: " + pattern);
			// logger.debug("formattedValues: " +
			// Arrays.toString(formattedValues.toArray()));
			//把serial的格式化值換成SQL的wildcard，以供like condition用
			formattedValues.set(serialIndexInMetadata, serialWildcard);
			String columnLike = MessageFormat.format(pattern, formattedValues.toArray());
			// logger.debug("columnLike: " + columnLike);
			//設定產生器資訊：查詢用的like值
			generationInfo.setColumnLike(columnLike);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void parseFormat(String format, GenerationInfo generationInfo) {
		String pattern = format;
		Matcher m;
		String g;
		int x;
		//於format中的變數
        List<String> vars = new ArrayList<String>();
        //於format中的變數metadata
        List<String> metadata = new ArrayList<String>();
        //預設的數字變數：年、月、日、流水號
		for (int i = 0, n = varName.length; i < n; i++) {
			m = Pattern.compile("\\" + varName[i] + "\\{[0-9]+(,[0-9]+)?\\}").matcher(pattern);
			while (m.find()) {
				g = m.group();
				x = vars.indexOf(g);
				if (x < 0) {
					vars.add(g);
					x = vars.size() - 1;
					int x1 = g.indexOf("{");
					int x2 = g.indexOf("}", x1);
					if (x1 > -1 && x2 > -1) {
						metadata.add(i + "," + g.substring(x1 + 1, x2)); //儲存欄位名稱當做資料表欄位變數的metadata
					}
				}
				pattern = pattern.replaceFirst(Pattern.quote(g), "{" + x + "}"); //把變數換成MessageFormat的變數格式
			}
		}
		  //資料表欄位的變數
		m = Pattern.compile("\\$C\\{[^\\}]+\\}").matcher(pattern);
		while (m.find()) {
			g = m.group();
			x = vars.indexOf(g);
			int x1 = g.indexOf("{");
			int x2 = g.indexOf("}", x1);
			if (x < 0) {
				vars.add(g);
				metadata.add(g.substring(x1 + 1, x2).toUpperCase()); //儲存欄位名稱當做資料表欄位變數的metadata
				x = vars.size() - 1;
			}
			pattern = pattern.replaceFirst(Pattern.quote(g), "{" + x + "}"); //把變數換成MessageFormat的變數格式
		}
		generationInfo.setPattern(pattern);
		generationInfo.setMetadata(metadata);
	}

	protected static String formatNumber(long value, int length, int radix) {
		String s = Long.toString(value, radix).toUpperCase();
		int slen = s.length();
		if (slen < length) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0, n = length - slen; i < n; i++)
				sb.append('0');
			sb.append(s);
			return sb.toString();
		} else if (slen > length)
			return s.substring(slen - length, slen);
		return s;
	}

	private static int[] parseNumberMetadata(String s) {
		String[] ss = s.split(",");
		int[] is = { 0, 1, 10 };
		if (ss.length > 0)
			is[0] = Integer.parseInt(ss[0]);
		if (ss.length > 1)
			is[1] = Integer.parseInt(ss[1]);
		if (ss.length > 2)
			is[2] = Integer.parseInt(ss[2]);
		return is;
	}

	protected static long parse(String s, int radix) {
		s = s.replaceAll("$0*", "");
		if (s.length() == 0)
			s = "0";
		long value = Long.parseLong(s, radix);
		return value;
	}

	private static String findWithKeys(String table, String column, Connection con, Map<String, Object> keys,
			String columnLike, boolean isCompositedColumn) throws Exception {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.putAll(keys);
		return find(table, column, con, conditions, columnLike, isCompositedColumn);
	}

	private static String findWithKeysAndFilters(String table, String column, Connection con, Map<String, Object> keys,
			Map<String, Object> filters, String columnLike, boolean isCompositedColumn) throws Exception {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.putAll(keys);
		conditions.putAll(filters);
		return find(table, column, con, conditions, columnLike, isCompositedColumn);
	}
    /**
     * 從資料表中Select出欲檢查的資料
     * @param table 檢查的資料表
     * @param column 該資料表的編號欄位鍵值
     * @param con 資料庫連線
     * @param conditions 查詢的條件值
     * @param columnLike 若有需要，條件要加上column LIKE ?
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @return 找到的最大column值
     * @throws Exception
     */
	private static String find(String table, String column, Connection con, Map conditions, String columnLike,
			boolean isCompositedColumn) throws Exception {
		String result = null;

		StringBuffer select = new StringBuffer("SELECT");
		select.append(" MAX(").append(column).append(")");
		select.append(" FROM ").append(table);

		StringBuffer condition = new StringBuffer(" WHERE ");
		String name;
		int j = 0;
		Object[] values = null;

		if (isCompositedColumn) { //若要檢查的column是組合出來的單號時，則條件要加上column LIKE ?，才能取得最大流水號
			condition.append(column).append(" LIKE ?");
			if (conditions.size() > 0)
				condition.append(" AND ");
			values = new Object[conditions.size() + 1];
			values[j++] = columnLike;
		} else { //若要檢查的column非組合出來的單號時，表示此column僅有流水號，故不加上LIKE便可取得最大流水號
			values = new Object[conditions.size()];
		}

		for (Iterator i = conditions.keySet().iterator(); i.hasNext();) {
			name = (String) i.next();
			condition.append(name).append(" = ?");
			if (i.hasNext())
				condition.append(" AND ");
			values[j++] = conditions.get(name);
		}

		String sql = (isCompositedColumn || conditions.size() > 0) ? select.append(condition).toString()
				: select.toString();

		logger.debug(Labels.getLabel("COMM.QRY") + sql);

		Exception ex = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			for (int i = 0, n = values.length; i < n; i++) {
				stmt.setObject(i + 1, values[i]);
				logger.debug(Labels.getLabel("DSTR11PRT.MESSAGE01") + (i + 1) + "->" + values[i]);
			}

			rs = stmt.executeQuery();
			if (rs.next()) { //已有單號產生
				result = rs.getString(1);
			}
		} catch (Exception e) {
			ex = e;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		if (ex != null)
			throw ex;
		return result;
	}

	/**
     * @param table 檢查的資料表
     * @param con 資料庫連線
     * @param keys 該資料表除編號欄位外其他key欄位
     * @param filters 該資料表除編號欄位外其他過濾的條件
     * @param isCompositedColumn 資料表編號欄位是否為組合值或僅存流水號
     * @return 單筆資料的各個column名稱與值
     * @throws Exception
     */
	protected static Map<String, Object> findColumnValues(String table, Connection con, Map<String, Object> keys,
			Map<String, Object> filters, boolean isCompositedColumn) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer select = new StringBuffer("SELECT * FROM ").append(table);

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.putAll(keys);
		conditions.putAll(filters);
		StringBuffer condition = new StringBuffer();
		Object[] values = new Object[conditions.size()];
		if (conditions.size() > 0) {
			condition.append(" WHERE ");
			String name;
			int j = 0;
			for (Iterator i = conditions.keySet().iterator(); i.hasNext();) {
				name = (String) i.next();
				condition.append(name).append(" = ?");
				if (i.hasNext())
					condition.append(" AND ");
				values[j++] = conditions.get(name);
			}
		}

		String sql = select.append(condition).toString();
		// logger.debug("find columns SQL: " + sql);

		Exception ex = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			for (int i = 0, n = values.length; i < n; i++) {
				if (values[i] instanceof Date)
					stmt.setDate(i + 1, new java.sql.Date(((Date) values[i]).getTime()));
				else
					stmt.setObject(i + 1, values[i]);
				// logger.debug("\t" + (i+1) + "->" + values[i]);
			}

			rs = stmt.executeQuery();
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1, n = rsmd.getColumnCount(); i <= n; i++)
					result.put(rsmd.getColumnName(i).toUpperCase(), rs.getObject(i));
			}
		} catch (Exception e) {
			ex = e;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		if (ex != null)
			throw ex;
		return result;
	}

	private static boolean insertOrUpdate(String table, String column, Connection con, Map<String, Object> keys,
			Map<String, Object> filters, String maxColumn, boolean foundByKeys) throws Exception {
		boolean result = false;

		boolean executeInsert = true;
		if (foundByKeys) {//以pk找得到資料，所以執行update
			executeInsert = false;
		}

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.putAll(keys);
		conditions.putAll(filters);
		Exception ex = null;
		if (executeInsert) {
			StringBuffer names = new StringBuffer();
			StringBuffer values = new StringBuffer();
			String name;
			Object[] o = new Object[conditions.size() + 1];
			int j = 0;
			names.append(column);
			values.append('?');
			o[j++] = maxColumn;
			for (Iterator i = conditions.keySet().iterator(); i.hasNext();) {
				name = (String) i.next();
				names.append(',').append(name);
				values.append(',').append('?');
				o[j++] = conditions.get(name);
			}

			StringBuffer insert = new StringBuffer("INSERT INTO ").append(table);
			insert.append('(').append(names).append(") VALUES (").append(values).append(')');

			String sql = insert.toString();
			// logger.debug("insert SQL: " + sql);

			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = con.prepareStatement(sql);
				for (int i = 0, n = o.length; i < n; i++) {
					if (o[i] instanceof Date)
						stmt.setDate(i + 1, new java.sql.Date(((Date) o[i]).getTime()));
					else
						stmt.setObject(i + 1, o[i]);
					// logger.debug("\t" + (i+1) + "->" + o[i]);
				}

				result = stmt.executeUpdate() != 0;
			} catch (Exception e) {
				ex = e;
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
				try {
					if (stmt != null)
						stmt.close();
				} catch (Exception e) {
				}
			}
		} else {
			// UPDATE table SET column=?,column=? WHERE pk1=? AND pk2
			StringBuffer updateColumns = new StringBuffer(column).append("=?");
			StringBuffer condition = new StringBuffer();
			String name;
			Object[] o = new Object[1 + filters.size() + keys.size()];
			int j = 0;
			o[j++] = maxColumn;
			for (Iterator i = filters.keySet().iterator(); i.hasNext();) {
				name = (String) i.next();
				updateColumns.append(",").append(name).append("=?");
				o[j++] = filters.get(name);
			}
			for (Iterator i = keys.keySet().iterator(); i.hasNext();) {
				name = (String) i.next();
				if (condition.length() > 0)
					condition.append(" AND ");
				condition.append(name).append("=?");
				o[j++] = keys.get(name);
			}

			StringBuffer update = new StringBuffer("UPDATE ").append(table);
			update.append(" SET ").append(updateColumns).append(" WHERE ").append(condition);

			String sql = update.toString();
			// logger.debug("update SQL: " + sql);

			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = con.prepareStatement(sql);
				for (int i = 0, n = o.length; i < n; i++) {
					if (o[i] instanceof Date)
						stmt.setDate(i + 1, new java.sql.Date(((Date) o[i]).getTime()));
					else
						stmt.setObject(i + 1, o[i]);
					// logger.debug("\t" + (i+1) + "->" + o[i]);
				}

				result = stmt.executeUpdate() != 0;
			} catch (Exception e) {
				ex = e;
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
				try {
					if (stmt != null)
						stmt.close();
				} catch (Exception e) {
				}
			}
		}
		if (ex != null)
			throw ex;
		return result;
	}

	private static boolean updateWithKeysAndFilters(String table, String column, Connection con,
			Map<String, Object> keys, Map<String, Object> filters, String maxColumn, String newMaxColumn)
					throws Exception {
		boolean result = false;

		StringBuffer update = new StringBuffer("UPDATE ").append(table);
		update.append(" SET ").append(column).append(" = ?");

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.putAll(keys);
		conditions.putAll(filters);
		StringBuffer condition = new StringBuffer(" WHERE ").append(column).append(" = ?");
		String name;
		int j = 0;
		Object[] values = new Object[conditions.size() + 2];
		values[j++] = newMaxColumn;
		values[j++] = maxColumn;
		for (Iterator i = conditions.keySet().iterator(); i.hasNext();) {
			name = (String) i.next();
			condition.append(" AND ").append(name).append(" = ?");
			values[j++] = conditions.get(name);
		}

		String sql = update.append(condition).toString();
		// logger.debug("update SQL: " + sql);

		Exception ex = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			for (int i = 0, n = values.length; i < n; i++) {
				if (values[i] instanceof Date)
					stmt.setDate(i + 1, new java.sql.Date(((Date) values[i]).getTime()));
				else
					stmt.setObject(i + 1, values[i]);
				// logger.debug("\t" + (i+1) + "->" + values[i]);
			}

			result = stmt.executeUpdate() != 0;
		} catch (Exception e) {
			ex = e;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		if (ex != null)
			throw ex;
		return result;
	}
}

class GenerationInfo {
	private int year;
	private int month;
	private int day;
	private String table;
	private String column;
	private Map<String, Object> keys;
	private Map<String, Object> filters;
	private String pattern;
	private List<String> metadata;
	private String columnLike;
	private boolean compositedColumn;
	private boolean updateTable;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getColumnLike() {
		return columnLike;
	}

	public void setColumnLike(String columnLike) {
		this.columnLike = columnLike;
	}

	public boolean isCompositedColumn() {
		return compositedColumn;
	}

	public void setCompositedColumn(boolean compositedColumn) {
		this.compositedColumn = compositedColumn;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public List<String> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<String> metadata) {
		this.metadata = metadata;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public boolean isUpdateTable() {
		return updateTable;
	}

	public void setUpdateTable(boolean updateTable) {
		this.updateTable = updateTable;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public Map<String, Object> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, Object> keys) {
		this.keys = keys;
	}

}

class SerialInfo {
	private long start = 0;
	private int radix = 10;
	private int length = 1;
	private String wildcard = "";

	public SerialInfo() {
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getRadix() {
		return radix;
	}

	public void setRadix(int radix) {
		this.radix = radix;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public String getWildcard() {
		return wildcard;
	}

	public void setWildcard(String wildcard) {
		this.wildcard = wildcard;
	}
}
