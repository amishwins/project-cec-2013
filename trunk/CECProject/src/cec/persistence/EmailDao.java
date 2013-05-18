package cec.persistence;

import java.util.Map;
import java.util.UUID;

public interface EmailDao {
	public void save(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, String location);
	
	public void delete(String path, UUID fileName);
	
	public void move(UUID fileName, String srcDir, String destDir);
	
	public Map<String, String> loadEmail(String folder, String xmlFileName);
}
