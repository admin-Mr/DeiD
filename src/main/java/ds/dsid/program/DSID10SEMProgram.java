package ds.dsid.program;

import ds.dsid.domain.DSID01_1;
import ds.dsid.domain.DSID10SE;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import util.ComponentColumn;
import util.Master;
import util.OperationMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DSID10SEMProgram extends Master {
    @Wire
    private Window windowMaster;
    @Wire
    private Button btnSaveMaster, btnCancelMaster, btnExport, btnExport1, btnCreateMaster, btnQuery, btnEditMaster, btnDeleteMaster;
    @Wire
    private Textbox TNIKE_SH_ARITCLE, TMODEL_NA, TSH_ST1, TSH_ST2, TSH_ST3, TSH_ST4, TSH_ST5, TSH_ST6, TSH_ST7, TSH_ST8, TSH_ST9, TSH_ST10, TSH_ST11, TSH_ST12, TMODEL_NA1;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        setWhereConditionals(getWhereConditionals());
        masterComponentColumns.add(new ComponentColumn<String>("TMODEL_NA", "NIKE_SH_ARITCLE", null, null, null, false));

        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST1", "SH_ST1", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST2", "SH_ST2", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST3", "SH_ST3", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST4", "SH_ST4", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST5", "SH_ST5", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST6", "SH_ST6", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST7", "SH_ST7", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST8", "SH_ST8", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST9", "SH_ST9", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST10", "SH_ST10", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST11", "SH_ST11", null, null, null, false));
        masterComponentColumns.add(new ComponentColumn<String>("TSH_ST12", "SH_ST12", null, null, null, false));

        masterComponentColumns.add(new ComponentColumn<String>("TMODEL_NA1", "MODEL_NAS", null, null, null, false));

    }

    @Override
    protected Window getRootWindow() {
        // TODO Auto-generated method stub
        return windowMaster;
    }

    @Override
    protected Class getMasterClass() {
        // TODO Auto-generated method stub
        return DSID10SEMProgram.class;
    }

    @Override
    protected Class getEntityClass() {
        // TODO Auto-generated method stub

        return DSID10SE.class;
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
        masterKey.add("NIKE_SH_ARITCLE");
        return masterKey;
    }

    @Override
    protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
        DSID10SE entity = (DSID10SE) entityMaster;
        ArrayList<String> masterKeyValue = new ArrayList<String>();
        masterKeyValue.add(entity.getNIKE_SH_ARITCLE());
        return masterKeyValue;
    }

    @Override
    protected String getPagingId() {

        return "pagingCourseqq";
    }

    @Override
    protected String getWhereConditionals() {
        // TODO Auto-generated method stub
        String sql = "";
        if (!TNIKE_SH_ARITCLE.getValue().isEmpty()) {
            sql += " AND NIKE_SH_ARITCLE='" + TNIKE_SH_ARITCLE.getValue() + "'";
        }
        return sql;
    }

    @Override
    protected void resetEditAreaMaster(Object entityMaster) {
        System.out.println("=========onclick=Editor===========");
        DSID10SE entity = (DSID10SE) entityMaster;
        TMODEL_NA.setValue(entity == null ? "" : entity.getNIKE_SH_ARITCLE());

        TSH_ST1.setValue(entity == null ? "" : entity.getSH_ST1());
        TSH_ST2.setValue(entity == null ? "" : entity.getSH_ST2());
        TSH_ST3.setValue(entity == null ? "" : entity.getSH_ST3());
        TSH_ST4.setValue(entity == null ? "" : entity.getSH_ST4());
        TSH_ST5.setValue(entity == null ? "" : entity.getSH_ST5());
        TSH_ST6.setValue(entity == null ? "" : entity.getSH_ST6());
        TSH_ST7.setValue(entity == null ? "" : entity.getSH_ST8());
        TSH_ST8.setValue(entity == null ? "" : entity.getSH_ST7());
        TSH_ST9.setValue(entity == null ? "" : entity.getSH_ST9());
        TSH_ST10.setValue(entity == null ? "" : entity.getSH_ST10());
        TSH_ST11.setValue(entity == null ? "" : entity.getSH_ST11());
        TSH_ST12.setValue(entity == null ? "" : entity.getSH_ST12());

        TMODEL_NA1.setValue(entity == null ? "" : entity.getMODEL_NAS());


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
        return "DSID10SE";
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
    public void masterReadMode(HashMap<String, Object> mapButton) {
        mapButton = new HashMap<String, Object>();
        mapButton.put("btncreatemaster", btnCreateMaster);
        mapButton.put("btnquery", btnQuery);
        super.masterReadMode(mapButton);
        btnSaveMaster.setDisabled(true);
        btnCancelMaster.setDisabled(true);
        btnEditMaster.setDisabled(false);
        btnDeleteMaster.setDisabled(false);

        TMODEL_NA.setReadonly(true);

        TSH_ST1.setReadonly(true);
        TSH_ST2.setReadonly(true);
        TSH_ST3.setReadonly(true);
        TSH_ST4.setReadonly(true);
        TSH_ST5.setReadonly(true);
        TSH_ST6.setReadonly(true);
        TSH_ST7.setReadonly(true);
        TSH_ST8.setReadonly(true);
        TSH_ST9.setReadonly(true);
        TSH_ST10.setReadonly(true);
        TSH_ST11.setReadonly(true);
        TSH_ST12.setReadonly(true);
        TMODEL_NA1.setReadonly(true);

    }

    @Override
    public void masterCreateMode(HashMap<String, Object> mapButton) {
        mapButton = new HashMap<String, Object>();
        mapButton.put("btncreatemaster", btnCreateMaster);
        mapButton.put("btnquery", btnQuery);
        super.masterCreateMode(mapButton);
        btnSaveMaster.setDisabled(false);
        btnCancelMaster.setDisabled(false);
        btnEditMaster.setDisabled(true);
        btnDeleteMaster.setDisabled(true);

        TMODEL_NA.setReadonly(false);

        TSH_ST1.setReadonly(false);
        TSH_ST2.setReadonly(false);
        TSH_ST3.setReadonly(false);
        TSH_ST4.setReadonly(false);
        TSH_ST5.setReadonly(false);
        TSH_ST6.setReadonly(false);
        TSH_ST7.setReadonly(false);
        TSH_ST8.setReadonly(false);
        TSH_ST9.setReadonly(false);
        TSH_ST10.setReadonly(false);
        TSH_ST11.setReadonly(false);
        TSH_ST12.setReadonly(false);
        TMODEL_NA1.setReadonly(false);
    }
}
