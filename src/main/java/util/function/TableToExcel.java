package util.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TableToExcel {
//	private static final String DATE_FORMAT = "YYYY/MM/DD HH:MM:SS";
	public static boolean importFromExcel(Connection conn, String tableName, File excel) throws FileNotFoundException, IOException {
        try {
            //資料庫連線
            if (conn == null) {
            	return false;
            }
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excel));
            HSSFDataFormat dateFormat = wb.createDataFormat();
            //for each table
            for (int i = 0, n = wb.getNumberOfSheets(); i < n; i++) {
            	importFromSheet(conn, tableName, wb.getSheetAt(i), dateFormat);
            }
        } catch(Exception e) {
        	e.printStackTrace();
        	return false;
        } finally {
        }
        return true;
	}
	private static void importFromSheet(Connection conn, String tableName, HSSFSheet sheet, HSSFDataFormat dateFormat) throws SQLException {
		HSSFRow header = sheet.getRow(0);
		if (header != null) {
			String[] columnNames = new String[header.getPhysicalNumberOfCells()];
			int[] columnTypes = new int[header.getPhysicalNumberOfCells()];
			StringBuffer columns = new StringBuffer();
			StringBuffer values = new StringBuffer();
			for (int i = 0, n = header.getPhysicalNumberOfCells(); i < n; i++) {
				columnNames[i] = getCellValue(header.getCell((short)i), dateFormat) + "";
				columns.append(columnNames[i]);
				values.append('?');
				if (i < n-1) {
					columns.append(',');
					values.append(',');
				}
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select " + columns + " from " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0, n = rsmd.getColumnCount(); i < n; i++) {
				columnTypes[i] = rsmd.getColumnType(i+1);
			}
			rs.close();
			stmt.close();
			
Runtime r = Runtime.getRuntime();
			PreparedStatement pstmt = conn.prepareStatement("insert into " + tableName + " (" + columns + ") values (" + values + ")");
			for (int i = 1, n = sheet.getPhysicalNumberOfRows(); i < n; i++) {
int used = (int)(100.0*r.freeMemory()/r.totalMemory());
System.out.println(" row " + i + " " + (used + "% [" + (int)(1.0*r.freeMemory()/1024) + "/" + (int)(1.0*r.totalMemory()/1024) + "]"));
				try {
					HSSFRow row = sheet.getRow(i);
					for (int j = 0, m = header.getPhysicalNumberOfCells(); j < m; j++) {
						HSSFCell cell = row.getCell((short)j);
						Object o = getCellValue(cell, dateFormat);
						if (o == null)
							pstmt.setNull(j+1, columnTypes[j]);
						else {
							if (o instanceof Date)
								pstmt.setDate(j+1, new java.sql.Date(((Date)o).getTime()));
							else
								pstmt.setObject(j+1, o);
						}
					}
					pstmt.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (i%1000 == 0)
					System.gc();
			}
			pstmt.close();
		}
	}
	public static boolean exportToExcel(Connection conn, String tableName, File excel) {
		ResultSet rs = null;
		Statement stmt = null;
        try {
            //資料庫連線
            if (conn == null) {
            	return false;
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from " + tableName);

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFDataFormat dateFormat = wb.createDataFormat();
           	exportToSheet(rs, wb, wb.createSheet(tableName), dateFormat);

            FileOutputStream out = new FileOutputStream(excel);
            wb.write(out);
            out.close();
        } catch(Exception e) {
        	e.printStackTrace();
        	return false;
        } finally {
        	try {
        		//關閉所有資料連線相關物件
        		if (rs != null) try {rs.close();}catch(Exception e){}
        		if (stmt != null) try {stmt.close();}catch(Exception e){}
        	} catch (Exception e) {}
        }
        return true;
	}
	private static void exportToSheet(ResultSet rs, HSSFWorkbook wb, HSSFSheet sheet, HSSFDataFormat dateFormat) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		
		HSSFRow header = sheet.createRow(0);
        short[] columnWidth = new short[rsmd.getColumnCount()];
		HSSFCell cell;
		Object obj;
		for (int i = 0, n = rsmd.getColumnCount(); i < n; i++) {
			cell = header.createCell((short)i);
			obj = rsmd.getColumnName(i+1);
			cell.setCellValue((String)obj);

            //調整寬度
            if (obj == null) obj = "";
			columnWidth[i] = (short)Math.max(columnWidth[i], (256*obj.toString().length()+4));
		}

Runtime rt = Runtime.getRuntime();
		HSSFRow row;
		for (int r = 1; rs.next(); r++) {
			row = sheet.createRow(r);
int used = (int)(100.0*rt.freeMemory()/rt.totalMemory());
System.out.println(" row " + r + " " + (used + "% [" + (int)(1.0*rt.freeMemory()/1024) + "/" + (int)(1.0*rt.totalMemory()/1024) + "]"));
			for (int i = 0, n = rsmd.getColumnCount(); i < n; i++) {
				if (rsmd.getColumnType(i+1) == Types.CLOB) {
					Clob c = rs.getClob(i+1);
					obj = c.getSubString(0, (int)c.length());
				} else
					obj = rs.getObject(i+1);
				if (obj != null) {
					cell = row.createCell((short)i);
					setCellValue(wb, cell, obj, dateFormat);
					columnWidth[i] = (short)Math.max(columnWidth[i], (256*obj.toString().length()+4));
				}

                //調整寬度
//                if (obj == null) obj = "";
			}
			if (r%1000 == 0)
				System.gc();
		}
		
        for (int i = 0, n = columnWidth.length; i < n; i++) {
            sheet.setColumnWidth((short)i, columnWidth[i]);
        }
	}

	public static Object getCellValue(HSSFCell cell, HSSFDataFormat dataFormat) {
		if (cell == null)
			return null;
        switch(cell.getCellType()) {
            case HSSFCell.CELL_TYPE_BLANK:
                return null;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return new Boolean(cell.getBooleanCellValue());
            case HSSFCell.CELL_TYPE_ERROR:
            	return cell.getErrorCellValue();
            case HSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula().trim();
            case HSSFCell.CELL_TYPE_NUMERIC:
//            	if (HSSFDateUtil.isValidExcelDate(cell.getNumericCellValue()) && 
//            			(HSSFDateUtil.isCellDateFormatted(cell) || dataFormat.getFormat(cell.getCellStyle().getDataFormat()).equals(DATE_FORMAT)))
            	if (HSSFDateUtil.isValidExcelDate(cell.getNumericCellValue()) && 
            			HSSFDateUtil.isCellDateFormatted(cell) )
            		return cell.getDateCellValue();
                return new Double(cell.getNumericCellValue());
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue().trim();
        }
        return null;
    }
    public static void setCellValue(HSSFWorkbook wb, HSSFCell cell, Object value, HSSFDataFormat dataFormat) {
        cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
        if (value == null)
            return;
        if (value instanceof String) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue((String)value);
        } else if (value instanceof Number) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Number)value).doubleValue());
        } else if (value instanceof Calendar) {
            cell.setCellValue(HSSFDateUtil.getExcelDate(((Calendar)value).getTime()));
//            HSSFCellStyle style = wb.createCellStyle();
//            style.setDataFormat(dataFormat.getFormat(DATE_FORMAT));
//            cell.setCellStyle(style);
        } else if (value instanceof Date) {
            cell.setCellValue(HSSFDateUtil.getExcelDate((Date)value));
//            HSSFCellStyle style = wb.createCellStyle();
//            style.setDataFormat(dataFormat.getFormat(DATE_FORMAT));
//            cell.setCellStyle(style);
        } else if (value instanceof Boolean) {
            cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
            cell.setCellValue(((Boolean)value).booleanValue());
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellValue(value.toString());
            
        }
    }
}