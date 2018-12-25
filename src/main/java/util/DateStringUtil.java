package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringUtil {
	private static String formatString = "yyyyMMdd HHmmss";
	private static SimpleDateFormat defaultDateTimeFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat defaultDateTimeFormat2 = new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(formatString);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
	public static String getDateFormatString() {
		return formatString;
	}
	public static DateFormat getFormat() {
		return dateTimeFormat;
	}
	public static String format(Date date) {
		return dateTimeFormat.format(date);
	}
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}
	public static Date parse(String date) throws ParseException {
		try {
			return dateTimeFormat.parse(date);
		} catch (ParseException e) {
			try {
				return defaultDateTimeFormat1.parse(date);
			} catch (ParseException e2) {
				return defaultDateTimeFormat2.parse(date);
			}
		}
	}
	public static String toDisplayFormat(String yyyyMMdd_HHmmss) {
		try {
			return defaultDateTimeFormat1.format(dateTimeFormat.parse(yyyyMMdd_HHmmss));
		} catch (ParseException e) {}
		return yyyyMMdd_HHmmss;
	}
}
