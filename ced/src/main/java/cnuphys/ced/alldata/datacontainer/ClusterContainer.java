package cnuphys.ced.alldata.datacontainer;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class ClusterContainer implements IDataContainer {
	
	private String _bankName;

	public byte sector[];
	public byte layer[];
	public float energy[];
	public float[] time;
	public float x[];
	public float y[];
	public float z[];

	//where it was last drawn
	public int ppx[];
	public int ppy[];


	public ClusterContainer(String bankName) {
		_bankName = bankName;
	}
	
	@Override
	public void clear() {
		sector = null;
		layer = null;
		energy = null;
		time = null;
			
		x = null;
		y = null;
		z = null;
		
		ppx = null;
		ppy = null;
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		if (bank == null) {
			return;
		}
		
		sector = bank.getByte("sector");
		if ((sector != null) && (sector.length > 0)) {
			layer = bank.getByte("layer");
			energy = bank.getFloat("energy");
			time = bank.getFloat("time");
			x = bank.getFloat("x");
			y = bank.getFloat("y");
			z = bank.getFloat("z");
			ppx = new int[sector.length];
			ppy = new int[sector.length];
		} 
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return sector.length;
	}

}
