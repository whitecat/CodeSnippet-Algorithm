package edu.uci.ics.websnippetparser.extra;

import java.util.ArrayList;

public class FolderCompare {

	public static void main(String args[]) {
		
		StringBuffer output = new StringBuffer();
		String directory1 = "GeneratedFiles/Google/Training/";
		String directory2 = "HandGeneratedFiles/Training/";
		String outputDirectory = "Repository/Code/";
		String outputName = "Errors.txt";
		
		ArrayList<String> fileArray1 = new ArrayList<String>();
		ArrayList<String> fileArray2 = new ArrayList<String>();
		// for (String file : fileMap.keySet()) {
		// System.out.println(file);
		// }

		for (String file : ExtraUtil.getFileList(directory1)) {
			String[] fileName = file.split(".txt");
			fileArray1.add(fileName[0]);
		}
		output.append("In Directory " + directory2 + " not in Directory " + directory1 + "\n");
		for (String file : ExtraUtil.getFileList(directory2)) {
			String[] fileName = file.split(".csv");
			fileArray2.add(fileName[0]);
			if (!fileArray1.contains(fileName[0])) {
				output.append(file + "\n");
			}
		}	
		output.append("In Directory " + directory1 + " not in Directory " + directory2 + "\n");
		for (String file : fileArray1) {
			if (!fileArray2.contains(file)) {
				output.append(file + "\n");
			}
		}
		 ExtraUtil.outputFile(output.toString(), outputDirectory+outputName);

	}
}
