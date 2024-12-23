package cnuphys.advisors.dialogs;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JPanel;

import cnuphys.advisors.graphics.SizedText;
import cnuphys.bCNU.dialog.SimpleDialog;

public class MessageDialog extends SimpleDialog {

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
	@Override
	protected Component createCenterComponent() {
		//size the the text area

		String message = (String)userObject1;
		Font font = (Font)userObject2;

		JPanel panel = new JPanel() {
			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}

		};

		panel.add(new SizedText(message, font, WIDTH));
		return panel;
	}

}
