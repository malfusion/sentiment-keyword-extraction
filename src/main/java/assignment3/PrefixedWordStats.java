package assignment3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PrefixedWordStats {
	
	String prefix;
	HashMap<String, Integer> wordCounts;
	
	public PrefixedWordStats(String prefix) {
		this.prefix = prefix;
		wordCounts = new HashMap<String, Integer>();
	}
	
	public void process(String content) {
		for(String word: content.split(" ")) {
			if(word.startsWith(prefix)) {
				String hashtag = word.toLowerCase();
				if(!wordCounts.containsKey(hashtag)) {
					wordCounts.put(hashtag, 0);
				}
				wordCounts.put(hashtag, wordCounts.get(hashtag)+1);
			}
		}
	}
	
	
	public void printWordCounts() {
		for(String key: wordCounts.keySet()) {
			System.out.println(key + " : " + wordCounts.get(key));
		}
	}
	
	public int getWordFreq(String key) {
		return wordCounts.getOrDefault(key, 0);
	}
	
	public String[] getTopFrequencyWords(int n) {
		List<Frequency<String>> freqList = new ArrayList<Frequency<String>>();
		for(String key: wordCounts.keySet()) {
			freqList.add(new Frequency<String>(key, wordCounts.get(key)));
		}
		Collections.sort(freqList, Collections.reverseOrder());
		
		String[] res = new String[n];
		for (int i = 0; i < n; i++) {
			res[i] = freqList.get(i).getKey();
		}
		return res;
	}

}
