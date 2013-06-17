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
import cec.service.ClientMeetingMerger;

class MeetingDataWrapper {
	public UUID id;
	public Meeting meetingObj;
	public ServerMeetingData meetingData;
}

class SentEmailWriteToSysout implements Runnable {
	static Logger logger = Logger.getLogger(SentEmailWriteToSysout.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SentEmailWriteToSysout.class.getPackage().getName() ) );
    }
	public void run() {
		while(true) {
			logger.info(String.valueOf(SuperCECServer.getSentEmailMap().size()));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.severe(StackTrace.asString(e));
			}
		}
	}
}

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
				
			}
						
			//CommunicationChangeSet response = new CommunicationChangeSet(ChangeSetState.CHANGE_REJECTED, ccs.getId());
			try {
				logger.info("About to write object back to client who sent change set.");
				Thread.sleep(300);
				SuperCECServer.getEmailToObjectOutputStream().get(emailAddress).writeObject(response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class SuperCECServer {
	
	static Logger logger = Logger.getLogger(SuperCECServer.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SuperCECServer.class.getPackage().getName() ) );
    }

    // All data structures which hold our CEC Email and Meeting Data which needs to be 
    // thread safe and concurrently accessable
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
		//executor.submit(new SentEmailWriteToSysout());

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);

		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
	}
}
