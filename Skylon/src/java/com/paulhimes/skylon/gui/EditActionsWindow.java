package com.paulhimes.skylon.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.paulhimes.skylon.Actions;
import com.paulhimes.skylon.chatactions.ChatAction;
import com.paulhimes.skylon.chatactions.ChatActionType;
import com.paulhimes.skylon.gui.chatactions.ChatActionsTableModel;
import com.paulhimes.skylon.logging.Logger;
import com.paulhimes.skylon.tools.TableTools;

public class EditActionsWindow {

	private final Logger logger = new Logger(getClass());

	private JFrame frame = new JFrame("Skylon - Edit Actions");
	private JPanel cardPanel = new JPanel();
	private SwingTable table;
	private Actions actions;
	private JComboBox chatActionTypeSelector;
	private CardLayout cardLayout = new CardLayout();
	private JButton backButton = new JButton("Back");
	private ChatActionsTableModel chatActionsTableModel;

	public EditActionsWindow(Actions actions) {
		this.actions = actions;

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
		contentPanel.add(new JLabel("Chat Action Types"), c);

		c.gridx = 1;
		c.weightx = 1;

		chatActionTypeSelector = new JComboBox(ChatActionType.values());
		contentPanel.add(chatActionTypeSelector, c);

		c.gridx = 2;
		c.weightx = 0;
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAction();
			}
		});
		contentPanel.add(addButton, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;

		chatActionsTableModel = new ChatActionsTableModel(actions);
		table = new SwingTable(chatActionsTableModel, 2);
		TableTools.packColumns(table, 0, 1, 2);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editAction();
			}
		});
		contentPanel.add(new JScrollPane(table), c);

		cardPanel.setLayout(cardLayout);
		cardPanel.add(contentPanel, "Main");

		frame.setLayout(new BorderLayout());
		frame.add(makeNavigationPanel(), BorderLayout.NORTH);
		frame.add(cardPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	private void addAction() {
		String selectedItem = chatActionTypeSelector.getSelectedItem().toString();
		ChatActionType chatActionType = (ChatActionType) chatActionTypeSelector.getSelectedItem();
		chatActionsTableModel.addAction(chatActionType.instantiate());
		logger.info(selectedItem);
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
