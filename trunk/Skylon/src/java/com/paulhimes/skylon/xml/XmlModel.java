package com.paulhimes.skylon.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface XmlModel {
	public Node encodeXml(Document document);
}
