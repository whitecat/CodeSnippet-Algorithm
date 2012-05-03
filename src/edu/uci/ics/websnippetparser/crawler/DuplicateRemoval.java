package edu.uci.ics.websnippetparser.crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DuplicateRemoval {
	String inputDirectory = "Repository/FileMaps/";

	public static void main(String[] args) {
		new DuplicateRemoval(args[0]);
	}

	public DuplicateRemoval(String fileName) {
		LinkedHashMap<String, String> urlList = new LinkedHashMap<String, String>();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(inputDirectory + fileName));
		} catch (FileNotFoundException e) {
			in = null;
			System.out.println(e);
		}
		String outFileName = "DuplicatesRemoved";
		if (in != null) {
			AllTextToOne.labelDuplicates(urlList, in);
		}

		print(urlList, outFileName);
	}

	private void print(LinkedHashMap<String, String> urlList, String outFileName) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(inputDirectory + outFileName
					+ ".Changed"));
			Iterator<String> iter = urlList.keySet().iterator();
			while (iter.hasNext()) {
				String s = iter.next();
				out.println(s + urlList.get(s));
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
