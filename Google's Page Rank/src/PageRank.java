import static java.lang.Math.abs;

import java.io.File;
import java.util.Scanner;

/**
 * This class implements Google's Page Rank Algorithm
 * 
 * @author Shubham Agarwal
 *
 */
public class PageRank {

	static int[][] adjacencyMatrix; // adjacency list declaration
	static double[] Arr; // array
	static int[] outDegree; // Out degree declaration

	/**
	 * Main method
	 * 
	 * @param args Strings[]
	 */
	public static void main(String[] args) {
		int iterations = 0; // total number of iterations
		int initialValue = 0; // Initial Value
		String fileName = null; // input file

		if (args.length != 0) {
			iterations = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
			fileName = args[2];
			if ((initialValue <= 1 && initialValue >= -2) != true) {
				System.out.println("Invalid Initial value entered!");
				return;
			}

			pageRankAlgo(iterations, initialValue, fileName);

		} else {
			System.out.println("Invalid number of arguments entered!");
			return;
		}
	}

	/**
	 * Source array initialization
	 * 
	 * @param vertexCount  int
	 * @param initialValue int
	 */
	private static void setSrcArray(int vertexCount, int initialValue) {
		int iter = 0;
		Arr = new double[vertexCount];
		if (initialValue == 0 || initialValue == 1) {
			while (iter < vertexCount) {
				Arr[iter] = initialValue;
				iter++;
			}
		} else if (initialValue == -1) {
			while (iter < vertexCount) {
				Arr[iter] = 1.0 / vertexCount;
				iter++;
			}
		} else if (initialValue == -2) {
			while (iter < vertexCount) {
				Arr[iter] = 1.0 / Math.sqrt(vertexCount);
				iter++;
			}
		}
	}

	/**
	 * Initializes target array
	 * 
	 * @param vertexCount int
	 * @param targetArr   double
	 */
	private static void initializeTargetArray(int vertexCount, double[] targetArr) {
		for (int iter = 0; iter < vertexCount; iter++) {
			targetArr[iter] = 0.0;
			for (int itr2 = 0; itr2 < vertexCount; itr2++) {
				if (adjacencyMatrix[itr2][iter] == 1) {
					targetArr[iter] += Arr[itr2] / outDegree[itr2];
				}
			}
		}
	}

