package assignment3;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;

import com.opencsv.CSVReader;


/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
//
//		
//		HashTag
//		TextPreprocessor preprocessor = new TextPreprocessor();

//		ArrayList<String> lines = FileOperations.readFileAsLines("/Users/coderpc/Class/BDS/trial.csv");
		
		
		ArrayList<String> positives = new ArrayList<String>();
		ArrayList<String> negatives = new ArrayList<String>();
		ArrayList<String> allTweets = new ArrayList<String>();
		
		CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader("/Users/coderpc/Class/BDS/trial.csv"));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
            	int sentiment = Integer.parseInt(line[0]);
            	String tweetText = DataCleaningUtils.runDataCleaners(line[5]);
            	if(sentiment == 0) negatives.add(tweetText);
            	if(sentiment == 4) positives.add(tweetText);
            	allTweets.add(tweetText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // START - DATA EXPLORATION
        
        PrefixedWordStats hashtagStats = new PrefixedWordStats("#");
        PrefixedWordStats mentionStats = new PrefixedWordStats("@");
    	hashtagStats.processAll(allTweets);
    	mentionStats.processAll(allTweets);
        System.out.println(Arrays.toString(hashtagStats.getTopFrequencyWords(5)));
        System.out.println(Arrays.toString(mentionStats.getTopFrequencyWords(5)));

        // END - DATA EXPLORATION
        
        
        
        // START - MOST COMMON 1,2,3,4-GRAMS
        
        NGramAnalyser nGramAnalyser = new NGramAnalyser();
        nGramAnalyser.analyseAll(allTweets);
        
        // END - MOST COMMON 1,2,3,4-GRAMS
        
        
        
        // START - MOST COMMON noun-GRAMS
        
        nGramAnalyser.analyseAllOnlyNouns(allTweets);
        
        // END - MOST COMMON noun-GRAMS
        
        
        
        
        
//		StringBuilder allNegatives = new StringBuilder();
//		for(String sentence: negatives) {
//			allNegatives.append(sentence);
//			allNegatives.append(". ");
//		}
//		String allNegativesStr = allNegatives.toString();
//		
//		System.out.println(allNegativesStr.length());
//        TextPreprocessor preprocessor = new TextPreprocessor();
//        preprocessor.process(allNegativesStr);
        
        
        

		
//		try {
//			String sentFragModel = "src/main/resources/en-sent.bin";
//			DefaultDocProcessor dp =new DefaultDocProcessor(sentFragModel);
//			LexicalChainingSummarizer lcs = new LexicalChainingSummarizer(dp,"src/main/resources/en-pos-maxent.bin");
//
//			List<Sentence> sent = dp.getSentencesFromStr(allNegativesStr);
//			List<LexicalChain> vh = lcs.buildLexicalChains(allNegativesStr, sent);
//			LexChainingKeywordExtractor ke = new LexChainingKeywordExtractor();
//			List<String> keywords = ke.getKeywords(vh, 5);
//			System.out.println(keywords);
//	        	
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
	}
}
