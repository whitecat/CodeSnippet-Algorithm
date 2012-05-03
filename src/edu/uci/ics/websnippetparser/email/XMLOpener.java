package edu.uci.ics.websnippetparser.email;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlparser.util.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.StatisticalData;

public class XMLOpener implements Iterator<Email> {

	private NodeList siteNodes;
	int siteIndex = -1;

	public XMLOpener(String XML) throws ParserException,
			ParserConfigurationException, XPathExpressionException,
			SAXException, IOException {

		// Prepare Document factory
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();

		// Prepare XPath factory
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		// Set where to look in XML
		XPathExpression expr = xpath.compile("//data/records/row");

		Document repositoryDoc = builder.parse(XML);
		Object siteResult = expr
				.evaluate(repositoryDoc, XPathConstants.NODESET);

		siteNodes = (NodeList) siteResult;

	}

	private Vector<DataBean> createContentVector(String[] textContent) {
		Vector<DataBean> content = new Vector<DataBean>();
		for (String textLine : textContent) {
			content.add(new StatisticalData(textLine, 0, 0, false, content
					.size(), content));
		}
		return content;
	}

	private Vector<String> createCodeVector(String[] split) {
		Vector<String> codeList = new Vector<String>();
		for (String code : split) {
			if (!code.equals("---code-snippet---") && !code.equals("")) {
				codeList.add(code);
			}
		}
		return codeList;
	}

	@Override
	public boolean hasNext() {
		siteIndex++;
		if (siteIndex < siteNodes.getLength()) {

			return true;
		} else
			return false;

	}

	@Override
	public Email next() {
		Vector<String> codeList = null;
		Vector<DataBean> eMail = null;
		String id = null;
		
		String codeTag = "code";
		String contentTag = "content";
		String idTag = "id";
		String[] split = null;
		Node site = siteNodes.item(siteIndex);
		NodeList columnNodes = site.getChildNodes();
		for (int columnIndex = 0; columnIndex < columnNodes.getLength(); columnIndex++) {

			Node column = columnNodes.item(columnIndex);

			NamedNodeMap attribute = column.getAttributes();
			if (attribute != null && attribute.getLength() > 0) {
				split = column.getTextContent().split("\n");
				if ((attribute.item(0).getTextContent()).equals(codeTag)) {
					if (attribute.item(1) == null) {
						codeList = createCodeVector(split);
					}
				}
				else if ((attribute.item(0).getTextContent()).equals(contentTag)) {
					eMail = createContentVector(split);
				}				
				else if ((attribute.item(0).getTextContent()).equals(idTag)) {
					id = column.getTextContent();
				}
			}
		}

		return new Email(codeList, eMail, id);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}
}
