package util.anonymous;

import java.util.*;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Include;

public class MenuViewModel {

	private List<MenuNode> menuHierarchy = null;
	private List<MenuNode> menuHierarchy1 = new LinkedList<MenuNode>();
	private String currentPage;
	
	@Init
	public void init(){
		//initialize menu
		initMenuHierachy1();
		menuHierarchy = menuHierarchy1;
	}
	
	@Command @SmartNotifyChange("currentPage")
	public void navigate(@ContextParam(ContextType.TRIGGER_EVENT) MouseEvent event, @BindingParam("menunode") MenuNode menunode){
//		currentPage = event.getSelectedItems().iterator().next().getLabel();
		System.out.println(menunode);
		System.out.println(((Navitem)event.getTarget()).getLabel());
	}
	
	private void initMenuHierachy1() {
		List<MenuNode> menuitems1 = new LinkedList<MenuNode>();
		menuitems1.add(new MenuNode("About Us", null));
		menuitems1.add(new MenuNode("Menu", null));
		menuitems1.add(new MenuNode("FAQ",null));
		menuitems1.add(new MenuNode("Press", null));
//		public MenuNode(String label, String iconSclass, String name, String iconUri, String uri) {
//		AnonymousSidebarPage("anony01","Demo01","/imgs/site.png","http://www.zkoss.org/"));
		MenuNode menuNode1 = new MenuNode("anony01","Demo01","/imgs/site.png","http://www.zkoss.org/", "z-icon-home");
//		MenuNode menuNode1 = new MenuNode("ZK Pizza", "z-icon-home");
//		menuNode1.setSubMenus(menuitems1);
		menuHierarchy1.add(menuNode1);
		
		List<MenuNode> menuitems2 = new LinkedList<MenuNode>();
		menuitems2.add(new MenuNode("Active Members", null));
		menuitems2.add(new MenuNode("Inactive Members", null));
		MenuNode menuNode2 = new MenuNode("anony01","Demo01","/imgs/site.png","http://www.zkoss.org/", "z-icon-home");
//		MenuNode menuNode2 = new MenuNode("Customers", "z-icon-group");
//		menuNode2.setSubMenus(menuitems2);
		menuHierarchy1.add(menuNode2);
		
		List<MenuNode> menuitems3 = new LinkedList<MenuNode>();
		menuitems3.add(new MenuNode("In Preparation", null));
		menuitems3.add(new MenuNode("Ready for Shipping", null));
		menuitems3.add(new MenuNode("Shipping", null));
		menuitems3.add(new MenuNode("Specified for Later", null));
		MenuNode menuNode3 = new MenuNode("Orders", "z-icon-truck");
//		menuNode3.setSubMenus(menuitems3);
//		menuHierarchy1.add(menuNode3);
		
		List<MenuNode> menuitems4 = new LinkedList<MenuNode>();
		menuitems4.add(new MenuNode("Events",null));
		menuitems4.add(new MenuNode("Promotion", null));
		MenuNode menuNode4 = new MenuNode("Fan Service", "z-icon-star");
//		menuNode4.setSubMenus(menuitems3);
//		menuHierarchy1.add(menuNode4);
		
		MenuNode menuNode5 = new MenuNode("Misc.", "z-icon-star");
//		menuHierarchy1.add(menuNode5);
	}
	
//	@Command @SmartNotifyChange("menuHierarchy")
//	public void switchMenu(@BindingParam("option")int option){
//		if (option == 1){
//			menuHierarchy = menuHierarchy1;
//		}else{
//			menuHierarchy = menuHierarchy2;
//		}
//	}
	
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}


	public String getCurrentPage() {
		return currentPage;
	}


	public List<MenuNode> getMenuHierarchy() {
		return menuHierarchy;
	}


	public void setMenuHierarchy(List<MenuNode> menuHierarchy) {
		this.menuHierarchy = menuHierarchy;
	}
	
}
