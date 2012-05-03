package edu.uci.ics.websnippetparser.algorithms;

import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

public class WeightedFeature {
	
	private Vector<DataBean> docString;

	public WeightedFeature(Vector<DataBean> incommingDoc, CodeExtractor code) {
		if (incommingDoc == null)
			throw new NoSuchElementException();
		
		docString = incommingDoc;
		summarizeStat();
		code.setGeneratedDoc(docString);
	}
	
	@SuppressWarnings("deprecation")
	public void summarizeStat() {
		// Get a SummaryStatistics instance using factory method
		SummaryStatistics statsCodeScore = SummaryStatistics.newInstance();
		SummaryStatistics statsTextScore = SummaryStatistics.newInstance();

		// adding values and updating sums, counters, etc.
		for (int i = 0; i < docString.size(); i++) {
			statsCodeScore.addValue(((StatisticalData) docString.get(i))
					.getCodeScore());
			statsTextScore.addValue(((StatisticalData) docString.get(i))
					.getTextScore());
		}

		// Compute the statistics
		double meanCodeScore = statsCodeScore.getMean();
		double stdCodeScore = statsCodeScore.getStandardDeviation();
		double meanTextScore = statsTextScore.getMean();
		double stdTextScore = statsTextScore.getStandardDeviation();

		for (int i = 0; i < docString.size(); i++) {
			// System.out.println(docString.get(i));
			StatisticalData statString = (StatisticalData) docString.get(i);
			String temp = statString.toString();
			temp = temp.replaceAll("\\s\\s+|\\n\\r|\\n", " ");

			statString.calcStdCodeScore(stdCodeScore, meanCodeScore);
			statString.calcStdTextScore(stdTextScore, meanTextScore);
			statString.calcClassification(docString, i);
		}			
	}
}
