package com.paulhimes.skylon.chatactions;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paulhimes.skylon.ChatListener;
import com.paulhimes.skylon.xml.XmlParseException;

public class ChatActions {

	private List<ChatAction> actions;
	private ChatListener listener;

	public ChatActions(List<ChatAction> actions) {
		this.actions = actions;
	}

	public ChatActions() {
		this(new ArrayList<ChatAction>());
	}

	public List<ChatAction> getActions() {
		return actions;
	}

	public void add(ChatAction action) {
		actions.add(action);

		registerCallback(listener);
	}

	public void remove(int index) {
		if (index >= 0 && index < actions.size()) {
			actions.remove(index);
		}
	}

	public void moveUp(int index) {
		if (index > 0 && index < actions.size()) {
			actions.add(index - 1, actions.remove(index));
		}
	}

	public void moveDown(int index) {
		if (index >= 0 && index < actions.size() - 1) {
			actions.add(index + 1, actions.remove(index));
		}
	}

	public void registerCallback(ChatListener listener) {
		this.listener = listener;

		for (ChatAction action : actions) {
			action.registerCallback(listener);
		}
	}

	public Node encodeXml(Document document) {
		Node chatActionsNode = document.createElement("chatActions");

		for (ChatAction action : actions) {
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
