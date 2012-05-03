package edu.uci.ics.websnippetparser.report;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import edu.uci.ics.websnippetparser.tests.StatDataBean;
import edu.uci.ics.websnippetparser.tests.StatDataCombine;

//This combines group contentsegment data.
public class SegmentGrouper {
	public Vector<StatDataCombine> gold;
	// private static Logger logger;

	public SegmentGrouper(String location) {
		gold = new Vector<StatDataCombine>();
		addFile(location);
	}

	public static void main(String[] args) {

	}

	// this is an entry point to the program that combines the information.
	// It will read the first line only then put the rest into openFile.
	public void addFile(String fileName) {
		StatDataCombine currentData = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// Get past the first 12 lines they are never anything..
			String line = (in.readLine());
			String[] lineValues = line.split(",");
			// This is the first object that will be added.
			currentData = new StatDataCombine(new StatDataBean(lineValues[0],
					Integer.parseInt(lineValues[1]), Double
							.parseDouble(lineValues[2]), Integer
							.parseInt(lineValues[3]), Integer
							.parseInt(lineValues[4]), Integer
							.parseInt(lineValues[5]), Integer
							.parseInt(lineValues[6]), Integer
							.parseInt(lineValues[7]), Integer
							.parseInt(lineValues[8]), Integer
							.parseInt(lineValues[9]), Integer
							.parseInt(lineValues[10]), lineValues[11]));

			openFile(in, currentData, lineValues[0]);

		} catch (IOException e) {

		}

	}

	private void openFile(BufferedReader in, StatDataCombine currentData,
			String type) {
		String lastType = type;
		try {
			while (in.ready()) {
				String line = (in.readLine());
				String[] lineValues = line.split(",");
				// if lastLine type != this type then make a new object
				// else add to last type
				// logger.debug(lineValues[0]);
				if (!lastType.equals(lineValues[0])) {
					gold.add(currentData);
					currentData = new StatDataCombine(new StatDataBean(
							lineValues[0], Integer.parseInt(lineValues[1]),
							Double.parseDouble(lineValues[2]), Integer
									.parseInt(lineValues[3]), Integer
									.parseInt(lineValues[4]), Integer
									.parseInt(lineValues[5]), Integer
									.parseInt(lineValues[6]), Integer
									.parseInt(lineValues[7]), Integer
									.parseInt(lineValues[8]), Integer
									.parseInt(lineValues[9]), Integer
									.parseInt(lineValues[10]), lineValues[11]));

				} else {
					currentData.add(new StatDataBean(lineValues[0], Integer
							.parseInt(lineValues[1]), Double
							.parseDouble(lineValues[2]), Integer
							.parseInt(lineValues[3]), Integer
							.parseInt(lineValues[4]), Integer
							.parseInt(lineValues[5]), Integer
							.parseInt(lineValues[6]), Integer
							.parseInt(lineValues[7]), Integer
							.parseInt(lineValues[8]), Integer
							.parseInt(lineValues[9]), Integer
							.parseInt(lineValues[10]), lineValues[11]));
				}
				lastType = lineValues[0];
			}
			gold.add(currentData);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
