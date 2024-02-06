package cnuphys.ced.event.data;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.event.data.lists.BaseHit2List;

/**
 * static methods to centralize getting data arrays related to BMT
 *
 * @author heddle
 *
 */

public class BMT extends DetectorData {


	// list of BMT reconstructed hits
	BaseHit2List _bmtRecHits;

	private static BMT _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the BMT singleton
	 */
	public static BMT getInstance() {
		if (_instance == null) {
			_instance = new BMT();
		}
		return _instance;
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		_bmtRecHits = new BaseHit2List("BMTRec::Hits", "strip");
	}

	/**
	 * Get the BMT reconstructed hits
	 *
	 * @return the BMT reconstructed hits
	 */
	public BaseHit2List getRecHits() {
		return _bmtRecHits;
	}

}
