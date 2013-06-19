package cec.net;

import java.io.Serializable;

/**
 * Serializable Object that represents a handshake beetween Client and Server 
 * that identifies the Client email adress and is used to set up the Server Mapping [email/Socket].
 */

public class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String emailAddress;

	public String toString() {
		return emailAddress;
	}
}