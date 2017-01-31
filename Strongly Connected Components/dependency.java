import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class dependency containing the main function
 * @author Ankur Garg, agarg12@ncsu.edu
 *
 */
public class dependency {
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner (System.in);
		
		int n = sc.nextInt();
		
		Graph g = new Graph(n);
		
		for(int i = 0; i < n; i++) {
			g.setLabel(i, sc.next());
		}
		
		int m = sc.nextInt();
		
		for(int i = 0; i < m; i++) {
			g.setEdge(sc.next(), sc.next());
		}
		
		g.findDependencies();
		System.out.println(g.toString());
		
		sc.close();
	}
}

/**
 * Class Graph for storing all the graph's information and finding dependencies
 * @author Ankur Garg, agarg12@ncsu.edu
 *
 */
class Graph {
	/**
	 * Variables for storing the labels mapped to the integer values.
	 * Need bi directional mapping.
	 * First for saving edges corresponding to the string vertices
	 * Second for getting the strings back for printing.
	 */
	private HashMap<String, Integer> vertices;
	private HashMap<Integer, String> reverseMap;
	private int V;
	/**
	 * edges store the edges from node i to node j
	 * and reverse edges store the edges from node j to i
	 * as it is required later for finding strongly connected components
	 */
	private int[][] edges;
	private int[][] reverseEdges;
	/**
	 * Variable for storing the reverse finishing time for the DFS 
	 */
	private int[] reverseF;
	/**
	 * Variable for marking whether the node has been visited or not in the DFS
	 */
	private int[] visited;
	private int index;
	/**
	 * Stores the final output string to be printed.
	 */
	private String output = "";
	/**
	 * Stores the final output (list of list, each list containing the strongly connected components.
	 */
	private ArrayList<ArrayList<Integer>> list;
	private ArrayList<Integer> currentList;
	
	/**
	 * Constructor
	 * @param v - number of vertices in the graph
	 */
	public Graph(int v) {
		vertices = new HashMap<String, Integer>();
		reverseMap = new HashMap<Integer, String>();
		V =v;
		edges = new int[v][v];
		reverseEdges = new int[V][V];
		visited = new int[V];
		reverseF = new int[V];
		currentList = new ArrayList<Integer>();
		list = new ArrayList<ArrayList<Integer>>();
	}
	
	/**
	 * Stores the labels of each vertex in the maps.
	 * @param i - input index
	 * @param label - Name of the vertex
	 */
	public void setLabel(int i, String label) {
		vertices.put(label, i);
		reverseMap.put(i, label);
	}
	
	/**
	 * Stores the edges in the adjacency matrices.
	 * Stores both edges and reverse edges.
	 * @param node1 - first vertex of the edge
	 * @param node2 - second vertex of the edge
	 */
	public void setEdge(String node1, String node2) {
		edges[vertices.get(node1)][vertices.get(node2)] = 1;
		reverseEdges[vertices.get(node2)][vertices.get(node1)] = 1;
	}
	
	/**
	 * Runs DFS and finds the reverse finishing time order for the vertices
	 * Stores the result in the reverseF variable.
	 */
	private void findReverseFinishingOrder () {
		index = V;
		for (int i = 0;i < V; i++) {
			if (visited[i] == 0) {
				visited[i] = 1;
				visit(i);
				visited[i] = 2;
				index--;
				reverseF[index] = i;
			}
		}
	}
	
	/**
	 * Function used to visit each node, used by findReverseFinishingOrder for DFS
	 * @param n - Node to be visited.
	 */
	private void visit(int n) {
		for (int i = 0; i < V; i++) {
			if (edges[n][i] == 1 && visited[i] == 0) {
				visited[i] = 1;
				visit(i);
				visited[i] = 2;
				index--;
				reverseF[index] = i;
			}
		}
	}
	
	/**
	 * Function for running DFS and storing the components in the list
	 * Uses reverseEdges for the DFS
	 * @param n - Node to be visited
	 */
	private void recursiveDFS(int n) {
		for (int i = 0; i < V; i++) {
			if (reverseEdges[n][i] == 1 && visited[i] == 0) {
				visited[i] = 1;
				currentList.add(i);
				recursiveDFS(i);
				visited[i] = 2;
				
			}
		}
	}
	
	/**
	 * Function for preparing the output string.
	 * sorts the main list in the ascending order of the first element of the individual lists
	 * Then uses the reverseMap to get the actual node names and  prepares the output string.
	 * Stores the string in the variable "output"
	 */
	private void prepareString() {
		Collections.sort(list, new Comparator<ArrayList<Integer>>() {
			@Override
			public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
				if (o1.get(0) < o2.get(0))
					return -1;
				else if (o1.get(0) > o2.get(0))
					return 1;
				else
					return 0;
			}
		});
		
		
		for (int i = 0; i < list.size(); i++) {
			String out = reverseMap.get(list.get(i).get(0));
			if (list.get(i).size() > 1) {
				for (int j = 1; j < list.get(i).size(); j++) {
					out += " " + reverseMap.get(list.get(i).get(j));
				}
				output += out + "\n";
			}	
		}
	}
	
	/**
	 * Function for finding dependencies. First finds the reverse finishing time.
	 * then runs DFS using reverseEdges to find each dependency list
	 * sorts the list in the ascending order
	 * adds this list to the list of lists.
	 */
	public void findDependencies() {
		findReverseFinishingOrder();
		
		visited = new int[V];
		
		for(int j = 0; j < V; j++) {
			int i = reverseF[j];
			if (visited[i] == 0) {
				currentList = new ArrayList<Integer>();
				currentList.add(i);
				visited[i] = 1;
				recursiveDFS(i);
				visited[i] = 2;
				Collections.sort(currentList);
				list.add(currentList);
			}
		}
	}
	
	/**
	 * Calls prepare String to prepare the output string
	 * Trims the output string of any unnecessary whitespace
	 * returns the string
	 */
	public String toString() {
		prepareString();
		return output.trim().replaceAll(" +", " ");
	}
}

