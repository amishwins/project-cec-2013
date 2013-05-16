package cec.model;

import java.util.ArrayList;

public class FolderFactory {
	
	private ArrayList<String> systemFolders = new ArrayList<String>();
	
	public FolderFactory() {
		
		// Setup some protected folders - there has to be a better way?
		systemFolders.add("emails/Inbox");
		systemFolders.add("emails/Draft");
		systemFolders.add("emails/Sent");
	}
	
	public Folder getFolder(String path) {
		if (systemFolders.contains(path)) {
			return new SystemFolder(path);
		}
		else 
			return new UserFolder(path);
	}
}
