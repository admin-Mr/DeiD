package ds.dsid.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import ds.common.services.CRUDService;

public class DSIDN10M_ExcelUtil {
	private static CRUDService CRUDService;

	// 使用poi匯出excel
	public static void ExcelExport(String sSql, String sFileName, String sTitle, String[] arrHead) {
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		try {
			Workbook wb = new HSSFWorkbook();
			// 檔名
			// FileOutputStream stream = new FileOutputStream(sFileName);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// 標題，設定格式:粗體置中 字體14
			HSSFCellStyle styleTitle = (HSSFCellStyle) wb.createCellStyle();
			Font fontTitle = wb.createFont();
			fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontTitle.setFontHeightInPoints((short) 14); // 字體
			styleTitle.setFont(fontTitle);
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleTitle);

			// 標題，設定格式:粗體置中 字體12
			HSSFCellStyle styleHead = (HSSFCellStyle) wb.createCellStyle();
			Font fontHead = wb.createFont();
			fontHead.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontHead.setFontHeightInPoints((short) 12); // 字體
			styleHead.setFont(fontHead);
			styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleHead); // 設定Border及自動換行

			// 內容格式-文字
			HSSFCellStyle styleString = (HSSFCellStyle) wb.createCellStyle();
			styleString.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleString.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleString);

			// 內容格式-日期
			HSSFCellStyle styleDate = (HSSFCellStyle) wb.createCellStyle();
			HSSFDataFormat dateFormat = (HSSFDataFormat) wb.createDataFormat();
			styleDate.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleDate.setDataFormat(dateFormat.getFormat("YYYY/MM/DD"));
			setStandStyle(styleDate);

			// 內容格式-數字
			HSSFCellStyle styleNumber = (HSSFCellStyle) wb.createCellStyle();
			styleNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleNumber);

			// 內容格式-數字-float
			HSSFCellStyle styleFloat = (HSSFCellStyle) wb.createCellStyle();
			styleFloat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFloat.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			setStandStyle(styleFloat);

			Query qry = CRUDService.createSQL(sSql);

			// 設定 Sheet名稱
			Sheet sh = wb.createSheet(sTitle);

			int iRow = 0;
			Row rowHead = sh.createRow(iRow);

			// 報表名稱
			Cell celltitle;
			for (int i = 0; i < arrHead.length; i++) {
				celltitle = rowHead.createCell(i);
				celltitle.setCellStyle(styleTitle);
			}
			celltitle = rowHead.createCell(0);
			celltitle.setCellValue(sTitle);
			celltitle.setCellStyle(styleTitle);

			// 合併儲存格
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, arrHead.length - 1));
			// 設定欄高
			rowHead.setHeightInPoints((short) 40);

			iRow++;
			rowHead = sh.createRow(iRow);

			// 標題
			for (int i = 0; i < arrHead.length; i++) {
				Cell cell = rowHead.createCell(i);
				cell.setCellStyle(styleHead);
				cell.setCellValue(arrHead[i]);

				int iWidth = 20;

				sh.setColumnWidth(i, (short) 256 * iWidth);
				// sh.autoSizeColumn(i);
			}

			iRow++;
			Row rowCell = sh.createRow(iRow);
			int k = 0;

			// 內容
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				rowCell = sh.createRow(iRow + k);
				Object[] obj = (Object[]) i.next();
				for (int j = 0; j < arrHead.length; j++) {
					Cell cell0 = rowCell.createCell(j);
					if (obj[j] instanceof String) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue((String) obj[j]);
					} else if (obj[j] instanceof BigDecimal) {
						cell0.setCellStyle(styleNumber);
						cell0.setCellValue(((BigDecimal) obj[j]).doubleValue());
					} else if (obj[j] instanceof Date) {
						cell0.setCellStyle(styleDate);
						cell0.setCellValue((Date) obj[j]);
					} else if (obj[j] == null) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue("");
					}
				}
				k++;
			}

			wb.write(stream);

			byte[] content = stream.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			// File file = new File(sFileName);
			// Filedownload.save(file, "application/file");
			Filedownload.save(is, "application/file", "abcdef");
			is.close();
			stream.flush();
			stream.close();
		} catch (IOException e) {
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "File Not Found", Messagebox.OK, Messagebox.ERROR);
		}

	}

	// 使用poi匯出excel
	public static void ExcelExporta(String sSql, String sFileName, String sTitle, String[] arrHead) {
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		try {
			Workbook wb = new HSSFWorkbook();
			// 檔名
			// FileOutputStream stream = new FileOutputStream(sFileName);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// 標題，設定格式:粗體置中 字體14
			HSSFCellStyle styleTitle = (HSSFCellStyle) wb.createCellStyle();
			Font fontTitle = wb.createFont();
			fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontTitle.setFontHeightInPoints((short) 14); // 字體
			styleTitle.setFont(fontTitle);
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleTitle);

			// 標題，設定格式:粗體置中 字體12
			HSSFCellStyle styleHead = (HSSFCellStyle) wb.createCellStyle();
			Font fontHead = wb.createFont();
			fontHead.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontHead.setFontHeightInPoints((short) 12); // 字體
			styleHead.setFont(fontHead);
			styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleHead); // 設定Border及自動換行

			// 內容格式-文字
			HSSFCellStyle styleString = (HSSFCellStyle) wb.createCellStyle();
			styleString.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleString.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleString);

			// 內容格式-日期
			HSSFCellStyle styleDate = (HSSFCellStyle) wb.createCellStyle();
			HSSFDataFormat dateFormat = (HSSFDataFormat) wb.createDataFormat();
			styleDate.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleDate.setDataFormat(dateFormat.getFormat("YYYY/MM/DD"));
			setStandStyle(styleDate);

			// 內容格式-數字
			HSSFCellStyle styleNumber = (HSSFCellStyle) wb.createCellStyle();
			styleNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleNumber);

			// 內容格式-數字-float
			HSSFCellStyle styleFloat = (HSSFCellStyle) wb.createCellStyle();
			styleFloat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFloat.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			setStandStyle(styleFloat);

			Query qry = CRUDService.createSQL(sSql);
			// 設定 Sheet名稱
			Sheet sh = wb.createSheet(sTitle);

			int iRow = 0;
			Row rowHead = sh.createRow(iRow);

			// 報表名稱
			Cell celltitle;
			for (int i = 0; i < arrHead.length; i++) {
				celltitle = rowHead.createCell(i);
				celltitle.setCellStyle(styleTitle);
			}
			celltitle = rowHead.createCell(0);
			celltitle.setCellValue(sTitle);
			celltitle.setCellStyle(styleTitle);

			// 合併儲存格
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, arrHead.length - 1));
			// 設定欄高
			rowHead.setHeightInPoints((short) 40);

			iRow++;
			rowHead = sh.createRow(iRow);

			// 標題
			for (int i = 0; i < arrHead.length; i++) {
				Cell cell = rowHead.createCell(i);
				cell.setCellStyle(styleHead);
				cell.setCellValue(arrHead[i]);

				int iWidth = 20;

				sh.setColumnWidth(i, (short) 256 * iWidth);
				// sh.autoSizeColumn(i);
			}

			iRow++;
			Row rowCell = sh.createRow(iRow);
			int k = 0;
			// // 內容
			// for (int i =0;i<30;i++) {
			// rowCell = sh.createRow(iRow + k);
			// String obj = i+"---";
			// for (int j = 0; j < arrHead.length; j++) {
			// Cell cell0 = rowCell.createCell(j);
			// cell0.setCellValue(obj +j);
			// }
			// k++;
			// }

			// 內容
			for (Iterator i = qry.getResultList().iterator(); i.hasNext();) {
				rowCell = sh.createRow(iRow + k);
				Object[] obj = (Object[]) i.next();
				for (int j = 0; j < arrHead.length; j++) {
					Cell cell0 = rowCell.createCell(j);
					if (obj[j] instanceof String) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue((String) obj[j]);
					} else if (obj[j] instanceof BigDecimal) {
						cell0.setCellStyle(styleNumber);
						cell0.setCellValue(((BigDecimal) obj[j]).doubleValue());
					} else if (obj[j] instanceof Date) {
						cell0.setCellStyle(styleDate);
						cell0.setCellValue((Date) obj[j]);
					} else if (obj[j] == null) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue("");
					}
				}
				k++;
			}

			wb.write(stream);

			byte[] content = stream.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			// File file = new File(sFileName);
			// Filedownload.save(file, "application/file");
			Filedownload.save(is, "application/xls", sFileName);
			is.close();
			stream.flush();
			stream.close();
		} catch (IOException e) {
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "File Not Found", Messagebox.OK, Messagebox.ERROR);
		}

	}
	
	// 使用poi匯出excel
	public static void ExcelExportb(String sSql, String sFileName, String sTitle, String[] arrHead,String starttime,String	endtime) {
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService2");
		try {
			Workbook wb = new HSSFWorkbook();
			// 檔名
			// FileOutputStream stream = new FileOutputStream(sFileName);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// 標題，設定格式:粗體置中 字體14
			HSSFCellStyle styleTitle = (HSSFCellStyle) wb.createCellStyle();
			Font fontTitle = wb.createFont();
			fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontTitle.setFontHeightInPoints((short) 14); // 字體
			styleTitle.setFont(fontTitle);
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleTitle);

			// 標題，設定格式:粗體置中 字體12
			HSSFCellStyle styleHead = (HSSFCellStyle) wb.createCellStyle();
			Font fontHead = wb.createFont();
			fontHead.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
			fontHead.setFontHeightInPoints((short) 12); // 字體
			styleHead.setFont(fontHead);
			styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平置中
			styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直置中
			setStandStyle(styleHead); // 設定Border及自動換行

			// 內容格式-文字
			HSSFCellStyle styleString = (HSSFCellStyle) wb.createCellStyle();
			styleString.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleString.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleString);

			// 內容格式-日期
			HSSFCellStyle styleDate = (HSSFCellStyle) wb.createCellStyle();
			HSSFDataFormat dateFormat = (HSSFDataFormat) wb.createDataFormat();
			styleDate.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			styleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleDate.setDataFormat(dateFormat.getFormat("YYYY/MM/DD"));
			setStandStyle(styleDate);

			// 內容格式-數字
			HSSFCellStyle styleNumber = (HSSFCellStyle) wb.createCellStyle();
			styleNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setStandStyle(styleNumber);

			// 內容格式-數字-float
			HSSFCellStyle styleFloat = (HSSFCellStyle) wb.createCellStyle();
			styleFloat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleFloat.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			setStandStyle(styleFloat);

			Query qry = CRUDService.createSQL(sSql);
			// 設定 Sheet名稱
			Sheet sh = wb.createSheet(sTitle);

			int iRow = 0;
			Row rowHead = sh.createRow(iRow);

			// 報表名稱
			Cell celltitle;
			for (int i = 0; i < arrHead.length; i++) {
				celltitle = rowHead.createCell(i);
				celltitle.setCellStyle(styleTitle);
			}
			celltitle = rowHead.createCell(0);
			celltitle.setCellValue(sTitle);
			celltitle.setCellStyle(styleTitle);

			// 合併儲存格
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, arrHead.length - 1));
			// 設定欄高
			rowHead.setHeightInPoints((short) 40);

			iRow++;
			rowHead = sh.createRow(iRow);

			// 標題
			for (int i = 0; i < arrHead.length; i++) {
				Cell cell = rowHead.createCell(i);
				cell.setCellStyle(styleHead);
				cell.setCellValue(arrHead[i]);

				int iWidth = 20;
				if(i==0)
					sh.setColumnWidth(i, (short) 256 * 40);
				else if(i==3)
					sh.setColumnWidth(i, (short) 256 * 41);
				else
					sh.setColumnWidth(i, (short) 256 * iWidth);
				
				// sh.autoSizeColumn(i);
			}

			iRow++;
			Row rowCell = sh.createRow(iRow);
			int k = 0;
			// // 內容
			// for (int i =0;i<30;i++) {
			// rowCell = sh.createRow(iRow + k);
			// String obj = i+"---";
			// for (int j = 0; j < arrHead.length; j++) {
			// Cell cell0 = rowCell.createCell(j);
			// cell0.setCellValue(obj +j);
			// }
			// k++;
			// }
			
			Map<String,String> countMap  = DSIDN10M_Program.getNewMessage4(starttime,endtime);
			Map<String,String> odnoMap =  DSIDN10M_Program.getNewMessage5(starttime,endtime);
			
			
			String tempcontent ="";
			int startrow =0;
			int endrow =0;
			int index =0;
			List lit = qry.getResultList();
			Iterator it = lit.iterator();
			System.out.println("ppppppppppppppppp==========================start");
			// 內容
			for (Iterator i = it; i.hasNext();) {
				rowCell = sh.createRow(iRow + k);
				///rowCell.setHeightInPoints((short) 20);
				Object[] obj = (Object[]) i.next();
				int cellnum = 1;
				for (int j = 0; j < obj.length; j++) {
					Cell cell0 = rowCell.createCell(j==3?j+2:j);
					
					if (obj[j] instanceof String) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue((String) obj[j]);
					} else if (obj[j] instanceof BigDecimal) {
						cell0.setCellStyle(styleNumber);
						cell0.setCellValue(((BigDecimal) obj[j]).doubleValue());
					} else if (obj[j] instanceof Date) {
						cell0.setCellStyle(styleDate);
						cell0.setCellValue((Date) obj[j]);
					} else if (obj[j] == null) {
						cell0.setCellStyle(styleString);
						cell0.setCellValue("");
					}
					if (j == 0) {
						if (index == 0) {
							tempcontent = (String) obj[0];
							startrow = iRow + k;
							endrow = startrow;
							Cell cell1 = rowCell.createCell(4);
							cell1.setCellStyle(styleString);
							//cell1.setCellValue(tempcontent);
							cell1.setCellValue(countMap != null?countMap.get(tempcontent):"");
							
							
							Cell cell2 = rowCell.createCell(3);
							cell2.setCellStyle(styleString);
							//cell1.setCellValue(tempcontent);
							cell2.setCellValue(odnoMap != null ?odnoMap.get(tempcontent):"");
							
						} else {
							System.out.println((index+iRow)+"===index"+obj[0]);
							if (tempcontent != null && !tempcontent.equals((String) obj[0])) {
								tempcontent = (String) obj[0];
								System.out.println(startrow);
								System.out.println(iRow + k-1);
								//System.out.println(startrow + index);
								System.out.println("===============u=======================");
								sh.addMergedRegion(new CellRangeAddress(
										startrow, // first// row (0-based)
										iRow + k-1, // last row (0-based)
										4, // first column (0-based)
										4// last column (0-based)
								));
								
								sh.addMergedRegion(new CellRangeAddress(
										startrow, // first// row (0-based)
										iRow + k-1, // last row (0-based)
										3, // first column (0-based)
										3// last column (0-based)
								));
								startrow = iRow + index;

								Cell cell1 = rowCell.createCell(4);
								cell1.setCellStyle(styleString);
								//cell1.setCellValue(tempcontent);

								cell1.setCellValue(countMap != null ?countMap.get(tempcontent):"");
								
								
								Cell cell2 = rowCell.createCell(3);
								cell2.setCellStyle(styleString);
								//cell1.setCellValue(tempcontent);
								cell2.setCellValue(odnoMap != null ?odnoMap.get(tempcontent):"");
								
								
								
							}else{
								endrow++;
							}
						}

						// if(j==)
					}

					if ((iRow + k) == lit.size()) {
						sh.addMergedRegion(new CellRangeAddress(
								startrow, // first row (0-based)
								iRow + k+1, // last row (0-based)
								4, // first column (0-based)
								4// last column (0-based)
						));
						sh.addMergedRegion(new CellRangeAddress(
								startrow, // first row (0-based)
								iRow + k+1, // last row (0-based)
								3, // first column (0-based)
								3// last column (0-based)
						));
					//	System.out.println(startrow);
					//	System.out.println((iRow + k + 1));
					}
				}
				k++;
				index++;
			}
			System.out.println("ppppppppppppppppp==========================end");

			sh.addMergedRegion(new CellRangeAddress(
					3, //first row (0-based)
					6, //last row (0-based)
					3, //first column (0-based)
					3//last column (0-based)
					));

			wb.write(stream);

			byte[] content = stream.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			// File file = new File(sFileName);
			// Filedownload.save(file, "application/file");
			Filedownload.save(is, "application/xls", sFileName);
			is.close();
			stream.flush();
			stream.close();
		} catch (IOException e) {
			Messagebox.show(Labels.getLabel("PUBLIC.MSG0008"), "File Not Found", Messagebox.OK, Messagebox.ERROR);
		}

	}

	// 設定框線
	private static void setStandStyle(HSSFCellStyle style) {
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setWrapText(true);
	}

}
