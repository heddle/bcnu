package cnuphys.ced.event.data;


import org.jlab.io.base.DataEvent;

import cnuphys.ced.event.data.lists.BaseHit2List;

public class BST extends DetectorData {


	// list of BST reconstructed hits
	BaseHit2List _bstRecHits;


	private static BST _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the BST singleton
	 */
	public static BST getInstance() {
		if (_instance == null) {
			_instance = new BST();
		}
		return _instance;
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		_bstRecHits = new BaseHit2List("BSTRec::Hits", "strip");
	}



	/**
	 * Get the BST reconstructed hits
	 *
	 * @return the BST reconstructed hits
	 */
	public BaseHit2List getRecHits() {
		return _bstRecHits;
	}



}