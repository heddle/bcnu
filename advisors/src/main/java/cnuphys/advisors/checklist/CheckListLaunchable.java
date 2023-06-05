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
import javax.swing.border.EmptyBorder;

import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.graphics.AdvisorDisplay;
import cnuphys.advisors.threading.ILaunchable;
import cnuphys.advisors.threading.ThreadManager;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;

public abstract class CheckListLaunchable extends JComponent implements ILaunchable {

	// is the step completed?
	public boolean done;

	// size of the led
	public static final Dimension ledSize = new Dimension(13, 13);

	private static final Color _doneColor = X11Colors.getX11Color("Dark Green");
	private static final Color _todoColor = Color.red;

	/** the run button */
	public JButton _doitButton;

	/** the red/green light */
	public JComponent _led;
	
	/** the name of the launchable */
	public String name;

	/**
	 * Create a component backing a step of the algorithm
	 * 
	 * @param info
	 * @param enabled
	 * @param done    already done?
	 */
	public CheckListLaunchable(String name, String info, boolean enabled) {
		this.name = name;
		setLayout(new FlowLayout(FlowLayout.LEFT, 4, 2));

		if (!done) {
			add(makeDoItButton());
		}
		add(led());
		add(makeLabel(info));

		setEnabled(enabled);
		setBorder(new EmptyBorder(2, 2, 0, 0));

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (_doitButton != null) {
			_doitButton.setEnabled(enabled);
		}
	}

	// make the button that launches the algorithm step
	private JButton makeDoItButton() {
		_doitButton = new JButton("Run");
		_doitButton.setFont(Fonts.tweenFont);
		
		final ILaunchable launchable = this;

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadManager.getInstance().queue(launchable);
			}

		};

		_doitButton.addActionListener(al);

		return _doitButton;
	}

	private JLabel makeLabel(String text) {
		JLabel lab = new JLabel(text);
		lab.setFont(Fonts.tweenFont);
		return lab;
	}

	// the led status
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

	@Override
	public void launchDone() {
		done = true;

		if (_doitButton != null) {
			remove(_doitButton);
			_doitButton = null;
		}

		repaint();
		AdvisorAssign.updateInfoLabel();
		AdvisorAssign.setBigText(name + ": done");
		CheckList.getInstance().checkState();

		AdvisorDisplay.getInstance().dataChange();
	}

}
