/*
Author: Ankur Garg (Student ID# 200157990)

The program takes input 2 strings and calculates the total number of
distinct substrings between the two strings.
To do that, it creates all possible distinct substrings of first string
and checks if that substring is present in the second string.
*/
import java.util.Scanner;

public class common {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str1 = sc.next();
		String str2 = sc.next();
		
		int count = CountCommonSubstrings(str1, str2);
		System.out.println(count);
		
		// Close the Scanner
		sc.close();
	}
	// Function to count the number of common substrings
	private static int CountCommonSubstrings(String str1, String str2) {
		// Count which will store the total count of common substrings
		int count = 0;
		for (int i = 0; i < str1.length(); i++)
			for (int j = i+1; j < str1.length() + 1; j++) {
				// Generates the substring for pair of indices i,j
				String temp = str1.substring(i, j);
				// Checks if this substring occured previously in str1.
				// If yes, skip, as we have already processed it
				if (str1.indexOf(temp) < i)
					continue;
				// Checks if the str2 contains this substring.
				// If yes, increase the count by 1
				if (str2.contains(temp))
					count++;
			}
		// Returns the total count
		return count;
	}

}
