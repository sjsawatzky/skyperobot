package com.paulhimes.skylon.gui;

import javax.swing.table.AbstractTableModel;

import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;

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
			return rule.getType();
		case 1:
			return rule.getMatch();
		case 2:
			result = rule.getValue();
			break;
		case 3:
			result = "X";
			break;
		}
		return result;
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

			fireTableCellUpdated(row, col);
		}

	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for each cell. If we didn't implement this method, then the last column would contain text ("true"/"false"), rather than a
	 * check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public String getColumnName(int col) {
		if (col < 0 || col > columnNames.length - 1)
			return null;

		return columnNames[col];
	}

	public boolean isCellEditable(int row, int col) {
		return col < 4;
	}

	public void removeRule(int selectedRow) {
		if (selectedRow >= 0 && selectedRow < rules.getRules().size()) {
			rules.getRules().remove(selectedRow);
			fireTableDataChanged();
		}
	}

	public void addRule() {
		rules.getRules().add(new Rule());

		fireTableDataChanged();
	}

}
