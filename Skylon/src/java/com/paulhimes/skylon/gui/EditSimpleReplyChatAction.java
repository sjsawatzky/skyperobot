package com.paulhimes.skylon.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public class EditSimpleReplyChatAction {

	private final JTextField nameField = new JTextField();
	private final JTextField replyField = new JTextField();
	private final SimpleReplyChatAction action;

	public EditSimpleReplyChatAction(SimpleReplyChatAction action) {
		this.action = action;

		JFrame frame = new JFrame("Skylon - Edit SimpleReplyChatAction");

		JPanel mainPanel = new JPanel(new GridLayout(5, 2));
		mainPanel.add(new JLabel("Name"));
		nameField.setText(action.getName());
		nameField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				updateAction();
			}
		});
		mainPanel.add(nameField);
		mainPanel.add(new JLabel("Reply"));
		replyField.setText(action.getReply());
		replyField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				updateAction();
			}
		});
		mainPanel.add(replyField);
		mainPanel.add(new JLabel("Rules Operator"));
		mainPanel.add(new JComboBox(RulesOperator.values()));
		mainPanel.add(new JLabel("Rules"));
		mainPanel.add(new JButton("Add"));
		mainPanel.add(new JTable());
		frame.add(mainPanel);

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
}
