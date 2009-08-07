package com.paulhimes.skylon;

import java.util.ArrayList;
import java.util.List;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;

public final class ChatListener implements ChatMessageListener {

	private final List<ChatAction> actions = new ArrayList<ChatAction>();
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

	public void add(ChatAction action) {
		action.registerCallback(this);
		actions.add(action);

	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		for (ChatAction action : actions) {
			action.received(receivedChatMessage);
		}
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		for (ChatAction action : actions) {
			action.sent(sentChatMessage);
		}
	}

	public void giveMessage(String message) {
		nerveCenter.giveMessage("Chat Action: " + message);
	}
}
