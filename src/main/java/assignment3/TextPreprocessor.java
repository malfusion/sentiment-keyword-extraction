package assignment3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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
//import intoxicant.analytics.coreNlp.StopwordAnnotator;

public class TextPreprocessor {
	private StanfordCoreNLP pipeline;
	
	public TextPreprocessor() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
//		props.setProperty("customAnnotatorClass.stopword", "intoxicant.analytics.coreNlp.StopwordAnnotator");
		props.setProperty("threads", "4");
		props.setProperty("ner.useSUTime", "false");
		props.setProperty("tokenize.options", "untokenizable=noneDelete");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	public List<String> process(String content) {
		ArrayList<String> res = new ArrayList<String>();
		Annotation document = new Annotation(content);
		
		pipeline.annotate(document);
		
//		List<String> keywords = new ArrayList<String>(); 
//		int j =0;
//		for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)) {
//			
//
//			
//			
//			SemanticGraph deps = (sentence.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class));
//			System.out.println(deps);
//			Collection<IndexedWord> roots = deps.getRoots();
//			for(IndexedWord root : roots) {
//				
//				Collection<IndexedWord> subjs = new ArrayList<IndexedWord>(); 
//				Collection<IndexedWord> objs = new ArrayList<IndexedWord>();
//				for (IndexedWord child: deps.getChildren(root)) {
//					if(deps.reln(root, child).toString() == "nsubj") {
//						subjs.add(child);
//					}
//					if (deps.reln(root, child).toString() == "dobj"){
//						objs.add(child);
//					}
//				}
//				for(IndexedWord subj: subjs) {
//					for(IndexedWord obj: objs) {
//						System.out.println(root.word() + " &-> " + subj.word() + " &-> " + obj.word());
//					}
//				}
//
//			}
//			
//			
//
//			j+=1;
//			System.out.println(j);
//			List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
////			labels = filterStopwords(labels);
//			System.out.println("Entities" + Arrays.toString(groupNamedEntities(labels).toArray()));
//
//			
//
//			for(CoreLabel label: labels) {
//				String pos = label.get(PartOfSpeechAnnotation.class);
//				System.out.println(sentence + " : " + label.word() + " : " + pos);
//			}
//			
//			
//			
//			
//
//			// Looking for Adjective Noun [JJ, NN] [RB,VB] [JJ, VB] [VB, NN]
//			List<String> labelsPos = new ArrayList<String>();
//			for(CoreLabel label: labels) {
//				labelsPos.add(label.get(PartOfSpeechAnnotation.class));
//			};
//			
//			for (int i = 0; i < labels.size()-1; i++) {
//				if(labelsPos.get(i).startsWith("JJ") && labelsPos.get(i+1).startsWith("NN")) {
//					keywords.add(labels.get(i).word()+"_"+labels.get(i+1).word());
//				}
//			}
//
//			
//		}
//		System.out.println("Sentence keywords: " +keywords.size() + " : " + Arrays.toString(keywords.toArray()));
//		labels
//		
//		
//		List<String> words = getPosEntities(labels);
//		
////		List<String> words = groupNamedEntities(labels);
//		ArrayList<Integer> sizes = new ArrayList<Integer>(Arrays.asList(1));
//		res.addAll(getNGrams(words, sizes));
		List<CoreLabel> labels = document.get(CoreAnnotations.TokensAnnotation.class);
		
//		labels = filterStopwords(labels);
		
		List<String> words = groupNamedEntities(labels);
		System.out.println(words);
		return res;
	}
	
//	private List<CoreLabel> filterStopwords(List<CoreLabel> labels){
//		List<CoreLabel> filtered = new ArrayList<CoreLabel>();
//		for (CoreLabel label: labels) {
//			if (!label.get(StopwordAnnotator.class).first()) {
//				filtered.add(label);
//			}	
//		}
//		return filtered;
//	}
	
	private List<String> getPosEntities(List<CoreLabel> labels){
		List<String> res = new ArrayList<String>(); 
		for(CoreLabel label: labels) {
			String pos = label.get(PartOfSpeechAnnotation.class);
			System.out.println(label.word() + ":" + pos);
			res.add(label.word());
		}
		return res;
	}
	
	private List<String> groupNamedEntities(List<CoreLabel> labels){
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
	
	private List<String> getNGrams(List<String> words, List<Integer> sizes){
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