package cnuphys.ced.event.data.lists;

import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.Cross;

public class CrossList extends Vector<Cross> {

	public CrossList(String bankName) {

		byte sector[] = ColumnData.getByteArray(bankName + ".sector");
		if (sector == null) {
			return;
		}

		int length = sector.length;

		if (length > 0) {
			byte[] region = ColumnData.getByteArray(bankName + ".region");

			short[] id = ColumnData.getShortArray(bankName + ".id");

			float[] x = ColumnData.getFloatArray(bankName + ".x");
			float[] y = ColumnData.getFloatArray(bankName + ".y");
			float[] z = ColumnData.getFloatArray(bankName + ".z");
			float[] ux = ColumnData.getFloatArray(bankName + ".ux");
			float[] uy = ColumnData.getFloatArray(bankName + ".uy");
			float[] uz = ColumnData.getFloatArray(bankName + ".uz");
			float[] err_x = ColumnData.getFloatArray(bankName + ".err_x");
			float[] err_y = ColumnData.getFloatArray(bankName + ".err_y");
			float[] err_z = ColumnData.getFloatArray(bankName + ".err_z");
			float[] err_ux = ColumnData.getFloatArray(bankName + ".err_ux");
			float[] err_uy = ColumnData.getFloatArray(bankName + ".err_uy");
			float[] err_uz = ColumnData.getFloatArray(bankName + ".err_uz");

			for (int i = 0; i < length; i++) {
				add(new Cross(sector[i], region[i], id[i], x[i], y[i], z[i], ux[i], uy[i], uz[i], err_x[i], err_y[i],
						err_z[i], err_ux[i], err_uy[i], err_uz[i]));
			}
		}

	}

}
