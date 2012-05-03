package edu.uci.ics.websnippetparser.algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This Class deals with all writes to an XML file.
 * 
 * 
 * @author C. Albert Thompson, Rosalva Gallardo
 * @version 1.0
 * 
 */

public class RepositoryMaker {

	/**
	 * This is the XML file in electronic representation.
	 */
	private Document repositoryDoc;
	/**
	 * Tells the program where the repository is kept.
	 */
	private String repositoryFileName = "Repository/Forums/Repository.xml";

	/**
	 * Class constructor needs a copy of the repository to write out.
	 * 
	 * @param repositoryDoc
	 *            The name of the file being worked on.
	 * 
	 */

	public RepositoryMaker(Document repositoryDoc) {
		this.repositoryDoc = repositoryDoc;
	}

	/**
	 * 
	 * Outputs the current XML file in memory to hard disk.
	 * 
	 * 
	 */

	public void writeXML() throws TransformerFactoryConfigurationError,
			TransformerException {
		// Converts XML back to something to be put out onto a file
		//
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		// initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(repositoryDoc);
		transformer.transform(source, result);

		String xmlString = result.getWriter().toString();
		// System.out.println(xmlString);
		Writer output = null;
		File file = new File(repositoryFileName);
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(xmlString);
			output.close();
		} catch (IOException e) {
			System.out
					.println("While men try to kill time, It slowly kills them.");
			e.printStackTrace();
		}
		// System.out.println("Your file has been written");
	}

	/**
	 * Updates the text value of the tag that is passed.
	 * 
	 * @param locationContent
	 *            The name of the file being worked on.
	 * @param location
	 * 
	 */

	public void tagUpdater(String locationContent, Node location) {
		location.setTextContent(locationContent);
	}

	/**
	 * Creates a new nested Node for the XML Node passed(savedTag.
	 * 
	 * @param tool_generated_tag
	 *            Name of the tag to be created
	 * @param fileName
	 *            The name of the file being worked on.
	 * @param savedTag
	 *            Location to save the new tag
	 * 
	 */
	public void tagCreator(String tool_generated_tag, String fileName,
			Node savedTag) {
		Node toolGeneratedNode = repositoryDoc
				.createElement(tool_generated_tag);
		toolGeneratedNode.setTextContent(fileName);
		savedTag.appendChild(toolGeneratedNode);
	}

//	public void createTag(String fileName, String url) {
//	
//		Node newNode = repositoryDoc.createElement("site");
//		NodeList x = repositoryDoc.getElementsByTagName("repository");
//		x.
//		x.appendChild(newNode);
//	}

}
