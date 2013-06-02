package cec.service;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.view.EmailViewEntity;

/**
 * Expose the action that can be done on an email from the model layer 
 */
public class EmailService {

	
	/**
	 * This method communicates the model layer the emailInView in parameter
	 * received from the view layer has to be sent
	 * @param emailInView the EmailViewEntity object received from view layer
	 */
	public void sendEmail(EmailViewEntity emailInView) {
		EmailBuilder mailBuilder = new EmailBuilder();
		Email newEmail = mailBuilder.withId(emailInView.getId()).withFrom()
				.withTo(emailInView.getTo())
				.withSubject(emailInView.getSubject())
				.withBody(emailInView.getBody()).withCC(emailInView.getCC())
				.computelastModifiedTime().computeSentTime()
				.withParentFolder(FolderFactory.getFolder(emailInView.getFolder()))
				.build();
		newEmail.send();
	}

	/**
	 * This method communicates the model layer the emailInView received as parameter
	 * from the view layer has to be saved in the draft folder.
	 * What it does is build at model layer, an email object from the emailInView object
	 * save that email object to the draft folder and set the folderName of emailInView object 
	 * to the Draft folder.
	 * @param emailInView the EmailViewEntity object received from view layer
	 * */
	public void draftEmail(EmailViewEntity emailInView) {
		EmailBuilder mailBuilder = new EmailBuilder();
		Email email = mailBuilder.withId(emailInView.getId()).withFrom()
				.withTo(emailInView.getTo())
				.withSubject(emailInView.getSubject())
				.withBody(emailInView.getBody()).withCC(emailInView.getCC())
				.computelastModifiedTime().computeSentTime()
				.withDraftsParentFolder().build();
		email.saveToDraftFolder();
		updateEmailViewEntity(emailInView, email);
	}

	
	private void updateEmailViewEntity(EmailViewEntity emailInView, Email email) {
	   emailInView.setFolder(email.getParentFolder().getPath());
	}

	/**
	 * This method communicates the model layer the emailInView object
	 * has to be deleted 
	 * and has to set  in the draft folder
	 * @param emailInView the EmailViewEntity object received from view layer
	 */
	public void delete(EmailViewEntity emailInView) {
		Email email = convertEmailInViewToEmailModel(emailInView);
		email.delete();
	}

	/**
	 * This method communicates the model layer the emailInView object
	 * has to be moved from its current location to the new one provided as second parameter 
	 * and has to set  in the draft folder
	 * @param emailInView the EmailViewEntity object received from view layer
	 * @param destinationFolderPath destination folder's name
	 * */
	public void move(EmailViewEntity emailInView, String destinationFolderPath) {
		Email email = convertEmailInViewToEmailModel(emailInView);
		Folder destinationFolder = FolderFactory
				.getFolder(destinationFolderPath);
		email.move(destinationFolder);
	}

	private Email convertEmailInViewToEmailModel(EmailViewEntity emailInView) {
		EmailBuilder mailBuilder = new EmailBuilder();

		Email email = mailBuilder
				.withId(emailInView.getId())
				.withParentFolder(
						FolderFactory.getFolder(emailInView.getFolder()))
						.build();
		return email;
	}
}