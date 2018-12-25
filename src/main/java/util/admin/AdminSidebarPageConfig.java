/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package util.admin;

import java.util.List;

import util.anonymous.MenuNode;

public interface AdminSidebarPageConfig {
	/** get pages of this application **/
	public List<MenuNode> getMenuHierarchy();
	
	/** get page **/
	public MenuNode getPage(String name);
}