package util.admin;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import ds.common.services.CRUDService;
import ds.common.services.UserCredential;
import ds.dspb.domain.DSPB00_NEW;
import util.anonymous.MenuNode;
import util.function.XmlUtil;
import util.info.LoginInfo;
import util.menu.ProgramInfo;
import util.menu.TreeItemInfo;
import util.menu.TreeModel;
import util.menu.TreeNode;

public class AdminSidebarPageConfigAjaxBasedImpl implements  AdminSidebarPageConfig,Serializable {
	private static final long serialVersionUID = 1L;
	UserCredential _userInfo = (UserCredential) Sessions.getCurrent().getAttribute("userCredential");	
	String strAccount = _userInfo.getAccount();
	
	HashMap<String, MenuNode> pageMap = new LinkedHashMap<String, MenuNode>();
	
	ResourceBundle resourceBundle;
	
	public Tree treemenu;
	
	private TreeModel menuTreeModel;
	
	public AdminSidebarPageConfigAjaxBasedImpl() {
		ServletContext sc =
			    (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		String strSYSID = sc.getInitParameter("projectID");

		MenuNode mnNode = null;
		List rsNodeList = null;
		String strNodeSQL = "Select * From DSPB00_NEW Where PB_MUNODE is null and PB_ID in ( " +
				"Select PB_ID From DSPB01 Where PB_USERID='" + strAccount + "' and PB_ID in (Select PB_ID From DSPB00_NEW Where PB_SYSID='" + strSYSID + "') union all " +
				"Select PB_ID From DSPB01_GRP Where PB_GROUPID in (Select DSPB04.PB_GROUPID From DSPB04 Left Join DSPB03 on DSPB04.PB_GROUPID = DSPB03.PB_GROUPID " +
						"Where DSPB04.PB_USERID='" + strAccount + "' and DSPB03.PB_SYSID='" + strSYSID + "')) and NVL(PB_ADMIN,'Y')='Y' and PB_SYSID='" + strSYSID + "' Order by PB_MUITEM";
		CRUDService CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		EntityManager newEntityManager =  CRUDService.getEmf().createEntityManager();
        EntityTransaction newEtx = newEntityManager.getTransaction();
		try{			
	        newEtx.begin();
	        rsNodeList = newEntityManager.createNativeQuery(strNodeSQL, DSPB00_NEW.class).getResultList();
	        System.out.println(rsNodeList.size() + " node record(s) found.");
	      
	        newEtx.commit();
	        
	        for (Object rsnode : rsNodeList) {
				DSPB00_NEW rsNode = (DSPB00_NEW) rsnode;
				if (rsNode != null) {
					String strNode = rsNode.getPB_MUITEM();
					String strTag = rsNode.getPB_LANGTAG();
					String strIconPath = rsNode.getPB_ICONPATH();
					String strIconSclass = rsNode.getPB_ICONSCLASS();
					String strFilePath = rsNode.getPB_FILEPATH();
					// Node
					mnNode = new MenuNode(strNode, Labels.getLabel(strTag), strIconPath, strFilePath, strIconSclass == null? "z-icon-cog" : strIconSclass);
					
					String strItemSQL = "Select * From DSPB00_NEW Where PB_SYSID='" + strSYSID + "' and PB_MUNODE = '" + strNode + "' "+
					"and PB_ID in (Select PB_ID From DSPB01 Where PB_USERID = '"+ strAccount +
					"' UNION Select PB_ID From DSPB01_GRP Where PB_GROUPID in (Select PB_GROUPID From DSPB04 Where PB_USERID = '" + strAccount +
					"')) order by PB_MUITEM";
					
					List rsItemList = newEntityManager.createNativeQuery(strItemSQL, DSPB00_NEW.class).getResultList();
					
					System.out.println(rsItemList.size() + " item record(s) found.");
					
					List<MenuNode> muItem = new LinkedList<MenuNode>();
					
					for (Object rsitem : rsItemList) {
						DSPB00_NEW rsItem = (DSPB00_NEW) rsitem;
						if (rsItem != null) {
							String strItem = strNode + "_" + rsItem.getPB_MUITEM();
							strTag = rsItem.getPB_LANGTAG();
							strIconPath = rsItem.getPB_ICONPATH();
							strIconSclass = rsItem.getPB_ICONSCLASS();
							strFilePath = rsItem.getPB_FILEPATH();
							muItem.add(new MenuNode(strItem, Labels.getLabel(strTag), strIconPath, strFilePath, strIconSclass == null? "z-icon-home" : strIconSclass, "Msg"));			
						}
					}
					
					if (muItem != null){
						mnNode.setSubMenus(muItem);
					}				
					pageMap.put("Node_"+strNode, mnNode);
				}
			}
	        

		}catch(Exception ex){
			System.out.println("[ERROR]CreateMenu: " + ex.getMessage());
		}finally {
			//Common.closeConnection(conn);
			newEntityManager.close();
		}		

	}
	
	public void readMenuFile(String file) {
		Document doc;

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

		String nodeName = null;
		ProgramInfo programInfo;
		int i;
		int j = 0;
		for(i=0; i<xmlNodeList.getLength(); i++) {
			Node xmlNode = xmlNodeList.item(i);
			if(xmlNode.getNodeName().equalsIgnoreCase("menuitem") && xmlNode.getNodeType()== Node.ELEMENT_NODE) {
				Element itemElement = (Element)xmlNode;
				String type = XmlUtil.getAttribute(itemElement, "type");
				programInfo = null;
				
				//判斷是否為modelu節點
				if(type.equalsIgnoreCase("module")) {
					String moduleid = XmlUtil.getAttribute(itemElement, "id");
					ProgramInfo pi = LoginInfo.findProgramInfo(moduleid);
					if (pi == null) {
						String label =  moduleid;
						String iconUri = XmlUtil.getAttribute(itemElement, "iconUri");
						String uri = XmlUtil.getAttribute(itemElement, "uri");
						String iconSclass = XmlUtil.getAttribute(itemElement, "iconSclass");
						try {
							label =  resourceBundle.getString(moduleid);
						}
						catch (Exception e) {};
						//node[i] = new MenuNode(nodeName, label, iconUri, uri, iconSclass);
					}

				}//判斷是否為program節點
				else if(type.equalsIgnoreCase("program")) {
					String programid = XmlUtil.getAttribute(itemElement, "id");
					ProgramInfo pi = LoginInfo.findProgramInfo(programid);
					if (pi == null) {
						String label =  programid;
						String iconUri = XmlUtil.getAttribute(itemElement, "iconUri");
						String uri = XmlUtil.getAttribute(itemElement, "uri");
						String iconSclass = XmlUtil.getAttribute(itemElement, "iconSclass");
						try {
							label =  resourceBundle.getString(programid);
						}
						catch (Exception e) {};
						
					}
				}
			
			}

		}
	}
	
	
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
	
	public List<MenuNode> getMenuHierarchy() {
		return new ArrayList<MenuNode>(pageMap.values());
	}

	public MenuNode getPage(String name) {
		return pageMap.get(name);
	}

}