package com.paulhimes.skylon.gui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import com.paulhimes.skylon.Actions;
import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActions;
import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;

public class ActionsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 4392135386648484092L;

	private String[] columnNames = new String[] { "Type", "Name", "", "", "" };

	private Actions actions;

	public ActionsTableModel(Actions actions) {
		this.actions = actions;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		int totalActions = 0;
		ChatActions chatActions = actions.getChatActions();
		totalActions += chatActions.getActions().size();
		return totalActions;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		ChatActions chatActions = actions.getChatActions();
		List<ChatAction> actionsList = chatActions.getActions();
		ChatAction action = actionsList.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return "SimpleReplyChatAction";
		case 1:
			return ((SimpleReplyChatAction) action).getName();
		case 2:
			return makeUpButton();
		case 3:
			return makeDownButton();
		case 4:
			return makeDeleteButton();
		}

		return null;
	}

	@Override
	public String getColumnName(int col) {
		if (col < 0 || col > columnNames.length - 1)
			return null;

		return columnNames[col];
	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	private JButton makeDeleteButton() {
		JButton deleteButton = new JButton("X");
		deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));

		return deleteButton;
	}

	private JButton makeUpButton() {
		JButton deleteButton = new JButton("Up");
		deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));

		return deleteButton;
	}

	private JButton makeDownButton() {
		JButton deleteButton = new JButton("Down");
		deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));

		return deleteButton;
	}
}
