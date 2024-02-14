package cnuphys.ced.ced3d;



import java.awt.Color;

import org.jlab.io.base.DataEvent;

import com.jogamp.opengl.GLAutoDrawable;

import bCNU3D.Support3D;
import cnuphys.ced.alldata.datacontainer.cal.ECalReconData;
import cnuphys.ced.alldata.datacontainer.cal.PCalReconData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.frame.CedColors;
import item3D.Item3D;

public class RecDrawer3D extends Item3D {

	// the event manager
	ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	//the current event
	private DataEvent _currentEvent;

	private static final float POINTSIZE = 5f;
	private CedPanel3D _cedPanel3D;

//data containers
	ECalReconData ecRecData = ECalReconData.getInstance();
	PCalReconData pcalRecData = PCalReconData.getInstance();


	public RecDrawer3D(CedPanel3D panel3D) {
		super(panel3D);
		_cedPanel3D = panel3D;
	}

	@Override
	public void draw(GLAutoDrawable drawable) {

		_currentEvent = _eventManager.getCurrentEvent();
		if (_currentEvent == null) {
			return;
		}

		if (_panel3D instanceof ForwardPanel3D) { // forward detectors

			//show any data from REC::Calorimiter?
			if (((ForwardPanel3D) _panel3D).showRecCal()) {
				showReconCalorimeter(drawable);
			}
		}
	}


	//show data from REC::Calorimeter
	private void showReconCalorimeter(GLAutoDrawable drawable) {

		if (_cedPanel3D.showECAL()) {
			for (int i = 0; i < ecRecData.count(); i++) {
				float x = ecRecData.x.get(i);
				float y = ecRecData.y.get(i);
				float z = ecRecData.z.get(i);
				Support3D.drawPoint(drawable, x, y, z, Color.black, POINTSIZE, true);
				float radius = ecRecData.getRadius(ecRecData.energy.get(i));
				if (radius > 0) {
					Support3D.solidSphere(drawable, x, y, z, radius, 40, 40, CedColors.RECCalFill);
				}
			} // end for
		}

		if (_cedPanel3D.showPCAL()) {
			for (int i = 0; i < pcalRecData.count(); i++) {
				float x = pcalRecData.x.get(i);
				float y = pcalRecData.y.get(i);
				float z = pcalRecData.z.get(i);
				Support3D.drawPoint(drawable, x, y, z, Color.black, POINTSIZE, true);
				float radius = pcalRecData.getRadius(pcalRecData.energy.get(i));
				if (radius > 0) {
					Support3D.solidSphere(drawable, x, y, z, radius, 40, 40, CedColors.RECCalFill);
				}
			} // end for
		}

	}

}
