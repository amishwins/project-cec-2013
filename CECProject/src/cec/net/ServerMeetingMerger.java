package cec.net;

import java.util.ArrayList;
import java.util.logging.Logger;

import cec.net.ChangeSetFields;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;

//Responsible to create a new change set and update the values on server side
public class ServerMeetingMerger {
	
	static Logger logger = Logger.getLogger(ServerMeetingMerger.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( ServerMeetingMerger.class.getPackage().getName() ) );
    }

	public CommunicationChangeSet getChangeSet(MeetingDataWrapper serverCurrent, CommunicationChangeSet clientsBefore) {
		
		CommunicationChangeSet ccs = new CommunicationChangeSet(ChangeSetState.CHANGE_REJECTED, clientsBefore.getId());

		ArrayList<Change> changes = clientsBefore.getChanges();
		for(Change c: changes) {

			 if(c.field.equals(ChangeSetFields.ATTENDEES)) 
					if (!serverCurrent.meetingObj.getAttendees().equals(c.before)){
						ccs.addChange(ChangeSetFields.ATTENDEES, c.before, serverCurrent.meetingObj.getAttendees());
					    logger.info("Field: "+  ChangeSetFields.ATTENDEES + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getAttendees());
					}
			
			 if(c.field.equals(ChangeSetFields.BODY))
				if (!serverCurrent.meetingObj.getBody().equals(c.before)){
					ccs.addChange(ChangeSetFields.BODY,c.before, serverCurrent.meetingObj.getBody());
					logger.info("Field: "+  ChangeSetFields.ATTENDEES + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getBody());
				}
			 if(c.field.equals(ChangeSetFields.SUBJECT))
					if (!serverCurrent.meetingObj.getSubject().equals(c.before)){
						ccs.addChange(ChangeSetFields.SUBJECT, c.before, serverCurrent.meetingObj.getSubject());
		                logger.info("Field: "+  ChangeSetFields.BODY + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getSubject());
		             }
			 
			 if(c.field.equals(ChangeSetFields.PLACE)) 
					if (!serverCurrent.meetingObj.getPlace().equals(c.before)){
						ccs.addChange(ChangeSetFields.PLACE, c.before, serverCurrent.meetingObj.getPlace());
		            	 logger.info("Field: "+  ChangeSetFields.PLACE + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getPlace());
                     }
			 
			 if(c.field.equals(ChangeSetFields.START_DATE))
					if (!serverCurrent.meetingObj.getStartDate().equals(c.before)){
						ccs.addChange(ChangeSetFields.START_DATE, c.before, serverCurrent.meetingObj.getStartDate());
						logger.info("Field: "+  ChangeSetFields.START_DATE + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getStartDate());
		             }
			 if(c.field.equals(ChangeSetFields.START_TIME))
					if (!serverCurrent.meetingObj.getStartTime().equals(c.before)){
						ccs.addChange(ChangeSetFields.START_TIME, c.before, serverCurrent.meetingObj.getStartTime());
						logger.info("Field: "+  ChangeSetFields.START_TIME + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getStartTime());
		             }			 
			 
			 if(c.field.equals(ChangeSetFields.END_DATE))
					if (!serverCurrent.meetingObj.getEndDate().equals(c.before)){
						ccs.addChange(ChangeSetFields.END_DATE, c.before, serverCurrent.meetingObj.getEndDate());
						logger.info("Field: "+  ChangeSetFields.END_DATE + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getEndDate());
		             }
			
			 if(c.field.equals(ChangeSetFields.END_TIME))
					if (!serverCurrent.meetingObj.getEndTime().equals(c.before)){
						ccs.addChange(ChangeSetFields.END_TIME, c.before, serverCurrent.meetingObj.getEndTime());
						logger.info("Field: "+  ChangeSetFields.END_TIME + " Client's Before: "+c.before + " Servers' Before: " + serverCurrent.meetingObj.getEndTime());
		             }
			
		}

		//If  client's Meeting before's state = Server's current Meeting object  > Accept the changes  
		if (ccs.getChanges().size() == 0){
			ccs = new CommunicationChangeSet(ChangeSetState.CHANGE_ACCEPTED, clientsBefore.getId());
			logger.info("Change Accepted for clients before meeting Id : " +clientsBefore.getId()  );
		}else{
			logger.info("Change was not Accepted for this client before meeting Id : " +clientsBefore.getId()  );
		}
					
		return ccs;

	}	
	
}
