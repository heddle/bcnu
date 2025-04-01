package cnuphys.fastMCed.snr;

import java.awt.Color;
import java.util.List;

import org.jlab.geom.DetectorHit;
import org.jlab.geom.DetectorId;

import cnuphys.fastMCed.fastmc.AugmentedDetectorHit;
import cnuphys.fastMCed.fastmc.ParticleHits;
import cnuphys.snr.ExtendedWord;
import cnuphys.snr.NoiseReductionParameters;
import cnuphys.snr.clas12.Clas12NoiseAnalysis;
import cnuphys.snr.clas12.Clas12NoiseResult;

public class SNRManager {

	// for use in theta estimates
	public static final double _thetaMin[] = { 5.3, 5.1, 5.4, 5.3, 5.0, 5.0 };
	public static final double _thetaMax[] = { 41.1, 40.9, 42.7, 42.7, 45., 45. };

	// bend direction
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	private static final String _fbColor = "$wheat$";

	/** noise mask color for left benders */
	public static final Color maskFillLeft = new Color(255, 128, 0, 48);

	/** noise mask color for right benders */
	public static final Color maskFillRight = new Color(0, 128, 255, 48);

	// singleton
	private static SNRManager instance;

	// The analysis package
	private Clas12NoiseAnalysis _noisePackage = new Clas12NoiseAnalysis();

	// result container
	private Clas12NoiseResult _noiseResults = new Clas12NoiseResult();

	// private constructor
	private SNRManager() {
	}

	/**
	 * Public access to the singleton
	 * 
	 * @return the SNRManager singleton
	 */
	public static SNRManager getInstance() {
		if (instance == null) {
			instance = new SNRManager();
		}
		return instance;
	}

	/**
	 * An approximate value of theta based on the wire
	 * 
	 * @param superlayer0 the superlayer [0..5]
	 * @param wire0
	 * @return
	 */
	public double approximateTheta(int superlayer0, int wire0) {
		double dTheta = (_thetaMax[superlayer0] - _thetaMin[superlayer0]) / 111.;
		return _thetaMin[superlayer0] + wire0 * dTheta;
	}

