package edu.uci.ics.websnippetparser.entryPoints;

import edu.uci.ics.websnippetparser.algorithms.CodeExtractor;
import edu.uci.ics.websnippetparser.algorithms.Util;

public class OracleDemo {

	public static void main(String[] args) {
		CodeExtractor myCode = new CodeExtractor(5);
		myCode.parseFile("WebRepository/abstract000.html");
		if (myCode.getDoc() != null) {
			Util.outputFile(myCode.getDoc(), "Files/test.txt");
		}
	}
}
