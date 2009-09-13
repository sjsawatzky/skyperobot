package com.paulhimes.skylon.chatactions.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paulhimes.skylon.tools.RegexBuilder;
import com.paulhimes.skylon.tools.XmlTools;

public class Rule {
	// <rule negativeFlag=false type=sender match=contains>paul</rule>
	private boolean negativeFlag = false;
	private RuleType type = RuleType.CONTENT;
	private RuleMatch match = RuleMatch.CONTAINS;
	private String value;

	private Pattern pattern;

	public Rule(RuleType type, RuleMatch match, String value, boolean negativeFlag) {
		this.type = type;
		this.match = match;
		this.value = value;
		this.negativeFlag = negativeFlag;

		updatePattern();
	}

	private void updatePattern() {
		String regexString = "";
		switch (match) {
		case CONTAINS:
			regexString = RegexBuilder.contains(value);
			break;
		case ENDS_WITH:
			regexString = RegexBuilder.endsWith(value);
			break;
		default:
			break;
		}

		pattern = Pattern.compile(regexString, Pattern.DOTALL);
	}

	public Rule() {
		this(RuleType.CONTENT, RuleMatch.CONTAINS, "", false);
	}

	public RuleMatch getMatch() {
		return match;
	}

	public void setMatch(RuleMatch match) {
		this.match = match;

		updatePattern();
	}

	public RuleType getType() {
		return type;
	}

	public void setType(RuleType type) {
		this.type = type;

		updatePattern();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;

		updatePattern();
	}

	public boolean isNegativeFlag() {
		return negativeFlag;
	}

	public static enum RuleType {
		SENDER("Sender"), CONTENT("Content");

		private final String displayString;

		private RuleType(String displayString) {
			this.displayString = displayString;
		}

		public String toString() {
			return displayString;
		};
	}

	public static enum RuleMatch {
		CONTAINS("contains"), ENDS_WITH("ends with");

		private final String displayString;

		private RuleMatch(String displayString) {
			this.displayString = displayString;
		}

		public String toString() {
			return displayString;
		};
	}

	public boolean matches(String senderId, String content) {
		String target;

		switch (type) {
		case CONTENT:
			target = content;
			break;
		case SENDER:
			target = senderId;
		default:
			target = senderId;
			break;
		}

		Matcher matcher = pattern.matcher(target);

		return matcher.matches() ^ negativeFlag;
	}

	public Node encodeXml(Document document) {
		Node ruleNode = document.createElement("rule");

		XmlTools.setAttribute(ruleNode, "type", getType().name(), document);
		XmlTools.setAttribute(ruleNode, "match", getMatch().name(), document);
		XmlTools.setAttribute(ruleNode, "negativeFlag", String.valueOf(isNegativeFlag()), document);
		ruleNode.setTextContent(getValue());

		return ruleNode;
	}

	public static List<Rule> parseXml(Element parent) {
		List<Rule> rulesList = new ArrayList<Rule>();

		NodeList ruleNodes = parent.getElementsByTagName("rule");
		for (int r = 0; r < ruleNodes.getLength(); r++) {
			Element rule = (Element) ruleNodes.item(r);
			RuleMatch match = RuleMatch.valueOf(rule.getAttribute("match"));
			boolean negativeFlag = Boolean.parseBoolean(rule.getAttribute("negativeFlag"));
			RuleType type = RuleType.valueOf(rule.getAttribute("type"));
			String content = rule.getTextContent();
			rulesList.add(new Rule(type, match, content, negativeFlag));
		}

		return rulesList;
	}
}
