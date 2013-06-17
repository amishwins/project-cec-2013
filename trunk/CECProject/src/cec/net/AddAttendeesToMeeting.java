package cec.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class AddAttendeesToMeeting implements Runnable {
	static Logger logger = Logger.getLogger(AddAttendeesToMeeting.class.getName());

	static {
		logger.setParent(Logger.getLogger(AddAttendeesToMeeting.class.getPackage().getName()));
	}
     
	String oldListOfAttendees=null;
	String newListOfAttendees=null;
	
	ServerSocket server = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	

	public AddAttendeesToMeeting(String oldList, String newList) {
	   this.oldListOfAttendees = oldList;
	   this.newListOfAttendees = newList;
	}

	public void run() {
		  logger.info("--------------------------------------------");
		  logger.info("Thread :: AddAttendeesToMeeting Started ... "); 
          logger.info("oldList Of Attendees : "+ newListOfAttendees);
          logger.info("newList Of Attendees : "+ newListOfAttendees);
          logger.info("Thread :: AddAttendeesToMeeting Ended ... ");
          logger.info("--------------------------------------------");
	}

	
}