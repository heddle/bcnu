package cnuphys.ced.cedview.sectorview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import cnuphys.bCNU.format.DoubleFormat;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.view.FBData;
import cnuphys.ced.alldata.datacontainer.cal.ECalClusterData;
import cnuphys.ced.alldata.datacontainer.cal.ECalReconData;
import cnuphys.ced.alldata.datacontainer.cal.PCalClusterData;
import cnuphys.ced.alldata.datacontainer.cal.PCalReconData;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.event.data.AIDC;
import cnuphys.ced.event.data.DC;
import cnuphys.ced.event.data.DCCluster;
import cnuphys.ced.event.data.DCReconHit;
import cnuphys.ced.event.data.DCTdcHit;
import cnuphys.ced.event.data.DataDrawSupport;
import cnuphys.ced.event.data.lists.ClusterList;
import cnuphys.ced.event.data.lists.DCClusterList;
import cnuphys.ced.event.data.lists.DCReconHitList;
import cnuphys.ced.event.data.lists.DCTdcHitList;
import cnuphys.ced.frame.CedColors;
import cnuphys.ced.geometry.ECGeometry;

public class ReconDrawer extends SectorViewDrawer {

	// cached for feedback
	private ArrayList<FBData> _fbData = new ArrayList<>();

    //data containers
	ECalReconData ecRecData = ECalReconData.getInstance();
	PCalReconData pcalRecData = PCalReconData.getInstance();


	/**
	 * Reconstructed hits drawer
	 *
	 * @param view
	 */
	public ReconDrawer(SectorView view) {
		super(view);
	}

	@Override
	public void draw(Graphics g, IContainer container) {

		_fbData.clear();


		if (_eventManager.isAccumulating() || !_view.isSingleEventMode()) {
			return;
		}

		// DC HB and TB Hits
		drawDCReconAndDOCA(g, container);

		// neural net overlays
		if (_view.showNN()) {
			drawNeuralNetOverlays(g, container);
		}


		// Reconstructed clusters
		if (_view.showClusters()) {
			drawClusters(g, container);
		}

		if (_view.showDCHBSegments()) {
			for (int supl = 1; supl <= 6; supl++) {
				_view.getSuperLayerDrawer(0, supl).drawHitBasedSegments(g, container);
			}
		}

		if (_view.showDCTBSegments()) {
			for (int supl = 1; supl <= 6; supl++) {
				_view.getSuperLayerDrawer(0, supl).drawTimeBasedSegments(g, container);
			}
		}

		if (_view.showAIDCHBSegments()) {
			for (int supl = 1; supl <= 6; supl++) {
				_view.getSuperLayerDrawer(0, supl).drawAIHitBasedSegments(g, container);
			}
		}

		if (_view.showAIDCTBSegments()) {
			for (int supl = 1; supl <= 6; supl++) {
				_view.getSuperLayerDrawer(0, supl).drawAITimeBasedSegments(g, container);
			}
		}

		if (_view.showRecCal()) {
			drawRecCal(g, container);
		}

	}

