/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Pankaj Kapania
 */
public class NewMessageXMLDao implements NewMessageDao {

	public void save(String to, String subject, String body, String location) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element eMail = doc.createElement("E-Mail");
			doc.appendChild(eMail);

			// to
			Element toLabel = doc.createElement("To");
			toLabel.appendChild(doc.createTextNode(to));
			eMail.appendChild(toLabel);

			// subject
			Element subjectLabel = doc.createElement("Subject");
			subjectLabel.appendChild(doc.createTextNode(subject));
			eMail.appendChild(subjectLabel);

			// body
			Element bodyLabel = doc.createElement("Body");
			bodyLabel.appendChild(doc.createTextNode(body));
			eMail.appendChild(bodyLabel);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"C:\\temp\\file.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}
}
