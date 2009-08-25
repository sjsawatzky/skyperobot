package com.paulhimes.skylon.chatactions;

import java.util.ArrayList;
import java.util.List;

import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public final class ChatActionReader {

	private ChatActionReader() {

	}

	public static List<ChatAction> readActions() {
		// Load all the actions
		List<ChatAction> actions = new ArrayList<ChatAction>();

		List<Rule> rulesList = new ArrayList<Rule>();
		rulesList.add(new Rule(RuleType.SENDER, RuleMatch.CONTAINS, "echo",
				false));
		rulesList.add(new Rule(RuleType.CONTENT, RuleMatch.ENDS_WITH,
				"\\(wave\\)", false));
		Rules rules = new Rules(RulesOperator.AND, rulesList);
		actions.add(new SimpleReplyChatAction("Wave Back", "Hello World!",
				rules));

		return actions;
	}
}
