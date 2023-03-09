package cnuphys.ced.event.data.lists;

import java.util.Collections;
import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.DataSupport;
import cnuphys.ced.event.data.TbHbHit;

public class TbHbHitList extends Vector<TbHbHit> {

	public TbHbHitList(String bankName) {

		byte sector[] = ColumnData.getByteArray(bankName + ".sector");
		if (sector == null) {
			return;
		}

		int length = sector.length;
		if (length > 0) {

			byte[] superlayer = ColumnData.getByteArray(bankName + ".superlayer");

			byte[] layer6 = ColumnData.getByteArray(bankName + ".layer");

			short[] wire = ColumnData.getShortArray(bankName + ".wire");
			short[] id = ColumnData.getShortArray(bankName + ".id");
			short[] status = ColumnData.getShortArray(bankName + ".status");
			int[] TDC = ColumnData.getIntArray(bankName + ".TDC");

			// complication.. for HB doca will be null
			float[] doca = ColumnData.getFloatArray(bankName + ".doca");
			float[] trkDoca = ColumnData.getFloatArray(bankName + ".trkDoca");

			for (int i = 0; i < length; i++) {
				float fdoca = DataSupport.safeValue(doca, i, -1f);
				float trkdoca = DataSupport.safeValue(trkDoca, i, -1f);
				int tdc = DataSupport.safeValue(TDC, i, -1);

				add(new TbHbHit(sector[i], superlayer[i], layer6[i], wire[i], id[i], status[i], tdc, fdoca, trkdoca));
			}
		}

		if (size() > 1) {
			Collections.sort(this);
		}

	}


	/**
	 * Find the index of a hit
	 *
	 * @param sector     the 1-based sector
	 * @param superlayer the superlayer
	 * @param layer6     the 1-based layer 1..6
	 * @param wire       the 1-based wire
	 * @return the index, or -1 if not found
	 */
	public int getIndex(byte sector, byte superlayer, byte layer6, short wire) {
		if (isEmpty()) {
			return -1;
		}

//		public DCHit(byte sector, byte superlayer, byte layer6, short wire, short id, short status, float time, float doca, float trkDoca) {

		TbHbHit hit = new TbHbHit(sector, superlayer, layer6, wire);
		int index = Collections.binarySearch(this, hit);
		if (index >= 0) {
			return index;
		} else { // not found
			return -1;
		}
	}

	/**
	 * Find the hit
	 *
	 * @param sector     the 1-based sector
	 * @param superlayer the superlayer
	 * @param layer6     the 1-based layer 1..6
	 * @param wire       the 1-based wire
	 * @return the index, or -1 if not found
	 */
	public TbHbHit get(byte sector, byte superlayer, byte layer6, short wire) {
		int index = getIndex(sector, superlayer, layer6, wire);
		return (index < 0) ? null : elementAt(index);
	}



}
