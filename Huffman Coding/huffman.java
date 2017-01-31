import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class huffman {
	static Node[] N = new Node[257];
	public static void main (String[] args) throws Exception {
		
		InputStream is = System.in;
		heapK<Node> h = new heapK<Node>(true, 2, new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				if (o1.freq() == o2.freq())
					return 0;
				else if (o1.freq() < o2.freq())
					return -1;
				else
					return 1;
			}
		});
		
		for(int i = 0; i <= 256; i++) {
			N[i] = new Node(i, 1);
		}
		
		while(true) {
			int b = is.read();
			if (b == -1)
				break;
			else {
				N[b].incrementFreq();
			}
		}
				
		for (int i = 0;i <= 256; i++) {
			h.insert(N[i]);
		}
		
		while(h.size() >= 2) {
			Node x = h.removeTop();
			Node y = h.removeTop();
			
			Node t = new Node(-100, x.freq()+y.freq());
			t.left = x;
			t.right = y;
			h.insert(t);
		}
		
		Node e = h.removeTop();
		traverse(e, "");
		
		for (int i = 0; i < N.length; i++) {
			System.out.println(N[i].toString());
		}
		
		
		
		is.close();
	}
	
	public static void traverse(Node e, String prefix) {
		if (e.left == null && e.right == null) {
			N[e.value()].code = prefix; 
		}
		if (e.left != null) {
			traverse(e.left, prefix+"0");
		}
		if (e.right != null) {
			traverse(e.right, prefix+"1");
		}
	}
}

class Node {
	private int value;
	private String val;
	private int freq;
	public String code;
	public Node left;
	public Node right;
	
	
	public Node(int b, int freq) {
		this.value = b;
		if (value >= 33 && value <= 126) {
			val = ((char) value)+"";
		}
		else if (value != 256){
			val = String.format("%02X", value);
		}
		else {
			val = "EOF";
		}
		this.freq = freq;
		left = null;
		right = null;
	}
	
	public int value() {
		return value;
	}
	
	public int freq() {
		return freq;
	}
	
	public void incrementFreq() {
		freq++;
	}
	
	public String toString(){
		if (val.length() == 1) 
			return "  "+val+" "+code;
		else if(val.length() == 2)
			return " "+val +" "+code;
		else
			return val+" "+code;
	}
}

/**
 * The class heapK. 
 * The class has been implemented for a general heap with k children and for both min-heap and max-heap styles
 * Its constructor takes input the k(Number of chilren at each node) and mintype boolean.
 * minType = true means, the type of the heap is min-heap, else type of the heap is Max-Heap.
 * @author Ankur Garg, agarg12@ncsu.edu, student-id:200157990
 *
 */
class heapK<T> {
	/**
	 * The ArrayList data structure is used for storing the actual data of the heap.
	 */
	private ArrayList<T> heap_data;
	/**
	 * The number of children of each node.
	 */
	private int k;
	/**
	 * Type of the heap (min or max)
	 */
	private boolean mintype;
	private Comparator<T> comp;
	
	public heapK(Comparator<T> c) {
		k = 2;
		mintype = true;
		heap_data = new ArrayList<T>();
		this.comp = c;
	}
	
	public heapK(boolean mintype, int K, Comparator<T> c) {
		k = K;
		this.mintype = mintype;
		heap_data = new ArrayList<T>();
		this.comp = c;
	}
	
	/**
	 * insert() function. Takes input the integer to be inserted into the list.
	 * @param i - the integer to be inserted into the list
	 */
	public void insert(T i) {
		heap_data.add(i);
		PercolateUp(heap_data.size()-1);
	}
	
