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

    public Document buildXmlFile(String to, String cc, String subject, String body, String lastAccessedTime) {

        DocumentBuilderFactory documentFactory = null;
        DocumentBuilder documentBuilder = null;
        Document emailInXMLFormat = null;
        try {

            documentFactory = DocumentBuilderFactory
                    .newInstance();
            documentBuilder = documentFactory.newDocumentBuilder();

            // root element Email
            emailInXMLFormat = documentBuilder.newDocument();
            Element emailRootElement = emailInXMLFormat.createElement("E-Mail");
            emailInXMLFormat.appendChild(emailRootElement);

            // to
            Element toLabel = emailInXMLFormat.createElement("To");
            toLabel.appendChild(emailInXMLFormat.createTextNode(to));
            emailRootElement.appendChild(toLabel);

            // cc
            Element ccLabel = emailInXMLFormat.createElement("CC");
            ccLabel.appendChild(emailInXMLFormat.createTextNode(cc));
            emailRootElement.appendChild(ccLabel);
            
            // lastAccessedTime
            Element lastAccessedTimeLabel = emailInXMLFormat.createElement("LastAccessedTime");
            lastAccessedTimeLabel.appendChild(emailInXMLFormat.createTextNode(lastAccessedTime));
            emailRootElement.appendChild(lastAccessedTimeLabel);
            
            // subject
            Element subjectLabel = emailInXMLFormat.createElement("Subject");
            subjectLabel.appendChild(emailInXMLFormat.createTextNode(subject));
            emailRootElement.appendChild(subjectLabel);

            // body
            Element bodyLabel = emailInXMLFormat.createElement("Body");
            bodyLabel.appendChild(emailInXMLFormat.createTextNode(body));
            emailRootElement.appendChild(bodyLabel);


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        return emailInXMLFormat;

    }

    public void save(String to, String cc, String subject, String body,String lastAccessedTime, String folder) {
        final String ROOT_FOLDER = "emails";
        final String INNER_FOLDER = folder;
        final String FILE_NAME=lastAccessedTime;
        final String PATH_TO_SAVE_EMAIL = ROOT_FOLDER+"/"+INNER_FOLDER+"/"+FILE_NAME;
        try {
            Document emailInXMLFormat = buildXmlFile(to ,cc , subject, body ,lastAccessedTime);        
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(emailInXMLFormat);
            StreamResult result = new StreamResult(new File(                   
                    PATH_TO_SAVE_EMAIL));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

         } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
