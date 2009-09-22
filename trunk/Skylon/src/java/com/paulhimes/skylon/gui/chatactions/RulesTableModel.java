package com.paulhimes.skylon.gui.chatactions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import com.paulhimes.skylon.DataStore;
import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;

public class RulesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3897700210715570245L;

	private String[] columnNames = new String[] { "Type", "Match", "Value", "" };
	private Class<?>[] columnClasses = new Class<?>[] { RuleType.class, RuleMatch.class, String.class, JButton.class };

	private Rules rules;

	public RulesTableModel(Rules rules) {
		this.rules = rules;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return rules.getRules().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Rule rule = rules.getRules().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return rule.getType();
		case 1:
			return rule.getMatch();
		case 2:
			return rule.getValue();
		case 3:
			JButton deleteButton = makeDeleteButton();
			final int row = rowIndex;
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeRule(row);
				}
			});
			return deleteButton;
		}

		return null;
	}

	private JButton makeDeleteButton() {
		JButton deleteButton = new JButton("X");
		deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));

		return deleteButton;
	}

	public void setValueAt(Object value, int row, int col) {

		if (row < rules.getRules().size()) {
			Rule rule = rules.getRules().get(row);

			switch (col) {
			case 0:
				rule.setType((RuleType) value);
				break;
			case 1:
				rule.setMatch((RuleMatch) value);
				break;
			case 2:
				rule.setValue((String) value);
				break;
			case 3:
				break;
			}

			updateActions();
		}

	}

	public Class<?> getColumnClass(int c) {
		return columnClasses[c];
	}

	@Override
	public String getColumnName(int col) {
		if (col < 0 || col > columnNames.length - 1)
			return null;

		return columnNames[col];
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	public void removeRule(int selectedRow) {
		if (selectedRow >= 0 && selectedRow < rules.getRules().size()) {
			rules.getRules().remove(selectedRow);
			updateActions();
		}
	}

	public void addRule() {
		rules.getRules().add(new Rule());

		updateActions();
	}

	public Rules getRules() {
		return rules;
	}

	private void updateActions() {
		fireTableDataChanged();

		DataStore.saveActions();
	}

}
