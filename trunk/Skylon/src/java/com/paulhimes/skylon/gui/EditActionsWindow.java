package com.paulhimes.skylon.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.paulhimes.skylon.Actions;

public class EditActionsWindow {

	private JFrame frame = new JFrame("Skylon - Edit Actions");

	public EditActionsWindow(Actions actions) {
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(new JScrollPane(new JTable(new ActionsTableModel(actions))), BorderLayout.CENTER);
		frame.add(contentPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
