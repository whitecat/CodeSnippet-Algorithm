package edu.uci.ics.websnippetparser.extra;

import java.text.DecimalFormat;


public class Sandbox {


	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		new Sandbox();

		long elapsed = System.currentTimeMillis() - start;
		System.out.println(elapsed);

	}

	public Sandbox() {
		DecimalFormat myFormat = new java.text.DecimalFormat("000");
		String i = myFormat.format(new Integer(10));
		
		System.out.println(i);
	}
}
