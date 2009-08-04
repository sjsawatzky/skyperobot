package com.paulhimes.skylon;

import java.util.List;

public final class NerveCenter {

	private ChatListener chatListener;

	public NerveCenter() {
		// Instantiate the ChatListener
		chatListener = new ChatListener();
	}

	public void addChatActions(List<ChatAction> actions) {
		for (ChatAction action : actions) {
			chatListener.add(action);
		}
	}
}
