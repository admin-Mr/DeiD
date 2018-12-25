/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package util.admin;

import java.io.Serializable;

public class AdminSidebarPage implements Serializable{
	private static final long serialVersionUID = 1L;
	String name;
	String label;
	String iconUri;
	String uri;

	public AdminSidebarPage(String name, String label, String iconUri, String uri) {
		this.name = name;
		this.label = label;
		this.iconUri = iconUri;
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public String getIconUri() {
		return iconUri;
	}

	public String getUri() {
		return uri;
	}
}