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
	
	
	public void processAll(ArrayList<String> contents) {
		for(String line: contents) {
			process(line);
		}
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
		return FrequencyStats.getTopFrequencyWords(wordCounts, n);
	}

}
