
package util.anonymous;

import java.util.List;

public interface AnonymousSidebarPageConfig {
	/** get pages of this application **/
	public List<MenuNode> getMenuHierarchy();
	
	/** get page **/
	public MenuNode getPage(String name);
}