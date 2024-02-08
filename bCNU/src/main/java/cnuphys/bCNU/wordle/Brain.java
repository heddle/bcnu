package cnuphys.bCNU.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Brain {
	private static volatile Brain _instance;
	
	//game words
	private static final String LANAME = "data/wordle-La.txt";

	//valid non-game words
	private static final String TANAME = "data/wordle-Ta.txt";
	
	//letters that haven't been used yet
	private ArrayList<Character> _usedLetters = new ArrayList<Character>();

	//list of game words
	private List<String> _laList;
	
	//list of real words not used as game words
	private List<String> _taList;
	
	//which word guess are we on? [0..5]
	private int _currentWordIndex = 0;
	
	//THE current word!
	private String _currentWord;
	
	
	private Brain() {
		_laList = readFileIntoList(LANAME);        
		_taList = readFileIntoList(TANAME);	
//		System.out.println("La list size: " + _laList.size());
//		System.out.println("Ta list size: " + _taList.size());
//		
//		String testWord1 = "steam";
//		String testWord2 = "xyzzy";
//		
//		System.out.println("Good word " + testWord1 + " " + goodWord(testWord1));
//		System.out.println("Good word " + testWord2 + " " + goodWord(testWord2));
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static Brain getInstance() {
		if (_instance == null) {
			_instance = new Brain();
		}
		return _instance;
	}
	
	/**
	 * Get the current word. For testing, not cheating!
	 * 
	 * @return the current word
	 */
	public String getCurrentWord() {
		return _currentWord;
	}
	
	/**
	 * Set the current word. For testing, not cheating!
	 * 
	 * @param word the current word
	 */
	public void setCurrentWord(String word) {
		_currentWord = word;
	}
	
	/**
	 * Reset everything for a new game
	 */
	public void newGame() {
		Wordle.getInstance().setMessage("New game!");
		_currentWord = (new String(randomWord())).toUpperCase();
		
		System.err.println("Current word: " + _currentWord);
		_currentWordIndex = 0;
		
		_usedLetters.clear();
		Keyboard.getInstance().reset();
	}
	
	// try reading word lists from the jar
	private BufferedReader bufferedReaderFromResource(String resourceName) {
		InputStream inStream = getClass().getClassLoader().getResourceAsStream(resourceName);
		return new BufferedReader(new InputStreamReader(inStream));
	}
	
	
	//read a list of words from a resource file in the bCNU jar
    private List<String> readFileIntoList(String fileName) {
    	ArrayList<String> list = new ArrayList<String>();
    	BufferedReader br = bufferedReaderFromResource(fileName);
		if (br != null) {
			try {
				String line;
				while ((line = br.readLine()) != null) {
					list.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//just to be sure
		Collections.sort(list);
		return list;
     }
    
    /**
     * Get a random word from the game list
     * @return a word for playing the game
     */
    private char[] randomWord() {
		char word[] = new char[5];
		String s = _laList.get((int) (Math.random() * _laList.size()));
		for (int i = 0; i < 5; i++) {
			word[i] = s.charAt(i);
		}
		return word;
    }
    
	/**
	 * Is the word a good word?
	 * It is good if it is in either list
	 * @param word the word to check
	 * @return <code>true</code> if the word is good
	 */
    public boolean goodWord(String word) {
		if (word == null) {
			return false;
		}
		
		if (word.length() != 5) {
			return false;
		}
		
		return wordInList(word, _laList) || wordInList(word, _taList);
    }

	private boolean wordInList(String word, List<String> list) {
		if (word == null) {
			return false;
		}
		int index = Collections.binarySearch(list, word);
		return index >= 0;
	}
	
	/**
	 * Process a character entry
	 * 
	 * @param c the character
	 */
	public void processCharacterEntry(char c) {
		if (c == Keyboard.ENTER) {
            processEnter();
         }
		else if (c == Keyboard.BACKSPACE) {
            processDelete();
         }
        else {
            processLetter(c);
        }
		
		refresh();
	}
	
	//process a letter
	private void processLetter(char c) {
        System.out.println("Letter " + c);
        LetterGrid.getInstance().processLetter(_currentWordIndex, c);
        refresh();
    }
	
	//process the enter key
	private void processEnter() {
		System.out.println("Enter");
		
		char[] word = LetterGrid.getInstance().getWord(_currentWordIndex);
		String guess = new String(word);
		
		if (guess.length() < 5) {
			Wordle.getInstance().setMessage("Guess too short!");
			LetterGrid.getInstance().badGuess(_currentWordIndex);
			return;
		}
		
		//handle not in dictionary
		if (!goodWord(guess)) {
			Wordle.getInstance().setMessage("Not a word!");
			LetterGrid.getInstance().badGuess(_currentWordIndex);
			return;
		}

		boolean result = submitGuess(guess);
		
		if (result) {
			System.out.println("Correct!");
			gameWon();
		} else {
			System.out.println("Incorrect!");
			LetterGrid.getInstance().badGuess(_currentWordIndex);
			
			//are we done?
			if (_currentWordIndex == 5) {
				gameLost();
			}
		}
		refresh();
	}
	
	//the game was won
	private void gameWon() {
		switch (_currentWordIndex) {
		case 0:
			Wordle.getInstance().setMessage("You won in one try! \nDid you cheat?");
			break;
		case 1:
			Wordle.getInstance().setMessage("You won! 2nd try! \nThat's pretty amazing");
			break;
		case 2:
			Wordle.getInstance().setMessage("You won! 3rd try! \nYou're good!");
			break;
		case 3:
                Wordle.getInstance().setMessage("You won! 4th try! \nNot too shabby!");
                break;
			case 4:
				Wordle.getInstance().setMessage("You won! 5th try! \nMeh.");
				break;
		default:
			Wordle.getInstance().setMessage("You won on the 6th try. \nColor me unimpressed");

		}
	}
	
	//the game was lost
	private void gameLost() {
		Wordle.getInstance().setMessage("Better luck next time!\n" + "The word was " + _currentWord);
	}
	
	//process the enter delete key
	private void processDelete() {
		System.out.println("Delete");
		LetterGrid.getInstance().delete(_currentWordIndex);
		refresh();
	}

	/**
	 * Refresh the display
	 */
	public void refresh() {
		_usedLetters.clear();
		LetterGrid.getInstance().fillUsedLetters(_usedLetters);
		LetterGrid.getInstance().refresh();
		Keyboard.getInstance().refresh();
	}
	
	/**
	 * Submit a guess
	 * 
	 * @param chars the characters of the guess
	 * @return <code>true</code> if the guess is correct
	 */
	private boolean submitGuess(String guess) {
		System.out.println("Guess: " + guess);
		return _currentWord.equals(guess);
	}
	
	/**
	 * Has a char been used?
	 * @param c the char
	 * @return <code>true</code> if the char has been used
     */
	public boolean unused(char c) {
		return !_usedLetters.contains(c);
	}
	
	

	public static void main(String arg[]) {
		char[] allNulls = { '\0', '\0', '\0', '\0', '\0' };
		String ans = new String(allNulls);
		System.out.println("Ans: [" + ans + "]");
	}
}
