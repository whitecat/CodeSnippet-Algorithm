package edu.uci.ics.websnippetparser.algorithms;

/**
 * This class has data that is used across classes
 * @author C. Albert
 *
 */
public class Constants {

	
	
	public static int mLAlg;
	public static String xmlEnd;
	public static String txtEnd;
	public static String trainFilePath;
	public static String separatorType;
	public static String rawSeparatorType;
	public static Dictionary wordDic;
	public static Dictionary keywordDic;
	public static Dictionary operatorDic;
	public static Dictionary separatorDic;
	public static Dictionary stopWordsDic;
	public static double indentationRatio;
	public static double comments;
	public static double nonDicRatio;
	public static double keywordRatio;
	public static double separatorRatio;
	public static double operatorRatio;
	public static double stopWordRatioCode;
	public static double numberWordsCode;
	public static double constantCode;
	public static double stopWordRatioText;
	public static double numberWordsText;
	public static double constantText;
	public static String filePath;
	
	/**
	 * This sets everything to null and pretty much cleans everything up.
	 */
	public static void collectGarbage(){
		xmlEnd = null;
		txtEnd = null;
		trainFilePath = null;
		separatorDic = null;
		separatorType = null;
		rawSeparatorType  = null;
		wordDic = null;
		keywordDic = null;
		operatorDic = null;
		stopWordsDic = null;
		filePath = null;
		System.gc();
		
	}
	
	
}
