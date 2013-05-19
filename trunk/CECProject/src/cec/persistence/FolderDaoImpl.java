package cec.persistence;

import java.util.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.filefilter.FileFileFilter;

public class FolderDaoImpl implements FolderDao {

	public Iterable<String>  loadSubFolders(String folderName) {
		List<String> subFoldersPath = new ArrayList<String>();
		List<File> subFolders = new ArrayList<File>();
	    subFolders = getSubFoldersRecursively(new File (folderName));
	    for(File subFolder: subFolders){
	    	subFoldersPath.add(subFolder.getPath().replace('\\','/'));
	    }
		return subFoldersPath;
	}
	public void delete(String folderPath) {
		FileDeleteStrategy file = FileDeleteStrategy.FORCE;
		try {
			 
			System.out.println("Delete : "+folderPath); 
			file.delete(new File(folderPath));
		} catch (IOException fileDeleteException) {
			fileDeleteException.printStackTrace();
		}

	}

	public void createSubFolder(String parentFolder, String newChildFolderName) {
		try {
			File newFolder = new File(parentFolder + "/" + newChildFolderName);
			newFolder.mkdir();
		} catch (Exception fileCreationException) {
			fileCreationException.printStackTrace();
		}
	}

	public  List<File> getSubFoldersRecursively(File folder) {
		List<File> subFolders = Arrays.asList(folder.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		}));
		subFolders = new ArrayList<File>(subFolders);
		List<File> innerFoldersList = new ArrayList<File>();
		for (File subFolder : subFolders) {
			innerFoldersList.addAll(getSubFoldersRecursively(subFolder));
		}
		subFolders.addAll(innerFoldersList);
		return subFolders;

	}

	public Iterable<Map<String, String>> loadEmails(String folder) {
		Collection<Map<String, String>> listOfEmails = new ArrayList<>();
		Map<String, String> email;
		EmailDao emailDao = new EmailXMLDao();

		String[] xmlFilesName = getFileNames(folder);
		for (String xmlFileName : xmlFilesName) {
			email = emailDao.loadEmail(folder, xmlFileName);//read(folder, xmlFileName);
			listOfEmails.add(email);
		}
		return listOfEmails;
	}

	private String[] getFileNames(String dir) {
		File folder = new File(dir);
		String[] xmlFiles = folder.list(FileFileFilter.FILE);
		return xmlFiles;
	}
}
