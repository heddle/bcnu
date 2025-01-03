package cnuphys.ced.ced3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.awt.GLJPanel;

import bCNU3D.Panel3D;
import cnuphys.bCNU.component.checkboxarray.CheckBoxArray;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.view.VirtualView;
import cnuphys.ced.ced3d.view.PlainView3D;
import cnuphys.lund.X11Colors;
import cnuphys.magfield.MagneticFields;
import item3D.Axes3D;

public abstract class PlainPanel3D extends Panel3D {

	// cm
	private final float xymax = 600f;
	private final float zmax = 600f;
	private final float zmin = -100f;

	private static final Color _torusColor = new Color(0, 255, 255, 255);
	private static final Color _solenoidColor = new Color(255, 0, 255, 255);

	// labels for the check box
	public static final String SHOW_VOLUMES = "Volumes";
	public static final String SHOW_TRUTH = "Truth";
	public static final String SHOW_DC = "DC";
	public static final String SHOW_ECAL = "ECAL";
	public static final String SHOW_PCAL = "PCAL";
	public static final String SHOW_FTOF = "FTOF";
//	public static final String SHOW_SIM_SDOCA = "SDOCA";
//	public static final String SHOW_TB_DOCA = "TB DOCA";
	public static final String SHOW_RECON_FTOF = "Rec-FTOF";
	public static final String SHOW_RECON_CROSSES = "Crosses";
	public static final String SHOW_BST = "BST";
	public static final String SHOW_BST_LAYER_1 = "BST Layer 1";
	public static final String SHOW_BST_LAYER_2 = "BST Layer 2";
	public static final String SHOW_BST_LAYER_3 = "BST Layer 3";
	public static final String SHOW_BST_LAYER_4 = "BST Layer 4";
	public static final String SHOW_BST_LAYER_5 = "BST Layer 5";
	public static final String SHOW_BST_LAYER_6 = "BST Layer 6";
	public static final String SHOW_BST_LAYER_7 = "BST Layer 7";
	public static final String SHOW_BST_LAYER_8 = "BST Layer 8";
	public static final String SHOW_BST_HITS = "BST Hits";

	public static final String SHOW_BMT = "BMT";
	public static final String SHOW_BMT_LAYER_1 = "BMT Layer 1";
	public static final String SHOW_BMT_LAYER_2 = "BMT Layer 2";
	public static final String SHOW_BMT_LAYER_3 = "BMT Layer 3";
	public static final String SHOW_BMT_LAYER_4 = "BMT Layer 4";
	public static final String SHOW_BMT_LAYER_5 = "BMT Layer 5";
	public static final String SHOW_BMT_LAYER_6 = "BMT Layer 6";
	public static final String SHOW_BMT_HITS = "BMT Hits";

	public static final String SHOW_SECTOR_1 = "Sector 1";
	public static final String SHOW_SECTOR_2 = "Sector 2";
	public static final String SHOW_SECTOR_3 = "Sector 3";
	public static final String SHOW_SECTOR_4 = "Sector 4";
	public static final String SHOW_SECTOR_5 = "Sector 5";
	public static final String SHOW_SECTOR_6 = "Sector 6";

	public static final String SHOW_CTOF = "CTOF";

	public static final String SHOW_CND = "CND";
	public static final String SHOW_CND_LAYER_1 = "CND Layer 1";
	public static final String SHOW_CND_LAYER_2 = "CND Layer 2";
	public static final String SHOW_CND_LAYER_3 = "CND Layer 3";

	public static final String SHOW_TB_CROSS = "TB Cross";
	public static final String SHOW_HB_CROSS = "HB Cross";

	public static final String SHOW_AITB_CROSS = "AITB Cross";
	public static final String SHOW_AIHB_CROSS = "AIHB Cross";

	public static final String SHOW_TB_TRACK = "Reg TB Track";
	public static final String SHOW_HB_TRACK = "Reg HB Track";

	public static final String SHOW_AITB_TRACK = "AI TB Track";
	public static final String SHOW_AIHB_TRACK = "AI HB Track";

	public static final String SHOW_CVTREC_TRACK = "CVT Rec Track";
	public static final String SHOW_CVTP1_TRACK = "CVT P1 Track";

	public static final String SHOW_REC_TRACK = "REC Track";

