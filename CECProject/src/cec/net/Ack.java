package cec.net;

import java.io.Serializable;
import java.util.UUID;

/**
 * Serializable Acknowledgement Object used by both Client and Server
 * to report that a Email or Meeting has been successfully received. 
 */

public class Ack implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID id;
	private MessageType msgType;
	
	
	public UUID getId() {
		return id;
	}
	public MessageType getMsgType() {
		return msgType;
	}
	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
    Ack(UUID id, MessageType msgType){
    	this.id = id;
    	this.msgType = msgType;
    }
 }

enum MessageType{
	EMAIL,
	MEETING,
	CHANGESET
}
