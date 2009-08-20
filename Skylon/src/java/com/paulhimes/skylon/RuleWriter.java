package com.paulhimes.skylon;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class RuleWriter {

	public static void writeRules() throws ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		Element root = document.createElement("actions");
		document.appendChild(root);

		Node chatActions = root.appendChild(document
				.createElement("chatactions"));
		Node simpleAction = chatActions.appendChild(document
				.createElement("simplechataction"));
		try {
			XMLSerializer output = new XMLSerializer(System.out,
					new OutputFormat(document));
			output.serialize(document);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		try {
			writeRules();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
