package edu.uci.ics.websnippetparser.email;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.OracleRawData;
import edu.uci.ics.websnippetparser.algorithms.StatisticalData;

/**
 * This will
 * 
 * @author C. Albert
 * 
 */
public class EMailOpener {

	private int alg;

	/**
	 * 
	 * This program allows for e-mails to be looked at. It breaks the e-mail
	 * apart by line.
	 *
	 * This still needs Statistical data to be loaded
	 * 
	 * @param alg
	 *            The algorithm that will be used.
	 */
	public EMailOpener(int alg) {
		this.alg = alg;
	}

	/**
	 * 
	 * @param eMailLocation
	 *            Takes the location of the file that is to be opened
	 * @return Returns the file in DataBean form.
	 */
	public Vector<DataBean> eMailExtractor(String eMailLocation) {

		Vector<DataBean> lineList = new Vector<DataBean>();

		BufferedReader br = null;
		String lineOfText = null;
		StatisticalData stat = null;
		OracleRawData raw = null;
		try {

			br = new BufferedReader(new FileReader(eMailLocation));
			while (br.ready()) {
				lineOfText = preProcess(br.readLine());
				switch (alg) {
				case 3:
				case 2:
					stat = new StatisticalData(lineOfText, 0, 0, false,
							lineList.size(), lineList);
					lineList.add(stat);
					break;
				case 5:
					raw = new OracleRawData(lineOfText, 0, 0, false, lineList
							.size(), lineList);
					lineList.add(raw);
					break;
				default:
					break;

				}
				// dispose all the resources after using them.
			}
			br.close();
			return lineList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String preProcess(String lineOfText) {
		lineOfText = StringEscapeUtils.unescapeHtml(lineOfText);
		while (lineOfText.startsWith(">") || lineOfText.startsWith("+")) {
			lineOfText = lineOfText.substring(1);
		}
		lineOfText = lineOfText.replaceAll(" =A0", "");
		return lineOfText;
	}

}
