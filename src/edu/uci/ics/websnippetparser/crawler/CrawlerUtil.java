package edu.uci.ics.websnippetparser.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

public class CrawlerUtil {

	/**
	 * Returns a String Array of all files in the input Directory.
	 * 
	 * @param inputDirectory Directory to open.
	 * @return The array of Strings returned is each file in the directory
	 */
	public static String[] getFileList(String inputDirectory) {

		File dir = new File(inputDirectory);
		String[] children = dir.list();

		return children;
	}

	/**
	 * Reads a String which is supposed to be a file and puts it into a HashMap<br>
	 * <br>
	 * File must look like<br>
	 * <br>
	 * FileName1 fileTitle1<br>
	 * FileName2 fileTitle2<br>
	 * 
	 * 
	 * @param inputFile
	 * @return The object contains all the files wiht thier maping according to the Title.
	 * 
	 */
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

	/**
	 *  Copies one file from one location to another
	 * 
	 * @param srFile Source File
	 * @param dtFile Destination File
	 *
	 */
	private static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
