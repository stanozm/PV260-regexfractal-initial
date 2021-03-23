package pv260.refactoring.regexfractals;

import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;

/**
 * Grid of the Fractal stored in an array
 */
public class FractalGrid {

	/**
	 * The array
	 * @since version 0.1
	 */
	private String[][] signaturesArray;

	/**
	 * The constructor
	 * @param size  the size
	 * @param limit send in 1 for default behavior
	 */
	public FractalGrid(int size, double limit) {
		QuadrantSlice rootSlize = new QuadrantSlice(size);
		signaturesArray = new String[size][size];
		rootSlize.writeLeavesTo(signaturesArray);
	}

	/**
	 * Implementation of the {@code sigOg}
	 * @param x the X
	 * @param y the Y
	 * @return signature for given position X Y
	 */
	public String sigOf(int x, int y) {
		if (x < 0 || x >= signaturesArray.length
				|| y < 0 || y >= signaturesArray.length) {
			throw new IndexOutOfBoundsException("Got index [" + x + "," + y + "] but the array is only " + signaturesArray.length + "x" + signaturesArray.length);
		}
		return signaturesArray[x][y];
	}

	private static int nthPowerOfTwo(int number) {
		int n = 0;
		int remainder = number;
		while (remainder > 1) {
			if ((remainder & 1) == 1) {
				throw new IllegalArgumentException(number + "is not power of two");
			}
			remainder = remainder / 2;
			n++;
		}
		return n;
	}

	/**
	 * One quarter of the parents space
	 * top left is 2, to right is 1,
	 * bottom left is 3, bottom right is 4
	 */
	private static class QuadrantSlice {

		/**
		 * accumulated x
		 */
		private int x;
		/**
		 * accumulated y
		 */
		private int y;
		/**
		 * accumulated signature
		 */
		private String signature;
		/**
		 * true if is leaf
		 */
		private boolean isLeaf = false;
		/**
		 * list of children
		 */
		private List<QuadrantSlice> children = Collections.emptyList();
		/**
		 * the addend to signature
		 */
		private char addend;

		/**
		 * the constructor
		 * use for root
		 * @param size the size left for this slice
		 */
		public QuadrantSlice(int size) {
			this.x = 0;
			this.y = 0;
			this.signature = "";
			children = asList(
					new QuadrantSlice(size / 2, x, y, signature, true, true),
					new QuadrantSlice(size / 2, x, y, signature, true, false),
					new QuadrantSlice(size / 2, x, y, signature, false, true),
					new QuadrantSlice(size / 2, x, y, signature, false, false)
			);
		}

		/**
		 * another constructor
		 * use for children
		 * @param size            the size left for this slice
		 * @param parentX
		 * @param parentY
		 * @param parentSignature
		 * @param isRightQuadrant
		 * @param isTopQuadrant
		 */
		public QuadrantSlice(int size, int parentX, int parentY, String parentSignature, boolean isRightQuadrant, boolean isTopQuadrant) {
			//add the size left of the quadrant if this is the right quadrant
			this.x = parentX + (isRightQuadrant ? size : 0);
			//ditto
			this.y = parentY + (!isTopQuadrant ? size : 0);
			//calculate what to add to the signature
			calculateAddend(isRightQuadrant, isTopQuadrant);
			this.signature = parentSignature + addend;
			//slices of size 1 are leaves
			if (size == 1) {
				isLeaf = true;
			} else { // this is not a child, so create more slices and make those children of this
				children = asList(
						new QuadrantSlice(size / 2, x, y, signature, true, true),
						new QuadrantSlice(size / 2, x, y, signature, true, false),
						new QuadrantSlice(size / 2, x, y, signature, false, true),
						new QuadrantSlice(size / 2, x, y, signature, false, false)
				);
			}
		}

		/**
		 * calculate what quadrant this is and thus what it adds to the signature
		 * @param isRightQuadrant true if this is the right quadrant
		 * @param isTopQuadrant   true if this is the left quadrant
		 */
		private void calculateAddend(boolean isRightQuadrant, boolean isTopQuadrant) {
			if (isRightQuadrant && isTopQuadrant) {
				addend = '1';
			} else if (!isRightQuadrant && isTopQuadrant) {
				addend = '2';
			} else if (!isRightQuadrant && !isTopQuadrant) {
				addend = '3';
			} else if (isRightQuadrant && !isTopQuadrant) {
				addend = '4';
			}
		}

		/**
		 * if this is a leaf add its signature to the collecting parameter,
		 * else pass it to all children recursively
		 * this method starts at the root and eventually collects signatures of all the children
		 * into the array
		 * called from constructor to initialize the signaturesArray
		 * @param signaturesArray
		 */
		public void writeLeavesTo(String[][] signaturesArray) {
			//if is leaf
			if (isLeaf) {
				//write its signature into the array
				signaturesArray[x][y] = signature;
			} else {
				//else call this method recursively on all the children
				for (QuadrantSlice child : children) {
					//call the method on the child 'child'
					child.writeLeavesTo(signaturesArray);
				}
			}
		}

	}

}
