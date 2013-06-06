package cec.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Auxiliary Class used by <code>RuleSettings</code> class to fill the JTable
 * <code>ruleTable</code> that shows all the Rules configured in the
 * Application. It receives trough its constructor a String containing the
 * columns and a list of <code>RuleViewEntity</code> objects and returns the
 * data in a Model that is compatible with the JTable component. To do this,
 * this class extends the AbstractTableModel class and it's default
 * implementations.
 * */

public class RuleListViewData extends AbstractTableModel {

	private static final long serialVersionUID = 5641320475551936954L;
	String[] header;
	ArrayList<RuleViewEntity> data;

	public RuleListViewData(String[] ruleTableViewColumns,
			Iterable<RuleViewEntity> ruleTableViewData) {
		header = ruleTableViewColumns;
		data = (ArrayList<RuleViewEntity>) ruleTableViewData;

	}

	public RuleViewEntity getViewEntityAtIndex(int index) {

		if (index < 0)
			return new RuleViewEntity();

		return data.get(index);
	}

	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int row, int column) {
		RuleViewEntity currentRow = data.get(row);
		String columnValue = "";
		switch (column) {
		case 0:
			columnValue = currentRow.getEmailAddresses();
			break;
		case 1:
			columnValue = currentRow.getWords();
			break;
		case 2:
			columnValue = currentRow.getFolderPath();
			break;
		case 3:
			columnValue = String.valueOf(currentRow.getRank());
			break;
		default:
			break;
		}

		return columnValue;
	}

	public String getColumnName(int i) {
		return header[i];
	}
}