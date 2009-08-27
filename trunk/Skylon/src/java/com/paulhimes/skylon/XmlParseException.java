package com.paulhimes.skylon;

public class XmlParseException extends Exception {

	public XmlParseException(String message) {
		super("Failed to parse: " + message);
	}
}
