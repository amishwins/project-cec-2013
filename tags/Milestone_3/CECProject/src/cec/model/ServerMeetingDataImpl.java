package cec.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import cec.net.Recipients;

public class ServerMeetingDataImpl implements ServerMeetingData {
	
	static Logger logger = Logger.getLogger(ServerMeetingDataImpl.class.getName()); 
	private UUID meetingID;
	
	private ConcurrentHashMap<String, InvitationStatus> attendees = new ConcurrentHashMap<>();

	@Override
	public void setup(Meeting m) {
		validateAttendees(m);
		Recipients r = new Recipients(m.getAttendees(), "");
		HashSet<String> allAttendees = r.getListOfAllTargetRecipients();
		attendees.put(m.getFrom(), InvitationStatus.ACCEPTED);
		for(String s: allAttendees) {
			attendees.put(s, InvitationStatus.INVITED);
		}
		meetingID = m.getId();
	}
	
	@Override
	public InvitationStatus invitationStatus(String emailAddress) {
		return attendees.get(emailAddress);
	}	
	@Override
	public List<String> getAcceptedRecipients() {
		return getRecipientsWithInvitationStatus(InvitationStatus.ACCEPTED);
	}

	@Override
	public List<String> getDeclinedRecipients() {
		return getRecipientsWithInvitationStatus(InvitationStatus.DECLINED);
	}

	@Override
	public List<String> getUnAnsweredRecipients() {
		return getRecipientsWithInvitationStatus(InvitationStatus.INVITED);
	}

	@Override
	public void setAccepted(String name) {
		setInvitationStatus(name, InvitationStatus.ACCEPTED);
	}

	@Override
	public void setDeclined(String name) {
		setInvitationStatus(name, InvitationStatus.DECLINED);
	}
	
	private void setInvitationStatus(String name, InvitationStatus i) {
		if (!attendees.containsKey(name)) {
			logger.severe("Tried to set: " + name + " as " + i + " in meeting: " + meetingID);
			throw new RuntimeException("This person does not exist: " + name);
		}
		attendees.put(name, i);
	}
	
	
	protected void validateAttendees(Meeting m) {
		// some defensive code:
		int semicolonCount = StringUtils.countMatches(m.getAttendees(), ";");
		int atSymbolCount = StringUtils.countMatches(m.getAttendees(), "@");
		
		if ( semicolonCount != (atSymbolCount - 1)) {
			logger.severe("Attendees is poorly formatted: " + m.getAttendees());
			throw new RuntimeException("The email addresses are poorly formatted");
		}
	}

	private List<String> getRecipientsWithInvitationStatus(InvitationStatus i) {
		ArrayList<String> recipients = new ArrayList<>();
		for(Map.Entry<String, InvitationStatus> entry: attendees.entrySet()) {
			if (entry.getValue().equals(i)) {
				recipients.add(entry.getKey());
			}
		}
		return recipients;		
	}


}
