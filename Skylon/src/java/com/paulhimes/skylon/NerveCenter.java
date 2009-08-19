package com.paulhimes.skylon;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.List;

public final class NerveCenter {

	private final ChatListener chatListener;
	private final TrayIcon trayIcon;

	public NerveCenter(TrayIcon trayIcon) {
		// Instantiate the ChatListener
		chatListener = new ChatListener(this);
		this.trayIcon = trayIcon;
		giveMessage("By your command!");
	}

	public void addChatActions(List<ChatAction> actions) {
		for (ChatAction action : actions) {
			chatListener.add(action);
		}
	}

	public void giveMessage(String message) {
		trayIcon.displayMessage("Event Triggered", message, MessageType.INFO);
	}
}
