package edu.uci.ics.websnippetparser.training;

import java.util.ArrayList;

public class TestGroup {
	private ArrayList<URLInfo> testSet;
	private ArrayList<URLInfo> trainSet;

	public TestGroup(ArrayList<URLInfo> trainSet, ArrayList<URLInfo> list) {
		this.trainSet = trainSet;
		testSet = new ArrayList<URLInfo>();
		for (URLInfo temp : list) {
			if (!trainSet.contains(temp)) {
				testSet.add(temp);
			}
		}
	}

	public ArrayList<URLInfo> getTestSet() {
		return testSet;
	}

	public ArrayList<URLInfo> getTrainSet() {
		return trainSet;
	}
}