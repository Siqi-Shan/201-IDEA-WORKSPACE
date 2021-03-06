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

import java.util.Arrays;

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
	@SuppressWarnings("FieldMayBeFinal")
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

		//Create the new node and link it to the linked list
		Node newNode = new Node(0, new Comparable[power(2, 0)], null);
		newNode.array[0] = e;
		newNode.next = this.head;
		this.head = newNode;
		mergeDown();
		size++;
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

		//Whether the dictionary contains the element
		if (!this.contains(e)) {
			return;
		}

		//Find the element
		Node current = head;
		while (current != null) {
			int index = current.indexOf(e);

			//If the node array contains the element
			if (index != -1) {
				if (current == head) {
					//If head node contains the element
					if (this.head.array.length == 1) {
						this.head = this.head.next;
						break;
					}

					//Remove the element from the array
					Comparable[] newArray = new Comparable[current.array.length - 1];
					System.arraycopy(current.array, 0, newArray, 0, index);
					System.arraycopy(current.array, index + 1, newArray, index, current.array.length - index - 1);
					current.array = newArray;

					//Split the array and add back
					java.util.Queue<Comparable[]> splitResult = splitUp(current.array, current.power);

					remove_helper(splitResult);
				}
				else {
					//If other nodes contain element, swap with largest element in the head node array
					Comparable headNum = this.head.array[this.head.array.length - 1];
					this.head.array[this.head.array.length - 1] = current.array[index];
					current.array[index] = headNum;
					Arrays.sort(current.array);

					if (this.head.array.length == 1) {
						this.head = this.head.next;
						break;
					}

					Comparable[] newArray = new Comparable[this.head.array.length - 1];
					System.arraycopy(this.head.array, 0, newArray, 0, this.head.array.length - 1);
					this.head.array = newArray;

					java.util.Queue<Comparable[]> splitResult = splitUp(this.head.array, this.head.power);

					remove_helper(splitResult);
				}
				break;
			}

			current = current.next;
		}
	}

	/** Helper function that insert the split arrays back to linked list from the head */
	private void remove_helper(java.util.Queue<Comparable[]> splitResult) {
		//Insert all arrays before second node
		int currentPower = this.head.power - 1;
		Node nextNode = this.head.next;
		this.head.next = null;

		//Insert from largest to smallest arrays as nodes
		for (Comparable[] temp : splitResult) {
			Node newNode = new Node(currentPower, temp, nextNode);
			this.head = newNode;
			nextNode = newNode;
			currentPower--;
		}
	}

	/**
	 * Returns true iff the dictionary contains an element equal to e.
	 */
	public boolean contains(AnyType e) {
		if(e == null) {
			return false;
		}

		//Searching from head node
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
	}

	/**
	 * Returns the number of elements in the dictionary equal to e.
	 * This is logically equivalent to the number of times remove(e) needs to be performed
	 * in order for contains(e) to be false.
	 */
	public int frequency(AnyType e) {
		if(e == null) {
			return 0;
		}

		//Counting number of elements from the head node
		int count = 0;
		Node current = head;
		while (current != null) {
			int NCount = current.frequency(e);
			count = count + NCount;
			current = current.next;
		}

		return count;
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
		if(other == null || this == other) {
			return;
		}

		//Increase the size
		this.size = this.size + other.size();

		this.head = combine_helper(this.head, other.head);

		//User mergeDown on every node to guarantee there are not two node with same array size
		Node temp = head;
		while (temp != null) {
			this.mergeDown();
			temp = temp.next;
		}

	}

	/** Recursively combining two linked lists with increasing order */
	private Node combine_helper(Node P, Node Q) {
		if (P == null) {
			return Q;
		}
		if (Q == null) {
			return P;
		}

		if (P.array.length < Q.array.length) {
			P.next = combine_helper(P.next, Q);
			return P;
		}
		else {
			Q.next = combine_helper(P, Q.next);
			return Q;
		}
	}

	/**
	 * Returns a helpful string representation of the dictionary.
	 */
	public String toString() {
		StringBuffer content = new StringBuffer();
		Node current = head;

		while (current != null) {
			//noinspection StringConcatenationInsideStringBufferAppend
			content.append(current.power + ": ");
			content.append(current.toString());
			content.append("\n");

			current = current.next;
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
		Node temp = this.head;
		mergeDown_helper(temp, temp.next);
	}

	/*private void mergeDown_helper(Node current, Node nextNode) {
		if (current == null || nextNode == null) {
			return;
		}

		if (current.power == nextNode.power) {
			Comparable[] result = merge(current.array, nextNode.array);
			current.array = result;
			current.next = nextNode.next;
			current.power = (int)(Math.log(current.array.length) / Math.log(2));
			mergeDown_helper(current, current.next);
		}
		//else{
		//	mergeDown_helper(current.next, current.next.next);
		//	mergeDown_helper(current, current.next);
		//}
	}*/

	/** Recursively merge same size arrays helper function */
	private void mergeDown_helper(Node current, Node nextNode) {
		//If two nodes have same array size, stop recursion
 		if (nextNode == null || current.array.length != nextNode.array.length) {
			return;
		}

 		//Check to merge last two nodes with same array size to maintain array size in ascending order
		if (nextNode.next == null || current.array.length != nextNode.next.array.length) {
			Comparable[] result = merge(current.array, nextNode.array);
			current.array = result;
			current.next = nextNode.next;
			current.power = (int)(Math.log(current.array.length) / Math.log(2));
			//Recurse to next node
			mergeDown_helper(current, current.next);
		}
		else{
			//Go to next pairs of nodes
			mergeDown_helper(current.next, current.next.next);
			//Check previous nodes if there are still nodes with same array size
			mergeDown_helper(current, current.next);
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
		int result = binarySearch_helper(a, item, 0, a.length - 1);
		return result;
	}

	/** Recursive binary search helper function */
	@SuppressWarnings("unchecked")
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

		return -1;
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
		int count = 0;
		int index = binarySearch(a, item);

		if (index != -1) {
			count++;
		}
		else {
			return 0;
		}

		//Check elements before index
		int temp = index - 1;
		while (temp >= 0) {
			if (a[temp].compareTo(item) == 0) {
				count++;
			}
			temp--;
		}

		//Check elements after index
		temp = index + 1;
		while (temp < a.length) {
			if (a[temp].compareTo(item) == 0) {
				count++;
			}
			temp++;
		}

		return count;
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
		Comparable[] newArray = new Comparable[a.length + b.length];
		int i = 0;
		int j = 0;
		int k = 0;

		while (i < a.length && j < b.length) {
			if (a[i].compareTo(b[j]) < 0) {
				newArray[k++] = a[i++];
			}
			else {
				newArray[k++] = b[j++];
			}
		}

		while (i < a.length) {
			newArray[k++] = a[i++];
		}

		while (j < b.length) {
			newArray[k++] = b[j++];
		}

		return newArray;
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
		java.util.Queue<Comparable[]> q = new java.util.LinkedList<Comparable[]>();

		for (int i = 1; i <= k; i++) {
			Comparable[] newArray = new Comparable[power(2, k - i)];

			System.arraycopy(a, power(2, k - i) - 1, newArray, 0, power(2, k - i));
			q.add(newArray);
		}

		return q;
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


