package edu.uci.ics.websnippetparser.entryPoints;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.htmlparser.util.ParserException;
import org.xml.sax.SAXException;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.email.Email;
import edu.uci.ics.websnippetparser.email.MachineLearningParser;
import edu.uci.ics.websnippetparser.email.XMLOpener;
import edu.uci.ics.websnippetparser.report.SimpleReport;

public class XMLEmailEntryPoint {

	public static void main(String[] args) throws ParserException,
			XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		// This is the location of the database results.
		String dataDirectory = "GeneratedFiles/eMails/ML/databaseResult.txt";
		// This is the location of where the stat results go.
		String statDirectroy = "Files/MLResults.txt";
		// This Value has to be changed if you want a different training set.
		String training = "EMailDatabase/Group_0_Size_10.txt";
		// Change this value to where the XML repo location is
		String xml = "EmailRepository/besc.xml";
		// This value shouldn't change
		// String header = ",Text,,,Code,,,,\nrecall,precision,f1,"
		// + "recall,precision,f1,Accuracy,"
		// + "codeCorrect,codeFalse,textCorrect,textFalse\n";

		new XMLEmailEntryPoint(dataDirectory, statDirectroy, training, xml);
	}

	/**
	 * 
	 * @param dataDirectory
	 *            This is the location of the database results.
	 * @param statDirectroy
	 *            This is the location of where the stat results go.
	 * @param training
	 *            Change This Value has to be changed if you want a different
	 *            training set.
	 * @param xml
	 *            Change This value to where the XML repo location is.
	 * @throws ParserException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XMLEmailEntryPoint(String dataDirectory, String statDirectroy,
			String training, String xml) throws ParserException,
			XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		Email result = null;
		MachineLearningParser mL = null;

		// Util.dictionarySetup();
		PrintWriter stat = printSetup(statDirectroy);
		PrintWriter data = printSetup(dataDirectory);
		mL = new MachineLearningParser(training);
		XMLOpener xmlData = new XMLOpener(xml);
		int i = 1;

		while (xmlData.hasNext()) {

			result = xmlData.next();

			mL.machineLearning(result.getContent(), result.getOracle(), result
					.getId());
			System.out.println(result.getId());

			result = null;
			if (i++ % 100 == 0) {
				printResultStat(stat, mL.getResultStat());
				printResultData(data, mL.getResultData());
				mL.garbageCollect();
				System.gc();
			}
		}
		stat.close();
		data.close();
	}

	
	/**
	 * This sets up the printing Function
	 * 
	 * @param outputLocation
	 *            Where the file is printed
	 * @return The file object to be written
	 */
	private PrintWriter printSetup(String outputLocation) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(
					outputLocation)));
		} catch (IOException e) {
			System.out.println("Failed to initalize Out!");
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * This loops through the Stat results to make functions ready to print
	 * 
	 * @param out
	 *            The file object to be written
	 * @param resultStat
	 *            The resulting stat values of each e-mail
	 */
	private void printResultStat(PrintWriter out,
			Vector<SimpleReport> resultStat) {
		for (SimpleReport fileStat : resultStat) {
			out.append(fileStat.basicString() + "\n");
		}
		out.flush();
	}

	/**
	 * This function prints the Data results
	 * 
	 * @param out
	 *            The file object to be written
	 * @param resultData
	 *            The resulting data values of each e-mail
	 */
	// 
	private void printResultData(PrintWriter out,
			HashMap<String, Vector<DataBean>> resultData) {
		for (String fileName : resultData.keySet()) {
			out.append("THIS IS file " + fileName);
			for (DataBean file : resultData.get(fileName)) {
				out.append(file + "\n");
			}
		}
		out.flush();
	}
}
