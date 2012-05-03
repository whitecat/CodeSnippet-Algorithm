package edu.uci.ics.websnippetparser.crawler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class URLPercentage {

	HashMap<String, Integer> list;
	int pageCount;
	StringBuffer returnString = new StringBuffer();
	DecimalFormat df = new DecimalFormat("#.###");

	public URLPercentage(int groupNumber) {
		list = new HashMap<String, Integer>();
		pageCount = 0;
		returnString.append("\n Count at " + groupNumber + " pages");
	}

	public void addQuery(String url) {
		pageCount++;
		if (list.containsKey(url)) {
			int count = list.get(url);
			list.put(url, count + 1);
		} else
			list.put(url, 1);
	}

	private void fullCount() {
		Iterator<String> iter = list.keySet().iterator();
		double runningPercentage = 0;
		while (iter.hasNext()) {
			String s = iter.next();
			double totalPercentage = ((double) list.get(s) / pageCount);
			if (totalPercentage > .02) {
				returnString
						.append("\n" + s + "{" + df.format(totalPercentage));
				runningPercentage += totalPercentage;
			}
		}
		returnString.append("\nOthers { " + df.format(1 - runningPercentage));
	}

	public void addList(HashMap<String, Integer> otherList, int otherPageCount) {
		pageCount += otherPageCount;
		Iterator<String> iter = otherList.keySet().iterator();
		while (iter.hasNext()) {
			String url = iter.next();
			int otherPageOccurance = otherList.get(url);
			if (list.containsKey(url)) {
				int listPageOccurance = list.get(url);
				list.put(url, listPageOccurance + otherPageOccurance);
			} else
				list.put(url, otherPageOccurance);
		}
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		list = (HashMap<String, Integer>) sortHashMapByValues(list, false);
		fullCount();
		return returnString.toString();
	}


	@SuppressWarnings("unchecked")
	public LinkedHashMap<?, ?> sortHashMapByValues(HashMap passedMap,
			boolean ascending) {

		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		if (!ascending)
			Collections.reverse(mapValues);

		LinkedHashMap someMap = new LinkedHashMap();
		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				if (passedMap.get(key).toString().equals(val.toString())) {
					passedMap.remove(key);
					mapKeys.remove(key);
					someMap.put(key, val);
					break;
				}
			}
		}
		return someMap;
	}

	public int getPageCount() {
		return pageCount;
	}
	
	public HashMap<String, Integer> getList() {
		return list;
	}

}
