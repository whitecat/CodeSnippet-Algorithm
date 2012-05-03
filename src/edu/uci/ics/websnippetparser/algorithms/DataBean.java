package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;
/**
 * This class is what every 
 * @author C. Albert
 *
 */
public class DataBean {

	protected int length;
	protected int wordCount;
	protected String originalString;
	protected int startIndex;
	protected int endIndex;
	protected String classification;
	protected boolean insidePre;
	protected int indexOf;
	protected Vector<DataBean> docString;
	protected int numOfWord;
	protected Vector<DataBean> groupedSegment;

	public void addGroupedSegments(Vector<DataBean> groupedSegments) {
		this.groupedSegment = groupedSegments;
	}

	public Vector<DataBean> getGroupedSegments() {
		return groupedSegment;

	}

	public DataBean(String statString, int endTag, int startTag,
			boolean insidePre, int indexOf, Vector<DataBean> docString) {
		this.indexOf = indexOf;
		this.docString = docString;
		originalString = statString;
		startIndex = startTag;
		endIndex = endTag;
		length = originalString.length();
		calulateNumOfWord();
		this.insidePre = insidePre;

	}

	public void calulateNumOfWord() {

		String wordOnly = Util.removeJunk(originalString);

		String[] word = wordOnly.split("\\s");
		if (word.length == 1 && word[0].equals(""))
			numOfWord = 0;
		else
			numOfWord = word.length;

	}

	public String toString() {
		return originalString;
	}

	public String getOriginal() {
		return originalString;
	}

	public int getLength() {
		return length;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public String getClassification() {
		return classification;
	}

	public int getNumOfWord() {
		return numOfWord;
	}

	public void setClassification(String classification) {
		if (groupedSegment != null) {
			for (DataBean i : groupedSegment) {
				i.setClassification(classification);
			}
		}
		this.classification = classification;

	}

	public boolean isInsidePre() {
		return insidePre;
	}

	public boolean groupInfo() {

		int i = indexOf;
		while (i + 1 < docString.size()	&& docString.get(i++ + 1).getNumOfWord() < 1) {
		}
		boolean next = docString.get(i).isInsidePre();
		i = indexOf;
		while (i >= 1 && docString.get(i-- - 1).getNumOfWord() < 1) {
		}
		boolean last = docString.get(i).isInsidePre();


		if (numOfWord < 1 || (numOfWord <= 1 && indexOf >= 1 && last != insidePre && next != insidePre)) {
			insidePre = last;
		}

		return insidePre;
	}

	public void setInsidePre(Boolean insidePre) {
		this.insidePre = insidePre;
		calulateNumOfWord();
	}

	public void calculateIfCode() {
		
		int i = indexOf;
		//Gets previous non empty Character String
		while (i >= 1 && docString.get(i-- - 1).getNumOfWord() < 1) {
		}
		String last = docString.get(i).getClassification();

		
		if (numOfWord < 1 || (numOfWord <= 1 && indexOf >= 1)) {
			setClassification(last);
//			System.out.println(getOriginal());
		}
	}
}
