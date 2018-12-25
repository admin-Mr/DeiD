package util.anonymous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zkplus.spring.SpringUtil;

import ds.common.services.CRUDService;
import ds.dspb.domain.DSPB00_NEW;
import util.info.SystemInfo;

public class AnonymousSidebarPageConfigAjaxBasedImpl implements AnonymousSidebarPageConfig {
	HashMap<String, MenuNode> pageMap = new LinkedHashMap<String, MenuNode>();

	public AnonymousSidebarPageConfigAjaxBasedImpl() {
		ServletContext sc =
			    (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		String strSYSID = sc.getInitParameter("projectID");
		
		MenuNode mnNode = null;
		List rsNodeList = null;		
		String strNodeSQL = "Select * From DSPB00_NEW Where PB_MUNODE is null and PB_SYSID='" + strSYSID + "' and NVL(PB_ADMIN,'Y') = 'N' Order by PB_MUITEM ";

		CRUDService CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");
		EntityManager newEntityManager = null;
		
		SystemInfo SystemInfo = (SystemInfo) SpringUtil.getBean("systemInfo");
		String[] Companies = new String[SystemInfo.getCompanies().size()];
		HashMap<Integer, String> mapCRUDService = new HashMap<>();
		for (int i = 0; i < SystemInfo.getCompanies().size(); i++) {
			Companies[i] = SystemInfo.getCompanies().get(i).getCompanyID();
			mapCRUDService.put(i, SystemInfo.getCompanies().get(i).getConnectionName());
		}
		
		if (CRUDService == null) {
			newEntityManager = ((CRUDService) SpringUtil.getBean(mapCRUDService.get(0))).getEmf().createEntityManager();
		} else {
			newEntityManager =  CRUDService.getEmf().createEntityManager();
		}
		
		EntityTransaction newEtx = newEntityManager.getTransaction();
		try{
			newEtx.begin();
			rsNodeList = newEntityManager.createNativeQuery(strNodeSQL, DSPB00_NEW.class).getResultList();
			System.out.println(rsNodeList.size() + " nodw record(s) found.");
			newEtx.commit();
			
			for(Object rsnode: rsNodeList){
				DSPB00_NEW rsNode = (DSPB00_NEW) rsnode;
				if (rsnode != null){
					String strNode = rsNode.getPB_MUITEM();
					String strTag = rsNode.getPB_LANGTAG();
					String strIconPath = rsNode.getPB_ICONPATH();
					String strIconSclass = rsNode.getPB_ICONSCLASS();
					String strFilePath = rsNode.getPB_FILEPATH();
					// Node
					mnNode = new MenuNode(strNode, Labels.getLabel(strTag), strIconPath, strFilePath, strIconSclass == null ? "z-icon-cog" : strIconSclass);
					// Item
					String strItemSQL = "Select * From DSPB00_NEW Where PB_SYSID = '" + strSYSID + "' and PB_MUNODE = '" + strNode + "' and NVL(PB_ADMIN,'Y') = 'N' order by PB_MUITEM";
					
					List rsItemList = newEntityManager.createNativeQuery(strItemSQL, DSPB00_NEW.class).getResultList();
					
					System.out.println(rsItemList.size() + " item record(s) found.");
					
					List<MenuNode> muItem = new LinkedList<MenuNode>();
					
					for (Object rsitem : rsItemList) {
						DSPB00_NEW rsItem = (DSPB00_NEW) rsitem;
						if (rsItem != null){
							String strItem = strNode + "_" + rsItem.getPB_MUITEM();
							strTag = rsItem.getPB_LANGTAG();
							strFilePath = rsItem.getPB_FILEPATH();
							strIconPath = rsItem.getPB_ICONPATH();
							strIconSclass = rsItem.getPB_ICONSCLASS();
							muItem.add(new MenuNode(strItem, Labels.getLabel(strTag), strIconPath, strFilePath, strIconSclass == null ? "z-icon-home" : strIconSclass, "Msg"));
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
			newEntityManager.close();
		}
	}

	public List<MenuNode> getMenuHierarchy() {
		return new ArrayList<MenuNode>(pageMap.values());
	}

	public MenuNode getPage(String name) {
		return pageMap.get(name);
	}
}