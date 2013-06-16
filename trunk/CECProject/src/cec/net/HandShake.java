package cec.net;

import java.io.Serializable;

public class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String emailAddress;

	public String toString() {
		return emailAddress;
	}
}