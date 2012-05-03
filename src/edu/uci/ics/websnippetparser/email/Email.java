package edu.uci.ics.websnippetparser.email;

import java.util.Vector;

import edu.uci.ics.websnippetparser.algorithms.DataBean;
import edu.uci.ics.websnippetparser.algorithms.OracleRawData;
/**
 * This is the e-mail object.<br>
 * It has get parameters. and All object are stored as DataBeans
 * 
 * @author C. Albert
 *
 */
public class Email {
	private Vector<String> codeList = null;
	private Vector<DataBean> eMail = null;
	private Vector<DataBean> oracle = null;
	private String id;

	/**
	 * 
	 * @param codeList The array of code segments in the e-mail.
	 * @param content The body of the e-mail broken up by line.
	 * @param id This is the e-mails number.
	 */
	public Email(Vector<String> codeList, Vector<DataBean> content, String id) {
		oracle = new Vector<DataBean>();
		this.id = id;
		this.codeList = codeList;
		this.eMail = content;
		createOracle();
		
	}

	/*
	 * Psudo Code: If there is a code oracle Check each one and match it up with
	 * every line with in the e-mail and get code and text. else if there is no
	 * oracle set everything to text
	 * 
	 */
	private void createOracle() {
		int codeListIndex = 0;
		if (codeList != null) {
			for (DataBean emailLine : eMail) {
				String textLine = emailLine.getOriginal();
				DataBean temp = new OracleRawData(textLine, 0, 0, true,
						oracle.size(), oracle);
				if (codeListIndex < codeList.size() && textLine.contains(codeList.get(codeListIndex)) && Math.abs(textLine.length()-codeList.get(codeListIndex).length())<30) {
					codeListIndex++;
					temp.setClassification("code");
				} else {
					temp.setClassification("text");
				}
				oracle.add(temp);
			}
			if (codeListIndex != codeList.size()
					&& oracle.size() != eMail.size()) {
				System.out.println("Oracle Size: " + oracle.size());
				System.out.println("eMail Size: " + oracle.size());
			}
		} else {
			for (DataBean emailLine : eMail) {
				String textLine = emailLine.getOriginal();

				DataBean temp = new OracleRawData(textLine, 0, 0, true,
						oracle.size(), oracle);
				temp.setClassification("text");
				oracle.add(temp);
			}
			//Make sure the program doesn't have bugs
			if (oracle.size() != eMail.size()) {
				System.out.println("Oracle Size: " + oracle.size());
				System.out.println("eMail Size: " + eMail.size());
				System.out.println("Error in Email Line 72");
			}
		}
	}

	/**
	 * 
	 * @return This is the body of the e-mail with little data on its structure.
	 */
	public Vector<DataBean> getContent() {
		return eMail;
	}

	/**
	 * 
	 * @return This is the body of the e-mail with correctly labeled code and text segments.
	 */
	public Vector<DataBean> getOracle() {
		return oracle;
	}
	/**
	 * 
	 * @return This is the e-mail number in the database.
	 */
	public String getId() {
		return id;
	}

}
