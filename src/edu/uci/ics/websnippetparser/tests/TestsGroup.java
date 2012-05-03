package edu.uci.ics.websnippetparser.tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import edu.uci.ics.websnippetparser.training.GroupSegmentCombiner;
import edu.uci.ics.websnippetparser.training.SimpleSegmentCombiner;
import edu.uci.ics.websnippetparser.training.URLInfo;

public class TestsGroup {

	private String directory = "Repository/FileMaps/";
	private String handDirectory = "HandGeneratedFiles/Training/";
	private String simpleOracle = "HandGeneratedFiles/SimpleTraining/Total";
	private String groupOracle =   "HandGeneratedFiles/GroupedTraining/Total";
	private String inputFile = "First50.txt";

	
	public static void main(String[] args) {

		new TestsGroup();

	}

	public TestsGroup() {

		ArrayList<URLInfo> list = createList();


		printList(list);
	}

	private void printList(ArrayList<URLInfo> list) {
		
			String simpleLocation = simpleOracle;
			String groupLocation = groupOracle;
			
			SimpleSegmentCombiner simple = new SimpleSegmentCombiner(simpleLocation);
			for (URLInfo singleTest : list) {
				simple.addFile(handDirectory + singleTest);
			}
			simple.close();

			GroupSegmentCombiner group = new GroupSegmentCombiner(groupLocation);
			for (URLInfo singleTest : list) {
				group.addFile(handDirectory + singleTest);
			}
			group.close();

	}


	private ArrayList<URLInfo> createList() {
		ArrayList<URLInfo> list = new ArrayList<URLInfo>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(directory
					+ inputFile));

			while (in.ready()) {
				String[] line = in.readLine().split("\\s");

				list.add(new URLInfo(line[0], line[1]));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}