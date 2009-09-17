package com.paulhimes.skylon.gui;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.paulhimes.skylon.tools.TableTools;

public class SwingTable extends JTable {

	private static final long serialVersionUID = -6116932797750444230L;

	private final int cellMargin;

	public SwingTable(TableModel model, int cellMargin) {
		super(model);

		this.cellMargin = cellMargin;

		// Don't allow the user to drag columns.
		getTableHeader().setReorderingAllowed(false);

		// Display/Edit enum cells as JComboBoxes.
		TableTools.configureEnumCells(this);

		// Set default renderer/editor for all JButton columns.
		TableTools.configureButtonCells(this);

		// Make all columns non-resizable
		Enumeration<TableColumn> columns = getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			column.setResizable(false);
		}

		pack();
	}

	public void pack() {
		TableTools.packTable(this, cellMargin);
	}

}
