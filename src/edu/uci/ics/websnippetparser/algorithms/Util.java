package edu.uci.ics.websnippetparser.algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.IB1;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.classifiers.meta.Decorate;
import weka.classifiers.meta.EnsembleSelection;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.meta.RotationForest;
import weka.classifiers.rules.DTNB;
import weka.classifiers.rules.NNge;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.FT;
import weka.classifiers.trees.LADTree;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import edu.uci.ics.websnippetparser.entryPoints.TestingEntryPoint;

public class Util {

	public final static String HEAD_TAG = "HEAD";
	public final static String CODE_TAG = "CODE";
	public final static String PRE_TAG = "PRE";
	public static final String BODY_TAG = "BODY";
	public static final String TEXTTAG = "text";
	public static final String CODETAG = "code";
	public static final String prefixPackageName = "/edu/uci/ics/websnippetparser/resources/";
	private static Logger logger;

	/**
	 * This function changes WF and ML algorithms into XML separated tags
	 * This should be used for algorithms 1-4.
	 * 
	 * @param docString This is the Vector of DataBeans to change into XML format
	 * @return A XML formated string.
	 */
	public static String constructBlock(Vector<DataBean> docString) {
		StringBuilder doc = new StringBuilder();

		boolean switchOver = false;
		doc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		doc.append("\n<codesnippets>");
		String output = "";
		String lastType = "";
		for (int i = 0; i < docString.size(); i++) {
			StatisticalData statString = (StatisticalData) docString.get(i);

			// if The classification is the same as the last classification then
			// augment them together
			if (statString.getClassification().equals(lastType)) {
				if (!(i == docString.size() - 1))
					output += statString.toString();
				else
					switchOver = false;
			}
			// if index is at 0 then print the start of the file
			else if (i == 0 && docString.size() > 1) {
				doc.append("\n<" + statString.getClassification());
				output += statString.toString();
			} else if (i == 0 && docString.size() <= 1) {
				doc.append("\n<" + statString.getClassification());
			}
			// If They are different from each other print the end position and
			// the body and start the new string.
			else {
				doc.append(">\n" + StringEscapeUtils.escapeXml(output) + "\n</"
						+ lastType + ">" + "\n<"
						+ statString.getClassification());
				if (!(i == docString.size() - 1)) {
					output = statString.toString();
					switchOver = true;
				}
			}

			// if we are at the end print what is left over.
			if (i == docString.size() - 1) {
				doc.append(">\n"
						+ (switchOver == true ? "" : StringEscapeUtils
								.escapeXml(output))
						+ StringEscapeUtils.escapeXml(statString.toString())
						+ "\n</" + statString.getClassification() + ">");
			}

			lastType = statString.getClassification();
		}
		doc.append("\n</codesnippets>");
		return doc.toString();
	}

	/**
	 * This function Tag Only algorithm into XML separated tags
	 * This should be used for algorithm 0.
	 * 
	 * @param docString This is the Vector of DataBeans to change into XML format
	 * @return A XML formated string.
	 */
	public static String constructTagBlock(Vector<DataBean> docString) {
		StringBuilder doc = new StringBuilder();
		doc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		doc.append("\n<codesnippets>");
		for (int i = 0; i < docString.size(); i++) {
			DataBean preString = docString.get(i);
			String type;
			if (preString.groupInfo())
				type = "code";
			else
				type = "text";
			preString.setClassification(type);
			doc.append("\n<" + type + ">\n" + preString.toString() + "\n</"
					+ type + ">");
		}
		doc.append("\n</codesnippets>");
		return (doc.toString());
	}

