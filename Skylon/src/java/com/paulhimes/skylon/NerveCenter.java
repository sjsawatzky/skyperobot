package com.paulhimes.skylon;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import com.paulhimes.skylon.logging.Logger;

public final class NerveCenter {

	@SuppressWarnings("unused")
	private final Logger logger = new Logger(getClass());

	private final TrayIcon trayIcon;

	public NerveCenter(TrayIcon trayIcon) {
		// Instantiate the ChatListener
		new ChatListener();
		this.trayIcon = trayIcon;

		giveMessage("By your command!");
	}

	public void giveMessage(String message) {
		trayIcon.displayMessage("Event Triggered", message, MessageType.INFO);
	}
}
