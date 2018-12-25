package util.component;

import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.annotation.Command;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import util.BlackBox;
import util.DataMode;
import util.XmlDAO;
import util.openwin.QryField;
import util.openwin.ReturnBox;

public class dscQueryField extends Div {
	
	private Window windowMaster;
	private String xmlID;
	private Button queryButton;
	private Button clearButton;
	private Textbox valueTextbox;
	private Textbox displayTextbox,displayValueTextbox;
	private Label displayLabel;
	private dscQueryField dscQueryField;
	private Doublebox valueDoublebox;
	private String format;
	protected String marginStyle="margin-right:4px;";
	private  HashMap<String, Object> setupFilter;
	private String parentWindow,WindoWidth,WindoHeight,xmlQryID,PageSize,returnField,displayField,returnMethod,CustomCondition,BaseCondition,returnComponent="";
	private Boolean Sizable,Closable,multiple,Maximizable,Disabled=true,multiLanguage=false,Readonly=true;
	private String Mode="0";
	public static Object object=new Object();
	private Window getWindowMaster(){
		return this.windowMaster;
	}
	
	/**
	 * 取得returnField的元件型態
	 * 
	 * @return
	 */
	public String getReturnComponent() {
		return returnComponent;
	}
	
	/**
	 * 取得自定過濾條件
	 * 
	 * @return
	 */
	public String getCustomCondition() {
		return CustomCondition;
	}

	/**
	 * 新增過濾條件
	 * "AND EXISTS(SELECT * FROM XXX WHERE ID=T.ID)"
	 * 一次性，每次開窗前需在設定一次
	 * @param condition
	 */
	public void setCustomCondition(String condition) {
		this.CustomCondition = condition;
	}
	
	/**
	 * 取得基本過濾條件
	 * @return
	 */
	public String getBaseCondition() {
		return BaseCondition;
	}

	/**
	 * 取代xml原本設定BaseCondition
	 * "ID='xxx' AND NAME='XXX'"
	 * 開頭不需加 AND 或 OR
	 * @param condition
	 */
	public void setBaseCondition(String baseCondition) {
		this.BaseCondition = baseCondition;
	}


	public String getReturnMethod() {
		return returnMethod;
	}

	public void setReturnMethod(String returnMethod) {
		this.returnMethod = returnMethod;
	}

	public void setWindow(Window win){
		this.windowMaster=win;
	}
	public void setXmlID(String xml){
		xmlID=xml;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		valueDoublebox.setFormat(format);
		valueDoublebox.setValue(123456);
		this.format = format;
	}

	public Boolean isMultiLanguage() {
		return this.multiLanguage;	
	}
	public void setMultiLanguage(Boolean multiLanguage) {
		this.multiLanguage = multiLanguage;
		doMultiLanguage();
	}
	public String getDisplayValue(){
		return displayValueTextbox.getValue();
	}
	
	/*public void setDisplayValue(Double doubleValue){
		valueDoublebox.setValue(doubleValue);
	}*/
	
	public void setDisplayValue(String value){
		/*if(Sessions.getCurrent().getAttribute("DataMode")==DataMode.CREATE_MODE || Sessions.getCurrent().getAttribute("DataMode")==DataMode.UPDATE_MODE){
			queryButton.setDisabled(false);
			clearButton.setDisabled(false);
		}else{
			queryButton.setDisabled(true);
			clearButton.setDisabled(true);
		}*/
		if(Mode.equals("0")){
			displayLabel.setValue(value);
			Events.postEvent("onChange", displayLabel, null);
		}else if(Mode.equals("1")){
			displayValueTextbox.setText(value);
			Events.postEvent("onChange", displayValueTextbox, null);
		}
		
	}
	public Boolean isDisabled(){
		return this.Disabled;
	}
		
	public void setDisabled(Boolean bool){
		this.Disabled=bool ;
		if(this.Disabled){
			this.valueTextbox.setReadonly(true);
			this.queryButton.setDisabled(true);
			this.clearButton.setDisabled(true);
		}else{
			this.valueTextbox.setReadonly(Readonly);
			this.queryButton.setDisabled(false);
			this.clearButton.setDisabled(false);
		}
	}
	
