package edu.uci.ics.websnippetparser.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.NodeVisitor;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.uci.ics.websnippetparser.algorithms.RepositoryMaker;

public class GoogleCrawler implements Runnable {

	public static boolean output;
	private String search;

	public static void main(String[] strings) {

		String[] list = {"for"  };

		for (int i = 0; i < list.length; i++) {
			new Thread (new GoogleCrawler(list[i])).start();
		}
	}

	@Override
	// This is the thread that we are running.
	//
	public void run() {

		Hashtable<String, String> properties = new Hashtable<String, String>();
		// properties.put("User-Agent",
		// "Mozilla/5.0 (X11; U; Linux i686; pl-PL; rv:1.9.0.2) Gecko/20121223 Ubuntu/9.25 (jaunty) Firefox/3.8"
		// );
		properties.put("User-Agent", "Firefox/3.8");

		ConnectionManager.setDefaultRequestProperties(properties);

		
		final int MAXPROJECT = 1000;// 19200000;
		NodeVisitor visitor;
		String url = "http://www.google.com/";
		int i = 0;
		try {
			PrintStream printData = new PrintStream(new File(
					// place to save that data.
					"F:\\Documents and Settings\\caseyt\\Research\\" + search
							+ "\\google_data.txt"));

			int offset = 0;
			for (offset = 0; offset < MAXPROJECT; offset += 100) {

				try {
					//This is a google Query
					url = "http://www.google.com/search?num=100&filter=0&sa=N&q=java+"
							+ search + "&start=" + offset;

					Parser parser = new Parser(url);

					System.out
							.println(offset + " : Parsing " + parser.getURL());
					NodeFilter filter;

					filter = new AndFilter(new TagNameFilter("a"),
							new AndFilter(new HasAttributeFilter("class", "l"),
									new HasParentFilter(
											new AndFilter(new TagNameFilter(
													"h3"),
													new HasAttributeFilter(
															"class", "r")))));

					NodeList nodeList = parser.extractAllNodesThatMatch(filter);
					// System.out.println(nodeList.size());
					for (SimpleNodeIterator iter = nodeList.elements(); iter
							.hasMoreNodes();) {

						String fileName = (search
								+ ((i < 10) ? ("00" + i)
										: ((i < 100) ? ("0" + i) : i)) + ".html");
						PrintWriter out = new PrintWriter(new FileWriter(
								"F:\\Documents and Settings\\caseyt\\Research\\"
										+ search + "\\" + fileName));
						Node n = iter.nextNode();
						LinkTag tag = (LinkTag) n;
						// visitor = new TagRemover(out);
						// try {
						// new Parser(tag.extractLink())
						// .visitAllNodesWith(visitor);
						// } catch (ParserException e) {
						// e.printStackTrace();
						// }

						try {
							out.print((new JGet()).getPage(tag.extractLink()));
						} catch (Exception e) {

						}

						// System.out.println(tag.extractLink());
						printData.println(fileName + ", " + tag.extractLink());
						out.close();

						i++;
						// System.out.println("File: " + i + " " + output
						// + " to parse: " + tag.extractLink());
						output = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			printData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public GoogleCrawler(String search) {
		this.search = search;
	}

	public void createTag(String fileName)
			throws ParserConfigurationException, SAXException, IOException {
		// Prepare Document factory
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		String repositoryFileName = "Repository/Repository.xml";
		Document repositoryDoc = builder.parse(repositoryFileName);
		RepositoryMaker repository = new RepositoryMaker(repositoryDoc);

		// repository.createTag(fileName, uRL);

	}

}
