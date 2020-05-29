package ds.dsid.program;

import ds.dsid.domain.GENERIC;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;
import util.Common;
import util.QueryWindow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DSID13MQryHURDLES extends QueryWindow {

    @Wire
    private Window windowQuery;
    @Wire
    private Button btnSearch, btnConfirmN, btnCancel;
    @Wire
    private Combobox cboColumn, cboCondition;
    @Wire
    private Textbox txtQuery;
    private String MODEL_NAS="";

    @Override
    public void doAfterCompose(Component Window) throws Exception {
        super.doAfterCompose(Window);
        Execution exec = Executions.getCurrent();
        MODEL_NAS=(String) exec.getArg().get("NIKE_SH_ARITCLE");
        doSearch();
        System.out.println(MODEL_NAS);
    }

    @Override
    protected Window getRootWindow() {
        return windowQuery;
    }

    @Override
    protected String getEntityName() { return null; }

    @Override
    protected List<String> setComboboxColumn() {
        List<String> listColumn = new ArrayList<String>();
        listColumn.add(Labels.getLabel("Operation.HURDLES"));
        return listColumn;
    }

    @Override
    protected List<String> setComboboxCondition() {
        List<String> listCondition = new ArrayList<String>();
        listCondition.add("=");
        return listCondition;
    }


    @Override
    protected String getSQLWhere() {
        String Sql="";
        return Sql;
    }

    @Override
    protected String getCustomSQL() {
        return null;
    }

    @Override
    protected String getCustomCountSQL() {
        return null;
    }

    @Override
    protected String getPagingId() {
        return "pagingOperation";
    }

    @Override
    protected int getPageSize() { return 0; }

    @Override
    protected String getTextBoxValue() { return txtQuery.getValue(); }

    @Override
    protected Combobox getcboCondition() {
        return cboCondition;
    }

    @Override
    protected Combobox getcboColumn() {
        return cboColumn;
    }

    @Override
    protected String getOrderby() {
        return null;
    }

    @Override
    protected HashMap getMap() {
        return null;
    }

    @Override
    protected List getCustList() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFillListbox(int index)  {

        List<GENERIC> list = new ArrayList<GENERIC>();
        Connection DB00 = Common.getDbConnection();
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;

        String sql = "SELECT * FROM DSID10SE WHERE MODEL_NAS LIKE '%"+MODEL_NAS+"%'";
        System.out.println(sql);

        try {
            ps1 = DB00.prepareStatement(sql);
            rs1 = ps1.executeQuery();
            while (rs1.next()){
                for(int i=1;i<13;i++) {
                    GENERIC GENER=new GENERIC();
                    GENER.setAA("SH_ST" + i);
                    GENER.setBB(rs1.getString("SH_ST" + i));
                    list.add(GENER);
                }
            }
            ps1.close();
            rs1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Common.closeConnection(DB00);
        ListModelList queryMod = new ListModelList(list, true);
        queryListBox.invalidate();
        queryListBox.setModel(queryMod);
        queryMod.setMultiple(true);
    }

    @SuppressWarnings("unchecked")
    @Listen("onClick = #btnConfirmN")
    public void onClickbtnConfirmN(Event event) {

        ArrayList<Object> arrList = new ArrayList<Object>();
        ArrayList<Object> arrList2 = new ArrayList<Object>();
        arrList.clear();
        String  HURDLES="",HURDLESS="";
        try{
            if (queryListBox.getSelectedItem() == null){
                Messagebox.show(Labels.getLabel("PUBLIC.MSG0060"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }else{

                for(Listitem ltAll : queryListBox.getItems()){
                    if (ltAll.isSelected()){
                        arrList.add((Object)ltAll.getValue());
                    }
                }
                for(int i=0; i<arrList.size(); i++){
                    HURDLES=((GENERIC)arrList.get(i)).getAA();
                    HURDLESS+=HURDLES+",";

                }
            }
        }catch (NullPointerException e){
            e.getStackTrace();
        }
        if(HURDLESS.length()>0){
            HURDLESS=HURDLESS.substring(0, HURDLESS.length()-1);
        }
        arrList2.add(HURDLESS);
        Events.sendEvent(new Event(returnMethodName, parentWindow, arrList2));
        getRootWindow().detach();

    }
}
