package com.paulhimes.skylon.chatactions;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paulhimes.skylon.xml.XmlParseException;

public class ChatActions {

	private List<ChatAction> chatActionList;

	public ChatActions(List<ChatAction> chatActionList) {
		this.chatActionList = chatActionList;
	}

	public ChatActions() {
		this(new ArrayList<ChatAction>());
	}

	public List<ChatAction> getChatActionList() {
		return chatActionList;
	}

	public void add(ChatAction action) {
		chatActionList.add(action);
	}

	public void remove(int index) {
		if (index >= 0 && index < chatActionList.size()) {
			chatActionList.remove(index);
		}
	}

	public void moveUp(int index) {
		if (index > 0 && index < chatActionList.size()) {
			chatActionList.add(index - 1, chatActionList.remove(index));
		}
	}

	public void moveDown(int index) {
		if (index >= 0 && index < chatActionList.size() - 1) {
			chatActionList.add(index + 1, chatActionList.remove(index));
		}
	}

	public Node encodeXml(Document document) {
		Node chatActionsNode = document.createElement("chatActions");

		for (ChatAction action : chatActionList) {
			// actions
			chatActionsNode.appendChild(action.encodeXml(document));
		}

		return chatActionsNode;
	}

	public static ChatActions parseXml(Element chatActionsNode) throws XmlParseException {
		if (chatActionsNode.getNodeName().equalsIgnoreCase("chatActions")) {

			List<ChatAction> actions = new ArrayList<ChatAction>();

			NodeList childNodes = chatActionsNode.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);

				try {
					if (childNode.getNodeName().equalsIgnoreCase("simpleReplyChatAction")) {
						actions.add(SimpleReplyChatAction.parseXml((Element) childNode));
					}
				} catch (XmlParseException xpe) {
					xpe.printStackTrace();
				}
			}

			return new ChatActions(actions);
		}

		throw new XmlParseException("chatActions");
	}

}
