package edu.uci.ics.websnippetparser.extra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CommentRemover {

	public static void main(String args[]) throws IOException {

		String inputDirectory = "HandGeneratedFiles/GroupedTraini";

			for (int file = 0; file < 10; file++) {
					BufferedReader in = null;
					in = new BufferedReader(new FileReader(inputDirectory+file));
					PrintWriter out = null;
					try {
						out = new PrintWriter(new FileWriter(inputDirectory+file));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (in.ready()) {
						String temp = in.readLine();
						if(temp.length()>1 && temp.charAt(0) != '@' && temp.charAt(0) != '%'){
						out.println(temp);
						}
					}
					out.close();
				
			}
		
	}
}
