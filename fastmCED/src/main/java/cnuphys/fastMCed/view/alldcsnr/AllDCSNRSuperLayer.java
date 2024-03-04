package cnuphys.fastMCed.view.alldcsnr;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jlab.geom.DetectorId;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.item.RectangleItem;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.fastMCed.eventio.PhysicsEventManager;
import cnuphys.fastMCed.fastmc.AugmentedDetectorHit;
import cnuphys.fastMCed.fastmc.ParticleHits;
import cnuphys.fastMCed.snr.SNRManager;
import cnuphys.fastMCed.streaming.StreamManager;
import cnuphys.fastMCed.view.alldc.AllDCView;
import cnuphys.lund.LundId;
import cnuphys.lund.LundStyle;
import cnuphys.snr.NoiseReductionParameters;

public class AllDCSNRSuperLayer extends RectangleItem {

	public static final int LAYER6 = 0;
	public static final int LAYER5 = 1;
	public static final int LAYER4 = 2;
	public static final int LAYER3 = 3;
	public static final int LAYER2 = 4;
	public static final int LAYER1 = 5;

	public static final int RIGHT = 6;
	public static final int LEFT  = 7;
	
	// for hits cells
	private static final Color _defaultHitCellFill = Color.red;
	private static final Color _defaultHitCellLine = X11Colors.getX11Color("Dark Red");

	private static final Color rowCol1 = X11Colors.getX11Color("alice blue");
	private static final Color rowCol2 = X11Colors.getX11Color("azure");
	private static final Color rowCol3 = X11Colors.getX11Color("ghost white");

	public static final String[] rowNames = { " L6", " L5", " L4", " L3", " L2", " L1", " R", " L", };
	public static final Color[] rowColors = { rowCol1, rowCol2, rowCol1, rowCol2, rowCol1, rowCol2, Color.white, rowCol3};
	
	private static final Color violet = X11Colors.getX11Color("violet");
	private static final Color missingColors[] = { Color.red, Color.orange, Color.yellow, Color.green, Color.blue,
			violet };

	// convenient access to the event manager
	private PhysicsEventManager _eventManager = PhysicsEventManager.getInstance();

	// font for label text
	private static final Font labelFont = Fonts.commonFont(Font.PLAIN, 14);

	// the super layer [1..6]
	private int _superLayer;
	
	// cell overlay transparent color
	private static final Color cellOverlayColor = new Color(180, 180, 180, 32);

	
	// the view that owns this superlayer
	private AllDCSNRView _view;
	
	// this is the world rectangle that defines the super layer
	private Rectangle2D.Double _worldRectangle;
	
	// result rects two per superlayer (L and R(
	private Rectangle2D.Double _resultWorldRects[] = new Rectangle2D.Double[8];
	
	// cache the "position" rects which span the superlayer. 
	//That is, a wire is the intersection of the layer rect and the position rect
	private Rectangle2D.Double _positionWorldRects[];


	
	/**
	 * Constructor for a geometrically unfaithful "all dc snr" superlayer.
	 * 
	 * @param layer          the Layer this item is on.
	 * @param view           the AllDCView parent
	 * @param worldRectangle the boundaries which are not the real boundaries.
	 * @param sector         the sector [0..5]
	 * @param superLayer     the superLayer [0..5]
	 */
	public AllDCSNRSuperLayer(ItemList layer, AllDCSNRView view, 
			Rectangle2D.Double worldRectangle, int superLayer) {
		super(layer, worldRectangle);
		_worldRectangle = worldRectangle;
		_view = view;

		_style.setFillColor(Color.white);
		_style.setLineColor(Color.black);
		
		setResultRects();
		setPositionRects();


		_superLayer = superLayer + 1; // convert to 1-based

	}
	
	// cache the result outline rectangles
	private void setResultRects() {
		double dy = _worldRectangle.height / 8;
		double x = _worldRectangle.x;
		double y = _worldRectangle.y;
		double w = _worldRectangle.width;

		for (int i = 7; i >= 0; i--) {
			// trick to invert layers in lower sector
			_resultWorldRects[i] = new Rectangle2D.Double(x, y, w, dy);
			y += dy;
		}
	}

