package cnuphys.advisors.graphics;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cnuphys.bCNU.util.Fonts;

public class AdvisorInfoLabel extends JPanel {

	private JLabel _label;

	//singleton
	private static AdvisorInfoLabel _instance;

	private AdvisorInfoLabel() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
		setBackground(Color.black);


		_label = new JLabel();
		_label.setBackground(Color.black);
		_label.setForeground(Color.cyan);

		add(_label);
		setFont(Fonts.mediumBoldFont);
		clear();
	}

	/**
	 * Access to the AdvisorDisplay singleton
	 * @return the AdvisorDisplay singleton
	 */
	public static AdvisorInfoLabel getInstance() {
		if (_instance == null) {
			_instance =new AdvisorInfoLabel();
		}
		return _instance;
	}

	public void clear() {
		_label.setText("Advisor Assigner");
	}

	public void setText(String text) {
		_label.setText(text);
	}

}