	public String getMode() {
		return Mode;
	}
	private void removeChild(){
		//this.removeChild(valueTextbox);
		this.removeChild(displayTextbox);
		this.removeChild(queryButton);
		this.removeChild(clearButton);
		this.removeChild(displayLabel);
		
	}
	
	public void setMode(String mode) {
		removeChild();
		this.Mode = mode;
		
		//this.appendChild(valueTextbox);
		if(this.Mode.equals("1")){
			this.appendChild(displayTextbox);
		}
		this.appendChild(queryButton);
		this.appendChild(clearButton);
		if(this.Mode.equals("0")){
			this.appendChild(displayLabel);
		}
		
		
		
	}
	public Button getQueryButton() {
		return queryButton;
	}
	public void setQueryButton(Button queryButton) {
		this.queryButton = queryButton;
	}
	public Button getClearButton() {
		return clearButton;
	}
	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}
	
	public void setValue(Object value){
//		try {
//			XmlDAO xml = new XmlDAO(getXmlQryID());
//			for (QryField field : xml.getQryField()) {
//				if (field.getFieldName().equals(getReturnField())){
//					if(field.getFieldName().equals(getReturnField())&&field.getType().equals("N")){
//						valueDoublebox.setValue((Double)value);
//					}else{
//						valueTextbox.setValue((String)value);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if (returnComponent.equals("Double"))
			valueDoublebox.setValue((Double)value);
		else
			valueTextbox.setValue((String)value);
	}
	
	public String getValue(){
//		try {
//			XmlDAO xml = new XmlDAO(getXmlQryID());
//			for (QryField field : xml.getQryField()) {
//				if (field.getFieldName().equals(getReturnField())){
//					if(field.getFieldName().equals(getReturnField())&&field.getType().equals("N")){
//						return String.valueOf(valueDoublebox.getValue());
//					}else{
//						return valueTextbox.getValue();
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
		if (returnComponent.equals("Double"))
			return valueDoublebox.getValue() == null ? "" : String.valueOf(valueDoublebox.getValue());				
		else
			return valueTextbox.getValue();
	}
	
	public Textbox getValueTextbox() {
		return valueTextbox;
	}
	/*public void setValueTextbox(Textbox valueTextbox) {
		this.valueTextbox = valueTextbox;
	}*/
	
	public Textbox getDisplayTextbox() {
		return displayTextbox;
	}
	/*public void setDisplayTextbox(Textbox displayTextbox) {
		this.displayTextbox = displayTextbox;
	}*/
	public String getMarginStyle() {
		return marginStyle;
	}
	public void setMarginStyle(String marginStyle) {
		this.marginStyle = marginStyle;
	}
	public String getParentWindow() {
		return parentWindow;
	}
	public void setParentWindow(String parentWindow) {
		this.parentWindow = parentWindow;
	}
	public String getWindoWidth() {
		return WindoWidth;
	}
	public void setWindoWidth(String windoWidth) {
		WindoWidth = windoWidth;
	}
	public String getWindoHeight() {
		return WindoHeight;
	}
	public void setWindoHeight(String windoHeight) {
		WindoHeight = windoHeight;
	}
	public Boolean getSizable() {
		return Sizable;
	}
	public void setSizable(Boolean sizable) {
		Sizable = sizable;
	}
	public Boolean getClosable() {
		return Closable;
	}
	public void setClosable(Boolean closable) {
		Closable = closable;
	}
	public Boolean getMaximizable() {
		return Maximizable;
	}
	public void setMaximizable(Boolean maximizable) {
		Maximizable = maximizable;
	}
	public HashMap<String, Object> getSetupFilter() {
		return setupFilter;
	}
	public void setSetupFilter(HashMap<String, Object> setupFilter) {
		this.setupFilter = setupFilter;
	}
	public Boolean getMultiple() {
		return multiple;
	}
	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}
	public String getXmlQryID() {
		return xmlQryID;
	}
	private void doValueVisible() throws Exception{
		if(getReturnField()==null){
			return;
		}
		valueTextbox.setVisible(true);
		valueDoublebox.setVisible(false);
		XmlDAO xml = new XmlDAO(getXmlQryID());
		for (QryField field : xml.getQryField()) {
//			if(field.getFieldName().equals(getReturnField())&&field.getType().equals("N")){
//			valueTextbox.setVisible(false);
//			valueDoublebox.setVisible(true);
//		}
			if (field.getFieldName().equals(getReturnField())){
				if(field.getFieldName().equals(getReturnField())&&field.getType().equals("N")){
					valueTextbox.setVisible(false);
					valueDoublebox.setVisible(true);
					returnComponent="Double";
				}else{
					returnComponent="String";
				}
			}			
		}
	}
	public void setXmlQryID(String xmlQryID) throws Exception {
		this.xmlQryID = xmlQryID;
		doValueVisible();
	}
	public String getPageSize() {
		return PageSize;
	}
	public void setPageSize(String pageSize) {
		PageSize = pageSize;
	}
	public String getReturnField() {
		return returnField;
	}
	public void setReturnField(String returnField) throws Exception {
		this.returnField = returnField;
		doValueVisible();
	}
	
	public String getDisplayField() {
		return displayField;
	}
	public void setDisplayField(String displayField) {
		if(displayField==null){
			return;
		}
		if(isMultiLanguage()){
			this.displayField = displayField.replace(displayField, !BlackBox.getLocale().toUpperCase().equals("en")?displayField+"_"+BlackBox.getLocale().toUpperCase():""); //20180809 kobe新增多國語系
			//this.displayField = displayField.replace("!", !BlackBox.getLocale().toUpperCase().equals("en")?"_"+BlackBox.getLocale().toUpperCase():""); //20180809 kobe新增多國語系
		}else{
			this.displayField = displayField;
		}	
	}
	
	public boolean getReadonly() {
		return Readonly;
	}
	public void setReadonly(boolean Readonly) throws Exception {
		this.Readonly = Readonly;
		valueTextbox.setReadonly(Readonly);
		valueDoublebox.setReadonly(Readonly);
	}
	
	public void doMultiLanguage(){
		if(this.displayField!=null){
			this.displayField = displayField.replace(displayField, !BlackBox.getLocale().toUpperCase().equals("en")?this.displayField+"_"+BlackBox.getLocale().toUpperCase():""); //20180809 kobe新增多國語系
		}
	}
	public String getXmlID() {
		return xmlID;
	}
	public void setWindowMaster(Window windowMaster) {
		this.windowMaster = windowMaster;
	}
	/**
	 * 指定主要值欄位長度
	 * @param width
	 */
	private void setTextWidth(String width){
		valueTextbox.setWidth(width);
	}
	/**
	 * 取得主要值欄位長度
	 * @return
	 */
	private String getTextWidth(){
		return valueTextbox.getWidth();
	}
	
	/**
	 * 取得回傳顯示欄位長度
	 * @return
	 */
	private String getDisplayTextWidth(){
		return displayTextbox.getWidth();
	}
	/**
	 * 指定回傳顯示欄位長度
	 * @param width
	 */
	private void setDisplayTextWidth(String width){
		displayTextbox.setWidth(width);
	}
	
	private void init(){
		dscQueryField=this;
		displayValueTextbox=new Textbox();
		displayValueTextbox.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
		    public void onEvent(Event event) {
		    	displayLabel.setValue(displayValueTextbox.getValue());
		    	displayTextbox.setText(displayValueTextbox.getValue());
		    }
		});
	}
	public dscQueryField(){
		init();
		
		
		/**主要值欄位**/
		valueTextbox=new Textbox();
		valueTextbox.setStyle(marginStyle);
		valueTextbox.setWidth("80px");
		valueTextbox.setReadonly(Readonly);
		this.appendChild(valueTextbox);
		valueDoublebox=new Doublebox();
		valueDoublebox.setFormat("#,##0.##");
		valueDoublebox.setStyle(marginStyle);
		valueDoublebox.setWidth("80px");
		valueDoublebox.setReadonly(Readonly);
		this.appendChild(valueDoublebox);
		/**回傳顯示欄位**/
		displayLabel=new Label();
//		displayLabel.setStyle("margin-left: 1px;margin-right:1px;");
		this.appendChild(displayLabel);
		
		displayTextbox=new Textbox();
		displayTextbox.setStyle(marginStyle);
		displayTextbox.setWidth("100px");
		displayTextbox.setReadonly(true);
		this.appendChild(displayTextbox);
		
		queryButton=new Button();
		//queryButton.addForward("onClick", this, "onButtonsClick", "cancel");
		
		queryButton.setSclass("btn-warning");
		//queryButton.setId("queryButton"+this.getId());
		queryButton.setIconSclass("mdi mdi-magnify");
		//queryButton.setMold("trendy");
		queryButton.setStyle(marginStyle);
		queryButton.setDisabled(false);
		//queryButton.addForward("onClick", dscQueryField, "onAction");
		queryButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				//event.getTarget().getParent()
				Events.postEvent("onOpenBefor", dscQueryField, null);
				Events.postEvent("onAction", queryButton, null);
				Events.postEvent("onOpenAfter", dscQueryField, null);
			};
		});
		queryButton.addEventListener("onAction", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
			        try {
						final HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("dscQueryField",dscQueryField );
						map.put("WindoWidth", getWindoWidth()!=null?getWindoWidth():"600px");	//開窗長度，未設定預設滿版長度
						//map.put("WindoHeight", "65%");	//開窗高度，未設定預設自動高度
						map.put("Sizable", getSizable()!=null?getSizable():true);		//是否可手動調整大小
						map.put("Closable", getClosable()!=null?getClosable():true);		//是否可關閉視窗
						map.put("Maximizable",getMaximizable()!=null?getMaximizable():true);	//是否可最大化視窗
						map.put("setupFilter", getSetupFilter()!=null?getSetupFilter():null);
						map.put("multiple", getMultiple()!=null?getMultiple():false); 	// false:單選,true:多選目前不支援共用
						map.put("xmlQryID",getXmlQryID()!=null?getXmlQryID():null);	//XML ID
						//map.put("ListBoxRows",10);	//每頁固定筆數，超過可下拉捲軸選擇，未設定預設為5
						map.put("PageSize",10);			//每頁顯示筆數，未設定預設為5
						map.put("returnMethod",getReturnMethod()!=null?getReturnMethod():null);	//選取資料後執行Method，不一定要設定
						map.put("returnField", getReturnField());
						map.put("Event", event);
						map.put("CustomCondition", getCustomCondition()!=null?getCustomCondition():null); //自定過濾條件
						map.put("BaseCondition", getBaseCondition()!=null?getBaseCondition():null);		  //取代基本過濾條件
						setCustomCondition(null);
						
						ArrayList<ReturnBox> returnBoxList = new ArrayList<>();
						if(valueTextbox.isVisible()){
							returnBoxList.add(new ReturnBox(getReturnField(), valueTextbox));	
						}else{
							returnBoxList.add(new ReturnBox(getReturnField(), valueDoublebox));	
						}
						
						//returnBoxList.add(new ReturnBox(getDisplayField(), displayTextbox));
						//returnBoxList.add(new ReturnBox(getDisplayField(), displayLabel));
						returnBoxList.add(new ReturnBox(getDisplayField(), displayValueTextbox));
						
						map.put("returnTextBoxList", returnBoxList);
						Executions.createComponents("/util/openwin/QueryField.zul", null, map);
			        } catch (Exception e) {}
			    }	
		});
		clearButton=new Button();
		
		clearButton.setSclass("btn-danger");
		clearButton.setIconSclass("mdi mdi-broom");
		//clearButton.setMold("trendy");
		clearButton.setStyle(marginStyle);
		clearButton.setDisabled(false);
		clearButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				valueDoublebox.setText(null);
				valueTextbox.setText(null);
				displayTextbox.setText(null);
				displayLabel.setValue(null);
			}
		});
		//this.appendChild(displayValueTextbox);
		//queryButton.addForward("onClick", target, targetEvent)

	}
	
	@Command
    public void buttonsClick (@ContextParam(ContextType.TRIGGER_EVENT) ForwardEvent event) {
        System.out.println("buttons click " + event.getData());
        System.out.println("buttons click " + event.getOrigin().getTarget());
        System.out.println("buttons click " + ((Button)event.getOrigin().getTarget()).getLabel());
    }
	
	@Listen("onClick=button#submit")
	public void onOpenQueryField(Event event) {
		System.out.println(123);
	}
}
