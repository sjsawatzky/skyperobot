package com.paulhimes.skylon.rules;

import java.util.ArrayList;
import java.util.List;

public class Rules {

	private RulesOperator operator = RulesOperator.AND;
	private final List<Rule> rules = new ArrayList<Rule>();

	public static enum RulesOperator {
		AND, OR;
	}
}
