package com.paulhimes.skylon.chatactions;

import java.util.regex.*;

import com.paulhimes.skylon.ChatAction;
import com.skype.*;

public class SimpleReplyChatAction implements ChatAction {

	private final String actionName;
	private final Pattern senderPattern;
	private final Pattern contentPattern;
	private final String reply;

	public SimpleReplyChatAction(String actionName, String sender, String content, String reply) {
		this.actionName = actionName;
		senderPattern = Pattern.compile(sender, Pattern.DOTALL);
		contentPattern = Pattern.compile(content, Pattern.DOTALL);
		this.reply = reply;
	}

	@Override
	public void received(ChatMessage message) throws SkypeException {
		Matcher senderMatcher = senderPattern.matcher(message.getSenderId());
		Matcher contentMatcher = contentPattern.matcher(message.getContent());
		if (senderMatcher.matches() && contentMatcher.matches()) {
			System.out.println("triggered");
			message.getChat().send(reply);
		} else {
			System.out.println("contentMatches=" + contentMatcher.matches());
			System.out.println("senderMatches=" + senderMatcher.matches());
		}

		System.out.println("hello");
	}

	@Override
	public void sent(ChatMessage message) throws SkypeException {
	}

}