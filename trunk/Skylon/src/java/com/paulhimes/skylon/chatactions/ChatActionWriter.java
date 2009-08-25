package com.paulhimes.skylon.chatactions;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.paulhimes.skylon.chatactions.tools.XmlTools;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ChatActionWriter {

	public static Document document;

	private ChatActionWriter() {

	}

	public static void writeActions(List<ChatAction> chatActions)
			throws ParserConfigurationException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.newDocument();

		Node actionsNode = XmlTools.appendChild(document, "actions", document);
		Node chatActionsNode = XmlTools.appendChild(actionsNode, "chatactions",
				document);

		for (ChatAction chatAction : chatActions) {
			chatActionsNode.appendChild(chatAction.encodeXml(document));
		}

		try {
			XMLSerializer output = new XMLSerializer(System.out,
					new OutputFormat(document));
			output.serialize(document);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
