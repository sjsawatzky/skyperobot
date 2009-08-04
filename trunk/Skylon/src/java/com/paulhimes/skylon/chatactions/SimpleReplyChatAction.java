package com.paulhimes.skylon.chatactions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.paulhimes.skylon.ChatAction;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SimpleReplyChatAction implements ChatAction {

	private String sender;
	private Pattern contentPattern;
	private String reply;

	public SimpleReplyChatAction(String sender, String content, String reply) {
		this.sender = sender;
		this.reply = reply;

		contentPattern = Pattern.compile(content, Pattern.DOTALL);
	}

	@Override
	public void received(ChatMessage message) throws SkypeException {
		Matcher matcher = contentPattern.matcher(message.getContent());
		if (matcher.matches()) {
			message.getChat().send(reply);
		}

		System.out.println("hello");
	}

	@Override
	public void sent(ChatMessage message) throws SkypeException {
		// TODO Auto-generated method stub

	}

}