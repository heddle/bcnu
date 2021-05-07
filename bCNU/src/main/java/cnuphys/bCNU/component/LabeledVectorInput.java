package cnuphys.bCNU.component;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledVectorInput extends JPanel {
	
	private JTextField _textField[];
	private JLabel _label;

	
	public LabeledVectorInput(String label, int dim, String units, int numcol, Font font, int labelWidth) {
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
		_label = new JLabel(label);
		add(_label);
		_label.setFont(font);
		
		_textField = new JTextField[dim];
		for (int i = 0; i < dim; i++) {
			_textField[i] = new JTextField(numcol);
			add(_textField[i]);
			_textField[i].setFont(font);
		}
		
		
		if (labelWidth > 0) {
			Dimension d = _label.getPreferredSize();
			d.width = labelWidth;
			_label.setPreferredSize(d);
		}
		
	}
	

}