	/**
	 * This is the filter add to here if you want to have more tags filtered.
	 * 
	 * 
	 * @param tagName
	 *            The name of the currently being looked at.
	 * @return True if it matches the deleted tags, false other wise.
	 */
	public static boolean filterFlag(String tagName) {

		if (tagName.equals("SCRIPT")) {
			return true;
		} else if (tagName.equals("NOSCRIPT")) {
			return true;
		} else if (tagName.equals("STYLE")) {
			return true;
		} else if (tagName.equals("ILAYER")) {
			return true;
		} else if (tagName.equals("IFRAME")) {
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @param str
	 *            string that will have white spaces removed
	 * @return Returns a string with no white spaces.
	 */
	public static String whiteSpace(String str) {
		return str.replaceAll("\\s\\s+|\\n\\r|\\n", " ");
	}

	/**
	 * This function removes all conjunctions and anything that is not a valid
	 * variable.
	 * 
	 * @param str
	 *            string that will have white spaces removed
	 * @return String with all valid variable names.
	 */
	public static String removeJunk(String str) {

		// Removes n't off words.
		str = str.replaceAll("n\'t\\s", " ");
		// Removes '* words from the string
		str = str.replaceAll("\'\\w\\s", " ");
		// Removes '** words from the string
		str = str.replaceAll("\'\\w\\w\\s", " ");

		// Removes characters that arn't [a-z][A-Z][0-9]'_' '$'
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetterOrDigit(str.charAt(i))
					|| str.charAt(i) == '$' || str.charAt(i) == '_') {
				sb.append(str.charAt(i));
			} else
				sb.append("  ");
		}
		sb.append("  ");
		// Makes every thing lower case
		str = sb.toString().toLowerCase();
		// Removes all stand alone digits from the String.
		str = str.replaceAll("\\s\\d+\\s", " ");
		// Removes all extra white spaces.
		str = str.replaceAll("\\s\\s+|\\n\\r|\\n", " ");
		// Trims the sentence.
		str = str.trim();

		return str;
	}

	/**
	 * Used to set up the algorithms to work. I.e. Dictionary setup Training.
	 * 
 	 * Training file for weighted features has to have the follow data each on a separate lines.
	 * 
	 * Indentation/# New Lines Ratio
	 * Non Dictionary/# Words Word Ratio
	 * Keyword/# Words Ratio
	 * Separator/# Chars Ratio
	 * Operator/# Chars Ratio
	 * Stop Word/# Words Ratio for code
	 * Number of Words for code
	 * The Constant value for code
	 * Stop Word/# Words Ratio for text
	 * Number of Words for text
	 * The Constant value for text
	 * 
	 * !Make sure that in this file the parameter of the properties file is set correctly
	 * @param alg The algorithm that is being setup
	 * @param training The location of the training file.
	 * @param code
	 * @return The abriviation of the algroithm being used.
	 */
	public static String setUp(int alg, String training,
			DocumentParser code) {

		Properties p = new Properties();
		try {
			p.load(CodeExtractor.class
					.getResourceAsStream("/edu/uci/ics/websnippetparser/resources/codesnippets.properties"));
		} catch (FileNotFoundException e) {
			System.out.println("Location of paramerts file not set correctly");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Location of paramerts file not set correctly");
			e.printStackTrace();
		}
		
		logger = Logger.getLogger(TestingEntryPoint.class);

		Constants.xmlEnd = (p.getProperty("xmlEnd"));
		Constants.txtEnd = (p.getProperty("txtEnd"));
		Constants.separatorType = (p.getProperty("separatorType"));
		Constants.rawSeparatorType = (p.getProperty("rawSeparatorType"));

		dictionarySetup();

		switch (alg) {
		case 3:
			mlSetup(training, code);
			Constants.filePath = (p.getProperty("mLFilePath"));
			Constants.mLAlg = Integer.parseInt(p.getProperty("ml.only"));
			return "ML";
		case 4:
			mlSetup(training, code);
			Constants.filePath = (p.getProperty("mLPreFilePath"));
			Constants.mLAlg = Integer.parseInt(p.getProperty("ml.pre"));
			return "TML";
		case 2:
			Constants.filePath = (p.getProperty("statFilePath"));
			wfSetup(training);
			return "WF";
		case 1:
			Constants.filePath = (p.getProperty("statPreFilePath"));
			wfSetup(training);
			return "TWF";
		case 0:
			Constants.filePath = (p.getProperty("preFilePath"));
			return "Tag";
		case 5:
			return "Training";
		default:
			return "ERROR Occured in Setting up of file";
		}
	}

