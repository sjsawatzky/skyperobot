package com.paulhimes.skylon.chatactions;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.paulhimes.skylon.Actions;
import com.paulhimes.skylon.XmlParseException;

public final class ChatActionReader {

	private ChatActionReader() {

	}

	public static Actions readActions(File file) throws XmlParseException {

		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

			NodeList actionsNodes = document.getElementsByTagName("actions");
			Element actionsNode = (Element) actionsNodes.item(0);

			return Actions.parseXml(actionsNode);
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
