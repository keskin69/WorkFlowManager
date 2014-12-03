package manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.Util;
import util.Util.MessageType;

public class FlowDefinition extends ArrayList<NodeDefinition> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6935518178414141984L;
	public String manager = null;

	@SuppressWarnings("unused")
	public FlowDefinition(String file) {
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document doc = null;
		NodeList nList = null;
		Node nNode = null;
		Element eElement = null;

		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		nList = doc.getElementsByTagName("manager");
		nNode = nList.item(0);

		eElement = (Element) nNode;
		manager = eElement.getAttribute("class");

		nList = doc.getElementsByTagName("node");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			nNode = nList.item(temp);
			NodeDefinition wNode = new NodeDefinition(nNode);

			if (Util.DEBUG_LEVEL <= 0) {
				System.out.println("Node detected " + wNode.name);
			}

			add(wNode);
		}
	}

	public NodeDefinition getStart() {
		return get(0);
	}

	public NodeDefinition findNodeByName(String nodeName) {
		for (NodeDefinition node : this) {
			if (node.name.equals(nodeName)) {
				return node;
			}
		}

		return null;
	}

	public NodeDefinition findNodeByClass(String className) {
		for (NodeDefinition node : this) {
			if (node.className.equals(className)) {
				return node;
			}
		}

		return null;
	}

	public NodeDefinition getNextNode(String nodeName, MessageType msgType) {
		String nextNode = null;

		NodeDefinition node = findNodeByClass(nodeName);

		// check all directions values
		for (String key : node.direction.keySet()) {
			nextNode = key;
			
			if (node.direction.get(key).equals(msgType.toString())) {
				return findNodeByName(nextNode);
			}
		}

		return findNodeByName(nextNode);
	}
}
