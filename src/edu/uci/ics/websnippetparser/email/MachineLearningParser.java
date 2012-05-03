package edu.uci.ics.websnippetparser.email;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Instances;
import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.DocumentParser;
import edu.uci.ics.websnippetparser.algorithms.MachineLearning;
import edu.uci.ics.websnippetparser.algorithms.Util;
import edu.uci.ics.websnippetparser.report.SimpleReport;

public class MachineLearningParser implements DocumentParser {

	private Classifier test = null;
	private Instances trainFile = null;
	private Vector<SimpleReport> resultStat = null;
	private HashMap<String, Vector<DataBean>> resultData = null;
	private String training;
	private Vector<DataBean> result;

	public MachineLearningParser(String training) {

		this.training = training;
		resultData = new HashMap<String, Vector<DataBean>>();
		resultStat = new Vector<SimpleReport>();
		mlSetup();
	}

	/**
	 * This Function gets the machine learning algorithm ready
	 * It run machine learning on all the files
	 * It runs statistical tests on all of the files.
	 * It also puts information into resultStat and resultData.
	 */
	public void directoryExtractor(String inputDirectory, String oracleDirectory) {
	
		int algroithm = 3;
		Vector<DataBean> file = null;
		EMailOpener opener = new EMailOpener(algroithm);


		for (String eMail : new File(inputDirectory).list()) {
			if (!eMail.equals(".svn")) {
				file = opener.eMailExtractor(inputDirectory+eMail);
				file = new MachineLearning(file, this).getAnalyzedString();
												
				resultData.put(eMail, file);
				resultStat.add(new SimpleReport(file, eMail + ".csv", oracleDirectory));
			}
		}
		
		opener = null;
	}
	
	public void machineLearning(Vector<DataBean> eMail, Vector<DataBean> oracle, String id){
		
		eMail = new MachineLearning(eMail, this).getAnalyzedString();
										
		resultData.put(id, eMail);
		resultStat.add(new SimpleReport(eMail, oracle));
		
		eMail = null;
		
	}

	public void machineLearning(String eMailLocation){
		Vector<DataBean> eMail = new EMailOpener(3).eMailExtractor(eMailLocation);
		
		eMail = new MachineLearning(eMail, this).getAnalyzedString();
		result = eMail;
	}
	
	private void mlSetup() {
			//Setup Algorithm.
			Util.setUp(3, "HandGeneratedFiles/SimpleTraining/" + training, this);
	}

	public Classifier getTestFile() {
		return test;
	}

	public Instances getTrainFile() {
		return trainFile;
	}
	public Vector<DataBean> getResult() {
		return result;
	}

	public void setTestFile(Classifier test) {
		this.test = test;
	}

	public void setTrainFile(Instances trainFile) {
		this.trainFile = trainFile;
	}

	public void setGeneratedDoc(Vector<DataBean> analyzedString) {
	}

	public Vector<SimpleReport> getResultStat() {
		return resultStat;
	}

	public HashMap<String, Vector<DataBean>> getResultData() {
		return resultData;
	}
	public void garbageCollect(){
		resultData = new HashMap<String, Vector<DataBean>>();
		resultStat = new Vector<SimpleReport>();
	}
}
