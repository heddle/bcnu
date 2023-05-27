package cnuphys.advisors.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.JTextArea;

import cnuphys.advisors.util.Utilities;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.util.FileUtilities;

public class MessageDialog extends SimpleDialog {
	
	private JTextArea _textArea;
	
	private static final int WIDTH = 300;

	public MessageDialog(String title, boolean modal, String message, Font font) {
		super(title, message, font, modal, "Close");
	}
	
	/**
	 * Override to create the component that goes in the center. Usually this is the
	 * "main" component.
	 * 
	 * @return the component that is placed in the center
	 */
	protected Component createCenterComponent() {
		//size the the text area
		
		String message = (String)userObject1;
		Font font = (Font)userObject2;
		String tokens[] = FileUtilities.tokens(message);
		
		FontMetrics fm = this.getFontMetrics(font);
		
		String fullStr = "";
		String s = "\n  ";
		int lineCount = 0;
		
		for (String tok : tokens) {
			if (fm.stringWidth(s + tok) < WIDTH) {
				s += (tok + " ");
			}
			else {
				fullStr += (s + "\n");
				s = "  " + tok + " ";
				lineCount++;
			}
		}
		fullStr += s;
		
		final Dimension d = new Dimension(WIDTH+10, (lineCount+2)*(fm.getHeight()+1));
		
		_textArea = new JTextArea(fullStr) {
			
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};

		
		return _textArea;
	}

}
