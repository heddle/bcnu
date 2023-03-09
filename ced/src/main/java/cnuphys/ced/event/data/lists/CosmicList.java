package cnuphys.ced.event.data.lists;

import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.Cosmic;

public class CosmicList extends Vector<Cosmic> {

	public CosmicList(String bankName) {

		short id[] = ColumnData.getShortArray(bankName + ".ID");
		if (id == null) {
			return;
		}

		int length = id.length;

		if (length > 0) {

			float[] chi2 = ColumnData.getFloatArray(bankName + ".chi2");
			float[] phi = ColumnData.getFloatArray(bankName + ".phi");
			float[] theta = ColumnData.getFloatArray(bankName + ".theta");
			float[] trkline_yx_interc = ColumnData.getFloatArray(bankName + ".trkline_yx_interc");
			float[] trkline_yx_slope = ColumnData.getFloatArray(bankName + ".trkline_yx_slope");
			float[] trkline_yz_interc = ColumnData.getFloatArray(bankName + ".trkline_yz_interc");
			float[] trkline_yz_slope = ColumnData.getFloatArray(bankName + ".trkline_yz_slope");

			for (int i = 0; i < length; i++) {
				add(new Cosmic(id[i], chi2[i], phi[i], theta[i], trkline_yx_interc[i], trkline_yx_slope[i],
						trkline_yz_interc[i], trkline_yz_slope[i]));
			}
		}

	}

}
