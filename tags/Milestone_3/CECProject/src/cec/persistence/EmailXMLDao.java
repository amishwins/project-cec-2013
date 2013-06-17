package cec.persistence;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

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

import cec.exceptions.StackTrace;



/**
 * 
 * EmailXMLDao is a class in the persistence layer responsible for handling 
 * email life cycle events at a lower level. It saves each email in a XML file format.
 * it is also responsible for deleting an XML file  and moving an XML file from one folder to 
 * another folder.
 * 
 */
public class EmailXMLDao implements EmailDao {
	
	/** Specifies the extension of file. */
	private final String FILE_EXTENSION=".xml";
	
	static Logger logger = Logger.getLogger(EmailXMLDao.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( EmailXMLDao.class.getPackage().getName() ) );
    }
	
	/**
	 * Builds the XML file using the specified arguments.
	 * Name of each file is its id field.
	 * 
	 * @param id the id
	 * @param from the F
	 * @param to the to
	 * @param cc the cc
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param location the location
	 * @return the document
	 */
	private Document buildXmlFile(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, String location, String meetingInvite) {
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

			// Is This Email for a Meeting Invite?
			Element isMeetingInvite = emailInXMLFormat
					.createElement("IsMeetingEmail");
			isMeetingInvite.appendChild(emailInXMLFormat.createTextNode(meetingInvite));
			emailRootElement.appendChild(isMeetingInvite);
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return emailInXMLFormat;

	}

	/** This method is responsible for building an XML file from the specified fields.
	 * It is also responsible for saving that file on the file system.
	 * Name of each file and Location where the file to be saved
	 * is given by the argument id and location.
	 * @param id the id
	 * @param from the from
	 * @param to the to
	 * @param cc the CC
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param location the location
	 */
	public void save(UUID id, String from, String to, String cc,
			String subject, String body, String lastModifiedTime,
			String sentTime, String location, String meetingInvite) {
        String path = location;
		String fileName = id.toString();
       	String pathToSaveFile = path + "/" + fileName
				+ FILE_EXTENSION;

		try {
			Document emailInXMLFormat = buildXmlFile(id, from, to, cc, subject,
					body, lastModifiedTime, sentTime, location, meetingInvite);
			
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
			logger.severe(StackTrace.asString(e));
		}
	}
	
	/**
	 * It deletes each file from the system.
	 * specification of an email to be deleted is given by
	 * argument path and filename respectively to identify each email before deleting it.
	 *  
	 * It deletes each file forcefully from the System just to avoid exceptions that generates if file
	 * is being used by another programs.
	 * 
	 * @param path the path
	 * @param fileName the file name
	 */
	public void delete(String path, UUID fileName){
       FileDeleteStrategy file = FileDeleteStrategy.FORCE;
       try{
    	   file.delete(new File(path+"/"+fileName.toString()+FILE_EXTENSION));
       }catch(Exception fileDeleteException){
    	   logger.severe(StackTrace.asString(fileDeleteException));;
       }     		
	}
	/**
	 * Moves an email from source folder to some different folder.
	 * Other folder or destination folder is specified by an argument desDir.
	 *
	 * @param fileName the file name
	 * @param srcDir the source  directory
	 * @param destDir the destination  directory
	 */
	public void move(UUID fileName, String srcDir, String destDir){
		try {
			File file = new File(srcDir+"/"+fileName.toString()+FILE_EXTENSION);
			File destinationDir = new File(destDir);
			updateXMLField(file.getPath(),destDir);
			FileUtils.moveFileToDirectory(file, destinationDir, false);
		} catch (Exception fileMoveException) {
			logger.severe(StackTrace.asString(fileMoveException));
		}
	}
	
	/**
	 * Updates the parentFolder tag value inside the xml file.
	 * this method is called by move method so that before it moves the file to another folder, it 
	 * should have the correct parentFolder tag value.
	 *
	 * @param xmlFile the xml file
	 * @param destDir the dest dir
	 */
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
			logger.severe(StackTrace.asString(exception));
		}
		
	}
	/**
	 * Loads an equivalent lower level representation of an email from a specific folder.
	 * It basically loads the email field values and returns a Map of 
	 * those values. 
	 *
	 * @param folder the folder
	 * @param FileName the file name
	 * @return the map
	 */
	public Map<String, String> loadEmail(String folder, String xmlFileName) {
		Map<String, String> emailData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(folder + "/" + xmlFileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document email = documentBuilder.parse(xmlFile);
			logger.info("loading Email(s) from folder: "+folder+ " and from file: "+xmlFileName+" " );
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
					emailData.put("IsMeetingEmail", eElement.getElementsByTagName("IsMeetingEmail").item(0).getTextContent());
					logger.info("Id: " + eElement.getElementsByTagName("Id").item(0).getTextContent());
					logger.info("From: " + eElement.getElementsByTagName("From").item(0).getTextContent());
					logger.info("To: " + eElement.getElementsByTagName("To").item(0).getTextContent());
					logger.info("CC: " + eElement.getElementsByTagName("CC").item(0).getTextContent());
					logger.info("Subject: " + eElement.getElementsByTagName("Subject").item(0).getTextContent());
					logger.info("Body: " + eElement.getElementsByTagName("Body").item(0).getTextContent());
					logger.info("LastModifiedTime: " + eElement.getElementsByTagName("LastModifiedTime").item(0).getTextContent());
					logger.info("SentTime: " + eElement.getElementsByTagName("SentTime").item(0).getTextContent());
					logger.info("ParentFolder: " + eElement.getElementsByTagName("ParentFolder").item(0).getTextContent());
					logger.info("IsMeetingEmail: " + eElement.getElementsByTagName("IsMeetingEmail").item(0).getTextContent());
					
				}
			}
		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
		return emailData;
	}
}
