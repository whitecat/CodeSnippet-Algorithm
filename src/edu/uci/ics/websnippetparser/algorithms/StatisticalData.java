package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;

public class StatisticalData extends DataBean {

	private double indentationPerLine = 0;
	private double comments = 0;
	private double nonDictionaryWords = 0;
	private double keyword = 0;
	private double separatorCount = 0;
	private double operatorCount = 0;
	private double stopWord;
	private double stdCodeScore;
	private double stdTextScore;
	@SuppressWarnings("unused")
	private int lookAtPoint = 15;
	private double codeScore;
	private double textScore;

	public StatisticalData(String statString, int endTag, int startTag,
			boolean insidePre, int indexOf, Vector<DataBean> docString) {
		super(statString, endTag, startTag, insidePre, indexOf, docString);
		classification = Util.TEXTTAG;

		calculateWords();
		calculateIndentation();
		calculateComments();
		calculateSeparators();
		calculateOperators();
		calculateTextScore();
		calculateCodeScore();

		// System.out.println("This is a string:" + statString + " " +
		// classification + ":END :" + stopWord);

	}

	//
	private void calculateCodeScore() {

		codeScore = (indentationPerLine * Constants.indentationRatio + comments
				* Constants.comments + nonDictionaryWords
				* Constants.nonDicRatio + Constants.separatorRatio
				* separatorCount + Constants.operatorRatio * operatorCount
				+ keyword * Constants.keywordRatio + stopWord
				* Constants.stopWordRatioCode + numOfWord
				* Constants.numberWordsCode + Constants.constantCode);

	}

	private void calculateTextScore() {

		textScore = (stopWord * Constants.stopWordRatioText + numOfWord
				* Constants.numberWordsText + Constants.constantText);

	}

	private void calculateIndentation() {
		boolean stop = false;
		double numNewLine = 1;
		double indentation = 0;

		for (int i = 0; i < length; i++) {
			char token = originalString.charAt(i);
			if (token == ' ' && !stop) {
				indentation++;
			} else if (token == '\t' && !stop) {
				indentation = indentation + 8;
			} else if (token == '\n') {
				stop = false;
				numNewLine++;
			} else
				stop = true;
		}

		indentationPerLine = (double) (indentation / numNewLine);

	}

	private void calculateComments() {

		String temp = originalString;
		temp = temp.replaceAll("://", " ");

		for (int i = 0; i + 1 < temp.length(); i++) {
			String token = temp.substring(i, i + 2);
			// System.out.println (token);

			if (token.equals("//")) {
				comments++;
			}
			if (token.equals("/*")) {
				comments += .5;
			}
			if (token.equals("*/")) {
				comments += .5;
			}
		}
	}

	private void calculateWords() {

		// if(RecursivePre.timer == 1){
		// System.out.println("This is a string:"+ wordOnly + ":END");
		// }

		String wordOnly = Util.removeJunk(originalString);

		String[] word = wordOnly.split("\\s");

		// for(int i = 0; i<word.length; i++){
		//
		// }

		for (int i = 0; i < word.length; i++) {
			// System.out.println("I looped " + i + " times.");
			String temp = word[i].trim();

			if (!Constants.wordDic.containsWord(temp) && !temp.equals("")) {
				// if(RecursivePre.timer == lookAtPoint)
				// System.out.println("THIS ISNT A WORD :" + temp +
				// ":word ends here");
				nonDictionaryWords++;
			}
			if (Constants.keywordDic.containsWord(temp)) {
				// if(RecursivePre.timer == lookAtPoint)
				// System.out.println("THIS IS A KEYWORD :" + temp +
				// ":word ends here");
				keyword++;
			}
			if (Constants.stopWordsDic.containsWord(temp)) {
				// if(RecursivePre.timer == lookAtPoint)
				// System.out.println("THIS A STOPWORD :" + temp +
				// ":word ends here");
				stopWord++;
			}

		}

		// System.out.println(wordOnly);
		// If the sentence contains comments then it is not text. or Reduces the
		// probability by 4 times.
		// if (comments != 0)
		// stopWords = (stopWords/8);

		if (numOfWord > 0) {
			nonDictionaryWords = nonDictionaryWords / numOfWord;
			keyword = keyword / numOfWord;
			stopWord = stopWord / numOfWord;
		} else {
			nonDictionaryWords = 0;
			keyword = 0;
			stopWord = 0;
		}
	}

