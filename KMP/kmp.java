import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Class kmp. Contains all the functions required for this part of the homework.
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
public class kmp {
	
	/**
	 * Variables for storing the time taken by the three algorithms.
	 */
	static long time1;
	static long time2;
	static long time3;
	
	/**
	 * variables storing the pattern and the text strings
	 */
	static String pattern = "";
	static String text = "";
	
	
	public static void main (String[] args) throws Exception {
		
		/**
		 * Checks if a file input has been given. If yes, read from file
		 * else generate text and pattern.
		 */
		if (args.length > 0) {
			Scanner sc = new Scanner(new File(args[0]));
			text = sc.nextLine();
			pattern = sc.nextLine();
			sc.close();
		}
		else {
			generateInput();
		}
		
		System.out.println("found at "+naive(pattern, text));
		System.out.println("naive search time: "+time1);
		System.out.println("found at "+inbuilt(pattern, text));
		System.out.println("standard search time: "+time2);
		System.out.println("found at "+KMP(pattern,text));
		System.out.println("kmp search time: "+time3);
		
		System.setOut(new PrintStream(new File("text.txt")));
		System.out.println(pattern);
		System.out.println(text);
		
	}
	
	/**
	 * Function for generating input.
	 * It first generates a pattern with repeated letters and then a long text string.  
	 */
	public static void generateInput() {
		char patternChar;
		
		int c = (int) (Math.random()*26) + 97;	
		patternChar = (char) c;
		
		int pattern_length = 5000;
		char[] patternArray = new char[pattern_length];
		
		for (int i = 0; i < pattern_length; i++) {
			patternArray[i] = patternChar; 
		}
			
		int text_length = 90000;
		char[] textArray = new char[text_length];
		
		int i = 0;
		while (i < text_length) {
			for (int j = 0; j < pattern_length-1 && i < text_length; j++) {
				textArray[i++] = patternArray[j];
			}
			if (i < text_length) {
				c = (int) (Math.random()*26) + 97;
				while (c == patternChar) 
					c = (int) (Math.random()*26) + 97;
				textArray[i++] = (char)c;
			}
		}
		
		pattern = new String(patternArray);
		text = new String(textArray) + pattern;
	}

	/**
	 * Function for naive algorithm. 
	 * The time taken by the function call is stored in the static time1 variable.
	 * @param pattern - the pattern to be searched
	 * @param text - text string
	 * @return - the index in the text where the pattern occurs. -1 if pattern is not found in the text
	 */
	public static int naive(String pattern, String text) {
		long start = System.currentTimeMillis();
		int i,j = 0,k;
		int result = -1;
		for (i = 0; i < text.length(); i++) {
			k = i;
			j = 0;
			while(k < text.length() && j < pattern.length()) {
				if (text.charAt(k) != pattern.charAt(j))
					break;
				j++;
				k++;
			}
			if (j == pattern.length())
				break;
		}
		
		if (i < text.length()) {
			result = i;
		}
		time1 = System.currentTimeMillis() - start;
		return result;
	}
	
	/**
	 * Function which uses the inbuilt function of java to search for substring.
	 * The time taken by the function call is stored in the static time2 variable.
	 * @param pattern
	 * @param text
	 * @return
	 */
	public static int inbuilt(String pattern, String text) {
		long start = System.currentTimeMillis();
		int i = text.indexOf(pattern);
		time2 = System.currentTimeMillis() - start;
		return i;
	}
	
	/**
	 * KMP algorithm for search substring in the text.
	 * The time taken by the function call is stored in the static time3 variable.
	 * @param pattern - pattern to be searched
	 * @param text - text string
	 * @return - the index in the text string where the substring occurs. -1 if the substring is not found.
	 */
	public static int KMP (String patternChars, String textChars) {
//		char[] patternChars = pattern.toCharArray();
//		char[] textChars = text.toCharArray();
		
		long start = System.currentTimeMillis();
		int[] P = prefix(patternChars);
		
		int result = -1;
		int plength = patternChars.length();
		int tlength = textChars.length();
		
		int q = -1;
		for (int i = 0; i < tlength; i++) {
			while(q >= 0 && patternChars.charAt(q+1) != textChars.charAt(i)) {
				q = P[q];
			}
			
			if (patternChars.charAt(q+1) == textChars.charAt(i))
				q++;
			
			if (q == plength-1) {
				result = i-plength + 1;
				break;
			}
			
		}
		time3 = System.currentTimeMillis() - start;
		return result;
	}
	
	/**
	 * Function to calculate the prefix array for the pattern
	 * @param pattern - pattern string for which the prefix array needs to be calculated
	 * @return - prefix array of size equal to the size of the pattern. 
	 */
	private static int[] prefix(String pattern) {
		int len = pattern.length();
		
		int[] P = new int[len];
		P[0] = -1;
		int k = -1;
		for (int i = 1; i < len; i++) {
			while (k >= 0 && pattern.charAt(k+1) != pattern.charAt(i)) {
				k = P[k];
			}
			if (pattern.charAt(k+1) == pattern.charAt(i)) {
				k++;
			}
			
			P[i] = k;
		}
		
		return P;
	}
}
