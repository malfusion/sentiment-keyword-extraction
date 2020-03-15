package assignment3;

public class DataCleaningUtils {
	
	public static String runDataCleaners(String str) {
		String text = DataCleaningUtils.removeNumbers(str);
		text = DataCleaningUtils.removeEndLine(text);
		text = DataCleaningUtils.cleanPrefixes(text, "@"); 
		text = DataCleaningUtils.cleanPrefixes(text, "#");
		text = DataCleaningUtils.removeUrls(text);
		text = DataCleaningUtils.cleanPunctuations(text);
		text = DataCleaningUtils.cleanSpaces(text);
		return text;
	}

	public static String removeNumbers(String str) {
		return str.replaceAll("[0-9]", "");
	}

	public static String removeEndLine(String str) {
		return str.replaceAll("\\r|\\n|\\t", " ") + " ";
	}
	
	public static String cleanPrefixes(String str, String prefix) {
		return str.replace(prefix + " ", prefix);
	}
	
	public static String cleanPunctuations(String str) {
		return str.replaceAll("/[!?;:-]/", ".").replaceAll("\\.+", ".").replaceAll("[\\p{Punct}&&[^@#.]]", "");

	}
	
	public static String cleanSpaces(String str) {
		return str.replaceAll("\\s+"," ");
	}
	
	public static String removeUrls(String str) {
		return str.replaceAll("http.*?\\s", "").replaceAll("www\\..*?\\s", "");
	}
	
	

}
