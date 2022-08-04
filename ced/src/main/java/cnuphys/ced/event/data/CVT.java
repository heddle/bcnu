package cnuphys.ced.event.data;

import cnuphys.ced.alldata.ColumnData;

public class CVT {
	
	/** the columns */
	public short[] rec_id;
	public byte[] rec_detector;
	public byte[] rec_sector;
	public byte[] rec_layer;
	public float[] rec_x; //cm
	public float[] rec_y;
	public float[] rec_z;
	
	public float[] rec_phi;
	public float[] rec_theta;
	public float[] rec_langle;
	public float[] rec_centroid;
	public float[] rec_path;
	
	//pass1 
	public short[] p1_id;
	public byte[] p1_detector;
	public byte[] p1_sector;
	public byte[] p1_layer;
	public float[] p1_x; //cm
	public float[] p1_y;
	public float[] p1_z;
	
	public float[] p1_phi;
	public float[] p1_theta;
	public float[] p1_langle;
	public float[] p1_centroid;
	public float[] p1_path;


	private static CVT _instance;

	/**
	 * Public access to the singleton
	 * 
	 * @return the CTOF singleton
	 */
	public static CVT getInstance() {
		if (_instance == null) {
			_instance = new CVT();
		}
		return _instance;
	}

	public void fillData() {
		rec_id = ColumnData.getShortArray("CVTRec::Trajectory.id");
		rec_detector = ColumnData.getByteArray("CVTRec::Trajectory.detector");
		rec_sector = ColumnData.getByteArray("CVTRec::Trajectory.sector");
		rec_layer = ColumnData.getByteArray("CVTRec::Trajectory.layer");
		rec_x = ColumnData.getFloatArray("CVTRec::Trajectory.x");
		rec_y = ColumnData.getFloatArray("CVTRec::Trajectory.y");
		rec_z = ColumnData.getFloatArray("CVTRec::Trajectory.z");
		
		rec_phi = ColumnData.getFloatArray("CVTRec::Trajectory.phi");
		rec_theta = ColumnData.getFloatArray("CVTRec::Trajectory.theta");
		rec_langle = ColumnData.getFloatArray("CVTRec::Trajectory.langle");
		rec_centroid = ColumnData.getFloatArray("CVTRec::Trajectory.centroid");
		rec_path = ColumnData.getFloatArray("CVTRec::Trajectory.path");
		
		p1_id = ColumnData.getShortArray("CVT::Trajectory.id");
		p1_detector = ColumnData.getByteArray("CVT::Trajectory.detector");
		p1_sector = ColumnData.getByteArray("CVT::Trajectory.sector");
		p1_layer = ColumnData.getByteArray("CVT::Trajectory.layer");
		p1_x = ColumnData.getFloatArray("CVT::Trajectory.x");
		p1_y = ColumnData.getFloatArray("CVT::Trajectory.y");
		p1_z = ColumnData.getFloatArray("CVT::Trajectory.z");
		
		p1_phi = ColumnData.getFloatArray("CVT::Trajectory.phi");
		p1_theta = ColumnData.getFloatArray("CVT::Trajectory.theta");
		p1_langle = ColumnData.getFloatArray("CVT::Trajectory.langle");
		p1_centroid = ColumnData.getFloatArray("CVT::Trajectory.centroid");
		p1_path = ColumnData.getFloatArray("CVT::Trajectory.path");
	}
	

}