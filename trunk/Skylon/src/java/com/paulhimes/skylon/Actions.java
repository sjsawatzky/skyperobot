package com.paulhimes.skylon;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActions;

public class Actions {

	private ChatActions chatActions;

	public Actions(ChatActions chatActions) {
		this.chatActions = chatActions;
	}

	public ChatActions getChatActions() {
		return chatActions;
	}

	public static Actions parseXml(Element actionsNode) throws XmlParseException {
		if (actionsNode.getNodeName().equalsIgnoreCase("actions")) {

			ChatActions chatActions = new ChatActions(new ArrayList<ChatAction>());
			try {
				// Get the chat actions node.
				NodeList chatActionsNodes = actionsNode.getElementsByTagName("chatactions");
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
