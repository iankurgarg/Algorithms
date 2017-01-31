import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

/**
 * Class sweep. Contains the main function.
 * Extra Credit Implemented.
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
public class sweep {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		LineSegment[] lines = new LineSegment[n];
		
		BST bst = new BST();
		
		//Reading the input
		for(int i = 0; i < n; i++) {
			Point A = new Point(sc.nextFloat(), sc.nextFloat());
			A.setLine(i);

			Point B = new Point(sc.nextFloat(), sc.nextFloat());
			B.setLine(i);
			
			// Making sure smaller point is marked as starting point
			// and larger point is marked as end point of the line
			if (A.compareTo(B) < 0) {
				A.type = 0;
				B.type = 1;
			}
			else {
				A.type = 1;
				B.type = 0;
			}
			
			lines[i] = new LineSegment(A, B);
			
			bst.addPoint(A);
			bst.addPoint(B);
		}
		
		bst.lines = lines;
		bst.solve();
		bst.printResult();
		sc.close();
	}

}

/**
 * Class BST for solving the problem statement
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
class BST {
	/**
	 * Comparator defined for comparing 2 points based on their coordinates.
	 */
	Comparator<Point> c = new Comparator<Point>() {
		@Override
		public int compare(Point o1, Point o2) {
			int t1x = (int)(100*o1.x);
			int t1y = (int)(100*o1.y);
			int t2x = (int)(100*o2.x);
			int t2y = (int)(100*o2.y);
			if (t1x < t2x) {
				return -1;
			}
			else if (t1x == t2x && t1y < t2y)
				return -1;
			else if (t1x == t2x && t1y == t2y) {
				if (o1.line2 == -1 && o2.line2 != -1)
					return -1;
				else if (o1.line2 != -1 && o1.line2 == -1)
					return 1;
				else
					return 0;
			}
			else
				return 1;
		}
	};
	/**
	 * Priority Queue for managing points.
	 * the minimum point is taken out one at a time.
	 */
	PriorityQueue<Point> pq;
	
	LineSegment[] lines;
	/**
	 * Manages sorted list of active lines which are currently intersecting the sweep line
	 */
	ArrayList <Integer> activeLines;
	/**
	 * Set for storing the point of intersections found so far. 'Set' ensures that there are no
	 * duplicates
	 */
	Set<Point> intersections;
	/**
	 * Current Sweep line location. It is updated at various steps
	 */
	float sweepLoc;
	/**
	 * Constant used later.
	 */
	static final float SIGDIFF = 1000; 
	
	public BST () {
		pq = new PriorityQueue<Point>(10,c);
		activeLines = new ArrayList<Integer>();
	}
	
	/**
	 * Adds point to the heap (priority queue)
	 * @param p - point to be added
	 */
	public void addPoint (Point p) {
		pq.add(p);
	}
	
	/**
	 * finds the index of the given Line in the sorted array list of active lines.
	 * This is used instead of normal ArrayList.indexOf() as this implements Binary Search (log n)
	 * instead of the O(n) of the inbuilt function
	 * @param Line - line to be searched
	 * @return index of the line in the array list activeLines. -1 if Line couldn't be found
	 */
	public int find(int Line) {
		boolean found = false;
		int start = 0;
		int end = activeLines.size()-1;
		int mid = 0;
		
		while (!found) {
			mid = (start+end)/2;
			if (mid == start || mid == end)
				break;
			LineSegment m = lines[activeLines.get(mid)];
			LineSegment c = lines[Line];
			
			if (Line == activeLines.get(mid)) {
				found = true;
			}
			else if (c.compareTo(m, sweepLoc) < 0) {
				end = mid;
			}
			else if (c.compareTo(m, sweepLoc) > 0) {
				start = mid;
			}
			else {
				if (mid - 1 > start ){
					mid--;
					m = lines[activeLines.get(mid)];
					if (Line == activeLines.get(mid)) {
						found = true;
					}
					else if (c.compareTo(m, sweepLoc) < 0) {
						end = mid;
					}
					else if (c.compareTo(m, sweepLoc) > 0) {
						start = mid;
					}
				}
				else if (mid+1 < end) {
					mid++;
					m = lines[activeLines.get(mid)];
					if (Line == activeLines.get(mid)) {
						found = true;
					}
					else if (c.compareTo(m, sweepLoc) < 0) {
						end = mid;
					}
					else if (c.compareTo(m, sweepLoc) > 0) {
						start = mid;
					}
				}
				else{
					break;
				}
			}
		}
		if (found)
			return mid;
		else {
			if (Line == activeLines.get(start))
				return start;
			else if (Line == activeLines.get(end))
				return end;
			else {
				return -1;
			}
		}
	}
	
	/**
	 * Inserts the given line at appropriate location (maintains sorted order of the ArrayList
	 * Used instead of ArrayList.add() to maintain log(n) ccomplexity for each insertion 
	 * @param Line - line to be inserted
	 * @return - the index at which the line was inserted.
	 */
	public int insertActiveLine (int Line) {

		int start = 0;
		int end = activeLines.size()-1;
		int mid = 0;

		while (start <= end) {
			mid = (start+end)/2;
			if (mid == start)
				break;
			LineSegment m = lines[activeLines.get(mid)];
			LineSegment c = lines[Line];
			
			if (c.compareTo(m, sweepLoc) < 0) {
				end = mid;
			}
			else if (c.compareTo(m, sweepLoc) > 0) {
				start = mid;
			}
			else {
				activeLines.add(mid, Line);
				return mid;
			}
		}
		if (start <= end) { 
			if (lines[Line].compareTo(lines[activeLines.get(end)], sweepLoc) > 0) {
				activeLines.add(end+1, Line);
				return end+1;
			}
			else if (lines[Line].compareTo(lines[activeLines.get(start)], sweepLoc) > 0) {
				activeLines.add(start+1, Line);
				return start+1;
			}
			else {
				activeLines.add(start, Line);
				return start;
			}
		}
		else {
			activeLines.add(start, Line);
			return start;
		}
	}
	
	/**
	 * deleted the line from the activeLines array list.
	 * Assumes that line exists. Will throw exception if trying to remove a nonexisting Line
	 * @param Line - line to be removed
	 * @return - the index from which the line was removed.
	 */
	public int deleteActiveLine(int Line) {
		int i = find(Line);
		activeLines.remove(i);
		return i;
	}
	
	/**
	 * Function to handle a single point.
	 * It checks the type of the point and based on that takes appropriate actions
	 * @param p - point to be handled
	 */
	public void handle(Point p) {
		if (p.type == 0) {
			int loc = insertActiveLine(p.line);
			if (loc + 1 < activeLines.size())
				checkIntersectionBetween(loc, loc+1);
			if (loc - 1 >= 0)
				checkIntersectionBetween(loc-1, loc);
		}
		else if (p.type == 1) {
			int loc = deleteActiveLine(p.line);
			if (loc - 1 >= 0 && loc < activeLines.size())
				checkIntersectionBetween(loc-1, loc);
		}
		else if (p.type == -1) {
			int loc1 = find(p.line);
			int loc2 = find(p.line2);
			if (loc1 >= 0 && loc2 >= 0) {
				if (loc1 < loc2 && lines[p.line2].compareTo(lines[p.line], sweepLoc+SIGDIFF) < 0) {
					activeLines.set(loc1, p.line2);
					activeLines.set(loc2, p.line);
					if (loc1-1 >=0)
						checkIntersectionBetween(loc1-1, loc1);
					if (loc2+1 < activeLines.size())
						checkIntersectionBetween(loc2, loc2+1);
				}
				else if (loc1 > loc2 && lines[p.line2].compareTo(lines[p.line], sweepLoc+SIGDIFF) > 0){
					activeLines.set(loc1, p.line2);
					activeLines.set(loc2, p.line);
					if (loc1+1 < activeLines.size())
						checkIntersectionBetween(loc1, loc1+1);
					if (loc2-1 >=0)
						checkIntersectionBetween(loc2-1, loc2);
				}
			}
		}
	}
	
	/**
	 * Checks if the lines at the given locations in the activeLines array list intersect or not
	 * if they do, the intersection point is added to the result arraylist and also to
	 * the heap of points for handling. 
	 * @param x - the first index
	 * @param y - the second index
	 */
	private void checkIntersectionBetween(int x, int y) {
		int i = activeLines.get(x);
		int j = activeLines.get(y);
		if (lines[i].intersects(lines[j])) {
			Point p = lines[i].intersection(lines[j]);
			
			p.line = i;
			p.line2 = j;
			p.type = -1;
			
			if (!intersections.contains(p)) {
				pq.add(p);
				intersections.add(p);
			}
			
			
		}
		return;
	}
	
	/**
	 * Function to be called to begin the handling of the points.
	 * Make sure to have lines array updated before calling this function
	 */
	public void solve() {
		if (lines == null) {
			System.err.println("LineSegments not set");
			return;
		}
		
		intersections = new HashSet<Point>();
		Point p = pq.remove();
		sweepLoc = p.x;
		activeLines.add(p.line);
		while (!pq.isEmpty()) {
			p = pq.remove();
			if (p.x < sweepLoc) {
				System.out.println("Sweep Line moved backward");
			}
			sweepLoc = p.x;
			handle(p);
		}
	}
	
	/**
	 * Prints the intersection points in ascending order.
	 */
	public void printResult() {
//		System.out.println(intersections.size());
		Point[] ints = new Point[intersections.size()];
		ints = intersections.toArray(ints);
		Arrays.sort(ints, c);
		for (int i = 0; i < ints.length; i++) {
			System.out.println(ints[i].toString());
		}
	}
}


