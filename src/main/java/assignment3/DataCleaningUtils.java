package assignment3;

public class DataCleaningUtils {

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
