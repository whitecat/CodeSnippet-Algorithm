package edu.uci.ics.websnippetparser.entryPoints;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;

//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

import edu.uci.ics.websnippetparser.algorithms.CodeExtractor;
import edu.uci.ics.websnippetparser.algorithms.Constants;
import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.report.SimpleReport;

public class TestingEntryPoint {
	//private static Logger logger;
	//Specify location of Webpages
	String trainingDirectory = "Repository/FileMaps/";
	String repositoryDirectory = "Repository/Google/";
	String groupDirectory = "HandGeneratedFiles/GroupedTraining/";
	String simpleDirectory = "HandGeneratedFiles/SimpleTraining/";
	String oracleDirectory = "HandGeneratedFiles/Training/";
	String outputDirectory = "GeneratedFiles/Google/";
	
	//CHANGE HERE FOR DIFERENT RATES OF TRAINING TESTING
	int trainingSets = 3;
	

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
//		PropertyConfigurator
//				.configure("C:/Documents and Settings/rgallard/workspace/WebSnippetParserOnlyBranch/log4j.properties");
//		logger = Logger.getLogger(CodeExtractor.class);

		Properties p = new Properties();
		p
				.load(CodeExtractor.class
						.getResourceAsStream("/edu/uci/ics/websnippetparser/resources/codesnippets.properties"));

		String algList = (p.getProperty("usage"));
		String[] alg = algList.split(" ");

		for (String i : alg) {
			int algNum = Integer.parseInt(i);
			// int algNum = 4;
			switch (algNum) {
			case 4:
			case 1:
				new TestingEntryPoint(algNum, true);
				break;
			case 2:
			case 3:
				new TestingEntryPoint(algNum, false);
				break;
			default:
				new TestingEntryPoint(algNum);
				break;
			}
		}
	}

	public TestingEntryPoint(int alg, boolean type) throws IOException {
		PrintStream parserPrintData = new PrintStream(new FileOutputStream(outputDirectory +alg+ ".out"));
		parserPrintData.println(",,,Text,,,Code,,,,");
		parserPrintData.println("Train,Test,,recall,precision,f1,recall,precision,f1,Accuracy,CC,CF,TC,TF");
		
		for (int i = 0; i < trainingSets; i++) {

			
			BufferedReader in = new BufferedReader(new FileReader(
					trainingDirectory + i));
			CodeExtractor test;

			long start = System.currentTimeMillis();
			if (type) {
				test = new CodeExtractor(alg, groupDirectory + in.readLine());
			} else
				test = new CodeExtractor(alg, simpleDirectory + in.readLine());

			long trainTime = System.currentTimeMillis() - start;

			while (in.ready()) {

				outputTest(test, trainTime, in.readLine(), parserPrintData);
			}
			in.close();
			if (test.getTrainFile() != null){
			test.getTrainFile().delete();
			}
			test.setTrainFile(null);
			test.setTestFile(null);
			test = null;
			Constants.collectGarbage();
		}
		parserPrintData.close();
	}

	private TestingEntryPoint(int alg) throws IOException {
		File dir = new File(repositoryDirectory);
		String[] children = dir.list();
		PrintStream parserPrintData = new PrintStream(new FileOutputStream(outputDirectory +alg+ ".out"));
		parserPrintData.println(",,,Text,,,Code,,,,");
		parserPrintData.println("Train,Test,,recall,precision,f1,recall,precision,f1,Accuracy");
		
		long start = System.currentTimeMillis();
		CodeExtractor test = new CodeExtractor(alg);
		long trainTime = System.currentTimeMillis() - start;

		for (String child : children) {
			if (!child.equals(".svn")) {

				outputTest(test, trainTime, child, parserPrintData);

			}
		}
		test = null;
		Constants.collectGarbage();
		parserPrintData.close();
	}

	private void outputTest(CodeExtractor test, long trainTime, String fileName, PrintStream groupFile)
			throws IOException {
		StringBuilder file = new StringBuilder();
		
		long start = System.currentTimeMillis();

		test.parseFile(repositoryDirectory + fileName);

		long testTime = System.currentTimeMillis() - start;

		String location = outputDirectory + test.getAlgType() + "/" + fileName
				+ ".txt";

		SimpleReport simple = new SimpleReport(test.getGeneratedDoc(), fileName, oracleDirectory);
		file.append("Time to Train: " + trainTime + "\n");
		file.append("Time to Test: " + testTime + "\n");
		file.append(fileName + ", " + simple.toString());

		
		groupFile.println(trainTime+","+testTime+","+fileName + ", " + simple.basicString());
		groupFile.flush();
		
		if (test.getGeneratedDoc().size() != 0) {
//			logger.debug(fileName);
			outputFile(file.toString(), location);
		}
		test.setDoc(null);
		test.setGeneratedDoc(new Vector<DataBean>());
		file = null;
		simple = null;
	}

	private void outputFile(String file, String Location) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				Location)));
		out.print(file);
		out.close();
	}
}
