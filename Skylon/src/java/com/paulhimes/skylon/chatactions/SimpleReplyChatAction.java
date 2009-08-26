package com.paulhimes.skylon.chatactions;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paulhimes.skylon.ChatListener;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.tools.XmlTools;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SimpleReplyChatAction implements ChatAction {

	private final String name;
	private final String reply;
	private final Rules rules;

	private ChatListener chatListener;

	public SimpleReplyChatAction(String name, String reply, Rules rules) {
		this.name = name;
		this.reply = reply;
		this.rules = rules;
	}

	@Override
	public void received(ChatMessage message) throws SkypeException {

		String senderId = message.getSenderId();
		String content = message.getContent();

		if (rules.matches(senderId, content)) {
			chatListener.giveMessage(name);
			message.getChat().send(reply);
		}

		System.out.println("Tested " + name);
	}

	@Override
	public void sent(ChatMessage message) throws SkypeException {
	}

	@Override
	public void registerCallback(ChatListener chatListener) {
		this.chatListener = chatListener;
	}

	public String getName() {
		return name;
	}

	public Rules getRules() {
		return rules;
	}

	public String getReply() {
		return reply;
	}

	@Override
	public Node encodeXml(Document document) {
		// <simpleReplyAction name="Wave Action">
		// <reply>Hello World!</reply>
		// <rules operator=AND>
		// <rule negativeFlag=false type=sender match=contains>paul</rule>
		// <rule negativeFlag=false type=content match=ends with>(wave)</rule>
		// </rules>
		// </action>

		Node simpleChatAction = document.createElement("simplereplychataction");
		XmlTools.setAttribute(simpleChatAction, "name", getName(), document);

		// reply
		Node replyNode = XmlTools.appendChild(simpleChatAction, "reply", document);
		replyNode.setTextContent(getReply());

		// rules
		simpleChatAction.appendChild(getRules().encodeXml(document));

		return simpleChatAction;
	}

	public static List<ChatAction> parseXml(Element parent) {
		List<ChatAction> chatActions = new ArrayList<ChatAction>();

		NodeList simpleReplyChatActionNodes = parent.getElementsByTagName("simplereplychataction");

		for (int i = 0; i < simpleReplyChatActionNodes.getLength(); i++) {

			Element simpleReplyChatActionNode = (Element) simpleReplyChatActionNodes.item(i);

			// Get the action name.
			String name = simpleReplyChatActionNode.getAttribute("name");

			// Get the reply text.
			NodeList replyNodes = simpleReplyChatActionNode.getElementsByTagName("reply");
			Element replyNode = (Element) replyNodes.item(0);
			String replyText = replyNode.getTextContent();

			// Get the rules.
			Rules rules = Rules.parseXml(simpleReplyChatActionNode);

			chatActions.add(new SimpleReplyChatAction(name, replyText, rules));
		}

		return chatActions;
	}
}