package cnuphys.ced.cedview.allpcal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jlab.geom.prim.Point3D;
import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.SymbolDraw;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.view.FBData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.RECCalorimeter;
import cnuphys.ced.frame.CedColors;
import cnuphys.ced.geometry.ECGeometry;
import cnuphys.ced.geometry.PCALGeometry;
import cnuphys.lund.LundId;

/**
 * Rec drawer for the AllPCALView
 * @author davidheddle
 *
 */
public class PCALRecDrawer extends PCALViewDrawer {

	// the event manager
	ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	//the current event
	private DataEvent _currentEvent;


	// cached for feedback
	private ArrayList<FBData> _fbData = new ArrayList<>();

	public PCALRecDrawer(PCALView view) {
		super(view);
	}

	//ignore drawing or feedback
	private boolean ignore() {
		if (!_view.showRecCal() || ClasIoEventManager.getInstance().isAccumulating() || !_view.isSingleEventMode()) {
			return true;
		}

		_currentEvent = _eventManager.getCurrentEvent();
		if ((_currentEvent == null) || !_currentEvent.hasBank("REC::Calorimeter")) {
			return true;
		}

		return false;
	}

	@Override
	public void draw(Graphics g, IContainer container) {
		_fbData.clear();

		if (ignore()) {
			return;
		}

		RECCalorimeter recCal = RECCalorimeter.getInstance();
		if (recCal.isEmpty()) {
			return;
		}

		for (int index = 0; index < recCal.count; index++) {
			drawRecCal(g, container, index, false);
		}


	}
	

	/**
	 * Draw a single RecCal "cluster"
	 * @param g
	 * @param container
	 * @param index
	 * @param highlight
	 */
	public void drawRecCal(Graphics g, IContainer container, int index, boolean highlight) {
		if (ignore()) {
			return;
		}

		RECCalorimeter recCal = RECCalorimeter.getInstance();
		if (recCal.isEmpty()) {
			return;
		}
		
		Point pp = new Point();
		Rectangle2D.Double wr = new Rectangle2D.Double();
		Point2D.Double wp = new Point2D.Double();

		if (recCal.layer[index] > 3) {  //is it ecal rather than pcal?
			return;
		}


		Point3D clasP = new Point3D(recCal.x[index], recCal.y[index], recCal.z[index]);
		Point3D localP = new Point3D();
		PCALGeometry.getTransformations().clasToLocal(localP, clasP);

		localP.setZ(0);

		// get the right item
		_view.getHexSectorItem(recCal.sector[index]).ijkToScreen(container, localP, pp);

		SymbolDraw.drawDavid(g, pp.x, pp.y, highlight ? 5 : 4, Color.black, highlight ? Color.yellow : Color.red);


		float radius = recCal.getRadius(recCal.energy[index]);
		if (radius > 0) {
			container.localToWorld(pp, wp);
			wr.setRect(wp.x - radius, wp.y - radius, 2 * radius, 2 * radius);

			LundId lid = recCal.getLundId(index);
			Color color = (lid == null) ? CedColors.RECEcalFill : lid.getStyle().getTransparentFillColor();


			WorldGraphicsUtilities.drawWorldOval(g, container, wr, color, null);
		}


		_fbData.add(new FBData(pp,
				String.format("$magenta$REC xyz (%-6.3f, %-6.3f, %-6.3f) cm", recCal.x[index], recCal.y[index],
						recCal.z[index]),
				String.format("$magenta$REC layer %d", recCal.layer[index]),
				String.format("$magenta$%s", recCal.getPIDStr(index)),
				String.format("$magenta$REC Energy %-7.4f GeV", recCal.energy[index])));

	}



	/**
	 * Use what was drawn to generate feedback strings
	 *
	 * @param container       the drawing container
	 * @param screenPoint     the mouse location
	 * @param worldPoint      the corresponding world location
	 * @param feedbackStrings add strings to this collection
	 */
	@Override
	public void feedback(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		if (ignore()) {
			return;
		}


		for (FBData fbdata : _fbData) {
			boolean added = fbdata.addFeedback(screenPoint, feedbackStrings);
			if (added) {
				break;
			}
		}

	}


}