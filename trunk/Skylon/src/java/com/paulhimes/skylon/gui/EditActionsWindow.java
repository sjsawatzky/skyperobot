package com.paulhimes.skylon.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.paulhimes.skylon.Actions;
import com.paulhimes.skylon.chatactions.ChatAction;

public class EditActionsWindow {

	private JFrame frame = new JFrame("Skylon - Edit Actions");
	private JTable table = new JTable();
	private Actions actions;

	public EditActionsWindow(Actions actions) {
		this.actions = actions;
		JPanel contentPanel = new JPanel(new BorderLayout());
		table.setModel(new ActionsTableModel(actions));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					editAction();
				}
			}
		});
		contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		frame.add(contentPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private void editAction() {
		int selectedRow = table.getSelectedRow();
		ChatAction chatAction = actions.getChatActions().getActions().get(selectedRow);

		chatAction.edit();
	}
}
