package ds.dsid.program;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;
import util.BlackBox;
import util.MSMode;
import util.OpenWinCRUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DSID01Remit extends OpenWinCRUD {

    @Wire
    private Combobox cboREP_CNAMEQ;
    @Wire
    private Window windowMaster;
    @Wire
    private Datebox po_date1;
    @Wire
    private Button btnExport,btnQryTGROUP_NO,btnQryTGROUP_NO1;
    @Wire
    private Textbox txtMODEL_NA,TGROUP_NO1,TGROUP_NO;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");

    @SuppressWarnings({ "unchecked", "rawtypes" })
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

    @Listen("onClick = #btnQryTGROUP_NO")
    public void onClickbtnQryHURDLES(Event event){
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentWindow", windowMaster);
        map.put("returnMethod", "onQryTGROUP_NO");
        map.put("multiple", true);
        map.put("NIKE_SH_ARITCLE", cboREP_CNAMEQ.getSelectedItem().getValue());
        Executions.createComponents("/ds/dsid/DSID01QryRemitA.zul", null, map);
    }


    @Listen("onClick = #btnQryTGROUP_NO1")
    public void onClickbtnQryHURDLES1(Event event){
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentWindow", windowMaster);
        map.put("returnMethod", "onQryTGROUP_NO1");
        map.put("multiple", true);
        map.put("NIKE_SH_ARITCLE", cboREP_CNAMEQ.getSelectedItem().getValue());
        Executions.createComponents("/ds/dsid/DSID01QryRemitB.zul", null, map);
    }

    //導出按鈕，根據日期來判斷形體獲取值
    @Listen("onClick =#btnExport")
    public void onClickbtnexport(Event event) throws Exception {
        Date date = po_date1.getValue();
        String MODEL_NA = "";
        String GROUP_NO = "";
        String GROUP_NO1 = "";
        String CONDITION = "";

        if (TGROUP_NO1.getValue().length()>0 && txtMODEL_NA.getValue().length()>0) {
            GROUP_NO1 = TGROUP_NO1.getValue();
            CONDITION = txtMODEL_NA.getValue();
            if (cboREP_CNAMEQ.getSelectedItem().getValue() != null | TGROUP_NO.getValue() != null
                    | (!"".equals(date) && date != null)) {

                String START = sdf.format(po_date1.getValue());
                MODEL_NA = cboREP_CNAMEQ.getSelectedItem().getValue();
                GROUP_NO = TGROUP_NO.getValue();
                DSID01RemitTask.ExcelExport1(MODEL_NA, START, GROUP_NO, GROUP_NO1, CONDITION);
            } else {
                Messagebox.show("日期、形體和選擇GROUP_NO都不能為空!");
            }
        } else if (cboREP_CNAMEQ.getSelectedItem().getValue() != null | TGROUP_NO.getValue() != null
                | (!"".equals(date) && date != null)) {

            MODEL_NA = cboREP_CNAMEQ.getSelectedItem().getValue();
            GROUP_NO = TGROUP_NO.getValue();
            String START = sdf.format(po_date1.getValue());
            DSID01RemitTask.ExcelExport(MODEL_NA, START, GROUP_NO);
        } else {
            Messagebox.show("日期、形體和選擇GROUP_NO都不能為空!");
        }

    }

    @SuppressWarnings("unchecked")
    @Listen("onQryTGROUP_NO = #windowMaster")
    public void onQryGROUP_NO(Event event){
        ArrayList<Object> arrList = (ArrayList<Object>) event.getData();
        System.err.println(">>>map "+arrList);
        TGROUP_NO.setValue(arrList.get(0).toString());
    }

    @SuppressWarnings("unchecked")
    @Listen("onQryTGROUP_NO1 = #windowMaster")
    public void onQryGROUP_NO1(Event event){
        ArrayList<Object> arrList = (ArrayList<Object>) event.getData();
        System.err.println(">>>map "+arrList);
        TGROUP_NO1.setValue(arrList.get(0).toString());
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
