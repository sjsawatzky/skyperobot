package com.paulhimes.skylon.rules;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.paulhimes.skylon.rules.Rule.RuleMatch;
import com.paulhimes.skylon.rules.Rule.RuleType;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class RuleWriter {

	public static Document document;

	public static void writeRules() throws ParserConfigurationException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.newDocument();

		Node actions = appendChild(document, "actions");
		Node chatActions = appendChild(actions, "chatactions");
		Node simpleChatAction = createSimpleChatAction("Wave Action",
				"Hello World!");
		chatActions.appendChild(simpleChatAction);
		// Node simpleAction = appendChild(chatActions, "simplechataction");
		// setAttribute(simpleAction, "name", "Wave Action");

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

	private static void setAttribute(Node node, String key, String value) {
		Attr name = document.createAttribute(key);
		name.setNodeValue(value);
		node.getAttributes().setNamedItem(name);
	}

	private static Node appendChild(Node parent, String name) {
		return parent.appendChild(document.createElement(name));
	}

	private static Node createSimpleChatAction(String name, String reply) {
		// <simpleReplyAction name="Wave Action">
		// <reply>Hello World!</reply>
		// <rules operator=AND>
		// <rule negativeFlag=false type=sender match=contains>paul</rule>
		// <rule negativeFlag=false type=content match=ends with>(wave)</rule>
		// </rules>
		// </action>

		Node simpleChatAction = document.createElement("simplechataction");
		setAttribute(simpleChatAction, "name", name);

		// reply
		Node replyNode = appendChild(simpleChatAction, "reply");
		replyNode.setTextContent(reply);

		// rules
		Node rules = appendChild(simpleChatAction, "rules");
		setAttribute(rules, "operator", "AND");

		rules.appendChild(encodeRule(new Rule(RuleType.SENDER,
				RuleMatch.CONTAINS, "paul", false)));

		rules.appendChild(encodeRule(new Rule(RuleType.CONTENT,
				RuleMatch.ENDS_WITH, "(wave)", false)));

		return simpleChatAction;
	}

	// TODO encode the rules object as an XML node
	private static Node encodeRules(Rules rules) {
		return document.createElement("rules");
	}

	private static Node encodeRule(Rule rule) {
		Node ruleNode = document.createElement("rule");

		setAttribute(ruleNode, "type", rule.getType().name());
		setAttribute(ruleNode, "match", rule.getMatch().name());
		setAttribute(ruleNode, "negativeFlag", String.valueOf(rule
				.isNegativeFlag()));
		ruleNode.setTextContent(rule.getValue());

		return ruleNode;
	}
}
