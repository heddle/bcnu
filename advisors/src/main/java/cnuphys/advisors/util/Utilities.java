package cnuphys.advisors.util;

import java.util.StringTokenizer;

public class Utilities {


	/**
	 * This method breaks a string into an array of tokens.
	 *
	 * @param str   the string to decompose.
	 * @param token the token
	 * @return an array of tokens
	 */

	public static String[] tokens(String str, String token) {

		StringTokenizer t = new StringTokenizer(str, token);
		int num = t.countTokens();
		String lines[] = new String[num];

		for (int i = 0; i < num; i++) {
			lines[i] = t.nextToken();
		}

		return lines;
	}

}
