package com.paulhimes.skylon.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public class EditSimpleReplyChatAction {

	private final JTextField nameField = new JTextField();
	private final JTextField replyField = new JTextField();
	private final SimpleReplyChatAction action;

	public EditSimpleReplyChatAction(SimpleReplyChatAction action) {
		this.action = action;

		JFrame frame = new JFrame("Skylon - Edit SimpleReplyChatAction");

		// Initialize components
		nameField.setText(action.getName());
		nameField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				updateAction();
			}
		});
		replyField.setText(action.getReply());
		replyField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				updateAction();
			}
		});

		// Layout all the components in the content pane
		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.ipadx = 10;
		c.ipady = 10;
		contentPanel.add(new JLabel("Name"), c);

		c.gridx = 1;
		c.weightx = 1;
		contentPanel.add(nameField, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		contentPanel.add(new JLabel("Reply"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		contentPanel.add(replyField, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		contentPanel.add(new JLabel("Rules Operator"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		contentPanel.add(new JComboBox(RulesOperator.values()), c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		contentPanel.add(new JLabel("Rules"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		contentPanel.add(new JButton("Add"), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JTable rulesTable = new JTable(new RulesTableModel(action.getRules()));
		contentPanel.add(new JScrollPane(rulesTable), c);

		frame.add(contentPanel);

		frame.pack();
		frame.setVisible(true);
	}

	private void updateAction() {
		setName(nameField.getText());
		setReply(replyField.getText());
	}

	private void setReply(String reply) {
		action.setReply(reply);
	}

	private void setName(String name) {
		action.setName(name);
	}

	public static void main(String[] args) {
		new EditSimpleReplyChatAction(new SimpleReplyChatAction("name", "reply", new Rules(RulesOperator.AND, new ArrayList<Rule>())));
	}
}
