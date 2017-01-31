import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class for QuickSort (with bubble sort for base K elements)
 * @author Ankur Garg, agarg12@ncsu.edu, Student ID# 200157990
 *
 */
public class qsort {
	private int[] input;
	private int[] output;
	private int K;
	private boolean asec;
	
	/**
	 * Constructor. Default value of K = 3
	 * @param a - input array
	 * @param asec - true if ascending order needed, false if descending order needed.
	 */
	public qsort(int[] a, boolean asec) {
		this(a, 3, asec);
	}
	
	/**
	 * Constructor
	 * @param a - input integer array
	 * @param k - value of k
	 * @param asec - true of ascending order, false if descending order
	 */
	public qsort (int[] a, int k, boolean asec) {
		this.input = a;
		this.K = k;
		this.asec = asec;
		this.output = a;
	}
	
	/**
	 * Function for sorting the elements
	 */
	public void sort() {
		this.output = this.input;
		sortRecursive(0, output.length-1);
	}
	
	/**
	 * Private function for recursive calls.
	 * Sorts array from index i to index j, both inclusive
	 * @param i - starting index
	 * @param j - ending index
	 */
	private void sortRecursive(int i, int j) {
		if (i >= j)
			return;
		if (j-i+1 <= K) {
			bubbleSort(i, j);
		}
		else {
			int p = partition(i, j);
			if (p-1 > i)
				sortRecursive(i, p-1);
			if (j > p+1)
				sortRecursive(p+1, j);
		}
	}
	
	/**
	 * Returns pivot for array from index i to index j
	 * @param i - starting index
	 * @param j - ending index
	 * @return
	 */
	private int pivot(int i, int j) {
		//return (int)(Math.random()*(j-i)) + i;
		return j;
	}
	
	/**
	 * Partitions the array from index p to index r
	 * @param p - starting index
	 * @param r - ending index
	 * @return
	 */
	private int partition(int p, int r) {
		int t = pivot(p, r);
		if (t < p || t > r)
			System.err.println("Something wrong with partition");
		
		int temp = output[t];
		output[t] = output[r];
		output[r] = temp;
		
		int i = p-1;
		for (int j = p; j < r; j++) {
			if (compare(output[j], output[r]) || (output[j] == output[r])) {
				i++;
				temp = output[i];
				output[i] = output[j];
				output[j] = temp;
			}
		}
		temp = output[i+1];
		output[i+1] = output[r];
		output[r] = temp;
		return i+1;
	}
	
	/**
	 * compare function for handling both ascending and descending order sorts.
	 * @param i - first value to be compared
	 * @param j - second value to be compared
	 * @return
	 */
	private boolean compare(int i, int j) {
		if (asec)
			return i < j;
		else
			return i > j;
	}
	
	/**
	 * Bubble sort algorithm for sorting elements from index i to index j
	 * @param i - starting index
	 * @param j - ending index
	 */
	private void bubbleSort(int i, int j) {
		int temp;
		if (j-i+1 > K)
			System.err.println("Error: Bubble sort called for array larger than K");
		for (int k = i; k <=j; k++) {
			for (int l = k+1; l<=j; l++) {
				if (compare(output[l], output[k])) {
					temp = output[k];
					output[k] = output[l];
					output[l] = temp;
				}
			}
		}
	}
	
	/**
	 * @return Returns the sorted array
	 */
	public int[] output() {
		return output;
	}
	
	/**
	 * Returns the output array in string form.
	 */
	public String toString() {
		return Arrays.toString(output);
	}
	
	/**
	 * Prints the output in required format to the given PrintStream
	 * @param o - output print stream
	 */
	public void printOutput(PrintStream o) {
		for(int i = 0; i < output.length; i++)
			o.println(output[i]);
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> A = new ArrayList<Integer>();
		
		Scanner br = new Scanner((System.in));
		
		String line;
		while (br.hasNextLine() && !(line = br.nextLine()).equals("")) {
			A.add(Integer.parseInt(line));
		}
		br.close();
		
		int[] input = new int[A.size()];
		for (int i = 0; i < input.length; i++) {
			input[i] = A.get(i);
		}
		
		int k = 3;
		
		if (args.length != 0)
			k = Integer.parseInt(args[0]);
		
		qsort q = new qsort(input, k, true);
		long stime = getMilliseconds();	
		q.sort();
		long etime = getMilliseconds();
		q.printOutput(System.out);
		System.err.println((etime-stime));
	}
	
	/**
	 * Function for getting the Milliseconds
	 * @return
	 */
	public static long getMilliseconds() {
	    return System.currentTimeMillis();
	}
}
