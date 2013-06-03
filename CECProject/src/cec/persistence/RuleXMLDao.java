package cec.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.StackTrace;


/**
 * 
 * EmailXMLDao is a class in the persistence layer responsible for handling 
 * email life cycle events at a lower level. It saves each email in a XML file format.
 * it is also responsible for deleting an XML file  and moving an XML file from one folder to 
 * another folder.
 * 
 */
public class RuleXMLDao implements RuleDao {
	
	/** Specifies the extension of file. */
	private final String FILE_EXTENSION=".xml";
	
	static Logger logger = Logger.getLogger(RuleXMLDao.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( RuleXMLDao.class.getPackage().getName() ) );
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
	private Document buildXmlFile(UUID id, String rank, String sender, String keywords,
			String tartgetFolder, String status) {
		
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document ruleInXMLFormat = null;
		
		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();			
			
			// root element Email
			ruleInXMLFormat = documentBuilder.newDocument();
			Element ruleRootElement = ruleInXMLFormat.createElement("Rule");
			ruleInXMLFormat.appendChild(ruleRootElement);

			// id
			Element idLabel = ruleInXMLFormat.createElement("Id");
			idLabel.appendChild(ruleInXMLFormat.createTextNode(id.toString()));
			ruleRootElement.appendChild(idLabel);

			// rank
			Element rankLabel = ruleInXMLFormat.createElement("Rank");
			rankLabel.appendChild(ruleInXMLFormat.createTextNode(rank));
			ruleRootElement.appendChild(rankLabel);

			// senders
			Element sendersLabel = ruleInXMLFormat.createElement("Senders");
			sendersLabel.appendChild(ruleInXMLFormat.createTextNode(sender));
			ruleRootElement.appendChild(sendersLabel);

			// keywords
			Element keywordsLabel = ruleInXMLFormat.createElement("Keywords");
			keywordsLabel.appendChild(ruleInXMLFormat.createTextNode(keywords));
			ruleRootElement.appendChild(keywordsLabel);

			// tartgetFolder
			Element tartgetFolderLabel = ruleInXMLFormat.createElement("TartgetFolder");
			tartgetFolderLabel.appendChild(ruleInXMLFormat.createTextNode(tartgetFolder));
			ruleRootElement.appendChild(tartgetFolderLabel);

			// status
			Element statusLabel = ruleInXMLFormat.createElement("Status");
			statusLabel.appendChild(ruleInXMLFormat.createTextNode(status));
			ruleRootElement.appendChild(statusLabel);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return ruleInXMLFormat;

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


	@Override
	public void save(UUID id, String rank, String sender, String keywords,
			String tartgetFolder, String status, String pathToSaveRuleFile) {

    String path = pathToSaveRuleFile;
	String fileName = id.toString();
   	String pathToSaveFile = path + "/" + fileName
			+ FILE_EXTENSION;

	try {
		Document emailInXMLFormat = buildXmlFile(id, rank, sender, keywords, tartgetFolder,
				status);
		
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

	
	@Override
	public Map<String, String> loadRule(String folder, String ruleXmlFileName) {
		Map<String, String> ruleData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(folder + "/" + ruleXmlFileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document rule = documentBuilder.parse(xmlFile);
			logger.info("loading Rules(s) from folder: "+folder+ " and from file: "+ruleXmlFileName+" " );
			rule.getDocumentElement().normalize();
			NodeList listOfRuleFields = rule.getElementsByTagName("Rule");
			for (int index = 0; index < listOfRuleFields.getLength(); index++) {
				Node ruleField = listOfRuleFields.item(index);
				if (ruleField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) ruleField;
					ruleData.put("Id", eElement.getElementsByTagName("Id").item(0).getTextContent());
					ruleData.put("Rank", eElement.getElementsByTagName("Rank").item(0).getTextContent());
					ruleData.put("Senders", eElement.getElementsByTagName("Senders").item(0).getTextContent());
					ruleData.put("Keywords", eElement.getElementsByTagName("Keywords").item(0).getTextContent());
					ruleData.put("TartgetFolder", eElement.getElementsByTagName("TartgetFolder").item(0).getTextContent());
					ruleData.put("Status", eElement.getElementsByTagName("Status").item(0).getTextContent());
					
					logger.info("Id: " + eElement.getElementsByTagName("Id").item(0).getTextContent());
					logger.info("Rank: " + eElement.getElementsByTagName("Rank").item(0).getTextContent());
					logger.info("Senders: " + eElement.getElementsByTagName("Senders").item(0).getTextContent());
					logger.info("Keywords: " + eElement.getElementsByTagName("Keywords").item(0).getTextContent());
					logger.info("TartgetFolder: " + eElement.getElementsByTagName("TartgetFolder").item(0).getTextContent());
					logger.info("Status: " + eElement.getElementsByTagName("Status").item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
		return ruleData;
	}

	
	@Override
	public Iterable<Map<String, String>> loadAllRules(String pathToRuleFolder) {
		Collection<Map<String, String>> listOfRules = new ArrayList<>();
		Map<String, String> template;
		
		String[] xmlFileNames = FolderDaoImpl.getFileNames(pathToRuleFolder);
		for (String xmlFileName : xmlFileNames) {
			xmlFileName = FilenameUtils.removeExtension(xmlFileName);
			template = loadRule(pathToRuleFolder, xmlFileName);
				listOfRules.add(template);
		}
		return listOfRules;
	}
	
}
