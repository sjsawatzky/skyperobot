package com.paulhimes.skylon.chatactions;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

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

		Node simpleChatAction = document.createElement("simplechataction");
		XmlTools.setAttribute(simpleChatAction, "name", getName(), document);

		// reply
		Node replyNode = XmlTools.appendChild(simpleChatAction, "reply",
				document);
		replyNode.setTextContent(getReply());

		// rules
		simpleChatAction.appendChild(getRules().encodeXml(document));

		return simpleChatAction;
	}
}