	/**
	 * This function will set all dictionaries up.
	 */
	public static void dictionarySetup() {

		// Make dictionaries
		try {
			Constants.wordDic = new Dictionary(
					Util.class.getResourceAsStream(prefixPackageName
							+ "dictionary/dictionary"));
			Constants.keywordDic = new Dictionary(
					Util.class.getResourceAsStream(prefixPackageName
							+ "dictionary/keywords"));
			Constants.operatorDic = new Dictionary(
					Util.class.getResourceAsStream(prefixPackageName
							+ "dictionary/operators"));
			Constants.separatorDic = new Dictionary(
					Util.class.getResourceAsStream(prefixPackageName
							+ "dictionary/separators"));
			Constants.stopWordsDic = new Dictionary(
					Util.class.getResourceAsStream(prefixPackageName
							+ "dictionary/stopwords"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function sets up Weighted Features
	 * 
	 * 	 * @param training
	 *            Location of weighted features
	 */
	private static void wfSetup(String training) {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(training));
			while (in.ready()) {
				Constants.indentationRatio = (Double.parseDouble(in.readLine()));
				Constants.comments = (Double.parseDouble(in.readLine()));
				Constants.nonDicRatio = (Double.parseDouble(in.readLine()));
				Constants.keywordRatio = (Double.parseDouble(in.readLine()));
				Constants.separatorRatio = (Double.parseDouble(in.readLine()));
				Constants.operatorRatio = (Double.parseDouble(in.readLine()));
				Constants.stopWordRatioCode = (Double
						.parseDouble(in.readLine()));
				Constants.numberWordsCode = (Double.parseDouble(in.readLine()));
				Constants.constantCode = (Double.parseDouble(in.readLine()));
				Constants.stopWordRatioText = (Double
						.parseDouble(in.readLine()));
				Constants.numberWordsText = (Double.parseDouble(in.readLine()));
				Constants.constantText = (Double.parseDouble(in.readLine()));
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * This sets up Machine Learning
	 * 
	 * @param training Location where the file to train is
	 * @param code The DocumentParser Object where the training will be saved.
	 * 			This object should be removed and a new Class called Setup should be made.
	 * 			
	 */
	private static void mlSetup(String training, DocumentParser code) {

		Classifier test;
		Instances trainFile = null;
		System.out.println(training);
		try {
			if (training == null || training.equals("")) {
				InputStreamReader trainingSetReader = new InputStreamReader(
						Util.class.getResourceAsStream(prefixPackageName
								+ "trainingset/PreTrain.arff"));
				trainFile = new Instances(trainingSetReader);

			} else {
				FileReader trainingSetReader = new FileReader(training);
				trainFile = new Instances(trainingSetReader);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		trainFile.setClassIndex(trainFile.numAttributes() - 1);

		switch (Constants.mLAlg) {
		case 1:
			test = new ClassificationViaRegression();
			break;
		case 2:
			test = new KStar();
			break;
		case 3:
			test = new LWL();
			break;
		case 4:
			test = new LogitBoost();
			break;
		case 5:
			test = new EnsembleSelection();
			break;
		case 6:
			test = new ADTree();
			break;
		case 7:
			test = new RotationForest();
			break;
		case 8:
			test = new DTNB();
			break;
		case 9:
			test = new DecisionStump();
			break;
		case 10:
			test = new Bagging();
			break;
		case 11:
			test = new IB1();
			break;
		case 12:
			test = new IBk();
			break;
		case 13:
			test = new SimpleLogistic();
			break;
		case 14:
			test = new LMT();
			break;
		case 15:
			test = new NNge();
			break;
		case 16:
			test = new FT();
			break;
		case 17:
			test = new SMO();
			break;
		case 18:
			test = new ADTree();
			break;
		case 19:
			test = new LADTree();
			break;
		case 20:
			test = new RandomForest();
			break;
		case 21:
			test = new NaiveBayes();
			break;
		case 22:
			test = new Decorate();
			break;
		case 23:
			test = new AdaBoostM1();
			break;
		case 24:
			test = new Logistic();
			break;
		case 25:
			test = new KStar();
			break;
		case 26:
			test = new NaiveBayes();
			break;

		default:
			test = new RotationForest();
		}

		try {
			test.buildClassifier(trainFile);

		} catch (Exception e) {
			System.out.println("Error occured while making test.");
			System.out.println(e);
			System.exit(0);
		}
		code.setTestFile(test);
		code.setTrainFile(trainFile);
	}

/**
 * This chooses the separator between features in the output
 * @param separator a string that is going to be the separator
 */
	public static void setSparator(String separator) {
		Constants.rawSeparatorType = separator;
		Constants.separatorType = separator;

	}
	
	/**
	 * This outputs a string file to the location location.
	 * @param file String to be written to a file.
	 * @param location Location in which the string will be written.
	 */
	public static void outputFile(String file, String location) {
		PrintWriter out;
		try {

			out = new PrintWriter(new BufferedWriter(new FileWriter(location)));
			out.print(file);
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
