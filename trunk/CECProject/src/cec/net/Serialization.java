package cec.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cec.config.CECConfigurator;
import cec.model.Email;
import cec.model.Folder;
import cec.model.FolderFactory;

public class Serialization {

	//public void SendFromOutbox(Iterable<Email> emailListInOutboxFolder) {
		public void SendFromOutbox() {

		Folder folder = FolderFactory.getFolder(CECConfigurator.getReference().get("Outbox"));
		Folder destfolder = FolderFactory.getFolder(CECConfigurator.getReference().get("Sent"));
		Iterable<Email> emailListInOutbox = folder.loadEmails();
		for(Email email:emailListInOutbox)
		{
			ObjectOutputStream emailStream=StreamEmail(email);
			email.move(destfolder);			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	}
	/*public void StreamEmail(Email emailInOutboxFolder) {
			
		try {
			FileOutputStream fileOut = new FileOutputStream("employee.ser");					
			 ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(emailInOutboxFolder);
	         out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
	
	public ObjectOutputStream StreamEmail(Email emailInOutboxFolder) {
		
		try {
			FileOutputStream fileOut = new FileOutputStream("email.obj");					
			 ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(emailInOutboxFolder);
	         out.close();
	         return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			return null;
	}
	
	
}
