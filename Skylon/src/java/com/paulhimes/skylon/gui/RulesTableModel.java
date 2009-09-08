package com.paulhimes.skylon.gui;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public class RulesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3897700210715570245L;

	private String[] columnNames = new String[] { "Type", "Match", "Value", "" };

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

		String result = "";
		switch (columnIndex) {
		case 0:
			return new JComboBox(RulesOperator.values());
		case 1:
			result = rule.getMatch().name();
			break;
		case 2:
			result = rule.getValue();
			break;
		case 3:
			result = "X";
			break;
		}
		return result;
	}

	@Override
	public String getColumnName(int col) {
		if (col < 0 || col > columnNames.length - 1)
			return null;

		return columnNames[col];
	}
}
