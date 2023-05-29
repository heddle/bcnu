package cnuphys.advisors.util;

import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

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
	
	/**
	 * Generate a random in int the range min to max inclusive
	 * @param min the min value
	 * @param max the max value
	 * @return randon int in the range [min, max]
	 */
	public static int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public static void main(String arg[]) {
		int minCount = 0;
		int maxCount = 0;
		int n = 100000000;
		int min = 2;
		int max = 101;
		
		for (int i = 0; i < n; i++) {
			int rannum = randomInt(min, max);
			
			if (rannum < min) {
				System.out.println("generated a num < min " + rannum);
				System.exit(1);
			}
			else if (rannum > max) {
				System.out.println("generated a num > max " + rannum);
				System.exit(1);
			}
			else if (rannum == min) {
				minCount++;
			}
			else if (rannum == max) {
				maxCount++;
			}


		}
		
		int expectedCount = (int)((double)n)/(max-min+1);
		System.out.println("num min = " + minCount + "   expected: " + expectedCount);
		System.out.println("num max = " + maxCount + "   expected: " + expectedCount);
	}

}
