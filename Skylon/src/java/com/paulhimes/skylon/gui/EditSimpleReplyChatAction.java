package com.paulhimes.skylon.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;

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
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;
import com.paulhimes.skylon.logging.Logger;

public class EditSimpleReplyChatAction {

	private final Logger logger = new Logger(getClass());

	private final JTextField nameField = new JTextField();
	private final JTextField replyField = new JTextField();
	private final JComboBox operatorBox = new JComboBox(RulesOperator.values());
	private final SimpleReplyChatAction action;
	private final RulesTableModel tableModel;
	private final JTable rulesTable;

	private final JPanel panel;

	public EditSimpleReplyChatAction(SimpleReplyChatAction action) {
		this.action = action;

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
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.ipadx = 10;
		c.ipady = 10;
		panel.add(new JLabel("Name"), c);

		c.gridx = 1;
		c.weightx = 1;
		panel.add(nameField, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		panel.add(new JLabel("Reply"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		panel.add(replyField, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		panel.add(new JLabel("Rules Operator"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		panel.add(operatorBox, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		panel.add(new JLabel("Rules"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		panel.add(newRuleButton, c);

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

		// Don't allow the user to drag columns.
		rulesTable.getTableHeader().setReorderingAllowed(false);

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
				return makeDeleteButton();
			}
		});
		deleteColumn.setCellEditor(new ButtonEditor());

		// Make all columns non-resizable
		Enumeration<TableColumn> columns = rulesTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			column.setResizable(false);
		}

		packTable(rulesTable);

		panel.add(new JScrollPane(rulesTable), c);

	}

	public JPanel getPanel() {
		return panel;
	}

	private void packTable(JTable rulesTable) {
		packRows(rulesTable, 2);
		packColumns(rulesTable, 0, 2, 2);
		packColumns(rulesTable, 3, 4, 2);
	}

	// Returns the preferred height of a row.
	// The result is equal to the tallest cell in the row.
	private int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
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

	// Returns the preferred width of a column.
	// The result is equal to the widest cell in the column.
	private int getPreferredColumnWidth(JTable table, int columnIndex, int margin) {
		int columnWidth = 0;

		int rowCount = table.getModel().getRowCount();
		for (int r = 0; r < rowCount; r++) {
			TableCellRenderer cellRenderer = table.getCellRenderer(r, columnIndex);
			Component component = table.prepareRenderer(cellRenderer, r, columnIndex);

			int w = component.getPreferredSize().width + 2 * margin;
			columnWidth = Math.max(columnWidth, w);
		}

		return columnWidth;
	}

	// The height of each row is set to the preferred height of the
	// tallest cell in that row.
	private void packRows(JTable table, int margin) {
		packRows(table, 0, table.getRowCount(), margin);
	}

	private void packColumns(JTable table, int margin) {
		packColumns(table, 0, table.getColumnCount(), margin);
	}

	private void packColumns(JTable table, int start, int end, int margin) {
		for (int c = start; c < end; c++) {
			int w = getPreferredColumnWidth(table, c, margin);

			TableColumn column = table.getColumnModel().getColumn(c);

			column.setWidth(w);
			column.setMaxWidth(w);
			column.setMinWidth(w);
		}
	}

	// For each row >= start and < end, the height of a
	// row is set to the preferred height of the tallest cell
	// in that row.
	private void packRows(JTable table, int start, int end, int margin) {
		for (int r = start; r < end; r++) {
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

			JButton deleteButton = makeDeleteButton();

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

	private JButton makeDeleteButton() {
		JButton deleteButton = new JButton("X");
		deleteButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));

		return deleteButton;
	}

	private void updateAction() {
		setName(nameField.getText());
		setReply(replyField.getText());
		action.getRules().setOperator((RulesOperator) operatorBox.getSelectedItem());
		action.setRules(tableModel.getRules());

		packTable(rulesTable);
	}

	private void setReply(String reply) {
		action.setReply(reply);
	}

	private void setName(String name) {
		action.setName(name);
	}

	public static void main(String[] args) {

		ArrayList<Rule> list = new ArrayList<Rule>();
		list.add(new Rule(RuleType.SENDER, RuleMatch.CONTAINS, "echo", false));
		list.add(new Rule(RuleType.CONTENT, RuleMatch.ENDS_WITH, "\\(wave\\)", false));

		SimpleReplyChatAction testAction = new SimpleReplyChatAction("Wave", "Wave back!", new Rules(RulesOperator.AND, list));
		JPanel testPanel = testAction.edit();

		JFrame editActionFrame = new JFrame("Skylon - Edit " + testAction.getTypeString());
		editActionFrame.add(testPanel);

		editActionFrame.pack();
		editActionFrame.setVisible(true);
	}
}
