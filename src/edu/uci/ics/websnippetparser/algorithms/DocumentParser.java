package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Instances;

public interface DocumentParser {

	public void setTestFile(Classifier test);
	public void setTrainFile(Instances trainFile);
	public Instances getTrainFile();
	public Classifier getTestFile();
	public void setGeneratedDoc(Vector<DataBean> analyzedString);
	
}
