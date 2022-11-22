package cnuphys.bCNU.ztransform;

import java.util.Random;

import cnuphys.bCNU.geometry.Point;

/**
 * Highly specialized class for creating the transformation (and inverse)
 * for aligning an arbitrary line segment with the z axis
 * @author heddle
 *
 */
public class ZTransform {
	
	//azimuthal angle needed for rotation about z (radians)
	private double _phi; 
	private double _sinPhi;
	private double _cosPhi;

	
	//polar angle needed for rotation about y (radians)
	private double _theta; 
	private double _sinTheta;
	private double _cosTheta;
	
	//save the endpoints
	private final Point _p0;
	private final Point _p1;
	
	//the delta point prel = p1- p0
	private Point _prel;
	
	/**
	 * Create a transformation object that will align the line segment
	 * specified by two points wiht the z axis with one point at the origin
	 * @param p0 one end point
	 * @param p1the other end point
	 */
	public ZTransform(final Point p0, final Point p1) {
		_p0 = p0;
		_p1 = p1;
		_prel = new Point(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
		
		_phi = Math.atan2(_prel.y, _prel.x);
		_cosPhi = Math.cos(_phi);
		_sinPhi = Math.sin(_phi);
		
		double len = _prel.length();
		_theta = Math.acos(_prel.z/len);
		_cosTheta = Math.cos(_theta);
		_sinTheta = Math.sin(_theta);
		

	}
	
	/**
	 * Convert a point in the ZFrame (where the orginal segment
	 * is along the z axis) to the lab frame
	 * @param pZ the point in the z frame
	 * @param pLab the same point in the lab frame
	 */
	public void zFrameToLab(final Point pZ, Point pLab) {
		pLab.set(pZ);
		invRotateY(pLab);
		invRotateZ(pLab);
		Point.sum(pLab, _p0, pLab);
		
	}
	
	/**
	 * Convert a point in the the lab frame to the
	 * ZFrame (where the orginal segment is along the z axis)
	 * @param pZ the point in the z frame
	 * @param pLab the same point in the lab frame
	 */
	public void labFrameToZ(Point pZ, final Point pLab) {
		//step one translate
		Point.difference(pLab, _p0, pZ);
		
		System.out.println("S1 TRANS  pLab:" + pLab + "  pZ: " + pZ);
		
		//step two rotate around z by phi
		rotateZ(pZ);
		System.out.println("S2  ROTZ  pLab:" + pLab + "  pZ: " + pZ);
		
		//step three rotate around y by theta
		rotateY(pZ);
		System.out.println("S3  ROTX  pLab:" + pLab + "  pZ: " + pZ);

		
	}
	
	//rotate around z using phi
	private void rotateZ(Point p) {
		final double tx = p.x;
		final double ty = p.y;
		
		p.x = _cosPhi*tx + _sinPhi*ty;
		p.y = -_sinPhi*tx + _cosPhi*ty;
	}
	
	//rotate around x using theta
	private void rotateY(Point p) {
		final double tx = p.x;
		final double tz = p.z;
		
		p.x = _cosTheta*tx - _sinTheta*tz;
		p.z = _sinTheta*tx + _cosTheta*tz;
	}
	
	//inverse rotate around z using phi
	private void invRotateZ(Point p) {
		final double tx = p.x;
		final double ty = p.y;
		
		p.x = _cosPhi*tx - _sinPhi*ty;
		p.y = _sinPhi*tx + _cosPhi*ty;
	}
	
	//inverse rotate around x using theta
	private void invRotateY(Point p) {
		final double tx = p.x;
		final double tz = p.z;
		
		p.x = _cosTheta*tx + _sinTheta*tz;
		p.z = -_sinTheta*tx + _cosTheta*tz;
	}

	
	public static void main(String[] arg) {
		Random rand = new Random();
	    Point p0 = new Point(100*rand.nextDouble(), 100*rand.nextDouble(), 100*rand.nextDouble());
	    Point p1 = new Point(100*rand.nextDouble(), 100*rand.nextDouble(), 100*rand.nextDouble());
	    
	    Point pZ = new Point();
	    Point pBack =new Point();

	    
	    System.out.println("p0  " + p0.toString());
	    System.out.println("p1  " + p1.toString());
	    
	    ZTransform zTrans = new ZTransform(p0, p1);
	    	    
	    zTrans.labFrameToZ(pZ, p1);
	    
	    zTrans.zFrameToLab(pZ, pBack);
	    System.out.println("Transform back p1 = " + pBack);

	}
	

}
