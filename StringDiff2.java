/** This class highlights text differences between two plain strings by generating html fragment to show changes using longest common subsequence algorithm

version 1.0, 24-11-2022, first release

*/
public class StringDiff2 {

    private static final String INSERT_COLOR = "#00ff66";
    private static final String DELETE_COLOR = "#ff9933";
    private static final int lcs_threshold = 1;//minimum threshold for longest common subsequence

    public static void main(String[] args) {

        String text1 = "Do not change this section. Please check any misqelling! Note that this section is obsolete.";
        String text2 = "New section added. Do not change this section. Please check any mispelling!";

        String result = markTextDiff2(text1, text2, INSERT_COLOR, DELETE_COLOR);
        System.out.println(result);
    }


	//highlights with htlm tags the changes from text1 to text2 using longest common subsequence algorithm
    public static String markTextDiff2(String text1, String text2, String insertColor, String deleteColor) {		
		String lcs = longestCommonSubsequence(text1, text2);
		StringBuilder stringBuilder = new StringBuilder();

        int cur1 = 0, cur2 = 0;//cursors
        for (int k = 0; k < lcs.length(); k++) {
            char common = lcs.charAt(k);
            int idx1 = text1.indexOf(common, cur1);
            int idx2 = text2.indexOf(common, cur2);
            if (idx1 > cur1) {
                stringBuilder.append("<del style='background-color:").append(deleteColor).append("'>").append(text1.substring(cur1, idx1)).append("</del>");
            }
            if (idx2 > cur2) {
                stringBuilder.append("<ins style='background-color:").append(insertColor).append("'>").append(text2.substring(cur2, idx2)).append("</ins>");
            }
            stringBuilder.append(common);
            cur1 = idx1 + 1;
            cur2 = idx2 + 1;
        }
        if (cur1 < text1.length()) {
            stringBuilder.append("<del style='background-color:").append(deleteColor).append("'>").append(text1.substring(cur1)).append("</del>");
        }
        if (cur2 < text2.length()) {
            stringBuilder.append("<ins style='background-color:").append(insertColor).append("'>").append(text2.substring(cur2)).append("</ins>");
        }

		return stringBuilder.toString();
    }


/* This algorithm uses a quadratic amount of memory
	public static String longestCommonSubsequence(String text1, String text2) {
//credit to: https://rosettacode.org/wiki/Longest_common_subsequence#Java
		int[][] lengths = new int[text1.length() + 1][text2.length() + 1];
		// row 0 and column 0 are initialized to 0 already
		for (int i = 0; i < text1.length(); i++)
			for (int j = 0; j < text2.length(); j++)
				if (text1.charAt(i) == text2.charAt(j))
					lengths[i+1][j+1] = lengths[i][j] + 1;
				else
					lengths[i+1][j+1] = Math.max(lengths[i+1][j], lengths[i][j+1]);

		// get the substring out from the matrix
		StringBuffer sb = new StringBuffer();
		for (int x = text1.length(), y = text2.length(); x != 0 && y != 0; ) {
			if (lengths[x][y] == lengths[x-1][y])
				x--;
			else if (lengths[x][y] == lengths[x][y-1])
				y--;
			else {
				x--;
				y--;
				assert text1.charAt(x) == text2.charAt(y);
				sb.append(text1.charAt(x));
			}
		}

		return sb.reverse().toString();
	}
*/

//Hirschberg algorithm, code is imported from package org.apache.commons.text.similarity, licensed to the Apache Software Foundation (ASF) 
//The Hirschberg algorithm uses a linear amount of memory
//reference: https://commons.apache.org/proper/commons-text/index.html
    private static String longestCommonSubsequence(final String left, final String right) {
        final int m = left.length();
        final int n = right.length();

        final StringBuilder out = new StringBuilder();

        if (m == 1) { // Handle trivial cases, as per the paper
			if (right.contains(left)) //left contains one and only one character
				out.append(left);
        } else if (n > 0 && m > 1) {
            final int mid = m / 2; // Find the middle point

            final String leftFirstPart = left.substring(0, mid);
            final String leftSecondPart = left.substring(mid, m);

            // Step 3 of the algorithm: two calls to Algorithm B
            final int[] l1 = algorithmB(leftFirstPart, right);
            final int[] l2 = algorithmB(reverse(leftSecondPart), reverse(right));

            // Find k, as per the Step 4 of the algorithm
            int k = 0;
            int t = 0;
            for (int j = 0; j <= n; j++) {
                final int s = l1[j] + l2[n - j];
                if (t < s) {
                    t = s;
                    k = j;
                }
            }

            // Step 5: solve simpler problems, recursively
            out.append(longestCommonSubsequence(leftFirstPart, right.substring(0, k)));
            out.append(longestCommonSubsequence(leftSecondPart, right.substring(k, n)));
        }

        return out.toString();
    }

    private static String reverse(final String s) {
        return new StringBuilder(s).reverse().toString();
    }

    private static int[] algorithmB(final String left, final String right) {
        final int m = left.length();
        final int n = right.length();

        // Creating an array for storing two rows of DP table
        final int[][] dpRows = new int[2][1 + n];

        for (int i = 1; i <= m; i++) {
            // K(0, j) <- K(1, j) [j = 0...n], as per the paper:
            // Since we have references in Java, we can swap references instead of literal copying.
            // We could also use a "binary index" using modulus operator, but directly swapping the
            // two rows helps readability and keeps the code consistent with the algorithm description
            // in the paper.
            final int[] temp = dpRows[0];
            dpRows[0] = dpRows[1];
            dpRows[1] = temp;

            for (int j = 1; j <= n; j++) {
                if (left.charAt(i - 1) == right.charAt(j - 1)) {
                    dpRows[1][j] = dpRows[0][j - 1] + 1;
                } else {
                    dpRows[1][j] = Math.max(dpRows[1][j - 1], dpRows[0][j]);
                }
            }
        }

        // LL(j) <- K(1, j) [j=0...n], as per the paper:
        // We don't need literal copying of the array, we can just return the reference
        return dpRows[1];
    }

}
