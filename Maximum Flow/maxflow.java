import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * class maxflow
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
public class maxflow {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int m = sc.nextInt();
		
		FlowGraph fg = new FlowGraph(n);
		
		for (int i = 0; i < m; i++) {
			fg.addEdge(sc.nextInt(), sc.nextInt(), sc.nextInt());
		}
		
		fg.findFlow();
		
		System.out.println(fg.totalFlow());
		fg.edgesFlow();
		
		sc.close();
	}
}

/**
 * class FlowGraph for storing th graph representation and finding the max flow
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
class FlowGraph {
	/**
	 * number of vertices
	 */
	private int V;
	/**
	 * Edges weights
	 */
	private int[][] E;
	/**
	 * Back edges (residual graph)
	 */
	private int[][] BE;
	/**
	 * source vertex
	 */
	private int source = 0;
	/**
	 * sink vertex
	 */
	private int sink = 1;
	/**
	 * stores predecessor vertices
	 */
	private int[] pred;
	/**
	 * array for storing the color of the vertices while traversing via BFS
	 */
	private int[] visited;
	/**
	 * order of the edges requried for printing flow at the end
	 */
	private ArrayList<Edge> OE;
	
	public FlowGraph(int v) {
		this.V = v;
		E = new int[V][V];
		BE = new int[V][V];
		OE = new ArrayList<Edge>();
		
		
		for (int i = 0; i < V; i++) {
			Arrays.fill(E[i], 0);
			Arrays.fill(BE[i], 0);
		}
	}
	
	/**
	 * adds the edge betwen two vertices. Also stores it in the ordered edges list (OE)
	 * @param v1 - vertex 1
	 * @param v2 - vertex 2
	 * @param w - weight of the edge
	 */
	public void addEdge(int v1, int v2, int w) {
		E[v1][v2] = w;
		OE.add(new Edge(v1, v2));
	}
	
	/**
	 * @return the total maximum flow from source to sink 
	 */
	public int totalFlow() {
		int sum = 0;
		for (int i = 0; i < V; i++) {
			sum += BE[source][i];
		}
		return sum;
	}
	
	/**
	 * prints the edge flow for each edge in the order of input
	 */
	public void edgesFlow() {
		for (int i = 0; i < OE.size(); i++) {
			Edge e = OE.get(i);
			System.out.println(e.v1+" " +e.v2+" "+BE[e.v1][e.v2]);
		}
	}
	
	/**
	 * runs the Edmond-Karp algorithm for finding maximum flow
	 */
	public void findFlow() {
		runBFS();
		
		while (pred[sink] != -1) {
			int m = findBottleNeck();
			updateFlow(m);
			runBFS();
		}
	}
	
	/**
	 * runs DFS, populates the predecessor array.
	 */
	private void runBFS() {
		pred = new int[V];
		visited = new int[V];
		Arrays.fill(pred, -1);
		Arrays.fill(visited, 0);
		
		Queue q = new Queue();
		
		q.enQueue(source);
		visited[source] = 1;
		
		
		while (!q.isEmpty()) {
			int c = q.deQueue();
			for (int i = 0; i < V; i++) {
				if ((E[c][i]-BE[c][i]) != 0 && visited[i] == 0) {
					visited[i] = 1;
					pred[i] = c;
					q.enQueue(i);
				}
			}
			visited[c] = 2;
		}
	}
	
	/**
	 * given the flow, uses the predecessor array to update the flow
	 * from sink to source
	 * @param m - the weight to be added
	 */
	private void updateFlow(int m) {
		int p = sink;
		while(pred[p] != -1) {
			BE[pred[p]][p] += m;
			p = pred[p];
		}
	}
	
	/**
	 * finds the bottleneck weight from the augmenting path
	 * @return
	 */
	private int findBottleNeck() {
		int p = sink;
		int min = Integer.MAX_VALUE; 
		while (pred[p] != -1) {
			if (min > (E[pred[p]][p]-BE[pred[p]][p])) {
				min = (E[pred[p]][p]-BE[pred[p]][p]);
			}
			p = pred[p];
		}
		if (min == Integer.MAX_VALUE)
			return -1;
		return min;
	}
	
}

/**
 * class Queue used for BFS
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
class Queue {
	private ArrayList<Integer> q;
	
	public Queue() {
		q = new ArrayList<Integer>();
	}
	
	public void enQueue(int a) {
		q.add(a);
	}
	
	public int deQueue() {
		if (q.isEmpty()) {
			return -1;
		}
		int r = q.get(0);
		q.remove(0);
		return r;
	}
	public boolean isEmpty() {
		return q.isEmpty();
	}
}

/**
 * class Edge. Used for storing the edges.
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
class Edge {
	int v1;
	int v2;
	
	public Edge(int a, int b) {
		v1 = a;
		v2 = b;
	}
}