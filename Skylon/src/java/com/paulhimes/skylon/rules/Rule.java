package com.paulhimes.skylon.rules;

public class Rule {
	// <rule negativeFlag=false type=sender match=contains>paul</rule>
	private boolean negativeFlag = false;
	private RuleType type = RuleType.CONTENT;
	private RuleMatch match = RuleMatch.CONTAINS;
	private String value;

	public Rule(RuleType type, RuleMatch match, String value,
			boolean negativeFlag) {
		this.type = type;
		this.match = match;
		this.value = value;
		this.negativeFlag = negativeFlag;
	}

	public RuleMatch getMatch() {
		return match;
	}

	public RuleType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public boolean isNegativeFlag() {
		return negativeFlag;
	}

	public static enum RuleType {
		SENDER, CONTENT;
	}

	public static enum RuleMatch {
		CONTAINS, ENDS_WITH;
	}
}
