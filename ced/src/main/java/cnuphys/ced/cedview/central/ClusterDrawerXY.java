package cnuphys.ced.cedview.central;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D.Double;
import java.util.List;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public class ClusterDrawerXY extends CentralXYViewDrawer {
	
	private static final Stroke THICKLINE = new BasicStroke(1.5f);

	// cached rectangles for feedback
	private Rectangle _bstFBRects[];

	// cached rectangles for feedback
	private Rectangle _bmtFBRects[];

	
	public ClusterDrawerXY(CentralXYView view) {
		super(view);
	}


	@Override
	public void draw(Graphics g, IContainer container) {

		if (ClasIoEventManager.getInstance().isAccumulating() || !_view.isSingleEventMode()) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		Shape oldClip = g2.getClip();
		// clip the active area
		Rectangle sr = container.getInsetRectangle();
		g2.clipRect(sr.x, sr.y, sr.width, sr.height);
		_bstFBRects = null;
		_bmtFBRects = null;

		Stroke oldStroke = g2.getStroke();
		g2.setStroke(THICKLINE);

		drawBSTClusters(g, container);
		drawBMTClusters(g, container);

		g2.setStroke(oldStroke);

		g2.setClip(oldClip);
	}
	
	/**
	 * Draw BST clusters
	 *
	 * @param g         the graphics context
	 * @param container the drawing container
	 */
	public void drawBSTClusters(Graphics g, IContainer container) {
		
		DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
		if (event == null) {
			return;
		}

		byte sector[] = ColumnData.getByteArray("BSTRec::Clusters.sector");

		int count = (sector == null) ? 0 : sector.length;
		if (count == 0) {
			return;
		}
		
		float x1[] = ColumnData.getFloatArray("BSTRec::Clusters.x1");
		if (x1 != null) {
			float y1[] = ColumnData.getFloatArray("BSTRec::Clusters.y1");
			float x2[] = ColumnData.getFloatArray("BSTRec::Clusters.x2");
			float y2[] = ColumnData.getFloatArray("BSTRec::Clusters.y2");

			Point p1 = new Point();
			Point p2 = new Point();

			for (int i = 0; i < count; i++) {
				container.worldToLocal(p1, 10 * x1[i], 10 * y1[i]);
				container.worldToLocal(p2, 10 * x2[i], 10 * y2[i]);
				g.setColor(Color.black);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				DataDrawSupport.drawReconCluster(g, p1);
				DataDrawSupport.drawReconCluster(g, p2);
			}
		}

	}
	
	/**
	 * Draw BMT clusters
	 *
	 * @param g         the graphics context
	 * @param container the drawing container
	 */
	public void drawBMTClusters(Graphics g, IContainer container) {
		DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
		if (event == null) {
			return;
		}

		byte sector[] = ColumnData.getByteArray("BMTRec::Clusters.sector");

		int count = (sector == null) ? 0 : sector.length;
		if (count == 0) {
			return;
		}

		float x1[] = ColumnData.getFloatArray("BMTRec::Clusters.x1");

		if (x1 != null) {
			float y1[] = ColumnData.getFloatArray("BMTRec::Clusters.y1");
			float x2[] = ColumnData.getFloatArray("BMTRec::Clusters.x2");
			float y2[] = ColumnData.getFloatArray("BMTRec::Clusters.y2");

			Point p1 = new Point();
			Point p2 = new Point();

			for (int i = 0; i < count; i++) {
				container.worldToLocal(p1, 10 * x1[i], 10 * y1[i]);
				container.worldToLocal(p2, 10 * x2[i], 10 * y2[i]);
				g.setColor(Color.black);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				DataDrawSupport.drawReconCluster(g, p1);
				DataDrawSupport.drawReconCluster(g, p2);
			}
		}

	}


	@Override
	public void feedback(IContainer container, Point screenPoint, Double worldPoint, List<String> feedbackStrings) {
		
	}

}