	public static final String SHOW_MAP_EXTENTS = "Map Extents";

	public static final String SHOW_COSMIC = "Cosmics";

	public static final String SHOW_REC_CAL = "REC Cal";

	// for ALert TOF
	public static final String TOF_SUPLAY1_LAY1 = "TOF_Suplay1_Lay1";
	public static final String TOF_SUPLAY2_LAY1 = "TOF_Suplay2_Lay1";
	public static final String TOF_SUPLAY2_LAY2 = "TOF_Suplay2_Lay2";
	public static final String TOF_SUPLAY2_LAY3 = "TOF_Suplay2_Lay3";
	public static final String TOF_SUPLAY2_LAY4 = "TOF_Suplay2_Lay4";
	public static final String TOF_SUPLAY2_LAY5 = "TOF_Suplay2_Lay5";
	public static final String TOF_SUPLAY2_LAY6 = "TOF_Suplay2_Lay6";
	public static final String TOF_SUPLAY2_LAY7 = "TOF_Suplay2_Lay7";
	public static final String TOF_SUPLAY2_LAY8 = "TOF_Suplay2_Lay8";
	public static final String TOF_SUPLAY2_LAY9 = "TOF_Suplay2_Lay9";
	public static final String TOF_SUPLAY2_LAY10 = "TOF_Suplay2_Lay10";
	public static final String SHOW_TOF = "TOF";

	// for Alert DC
	public static final String DC_SUPLAY1_LAY1 = "DC_Suplay1_Lay1";
//	public static final String DC_SUPLAY1_LAY2 = "DC_Suplay1_Lay2";
	public static final String DC_SUPLAY2_LAY1 = "DC_Suplay2_Lay1";
	public static final String DC_SUPLAY2_LAY2 = "DC_Suplay2_Lay2";
	public static final String DC_SUPLAY3_LAY1 = "DC_Suplay3_Lay1";
	public static final String DC_SUPLAY3_LAY2 = "DC_Suplay3_Lay2";
	public static final String DC_SUPLAY4_LAY1 = "DC_Suplay4_Lay1";
	public static final String DC_SUPLAY4_LAY2 = "DC_Suplay4_Lay2";
	public static final String DC_SUPLAY5_LAY1 = "DC_Suplay5_Lay1";
//	public static final String DC_SUPLAY5_LAY2 = "DC_Suplay5_Lay2";
	
	public static final String SHOW_FMT_LAYER_1 = "FMT Layer 1";
	public static final String SHOW_FMT_LAYER_2 = "FMT Layer 2";
	public static final String SHOW_FMT_LAYER_3 = "FMT Layer 3";
	public static final String SHOW_FMT_LAYER_4 = "FMT Layer 4";
	public static final String SHOW_FMT_LAYER_5 = "FMT Layer 5";
	public static final String SHOW_FMT_LAYER_6 = "FMT Layer 6";



	// Check box array
	protected CheckBoxArray _checkBoxArray;

	// alpha slider for volume alphas
	protected AlphaSlider _volumeAlphaSlider;

	// display array labels
	protected String _cbaLabels[];

	protected PlainView3D _view;

