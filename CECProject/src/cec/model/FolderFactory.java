package cec.model;

import cec.config.CECConfigurator;

public class FolderFactory {
	
	public static Folder getFolder(String path) {
		if (CECConfigurator.getReference().isPathForASystemFolder(path)) {
			return new SystemFolder(path);
		}
		else 
			return new UserFolder(path);
	}
}
