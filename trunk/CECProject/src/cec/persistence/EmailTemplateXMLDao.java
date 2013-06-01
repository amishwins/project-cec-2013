package cec.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
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
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EmailTemplateXMLDao implements EmailTemplateDao {
	private static final String pathToTemplate = "templates/";

	@Override
	public void saveAsTemplate(String name, String to, String cc, String subject, String body) {

		String pathToSaveFile = pathToTemplate + name + ".xml"; // TODO: move to configurator
		
		try {
			Document emailTemplateInXMLFormat = buildXmlFile(name, to, cc, subject, body);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			StreamSource stylesource = new StreamSource(getClass().getResourceAsStream("proper-indenting.xsl"));
			Transformer transformer = transformerFactory.newTransformer(stylesource);			
			DOMSource source = new DOMSource(emailTemplateInXMLFormat);
			StreamResult result = new StreamResult(new File(pathToSaveFile));
		
			transformer.transform(source, result);
		
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot save template!");
		}
	
	}

	@Override
	public void deleteTemplate(String fileName) {
		FileDeleteStrategy file = FileDeleteStrategy.FORCE;
		try{
			file.delete(new File( pathToTemplate + fileName.toString() + ".xml")); // TODO: get rid of magic #
		} catch(IOException fileDeleteException){
			fileDeleteException.printStackTrace();
			throw new RuntimeException("Cannot delete template!");
		}    
	}

	@Override
	public Map<String, String> loadTemplate(String fileName) {
		Map<String, String> templateData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(pathToTemplate + fileName + ".xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document email = documentBuilder.parse(xmlFile);
			email.getDocumentElement().normalize();
			NodeList listOfTemplateFields = email.getElementsByTagName("Template");
			for (int index = 0; index < listOfTemplateFields.getLength(); index++) {
				Node templateField = listOfTemplateFields.item(index);
				if (templateField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) templateField;
					templateData.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
					templateData.put("To", eElement.getElementsByTagName("To").item(0).getTextContent());
					templateData.put("CC", eElement.getElementsByTagName("CC").item(0).getTextContent());
					templateData.put("Subject", eElement.getElementsByTagName("Subject").item(0).getTextContent());
					templateData.put("Body", eElement.getElementsByTagName("Body").item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot load template!");
		}
		return templateData;
	}

	
	private Document buildXmlFile(String name, String to, String cc, String subject, String body) {
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document emailInXMLFormat = null;
		
		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();			
			
			// root element Email
			emailInXMLFormat = documentBuilder.newDocument();
			Element emailRootElement = emailInXMLFormat.createElement("Template");
			emailInXMLFormat.appendChild(emailRootElement);

			// template name
			Element nameLabel = emailInXMLFormat.createElement("Name");
			nameLabel.appendChild(emailInXMLFormat.createTextNode(name));
			emailRootElement.appendChild(nameLabel);
			
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

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return emailInXMLFormat;
	}

	@Override
	public Iterable<Map<String, String>> loadAllTemplates() {
		Collection<Map<String, String>> listOfTepmlates = new ArrayList<>();
		Map<String, String> template;
		
		String[] xmlFileNames = FolderDaoImpl.getFileNames(pathToTemplate);
		for (String xmlFileName : xmlFileNames) {
			xmlFileName = FilenameUtils.removeExtension(xmlFileName);
			template = loadTemplate(xmlFileName);
				listOfTepmlates.add(template);
		}
		return listOfTepmlates;
	}
}
