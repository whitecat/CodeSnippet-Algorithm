package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;

public class OracleRawData extends DataBean {

	private int indentation = 0;
	private double comments = 0;
	private int nonDictionaryWord = 0;
	private int keyword = 0;
	private int separatorCount = 0;
	private int operatorCount = 0;
	private int stopWord;
	private int newLineCount = 1;
	@SuppressWarnings("unused")
	private int lookAtPoint = 15;
	
	

	public OracleRawData(String statString, int endTag, int startTag,
			boolean insidePre, int indexOf, Vector<DataBean> docString) {
		super(statString, endTag, startTag, insidePre, indexOf, docString);
		classification = Util.TEXTTAG;

		calculateWords();
		calculateIndentation();
		calculateComments();
		calculateSeparators();
		calculateOperators();

	}

	//

	private void calculateIndentation() {
		boolean stop = false;

		for (int i = 0; i < length; i++) {
			char token = originalString.charAt(i);
			if (token == ' ' && !stop) {
				indentation++;
			} else if (token == '\t' && !stop) {
				indentation = indentation + 8;
			} else if (token == '\n') {
				stop = false;
				newLineCount++;
			} else
				stop = true;
		}
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
				nonDictionaryWord++;
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
	}


	public int getIndentation() {
		return indentation;

	}

	public double getComments() {
		return comments;

	}

	public int getNonDictionaryWord() {
		return nonDictionaryWord;

	}

	public int getKeyword() {
		return keyword;

	}

	public int getStopWords() {
		return stopWord;
	}

	public int getSeparators() {
		return separatorCount;

	}

	public int getOperators() {
		return operatorCount;

	}

	public int getNewLineCount() {
		return newLineCount;
	}

	// Prints out raw data in perfect format.
	public String toString() {
		String seperator = Constants.rawSeparatorType;
		
		return 	Util.removeJunk(getOriginal())
				+ seperator + getIndentation()
				+ seperator + getComments()
				+ seperator + getNonDictionaryWord()
				+ seperator + getKeyword()
				+ seperator + getSeparators()
				+ seperator + getOperators()
				+ seperator + getNumOfWord()
				+ seperator + getStopWords()
				+ seperator + getNewLineCount()
				+ seperator + getLength()
				+ seperator + getClassification();
	}

}
