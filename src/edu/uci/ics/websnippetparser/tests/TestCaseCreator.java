package edu.uci.ics.websnippetparser.tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import edu.uci.ics.websnippetparser.training.SimpleSegmentCombiner;

public class TestCaseCreator {

	private String directory = "Files/";
	private String handDirectory = "HandGeneratedFiles/Training/EMailDatabase/";
	private String simpleOracle = "HandGeneratedFiles/SimpleTraining/";
	private String inputFile = "eMails";
	private double listPercent = 4;
	
	
	
	public static void main(String[] args) {

		new TestCaseCreator();

	}

	public TestCaseCreator() {

		ArrayList<URLInfo> list = createList();

		ArrayList<TestGroup> randomList = getRandomList(list, listPercent);
		
		printList(randomList);
	}

	/**
	 * 
	 * @param randomList List of files that need to be tested
	 * 
	 * This is used to write the list of files to be tested onto the disk
	 * 
	 */
	private void printList(ArrayList<TestGroup> randomList) {
		int i = 0;
//		System.out.println("List Size: "+randomList.size());
		for (TestGroup test : randomList) {
			String simpleLocation = simpleOracle + i;
			ArrayList<URLInfo> trainSet = test.getTrainSet();
//			System.out.println(trainSet.size());
			//System.out.println("=====RandomList Iteration=====");
			SimpleSegmentCombiner simple = new SimpleSegmentCombiner(simpleLocation);
			for (URLInfo website : trainSet) {
				simple.addFile(handDirectory + website);
			}
			simple.close();
			
			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter(directory + i));
			} catch (IOException e) {
				System.out.println("cannot create file.");
				e.printStackTrace();
			}

			out.println(i);
			out.print(compileList(test.getTestSet()));
			i++;
			out.close();
			
		}
	}
/**
 * 
 * @param randomList List of files to be tested
 * @return the list of files as a StringBuffer
 */
	private StringBuffer compileList(ArrayList<URLInfo> randomList) {
		StringBuffer output = new StringBuffer();

		for (URLInfo iter : randomList) {
			output.append(iter.getFileName() + "\n");
		}
		return output;
	}

	
/**
 * 
 * @param list Takes a list of files and randomly picks 
 * @param percent The percent of files to randomly pick as test subjects.
 * @return the list of random test subjects are returned.
 * 
 * This picks out test subjects rather than train subjects.
 * 
 * 
 */
	private ArrayList<TestGroup> getRandomList(ArrayList<URLInfo> list, double percent) {
		ArrayList<URLInfo> listCopy = new ArrayList<URLInfo>();
		ArrayList<TestGroup> randList = new ArrayList<TestGroup>();
		ArrayList<URLInfo> testSet = new ArrayList<URLInfo>();
		listCopy.addAll(list);
		Random ran = new Random(Calendar.getInstance().getTimeInMillis());
		int listSize = list.size();
		
		int percentOfTotal = (int) (list.size() * ((percent / 100)));

		for (int i = 0; i < listSize; i++) {
			if (i != 0 && i % percentOfTotal == 0
			/* && ((i + (percentOfTotal)) <= listSize) */) {
				randList.add(new TestGroup(testSet, listCopy));
				testSet = new ArrayList<URLInfo>();
			}

			int randomLocation = ran.nextInt(list.size());
			testSet.add(list.get(randomLocation));
			list.remove(randomLocation);
		}
		// randList.add(new TestGroup(percentList, listCopy));
		return randList;
	}
/**
 * 
 * @return The list of files are returned as an Array
 * 
 * This will read from the directory specified and make them into a URL
 * 
 */
	private ArrayList<URLInfo> createList() {
		String[] line = null;
		ArrayList<URLInfo> list = new ArrayList<URLInfo>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(directory
					+ inputFile));

			while (in.ready()) {
				line = in.readLine().split("\\s");
				list.add(new URLInfo(line[0], line[1]));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	class TestGroup {
		private ArrayList<URLInfo> testSet;
		private ArrayList<URLInfo> trainSet;

		public TestGroup(ArrayList<URLInfo> testSet, ArrayList<URLInfo> list) {
			this.testSet = testSet;
			trainSet = new ArrayList<URLInfo>();
			for (URLInfo temp : list) {
				if (!testSet.contains(temp)) {
					trainSet.add(temp);
				}
			}
		}

		public ArrayList<URLInfo> getTestSet() {
			return testSet;
		}

		public ArrayList<URLInfo> getTrainSet() {
			return trainSet;
		}
	}

	class URLInfo {

		private String fileName;
		private String url;

		public URLInfo(String fileName, String url) {
			this.setFileName(fileName);
			this.setUrl(url);
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

		public String toString() {
			return fileName;
		}
	}
}