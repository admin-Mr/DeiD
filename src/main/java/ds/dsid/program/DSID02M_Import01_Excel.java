package ds.dsid.program;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;


public class DSID02M_Import01_Excel {
	
	public  static List<Object [] > importExcel(InputStream input,int sheet,int startIndex) {
		List<Object [] > relist = null;
		HSSFSheet sheetAt = null;
		HSSFWorkbook wb = null;
		try {
			relist= new ArrayList<Object[]>();
			wb = new HSSFWorkbook(input);
			sheetAt = wb.getSheetAt(sheet);
			System.out.println(sheetAt.getPhysicalNumberOfRows());
			
			String cell1 ="";
			String cell2="";
			String cell3 ="";
			for (int i = 0; i < sheetAt.getPhysicalNumberOfRows(); i++) {
				HSSFRow row = sheetAt.getRow(i);
				System.out.println("===================="+i+"===================");
				Object [] obj = new Object[4];
				String c1 = getCellValue(row.getCell(0));
				String c2 = getCellValue(row.getCell(1));
				String c3 = getCellValue(row.getCell(2));
				
				if(i==0){
					cell1=c1;
					cell2=c2;
					cell3=c3;
				}else{
					if(!"".equals(c1)){
						cell1=c1;
						cell2=c2;
						cell3=c3;
					}
				}
				String tempel_no = cell2;
				String color="";
				String el_no ="";
				if(tempel_no.contains("[")&&tempel_no.contains("]")){
					String [] str = tempel_no.split("]");
					color=str[0].replace("[", "");
					el_no = str[1];
				}
				obj[0]=cell1;
				obj[1]=el_no;
				obj[2]=color;
				obj[3]=cell3;
				System.out.println(" 1="+cell1+"  2="+el_no+"  3="+color+"  4="+cell3);
				System.out.println();
				System.out.println();
				relist.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relist;
	}
	
	
	
	public  static void importExcel01(InputStream input,int sheet) {
		System.out.println("===========InputStream==="+input);
		HSSFSheet sheetAt = null;
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(input);
			sheetAt = wb.getSheetAt(sheet);
			System.out.println(sheetAt.getPhysicalNumberOfRows());
			String cell1 ="";
			String cell2="";
			String cell3 ="";
			String cell4="";
			String cell5="";
			
			for (int i = 0; i < sheetAt.getPhysicalNumberOfRows(); i++) {
				HSSFRow row = sheetAt.getRow(i);
				System.out.println("===================="+i+"===================");
				
				String c1 = getCellValue(row.getCell(0));
				String c2 = getCellValue(row.getCell(1));
				String c3 = getCellValue(row.getCell(2));
				String c4 = getCellValue(row.getCell(3));
				String c5 = getCellValue(row.getCell(4));
				
				if(i==0){
					cell1=c1;
					cell2=c2;
					cell3=c3;
					cell4=c4;
					cell5=c5;
				}else{
					if(!"".equals(c1)){
						cell1=c1;
						cell2=c2;
						cell3=c3;
						cell4=c4;
						cell5=c5;
					}
				}
				
				System.out.println("cell1:"+cell1+","+"cell2:"+cell2+","+"cell3:"+cell3+","+"cell4:"+cell4+","+"cell5:"+cell5);
//				for(int j=0;j<row.getPhysicalNumberOfCells();j++){
//					String c1="";
//					HSSFCell cell = row.getCell(j);
//					System.out.print(j+"="+getCellValue(cell)+(("").equals(getCellValue(cell)))+" ,");
//				}
				System.out.println();
				
				System.out.println();
				
//				//  提案日期
//				HSSFCell PDate = row.getCell(2);
//				try{
//					System.out.println("------------------celltype === "+PDate.getCellType());
//					System.out.println("HSSFCell.CELL_TYPE_NUMERIC ---------"+ HSSFCell.CELL_TYPE_NUMERIC);
//					System.out.println("--------------------------------"+getCellValue(PDate));
//				}catch(Exception e){
//				}
//				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static String getCellValue(HSSFCell cell) {
		String cellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				System.out.println("]]]]]]]]]]]]]]]]]]"+ DateUtil.isValidExcelDate(cell.getNumericCellValue()));
				if(DateUtil.isCellDateFormatted(cell) || DateUtil.isValidExcelDate(cell.getNumericCellValue())){
					System.out.println("date ====================== "+cell);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date dt= DateUtil.getJavaDate(cell.getNumericCellValue());
					System.out.println(dt);
					cellValue = sdf.format(dt);
				}else{
					cellValue = String.valueOf(cell.getNumericCellValue()) + "";
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				cellValue = cellValue.trim();
				break;
			default:
				break;
			}
		}
		return cellValue;
	}
    
    
}
