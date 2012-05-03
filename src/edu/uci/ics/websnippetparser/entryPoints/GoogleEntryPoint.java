package edu.uci.ics.websnippetparser.entryPoints;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.uci.ics.websnippetparser.algorithms.CodeExtractor;

//
//This is the main entry point to generate test cases.

public class GoogleEntryPoint {

	private static Logger logger;

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(CodeExtractor.class);

		String inputDirectory = "SnippetRepository/Repository/Pre/";
		String outputDirectory = "GeneratedFiles/Pre/Training/";

		File dir = new File(inputDirectory);
		String[] children = dir.list();
		Properties p = new Properties();
		p.load(CodeExtractor.class
				.getResourceAsStream("/edu/uci/ics/websnippetparser/resources/codesnippets.properties"));
		String algList = (p.getProperty("usage"));
		String[] alg = algList.split(" ");
		for (String i : alg) {
			CodeExtractor test = new CodeExtractor(Integer.parseInt(i));
			for (String child : children) {
				if (!child.equals(".svn")) {
					test.setDoc(null);
					logger.debug(child);
					test.parseFile(inputDirectory + child);
					test.createXML();
					String file = test.getDoc();
					String location = outputDirectory + child;
					outputFile(file, location);
				}
			}
		}
	}

	private static void outputFile(String file, String Location)
			throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				Location)));
		out.print(file);
		out.close();
	}
}
