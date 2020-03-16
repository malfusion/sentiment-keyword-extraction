package assignment3;


import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.opencsv.CSVReader;


public class App {
	
	public static void main(String[] args) {
		
		String filePath = args[0];
		String outputFilepath = args[1];
		
		WordCloudGenerator wordcloud = new WordCloudGenerator(outputFilepath);
		
		
		ArrayList<String> allContent = new ArrayList<String>();
		
		CSVReader reader = null;
		File file = new File(filePath); 
	    Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
            	String cleanedLine= DataCleaningUtils.runDataCleaners(line);
            	allContent.add(cleanedLine);
		    }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
        // START - DATA EXPLORATION
        
        PrefixedWordStats hashtagStats = new PrefixedWordStats("#");
        PrefixedWordStats mentionStats = new PrefixedWordStats("@");
    	hashtagStats.processAll(allContent);
    	mentionStats.processAll(allContent);
        System.out.println("Top 20 Hashtags: " + Arrays.toString(hashtagStats.getTopFrequencyWords(20)));
        System.out.println("Top 20 Mentions: " + Arrays.toString(mentionStats.getTopFrequencyWords(20)));
        
        wordcloud.createWordCloud(hashtagStats.getFrequencyMap(), 50, "hashtags");
        wordcloud.createWordCloud(mentionStats.getFrequencyMap(), 50, "mentions");
        
        
        // END - DATA EXPLORATION
        
        
        // START - MOST COMMON 1,2,3,4-GRAMS

        NGramAnalyser nGramAnalyser = new NGramAnalyser();
        nGramAnalyser.analyseAll(allContent);
        
        // END - MOST COMMON 1,2,3,4-GRAMS
        
        
        // START - MOST COMMON noun-GRAMS
        
        nGramAnalyser.analyseAllOnlyNouns(allContent);
        
        // END - MOST COMMON noun-GRAMS
        
        
        // START - POS, DEPENDENCY PARSING, NER
        
        KeywordAnalyser keywordAnalyser = new KeywordAnalyser();
        keywordAnalyser.analyseAll(allContent);
        wordcloud.createWordCloud(keywordAnalyser.getPosFrequencyMap(), 50, "pos");
        wordcloud.createWordCloud(keywordAnalyser.getDepFrequencyMap(), 50, "deps");
        wordcloud.createWordCloud(keywordAnalyser.getNerFrequencyMap(), 50, "ner");
        
        // END - POS, DEPENDENCY PARSING, NER
	}
}
