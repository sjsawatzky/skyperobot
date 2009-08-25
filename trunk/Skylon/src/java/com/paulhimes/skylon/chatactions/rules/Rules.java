package com.paulhimes.skylon.chatactions.rules;

import java.util.ArrayList;
import java.util.List;

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
}
