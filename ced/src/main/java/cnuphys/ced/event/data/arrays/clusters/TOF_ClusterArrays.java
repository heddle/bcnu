package cnuphys.ced.event.data.arrays.clusters;

import java.util.List;

import cnuphys.ced.event.data.arrays.BaseArrays;
import cnuphys.ced.event.data.arrays.hits.TOF_HitArrays;
import cnuphys.ced.geometry.ftof.FTOFGeometry;

public class TOF_ClusterArrays extends TOF_HitArrays {

	protected TOF_ClusterArrays(String bankName) {
		super(bankName);
	}

	/**
	 * Get the tof cluster arrays for a given bank name
	 *
	 * @param bankName the bank name, either "CTOF::adc" or "FTOF::adc"
	 * @return the cluster arrays, either created or from cache
	 */
	public static TOF_ClusterArrays getTOF_ClusterArrays(String bankName) {
		//try to get from cache
		BaseArrays arrays = dataWarehouse.getArrays(bankName);
		if (arrays != null) {
			return (TOF_ClusterArrays) arrays;
		}

		TOF_ClusterArrays clusterArrays = new TOF_ClusterArrays(bankName);
		dataWarehouse.putArrays(bankName, clusterArrays);
		return clusterArrays;
	}


	/**
	 * Add feedback for the given hit index
	 *
	 * @param clusterIndex the hit index
	 * @param feedback the feedback list
	 */
	@Override
	public void addFeedback(int clusterIndex, List<String> feedback) {
		if (clusterIndex >= 0) {

			if (clusterIndex >= 0) {
				feedback.add(_fbColor + detectorIdentityString(clusterIndex));
				feedback.add(_fbColor + "cluster index " + clusterIndex);
				feedback.add(_fbColor + "cluster energy " + energy[clusterIndex]);

				String locStr = String.format("cluster location (%6.4f, %6.4f, %6.4f) cm", x[clusterIndex], y[clusterIndex], z[clusterIndex]);

				feedback.add(_fbColor + locStr);

			} else {
				feedback.add(_fbColor + "no cluster");
			}


		} else {
			feedback.add(_fbColor + "no cluster");
		}
	}


	/**
	 * Return a detector specific identity string
	 * @param hitIndex the hit index
	 * @return a detector specific identity string
	 */
	@Override
	protected String detectorIdentityString(int clusterIndex) {
		//won't be called unless hitIndex is valid

		byte sector = this.sector[clusterIndex];
		byte layer = this.layer[clusterIndex];
		short component = this.component[clusterIndex];

		String layStr = FTOFGeometry.getPanelName(layer-1);
		return "TOF cluster: sector " + sector + ", layer " + layStr + ", paddle " + component;
	}


}
