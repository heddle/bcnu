package cnuphys.ced.event.data.lists;

import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.BaseHit2;

@SuppressWarnings("serial")
public class BaseHit2List extends Vector<BaseHit2> {

	// for reporting read errors
	protected String _error;

	protected String bankName;

	// the 1-based sector array
	protected byte[] sector;

	// number of hits
	protected int _count;

	public BaseHit2List(String bankName, String componentName) {

		this.bankName = bankName;
		sector = ColumnData.getByteArray(bankName + ".sector");

		_count = (sector != null) ? sector.length : 0;

		if (_count > 0) {
			byte layer[] = ColumnData.getByteArray(bankName + ".layer");
			int component[] = ColumnData.getIntArray(bankName + "." + componentName);
			for (int i = 0; i < _count; i++) {
				BaseHit2 baseHit2 = new BaseHit2(sector[i], layer[i], component[i]);
				add(baseHit2);
			}
		}
	}


	/**
	 * Get the bank name backing this list
	 *
	 * @return the bank name backing this list
	 */
	public String getBankName() {
		return bankName;
	}

	public int count() {
		return _count;
	}

}