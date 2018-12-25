package util.menu;


import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;

import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;

import util.function.XmlUtil;
import util.info.LoginInfo;
import util.menu.TreeModel;
import util.menu.TreeNode;
import util.menu.TreeItemInfo;

//import org.zkoss.zul.DefaultTreeModel;
//import org.zkoss.zul.TreeModel;
//import org.zkoss.zul.TreeNode;

public class TreeMenu extends SelectorComposer<Component> {
//	public static ImageReference imgTreeModule;
//	public static ImageReference imgTreeProgram;
	
	ResourceBundle resourceBundle;
//	private AdvancedTreeModel0 contactTreeModel;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		System.out.println("TreeMenu doAfterCompose");
		//		btnTest.setLabel("Tiger");
	}
	

	public TreeMenu() {
		super();
	}
	
	public Tree treemenu;
	private TreeModel menuTreeModel;
	
	public void init(String	TREENUMUFILE){
		HashMap<String, String> map1 = new HashMap<String, String>();
		treemenu = (Tree)Executions.createComponents("/dsc/zk2app/program/TreeMenu.zul", null, map1);
		
		readMenuFile(TREENUMUFILE);
		
//		menuTreeModel = new AdvancedTreeModel(new MenuList().getRoot());
		treemenu.setItemRenderer(new MenuTreeRenderer());
		treemenu.setModel(menuTreeModel);

		
	}
	
	public void readMenuFile(String file) {
		Document doc;
		resourceBundle = ResourceBundle.getBundle("conf.ModuleProgramName",LoginInfo.getLocale());

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(file);
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(url.getFile());
		}
		catch (Exception E) {
			E.printStackTrace();
			resourceBundle = null;
			return;
		}

		Element rootElement = doc.getDocumentElement();
		if(rootElement == null) return;
		if(!rootElement.getTagName().equalsIgnoreCase("TreeMenu")) return;
		
		TreeNode[] cs = null;
		TreeNode root = new TreeNode(null, cs);
		menuTreeModel = new TreeModel(root);
        
        renderMenuItem(rootElement, root);
		resourceBundle = null;
	}
	
	private void renderMenuItem(Element xmlElement, TreeNode parentNode) {
		if(xmlElement == null) return;
		NodeList xmlNodeList =  xmlElement.getChildNodes();
		if(xmlNodeList == null) return;
		
		ProgramInfo programInfo;
		for(int i=0; i<xmlNodeList.getLength(); i++) {
			Node xmlNode = xmlNodeList.item(i);
			if(xmlNode.getNodeName().equalsIgnoreCase("menuitem") && xmlNode.getNodeType()== Node.ELEMENT_NODE) {
				Element itemElement = (Element)xmlNode;
				String type = XmlUtil.getAttribute(itemElement, "type");
				programInfo = null;
				if(type.equalsIgnoreCase("module")) {
					String moduleid = XmlUtil.getAttribute(itemElement, "id");
					String caption =  moduleid;
					String description = moduleid;
					String icon = XmlUtil.getAttribute(itemElement, "icon");
					String imageMap = XmlUtil.getAttribute(itemElement, "imageMap");
					try {
						caption =  resourceBundle.getString(moduleid);
					}
					catch (Exception e) {};
					
					TreeItemInfo itemInfo = new TreeItemInfo(
							"module", moduleid, caption, description, icon,
							XmlUtil.getAttribute(itemElement, "imageMap")
							);
					
					TreeNode[] cs = null;
					TreeNode treeNode = new TreeNode(itemInfo, cs);
					parentNode.add(treeNode);

					if (xmlNode.getChildNodes().getLength() > 0) {
						if(xmlNode.getNodeType() == Node.ELEMENT_NODE) {
							renderMenuItem((Element)xmlNode, treeNode);
						}
		        	}
				}
				else if(type.equalsIgnoreCase("program")) {
					String programid = XmlUtil.getAttribute(itemElement, "id");
					ProgramInfo pi = LoginInfo.findProgramInfo(programid);
					if (pi != null) {
						String caption =  programid;
						String description = programid;
						String icon = XmlUtil.getAttribute(itemElement, "icon");
						try {
							caption =  resourceBundle.getString(programid);
						}
						catch (Exception e) {};
						
						TreeItemInfo itemInfo = new TreeItemInfo(
								"program", programid, caption, description, icon,
								pi.getRightInfo(),
								""
								);
						TreeNode treeNode = new TreeNode(itemInfo);
//						DefaultMutableTreeNode treeNode= new DefaultMutableTreeNode( programInfo);
						parentNode.add(treeNode);
						
					}
				}
			
			}//if(xmlNode.getNodeName().equalsIgnoreCase("menuitem")) {
		}
	}//randerMenuItem
	
	private final class MenuTreeRenderer implements TreeitemRenderer<TreeNode> {
		@Override
		public void render(final Treeitem treeItem, TreeNode treeNode, int index) throws Exception {
			TreeNode ctn = treeNode;
			TreeItemInfo menuitem = (TreeItemInfo) ctn.getData();
			Treerow dataRow = new Treerow();
			dataRow.setParent(treeItem);
			treeItem.setValue(ctn);
			treeItem.setOpen(ctn.isOpen());
			
			if ("program".equalsIgnoreCase(menuitem.getType())) { // Contact Row
				Hlayout hl = new Hlayout();
				hl.appendChild(new Image("/imgs/icon.gif"));
				hl.appendChild(new Label(menuitem.getCaption()));
				hl.setSclass("h-inline-block");
				//hl.setHeight(Application.MenuHeight);
				
				Treecell treeCell = new Treecell();
				treeCell.appendChild(hl);
				
				dataRow.setTooltiptext(menuitem.getCaption());
				
				dataRow.setDraggable("true");
				dataRow.appendChild(treeCell);
				//dataRow.appendChild(new Treecell(menuitem.getCaption()));
				dataRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					TreeNode clickedNodeValue = (TreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
					TreeItemInfo menuitem = (TreeItemInfo)clickedNodeValue.getData();
					
					ProgramInfo programInfo = new ProgramInfo(
							menuitem.getId(),
							menuitem.getCaption(),
							menuitem.getDescription(),
							menuitem.getIcon(),
							menuitem.getRightInfo());

					//mainScreen.doExecuteProgram(programInfo);
					
				}
			});
			} //
			else if ("module".equalsIgnoreCase(menuitem.getType())) { // Category Row
				dataRow.setTooltiptext(menuitem.getCaption());
				dataRow.appendChild(new Treecell(menuitem.getCaption()));
			}
			// Both category row and contact row can be item dropped
			dataRow.setDroppable("true");
			dataRow.addEventListener(Events.ON_DROP, new EventListener<Event>() {
				@SuppressWarnings("unchecked")
				@Override
				public void onEvent(Event event) throws Exception {
					// The dragged target is a TreeRow belongs to an
					// Treechildren of TreeItem.
					Treeitem draggedItem = (Treeitem) ((DropEvent) event).getDragged().getParent();
					TreeNode draggedValue = (TreeNode) draggedItem.getValue();
					Treeitem parentItem = treeItem.getParentItem();
					menuTreeModel.remove(draggedValue);
					if (isMenu((TreeItemInfo) ((TreeNode) treeItem.getValue()).getData())) {
						menuTreeModel.add((TreeNode) treeItem.getValue(),
								new DefaultTreeNode[] { draggedValue });
					} else {
						int index = parentItem.getTreechildren().getChildren().indexOf(treeItem);
						if(parentItem.getValue() instanceof TreeNode) {
							menuTreeModel.insert((TreeNode)parentItem.getValue(), index, index,
									new DefaultTreeNode[] { draggedValue });
						}
						
					}
				}
			});

		}

		private boolean isMenu(TreeItemInfo menuItem) {
			return menuItem.getType().equals("module");
		}
	}	

//	
//	
//	/**
//	 * 取得TreeProgramIcon
//	 * @return This value.
//	 */
//	public static ImageReference getTreeProgramIcon() {
//		return imgTreeProgram;
//	}
//
//	/**
//	 * 設定
//	 * @param imgTreeProgram The TreeProgramIcon to set.
//	 */
//	public static void setTreeProgramIcon(ImageReference imgTreeProgram) {
//		TreeMenu.imgTreeProgram = imgTreeProgram;
//	}
//
//	/**
//	 * 取得TreeModuleIcon
//	 * @return This value.
//	 */
//	public static ImageReference getTreeModuleIcon() {
//		return imgTreeModule;
//	}
//
//	/**
//	 * 設定
//	 * @param imgTreeModule The TreeModuleIcon to set.
//	 */
//	public static void setTreeModuleIcon(ImageReference imgTreeModule) {
//		TreeMenu.imgTreeModule = imgTreeModule;
//	}
	
}