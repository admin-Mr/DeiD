/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package util.anonymous;

import java.util.Collections;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import util.anonymous.AnonymousSidebarPage;
import util.anonymous.AnonymousSidebarPageConfig;

public class BookmarkChangeViewModel  {

	//todo: wire service
	private AnonymousSidebarPageConfig pageConfig = new AnonymousSidebarPageConfigAjaxBasedImpl();
	
	@Command("onBookmarkNavigate")
	public void onBookmarkNavigate(@BindingParam("bookmark") String bookmark) {
		if(bookmark.startsWith("p_")){
			//retrieve page from config
			String p = bookmark.substring("p_".length());
			MenuNode page = pageConfig.getPage(p);
			if(page!=null) {
				//and post command to NavigationViewModel
				BindUtils.postGlobalCommand(null,  null, "onNavigate", Collections.<String, Object>singletonMap("page", page));
			}
		}
	}
}
