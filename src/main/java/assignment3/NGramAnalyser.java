package assignment3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import intoxicant.analytics.coreNlp.StopwordAnnotator;


public class NGramAnalyser {
	private StanfordCoreNLP pipeline;
    HashMap<String, Integer> oneGramCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> twoGramCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> threeGramCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> fourGramCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> oneGramNounCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> twoGramNounCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> threeGramNounCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> fourGramNounCounts = new HashMap<String, Integer>();
    
	
	public NGramAnalyser() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,stopword,pos,lemma");
		props.setProperty("customAnnotatorClass.stopword", "intoxicant.analytics.coreNlp.StopwordAnnotator");
		props.setProperty("tokenize.options", "untokenizable=noneDelete");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	public void analyseAll(ArrayList<String> contents) {
		for (String content: contents) {
			Annotation document = new Annotation(content);
			pipeline.annotate(document);
			List<CoreLabel>   labels = document.get(CoreAnnotations.TokensAnnotation.class);
			ArrayList<String> lemmas = new ArrayList<String>();
			labels = filterStopwords(labels);			

			
			for(CoreLabel label: labels) lemmas.add(label.lemma());
			
			List<String> oneGrams   = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(1)));
			List<String> twoGrams   = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(2)));
			List<String> threeGrams = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(3)));
			List<String> fourGrams  = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(4)));
			
			for(String oneGram: oneGrams) {
				FrequencyStats.incrementFreq(oneGramCounts, oneGram.toLowerCase());
			}
			for(String twoGram: twoGrams) {
				FrequencyStats.incrementFreq(twoGramCounts, twoGram.toLowerCase());
			}
			for(String threeGram: threeGrams) {
				FrequencyStats.incrementFreq(threeGramCounts, threeGram.toLowerCase());
			}
			for(String fourGram: fourGrams) {
				FrequencyStats.incrementFreq(fourGramCounts, fourGram.toLowerCase());
			}
		}
		System.out.println("Top 20 1-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(oneGramCounts,   20)));
		System.out.println("Top 20 2-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(twoGramCounts,   20)));
		System.out.println("Top 20 3-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(threeGramCounts, 20)));
		System.out.println("Top 20 4-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(fourGramCounts,  20)));
	}
	
	public void analyseAllOnlyNouns(ArrayList<String> contents) {
		for (String content: contents) {
			Annotation document = new Annotation(content);
			pipeline.annotate(document);
			List<CoreLabel>   labels = document.get(CoreAnnotations.TokensAnnotation.class);
			ArrayList<String> lemmas = new ArrayList<String>();
			labels = filterStopwords(labels);			

			
			for(CoreLabel label: labels) {
				String pos = label.get(PartOfSpeechAnnotation.class);
				if(pos.startsWith("NN")) {
					lemmas.add(label.lemma());
				}
			}
			
			List<String> oneGrams   = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(1)));
			List<String> twoGrams   = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(2)));
			List<String> threeGrams = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(3)));
			List<String> fourGrams  = NGramAnalyser.getNGrams(lemmas, new ArrayList<Integer>(Arrays.asList(4)));
			
			for(String oneGram: oneGrams) {
				FrequencyStats.incrementFreq(oneGramNounCounts, oneGram.toLowerCase());
			}
			for(String twoGram: twoGrams) {
				FrequencyStats.incrementFreq(twoGramNounCounts, twoGram.toLowerCase());
			}
			for(String threeGram: threeGrams) {
				FrequencyStats.incrementFreq(threeGramNounCounts, threeGram.toLowerCase());
			}
			for(String fourGram: fourGrams) {
				FrequencyStats.incrementFreq(fourGramNounCounts, fourGram.toLowerCase());
			}
		}
		System.out.println("Top 20 Noun 1-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(oneGramNounCounts,   20)));
		System.out.println("Top 20 Noun 2-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(twoGramNounCounts,   20)));
		System.out.println("Top 20 Noun 3-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(threeGramNounCounts, 20)));
		System.out.println("Top 20 Noun 4-Grams:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(fourGramNounCounts,  20)));
	}
		
	private List<CoreLabel> filterStopwords(List<CoreLabel> labels){
		List<CoreLabel> filtered = new ArrayList<CoreLabel>();
		for (CoreLabel label: labels) {
			if (!label.get(StopwordAnnotator.class).first()) {
				filtered.add(label);
			}	
		}
		return filtered;
	}
	
	public static List<String> getNGrams(List<String> words, List<Integer> sizes){
		ArrayDeque<String> deque = new ArrayDeque<String>();
		ArrayList<String> ngrams = new ArrayList<String>();
		
		Integer maxSize = Collections.max(sizes);		
		for (String word: words) {
			if (word != null) {
				word = word.replaceAll("\\p{Punct}","");
				if(word.trim().length() == 0) {
					continue;
				}
				if(deque.size() == maxSize) {
					deque.removeFirst();
				}
				deque.addLast(word);
				Integer currn = 0;
				StringBuilder sb = new StringBuilder();
				Iterator<String> iterator = deque.descendingIterator();
				while (iterator.hasNext()) {
					String deqWord = iterator.next();
					
					if(sb.length() > 0) {
						sb.insert(0, deqWord + '_');
					}else {
						sb.insert(0, deqWord);
					}
					
					currn += 1;
					if(sizes.contains(currn)) {
						ngrams.add(sb.toString());
					}
				}
			}
		}
		return ngrams;
	}
	
}
