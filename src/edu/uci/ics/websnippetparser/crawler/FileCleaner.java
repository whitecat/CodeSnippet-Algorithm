package edu.uci.ics.websnippetparser.crawler;

import java.io.File;
import java.util.LinkedHashMap;

public class FileCleaner {

	public static void main(String args[]) {

		String inputDirectory = "GeneratedFiles/Google/Training/";
		String repositoryLocation = "Repository/FileMaps/First50.txt";
		String outputDirectory = "Repository/Code/";
		String newFileListOutput = "First50.txt";
		String toBeRemovedList = "EmptyFiles.txt";
		String[] fileList = CrawlerUtil.getFileList(inputDirectory);
		LinkedHashMap<String, String> fileMap = CrawlerUtil.getFileMap(repositoryLocation);
		
//		for (String file : fileMap.keySet()) {
//			System.out.println(file);
//		}
		
		
		LinkedHashMap<String, String> linesRemovedFileMap = new LinkedHashMap<String, String>();
		
		
		StringBuffer output = new StringBuffer();
		long lastSize = 0;
		long sizeEmptyFile = 4; // todo: change to actual empty file size value
		//File list contains a list 
		if (fileList != null) {
			int count = 0;
			for (String file : fileList) {
				File f = new File(inputDirectory + file);
				long size = f.length();
				String[] xmlRemoved = file.split(".txt");				
				if (size <= sizeEmptyFile) {
					count++;
					linesRemovedFileMap.put(xmlRemoved[0], fileMap
							.remove(xmlRemoved[0]));
//					System.out.println(xmlRemoved[0]);
		
				}
				lastSize = size;
			}
		} 
		
//			System.out.println("File was Empty");

		for (String file : fileMap.keySet()) {
			output.append(file + " " + fileMap.get(file)+ "\n");
 		}
		
		CrawlerUtil.outputFile(output.toString(), outputDirectory+newFileListOutput);
		output = new StringBuffer();
		for (String file : linesRemovedFileMap.keySet()) {
			output.append(file + " " + linesRemovedFileMap.get(file)+ "\n");
		}
		CrawlerUtil.outputFile(output.toString(), outputDirectory+toBeRemovedList);

		
	}
}
