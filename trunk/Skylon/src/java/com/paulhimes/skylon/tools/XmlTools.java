package com.paulhimes.skylon.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.paulhimes.skylon.XmlParseException;

public class XmlTools {

	private XmlTools() {

	}

	public static void setAttribute(Node node, String key, String value, Document document) {
		Attr name = document.createAttribute(key);
		name.setNodeValue(value);
		node.getAttributes().setNamedItem(name);
	}

	public static Node appendChild(Node parent, String name, Document document) {
		return parent.appendChild(document.createElement(name));
	}

	public static Element getElementFromFile(File file, String elementName) throws XmlParseException {
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

			// Element rootNode = document.getDocumentElement();
			// System.out.println(rootNode.getNodeName());

			NodeList actionsNodes = document.getElementsByTagName(elementName);
			return (Element) actionsNodes.item(0);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		throw new XmlParseException(file.getAbsolutePath());
	}
}
