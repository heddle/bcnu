package cnuphys.ced.event.data;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.event.data.lists.DCClusterList;
import cnuphys.ced.event.data.lists.DCReconHitList;

public class DC extends DetectorData {


	// HB reconstructed hits
	private DCReconHitList _hbHits;

	// TB reconstructed hits
	private DCReconHitList _tbHits;

	// HB reconstructed clusters
	private DCClusterList _hbClusters;

	// TB reconstructed clusters
	private DCClusterList _tbClusters;


	// singleton
	private static DC _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static DC getInstance() {
		if (_instance == null) {
			_instance = new DC();
		}
		return _instance;
	}

	@Override
	public void newClasIoEvent(DataEvent event) {


		//the reconstructed hits, HB and TB
		_hbHits = new DCReconHitList("HitBasedTrkg::HBHits");
		_tbHits = new DCReconHitList("TimeBasedTrkg::TBHits");

		//the clusters
		_hbClusters = new DCClusterList("HitBasedTrkg::HBClusters");
		_tbClusters = new DCClusterList("TimeBasedTrkg::TBClusters");

	}

	/**
	 * Get the hit based hit list
	 *
	 * @return the hit based hit list
	 */
	public DCReconHitList getHBHits() {
		return _hbHits;
	}

	/**
	 * Get the time based hit list
	 *
	 * @return the time based hit list
	 */
	public DCReconHitList getTBHits() {
		return _tbHits;
	}

	/**
	 * Get the hit based cluster list
	 *
	 * @return the hit based cluster list
	 */
	public DCClusterList getHBClusters() {
		return _hbClusters;
	}

	/**
	 * Get the time based cluster list
	 *
	 * @return the time based cluster list
	 */
	public DCClusterList getTBClusters() {
		return _tbClusters;
	}


}
