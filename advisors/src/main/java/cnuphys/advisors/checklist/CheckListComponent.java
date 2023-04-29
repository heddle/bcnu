package cnuphys.advisors.checklist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.graphics.AdvisorDisplay;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;

public class CheckListComponent extends JPanel {
	
	public static final Dimension ledSize = new Dimension(18, 18);
	
	//is the step completed?
	public boolean done;
	
	//the algorithm step
	private IAlgorithmStep _step;
	
	private static final Color _doneColor = X11Colors.getX11Color("Dark Green");
	private static final Color _todoColor = Color.red;
	
	//do it button
	public JButton _doitButton;
	
	//LED
	public JComponent _led;
	
	private static int count = 0;
	
	/**
	 * Create a component backing a step of the algorithm
	 * @param leftLab
	 * @param info
	 * @param step
	 * @param enabled
	 */
	public CheckListComponent(String info, IAlgorithmStep step, boolean enabled) {
		_step = step;
		setLayout(new FlowLayout(FlowLayout.LEFT, 4, 2));
		
		add(makeLabel("" + ++count));
		add(makeDoItButton());
		add(led());
		add(makeLabel(info));

		
		setEnabled(enabled);
		setBorder(new EmptyBorder(2, 2, 0, 0));
		
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		_doitButton.setEnabled(enabled);
	}
	
	// make the button that launches the algorithm step
	private JButton makeDoItButton() {
		_doitButton = new JButton("Do It");
		_doitButton.setFont(Fonts.defaultFont);
		
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_step != null) {
					done = _step.run();
					repaint();
					AdvisorAssign.updateInfoLabel();
					CheckList.getInstance().checkState();
					
					AdvisorDisplay.getInstance().dataChange();
				}
			}
			
		};
		
		_doitButton.addActionListener(al);
		
		return _doitButton;
	}
	
	private JLabel makeLabel(String text) {
		JLabel lab = new JLabel(text);
		lab.setFont(Fonts.defaultFont);
		return lab;
	}
	
	//the led status
	private JComponent led() {
		_led = new JComponent() {
			@Override
			public void paintComponent(Graphics g) {
				Rectangle b = getBounds();
				g.setColor(done ? _doneColor : _todoColor);
				
				g.fillRect(0, 0, b.width, b.height);
				GraphicsUtilities.drawSimple3DRect(g, 0, 0, b.width, b.height, done);
			}
			
			@Override
			public Dimension getPreferredSize() {
				return ledSize;
			}
		};
		
		_led.setSize(ledSize);
		
		return _led;
	}
}
