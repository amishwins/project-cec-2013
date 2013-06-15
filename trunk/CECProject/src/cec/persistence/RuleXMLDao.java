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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileDeleteStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cec.config.CECConfigurator;
import cec.exceptions.StackTrace;


// TODO: Auto-generated Javadoc
/**
 * 
 * RuleXMLDao is a class in the persistence layer responsible for handling rule
 * life cycle events at a lower level. It saves each rule in a XML file format.
 * it is also responsible for deleting an XML file and swapping the ranks of 2
 * rules.
 * 
 */
public class RuleXMLDao implements RuleDao {

	/** Specifies the extension of file. */
	private final String FILE_EXTENSION = ".xml";

	/** The logger. */
	static Logger logger = Logger.getLogger(RuleXMLDao.class.getName());

	static {
		logger.setParent(Logger.getLogger(RuleXMLDao.class.getPackage()
				.getName()));
	}

	/**
	 * Builds the XML file using the specified arguments. Name of each file is
	 * its id field.
	 * 
	 * @param id
	 *            the id
	 * @param rank
	 *            the rank
	 * @param sender
	 *            the sender
	 * @param keywords
	 *            the keywords
	 * @param targetFolder
	 *            the target folder
	 * @param status
	 *            the status
	 * @return the document
	 */
	private Document buildXmlFile(UUID id, String rank, String sender,
			String keywords, String targetFolder, String status) {

		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document ruleInXMLFormat = null;

		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();

			// root element rule
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
			Element tartgetFolderLabel = ruleInXMLFormat
					.createElement("TartgetFolder");
			tartgetFolderLabel.appendChild(ruleInXMLFormat
					.createTextNode(targetFolder));
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
	 * It deletes each file from the system. specification of an rule to be
	 * deleted is given by argument path and filename respectively to identify
	 * each rule before deleting it.
	 * 
	 * Precondition: Rule file must exists on the System. Postcondition: Rule
	 * file does not in the System any more. Invariant: System remains in the
	 * consistent change.
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
			;
		}
	}

	/**
	 * This method is responsible for building an XML file from the specified
	 * fields. It is also responsible for saving that file on the file system.
	 * Name of each file and Location where the file to be saved is given by the
	 * argument id and location. Precondition: each rule must have id and
	 * related data fields to be persisted on the System. Postcondition: Each
	 * rule will have its id and rank, and must persists on the file system.
	 * 
	 * @param id
	 *            the id
	 * @param sender
	 *            the sender
	 * @param keywords
	 *            the keywords
	 * @param targetFolder
	 *            the target folder
	 * @param status
	 *            the status
	 * @param pathToSaveRuleFile
	 *            the path to save rule file
	 */
	@Override
	public synchronized void save(UUID id, String sender, String keywords,
			String targetFolder, String status, String pathToSaveRuleFile) {

		String path = pathToSaveRuleFile;
		String fileName = id.toString();
		String pathToSaveFile = path + "/" + fileName + FILE_EXTENSION;
		String rank = String.valueOf(getLatestRank() + 1);
		try {
			Document emailInXMLFormat = buildXmlFile(id, rank, sender,
					keywords, targetFolder, status);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			StreamSource stylesource = new StreamSource(getClass()
					.getResourceAsStream("proper-indenting.xsl"));
			Transformer transformer = transformerFactory
					.newTransformer(stylesource);
			DOMSource source = new DOMSource(emailInXMLFormat);
			StreamResult result = new StreamResult(new File(pathToSaveFile));

			transformer.transform(source, result);
			updateRank(rank);

		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}

	}

	/**
	 * Loads an equivalent lower level representation of a rule from a specific
	 * folder. It basically loads the meeting field values and returns a Map of
	 * those values.
	 * 
	 * @param folder
	 *            the folder
	 * @param ruleXmlFileName
	 *            the rule xml file name
	 * @return the map
	 */
	@Override
	public Map<String, String> loadRule(String folder, String ruleXmlFileName) {
		Map<String, String> ruleData = new TreeMap<String, String>();
		try {
			File xmlFile = new File(folder + "/" + ruleXmlFileName);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document rule = documentBuilder.parse(xmlFile);
			logger.info("loading Rules(s) from folder: " + folder
					+ " and from file: " + ruleXmlFileName + " ");
			rule.getDocumentElement().normalize();
			NodeList listOfRuleFields = rule.getElementsByTagName("Rule");
			for (int index = 0; index < listOfRuleFields.getLength(); index++) {
				Node ruleField = listOfRuleFields.item(index);
				if (ruleField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) ruleField;
					ruleData.put("Id", eElement.getElementsByTagName("Id")
							.item(0).getTextContent());
					ruleData.put("Rank", eElement.getElementsByTagName("Rank")
							.item(0).getTextContent());
					ruleData.put("Senders",
							eElement.getElementsByTagName("Senders").item(0)
									.getTextContent());
					ruleData.put("Keywords",
							eElement.getElementsByTagName("Keywords").item(0)
									.getTextContent());
					ruleData.put("TartgetFolder", eElement
							.getElementsByTagName("TartgetFolder").item(0)
							.getTextContent());
					ruleData.put("Status",
							eElement.getElementsByTagName("Status").item(0)
									.getTextContent());

					logger.info("Id: "
							+ eElement.getElementsByTagName("Id").item(0)
									.getTextContent());
					logger.info("Rank: "
							+ eElement.getElementsByTagName("Rank").item(0)
									.getTextContent());
					logger.info("Senders: "
							+ eElement.getElementsByTagName("Senders").item(0)
									.getTextContent());
					logger.info("Keywords: "
							+ eElement.getElementsByTagName("Keywords").item(0)
									.getTextContent());
					logger.info("TartgetFolder: "
							+ eElement.getElementsByTagName("TartgetFolder")
									.item(0).getTextContent());
					logger.info("Status: "
							+ eElement.getElementsByTagName("Status").item(0)
									.getTextContent());
				}
			}
		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
		return ruleData;
	}

	
	/**
     * It returns the equivalent low level representation of rule object under given 
     *  folder specified by the argument folder. It basically returns data pertaining 
     *  to all the rules under a given folder in the form of a collection of 
     *  Map<String,String>.
     *  Precondition: Rules must exists in the rules folder.
     *  Postcondition: Rules list has been returned to the caller.
     *  invariant: rules data has not been changed during invocation.
     * 
     * @param folder the folder
     * @return the iterable
     */
	@Override
	public Iterable<Map<String, String>> loadAllRules(String pathToRuleFolder) {
		Collection<Map<String, String>> listOfRules = new ArrayList<>();
		Map<String, String> rule;
		String[] xmlFileNames = FolderDaoImpl.getFileNames(pathToRuleFolder);
		for (String xmlFileName : xmlFileNames) {
			rule = loadRule(pathToRuleFolder, xmlFileName);
			listOfRules.add(rule);
		}
		return listOfRules;
	}

	/**
	 * Gets the latest rank. Precondition: Rank file must persist on the System.
	 * 
	 * @return the latest rank
	 */
	private synchronized int getLatestRank() {
		int rank = -1;
		try {
			File xmlFile = new File(CECConfigurator.getReference().get(
					"LastRank"));
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document lastRank = documentBuilder.parse(xmlFile);
			logger.info("loading Rank from xmlFile: " + xmlFile);

			lastRank.getDocumentElement().normalize();
			NodeList listOfRuleFields = lastRank.getElementsByTagName("Rule");
			for (int index = 0; index < listOfRuleFields.getLength(); index++) {
				Node ruleField = listOfRuleFields.item(index);
				if (ruleField.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) ruleField;
					rank = Integer.parseInt(eElement
							.getElementsByTagName("Rank").item(0)
							.getTextContent());

					logger.info("Rank: " + rank);
				}
			}
		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
		return rank;
	}

	/**
	 * Precondition: Rank file must persists on the system. Postcondition:
	 * latest rank has been returned for the new rule request.
	 * 
	 * @param rank
	 *            the rank
	 */
	private synchronized void updateRank(String rank) {
		DocumentBuilderFactory documentFactory = null;
		DocumentBuilder documentBuilder = null;
		Document ruleRankInXMLFormat = null;

		try {
			File xmlFile = new File(CECConfigurator.getReference().get(
					"LastRank"));
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			ruleRankInXMLFormat = documentBuilder.parse(xmlFile);
			ruleRankInXMLFormat.getDocumentElement().normalize();

			NodeList rule = ruleRankInXMLFormat.getElementsByTagName("Rule");
			Element field = (Element) rule.item(0);
			Node newRank = field.getElementsByTagName("Rank").item(0)
					.getFirstChild();
			newRank.setNodeValue(rank);

			ruleRankInXMLFormat.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(ruleRankInXMLFormat);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);
		} catch (Exception exception) {
			logger.severe(StackTrace.asString(exception));
		}

	}

	/**
	 * Invariant: rule ID will not be changed even after the modification of the
	 * other fields. Saves each rule object with latest changes to its
	 * equivalent lower level representation( for example : a File).
	 * Postconditions: Rule changes will be persisted on the system.
	 * 
	 * @param id
	 *            the id
	 * @param rank
	 *            the rank
	 * @param sender
	 *            the sender
	 * @param keywords
	 *            the keywords
	 * @param tartgetFolder
	 *            the tartget folder
	 * @param status
	 *            the status
	 * @param pathToSaveRuleFile
	 *            the path to save rule file
	 */

	@Override
	public synchronized void update(UUID id, String rank, String sender,
			String keywords, String tartgetFolder, String status,
			String pathToSaveRuleFile) {
		String path = pathToSaveRuleFile;
		String fileName = id.toString();
		String pathToSaveFile = path + "/" + fileName + FILE_EXTENSION;
		try {
			Document emailInXMLFormat = buildXmlFile(id, rank, sender,
					keywords, tartgetFolder, status);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			StreamSource stylesource = new StreamSource(getClass()
					.getResourceAsStream("proper-indenting.xsl"));
			Transformer transformer = transformerFactory
					.newTransformer(stylesource);
			DOMSource source = new DOMSource(emailInXMLFormat);
			StreamResult result = new StreamResult(new File(pathToSaveFile));

			transformer.transform(source, result);

		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}

	}

}
