package com.paulhimes.skylon.logging;

import java.util.logging.ConsoleHandler;

public class Logger {

	private final java.util.logging.Logger delegate;

	public Logger(Class<?> clazz) {
		delegate = java.util.logging.Logger.getLogger(clazz.getName());
		delegate.setUseParentHandlers(false);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new LogFormatter());
		delegate.addHandler(consoleHandler);
	}

	public void info(String message) {
		delegate.info(message);
	}

	public void fine(String message) {
		delegate.fine(message);
	}
}
