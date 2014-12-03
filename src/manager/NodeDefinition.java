package manager;

import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.Characteristic;

public class NodeDefinition {
	public String name;
	public String className;
	public String title;
	public boolean healthy = false;
	public Vector<Characteristic> attributes = null;
	public HashMap<String, String> direction = null;

	public NodeDefinition(Node node) {
		direction = new HashMap<String, String>();

		className = node.getNodeName();
		Element eElement = null;
		NodeList nList = null;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			eElement = (Element) node;
			name = eElement.getAttribute("name");
			className = eElement.getElementsByTagName("class").item(0)
					.getTextContent();

			// connection definition
			nList = (NodeList) eElement.getElementsByTagName("connect");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node n = nList.item(temp);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;

					direction.put(n.getTextContent(), e.getAttribute("value")
							.toUpperCase());
				}
			}

			title = eElement.getElementsByTagName("title").item(0)
					.getTextContent();

			attributes = new Vector<Characteristic>();
			nList = (NodeList) eElement.getElementsByTagName("gui").item(0);

			if (nList != null) {
				parseAttributes(nList);
			}
		}
	}

	public void parseAttributes(NodeList nList) {
		Element eElement = null;
		Characteristic att = null;

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node n = nList.item(temp);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) n;

				att = new Characteristic();
				att.name = eElement.getAttribute("name");
				att.value = eElement.getAttribute("value");
				att.mandatory = (eElement.getAttribute("mandatory").equals(
						"yes") ? true : false);
				att.displayOnly = (eElement.getAttribute("input").equals("yes") ? false
						: true);

				att.type = n.getNodeName();

				attributes.add(att);
			}
		}
	}
}
