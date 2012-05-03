package edu.uci.ics.websnippetparser.entryPoints;

import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.OutputMethods;
import edu.uci.ics.websnippetparser.email.MachineLearningParser;
import edu.uci.ics.websnippetparser.email.RawDataParser;
import edu.uci.ics.websnippetparser.report.SimpleReport;

public class EmailEntryPoint {

	private String inputDirectory = "SnippetRepository/";
	String simpleDirectory = "HandGeneratedFiles/SimpleTraining/";
	String oracleDirectory = "HandGeneratedFiles/Training/";
	String separator = "^";

	public static void main(String[] args) {

		long start;
		long elapsed;

		start = System.currentTimeMillis();

		new EmailEntryPoint(3);

		elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed);

	}

	public EmailEntryPoint(int alg) {
		switch (alg) {
		case 2:
			System.out.println("Not yet implemented");
			break;
		case 3:
			machineLearning(inputDirectory);
			break;
		case 5:
			rawData(inputDirectory);
			break;
		}

	}

	private void machineLearning(String inputDirectory) {
		MachineLearningParser mL = null;
		HashMap<String, Vector<DataBean>> resultData = null;
		Vector<SimpleReport> resultStat = null;
		String outputDirectory = "GeneratedFiles/eMails/ML/";
		String statDirectroy = "Files/MLResults.txt";
		String training = "0";

		mL = new MachineLearningParser(training);
		mL.directoryExtractor(inputDirectory, oracleDirectory);
		resultData = mL.getResultData();
		resultStat = mL.getResultStat();
		
		OutputMethods.printResultData(outputDirectory, resultData);

		OutputMethods.printResultStat(statDirectroy, resultStat);
		
		resultData = null;
		resultStat = null;
	}

	private void rawData(String inputDirectory) {
		HashMap<String, Vector<DataBean>> resultData = null;
		String outputDirectory = "GeneratedFiles/eMails/training/";
		RawDataParser rawData = new RawDataParser(inputDirectory);

		resultData = rawData.getFileList();

		OutputMethods.printResultData(outputDirectory, resultData);
		resultData = null;
	}
}
