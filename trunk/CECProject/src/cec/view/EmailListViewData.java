package cec.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Auxiliary Class used by <code>EmailClient</code> class that receives trough
 * its construct a String containing headers and list of Email Entities
 * and returns the data in a Model that is compatible with the JTable
 *  <code>emailTable</code> that shows the Emails of the selected folder.
 *  To do this, this class extends the AbstractTableModel class.
 * */

public class EmailListViewData extends AbstractTableModel {
	
	private static final long serialVersionUID = 5641320475551936954L;
	String[] header;
	ArrayList<EmailViewEntity> data;
	
	public EmailListViewData(String[] emailTableViewColumns,  Iterable<EmailViewEntity> emailTableViewData) { 
		header = emailTableViewColumns;
		data   = (ArrayList<EmailViewEntity>)emailTableViewData;
		
	}
	
	public EmailViewEntity getViewEntityAtIndex(int index) {
		
		if (index < 0)
			return new EmailViewEntity();
		
		return data.get(index);
	}
	
	public int getRowCount() {
		return data.size();
	}
	
    public int getColumnCount() { 
    	return 3; 
	}
    
    public Object getValueAt(int row, int column){ 
    	EmailViewEntity currentRow = data.get(row);
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