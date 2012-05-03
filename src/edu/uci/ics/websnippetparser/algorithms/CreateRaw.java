package edu.uci.ics.websnippetparser.algorithms;

import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.log4j.Logger;
/**
 * This class is used to create raw feature counts.
 * 
 * @author C. Albert
 *
 */
public class CreateRaw {
	static Logger logger = Logger.getLogger(CreateRaw.class);
	private Vector<DataBean> analyzedString;

	/**
	 * 
	 * @param incommingString The Vector<DataBean> of a string coming in.
	 * @param code This is the code Object that all the information will be saved to.
	 */
	public CreateRaw(Vector<DataBean> incommingString, CodeExtractor code) {		
		if (incommingString == null)
			throw new NoSuchElementException();

		this.analyzedString = incommingString;
		code.setDoc(summarizeMLBlock());
		code.setGeneratedDoc(analyzedString);
	}

	/**
	 * This makes a string of features and their numbers so weka can analyze it.
	 * 
	 * @return docString This is the HTML sperated into features and ready to be
	 *         analyzed.
	 */
	private String summarizeMLBlock() {

		String doc = new String();

		for (int i = 0; i < analyzedString.size(); i++) {
			OracleRawData statString = (OracleRawData) analyzedString.get(i);
			if (statString.groupInfo()) {
				doc += "group";
			}				
			doc += (statString + "\n");
		}
		return doc;
	}
}
