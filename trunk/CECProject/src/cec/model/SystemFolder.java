package cec.model;

import exceptions.CannotDeleteSystemFolderException;

public class SystemFolder extends Folder {

	public SystemFolder(String path) {
		super(path);
	}

	@Override
	public void delete() {		
		handleSystemDelete();
	}

	protected void handleSystemDelete() {
		throw new CannotDeleteSystemFolderException();
	}

}
