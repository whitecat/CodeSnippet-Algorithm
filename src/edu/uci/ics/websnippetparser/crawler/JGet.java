package edu.uci.ics.websnippetparser.crawler;

import java.io.*;
import java.net.*;

public class JGet {

	public String getPage(String url) {

		// if ( (args.length != 1) )
		// {
		// System.err.println( "\nUsage: java JGet [urlToGet]" );
		// System.exit(1);
		// }

		URL u;
		InputStream is = null;
		DataInputStream dis;
		String s;
		String webPage = "";
		try {
			u = new URL(url);
			is = u.openStream();
			dis = new DataInputStream(new BufferedInputStream(is));
			while ((s = dis.readLine()) != null) {
				webPage += s + "\n";
			}
		} catch (MalformedURLException mue) {
			System.err.println("Ouch - a MalformedURLException happened.");
			mue.printStackTrace();
			System.exit(2);
		} catch (IOException ioe) {
			System.err.println("Oops- an IOException happened.");
			// ioe.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException ioe) {
			}
		}
		return webPage;

	}

}
