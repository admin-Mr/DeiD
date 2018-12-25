package util;

import java.util.List;
import org.zkoss.zk.ui.Component;

public class ComponentColumn<T> {
	Component component;
	String componentName;
	String columnName;
	String realColumnName;
	T defaultValue;
	List<EntityCheck> arrChecks;
	List<String> arrCheckMessages;
	boolean bPK;
	//Class classEntity;
	
	public ComponentColumn(Component component, String columnName, T defaultValue, List<EntityCheck> arrChecks, List<String> arrCheckMessages){
		super();
		this.component = component;
		this.columnName = columnName;
		this.defaultValue = defaultValue;
		this.arrChecks = arrChecks;
		this.arrCheckMessages = arrCheckMessages;
		this.bPK = false;
		this.componentName = null;
	}
	
	public ComponentColumn(Component component, String columnName, String realColumnName, T defaultValue, List<EntityCheck> arrChecks, List<String> arrCheckMessages){
		super();
		this.component = component;
		this.columnName = columnName;
		this.realColumnName = realColumnName;
		this.defaultValue = defaultValue;
		this.arrChecks = arrChecks;
		this.arrCheckMessages = arrCheckMessages;
		this.bPK = false;
		this.componentName = null;
	}


	public ComponentColumn(String componentName, String columnName, T defaultValue, List<EntityCheck> arrChecks, List<String> arrCheckMessages, boolean bISPK) {
		super();
		this.componentName = componentName;
		this.columnName = columnName;
		this.defaultValue = defaultValue;
		this.arrChecks = arrChecks;
		this.arrCheckMessages = arrCheckMessages;
		this.bPK = bISPK;
		this.component = null;
	}

	public ComponentColumn(String componentName, String columnName, String realColumnName, T defaultValue, List<EntityCheck> arrChecks, List<String> arrCheckMessages, boolean bISPK) {
		super();
		this.componentName = componentName;
		this.columnName = columnName;
		this.realColumnName = realColumnName;
		this.defaultValue = defaultValue;
		this.arrChecks = arrChecks;
		this.arrCheckMessages = arrCheckMessages;
		this.bPK = bISPK;
		this.component = null;
	}	
	
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public List<EntityCheck> getArrChecks() {
		return arrChecks;
	}

	public void setArrChecks(List<EntityCheck> arrChecks) {
		this.arrChecks = arrChecks;
	}

	public List<String> getArrCheckMessages() {
		return arrCheckMessages;
	}

	public void setArrCheckMessages(List<String> arrCheckMessages) {
		this.arrCheckMessages = arrCheckMessages;
	}

	public String getColumnName() {
		return columnName;
	}
		
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}		
	
	public T getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public boolean getPK(){
		return bPK;
	}
	
	public void setPK(boolean bPK){
		this.bPK = bPK;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public void setComponentName(String ComponentName) {
		this.componentName = ComponentName;
	}

	public String getRealColumnName() {
		return realColumnName;
	}
		
	public void setRealColumnName(String realColumnName) {
		this.realColumnName = realColumnName;
	}	

}
