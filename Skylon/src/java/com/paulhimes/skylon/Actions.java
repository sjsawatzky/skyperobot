package com.paulhimes.skylon;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActions;
import com.paulhimes.skylon.xml.XmlModel;
import com.paulhimes.skylon.xml.XmlParseException;

public class Actions implements XmlModel {

	private ChatActions chatActions;

	public Actions(ChatActions chatActions) {
		this.chatActions = chatActions;
	}

	public Actions() {
		this.chatActions = new ChatActions();
	}

	public ChatActions getChatActions() {
		return chatActions;
	}

	public Node encodeXml(Document document) {
		Node actionsNode = document.createElement("actions");
		actionsNode.appendChild(chatActions.encodeXml(document));

		return actionsNode;
	}

	public static Actions parseXml(Element actionsNode) throws XmlParseException {
		if (actionsNode.getNodeName().equalsIgnoreCase("actions")) {

			ChatActions chatActions = new ChatActions(new ArrayList<ChatAction>());
			try {
				// Get the chat actions node.
				NodeList chatActionsNodes = actionsNode.getElementsByTagName("chatActions");
				Element chatActionsNode = (Element) chatActionsNodes.item(0);
				chatActions = ChatActions.parseXml(chatActionsNode);
			} catch (XmlParseException xpe) {
				xpe.printStackTrace();
			}

			return new Actions(chatActions);
		}

		throw new XmlParseException("actions");
	}
}
