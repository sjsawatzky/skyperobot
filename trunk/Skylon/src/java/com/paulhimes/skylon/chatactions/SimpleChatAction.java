package com.paulhimes.skylon.chatactions;

import com.paulhimes.skylon.ChatAction;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SimpleChatAction implements ChatAction {

	@Override
	public void received(ChatMessage message) throws SkypeException {
		if (message.getContent().contains("(wave)")) {
			message.getChat().send("(yes)");
		}

		System.out.println("hello");
	}

	@Override
	public void sent(ChatMessage message) throws SkypeException {
		// TODO Auto-generated method stub

	}

}