package edu.uci.ics.websnippetparser.extra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class FileSplitter {
	public static void main(String[] args) {
		new FileSplitter();
	}

	public FileSplitter() {
		String file = "ForumRepository/Oracle.csv";
		String pattern = ",0,0,0,0,0,0,0,0,3,5,text";
		DecimalFormat myFormat = new java.text.DecimalFormat("000");
		
		
		StringBuffer output = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			int i = 0;
			while (in.ready()) {
				String line = in.readLine();
				
				if (line.equals(pattern) || line.equals("group"+pattern)) {
					String fileName = myFormat.format(new Integer(i))+".txt";
					print(output.toString(),
							"HandGeneratedFiles/Training/StackOverFlow/" + fileName);
					output = new StringBuffer();
					i++;
				}
				
				output.append(line + "\n");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print(String file, String Location) {
		PrintWriter out;
		try {

			out = new PrintWriter(new BufferedWriter(new FileWriter(Location)));
			out.print(file);
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
