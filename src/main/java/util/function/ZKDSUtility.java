package util.function;

import java.lang.reflect.Method;
import java.util.List;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

public class ZKDSUtility {

	
	public static void setListboxDisabled(Listbox listbox, boolean disabled){
		List<Listitem> list = listbox.getItems();
		for(Listitem item : list){
			item.setDisabled(disabled);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void setDisableComponents( AbstractComponent pComponent, boolean enable, Tree tree ) {
		  if(tree == null)
			  return;
		  if(enable)
			  tree.setNonselectableTags("*");//但是Tree包含的結點的ON_CLICK事件，依然有效。
		  else
			  tree.setNonselectableTags("");
		  for( Object o : pComponent.getChildren() ) {
//			  System.out.println(o);
		     AbstractComponent ac = ( AbstractComponent ) o;
		     try {
		        if(ac instanceof Textbox||ac instanceof Combobox){
			        Method m = ac.getClass().getMethod( "setReadonly", Boolean.TYPE );
			        m.invoke( ac, enable );
		        }
		        else if(ac instanceof Datebox||ac instanceof Selectbox||ac instanceof Checkbox||
		        		ac instanceof Button||ac instanceof Treeitem){
//					  System.out.println(o + "-" + "setDisabled");
				    Method m = ac.getClass().getMethod( "setDisabled", Boolean.TYPE );
				    m.invoke( ac, enable );
		        }
		     } catch( Exception e ) {
//		    	 System.out.println("Exception: setDisableComponents");
		     }
		     List children = ac.getChildren();
		     if( children != null ) {
		    	 setDisableComponents( ac, enable);
		     }
		  }
	}	
	
	@SuppressWarnings("rawtypes")
	public static void setDisableComponents( AbstractComponent pComponent, boolean enable ) {
		  for( Object o : pComponent.getChildren() ) {
//			  System.out.println(o);
		     AbstractComponent ac = ( AbstractComponent ) o;
		     try {
		        if(ac instanceof Textbox||ac instanceof Combobox){
			        Method m = ac.getClass().getMethod( "setReadonly", Boolean.TYPE );
			        m.invoke( ac, enable );
		        }
		        else if(ac instanceof Datebox||ac instanceof Selectbox||ac instanceof Checkbox||
		        		ac instanceof Button||ac instanceof Treeitem){
//		        	System.out.println(o + "-" + "setDisabled");
				    Method m = ac.getClass().getMethod( "setDisabled", Boolean.TYPE );
				    m.invoke( ac, enable );
		        }
		     } catch( Exception e ) {
//		    	 System.out.println("Exception : setDisableComponents( AbstractComponent pComponent, boolean enable ) {");
		     }
		     List children = ac.getChildren();
		     if( children != null ) {
		    	 
		    	 setDisableComponents( ac, enable);
		     }
		  }
	}
	
	@SuppressWarnings("rawtypes")
	public static void setStringValueComponents( AbstractComponent pComponent, String value ) {
		  for( Object o : pComponent.getChildren() ) {
		     AbstractComponent ac = ( AbstractComponent ) o;
		     try {
		        Method m = ac.getClass().getMethod( "setText", String.class );
		        m.invoke( ac, value );
		     } catch( Exception e ) {
		     }
		     List children = ac.getChildren();
		     if( children != null ) {
		    	 setStringValueComponents( ac, value);
		     }
		  }
	
	}
	
}