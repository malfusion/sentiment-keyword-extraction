package assignment3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class KeywordAnalyser {
	private StanfordCoreNLP pipeline;
    HashMap<String, Integer> JJNNCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> RBVBCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> JJVBCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> VBNNCounts = new HashMap<String, Integer>();
	
	public KeywordAnalyser() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,ner");
//		props.setProperty("threads", "4");
		props.setProperty("ner.useSUTime", "false");
		props.setProperty("tokenize.options", "untokenizable=noneDelete");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	
	public List<String> getPosOfLabels(List<CoreLabel> labels) {
		List<String> labelsPos = new ArrayList<String>();
		for(CoreLabel label: labels) {
			labelsPos.add(label.get(PartOfSpeechAnnotation.class));
		};
		return labelsPos;
	}
	
	
	public List<String> getWordsWithPOSPattern(List<CoreLabel> labels, List<String> labelsPos, String firstPos, String secondPos) {
		List<String> keywords = new ArrayList<String>();
		for (int i = 0; i < labels.size()-1; i++) {
			if(labelsPos.get(i).startsWith(firstPos) && labelsPos.get(i+1).startsWith(secondPos)) {
				keywords.add(labels.get(i).word()+"_"+labels.get(i+1).word());
			}
		}
		return keywords;
	}
	
	
	public void analyseAll(List<String> contents) {
		for(String content: contents) {
			analyse(content);
		}
		System.out.println("Top JJ-NN:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(JJNNCounts, 20)));
		System.out.println("Top RB-VB:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(RBVBCounts, 20)));
		System.out.println("Top JJ-VB:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(JJVBCounts, 20)));
		System.out.println("Top VB-NN:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(VBNNCounts, 20)));
		
	}
	
	public void analyse(String content) {
		ArrayList<String> res = new ArrayList<String>();
		Annotation document = new Annotation(content);
		pipeline.annotate(document);
		
 
		for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)) {
			List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
			List<String> labelsPos = getPosOfLabels(labels);
			// Looking for Adjective Noun [JJ, NN]
			for(String keyword: getWordsWithPOSPattern(labels, labelsPos, "JJ", "NN")) {
				FrequencyStats.incrementFreq(JJNNCounts, keyword.toLowerCase());
			}
			// Looking for Adverb Verb [RB, VB]
			for(String keyword: getWordsWithPOSPattern(labels, labelsPos, "RB", "VB")) {
				FrequencyStats.incrementFreq(RBVBCounts, keyword.toLowerCase());
			}
			// Looking for Adjective Verb [JJ, VB]
			for(String keyword: getWordsWithPOSPattern(labels, labelsPos, "JJ", "VB")) {
				FrequencyStats.incrementFreq(JJVBCounts, keyword.toLowerCase());
			}
			// Looking for Verb Noun [VB, NN]
			for(String keyword: getWordsWithPOSPattern(labels, labelsPos, "VB", "NN")) {
				FrequencyStats.incrementFreq(VBNNCounts, keyword.toLowerCase());
			}
		}
		

	}
	
	
	
	
}
