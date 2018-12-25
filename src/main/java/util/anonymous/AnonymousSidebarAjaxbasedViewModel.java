/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package util.anonymous;

import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;

public class AnonymousSidebarAjaxbasedViewModel {
	
	//todo: wire service
	private AnonymousSidebarPageConfigAjaxBasedImpl pageConfig = new AnonymousSidebarPageConfigAjaxBasedImpl();
	private List<MenuNode> menuHierarchy = pageConfig.getMenuHierarchy();
	
//	public List<MenuNode> getSidebarPages() {
////		return pageConfig.getMenuHierarchy();
//		return this.menuHierarchy;
//	}
	
	public List<MenuNode> getMenuHierarchy() {
		return menuHierarchy;
	}

	public void setMenuHierarchy(List<MenuNode> menuHierarchy) {
		this.menuHierarchy = menuHierarchy;
	}
	
	@Command
	public void sibebarfunction(@BindingParam("target") Component target) {
		Component cc = (A)target;
		Component cc1 = cc.getParent();
		Div sidebar = (Div)cc1.getParent();

		sidebar.setSclass("sidebar sidebar-min");
		Navbar navbar = (Navbar)sidebar.getFellow("navbar");
		A toggler = (A)sidebar.getFellow("toggler");
		
		if (navbar.isCollapsed()) {
			sidebar.setSclass("sidebar");
			navbar.setCollapsed(false);
			toggler.setIconSclass("z-icon-angle-double-left");
		} else {
			sidebar.setSclass("sidebar sidebar-min");
			navbar.setCollapsed(true);
			toggler.setIconSclass("z-icon-angle-double-right");
		}
		// Force the hlayout contains sidebar to recalculate its size
		Clients.resize(sidebar.getRoot().query("#main"));
		
	}
	
}









