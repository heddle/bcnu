package chimera.grid;

import chimera.util.ThetaPhi;

public class Fiveplet {
	private static final String NTHETA = "n" + ThetaPhi.SMALL_THETA;
	private static final String NPHI = "n" + ThetaPhi.SMALL_PHI;
	
	public int nx;
	public int ny;
	public int nz;
	public int ntheta;
	public int nphi;
	
	/**
	 * Constructor for the Fiveplet class.
	 *
	 * @param nx     index on the x grid.
	 * @param ny     index on the y grid.
	 * @param nz     index on the z grid.
	 * @param ntheta index on the theta grid.
	 * @param nphi   index on the phi grid.
	 */
	public Fiveplet(int nx, int ny, int nz, int ntheta, int nphi) {
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
		this.ntheta = ntheta;
		this.nphi = nphi;
	}
	
	@Override
	public String toString() {
		return String.format("[nx = %d, ny = %d, nz = %d, %s = %d, %s = %d]", nx, ny, nz, NTHETA, ntheta, NPHI, nphi);
	}

}
