package cec.model;

import cec.config.CECConfigurator;

public class FolderFactory {
	
	public static Folder getFolder(String path) {
		// What do we do if path is empty? invalid argument?		
		if (CECConfigurator.getReference().isPathForASystemFolder(path)) {
			return new SystemFolder(path);
		}
		else 
			return new UserFolder(path);
	}
}
