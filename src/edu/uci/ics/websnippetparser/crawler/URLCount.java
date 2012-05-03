package edu.uci.ics.websnippetparser.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class URLCount {

	HashMap<String, Integer> list;
	int pageCount;
	StringBuffer returnString = new StringBuffer();

	public URLCount(String filename) {
		list = new HashMap<String, Integer>();
		pageCount = 0;
		returnString.append(filename);
	}

	//Adds a page to the list of URLs.
	@SuppressWarnings("unchecked")
	public void addPage(String url) {
		if (list.containsKey(url)) {
			int count = list.get(url);
			list.put(url, count + 1);
			// System.out.println("hello");
		} else
			list.put(url, 1);

		pageCount++;
		if (pageCount % 100 == 0) {
			list = sortHashMapByValues(list, false);
			partialCount();
		}
	}

	//This does a total of the current number of URLs in the list of URLs
	//If the number of occurrences is greater than 4
	//This total is added to the string that will be returned.
	private void partialCount() {
		returnString.append("\n Count at " + pageCount + " pages");
		Iterator<String> iter = list.keySet().iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			int count = list.get(s);
			if (count > 4) {
				returnString.append("\n" + s + "{" + count);
			}
		}
	}

	// Sorts the list of URL's then puts it into a stringbuffer and returns 
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		list = sortHashMapByValues(list, false);
		partialCount();
		return returnString.toString();
	}
	
	
	
     // Orders a list
	//if ascending is true it orders asending, else decending.
	//returns the very same list
	@SuppressWarnings("unchecked")
	public LinkedHashMap sortHashMapByValues(HashMap passedMap,
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

}