class Point {
	float x;
	float y;
	
	/**
	 * variable line stores the line to which this point belongs if the type of point is 0 or 1
	 */
	int line;
	/**
	 * Variable line2 stores the second line representing the intersection of two line if the 
	 * type of the point is -1
	 */
	int line2;
	/**
	 * 0 if it is a start point of the line
	 * 1 if it is a end point of the line
	 * -1 if it is an intersection point of two lines
	 */
	int type;
	
	public Point(float x, float y) {
		this.line2 = -1;
		this.x = x;
		this.y = y;
	}
	
	public void setLine(int l) {
		line = l;
	}
	
	/**
	 * returns the slope of the vector represented by this point.
	 * @return
	 */
	public float slope() {
		return y/x;
	}
	
	/**
	 * Adds another point to this point.
	 * @param B the point to be added.
	 * @return the resulting point after addition
	 */
	public Point plus(Point B) {
		return new Point (x + B.x, y + B.y);
	}
	
	/**
	 * Subtracts the point from this point
	 * @param B - the point to be subtracted
	 * @return the resulting point after subtraction
	 */
	public Point minus(Point B) {
		return new Point (x - B.x, y - B.y);
	}
	
	/**
	 * Multiply a constant to the vector represented by this point
	 * @param t - the constant to be multiplied
	 * @return - the resulting point after multiplication
	 */
	public Point multiply(float t) {
		return new Point (x*t, y*t);
	}
	
