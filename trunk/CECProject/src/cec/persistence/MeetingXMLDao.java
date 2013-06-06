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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.StackTrace;

/**
 * 
 * MeetingXMLDao is a class in the persistence layer responsible for handling
 * meeting life cycle events at a lower level. It saves each meeting in a XML file
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
	 * Postcondition: It builds the XML file and returns a Document Object.
	 *
	 * @param id the id
	 * @param from the from
	 * @param attendees the attendees
	 * @param meetingStartDate the meeting start date
	 * @param meetingEndDate the meeting end date
	 * @param meetingStartTime the meeting start time
	 * @param meetingEndTime the meeting end time
	 * @param meetingPlace the meeting place
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param location the location
	 * @return the document
	 */
	private Document buildXmlFile(UUID id, String from, String attendees,
			String meetingStartDate, String meetingEndDate,
			String meetingStartTime, String meetingEndTime,
			String meetingPlace, String subject, String body,
			String lastModifiedTime, String sentTime, String location) {
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document meetingInXMLFormat = null;

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();

			// root element Meeting
			meetingInXMLFormat = documentBuilder.newDocument();
			Element meetingRootElement = meetingInXMLFormat
					.createElement("Meeting");
			meetingInXMLFormat.appendChild(meetingRootElement);

			// id
			Element idLabel = meetingInXMLFormat.createElement("Id");
			idLabel.appendChild(meetingInXMLFormat.createTextNode(id.toString()));
			meetingRootElement.appendChild(idLabel);

			// from
			Element fromLabel = meetingInXMLFormat.createElement("From");
			fromLabel.appendChild(meetingInXMLFormat.createTextNode(from));
			meetingRootElement.appendChild(fromLabel);

			// attendees
			Element attendeesLabel = meetingInXMLFormat
					.createElement("Attendees");
			attendeesLabel.appendChild(meetingInXMLFormat
					.createTextNode(attendees));
			meetingRootElement.appendChild(attendeesLabel);

			// meetingStartDate
			Element meetingStartDateLabel = meetingInXMLFormat
					.createElement("MeetingStartDate");
			meetingStartDateLabel.appendChild(meetingInXMLFormat
					.createTextNode(meetingStartDate));
			meetingRootElement.appendChild(meetingStartDateLabel);

			// meetingEndDate
			Element meetingEndDateLabel = meetingInXMLFormat
					.createElement("MeetingEndDate");
			meetingEndDateLabel.appendChild(meetingInXMLFormat
					.createTextNode(meetingEndDate));
			meetingRootElement.appendChild(meetingEndDateLabel);

			// meetingStartTime
			Element meetingStartTimeLabel = meetingInXMLFormat
					.createElement("MeetingStartTime");
			meetingStartTimeLabel.appendChild(meetingInXMLFormat
					.createTextNode(meetingStartTime));
			meetingRootElement.appendChild(meetingStartTimeLabel);

			// meetingEndTime
			Element meetingEndTimeLabel = meetingInXMLFormat
					.createElement("MeetingEndTime");
			meetingEndTimeLabel.appendChild(meetingInXMLFormat
					.createTextNode(meetingEndTime));
			meetingRootElement.appendChild(meetingEndTimeLabel);

			// meetingPlace
			Element meetingPlaceLabel = meetingInXMLFormat
					.createElement("MeetingPlace");
			meetingPlaceLabel.appendChild(meetingInXMLFormat
					.createTextNode(meetingPlace));
			meetingRootElement.appendChild(meetingPlaceLabel);

			// subject
			Element subjectLabel = meetingInXMLFormat.createElement("Subject");
			subjectLabel.appendChild(meetingInXMLFormat.createTextNode(subject));
			meetingRootElement.appendChild(subjectLabel);

			// body
			Element bodyLabel = meetingInXMLFormat.createElement("Body");
			bodyLabel.appendChild(meetingInXMLFormat.createTextNode(body));
			meetingRootElement.appendChild(bodyLabel);

			// lastModifiedTime
			Element lastModifiedTimeLabel = meetingInXMLFormat
					.createElement("LastModifiedTime");
			lastModifiedTimeLabel.appendChild(meetingInXMLFormat
					.createTextNode(lastModifiedTime));
			meetingRootElement.appendChild(lastModifiedTimeLabel);

			// sentTime
			Element sentTimeLabel = meetingInXMLFormat.createElement("SentTime");
			sentTimeLabel
					.appendChild(meetingInXMLFormat.createTextNode(sentTime));
			meetingRootElement.appendChild(sentTimeLabel);

			// parent folder
			Element parentFolder = meetingInXMLFormat
					.createElement("ParentFolder");
			parentFolder.appendChild(meetingInXMLFormat.createTextNode(location));
			meetingRootElement.appendChild(parentFolder);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return meetingInXMLFormat;

	}

	/**
	 * This method is responsible for building an XML file from the specified
	 * fields. It is also responsible for saving that file on the file system.
	 * Name of each file and Location where the file to be saved is given by the
	 * argument id and location.
	 * 
	 * Postcondition: Each meeting will have its id and persisted on the file system.
	 * 
	 * @param id the id
	 * @param from the from
	 * @param attendees the attendees
	 * @param meetingStartDate the meeting start date
	 * @param meetingEndDate the meeting end date
	 * @param meetingStartTime the meeting start time
	 * @param meetingEndTime the meeting end time
	 * @param meetingPlace the meeting place
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param location the location
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
			Document meetingInXMLFormat = buildXmlFile(id, from, attendees,
					meetingStartDate, meetingEndDate, meetingStartTime,
					meetingEndTime, meetingPlace, subject, body,
					lastModifiedTime, sentTime, location);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			StreamSource stylesource = new StreamSource(getClass()
					.getResourceAsStream("proper-indenting.xsl"));
			Transformer transformer = transformerFactory
					.newTransformer(stylesource);
			DOMSource source = new DOMSource(meetingInXMLFormat);
			StreamResult result = new StreamResult(new File(pathToSaveFile));

			transformer.transform(source, result);

		} catch (TransformerException e) {
			logger.severe(StackTrace.asString(e));
		}
	}

	/**
	 * It deletes each file from the system. specification of an meeting to be
	 * deleted is given by argument path and filename respectively to identify
	 * each meeting before deleting it.
	 * 
	 * It deletes each file forcefully from the System just to avoid exceptions
	 * that generates if file is being used by another programs.
	 *
	 * @param path the path
	 * @param fileName the file name
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
	 * Loads an equivalent lower level representation of an meeting from a
	 * specific folder. It basically loads the meeting field values and returns a
	 * Map of those values.
	 *
	 * @param folder the folder
	 * @param xmlFileName the xml file name
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
			Document meeting = documentBuilder.parse(xmlFile);
			logger.info("loading Meeting(s) Data from folder: " + folder
					+ " and from file: " + xmlFileName + " ");
			meeting.getDocumentElement().normalize();
			NodeList listOfMeetingFields = meeting.getElementsByTagName("Meeting");
			for (int index = 0; index < listOfMeetingFields.getLength(); index++) {
				Node meetingField = listOfMeetingFields.item(index);
				if (meetingField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) meetingField;
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
