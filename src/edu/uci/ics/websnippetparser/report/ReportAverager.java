package edu.uci.ics.websnippetparser.report;

public class ReportAverager {

	private String name;
	private double recallCode = 0;
	private double recallText = 0;
	private double percisionCode = 0;
	private double percisionText = 0;
	private double f1Code = 0;
	private double f1Text = 0;
	private double accuracy = 0;
	private int codeCorrect = 0;
	private int codeFalse = 0;
	private int textCorrect = 0;
	private int textFalse = 0;
	private int numberOfTests = 0;
	private double testTime;

	public ReportAverager(String name) {

		this.name = name;

	}

	public void addAverage(SimpleReport report, long testTime) {
		numberOfTests++;
		recallCode += report.getRecallCode();
		recallText += report.getRecallText();
		percisionCode += report.getPercisionCode();
		percisionText += report.getPercisionText();
		f1Code += report.getF1Code();
		f1Text += report.getF1Text();
		accuracy += report.getAccuracy();
		codeCorrect += report.getCodeCorrect();
		codeFalse += report.getCodeFalse();
		textCorrect += report.getTextCorrect();
		textFalse += report.getTextFalse();
		this.testTime += testTime;
		report=null;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		calculateAvg();
		String returnString = name + "," + testTime + "," + recallText + "," + percisionText
				+ "," + f1Text + "," + recallCode + "," + percisionCode + ","
				+ f1Code + "," + accuracy + "," + codeCorrect + "," + codeFalse
				+ "," + textCorrect + "," + textFalse ;
		return returnString;
	}

	private void calculateAvg() {
		recallCode = recallCode / numberOfTests;
		recallText = recallText / numberOfTests;
		percisionCode = percisionCode / numberOfTests;
		percisionText = percisionText / numberOfTests;
		f1Code = f1Code / numberOfTests;
		f1Text = f1Text / numberOfTests;
		accuracy = accuracy / numberOfTests;
		codeCorrect = codeCorrect / numberOfTests;
		codeFalse = codeFalse / numberOfTests;
		textCorrect = textCorrect / numberOfTests;
		textFalse = textFalse / numberOfTests;
		testTime = testTime / numberOfTests;
	}
}
