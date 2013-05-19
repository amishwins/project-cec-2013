package cec.service;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.view.EmailViewEntity;

public class EmailService {

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

	public void delete(EmailViewEntity emailInView) {
		Email email = convertEmailInViewToEmailModel(emailInView);
		email.delete();
	}

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
