package assignment3;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;


public class App {
	
	public static void main(String[] args) {

		
		
		ArrayList<String> positives = new ArrayList<String>();
		ArrayList<String> negatives = new ArrayList<String>();
		ArrayList<String> allTweets = new ArrayList<String>();
		
		CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader("/Users/coderpc/Class/BDS/sentiment140.csv"));
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
        System.out.println("Top 20 Hashtags: " + Arrays.toString(hashtagStats.getTopFrequencyWords(20)));
        System.out.println("Top 20 Mentions: " + Arrays.toString(mentionStats.getTopFrequencyWords(20)));

        // END - DATA EXPLORATION
        
        
        // START - MOST COMMON 1,2,3,4-GRAMS

        NGramAnalyser nGramAnalyser = new NGramAnalyser();
        nGramAnalyser.analyseAll(allTweets);
        
        // END - MOST COMMON 1,2,3,4-GRAMS
        
        
        // START - MOST COMMON noun-GRAMS
        
        nGramAnalyser.analyseAllOnlyNouns(allTweets);
        
        // END - MOST COMMON noun-GRAMS
        
        
        // START - POS, DEPENDENCY PARSING, NER
        
        KeywordAnalyser keywordAnalyser = new KeywordAnalyser();
        keywordAnalyser.analyseAll(allTweets);
        
        // END - POS, DEPENDENCY PARSING, NER
	}
}
