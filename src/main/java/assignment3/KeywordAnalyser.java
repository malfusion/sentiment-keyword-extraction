package assignment3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class KeywordAnalyser {
	private StanfordCoreNLP pipeline;
    HashMap<String, Integer> JJNNCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> RBVBCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> JJVBCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> VBNNCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> SubjRootObjCounts = new HashMap<String, Integer>();
    HashMap<String, Integer> EntityCounts = new HashMap<String, Integer>();
	
	public KeywordAnalyser() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,ner");
		props.setProperty("ner.useSUTime", "false");
		props.setProperty("tokenize.options", "untokenizable=noneDelete");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	
	
	public void analyseAll(List<String> contents) {
		int cnt = 0;
		System.out.println("Total:" + contents.size());
		for(String content: contents) {
			if(cnt % 10000 == 0) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now();  
				System.out.println(cnt + " :: " + dtf.format(now));  
			}				
			analyse(content);
			cnt += 1;
		}
		System.out.println("Top 20 JJ-NN:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(JJNNCounts, 20)));
		System.out.println("Top 20 RB-VB:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(RBVBCounts, 20)));
		System.out.println("Top 20 JJ-VB:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(JJVBCounts, 20)));
		System.out.println("Top 20 VB-NN:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(VBNNCounts, 20)));
		System.out.println("Top 20 Subj-Root-Obj:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(SubjRootObjCounts, 20)));
		System.out.println("Top 20 Entities:" + Arrays.toString(FrequencyStats.getTopFrequencyWords(EntityCounts, 20)));
		
	}
	
	public void analyse(String content) {
		ArrayList<String> res = new ArrayList<String>();
		Annotation document = new Annotation(content);
		pipeline.annotate(document);
		
		
		// START - POS ANALYSIS
		for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)) {
			List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
			labels = removeDotLabels(labels);
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
		
		// START - DEPENDENCY ANALYSIS
		
		for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)) {
			SemanticGraph deps = (sentence.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class));

			Collection<IndexedWord> roots = deps.getRoots();
			if(roots.size() > 0) {
				IndexedWord root = roots.iterator().next();
				Collection<IndexedWord> subjs = new ArrayList<IndexedWord>();
				Collection<IndexedWord> objs = new ArrayList<IndexedWord>();
				for (IndexedWord child: deps.getChildren(root)) {
					if(deps.reln(root, child).toString() == "nsubj") {
						subjs.add(child);
					}
					if (deps.reln(root, child).toString() == "dobj"){
						objs.add(child);
					}
				}
				for(IndexedWord subj: subjs) {
					for(IndexedWord obj: objs) {
						FrequencyStats.incrementFreq(SubjRootObjCounts , (subj.lemma() + "_" + root.lemma() + "_" + obj.lemma()).toLowerCase());
					}
				}
			}
		}
		
		// END - DEPENDENCY ANALYSIS
		
		// START - NAMED ENTITIES ANALY
		
		for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)) {
			List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
			labels = removeDotLabels(labels);
			for(String entity: getEntities(labels)) {
				FrequencyStats.incrementFreq(EntityCounts , entity.toLowerCase());
			}
		}
		
		// END - NAMED ENTITIES ANALY
		
		
	}
	

	private List<String> getPosOfLabels(List<CoreLabel> labels) {
		List<String> labelsPos = new ArrayList<String>();
		for(CoreLabel label: labels) {
			labelsPos.add(label.get(PartOfSpeechAnnotation.class));
		};
		return labelsPos;
	}
	
	
	private List<String> getWordsWithPOSPattern(List<CoreLabel> labels, List<String> labelsPos, String firstPos, String secondPos) {
		List<String> keywords = new ArrayList<String>();
		for (int i = 0; i < labels.size()-1; i++) {
			if(labelsPos.get(i).startsWith(firstPos) && labelsPos.get(i+1).startsWith(secondPos)) {
				keywords.add(labels.get(i).word()+"_"+labels.get(i+1).word());
			}
		}
		return keywords;
	}
	
	
	private List<String> getEntities(List<CoreLabel> labels){
		ArrayList<String> res = new ArrayList<String>();
		String prev = "";
		String word = "";
		for(CoreLabel label: labels) {
			String curr = label.ner();
			if (prev != curr) {
				if(prev.length() != 1 && word != "") {
					res.add(word);
				}
				word = label.lemma();
			}
			else {
				word += "_" + label.lemma();
			}
			prev = curr;
		}
		if(word != "") {
			res.add(word);
		}
		return res;
	}
	
	
	private List<CoreLabel> removeDotLabels(List<CoreLabel> labels) {
		List<CoreLabel> res = new ArrayList<CoreLabel>();

		for(CoreLabel label: labels) {
			if(!label.lemma().contentEquals(".")) {
				res.add(label);
			}
		}
		return res;
	}
	
	
	
	
}
