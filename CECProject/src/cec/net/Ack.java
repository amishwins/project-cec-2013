package cec.net;

import java.util.UUID;

public class Ack {
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
