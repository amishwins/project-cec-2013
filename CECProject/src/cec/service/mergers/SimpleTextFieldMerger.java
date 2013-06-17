package cec.service.mergers;

public class SimpleTextFieldMerger implements Merger {

	@Override
	public String merge(String before, String after) {
		return ">>>From server: " + before +" <<<Your version: " + after;
	}
	
	

}
