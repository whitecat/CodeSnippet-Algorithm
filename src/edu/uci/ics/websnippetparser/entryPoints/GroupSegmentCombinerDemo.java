package edu.uci.ics.websnippetparser.entryPoints;

import edu.uci.ics.websnippetparser.training.GroupSegmentCombiner;
/**
 * This is example code of how to make one HTML oracle file into a GroupSegment training file.
 * 
 *  @author C. Albert
 *
 */
public class GroupSegmentCombinerDemo {
	public static void main(String[] args) {
		GroupSegmentCombiner simple = new GroupSegmentCombiner("Files/test.txt");
		simple.addFile("HandGeneratedFiles/Training/WebPages/abstract000.html");
		simple.close();
	}
}