	/**
	 * The method to calculate errorate
	 * 
	 * @param Arr         double
	 * @param targetArr   double
	 * @param vertexCount int
	 * @param iterations  int
	 * @return boolean
	 */
	public static boolean errorateCalculation(double[] Arr, double[] targetArr, int vertexCount, int iterations) {
		double errorRate = 0.00001; // Default errorate of iteration is 0
		if (iterations < 0) {
			errorRate = Math.pow(10, iterations);
		}
		for (int index = 0; index < vertexCount; index++) {
			if (abs(Arr[index] - targetArr[index]) > errorRate) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Page rank algorithm's
	 * 
	 * @param fileName     int
	 * @param initialValue int
	 * @param iterations   String
	 */
	@SuppressWarnings({ "resource", "unused" })
	public static void pageRankAlgo(int iterations, int initialValue, String fileName) {

		final double d = 0.85;
		int vertexCount = 0; // Count of Vertices
		int edgeCount = 0; // Count of Edges
		int noOfRows = 0;
		int noOfCols = 0;
		int itr = 0;
		int itr2 = 0;

		try {

			Scanner scanner = new Scanner(new File(fileName));
			vertexCount = scanner.nextInt();
			edgeCount = scanner.nextInt();

			adjacencyMatrix = new int[vertexCount][vertexCount]; // initialize adj matrix

			while (scanner.hasNextInt()) {
				noOfRows = scanner.nextInt();
				noOfCols = scanner.nextInt();
				adjacencyMatrix[noOfRows][noOfCols] = 1;
			}

			outDegree = new int[vertexCount];
			for (itr = 0; itr < vertexCount; itr++) {
				outDegree[itr] = 0;
				for (itr2 = 0; itr2 < vertexCount; itr2++) {
					outDegree[itr] += adjacencyMatrix[itr][itr2];
				}
			}

			setSrcArray(vertexCount, initialValue);

		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
		}

		double[] targetArr = new double[vertexCount];
		boolean flag = true;
		if (vertexCount > 10) {
			iterations = 0;
			initialValue = -1;
			for (itr = 0; itr < vertexCount; itr++) {
				Arr[itr] = 1.0 / vertexCount;
			}

			int i = 0;
			do {
				if (flag == true) {
					flag = false;
				} else {
					for (itr = 0; itr < vertexCount; itr++) {
						Arr[itr] = targetArr[itr];
					}
				}
				initializeTargetArray(vertexCount, targetArr);

				for (itr = 0; itr < vertexCount; itr++) {
					targetArr[itr] = d * targetArr[itr] + (1 - d) / vertexCount;
				}
				i++;
			} while (errorateCalculation(Arr, targetArr, vertexCount, iterations) == false);

			System.out.println("Iter   : " + i);
			for (itr = 0; itr < vertexCount; itr++) {
				if (itr < 10)
					System.out.printf("P[ " + itr + "]=%.7f\n", Math.round(targetArr[itr] * 10000000.0) / 10000000.0);
				else
					System.out.printf("P[" + itr + "]=%.7f\n", Math.round(targetArr[itr] * 10000000.0) / 10000000.0);
			}
			return;
		}
		System.out.print("Base    :  0 :");
		for (itr = 0; itr < vertexCount; itr++) {
			if (itr == 0)
				System.out.printf("P[" + itr + "]=%.7f", Math.round(Arr[itr] * 10000000.0) / 10000000.0);
			else
				System.out.printf(" P[" + itr + "]=%.7f", Math.round(Arr[itr] * 10000000.0) / 10000000.0);
		}

		if (iterations > 0) {
			for (itr = 0; itr < iterations; itr++) {
				initializeTargetArray(vertexCount, targetArr);

				System.out.println();
				if (itr < 9)
					System.out.print("Iter    :  " + (itr + 1) + " :");
				else
					System.out.print("Iter    : " + (itr + 1) + " :");
				for (int iter = 0; iter < vertexCount; iter++) {
					targetArr[iter] = d * targetArr[iter] + (1 - d) / vertexCount;
					if (iter == 0)
						System.out.printf("P[" + iter + "]=%.7f",
								Math.round(targetArr[iter] * 10000000.0) / 10000000.0);
					else {
						System.out.printf(" P[" + iter + "]=%.7f",
								Math.round(targetArr[iter] * 10000000.0) / 10000000.0);
					}
				}
				int a = 0;
				while (a < vertexCount) {
					Arr[a] = targetArr[a];
					a++;
				}
			}
			System.out.println();
		} else {
			int i = 0;
			do {
				if (flag == true) {
					flag = false;
				} else {
					for (itr = 0; itr < vertexCount; itr++) {
						Arr[itr] = targetArr[itr];
					}
				}
				initializeTargetArray(vertexCount, targetArr);
				System.out.println();
				if (i < 9)
					System.out.print("Iter    :  " + (i + 1) + " :");
				else
					System.out.print("Iter    : " + (i + 1) + " :");
				for (int iter = 0; iter < vertexCount; iter++) {
					targetArr[iter] = d * targetArr[iter] + (1 - d) / vertexCount;
					if (iter == 0)
						System.out.printf("P[" + iter + "]=%.7f",
								Math.round(targetArr[iter] * 10000000.0) / 10000000.0);
					else {
						System.out.printf(" P[" + iter + "]=%.7f",
								Math.round(targetArr[iter] * 10000000.0) / 10000000.0);
					}
				}
				i++;
			} while (errorateCalculation(Arr, targetArr, vertexCount, iterations) == false);
			System.out.println("\n");
		}
	}
}