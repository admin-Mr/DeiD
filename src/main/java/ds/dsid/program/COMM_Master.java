package ds.dsid.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

import util.ComponentColumn;
import util.Detail;
import util.EntityCheck;
import util.Master;
import util.OperationMode;

/**
 * 
 * @author 038634
 *
 *   實現Master的抽象類，
 *   注意點返回方法為布爾值的必須要返回True才能保存成功(需要看情況處理)
 */
@SuppressWarnings("serial")
public class COMM_Master extends Master{

	@Override
	protected Window getRootWindow() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMasterClass() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getEntityClass() {
		return null;
	}

	@Override
	protected OperationMode getOperationMode() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getDetailClass(int indexDetail) {
		return null;
	}

	@Override
	protected Detail getDetailProgram(int indexDetail) {
		return null;
	}

	@Override
	protected void addDetailPrograms() {
		
	}

	@Override
	protected ArrayList<String> getMasterKeyName() {
		return null;
	}

	@Override
	protected ArrayList<String> getMasterKeyValue(Object entityMaster) {
		return null;
	}

	@Override
	protected String getPagingId() {
		return null;
	}

	@Override
	protected String getWhereConditionals() {
		return null;
	}

	@Override
	protected String getDetailConditionals() {
		return null;
	}

	@Override
	protected String getMasterCreateZul() {
		return null;
	}

	@Override
	protected String getMasterUpdateZul() {
		return null;
	}

	@Override
	protected String getMasterQueryZul() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected HashMap getMasterCreateMap() {
		return null;
	}

	@Override
	protected void resetEditAreaMaster(Object entityMaster) {
		
	}

	@Override
	protected boolean beforeMasterSave(Object entityMaster) {
		return true;
	}

	@Override
	protected boolean beforeMasterDel(Object entityMaster) {
		return true;
	}

	@Override
	protected void doCreateDefault() {
	}

	@Override
	protected Object doSaveDefault(String columnName) {
		return null;
	}

	@Override
	protected String getEntityName() {
		return null;
	}

	@Override
	protected int getPageSize() {
		return 0;
	}

	/**
	 * 此方法必須要返回TRUE才能保存成功
	 */
	@Override
	protected boolean doCustomSave() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected List getCustList() {
		return null;
	}
	/**
	 * 
	 * @param component        輸入框控件
	 * @param columnName       綁定實體類對應的字段名稱
	 * @param defaultValue     綁定實體類對應的字段的值
	 * @param arrChecks        未知待考核
	 * @param arrCheckMessages 未知待考核
	 */
	protected <T>void bindComponent(Component component, String columnName, T defaultValue, List<EntityCheck> arrChecks, List<String> arrCheckMessages){
		masterComponentColumns.add(new ComponentColumn<T>(component, columnName,defaultValue, arrChecks, arrCheckMessages));
	}
}
