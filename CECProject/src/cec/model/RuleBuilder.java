package cec.model;

import java.util.UUID;

import cec.persistence.RuleDaoFactory;

/**
 * A builder of email instances.
 * 
 */
public class RuleBuilder {

	/** Unique identifiers for each email. */
	UUID id;
	int rank;
	String emailAddresses = " ";
	String words = " ";
	Folder targetFolder;
	Boolean isActive;

	/**
	 * sets the value of id based on what is specified in the argument id.
	 * 
	 * @param id
	 *            the id
	 * @return the email builder
	 */
	public RuleBuilder withId(UUID id) {
		this.id = id;
		return this;
	}

	public RuleBuilder computeID() {
		this.id = UUID.randomUUID();
		return this;
	}

	/**
	 * sets the value of from based on what is specified in the argument named
	 * from
	 * 
	 * @param from
	 *            the from
	 * @return the email builder
	 */
	public RuleBuilder withRank(int rank) {
		this.rank = rank;
		return this;
	}

	
	/**
	 * sets the value of To field based on what is specified in the argument
	 * named to.
	 * 
	 * @param to
	 *            the to
	 * @return the email builder
	 */
	public RuleBuilder withEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
		return this;
	}

	/**
	 * sets the value of words field based on what is specified in the argument
	 * named cc.
	 * 
	 * @param cc
	 *            the cc
	 * @return the email builder
	 */
	public RuleBuilder withWords(String words) {
		this.words = words;
		return this;
	}

	/**
	 * sets the value of subject field based on what is specified in the
	 * argument named subject.
	 * 
	 * @param subject
	 *            the subject
	 * @return the email builder
	 */
	public RuleBuilder withTargetFolder(Folder targetFolder) {
		this.targetFolder = targetFolder;
		return this;
	}

	/**
	 * sets the value of body field based on what is specified in the argument
	 * named body.
	 * 
	 * @param body
	 *            the body
	 * @return the email builder
	 */
	public RuleBuilder withIsActive(Boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	/**
	 * returns the EmailImpl object.
	 * 
	 * @return the email
	 */
	public Rule build() {
		return new RuleImpl(id, rank, emailAddresses, words, targetFolder,
				isActive);
	}
}
