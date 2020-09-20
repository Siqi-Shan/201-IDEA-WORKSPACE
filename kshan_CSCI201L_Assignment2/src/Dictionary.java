/**
 ******************************************************************************
 *                    HOMAnyTypeWORK-2, cs201
 ******************************************************************************
 *                    Amortized Dictionary
 ******************************************************************************
 *
 * Implementation of an Amortized Array-Based Dictionary data structure.
 *
 * This data structure supports duplicates and does *NOT* support storage of
 * null references.
 *
 * Notes:
 * 		-It is *highly* recommended that you begin by reading over all the methods,
 *       all the comments, and all the code that has already been written for you.
 *
 * 		-the specifications provided are to help you understand what the methods
 *       are supposed to accomplish.
 * 		-they are *NOT* descriptions for how you should implement the methods.
 * 		-See the lab documentation & lecture notes for implementation details.
 *
 * 		-Some of the helper methods specify a runtime cost; make sure your
 *       implementation meets that requirement.
 * 		-(Also, obviously, if the lecture notes and/or the lab documentation specifies
 *       a runtime cost for a method, you need to pay attention to that).
 *
 *
 *****************************************************************************/



public class Dictionary<AnyType extends Comparable<AnyType>>  implements DictionaryInterface<AnyType>
{
	/*
	 * Keeps track of the number of elements in the dictionary.
	 * Take a look at the implementation of size()
	 */
	private int size;
	/*
	 * The head reference to the linked list of Nodes.
	 * Take a look at the Node class.
	 */
	private Node head;

	/**
	 * Creates an empty dictionary.
	 */
	public Dictionary() {
		size = 0;
		head = null;
	}

