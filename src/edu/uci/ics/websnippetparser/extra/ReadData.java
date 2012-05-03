package edu.uci.ics.websnippetparser.extra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadData {
	public static void main(String[] args) {
		new ReadData();
	}

	public ReadData() {
		String inputDirectory = "HandGeneratedFiles/GroupedTraining/";

		for (int file = 0; file < 10; file++) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(inputDirectory + file
						+ ".data"));
				while (in.ready()) {
					String temp = in.readLine();
					System.out.println(Double.parseDouble(temp));
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
