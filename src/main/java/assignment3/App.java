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
		
		CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader("/Users/coderpc/Class/BDS/prefixtest.csv"));
            String[] line;
            while ((line = reader.readNext()) != null) {
            	int sentiment = Integer.parseInt(line[0]);
            	String text = line[5];
            	if(sentiment == 0) {
					text = DataCleaningUtils.removeEndLine(text);
            		text = DataCleaningUtils.cleanPrefixes(text, "@"); 
					text = DataCleaningUtils.cleanPrefixes(text, "#");
					text = DataCleaningUtils.removeUrls(text);
            		text = DataCleaningUtils.cleanPunctuations(text);
            		text = DataCleaningUtils.cleanSpaces(text);
            		negatives.add(text);
            	}
            	else if(sentiment == 4) {
					text = DataCleaningUtils.removeEndLine(text);
            		text = DataCleaningUtils.cleanPrefixes(text, "@"); 
					text = DataCleaningUtils.cleanPrefixes(text, "#");
					text = DataCleaningUtils.removeUrls(text);
            		text = DataCleaningUtils.cleanPunctuations(text);
            		text = DataCleaningUtils.cleanSpaces(text);
            		positives.add(text);
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Positive lines: " + positives.size());
        System.out.println("Negative lines: " + negatives.size());
        
//        PrefixedWordStats hashtagStats = new PrefixedWordStats("#");
//        PrefixedWordStats mentionStats = new PrefixedWordStats("@");
//        for(String line: negatives) {
//        	hashtagStats.process(line);
//        	mentionStats.process(line);
//        }
//        
//        System.out.println(Arrays.toString(hashtagStats.getTopFrequencyWords(5)));
//        System.out.println(Arrays.toString(mentionStats.getTopFrequencyWords(5)));
//        
		StringBuilder allNegatives = new StringBuilder();
		for(String sentence: negatives) {
			allNegatives.append(sentence);
			allNegatives.append(". ");
		}
		String allNegativesStr = allNegatives.toString();
		
		System.out.println(allNegativesStr.length());
        TextPreprocessor preprocessor = new TextPreprocessor();
        preprocessor.process(allNegativesStr);
        
        
        

		
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
