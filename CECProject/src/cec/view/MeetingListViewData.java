package cec.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Auxiliary Class used by <code>EmailClient</code> class to fill the JTable
 *  <code>emailTable</code> that shows the Emails of the selected folder.
 * It receives trough its constructor a String containing the columns
 * and list of Email Entities and returns the data in a Model that is
 * compatible with the JTable component. To do this, this class extends
 * the AbstractTableModel class and it's default implementations.
 * */

public class MeetingListViewData extends AbstractTableModel {
	
	private static final long serialVersionUID = 5641320475551936954L;
	String[] header;
	ArrayList<MeetingViewEntity> data;
	
	public MeetingListViewData(String[] meetingTableViewColumns,  Iterable<MeetingViewEntity> meetingTableViewData) { 
		header = meetingTableViewColumns;
		data   = (ArrayList<MeetingViewEntity>)meetingTableViewData;
		
	}
	
	public MeetingViewEntity getViewEntityAtIndex(int index) {
		
		if (index < 0)
			return new MeetingViewEntity();
		
		return data.get(index);
	}
	
	public int getRowCount() {
		return data.size();
	}
	
    public int getColumnCount() { 
    	return 3; 
	}
    
    public Object getValueAt(int row, int column){ 
    	MeetingViewEntity currentRow = data.get(row);
    	String columnValue = "";
    	switch (column) {
			case 0:
				columnValue = currentRow.getFrom();		
				break;
			case 1:
				columnValue = currentRow.getSubject();
				break;
			case 2:
				columnValue = currentRow.getLastModifiedTime();
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