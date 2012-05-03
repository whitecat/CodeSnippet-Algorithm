package edu.uci.ics.websnippetparser.algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.websnippetparser.report.SimpleReport;

public class OutputMethods {

	public static void printResultStat(String outputDirectory,
			Vector<SimpleReport> resultStat) {

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(
					outputDirectory + "email.txt")));
			out.append(",Text,,,Code,,,,\nrecall,precision,f1,"
					+ "recall,precision,f1,Accuracy,"
					+ "codeCorrect,codeFalse,textCorrect,textFalse\n");
			for (SimpleReport fileStat : resultStat) {
				out.append(fileStat.basicString() + "\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.println("Failed to initalize Out!");
			e.printStackTrace();
		}
	}

	public static void printResultData(String outputDirectory,
			HashMap<String, Vector<DataBean>> resultData) {
//		String doc = ("@RELATION codesnippets\n\n"
//				+ "@ATTRIBUTE indentation\tNUMERIC\n"
//				+ "@ATTRIBUTE comments\tNUMERIC\n@ATTRIBUTE nondic\tNUMERIC\n@ATTRIBUTE keywords\tNUMERIC\n"
//				+ "@ATTRIBUTE separator\tNUMERIC\n@ATTRIBUTE operator\tNUMERIC\n@ATTRIBUTE numofwords\tNUMERIC\n"
//				+ "@ATTRIBUTE stopwords\tNUMERIC\n@ATTRIBUTE class\t{text, code}\n\n@DATA\n");

		for (String fileName : resultData.keySet()) {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(
						outputDirectory + fileName + ".txt")));
				// out.append(doc);
				for (DataBean file : resultData.get(fileName)) {
					out.append(file + "\n");
				}
				out.close();
			} catch (IOException e) {
				System.out.println("Failed to initalize Out!");
				e.printStackTrace();
			}
		}
	}
}
