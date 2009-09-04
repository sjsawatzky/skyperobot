package com.paulhimes.skylon;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import com.paulhimes.skylon.logging.Logger;

public final class NerveCenter {

	private final Logger logger = new Logger(getClass());

	private final Actions actions;
	private final ChatListener chatListener;
	private final TrayIcon trayIcon;

	public NerveCenter(TrayIcon trayIcon, Actions actions) {
		// Instantiate the ChatListener
		chatListener = new ChatListener(this);
		this.trayIcon = trayIcon;
		this.actions = actions;

		chatListener.setChatActions(actions.getChatActions());
		giveMessage("By your command!");
	}

	public void giveMessage(String message) {
		trayIcon.displayMessage("Event Triggered", message, MessageType.INFO);
	}

	public Actions getActions() {
		return actions;
	}
}
