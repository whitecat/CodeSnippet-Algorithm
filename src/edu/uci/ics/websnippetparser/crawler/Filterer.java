package edu.uci.ics.websnippetparser.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

public class Filterer {
	private String directory = "Repository/Forums/";
	private String outDirectory = "HandGeneratedFiles/AllWeb/";
	public static void main(String[] args) throws ParserException {
		new Filterer();
	}

	public Filterer(){

		File dir = new File(directory);
		String[] children = dir.list();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
//				System.out.println("input file: " + filename);

				NodeVisitor visitor;
				Parser parser;
				visitor = new TagRemover(this, filename);
				try {
					parser = new Parser(directory + filename);
					parser.visitAllNodesWith(visitor);
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}

	public void printer(StringBuffer file, String fileName) {
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object

		try {
			// Create a new file output stream
			// connected to "myfile.txt"
//			System.out.println("Output file: "+fileName);
			out = new FileOutputStream(outDirectory+fileName);

			// Connect print stream to the output stream
			p = new PrintStream(out);
			p.println(file);
			
			p.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Error writing to file");
		}

	}

}
