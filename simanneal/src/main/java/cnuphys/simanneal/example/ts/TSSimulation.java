package cnuphys.simanneal.example.ts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import cnuphys.bCNU.attributes.Attribute;
import cnuphys.bCNU.util.Fonts;
import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.Solution;

public class TSSimulation extends Simulation {

	// custom attributes
	public static final String NUMCITY = "num city";
	public static final String RIVER = "river penalty";

	private TSSolution _tsSolution;

	// for river penalty
	private JSlider _riverSlider;

	@Override
	public Solution setInitialSolution() {
		_tsSolution = new TSSolution(this);
		return _tsSolution;
	}

	@Override
	protected void setInitialAttributes() {

		// change some defaults
		_attributes.setPlotTitle("Traveling Salesperson");
		_attributes.setYAxisLabel("Distance");
		_attributes.setXAxisLabel("Log(Temp)");
		_attributes.setUseLogTemp(true);

		// custom
		_attributes.add(NUMCITY, 200);

		_riverSlider = new JSlider(-5, 5, 0);

		_riverSlider.setMajorTickSpacing((_riverSlider.getMaximum() - _riverSlider.getMinimum()) / 2);
		// _riverSlider.setPaintTicks(true);
		_riverSlider.setPaintLabels(true);
//		_riverSlider.setBorder(
//                BorderFactory.createEmptyBorder(2, 2, 2, 2));
		_riverSlider.setFont(Fonts.tinyFont);
		Dimension d = _riverSlider.getPreferredSize();
		d.width = 150;
		_riverSlider.setPreferredSize(d);
		_attributes.add(new Attribute(RIVER, _riverSlider));

	}

	/**
	 * Get the penalty (or bonus) for crossing the river
	 *
	 * @return the penalty for crossing the river
	 */
	public double getRiverPenalty() {
		return (0.5) * _riverSlider.getValue();
	}

	// main program for testing
	public static void main(String arg[]) {

		final JFrame frame = new JFrame();

		// set up what to do if the window is closed
		WindowAdapter windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(1);
			}
		};

		frame.addWindowListener(windowAdapter);

		frame.setLayout(new BorderLayout());

		TSSimulation simulation = new TSSimulation();

		TSPanel tsPanel = new TSPanel(simulation);

		frame.add(tsPanel, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.pack();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
	}

}