	/*
	 * The panel that holds the 3D objects
	 *
	 * @param angleX the initial x rotation angle in degrees
	 *
	 * @param angleY the initial y rotation angle in degrees
	 *
	 * @param angleZ the initial z rotation angle in degrees
	 *
	 * @param xdist move viewpoint left/right
	 *
	 * @param ydist move viewpoint up/down
	 *
	 * @param zdist the initial viewer z distance should be negative
	 */
	public PlainPanel3D(PlainView3D view, float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist, float bgRed, float bgGreen, float bgBlue, String... cbaLabels) {

		super(angleX, angleY, angleZ, xDist, yDist, zDist, bgRed, bgGreen, bgBlue);

		_view = view;
		_cbaLabels = cbaLabels;
		_volumeAlphaSlider = new AlphaSlider(this, "Volume alpha");

		gljpanel.setBorder(new CommonBorder());
		final GLJPanel gljp = gljpanel;

		addEast();
		addNorth();
		addWest();
		addSouth();

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
				gljp.requestFocus();
			}

		};
		for (String s : _cbaLabels) {
			AbstractButton ab = _checkBoxArray.getButton(s);
			ab.setFont(Fonts.smallFont);
			ab.setSelected(!SHOW_MAP_EXTENTS.equals(s));
			ab.addActionListener(al);
		}
		fixSize();
	}

	@Override
	public void createInitialItems() {
		// coordinate axes
		Axes3D axes = new Axes3D(this, -xymax, xymax, -xymax, xymax, zmin, zmax, null, Color.darkGray, 1f, 7, 7, 8,
				Color.black, X11Colors.getX11Color("Dark Green"), new Font("SansSerif", Font.PLAIN, 12), 0);
		addItem(axes);

		// trajectory drawer
		TrajectoryDrawer3D trajDrawer = new TrajectoryDrawer3D(this);
		addItem(trajDrawer);

		// dc super layers
		for (int sector = 1; sector <= 6; sector++) {
			for (int superlayer = 1; superlayer <= 6; superlayer++) {
				DCSuperLayer3D dcsl = new DCSuperLayer3D(this, sector, superlayer);
				addItem(dcsl);
			}
		}

		// tof paddles
		for (int sector = 1; sector <= 6; sector++) {
			addItem(new FTOF3D(this, sector));
		}

		// pcal
		for (int sector = 1; sector <= 6; sector++) {
			for (int view = 1; view <= 3; view++) {
				PCALViewPlane3D pcalvp = new PCALViewPlane3D(this, sector, view);
				addItem(pcalvp);
			}
		}

		// EC planes
		for (int sector = 1; sector <= 6; sector++) {
			for (int stack = 1; stack <= 2; stack++) {
				for (int view = 1; view <= 3; view++) {
					ECViewPlane3D ecvp = new ECViewPlane3D(this, sector, stack, view);
					addItem(ecvp);
				}
			}
		}

		// mag field boundaries
		if (MagneticFields.getInstance().hasActiveSolenoid()) {
			System.out.println("Adding 3D Solenoid Boundary");
			addItem(new FieldBoundary(this, MagneticFields.getInstance().getSolenoid(), _solenoidColor));
		}
		if (MagneticFields.getInstance().hasActiveTorus()) {
			System.out.println("Adding 3D Torus Boundary");
			addItem(new FieldBoundary(this, MagneticFields.getInstance().getTorus(), _torusColor));
		}

	} // create initial items

	@Override
	public void display(GLAutoDrawable drawable) {
		if (VirtualView.getInstance().isViewVisible(_view)) {
			super.display(drawable);
		} else {
	 		System.err.println("SKIPPED");
		}
	}

	// add north panel
	protected abstract void addNorth();

	// add west panel
	protected abstract void addWest();

	// add south panel
	protected abstract void addSouth();

	// add eastern panel
	private void addEast() {
		JPanel ep = new JPanel();
		ep.setLayout(new VerticalFlowLayout());

		ep.add(new KeyboardLegend(this));
		_checkBoxArray = new CheckBoxArray(2, 4, 4, _cbaLabels);

		AbstractButton ab = _checkBoxArray.getButton(SHOW_MAP_EXTENTS);
		if (ab != null) {
			ab.setSelected(false);
		}

		_checkBoxArray.setBorder(new CommonBorder());
		ep.add(_checkBoxArray);

		add(ep, BorderLayout.EAST);
	}

	/**
	 * Snapshot of the panel.
	 */
	@Override
	public void snapshot() {
		GraphicsUtilities.saveAsPng(this);
	}

	// a fixed fraction of the screen
	private void fixSize() {
		Dimension d = GraphicsUtilities.screenFraction(0.60);
		d.width = d.height;
		gljpanel.setPreferredSize(d);
	}

	/**
	 * Get the alpha for volume drawing
	 *
	 * @return the alpha for volume drawing
	 */
	public int getVolumeAlpha() {
		return _volumeAlphaSlider.getAlpha();
	}

	/**
	 * Get one of the display buttons
	 *
	 * @param label the button label
	 * @return the button or null on failure
	 */
	public AbstractButton getDisplayButton(String label) {
		if (_checkBoxArray == null) {
			return null;
		}
		return _checkBoxArray.getButton(label);
	}

	/**
	 * Get all the display buttons in an array
	 *
	 * @return all the display buttons
	 */
	public AbstractButton[] gatAllDisplayButtons() {
		if ((_checkBoxArray == null) || (_cbaLabels == null) || (_cbaLabels.length < 1)) {
			return null;
		}

		AbstractButton buttons[] = new AbstractButton[_cbaLabels.length];

		for (int i = 0; i < _cbaLabels.length; i++) {
			buttons[i] = _checkBoxArray.getButton(_cbaLabels[i]);
		}

		return buttons;
	}

	/**
	 * Check if a feature should be drawn
	 *
	 * @param label the label for the check box on the option array
	 * @return <code>true</code> if the feature should be drawn
	 */
	private boolean show(String label) {
		AbstractButton ab = _checkBoxArray.getButton(label);
		return (ab == null) ? false : ab.isSelected();
	}

	public void enableLabel(String label, boolean enabled) {
		AbstractButton ab = _checkBoxArray.getButton(label);
		if (ab != null) {
			ab.setEnabled(enabled);
		}
	}

	/**
	 * Show ECAL?
	 *
	 * @return <code>true</code> if we are to show ECAL
	 */
	public boolean showECAL() {
		return show(PlainPanel3D.SHOW_ECAL);
	}

	/**
	 * Show PCAL?
	 *
	 * @return <code>true</code> if we are to show PCAL
	 */
	public boolean showPCAL() {
		return show(PlainPanel3D.SHOW_PCAL);
	}

	/**
	 * Show forward TOF?
	 *
	 * @return <code>true</code> if we are to show FTOF
	 */
	public boolean showFTOF() {
		return show(PlainPanel3D.SHOW_FTOF);
	}

	/**
	 * Show CTOF?
	 *
	 * @return <code>true</code> if we are to show CTOF
	 */
	public boolean showCTOF() {
		return show(PlainPanel3D.SHOW_CTOF);
	}

	/**
	 * Show BST?
	 *
	 * @return <code>true</code> if we are to show BST
	 */
	public boolean showBST() {
		return show(PlainPanel3D.SHOW_BST);
	}

	/**
	 * Show BMT?
	 *
	 * @return <code>true</code> if we are to show BMT
	 */
	public boolean showBMT() {
		return show(PlainPanel3D.SHOW_BMT);
	}

	/**
	 * Show CND?
	 *
	 * @return <code>true</code> if we are to show CND
	 */
	public boolean showCND() {
		return show(PlainPanel3D.SHOW_CND);
	}

	/**
	 * Show CND Layer 1?
	 *
	 * @return <code>true</code> if we are to show CND Layer 1
	 */
	public boolean showCNDLayer1() {
		return showCND() && show(PlainPanel3D.SHOW_CND_LAYER_1);
	}

	/**
	 * Show CND Layer 2?
	 *
	 * @return <code>true</code> if we are to show CND Layer 2
	 */
	public boolean showCNDLayer2() {
		return showCND() && show(PlainPanel3D.SHOW_CND_LAYER_2);
	}

	/**
	 * Show CND Layer 3?
	 *
	 * @return <code>true</code> if we are to show CND Layer 3
	 */
	public boolean showCNDLayer3() {
		return showCND() && show(PlainPanel3D.SHOW_CND_LAYER_3);
	}

	/**
	 * Show reconstructed Crosses?
	 *
	 * @return <code>true</code> if we are to show reconstructed crosses
	 */
	public boolean showReconCrosses() {
		return show(PlainPanel3D.SHOW_RECON_CROSSES);
	}

	/**
	 * Show reconstructed FTOF?
	 *
	 * @return <code>true</code> if we are to show reconstructed ftof
	 */
	public boolean showReconFTOF() {
		return show(PlainPanel3D.SHOW_RECON_FTOF);
	}

	/**
	 * Show time based track?
	 *
	 * @return <code>true</code> if we are to show time based track
	 */
	public boolean showTBTrack() {
		return show(PlainPanel3D.SHOW_TB_TRACK);
	}

	/**
	 * Show hit based track?
	 *
	 * @return <code>true</code> if we are to show hit based track
	 */
	public boolean showHBTrack() {
		return show(PlainPanel3D.SHOW_HB_TRACK);
	}

	/**
	 * Show AI time based track?
	 *
	 * @return <code>true</code> if we are to show time based tracks
	 */
	public boolean showAITBTrack() {
		return show(PlainPanel3D.SHOW_AITB_TRACK);
	}

	/**
	 * Show AI hit based track?
	 *
	 * @return <code>true</code> if we are to show hit based tracks
	 */
	public boolean showAIHBTrack() {
		return show(PlainPanel3D.SHOW_AIHB_TRACK);
	}

	/**
	 * Show REC::Particle tracks?
	 *
	 * @return <code>true</code> if we are to show recon tracks
	 */
	public boolean showRecTrack() {
		return show(PlainPanel3D.SHOW_REC_TRACK);
	}

	/**
	 * Show REC::Calorimiter data?
	 *
	 * @return <code>true</code> if we are to REC::Calorimeter data
	 */
	public boolean showRecCal() {
		return show(PlainPanel3D.SHOW_REC_CAL);
	}

	/**
	 * Show field map extents
	 *
	 * @return <code>true</code> if we are to show torus and solenoid extent
	 */
	public boolean showMapExtents() {
		return show(PlainPanel3D.SHOW_MAP_EXTENTS);
	}

	/**
	 * Show cvt rec based track?
	 *
	 * @return <code>true</code> if we are to show cvt based track
	 */
	public boolean showCVTRecTrack() {
		return show(PlainPanel3D.SHOW_CVTREC_TRACK);
	}

	/**
	 * Show cvt pass 1based track?
	 *
	 * @return <code>true</code> if we are to show cvt based track
	 */
	public boolean showCVTP1Track() {
		return show(PlainPanel3D.SHOW_CVTP1_TRACK);
	}

	/**
	 * Show hit based cross?
	 *
	 * @return <code>true</code> if we are to show hit based cross
	 */
	public boolean showHBCross() {
		return show(PlainPanel3D.SHOW_HB_CROSS);
	}

	/**
	 * Show time based cross?
	 *
	 * @return <code>true</code> if we are to show time based cross
	 */
	public boolean showTBCross() {
		return show(PlainPanel3D.SHOW_TB_CROSS);
	}

	/**
	 * Show AI hit based cross?
	 *
	 * @return <code>true</code> if we are to show AI hit based cross
	 */
	public boolean showAIHBCross() {
		return show(PlainPanel3D.SHOW_AIHB_CROSS);
	}

	/**
	 * Show AI time based cross?
	 *
	 * @return <code>true</code> if we are to show AI time based cross
	 */
	public boolean showAITBCross() {
		return show(PlainPanel3D.SHOW_AITB_CROSS);
	}

	/**
	 * Show BST Hits?
	 *
	 * @return <code>true</code> if we are to show BST Hits
	 */
	public boolean showBSTHits() {
		return show(PlainPanel3D.SHOW_BST_HITS);
	}

	/**
	 * Show BMT Hits?
	 *
	 * @return <code>true</code> if we are to show BMT Hits
	 */
	public boolean showBMTHits() {
		return show(PlainPanel3D.SHOW_BMT_HITS);
	}

	/**
	 * Show DC?
	 *
	 * @return <code>true</code> if we are to show DC
	 */
	public boolean showDC() {
		return show(PlainPanel3D.SHOW_DC);
	}

	/**
	 * Show BST Layer 1?
	 *
	 * @return <code>true</code> if we are to show BST Layer 1
	 */
	public boolean showBSTLayer1() {
		return showBST() && show(PlainPanel3D.SHOW_BST_LAYER_1);
	}

	/**
	 * Show BST Layer 2?
	 *
	 * @return <code>true</code> if we are to show BST Layer 2
	 */
	public boolean showBSTLayer2() {
		return showBST() && show(PlainPanel3D.SHOW_BST_LAYER_2);
	}

	/**
	 * Show BST Layer 3?
	 *
	 * @return <code>true</code> if we are to show BST Layer 3
	 */
	public boolean showBSTLayer3() {
		return showBST() && show(PlainPanel3D.SHOW_BST_LAYER_3);
	}

	/**
	 * Show BST Layer 4?
	 *
	 * @return <code>true</code> if we are to show BST Layer 4
	 */
	public boolean showBSTLayer4() {
		return showBST() && show(PlainPanel3D.SHOW_BST_LAYER_4);
	}

	/**
	 * Show BST Layer 5?
	 *
	 * @return <code>true</code> if we are to show BST Layer 5
	 */
	public boolean showBSTLayer5() {
		return showBST() && show(PlainPanel3D.SHOW_BST_LAYER_5);
	}

	/**
	 * Show BST Layer 6?
	 *
	 * @return <code>true</code> if we are to show BST Layer 6
	 */
	public boolean showBSTLayer6() {
		return showBST() && show(PlainPanel3D.SHOW_BST_LAYER_6);
	}

	/**
	 * Show BMT Layer 1?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 1
	 */
	public boolean showBMTLayer1() {
		return showBMT() && show(PlainPanel3D.SHOW_BMT_LAYER_1);
	}

	/**
	 * Show BMT Layer 2?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 2
	 */
	public boolean showBMTLayer2() {
		return showBMT() && show(PlainPanel3D.SHOW_BMT_LAYER_2);
	}

	/**
	 * Show BMT Layer 3?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 3
	 */
	public boolean showBMTLayer3() {
		return showBMT() && show(PlainPanel3D.SHOW_BMT_LAYER_3);
	}

	/**
	 * Show BMT Layer 4?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 4
	 */
	public boolean showBMTLayer4() {
		return showBMT() && show(PlainPanel3D.SHOW_BMT_LAYER_4);
	}

	/**
	 * Show BMT Layer 5?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 5
	 */
	public boolean showBMTLayer5() {
		return showBMT() && show(PlainPanel3D.SHOW_BMT_LAYER_5);
	}

	/**
	 * Show BMT Layer 6?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 6
	 */
	public boolean showBMTLayer6() {
		return showBMT() && show(PlainPanel3D.SHOW_BMT_LAYER_6);
	}

	/**
	 * Show sector 1?
	 *
	 * @return <code>true</code> if we are to show sector 1
	 */
	public boolean showSector1() {
		return show(PlainPanel3D.SHOW_SECTOR_1);
	}

	/**
	 * Show sector 2?
	 *
	 * @return <code>true</code> if we are to show sector 2
	 */
	public boolean showSector2() {
		return show(PlainPanel3D.SHOW_SECTOR_2);
	}

	/**
	 * Show sector 3?
	 *
	 * @return <code>true</code> if we are to show sector 3
	 */
	public boolean showSector3() {
		return show(PlainPanel3D.SHOW_SECTOR_3);
	}

	/**
	 * Show sector 4?
	 *
	 * @return <code>true</code> if we are to show sector 4
	 */
	public boolean showSector4() {
		return show(PlainPanel3D.SHOW_SECTOR_4);
	}

	/**
	 * Show sector 5?
	 *
	 * @return <code>true</code> if we are to show sector 5
	 */
	public boolean showSector5() {
		return show(PlainPanel3D.SHOW_SECTOR_5);
	}

	/**
	 * Show sector 6?
	 *
	 * @return <code>true</code> if we are to show sector 6
	 */
	public boolean showSector6() {
		return show(PlainPanel3D.SHOW_SECTOR_6);
	}

	/**
	 * Show Cosmics?
	 *
	 * @return <code>true</code> if we are to show Cosmics
	 */
	public boolean showCosmics() {
		return show(PlainPanel3D.SHOW_COSMIC);
	}

	/**
	 * Show we show the 1-based sector?
	 *
	 * @param sector the sector [1..6]
	 * @return <code>true</code> if we are to show the sector
	 */
	public boolean showSector(int sector) {
		switch (sector) {
		case 1:
			return showSector1();
		case 2:
			return showSector2();
		case 3:
			return showSector3();
		case 4:
			return showSector4();
		case 5:
			return showSector5();
		case 6:
			return showSector6();
		}
		return false;
	}

	/**
	 * Show FMT Layer 1?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 1
	 */
	public boolean showFMTLayer1() {
		return show(PlainPanel3D.SHOW_FMT_LAYER_1);
	}

	/**
	 * Show FMT Layer 2?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 2
	 */
	public boolean showFMTLayer2() {
		return show(PlainPanel3D.SHOW_FMT_LAYER_2);
	}

	/**
	 * Show FMT Layer 3?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 3
	 */
	public boolean showFMTLayer3() {
		return show(PlainPanel3D.SHOW_FMT_LAYER_3);
	}

	/**
	 * Show FMT Layer 4?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 4
	 */
	public boolean showFMTLayer4() {
		return show(PlainPanel3D.SHOW_FMT_LAYER_4);
	}

	/**
	 * Show FMT Layer 5?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 5
	 */
	public boolean showFMTLayer5() {
		return show(PlainPanel3D.SHOW_FMT_LAYER_5);
	}

	/**
	 * Show FMT Layer 6?
	 *
	 * @return <code>true</code> if we are to show BMT Layer 6
	 */
	public boolean showFMTLayer6() {
		return show(PlainPanel3D.SHOW_FMT_LAYER_6);
	}
	
	public boolean showFMTLayer(int layer) {
		switch (layer) {
		case 1:
			return showFMTLayer1();
		case 2:
			return showFMTLayer2();
		case 3:
			return showFMTLayer3();
		case 4:
			return showFMTLayer4();
		case 5:
			return showFMTLayer5();
		case 6:
			return showFMTLayer6();
		}
		return false;
	}

	// Show methods
	/**
	 * Show TOF_Suplay1_Lay1?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay1_Lay1
	 */
	public boolean showTOF_SUPLAY1_LAY1() {
		return show(PlainPanel3D.TOF_SUPLAY1_LAY1);
	}

	/**
	 * Show TOF_Suplay2_Lay1?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay1
	 */
	public boolean showTOF_SUPLAY2_LAY1() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY1);
	}

	/**
	 * Show TOF_Suplay2_Lay2?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay2
	 */
	public boolean showTOF_SUPLAY2_LAY2() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY2);
	}

	/**
	 * Show TOF_Suplay2_Lay3?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay3
	 */
	public boolean showTOF_SUPLAY2_LAY3() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY3);
	}

	/**
	 * Show TOF_Suplay2_Lay4?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay4
	 */
	public boolean showTOF_SUPLAY2_LAY4() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY4);
	}

	/**
	 * Show TOF_Suplay2_Lay5?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay5
	 */
	public boolean showTOF_SUPLAY2_LAY5() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY5);
	}

	/**
	 * Show TOF_Suplay2_Lay6?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay6
	 */
	public boolean showTOF_SUPLAY2_LAY6() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY6);
	}

	/**
	 * Show TOF_Suplay2_Lay7?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay7
	 */
	public boolean showTOF_SUPLAY2_LAY7() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY7);
	}

	/**
	 * Show TOF_Suplay2_Lay8?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay8
	 */
	public boolean showTOF_SUPLAY2_LAY8() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY8);
	}

	/**
	 * Show TOF_Suplay2_Lay9?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay9
	 */
	public boolean showTOF_SUPLAY2_LAY9() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY9);
	}

	/**
	 * Show TOF_Suplay2_Lay10?
	 *
	 * @return <code>true</code> if we are to show TOF_Suplay2_Lay10
	 */
	public boolean showTOF_SUPLAY2_LAY10() {
		return show(PlainPanel3D.TOF_SUPLAY2_LAY10);
	}

	/**
	 * Show TOF?
	 *
	 * @return <code>true</code> if we are to show TOF
	 */
	public boolean showTOF() {
		return show(PlainPanel3D.SHOW_TOF);
	}


	// Generalized method to call appropriate show methods
	/**
	 * Show TOF for the given superlayer and layer.
	 *
	 * @param superlayer 1-based superlayer number
	 * @param layer      1-based layer number
	 * @return <code>true</code> if the appropriate layer is shown
	 */
	public boolean showTOF(int superlayer, int layer) {

		if (!showTOF()) {
			return false;
		}

		switch (superlayer) {
		case 1:
			if (layer == 1) {
				return showTOF_SUPLAY1_LAY1();
			}
			break;
		case 2:
			switch (layer) {
			case 1:
				return showTOF_SUPLAY2_LAY1();
			case 2:
				return showTOF_SUPLAY2_LAY2();
			case 3:
				return showTOF_SUPLAY2_LAY3();
			case 4:
				return showTOF_SUPLAY2_LAY4();
			case 5:
				return showTOF_SUPLAY2_LAY5();
			case 6:
				return showTOF_SUPLAY2_LAY6();
			case 7:
				return showTOF_SUPLAY2_LAY7();
			case 8:
				return showTOF_SUPLAY2_LAY8();
			case 9:
				return showTOF_SUPLAY2_LAY9();
			case 10:
				return showTOF_SUPLAY2_LAY10();
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid superlayer or layer.");
		}
		return false;
	}

	/**
	 * Show DC_Suplay1_Lay1?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay2_Lay1
	 */
	public boolean showDC_SUPLAY1_LAY11() {
		return show(PlainPanel3D.DC_SUPLAY1_LAY1);
	}

	/**
	 * Show DC_Suplay2_Lay1?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay2_Lay1
	 */
	public boolean showDC_SUPLAY2_LAY1() {
		return show(PlainPanel3D.DC_SUPLAY2_LAY1);
	}

	/**
	 * Show DC_Suplay2_Lay2?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay2_Lay2
	 */
	public boolean showDC_SUPLAY2_LAY2() {
		return show(PlainPanel3D.DC_SUPLAY2_LAY2);
	}

	/**
	 * Show DC_Suplay3_Lay1?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay3_Lay1
	 */
	public boolean showDC_SUPLAY3_LAY1() {
		return show(PlainPanel3D.DC_SUPLAY3_LAY1);
	}

	/**
	 * Show DC_Suplay3_Lay2?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay3_Lay2
	 */
	public boolean showDC_SUPLAY3_LAY2() {
		return show(PlainPanel3D.DC_SUPLAY3_LAY2);
	}

	/**
	 * Show DC_Suplay4_Lay1?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay4_Lay1
	 */
	public boolean showDC_SUPLAY4_LAY1() {
		return show(PlainPanel3D.DC_SUPLAY4_LAY1);
	}

	/**
	 * Show DC_Suplay4_Lay2?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay4_Lay2
	 */
	public boolean showDC_SUPLAY4_LAY2() {
		return show(PlainPanel3D.DC_SUPLAY4_LAY2);
	}

	/**
	 * Show DC_Suplay5_Lay1?
	 *
	 * @return <code>true</code> if we are to show DC_Suplay5_Lay1
	 */
	public boolean showDC_SUPLAY5_LAY1() {
		return show(PlainPanel3D.DC_SUPLAY5_LAY1);
	}


	// Generalized method to call appropriate show methods
	/**
	 * Show DC for the given superlayer and layer.
	 *
	 * @param superlayer Superlayer number
	 * @param layer      Layer number
	 * @return <code>true</code> if the appropriate layer is shown
	 */
	public boolean showDC(int superlayer, int layer) {

		if (!showDC()) {
			return false;
		}

		switch (superlayer) {
		case 1:
			if (layer == 1) {
				return showDC_SUPLAY1_LAY11();
			}
			break;
		case 2:
			switch (layer) {
			case 1:
				return showDC_SUPLAY2_LAY1();
			case 2:
				return showDC_SUPLAY2_LAY2();
			}
			break;
		case 3:
			switch (layer) {
			case 1:
				return showDC_SUPLAY3_LAY1();
			case 2:
				return showDC_SUPLAY3_LAY2();
			}
			break;
		case 4:
			switch (layer) {
			case 1:
				return showDC_SUPLAY4_LAY1();
			case 2:
				return showDC_SUPLAY4_LAY2();
			}
			break;
		case 5:
			switch (layer) {
			case 1:
				return showDC_SUPLAY5_LAY1();
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid superlayer or layer.");
		}
		return false;
	}

	/**
	 * Show MC truth?
	 *
	 * @return <code>true</code> if we are to show simulation truth
	 */
	public boolean showMCTruth() {
		return show(PlainPanel3D.SHOW_TRUTH);
	}

	/**
	 * Show Volumes?
	 *
	 * @return <code>true</code> if we are to show volumes
	 */
	public boolean showVolumes() {
		return show(PlainPanel3D.SHOW_VOLUMES);
	}

	/**
	 * This gets the z step used by the mouse and key adapters, to see how fast we
	 * move in or in in response to mouse wheel or up/down arrows. It should be
	 * overridden to give something sensible. like the scale/100;
	 *
	 * @return the z step (changes to zDist) for moving in and out
	 */
	@Override
	public float getZStep() {
		return (zmax - zmin) / 50f;
	}
}
