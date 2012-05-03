package edu.uci.ics.websnippetparser.algorithms;

import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;

public class SimpleSegment extends NodeVisitor {
	private boolean filterTagFlag = false;
	private int filterHeadFlag = 0;
	private int codeNestNum = 0;
	Vector<DataBean> docString;
	private String temp = "";
	private int alg;
	private CodeExtractor code;
	int index;

	public SimpleSegment(CodeExtractor code, int alg) {
		this.alg = alg;
		docString = new Vector<DataBean>();
		this.code = code;
	}

	public void visitTag(Tag tag) {
		String tagName = (tag.getTagName());
		if (tagName.equals(Util.HEAD_TAG)) {
			filterHeadFlag++;
		} else if (tagName.equals("BR")) {
			temp += "\n";
		} else if (tagName.equals(Util.CODE_TAG)
				|| tagName.equals(Util.PRE_TAG)) {
			codeNestNum++;
		}
		filterTagFlag = Util.filterFlag(tagName);
	}

	public void visitStringNode(Text string) {

		String data = StringEscapeUtils.unescapeHtml(temp + string.toHtml());
		if (!filterTagFlag && filterHeadFlag == 0) {
			// System.out.print("BREAK HERE!!" + string.toPlainTextString());
			if (alg == 5) {
				OracleRawData raw = new OracleRawData(data,
						string.getEndPosition(), string.getStartPosition(),
						codeNestNum != 0 ? true : false, docString.size(),
						docString);
				createGroupSegment(string, data, raw);
				docString.add(raw);
			} else if (alg == 0 || alg == 4 || alg == 1) {
				docString.add(new DataBean(data, string.getEndPosition(),
						string.getStartPosition(), codeNestNum != 0 ? true
								: false, docString.size(), docString));
			} else {
				StatisticalData stat = new StatisticalData(data,
						string.getEndPosition(), string.getStartPosition(),
						codeNestNum != 0 ? true : false, docString.size(),
						docString);
				createGroupSegment(string, data, stat);
				docString.add(stat);
			}
			temp = "";
		}

	}

	private void createGroupSegment(Text string, String data, DataBean stat) {
		Vector<DataBean> segment = new Vector<DataBean>();
		segment.add(new DataBean(data, string.getEndPosition(), string
				.getStartPosition(), codeNestNum != 0 ? true : false, docString
				.size(), docString));
		stat.addGroupedSegments(segment);
	}

	public void visitEndTag(Tag tag) {

		String tagName = (tag.getTagName());

		if (tagName.equals(Util.HEAD_TAG)) {
			filterHeadFlag--;
		} else if (tagName.equals(Util.CODE_TAG)
				|| tagName.equals(Util.PRE_TAG)) {
			codeNestNum--;
		} else if (tagName.equals(Util.BODY_TAG)) {

			switch (alg) {
			case 2:
				new WeightedFeature(docString, code);
				break;
			case 3:
				new MachineLearning(docString, code);
				break;
			case 5:
				new CreateRaw(docString, code);
				break;
			case 0:
			case 4:
			case 1:
				new TagGroup(docString, code, alg);
				break;
			}
		}
	}
}