package cec.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EmailXMLDao implements EmailDao {
	
	final String FILE_EXTENSION=".xml";
	
	public Document buildXmlFile(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, String location) {
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document emailInXMLFormat = null;
		
		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();			
			
			// root element Email
			emailInXMLFormat = documentBuilder.newDocument();
			Element emailRootElement = emailInXMLFormat.createElement("E-Mail");
			emailInXMLFormat.appendChild(emailRootElement);

			// id
			Element idLabel = emailInXMLFormat.createElement("Id");
			idLabel.appendChild(emailInXMLFormat.createTextNode(id.toString()));
			emailRootElement.appendChild(idLabel);

			// from
			Element fromLabel = emailInXMLFormat.createElement("From");
			fromLabel.appendChild(emailInXMLFormat.createTextNode(from));
			emailRootElement.appendChild(fromLabel);

			// to
			Element toLabel = emailInXMLFormat.createElement("To");
			toLabel.appendChild(emailInXMLFormat.createTextNode(to));
			emailRootElement.appendChild(toLabel);

			// cc
			Element ccLabel = emailInXMLFormat.createElement("CC");
			ccLabel.appendChild(emailInXMLFormat.createTextNode(cc));
			emailRootElement.appendChild(ccLabel);

			// subject
			Element subjectLabel = emailInXMLFormat.createElement("Subject");
			subjectLabel.appendChild(emailInXMLFormat.createTextNode(subject));
			emailRootElement.appendChild(subjectLabel);

			// body
			Element bodyLabel = emailInXMLFormat.createElement("Body");
			bodyLabel.appendChild(emailInXMLFormat.createTextNode(body));
			emailRootElement.appendChild(bodyLabel);

			// lastModifiedTime
			Element lastModifiedTimeLabel = emailInXMLFormat
					.createElement("LastModifiedTime");
			lastModifiedTimeLabel.appendChild(emailInXMLFormat
					.createTextNode(lastModifiedTime));
			emailRootElement.appendChild(lastModifiedTimeLabel);

			// sentTime
			Element sentTimeLabel = emailInXMLFormat.createElement("SentTime");
			sentTimeLabel
			.appendChild(emailInXMLFormat.createTextNode(sentTime));
			emailRootElement.appendChild(sentTimeLabel);

			// parent folder
			Element parentFolder = emailInXMLFormat
					.createElement("ParentFolder");
			parentFolder.appendChild(emailInXMLFormat.createTextNode(location));
			emailRootElement.appendChild(parentFolder);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return emailInXMLFormat;

	}

	public void save(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, String location) {
        String path = location;
		String fileName = id.toString();
       	String pathToSaveFile = path + "/" + fileName
				+ FILE_EXTENSION;

		try {
			Document emailInXMLFormat = buildXmlFile(id, from, to, cc, subject,
					body, lastModifiedTime, sentTime, location);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			StreamSource stylesource = new StreamSource(getClass()
					.getResourceAsStream("proper-indenting.xsl"));
			Transformer transformer = transformerFactory
					.newTransformer(stylesource);
			DOMSource source = new DOMSource(emailInXMLFormat);
			StreamResult result = new StreamResult(new File(pathToSaveFile));

			transformer.transform(source, result);

		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String path, UUID fileName){
       FileDeleteStrategy file = FileDeleteStrategy.FORCE;
       try{
    	   file.delete(new File(path+"/"+fileName.toString()+FILE_EXTENSION));
       }catch(IOException fileDeleteException){
    	   fileDeleteException.printStackTrace();
       }     		
	}
	
	public void move(UUID fileName, String srcDir, String destDir){
		File file = new File(srcDir+"/"+fileName.toString()+FILE_EXTENSION);
		File destinationDir = new File(destDir);
		updateXMLField(file.getPath(),destDir);
		try {
			FileUtils.moveFileToDirectory(file, destinationDir, false);
		} catch (IOException fileMoveException) {
			fileMoveException.printStackTrace();
		}
	}
	
	private void updateXMLField(String xmlFile, String destDir){
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document emailInXMLFormat = null;
		
		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			 emailInXMLFormat = documentBuilder.parse(xmlFile);
			 emailInXMLFormat.getDocumentElement().normalize();

			NodeList email = emailInXMLFormat.getElementsByTagName("E-Mail");
			Element	field = (Element) email.item(0);
			Node parentFolder = field.getElementsByTagName("ParentFolder").item(0)
						.getFirstChild();
		    parentFolder.setNodeValue(destDir);

			emailInXMLFormat.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(emailInXMLFormat);
			StreamResult result = new StreamResult(
					new File(xmlFile));
			transformer.transform(source, result);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}

	@Override
	public Map<String, String> loadEmail(String folder, String xmlFileName) {
		Map<String, String> emailData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(folder + "/" + xmlFileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document email = documentBuilder.parse(xmlFile);
			email.getDocumentElement().normalize();
			NodeList listOfEmailFields = email.getElementsByTagName("E-Mail");
			for (int index = 0; index < listOfEmailFields.getLength(); index++) {
				Node emailField = listOfEmailFields.item(index);
				if (emailField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) emailField;
					emailData.put("Id", eElement.getElementsByTagName("Id").item(0).getTextContent());
					emailData.put("From", eElement.getElementsByTagName("From").item(0).getTextContent());
					emailData.put("To", eElement.getElementsByTagName("To").item(0).getTextContent());
					emailData.put("CC", eElement.getElementsByTagName("CC").item(0).getTextContent());
					emailData.put("Subject", eElement.getElementsByTagName("Subject").item(0).getTextContent());
					emailData.put("Body", eElement.getElementsByTagName("Body").item(0).getTextContent());
					emailData.put("LastModifiedTime", eElement.getElementsByTagName("LastModifiedTime").item(0).getTextContent());
					emailData.put("SentTime", eElement.getElementsByTagName("SentTime").item(0).getTextContent());
					emailData.put("ParentFolder", eElement.getElementsByTagName("ParentFolder").item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emailData;
	}
}
