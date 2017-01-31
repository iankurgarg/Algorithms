import java.util.Scanner;
/**
 * Class mult.java for solving the matrix multiplication problem
 * @author Ankur Garg, agarg12@ncsu.edu, 200157990
 *
 */
public class mult {
	/**
	 * -Variables length and width store the length and width of the matrix with index i
	 * -Variable  subp stores the optimal cost for multiplying matrices from i to j (subproblem for DP)
	 * -Since we need only the upper half of the matrix, lower half of the matrix is used to store the 
	 * corresponding k for which the cost was optimal while calculating optimal cost for matrices from i to j 
	 */
	private int[] length;
	private int[] width;
	private int[][] subp;
	int n;
	private String output = "";

	/***
	 * Constructor. Initializes the length, width and subp variables.
	 * @param m - the number of matrices to be multiplied
	 */
	public mult(int m) {
		length = new int[m];
		width = new int[m];
		subp = new int[m][m];
		n = m;
	}
	
	/***
	 * Set for storing the length of the matrix i, which is also width of matrix i-1
	 * @param i - index of matrix
	 * @param l - length of the matrix
	 */
	public void set(int i, int l){
		if (i == 0) {
			length[i] = l;
		}
		else if (i < length.length) { 
			length[i] = l;
			width[i-1] = l;
		}
		else {
			width[i-1] = l; 
		}
	}
	
	/***
	 * solves the optimal multiplication problem using dp.
	 */
	public void solve() {
		int x, y;
		int min;
		int mink;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n && j+i < n; j++) {
				// Here x, y are such that we are traversing the matrix diagonally.
				x = j;
				y = j+i;
				if (x == y) {
					subp[x][y] = 0;
				}
				else {
					min = subp[x][x] + subp[x+1][y] + length[x]*width[x]*width[y];
					mink = x;
					//For loop to try all possible values of k and choose the one with min cost.
					for (int k = x; k < y; k++) {
						if ((subp[x][k] + subp[k+1][y] + length[x]*width[k]*width[y]) < min) {
							min = subp[x][k] + subp[k+1][y] + length[x]*width[k]*width[y];
							mink = k;
						}
					}
					//Store the min cost at index [x][y] and corresponding k value for future use at [y][x]
					subp[x][y] = min;
					subp[y][x] = mink;
				}
			}
		}
	}
	
	/**
	 * function for printing the output in desired format.
	 */
	public void printSolution() {
		print(0, n-1);
		output = output.trim().replaceAll(" +"," ");
		System.out.println(output);
	}
	
	/**
	 * internal function for recursively printing the output in desired format from matrix i to j
	 * @param i starting index of matrices
	 * @param j ending index of matrices
	 */
	private void print(int i, int j) {
		
		if (i == j) {
			output += " M"+(i+1)+" ";
		}
		else if (i == 0 && j == n-1){
			int k = subp[j][i];
			print(i,k);
			output += " * ";
			print(k+1,j);
		}
		else {
			int k = subp[j][i];
			output += " ( ";
			print(i,k);
			output += " * ";
			print(k+1,j);
			output += " ) ";
		}
	}
	
	/**
	 * returns the output in string format.
	 */
	public String toString() {
		print(0,n-1);
		output = output.trim().replaceAll(" +"," ");
		return output;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		mult m = new mult(n);
		
		for(int i = 0; i < n+1; i++) {
			m.set(i, sc.nextInt());
		}
		
		m.solve();
		
		System.out.println(m.toString());
		
		sc.close();
	}
	
}
