package cec.model;

public class SystemFolder extends Folder {

	public SystemFolder(String path) {
		super(path);
	}

	@Override
	public void delete() {
		// return an error
	}

}
