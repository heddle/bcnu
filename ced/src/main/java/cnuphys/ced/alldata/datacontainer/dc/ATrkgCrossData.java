package cnuphys.ced.alldata.datacontainer.dc;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonCrossData;

public abstract class ATrkgCrossData extends ACommonCrossData {


	/** ID */
	public short id[];

	/** the cross x direction */
	public float ux[];

	/** the cross y direction */
	public float uy[];

	/** the cross z direction*/
	public float uz[];

	/** segment 1 id */
	public short Segment1_ID[];

	/** segment 2 id */
	public short Segment2_ID[];

	@Override
	public void clear() {
		super.clear();
		id = null;
		ux = null;
		uy = null;
		uz = null;
		Segment1_ID = null;
		Segment2_ID = null;
	}

	@Override
	public void update(DataEvent event) {
		String bankName = bankName();
		DataBank bank = event.getBank(bankName);

		if (bank == null) {
			return;
		}

		id = bank.getShort("id");
		sector = bank.getByte("sector");
		region = bank.getByte("region");
		x = bank.getFloat("x");
		y = bank.getFloat("y");
		z = bank.getFloat("z");
		err_x = bank.getFloat("err_x");
		err_y = bank.getFloat("err_y");
		err_z = bank.getFloat("err_z");
		ux = bank.getFloat("ux");
		uy = bank.getFloat("uy");
		uz = bank.getFloat("uz");
        Segment1_ID = bank.getShort("Segment1_ID");
        Segment2_ID = bank.getShort("Segment2_ID");

		int n = (x != null) ? x.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}

	}

	/**
	 * Provide feedback for a cross
	 *
	 * @param index           the index of the cluster
	 * @param feedbackStrings add strings to this collection
	 */
	public void feedback(int index, List<String> feedbackStrings) {

		String name = feedbackName();
		feedbackStrings.add(String.format("$Forest Green$%s cross ID %d", name, id[index]));
		super.feedback(name, index, feedbackStrings);
		feedbackStrings.add(String.format("$Forest Green$%s cross direction (%-6.3f, %-6.3f, %-6.3f)", name,
				ux[index], uy[index], uz[index]));
		feedbackStrings.add(String.format("$Forest Green$%s cross seg ids %d, %d",
				name, Segment1_ID[index], Segment2_ID[index]));
	}


	/**
	 * Get the name of the trkg cross bank
	 * @return the name of the bank
	 */
	public abstract String bankName();

	/**
	 * Get the name of the trkg fb name
	 * @return the name of the feedback
	 */
	public abstract String feedbackName();
}
