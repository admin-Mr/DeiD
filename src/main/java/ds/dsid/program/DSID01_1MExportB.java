package ds.dsid.program;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.*;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;
import util.BlackBox;
import util.Common;
import util.MSMode;
import util.OpenWinCRUD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DSID01_1MExportB extends OpenWinCRUD {
    @Wire
    private Window windowMaster;
    @Wire
    private Datebox po_date1;
    @Wire
    private Button btnexport;
    @Wire
    private Combobox cboREP_CNAMEQ;
    @Wire
    private Textbox YPG_TYPE,YPG_TYPE1,txtMODEL_NA,txtMODEL_NA1;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");


    @Listen("onClick =#btnexport")
    public void onClickbtnexport(Event event) throws Exception {
        Date date = po_date1.getValue();
        if(YPG_TYPE1.getValue().trim().length()>0&&txtMODEL_NA1.getValue().trim().length()>0){
            if(cboREP_CNAMEQ.getSelectedItem().getValue() != null | (!"".equals(date) && date != null)
                |YPG_TYPE.getValue().length()>0 |txtMODEL_NA.getValue().length()>0  ){
                filterHeader();
            }else{
                Messagebox.show("日期、形體和選擇GROUP_NO都不能為空!");
            }

        }else if(cboREP_CNAMEQ.getSelectedItem().getValue() != null | (!"".equals(date) && date != null)
                |YPG_TYPE.getValue().length()>0 |txtMODEL_NA.getValue().length()>0){
                filterHeader1();
        }else{
            Messagebox.show("日期、形體和選擇GROUP_NO都不能為空!");
        }

    }

    private void filterHeader1() {
        String name = "拉链版大勾勾派工单";
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

        String START = po_date1.getValue().toString();
        START = sdf.format(po_date1.getValue());
        HSSFSheet sheet = wb.createSheet("大勾勾派工單");
        String model_na = cboREP_CNAMEQ.getSelectedItem().getValue();
        String group = YPG_TYPE.getValue();
        String string=txtMODEL_NA.getValue();
        SetColumnWidth(sheet);
        Exportexce1(wb, sheet, model_na, START, group,string, Conn, conn, style1, style2, style3, style4);

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
    private void Exportexce1(HSSFWorkbook wb, HSSFSheet sheet, String model_na, String start, String group,String string,Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        Header1(model_na,sheet, style1, row, cell, conn);

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

        int str = 1;
        int num=3;
        String sql=" SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"'" +
                " AND MODEL_NA like '%"+model_na+"' AND "+group+"='"+string+"' order by ON_ORDER";
        System.out.println(sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                row = sheet.createRow(num);
                row.setHeightInPoints(25);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(str++);

                cell = row.createCell(1);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("ORDER_DATE"));

                cell = row.createCell(2);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("ROUND"));

                cell = row.createCell(3);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("OD_NO"));

                cell = row.createCell(4);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("MODEL_NA"));

                cell = row.createCell(5);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("TOOLING_SIZE"));

                cell = row.createCell(6);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString(""+group+""));

                num++;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
    private void Header1(String model_na,HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell, Connection conn) {

        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(model_na+"大勾勾派工单");

        row = sheet.createRow(1);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("派工日期");

        row = sheet.createRow(2);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("項次");

        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("接單日期");

        cell = row.createCell(2);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("輪次號");

        cell = row.createCell(3);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("訂單號碼");

        cell = row.createCell(4);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("型體");

        cell = row.createCell(5);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("TOOLING_SIZE");

        cell = row.createCell(6);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("勾勾大小");


    }


    private void filterHeader() {
        String name = "拉链版大勾勾派工单";
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

        String START = po_date1.getValue().toString();
        START = sdf.format(po_date1.getValue());
        HSSFSheet sheet = wb.createSheet("大勾勾派工單");
        String model_na = cboREP_CNAMEQ.getSelectedItem().getValue();
        String group = YPG_TYPE.getValue();
        String string=txtMODEL_NA.getValue();
        String group1=YPG_TYPE1.getValue();
        String string1=txtMODEL_NA1.getValue();
        SetColumnWidth(sheet);
        Exportexce(wb, sheet, model_na, START, group,string,group1,string1, Conn, conn, style1, style2, style3, style4);

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

    private void Exportexce(HSSFWorkbook wb, HSSFSheet sheet, String model_na, String start, String group,String string,String group1,String string1, Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        Header(model_na,sheet, style1, row, cell, conn);

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

        int str = 1;
        int num=3;
        String sql=" SELECT * FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"'" +
                " AND MODEL_NA like '%"+model_na+"' AND "+group+"='"+string+"' " +
                "AND "+group1+" like '%"+string1+"' order by ON_ORDER";
        System.out.println(sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                row = sheet.createRow(num);
                row.setHeightInPoints(25);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(str++);

                cell = row.createCell(1);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("ORDER_DATE"));

                cell = row.createCell(2);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("ROUND"));

                cell = row.createCell(3);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("OD_NO"));

                cell = row.createCell(4);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("MODEL_NA"));

                cell = row.createCell(5);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString("TOOLING_SIZE"));

                cell = row.createCell(6);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString(""+group+""));

                cell = row.createCell(7);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(rs.getString(""+group1+""));

                num++;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
    private void Header(String model_na,HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell, Connection conn) {

        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(model_na+"大勾勾派工单");

        row = sheet.createRow(1);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("派工日期");

        row = sheet.createRow(2);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("項次");

        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("接單日期");

        cell = row.createCell(2);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("輪次號");

        cell = row.createCell(3);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("訂單號碼");

        cell = row.createCell(4);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("型體");

        cell = row.createCell(5);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("TOOLING_SIZE");

        cell = row.createCell(6);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("勾勾大小");

        cell = row.createCell(7);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("勾勾顏色");

    }

    //設置表單的長度
    private static void SetColumnWidth(HSSFSheet sheet1) {
        // TODO Auto-generated method stub
        sheet1.setColumnWidth(0, 15 * 256);
        sheet1.setColumnWidth(1, 15 * 256);
        sheet1.setColumnWidth(2, 15 * 256);
        sheet1.setColumnWidth(3, 15 * 256);
        sheet1.setColumnWidth(4, 25 * 256);
        sheet1.setColumnWidth(5, 15 * 256);
        sheet1.setColumnWidth(6, 15 * 256);
        sheet1.setColumnWidth(7, 15 * 256);
        sheet1.setColumnWidth(8, 15 * 256);
        sheet1.setColumnWidth(9, 15 * 256);
        sheet1.setColumnWidth(10, 15 * 256);
        sheet1.setColumnWidth(11, 15 * 256);
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


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Listen("onOpen= #cboREP_CNAMEQ")
    public void prepare1() {
        String START = po_date1.getValue().toString();
        START = sdf.format(po_date1.getValue());
        List<BlackBox.temp> lstREP_CNAME;
        lstREP_CNAME = BlackBox.getListModelList("DISTINCT MODEL_NA", "MODEL_NA", " DSID01",
                " AND TO_CHAR(ORDER_DATE,'YYYY/MM/DD') ='" + START + "'AND MODEL_NA NOT LIKE 'W%'",
                "MODEL_NA", CRUDService);
        cboREP_CNAMEQ.setModel(new ListModelList(lstREP_CNAME, true));
    }
    @Override
    protected Class getEntityClass() {
        return null;
    }

    @Override
    protected Window getRootWindow() {
        return windowMaster;
    }

    @Override
    protected MSMode getMSMode() {
        return MSMode.MASTER;
    }

    @Override
    protected void init() {

    }

    @Override
    protected Object doSaveDefault(String columnName) {
        return null;
    }

    @Override
    protected HashMap getReturnMap() {
        return null;
    }
}
