package cec.net;

import java.util.UUID;

public class MeetingLock {
	UUID id;
	
	public MeetingLock() {
	}
	
	public void wait(UUID id) {
		this.id = id;
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean canUnlock(UUID compareTo) {
		return (this.id.equals(compareTo));
	}

}
