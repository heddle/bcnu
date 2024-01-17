package cnuphys.ced.event.data.arrays;

import java.util.List;

import cnuphys.ced.geometry.ftof.FTOFGeometry;

public class TOF_HitArrays extends HitArrays {

	public TOF_HitArrays(String bankName) {
		super(bankName);
	}
	
	/**
	 * Add feedback for the given hit index
	 *
	 * @param hitIndex the hit index
	 * @param feedback the feedback list
	 */
	@Override
	public void addFeedback(int hitIndex, List<String> feedback) {
		if (hitIndex >= 0) {

			if (hitIndex >= 0) {
				feedback.add(_fbColor + detectorIdentityString(hitIndex));
				feedback.add(_fbColor + "hit index " + hitIndex);
				feedback.add(_fbColor + "hit energy " + energy[hitIndex]);

				String locStr = String.format("hit location (%6.4f, %6.4f, %6.4f) cm", x[hitIndex], y[hitIndex], z[hitIndex]);

				feedback.add(_fbColor + locStr);

			} else {
				feedback.add(_fbColor + "no hit");
			}


		} else {
			feedback.add(_fbColor + "no hit");
		}
	}


	/**
	 * Return a detector specific identity string
	 * @param hitIndex the hit index
	 * @return a detector specific identity string
	 */
	@Override
	protected String detectorIdentityString(int hitIndex) {
		//won't be called unless hitIndex is valid

		byte sector = this.sector[hitIndex];
		byte layer = this.layer[hitIndex];
		short component = this.component[hitIndex];

		String layStr = FTOFGeometry.getPanelName(layer-1);
		return "TOF hit: sector " + sector + ", layer " + layStr + ", paddle " + component;
	}


}
