package edu.uci.ics.websnippetparser.entryPoints;

import java.util.ArrayList;

import edu.uci.ics.websnippetparser.training.SimpleSegmentCombiner;
import edu.uci.ics.websnippetparser.training.TestGroup;
import edu.uci.ics.websnippetparser.training.Trainer;
import edu.uci.ics.websnippetparser.training.URLInfo;

public class TrainerEntryPoint {

	// This class uses all of the trainer classes.
	int numOfGroups = 2;
	int trainingSize = 1;
	int maxGroupSize = 20;

	public static void main(String[] args) {
		new TrainerEntryPoint();
	}

	public TrainerEntryPoint() {
		ArrayList<TestGroup> trainSetEMail;
		ArrayList<TestGroup> trainSetForum;
		ArrayList<TestGroup> trainSetWebPage;
		// Output Location
		String oracleLocEmail = "HandGeneratedFiles/SimpleTraining/EMailDatabase/";
		// Input Location
		String trainingLocEmail = "HandGeneratedFiles/Training/EMailDatabase/";
		String oracleLocForum = "HandGeneratedFiles/SimpleTraining/StackOverFlow/";
		String trainingLocForum = "HandGeneratedFiles/Training/StackOverFlow/";
		String oracleLocWebsite = "HandGeneratedFiles/SimpleTraining/WebPages/";
		String trainingLocWebsite = "HandGeneratedFiles/Training/WebPages/";
		String oracleLocCombined = "HandGeneratedFiles/SimpleTraining/Combined/";

		for (int i = 1; (i * trainingSize) <= maxGroupSize; i++) {
			trainSetEMail = tests(i * trainingSize, oracleLocEmail,
					trainingLocEmail);
			// trainSetForum = tests(i * trainingSize, oracleLocForum,
			// trainingLocForum);
			// trainSetWebPage = tests(i * trainingSize, oracleLocWebsite,
			// trainingLocWebsite);
			// new CombineTraining(trainSetEMail, trainSetForum,
			// trainSetWebPage,combinedOracleLocation , numOfGroups);
		}
	}

	private ArrayList<TestGroup> tests(int trainingSize, String oracleLoc,
			String trainingLoc) {

		Trainer email = new Trainer(trainingSize, numOfGroups);
		ArrayList<TestGroup> randomList = email.randomizeList(email
				.createList(trainingLoc));
		email.printList(randomList, trainingSize, oracleLoc, trainingLoc);
		return randomList;

	}
}
