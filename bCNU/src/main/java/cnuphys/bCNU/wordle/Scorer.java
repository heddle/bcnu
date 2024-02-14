package cnuphys.bCNU.wordle;

public class Scorer {

    public static int[] scoreGuess(String answer, String guess, int scores[]) {
        boolean[] matched = new boolean[5]; // Keep track of letters in the answer that are already matched.

        // First pass: Check for correct positions (2s).
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                scores[i] = 2;
                matched[i] = true; // Mark this letter as matched.
            }
        }

        // Second pass: Check for correct letters in wrong positions (1s).
        for (int i = 0; i < guess.length(); i++) {
            if (scores[i] != 2) { // Skip already correctly placed letters.
                for (int j = 0; j < answer.length(); j++) {
                    if (!matched[j] && guess.charAt(i) == answer.charAt(j)) {
                        scores[i] = 1;
                        matched[j] = true; // Mark this letter in the answer as matched.
                        break; // Stop searching once a match is found.
                    }
                }
            }
        }

        return scores;
    }

    public static void main(String[] args) {
    	int scores[] = new int[5];
        String answer = "apple";
        String guess = "apxep";
        scoreGuess(answer, guess, scores);

        // Print the scores array to check the result.
        for (int score : scores) {
            System.out.print(score + " ");
        }
    }
}
