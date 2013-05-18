package cec.service;

import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Folder;
import cec.model.FolderFactory;
import cec.view.EmailViewEntity;

public class EmailService {

	public void sendEmail(String from, String to, String cc, String subject,
			String body) {
		EmailBuilder mailBuilder = new EmailBuilder();
		Email newEmail = mailBuilder.computeID().withFrom(from).withTo(to)
				.withSubject(subject).withBody(body).withCC(cc)
				.computelastModifiedTime().computeSentTime().build();
		newEmail.send();
	}

	public void draftEmail(String from, String to, String cc, String subject,
			String body) {
		EmailBuilder mailBuilder = new EmailBuilder();
		Email email = mailBuilder.computeID().withFrom(from).withTo(to)
				.withSubject(subject).withBody(body).withCC(cc)
				.computelastModifiedTime().computeSentTime().build();
		email.saveToDraftFolder();
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
