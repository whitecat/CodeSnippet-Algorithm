package edu.uci.ics.websnippetparser.tests;

import java.util.ArrayList;

public class StatDataCombine {
	private double indentations;
	private double operator;
	private double nonDictionary;
	private double comments;
	private double keyword;
	private double seperator;
	private double stopword;
	private String delimiter = ",";
	private double numofwords;
	private double newLineCount;
	private double length;
	private double textLength;
	private double codeLength;
	private String type;
	ArrayList<StatDataBean> stat;

	// This class should group everything together.
	// Also it will compute everything it needs to make a valid test case.

	public StatDataCombine(StatDataBean inital) {
		stat = new ArrayList<StatDataBean>();
		stat.add(inital);
	}

	// Add something to the test case.

	public void add(StatDataBean addition) {
		stat.add(addition);
	}

	public String getType() {

		for (StatDataBean statData : stat) {
			if (statData.getType().equals("text")) {
				textLength += statData.getNumofwords();
			} else
				codeLength += statData.getNumofwords();

		}

		if (textLength > codeLength) {
			type = "text";
		} else
			type = "code";

		return type;
	}

	public String toString() {
		for (StatDataBean statData : stat) {
			indentations += statData.getIndentations();
			comments += statData.getComments();
			nonDictionary += statData.getNonDictionary();
			keyword += statData.getKeyword();
			seperator += statData.getSeperator();
			operator += statData.getOperator();
			stopword += statData.getStopword();
			numofwords += statData.getNumofwords();
			newLineCount += statData.getNewLineCount();
			length += statData.getLength();

			if (statData.getType().equals("text")) {
				textLength += statData.getNumofwords();
			} else
				codeLength += statData.getNumofwords();

		}

		if (textLength > codeLength) {
			type = "text";
		} else
			type = "code";

		String returnString = (newLineCount >0 ? (indentations/newLineCount): indentations) + delimiter + comments + delimiter
		+ (numofwords >0 ? (nonDictionary/numofwords) : nonDictionary) + delimiter + (numofwords >0 ? (keyword/numofwords) : keyword) + delimiter + (seperator/length)
		+ delimiter + (operator/length) + delimiter + numofwords + delimiter
		+ (numofwords >0 ? (stopword/numofwords) : stopword) + delimiter + type;
		return returnString;

	}
}
