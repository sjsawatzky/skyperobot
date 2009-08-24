package com.paulhimes.skylon.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.paulhimes.skylon.ChatAction;
import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.rules.Rule.RuleMatch;
import com.paulhimes.skylon.rules.Rule.RuleType;
import com.paulhimes.skylon.rules.Rules.RulesOperator;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class RuleWriter {

	public static Document document;

	public static void writeChatActions(ChatAction[] actions)
			throws ParserConfigurationException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.newDocument();

		Node actionsNode = appendChild(document, "actions");
		Node chatActions = appendChild(actionsNode, "chatactions");

		for (ChatAction action : actions) {
			chatActions.appendChild(action.encodeXml(document));
		}

		try {
			XMLSerializer output = new XMLSerializer(System.out,
					new OutputFormat(document));
			output.serialize(document);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {

		List<Rule> rulesList = new ArrayList<Rule>();
		rulesList.add(new Rule(RuleType.SENDER, RuleMatch.CONTAINS, "echo",
				false));
		rulesList.add(new Rule(RuleType.CONTENT, RuleMatch.ENDS_WITH,
				"\\(wave\\)", false));
		Rules rules = new Rules(RulesOperator.AND, rulesList);
		SimpleReplyChatAction action = new SimpleReplyChatAction("Wave Back",
				"Hello Worlds!", rules);

		try {
			writeChatActions(new ChatAction[] { action });
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setAttribute(Node node, String key, String value) {
		Attr name = document.createAttribute(key);
		name.setNodeValue(value);
		node.getAttributes().setNamedItem(name);
	}

	public static Node appendChild(Node parent, String name) {
		return parent.appendChild(document.createElement(name));
	}

	// TODO encode the rules object as an XML node
	public static Node encodeRules(Rules rules) {
		Node rulesNode = document.createElement("rules");
		setAttribute(rulesNode, "operator", rules.getOperator().name());
		List<Rule> ruleList = rules.getRules();
		for (Rule rule : ruleList) {
			Node ruleNode = encodeRule(rule);
			rulesNode.appendChild(ruleNode);
		}

		return rulesNode;
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
