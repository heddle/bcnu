package cnuphys.ced.ced3d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import cnuphys.bCNU.component.LabeledTextField;
import cnuphys.bCNU.component.VariableRange;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.UnicodeSupport;

public class SwimmerControlPanel extends JPanel implements ActionListener {
	
	private Font _font = Fonts.defaultFont;
	
	//the ranges
	private VariableRange _pRange;
	private VariableRange _xoRange;
	private VariableRange _yoRange;
	private VariableRange _zoRange;
	private VariableRange _thetaRange;
	private VariableRange _phiRange;
	
	//number to swim
	private LabeledTextField _swimCount;
	private int _lastGoodSwimCount = 100;
	
	//the swim button
	private JButton _swimButton;
	
	//clear trajectories button
	private JButton _clearButton;
	
	//for the measured string to get them to align
	private String _measureString = "XYZ from";
	
	private static final int MINWIDTH = 300;

	public SwimmerControlPanel() {
		setLayout(new BorderLayout(6, 8));
		setBorder(new CommonBorder("Swimmer Test Controls"));
		addNorth();
		addCenter();
		addSouth();
		
	}
	
	//add the north component
	private void addNorth() {
		JPanel np = new JPanel();
		np.setLayout(new BoxLayout(np, BoxLayout.PAGE_AXIS));
		
		//how many to swim
		_swimCount = new LabeledTextField("Number of swims:", 6, _font);
		_swimCount.setText("" + _lastGoodSwimCount);
		np.add(_swimCount);

		add(np, BorderLayout.NORTH);
	}

	//add the center component
	private void addCenter() {
		JPanel cp = new JPanel();
		cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
		
		//add the ranges
		addRanges(cp);
		
		add(cp, BorderLayout.CENTER);
	}
	
	//add the south component
	private void addSouth() {
		JPanel sp = new JPanel();
		sp.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
		
		_clearButton = createButton("Clear Trajectories", sp);
		_swimButton  = createButton("Swim Trajectories", sp);
		
		add(sp, BorderLayout.SOUTH);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();

		if (d.width < MINWIDTH) {
			d.width = MINWIDTH;
		}

		return d;
		
	}
	
	/**
	 * Get how many swims we will do
	 * @return how many swims
	 */
	public int getSwimCount() {
		
		int count;
		
		try {
			count = Integer.parseInt(_swimCount.getText());
		}
		catch (Exception e) {
			count = _lastGoodSwimCount;
			_swimCount.setText("" + count);
		}
		
		return count;
	}
	
	//add the ranges
	private void addRanges(JPanel panel) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		// add the ranges
		_pRange = createRange("p from", "GeV/c", p, 1, 10);
		_xoRange = createRange("x" + UnicodeSupport.SUBZERO + " from", "cm", p, 0, 0);
		_yoRange = createRange("y" + UnicodeSupport.SUBZERO + " from", "cm", p, 0, 0);
		_zoRange = createRange("z" + UnicodeSupport.SUBZERO + " from", "cm", p, 0, 0);
		_thetaRange = createRange(UnicodeSupport.SMALL_THETA + " from", "deg", p, 20, 50);
		_phiRange = createRange(UnicodeSupport.SMALL_PHI + " from", "deg", p, -180, 180);

		panel.add(p);
		
	}
	
	//create a variable range component
	private VariableRange createRange(String prompt, String units, JPanel p, double minVal, double maxVal) {
		
		VariableRange vr = new VariableRange(prompt, units, _measureString, _font, minVal, maxVal);
		p.add(vr);
		return vr;
		
	}
	
	//convenience method to create a button with "this" as the action listener
	private JButton createButton(String label, JPanel p) {
		JButton button = new JButton(label);
		button.setFont(_font);
		button.addActionListener(this);
		p.add(button);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == _clearButton) {
			handleClear();
		}
		else if (source == _swimButton) {
			handleSwim();
		}
		
	}
	
	//handle clear trajectories
	private void handleClear() {
		System.err.println("hit clear");
	}

	//handle swim trajectories
	private void handleSwim() {
		System.err.println("hit swim");
	}

}
