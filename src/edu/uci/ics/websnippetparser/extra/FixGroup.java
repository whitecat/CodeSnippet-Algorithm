package edu.uci.ics.websnippetparser.extra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FixGroup {

	public static void main(String args[]) {


		new FixGroup(1);

	}

	//Used for fixing a single file.
	private FixGroup(int i) {
//		String directory1 = "GeneratedFiles/eMails/Training/";
//		String directory2 = "HandGeneratedFiles/Training/";
//		String outputDirectory = "HandGeneratedFiles/CorrectingGroup/";
//		
//		fixDirectory(directory1, directory2, outputDirectory);
		
		String fileLoc1 = "ForumRepository/QueryResults10k.csv";
		String fileLoc2 = "ForumRepository/test.csv";
		String outputLoc = "ForumRepository/Adjusted.txt";
		int replacementLoc = 11;
		
		fixFile(fileLoc1, fileLoc2, outputLoc, replacementLoc);
		
	}
/**
 * 
 * @param fileLoc1 The location of the first file
 * @param fileLoc2 The location of the file that will have a value replaced
 * @param outputLoc The output location of the edited file.
 * @param replacementLoc The location in the array that will be changed.
 */
	public void fixFile(String fileLoc1, String fileLoc2, String outputLoc, int replacementLoc) {
		StringBuffer output = new StringBuffer();

		ArrayList<String[]> file1;
		ArrayList<String[]> file2;

		file1 = addFile(fileLoc1);
		file2 = addFile(fileLoc2);
		
		for (int i = 0; i < file1.size(); i++) {
		
			String[] lineXFile1 = file1.get(i);
			String[] lineXFile2 = file2.get(i);
			
			lineXFile2[11] = lineXFile1[11];
			output.append(arrayToString2(lineXFile2, ",") + "\n");
		}
		
		ExtraUtil.outputFile(output.toString(), outputLoc);
		output = new StringBuffer();
		
	}

	private void fixDirectory(String directory1, String directory2, String outputDirectory){
		String fileLoc1;
		String fileLoc2;
		String outputFile;
		int replacementLoc = 10;
		
		for (String file : ExtraUtil.getFileList(directory1)) {
			if (!file.equals(".svn")) {
				String[] fileTxt = file.split(".txt");
				String[] fileCsv = fileTxt[0].split(".csv");
				
				fileLoc1 = directory1 + fileTxt[0] + ".txt";
				fileLoc2 = directory2 + fileCsv[0] + ".csv";
				outputFile = outputDirectory + fileTxt[0]+ ".csv";
				fixFile(fileLoc1, fileLoc2, outputFile, replacementLoc);
			}
		}

	}

	public ArrayList<String[]> addFile(String file) {
		BufferedReader in = null;
		ArrayList<String[]> fileObject = new ArrayList<String[]>();
		try {
			in = new BufferedReader(new FileReader(file));
		int i = 0;
		String line = null;
			while (in.ready()) {
				i++;
				line = (in.readLine());
				String[] lineValues = line.split(",");
				fileObject.add(lineValues);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileObject;
	}

	// Convert an array of strings to one string.
	// Put the 'separator' string between each element.
	public String arrayToString2(String[] a, String separator) {
	    StringBuffer result = new StringBuffer();
	    if (a.length > 0) {
	        result.append(a[0]);
	        for (int i=1; i<a.length; i++) {
	            result.append(separator);
	            result.append(a[i]);
	        }
	    }
	    return result.toString();
	}
}
