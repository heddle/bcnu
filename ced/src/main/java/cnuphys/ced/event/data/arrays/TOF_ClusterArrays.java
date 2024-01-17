package cnuphys.ced.event.data.arrays;

import java.util.List;

import cnuphys.ced.geometry.ftof.FTOFGeometry;

public class TOF_ClusterArrays extends TOF_HitArrays {

	public TOF_ClusterArrays(String bankName) {
		super(bankName);
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
