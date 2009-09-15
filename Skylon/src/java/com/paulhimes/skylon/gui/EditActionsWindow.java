package com.paulhimes.skylon.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.paulhimes.skylon.Actions;
import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.logging.Logger;

public class EditActionsWindow {

	private final Logger logger = new Logger(getClass());

	private JFrame frame = new JFrame("Skylon - Edit Actions");
	private JPanel cardPanel = new JPanel();
	private JTable table = new JTable();
	private Actions actions;
	private CardLayout cardLayout = new CardLayout();
	private JButton backButton = new JButton("Back");

	public EditActionsWindow(Actions actions) {
		this.actions = actions;
		JPanel contentPanel = new JPanel(new BorderLayout());
		table.setModel(new ActionsTableModel(actions));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editAction();
			}
		});
		contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		cardPanel.setLayout(cardLayout);
		cardPanel.add(contentPanel, "Main");

		frame.setLayout(new BorderLayout());
		frame.add(makeNavigationPanel(), BorderLayout.NORTH);
		frame.add(cardPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	private void editAction() {
		int selectedRow = table.getSelectedRow();
		ChatAction chatAction = actions.getChatActions().getActions().get(selectedRow);

		// Add the new card.
		cardPanel.add(chatAction.edit(), "other");

		// Flip to the new card.
		cardLayout.show(cardPanel, "other");
		frame.setTitle("Skylon - Edit " + chatAction.getTypeString());

		backButton.setEnabled(true);
	}

	/**
	 * Remove all but the first card from the stack.
	 */
	private void cleanCards() {
		Component[] components = cardPanel.getComponents();

		for (int i = 1; i < components.length; i++) {
			cardPanel.remove(components[i]);
		}
	}

	private JPanel makeNavigationPanel() {
		JPanel navPanel = new JPanel(new BorderLayout());
		navPanel.setBackground(Color.LIGHT_GRAY);

		backButton.setEnabled(false);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Switch to the main edit card.
				cardLayout.first(cardPanel);
				// Clean up the stack.
				cleanCards();

				backButton.setEnabled(false);
			}
		});

		navPanel.add(backButton, BorderLayout.WEST);

		return navPanel;
	}
}
