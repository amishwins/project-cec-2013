package cec.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.model.Email;
import cec.model.Meeting;
import cec.model.MeetingImpl;
import cec.model.ServerMeetingData;

/**
 * Class MeetingDataWrapper is a convenience class which stores a meeting object,
 * and ServerMeetingData. ServerMeetingData is a simplified abstraction of storing 
 * who has been invited to a meeting, who has accepted, and who has declined. This 
 * object must be kept in sync on the server.  
 *
 */
class MeetingDataWrapper {
	public UUID id;
	public Meeting meetingObj;
	public ServerMeetingData meetingData;
}

/**
 * This thread listens for acknowledgements from the client. This is to clean up
 * any dangling objects the server has kept in case the client was unreachable. For the 
 * most part, we are not yet using this functionality. 
 *
 */
class SentEmailCleanupThread implements Runnable {
	static Logger logger = Logger.getLogger(SentEmailCleanupThread.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SentEmailCleanupThread.class.getPackage().getName() ) );
    }
	public void run() {
		while(true) {
			try {
				Ack ack = SuperCECServer.getArrivingEmailAcks().take();
				
				SuperCECServer.getSentEmailMap().remove(ack.getId());
				
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
			}
		}
		
	}
}

/**
 * The main thread which responds to a communication change set it has received. It handles
 * taking this change set (from the client), and trying to update the server with it. It first reads 
 * the shared meetinMap data. From it, it builds its own change set response by using the ServerMeetingMerger.
 * For each difference, it then make the update for the stored meeting.
 * 
 * Pre-condition: this meeting MUST have been made in this session. If the server goes down,
 * all meetings are gone. And therefore, the client must clear his meetings. 
 *
 */
class ChangeSetThreadForMeetings implements Runnable {
	static Logger logger = Logger.getLogger(ChangeSetThreadForMeetings.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( ChangeSetThreadForMeetings.class.getPackage().getName() ) );
    }
	
	private String emailAddress;
	private CommunicationChangeSet ccs;
	
	public ChangeSetThreadForMeetings (String emailAddress, CommunicationChangeSet ccs) {
		this.emailAddress = emailAddress;
		this.ccs = ccs;
	}
	
	public void run() {
		ConcurrentHashMap<UUID, MeetingDataWrapper> meetingMap = SuperCECServer.getMeetingMap();
		MeetingDataWrapper md = meetingMap.get(ccs.getId());
		
		if (ccs.isAccept()) {
			md.meetingData.setAccepted(emailAddress);
			logger.info("The meeting invite for " + emailAddress + " is " + md.meetingData.invitationStatus(emailAddress));
		}
		
		else if (ccs.isDecline()) {
			md.meetingData.setDeclined(emailAddress);
			logger.info("The meeting invite for " + emailAddress + " is " + md.meetingData.invitationStatus(emailAddress));
		}
		
		else if (ccs.isChange()) {
			logger.info("Changes Sent by client: "+emailAddress );
			ServerMeetingMerger cm = new ServerMeetingMerger();
			CommunicationChangeSet response = cm.getChangeSet(md, ccs);
			
			if (response.isChangeAccepted()){
			    for(Change c: ccs.getChanges()) {
			    	MeetingImpl m = (MeetingImpl)md.meetingObj;
			    	if(c.field.equals(ChangeSetFields.ATTENDEES)) {
			    		m.setAttendees(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.BODY)) {
			    		m.setBody(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.SUBJECT)) {
    		    		m.setSubject(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.PLACE)) {
			    		m.setPlace(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.START_DATE)) {
			    		m.setStartDate(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.START_TIME)) {
			    		m.setStartTime(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.END_DATE)) {
			    		m.setEndDate(c.after);
			    	}
			    	if(c.field.equals(ChangeSetFields.END_TIME)) {
			    		m.setEndTime(c.after);
			    	}
			    }
			    logCurrentServerMeetingData(md);
			}
			
			
						
			//CommunicationChangeSet response = new CommunicationChangeSet(ChangeSetState.CHANGE_REJECTED, ccs.getId());
			try {
				logger.info("About to write object back to client who sent change set.");
				Thread.sleep(100);
				SuperCECServer.getEmailToObjectOutputStream().get(emailAddress).writeObject(response);
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
			}
		}
	}

	private void logCurrentServerMeetingData(MeetingDataWrapper md) {
		logger.info("Printing Servers Content when changed by Client: ");
		logger.info("Attendees: "+md.meetingObj.getAttendees());
		logger.info("Body: "+md.meetingObj.getBody());
		logger.info("Subject: "+md.meetingObj.getSubject());
		logger.info("Place: "+md.meetingObj.getPlace());
		logger.info("StartDate: "+md.meetingObj.getStartDate());
		logger.info("StartTime: "+md.meetingObj.getStartTime());
		logger.info("EndDate: "+md.meetingObj.getEndDate());
		logger.info("EndTime: "+md.meetingObj.getEndTime());
   }
}

