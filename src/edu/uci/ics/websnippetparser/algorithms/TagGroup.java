package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;

public class TagGroup {

	private Vector<DataBean> groupedString;

	// Group segments according to types
	public TagGroup(Vector<DataBean> docString, CodeExtractor code, int alg) {
		groupedString = new Vector<DataBean>();
		String runningString = "";
		int startIndex = 0;
		int count = 0;
		Vector<DataBean> groupedSegments = new Vector<DataBean>();

		for (int j = 0; j < docString.size(); j++) {

			DataBean lastType = docString.get(j - 1 > 0 ? j - 1 : j);
			DataBean nextType = docString
					.get(j + 1 < docString.size() - 2 ? j + 1 : j);
			DataBean i = docString.get(j);

			if (lastType.isInsidePre() == i.isInsidePre()
					|| (i.getNumOfWord() <= 1 && (nextType.isInsidePre() != i
							.isInsidePre()))) {
				runningString += i.getOriginal();
				i.setInsidePre(lastType.isInsidePre());
				groupedSegments.add(i);
			} else {
				StatisticalData stat = new StatisticalData(runningString, i
						.getEndIndex(), startIndex, lastType.isInsidePre(),
						groupedString.size(), groupedString);
				stat.addGroupedSegments(groupedSegments);
				groupedString.add(stat);
				groupedSegments = new Vector<DataBean>();
				runningString = i.getOriginal();
				i.setInsidePre(i.isInsidePre());
				groupedSegments.add(i);
				startIndex = i.getStartIndex();
			}
			// your at the end so put the bottom into a Vector.
			if (++count == docString.size()) {
				StatisticalData stat = new StatisticalData(runningString, i
						.getEndIndex(), startIndex, lastType.isInsidePre(),
						groupedString.size(), groupedString);
				stat.addGroupedSegments(groupedSegments);
				groupedString.add(stat);
			}
		}

		switch (alg) {
		case 1:
			new WeightedFeature(groupedString, code);
			break;
		case 4:
			new MachineLearning(groupedString, code);
			break;
		case 0:
			for (DataBean calulate : docString) {
				
				calulate.calculateIfCode();
			}
			code.setGeneratedDoc(docString);
			break;
		}

	}

	public Vector<DataBean> getGroupedString() {

		return groupedString;
	}
}
