package com.paulhimes.skylon.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;

public class EditSimpleReplyChatAction {

	private final JTextField nameField = new JTextField();
	private final JTextField replyField = new JTextField();
	private final JComboBox operatorBox = new JComboBox(RulesOperator.values());
	private final SimpleReplyChatAction action;
	private final RulesTableModel tableModel;
	private final JTable rulesTable;

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
		operatorBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					updateAction();
				}
			}
		});
		JButton newRuleButton = new JButton("New Rule");
		newRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRule();
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
		contentPanel.add(operatorBox, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		contentPanel.add(new JLabel("Rules"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		contentPanel.add(newRuleButton, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		tableModel = new RulesTableModel(action.getRules());
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				updateAction();
			}
		});
		rulesTable = new JTable(tableModel);
		rulesTable.setDefaultEditor(RuleType.class, new DefaultCellEditor(new JComboBox(RuleType.values())));
		rulesTable.setDefaultRenderer(RuleType.class, new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JComboBox comboBox = new JComboBox(RuleType.values());
				comboBox.setSelectedItem(value);
				return comboBox;
			}
		});
		rulesTable.setDefaultEditor(RuleMatch.class, new DefaultCellEditor(new JComboBox(RuleMatch.values())));
		rulesTable.setDefaultRenderer(RuleMatch.class, new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JComboBox comboBox = new JComboBox(RuleMatch.values());
				comboBox.setSelectedItem(value);
				return comboBox;
			}
		});

		TableColumn deleteColumn = rulesTable.getColumnModel().getColumn(3);
		deleteColumn.setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				return new JButton("X");
			}
		});
		deleteColumn.setCellEditor(new ButtonEditor());

		packRows(rulesTable, 2);

		contentPanel.add(new JScrollPane(rulesTable), c);

		frame.add(contentPanel);

		frame.pack();
		frame.setVisible(true);
	}

	// Returns the preferred height of a row.
	// The result is equal to the tallest cell in the row.
	public int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
		// Get the current default height for all rows
		int height = table.getRowHeight();

		// Determine highest cell in the row
		for (int c = 0; c < table.getColumnCount(); c++) {
			TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
			Component comp = table.prepareRenderer(renderer, rowIndex, c);
			int h = comp.getPreferredSize().height + 2 * margin;
			height = Math.max(height, h);
		}
		return height;
	}

	// The height of each row is set to the preferred height of the
	// tallest cell in that row.
	public void packRows(JTable table, int margin) {
		packRows(table, 0, table.getRowCount(), margin);
	}

	// For each row >= start and < end, the height of a
	// row is set to the preferred height of the tallest cell
	// in that row.
	public void packRows(JTable table, int start, int end, int margin) {
		for (int r = 0; r < table.getRowCount(); r++) {
			// Get the preferred height
			int h = getPreferredRowHeight(table, r, margin);

			// Now set the row height using the preferred height
			if (table.getRowHeight(r) != h) {
				table.setRowHeight(r, h);
			}
		}
	}

	private void removeRule(int rowIndex) {
		tableModel.removeRule(rowIndex);
	}

	private void addRule() {
		tableModel.addRule();
	}

	private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

		private static final long serialVersionUID = -4049435441388527521L;

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			JButton deleteButton = new JButton("X");

			final int rowIndex = row;

			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					removeRule(rowIndex);
				}
			});

			return deleteButton;
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private void updateAction() {
		setName(nameField.getText());
		setReply(replyField.getText());
		action.getRules().setOperator((RulesOperator) operatorBox.getSelectedItem());
		action.setRules(tableModel.getRules());

		packRows(rulesTable, 10);
	}

	private void setReply(String reply) {
		action.setReply(reply);
	}

	private void setName(String name) {
		action.setName(name);
	}

	// public static void main(String[] args) {
	// new EditSimpleReplyChatAction(new SimpleReplyChatAction("name", "reply", new Rules(RulesOperator.AND, new ArrayList<Rule>())));
	// }
}
