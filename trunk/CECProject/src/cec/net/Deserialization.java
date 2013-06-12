package cec.net;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import cec.model.Email;
import cec.model.EmailBuilder;
//import cec.view.EmailViewEntity;

public class Deserialization {

	ObjectInputStream emailStream;
	public Deserialization()
	{
		 FileInputStream fileIn;
		try {
			fileIn = new FileInputStream("email.obj");
			emailStream = new ObjectInputStream(fileIn);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		
	}
	
	//public void saveToInbox(ObjectInputStream emailStream ) {
		public void saveToInbox() {

		try {
			Email email = (Email)emailStream.readObject();
			EmailBuilder mailBuilder = new EmailBuilder();
			
			Email newEmail  =  mailBuilder.computeID().withFrom(email.getFrom())
				.withTo(email.getTo())
				.withSubject(email.getSubject())
				.withBody(email.getBody()).withCC(email.getCC())
				.computelastModifiedTime().computeSentTime()				
				.withInboxParentFolder().build();	
			
			newEmail.saveToInboxFolder();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
}
