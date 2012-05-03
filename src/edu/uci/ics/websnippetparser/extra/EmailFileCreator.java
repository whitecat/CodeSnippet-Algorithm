package edu.uci.ics.websnippetparser.extra;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.htmlparser.util.ParserException;
import org.xml.sax.SAXException;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.Util;
import edu.uci.ics.websnippetparser.email.Email;
import edu.uci.ics.websnippetparser.email.XMLOpener;

public class EmailFileCreator {

	public static void main(String[] args) throws ParserException,
			XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		new EmailFileCreator();

	}

	public EmailFileCreator() throws ParserException, XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		Email result = null;
		String outputDirectroy = "HandGeneratedFiles/Training/EMailDatabase/";

		String xml = "EmailRepository/besc.xml";

		Util.dictionarySetup();
		Util.setSparator(",");
		XMLOpener xmlData = new XMLOpener(xml);
		while (xmlData.hasNext()) {
			result = xmlData.next();

			int i = Integer.parseInt(result.getId());

			String fileName = ((i < 10) ? ("000" + i) : 
											((i < 100) ? ("00" + i)	: 
													(i < 1000) ? ("0" + i) : i))
					+ ".txt";

			System.out.println(fileName);

			printEmail(outputDirectroy + fileName, result.getOracle());

		}
	}

	private void printEmail(String location, Vector<DataBean> eMail) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(location)));
			for (DataBean line : eMail) {
				out.append(line.toString() + "\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.println("Failed to initalize Out!");
			e.printStackTrace();
		}
	}

}
