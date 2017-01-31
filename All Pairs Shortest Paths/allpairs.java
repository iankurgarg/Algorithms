import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class allpairs
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
public class allpairs {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		Graph g = new Graph();
		
		
		for(int i = 0; i < n; i++) {
			g.addVertex(sc.next());
		}
		
		g.populateEdges();

		g.runFlyodWarshal();
		
		System.out.println(g.calculateAverageEdges());

		int q = sc.nextInt();
		
		for (int i = 0; i < q; i++) {
			System.out.println(g.findPath(sc.next(), sc.next()));	
		}
		
		sc.close();
	}
}

/**
 * class graph for storing the vertex and edge information
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
class Graph {
	/**
	 * Hashmaps for converting the string nodes to integer nodes and vice-versa.
	 */
	HashMap<Integer, String> vertices;
	HashMap<String, Integer> revV;
	/**
	 * Number of vertices
	 */
	int V;
	/**
	 * Edge matrix
	 */
	int[][] E;
	/**
	 * shortest path weights
	 */
	int[][] W;
	/**
	 * corresponding K values for the shortest paths
	 */
	int[][] K;
	public Graph() {
		V = 0;
		vertices = new HashMap<Integer, String>();
		revV = new HashMap<String, Integer>();
	}
	
	/**
	 * add the vertex in the hashamps
	 * @param node - the string value of the node
	 */
	public void addVertex(String node) {
		vertices.put(V, node);
		revV.put(node, V++);
	}
	
	/**
	 * populates the edges based on the rule given in the question
	 * compares to string and if only one character is different, the corresponding
	 * difference in the ascii values of that character is the weight of the edge
	 * between the two nodes.
	 */
	public void populateEdges() {
		if (V != 0) {
			E = new int[V][V];
			W = new int[V][V];
			for (int i = 0; i < V; i++) {
				for (int j = i+1; j < V; j++) {
					int r = compare(vertices.get(i), vertices.get(j));
					E[i][j] = r;
					E[j][i] = r;
				}
			}
		}
	}
	
	/**
	 * compares two strings.
	 * @param str1
	 * @param str2
	 * @return the index of the only character where the two strings differ.
	 * returns -1 if the strings differ at more than one character index.
	 */
	private int compare(String str1, String str2) {
		int a = 0;
		int ai = -1;
		if (str1.length() != str2.length())
			return -1;
		else {
			for (int i = 0; i < str1.length(); i++) {
				if (str1.charAt(i) != str2.charAt(i)) {
					a++;
					ai = i;
				}
			}
			
			if (a == 1) {
				return Math.abs(str1.charAt(ai) - str2.charAt(ai));
			}
			else if (a == 0)
				return 0;
			else
				return -1;
		}
	}
	
	/**
	 * Runs Floyd Warshal algorithm for finding minimum paths between all pairs of vertices
	 * Stores the weights in the array W;
	 * Store the corresponding K values in the array K;
	 */
	public void runFlyodWarshal() {
		K = new int[V][V];
		for (int i = 0; i < V; i++) {
			Arrays.fill(K[i], -1);
			for (int j = 0; j < V; j++)
				W[i][j] = E[i][j];
		}
		for (int k = 0; k < V; k++) {
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < V; j++) {
					if (i != j && k != i && k != j) {
						if (W[i][k] != -1 && W[k][j] != -1) {
							int s = W[i][k] + W[k][j];
							if (W[i][j] == -1 || W[i][j] > s) {
								W[i][j] = s;
								// The Extra Credit Part. The corresponding k value is stored in the array K
								K[i][j] = k;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * function for recursively calling itself for finding shortest paths between current
	 * nodes and the k value of the corresponding shortest path.
	 * @param v1
	 * @param v2
	 * @return
	 */
	private String findPath(int v1, int v2) {
		// For Extra Credit, the K value stored in the array 'K', is used for calculating the path
		if (E[v1][v2] == W[v1][v2] && E[v1][v2] > 0) {
			return vertices.get(v1) +" ";
		}
		int k = K[v1][v2];
		return findPath(v1, k) + findPath(k, v2);
	}
	
	/**
	 * Given two string nodes returns the string containing the length as well as 
	 * the shortest path between the two nodes. 
	 * @param v1 - the string value of the first node
	 * @param v2 - the string value of the second node
	 * @return output string
	 */
	public String findPath(String node1, String node2) {
		int v1 = revV.get(node1);
		int v2 = revV.get(node2);
		
		if (W[v1][v2] == -1) {
			return node1 + " " + node2 +" not reachable";
		}
		else {
			return W[v1][v2]+" "+ findPath(v1, v2) + node2;
		}
	}
	
	/**
	 * Calculates the average number of vertices for each vertex which are connected to
	 * the vertex via some shortest path. (required in question)
	 * @return - the average value rounded off to 2 decimal palces.
	 */
	public float calculateAverageEdges() {
		int sum = 0;
		for(int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				if (W[i][j] >= 0)
					sum++;
			}
		}
		float res = (float)sum/V;
		
		BigDecimal bd = new BigDecimal(Float.toString(res));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return bd.floatValue();
	}
}