	/**
	 * Adds e to the dictionary, thus making contains(e) true.
	 * Increments size so as to ensure size() is correct.
	 */
	public void add(AnyType e) {
		if (e == null) {
			return;
		}

		/*
		 * Your code goes here...
		 */
		Node newNode = new Node(0, new Comparable[power(2, 0)], null);
		newNode.array[0] = e;
		mergeDown();
		size++;

		

		throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Removes e from the dictionary.  If contains(e) was formerly false,
	 * it is still false.
	 * Otherwise, decrements size so as to ensure size() is correct.
	 */
	public void remove(AnyType e) {
		if (e == null) {
			return;
		}

		/*
		 * Your code goes here...
		 */
		if (!this.contains(e)) {
			return;
		}



		throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Returns true iff the dictionary contains an element equal to e.
	 */
	public boolean contains(AnyType e)
	{
		if(e == null)
		{
			return false;
		}

		/*
		 * Your code goes here...
		 */
		Node current = head;

		while (current != null) {
			boolean result = current.contains(e);

			if (!result) {
				current = current.next;
			}
			else {
				return true;
			}
		}

		return false;

		//throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Returns the number of elements in the dictionary equal to e.
	 * This is logically equivalent to the number of times remove(e) needs to be performed
	 * in order for contains(e) to be false.
	 */
	public int frequency(AnyType e)
	{
		if(e == null)
		{
			return 0;
		}

		/*
		 * Your code goes here...
		 */
		int count = 0;
		Node current = head;

		while (current != null) {
			int NCount = current.frequency(e);
			count = count + NCount;
			current = current.next;
		}

		return count;

		//throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Returns the size of the dictionary.
	 */
	public int size() {
		return size;
	}

	/**
	 * Combines with the other AAD using the algorithm discussed in lecture.
	 *
	 * Formally, the following need to be true after combining an AAD with another AAD:
	 * 		-the resulting dictionary contains an item iff it was contained in either of the two dictionaries
	 * 		-the resulting frequency of any item is the sum of its frequency in the two dictionaries
	 * 		-the resulting size is the sum of the two sizes
	 */
	public void combine(Dictionary<AnyType> other) {
		if(other == null || this == other)
		{
			return;
		}

		/*
		 * Your code goes here...
		 */

		throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Returns a helpful string representation of the dictionary.
	 */
	public String toString() {
		StringBuffer content = new StringBuffer();
		Node current = head;
		int nodeIndex = 0;

		while (current != null) {
			content.append(nodeIndex + ": ");
			content.append(current.toString());
			content.append("\n");

			current = current.next;
			nodeIndex++;
		}

		return new String(content);
	}


	/**
	 * Starting with the smallest array, mergeDown() merges arrays of the same size together until
	 * all the arrays have different size.
	 *
	 * This is very useful for implementing add(e)!!!  See the lecture notes for the theory behind this.
	 */
	private void mergeDown() {
		/*
		 * Your code goes here...
		 */
		Node temp = head;
		mergeDown_helper(temp, temp.array);



		Node temp = head;
		if (temp != null) {

		}

		throw new RuntimeException("You need to implement this method!");
	}

	private void mergeDown_helper(Node root, Comparable[] C) {
 		if (root == null) {
 			return;
		}

 		if (root.array.length == C.length) {
 			Comparable[] result = merge(root.array, C);

		}


	}

	/**
	 * Assumes a is sorted.
	 *
	 * contains(a, item) 	= -1, if there is no element of a equal to item
	 * 						= k, otherwise, where a[k] is equal to item
	 *
	 * This is needed for Node's indexOf(e)
	 *
	 * O(log(a.length))
	 */
	@SuppressWarnings("unchecked")
	public static int binarySearch(Comparable[] a, Comparable item) {
		/*
		 * Your code goes here...
		 */
		int result = binarySearch_helper(a, item, 0, a.length - 1);
		return result;

		throw new RuntimeException("You need to implement this method!");
	}

	/** Recursive binary search helper function */
	private static int binarySearch_helper(Comparable[] a, Comparable item, int L, int H) {
		int M = (L + H) / 2;
		if (H < L) {
			return -1;
		}

		if (item.compareTo(a[M]) < 0) {
			return binarySearch_helper(a, item, L, M - 1);
		}
		else if (item.compareTo(a[M]) > 0) {
			return binarySearch_helper(a, item, M + 1, H);
		}
		else if (item.compareTo(a[M]) == 0) {
			return M;
		}
	}

	/**
	 * Assumes a is sorted.
	 *
	 * Returns the number of elements of a equal to item.
	 *
	 * This is needed for Node's frequency(e).
	 *
	 * O(log(a.length) + frequency(item))
	 */
	@SuppressWarnings("unchecked")
	public static int frequency(Comparable[] a, Comparable item) {
		/*
		 * Your code goes here...
		 */
		int count = 0;
		int index = binarySearch(a, item);

		if (index != -1) {
			count++;
		}
		else {
			return 0;
		}

		int temp = index - 1;
		while (temp >= 0) {
			if (a[temp].compareTo(item) == 0) {
				count++;
			}
			temp--;
		}

		temp = index + 1;
		while (temp < a.length) {
			if (a[temp].compareTo(item) == 0) {
				count++;
			}
			temp++;
		}

		return count;


		//throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * When a and b are sorted arrays, merge(a,b) returns a sorted array
	 * that has length (a.length+b.length) than contains the elements
	 * of a and the elements of b.
	 *
	 * This is useful for implementing the mergeDown() method.
	 *
	 * O(a.length + b.length)
	 */
	@SuppressWarnings("unchecked")
	public static Comparable[] merge(Comparable[] a, Comparable[] b) {
		/*
		 * Your code goes here...
		 */
		Comparable[] newArray = new Comparable[a.length+b.length];
		int i = 0;
		int j = 0;
		int k = 0;

		while (i < (a.length + b.length) && j < (a.length + b.length)) {
			if (a[i].compareTo(b[j]) < 0) {
				newArray[k] = a[i];
				i++;
				k++;
			}
			else {
				newArray[k] = a[j];
				j++;
				k++;
			}
		}

		return newArray;

		throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Returns base^exponent.  This is useful for implementing splitUp(a,k)
	 */
	private static int power(int base, int exponent) {
		return (int)(Math.pow(base, exponent));
	}

	/**
	 * Assumes a.length >= 2^k - 1, for the given k.
	 *
	 * Splits the first (2^k -1) elements of a up into k-1 sorted arrays of
	 * length 2^(k-1), 2^(k-2), ..., 2, 1.
	 * Returns a Queue of these arrays (in the above order, i.e. the one with
	 * length 2^(k-1) is at the front).
	 *
	 * This is useful for implementing remove(e) using the algorithm discussed in class.
	 *
	 * O(a.length)
	 */
	@SuppressWarnings("unchecked")
	public static java.util.Queue<Comparable[]> splitUp(Comparable[] a, int k) {
		/*
		 * We'll just use a LinkedList as a Queue in this fashion.  Take a look at the
		 * API for the java.util.Queue interface.
		 */

		java.util.Queue<Comparable[]> q = new java.util.LinkedList<Comparable[]>();

		/*
		 * Your code goes here...
		 */

		throw new RuntimeException("You need to implement this method!");
	}

	/**
	 * Implementation of the underlying array-based data structure.
	 *
	 * AnyTypeach Node:
	 * 			-knows k, its "power"
	 * 			-has myArray, a sorted array of 2^k elements
	 * 			-knows myNext, the next Node in the linked list of Nodes
	 *
	 * You do *NOT* need to change this class.
	 * It is, however, very important that you understand how it works.
	 * You may add additional methods, although you have been provided with sufficient
	 * functionality needed to implement the dictionary.
	 */
	@SuppressWarnings("unchecked")
	private static class Node
	{
		private int power;
		private Comparable[] array;
		private Node next;

		/**
		 * Creates an Node with the specified parameters.
		 */
		public Node(int power, Comparable[] array, Node next)
		{
			this.power = power;
			this.array = array;
			this.next = next;
		}

		/**
		 * Returns 	-1, if there is no element in the array equal to e
		 * 			 k, otherwise, where array[k] is equal to e
		 */
		public int indexOf(Comparable e)
		{
			return Dictionary.binarySearch(array, e);
		}

		/**
		 * Returns	true, if there is an element in the array equal to e
		 * 			false, otherwise
		 */
		public boolean contains(Comparable e)
		{
			return indexOf(e) > -1;
		}

		/**
		 * Returns the number of elements in the array equal to e
		 */
		public int frequency(Comparable e)
		{
			return Dictionary.frequency(array, e);
		}

		/**
		 * Returns a useful representation of this Node.  (Note how this is used by Dictionary's toString()).
		 */
		public String toString()
		{
			return java.util.Arrays.toString(array);
		}
	}

}


