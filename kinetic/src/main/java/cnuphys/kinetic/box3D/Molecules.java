package cnuphys.kinetic.box3D;

import bCNU3D.GrowablePointSet;
import bCNU3D.Panel3D;
import item3D.GrowablePointSets3D;

public class Molecules extends GrowablePointSets3D {
	
	private GrowablePointSet _molecules;
	
	private static final String pointSetName = "Molecules";

	public Molecules(Panel3D panel3d) {
		super(panel3d);
		
		addPointSet(pointSetName, java.awt.Color.RED, 2f, true);
		_molecules = findByName(pointSetName);
	}
	
	public void clear() {
		_molecules.clear();
	}
	
	public void addMolecule(float x, float y, float z) {
        _molecules.add(x, y, z);
    }

}
