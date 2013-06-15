package cec.net;

import java.util.ArrayList;

public class Recipients {
	String to;
	String cc;
	ArrayList<String> splitTo = null;
	ArrayList<String> splitCC = null;
	
	public Recipients(String to, String cc) {
		this.to = to;
		this.cc = cc;
	}
	
	public ArrayList<String> getListOfAllTargetRecipients() {
		String tocc;
		
		if (to.isEmpty() && cc.isEmpty())
			throw new RuntimeException("Both to and cc cannot be empty!");
		
		if (cc.isEmpty())
			tocc = to;
		else if (to.isEmpty())
			tocc = cc;
		else
			tocc = to.concat(";".concat(cc));
		
		System.out.println(tocc);

		ArrayList<String> recipients = new ArrayList<String>();
		if(tocc.contains(";")) {
			String[] arrayTo = tocc.split(";");
			for(int i = 0; i < arrayTo.length; i++) {
				String s = arrayTo[i].trim();	
				recipients.add(s);
			}
		}
		else
			recipients.add(tocc);
		
		return recipients;
	}

	
	public static void main(String[] args) {

	}

}
