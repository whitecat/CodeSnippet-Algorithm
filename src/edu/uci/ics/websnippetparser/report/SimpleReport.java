package edu.uci.ics.websnippetparser.report;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.Util;

public class SimpleReport {

	private int codeCorrect = 0;
	private int codeFalse = 0;
	private int textCorrect = 0;
	private int textFalse = 0;
	private double recallCode = 0;
	private double recallText = 0;
	private double percisionCode = 0;
	private double percisionText = 0;
	private double f1Code = 0;
	private double f1Text = 0;
	private double accuracy = 0;
	private String fileName = null;

	// public static void main(String[] args) {
	// SimpleReport report = new SimpleReport();
	// report.openFile("PRE0005.arff");
	//
	// }

	/**
	 * @param algorithm
	 *            This is the file that is to be tested
	 * @param fileName
	 *            This is the location of the gold(oracle) of the file to be
	 *            tested
	 * 
	 *            This constructor opens up the file containing Gold version
	 *            then it does all the calculations necessary to computer
	 *            Recall, Precision, F1, and Accuracy for Both Code and Text
	 * 
	 * 
	 */
	public SimpleReport(Vector<DataBean> algorithm, String fileName, String directory) {
		this.fileName = fileName;
		Vector<DataBean> gold = openFile(fileName, directory);
		if (gold.size() != algorithm.size()) {
			System.out.println("incorrect size");
			System.out.println(gold.size());
			System.out.println(algorithm.size());
		}
		calculate(algorithm, gold);
	}

	/**
	 * @param algorithm
	 *            This is the file that is to be tested
	 * @param gold
	 *            This is the gold(oracle) of the file to be tested
	 * 
	 *            No file needs to be opened with this constructor This
	 *            constructor does all the calculations necessary to computer
	 *            Recall, Precision, F1, and Accuracy for Both Code and Text
	 * 
	 */
	public SimpleReport(Vector<DataBean> algorithm, Vector<DataBean> gold) {

		if (gold.size() != algorithm.size()) {
			System.out.println("incorrect size");
			System.out.println(gold.size());
			System.out.println(algorithm.size());
		}

		calculate(algorithm, gold);
	}

	private void calculate(Vector<DataBean> algorithm, Vector<DataBean> gold) {
		for (int i = 0; i < gold.size(); i++) {

			String goldClass = gold.get(i).getClassification();
			String algorithmClass = algorithm.get(i).getClassification();
			int length = algorithm.get(i).getLength();

			// System.out.println("Gold class : \"" + goldClass
			// + "\" Alg class : " + algorithmClass + "\" :Line Number : " + i);
		
			if (goldClass.equals("text") && algorithmClass.equals("text"))
				textCorrect += length;
			else if (goldClass.equals("code") && algorithmClass.equals("text"))
				codeFalse += length;
			else if (goldClass.equals("text") && algorithmClass.equals("code"))
				textFalse += length;
			else if (goldClass.equals("code") && algorithmClass.equals("code"))
				codeCorrect += length;
			else{
				System.err.println("FAIL to find code or text in report line " + i);
			}
		}

		percisionCode = calcPercision(codeCorrect, (codeCorrect + codeFalse));
		percisionText = calcPercision(textCorrect, (textCorrect + textFalse));
		recallCode = calcRecall(codeCorrect, (codeCorrect + textFalse));
		recallText = calcRecall(textCorrect, (textCorrect + codeFalse));
		f1Code = calcF1(percisionCode, recallCode);
		f1Text = calcF1(percisionText, recallText);
		accuracy = calcAccuracy(textCorrect, textFalse, codeFalse, codeCorrect);
	}

	private double calcRecall(double sameLines, double lengthOfGoldStandardLines) {
		double recall = 0;
		if (sameLines == 0 && lengthOfGoldStandardLines == 0)
			recall = 1;
		else
			recall = (float) sameLines / lengthOfGoldStandardLines;
		return recall;
	}

	private double calcPercision(double sameLines,
			double lengthOfToolGeneratedLines) {
		double precision = 0;
		if (sameLines == 0 && lengthOfToolGeneratedLines == 0)
			precision = 1;
		else
			precision = (float) sameLines / lengthOfToolGeneratedLines;
		return precision;
	}

	private double calcF1(double precision, double recall) {
		double f1 = 0;
		// System.out.println("Percision = " + precision + " Recall = " +
		// recall);
		if (precision == 0 && recall == 0)
			f1 = 0;
		else
			f1 = 2 * (precision * recall) / (precision + recall);
		// System.out.println("f1 = " + f1);
		return f1;
	}

	private  double calcAccuracy(double a, double b, double c, double d) {
		double accuracy = ((a + d) / (a + b + c + d));
		return accuracy;
	}

	private Vector<DataBean> openFile(String fileName, String directory) {
		Vector<DataBean> gold = new Vector<DataBean>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(directory + fileName));

			try {
				while (in.ready()) {
					String line = (in.readLine());
					String[] lineValues = line.split(",");
					if (!lineValues[0].equals("%")) {
						DataBean temp = new DataBean(line, 0, 0, lineValues[11]
								.equals(Util.TEXTTAG) ? false : true, gold
								.size(), gold);
						temp.setClassification(lineValues[11]);
						gold.add(temp);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return gold;
		} catch (FileNotFoundException e) {
			System.out.println("ERROR");
			System.out.println(e);
		}
		return gold;
	}

	public int getCodeCorrect() {
		return codeCorrect;
	}

	public int getCodeFalse() {
		return codeFalse;
	}

	public int getTextCorrect() {
		return textCorrect;
	}

	public int getTextFalse() {
		return textFalse;
	}

	public double getRecallCode() {
		return recallCode;
	}

	public double getRecallText() {
		return recallText;
	}

	public double getPercisionCode() {
		return percisionCode;
	}

	public double getPercisionText() {
		return percisionText;
	}

	public double getF1Code() {
		return f1Code;
	}

	public double getF1Text() {
		return f1Text;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public String toString() {
		String returnString = (",Text,,,Code,,,,");
		returnString += "\n" + fileName
				+ ",recall,precision,f1,recall,precision,f1,Accuracy\n";
		returnString += recallText + "," + percisionText + "," + f1Text + ","
				+ recallCode + "," + percisionCode + "," + f1Code + ","
				+ accuracy;
		return returnString;
	}

	public String basicString() {
		String returnString = fileName + "," + recallText + "," + percisionText
				+ "," + f1Text + "," + recallCode + "," + percisionCode + ","
				+ f1Code + "," + accuracy + "," + codeCorrect + "," + codeFalse
				+ "," + textCorrect + "," + textFalse;
		return returnString;
	}
}