	// draw data from the REC::Calorimeter bank
	private void drawRecCal(Graphics g, IContainer container) {



		Point pp = new Point();
		Point2D.Double wp = new Point2D.Double();
		Rectangle2D.Double wr = new Rectangle2D.Double();

		//draw ECAL
		for (int i = 0; i < ecRecData.count(); i++) {
			if (_view.containsSector(ecRecData.sector.get(i))) {

				float x = ecRecData.x.get(i);
				float y = ecRecData.y.get(i);
				float z = ecRecData.z.get(i);

                _view.projectClasToWorld(x, y, z, _view.getProjectionPlane(), wp);
                container.worldToLocal(pp, wp);
                DataDrawSupport.drawECALRec(g, pp, false);

                double r = Math.sqrt(x*x + y*y + z*z);
                double theta = Math.toDegrees(Math.acos(z/r));
                double phi = Math.toDegrees(Math.atan2(y, x));

                float radius = ecRecData.getRadius(ecRecData.energy.get(i));
                if (radius > 0) {
					container.localToWorld(pp, wp);
					wr.setRect(wp.x - radius, wp.y - radius, 2 * radius, 2 * radius);
					WorldGraphicsUtilities.drawWorldOval(g, container, wr, CedColors.RECCalFill, null);
                }

				_fbData.add(new FBData(pp,
						String.format("$magenta$REC xyz (%-6.3f, %-6.3f, %-6.3f) cm", x, y, z),
						String.format("$magenta$REC %s (%-6.3f, %-6.3f, %-6.3f)", CedView.rThetaPhi, r, theta, phi),
						String.format("$magenta$REC plane %s", ECGeometry.PLANE_NAMES[ecRecData.plane.get(i)]),
						String.format("$magenta$REC view %s", ECGeometry.VIEW_NAMES[ecRecData.view.get(i)]),
						String.format("$magenta$%s", ecRecData.getPIDStr(i)),
						String.format("$magenta$REC Energy %-7.4f GeV", ecRecData.energy.get(i))));

			}
		} //for i

		//draw PCAL
		for (int i = 0; i < pcalRecData.count(); i++) {
			if (_view.containsSector(pcalRecData.sector.get(i))) {

				float x = pcalRecData.x.get(i);
				float y = pcalRecData.y.get(i);
				float z = pcalRecData.z.get(i);

                _view.projectClasToWorld(x, y, z, _view.getProjectionPlane(), wp);
                container.worldToLocal(pp, wp);
                DataDrawSupport.drawECALRec(g, pp, false);

                double r = Math.sqrt(x*x + y*y + z*z);
                double theta = Math.toDegrees(Math.acos(z/r));
                double phi = Math.toDegrees(Math.atan2(y, x));

                float radius = pcalRecData.getRadius(pcalRecData.energy.get(i));
                if (radius > 0) {
					container.localToWorld(pp, wp);
					wr.setRect(wp.x - radius, wp.y - radius, 2 * radius, 2 * radius);
					WorldGraphicsUtilities.drawWorldOval(g, container, wr, CedColors.RECCalFill, null);
                }

				_fbData.add(new FBData(pp,
						String.format("$magenta$REC xyz (%-6.3f, %-6.3f, %-6.3f) cm", x, y, z),
						String.format("$magenta$REC %s (%-6.3f, %-6.3f, %-6.3f)", CedView.rThetaPhi, r, theta, phi),
						String.format("$magenta$REC view %s", ECGeometry.VIEW_NAMES[pcalRecData.view.get(i)]),
						String.format("$magenta$%s", pcalRecData.getPIDStr(i)),
						String.format("$magenta$REC Energy %-7.4f GeV", pcalRecData.energy.get(i))));

			}
		} //for i


	}

	// draw neural net overlays
	private void drawNeuralNetOverlays(Graphics g, IContainer container) {
		DCTdcHitList hits = DC.getInstance().getTDCHits();
		if ((hits != null) && !hits.isEmpty()) {

			for (DCTdcHit hit : hits) {
				if (hit.nnHit) {
					if (_view.containsSector(hit.sector)) {
						_view.drawDCRawHit(g, container, CedColors.NN_TRANS, Color.black, hit);				}
				}
			}
		}
	}




	// draw reconstructed clusters
	private void drawClusters(Graphics g, IContainer container) {
		drawCalClusters(g, container);
	}

	//draw calorimeter clusters
	private void drawCalClusters(Graphics g, IContainer container) {

		Point2D.Double wp = new Point2D.Double();
		Point pp = new Point();

		//ECal
		ECalClusterData ecClusterData = ECalClusterData.getInstance();
		for (int i = 0; i < ecClusterData.count(); i++) {
			if (_view.containsSector(ecClusterData.sector.get(i))) {

				_view.projectClasToWorld(ecClusterData.x.get(i), ecClusterData.y.get(i), ecClusterData.z.get(i),
						_view.getProjectionPlane(), wp);
				container.worldToLocal(pp, wp);
				ecClusterData.setLocation(i, pp);
				DataDrawSupport.drawCluster(g, pp);
			}
		} // for i


        //PCal
		PCalClusterData pcalClusterData = PCalClusterData.getInstance();
		for (int i = 0; i < pcalClusterData.count(); i++) {
			if (_view.containsSector(pcalClusterData.sector.get(i))) {

				_view.projectClasToWorld(pcalClusterData.x.get(i), pcalClusterData.y.get(i), pcalClusterData.z.get(i),
						_view.getProjectionPlane(), wp);
				container.worldToLocal(pp, wp);
				pcalClusterData.setLocation(i, pp);
				DataDrawSupport.drawCluster(g, pp);
			}
		} // for i
	}

	// draw reconstructed DC hit Hit based and time based based hits
	private void drawDCReconAndDOCA(Graphics g, IContainer container) {
		if (_view.showDCHBHits()) {
			drawDCHitList(g, container, CedColors.HB_COLOR, DC.getInstance().getHBHits(), false);
		}
		if (_view.showDCTBHits()) {
			drawDCHitList(g, container, CedColors.TB_COLOR, DC.getInstance().getTBHits(), true);
		}
		if (_view.showAIDCHBHits()) {
			drawDCHitList(g, container, CedColors.AIHB_COLOR, AIDC.getInstance().getAIHBHits(), false);
		}
		if (_view.showAIDCTBHits()) {
			drawDCHitList(g, container, CedColors.AITB_COLOR, AIDC.getInstance().getAITBHits(), true);
		}
	}

