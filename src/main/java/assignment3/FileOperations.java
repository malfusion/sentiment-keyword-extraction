package assignment3;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOperations
{
	public static String readFile(String filepath) {
		String res = "";
		File file = new File(filepath); 
	    Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				res += sc.nextLine();
		    }
			return res;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static ArrayList<String> readFileAsLines(String filepath) {
		ArrayList<String> res = new ArrayList<String>();
		File file = new File(filepath); 
	    Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				res.add(sc.nextLine());
		    }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return res;
	}
	
	public static List<String> getFilesInFolder(String path){
		File folder = new File(path);
		ArrayList<String> files = new ArrayList<String>();
		if (folder.isDirectory()) {
			for (File file: folder.listFiles()) {
		            files.add(file.getAbsolutePath());
	        }
	    }
		return files;
	}
}
