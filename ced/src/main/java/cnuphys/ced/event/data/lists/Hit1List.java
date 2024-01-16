package cnuphys.ced.event.data.lists;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.Hit1;

public class Hit1List extends BaseHitList<Hit1> {

	public Hit1List(String bankName) {

		super(bankName);

		if (sector == null) {
			return;
		}

		int length = sector.length;

		if (length > 0) {
			byte layer[] = ColumnData.getByteArray(bankName + ".layer");
			short component[] = ColumnData.getShortArray(bankName + ".component");
			float energy[] = ColumnData.getFloatArray(bankName + ".energy");
			float x[] = ColumnData.getFloatArray(bankName + ".x");
			float y[] = ColumnData.getFloatArray(bankName + ".y");
			float z[] = ColumnData.getFloatArray(bankName + ".z");

			for (int i = 0; i < length; i++) {
				add(new Hit1(sector[i], layer[i], component[i], energy[i], x[i], y[i], z[i]));
			}
		}

	}

}
