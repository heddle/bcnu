package cnuphys.kinetic.molecule;

import bCNU3D.GrowablePointSet;
import bCNU3D.Panel3D;
import item3D.GrowablePointSets3D;

public class Molecules extends GrowablePointSets3D {
	
	// the molecules (default point set)
	private GrowablePointSet _molecules;
	
	// the name of the default point set
	private static final String pointSetName = "Molecules";

	/**
	 * Create the molecules object
	 * 
	 * @param panel3d the 3D panel
	 */
	public Molecules(Panel3D panel3d) {
		super(panel3d);
		
		addPointSet(pointSetName, java.awt.Color.RED, 2f, true);
		_molecules = findByName(pointSetName);
	}
	
	/**
	 * Clear all the molecules
	 */
	public void clear() {
		_molecules.clear();
	}
	
	/**
	 * Add a molecule
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public void addMolecule(Molecule molecule) {
        _molecules.add(molecule);
    }

}
