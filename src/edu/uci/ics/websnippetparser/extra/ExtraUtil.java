package edu.uci.ics.websnippetparser.extra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

public class ExtraUtil {

	public static String[] getFileList(String inputDirectory) {

		File dir = new File(inputDirectory);
		String[] children = dir.list();

		return children;
	}

	public static LinkedHashMap<String, String> getFileMap(String inputFile) {
		LinkedHashMap<String, String> fileMap = new LinkedHashMap<String, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			System.out.println("Error in reading file");
			e.printStackTrace();
		}

		if (in != null) {

			try {
				while (in.ready()) {
					String[] results = in.readLine().split("\\s");
					fileMap.put(results[0] , results[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fileMap;
		
	}

	public static void outputFile(String file, String Location) {
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
