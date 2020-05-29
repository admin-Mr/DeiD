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

public class DSID01QryRemitB extends QueryWindow {

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
    }

    @Override
    protected Window getRootWindow() {
        return windowQuery;
    }

    @Override
    protected List<String> setComboboxColumn() {
        List<String> listColumn = new ArrayList<String>();
        listColumn.add(Labels.getLabel("Operation.GROUP_NO"));
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getCustomCountSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getEntityName() {
        return null;
    }

    @Override
    protected String getTextBoxValue() { return txtQuery.getValue(); }

    @Override
    protected String getPagingId() {
        return "pagingOperation";
    }

    @Override
    protected Combobox getcboColumn() {
        return cboColumn;
    }

    @Override
    protected Combobox getcboCondition() {
        return cboCondition;
    }

    @Override
    protected HashMap getMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getOrderby() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected int getPageSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected List getCustList() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFillListbox(int index) {

        List<GENERIC> list = new ArrayList<GENERIC>();
        Connection DB00 = Common.getDbConnection();
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;

        String sql = "SELECT * FROM DSID10 WHERE MODEL_NAS LIKE '%"+MODEL_NAS+"%'";

        try {
            ps1 = DB00.prepareStatement(sql);
            rs1 = ps1.executeQuery();
            while(rs1.next()){

                for(int i=1;i<21;i++){
                    if("Y".equals(rs1.getString("GROUP"+i+"_STATUS"))){
                        GENERIC GENER=new GENERIC();
                        GENER.setAA("GROUP"+i);
                        GENER.setBB(rs1.getString("GROUP"+i+"_NA"));
                        list.add(GENER);
                    }
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
        String  GROUP_NO="",GROUP_NOS="";
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
                    GROUP_NO=((GENERIC)arrList.get(i)).getAA();
                    GROUP_NOS+=GROUP_NO+",";

                }
            }
        }catch (NullPointerException e){
            e.getStackTrace();
        }
        if(GROUP_NOS.length()>0){
            GROUP_NOS=GROUP_NOS.substring(0, GROUP_NOS.length()-1);
        }
        arrList2.add(GROUP_NOS);
        Events.sendEvent(new Event(returnMethodName, parentWindow, arrList2));
        getRootWindow().detach();

    }

}
