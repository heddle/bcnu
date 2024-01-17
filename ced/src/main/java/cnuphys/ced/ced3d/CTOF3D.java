package cnuphys.ced.ced3d;

import java.awt.Color;

import com.jogamp.opengl.GLAutoDrawable;

import cnuphys.ced.event.data.arrays.LR_ADCArrays;
import cnuphys.lund.X11Colors;

public class CTOF3D extends DetectorItem3D {

	// child layer items
	private CTOFPaddle3D _paddles[];

	/**
	 * The 3D CND
	 *
	 * @param panel3d the 3D panel owner
	 */
	public CTOF3D(CedPanel3D panel3D) {
		super(panel3D);

		_paddles = new CTOFPaddle3D[48];
		for (int paddleId = 1; paddleId <= 48; paddleId++) {
			_paddles[paddleId - 1] = new CTOFPaddle3D(paddleId);
		}

	}

	@Override
	public void drawShape(GLAutoDrawable drawable) {
		
		Color color = X11Colors.getX11Color("Dodger blue", getVolumeAlpha());

		for (int paddleId = 1; paddleId <= 48; paddleId++) {
			_paddles[paddleId - 1].drawPaddle(drawable, color);
		}

		
		//draw based on ADC data
		LR_ADCArrays arrays = new LR_ADCArrays("CTOF::adc");
		if (arrays.hasData()) {
			for (int index = 0; index < arrays.sector.length; index++) {
				short paddleId = arrays.component[index];
				Color fc = arrays.getColor((byte)1, (byte)1, paddleId);
				_paddles[paddleId-1].drawPaddle(drawable, fc);
			}

		}
	}

	@Override
	public void drawData(GLAutoDrawable drawable) {
		// Children handle drawing

	}

	@Override
	protected boolean show() {
		return _cedPanel3D.showCTOF();
	}

}
