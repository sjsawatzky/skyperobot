package com.paulhimes.skylon;

public class XmlParseException extends Exception {

	private static final long serialVersionUID = -308682243974912602L;

	public XmlParseException(String message) {
		super("Failed to parse: " + message);
	}
}
