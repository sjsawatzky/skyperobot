package com.paulhimes.skylon.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;

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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.paulhimes.skylon.chatactions.SimpleReplyChatAction;
import com.paulhimes.skylon.chatactions.rules.Rule;
import com.paulhimes.skylon.chatactions.rules.Rules;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleMatch;
import com.paulhimes.skylon.chatactions.rules.Rule.RuleType;
import com.paulhimes.skylon.chatactions.rules.Rules.RulesOperator;
import com.paulhimes.skylon.logging.Logger;
import com.paulhimes.skylon.tools.TableTools;

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

		// Set default renderer/editor for all JButton columns.
		rulesTable.setDefaultRenderer(JButton.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				return (JButton) value;
			}
		});
		rulesTable.setDefaultEditor(JButton.class, new TableTools.ButtonEditor());

		// Make all columns non-resizable
		Enumeration<TableColumn> columns = rulesTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			column.setResizable(false);
		}

		packTable(rulesTable);

		panel.add(new JScrollPane(rulesTable), c);

	}

	public void packTable(JTable rulesTable) {
		TableTools.packRows(rulesTable, 2);
		TableTools.packColumns(rulesTable, 0, 2, 2);
		TableTools.packColumns(rulesTable, 3, 4, 2);
	}

	public JPanel getPanel() {
		return panel;
	}

	private void addRule() {
		tableModel.addRule();
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
		editActionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editActionFrame.add(testPanel);

		editActionFrame.pack();
		editActionFrame.setVisible(true);
	}
}
