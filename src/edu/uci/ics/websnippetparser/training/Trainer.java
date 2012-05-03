package edu.uci.ics.websnippetparser.training;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Trainer {

	private int trainingNumber;
	private int numOfGroups;

	public static void main(String[] args) {
		String oracleLocation = "HandGeneratedFiles/SimpleTraining/EMailDatabase/";
		String trainingLocation = "HandGeneratedFiles/Training/EMailDatabase/";
		int trainingNumber = 10;
		int numOfGroups = 2;

		Trainer email = new Trainer(trainingNumber, numOfGroups);
		ArrayList<TestGroup> randomList = email.randomizeList(email
				.createList(trainingLocation));
		email.printList(randomList, trainingNumber, oracleLocation,
				trainingLocation);
	}

	/**
	 * This is the constructor
	 * 
	 * @param trainingNumber
	 *            The number of files you want to have in training set.
	 * @param numOfGroups
	 *            The number of training sets with the trainingNumber size.
	 */
	public Trainer(int trainingNumber, int numOfGroups) {
		this.trainingNumber = trainingNumber;
		this.numOfGroups = numOfGroups;
	}

	public ArrayList<URLInfo> createList(String trainingLocation) {

		ArrayList<URLInfo> list = new ArrayList<URLInfo>();
		File dir = new File(trainingLocation);
		String[] children = dir.list();

		for (String fileName : children) {
			list.add(new URLInfo(fileName, trainingLocation));
		}

		return list;
	}

	/**
	 * 
	 * @param list
	 *            This is the group of things that will be tested in list from
	 * @return An array of all groups of tests are returned.
	 * 
	 *         This takes a list Selects out groups of 100 lines When the Number
	 *         of groups adds up to be the total needed It creates a TestGroup
	 *         Object. This continues till all of all of the groups are used up.
	 * 
	 */
	public ArrayList<TestGroup> randomizeList(ArrayList<URLInfo> list) {
		ArrayList<URLInfo> listCopy = new ArrayList<URLInfo>();
		ArrayList<TestGroup> randList = new ArrayList<TestGroup>();
		ArrayList<URLInfo> trainSet = new ArrayList<URLInfo>();
		listCopy.addAll(list);
		Random ran = new Random(Calendar.getInstance().getTimeInMillis());
		int sampleSize = (trainingNumber);
		//
		for (int i = 0; i <= (sampleSize) * numOfGroups; i++) {
			if (i != 0 && i % sampleSize == 0) {
				randList.add(new TestGroup(trainSet, listCopy));
				trainSet = new ArrayList<URLInfo>();
			}

			int randomLocation = ran.nextInt(list.size());
			trainSet.add(list.get(randomLocation));
		}
		return randList;
	}

	public void printList(ArrayList<TestGroup> randomList, int trainingSize,
			String oracleLocation, String trainingLocation) {

		int i = 0;
		for (TestGroup test : randomList) {
			ArrayList<URLInfo> trainSet = test.getTrainSet();
			System.out.println(trainSet.size());
			// System.out.println("=====RandomList Iteration=====");
			SimpleSegmentCombiner simple = new SimpleSegmentCombiner(oracleLocation + "Group_"
					+ i + "_Size_" + trainingSize + ".txt");
			for (URLInfo fileName : trainSet) {
				// System.out.println(fileName);
				if (!fileName.equals(".svn")) {
					simple.addFile(trainingLocation + fileName);
				}
			}
			simple.close();
			i++;
		}

	}
	


	public StringBuffer compileList(ArrayList<URLInfo> randomList) {
		StringBuffer output = new StringBuffer();

		for (URLInfo iter : randomList) {
			output.append(iter.getFileName() + "\n");
		}
		return output;
	}
}
