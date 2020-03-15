package assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FrequencyStats {

	public static String[] getTopFrequencyWords(HashMap<String, Integer> freqMap, int n) {
		List<Frequency<String>> freqList = new ArrayList<Frequency<String>>();
		for (String key : freqMap.keySet()) {
			freqList.add(new Frequency<String>(key, freqMap.get(key)));
		}
		Collections.sort(freqList, Collections.reverseOrder());
		String[] res = new String[n];
		System.out.println("Freq" + freqList.size());
		for (int i = 0; i < n && i < freqList.size(); i++) {
			res[i] = freqList.get(i).getKey() + " : " + freqList.get(i).getFreq();
		}
		return res;
	}

	public static void incrementFreq(HashMap<String, Integer> freqMap, String key) {
		FrequencyStats.incrementFreqBy(freqMap, key, 1);
	}

	public static void incrementFreqBy(HashMap<String, Integer> freqMap, String key, int value) {
		freqMap.put(key, freqMap.getOrDefault(key, 0) + value);
	}

}
