package cec.net;

import java.util.ArrayList;
import java.util.logging.Logger;

import cec.net.ChangeSetFields;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;

/**
 * Auxiliary Class used in the server-side by <code>SuperCECServer</code> class to compare the content of a specific meeting
 * object between the server's version and the version that the client is trying to update, find the differences and create a new Change Set.<BR>
 * If there differences, a Change Set with the status "CHANGE_REJECTED" containing all unmatched fields is generated to be sent back to the client.<BR>
 * If there are no differences, which means that the client and server have the same Meeting version, 
 * the server accepts client's changes and an empty Change Set with the status "CHANGE_ACCEPTED" is created to be sent back to the client.
 */

public class ServerMeetingMerger {
	
	static Logger logger = Logger.getLogger(ServerMeetingMerger.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( ServerMeetingMerger.class.getPackage().getName() ) );
    }

	public CommunicationChangeSet getChangeSet(MeetingDataWrapper serverCurrent, CommunicationChangeSet clientsBefore) {
		
		CommunicationChangeSet ccs = new CommunicationChangeSet(ChangeSetState.CHANGE_REJECTED, clientsBefore.getId());
       
		ArrayList<Change> changes = clientsBefore.getChanges();
		for(Change c: changes) {
			/*if(c.field.equals(ChangeSetFields.ATTENDEES)) {
			          SuperCECServer.getExecutor().submit(new AddAttendeesToMeeting(serverCurrent.meetingObj.getAttendees(),c.before));
			}*/
			 if(c.field.equals(ChangeSetFields.ATTENDEES)) 
					if (!serverCurrent.meetingObj.getAttendees().equals(c.before)){
						ccs.addChange(ChangeSetFields.ATTENDEES,serverCurrent.meetingObj.getAttendees(), serverCurrent.meetingObj.getAttendees());
					    logger.info("Field: "+  ChangeSetFields.ATTENDEES + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getAttendees()+">");
					}
			 
			 if(c.field.equals(ChangeSetFields.BODY))
				if (!serverCurrent.meetingObj.getBody().equals(c.before)){
					ccs.addChange(ChangeSetFields.BODY,serverCurrent.meetingObj.getBody(), serverCurrent.meetingObj.getBody());
					logger.info("Field: "+  ChangeSetFields.BODY + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getBody()+">");
				}
			 
			 if(c.field.equals(ChangeSetFields.SUBJECT))
					if (!serverCurrent.meetingObj.getSubject().equals(c.before)){
						ccs.addChange(ChangeSetFields.SUBJECT, serverCurrent.meetingObj.getSubject(), serverCurrent.meetingObj.getSubject());
		                logger.info("Field: "+  ChangeSetFields.SUBJECT + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getSubject()+">");
		             }
			 
			 if(c.field.equals(ChangeSetFields.PLACE)) 
					if (!serverCurrent.meetingObj.getPlace().equals(c.before)){
						ccs.addChange(ChangeSetFields.PLACE, serverCurrent.meetingObj.getPlace(), serverCurrent.meetingObj.getPlace());
		            	 logger.info("Field: "+  ChangeSetFields.PLACE + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getPlace()+">");
                     }
			 
			 if(c.field.equals(ChangeSetFields.START_DATE))
					if (!serverCurrent.meetingObj.getStartDate().equals(c.before)){
						ccs.addChange(ChangeSetFields.START_DATE, serverCurrent.meetingObj.getStartDate(), serverCurrent.meetingObj.getStartDate());
						logger.info("Field: "+  ChangeSetFields.START_DATE + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getStartDate()+">");
		             }
			 if(c.field.equals(ChangeSetFields.START_TIME))
					if (!serverCurrent.meetingObj.getStartTime().equals(c.before)){
						ccs.addChange(ChangeSetFields.START_TIME, serverCurrent.meetingObj.getStartTime(), serverCurrent.meetingObj.getStartTime());
						logger.info("Field: "+  ChangeSetFields.START_TIME + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getStartTime()+">");
		             }			 
			 
			 if(c.field.equals(ChangeSetFields.END_DATE))
					if (!serverCurrent.meetingObj.getEndDate().equals(c.before)){
						ccs.addChange(ChangeSetFields.END_DATE, serverCurrent.meetingObj.getEndDate(), serverCurrent.meetingObj.getEndDate());
						logger.info("Field: "+  ChangeSetFields.END_DATE + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getEndDate()+">");
		             }
			
			 if(c.field.equals(ChangeSetFields.END_TIME))
					if (!serverCurrent.meetingObj.getEndTime().equals(c.before)){
						ccs.addChange(ChangeSetFields.END_TIME, serverCurrent.meetingObj.getEndTime(), serverCurrent.meetingObj.getEndTime());
						logger.info("Field: "+  ChangeSetFields.END_TIME + " Client's Before: <"+c.before + "> Servers' Before: <" + serverCurrent.meetingObj.getEndTime()+">");
		             }			
		}

		
		if (ccs.getChanges().size() == 0){
			ccs = new CommunicationChangeSet(ChangeSetState.CHANGE_ACCEPTED, clientsBefore.getId());
			logger.info("Meeting change request ACCEPTED for clients before meeting Id : " +clientsBefore.getId()  );
		}else{
			logger.info("Meeting change request NOT ACCEPTED for this client before meeting Id : " +clientsBefore.getId()  );
		}
					
		return ccs;

	}	
	
}
