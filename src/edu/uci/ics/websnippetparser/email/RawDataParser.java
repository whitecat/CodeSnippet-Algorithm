package edu.uci.ics.websnippetparser.email;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Instances;
import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.DocumentParser;
import edu.uci.ics.websnippetparser.algorithms.Util;

public class RawDataParser implements DocumentParser {
	private HashMap<String, Vector<DataBean>> fileList = null;
	private String inputDirectory = null;

	public RawDataParser(String inputDirectory) {
		this.inputDirectory = inputDirectory;
		fileList = new HashMap<String, Vector<DataBean>>();
		rawData();

	}

	private void rawData() {
		int algorithm = 5;
		// Setup Algorithm.
		Util.setUp(algorithm, "", this);
		EMailOpener opener = new EMailOpener(algorithm);

		for (String eMail : new File(inputDirectory).list()) {
			if (!eMail.equals(".svn")) {
				fileList.put(eMail,
						opener.eMailExtractor(inputDirectory + eMail));
			}
		}
		opener = null;
	}

	public HashMap<String, Vector<DataBean>> getFileList() {
		return fileList;
	}

	@Override
	public Classifier getTestFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instances getTrainFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGeneratedDoc(Vector<DataBean> analyzedString) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTestFile(Classifier test) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTrainFile(Instances trainFile) {
		// TODO Auto-generated method stub

	}

}
