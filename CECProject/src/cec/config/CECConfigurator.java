package cec.config;

import java.util.HashMap;
import java.util.Map;

public class CECConfigurator {
	private static Map<String, String> systemSettings;
	private static CECConfigurator singleton;
	
	public static CECConfigurator getReference() {
		if (singleton == null) {
			singleton = new CECConfigurator();
		}		
		return singleton;
	}
	
	private CECConfigurator() {
		systemSettings = new HashMap<String, String>();
		mapDefaultSystemFolders();
		mapDefaultDateFormat();
		mapDefaultClientEmailAddress();
	}

	public void put(String key, String value) {
		systemSettings.put(key, value);
	}
	
	public String get(String key) {
		String value = systemSettings.get(key); 
		if (value == null) {
			throw new NullPointerException();
		}
		return value;
	}
	
	public boolean isPathForASystemFolder(String path) {
		if ( systemSettings.get("Inbox").equals(path) ||
		     systemSettings.get("Drafts").equals(path) ||
		     systemSettings.get("Sent").equals(path) ||
		     systemSettings.get("Outbox").equals(path) )
			return true;
		
		return false;
		
	}
	
	private void mapDefaultSystemFolders() {
		systemSettings.put("Inbox", "emails\\Inbox");
		systemSettings.put("Drafts", "emails\\Drafts");
		systemSettings.put("Outbox", "emails\\Outbox");
		systemSettings.put("Sent", "emails\\Sent");
	}
	
	private void mapDefaultDateFormat() {
		systemSettings.put("DateFormat", "yyyy.MM.dd_'At'_HH.mm.ss.SSS");		
	}
	
	private void mapDefaultClientEmailAddress() {
		systemSettings.put("ClientEmail", "cec.user@cec.com");
		
	}
	
	public String getClientEmailAddress(){
		return systemSettings.get("ClientEmail");
	}
	
}
