package cnuphys.bCNU.wordle;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class ButtonRow extends JPanel {

	public ButtonRow(int width, char...labels) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
		
		for (char label : labels) {
			add(new Key(width, label));
		}
	}
}
