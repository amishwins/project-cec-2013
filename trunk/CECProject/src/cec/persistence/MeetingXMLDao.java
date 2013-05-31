package cec.persistence;

import java.io.File;
import java.io.IOException;
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

import exceptions.StackTrace;

/**
 * 
 * EmailXMLDao is a class in the persistence layer responsible for handling
 * email life cycle events at a lower level. It saves each email in a XML file
 * format. it is also responsible for deleting an XML file and moving an XML
 * file from one folder to another folder.
 * 
 */
public class MeetingXMLDao implements MeetingDao {

	/** Specifies the extension of file. */
	private final String FILE_EXTENSION = ".xml";

	static Logger logger = Logger.getLogger(MeetingXMLDao.class.getName());

	static {
		logger.setParent(Logger.getLogger(MeetingXMLDao.class.getPackage()
				.getName()));
	}

	/**
	 * Builds the XML file using the specified arguments. Name of each file is
	 * its id field.
	 * 
	 * @param id
	 *            the id
	 * @param from
	 *            the F
	 * @param attendees
	 *            the to
	 * @param cc
	 *            the cc
	 * @param subject
	 *            the subject
	 * @param body
	 *            the body
	 * @param lastModifiedTime
	 *            the last modified time
	 * @param sentTime
	 *            the sent time
	 * @param location
	 *            the location
	 * @return the document
	 */
	private Document buildXmlFile(UUID id, String from, String attendees,
			String meetingStartDate, String meetingEndDate,
			String meetingStartTime, String meetingEndTime,
			String meetingPlace, String subject, String body,
			String lastModifiedTime, String sentTime, String location) {
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document emailInXMLFormat = null;

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();

			// root element Meeting
			emailInXMLFormat = documentBuilder.newDocument();
			Element emailRootElement = emailInXMLFormat
					.createElement("Meeting");
			emailInXMLFormat.appendChild(emailRootElement);

			// id
			Element idLabel = emailInXMLFormat.createElement("Id");
			idLabel.appendChild(emailInXMLFormat.createTextNode(id.toString()));
			emailRootElement.appendChild(idLabel);

			// from
			Element fromLabel = emailInXMLFormat.createElement("From");
			fromLabel.appendChild(emailInXMLFormat.createTextNode(from));
			emailRootElement.appendChild(fromLabel);

			// attendees
			Element attendeesLabel = emailInXMLFormat
					.createElement("Attendees");
			attendeesLabel.appendChild(emailInXMLFormat
					.createTextNode(attendees));
			emailRootElement.appendChild(attendeesLabel);

			// meetingStartDate
			Element meetingStartDateLabel = emailInXMLFormat
					.createElement("MeetingStartDate");
			meetingStartDateLabel.appendChild(emailInXMLFormat
					.createTextNode(meetingStartDate));
			emailRootElement.appendChild(meetingStartDateLabel);

			// meetingEndDate
			Element meetingEndDateLabel = emailInXMLFormat
					.createElement("MeetingEndDate");
			meetingEndDateLabel.appendChild(emailInXMLFormat
					.createTextNode(meetingEndDate));
			emailRootElement.appendChild(meetingEndDateLabel);

			// meetingStartTime
			Element meetingStartTimeLabel = emailInXMLFormat
					.createElement("MeetingStartTime");
			meetingStartTimeLabel.appendChild(emailInXMLFormat
					.createTextNode(meetingStartTime));
			emailRootElement.appendChild(meetingStartTimeLabel);

			// meetingEndTime
			Element meetingEndTimeLabel = emailInXMLFormat
					.createElement("MeetingEndTime");
			meetingEndTimeLabel.appendChild(emailInXMLFormat
					.createTextNode(meetingEndTime));
			emailRootElement.appendChild(meetingEndTimeLabel);

			// meetingPlace
			Element meetingPlaceLabel = emailInXMLFormat
					.createElement("MeetingPlace");
			meetingPlaceLabel.appendChild(emailInXMLFormat
					.createTextNode(meetingPlace));
			emailRootElement.appendChild(meetingPlaceLabel);

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

	/**
	 * This method is responsible for building an XML file from the specified
	 * fields. It is also responsible for saving that file on the file system.
	 * Name of each file and Location where the file to be saved is given by the
	 * argument id and location.
	 * 
	 * @param id
	 *            the id
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param cc
	 *            the CC
	 * @param subject
	 *            the subject
	 * @param body
	 *            the body
	 * @param lastModifiedTime
	 *            the last modified time
	 * @param sentTime
	 *            the sent time
	 * @param location
	 *            the location
	 */
	public void save(UUID id, String from, String attendees,
			String meetingStartDate, String meetingEndDate,
			String meetingStartTime, String meetingEndTime,
			String meetingPlace, String subject, String body,
			String lastModifiedTime, String sentTime, String location) {
		String path = location;
		String fileName = id.toString();
		String pathToSaveFile = path + "/" + fileName + FILE_EXTENSION;

		try {
			Document emailInXMLFormat = buildXmlFile(id, from, attendees,
					meetingStartDate, meetingEndDate, meetingStartTime,
					meetingEndTime, meetingPlace, subject, body,
					lastModifiedTime, sentTime, location);

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
	 * It deletes each file from the system. specification of an email to be
	 * deleted is given by argument path and filename respectively to identify
	 * each email before deleting it.
	 * 
	 * It deletes each file forcefully from the System just to avoid exceptions
	 * that generates if file is being used by another programs.
	 * 
	 * @param path
	 *            the path
	 * @param fileName
	 *            the file name
	 */
	public void delete(String path, UUID fileName) {
		FileDeleteStrategy file = FileDeleteStrategy.FORCE;
		try {
			file.delete(new File(path + "/" + fileName.toString()
					+ FILE_EXTENSION));
		} catch (Exception fileDeleteException) {
			logger.severe(StackTrace.asString(fileDeleteException));
		}
	}

	/**
	 * Loads an equivalent lower level representation of an email from a
	 * specific folder. It basically loads the email field values and returns a
	 * Map of those values.
	 * 
	 * @param folder
	 *            the folder
	 * @param FileName
	 *            the file name
	 * @return the map
	 */
	public Map<String, String> loadMeeting(String folder, String xmlFileName) {
		Map<String, String> meetingData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(folder + "/" + xmlFileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document email = documentBuilder.parse(xmlFile);
			logger.info("loading Meeting(s) Data from folder: " + folder
					+ " and from file: " + xmlFileName + " ");
			email.getDocumentElement().normalize();
			NodeList listOfEmailFields = email.getElementsByTagName("Meeting");
			for (int index = 0; index < listOfEmailFields.getLength(); index++) {
				Node emailField = listOfEmailFields.item(index);
				if (emailField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) emailField;
					meetingData.put("Id", eElement.getElementsByTagName("Id")
							.item(0).getTextContent());
					meetingData.put("From", eElement.getElementsByTagName("From")
							.item(0).getTextContent());
					meetingData.put("Attendees", eElement.getElementsByTagName("Attendees")
							.item(0).getTextContent());
					meetingData.put("MeetingStartDate", eElement.getElementsByTagName("MeetingStartDate")
							.item(0).getTextContent());
					meetingData.put("MeetingEndDate", eElement.getElementsByTagName("MeetingEndDate")
							.item(0).getTextContent());
					meetingData.put("MeetingStartTime", eElement.getElementsByTagName("MeetingStartTime")
							.item(0).getTextContent());
					meetingData.put("MeetingEndTime", eElement.getElementsByTagName("MeetingEndTime")
							.item(0).getTextContent());
					meetingData.put("MeetingPlace", eElement.getElementsByTagName("MeetingPlace")
							.item(0).getTextContent());
					meetingData.put("Subject",
							eElement.getElementsByTagName("Subject").item(0)
									.getTextContent());
					meetingData.put("Body", eElement.getElementsByTagName("Body")
							.item(0).getTextContent());
					meetingData.put("LastModifiedTime", eElement
							.getElementsByTagName("LastModifiedTime").item(0)
							.getTextContent());
					meetingData.put("SentTime",
							eElement.getElementsByTagName("SentTime").item(0)
									.getTextContent());
					meetingData.put("ParentFolder", eElement
							.getElementsByTagName("ParentFolder").item(0)
							.getTextContent());
				}
			}
		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
		return meetingData;
	}
}
