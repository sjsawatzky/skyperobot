package com.paulhimes.skylon.chatactions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.paulhimes.skylon.tools.XmlTools;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ChatActionWriter {

	private ChatActionWriter() {

	}

	public static void writeActions(List<ChatAction> chatActions, File file) throws ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Node actionsNode = XmlTools.appendChild(document, "actions", document);
		Node chatActionsNode = XmlTools.appendChild(actionsNode, "chatactions", document);

		for (ChatAction chatAction : chatActions) {
			chatActionsNode.appendChild(chatAction.encodeXml(document));
		}

		try {
			FileOutputStream out = new FileOutputStream(file);
			XMLSerializer output = new XMLSerializer(out, new OutputFormat(document));
			output.serialize(document);
			out.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
