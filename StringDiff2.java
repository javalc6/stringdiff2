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

}
