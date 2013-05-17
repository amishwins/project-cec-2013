package cec.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class EmailListViewData extends AbstractTableModel {
	
	private static final long serialVersionUID = 5641320475551936954L;
	String[] header;
	ArrayList<EmailViewEntity> data;
	
	public EmailListViewData(String[] emailTableViewColumns,  Iterable<EmailViewEntity> emailTableViewData) { 
		header = emailTableViewColumns;
		data   = (ArrayList<EmailViewEntity>)emailTableViewData;
		
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