	/**
	 * returns the vector perpendicular to the vector represented by this point 
	 * @return
	 */
	public Point perpendicular () {
		return new Point(-y, x);
	}
	
	/**
	 * returns the dot product of the vector represented by this point and the given point
	 * @param B - the point representing the vector B
	 * @return
	 */
	public float dot(Point B) {
		return (x*B.x) + (y*B.y);
	}
	
	/**
	 * Returns the magnitude of the cross product of the given vector 
	 * and the vector represented by this point
	 * @param B
	 * @return
	 */
	public float cross(Point B) {
		return (x*B.y) - (B.x*y);
	}
	
	/**
	 * Function for comparing two points
	 * @param o2
	 * @return
	 */
	public int compareTo(Point o2) {
		if (x < o2.x) {
			return -1;
		}
		else if (x == o2.x && y < o2.y)
			return -1;
		else if (x == o2.x && y == o2.y)
			return 0;
		else
			return 1;
	}
	
	/**
	 * Returns a string format of the point as required by the homework
	 */
	public String toString () {
		return String.format("%.2f", x) + " " + String.format("%.2f", y);
	}
	
	/**
	 * Hash function. Required by Set<>
	 */
	public int hashCode() {
		int result = 1;
		result = result*37 + (int)(100*x);
		result = result*37 + (int)(y*100);
		return result;
	}
	
