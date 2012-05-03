package edu.uci.ics.websnippetparser.entryPoints;

import edu.uci.ics.websnippetparser.training.SimpleSegmentCombiner;

/**
 * This is example code of how to make one HTML oracle file into a SimpleSegment
 * training file.
 * 
 * @author C. Albert
 * 
 */
public class SimpleSegmentCombinerDemo {
	public static void main(String[] args) {
		SimpleSegmentCombiner simple = new SimpleSegmentCombiner(
				"Files/test.txt");
		simple.addFile("HandGeneratedFiles/Training/WebPages/abstract000.html");
		simple.close();
	}
}
