package com.paulhimes.skylon.chatactions.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.paulhimes.skylon.chatactions.tools.XmlTools;

public class Rules {

	private RulesOperator operator = RulesOperator.AND;
	private List<Rule> rules = new ArrayList<Rule>();

	public Rules(RulesOperator operator, List<Rule> rulesList) {
		this.operator = operator;
		this.rules = rulesList;
	}

	public static enum RulesOperator {
		AND, OR;
	}

	public RulesOperator getOperator() {
		return operator;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public boolean matches(String senderId, String content) {
		switch (operator) {
		case AND:
			return matchAll(senderId, content);
		case OR:
			return matchAny(senderId, content);
		default:
			return matchAll(senderId, content);
		}
	}

	private boolean matchAll(String senderId, String content) {
		System.out.println("Matching all");
		for (Rule rule : rules) {
			if (!rule.matches(senderId, content)) {
				return false;
			}
		}
		return true;
	}

	private boolean matchAny(String senderId, String content) {
		System.out.println("Matching any");
		for (Rule rule : rules) {
			if (rule.matches(senderId, content)) {
				return true;
			}
		}
		return false;
	}

	public Node encodeXml(Document document) {
		Node rulesNode = document.createElement("rules");
		XmlTools.setAttribute(rulesNode, "operator", getOperator().name(),
				document);
		List<Rule> ruleList = getRules();
		for (Rule rule : ruleList) {
			Node ruleNode = rule.encodeXml(document);
			rulesNode.appendChild(ruleNode);
		}

		return rulesNode;
	}
}
