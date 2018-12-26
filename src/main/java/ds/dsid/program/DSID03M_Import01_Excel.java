package ds.dsid.program;

import java.io.InputStream;
import java.math.BigDecimal;
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


public class DSID03M_Import01_Excel {
	
	public  static List<Object [] > importExcel(InputStream input,int sheet,int startIndex) {
		List<Object [] > relist = null;
		HSSFSheet sheetAt = null;
		HSSFWorkbook wb = null;
		try {
			relist= new ArrayList<Object[]>();
			wb = new HSSFWorkbook(input);
			sheetAt = wb.getSheetAt(sheet);
			System.out.println(sheetAt.getPhysicalNumberOfRows());
			
			for (int i = startIndex; i < sheetAt.getPhysicalNumberOfRows(); i++) {
				HSSFRow row = sheetAt.getRow(i);
				System.out.println("===================="+i+"===================");
				Object [] obj = new Object[8];
				
				String c1 = getCellValue(row.getCell(0));
				String c2 = getCellValue(row.getCell(1));
				String c3 = getCellValue(row.getCell(2));
				String c4 = getCellValue(row.getCell(3));
				String c5 = getCellValue(row.getCell(4));
				String c6 = getCellValue(row.getCell(5));
				String c7 = getCellValue(row.getCell(6));
				String c8 = getCellValue(row.getCell(7));
				String c9 = getCellValue(row.getCell(8));
				String c10 = getCellValue(row.getCell(9));
				String c11 = getCellValue(row.getCell(10));
				String c12 = getCellValue(row.getCell(11));
				String c13 = getCellValue(row.getCell(12));
				String c14 = getCellValue(row.getCell(13));
				String c15 = getCellValue(row.getCell(14));
				String c16 = getCellValue(row.getCell(15));
				String c17 = getCellValue(row.getCell(16));
				String c18 = getCellValue(row.getCell(17));
				
				if(c6 == null|| "".equals(c6)){
					break;
				}
				obj[0]=c1;
				obj[1]=c2;
				obj[2]=c3;
				obj[3]=c4;
				obj[4]=c5;
				obj[5]=c6;
				obj[6]=c7;
				obj[7]=c18;
				relist.add(obj);
				System.out.println(
					"cell1:"+c1+",   "+"cell2:"+c2+",  "
					+"cell3:"+c3+",  "+"cell4:"+c4+",  "+"  cell5:"+c5+",  "
					+"cell6:"+c6+"  cell7:"+c7+"  cell8:"+c8+"  cell9:"
					+c9+"  cell10:"+c10+"  cell11:"+c11+"  cell12:"+c12
					+"  cell13:"+c13+"  cell14:"+c14+"  cell15:"+c15
					+"  cell16:"+c16+"  cell17:"+c17+"  cell18:"+c18
				);
				System.out.println();
				
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relist;
	}
	
	public  static void importExcel01(InputStream input,int sheet) {
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
	
	
	
	public  static void importExcel02(InputStream input,int sheet,int startIndex) {
		HSSFSheet sheetAt = null;
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(input);
			sheetAt = wb.getSheetAt(sheet);
			System.out.println(sheetAt.getPhysicalNumberOfRows());
			String cell1 ="";
			String cell2 ="";
			String cell3 ="";
			String cell4 ="";
			String cell5 ="";
			String cell6 ="";
			String cell7 ="";
			String cell8 ="";
			String cell9 ="";
			String cell10 ="";
			String cell11 ="";
			String cell12 ="";
			String cell13 ="";
			String cell14 ="";
			String cell15 ="";
			String cell16 ="";
			String cell17 ="";
			String cell18 ="";
			
			for (int i = startIndex; i < sheetAt.getPhysicalNumberOfRows(); i++) {
				HSSFRow row = sheetAt.getRow(i);
				System.out.println("===================="+i+"===================");
				
				String c1 = getCellValue(row.getCell(0));
				String c2 = getCellValue(row.getCell(1));
				String c3 = getCellValue(row.getCell(2));
				String c4 = getCellValue(row.getCell(3));
				String c5 = getCellValue(row.getCell(4));
				String c6 = getCellValue(row.getCell(5));
				String c7 = getCellValue(row.getCell(6));
				String c8 = getCellValue(row.getCell(7));
				String c9 = getCellValue(row.getCell(8));
				String c10 = getCellValue(row.getCell(9));
				String c11 = getCellValue(row.getCell(10));
				String c12 = getCellValue(row.getCell(11));
				String c13 = getCellValue(row.getCell(12));
				String c14 = getCellValue(row.getCell(13));
				String c15 = getCellValue(row.getCell(14));
				String c16 = getCellValue(row.getCell(15));
				String c17 = getCellValue(row.getCell(16));
				String c18 = getCellValue(row.getCell(17));
				
				if(i==0){
					cell1=c1;
					cell2=c2;
					cell3=c3;
					cell4=c4;
					cell5=c5;
					cell6=c6;
					cell7=c7;
					cell8=c8;
					cell9=c9;
					cell10=c10;
					cell11=c11;
					cell12=c11;
					cell13=c11;
					cell14=c11;
					cell15=c11;
					cell16=c11;
					cell17=c11;
					cell18=c11;
				}else{
					if(!"".equals(c1)){
						cell1=c1;
						cell2=c2;
						cell3=c3;
						cell4=c4;
						cell5=c5;
						cell6=c6;
						cell7=c7;
						cell8=c8;
						cell9=c9;
						cell10=c10;
						cell11=c11;
						cell12=c11;
						cell13=c11;
						cell14=c11;
						cell15=c11;
						cell16=c11;
						cell17=c11;
						cell18=c11;
					}
				}
				
				System.out.println("cell1:"+cell1+","+"cell2:"+cell2+","+"cell3:"+cell3+","+"cell4:"+cell4+","
				+"cell6:"+cell6+"cell7:"+cell7+"cell8:"+cell8+"cell9:"+cell9+"cell10:"+cell10+"cell11:"+cell11);
//				String c1a="";
//				for(int j=0;j<row.getPhysicalNumberOfCells();j++){
//					HSSFCell cell = row.getCell(j);
//					if(cell !=null)
//					c1a+=i+"="+cell.getCellNum()+"="+getCellValue(cell);
//				//	System.out.print(j+"="+getCellValue(cell)+(("").equals(getCellValue(cell)))+" ,");
//				}
//				System.out.println(c1a);
//				c1a="";
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
				System.out.println(DateUtil.isCellDateFormatted(cell)+"]]]]]]]]]]]]]]]]]]"+ DateUtil.isValidExcelDate(cell.getNumericCellValue()));
				if(DateUtil.isCellDateFormatted(cell) && DateUtil.isValidExcelDate(cell.getNumericCellValue())){
					//System.out.println("date ====================== "+cell);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date dt= DateUtil.getJavaDate(cell.getNumericCellValue());
					//System.out.println(dt);
					cellValue = sdf.format(dt);
				}else{
					try{
						//java.text.DecimalFormat formatter = new java.text.DecimalFormat("########");
						//String str = formatter.format(cell.getNumericCellValue());
						BigDecimal bd = new BigDecimal(cell.getNumericCellValue()); 
						cellValue =String.valueOf(bd.setScale(5, BigDecimal.ROUND_HALF_UP));// String.valueOf(cell.getNumericCellValue()) + "";
						//cellValue =String.valueOf(bd.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue());// String.valueOf(cell.getNumericCellValue()) + "";
					}catch(Exception ex){
					}
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
