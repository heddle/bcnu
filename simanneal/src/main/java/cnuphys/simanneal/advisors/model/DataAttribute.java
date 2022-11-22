package cnuphys.simanneal.advisors.model;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataAttribute {
	
	
	/**
	 * To store a list of synonyms for a given object
	 * @author heddle
	 *
	 */
	
	/**
	 * The "correct" name of the list
	 */
	public String name;
	
	//lowercase version
	private String _lcName;
	
	//width if used as a cilumn in a table
	public int width;
	
	//list of synonyms
	private String[] _synonyms;
	
 	/**
	 * Create a DataAttribute which will be a column header
	 * @param name the name as a column header
	 * @param width the column width
	 * @param synonyms an optional list of synonyms
	 */
	public DataAttribute(String name, int width, String ...synonyms) {
		this.name = name;
		this.width = width;
		_lcName = name.toLowerCase();
		_synonyms = synonyms;
	}
	
	/**
	 * Checks (case insensitive) whether the given string is one of the synonyms
	 * @param s the string to check
	 * @return the name of this object if there is a match, otherwise null
	 */
	public String isSynonym(String s) {
		s = replaceNonAscii(s.toLowerCase());
		
		//the name counts as its own synonym
		String bs = s.equals(_lcName) ? "TRUE" : "FALSE";
		
		if (s.equals(_lcName)) {
			return name;
		}
		
		for (String syn : _synonyms) {
			if (s.equals(syn.toLowerCase())) {
				return name;
			}
		}
		
		return null;
	}
	
	//remove non ascii chars that may have come from a csv file UTF8 encoded
	private String replaceNonAscii(String s) {
		return s.replaceAll("[^\\x00-\\x7F]", "");
	}
	
	/**
	 * Get the column index for a synonym
	 * @param header the column labels
	 * @param colSyn the synonym to match
	 * @return the index, or -1 if not founds
	 */
	public static int getColumnIndex(String[] header, DataAttribute colSyn) {
		
		for (int i = 0; i < header.length; i++) {
			
			if (colSyn.isSynonym(header[i]) != null) {
				return i;
			}
		}
		
		return -1;
	}
	
	private static String utf8String(String rawString) {
		ByteBuffer buffer = StandardCharsets.UTF_8.encode(rawString); 

		String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
		return utf8EncodedString;
	}

}
