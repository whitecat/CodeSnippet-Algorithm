package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * 
 *
 * 
 *         To run this class you need to first call the constructor passing what
 *         Algorithm you want to use. <br>
 * 
 *         5 = Generates data for labeling training data. Not for testing purposes.<br>
 *         4 = MachineLearning and Pre <br>
 *         3 = MachineLearning Only <br>
 *         2 = Statistical Only <br>
 *         1 = StatisticalPre <br>
 *         0 = Pre<br>
 * 
 *         If you are using machine learning you have to Pass a training in the
 *         constructor also I.E.<br>
 * 
 *         <pre>
 *         CodeExtractor myCode = new CodeExtractor(3, locationOfTraining); or
 *         CodeExtractor myCode = new CodeExtractor(0);
 * </pre>
 * 
 *         After this you call the parseFile function.<br>
 * 
 *         <pre>
 * myCode.parseFile(locationOfFileToTest);
 * </pre>
 * 
 *         Then get the output of the file. This gets the objects of data each
 *         being code or text.<br>
 *  *         The doc is returned as a Vector<DataBean>. Use getClassification for
 *         each segments classification and getOriginal it get the original
 *         string.<br>
 *         
 *         <pre>
 * myCode.getGeneratedDoc();
 * </pre>
 * 
 *         This will give you a String in XML format that has all code and text
 *         broken up into tags.<br>
 * 
 *         <pre>
 * myCode.createXML();
 * </pre>
 * 
 *         
 * 
 *         Lastly if you are trying to create raw feature files (this is for
 *         creating an oracle.) 
 *         <pre>
 *         CodeExtractor myCode = new CodeExtractor(5);
 *         myCode.parseFile(LocationOfFile); myCode.getDoc();
 *         </pre>
 * 
 *  @author C. Albert Thompson
 */

public class CodeExtractor implements DocumentParser {
	private String doc;
	private boolean status;
	private int alg;
	private String algType;
	private Vector<DataBean> generatedDoc;
	private Classifier test;
	private Instances trainFile;

	// private static Logger logger;

	public static void main(String[] args) {
		// PropertyConfigurator.configure("log4j.properties");
		// logger = Logger.getLogger(CodeExtractor.class);

		if (args.length != 1) {
			System.out.println("Usage: java -jar CodeExtractor.jar URL");
			System.out
					.println("Example: \n\t java -jar CodeExtractor.jar http://java.sun.com/docs/books/tutorial/java/nutsandbolts/for.html");
			return;
		}

		
		CodeExtractor myCode = new CodeExtractor(5);
		// parse the webpage
		myCode.parseFile(args[0]);

		
		if (myCode.getDoc() != null) {
			Util.outputFile(myCode.getDoc(), "Files/test.txt");
		}
	}

	/**
	 * This class sets everything up for running the code, it should only use the algorithms below.<br>
	 * 5 = Generates data for labeling training data. Not for testing purposes<br>
	 * 0 = Pre<br>
	 * 
	 * @param alg
	 *            This is the algorithm that will be used to evaluate the code
	 * 
	 */
	public CodeExtractor(int alg) {
		this.alg = alg;
		algType = (Util.setUp(alg, "", this));

	}

	/**
	 * This class sets everything up for running the code, but must take a
	 * training set.<br>
	 * 4 = MachineLearning and Pre<br>
	 * 3 = MachineLearning Only<br>
	 * 2 = Statistical Only<br>
	 * 1 = StatisticalPre
	 * 
	 * @param alg
	 *            algorithm that will be used to evaluate the code
	 * @param training
	 *            The location of the training file.
	 */
	public CodeExtractor(int alg, String training) {
		this.alg = alg;
		algType = (Util.setUp(alg, training, this));

	}

	/**
	 * This is the method that actually tells the algorithm to go.
	 * 
	 * @param pathURL
	 *            The path where the parser should work.
	 * 
	 */
	public boolean parseFile(String pathURL) {
		NodeVisitor visitor;
		Parser parser;
		visitor = new SimpleSegment(this, alg);
		try {
			parser = new Parser(pathURL);
			parser.visitAllNodesWith(visitor);
			return true;
		} catch (ParserException e) {
			System.out.println(e);
			return false;
		}

	}

	/**
	 * Makes the object into XML format so it can be saved to a file
	 */
	public void createXML() {
		if (generatedDoc != null) {
			if (generatedDoc.get(0) instanceof StatisticalData) {
				setDoc(Util.constructBlock(generatedDoc));
			} else {
				setDoc(Util.constructTagBlock(generatedDoc));
			}
		}
	}

	public void setDoc(String doc) {

		this.doc = doc;
	}

	/**
	 * To use this make sure you
	 * 
	 * @return XML format of your file.
	 */
	public String getDoc() {
		return doc;
	}

	public boolean getStatus() {
		return status;
	}

	public String getAlgType() {
		return algType;
	}

	/**
	 * Here we get the final output. There are many different ways the final out
	 * put comes. Then we get all of the strings that have been stored in the
	 * final output.
	 * 
	 * @param generatedSegment
	 *            This is the final product of the algorithm
	 */
	public void setGeneratedDoc(Vector<DataBean> generatedSegment) {
		generatedDoc = new Vector<DataBean>();
		for (DataBean segmentVector : generatedSegment) {
			// Expand all of the group segments.
			// Sometimes there might only be one but still expand it and put it
			// into final output.
			for (DataBean segment : segmentVector.getGroupedSegments()) {
				StatisticalData temp = new StatisticalData(segment.toString(),
						segment.getEndIndex(), segment.getStartIndex(),
						segment.insidePre, segment.indexOf, segment.docString);
				temp.setClassification(segment.getClassification());
				generatedDoc.add(temp);
			}
		}
	}

	public Vector<DataBean> getGeneratedDoc() {
		return generatedDoc;
	}

	public Classifier getTestFile() {
		return test;
	}

	public Instances getTrainFile() {
		return trainFile;
	}

	public void setTestFile(Classifier test) {
		this.test = test;
	}

	public void setTrainFile(Instances trainFile) {
		this.trainFile = trainFile;
	}

}
