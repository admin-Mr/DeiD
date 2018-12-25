/**
 * Author: 		Wilson
 * Create Date:	2005/11/08
 * Version: 	0.90
 * Description:	�B�zXML�ɮפu��
 */
package util.function;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class XmlUtil {

	public static Document openXmlFile(String file, boolean validating) throws Exception {
		Document doc;
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource(systemFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(file);
		return doc;
	}//openXmlFile
	
	public static Element[] getChildren(Node node, String tagName )	{
		if (node.getFirstChild() == null)
			return null;
		NodeList nodes = node.getChildNodes();
		ArrayList elements = new ArrayList();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node childNode = nodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE
					&& tagName.equalsIgnoreCase(childNode.getNodeName()))
				elements.add(childNode);
		}
		if (elements.size() == 0)
			return null;

		Element[] es = new Element[elements.size()];
		elements.toArray(es);
		return es;
	}//getChildren

	public static Element getFirstChildren(Node node, String tagName )	{
		if( node.getFirstChild() == null )	return null;
		NodeList nodes = node.getChildNodes();
		for(int i=0; i<nodes.getLength(); i++ )	{
			Node childNode = nodes.item(i);
			if( childNode.getNodeType() == Node.ELEMENT_NODE &&	tagName.equalsIgnoreCase(childNode.getNodeName()))
				return (Element)childNode;
		}
		return null;
	}

	public static String getAttribute(Element elem, String name) {
		String s = elem.getAttribute(name);
		
		return s==null ? "" : s;
	}//getAttribute

	public static String getNodeString(Element elem) {
		NodeList nodes = elem.getChildNodes();
  		int len = nodes.getLength();
  		String s=null;
  		for ( int i = 0; i < len; i++ )	{
  			Node node = nodes.item(i);
 			if ( node.getNodeType() == Node.TEXT_NODE ) {
  				s = node.getNodeValue();
 				break;
 			}
 			if ( node.getNodeType() == Node.CDATA_SECTION_NODE ) {
  				s = node.getNodeValue();
 				break;
 			}
 			if( node.getNodeType() == Node.ELEMENT_NODE) {
 				s = getNodeString(node);
 			}
	    }
		return s==null ? "" : s;
	}//getNodeString
	
	public static String getNodeString(Node node) {
		String s=null;
		if(node.getFirstChild() != null) {
            NodeList sub = node.getChildNodes();
            if (sub != null) {
                for (int i = 0; i < sub.getLength(); i++) {
                	Node n = sub.item(i);
                	if (n.getNodeType() == Node.TEXT_NODE) {
                		s = n.getNodeValue();
                		break;
                	}
         			if ( n.getNodeType() == Node.CDATA_SECTION_NODE ) {
          				s = n.getNodeValue();
         				break;
         			}
                }
            }
        }
		return s==null ? "" : s;
/*		
		String s=null;
        if(node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList sub = node.getChildNodes();
            if (sub != null) {
                for (int i = 0; i < sub.getLength(); i++) {
                    s = getNodeString(sub.item(i));
                }
            }
        } else {
            if (node.getNodeValue() != null)
                s = node.getNodeValue();
        }
		return s==null ? "" : s;
*/		
	}//getNodeString

	public static int getNodeInt(Element elem, int defaultInt) {
		String s = getNodeString(elem);
		try {
			return Integer.parseInt(s);
		}
		catch (Exception e){
			return defaultInt;
		}
	}//getNodeInt
	
	public static int getNodeInt(Node node, int defaultInt) {
		String s = getNodeString(node);
		try {
			return Integer.parseInt(s);
		}
		catch (Exception e){
			return defaultInt;
		}
	}//getNodeInt
	
	public static String getChildNodeText(Element elem, String tagName) {
		Element element = getFirstChildren(elem, tagName);
		if(element == null) return "";
		
		String s=null;
		NodeList nodes = element.getChildNodes();
  		int len = nodes.getLength();
  		for ( int i = 0; i < len; i++ )	{
  			Node node = nodes.item(i);
 			if ( node.getNodeType() == Node.TEXT_NODE ) {
  				s = node.getNodeValue();
 				break;
 			}
	    }
		return s==null ? "" : s;
	}//getNodeValue
	
    public static String getNodeAllXmlText(Node member) {
        return getNodeAllXmlText(member, new StringBuffer()).toString();
    }

    private static StringBuffer getNodeAllXmlText(Node member, StringBuffer value) {
        if(member.getNodeType() == Node.ELEMENT_NODE) {
            NodeList sub = member.getChildNodes();
            if (sub != null) {
                for (int i = 0; i < sub.getLength(); i++) {
                    value = getNodeAllXmlText(sub.item(i), value);
                }
            }
        } else {
            if (member.getNodeValue() != null)
                value.append( member.getNodeValue().trim() );
        }
        return value;
    }//getTextValue
	
}
