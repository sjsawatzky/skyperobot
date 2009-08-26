package com.paulhimes.skylon.chatactions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class ChatActionReader {

	private ChatActionReader() {

	}

	public static List<ChatAction> readActions(File file) {

		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

			Element rootNode = document.getDocumentElement();
			System.out.println(rootNode.getNodeName());

			NodeList chatActionsNodes = rootNode.getElementsByTagName("chatactions");

			Element chatActionsNode = (Element) chatActionsNodes.item(0);

			return SimpleReplyChatAction.parseXml(chatActionsNode);

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

		return new ArrayList<ChatAction>();
	}
}
