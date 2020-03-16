package assignment3;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class WordCloudGenerator {
	String outputFolder;
	
	public WordCloudGenerator(String outputFolderPath) {
		outputFolder = outputFolderPath;
	}
	
	public void createWordCloud(HashMap<String, Integer> freqCount, int topn, String outputFileName) {
		try {
        	final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            final List<WordFrequency> wordFrequencies = new ArrayList<WordFrequency>();
            for (Frequency freq: FrequencyStats.getTopFrequencyObjects(freqCount, topn)) {
            	if(freq != null) {
            		wordFrequencies.add(new WordFrequency((String)freq.getKey(), freq.getFreq()));	
            	}
            }
            final Dimension dimension = new Dimension(600, 600);
            final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setPadding(2);
            wordCloud.setBackground(new CircleBackground(300));
            wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
            wordCloud.setFontScalar(new SqrtFontScalar(10, 60));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(outputFolder + "/" + outputFileName + ".png");
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	

}
