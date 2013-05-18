/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.persistence;

import java.util.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.filefilter.FileFileFilter;

/**
 * 
 * @author Pankaj Kapania
 */
public class FolderDaoImpl implements FolderDao {

	public/* Iterable<List<String>> */void loadFolders() {
		// Collection<List<String>> folderIDs = new ArrayList<String>();

	}

	public void delete(String folderPath) {
		FileDeleteStrategy file = FileDeleteStrategy.FORCE;
		try {
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

	public static List<File> getSubFoldersRecursively(File file) {
		List<File> subFolders = Arrays.asList(file.listFiles(new FileFilter() {
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

		String[] xmlFilesName = getFileNames(folder);
		for (String xmlFileName : xmlFilesName) {
			email = read(folder, xmlFileName);
			listOfEmails.add(email);
		}
		return listOfEmails;
	}

	private String[] getFileNames(String dir) {
		File folder = new File(dir);
		String[] xmlFiles = folder.list(FileFileFilter.FILE);
		return xmlFiles;
	}

	private Map<String, String> read(String folder, String xmlFileName) {
		Map<String, String> emailData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(folder + "/" + xmlFileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document email = documentBuilder.parse(xmlFile);
			email.getDocumentElement().normalize();
			NodeList listOfEmailFields = email.getElementsByTagName("E-Mail");
			for (int index = 0; index < listOfEmailFields.getLength(); index++) {
				Node emailField = listOfEmailFields.item(index);
				if (emailField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) emailField;
					emailData.put("Id", eElement.getElementsByTagName("Id")
							.item(0).getTextContent());
					emailData.put("From", eElement.getElementsByTagName("From")
							.item(0).getTextContent());
					emailData.put("To", eElement.getElementsByTagName("To")
							.item(0).getTextContent());
					emailData.put("CC", eElement.getElementsByTagName("CC")
							.item(0).getTextContent());
					emailData.put("Subject",
							eElement.getElementsByTagName("Subject").item(0)
							.getTextContent());
					emailData.put("Body", eElement.getElementsByTagName("Body")
							.item(0).getTextContent());
					emailData.put("LastModifiedTime", eElement
							.getElementsByTagName("LastModifiedTime").item(0)
							.getTextContent());
					emailData.put("SentTime",
							eElement.getElementsByTagName("SentTime").item(0)
							.getTextContent());
					emailData.put("ParentFolder", eElement
							.getElementsByTagName("ParentFolder").item(0)
							.getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emailData;
	}
}
