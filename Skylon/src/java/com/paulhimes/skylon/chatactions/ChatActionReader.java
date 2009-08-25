package com.paulhimes.skylon.chatactions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public final class ChatActionReader {

	private ChatActionReader() {

	}

	public static List<ChatAction> readActions(File file) {

		try {
			Document document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(file);

			Element rootNode = document.getDocumentElement();
			System.out.println(rootNode.getNodeName());

			NodeList chatActionsNodes = rootNode
					.getElementsByTagName("chatactions");

			Element chatActionsNode = (Element) chatActionsNodes.item(0);
			NodeList simpleChatActionNodes = chatActionsNode
					.getElementsByTagName("simplechataction");

			Element simpleChatActionNode = (Element) simpleChatActionNodes
					.item(0);

			System.out.println(simpleChatActionNode.getAttribute("name"));

			NodeList replyNodes = simpleChatActionNode
					.getElementsByTagName("reply");
			Element replyNode = (Element) replyNodes.item(0);

			NodeList rulesNodes = simpleChatActionNode
					.getElementsByTagName("rules");
			Element rulesNode = (Element) rulesNodes.item(0);

			String operatorString = rulesNode.getAttribute("operator");
			RulesOperator operator = RulesOperator.valueOf(operatorString);

			List<Rule> rulesList = new ArrayList<Rule>();

			NodeList ruleNodes = rulesNode.getElementsByTagName("rule");
			for (int i = 0; i < ruleNodes.getLength(); i++) {
				Element rule = (Element) ruleNodes.item(i);
				RuleMatch match = RuleMatch.valueOf(rule.getAttribute("match"));
				boolean negativeFlag = Boolean.parseBoolean(rule
						.getAttribute("negativeFlag"));
				RuleType type = RuleType.valueOf(rule.getAttribute("type"));
				String content = rule.getTextContent();
				rulesList.add(new Rule(type, match, content, negativeFlag));
			}

			SimpleReplyChatAction chatAction = new SimpleReplyChatAction(
					simpleChatActionNode.getAttribute("name"), replyNode
							.getTextContent(), new Rules(operator, rulesList));

			List<ChatAction> actions = new ArrayList<ChatAction>();
			actions.add(chatAction);

			return actions;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<ChatAction>();
		// // Load all the actions
		// List<ChatAction> actions = new ArrayList<ChatAction>();
		//
		// List<Rule> rulesList = new ArrayList<Rule>();
		// rulesList.add(new Rule(RuleType.SENDER, RuleMatch.CONTAINS, "echo",
		// false));
		// rulesList.add(new Rule(RuleType.CONTENT, RuleMatch.ENDS_WITH,
		// "\\(wave\\)", false));
		// Rules rules = new Rules(RulesOperator.AND, rulesList);
		// actions.add(new SimpleReplyChatAction("Wave Back", "Hello World!",
		// rules));
		//
		// return actions;
	}
}
