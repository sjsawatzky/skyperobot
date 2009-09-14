package com.paulhimes.skylon.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
	}

	/**
	 * Remove all but the first card from the stack.
	 */
	private void cleanCards() {
		Component[] components = cardPanel.getComponents();

		logger.info("There are " + components.length);
		for (int i = 1; i < components.length; i++) {
			cardPanel.remove(components[i]);
		}
	}

	private JPanel makeNavigationPanel() {
		JPanel navPanel = new JPanel(new BorderLayout());
		JButton backButton = new JButton("Back");
		JButton editButton = new JButton("Edit");

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.first(cardPanel);

				// Clean up the stack.
				cleanCards();

				logger.info("" + cardPanel.getComponentCount());
			}
		});

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editAction();
			}
		});

		navPanel.add(backButton, BorderLayout.WEST);
		navPanel.add(editButton, BorderLayout.EAST);

		return navPanel;
	}
}