/**
 * This is the main server class. This class represents our server. It encapsulates and 
 * hides the complexity for handling sockets, and input/output streams. Upon launch, it 
 * submits three main tasks to a thread pool which it also encapsulates (ExecutorService).
 * 
 * Most threads need to communicate with the server in order to access shared data. This
 * is made possible by convenient accessor methods.  
 *
 */
public class SuperCECServer {
	
	static Logger logger = Logger.getLogger(SuperCECServer.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SuperCECServer.class.getPackage().getName() ) );
    }

    // All data structures which hold our CEC Email and Meeting Data which needs to be 
    // thread safe and concurrently accessible
   	static LinkedBlockingDeque<Email> arrivingEmails = new LinkedBlockingDeque<>();
	static LinkedBlockingDeque<Ack> arrivingEmailAcks = new LinkedBlockingDeque<>();
	static ConcurrentHashMap<UUID, Email> sentEmails = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, List<Email>> unSendableEmailsMap = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, Socket> emailToSocketMap = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, ObjectInputStream> emailToObjectInputStream = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, ObjectOutputStream> emailToObjectOutputStream = new ConcurrentHashMap<>();
	static ConcurrentHashMap<UUID, MeetingDataWrapper> meetingMap = new ConcurrentHashMap<>();
	
	// The Thread Pool we will use is the Executor Service, a really awesome implementation
	static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static ConcurrentHashMap<UUID, MeetingDataWrapper> getMeetingMap() {
		return meetingMap;
	}
	
	public static ConcurrentHashMap<String, ObjectInputStream> getEmailToObjectInputStream() {
		return emailToObjectInputStream;
	}

	public static ConcurrentHashMap<String, ObjectOutputStream> getEmailToObjectOutputStream() {
		return emailToObjectOutputStream;
	}

	public static ConcurrentHashMap<String, Socket> getEmailToSocketMap() {
		return emailToSocketMap;
	}

	public static ExecutorService getExecutor() {
		return executor;
	}

	public static LinkedBlockingDeque<Email> getArrivingEmailQueue() {
		return arrivingEmails;
	}
	
	public static LinkedBlockingDeque<Ack> getArrivingEmailAcks() {
		return arrivingEmailAcks;
	}
	
	public static ConcurrentHashMap<UUID, Email> getSentEmailMap() {
		return sentEmails;
	}

	public static ConcurrentHashMap<String, List<Email>> getUnSendableEmailsMap() {
		return unSendableEmailsMap;
	}
	public static void main(String[] args) throws IOException {

		ServerSocket server = null;
		try {
			server = new ServerSocket(7777);
		} catch (BindException e) {
			logger.info("CECServer is already running on this port!");
			logger.severe(StackTrace.asString(e));
		} catch (Exception e){
			logger.severe(StackTrace.asString(e));
		}

		executor.submit(new ServerClientAcceptor(server));
		executor.submit(new EmailListenerCECServer());
		executor.submit(new SentEmailCleanupThread());
		

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);

		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
	}
}
