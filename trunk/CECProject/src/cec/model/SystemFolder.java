package cec.model;

import exceptions.CannotDeleteSystemFolderException;

public class SystemFolder extends Folder {

	public SystemFolder(String path) {
		super(path);
	}

	@Override
	public void delete() {
		throw new CannotDeleteSystemFolderException();
	}

}
