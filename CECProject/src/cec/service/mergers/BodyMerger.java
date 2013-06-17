package cec.service.mergers;

public class BodyMerger implements Merger {

	@Override
	public String merge(String before, String after) {
		return ">>>From server: \n" + before + "\n\n<<<Your version: \n" + after;
	}

}
