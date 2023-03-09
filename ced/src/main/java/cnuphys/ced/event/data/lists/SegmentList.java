package cnuphys.ced.event.data.lists;

import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.Segment;

public class SegmentList extends Vector<Segment> {

	private String _error;

	public SegmentList(String bankName) {

		byte[] sector = ColumnData.getByteArray(bankName + ".sector");
		if (sector == null) {
			return;
		}

		int length = sector.length;

		if (length > 0) {
			byte[] superlayer = ColumnData.getByteArray(bankName + ".superlayer");

			float[] x1 = ColumnData.getFloatArray(bankName + ".SegEndPoint1X");
			float[] z1 = ColumnData.getFloatArray(bankName + ".SegEndPoint1Z");
			float[] x2 = ColumnData.getFloatArray(bankName + ".SegEndPoint2X");
			float[] z2 = ColumnData.getFloatArray(bankName + ".SegEndPoint2Z");

			for (int i = 0; i < length; i++) {
				add(new Segment(sector[i], superlayer[i], x1[i], z1[i], x2[i], z2[i]));
			}
		}

	}

}
