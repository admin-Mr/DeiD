/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package util.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;

import util.Common;
import util.anonymous.MenuNode;

public class AdminSidebarAjaxbasedViewModel{

	//todo: wire service
	private AdminSidebarPageConfigAjaxBasedImpl pageConfig = new AdminSidebarPageConfigAjaxBasedImpl();
	private List<MenuNode> menuHierarchy = pageConfig.getMenuHierarchy();
	
	public List<MenuNode> getMenuHierarchy() {
		return menuHierarchy;
	}

	public void setMenuHierarchy(List<MenuNode> menuHierarchy) {
		this.menuHierarchy = menuHierarchy;
	}
	
	@GlobalCommand("onBugReturn")
	public void clickBugReturn(@ContextParam(ContextType.TRIGGER_EVENT) MouseEvent event,
			@BindingParam("menunode") Menuitem menuitem) {
		System.out.println("0000000000:" + event);
		System.out.println("1111111111:" + menuitem);
		// Executions.getCurrent().sendRedirect(url, "_blank");

		final HashMap<String, Object> map = new HashMap<String, Object>();
		/** 選擇記錄(選擇的資料) */
		map.put("selectedRecord", null);
		/** 自訂放入模式  */
		map.put("recordMode", Common.DATA_MODE_QUERY);
		map.put("parentWindow", null);
		map.put("courseModel", null);// tiger 20160318 001
		Messagebox.show("維修中(system is being maintained)", Labels.getLabel("PUBLIC.MSG0002"), Messagebox.OK, Messagebox.INFORMATION);	
//		Executions.createComponents("util/admin/BugReturnWindow.zul", null, map);
	}


	@SuppressWarnings("unused")
	@GlobalCommand("onMsgRedo")
	public void clickMsgRedo(@ContextParam(ContextType.TRIGGER_EVENT) MouseEvent event,
			@BindingParam("menunode") Menuitem menuitem) {
		System.out.println("0000000000:" + event);
		System.out.println("1111111111:" + menuitem);
		MenuNode catchTemp = (MenuNode) Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.TITLE);
		Locale preferredLocale = (Locale) Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);
		if (catchTemp != null && (preferredLocale == null || preferredLocale.getCountry().equals("TW")
				|| preferredLocale.getCountry().equals("CN"))) {
			String controlWeb = "window.open('http://10.1.1.67/help/index.php/" + catchTemp.getLabel()
					+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')";
			Clients.evalJavaScript(controlWeb);
		} else if (catchTemp == null) {
			Clients.evalJavaScript(
					"window.open('http://10.1.1.67/help/index.php/Learning_Master%E7%B3%BB%E7%B5%B1','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
		} else if (catchTemp != null
				&& (!preferredLocale.getCountry().equals("TW") || !preferredLocale.getCountry().equals("CN"))) {
			String controlWebEN = "window.open('http://10.1.1.67/help/index.php/" + catchTemp.getLabel()
					+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')";
			Clients.evalJavaScript(controlWebEN);
		} else if (catchTemp == null
				&& (!preferredLocale.getCountry().equals("TW") || !preferredLocale.getCountry().equals("CN"))) {
			Clients.evalJavaScript(
					"window.open('http://10.1.1.67/help/index.php/DSG_Learning_Master_System','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
		}
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
