package cnuphys.fastMCed.view.alldcsnr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.SwingConstants;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.style.Styled;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.fastMCed.graphics.Sector;
import cnuphys.fastMCed.graphics.SectorSelector;
import cnuphys.fastMCed.view.AView;
import cnuphys.fastMCed.view.ControlPanel;

/**
 * The AllDC_SRN view is a non-faithful representation of all six sectors of
 * driftchambers. It shows the SNR data used for machine learning.
 * 
 * @author heddle
 * 
 */

@SuppressWarnings("serial")
public class AllDCSNRView extends AView {

	// for naming clones
	private static int CLONE_COUNT = 0;

	// base title
	private static final String _baseTitle = "SNR Machine Learning Inputs";


	// font for label text
	private static final Font labelFont = Fonts.commonFont(Font.PLAIN, 11);

	/**
	 * Used for drawing the sector rects.
	 */
	private Styled _sectorStyle;
	
	
	//the current sector
	private Sector _sector = Sector.SECTOR1;

	// The optional "before" drawer for this view
	private IDrawable _beforeDraw;

	// transparent color
	private static final Color TRANS = new Color(255, 255, 0, 80);


	// all the superlayer items indexed by superlayer (0..5)
	private AllDCSNRSuperLayer _superLayerItems[] = new AllDCSNRSuperLayer[6];

	private static Rectangle2D.Double _defaultWorldRectangle = new Rectangle2D.Double(0.0, 0.0, 1.0, 1.0);
	
	/**
	 * Create an allDCView
	 * 
	 * @param keyVals variable set of arguments.
	 */
	private AllDCSNRView(Object... keyVals) {
		super(keyVals);

		setBeforeDraw();
		setAfterDraw();
		addItems();
		
		// add the sector selector
		SectorSelector selector = new SectorSelector(Fonts.defaultLargeFont, SwingConstants.HORIZONTAL);
        selector.addSelectionChangeListener(e -> {
        	_sector = Sector.fromName(e.getActionCommand());
             refresh();
        });
        
        add(selector, BorderLayout.SOUTH);
	}
	
	/**
	 * Convenience method for creating an AllDC View.
	 * 
	 * @return a new AllDCView.
	 */
	public static AllDCSNRView createAllDCSNRView() {
		AllDCSNRView view = null;

		// set to a fraction of screen
		Dimension d = GraphicsUtilities.screenFraction(0.75);

		// create the view
		view = new AllDCSNRView(PropertySupport.WORLDSYSTEM, _defaultWorldRectangle, PropertySupport.WIDTH, d.width, 
				PropertySupport.BACKGROUND, X11Colors.getX11Color("mint cream"),
				PropertySupport.HEIGHT, d.height,
				PropertySupport.TOOLBAR, true, PropertySupport.TOOLBARBITS, AView.TOOLBARBITS, PropertySupport.VISIBLE,
				true, PropertySupport.TITLE, _baseTitle + ((CLONE_COUNT == 0) ? "" : ("_(" + CLONE_COUNT + ")")),
				PropertySupport.STANDARDVIEWDECORATIONS, true);

		view._controlPanel = new ControlPanel(view, ControlPanel.NOISECONTROL + ControlPanel.FEEDBACK, 0, 3, 5);

		view.add(view._controlPanel, BorderLayout.EAST);
		view.pack();
		return view;
	}

	
	/**
	 * Create the before drawer to draw the sector outlines.
	 */
	private void setBeforeDraw() {
		// style for sector rects
		_sectorStyle = new Styled(X11Colors.getX11Color("dark green"));
		_sectorStyle.setLineColor(Color.lightGray);

		// use a before-drawer to sector dividers and labels
		_beforeDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {
			}

		};

		getContainer().setBeforeDraw(_beforeDraw);
	}
	
	/**
	 * Set the views before draw
	 */
	private void setAfterDraw() {
		IDrawable _afterDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {
			}

		};
		getContainer().setAfterDraw(_afterDraw);
	}


	/**
	 * This adds the detector items. The AllDC view is not faithful to geometry. All
	 * we really uses in the number of superlayers, number of layers, and number of
	 * wires.
	 */
	private void addItems() {
		// use sector 0 all the same
		ItemList detectorLayer = getContainer().getItemList(_detectorLayerName);
		
		double gap = 0.02;
		double x = 0.05;
		double width = 1.0 - 1.05 * x;
		double height = (1.0 - 7 * gap) / 6.0;

		for (int superLayer = 0; superLayer < 6; superLayer++) {
			Rectangle2D.Double wr = new Rectangle2D.Double(x, gap + superLayer * (height + gap), width, height);

			_superLayerItems[superLayer] = new AllDCSNRSuperLayer(detectorLayer, this, wr, superLayer);
		}


	}

	/**
	 * Get the sector corresponding to the current pointer location..
	 * 
	 * @param container   the base container for the view.
	 * @param screenPoint the pixel point
	 * @param worldPoint  the corresponding world location.
	 * @return the sector [1..6] or -1 for none.
	 */
	@Override
	public int getSector(IContainer container, Point screenPoint, Point2D.Double worldPoint) {
		return getSector();
	}
	
	/**
	 * Get the sector
	 * 
	 * @return the sector [1..6]
	 */
	public int getSector() {
		return _sector.ordinal() + 1;
	}


	/**
	 * Some view specific feedback. Should always call super.getFeedbackStrings
	 * first.
	 * 
	 * @param container   the base container for the view.
	 * @param screenPoint the pixel point
	 * @param worldPoint  the corresponding world location.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		// get the common information
		super.getFeedbackStrings(container, screenPoint, worldPoint, feedbackStrings);

		int sector = getSector(container, screenPoint, worldPoint);

		if (sector > 0) {
		}

	}

}
