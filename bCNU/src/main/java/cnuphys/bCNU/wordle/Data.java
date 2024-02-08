package cnuphys.bCNU.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The data for the game. Consists of two lists of words. One list is the game
 * words. The other list is valid words not used as game words.
 */
public class Data {
	
	//game words
	private static final String LANAME = "data/wordle-La.txt";

	//valid non-game words
	private static final String TANAME = "data/wordle-Ta.txt";
	
	//list of game words
	private List<String> _laList;
	
	//list of real words not used as game words
	private List<String> _taList;

	
	//singleton instance
	private static volatile Data _instance;
	
	private Data() {
		_laList = readFileIntoList(LANAME);        
		_taList = readFileIntoList(TANAME);			
	}
	
	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static Data getInstance() {
		if (_instance == null) {
			_instance = new Data();
		}
		return _instance;
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
    public char[] randomWord() {
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
    	
    	word = word.toLowerCase();
		if (word == null) {
			return false;
		}
		
		if (word.length() != 5) {
			return false;
		}
		
		return wordInList(word, _laList) || wordInList(word, _taList);
    }

    //is the word in the given list
	private boolean wordInList(String word, List<String> list) {
		if (word == null) {
			return false;
		}
		int index = Collections.binarySearch(list, word);
		return index >= 0;
	}


}
