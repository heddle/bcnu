package cnuphys.ced.cedview.ftof;

import java.awt.Graphics;
import java.awt.Point;

import cnuphys.ced.event.data.DataDrawSupport;

public class FTOFHitHandler extends FTOFGenericHitHandler {
//FTOFGenericHitHandler(FTOFView view, String bankname, String fbcolor, String fbprefix)

	public FTOFHitHandler(FTOFView view) {
		super(view, "FTOF::hits", "$olive drab$", "recon hit");
	}

	@Override
	public boolean showTheseHits() {
		return _view.showReconHits();
	}

	@Override
	public void drawHit(Graphics g, Point pp) {
		DataDrawSupport.drawReconHit(g, pp);
	}


}
