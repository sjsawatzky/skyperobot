package com.paulhimes.skylon.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		contentPanel.add(new JComboBox(RulesOperator.values()), c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		contentPanel.add(new JLabel("Rules"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		contentPanel.add(new JButton("Add"), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		tableModel = new RulesTableModel(action.getRules());
		rulesTable = new JTable(tableModel);

		rulesTable.setDefaultEditor(RuleType.class, new DefaultCellEditor(new JComboBox(RuleType.values())));
		rulesTable.setDefaultEditor(RuleMatch.class, new DefaultCellEditor(new JComboBox(RuleMatch.values())));

		TableColumn deleteColumn = rulesTable.getColumnModel().getColumn(3);
		deleteColumn.setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				return new JButton("X");
			}
		});
		deleteColumn.setCellEditor(new ButtonEditor());

		contentPanel.add(new JScrollPane(rulesTable), c);

		frame.add(contentPanel);

		frame.pack();
		frame.setVisible(true);
	}

	private void removeSelectedRule() {
		int selectedRow = rulesTable.getSelectedRow();

		tableModel.removeRule(selectedRow);
	}

	private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			JButton deleteButton = new JButton("X");

			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					removeSelectedRule();
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
