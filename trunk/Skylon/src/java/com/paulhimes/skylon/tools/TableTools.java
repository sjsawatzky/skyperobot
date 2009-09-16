package com.paulhimes.skylon.tools;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class TableTools {

	// Returns the preferred height of a row.
	// The result is equal to the tallest cell in the row.
	private static int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
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
	private static int getPreferredColumnWidth(JTable table, int columnIndex, int margin) {
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
	public static void packRows(JTable table, int margin) {
		packRows(table, 0, table.getRowCount(), margin);
	}

	public static void packColumns(JTable table, int start, int end, int margin) {
		for (int c = start; c < end; c++) {
			int w = getPreferredColumnWidth(table, c, margin);

			TableColumn column = table.getColumnModel().getColumn(c);

			column.setWidth(w);
			column.setMaxWidth(w);
			column.setMinWidth(w);
		}
	}

	public static void packButtonColumns(JTable table, int margin) {
		TableModel tableModel = table.getModel();

		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			if (JButton.class.isAssignableFrom(tableModel.getColumnClass(i))) {
				packColumns(table, i, i + 1, margin);
			}
		}
	}

	public static void packEnumColumns(JTable table, int margin) {
		TableModel tableModel = table.getModel();

		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			if (Enum.class.isAssignableFrom(tableModel.getColumnClass(i))) {
				packColumns(table, i, i + 1, margin);
			}
		}
	}

	public static void packTable(JTable rulesTable, int margin) {
		TableTools.packRows(rulesTable, margin);

		TableTools.packButtonColumns(rulesTable, margin);
		TableTools.packEnumColumns(rulesTable, margin);
	}

	// For each row >= start and < end, the height of a
	// row is set to the preferred height of the tallest cell
	// in that row.
	private static void packRows(JTable table, int start, int end, int margin) {
		for (int r = start; r < end; r++) {
			// Get the preferred height
			int h = getPreferredRowHeight(table, r, margin);

			// Now set the row height using the preferred height
			if (table.getRowHeight(r) != h) {
				table.setRowHeight(r, h);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void configureEnumCells(JTable table) {
		TableModel tableModel = table.getModel();
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			if (Enum.class.isAssignableFrom(tableModel.getColumnClass(i))) {
				configureEnumCells(table, (Class<? extends Enum<?>>) tableModel.getColumnClass(i));
			}
		}
	}

	public static void configureEnumCells(JTable table, final Class<? extends Enum<?>> enumeration) {
		table.setDefaultEditor(enumeration, new DefaultCellEditor(new JComboBox(enumeration.getEnumConstants())));
		table.setDefaultRenderer(enumeration, new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JComboBox comboBox = new JComboBox(enumeration.getEnumConstants());
				comboBox.setSelectedItem(value);
				return comboBox;
			}
		});
	}

	public static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

		private static final long serialVersionUID = -4049435441388527521L;

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return (JButton) value;
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
