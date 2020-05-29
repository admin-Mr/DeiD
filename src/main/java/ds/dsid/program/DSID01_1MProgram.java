package ds.dsid.program;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ds.dsid.domain.DSID01_1;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.ComponentColumn;
import util.Detail;
import util.Master;
import util.OperationMode;


public class DSID01_1MProgram extends Master {

    @Wire
    private Window windowMaster;
    @Wire
    private Button btnSaveMaster, btnCancelMaster,btnExport,btnExport1, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;
    @Wire
    private Textbox TMODEL_NONA,TMODEL_NA,TSECTION1,TSECTION2,TSECTION3,TSECTION4,TSECTION5,TSECTION6,TSECTION7,TSECTION8,TSECTION9,TSECTION10,
                    TSECTION1_NA,TSECTION2_NA,TSECTION3_NA,TSECTION4_NA,TSECTION5_NA,TSECTION6_NA,TSECTION7_NA,TSECTION8_NA,TSECTION9_NA,TSECTION10_NA;

    @Override
    public void doAfterCompose(Component window) throws Exception{
        super.doAfterCompose(window);
        setWhereConditionals(getWhereConditionals());
        masterComponentColumns.add(new ComponentColumn<String>("TMODEL_NA", "MODEL_NA", null, null, null,false));

        masterComponentColumns.add(new ComponentColumn<String>("TSECTION1", "SECTION1", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION2", "SECTION2", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION3", "SECTION3", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION4", "SECTION4", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION5", "SECTION5", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION6", "SECTION6", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION7", "SECTION7", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION8", "SECTION8", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION9", "SECTION9", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION10", "SECTION10", null, null, null,false));

        masterComponentColumns.add(new ComponentColumn<String>("TSECTION1_NA", "SECTION1_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION2_NA", "SECTION2_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION3_NA", "SECTION3_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION4_NA", "SECTION4_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION5_NA", "SECTION5_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION6_NA", "SECTION6_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION7_NA", "SECTION7_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION8_NA", "SECTION8_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION9_NA", "SECTION9_NA", null, null, null,false));
        masterComponentColumns.add(new ComponentColumn<String>("TSECTION10_NA", "SECTION10_NA", null, null, null,false));

    }

    @Listen("onClick =#btnExport")
    public void onClickbtnExport(Event event) throws Exception{
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentWindow", windowMaster);
        map.put("DSID01_1MProgram", this);
        Executions.createComponents("/ds/dsid/DSID01_1MExportA.zul", null, map);
    }

    @Listen("onClick =#btnExport1")
    public void onClickbtnExport1(Event event) throws Exception{
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentWindow", windowMaster);
        map.put("DSID01_1MProgram", this);
        Executions.createComponents("/ds/dsid/DSID01_1MExportB.zul", null, map);
    }

    @Override
    protected Window getRootWindow() {
        // TODO Auto-generated method stub
        return windowMaster;
    }

    @Override
    protected Class getMasterClass() {
        // TODO Auto-generated method stub
        return DSID01_1MProgram.class;
    }

    @Override
    protected Class getEntityClass() {
        // TODO Auto-generated method stub
        return DSID01_1.class;
    }

    @Override
    protected OperationMode getOperationMode() {
        // TODO Auto-generated method stub
        return OperationMode.NORMAL;
    }


    @Override
    protected void addDetailPrograms() {
        // TODO Auto-generated method stub
    }

    @Override
    protected ArrayList<String> getMasterKeyName() {
        ArrayList<String> masterKey = new ArrayList<String>();
        masterKey.add("MODEL_NA");
        return masterKey;
    }

    @Override
    protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
        DSID01_1 entity = (DSID01_1) entityMaster;
        ArrayList<String> masterKeyValue = new ArrayList<String>();
        masterKeyValue.add(entity.getMODEL_NA());
        return masterKeyValue;
    }

    @Override
    protected String getPagingId() {

        return "pagingCourseqq";
    }

    @Override
    protected String getWhereConditionals() {
        // TODO Auto-generated method stub
        String sql="";
       if(!TMODEL_NONA.getValue().isEmpty()){
            sql+=" AND MODEL_NA='"+TMODEL_NONA.getValue()+"'";
        }
        return sql;
    }

    @Override
    protected void resetEditAreaMaster(Object entityMaster) {
		System.out.println("=========onclick=Editor===========");
        DSID01_1 entity = (DSID01_1) entityMaster;
        TMODEL_NA.setValue(entity == null ? "" : entity.getMODEL_NA());

        TSECTION1_NA.setValue(entity == null ? "" : entity.getSECTION1_NA());
        TSECTION2_NA.setValue(entity == null ? "" : entity.getSECTION2_NA());
        TSECTION3_NA.setValue(entity == null ? "" : entity.getSECTION3_NA());
        TSECTION4_NA.setValue(entity == null ? "" : entity.getSECTION4_NA());
        TSECTION5_NA.setValue(entity == null ? "" : entity.getSECTION5_NA());
        TSECTION6_NA.setValue(entity == null ? "" : entity.getSECTION6_NA());
        TSECTION7_NA.setValue(entity == null ? "" : entity.getSECTION7_NA());
        TSECTION8_NA.setValue(entity == null ? "" : entity.getSECTION8_NA());
        TSECTION9_NA.setValue(entity == null ? "" : entity.getSECTION9_NA());
        TSECTION10_NA.setValue(entity == null ? "" : entity.getSECTION10_NA());

        TSECTION1.setValue(entity == null ? "" : entity.getSECTION1());
        TSECTION2.setValue(entity == null ? "" : entity.getSECTION2());
        TSECTION3.setValue(entity == null ? "" : entity.getSECTION3());
        TSECTION4.setValue(entity == null ? "" : entity.getSECTION4());
        TSECTION5.setValue(entity == null ? "" : entity.getSECTION5());
        TSECTION6.setValue(entity == null ? "" : entity.getSECTION6());
        TSECTION7.setValue(entity == null ? "" : entity.getSECTION7());
        TSECTION8.setValue(entity == null ? "" : entity.getSECTION8());
        TSECTION9.setValue(entity == null ? "" : entity.getSECTION9());
        TSECTION10.setValue(entity == null ? "" : entity.getSECTION10());




    }
    @Override
    protected String getDetailConditionals() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getMasterCreateZul() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getMasterUpdateZul() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getMasterQueryZul() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected HashMap getMasterCreateMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean beforeMasterSave(Object entityMaster) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected boolean beforeMasterDel(Object entityMaster) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected void doCreateDefault() {
    }

    @Override
    protected Object doSaveDefault(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getEntityName() {
        // TODO Auto-generated method stub
        return "DSID01_1";
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

    @Override
    public void masterReadMode(HashMap<String, Object> mapButton){
        mapButton = new HashMap<String, Object>();
        mapButton.put("btncreatemaster", btnCreateMaster);
        mapButton.put("btnquery", btnQuery);
        super.masterReadMode(mapButton);
        btnSaveMaster.setDisabled(true);
        btnCancelMaster.setDisabled(true);
        btnEditMaster.setDisabled(false);
        btnDeleteMaster.setDisabled(false);

        TMODEL_NA.setReadonly(true);

        TSECTION1_NA.setReadonly(true);
        TSECTION2_NA.setReadonly(true);
        TSECTION3_NA.setReadonly(true);
        TSECTION4_NA.setReadonly(true);
        TSECTION5_NA.setReadonly(true);
        TSECTION6_NA.setReadonly(true);
        TSECTION7_NA.setReadonly(true);
        TSECTION8_NA.setReadonly(true);
        TSECTION9_NA.setReadonly(true);
        TSECTION10_NA.setReadonly(true);

        TSECTION1.setReadonly(true);
        TSECTION2.setReadonly(true);
        TSECTION3.setReadonly(true);
        TSECTION4.setReadonly(true);
        TSECTION5.setReadonly(true);
        TSECTION6.setReadonly(true);
        TSECTION7.setReadonly(true);
        TSECTION8.setReadonly(true);
        TSECTION9.setReadonly(true);
        TSECTION10.setReadonly(true);

    }
    @Override
    public void masterCreateMode(HashMap<String, Object> mapButton){
        mapButton = new HashMap<String, Object>();
        mapButton.put("btncreatemaster", btnCreateMaster);
        mapButton.put("btnquery", btnQuery);
        super.masterCreateMode(mapButton);
        btnSaveMaster.setDisabled(false);
        btnCancelMaster.setDisabled(false);
        btnEditMaster.setDisabled(true);
        btnDeleteMaster.setDisabled(true);

        TMODEL_NA.setReadonly(false);
        TSECTION1_NA.setReadonly(false);
        TSECTION2_NA.setReadonly(false);
        TSECTION3_NA.setReadonly(false);
        TSECTION4_NA.setReadonly(false);
        TSECTION5_NA.setReadonly(false);
        TSECTION6_NA.setReadonly(false);
        TSECTION7_NA.setReadonly(false);
        TSECTION8_NA.setReadonly(false);
        TSECTION9_NA.setReadonly(false);
        TSECTION10_NA.setReadonly(false);

        TSECTION1.setReadonly(false);
        TSECTION2.setReadonly(false);
        TSECTION3.setReadonly(false);
        TSECTION4.setReadonly(false);
        TSECTION5.setReadonly(false);
        TSECTION6.setReadonly(false);
        TSECTION7.setReadonly(false);
        TSECTION8.setReadonly(false);
        TSECTION9.setReadonly(false);
        TSECTION10.setReadonly(false);
    }
}
