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
public class ProgramInfo implements Serializable {
	private	String id="";
	private	String caption="";
	private	String description="";
	private String icon="";
	private	RightInfo rightInfo=null;
	
	/**
	 * @param id
	 * @param caption
	 * @param description 
	 */
	public ProgramInfo(String id, String caption, String description, String icon) {
		this(id, caption, description, icon, new RightInfo());
	}
	
	public ProgramInfo(String id, String caption, String description, String icon, RightInfo rightInfo) {
		this.id = id;
		this.caption = caption;
		this.description = description;
		this.icon = icon;
		this.rightInfo = rightInfo;
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
}
