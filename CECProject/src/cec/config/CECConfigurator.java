package cec.config;

import java.util.HashMap;
import java.util.Map;

public class CECConfigurator {
	private Map<String, String> systemSettings;
	
	public CECConfigurator() {
		systemSettings = new HashMap<String, String>();
		mapDefaultSystemFolders();
	}

	public void put(String key, String value) {
		systemSettings.put(key, value);
	}
	
	public String get(String key) {
		return systemSettings.get(key);
	}
	
	private void mapDefaultSystemFolders() {
		systemSettings.put("Inbox", "emails/Inbox");
		systemSettings.put("Drafts", "emails/Drafts");
		systemSettings.put("Sent", "emails/Sent");
	}
}
