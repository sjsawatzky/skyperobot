package com.paulhimes.skylon;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import com.paulhimes.skylon.chatactions.ChatActions;
import com.paulhimes.skylon.logging.Logger;

public final class NerveCenter {

	private final Logger logger = new Logger(getClass());

	private final ChatListener chatListener;
	private final TrayIcon trayIcon;

	public NerveCenter(TrayIcon trayIcon) {
		// Instantiate the ChatListener
		chatListener = new ChatListener(this);
		this.trayIcon = trayIcon;
		giveMessage("By your command!");
	}

	public void addChatActions(ChatActions actions) {
		chatListener.setChatActions(actions);
	}

	public void giveMessage(String message) {
		trayIcon.displayMessage("Event Triggered", message, MessageType.INFO);
	}
}
