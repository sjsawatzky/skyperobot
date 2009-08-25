package com.paulhimes.skylon.chatactions.tools;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlTools {

	private XmlTools() {

	}

	public static void setAttribute(Node node, String key, String value,
			Document document) {
		Attr name = document.createAttribute(key);
		name.setNodeValue(value);
		node.getAttributes().setNamedItem(name);
	}

	public static Node appendChild(Node parent, String name, Document document) {
		return parent.appendChild(document.createElement(name));
	}
}
