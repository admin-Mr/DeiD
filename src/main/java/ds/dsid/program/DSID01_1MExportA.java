package ds.dsid.program;

import ds.dsid.domain.DSID01;
import ds.dsid.domain.GENERIC;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.hssf.usermodel.*;
import org.zkoss.poi.ss.usermodel.CellRange;
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

import javax.swing.plaf.synth.Region;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DSID01_1MExportA extends OpenWinCRUD {
    @Wire
    private Window windowMaster;
    @Wire
    private Datebox po_date1;
    @Wire
    private Button btnexport, btnexport1, btnexport2;
    @Wire
    private Combobox cboREP_CNAMEQ;
    @Wire
    private Textbox YPG_TYPE;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    @Listen("onClick =#btnexport")
    public void onClickbtnexport(Event event) throws Exception {
        filterHeader();
    }

    @Listen("onClick =#btnexport1")
    public void onClickbtnexport1(Event event) throws Exception {
        filterHeader1();
    }

    @Listen("onClick =#btnexport2")
    public void onClickbtnexport2(Event event) throws Exception {
        filterHeader2();
    }

    private void filterHeader2() {
        String name = "拉链版鞋垫派工单";
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
        HSSFSheet sheet = wb.createSheet("拉链版鞋垫派工单");
        SetColumnWidth2(sheet);
        String model_na = cboREP_CNAMEQ.getSelectedItem().getValue();
        String group = YPG_TYPE.getValue().trim();
        Exportexcel2(wb, sheet, model_na, START, group, Conn, conn, style1, style2, style3, style4);

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

    private void Exportexcel2(HSSFWorkbook wb, HSSFSheet sheet, String value, String start, String group, Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
        System.out.println("形體：" + value);
        System.out.println("時間：" + start);
        System.out.println("GROUP：" + group);


        HSSFRow row = null;
        HSSFCell cell = null;
        Header2(sheet, style1, row, cell, conn, value, start, group);

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

        int a=3;
        double c=0,com=0;
        List<GENERIC> list = directory(conn, value, start, group);
        for (int i = 0; i < list.size(); i++) {
            int b=1;
            List<String> list1=Getdata(conn, value, start);
            for (int i1 = 0; i1 < list1.size(); i1++) {
                List<GENERIC> list2=Getvalue(conn, value, start,group,list.get(i).getAA(),list1.get(i1));
                for (int i2 = 0; i2 < list2.size(); i2++) {
                    c = Double.parseDouble(list2.get(i2).getBB());
                    row = sheet.getRow(a);
                    cell = row.createCell(b);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(c);
                    com+=c;
                }
                b++;
            }
            a++;
        }

        int d=a+list.size()-1;
        row = sheet.getRow(d);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(com);


        //源碼
        int f=d+4;
        for (int i = 0; i < list.size(); i++) {
            int b=1;
            List<String> list3=Getdata1(conn, value, start);
            for (int i1 = 0; i1 < list3.size(); i1++) {
                List<GENERIC> list4=Getvalue1(conn, value, start,group,list.get(i).getAA(),list3.get(i1));
                for (GENERIC generic : list4) {
                    row = sheet.getRow(f);
                    cell = row.createCell(b);
                    cell.setCellType(1);
                    cell.setCellStyle(style1);
                    cell.setCellValue(generic.getBB());
                }
                b++;
            }
            f++;
        }
        int g=f+list.size()-1;
        row = sheet.getRow(g);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(com);




        //降嗎
        int e=g+4;
        System.out.println("e:"+e);
        for (int i = 0; i < list.size(); i++) {
            int b=1;
            List<String> list7=Getdata2(value,start,conn);
            for (int i1 = 0; i1 < list7.size(); i1++) {
                List<GENERIC> list8=Getvalue2(conn, value, start,group,list.get(i).getAA());
                for (int j = 0; j <list8.size() ; j++) {
                    if(list7.get(i1).equals(list8.get(j).getAA())){
                        row = sheet.getRow(e);
                        cell = row.createCell(b);
                        cell.setCellType(1);
                        cell.setCellStyle(style1);
                        cell.setCellValue(list8.get(j).getBB());
                        break;
                    }
                }
                b++;
            }
            e++;
        }

        int j=e+list.size()-1;
        row = sheet.getRow(j);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(com);


    }


        private void Header2(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell, Connection conn, String value, String start, String group) {


        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(value);

        row = sheet.createRow(1);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("日期");

        row = sheet.createRow(2);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("TOOLING_SIZE");

        int a=1;
        List<String> list=Getdata(conn, value, start);
        for (int i = 0; i < list.size(); i++) {
            cell = row.createCell(a);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(list.get(i));
            a++;
        }

        int b=3;
        List<GENERIC> list1 = directory(conn, value, start, group);
        for (int i = 0; i < list1.size(); i++) {
            row = sheet.createRow(b);
            row.setHeightInPoints(25);

            cell = row.createCell(0);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(list1.get(i).getAA());
            b++;
        }
        row = sheet.createRow(b+list1.size()-1);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("總計：");




        //源碼
        int c=b+list1.size()+2;
        row = sheet.createRow(c);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("原碼");

        int aa=1;
        List<String> list2=Getdata1(conn, value, start);
        for (String generic : list2) {
            cell = row.createCell(aa);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(generic);
            aa++;
        }

        int bb=8+list1.size();
        for (GENERIC generic : list1) {
            row = sheet.createRow(bb);
            row.setHeightInPoints(25);

            cell = row.createCell(0);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(generic.getAA());
            bb++;
        }
        int cc=bb+list1.size()-1;
        row = sheet.createRow(cc);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("總計：");




        //降嗎
        int dd=cc+3;
        row = sheet.createRow(dd);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("降嗎");

        int ff=1;
        List<String> list3=Getdata2(value,start,conn);
        for (String generic : list3) {
            cell = row.createCell(ff);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(generic);
            ff++;
        }
        int ee=15+list1.size();
        for (GENERIC generic : list1) {
            row = sheet.createRow(ee);
            row.setHeightInPoints(25);

            cell = row.createCell(0);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(generic.getAA());
            ee++;
        }

        row = sheet.createRow(ee+list1.size()-1);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("總計：");
    }

    public List<GENERIC> Getvalue2(Connection conn,String value, String start,String group,String roperties) {
        List<GENERIC> list = new ArrayList<>();
        List<GENERIC> list2 = new ArrayList<>();
        List<GENERIC> list3 = new ArrayList<>();
        PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null;
        ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
        String hql1 = "SELECT IS_DOWN FROM DSID21_OLD WHERE MODEL_NA ='W " + value + "'";
        try {
            ps2 = conn.prepareStatement(hql1);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("IS_DOWN").equals("Y")) {
                    String sql = "SELECT to_number(LEFT_SIZE_RUN)-1.5 AS LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA ='W " + value + "' AND "+group+"='"+roperties+"' GROUP by LEFT_SIZE_RUN";
                    String hql = "SELECT to_number(LEFT_SIZE_RUN) AS LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA = '" + value + "' AND "+group+"='"+roperties+"' GROUP by LEFT_SIZE_RUN";
                    try {
                        ps = conn.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            GENERIC generic = new GENERIC();
                            generic.setAA(rs.getString("LEFT_SIZE_RUN"));
                            generic.setBB(rs.getString("Times"));
                            list.add(generic);
                        }
                        ps.close();
                        ps.close();
                        ps1 = conn.prepareStatement(hql);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            GENERIC generic = new GENERIC();
                            generic.setAA(rs1.getString("LEFT_SIZE_RUN"));
                            generic.setBB(rs1.getString("Times"));
                            list2.add(generic);
                        }
                        ps1.close();
                        ps1.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list2.size(); j++) {
                            if (list.get(i).getAA().equals(list2.get(j).getAA())) {
                                GENERIC generic = new GENERIC();
                                generic.setAA(list.get(i).getAA());
                                int b_d = Integer.valueOf(list.get(i).getBB().toString()).intValue();
                                int b_d1 = Integer.valueOf(list2.get(j).getBB().toString()).intValue();
                                generic.setBB(String.valueOf(b_d + b_d1));
                                list3.add(generic);
                            }
                        }
                    }

                    for (int j = 0; j < list3.size(); j++) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list3.get(j).getAA().equals(list.get(i).getAA())) {
                                list.remove(i);
                            }
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            if (list3.get(j).getAA().equals(list2.get(i).getAA())) {
                                list2.remove(i);
                            }
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        GENERIC generic = new GENERIC();
                        generic.setAA(list.get(i).getAA());
                        generic.setBB(list.get(i).getBB());
                        list3.add(generic);
                    }
                    for (int i = 0; i < list2.size(); i++) {
                        GENERIC generic = new GENERIC();
                        generic.setAA(list2.get(i).getAA());
                        generic.setBB(list2.get(i).getBB());
                        list3.add(generic);
                    }

                } else {
                    String sql = "SELECT LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "' AND "+group+"='"+roperties+"'  GROUP by LEFT_SIZE_RUN";
                    System.out.println("sql走這裡了嘛:" + sql);
                    try {
                        ps3 = conn.prepareStatement(sql);
                        rs3 = ps3.executeQuery();
                        while (rs3.next()) {
                            GENERIC GENER = new GENERIC();
                            GENER.setAA(rs3.getString("LEFT_SIZE_RUN"));
                            GENER.setBB(rs3.getString("Times"));
                            list3.add(GENER);
                        }
                        ps3.close();
                        rs3.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list3;
    }

    private List<String> Getdata2(String value, String start,Connection conn){
        List<GENERIC> list = new ArrayList<>();
        List<GENERIC> list2 = new ArrayList<>();
        List<GENERIC> list3 = new ArrayList<>();
        List<String> list4=new ArrayList<>();
        PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null;
        ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
        String hql1 = "SELECT IS_DOWN FROM DSID21_OLD WHERE MODEL_NA='W " + value + "'";
        try {
            ps2 = conn.prepareStatement(hql1);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("IS_DOWN").equals("Y")) {
                    String sql = "SELECT to_number(LEFT_SIZE_RUN)-1.5 AS LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA ='W " + value + "'  GROUP by LEFT_SIZE_RUN";
                    String hql = "SELECT to_number(LEFT_SIZE_RUN) AS LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA = '" + value + "'  GROUP by LEFT_SIZE_RUN";
                    try {
                        ps = conn.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            GENERIC generic = new GENERIC();
                            generic.setAA(rs.getString("LEFT_SIZE_RUN"));
                            generic.setBB(rs.getString("Times"));
                            list.add(generic);
                        }
                        ps.close();
                        ps.close();
                        ps1 = conn.prepareStatement(hql);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            GENERIC generic = new GENERIC();
                            generic.setAA(rs1.getString("LEFT_SIZE_RUN"));
                            generic.setBB(rs1.getString("Times"));
                            list2.add(generic);
                        }
                        ps1.close();
                        ps1.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list2.size(); j++) {
                            if (list.get(i).getAA().equals(list2.get(j).getAA())) {
                                GENERIC generic = new GENERIC();
                                generic.setAA(list.get(i).getAA());
                                int b_d = Integer.valueOf(list.get(i).getBB().toString()).intValue();
                                int b_d1 = Integer.valueOf(list2.get(j).getBB().toString()).intValue();
                                generic.setBB(String.valueOf(b_d + b_d1));
                                list3.add(generic);
                            }
                        }
                    }

                    for (int j = 0; j < list3.size(); j++) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list3.get(j).getAA().equals(list.get(i).getAA())) {
                                list.remove(i);
                            }
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            if (list3.get(j).getAA().equals(list2.get(i).getAA())) {
                                list2.remove(i);
                            }
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        GENERIC generic = new GENERIC();
                        generic.setAA(list.get(i).getAA());
                        generic.setBB(list.get(i).getBB());
                        list3.add(generic);
                    }
                    for (int i = 0; i < list2.size(); i++) {
                        GENERIC generic = new GENERIC();
                        generic.setAA(list2.get(i).getAA());
                        generic.setBB(list2.get(i).getBB());
                        list3.add(generic);
                    }

                } else {
                    String sql = "SELECT LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "'  GROUP by LEFT_SIZE_RUN";
                    System.out.println("sql走這裡了嘛:" + sql);
                    try {
                        ps3 = conn.prepareStatement(sql);
                        rs3 = ps3.executeQuery();
                        while (rs3.next()) {
                            GENERIC GENER = new GENERIC();
                            GENER.setAA(rs3.getString("LEFT_SIZE_RUN"));
                            GENER.setBB(rs3.getString("Times"));
                            list3.add(GENER);
                        }
                        ps3.close();
                        rs3.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list3.size(); i++) {
            list4.add(list3.get(i).getAA());
        }
        Collections.sort(list4);

        return list4;
    }


    public List Getdata1(Connection conn,String value, String start){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GENERIC> list=new ArrayList<>();
        List<String> list1=new ArrayList<>();
        String sql="SELECT distinct to_number(LEFT_SIZE_RUN) AS LEFT_SIZE_RUN FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"' AND MODEL_NA LIKE '%"+value+"' order by LEFT_SIZE_RUN ";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                GENERIC generic=new GENERIC();
                generic.setAA(rs.getString("LEFT_SIZE_RUN"));
                list.add(generic);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getAA().contains(".")){
                list1.add(list.get(i).getAA());
            }else{
                String b=".0";
                String c= list.get(i).getAA().concat(b);
                list1.add(c);
            }
        }
        return list1;
    }

    public List Getvalue1(Connection conn,String value, String start,String group,String roperties,String tooling){
        List<GENERIC> list=new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="SELECT LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"' AND MODEL_NA LIKE '%"+value+"' AND "+group+"='"+roperties+"'  AND LEFT_SIZE_RUN='"+tooling+"'  GROUP by LEFT_SIZE_RUN";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                GENERIC generic=new GENERIC();
                generic.setAA(rs.getString("LEFT_SIZE_RUN"));
                generic.setBB(rs.getString("Times"));
                list.add(generic);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List Getvalue(Connection conn,String value, String start,String group,String roperties,String tooling){
        List<GENERIC> list=new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="SELECT TOOLING_SIZE,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"' AND MODEL_NA LIKE '%"+value+"' AND "+group+"='"+roperties+"'  AND TOOLING_SIZE='"+tooling+"'  GROUP by TOOLING_SIZE";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                GENERIC generic=new GENERIC();
                generic.setAA(rs.getString("TOOLING_SIZE"));
                generic.setBB(rs.getString("Times"));
                list.add(generic);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List Getdata(Connection conn,String value, String start){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GENERIC> list=new ArrayList<>();
        List<String> list1=new ArrayList<>();
        String sql="SELECT distinct to_number(TOOLING_SIZE)  AS TOOLING_SIZE FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"' AND MODEL_NA LIKE '%"+value+"' order by TOOLING_SIZE ";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                GENERIC generic=new GENERIC();
                generic.setAA(rs.getString("TOOLING_SIZE"));
                list.add(generic);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getAA().contains(".")){
                list1.add(list.get(i).getAA());
            }else{
                String b=".0";
                String c= list.get(i).getAA().concat(b);
                list1.add(c);
            }
        }
        return list1;
    }

    public List directory(Connection conn, String value, String start, String group) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GENERIC> list = new ArrayList<>();
        String sql = "SELECT "+group+" FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='"+start+"' AND MODEL_NA LIKE '%"+value+"'  GROUP by "+group+"";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                GENERIC dsid01 = new GENERIC();
                dsid01.setAA(rs.getString("" + group + ""));
                list.add(dsid01);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void filterHeader1() {
        String name = "鞋帶派工單";
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
        HSSFSheet sheet = wb.createSheet("鞋帶扣派工單");
        SetColumnWidth1(sheet);
        String model_na = cboREP_CNAMEQ.getSelectedItem().getValue();
        String group = YPG_TYPE.getValue().trim();
        Exportexcel1(wb, sheet, model_na, START, group, Conn, conn, style1, style2, style3, style4);

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

    private void filterHeader() {
        String name = "訂單報表拆分";
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
        HSSFSheet sheet = wb.createSheet("鞋帶派工單");
        SetColumnWidth(sheet);
        String model_na = cboREP_CNAMEQ.getSelectedItem().getValue();
        String group = YPG_TYPE.getValue().trim();
        Exportexcel(wb, sheet, model_na, START, group, Conn, conn, style1, style2, style3, style4);

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

    //鞋帶扣派工單
    private void Exportexcel1(HSSFWorkbook wb, HSSFSheet sheet, String value, String start, String group, Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
        System.out.println("形體：" + value);
        System.out.println("時間：" + start);
        System.out.println("GROUP：" + group);


        HSSFRow row = null;
        HSSFCell cell = null;
        Header1(sheet, style1, row, cell, conn, value, start, group);

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

        int num = 3;
        int som = 0;
        List list = Lace(value, start, group, conn);
        for (int i = 0; i < list.size(); i++) {

            row = sheet.createRow(num);
            row.setHeightInPoints(25);

            cell = row.createCell(0);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(list.get(i).toString());

            List list1 = buckle(value, start, group, conn, list.get(i).toString());
            int c = Integer.valueOf(list1.get(0).toString()).intValue();
            cell = row.createCell(1);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(c);
            som += c;

            num++;
        }
        int c = 2;
        int b = c + num;
        row = sheet.getRow(b);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(som);


    }

    private void Header1(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell, Connection conn, String value, String start, String group) {


        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(value);

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
        cell.setCellValue("鞋帶扣顏色");

        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("數量");

        List list = Lace(value, start, group, conn);

        row = sheet.createRow(5 + list.size());
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("總計:");

    }

    //鞋帶派工單
    private void Exportexcel(HSSFWorkbook wb, HSSFSheet sheet, String value, String start, String group, Connection conn, Connection conn1, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {

        PreparedStatement ps = null, ps1 = null, ps2 = null;
        ResultSet rs = null, rs1 = null, rs2 = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        int isB = 0;
        Header(sheet, style1, row, cell, conn, value, start, group);
        List list = Times(value, start, group, conn);
        List list2 = split(value, conn);

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

        int rowNum = 3;
        double Total = 0;
        double Total1 = 0;
        for (int i = 0; i < list.size(); i++) {
            int c = 0;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(25);

            cell = row.createCell(0);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(list.get(i).toString());

            List<GENERIC> list1 = GROUP(value, start, group, list.get(i), conn);
            for (int i2 = 0; i2 < list2.size(); i2++) {
                c++;
                double sum = 0;
                List<Double> data1 = new ArrayList<Double>();
                for (int i1 = 0; i1 < list1.size(); i1++) {
                    double b = Double.parseDouble(list1.get(i1).getAA());
                    double g = Double.parseDouble(list1.get(i1).getBB());
                    String na = list2.get(i2).toString().substring(0, list2.get(i2).toString().length() - 1);
                    String[] bStrings = na.split("-");
                    double parseDouble = Double.parseDouble(bStrings[0]);
                    double parseDouble1 = Double.parseDouble(bStrings[1]);
                    if (b >= parseDouble && b <= parseDouble1) {
                        data1.add(g);
                    }
                }
                for (Double aDouble : data1) {
                    sum += aDouble;
                    Total += aDouble;
                }

                cell = row.createCell(c);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(sum);
            }
            rowNum++;
        }

        row = sheet.getRow(rowNum + 2);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(Total);

        isB = 5 + rowNum;


        //設置表頭的形體   女鞋降嗎

        //接單的日期
        row = sheet.getRow(isB + 1);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(start);
        int rowNum1 = isB + 3;
        for (int i = 0; i < list.size(); i++) {
            int c = 0;
            row = sheet.createRow(rowNum1);
            row.setHeightInPoints(25);

            cell = row.createCell(0);
            cell.setCellType(1);
            cell.setCellStyle(style1);
            cell.setCellValue(list.get(i).toString());

            List<GENERIC> list1 = GROUP1(value, start, group, list.get(i), conn);
            for (int i2 = 0; i2 < list2.size(); i2++) {
                c++;
                double sum = 0;
                List<Double> data1 = new ArrayList<Double>();
                for (int i1 = 0; i1 < list1.size(); i1++) {
                    double b = Double.parseDouble(list1.get(i1).getAA());
                    double g = Double.parseDouble(list1.get(i1).getBB());
                    String na = list2.get(i2).toString().substring(0, list2.get(i2).toString().length() - 1);
                    String[] bStrings = na.split("-");
                    double parseDouble = Double.parseDouble(bStrings[0]);
                    double parseDouble1 = Double.parseDouble(bStrings[1]);
                    if (b >= parseDouble && b <= parseDouble1) {
                        data1.add(g);
                    }
                }
                for (Double aDouble : data1) {
                    sum += aDouble;
                    Total1 += aDouble;
                }

                cell = row.createCell(c);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue(sum);
            }
            rowNum1++;
        }
        int a = rowNum1;
        int b = 2;
        int r = a + b;

        row = sheet.getRow(r);
        cell = row.createCell(1);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(Total1);
    }


    private void Header(HSSFSheet sheet, HSSFCellStyle style1, HSSFRow row, HSSFCell cell, Connection conn, String value, String start, String group) {
        int isA = 0;
        int c = 1;
        int b = 1;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null, rs1 = null;
        List list = Times(value, start, group, conn);

        //設置表頭的形體
        row = sheet.createRow(0);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue(value);

        String sql = "SELECT * FROM DSID01_1 WHERE MODEL_NA='" + value + "'";
        System.out.println("sql1:" + sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
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
                cell.setCellValue("鞋帶顏色");

                for (int a = 1; a <= 10; a++) {
                    if (rs.getString("SECTION" + a) != null) {
                        cell = row.createCell(c);
                        cell.setCellType(1);
                        cell.setCellStyle(style1);
                        cell.setCellValue(rs.getString("SECTION" + a));
                        c++;
                    }
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isA = 5 + list.size();

        row = sheet.createRow(5 + list.size());
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("總計:");


        //設置表頭的形體   女鞋降嗎
        row = sheet.createRow(isA + 3);
        row.setHeightInPoints(30);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("女鞋降嗎");
        try {
            ps1 = conn.prepareStatement(sql);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                row = sheet.createRow(isA + 4);
                row.setHeightInPoints(30);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("派工日期");

                row = sheet.createRow(isA + 5);
                row.setHeightInPoints(30);

                cell = row.createCell(0);
                cell.setCellType(1);
                cell.setCellStyle(style1);
                cell.setCellValue("鞋帶顏色");

                for (int a = 1; a <= 10; a++) {
                    if (rs1.getString("SECTION" + a) != null) {
                        cell = row.createCell(b);
                        cell.setCellType(1);
                        cell.setCellStyle(style1);
                        cell.setCellValue(rs1.getString("SECTION" + a));
                        b++;
                    }
                }
            }
            ps1.close();
            rs1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        row = sheet.createRow(isA + list.size() + 8);
        row.setHeightInPoints(25);

        cell = row.createCell(0);
        cell.setCellType(1);
        cell.setCellStyle(style1);
        cell.setCellValue("總計:");

    }

    //鞋帶扣
    private List<String> Lace(String value, String start, String group, Connection conn) {
        List<String> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT distinct " + group + "  FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "' ";
        System.out.println("sql:" + sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("" + group + "").contains("_")) {
                    list.add(rs.getString("" + group + ""));
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private List buckle(String value, String start, String group, Connection conn, String string) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<>();
        String sql = "SELECT COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "'  AND " + group + "='" + string + "' ";
        System.out.println("sql:" + sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Times"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //碼數
    private List<String> split(String value, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM DSID01_1 WHERE MODEL_NA='" + value + "'";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                for (int a = 1; a <= 10; a++) {
                    if (rs.getString("SECTION" + a + "_NA") != null) {
                        list.add(rs.getString("SECTION" + a + "_NA"));
                    }
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //group
    private List Times(String value, String start, String group, Connection conn) {
        List<String> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT distinct " + group + "  FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "'";
        System.out.println("sql:" + sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if(rs.getString("" + group + "").contains("_")){
                }else{
                    list.add(rs.getString("" + group + ""));
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<GENERIC> GROUP(String value, String start, String group, Object section, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GENERIC> list = new ArrayList<>();
        String sql = "SELECT TOOLING_SIZE,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "' AND " + group + "='" + section + "' GROUP by TOOLING_SIZE";
        System.out.println("sql:" + sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                GENERIC GENER = new GENERIC();
                GENER.setAA(rs.getString("TOOLING_SIZE"));
                GENER.setBB(rs.getString("Times"));
                list.add(GENER);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    private List<GENERIC> GROUP1(String value, String start, String group, Object section, Connection conn) {
        List<GENERIC> list = new ArrayList<>();
        List<GENERIC> list2 = new ArrayList<>();
        List<GENERIC> list3 = new ArrayList<>();
        PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null;
        ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
        String hql1 = "SELECT IS_DOWN FROM DSID21_OLD WHERE MODEL_NA='W " + value + "'";
        try {
            ps2 = conn.prepareStatement(hql1);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("IS_DOWN").equals("Y")) {
                    String sql = "SELECT to_number(LEFT_SIZE_RUN)-1.5 AS LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA ='W " + value + "' AND " + group + "='" + section + "' GROUP by LEFT_SIZE_RUN";
                    String hql = "SELECT to_number(LEFT_SIZE_RUN) AS LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA = '" + value + "' AND " + group + "='" + section + "' GROUP by LEFT_SIZE_RUN";
                    try {
                        ps = conn.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            GENERIC generic = new GENERIC();
                            generic.setAA(rs.getString("LEFT_SIZE_RUN"));
                            generic.setBB(rs.getString("Times"));
                            list.add(generic);
                        }
                        ps.close();
                        ps.close();
                        ps1 = conn.prepareStatement(hql);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                            GENERIC generic = new GENERIC();
                            generic.setAA(rs1.getString("LEFT_SIZE_RUN"));
                            generic.setBB(rs1.getString("Times"));
                            list2.add(generic);
                        }
                        ps1.close();
                        ps1.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list2.size(); j++) {
                            if (list.get(i).getAA().equals(list2.get(j).getAA())) {
                                GENERIC generic = new GENERIC();
                                generic.setAA(list.get(i).getAA());
                                int b_d = Integer.valueOf(list.get(i).getBB().toString()).intValue();
                                int b_d1 = Integer.valueOf(list2.get(j).getBB().toString()).intValue();
                                generic.setBB(String.valueOf(b_d + b_d1));
                                list3.add(generic);
                            }
                        }
                    }

                    for (int j = 0; j < list3.size(); j++) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list3.get(j).getAA().equals(list.get(i).getAA())) {
                                list.remove(i);
                            }
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            if (list3.get(j).getAA().equals(list2.get(i).getAA())) {
                                list2.remove(i);
                            }
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        GENERIC generic = new GENERIC();
                        generic.setAA(list.get(i).getAA());
                        generic.setBB(list.get(i).getBB());
                        list3.add(generic);
                    }
                    for (int i = 0; i < list2.size(); i++) {
                        GENERIC generic = new GENERIC();
                        generic.setAA(list2.get(i).getAA());
                        generic.setBB(list2.get(i).getBB());
                        list3.add(generic);
                    }
                } else {
                    String sql = "SELECT LEFT_SIZE_RUN,COUNT(*) AS Times FROM DSID01 WHERE TO_CHAR(ORDER_DATE,'YYYY/MM/DD')='" + start + "' AND MODEL_NA LIKE '%" + value + "' AND " + group + "='" + section + "' GROUP by LEFT_SIZE_RUN";
                    System.out.println("sql走這裡了嘛:" + sql);
                    try {
                        ps3 = conn.prepareStatement(sql);
                        rs3 = ps3.executeQuery();
                        while (rs3.next()) {
                            GENERIC GENER = new GENERIC();
                            GENER.setAA(rs3.getString("LEFT_SIZE_RUN"));
                            GENER.setBB(rs3.getString("Times"));
                            list3.add(GENER);
                        }
                        ps3.close();
                        rs3.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list3;
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

    //設置表單的長度
    private static void SetColumnWidth(HSSFSheet sheet1) {
        // TODO Auto-generated method stub
        sheet1.setColumnWidth(0, 10 * 256);
        sheet1.setColumnWidth(1, 15 * 256);
        sheet1.setColumnWidth(2, 10 * 256);
        sheet1.setColumnWidth(3, 15 * 256);
        sheet1.setColumnWidth(4, 10 * 256);
        sheet1.setColumnWidth(5, 15 * 256);
        sheet1.setColumnWidth(6, 10 * 256);
        sheet1.setColumnWidth(7, 15 * 256);
        sheet1.setColumnWidth(8, 10 * 256);
        sheet1.setColumnWidth(9, 15 * 256);
        sheet1.setColumnWidth(10, 10 * 256);
        sheet1.setColumnWidth(11, 15 * 256);
        sheet1.setColumnWidth(12, 10 * 256);
        sheet1.setColumnWidth(13, 15 * 256);
        sheet1.setColumnWidth(14, 10 * 256);
        sheet1.setColumnWidth(15, 15 * 256);
        sheet1.setColumnWidth(16, 10 * 256);
        sheet1.setColumnWidth(17, 15 * 256);
        sheet1.setColumnWidth(18, 10 * 256);
        sheet1.setColumnWidth(19, 15 * 256);
    }

    //設置表單的長度
    private static void SetColumnWidth1(HSSFSheet sheet1) {
        // TODO Auto-generated method stub
        sheet1.setColumnWidth(0, 15 * 256);
        sheet1.setColumnWidth(1, 15 * 256);
    }

    private static void SetColumnWidth2(HSSFSheet sheet1) {
        // TODO Auto-generated method stub
        sheet1.setColumnWidth(0, 12 * 256);
        sheet1.setColumnWidth(1, 8 * 256);
        sheet1.setColumnWidth(2, 8 * 256);
        sheet1.setColumnWidth(3, 8 * 256);
        sheet1.setColumnWidth(4, 8 * 256);
        sheet1.setColumnWidth(5, 8 * 256);
        sheet1.setColumnWidth(6, 8 * 256);
        sheet1.setColumnWidth(7, 8 * 256);
        sheet1.setColumnWidth(8, 8 * 256);
        sheet1.setColumnWidth(9, 8 * 256);
        sheet1.setColumnWidth(10, 8 * 256);
        sheet1.setColumnWidth(11, 8 * 256);
        sheet1.setColumnWidth(12, 8 * 256);
        sheet1.setColumnWidth(13, 8 * 256);
        sheet1.setColumnWidth(14, 8 * 256);
        sheet1.setColumnWidth(15, 8 * 256);
        sheet1.setColumnWidth(16, 8 * 256);
        sheet1.setColumnWidth(17, 8 * 256);

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
    protected void init() { }

    @Override
    protected Object doSaveDefault(String columnName) {
        return null;
    }

    @Override
    protected HashMap getReturnMap() {
        return null;
    }
}
