package edu.uci.ics.websnippetparser.tests;

public class StatDataBean {
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
	private String type;
	private String group;
	

	public double getIndentations() {
		return indentations;
	}

	public double getOperator() {
		return operator;
	}

	public double getNonDictionary() {
		return nonDictionary;
	}

	public double getComments() {
		return comments;
	}

	public double getKeyword() {
		return keyword;
	}

	public double getSeperator() {
		return seperator;
	}

	public double getStopword() {
		return stopword;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public double getNumofwords() {
		return numofwords;
	}

	public double getNewLineCount() {
		return newLineCount;
	}

	public double getLength() {
		return length;
	}

	public String getType() {
		return type;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public StatDataBean(String group, int indentations, double comments,
			int nonDictionary, int keyword, int seperator,
			int operator, int numofwords, double stopword,
			int newLineCount, int length, String type) {
		this.setGroup(group);
		this.indentations = indentations;
		this.comments = comments;
		this.nonDictionary = nonDictionary;
		this.keyword = keyword;
		this.seperator = seperator;
		this.operator = operator;
		this.stopword = stopword;
		this.numofwords = numofwords;
		this.newLineCount = newLineCount;
		this.length = length;
		this.type = type;
	}

	public String toString() {
		String returnString = group +  delimiter+ (int)indentations + delimiter + comments + delimiter
				+ (int)nonDictionary + delimiter +(int) keyword + delimiter + (int)seperator
				+ delimiter +(int)operator + delimiter +(int) numofwords + delimiter
				+ (int)stopword + delimiter + (int)newLineCount + delimiter +(int) length + delimiter + type;
		return returnString;

	}
	
	public String ratioString(){
		return (newLineCount >0 ? (indentations/newLineCount): indentations) + delimiter + comments + delimiter
		+ (numofwords >0 ? (nonDictionary/numofwords) : nonDictionary) + delimiter + (numofwords >0 ? (keyword/numofwords) : keyword) + delimiter + (seperator/length)
		+ delimiter + (operator/length) + delimiter + numofwords + delimiter
		+ (numofwords >0 ? (stopword/numofwords) : stopword) + delimiter + type;
	}


}