	private void calculateSeparators() {
		String oneChar;
		for (int i = 0; i < length; i++) {
			oneChar = originalString.substring(i, i + 1);
			if (Constants.separatorDic.containsWord(oneChar)
					&& !oneChar.equals("")) {
				// if(RecursivePre.timer == lookAtPoint)
				// System.out.println("This is a Seperator:"+ word[i] + ":End");
				separatorCount++;
			}
		}
		if (length > 0) {
			separatorCount = separatorCount / length;
		} else {
			separatorCount = 0;
		}

	}

	private void calculateOperators() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char token = originalString.charAt(i);
			if (token == '=') {
				sb.append(token);
			} else if (token == '>') {
				sb.append(token);
			} else if (token == '<') {
				sb.append(token);
			} else if (token == '?') {
				sb.append(token);
			} else if (token == ':') {
				sb.append(token);
			} else if (token == '&') {
				sb.append(token);
			} else if (token == '%') {
				sb.append(token);
			} else if (token == '!') {
				sb.append(token);
			} else if (token == '~') {
				sb.append(token);
			} else if (token == '+') {
				sb.append(token);
			} else if (token == '*') {
				sb.append(token);
			} else if (token == '/') {
				sb.append(token);
			} else if (token == '|') {
				sb.append(token);
			} else if (token == '^') {
				sb.append(token);
			} else
				sb.append(" ");
			;
		}

		// System.out.println("Edited String: "+sb.toString());

		String[] word = sb.toString().split("\\s");
		for (int i = 0; i < word.length; i++) {

			if (Constants.operatorDic.containsWord(word[i])
					&& !word[i].equals("")) {
				// if(RecursivePre.timer == lookAtPoint)
				// System.out.println("This is Operator:"+ word[i] + ":End");
				operatorCount++;
			}
		}
		if (length > 0) {
			operatorCount = operatorCount / length;
		} else {
			operatorCount = 0;
		}
	}

	public double calcStdCodeScore(double std, double mean) {

		stdCodeScore = ((codeScore - mean) / std);
		return stdCodeScore;

	}

	public double calcStdTextScore(double std, double mean) {
		stdTextScore = ((textScore - mean) / std);
		return stdTextScore;

	}

	public void calcClassification(Vector<DataBean> docString, int index) {

		if (numOfWord < 2) {
			lessThanNWords(docString, index, 2);
		} else {
			if (stdCodeScore > stdTextScore)
				setClassification(Util.CODETAG);
			else
				setClassification(Util.TEXTTAG);
		}

		// if ((temp.split(" ")).length<2)
		// System.out.println("Lenght of String :" +temp +": Equals :" +
		// (temp.split(" ")).length);

	}

	// You need the current document
	// the index number of the item being looked at
	// the number of words less than
	public void lessThanNWords(Vector<DataBean> docString, int index, int N) {
		if (index > 1) {
			setClassification(docString.get(index - 1).getClassification());
		} else
			setClassification(Util.TEXTTAG);
	}

	public void setStdWord(double stdWord) {
		this.stdTextScore = stdWord;
	}

	public void setStdScore(double stdScore) {
		this.stdCodeScore = stdScore;
	}

	public double getStdWord() {
		return stdTextScore;
	}

	public double getStdScore() {
		return stdCodeScore;
	}

	public double getIndentationPerLine() {
		return indentationPerLine;

	}

	public double getComments() {
		return comments;

	}

	public double getNonDictionaryWords() {
		return nonDictionaryWords;

	}

	public double getKeywordPerPara() {
		return keyword;

	}

	public double getStopWords() {
		return stopWord;
	}

	public double getSeparators() {
		return separatorCount;

	}

	public double getOperators() {
		return operatorCount;

	}

	public double getCodeScore() {
		return codeScore;

	}

	public double getTextScore() {
		return textScore;
	}

	public String toString() {
		String seperator = Constants.separatorType;

		return getIndentationPerLine() + seperator + getComments() + seperator
				+ getNonDictionaryWords() + seperator + getKeywordPerPara()
				+ seperator + getSeparators() + seperator + getOperators()
				+ seperator + getNumOfWord() + seperator + getStopWords()
				+ seperator + getClassification();

	}

	public String toPrintString() {
		String seperator = Constants.separatorType;

		return Util.removeJunk(getOriginal()) + seperator
				+ getIndentationPerLine() + seperator + getComments()
				+ seperator + getNonDictionaryWords() + seperator
				+ getKeywordPerPara() + seperator + getSeparators() + seperator
				+ getOperators() + seperator + getNumOfWord() + seperator
				+ getStopWords() + seperator + getClassification();

	}
}
