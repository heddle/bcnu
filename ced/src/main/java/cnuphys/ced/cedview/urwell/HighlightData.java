package cnuphys.ced.cedview.urwell;

/**
 * This will hold the highlighted data from uRWell banks
 * resulting from clicking on one of the bank rows
 * @author heddle
 *
 */
public class HighlightData {
	
	/** 1-based index of highlight hit, none if 0 */
	public int hit;
	
	/** 1-based index of highlight cluster, none if 0 */
	public int cluster;
	
	/** 1-based index of highlight cross, none if 0 */
	public int cross;


	
	public HighlightData() {
	}

}
