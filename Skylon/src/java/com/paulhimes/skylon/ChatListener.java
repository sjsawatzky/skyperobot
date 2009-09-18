package com.paulhimes.skylon;

import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActions;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;

public final class ChatListener implements ChatMessageListener {

	private ChatActions actions = new ChatActions();
	private final NerveCenter nerveCenter;

	public ChatListener(NerveCenter nerveCenter) {

		this.nerveCenter = nerveCenter;

		// Listen for chat messages.
		try {
			Skype.addChatMessageListener(this);
		} catch (SkypeException x) {
			x.printStackTrace();
		}
	}

	public void setChatActions(ChatActions actions) {
		this.actions = actions;
		actions.registerCallback(this);
	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage) throws SkypeException {
		for (ChatAction action : actions.getActions()) {
			action.received(receivedChatMessage);
		}
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage) throws SkypeException {
		for (ChatAction action : actions.getActions()) {
			action.sent(sentChatMessage);
		}
	}

	public void giveMessage(String message) {
		nerveCenter.giveMessage("Chat Action: " + message);
	}
}
