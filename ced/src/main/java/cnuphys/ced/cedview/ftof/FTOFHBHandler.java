package cnuphys.ced.cedview.ftof;

import java.awt.Graphics;
import java.awt.Point;

import cnuphys.ced.alldata.DataDrawSupport;

public class FTOFHBHandler extends FTOFGenericHitHandler {
//FTOFGenericHitHandler(FTOFView view, String bankname, String fbcolor, String fbprefix)

	public FTOFHBHandler(FTOFView view) {
		super(view, "FTOF::hbhits", "$yellow$", "hb hit");
	}

	@Override
	public boolean showTheseHits() {
		return _view.showHB();
	}

	@Override
	public void drawHit(Graphics g, Point pp) {
		DataDrawSupport.drawHBHit(g, pp);
	}


}
