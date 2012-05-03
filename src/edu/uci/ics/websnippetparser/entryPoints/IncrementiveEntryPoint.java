package edu.uci.ics.websnippetparser.entryPoints;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.htmlparser.util.ParserException;
import org.xml.sax.SAXException;

import edu.uci.ics.websnippetparser.algorithms.CodeExtractor;
import edu.uci.ics.websnippetparser.algorithms.Constants;
import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.email.Email;
import edu.uci.ics.websnippetparser.email.MachineLearningParser;
import edu.uci.ics.websnippetparser.email.XMLOpener;
import edu.uci.ics.websnippetparser.report.ReportAverager;
import edu.uci.ics.websnippetparser.report.SimpleReport;

public class IncrementiveEntryPoint {
	// private static Logger logger;

	String simpleDirectory = "HandGeneratedFiles/SimpleTraining/";
	String oracleDirectory = "HandGeneratedFiles/Training/";
	String outputDirectory = "Files/Results/";

	// CHANGE HERE FOR DIFERENT RATES OF TRAINING TESTING
	//TODO: Explain these better
	int trainingSets = 2;
	int trainingSize = 40;
	int maxGroupSize = 400;
	
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// PropertyConfigurator
		// .configure("C:/Documents and Settings/rgallard/workspace/WebSnippetParserOnlyBranch/log4j.properties");
		// logger = Logger.getLogger(CodeExtractor.class);

		new IncrementiveEntryPoint();
	}

	public IncrementiveEntryPoint() {

		String repositoryDirectoryForum = "ForumRepository/";
		String repositoryDirectoryEmail = "EmailRepository/";
		String repositoryDirectoryWebsite = "WebRepository/";
		String forumDirectory = "StackOverFlow/";
		String emailDirectory = "EMailDatabase/";
		String webDirectory = "WebPages/";

		try {
			tester(4, repositoryDirectoryWebsite, webDirectory, 'w');
			tester(3, repositoryDirectoryForum, forumDirectory, 'f');
			tester(3, repositoryDirectoryEmail, emailDirectory, 'e');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tester(int alg, String repositoryLocation, String fileLocation,
			char type) throws FileNotFoundException {
		String filename;

		PrintStream parserPrintData = new PrintStream(new FileOutputStream(
				outputDirectory + fileLocation + "results.out"));
		parserPrintData.println(",,,Text,,,Code,,,,");
		parserPrintData
				.println("Train,Test,,recall,precision,f1,recall,precision,f1,Accuracy,CC,CF,TC,TF");

		int i = 0;
		int j = 50;
		while (j <= maxGroupSize) {
			while (i < trainingSets) {

				filename = "Group_" + i + "_Size_" + j + ".txt";
//				 filename = "Group_1_Size_10.txt";
//				 j=100;
				 
				ReportAverager average = new ReportAverager(filename);

				switch (type) {
				case 'w':
					webPages(repositoryLocation, fileLocation, parserPrintData,
							average, filename, alg);
					break;
				case 'f':
					forum(repositoryLocation, fileLocation, parserPrintData,
							average, filename, alg);
					break;
				case 'e':
					email(repositoryLocation, fileLocation, parserPrintData,
							average, filename, alg);
					break;
				}
				parserPrintData.println(average);
				parserPrintData.flush();
				i++;
			}
			parserPrintData.println("\n");
			i = 0;
			j += trainingSize;
		}
		parserPrintData.close();
	}

	private void forum(String repositoryLocation, String fileLocation,
			PrintStream parserPrintData, ReportAverager average,
			String fileName, int alg) {

		long start;
		long trainTime;
		long testTime;
		
		CodeExtractor forumTest;
//
//		System.out.println(fileLocation);
//		System.out.println(fileName);
//		System.out.println(repositoryLocation);
		
		start = System.currentTimeMillis();
		forumTest = new CodeExtractor(alg, simpleDirectory + fileLocation + fileName);
		trainTime = System.currentTimeMillis() - start;
		
		
		start = System.currentTimeMillis();
		forumTest.parseFile(repositoryLocation + "QueryResultsAugust2010Body.csv");
		testTime = System.currentTimeMillis() - start;

		//System.out.println(forumTest.getGeneratedDoc().size());
		
		SimpleReport simple = new SimpleReport(forumTest.getGeneratedDoc(),
				"Oracle.csv", repositoryLocation);

		average.addAverage(simple, testTime);
		
		

	}

	private void email(String repositoryLocation, String fileLocation,
			PrintStream parserPrintData, ReportAverager average,
			String fileName, int alg) {
		
		Email result = null;
		MachineLearningParser mL = null;
		XMLOpener xmlData = null;
		long start = 0; 
		long testTime = 0;
		mL = new MachineLearningParser(fileLocation + fileName);
		try {
			xmlData = new XMLOpener(repositoryLocation+"besc.xml");
		int i = 1;
		start = System.currentTimeMillis();
		while (xmlData.hasNext()) {

			result = xmlData.next();

			mL.machineLearning(result.getContent(), result.getOracle(), result
					.getId());

			result = null;
			if (i++ % 100 == 0) {
				testTime = System.currentTimeMillis() - start;
				start = System.currentTimeMillis();
				for (SimpleReport fileStat : mL.getResultStat()) {
					average.addAverage(fileStat, testTime/100);
				}
				mL.garbageCollect();
				System.gc();
			}
//			if (i % 800 == 0){
//				return;
//			}
		}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void webPages(String repositoryLocation, String fileLocation,
			PrintStream parserPrintData, ReportAverager average,
			String filename, int alg) {

		CodeExtractor test;
		long start = System.currentTimeMillis();
		test = new CodeExtractor(alg, simpleDirectory + fileLocation + filename);
		long trainTime = System.currentTimeMillis() - start;

		File dir = new File(repositoryLocation);
		String[] children = dir.list();
		for (String fileName : children) {
			if (!fileName.equals(".svn")) {
				start = System.currentTimeMillis();
				test.parseFile(repositoryLocation + fileName);
				long testTime = System.currentTimeMillis() - start;

//				System.out.println(fileName);
				SimpleReport simple = new SimpleReport(test.getGeneratedDoc(),
						fileLocation + fileName, oracleDirectory);

				average.addAverage(simple, testTime);

				test.setGeneratedDoc(new Vector<DataBean>());
				simple = null;

			}
		}
		if (test.getTrainFile() != null) {
			test.getTrainFile().delete();
		}
		test.setTrainFile(null);
		test.setTestFile(null);
		test = null;
		Constants.collectGarbage();
	}
}
