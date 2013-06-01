package cec.view;

public class TemplateViewEntity {
	
	// private class attributes
	private String name = "";
	private String to = "";
	private String cc = "";
	private String subject = "";
	private String body = "";

	// boilerplate setters and getters	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCC() {
		return cc;
	}

	public void setCC(String cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
