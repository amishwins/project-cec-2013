package cec.config;

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton convenience class to help us centralize some Collaborative 
 * Email Client system settings. It essentially wraps a hash map and provides
 * default settings when first instantiated. In addition, there's a method 
 * to help determine if a folder is a system folder
 *
 */
public class CECConfigurator {
	private static Map<String, String> systemSettings;
	private static CECConfigurator singleton;
	
	/**
	 * Static getReference method in classic Singleton pattern
	 * @return a reference to the configurator class
	 */
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

	/**
	 * Wrapper to the Hash Map put method 
	 * @param key 
	 * @param value
	 */
	public void put(String key, String value) {
		systemSettings.put(key, value);
	}
	
	/**
	 * Wrapper to the Hash Map get method
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String value = systemSettings.get(key); 
		if (value == null) {
			throw new NullPointerException();
		}
		return value;
	}
	
	/**
	 * @param path value which represents a path to a folder in the system
	 * @return boolean whether the path is actually to a system folder
	 */
	public boolean isPathForAEmailSystemFolder(String path) {
		if ( systemSettings.get("emails").equals(path) ||
			 systemSettings.get("Inbox").equals(path) ||
		     systemSettings.get("Drafts").equals(path) ||
		     systemSettings.get("Sent").equals(path) ||
		     systemSettings.get("Outbox").equals(path) )
			return true;
		
		return false;
		
	}
	
	public boolean isPathForAMeetingSystemFolder(String path){
		if ( systemSettings.get("Meetings").equals(path) ){
			return true;
		}
		return false;
	}
	
	private void mapDefaultSystemFolders() {
		systemSettings.put("emails", "emails");
		systemSettings.put("Inbox", "emails/Inbox");
		systemSettings.put("Drafts", "emails/Drafts");
		systemSettings.put("Outbox", "emails/Outbox");
		systemSettings.put("Sent", "emails/Sent");
		systemSettings.put("Meetings", "Meetings");
	}
	
	private void mapDefaultDateFormat() {
		systemSettings.put("DateFormat", "yyyy.MM.dd_'At'_HH.mm.ss.SSS");
		systemSettings.put("DateFormatForMeetingFields", "yyyy-MM-dd");
	}
	
	private void mapDefaultClientEmailAddress() {
		systemSettings.put("ClientEmail", "cec.user@cec.com");
	}
	
	public String getClientEmailAddress(){
		return systemSettings.get("ClientEmail");
	}	
}
