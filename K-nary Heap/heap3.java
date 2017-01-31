import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class heap3. 
 * The class has been implemented for a general heap with k children and for both min-heap and max-heap styles
 * Its constructor takes input the k(Number of chilren at each node) and mintype boolean.
 * minType = true means, the type of the heap is min-heap, else type of the heap is Max-Heap.
 * @author "Ankur Garg" "agarg12@ncsu.edu" "student-id:200157990"
 *
 */
public class heap3 {
	/**
	 * The ArrayList data structure is used for storing the actual data of the heap.
	 */
	ArrayList<Integer> heap_data;
	/**
	 * The number of children of each node.
	 */
	int k;
	/**
	 * Type of the heap (min or max)
	 */
	boolean mintype;
	
	public heap3() {
		k = 2;
		mintype = true;
		heap_data = new ArrayList<Integer>();
	}
	
	public heap3(boolean mintype, int K) {
		k = K;
		this.mintype = mintype;
		heap_data = new ArrayList<Integer>();
	}
	
	/**
	 * insert() function. Takes input the integer to be inserted into the list.
	 * @param i - the integer to be inserted into the list
	 */
	public void insert(int i) {
		heap_data.add(i);
		PercolateUp(heap_data.size()-1);
	}
	
	/**
	 * The removeTop() function. Removed the min (or max) element from the heap and re-organizes the heap. 
	 * @return the min(or max) - element that has been removed, 
	 * @throws Exception - if the function is called for empty heap. 
	 */
	public int removeTop() throws Exception {
		int result;
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
		int temp;
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
		int top = heap_data.get(fc);
		
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
		int temp;
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
	private boolean compare(int integer, int integer2) {
		if (mintype)
			return integer < integer2;
		else
			return integer > integer2;
	}
	
	/**
	 * Returns the parent of the node at index i
	 * Parent at a node i is given by: floor((i-1)/k)
	 * k is the number of children at each node of the heap. In our case, k = 3;
	 * This index is based on heap indexing starting from 0. 
	 * @param i - the index for which index of the parent is required.
	 * @return - the index of the parent.
	 */
	public int parent(int i) {
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
	public int firstChild(int i) {
		
		if (i*k +1 >= heap_data.size())
			return -1;
		else {
			return i*k + 1;
		}
	}
	
	/**
	 * Function to convert the heap to string (for output purposes)
	 */
	public String toString() {
		return heap_data.toString();
	}
	
	public static void main (String[] args) throws Exception {
		
		Scanner br = new Scanner((System.in));
		heap3 h = new heap3(true, 3);
		String line;
		
		while (br.hasNextLine() && !(line = br.nextLine()).equals("")) {
			String[] words = line.split(" ");
			if (words[0].equals("add"))
				h.insert(Integer.parseInt(words[1]));
			else if (words[0].equals("remove"))
				System.out.println(h.removeTop());
			else {
				br.close();
				throw new Exception("Invalid keyword - " + words[0]);
			}
		}
		br.close();
	}
}
