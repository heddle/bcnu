package cnuphys.ced.ced3d;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cnuphys.ced.ced3d.view.PlainView3D;

public class SwimmerPanel3D extends PlainPanel3D {
	
	//the control panel
	private SwimmerControlPanel _controlPanel;


	private static final String _cbaLabels[] = { SHOW_VOLUMES, SHOW_SECTOR_1, SHOW_SECTOR_2, SHOW_SECTOR_3,
			SHOW_SECTOR_4, SHOW_SECTOR_5, SHOW_SECTOR_6, SHOW_DC, SHOW_ECAL, SHOW_PCAL, SHOW_FTOF, 
			SHOW_MAP_EXTENTS };

	public SwimmerPanel3D(PlainView3D view, float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist) {
		super(view, angleX, angleY, angleZ, xDist, yDist, zDist, 0.92f, 0.92f, 0.92f, _cbaLabels);
	}


	
	// add north panel
	@Override
	protected void addNorth() {
		JPanel np = new JPanel();
		np.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

		JLabel label = new JLabel("This is a stand-alone view independent of any physics events.");
		
		np.add(label);
		
		np.add(_volumeAlphaSlider, BorderLayout.EAST);
		add(np, BorderLayout.NORTH);
	}

	// add west panel
	protected void addWest() {
		_controlPanel = new SwimmerControlPanel();
		
		add(_controlPanel, BorderLayout.WEST);
	}

	// add south panel
	protected void addSouth() {
	}


}