	/**
	 * Is there a potential right leaning track
	 * 
	 * @param sect0 the zero based sector [0..5]
	 * @return <code>true</code> if the track search was turned on and a potential
	 *         right leaning track based on the composite shifts was found
	 */
	public boolean potentialRightTrack(int sect0) {
		if (segmentsInAllSuperlayers(sect0, SNRManager.RIGHT)) {
			NoiseReductionParameters params0 = getParameters(sect0, 0);
			NoiseReductionParameters params4 = getParameters(sect0, 4);

			int rm0 = leftMostBitIndex(params0.getRightSegments());
			int rm4 = rightMostBitIndex(params4.getRightSegments());

			if (approximateTheta(4, rm4) > approximateTheta(0, rm0)) {
				return false;
			}

			NoiseReductionParameters params1 = getParameters(sect0, 1);
			NoiseReductionParameters params5 = getParameters(sect0, 5);
			int rm1 = leftMostBitIndex(params1.getRightSegments());
			int rm5 = rightMostBitIndex(params5.getRightSegments());

			if (approximateTheta(5, rm5) > approximateTheta(1, rm1)) {
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * Is there a potential left leaning track
	 * 
	 * @param sect0 the zero based sector [0..5]
	 * @return <code>true</code> if the track search was turned on and a potential
	 *         left leaning track based on the composite shifts was found
	 */
	public boolean potentialLeftTrack(int sect0) {
		if (segmentsInAllSuperlayers(sect0, SNRManager.RIGHT)) {
			NoiseReductionParameters params0 = getParameters(sect0, 0);
			NoiseReductionParameters params4 = getParameters(sect0, 4);

			int rm0 = rightMostBitIndex(params0.getRightSegments());
			int rm4 = leftMostBitIndex(params4.getRightSegments());

			if (approximateTheta(4, rm4) < approximateTheta(0, rm0)) {
				return false;
			}

			NoiseReductionParameters params1 = getParameters(sect0, 1);
			NoiseReductionParameters params5 = getParameters(sect0, 5);
			int rm1 = rightMostBitIndex(params1.getRightSegments());
			int rm5 = leftMostBitIndex(params5.getRightSegments());

			if (approximateTheta(5, rm5) < approximateTheta(1, rm1)) {
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * Get the left most bit set
	 * 
	 * @param w the long to check
	 * @return the left most bit set. It returns 0 if the "first" (LSB) bit is set.
	 *         returns 63 if the MSB is set. Returns -1 if no bit is set.
	 */
	private static int leftMostBit(long w) {
		int index = Long.numberOfLeadingZeros(w);
		return 63 - index;
	}

	/**
	 * Get the right most bit set
	 * 
	 * @param w the long to check
	 * @return the right most bit set. Returns -1 if no bit is set.
	 */
	private static int rightMostBit(long w) {
		int index = Long.numberOfTrailingZeros(w);
		return (index > 63) ? -1 : index;
	}

	/**
	 * Get the left most bit set
	 * 
	 * @param w the long to check
	 * @return the left most bit set. It returns 0 if the "first" (LSB) bit is set.
	 *         Returns -1 if no bit is set.
	 */
	private static int leftMostBitIndex(ExtendedWord ew) {
		long words[] = ew.getWords();
		int index = leftMostBit(words[1]);
		if (index > -1) {
			return 64 + index;
		}
		return leftMostBit(words[0]);
	}

	/**
	 * Get the right most bit set
	 * 
	 * @param w the long to check
	 * @return the right most bit set. Returns -1 if no bit is set.
	 */
	private static int rightMostBitIndex(ExtendedWord ew) {
		long words[] = ew.getWords();
		int index = rightMostBit(words[0]);
//		System.err.println("INDEX: " + index);
		if (index > -1) {
			return index;
		}
		index = rightMostBit(words[1]);
		return (index < 0) ? -1 : (index + 64);
	}

	/**
	 * see if SNR found a segment in every superlayer of given sector
	 * 
	 * @param sect0     zero based sector
	 * @param direction should be LEFT or RIGHT
	 * @return <code>true</code> if segments found in all six superlayers
	 */
	public boolean segmentsInAllSuperlayers(int sect0, int direction) {

		for (int supl0 = 0; supl0 < 6; supl0++) {
			if (!segmentInSuperlayer(sect0, supl0, direction)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * See if SNR found a segment in the given sector and superlayer
	 * 
	 * @param sect0     zero based sector
	 * @param supl0     zero based superlayer
	 * @param direction should be LEFT or RIGHT
	 * @return <code>true</code> if segment found in given sector and superlayer
	 */
	public boolean segmentInSuperlayer(int sect0, int supl0, int direction) {
		NoiseReductionParameters params = getParameters(sect0, supl0);

		if (direction == LEFT) {
			return !params.getLeftSegments().isZero();
		} else {
			return !params.getRightSegments().isZero();
		}
	}

	// used to set the bit in the summary word
	private short setBitAtLocation(short bits, short bitIndex) {
		bits |= (1 << bitIndex);
		return bits;
	}

	/**
	 * Get the words for a "segments" extended word
	 * @param sector the zero based sector
	 * @param superlayer the zero based super
	 * @param direction should be LEFT or RIGHT
	 * @return the long[4] word array that makes up the segments extended word
	 */
	public long[] segmentWords(int sector, int superlayer, int direction) {
		NoiseReductionParameters params = getParameters(sector, superlayer);
		if (direction == LEFT) {
			return params.getLeftSegments().getWords();
		} else {
			return params.getRightSegments().getWords();
		}
	}
	
	/**
	 * Get the words for a "segments" extended word in a csv string
	 * @param sector the zero based sector
	 * @param superlayer the zero based super
	 * @param direction should be LEFT or RIGHT
	 * @return the long[2] word array in a csv string
	 */
	public String csvSegmentWords(int sector, int superlayer, int direction) {
		long[] words = segmentWords(sector, superlayer, direction);
		return words[0] + ", " + words[1];
	}

	/**
	 * Get the parameters for a given 0-based superlayer
	 * 
	 * @param sect0 the 0-based sector
	 * @param supl0 the 0-based superlayer in question
	 * @return the parameters for that superlayer
	 */
	public NoiseReductionParameters getParameters(int sect0, int supl0) {
		return _noisePackage.getParameters(sect0, supl0);
	}
	
	public String encodeSegments(int direction, int sect0, int supl0) {
		byte bytes[] = encodeAsBytes(direction, sect0, supl0);
		return new String(bytes);
	}
	
	public byte[] encodeAsBytes(int direction, int sect0, int supl0) {
		NoiseReductionParameters parameters = SNRManager.getInstance().getParameters(sect0, supl0);
		
		byte bytes[] = new byte[112];

		for (int wire = 0; wire < 112; wire++) {
			int index = 111 - wire;
			if (direction == LEFT) {
				if (parameters.getLeftSegments().checkBit(wire)) {
					int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.LEFT_LEAN, wire);
					bytes[index] = (byte) ('a' + numMiss);
				} else {
					bytes[index] = '.';
				}
			} else {
				if (parameters.getRightSegments().checkBit(wire)) {
					int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.RIGHT_LEAN, wire);
					bytes[index] = (byte) ('a' + numMiss);
				} else {
					bytes[index] = '.';
				}
			}
		}
		
		return bytes;
	}

	// add feedback string with common color
	private void addFBStr(String s, List<String> feedbackStrings) {
		feedbackStrings.add(_fbColor + s);
	}

	/**
	 * So that we can see the snr parameters
	 * 
	 * @param sector          the sector [1..6]
	 * @param superlayer      the superlayer [1..6]
	 * @param feedbackStrings the strings to add to
	 */
	public void addParametersToFeedback(int sector, int superlayer, List<String> feedbackStrings) {
		NoiseReductionParameters params = getParameters(sector - 1, superlayer - 1);
		addFBStr("snr parameters: ", feedbackStrings);
		addFBStr("allowed missing layers " + params.getAllowedMissingLayers(), feedbackStrings);

		int[] ls = params.getLeftLayerShifts();
		int[] rs = params.getRightLayerShifts();

		addFBStr(String.format("left shifts  [%d, %d, %d, %d, %d, %d]", ls[0], ls[1], ls[2], ls[3], ls[4], ls[5]),
				feedbackStrings);
		addFBStr(String.format("right shifts [%d, %d, %d, %d, %d, %d]", rs[0], rs[1], rs[2], rs[3], rs[4], rs[5]),
				feedbackStrings);

	}

	/**
	 * Clear the SNR data
	 */
	public void clear() {
		_noisePackage.clear();
		_noiseResults.clear();
	}

	/**
	 * Perform the SNR analysis
	 * 
	 * @param particleHits the input hit data
	 */
	public void analyzeSNR(List<ParticleHits> particleHits) {
		clear();

		if ((particleHits == null) || particleHits.isEmpty()) {
			return;
		}

		// total dc hit count
		int dcCount = 0;
		for (ParticleHits ph : particleHits) {
			dcCount += ph.totalHitCount(DetectorId.DC);
		}

		if (dcCount < 1) {
			return;
		}

		int sector[] = new int[dcCount];
		int superlayer[] = new int[dcCount];
		int layer[] = new int[dcCount];
		int wire[] = new int[dcCount];

		int index = 0;
		for (ParticleHits ph : particleHits) {
			List<AugmentedDetectorHit> dcHits = ph.getAllHits(DetectorId.DC);
			if ((dcHits != null) && !dcHits.isEmpty()) {
				for (AugmentedDetectorHit aughit : dcHits) {
					DetectorHit hit = aughit._hit;
					// a hit has 0-based indices noise package wants 1-based
					sector[index] = hit.getSectorId() + 1;
					superlayer[index] = hit.getSuperlayerId() + 1;
					layer[index] = hit.getLayerId() + 1;
					wire[index] = hit.getComponentId() + 1;
					index++;
				}
			}
		}

		_noisePackage.findNoise(sector, superlayer, layer, wire, _noiseResults);

		// now augment
		index = 0;
		boolean[] isNoise = _noiseResults.noise;
		for (ParticleHits ph : particleHits) {
			List<AugmentedDetectorHit> dcHits = ph.getAllHits(DetectorId.DC);
			if ((dcHits != null) && !dcHits.isEmpty()) {
				for (AugmentedDetectorHit aughit : dcHits) {
					aughit.setNoise(isNoise[index]);
					index++;
				}
			}
		}

	}

	/**
	 * Get the total number of noise hits in the current event
	 * 
	 * @return the total number of noise hits
	 */
	public int getNoiseCount() {
		if (_noiseResults == null) {
			return 0;
		}
		return _noiseResults.noiseCount();
	}

}