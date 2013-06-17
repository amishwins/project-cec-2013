package cec.model;

import java.util.List;

public interface ServerMeetingData {
	
	public void setup(Meeting m);
	
	public List<String> getAcceptedRecipients();
	public List<String> getDeclinedRecipients();
	public List<String> getUnAnsweredRecipients();
	
	public void setAccepted(String name);
	public void setDeclined(String name);

	public InvitationStatus invitationStatus(String emailAddress);	
}
