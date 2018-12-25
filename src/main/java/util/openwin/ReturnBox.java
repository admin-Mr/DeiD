package util.openwin;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class ReturnBox {
	String FieldName;
	Textbox TextboxName;
	Doublebox DoubleboxName;
	Combobox ComboboxName;
	Object Obj;
	Label label;
	public ReturnBox(String fieldName,Textbox textboxName){
		this.FieldName=fieldName;
		this.TextboxName=textboxName;
		this.Obj=textboxName;
	}
	public ReturnBox(String fieldName,Doublebox doubleboxName){
		this.FieldName=fieldName;
		this.DoubleboxName=doubleboxName;
		this.Obj=doubleboxName;
	}
	public ReturnBox(String fieldName,Label label){
		this.FieldName=fieldName;
		this.label=label;
		this.Obj=label;
	}
	public ReturnBox(String fieldName,Combobox comboboxName){
		this.FieldName=fieldName;
		this.ComboboxName=comboboxName;
		this.Obj=comboboxName;
	}
	
	public String getFieldName() {
		return FieldName;
	}
	public void setFieldName(String fieldName) {
		FieldName = fieldName;
	}
	public Textbox getTextboxName() {
		return TextboxName;
	}
	public void setTextboxName(Textbox textboxName) {
		TextboxName = textboxName;
	}
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public Doublebox getDoubleboxName() {
		return DoubleboxName;
	}
	public void setDoubleboxName(Doublebox doubleboxName) {
		DoubleboxName = doubleboxName;
	}
	public Combobox getComboboxName() {
		return ComboboxName;
	}
	public void setComboboxName(Combobox comboboxName) {
		ComboboxName = comboboxName;
	}
	public Object getObj(){
		return Obj;
	}
	
}