	// cache the position rectangles
	private void setPositionRects() {
		_positionWorldRects = new Rectangle2D.Double[112];

		double dx = _worldRectangle.width / 112;
		double x = _worldRectangle.x;
		double y = _worldRectangle.y;
		double h = _worldRectangle.height;

		// note counting right to left
		for (int i = 0; i < 112; i++) {
			_positionWorldRects[112 - i - 1] = new Rectangle2D.Double(x, y, dx, h);
			x += dx;
		}
	}
	
	//get the sector from the view [1..6]
	private int sector() {
		return _view.getSector();
	}


	/**
	 * Custom drawer for the item.
	 * 
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {

		if (StreamManager.getInstance().isStarted()) {
			return;
		}

		super.drawItem(g, container); // draws rectangular shell
		

		// shade the result rectangles
		g.setFont(labelFont);
		for (int i = 0; i < 8; i++) {
			Rectangle2D.Double wr = _resultWorldRects[i];
			WorldGraphicsUtilities.drawWorldRectangle(g, container, wr, rowColors[i], Color.black);
			g.setColor(Color.black);
			WorldGraphicsUtilities.drawWorldText(g, container, wr.x, wr.y, 
					"" + _superLayer + rowNames[i], -30, -5);
		}
		
		// draw SNR masks?
		if (_view.showMasks()) {
			drawSNRMasks(g, container);
		}

		// now the raw data
		drawHitData(g, container);
		
		// draw the snr segment locations
		drawSNRSegmentLocations(g, container);
		
		// causes cell shading
		for (int i = 0; i < 112; i += 2) {
			WorldGraphicsUtilities.drawWorldRectangle(g, container, _positionWorldRects[i], cellOverlayColor, null);

		}

		
		// just to make clean
		g.setColor(_style.getLineColor());
		g.drawPolygon(_lastDrawnPolygon);

	}
	
	// mark location SNR thinks segments might start
	private void drawSNRSegmentLocations(Graphics g, IContainer container) {
		// need zero based sector and super layer
		NoiseReductionParameters parameters = SNRManager.getInstance().getParameters(sector() - 1, _superLayer - 1);

		Rectangle2D.Double wr = new Rectangle2D.Double();

		for (int wire = 0; wire < parameters.getNumWire(); wire++) {
			// where it thinks segments start
			boolean leftSeg = parameters.getLeftSegments().checkBit(wire);
			boolean rightSeg = parameters.getRightSegments().checkBit(wire);
			
			
			
			if (leftSeg || rightSeg) {
				if (leftSeg) {
					int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.LEFT_LEAN, wire);
					drawSegmentLocation(g, container, LEFT, wire + 1, missingColors[numMiss], wr);
				}
				if (rightSeg) {
					int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.RIGHT_LEAN, wire);
					drawSegmentLocation(g, container, RIGHT, wire + 1, missingColors[numMiss], wr);
				}
			}
		}
	}

	/**
	 * Draw a segment location
	 * 
	 * @param g         the graphics context
	 * @param container the rendering container
	 * @param row       RIGHT or LEFT
	 * @param wire      the 1-based wire
	 * @param color     the color to use
	 * @param wr        essentially workspace
	 */
	private void drawSegmentLocation(Graphics g, IContainer container, int row, int wire, Color color, Rectangle2D.Double wr) {
		getCell(row, wire, wr);
		WorldGraphicsUtilities.drawWorldOval(g, container, wr, color, Color.black);
	}

	
	// draw the SNR mask data where SNR thinks segments might start
	private void drawSNRMasks(Graphics g, IContainer container) {
		// need zero based sector and super layer
		NoiseReductionParameters parameters = SNRManager.getInstance().getParameters(sector() - 1, _superLayer - 1);

		Rectangle2D.Double wr = new Rectangle2D.Double();

		for (int wire = 0; wire < parameters.getNumWire(); wire++) {
			// where it thinks segments start
			boolean leftSeg = parameters.getLeftSegments().checkBit(wire);
			boolean rightSeg = parameters.getRightSegments().checkBit(wire);
			if (leftSeg || rightSeg) {
				if (leftSeg) {
					drawMask(g, container, wire, parameters.getLeftLayerShifts(), 1, wr);
				}
				if (rightSeg) {
					drawMask(g, container, wire, parameters.getRightLayerShifts(), -1, wr);
				}
			}
		}
	}
	
	
	/**
	 * Draws the masking that shows where the noise algorithm thinks there are
	 * segments. Anything not masked is noise.
	 * 
	 * @param g         the graphics context.
	 * @param container the rendering container
	 * @param wire      the ZERO BASED wire 0..
	 * @param shifts    the parameter shifts for this direction
	 * @param sign      the direction 1 for left -1 for right
	 * @param wr        essentially workspace
	 */
	private void drawMask(Graphics g, IContainer container, int wire, int shifts[], int sign, Rectangle2D.Double wr) {

		wire++; // convert to 1-based

		Color fill;
		if (sign == 1) {
			fill = SNRManager.maskFillLeft;
		} else {
			fill = SNRManager.maskFillRight;
		}

		for (int layer = 1; layer <= 6; layer++) {
			int row = 6 - layer;

			getCell(row, wire, wr);
			WorldGraphicsUtilities.drawWorldRectangle(g, container, wr, fill, null);

			// ugh -- shifts are 0-based
			for (int shift = 1; shift <= shifts[layer - 1]; shift++) {
				int tempWire = wire + sign * shift;
				if ((tempWire > 0) && (tempWire <= 112)) {
					getCell(row, tempWire, wr);
					WorldGraphicsUtilities.drawWorldRectangle(g, container, wr, fill, null);
				}
			}
		}

	}

	
	/**
	 * Draw in single event mode
	 * 
	 * @param g         the graphics context
	 * @param container the rendering container
	 */
	private void drawHitData(Graphics g, IContainer container) {

		Rectangle2D.Double wr = new Rectangle2D.Double(); // used over and over

		List<ParticleHits> hits = _eventManager.getParticleHits();

		if (hits != null) {
			for (ParticleHits particleHits : hits) { // essentially a loop over tracks
				LundId lid = particleHits.getLundId();

				List<AugmentedDetectorHit> augHits = particleHits.getHits(DetectorId.DC, sector() - 1, _superLayer - 1);

				if (augHits != null) {
					for (AugmentedDetectorHit hit : augHits) {
						// get 1-based
						int layer = hit.getLayerId() + 1;
						int wire = hit.getComponentId() + 1;

						drawDCHit(g, container, layer, wire, hit.isNoise(), lid, wr);
					}
				}
			}
		}
	}

