package edu.uci.ics.websnippetparser.algorithms;

import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Vector;

import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instances;

public class MachineLearning {
	private Vector<DataBean> analyzedString;
	private final String separator = ",";

	public MachineLearning(Vector<DataBean> incommingString, DocumentParser code) {
		if (incommingString == null)
			throw new NoSuchElementException();

		analyzedString = incommingString;
		calculateML(summarizeMLBlock(), code);
		code.setGeneratedDoc(analyzedString);
	}

	/**
	 * The HTML page is now analyzed and tags are given.
	 * 
	 * @param testString
	 *            This is the HTML now in format for Weka to analyze it
	 * @param code
	 */
	private void calculateML(String testString, DocumentParser code) {
		if (testString == null)
			throw new NoSuchElementException();

		Instances testset = null;

		try {
			// Read all the instances in the file
			StringReader testFile = new StringReader(testString);
			testset = new Instances(testFile);
		} catch (IOException e1) {
			System.out.println(testString);
			e1.printStackTrace();
		}

		// Make the last attribute be the class
		testset.setClassIndex(testset.numAttributes() - 1);

		// System.out.println(tree.toString());

		Evaluation evaluation = null;

		try {
			evaluation = new Evaluation(code.getTrainFile());
			evaluation.evaluateModel(code.getTestFile(), testset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(evaluation.toSummaryString(true));
		// System.out.println(evaluation.toMatrixString());

		FastVector extracted = evaluation.predictions();

		// System.out.println(docString.size());

		// System.out.println("Predicted String Size: " + extracted.size() +
		// "\nActual String Size: " +docString.size());

		// TODO: look at element to see if it changes

		// Parse to tell whether there is more than one word in a certain
		// string.
		// System.out.println("Analyzed Size "+analyzedString.size());
		// System.out.println("Extracted Size "+extracted.size());
		for (int i = 0; i < extracted.size(); i++) {
			DataBean statString = analyzedString.get(i);
			if ((((NominalPrediction) extracted.elementAt(i)).predicted()) == 0) {
				statString.setClassification(Util.TEXTTAG);
			} else
				statString.setClassification(Util.CODETAG);
		}
		for (DataBean calulate : analyzedString) {
			calulate.calculateIfCode();
		}

		testString = null;
		evaluation = null;
		testset.delete();
		testset = null;
	}

	/**
	 * This makes a string of features and their numbers so weka can analyze it.
	 * 
	 * @return docString This is the HTML sperated into features and ready to be
	 *         analyzed.
	 */
	private String summarizeMLBlock() {

		String doc = ("@RELATION codesnippets\n\n"
				+ "@ATTRIBUTE indentation\tNUMERIC\n"
				+ "@ATTRIBUTE comments\tNUMERIC\n@ATTRIBUTE nondic\tNUMERIC\n@ATTRIBUTE keywords\tNUMERIC\n"
				+ "@ATTRIBUTE separator\tNUMERIC\n@ATTRIBUTE operator\tNUMERIC\n@ATTRIBUTE numofwords\tNUMERIC\n"
				+ "@ATTRIBUTE stopwords\tNUMERIC\n@ATTRIBUTE class\t{text, code}\n\n@DATA\n");

		for (int i = 0; i < analyzedString.size(); i++) {
			StatisticalData statString = (StatisticalData) analyzedString
					.get(i);
			// if (statString.isInsidePre()) {
			// doc += "%,Pre\n";
			// }
			doc += (/*
					 * statString.originalString + "#" +
					 */statString.getIndentationPerLine() + separator
					+ statString.getComments() + separator
					+ statString.getNonDictionaryWords() + separator
					+ statString.getKeywordPerPara() + separator
					+ statString.getSeparators() + separator
					+ statString.getOperators() + separator
					+ statString.getNumOfWord() + separator
					+ statString.getStopWords() + separator
					+ statString.getClassification() + "\n");
		}
		return doc;
	}

	public Vector<DataBean> getAnalyzedString() {
		// TODO Auto-generated method stub
		return analyzedString;
	}
}