	/**
	 * The removeTop() function. Removed the min (or max) element from the heap and re-organizes the heap. 
	 * @return the min(or max) - element that has been removed, 
	 * @throws Exception - if the function is called for empty heap. 
	 */
	public T removeTop() throws Exception {
		T result;
		if (heap_data.size() == 0)
			throw new Exception("Can't remove top from Empty Heap");
		else if (heap_data.size() ==1) {
			result = heap_data.get(0);
			heap_data.remove(0);
		}
		else {
			result = heap_data.get(0);
			heap_data.set(0, heap_data.get(heap_data.size()-1));
			heap_data.remove(heap_data.size()-1);
			Heapify(0);
		}
		return result;
	}
	
	/**
	 * The Heapify() function at index i Percolates down the element at index i if the element is out
	 * of position in the heap. 
	 * @param i - the index of the element which is out of position in the heap.
	 */
	private void Heapify(int i) {
		T temp;
		int tchild = TopChild(i);
		if (tchild == -1)
			return;
		int index = i;
		while (compare(heap_data.get(tchild), heap_data.get(index))) {
			temp = heap_data.get(tchild);
			heap_data.set(tchild, heap_data.get(index));
			heap_data.set(index, temp);
			index = tchild;
			tchild = TopChild(index);
			if (tchild == -1)
				break;
		}
	}
	
	/**
	 * Returns the index of the child of i which is biggest or smallest out of all the children.
	 * Biggest or Smallest based on whether the heap is min or max.
	 * @param i
	 * @return
	 */
	private int TopChild(int i) {
		int fc = firstChild(i);
		if (fc == -1)
			return -1;
		int index = fc;
		T top = heap_data.get(fc);
		
		for (int j = 1; j < k && fc+j < heap_data.size(); j++) {
			if (compare(heap_data.get(fc+j), top)) {
				index = fc+j;
				top = heap_data.get(fc+j);
			}
		}
		return index;
	}
	
	/**
	 * PercolateUp() function at index i, moves the element at index i up the heap where it fits
	 * the conditions of the heap.
	 * @param i - the index which needs to be moved up to the proper position.
	 */
	public void PercolateUp(int i) {
		int index = i;
		T temp;
		int p = parent(index);
		if (p == -1)
			return;
		
		while (compare(heap_data.get(index), heap_data.get(p))) {
			temp = heap_data.get(index);
			heap_data.set(index, heap_data.get(p));
			heap_data.set(p, temp);
			index = p;
			p = parent(index);
			if (p == -1)
				break;
		}
		return;
	}
	
	/**
	 * function compare(). This is created to accommodate both min-type and max-type of the heap.
	 * @param integer - First integer to be compared
	 * @param integer2 - Second integer to be compared.
	 * @return true - if the heap is min and integer < integer2 or heap is max and integer > integer2.
	 * fasle - otherwise.
	 */
	private boolean compare(T integer, T integer2) {
		if (mintype)
			return comp.compare(integer, integer2) < 0;
		else
			return comp.compare(integer, integer2) > 0;
	}
	
	/**
	 * Returns the parent of the node at index i
	 * Parent at a node i is given by: floor((i-1)/k)
	 * k is the number of children at each node of the heap. In our case, k = 3;
	 * This index is based on heap indexing starting from 0. 
	 * @param i - the index for which index of the parent is required.
	 * @return - the index of the parent.
	 */
	private int parent(int i) {
		if (i == 0)
			return -1;
		else {
			return ((i-1)/k);
		}
	}
	
	/**
	 * Returns the index of the first child of the current node.
	 * indices of the children for the index i are:
	 * i*k+1 to i*k+k.
	 * For us, k = 3.
	 * for index of the children are: 3i+1, 3i+2, 3i+3
	 * @param i - the index for which the index of the first child is required.
	 * @return - the index of the first child.
	 */
	private int firstChild(int i) {
		
		if (i*k +1 >= heap_data.size())
			return -1;
		else {
			return i*k + 1;
		}
	}
	
	public int size() {
		return heap_data.size();
	}
	
	/**
	 * Function to convert the heap to string (for output purposes)
	 */
	public String toString() {
		return heap_data.toString();
	}
}