	/**
	 * Draw a single dc hit
	 * 
	 * @param g         the graphics context
	 * @param container the rendering container
	 * @param layer     the 1-based layer
	 * @param wire      the 1-based wire
	 * @param noise     is it noise?
	 * @param lid       the lund id
	 * @param wr        essentially workspace
	 */
	private void drawDCHit(Graphics g, IContainer container, int layer, int wire, boolean noise, LundId lid,
			Rectangle2D.Double wr) {

		// might not even draw it if it is noise
		if (noise && _view.hideNoise()) {
			return;
		}

		if (wire > 112) {
			String msg = "Bad wire number in drawGemcDCHit " + wire + " event number " + _eventManager.eventNumber();
			Log.getInstance().warning(msg);
			System.err.println(msg);
			return;
		}

		//convert layer to row
		int row = 6 - layer;
		getCell(row, wire, wr);

		Color hitFill = _defaultHitCellFill;
		Color hitLine = _defaultHitCellLine;

		// do we have simulated "truth" data?
		if (lid != null) {
			LundStyle style = lid.getStyle();
			if (style != null) {
				hitFill = lid.getStyle().getFillColor();
				hitFill = new Color(hitFill.getRed(), hitFill.getGreen(), hitFill.getBlue(), 128);
				hitLine = hitFill.darker();
			}
		}

		if (noise && _view.showNoiseAnalysis()) {
			hitFill = Color.black;
		}

		WorldGraphicsUtilities.drawWorldRectangle(g, container, wr, hitFill, hitLine);
	}
	
	
	/**
	 * For the given world point return the 1-based wire.
	 * 
	 * @param worldPoint the point in question
	 * @return the wire [1..]
	 */
	private int getWire(Point2D.Double worldPoint) {
		if (_worldRectangle.contains(worldPoint)) {
			for (int i = 0; i < 112; i++) {
				if (_positionWorldRects[i].contains(worldPoint)) {
					return i + 1; // convert to 1-based
				}
			}
		}

		return -1;
	}
	