	/**
	 * Equals function, Required by Set<>
	 */
	public boolean equals(Object o) {
		if (! (o instanceof Point)) return false;
        Point p = (Point)o;
        return (((int)(x*100) == (int)(p.x*100)) && ((int)(y*100) == (int)(p.y*100)));
	}
} 

/**
 * Class LineSegment for storing a line segment represented by 2 points.
 * @author Ankur Garg <agarg12@ncsu.edu> <200157990>
 *
 */
class LineSegment {
	Point P1;
	Point P2;
	
	/**
	 * Constructor
	 * @param A
	 * @param B
	 */
	public LineSegment(Point A, Point B) {
		if (A.x < B.x) {
			this.P1 = A;
			this.P2 = B;
		}
		else {
			this.P1 = B;
			this.P2 = A;
		}
		
		if (P1.x == P2.x)
			System.out.println("Can't handle vertical lines");
	}
	
	/**
	 * Returns the vector formed by the subtraction of the points representing this line. 
	 * @return
	 */
	public Point vector() {
		return P2.minus(P1);
	}
	
	/**
	 * Returns the intersection point of this line and a given line.
	 * @param L - the line with which intersection needs to be calculated
	 * @return - the point of intersection and null if the lines don't intersect.
	 */
	public Point intersection(LineSegment L) {
		if (!intersects(L))
			return null;
		
		float t4 = P1.minus(L.P1).dot(vector().perpendicular());
		float temp = L.vector().dot(vector().perpendicular());
		t4 = t4/temp;
		
		float t2 = L.P1.minus(P1).dot(L.vector().perpendicular());
		temp = vector().dot(L.vector().perpendicular());
		t2 = t2/temp;
		
		
		if (t4 >= 0 && t4 <=1 && t2 >=0 && t2 <= 1) {
			return (L.P1.plus(L.vector().multiply(t4)));
		}
		else {
			System.out.println(this.toString());
			System.out.println(L.toString());
			System.out.println("Something is wrong with the boolean intersects()");
			return null;
		}
		
	}
	
	/**
	 * Checks if two lines intersect.
	 * @param L - the line with the intersection needs to be checked
	 * @return - true if the lines intersect and false otherwise
	 */
	public boolean intersects(LineSegment L) {
		float p1 = L.P1.minus(P1).cross(P2.minus(P1));
		float p2 = L.P2.minus(P1).cross(P2.minus(P1));
		float p3 = P1.minus(L.P1).cross(L.P2.minus(L.P1));
		float p4 = P2.minus(L.P1).cross(L.P2.minus(L.P1));
		
		if (p1*p2 <= 0 && p3*p4 <=0)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if the given line is above or below this line for a given x coordinate
	 * For checking this, maximum 2 digit precision is taken into account
	 * @param Line - the line with which comparison needs to be done.e
	 * @param loc - X coordinate at which comparison needs to be done.
	 * @return - 1 if this line is below the given line and 
	 * -1 if the given line is below this line.
	 * 0 if both lines are equal  
	 */
	public int compareTo (LineSegment Line, float loc) {
		int yloc = (int)(equation(loc)*100);
		int yloc2 = (int)(Line.equation(loc)*100);
		
		if (yloc > yloc2)
			return -1;
		else if (yloc < yloc2)
			return 1;
		else
			return 0;
	}
	
	/**
	 * returns the y coordinate for the given x coordinate for this line
	 * @param X - the X coordinate
	 * @return - the Y coordinate
	 */
	private float equation(float X) {
		if (P1.y == P2.y) {
			return P1.y;
		}
		else {
			float m = P2.minus(P1).slope();
			return P1.y+(m*(X-P1.x));
		}
	}
	
	public String toString() {
		return "Line: "+P1.toString()+","+P2.toString();
	}
	
}
