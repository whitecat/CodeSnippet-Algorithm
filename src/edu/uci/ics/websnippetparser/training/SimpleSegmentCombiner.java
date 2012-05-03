package edu.uci.ics.websnippetparser.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uci.ics.websnippetparser.tests.StatDataBean;

public class SimpleSegmentCombiner {
	PrintWriter out;


	public SimpleSegmentCombiner(String location) {

		try {
			out = new PrintWriter(new FileWriter(location));
			out.append("@RELATION codesnippets\n\n"
					+ "@ATTRIBUTE indentation\tNUMERIC\n"
					+ "@ATTRIBUTE comments\tNUMERIC\n"
					+ "@ATTRIBUTE nondic\tNUMERIC\n"
					+ "@ATTRIBUTE keywords\tNUMERIC\n"
					+ "@ATTRIBUTE separator\tNUMERIC\n"
					+ "@ATTRIBUTE operator\tNUMERIC\n"
					+ "@ATTRIBUTE numofwords\tNUMERIC\n"
					+ "@ATTRIBUTE stopwords\tNUMERIC\n"
					+ "@ATTRIBUTE class\t{text, code}\n\n" + "@DATA\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void addFile(String file) {

		if (!file.equals(".svn")) {
//			out.append("%" + file + "\n");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int i = 0;
			String line = null;
			try {
				while (in.ready()) {

					i++;
					line = (in.readLine());
					String[] lineValues = line.split(",");

					StatDataBean temp = new StatDataBean(lineValues[0], Integer
							.parseInt(lineValues[1]), Double
							.parseDouble(lineValues[2]), Integer
							.parseInt(lineValues[3]), Integer
							.parseInt(lineValues[4]), Integer
							.parseInt(lineValues[5]), Integer
							.parseInt(lineValues[6]), Integer
							.parseInt(lineValues[7]), Integer
							.parseInt(lineValues[8]), Integer
							.parseInt(lineValues[9]), Integer
							.parseInt(lineValues[10]), lineValues[11]);

					out.append(temp.ratioString() + "\n");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.out.println("error in Simple Grouper");
				System.out.println(line + "  Line Number: " + i + " in file: "
						+ file);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("error in Simple Grouper");
				System.out.println(line + "  Line Number: " + i + " in file: "
						+ file);
			}
		}
		out.flush();
	}

	public void close() {
		out.close();
	}
	
	public static String[] getFileList(String inputDirectory) {

		File dir = new File(inputDirectory);
		String[] children = dir.list();

		return children;
	}

}
