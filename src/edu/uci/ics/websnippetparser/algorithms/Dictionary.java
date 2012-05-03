package edu.uci.ics.websnippetparser.algorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Dictionary {

private	HashMap<String, String> dictionary = new HashMap<String, String>();

	public Dictionary(String string) throws FileNotFoundException {

		BufferedReader br = new BufferedReader(new FileReader(string));

		try {

			String record;
			while ((record = br.readLine()) != null) {
				dictionary.put(record.toLowerCase(), "0");
			}

		} catch (IOException e) {
			// 
			// put your error-handling code here
			// 
		}

		// System.out.println();

	}
	
	public Dictionary(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		String record;
		while ((record = br.readLine()) != null) {
			dictionary.put(record.toLowerCase(), "0");
		}
	}

	public boolean containsWord(String word) {
		word = word.toLowerCase();
		if (dictionary.containsKey(word))
			return true;
		else
			return false;
	}

}
