package cec.net;

import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.view.EmailClient;


/**
 * The Class UpdateEmailClientView.
 */
public class UpdateEmailClientView implements Runnable {

	/* It creates the logger for this class. */
	static Logger logger = Logger.getLogger(UpdateEmailClientView.class
			.getName());

	static {
		logger.setParent(Logger.getLogger(UpdateEmailClientView.class
				.getPackage().getName()));
	}

	/**
	 * It refreshes the email client view whenever email is received by the client
	 */
			
	public void run() {
		try {
			EmailClient.getReference().updateEmailsTable();
			logger.info("Email Client Updated itself.");

		} catch (Exception e) {
			logger.severe("Some issues with client updating itself.");
			logger.severe(StackTrace.asString(e));
		}

	}

}