	//feedback for clusters
	private boolean clusterListFeedback(String prefix, ClusterList clusters, Point screenPoint,
			List<String> feedbackStrings) {

		if (clusters != null) {

			if ((clusters.sector == null) || (clusters.sector.length == 0)) {
				return false;
			}

			for (int index = 0; index < clusters.length; index++) {

				if (_view.containsSector(clusters.sector[index])) {
					if (clusters.contains(index, screenPoint)) {
						clusters.getFeedbackStrings(prefix, index, feedbackStrings);
						return true;
					}
				}
			}
		}
		return false;
	}

	//feedback for calorimeter clusters
	private boolean CalClusterFeedback(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {
		if (_view.showClusters()) {

			//ECal
			ECalClusterData ecClusterData = ECalClusterData.getInstance();
			for (int i = 0; i < ecClusterData.count(); i++) {
				if (_view.containsSector(ecClusterData.sector.get(i))) {
					if (ecClusterData.contains(i, screenPoint)) {

						float x = ecClusterData.x.get(i);
						float y = ecClusterData.y.get(i);
						float z = ecClusterData.z.get(i);

						feedbackStrings
								.add(String.format("$magenta$EC cluster xyz (%-6.3f, %-6.3f, %-6.3f) cm", x, y, z));
						feedbackStrings.add(String.format("$magenta$EC cluster plane %s",
								ECGeometry.PLANE_NAMES[ecClusterData.plane.get(i)]));
						feedbackStrings.add(String.format("$magenta$EC cluster view %s",
								ECGeometry.VIEW_NAMES[ecClusterData.view.get(i)]));
						feedbackStrings.add(
								String.format("$magenta$EC cluster Energy %-7.4f GeV", ecClusterData.energy.get(i)));

						return true;
					}
				}
			} // for i

			// PCal
			PCalClusterData pcalClusterData = PCalClusterData.getInstance();
			for (int i = 0; i < pcalClusterData.count(); i++) {
				if (_view.containsSector(pcalClusterData.sector.get(i))) {
					if (pcalClusterData.contains(i, screenPoint)) {

						float x = pcalClusterData.x.get(i);
						float y = pcalClusterData.y.get(i);
						float z = pcalClusterData.z.get(i);

						feedbackStrings
								.add(String.format("$magenta$PCAL cluster xyz (%-6.3f, %-6.3f, %-6.3f) cm", x, y, z));
						feedbackStrings.add(String.format("$magenta$PCAL cluster view %s",
								ECGeometry.VIEW_NAMES[pcalClusterData.view.get(i)]));
						feedbackStrings.add(String.format("$magenta$PCAL cluster Energy %-6.3f GeV",
								pcalClusterData.energy.get(i)));

						return true;
					}
				}
			} // for i

		}

		return false;
	}

	/**
	 * Use what was drawn to generate feedback strings
	 *
	 * @param container       the drawing container
	 * @param screenPoint     the mouse location
	 * @param worldPoint      the corresponding world location
	 * @param feedbackStrings add strings to this collection
	 * @param option          0 for hit based, 1 for time based
	 */
	@Override
	public void vdrawFeedback(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings, int option) {

		if (CalClusterFeedback(container, screenPoint, worldPoint, feedbackStrings)) {
			return;
		}


		if (_view.showReconHits()) {
		}


		//data from REC::Calorimeter
		if (_view.showRecCal()) {
			for (FBData fbdata : _fbData) {
				boolean added = fbdata.addFeedback(screenPoint, feedbackStrings);
				if (added) {
					break;
				}
			}
		}

		// DC HB Recon Hits
		if (_view.showDCHBHits()) {
			DCReconHitList hits = DC.getInstance().getHBHits();
			DCClusterList clusters = DC.getInstance().getHBClusters();

			if ((hits != null) && !hits.isEmpty()) {
				for (DCReconHit hit : hits) {
					if (_view.containsSector(hit.sector)) {
						if (hit.contains(screenPoint)) {
							hit.getFeedbackStrings("HB", feedbackStrings);


							//possibly have cluster info
							short clusterId = hit.clusterID;

							if (clusterId > 0) {
								DCCluster cluster = clusters.fromClusterId(clusterId);

								String str1;
								if (cluster == null) {
									str1 = String.format("$red$" + "HB clusterID %d", clusterId);
								} else {
									str1 = String.format("$red$" + "HB clusterID %d size %d", clusterId, cluster.size);
								}
								feedbackStrings.add(str1);
							}

							return;
						}
					}
				}
			}
		}  //show hb hits

		// DC TB Recon Hits
		if (_view.showDCTBHits()) {
			DCReconHitList hits = DC.getInstance().getTBHits();
			DCClusterList clusters = DC.getInstance().getTBClusters();

			if ((hits != null) && !hits.isEmpty()) {
				for (DCReconHit hit : hits) {
					if (_view.containsSector(hit.sector)) {
						if (hit.contains(screenPoint)) {
							hit.getFeedbackStrings("TB", feedbackStrings);


							//possibly have cluster info
							short clusterId = hit.clusterID;

							if (clusterId > 0) {
								DCCluster cluster = clusters.fromClusterId(clusterId);

								String str1;
								if (cluster == null) {
									str1 = String.format("$red$" + "TB clusterID %d", clusterId);
								} else {
									str1 = String.format("$red$" + "TB clusterID %d size %d", clusterId, cluster.size);
								}
								feedbackStrings.add(str1);
							}

							return;
						}
					}
				}
			}
		}  //show tb hits



		// AI DC HB Recon Hits
		if (_view.showAIDCHBHits()) {
			DCReconHitList hits = AIDC.getInstance().getAIHBHits();
			DCClusterList clusters = AIDC.getInstance().getAIHBClusters();

			if ((hits != null) && !hits.isEmpty()) {
				for (DCReconHit hit : hits) {
					if (_view.containsSector(hit.sector)) {
						if (hit.contains(screenPoint)) {
							hit.getFeedbackStrings("AI HB", feedbackStrings);


							//possibly have cluster info
							short clusterId = hit.clusterID;

							if (clusterId > 0) {
								DCCluster cluster = clusters.fromClusterId(clusterId);

								String str1;
								if (cluster == null) {
									str1 = String.format("$red$" + "HB clusterID %d", clusterId);
								} else {
									str1 = String.format("$red$" + "HB clusterID %d size %d", clusterId, cluster.size);
								}
								feedbackStrings.add(str1);
							}

							return;
						}
					}
				}
			}
		}  //show AI hb hits

		// AI DC TB Recon Hits
		if (_view.showAIDCTBHits()) {
			DCReconHitList hits = AIDC.getInstance().getAITBHits();
			DCClusterList clusters = AIDC.getInstance().getAITBClusters();

			if ((hits != null) && !hits.isEmpty()) {
				for (DCReconHit hit : hits) {
					if (_view.containsSector(hit.sector)) {
						if (hit.contains(screenPoint)) {
							hit.getFeedbackStrings("AI TB", feedbackStrings);


							//possibly have cluster info
							short clusterId = hit.clusterID;

							if (clusterId > 0) {
								DCCluster cluster = clusters.fromClusterId(clusterId);

								String str1;
								if (cluster == null) {
									str1 = String.format("$red$" + "TB clusterID %d", clusterId);
								} else {
									str1 = String.format("$red$" + "TB clusterID %d size %d", clusterId, cluster.size);
								}
								feedbackStrings.add(str1);
							}

							return;
						}
					}
				}
			}
		}  //show AI tb hits


	}

	// for writing out a vector
	private String vecStr(String prompt, double vx, double vy, double vz) {
		return vecStr(prompt, vx, vy, vz, 2);
	}

	// for writing out a vector
	private String vecStr(String prompt, double vx, double vy, double vz, int ndig) {
		return prompt + " (" + DoubleFormat.doubleFormat(vx, ndig) + ", " + DoubleFormat.doubleFormat(vy, ndig) + ", "
				+ DoubleFormat.doubleFormat(vz, ndig) + ")";
	}

	// draw a reconstructed cluster list
	private void drawClusterList(Graphics g, IContainer container, ClusterList clusters) {
		if ((clusters == null) || (clusters.length < 1) || (clusters.sector == null) || (clusters.sector.length == 0)) {
			return;
		}


		Point2D.Double wp = new Point2D.Double();
		Point pp = new Point();

		for (int index = 0; index < clusters.length; index++) {
			if (_view.containsSector(clusters.sector[index])) {
				_view.projectClasToWorld(clusters.x[index], clusters.y[index], clusters.z[index], _view.getProjectionPlane(), wp);
				container.worldToLocal(pp, wp);
				clusters.setLocation(index, pp);
				DataDrawSupport.drawCluster(g, pp);
			}
		}
	}

	// draw a reconstructed hit list
	private void drawDCHitList(Graphics g, IContainer container, Color fillColor, DCReconHitList hits, boolean isTimeBased) {
		if ((hits == null) || hits.isEmpty()) {
			return;
		}

		for (DCReconHit hit : hits) {
			if (_view.containsSector(hit.sector)) {
				_view.drawDCReconHit(g, container, fillColor, Color.black, hit, isTimeBased);
			}
		}

	}

}
