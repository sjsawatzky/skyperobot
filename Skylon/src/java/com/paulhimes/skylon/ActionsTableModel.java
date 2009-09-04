package com.paulhimes.skylon;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActions;
import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;

public class ActionsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 4392135386648484092L;

	private String[] columnNames = new String[] { "Type", "Name" };

	private Actions actions;

	public ActionsTableModel(Actions actions) {
		this.actions = actions;
	}

	@Override
	public int getColumnCount() {
		return 2;
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

		String result = "";
		switch (columnIndex) {
		case 0:
			result = "SimpleReplyChatAction";
			break;
		case 1:
			result = ((SimpleReplyChatAction) action).getName();
			break;
		}
		return result;
	}

	public String getColumnName(int col) {
		if (col < 0 || col > columnNames.length - 1)
			return null;

		return columnNames[col];
	}

}
