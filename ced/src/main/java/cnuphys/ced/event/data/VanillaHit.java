package cnuphys.ced.event.data;

public class VanillaHit implements Comparable<VanillaHit> {
	
	public final byte sector;
	public final byte layer;
	public final short component;
	public final byte order;
	public short occurances;

	public VanillaHit(byte sector, byte layer, short component, byte order) {
		super();
		this.sector = sector;
		this.layer = layer;
		this.component = component;
		this.order = order;
		occurances = 1;
	}


	@Override
	public int compareTo(VanillaHit hit) {
		int c = Integer.valueOf(sector).compareTo(Integer.valueOf(hit.sector));
		if (c == 0) {
			c = Integer.valueOf(layer).compareTo(Integer.valueOf(hit.layer));
			if (c == 0) {
				c = Integer.valueOf(component).compareTo(Integer.valueOf(hit.component));
				if (c == 0) {
					c = Byte.valueOf(order).compareTo(Byte.valueOf(hit.order));
				}
			}
		}
		return c;
	}
	
	@Override
	public String toString() {
		return String.format("sect: %d lay: %d comp: %d ord: %d count: %d", sector, layer, component, order, occurances);
	}

}
