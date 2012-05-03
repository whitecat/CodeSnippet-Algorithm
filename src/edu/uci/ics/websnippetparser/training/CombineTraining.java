package edu.uci.ics.websnippetparser.training;

import java.util.ArrayList;

public class CombineTraining {

	public CombineTraining(ArrayList<TestGroup> randomEmailList,
			ArrayList<TestGroup> randomForumList,
			ArrayList<TestGroup> randomWebPageList, String oracleLocation,
			int numOfGroups) {
		printCombinedList(randomEmailList, randomForumList, randomWebPageList,
				oracleLocation, numOfGroups);
	}

	private void printCombinedList(ArrayList<TestGroup> randomEmailList,
			ArrayList<TestGroup> randomForumList,
			ArrayList<TestGroup> randomWebPageList, String oracleLocation,
			int numOfGroups) {

		for (int i = 0; i < numOfGroups; i++) {
			ArrayList<URLInfo> trainSetEMail = randomEmailList.get(i)
					.getTrainSet();
			ArrayList<URLInfo> trainSetForum = randomForumList.get(i)
					.getTrainSet();
			ArrayList<URLInfo> trainSetWebPage = randomWebPageList.get(i)
					.getTrainSet();

			// System.out.println("=====RandomList Iteration=====");
			SimpleSegmentCombiner simple = new SimpleSegmentCombiner(oracleLocation + "Group_"
					+ i + ".txt");
			printData(simple, trainSetEMail);
			printData(simple, trainSetForum);
			printData(simple, trainSetWebPage);

			simple.close();
		}
	}

	private void printData(SimpleSegmentCombiner simple, ArrayList<URLInfo> trainSet) {
		for (URLInfo website : trainSet) {
			// System.out.println(website.getUrl() + website);
			simple.addFile(website.getUrl() + website);
		}
	}

}