	/**
	 * Get the world rectangle for a given cell (the wire is in the center)
	 * 
	 * @param row the 0-based row [0..6 for layers 6..1, 7 for R, 8 for L]
	 * @param wire  the 1-based wire [1..] return the world rectangle cell for this
	 *              layer, wire
	 */
	public void getCell(int row, int wire, Rectangle2D.Double wr) {

		int wm1 = wire - 1;

		Rectangle2D.Double leanRect = _resultWorldRects[row];
		Rectangle2D.Double positionRect = _positionWorldRects[wm1];
		wr.setFrame(positionRect.x, leanRect.y, positionRect.width, leanRect.height);

	}

	/**
	 * Add any appropriate feedback strings panel.
	 * 
	 * @param container       the Base container.
	 * @param screenPoint     the mouse location.
	 * @param worldPoint      the corresponding world point.
	 * @param feedbackStrings the List of feedback strings to add to.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		if (StreamManager.getInstance().isStarted()) {
			return;
		}

		if (_worldRectangle.contains(worldPoint)) {
			feedbackStrings.add("superlayer " + _superLayer);

			int row = getRow(worldPoint); // 0: R 1: L
			int wire = getWire(worldPoint); // 1-based

			if ((wire > 0) && (wire <= 112) && (row >= 0)) {
				feedbackStrings.add(rowNames[row] + " wire " + wire);
				
				int layer = 6 - row;
				if (layer > 0) {
					singleEventFeedbackStrings(layer, wire, feedbackStrings);
				}
			}
		}
	}
	
	/**
	 * Get the feedback strings for single event mode
	 * 
	 * @param layer           [1..6]
	 * @param wire            [1..112]
	 * @param feedbackStrings
	 */
	private void singleEventFeedbackStrings(int layer, int wire, List<String> feedbackStrings) {

		List<ParticleHits> hits = _eventManager.getParticleHits();
		int wire0 = wire - 1;
		int layer0 = layer - 1;

		if (hits != null) {
			for (ParticleHits particleHits : hits) {
				LundId lid = particleHits.getLundId();

				List<AugmentedDetectorHit> augHits = particleHits.getHits(DetectorId.DC, sector() - 1, _superLayer - 1);

				if (augHits != null) {
					for (AugmentedDetectorHit hit : augHits) {
						if ((hit.getLayerId() == layer0) && (hit.getComponentId() == wire0)) {

							// might not even care if it is noise
							if (hit.isNoise() && _view.hideNoise()) {
								break;
							}

							ParticleHits.addHitFeedback(hit, lid, feedbackStrings);
							break;
						}
					}
				}
			}

		}
		
		
		SNRManager.getInstance().addParametersToFeedback(sector(), _superLayer, feedbackStrings);

	}
	
	/**
	 * For the given world point return the 1-based layer.
	 * 
	 * @param worldPoint the point in question
	 * @return the 0-based row [0..6 for layers 6..1, 7 for R, 8 for L]
	 */
	private int getRow(Point2D.Double worldPoint) {
		if (_worldRectangle.contains(worldPoint)) {
			for (int i = 0; i < 8; i++) {
				if (_resultWorldRects[i].contains(worldPoint)) {
					return i;
				}
			}
		}

		return -1;
	}



}
