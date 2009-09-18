package com.paulhimes.skylon.chatactions;

import java.util.ArrayList;

import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public enum ChatActionType {
	SIMPLE_REPLY_CHAT_ACTION("Simple Reply Chat Action") {
		public ChatAction instantiate() {
			return new SimpleReplyChatAction("No Name", "", new Rules(RulesOperator.AND, new ArrayList<Rule>()));
		}
	};

	private final String displayString;

	private ChatActionType(String displayString) {
		this.displayString = displayString;
	}

	@Override
	public String toString() {
		return displayString;
	}

	public abstract ChatAction instantiate();
}
