package util.anonymous;

import java.util.List;

public class MenuNode {

	private String label;
	private String iconSclass;
	private List<MenuNode> subMenus;
	
	private String name;
	private String iconUri;
	private String uri;
	
	//tiger 20160524
	private String adminController;
	private String operation;
	
//	public MenuNode(String name, String label, String iconUri, String uri, String iconSclass, String adminController, String operation) {
	public MenuNode(String name, String label, String iconUri, String uri, String iconSclass, String adminController) {
		this.label = label;
		this.iconSclass = iconSclass;
		this.name = name;
		this.iconUri = iconUri;
		this.uri = uri;
		this.adminController = adminController;
//		this.operation = operation;
	}
	
	public MenuNode(String name, String label, String iconUri, String uri, String iconSclass) {
		this.label = label;
		this.iconSclass = iconSclass;
		this.name = name;
		this.iconUri = iconUri;
		this.uri = uri;
	}

	public MenuNode(String label, String iconSclass){
		this.label = label;
		this.iconSclass = iconSclass;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getIconSclass() {
		return iconSclass;
	}
	public void setIconSclass(String iconSclass) {
		this.iconSclass = iconSclass;
	}
	public List<MenuNode> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<MenuNode> subMenus) {
		this.subMenus = subMenus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAdminController() {
		return adminController;
	}

	public void setAdminController(String adminController) {
		this.adminController = adminController;
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
