/**
 * Project:		Web應用系統開發平台
 * Filename: 	ProgramInfo.java
 * Author: 		wilson
 * Create Date:	2005/11/28
 * Version: 	0.90
 * Description:	作業程式資訊
 */

package util.menu;

import java.io.Serializable;

/**
 * 作業程式資訊 ProgramInfo
 */
public class TreeItemInfo implements Serializable {
	private	String type="";	
	private	String id="";
	private	String caption="";
	private	String description="";
	private String icon="";
	private	RightInfo rightInfo=null;
	private	String imageMap="";
	
	/**
	 * @param id
	 * @param caption
	 * @param description 
	 */
	public TreeItemInfo(String type, String id, String caption, String description, String icon, String imageMap) {
		this(type, id, caption, description, icon, new RightInfo(), imageMap);
	}
	//new TreeItemInfo("module", "", "固定資產管理系統", "固定資產管理系統", "Contact.png", "");
	public TreeItemInfo(String type, String id, String caption, String description, String icon, RightInfo rightInfo, String imageMap) {
		this.type = type;
		this.id = id;
		this.caption = caption;
		this.description = description;
		this.icon = icon;
		this.rightInfo = rightInfo;
		this.imageMap = imageMap;
	}

	public String getType() {
		return type;
	}
	
	/**
	 * 作業名稱
	 *
	 * @return Returns the caption.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * 程式詳細說明
	 *
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 程式代號
	 *
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 節點圖示
	 * 
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 程式權限
	 *
	 * @return Returns the rightInfo.
	 */
	public RightInfo getRightInfo() {
		return rightInfo;
	}
	
	/**
	 * 影像地圖檔
	 *
	 * @return Returns the caption.
	 */
	public String getImageMap() {
		return imageMap;
	}
}
