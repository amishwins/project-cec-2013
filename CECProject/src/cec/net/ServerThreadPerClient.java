package cec.net;

import java.io.EOFException;
import java.net.SocketException;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.model.Email;
import cec.model.EmailImpl;
import cec.model.Meeting;
import cec.model.MeetingBuilder;
import cec.model.ServerMeetingData;
import cec.model.ServerMeetingDataImpl;

/**
 * The Class ServerThreadPerClient object handles the requests per client. 
 */
public class ServerThreadPerClient implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger(ServerThreadPerClient.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( ServerThreadPerClient.class.getPackage().getName() ) );
    }
	
	/** The email address. */
	private String emailAddress;

	/**
	 * Instantiates a new server thread(task) per client. 
	 *
	 * @param emailAddress the email address
	 */
	public ServerThreadPerClient(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	
	/**
	 * This thread accepts email, change-set, acknowledgement etc.
	 * 
	 */
	public void run() {
		logger.info("Accepting Emails from this client: " + emailAddress);
		while (true) {
			try {
				// Receiving objects
					// handle meeting request
				Object obj = SuperCECServer.getEmailToObjectInputStream()
						.get(emailAddress).readObject();
				
				if (obj instanceof Email) {
					// handle emails
					boolean emailAdded = false;

					EmailImpl e = (EmailImpl) obj;
					if(!e.isMeetingEmail()){
						emailAdded = SuperCECServer.getArrivingEmailQueue().add(e);
						logger.info("Is Email " + e.getSubject()+ " added to Queue: " + emailAdded);

					}else{
						logger.info("Going to build a Meeting object from the email meeting invitation" + e.getSubject());
						Meeting meeting = buildMeeting(e);
						MeetingDataWrapper md = new MeetingDataWrapper();
						md.id = meeting.getId(); //
						md.meetingObj = meeting; // 

						ServerMeetingData serverMeetingData = new ServerMeetingDataImpl();
						serverMeetingData.setup(meeting);
						md.meetingData = serverMeetingData; //

						SuperCECServer.getMeetingMap().put(meeting.getId(), md);
						
						emailAdded = SuperCECServer.getArrivingEmailQueue().add(e);
						logger.info("Is Email " + e.getSubject()+ " added to Queue: " + emailAdded);
					}
					
					// Send the acknowledgement regarless if we recieved an email or an email invitation to a meeting
					logger.info("CECServer Sending Ack to : "+ emailAddress+" for email subject: "+e.getSubject());
					Ack ack = new Ack(e.getId(),MessageType.EMAIL);
	                SuperCECServer.getEmailToObjectOutputStream().get(emailAddress).writeObject(ack);

				} else if ((obj instanceof Ack)) {
					// handle acks
					Ack ack = (Ack) obj;
					handleAck(ack);
					// delete the thing that we were saving for this ack (regular email, email for meeting, 
					// or changeset)
					
				} else if ((obj instanceof CommunicationChangeSet)) {
					CommunicationChangeSet ccs = (CommunicationChangeSet) obj;
					applyChangeSetToMeetingMap(emailAddress, ccs);

				}
			} catch (SocketException e) {
				logger.info(emailAddress
						+ " is Disconnected from the server!");
				break;
			}  catch (EOFException e) {
				logger.info(emailAddress
						+ " Disconnected from the server!");
				break;
			} catch (Exception e) {
				logger.info(emailAddress
						+ " Disconnected from the server!");
				logger.severe(StackTrace.asString(e));
				break;
			}
		}
	}
	
	/**
	 * Apply change set to meeting map.
	 *
	 * @param emailAddress the email address
	 * @param ccs the ccs
	 */
	private void applyChangeSetToMeetingMap(String emailAddress, CommunicationChangeSet ccs) {
		SuperCECServer.getExecutor().submit(new ChangeSetThreadForMeetings(emailAddress, ccs));
	}

	/**
	 * Builds the meeting object.
	 *
	 * @param e the e
	 * @return the meeting
	 */
	private Meeting buildMeeting(EmailImpl e) {
		MeetingBuilder mb = new MeetingBuilder();
		return mb.buildFromAcceptInvite(e);
      }

	/**
	 * Handles acknowledgement object.
	 *
	 * @param ack the ack
	 */
	private void handleAck(Ack ack) {
		if (ack.getMsgType() == MessageType.EMAIL || ack.getMsgType() == MessageType.MEETING ) {
			try {
				logger.info("Putting ack on EmailAckQueue");
				SuperCECServer.getArrivingEmailAcks().put(ack);
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
			}
		
		} else if (ack.getMsgType() == MessageType.CHANGESET) {
			// what does the server do with changesets? Does it apply immediately? 
		}
		
	}
}