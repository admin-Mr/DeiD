package ds.dsid.program;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.*;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.zul.Filedownload;
import util.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DSID01RemitTask {
    int anInt=0;

    public static void ExcelExport1(String MODEL_NA, String START, String GROUP_NO, String GROUP_NO1, String CONDITION) {

        System.out.println("MODEL_NA:" + MODEL_NA + "  START:" + START + " GROUP_NO:" + GROUP_NO
                + " GROUP_NO1:" + GROUP_NO1 + " CONDITION:" + CONDITION);
        String name = "訂單報表拆分匯出";
        Connection Conn = getDB01Conn();
        Connection conn = Common.getDbConnection();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HSSFWorkbook wb = new HSSFWorkbook();

        // 设置字体，表格的长宽高，
        HSSFFont font1 = wb.createFont();
        font1.setFontName("新細明體");                    //设置字體
        font1.setFontHeightInPoints((short) 10);            //设置字体高度
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //设置字體樣式 		style1

        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font1);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style1.setFillPattern((short) 0);
        style1.setWrapText(true);

        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font1);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style2.setFillPattern((short) 0);
        style2.setWrapText(true);
        style2.setDataFormat(wb.createDataFormat().getFormat("0.0"));

        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font1);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style3.setFillPattern((short) 0);
        style3.setWrapText(true);
        style3.setDataFormat(wb.createDataFormat().getFormat("0.00"));

        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font1);
        style4.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style4.setFillPattern((short) 0);
        style4.setWrapText(true);
        style4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style4.setFillPattern(CellStyle.SOLID_FOREGROUND);


        HSSFSheet sheet = wb.createSheet(MODEL_NA+"已分隔");
        SetColumnWidth(sheet);
        Exportexcel1(wb, sheet, MODEL_NA, START, GROUP_NO,GROUP_NO1,CONDITION, Conn, conn, style1, style2, style3, style4);

        HSSFSheet sheet1 = wb.createSheet(MODEL_NA+"未分隔");
        SetColumnWidth(sheet1);
        Exportexcel2(wb, sheet1, MODEL_NA, START, GROUP_NO,GROUP_NO1,CONDITION, Conn, conn, style1, style2, style3, style4);

        try {
            wb.write(stream);
            byte[] content = stream.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            //儲存位置、名稱
            Filedownload.save(is, "application/xls", name);
            is.close();
            stream.flush();
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Common.closeConnection(conn);
        Common.closeConnection(Conn);


    }

    private static void Exportexcel2(HSSFWorkbook wb, HSSFSheet sheet1, String model_na, String start, String group_no, String group_no1, String condition, Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
        HSSFRow row = null;
        HSSFCell cell = null;
        PreparedStatement ps = null,ps1=null;
        ResultSet rs = null,rs1=null;
        List<String> list2 =new ArrayList<>();
        List<String> list3 =new ArrayList<>();
        List<String> list = GROUP_NOE(group_no);
        List<String> list1 = GROUP_NOE(group_no1);
        String SQLI = "";
        if (!"".equals(model_na)) {
            SQLI = "AND MODEL_NA LIKE '%" + model_na + "'";
        }
        for (String s : list1) {
            String sql="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' " + SQLI + "order by OD_NO";
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()){
                    if(rs.getString(""+s+"").indexOf(""+condition+"") !=-1){
                        list2.add(rs.getString("OD_NO"));
                    }else{
                        list3.add(rs.getString("OD_NO"));
                    }
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        List newList1 = new ArrayList(new HashSet(list2));//有逗號的
        List newList2 = new ArrayList(new HashSet(list3));//無

        Header1(sheet1, style1, row, model_na, cell, conn, list);

        //設置表頭格式
        row = sheet1.getRow(0);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        //接單的日期
        row = sheet1.getRow(1);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(start);

        //數據從第三行開始
        int rowNum = 3;
        int str = 1;
        for (Object o : newList2) {
            System.out.println("enene:"+o);
            String sql1 = "SELECT * FROM DSID01 WHERE OD_NO in ('"+o+"') order by OD_NO";
            System.out.println("en:"+sql1);
            try {
                ps1 = conn.prepareStatement(sql1);
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    row = sheet1.createRow(rowNum);
                    row.setHeightInPoints(25);

                    cell = row.createCell(0);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(str++);

                    cell = row.createCell(1);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs1.getString("ROUND"));

                    cell = row.createCell(2);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs1.getString("OD_NO"));

                    cell = row.createCell(3);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs1.getString("MODEL_NA"));

                    cell = row.createCell(4);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs1.getString("TOOLING_SIZE"));

                    for (int i = 1; i < list.size() + 1; i++) {
                        cell = row.createCell(4 + i);
                        cell.setCellType(1);
                        cell.setCellStyle(style1);
                        cell.setCellValue(rs1.getString(list.get(i - 1)));
                    }
                    //每加一條數據就自增一欄
                    rowNum++;
                }
                System.out.println("rowNum:"+rowNum);

                //關閉流
                ps1.close();
                rs1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void ExcelExport(String MODEL_NA, String START, String GROUP_NO) {

        System.out.println("MODEL_NA:" + MODEL_NA + "  START:" + START + " GROUP_NO:" + GROUP_NO);
        String name = "訂單報表匯出";
        Connection Conn = getDB01Conn();
        Connection conn = Common.getDbConnection();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HSSFWorkbook wb = new HSSFWorkbook();

        // 设置字体，表格的长宽高，
        HSSFFont font1 = wb.createFont();
        font1.setFontName("新細明體");                    //设置字體
        font1.setFontHeightInPoints((short) 10);            //设置字体高度
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //设置字體樣式 		style1

        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font1);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style1.setFillPattern((short) 0);
        style1.setWrapText(true);

        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font1);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style2.setFillPattern((short) 0);
        style2.setWrapText(true);
        style2.setDataFormat(wb.createDataFormat().getFormat("0.0"));

        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font1);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style3.setFillPattern((short) 0);
        style3.setWrapText(true);
        style3.setDataFormat(wb.createDataFormat().getFormat("0.00"));

        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font1);
        style4.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style4.setFillPattern((short) 0);
        style4.setWrapText(true);
        style4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style4.setFillPattern(CellStyle.SOLID_FOREGROUND);
        HSSFSheet sheet = wb.createSheet(MODEL_NA);
        SetColumnWidth(sheet);
        Exportexcel(wb, sheet, MODEL_NA, START, GROUP_NO, Conn, conn, style1, style2, style3, style4);
        try {
            wb.write(stream);
            byte[] content = stream.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            //儲存位置、名稱
            Filedownload.save(is, "application/xls", name);
            is.close();
            stream.flush();
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Common.closeConnection(conn);
        Common.closeConnection(Conn);

    }

    private static void Exportexcel1(HSSFWorkbook wb, HSSFSheet sheet, String model_na, String start, String group_no, String group_no1, String condition, Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
        HSSFRow row = null;
        HSSFCell cell = null;
        PreparedStatement ps = null,ps1=null;
        ResultSet rs = null,rs1=null;
        List<String> list2 =new ArrayList<>();
        List<String> list3 =new ArrayList<>();
        List<String> list = GROUP_NOE(group_no);
        List<String> list1 = GROUP_NOE(group_no1);
        String SQLI = "";
        if (!"".equals(model_na)) {
            SQLI = "AND MODEL_NA LIKE '%" + model_na + "'";
        }
        for (String s : list1) {
            String sql="SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' " + SQLI + "order by OD_NO";
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()){
                    if(rs.getString(""+s+"").indexOf(""+condition+"") !=-1){
                        list2.add(rs.getString("OD_NO"));
                    }else{
                        list3.add(rs.getString("OD_NO"));
                    }
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        List newList1 = new ArrayList(new HashSet(list2));//有逗號的
        List newList2 = new ArrayList(new HashSet(list3));//無

        Header1(sheet, style1, row, model_na, cell, conn, list);

        //設置表頭格式
        row = sheet.getRow(0);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        //接單的日期
        row = sheet.getRow(1);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(start);

        //數據從第三行開始
        int rowNum = 3;
        int str = 1;
        for (Object o : newList1) {
            System.out.println("enene:"+o);
            String sql1 = "SELECT * FROM DSID01 WHERE OD_NO in ('"+o+"') order by OD_NO";
            System.out.println("en:"+sql1);
        try {
            ps1 = conn.prepareStatement(sql1);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(str++);

                cell = row.createCell(1);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("ROUND"));

                cell = row.createCell(2);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("OD_NO"));

                cell = row.createCell(3);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("MODEL_NA"));

                cell = row.createCell(4);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("TOOLING_SIZE"));

                for (int i = 1; i < list.size() + 1; i++) {
                    cell = row.createCell(4 + i);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs1.getString(list.get(i - 1)));
                }
                //每加一條數據就自增一欄
                rowNum++;
            }
            System.out.println("rowNum:"+rowNum);

            //關閉流
            ps1.close();
            rs1.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        }

    }

    private static void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, String model_na, HSSFCell cell, Connection conn,  List<String> list) {

        PreparedStatement ps3 = null;
        ResultSet rs3 = null;

        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(model_na);

        String sql3 = "SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%" + model_na + "'";

        try {
            ps3 = conn.prepareStatement(sql3);
            rs3 = ps3.executeQuery();
            //把需要的欄位名稱設置到excle中
            while (rs3.next()) {
                row = sheet.createRow(1);
                row.setHeightInPoints(30);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("接單日期");

                row = sheet.createRow(2);
                row.setHeightInPoints(30);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("項次");

                cell = row.createCell(1);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("輪次號");

                cell = row.createCell(2);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("訂單號");

                cell = row.createCell(3);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("形體");

                cell = row.createCell(4);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("TOOLING_SIZE");

                for (int i = 1; i < list.size() + 1; i++) {
                    cell = row.createCell(4 + i);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs3.getString(list.get(i - 1) + "_NA"));
                }

            }
            //關閉流
            ps3.close();
            rs3.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void Exportexcel(HSSFWorkbook wb, HSSFSheet sheet, String MODEL_NA, String START, String GROUP_NO, Connection Conn, Connection conn, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
        HSSFRow row = null;
        HSSFCell cell = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        List<String> list = GROUP_NOE(GROUP_NO);
        Header(sheet, style1, row, MODEL_NA, cell, conn, list);

        //設置表頭格式
        row = sheet.getRow(0);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        //接單的日期
        row = sheet.getRow(1);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(START);

        //數據從第三行開始
        int rowNum = 3;
        int str = 1;
        //判斷形體是否相同
        String SQLI = "";
        if (!"".equals(MODEL_NA)) {
            SQLI = "AND MODEL_NA LIKE '%" + MODEL_NA + "'";
        }
        String sql1 = "SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + START + "' " + SQLI + "order by OD_NO";
        try {
            ps1 = conn.prepareStatement(sql1);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(str++);

                cell = row.createCell(1);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("ROUND"));

                cell = row.createCell(2);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("OD_NO"));

                cell = row.createCell(3);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("MODEL_NA"));

                cell = row.createCell(4);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs1.getString("TOOLING_SIZE"));

                for (int i = 1; i < list.size() + 1; i++) {
                    cell = row.createCell(4 + i);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs1.getString(list.get(i - 1)));
                }
                //每加一條數據就自增一欄
                rowNum++;
            }
            //關閉流
            ps1.close();
            rs1.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void Header(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, String MODEL_NA, HSSFCell cell, Connection conn, List<String> list) {

        PreparedStatement ps3 = null;
        ResultSet rs3 = null;

        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(MODEL_NA);

        String sql3 = "SELECT * FROM DSID10 WHERE MODEL_NAS LIKE'%" + MODEL_NA + "'";

        try {
            ps3 = conn.prepareStatement(sql3);
            rs3 = ps3.executeQuery();
            //把需要的欄位名稱設置到excle中
            while (rs3.next()) {
                row = sheet.createRow(1);
                row.setHeightInPoints(30);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("接單日期");

                row = sheet.createRow(2);
                row.setHeightInPoints(30);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("項次");

                cell = row.createCell(1);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("輪次號");

                cell = row.createCell(2);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("訂單號");

                cell = row.createCell(3);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("形體");

                cell = row.createCell(4);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("TOOLING_SIZE");

                for (int i = 1; i < list.size() + 1; i++) {
                    cell = row.createCell(4 + i);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(rs3.getString(list.get(i - 1) + "_NA"));
                }

            }
            //關閉流
            ps3.close();
            rs3.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //拆分字符串
    public static List<String> GROUP_NOE(String GROUP_NO) {
        List<String> list = new ArrayList<String>();
        String[] str1 = GROUP_NO.split(",");
        for (int i = 0; i < str1.length; i++) {
            list.add(str1[i]);
        }
        return list;
    }

    //設置表單的長度
    private static void SetColumnWidth(HSSFSheet sheet1) {
        // TODO Auto-generated method stub
        sheet1.setColumnWidth(0, 10 * 256);
        sheet1.setColumnWidth(1, 15 * 256);
        sheet1.setColumnWidth(4, 30 * 256);
        sheet1.setColumnWidth(5, 20 * 256);
    }

    //连接数据库
    public static Connection getDB01Conn() {
        Connection conn = null;
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@10.8.1.32:1521:ftldb1";
        String user = "DSOD";
        String pwd = "ora@it2013";
        try {
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            System.err.println(">>>鏈接DB01數據庫");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
