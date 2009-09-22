package com.paulhimes.skylon;

import com.paulhimes.skylon.chatactions.ChatAction;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;

public final class ChatListener implements ChatMessageListener {

	public ChatListener() {
		// Listen for chat messages.
		try {
			Skype.addChatMessageListener(this);
		} catch (SkypeException x) {
			x.printStackTrace();
		}
	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage) throws SkypeException {
		for (ChatAction action : DataStore.getChatActions().getChatActionList()) {
			action.received(receivedChatMessage);
		}
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage) throws SkypeException {
		for (ChatAction action : DataStore.getChatActions().getChatActionList()) {
			action.sent(sentChatMessage);
		}
	}
}
