package cnuphys.ced.cedview.central;

import java.awt.Color;
import java.awt.Graphics2D;

import cnuphys.bCNU.graphics.colorscale.ColorScaleModel;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.geometry.BSTxyPanel;
import cnuphys.ced.geometry.bmt.BMTSectorItem;

public interface ICentralXYView {

	/**
	 * Get a CTOF scintillator polygon
	 *
	 * @param index1 the 1=based index [1..48]
	 * @return the most recently drawn polygon
	 */
	public CTOFXYPolygon getCTOFPolygon(int index1);
	
	/**
	 * Get the CND polygon from Gagik's geometry layer and paddle
	 *
	 * @param layer    1..3
	 * @param paddleId 1..48
	 * @return the CND polygon
	 */
	public CNDXYPolygon getCNDPolygon(int layer, int paddleId);
	
	/**
	 * Get the CND polygon from "real" numbering
	 *
	 * @param sector    1..24
	 * @param layer     1..3
	 * @param component 1..2
	 * @return the CND polygon
	 */
	public CNDXYPolygon getCNDPolygon(int sector, int layer, int component);
	
	
	// draw one BST panel
	public void drawBSTPanel(Graphics2D g2, IContainer container, BSTxyPanel panel, Color color);
	
	/**
	 * Get the BMT Sector item
	 *
	 * @param sector the geo sector 1..3
	 * @param layer  the layer 1..6
	 * @return the BMS Sector Item
	 */
	public BMTSectorItem getBMTSectorItem(int sector, int layer);
}
