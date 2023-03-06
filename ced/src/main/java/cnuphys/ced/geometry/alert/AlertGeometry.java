package cnuphys.ced.geometry.alert;

import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.geom.detector.alert.AHDC.AlertDCDetector;
import org.jlab.geom.detector.alert.AHDC.AlertDCFactory;
import org.jlab.geom.detector.alert.AHDC.AlertDCLayer;
import org.jlab.geom.detector.alert.AHDC.AlertDCSector;
import org.jlab.geom.detector.alert.AHDC.AlertDCSuperlayer;
import org.jlab.geom.detector.alert.ATOF.AlertTOFDetector;
import org.jlab.geom.detector.alert.ATOF.AlertTOFFactory;
import org.jlab.logging.DefaultLogger;

import cnuphys.ced.frame.Ced;

public class AlertGeometry {
	
	//the name of the detector
	public static String NAME = "ALERT";
	
	//the alert dc geometry factory
	public static AlertDCFactory dcFactory = new AlertDCFactory();

	private static AlertDCDetector _dcCLASDetector; //dc in clas coordinates
	
	/** number of sectors for alert DC detector */
	public static int DC_NUM_SECT;
	
	/** number of superlayers per sector for alert DC detector */
	public static int DC_NUM_SUPL;

	/** number of layers per superlater for alert DC detector */
	public static int DC_NUM_LAY;
		
	/** array of all DC layers for Alert detector indices are [sect, supl, lay]*/
	public static DCLayer[][][] dcLayers;
	
	//the alert tof geometry factory
	public static AlertTOFFactory tofFactory = new AlertTOFFactory();
	
	private static AlertTOFDetector _tofCLASDetector; //tof in clas coordinates
	
	/** number of sectors for alert TOF detector */
	public static int TOF_NUM_SECT;
	
	/** number of superlayers per sector for alert TOF detector */
	public static int TOF_NUM_SUPL;

	/** number of layers per superlater for alert TOF detector */
	public static int TOF_NUM_LAY;



	
	/** the radii in mm of the DC layers */
	public static double midPointDCRadii[][][];
	
	
	/**
	 * Init the uRwell geometry
	 */
	public static void initialize() {
		System.out.println("\n=======================================");
		System.out.println("===  " + NAME + " Geometry Initialization ===");
		System.out.println("=======================================");

        String variationName = Ced.getGeometryVariation();
        DatabaseConstantProvider cp = new DatabaseConstantProvider(11, variationName);
        
        //create everything for DC
        _dcCLASDetector = dcFactory.createDetectorCLAS(cp);
        DC_NUM_SECT = _dcCLASDetector.getNumSectors();
        DC_NUM_SUPL = dcFactory.createSector(cp, 0).getNumSuperlayers();
        DC_NUM_LAY = dcFactory.createSuperlayer(cp, 0, 0).getNumLayers();
        
        midPointDCRadii = new double[DC_NUM_SECT][DC_NUM_SUPL][DC_NUM_LAY];

		dcLayers = new DCLayer[DC_NUM_SECT][DC_NUM_SUPL][DC_NUM_LAY];

		for (int sect = 0; sect < DC_NUM_SECT; sect++) {
			for (int supl = 0; supl < DC_NUM_SUPL; supl++) {
				for (int lay = 0; lay < DC_NUM_LAY; lay++) {
					dcLayers[sect][supl][lay] = new DCLayer(dcFactory.createLayer(cp, sect, supl, lay));
				}
			}
		}
		
		//create everything for tof
		_tofCLASDetector = tofFactory.createDetectorCLAS(cp);
        TOF_NUM_SECT = _tofCLASDetector.getNumSectors();
        TOF_NUM_SUPL = tofFactory.createSector(cp, 0).getNumSuperlayers();
        TOF_NUM_LAY = tofFactory.createSuperlayer(cp, 0, 0).getNumLayers();

	}

	//main program for testing
	public static void main(String[] arg) {
		//this is supposed to create less pounding of ccdb
		DefaultLogger.initialize();

		initialize();
		
		System.out.println("Alert DC");
		System.out.println("  Number of sectors: " + DC_NUM_SECT);
		System.out.println("  Number of superlayers: " + DC_NUM_SUPL);
		System.out.println("  Number of layers: " + DC_NUM_LAY);
		
		
		System.out.println("\nAlert TOF");
		System.out.println("  Number of sectors: " + TOF_NUM_SECT);
		System.out.println("  Number of superlayers: " + TOF_NUM_SUPL);
		System.out.println("  Number of layers: " + TOF_NUM_LAY);
		
		
		System.out.println("\nAlert TOF");
		
		System.out.println("done");
		
	}
	



}
