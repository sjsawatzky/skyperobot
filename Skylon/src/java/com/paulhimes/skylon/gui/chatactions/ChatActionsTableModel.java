package com.paulhimes.skylon.gui.chatactions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import com.paulhimes.skylon.Actions;
import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActions;
import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.logging.Logger;

public class ChatActionsTableModel extends AbstractTableModel {

	private final Logger logger = new Logger(getClass());

	private static final long serialVersionUID = 4392135386648484092L;

	private String[] columnNames = new String[] { "Type", "Name", "", "", "" };

	private Actions actions;

	public ChatActionsTableModel(Actions actions) {
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
			return action.getTypeString();
		case 1:
			return ((SimpleReplyChatAction) action).getName();
		case 2:
			JButton upButton = makeUpButton();
			final int upRow = rowIndex;
			upButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					moveActionUp(upRow);
				}
			});
			return upButton;
		case 3:
			JButton downButton = makeDownButton();
			final int downRow = rowIndex;
			downButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					moveActionDown(downRow);
				}
			});
			return downButton;
		case 4:
			JButton deleteButton = makeDeleteButton();
			final int deleteRow = rowIndex;
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					deleteAction(deleteRow);
				}
			});
			return deleteButton;
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

	private void deleteAction(int index) {
		logger.info("delete action " + index);
		actions.getChatActions().remove(index);

		fireTableDataChanged();
	}

	private void moveActionUp(int index) {
		logger.info("move up action " + index);

		actions.getChatActions().moveUp(index);

		fireTableDataChanged();
	}

	private void moveActionDown(int index) {
		logger.info("move down action " + index);

		actions.getChatActions().moveDown(index);

		fireTableDataChanged();
	}

	private JButton makeDeleteButton() {
		JButton deleteButton = new JButton("X");
		deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));

		return deleteButton;
	}

	private JButton makeUpButton() {
		JButton upButton = new JButton("Up");
		upButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		upButton.setPreferredSize(new Dimension(30, upButton.getPreferredSize().height));

		return upButton;
	}

	private JButton makeDownButton() {
		JButton downButton = new JButton("Down");
		downButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		downButton.setPreferredSize(new Dimension(30, downButton.getPreferredSize().height));

		return downButton;
	}

	public void addAction(ChatAction action) {
		actions.getChatActions().add(action);

		fireTableDataChanged();
	}
}
