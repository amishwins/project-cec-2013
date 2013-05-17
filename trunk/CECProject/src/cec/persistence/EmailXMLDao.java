/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.persistence;

import java.io.File;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Pankaj Kapania
 */
public class EmailXMLDao implements EmailDao {

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
		final String FOLDER_NAME = location;
		final String FILE_NAME = id.toString();
		final String EXTENSION = ".xml";
		final String PATH_TO_SAVE_EMAIL = FOLDER_NAME + "/" + FILE_NAME
				+ EXTENSION;

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
			StreamResult result = new StreamResult(new File(PATH_TO_SAVE_EMAIL));

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
