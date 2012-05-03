package edu.uci.ics.websnippetparser.crawler;

import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.Util;

public class TagRemover extends NodeVisitor {
	private StringBuffer file;
	Vector<DataBean> docString;
	private String headHTMLTag = "HEAD";
	private boolean filterHeadFlag = false;
	private Filterer filterer;
	private boolean filterTagFlag;
	private String filename;

	public TagRemover(Filterer filterer, String filename) {
		this.filename = filename;
		this.filterer = filterer;
		file = new StringBuffer();
	}

	public void visitTag(Tag tag) {
		String tagName = (tag.getTagName());
		if (tagName.equals("HTML")){
			file.append("\n<codesnippets>\n<text>\n");
		}
		if (tagName.equals(headHTMLTag)) {
			filterHeadFlag = true;
		} else if (tagName.equals("BR")) {
			file.append("\n");
		} else if (tagName.equals("PRE")) {
			file.append("\n</text>\n<sourcecode>\n");
		} else if (tagName.equals("CODE")) {
			file.append("\n</text>\n<sourcecode>\n");
		}
		filterTagFlag = Util.filterFlag(tag.getTagName());

	}

	public void visitEndTag(Tag tag) {
		String tagName = (tag.getTagName());
		if (tagName.equals(headHTMLTag)) {
			filterHeadFlag = false;
		} else if (tagName.equals("HTML")) {
			file.append("\n</text>\n</codesnippets>\n");
			filterer.printer(file, filename);
		} else if (tagName.equals("PRE")) {
			file.append("\n</sourcecode>\n<text>\n");
		} else if (tagName.equals("CODE")) {
			file.append("\n</sourcecode>\n<text>\n");
		}
	}

	public void visitStringNode(Text txt) {
		if (!filterHeadFlag && !filterTagFlag) {
			file.append(StringEscapeUtils.escapeXml(StringEscapeUtils.unescapeHtml(txt.toHtml())));
		}
	}
}
