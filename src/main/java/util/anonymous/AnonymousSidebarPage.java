
package util.anonymous;

import java.io.Serializable;

public class AnonymousSidebarPage implements Serializable{
	private static final long serialVersionUID = 1L;
	String name;
	String label;
	String iconUri;
	String uri;

	public AnonymousSidebarPage(String name, String label, String iconUri, String uri) {
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