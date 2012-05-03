package edu.uci.ics.websnippetparser.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class AllTextToOne {

	private String regex = "(https?)://(.*?)/";


	public static void main(String[] args) {
		// minor change
		String inputDirectory = "F:\\Documents and Settings\\caseyt\\Research\\compliation";
		String outputDirectory = "F:\\Documents and Settings\\caseyt\\Research\\Output";
		String topDirectory = "F:\\Documents and Settings\\caseyt\\Research\\top100";
		File dir = new File(inputDirectory);
		String[] children = dir.list();
		// StringBuffer wholeFile = new StringBuffer();
		LinkedHashMap<String, String> urlList = new LinkedHashMap<String, String>();
		ArrayList<URLCount> urlCount = new ArrayList<URLCount>();
		ArrayList<URLPercentage> queryPrecent = new ArrayList<URLPercentage>();
		int test = 0;
		int n = 100;
		BufferedReader in = null;
		String outFileName = null;

		for (int i = 0; i < 10; i++) {

			queryPrecent.add(new URLPercentage((i + 1) * 100));
		}

		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				System.out.println(filename);

				try {
					in = new BufferedReader(new FileReader(inputDirectory
							+ "\\" + filename));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (in != null) {
					switch (test) {
					case 0:
						// This will put the fileName in the First column and
						labelDuplicates(urlList, in);
						outFileName = "FileNameFirst";
						break;
					case 1:
						urlCount.add(sumURL(filename, in));
						outFileName = "SeperateWithCount";
						break;
					case 2:
						sumQuery(queryPrecent, in);
						outFileName = "TotalWithPercentages";
						break;
					case 3:
						extractDuplicates(urlList, in);
						outFileName = "URLFirst";
						break;
					case 4:
						extractFirstN(urlList, in, n);
						outFileName = "First" + n;
						break;
					}
				}
			}
		}
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(outputDirectory + "\\"
					+ outFileName + ".txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (test) {
		case 0:
		case 3:
		case 4:
			Iterator<String> iter = urlList.keySet().iterator();
			while (iter.hasNext()) {
				String s = iter.next();
				out.println(s + urlList.get(s));

			}
			break;
		case 1:
			for (URLCount i : urlCount) {
				out.println(i);
			}

			break;
		case 2:
			HashMap<String, Integer> lastList = null;
			int lastPageCount = 0;
			for (URLPercentage i : queryPrecent) {
				if (lastList != null && lastPageCount != 0) {
					i.addList(lastList, lastPageCount);
				}
				out.println(i);
				lastList = i.getList();
				lastPageCount = i.getPageCount();
			}
			break;
		}

		out.close();
	}

	/**
	 *  Extracts the frist N pages from the files N is passed in.
	 *  
	 * @param urlList
	 * @param in
	 * @param n
	 */
	private static void extractFirstN(HashMap<String, String> urlList,
			BufferedReader in, int n) {

		int i = 0;
		try {
			while (in.ready()) {
				if (i++ == n) {
					return;
				}
				String[] results = in.readLine().split(",\\s");
				urlList.put(results[0] + " ", results[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  Extracts Domain from the URL
	 *  
	 */
	private static String extractDomain(String url) {

		char[] stringArray = url.toCharArray();
		StringBuffer mainUrl = new StringBuffer();

		int counter = 0;
		for (int i = 0; i < stringArray.length; i++) {

			if (counter != 3) {
				if (stringArray[i] == '/') {
					counter++;
				}
				mainUrl.append(stringArray[i]);
			} else
				break;

		}
		return mainUrl.toString();
	}

	/**
	 *  This will label all duplicates in the hash But keeping the File name in the first column.
	 *
	 * @param urlList
	 * @param in
	 */
	public static void labelDuplicates(HashMap<String, String> urlList,
			BufferedReader in) {

		try {
			while (in.ready()) {
				String[] results = in.readLine().split("\\s");
				if (urlList.containsValue(results[1])) {
					urlList.put(results[0] + "{ ", results[1] + "{ Duplicate");
				} else {
					urlList.put(results[0] + "{ ", results[1]);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This Sums up the first 100 of each web page couting the domain of each
	 *  URL and how many time sit occurs
	 * 
	 * @param filename
	 * @param in
	 * @return
	 */
	private static URLCount sumURL(String filename, BufferedReader in) {

		URLCount currentFile = new URLCount(filename);
		try {
			while (in.ready()) {
				String[] results = in.readLine().split("\\s");
				currentFile.addPage(extractDomain(results[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentFile;
	}

	/**
	 *  This will provide a total sum of all 16 queries incrementing by 100
	 * 
	 * @param queryCount
	 * @param in
	 */
	private static void sumQuery(ArrayList<URLPercentage> queryCount,
			BufferedReader in) {

		int i = 0;
		try {
			while (in.ready()) {
				String[] results = in.readLine().split("\\s");
				queryCount.get(i++ / 100).addQuery(extractDomain(results[1]));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This provides duplicate information putting the Domain in the first
	 * column and the file in the second column.
	 * 
	 * @param address
	 * @param in
	 * 
	 */
	private static void extractDuplicates(HashMap<String, String> address,
			BufferedReader in) {

		int j = 0;
		try {
			while (in.ready()) {

				String[] results = in.readLine().split("\\s");

				if (address.containsKey(results[1])) {

					String temp = address.get(results[1]);
					if (temp.length() < 20)
						address.put(results[1], "{ Duplicate { " + results[0]
								+ temp);
					else {
						address.put(results[1], temp + "{ " + results[0]);
					}

				} else {
					address.put(results[1], "{ " + results[0]);
				}

				j++;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
