/**
 * Project:		隆典-Web應用系統開發平台專案
 * Filename: 	XmlHandler.java
 * Description:	
 * Author: 		吳家瑞
 * Create Date:	2006/1/12
 * Version: 	
 */
package util.function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xpath.internal.XPathAPI;


/**
 * 幫助取得xml中的值
 * @author 吳家瑞
 *
 */
public class XmlHandler {
    private static final Log logger = LogFactory.getLog(XmlHandler.class);
    private DOMParser parser;
    private Document document;

    /**
     * 建立一個新的 <code>XmlHandler</code>物件
     *
     * @param file 傳入XML檔案
     * @throws FileNotFoundException
     */
    public XmlHandler(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }
    
    /**
     * 建立一個新的 <code>XmlHandler</code>物件
     *
     * @param string 傳入XML字串
     */
    public XmlHandler(String string) {
        this(new StringReader(string));
    }
    
    /**
     * 建立一個新的 <code>XmlHandler</code>物件
     *
     * @param reader 傳入XML character stream
     */
    public XmlHandler(Reader reader) {
        try {
            parser = new DOMParser();
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            parser.setFeature("http://apache.org/xml/features/validation/dynamic", false);
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            parser.parse(new InputSource(reader));
            reader.close();
            
            document = parser.getDocument();
        } catch (Exception e) {
            logger.error("", e);
        }
    }
    
	//*********************************************************
	//*	屬性存取 method
	//*********************************************************
    public Document getDocument() {
        return document;
    }
    
	//*********************************************************
	//*	公用 method
	//*********************************************************
    /**
     * 取得指定tag名稱的第一個內容值
     *
     * @param path
     * @return
     */
    public String getValue(String path) {
        String[] s = getValues(path);
        if (s.length > 0)
            return s[0];
        return "";
    }//getValue

    /**
     * 取得指定xml node裡面的tag名稱的第一個內容值
     *
     * @param node
     * @param path
     * @return
     */
    public static String getValue(Node node, String path) {
        String[] s = getValues(node, path);
        if (s.length > 0)
            return s[0];
        return "";
    }//getValue

    /**
     * 取得指定tag名稱的所有內容值
     *
     * @param path
     * @return
     */
    public String[] getValues(String path) {
        return getValues(document.getDocumentElement(), path);
    }//getValues
    
    /**
     * 取得指定xml node裡面的tag名稱的所有內容值
     *
     * @param node
     * @param path
     * @return
     */
    public static String[] getValues(Node node, String path) {
        String[] nodeValues = new String[0];
        int n;

        try {
            NodeList nl = XPathAPI.selectNodeList(node, path);
            n = nl.getLength();
            String data;

//            if (n > 1) {
                nodeValues = new String[n];
                for (int i = 0; i < n; i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
                        data = getTextValue((Element)nl.item(i));
                    else if (nl.item(i).getNodeType() == Node.ATTRIBUTE_NODE)
                        data = ((Attr)nl.item(i)).getValue();
                    else
                        data = ""; 
                    if (data == null) data = "";
                    nodeValues[i] = data;
                }
//            } else if (n > 0) {
//                if (nl.item(0).getNodeType() == Node.ELEMENT_NODE)
//                    data = getTextValue((Element)nl.item(0));
//                else if (nl.item(0).getNodeType() == Node.ATTRIBUTE_NODE) {
//                    data = ((Attr)nl.item(0)).getValue();
//                } else
//                    data = "";
//                nodeValues = new String[]{data};
//            }
        } catch (TransformerException e) {
            logger.error("", e);
        }
        return nodeValues;
    }//getValues
    
    /**
     * 取得指定xml node 的文字內容
     *
     * @param member
     * @return
     */
    public static String getTextValue(Node member) {
        return getTextValue(member, new StringBuffer()).toString();
    }
    
    /**
     * 取得指定node 的文字內容至StringBuffer物件
     *
     * @param member
     * @param value
     * @return
     */
    private static StringBuffer getTextValue(Node member, StringBuffer value) {
        if(member.getNodeType() == Node.ELEMENT_NODE) {
            NodeList sub = member.getChildNodes();
            if (sub != null) {
                for (int i = 0; i < sub.getLength(); i++) {
                    value = getTextValue(sub.item(i), value);
                }
            }
        } else {
            if (member.getNodeValue() != null)
                value.append( member.getNodeValue().trim() );
        }
        return value;
    }//getTextValue
    
    /**
     * 取得指定 tag名稱下的所有 node
     *
     * @param path
     * @return
     */
    public Node[] getNodes(String path) {
        return getNodes(document.getDocumentElement(), path);
    }
    
    /**
     * 取得指定node裡面的 tag名稱下的所有node
     *
     * @param node
     * @param path
     * @return
     */
    public static Node[] getNodes(Node node, String path) {
        Node[] nodes = new Node[0];

        try {
            NodeList nl = XPathAPI.selectNodeList(node, path);
            int n = nl.getLength();
            nodes = new Node[nl.getLength()];
            for (int i = 0; i < n; i++)
                nodes[i] = nl.item(i);
        } catch (TransformerException e) {
            logger.error("", e);
        }
        return nodes;
    }

    /**
     * 取得指定tag名稱的xml node
     *
     * @param path
     * @return
     */
    public Node getNode(String path) {
        Node[] n = getNodes(path);
        if (n.length > 0)
            return n[0];
        return null;
    }//getNode
    
    /**
     * 取得指定node下的tag名稱的node
     *
     * @param node
     * @param path
     * @return
     */
    public static Node getNode(Node node, String path) {
        Node[] n = getNodes(node, path);
        if (n.length > 0)
            return n[0];
        return null;
    }
}
